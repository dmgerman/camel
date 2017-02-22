begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.azure.queue
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|azure
operator|.
name|queue
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|microsoft
operator|.
name|azure
operator|.
name|storage
operator|.
name|queue
operator|.
name|CloudQueue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|microsoft
operator|.
name|azure
operator|.
name|storage
operator|.
name|queue
operator|.
name|CloudQueueMessage
import|;
end_import

begin_import
import|import
name|com
operator|.
name|microsoft
operator|.
name|azure
operator|.
name|storage
operator|.
name|queue
operator|.
name|MessageUpdateFields
import|;
end_import

begin_import
import|import
name|com
operator|.
name|microsoft
operator|.
name|azure
operator|.
name|storage
operator|.
name|queue
operator|.
name|QueueListingDetails
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
name|component
operator|.
name|azure
operator|.
name|blob
operator|.
name|BlobServiceConstants
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
name|azure
operator|.
name|common
operator|.
name|ExchangeUtil
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

begin_comment
comment|/**  * A Producer which sends messages to the Azure Storage Queue Service  */
end_comment

begin_class
DECL|class|QueueServiceProducer
specifier|public
class|class
name|QueueServiceProducer
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
name|QueueServiceProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|QueueServiceProducer (final Endpoint endpoint)
specifier|public
name|QueueServiceProducer
parameter_list|(
specifier|final
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
annotation|@
name|Override
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|QueueServiceOperations
name|operation
init|=
name|determineOperation
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|operation
operator|=
name|QueueServiceOperations
operator|.
name|listQueues
expr_stmt|;
block|}
else|else
block|{
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|retrieveMessage
case|:
name|retrieveMessage
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|peekMessage
case|:
name|peekMessage
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|createQueue
case|:
name|createQueue
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|deleteQueue
case|:
name|deleteQueue
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|addMessage
case|:
name|addMessage
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|updateMessage
case|:
name|updateMessage
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|deleteMessage
case|:
name|deleteMessage
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|listQueues
case|:
name|listQueues
argument_list|(
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
block|}
DECL|method|listQueues (Exchange exchange)
specifier|private
name|void
name|listQueues
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|CloudQueue
name|client
init|=
name|QueueServiceUtil
operator|.
name|createQueueClient
argument_list|(
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|QueueServiceRequestOptions
name|opts
init|=
name|QueueServiceUtil
operator|.
name|getRequestOptions
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|QueueListingDetails
name|details
init|=
operator|(
name|QueueListingDetails
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|QueueServiceConstants
operator|.
name|QUEUE_LISTING_DETAILS
argument_list|)
decl_stmt|;
if|if
condition|(
name|details
operator|==
literal|null
condition|)
block|{
name|details
operator|=
name|QueueListingDetails
operator|.
name|ALL
expr_stmt|;
block|}
name|Iterable
argument_list|<
name|CloudQueue
argument_list|>
name|list
init|=
name|client
operator|.
name|getServiceClient
argument_list|()
operator|.
name|listQueues
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getQueuePrefix
argument_list|()
argument_list|,
name|details
argument_list|,
name|opts
operator|.
name|getRequestOpts
argument_list|()
argument_list|,
name|opts
operator|.
name|getOpContext
argument_list|()
argument_list|)
decl_stmt|;
name|ExchangeUtil
operator|.
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
operator|.
name|setBody
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
DECL|method|createQueue (Exchange exchange)
specifier|private
name|void
name|createQueue
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|CloudQueue
name|client
init|=
name|QueueServiceUtil
operator|.
name|createQueueClient
argument_list|(
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|QueueServiceRequestOptions
name|opts
init|=
name|QueueServiceUtil
operator|.
name|getRequestOptions
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|doCreateQueue
argument_list|(
name|client
argument_list|,
name|opts
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|doCreateQueue (CloudQueue client, QueueServiceRequestOptions opts, Exchange exchange)
specifier|private
name|void
name|doCreateQueue
parameter_list|(
name|CloudQueue
name|client
parameter_list|,
name|QueueServiceRequestOptions
name|opts
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating the queue [{}] from exchange [{}]..."
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|client
operator|.
name|createIfNotExists
argument_list|(
name|opts
operator|.
name|getRequestOpts
argument_list|()
argument_list|,
name|opts
operator|.
name|getOpContext
argument_list|()
argument_list|)
expr_stmt|;
name|ExchangeUtil
operator|.
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
operator|.
name|setHeader
argument_list|(
name|QueueServiceConstants
operator|.
name|QUEUE_CREATED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteQueue (Exchange exchange)
specifier|private
name|void
name|deleteQueue
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Deleting the queue [{}] from exchange [{}]..."
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|CloudQueue
name|client
init|=
name|QueueServiceUtil
operator|.
name|createQueueClient
argument_list|(
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|QueueServiceRequestOptions
name|opts
init|=
name|QueueServiceUtil
operator|.
name|getRequestOptions
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|client
operator|.
name|delete
argument_list|(
name|opts
operator|.
name|getRequestOpts
argument_list|()
argument_list|,
name|opts
operator|.
name|getOpContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|addMessage (Exchange exchange)
specifier|private
name|void
name|addMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Putting the message into the queue [{}] from exchange [{}]..."
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|CloudQueue
name|client
init|=
name|QueueServiceUtil
operator|.
name|createQueueClient
argument_list|(
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|QueueServiceRequestOptions
name|opts
init|=
name|QueueServiceUtil
operator|.
name|getRequestOptions
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Boolean
name|queueCreated
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|QueueServiceConstants
operator|.
name|QUEUE_CREATED
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|!=
name|queueCreated
condition|)
block|{
name|doCreateQueue
argument_list|(
name|client
argument_list|,
name|opts
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|CloudQueueMessage
name|message
init|=
name|getCloudQueueMessage
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|client
operator|.
name|addMessage
argument_list|(
name|message
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getMessageTimeToLive
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getMessageVisibilityDelay
argument_list|()
argument_list|,
name|opts
operator|.
name|getRequestOpts
argument_list|()
argument_list|,
name|opts
operator|.
name|getOpContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|updateMessage (Exchange exchange)
specifier|private
name|void
name|updateMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|CloudQueue
name|client
init|=
name|QueueServiceUtil
operator|.
name|createQueueClient
argument_list|(
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|QueueServiceRequestOptions
name|opts
init|=
name|QueueServiceUtil
operator|.
name|getRequestOptions
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|CloudQueueMessage
name|message
init|=
name|getCloudQueueMessage
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Updating the message in the queue [{}] from exchange [{}]..."
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|EnumSet
argument_list|<
name|MessageUpdateFields
argument_list|>
name|fields
init|=
literal|null
decl_stmt|;
name|Object
name|fieldsObject
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|QueueServiceConstants
operator|.
name|MESSAGE_UPDATE_FIELDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|fieldsObject
operator|instanceof
name|EnumSet
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|EnumSet
argument_list|<
name|MessageUpdateFields
argument_list|>
name|theFields
init|=
operator|(
name|EnumSet
argument_list|<
name|MessageUpdateFields
argument_list|>
operator|)
name|fieldsObject
decl_stmt|;
name|fields
operator|=
name|theFields
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fieldsObject
operator|instanceof
name|MessageUpdateFields
condition|)
block|{
name|fields
operator|=
name|EnumSet
operator|.
name|of
argument_list|(
operator|(
name|MessageUpdateFields
operator|)
name|fieldsObject
argument_list|)
expr_stmt|;
block|}
name|client
operator|.
name|updateMessage
argument_list|(
name|message
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getMessageVisibilityDelay
argument_list|()
argument_list|,
name|fields
argument_list|,
name|opts
operator|.
name|getRequestOpts
argument_list|()
argument_list|,
name|opts
operator|.
name|getOpContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteMessage (Exchange exchange)
specifier|private
name|void
name|deleteMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Deleting the message from the queue [{}] from exchange [{}]..."
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|CloudQueue
name|client
init|=
name|QueueServiceUtil
operator|.
name|createQueueClient
argument_list|(
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|QueueServiceRequestOptions
name|opts
init|=
name|QueueServiceUtil
operator|.
name|getRequestOptions
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|CloudQueueMessage
name|message
init|=
name|getCloudQueueMessage
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|client
operator|.
name|deleteMessage
argument_list|(
name|message
argument_list|,
name|opts
operator|.
name|getRequestOpts
argument_list|()
argument_list|,
name|opts
operator|.
name|getOpContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|retrieveMessage (Exchange exchange)
specifier|private
name|void
name|retrieveMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|QueueServiceUtil
operator|.
name|retrieveMessage
argument_list|(
name|exchange
argument_list|,
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|peekMessage (Exchange exchange)
specifier|private
name|void
name|peekMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|CloudQueue
name|client
init|=
name|QueueServiceUtil
operator|.
name|createQueueClient
argument_list|(
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|QueueServiceRequestOptions
name|opts
init|=
name|QueueServiceUtil
operator|.
name|getRequestOptions
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|CloudQueueMessage
name|message
init|=
name|client
operator|.
name|peekMessage
argument_list|(
name|opts
operator|.
name|getRequestOpts
argument_list|()
argument_list|,
name|opts
operator|.
name|getOpContext
argument_list|()
argument_list|)
decl_stmt|;
name|ExchangeUtil
operator|.
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
operator|.
name|setBody
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|getCloudQueueMessage (Exchange exchange)
specifier|private
name|CloudQueueMessage
name|getCloudQueueMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|()
decl_stmt|;
name|CloudQueueMessage
name|message
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|CloudQueueMessage
condition|)
block|{
name|message
operator|=
operator|(
name|CloudQueueMessage
operator|)
name|body
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|String
condition|)
block|{
name|message
operator|=
operator|new
name|CloudQueueMessage
argument_list|(
operator|(
name|String
operator|)
name|body
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported queue message type:"
operator|+
name|body
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|message
return|;
block|}
DECL|method|determineOperation (Exchange exchange)
specifier|private
name|QueueServiceOperations
name|determineOperation
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|QueueServiceOperations
name|operation
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|BlobServiceConstants
operator|.
name|OPERATION
argument_list|,
name|QueueServiceOperations
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
name|QueueServiceConfiguration
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
return|return
literal|"StorageQueueProducer["
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
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|QueueServiceEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|QueueServiceEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
block|}
end_class

end_unit

