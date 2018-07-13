begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.mq
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
name|mq
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|AmazonServiceException
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
name|mq
operator|.
name|AmazonMQ
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
name|mq
operator|.
name|model
operator|.
name|ConfigurationId
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
name|mq
operator|.
name|model
operator|.
name|CreateBrokerRequest
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
name|mq
operator|.
name|model
operator|.
name|CreateBrokerResult
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
name|mq
operator|.
name|model
operator|.
name|DeleteBrokerRequest
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
name|mq
operator|.
name|model
operator|.
name|DeleteBrokerResult
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
name|mq
operator|.
name|model
operator|.
name|ListBrokersRequest
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
name|mq
operator|.
name|model
operator|.
name|ListBrokersResult
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
name|mq
operator|.
name|model
operator|.
name|RebootBrokerRequest
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
name|mq
operator|.
name|model
operator|.
name|RebootBrokerResult
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
name|mq
operator|.
name|model
operator|.
name|UpdateBrokerRequest
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
name|mq
operator|.
name|model
operator|.
name|UpdateBrokerResult
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
name|Message
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
name|DefaultProducer
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

begin_import
import|import static
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
name|common
operator|.
name|AwsExchangeUtil
operator|.
name|getMessageForResponse
import|;
end_import

begin_comment
comment|/**  * A Producer which sends messages to the Amazon MQ Service  *<a href="http://aws.amazon.com/mq/">AWS MQ</a>  */
end_comment

