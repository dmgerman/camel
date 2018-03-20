begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|amazonaws
operator|.
name|ClientConfiguration
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|AWSCredentials
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|AWSCredentialsProvider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|AWSStaticCredentialsProvider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|BasicAWSCredentials
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|regions
operator|.
name|Regions
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
name|AmazonSimpleWorkflow
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
name|AmazonSimpleWorkflowClient
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
name|AmazonSimpleWorkflowClientBuilder
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
name|StartWorkflowOptions
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
name|Consumer
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
name|Producer
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
name|impl
operator|.
name|DefaultEndpoint
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|EndpointHelper
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
name|ExchangeHelper
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

begin_comment
comment|/**  * The aws-swf component is used for managing workflows from Amazon Simple Workflow.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.13.0"
argument_list|,
name|scheme
operator|=
literal|"aws-swf"
argument_list|,
name|title
operator|=
literal|"AWS Simple Workflow"
argument_list|,
name|syntax
operator|=
literal|"aws-swf:type"
argument_list|,
name|consumerClass
operator|=
name|SWFWorkflowConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"cloud,workflow"
argument_list|)
DECL|class|SWFEndpoint
specifier|public
class|class
name|SWFEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|amazonSWClient
specifier|private
name|AmazonSimpleWorkflowClient
name|amazonSWClient
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|SWFConfiguration
name|configuration
decl_stmt|;
DECL|method|SWFEndpoint ()
specifier|public
name|SWFEndpoint
parameter_list|()
block|{     }
DECL|method|SWFEndpoint (String uri, SWFComponent component, SWFConfiguration configuration)
specifier|public
name|SWFEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|SWFComponent
name|component
parameter_list|,
name|SWFConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|isWorkflow
argument_list|()
condition|?
operator|new
name|SWFWorkflowProducer
argument_list|(
name|this
argument_list|,
operator|new
name|CamelSWFWorkflowClient
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
argument_list|)
else|:
operator|new
name|SWFActivityProducer
argument_list|(
name|this
argument_list|,
operator|new
name|CamelSWFActivityClient
argument_list|(
name|configuration
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|Consumer
name|consumer
init|=
name|isWorkflow
argument_list|()
condition|?
operator|new
name|SWFWorkflowConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|configuration
argument_list|)
else|:
operator|new
name|SWFActivityConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
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
if|if
condition|(
name|configuration
operator|.
name|getAmazonSWClient
argument_list|()
operator|==
literal|null
condition|)
block|{
name|amazonSWClient
operator|=
operator|(
name|AmazonSimpleWorkflowClient
operator|)
name|createSWClient
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getAmazonSWClient
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|amazonSWClient
operator|!=
literal|null
condition|)
block|{
name|amazonSWClient
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|amazonSWClient
operator|=
literal|null
expr_stmt|;
block|}
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getSWClient ()
specifier|public
name|AmazonSimpleWorkflowClient
name|getSWClient
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getAmazonSWClient
argument_list|()
operator|!=
literal|null
condition|?
name|configuration
operator|.
name|getAmazonSWClient
argument_list|()
else|:
name|amazonSWClient
return|;
block|}
DECL|method|createSWClient ()
specifier|private
name|AmazonSimpleWorkflow
name|createSWClient
parameter_list|()
throws|throws
name|Exception
block|{
name|AWSCredentials
name|credentials
init|=
operator|new
name|BasicAWSCredentials
argument_list|(
name|configuration
operator|.
name|getAccessKey
argument_list|()
argument_list|,
name|configuration
operator|.
name|getSecretKey
argument_list|()
argument_list|)
decl_stmt|;
name|AWSCredentialsProvider
name|credentialsProvider
init|=
operator|new
name|AWSStaticCredentialsProvider
argument_list|(
name|credentials
argument_list|)
decl_stmt|;
name|ClientConfiguration
name|clientConfiguration
init|=
operator|new
name|ClientConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|configuration
operator|.
name|getClientConfigurationParameters
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setProperties
argument_list|(
name|clientConfiguration
argument_list|,
name|configuration
operator|.
name|getClientConfigurationParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|AmazonSimpleWorkflowClientBuilder
name|builder
init|=
name|AmazonSimpleWorkflowClientBuilder
operator|.
name|standard
argument_list|()
operator|.
name|withClientConfiguration
argument_list|(
name|clientConfiguration
argument_list|)
operator|.
name|withCredentials
argument_list|(
name|credentialsProvider
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getRegion
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|withRegion
argument_list|(
name|Regions
operator|.
name|valueOf
argument_list|(
name|configuration
operator|.
name|getRegion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|AmazonSimpleWorkflow
name|client
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|configuration
operator|.
name|getSWClientParameters
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setProperties
argument_list|(
name|client
argument_list|,
name|configuration
operator|.
name|getSWClientParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
DECL|method|getStartWorkflowOptions ()
specifier|public
name|StartWorkflowOptions
name|getStartWorkflowOptions
parameter_list|()
block|{
name|StartWorkflowOptions
name|startWorkflowOptions
init|=
operator|new
name|StartWorkflowOptions
argument_list|()
decl_stmt|;
try|try
block|{
name|EndpointHelper
operator|.
name|setReferenceProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|startWorkflowOptions
argument_list|,
name|configuration
operator|.
name|getStartWorkflowOptionsParameters
argument_list|()
argument_list|)
expr_stmt|;
name|EndpointHelper
operator|.
name|setProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|startWorkflowOptions
argument_list|,
name|configuration
operator|.
name|getStartWorkflowOptionsParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|startWorkflowOptions
return|;
block|}
DECL|method|isWorkflow ()
specifier|private
name|boolean
name|isWorkflow
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getType
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"workflow"
argument_list|)
return|;
block|}
DECL|method|createExchange (Object request, String action)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Object
name|request
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SWFConstants
operator|.
name|ACTION
argument_list|,
name|action
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|getResult (Exchange exchange)
specifier|public
name|Object
name|getResult
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|?
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
else|:
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
DECL|method|setResult (Exchange exchange, Object result)
specifier|public
name|void
name|setResult
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|result
parameter_list|)
block|{
if|if
condition|(
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setConfiguration (SWFConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|SWFConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|SWFConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
block|}
end_class

end_unit

