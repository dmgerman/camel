begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.swf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|swf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|worker
operator|.
name|GenericActivityWorker
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
name|URISupport
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
DECL|class|SWFActivityConsumer
specifier|public
class|class
name|SWFActivityConsumer
extends|extends
name|DefaultConsumer
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
name|SWFWorkflowProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|SWFEndpoint
name|endpoint
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|SWFConfiguration
name|configuration
decl_stmt|;
DECL|field|genericWorker
specifier|private
name|GenericActivityWorker
name|genericWorker
decl_stmt|;
DECL|field|swfActivityConsumerToString
specifier|private
specifier|transient
name|String
name|swfActivityConsumerToString
decl_stmt|;
DECL|method|SWFActivityConsumer (SWFEndpoint endpoint, Processor processor, SWFConfiguration configuration)
specifier|public
name|SWFActivityConsumer
parameter_list|(
name|SWFEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|SWFConfiguration
name|configuration
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
name|endpoint
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|processActivity (Object[] inputParameters, String taskToken)
specifier|public
name|Object
name|processActivity
parameter_list|(
name|Object
index|[]
name|inputParameters
parameter_list|,
name|String
name|taskToken
parameter_list|)
throws|throws
name|Exception
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Processing activity task: "
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|inputParameters
argument_list|)
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|inputParameters
argument_list|,
name|SWFConstants
operator|.
name|EXECUTE_ACTION
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SWFConstants
operator|.
name|TASK_TOKEN
argument_list|,
name|taskToken
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
name|endpoint
operator|.
name|getResult
argument_list|(
name|exchange
argument_list|)
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
name|CamelActivityImplementationFactory
name|factory
init|=
operator|new
name|CamelActivityImplementationFactory
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|genericWorker
operator|=
operator|new
name|GenericActivityWorker
argument_list|(
name|endpoint
operator|.
name|getSWClient
argument_list|()
argument_list|,
name|configuration
operator|.
name|getDomainName
argument_list|()
argument_list|,
name|configuration
operator|.
name|getActivityList
argument_list|()
argument_list|)
expr_stmt|;
name|genericWorker
operator|.
name|setActivityImplementationFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|genericWorker
operator|.
name|setTaskExecutorThreadPoolSize
argument_list|(
name|configuration
operator|.
name|getActivityThreadPoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|genericWorker
operator|.
name|start
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
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
name|genericWorker
operator|.
name|setDisableServiceShutdownOnStop
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|genericWorker
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
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
if|if
condition|(
name|swfActivityConsumerToString
operator|==
literal|null
condition|)
block|{
name|swfActivityConsumerToString
operator|=
literal|"SWFActivityConsumer["
operator|+
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|+
literal|"]"
expr_stmt|;
block|}
return|return
name|swfActivityConsumerToString
return|;
block|}
block|}
end_class

end_unit

