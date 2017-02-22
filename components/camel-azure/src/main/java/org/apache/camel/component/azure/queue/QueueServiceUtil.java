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
name|net
operator|.
name|URI
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
name|OperationContext
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
name|StorageCredentials
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
name|QueueRequestOptions
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
name|common
operator|.
name|ExchangeUtil
import|;
end_import

begin_class
DECL|class|QueueServiceUtil
specifier|public
specifier|final
class|class
name|QueueServiceUtil
block|{
DECL|method|QueueServiceUtil ()
specifier|private
name|QueueServiceUtil
parameter_list|()
block|{      }
DECL|method|prepareStorageQueueUri (QueueServiceConfiguration cfg)
specifier|public
specifier|static
name|URI
name|prepareStorageQueueUri
parameter_list|(
name|QueueServiceConfiguration
name|cfg
parameter_list|)
block|{
return|return
name|prepareStorageQueueUri
argument_list|(
name|cfg
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|prepareStorageQueueUri (QueueServiceConfiguration cfg, boolean isForMessages)
specifier|public
specifier|static
name|URI
name|prepareStorageQueueUri
parameter_list|(
name|QueueServiceConfiguration
name|cfg
parameter_list|,
name|boolean
name|isForMessages
parameter_list|)
block|{
name|StringBuilder
name|uriBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|uriBuilder
operator|.
name|append
argument_list|(
literal|"https://"
argument_list|)
operator|.
name|append
argument_list|(
name|cfg
operator|.
name|getAccountName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|QueueServiceConstants
operator|.
name|SERVICE_URI_SEGMENT
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
name|cfg
operator|.
name|getQueueName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isForMessages
condition|)
block|{
name|uriBuilder
operator|.
name|append
argument_list|(
literal|"/messages"
argument_list|)
expr_stmt|;
block|}
return|return
name|URI
operator|.
name|create
argument_list|(
name|uriBuilder
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createQueueClient (QueueServiceConfiguration cfg)
specifier|public
specifier|static
name|CloudQueue
name|createQueueClient
parameter_list|(
name|QueueServiceConfiguration
name|cfg
parameter_list|)
throws|throws
name|Exception
block|{
name|CloudQueue
name|client
init|=
operator|(
name|CloudQueue
operator|)
name|getConfiguredClient
argument_list|(
name|cfg
argument_list|)
decl_stmt|;
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|URI
name|uri
init|=
name|prepareStorageQueueUri
argument_list|(
name|cfg
argument_list|)
decl_stmt|;
name|StorageCredentials
name|creds
init|=
name|getAccountCredentials
argument_list|(
name|cfg
argument_list|)
decl_stmt|;
name|client
operator|=
operator|new
name|CloudQueue
argument_list|(
name|uri
argument_list|,
name|creds
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
DECL|method|getConfiguredClient (QueueServiceConfiguration cfg)
specifier|public
specifier|static
name|CloudQueue
name|getConfiguredClient
parameter_list|(
name|QueueServiceConfiguration
name|cfg
parameter_list|)
block|{
name|CloudQueue
name|client
init|=
name|cfg
operator|.
name|getAzureQueueClient
argument_list|()
decl_stmt|;
if|if
condition|(
name|client
operator|!=
literal|null
operator|&&
operator|!
name|client
operator|.
name|getUri
argument_list|()
operator|.
name|equals
argument_list|(
name|prepareStorageQueueUri
argument_list|(
name|cfg
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid Client URI"
argument_list|)
throw|;
block|}
return|return
name|client
return|;
block|}
DECL|method|getAccountCredentials (QueueServiceConfiguration cfg)
specifier|public
specifier|static
name|StorageCredentials
name|getAccountCredentials
parameter_list|(
name|QueueServiceConfiguration
name|cfg
parameter_list|)
block|{
return|return
name|cfg
operator|.
name|getCredentials
argument_list|()
return|;
block|}
DECL|method|retrieveMessage (Exchange exchange, QueueServiceConfiguration cfg)
specifier|public
specifier|static
name|void
name|retrieveMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|QueueServiceConfiguration
name|cfg
parameter_list|)
throws|throws
name|Exception
block|{
name|CloudQueue
name|client
init|=
name|createQueueClient
argument_list|(
name|cfg
argument_list|)
decl_stmt|;
name|QueueServiceRequestOptions
name|opts
init|=
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
name|retrieveMessage
argument_list|(
name|cfg
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
DECL|method|getRequestOptions (Exchange exchange)
specifier|public
specifier|static
name|QueueServiceRequestOptions
name|getRequestOptions
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|QueueServiceRequestOptions
name|opts
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
name|QUEUE_SERVICE_REQUEST_OPTIONS
argument_list|,
name|QueueServiceRequestOptions
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|opts
operator|!=
literal|null
condition|)
block|{
return|return
name|opts
return|;
block|}
else|else
block|{
name|opts
operator|=
operator|new
name|QueueServiceRequestOptions
argument_list|()
expr_stmt|;
block|}
name|QueueRequestOptions
name|requestOpts
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
name|QUEUE_REQUEST_OPTIONS
argument_list|,
name|QueueRequestOptions
operator|.
name|class
argument_list|)
decl_stmt|;
name|OperationContext
name|opContext
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
name|OPERATION_CONTEXT
argument_list|,
name|OperationContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|opts
operator|.
name|setOpContext
argument_list|(
name|opContext
argument_list|)
expr_stmt|;
name|opts
operator|.
name|setRequestOpts
argument_list|(
name|requestOpts
argument_list|)
expr_stmt|;
return|return
name|opts
return|;
block|}
block|}
end_class

end_unit