begin_class
DECL|class|MQProducer
specifier|public
class|class
name|MQProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MQProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|mqProducerToString
specifier|private
specifier|transient
name|String
name|mqProducerToString
decl_stmt|;
DECL|method|MQProducer (Endpoint endpoint)
specifier|public
name|MQProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
switch|switch
condition|(
name|determineOperation
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
case|case
name|listBrokers
case|:
name|listBrokers
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getAmazonMqClient
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|createBroker
case|:
name|createBroker
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getAmazonMqClient
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|deleteBroker
case|:
name|deleteBroker
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getAmazonMqClient
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|rebootBroker
case|:
name|rebootBroker
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getAmazonMqClient
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|updateBroker
case|:
name|updateBroker
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getAmazonMqClient
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported operation"
argument_list|)
throw|;
block|}
block|}
DECL|method|determineOperation (Exchange exchange)
specifier|private
name|MQOperations
name|determineOperation
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|MQOperations
name|operation
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MQConstants
operator|.
name|OPERATION
argument_list|,
name|MQOperations
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|operation
operator|==
literal|null
condition|)
block|{
name|operation
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getOperation
argument_list|()
expr_stmt|;
block|}
return|return
name|operation
return|;
block|}
DECL|method|getConfiguration ()
specifier|protected
name|MQConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
return|;
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
name|mqProducerToString
operator|==
literal|null
condition|)
block|{
name|mqProducerToString
operator|=
literal|"MQProducer["
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
name|mqProducerToString
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|MQEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|MQEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|listBrokers (AmazonMQ mqClient, Exchange exchange)
specifier|private
name|void
name|listBrokers
parameter_list|(
name|AmazonMQ
name|mqClient
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|ListBrokersRequest
name|request
init|=
operator|new
name|ListBrokersRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MQConstants
operator|.
name|MAX_RESULTS
argument_list|)
argument_list|)
condition|)
block|{
name|int
name|maxResults
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MQConstants
operator|.
name|MAX_RESULTS
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|request
operator|.
name|withMaxResults
argument_list|(
name|maxResults
argument_list|)
expr_stmt|;
block|}
name|ListBrokersResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|mqClient
operator|.
name|listBrokers
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmazonServiceException
name|ase
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"List Brokers command returned the error code {}"
argument_list|,
name|ase
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ase
throw|;
block|}
name|Message
name|message
init|=
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|createBroker (AmazonMQ mqClient, Exchange exchange)
specifier|private
name|void
name|createBroker
parameter_list|(
name|AmazonMQ
name|mqClient
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|brokerName
decl_stmt|;
name|String
name|deploymentMode
decl_stmt|;
name|CreateBrokerRequest
name|request
init|=
operator|new
name|CreateBrokerRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MQConstants
operator|.
name|BROKER_NAME
argument_list|)
argument_list|)
condition|)
block|{
name|brokerName
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MQConstants
operator|.
name|BROKER_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withBrokerName
argument_list|(
name|brokerName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Broker Name must be specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MQConstants
operator|.
name|BROKER_DEPLOYMENT_MODE
argument_list|)
argument_list|)
condition|)
block|{
name|deploymentMode
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MQConstants
operator|.
name|BROKER_DEPLOYMENT_MODE
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withDeploymentMode
argument_list|(
name|deploymentMode
argument_list|)
expr_stmt|;
block|}
name|CreateBrokerResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|mqClient
operator|.
name|createBroker
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmazonServiceException
name|ase
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Create Broker command returned the error code {}"
argument_list|,
name|ase
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ase
throw|;
block|}
name|Message
name|message
init|=
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteBroker (AmazonMQ mqClient, Exchange exchange)
specifier|private
name|void
name|deleteBroker
parameter_list|(
name|AmazonMQ
name|mqClient
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|brokerId
decl_stmt|;
name|DeleteBrokerRequest
name|request
init|=
operator|new
name|DeleteBrokerRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MQConstants
operator|.
name|BROKER_ID
argument_list|)
argument_list|)
condition|)
block|{
name|brokerId
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MQConstants
operator|.
name|BROKER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withBrokerId
argument_list|(
name|brokerId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Broker Name must be specified"
argument_list|)
throw|;
block|}
name|DeleteBrokerResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|mqClient
operator|.
name|deleteBroker
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmazonServiceException
name|ase
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Delete Broker command returned the error code {}"
argument_list|,
name|ase
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ase
throw|;
block|}
name|Message
name|message
init|=
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|rebootBroker (AmazonMQ mqClient, Exchange exchange)
specifier|private
name|void
name|rebootBroker
parameter_list|(
name|AmazonMQ
name|mqClient
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|brokerId
decl_stmt|;
name|RebootBrokerRequest
name|request
init|=
operator|new
name|RebootBrokerRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MQConstants
operator|.
name|BROKER_ID
argument_list|)
argument_list|)
condition|)
block|{
name|brokerId
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MQConstants
operator|.
name|BROKER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withBrokerId
argument_list|(
name|brokerId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Broker Name must be specified"
argument_list|)
throw|;
block|}
name|RebootBrokerResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|mqClient
operator|.
name|rebootBroker
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmazonServiceException
name|ase
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Reboot Broker command returned the error code {}"
argument_list|,
name|ase
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ase
throw|;
block|}
name|Message
name|message
init|=
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|updateBroker (AmazonMQ mqClient, Exchange exchange)
specifier|private
name|void
name|updateBroker
parameter_list|(
name|AmazonMQ
name|mqClient
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|brokerId
decl_stmt|;
name|ConfigurationId
name|configurationId
decl_stmt|;
name|UpdateBrokerRequest
name|request
init|=
operator|new
name|UpdateBrokerRequest
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MQConstants
operator|.
name|BROKER_ID
argument_list|)
argument_list|)
condition|)
block|{
name|brokerId
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MQConstants
operator|.
name|BROKER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withBrokerId
argument_list|(
name|brokerId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Broker Name must be specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MQConstants
operator|.
name|CONFIGURATION_ID
argument_list|)
argument_list|)
condition|)
block|{
name|configurationId
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MQConstants
operator|.
name|CONFIGURATION_ID
argument_list|,
name|ConfigurationId
operator|.
name|class
argument_list|)
expr_stmt|;
name|request
operator|.
name|withConfiguration
argument_list|(
name|configurationId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Broker Name must be specified"
argument_list|)
throw|;
block|}
name|UpdateBrokerResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|mqClient
operator|.
name|updateBroker
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AmazonServiceException
name|ase
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Update Broker command returned the error code {}"
argument_list|,
name|ase
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ase
throw|;
block|}
name|Message
name|message
init|=
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

