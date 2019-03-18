begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.streaming
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|internal
operator|.
name|streaming
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
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
name|CamelException
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
name|salesforce
operator|.
name|SalesforceEndpointConfig
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
name|salesforce
operator|.
name|api
operator|.
name|SalesforceException
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|CreateSObjectResult
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
name|salesforce
operator|.
name|api
operator|.
name|utils
operator|.
name|JsonUtils
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
name|salesforce
operator|.
name|internal
operator|.
name|client
operator|.
name|RestClient
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
name|salesforce
operator|.
name|internal
operator|.
name|client
operator|.
name|SyncResponseCallback
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
name|salesforce
operator|.
name|internal
operator|.
name|dto
operator|.
name|PushTopic
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
name|salesforce
operator|.
name|internal
operator|.
name|dto
operator|.
name|QueryRecordsPushTopic
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|http
operator|.
name|HttpStatus
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
DECL|class|PushTopicHelper
specifier|public
class|class
name|PushTopicHelper
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
name|PushTopicHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|OBJECT_MAPPER
specifier|private
specifier|static
specifier|final
name|ObjectMapper
name|OBJECT_MAPPER
init|=
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
decl_stmt|;
DECL|field|PUSH_TOPIC_OBJECT_NAME
specifier|private
specifier|static
specifier|final
name|String
name|PUSH_TOPIC_OBJECT_NAME
init|=
literal|"PushTopic"
decl_stmt|;
DECL|field|API_TIMEOUT
specifier|private
specifier|static
specifier|final
name|long
name|API_TIMEOUT
init|=
literal|60
decl_stmt|;
comment|// Rest API call timeout
DECL|field|config
specifier|private
specifier|final
name|SalesforceEndpointConfig
name|config
decl_stmt|;
DECL|field|topicName
specifier|private
specifier|final
name|String
name|topicName
decl_stmt|;
DECL|field|restClient
specifier|private
specifier|final
name|RestClient
name|restClient
decl_stmt|;
DECL|field|preApi29
specifier|private
specifier|final
name|boolean
name|preApi29
decl_stmt|;
DECL|method|PushTopicHelper (SalesforceEndpointConfig config, String topicName, RestClient restClient)
specifier|public
name|PushTopicHelper
parameter_list|(
name|SalesforceEndpointConfig
name|config
parameter_list|,
name|String
name|topicName
parameter_list|,
name|RestClient
name|restClient
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|topicName
operator|=
name|topicName
expr_stmt|;
name|this
operator|.
name|restClient
operator|=
name|restClient
expr_stmt|;
name|this
operator|.
name|preApi29
operator|=
name|Double
operator|.
name|valueOf
argument_list|(
name|config
operator|.
name|getApiVersion
argument_list|()
argument_list|)
operator|<
literal|29.0
expr_stmt|;
comment|// validate notify fields right away
if|if
condition|(
name|preApi29
operator|&&
operator|(
name|config
operator|.
name|getNotifyForOperationCreate
argument_list|()
operator|!=
literal|null
operator|||
name|config
operator|.
name|getNotifyForOperationDelete
argument_list|()
operator|!=
literal|null
operator|||
name|config
operator|.
name|getNotifyForOperationUndelete
argument_list|()
operator|!=
literal|null
operator|||
name|config
operator|.
name|getNotifyForOperationUpdate
argument_list|()
operator|!=
literal|null
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"NotifyForOperationCreate, NotifyForOperationDelete"
operator|+
literal|", NotifyForOperationUndelete, and NotifyForOperationUpdate"
operator|+
literal|" are only supported since API version 29.0"
operator|+
literal|", instead use NotifyForOperations"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|preApi29
operator|&&
name|config
operator|.
name|getNotifyForOperations
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"NotifyForOperations is readonly since API version 29.0"
operator|+
literal|", instead use NotifyForOperationCreate, NotifyForOperationDelete"
operator|+
literal|", NotifyForOperationUndelete, and NotifyForOperationUpdate"
argument_list|)
throw|;
block|}
block|}
DECL|method|createOrUpdateTopic ()
specifier|public
name|void
name|createOrUpdateTopic
parameter_list|()
throws|throws
name|CamelException
block|{
specifier|final
name|String
name|query
init|=
name|config
operator|.
name|getSObjectQuery
argument_list|()
decl_stmt|;
specifier|final
name|SyncResponseCallback
name|callback
init|=
operator|new
name|SyncResponseCallback
argument_list|()
decl_stmt|;
comment|// lookup Topic first
try|try
block|{
comment|// use SOQL to lookup Topic, since Name is not an external ID!!!
name|restClient
operator|.
name|query
argument_list|(
literal|"SELECT Id, Name, Query, ApiVersion, IsActive, "
operator|+
literal|"NotifyForFields, NotifyForOperations, NotifyForOperationCreate, "
operator|+
literal|"NotifyForOperationDelete, NotifyForOperationUndelete, "
operator|+
literal|"NotifyForOperationUpdate, Description "
operator|+
literal|"FROM PushTopic WHERE Name = '"
operator|+
name|topicName
operator|+
literal|"'"
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|,
name|callback
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|callback
operator|.
name|await
argument_list|(
name|API_TIMEOUT
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SalesforceException
argument_list|(
literal|"API call timeout!"
argument_list|,
literal|null
argument_list|)
throw|;
block|}
specifier|final
name|SalesforceException
name|callbackException
init|=
name|callback
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|callbackException
operator|!=
literal|null
condition|)
block|{
throw|throw
name|callbackException
throw|;
block|}
name|QueryRecordsPushTopic
name|records
init|=
name|OBJECT_MAPPER
operator|.
name|readValue
argument_list|(
name|callback
operator|.
name|getResponse
argument_list|()
argument_list|,
name|QueryRecordsPushTopic
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|records
operator|.
name|getTotalSize
argument_list|()
operator|==
literal|1
condition|)
block|{
name|PushTopic
name|topic
init|=
name|records
operator|.
name|getRecords
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Found existing topic {}: {}"
argument_list|,
name|topicName
argument_list|,
name|topic
argument_list|)
expr_stmt|;
comment|// check if we need to update topic
specifier|final
name|boolean
name|notifyOperationsChanged
decl_stmt|;
if|if
condition|(
name|preApi29
condition|)
block|{
name|notifyOperationsChanged
operator|=
name|notEquals
argument_list|(
name|config
operator|.
name|getNotifyForOperations
argument_list|()
argument_list|,
name|topic
operator|.
name|getNotifyForOperations
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|notifyOperationsChanged
operator|=
name|notEquals
argument_list|(
name|config
operator|.
name|getNotifyForOperationCreate
argument_list|()
argument_list|,
name|topic
operator|.
name|getNotifyForOperationCreate
argument_list|()
argument_list|)
operator|||
name|notEquals
argument_list|(
name|config
operator|.
name|getNotifyForOperationDelete
argument_list|()
argument_list|,
name|topic
operator|.
name|getNotifyForOperationDelete
argument_list|()
argument_list|)
operator|||
name|notEquals
argument_list|(
name|config
operator|.
name|getNotifyForOperationUndelete
argument_list|()
argument_list|,
name|topic
operator|.
name|getNotifyForOperationUndelete
argument_list|()
argument_list|)
operator|||
name|notEquals
argument_list|(
name|config
operator|.
name|getNotifyForOperationUpdate
argument_list|()
argument_list|,
name|topic
operator|.
name|getNotifyForOperationUpdate
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|query
operator|.
name|equals
argument_list|(
name|topic
operator|.
name|getQuery
argument_list|()
argument_list|)
operator|||
name|notEquals
argument_list|(
name|config
operator|.
name|getNotifyForFields
argument_list|()
argument_list|,
name|topic
operator|.
name|getNotifyForFields
argument_list|()
argument_list|)
operator|||
name|notifyOperationsChanged
condition|)
block|{
if|if
condition|(
operator|!
name|config
operator|.
name|isUpdateTopic
argument_list|()
condition|)
block|{
name|String
name|msg
init|=
literal|"Query doesn't match existing Topic and updateTopic is set to false"
decl_stmt|;
throw|throw
operator|new
name|CamelException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
comment|// otherwise update the topic
name|updateTopic
argument_list|(
name|topic
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|createTopic
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SalesforceException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error retrieving Topic %s: %s"
argument_list|,
name|topicName
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Un-marshaling error retrieving Topic %s: %s"
argument_list|,
name|topicName
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|CamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Un-marshaling error retrieving Topic %s: %s"
argument_list|,
name|topicName
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
comment|// close stream to close HttpConnection
if|if
condition|(
name|callback
operator|.
name|getResponse
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|callback
operator|.
name|getResponse
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
DECL|method|createTopic ()
specifier|private
name|void
name|createTopic
parameter_list|()
throws|throws
name|CamelException
block|{
specifier|final
name|PushTopic
name|topic
init|=
operator|new
name|PushTopic
argument_list|()
decl_stmt|;
name|topic
operator|.
name|setName
argument_list|(
name|topicName
argument_list|)
expr_stmt|;
name|topic
operator|.
name|setApiVersion
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
name|config
operator|.
name|getApiVersion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|topic
operator|.
name|setQuery
argument_list|(
name|config
operator|.
name|getSObjectQuery
argument_list|()
argument_list|)
expr_stmt|;
name|topic
operator|.
name|setDescription
argument_list|(
literal|"Topic created by Camel Salesforce component"
argument_list|)
expr_stmt|;
name|topic
operator|.
name|setNotifyForFields
argument_list|(
name|config
operator|.
name|getNotifyForFields
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|preApi29
condition|)
block|{
name|topic
operator|.
name|setNotifyForOperations
argument_list|(
name|config
operator|.
name|getNotifyForOperations
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|topic
operator|.
name|setNotifyForOperationCreate
argument_list|(
name|config
operator|.
name|getNotifyForOperationCreate
argument_list|()
argument_list|)
expr_stmt|;
name|topic
operator|.
name|setNotifyForOperationDelete
argument_list|(
name|config
operator|.
name|getNotifyForOperationDelete
argument_list|()
argument_list|)
expr_stmt|;
name|topic
operator|.
name|setNotifyForOperationUndelete
argument_list|(
name|config
operator|.
name|getNotifyForOperationUndelete
argument_list|()
argument_list|)
expr_stmt|;
name|topic
operator|.
name|setNotifyForOperationUpdate
argument_list|(
name|config
operator|.
name|getNotifyForOperationUpdate
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating Topic {}: {}"
argument_list|,
name|topicName
argument_list|,
name|topic
argument_list|)
expr_stmt|;
specifier|final
name|SyncResponseCallback
name|callback
init|=
operator|new
name|SyncResponseCallback
argument_list|()
decl_stmt|;
try|try
block|{
name|restClient
operator|.
name|createSObject
argument_list|(
name|PUSH_TOPIC_OBJECT_NAME
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|OBJECT_MAPPER
operator|.
name|writeValueAsBytes
argument_list|(
name|topic
argument_list|)
argument_list|)
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|,
name|callback
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|callback
operator|.
name|await
argument_list|(
name|API_TIMEOUT
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SalesforceException
argument_list|(
literal|"API call timeout!"
argument_list|,
literal|null
argument_list|)
throw|;
block|}
specifier|final
name|SalesforceException
name|callbackException
init|=
name|callback
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|callbackException
operator|!=
literal|null
condition|)
block|{
throw|throw
name|callbackException
throw|;
block|}
name|CreateSObjectResult
name|result
init|=
name|OBJECT_MAPPER
operator|.
name|readValue
argument_list|(
name|callback
operator|.
name|getResponse
argument_list|()
argument_list|,
name|CreateSObjectResult
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|getSuccess
argument_list|()
condition|)
block|{
specifier|final
name|SalesforceException
name|salesforceException
init|=
operator|new
name|SalesforceException
argument_list|(
name|result
operator|.
name|getErrors
argument_list|()
argument_list|,
name|HttpStatus
operator|.
name|BAD_REQUEST_400
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|CamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error creating Topic %s: %s"
argument_list|,
name|topicName
argument_list|,
name|result
operator|.
name|getErrors
argument_list|()
argument_list|)
argument_list|,
name|salesforceException
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|SalesforceException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error creating Topic %s: %s"
argument_list|,
name|topicName
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Un-marshaling error creating Topic %s: %s"
argument_list|,
name|topicName
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Un-marshaling error creating Topic %s: %s"
argument_list|,
name|topicName
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|callback
operator|.
name|getResponse
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|callback
operator|.
name|getResponse
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
DECL|method|updateTopic (String topicId)
specifier|private
name|void
name|updateTopic
parameter_list|(
name|String
name|topicId
parameter_list|)
throws|throws
name|CamelException
block|{
specifier|final
name|String
name|query
init|=
name|config
operator|.
name|getSObjectQuery
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Updating Topic {} with Query [{}]"
argument_list|,
name|topicName
argument_list|,
name|query
argument_list|)
expr_stmt|;
specifier|final
name|SyncResponseCallback
name|callback
init|=
operator|new
name|SyncResponseCallback
argument_list|()
decl_stmt|;
try|try
block|{
comment|// update the query, notifyForFields and notifyForOperations fields
specifier|final
name|PushTopic
name|topic
init|=
operator|new
name|PushTopic
argument_list|()
decl_stmt|;
name|topic
operator|.
name|setQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|topic
operator|.
name|setNotifyForFields
argument_list|(
name|config
operator|.
name|getNotifyForFields
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|preApi29
condition|)
block|{
name|topic
operator|.
name|setNotifyForOperations
argument_list|(
name|config
operator|.
name|getNotifyForOperations
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|topic
operator|.
name|setNotifyForOperationCreate
argument_list|(
name|config
operator|.
name|getNotifyForOperationCreate
argument_list|()
argument_list|)
expr_stmt|;
name|topic
operator|.
name|setNotifyForOperationDelete
argument_list|(
name|config
operator|.
name|getNotifyForOperationDelete
argument_list|()
argument_list|)
expr_stmt|;
name|topic
operator|.
name|setNotifyForOperationUndelete
argument_list|(
name|config
operator|.
name|getNotifyForOperationUndelete
argument_list|()
argument_list|)
expr_stmt|;
name|topic
operator|.
name|setNotifyForOperationUpdate
argument_list|(
name|config
operator|.
name|getNotifyForOperationUpdate
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|restClient
operator|.
name|updateSObject
argument_list|(
literal|"PushTopic"
argument_list|,
name|topicId
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|OBJECT_MAPPER
operator|.
name|writeValueAsBytes
argument_list|(
name|topic
argument_list|)
argument_list|)
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|,
name|callback
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|callback
operator|.
name|await
argument_list|(
name|API_TIMEOUT
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SalesforceException
argument_list|(
literal|"API call timeout!"
argument_list|,
literal|null
argument_list|)
throw|;
block|}
specifier|final
name|SalesforceException
name|callbackException
init|=
name|callback
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|callbackException
operator|!=
literal|null
condition|)
block|{
throw|throw
name|callbackException
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|SalesforceException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error updating topic %s with query [%s] : %s"
argument_list|,
name|topicName
argument_list|,
name|query
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// reset interrupt status
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|CamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error updating topic %s with query [%s] : %s"
argument_list|,
name|topicName
argument_list|,
name|query
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error updating topic %s with query [%s] : %s"
argument_list|,
name|topicName
argument_list|,
name|query
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|callback
operator|.
name|getResponse
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|callback
operator|.
name|getResponse
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ignore
parameter_list|)
block|{                 }
block|}
block|}
block|}
DECL|method|notEquals (T o1, T o2)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|boolean
name|notEquals
parameter_list|(
name|T
name|o1
parameter_list|,
name|T
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|!=
literal|null
operator|&&
operator|!
name|o1
operator|.
name|equals
argument_list|(
name|o2
argument_list|)
return|;
block|}
block|}
end_class

end_unit

