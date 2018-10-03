begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.hpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kubernetes
operator|.
name|hpa
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|api
operator|.
name|model
operator|.
name|DoneableHorizontalPodAutoscaler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|api
operator|.
name|model
operator|.
name|HorizontalPodAutoscaler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|api
operator|.
name|model
operator|.
name|HorizontalPodAutoscalerList
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|KubernetesClientException
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|Watch
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|Watcher
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|dsl
operator|.
name|MixedOperation
import|;
end_import

begin_import
import|import
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|dsl
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kubernetes
operator|.
name|AbstractKubernetesEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kubernetes
operator|.
name|KubernetesConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kubernetes
operator|.
name|consumer
operator|.
name|common
operator|.
name|HPAEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|DefaultConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|KubernetesHPAConsumer
specifier|public
class|class
name|KubernetesHPAConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|executor
specifier|private
name|ExecutorService
name|executor
decl_stmt|;
DECL|field|hpasWatcher
specifier|private
name|HpaConsumerTask
name|hpasWatcher
decl_stmt|;
DECL|method|KubernetesHPAConsumer (AbstractKubernetesEndpoint endpoint, Processor processor)
specifier|public
name|KubernetesHPAConsumer
parameter_list|(
name|AbstractKubernetesEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|AbstractKubernetesEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|AbstractKubernetesEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|executor
operator|=
name|getEndpoint
argument_list|()
operator|.
name|createExecutor
argument_list|()
expr_stmt|;
name|hpasWatcher
operator|=
operator|new
name|HpaConsumerTask
argument_list|()
expr_stmt|;
name|executor
operator|.
name|submit
argument_list|(
name|hpasWatcher
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Stopping Kubernetes HPA Consumer"
argument_list|)
expr_stmt|;
if|if
condition|(
name|executor
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|!=
literal|null
operator|&&
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|hpasWatcher
operator|!=
literal|null
condition|)
block|{
name|hpasWatcher
operator|.
name|getWatch
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|hpasWatcher
operator|!=
literal|null
condition|)
block|{
name|hpasWatcher
operator|.
name|getWatch
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
block|}
name|executor
operator|=
literal|null
expr_stmt|;
block|}
DECL|class|HpaConsumerTask
class|class
name|HpaConsumerTask
implements|implements
name|Runnable
block|{
DECL|field|watch
specifier|private
name|Watch
name|watch
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|MixedOperation
argument_list|<
name|HorizontalPodAutoscaler
argument_list|,
name|HorizontalPodAutoscalerList
argument_list|,
name|DoneableHorizontalPodAutoscaler
argument_list|,
name|Resource
argument_list|<
name|HorizontalPodAutoscaler
argument_list|,
name|DoneableHorizontalPodAutoscaler
argument_list|>
argument_list|>
name|w
init|=
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesClient
argument_list|()
operator|.
name|autoscaling
argument_list|()
operator|.
name|horizontalPodAutoscalers
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesConfiguration
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|)
condition|)
block|{
name|w
operator|.
name|inNamespace
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesConfiguration
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesConfiguration
argument_list|()
operator|.
name|getLabelKey
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesConfiguration
argument_list|()
operator|.
name|getLabelValue
argument_list|()
argument_list|)
condition|)
block|{
name|w
operator|.
name|withLabel
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesConfiguration
argument_list|()
operator|.
name|getLabelKey
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesConfiguration
argument_list|()
operator|.
name|getLabelValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesConfiguration
argument_list|()
operator|.
name|getResourceName
argument_list|()
argument_list|)
condition|)
block|{
name|w
operator|.
name|withName
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getKubernetesConfiguration
argument_list|()
operator|.
name|getResourceName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|watch
operator|=
name|w
operator|.
name|watch
argument_list|(
operator|new
name|Watcher
argument_list|<
name|HorizontalPodAutoscaler
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|eventReceived
parameter_list|(
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|Watcher
operator|.
name|Action
name|action
parameter_list|,
name|HorizontalPodAutoscaler
name|resource
parameter_list|)
block|{
name|HPAEvent
name|hpae
init|=
operator|new
name|HPAEvent
argument_list|(
name|action
argument_list|,
name|resource
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|hpae
operator|.
name|getHpa
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KubernetesConstants
operator|.
name|KUBERNETES_EVENT_ACTION
argument_list|,
name|hpae
operator|.
name|getAction
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KubernetesConstants
operator|.
name|KUBERNETES_EVENT_TIMESTAMP
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error during processing"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onClose
parameter_list|(
name|KubernetesClientException
name|cause
parameter_list|)
block|{
if|if
condition|(
name|cause
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|getWatch ()
specifier|public
name|Watch
name|getWatch
parameter_list|()
block|{
return|return
name|watch
return|;
block|}
DECL|method|setWatch (Watch watch)
specifier|public
name|void
name|setWatch
parameter_list|(
name|Watch
name|watch
parameter_list|)
block|{
name|this
operator|.
name|watch
operator|=
name|watch
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

