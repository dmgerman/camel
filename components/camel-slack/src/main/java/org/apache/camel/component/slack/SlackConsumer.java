begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.slack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|slack
package|;
end_package

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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
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
name|ScheduledBatchPollingConsumer
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
name|CastUtils
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
name|json
operator|.
name|DeserializationException
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
name|json
operator|.
name|JsonArray
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
name|json
operator|.
name|JsonObject
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
name|json
operator|.
name|Jsoner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|entity
operator|.
name|UrlEncodedFormEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpPost
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|HttpClientBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|message
operator|.
name|BasicNameValuePair
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
name|slack
operator|.
name|utils
operator|.
name|SlackUtils
operator|.
name|readResponse
import|;
end_import

begin_class
DECL|class|SlackConsumer
specifier|public
class|class
name|SlackConsumer
extends|extends
name|ScheduledBatchPollingConsumer
block|{
DECL|field|slackEndpoint
specifier|private
name|SlackEndpoint
name|slackEndpoint
decl_stmt|;
DECL|field|timestamp
specifier|private
name|String
name|timestamp
decl_stmt|;
DECL|field|channelId
specifier|private
name|String
name|channelId
decl_stmt|;
DECL|method|SlackConsumer (SlackEndpoint endpoint, Processor processor)
specifier|public
name|SlackConsumer
parameter_list|(
name|SlackEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|IOException
throws|,
name|DeserializationException
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
name|slackEndpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|channelId
operator|=
name|getChannelId
argument_list|(
name|slackEndpoint
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|exchanges
decl_stmt|;
name|HttpClient
name|client
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|useSystemProperties
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|HttpPost
name|httpPost
init|=
operator|new
name|HttpPost
argument_list|(
name|slackEndpoint
operator|.
name|getServerUrl
argument_list|()
operator|+
literal|"/api/channels.history"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|BasicNameValuePair
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<
name|BasicNameValuePair
argument_list|>
argument_list|()
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"channel"
argument_list|,
name|channelId
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|timestamp
argument_list|)
condition|)
block|{
name|params
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"oldest"
argument_list|,
name|timestamp
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|params
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"count"
argument_list|,
name|slackEndpoint
operator|.
name|getMaxResults
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|params
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"token"
argument_list|,
name|slackEndpoint
operator|.
name|getToken
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|httpPost
operator|.
name|setEntity
argument_list|(
operator|new
name|UrlEncodedFormEntity
argument_list|(
name|params
argument_list|)
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|httpPost
argument_list|)
decl_stmt|;
name|String
name|jsonString
init|=
name|readResponse
argument_list|(
name|response
argument_list|)
decl_stmt|;
name|JsonObject
name|c
init|=
operator|(
name|JsonObject
operator|)
name|Jsoner
operator|.
name|deserialize
argument_list|(
name|jsonString
argument_list|)
decl_stmt|;
name|JsonArray
name|list
init|=
name|c
operator|.
name|getCollection
argument_list|(
literal|"messages"
argument_list|)
decl_stmt|;
name|exchanges
operator|=
name|createExchanges
argument_list|(
name|list
argument_list|)
expr_stmt|;
return|return
name|processBatch
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|exchanges
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createExchanges (List<Object> list)
specifier|private
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|createExchanges
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|list
parameter_list|)
block|{
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|answer
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|list
argument_list|)
condition|)
block|{
name|Iterator
name|it
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|object
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|JsonObject
name|singleMess
init|=
operator|(
name|JsonObject
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
name|timestamp
operator|=
operator|(
name|String
operator|)
name|singleMess
operator|.
name|get
argument_list|(
literal|"ts"
argument_list|)
expr_stmt|;
block|}
name|i
operator|++
expr_stmt|;
name|Exchange
name|exchange
init|=
name|slackEndpoint
operator|.
name|createExchange
argument_list|(
name|singleMess
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|processBatch (Queue<Object> exchanges)
specifier|public
name|int
name|processBatch
parameter_list|(
name|Queue
argument_list|<
name|Object
argument_list|>
name|exchanges
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|total
init|=
name|exchanges
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|total
operator|&&
name|isBatchAllowed
argument_list|()
condition|;
name|index
operator|++
control|)
block|{
comment|// only loop if we are started (allowed to run)
specifier|final
name|Exchange
name|exchange
init|=
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|Exchange
operator|.
name|class
argument_list|,
name|exchanges
operator|.
name|poll
argument_list|()
argument_list|)
decl_stmt|;
comment|// add current index and total as properties
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_SIZE
argument_list|,
name|total
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|,
name|index
operator|==
name|total
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|// update pending number of exchanges
name|pendingExchanges
operator|=
name|total
operator|-
name|index
operator|-
literal|1
expr_stmt|;
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
name|log
operator|.
name|trace
argument_list|(
literal|"Processing exchange done"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
DECL|method|getChannelId (String channel)
specifier|private
name|String
name|getChannelId
parameter_list|(
name|String
name|channel
parameter_list|)
throws|throws
name|IOException
throws|,
name|DeserializationException
block|{
name|HttpClient
name|client
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|useSystemProperties
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|HttpPost
name|httpPost
init|=
operator|new
name|HttpPost
argument_list|(
name|slackEndpoint
operator|.
name|getServerUrl
argument_list|()
operator|+
literal|"/api/channels.list"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|BasicNameValuePair
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<
name|BasicNameValuePair
argument_list|>
argument_list|()
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
operator|new
name|BasicNameValuePair
argument_list|(
literal|"token"
argument_list|,
name|slackEndpoint
operator|.
name|getToken
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|httpPost
operator|.
name|setEntity
argument_list|(
operator|new
name|UrlEncodedFormEntity
argument_list|(
name|params
argument_list|)
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|httpPost
argument_list|)
decl_stmt|;
name|String
name|jsonString
init|=
name|readResponse
argument_list|(
name|response
argument_list|)
decl_stmt|;
name|JsonObject
name|c
init|=
operator|(
name|JsonObject
operator|)
name|Jsoner
operator|.
name|deserialize
argument_list|(
name|jsonString
argument_list|)
decl_stmt|;
name|JsonArray
name|list
init|=
operator|(
name|JsonArray
operator|)
name|c
operator|.
name|getCollection
argument_list|(
literal|"channels"
argument_list|)
decl_stmt|;
for|for
control|(
name|JsonObject
name|singleChannel
range|:
call|(
name|List
argument_list|<
name|JsonObject
argument_list|>
call|)
argument_list|(
name|List
argument_list|)
name|list
control|)
block|{
if|if
condition|(
name|singleChannel
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|singleChannel
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
operator|.
name|equals
argument_list|(
name|channel
argument_list|)
condition|)
block|{
if|if
condition|(
name|singleChannel
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|singleChannel
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
return|;
block|}
block|}
block|}
block|}
return|return
name|jsonString
return|;
block|}
block|}
end_class

end_unit

