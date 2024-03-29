begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbpm
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbpm
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncCallback
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
name|Endpoint
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
name|ExchangePattern
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
name|jbpm
operator|.
name|emitters
operator|.
name|CamelEventEmitter
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
name|jbpm
operator|.
name|listeners
operator|.
name|CamelCaseEventListener
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
name|jbpm
operator|.
name|listeners
operator|.
name|CamelProcessEventListener
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
name|jbpm
operator|.
name|listeners
operator|.
name|CamelTaskEventListener
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
name|jbpm
operator|.
name|services
operator|.
name|api
operator|.
name|DeploymentEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jbpm
operator|.
name|services
operator|.
name|api
operator|.
name|DeploymentEventListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jbpm
operator|.
name|services
operator|.
name|api
operator|.
name|DeploymentService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jbpm
operator|.
name|services
operator|.
name|api
operator|.
name|ListenerSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jbpm
operator|.
name|services
operator|.
name|api
operator|.
name|model
operator|.
name|DeployedUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jbpm
operator|.
name|services
operator|.
name|api
operator|.
name|service
operator|.
name|ServiceRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kie
operator|.
name|internal
operator|.
name|runtime
operator|.
name|manager
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kie
operator|.
name|internal
operator|.
name|runtime
operator|.
name|manager
operator|.
name|InternalRuntimeManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|JBPMConsumer
specifier|public
class|class
name|JBPMConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|DeploymentEventListener
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JBPMConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|JBPMEndpoint
name|endpoint
decl_stmt|;
DECL|field|configuration
specifier|private
name|JBPMConfiguration
name|configuration
decl_stmt|;
DECL|method|JBPMConsumer (Endpoint endpoint, Processor processor)
specifier|public
name|JBPMConsumer
parameter_list|(
name|Endpoint
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
name|endpoint
operator|=
operator|(
name|JBPMEndpoint
operator|)
name|endpoint
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
operator|(
operator|(
name|JBPMEndpoint
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
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
name|DeploymentService
name|deploymentService
init|=
operator|(
name|DeploymentService
operator|)
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|service
argument_list|(
name|ServiceRegistry
operator|.
name|DEPLOYMENT_SERVICE
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getDeploymentId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|InternalRuntimeManager
name|manager
init|=
operator|(
name|InternalRuntimeManager
operator|)
name|deploymentService
operator|.
name|getRuntimeManager
argument_list|(
name|configuration
operator|.
name|getDeploymentId
argument_list|()
argument_list|)
decl_stmt|;
name|configure
argument_list|(
name|manager
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"JBPM Camel Consumer configured and started for deployment id {}"
argument_list|,
name|configuration
operator|.
name|getDeploymentId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
operator|(
operator|(
name|ListenerSupport
operator|)
name|deploymentService
operator|)
operator|.
name|addListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
for|for
control|(
name|DeployedUnit
name|deployed
range|:
name|deploymentService
operator|.
name|getDeployedUnits
argument_list|()
control|)
block|{
name|InternalRuntimeManager
name|manager
init|=
operator|(
name|InternalRuntimeManager
operator|)
name|deployed
operator|.
name|getRuntimeManager
argument_list|()
decl_stmt|;
name|configure
argument_list|(
name|manager
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"JBPM Camel Consumer configured and started on all available deployments"
argument_list|)
expr_stmt|;
block|}
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
name|DeploymentService
name|deploymentService
init|=
operator|(
name|DeploymentService
operator|)
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|service
argument_list|(
name|ServiceRegistry
operator|.
name|DEPLOYMENT_SERVICE
argument_list|)
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getDeploymentId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"JBPM Camel Consumer unconfigured and stopped for deployment id {}"
argument_list|,
name|configuration
operator|.
name|getDeploymentId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
operator|(
operator|(
name|ListenerSupport
operator|)
name|deploymentService
operator|)
operator|.
name|removeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"JBPM Camel Consumer unconfigured and stopped on all available deployments"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|JBPMConstants
operator|.
name|JBPM_EVENT_EMITTER
operator|.
name|equals
argument_list|(
name|configuration
operator|.
name|getEventListenerType
argument_list|()
argument_list|)
condition|)
block|{
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|remove
argument_list|(
literal|"CamelEventEmitter"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|sendMessage (String eventType, Object body)
specifier|public
name|void
name|sendMessage
parameter_list|(
name|String
name|eventType
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"EventType"
argument_list|,
name|eventType
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isSynchronous
argument_list|()
condition|)
block|{
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// handle any thrown exception
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|getProcessor
argument_list|()
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
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// handle any thrown exception
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|onDeploy (DeploymentEvent event)
specifier|public
name|void
name|onDeploy
parameter_list|(
name|DeploymentEvent
name|event
parameter_list|)
block|{
name|InternalRuntimeManager
name|manager
init|=
operator|(
name|InternalRuntimeManager
operator|)
name|event
operator|.
name|getDeployedUnit
argument_list|()
operator|.
name|getRuntimeManager
argument_list|()
decl_stmt|;
name|configure
argument_list|(
name|manager
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onUnDeploy (DeploymentEvent event)
specifier|public
name|void
name|onUnDeploy
parameter_list|(
name|DeploymentEvent
name|event
parameter_list|)
block|{
comment|// no-op
block|}
annotation|@
name|Override
DECL|method|onActivate (DeploymentEvent event)
specifier|public
name|void
name|onActivate
parameter_list|(
name|DeploymentEvent
name|event
parameter_list|)
block|{
comment|// no-op
block|}
annotation|@
name|Override
DECL|method|onDeactivate (DeploymentEvent event)
specifier|public
name|void
name|onDeactivate
parameter_list|(
name|DeploymentEvent
name|event
parameter_list|)
block|{
comment|// no-op
block|}
DECL|method|configure (InternalRuntimeManager manager, JBPMConsumer consumer)
specifier|protected
name|void
name|configure
parameter_list|(
name|InternalRuntimeManager
name|manager
parameter_list|,
name|JBPMConsumer
name|consumer
parameter_list|)
block|{
name|String
name|eventListenerType
init|=
name|configuration
operator|.
name|getEventListenerType
argument_list|()
decl_stmt|;
if|if
condition|(
name|eventListenerType
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|configureConsumer
argument_list|(
name|eventListenerType
argument_list|,
name|manager
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|configureConsumer (String eventListenerType, InternalRuntimeManager manager, JBPMConsumer consumer)
specifier|protected
name|void
name|configureConsumer
parameter_list|(
name|String
name|eventListenerType
parameter_list|,
name|InternalRuntimeManager
name|manager
parameter_list|,
name|JBPMConsumer
name|consumer
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Configuring Camel JBPM Consumer for {} on runtime manager {}"
argument_list|,
name|eventListenerType
argument_list|,
name|manager
argument_list|)
expr_stmt|;
name|CacheManager
name|cacheManager
init|=
name|manager
operator|.
name|getCacheManager
argument_list|()
decl_stmt|;
name|JBPMCamelConsumerAware
name|consumerAware
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|JBPMConstants
operator|.
name|JBPM_PROCESS_EVENT_LISTENER
operator|.
name|equals
argument_list|(
name|eventListenerType
argument_list|)
condition|)
block|{
name|consumerAware
operator|=
operator|(
name|JBPMCamelConsumerAware
operator|)
name|cacheManager
operator|.
name|get
argument_list|(
literal|"new org.apache.camel.component.jbpm.listeners.CamelProcessEventListener()"
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumerAware
operator|==
literal|null
condition|)
block|{
name|consumerAware
operator|=
operator|new
name|CamelProcessEventListener
argument_list|()
expr_stmt|;
name|cacheManager
operator|.
name|add
argument_list|(
literal|"new org.apache.camel.component.jbpm.listeners.CamelProcessEventListener()"
argument_list|,
name|consumerAware
argument_list|)
expr_stmt|;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Configuring JBPMConsumer on process event listener {}"
argument_list|,
name|consumerAware
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|JBPMConstants
operator|.
name|JBPM_TASK_EVENT_LISTENER
operator|.
name|equals
argument_list|(
name|eventListenerType
argument_list|)
condition|)
block|{
name|consumerAware
operator|=
operator|(
name|JBPMCamelConsumerAware
operator|)
name|cacheManager
operator|.
name|get
argument_list|(
literal|"new org.apache.camel.component.jbpm.listeners.CamelTaskEventListener()"
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumerAware
operator|==
literal|null
condition|)
block|{
name|consumerAware
operator|=
operator|new
name|CamelTaskEventListener
argument_list|()
expr_stmt|;
name|cacheManager
operator|.
name|add
argument_list|(
literal|"new org.apache.camel.component.jbpm.listeners.CamelTaskEventListener()"
argument_list|,
name|consumerAware
argument_list|)
expr_stmt|;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Configuring JBPMConsumer on task event listener {}"
argument_list|,
name|consumerAware
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|JBPMConstants
operator|.
name|JBPM_CASE_EVENT_LISTENER
operator|.
name|equals
argument_list|(
name|eventListenerType
argument_list|)
condition|)
block|{
name|consumerAware
operator|=
operator|(
name|JBPMCamelConsumerAware
operator|)
name|cacheManager
operator|.
name|get
argument_list|(
literal|"new org.apache.camel.component.jbpm.listeners.CamelCaseEventListener()"
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumerAware
operator|==
literal|null
condition|)
block|{
name|consumerAware
operator|=
operator|new
name|CamelCaseEventListener
argument_list|()
expr_stmt|;
name|cacheManager
operator|.
name|add
argument_list|(
literal|"new org.apache.camel.component.jbpm.listeners.CamelCaseEventListener()"
argument_list|,
name|consumerAware
argument_list|)
expr_stmt|;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Configuring JBPMConsumer on case event listener {}"
argument_list|,
name|consumerAware
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|JBPMConstants
operator|.
name|JBPM_EVENT_EMITTER
operator|.
name|equals
argument_list|(
name|eventListenerType
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Configuring JBPMConsumer for event emitter"
argument_list|)
expr_stmt|;
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|register
argument_list|(
literal|"CamelEventEmitter"
argument_list|,
operator|new
name|CamelEventEmitter
argument_list|(
name|this
argument_list|,
name|configuration
operator|.
name|getEmitterSendItems
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Adding consumer {} on {}"
argument_list|,
name|consumer
argument_list|,
name|consumerAware
argument_list|)
expr_stmt|;
name|consumerAware
operator|.
name|addConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"JBPMConsumer [endpoint="
operator|+
name|endpoint
operator|+
literal|", configuration="
operator|+
name|configuration
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

