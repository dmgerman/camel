begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ironmq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ironmq
package|;
end_package

begin_import
import|import
name|io
operator|.
name|iron
operator|.
name|ironmq
operator|.
name|Client
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
name|Metadata
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
name|spi
operator|.
name|UriParams
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
name|UriPath
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|IronMQConfiguration
specifier|public
class|class
name|IronMQConfiguration
block|{
comment|// common properties
annotation|@
name|UriParam
DECL|field|projectId
specifier|private
name|String
name|projectId
decl_stmt|;
annotation|@
name|UriParam
DECL|field|token
specifier|private
name|String
name|token
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|queueName
specifier|private
name|String
name|queueName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"https://mq-aws-us-east-1-1.iron.io"
argument_list|)
DECL|field|ironMQCloud
specifier|private
name|String
name|ironMQCloud
init|=
literal|"https://mq-aws-us-east-1-1.iron.io"
decl_stmt|;
annotation|@
name|UriParam
DECL|field|preserveHeaders
specifier|private
name|boolean
name|preserveHeaders
decl_stmt|;
annotation|@
name|UriParam
DECL|field|client
specifier|private
name|Client
name|client
decl_stmt|;
comment|// producer properties
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|visibilityDelay
specifier|private
name|int
name|visibilityDelay
decl_stmt|;
comment|// consumer properties
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1"
argument_list|,
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|concurrentConsumers
specifier|private
name|int
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|batchDelete
specifier|private
name|boolean
name|batchDelete
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1"
argument_list|,
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|maxMessagesPerPoll
specifier|private
name|int
name|maxMessagesPerPoll
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"60"
argument_list|,
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|timeout
specifier|private
name|int
name|timeout
init|=
literal|60
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|wait
specifier|private
name|int
name|wait
decl_stmt|;
DECL|method|getClient ()
specifier|public
name|Client
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
comment|/**      * Reference to a io.iron.ironmq.Client in the Registry.       */
DECL|method|setClient (Client client)
specifier|public
name|void
name|setClient
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
DECL|method|getConcurrentConsumers ()
specifier|public
name|int
name|getConcurrentConsumers
parameter_list|()
block|{
return|return
name|concurrentConsumers
return|;
block|}
comment|/**      * The number of concurrent consumers.      */
DECL|method|setConcurrentConsumers (int concurrentConsumers)
specifier|public
name|void
name|setConcurrentConsumers
parameter_list|(
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
DECL|method|getProjectId ()
specifier|public
name|String
name|getProjectId
parameter_list|()
block|{
return|return
name|projectId
return|;
block|}
comment|/**      * IronMQ projectId      */
DECL|method|setProjectId (String projectId)
specifier|public
name|void
name|setProjectId
parameter_list|(
name|String
name|projectId
parameter_list|)
block|{
name|this
operator|.
name|projectId
operator|=
name|projectId
expr_stmt|;
block|}
DECL|method|getToken ()
specifier|public
name|String
name|getToken
parameter_list|()
block|{
return|return
name|token
return|;
block|}
comment|/**      * IronMQ token      */
DECL|method|setToken (String token)
specifier|public
name|void
name|setToken
parameter_list|(
name|String
name|token
parameter_list|)
block|{
name|this
operator|.
name|token
operator|=
name|token
expr_stmt|;
block|}
comment|/**      * The name of the IronMQ queue       */
DECL|method|setQueueName (String queueName)
specifier|public
name|void
name|setQueueName
parameter_list|(
name|String
name|queueName
parameter_list|)
block|{
name|this
operator|.
name|queueName
operator|=
name|queueName
expr_stmt|;
block|}
DECL|method|getQueueName ()
specifier|public
name|String
name|getQueueName
parameter_list|()
block|{
return|return
name|queueName
return|;
block|}
comment|/**      * IronMq Cloud url. Urls for public clusters: mq-aws-us-east-1-1.iron.io (US), mq-aws-eu-west-1-1.iron.io (EU)      */
DECL|method|setIronMQCloud (String ironMQCloud)
specifier|public
name|void
name|setIronMQCloud
parameter_list|(
name|String
name|ironMQCloud
parameter_list|)
block|{
name|this
operator|.
name|ironMQCloud
operator|=
name|ironMQCloud
expr_stmt|;
block|}
DECL|method|getIronMQCloud ()
specifier|public
name|String
name|getIronMQCloud
parameter_list|()
block|{
return|return
name|ironMQCloud
return|;
block|}
DECL|method|getTimeout ()
specifier|public
name|int
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
comment|/**      * After timeout (in seconds), item will be placed back onto the queue.      */
DECL|method|setTimeout (int timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getMaxMessagesPerPoll ()
specifier|public
name|int
name|getMaxMessagesPerPoll
parameter_list|()
block|{
return|return
name|maxMessagesPerPoll
return|;
block|}
comment|/**      * Number of messages to poll pr. call. Maximum is 100.      */
DECL|method|setMaxMessagesPerPoll (int maxMessagesPerPoll)
specifier|public
name|void
name|setMaxMessagesPerPoll
parameter_list|(
name|int
name|maxMessagesPerPoll
parameter_list|)
block|{
name|this
operator|.
name|maxMessagesPerPoll
operator|=
name|maxMessagesPerPoll
expr_stmt|;
block|}
DECL|method|getVisibilityDelay ()
specifier|public
name|int
name|getVisibilityDelay
parameter_list|()
block|{
return|return
name|visibilityDelay
return|;
block|}
comment|/**      * The item will not be available on the queue until this many seconds have passed.       * Default is 0 seconds.      */
DECL|method|setVisibilityDelay (int visibilityDelay)
specifier|public
name|void
name|setVisibilityDelay
parameter_list|(
name|int
name|visibilityDelay
parameter_list|)
block|{
name|this
operator|.
name|visibilityDelay
operator|=
name|visibilityDelay
expr_stmt|;
block|}
DECL|method|isPreserveHeaders ()
specifier|public
name|boolean
name|isPreserveHeaders
parameter_list|()
block|{
return|return
name|preserveHeaders
return|;
block|}
comment|/**      * Should message headers be preserved when publishing messages.      * This will add the Camel headers to the Iron MQ message as a json payload with a header list, and a message body.      * Useful when Camel is both consumer and producer.      */
DECL|method|setPreserveHeaders (boolean preserveHeaders)
specifier|public
name|void
name|setPreserveHeaders
parameter_list|(
name|boolean
name|preserveHeaders
parameter_list|)
block|{
name|this
operator|.
name|preserveHeaders
operator|=
name|preserveHeaders
expr_stmt|;
block|}
DECL|method|isBatchDelete ()
specifier|public
name|boolean
name|isBatchDelete
parameter_list|()
block|{
return|return
name|batchDelete
return|;
block|}
comment|/**      * Should messages be deleted in one batch.       * This will limit the number of api requests since messages are deleted in one request, instead of one pr. exchange.       * If enabled care should be taken that the consumer is idempotent when processing exchanges.      */
DECL|method|setBatchDelete (boolean batchDelete)
specifier|public
name|void
name|setBatchDelete
parameter_list|(
name|boolean
name|batchDelete
parameter_list|)
block|{
name|this
operator|.
name|batchDelete
operator|=
name|batchDelete
expr_stmt|;
block|}
DECL|method|getWait ()
specifier|public
name|int
name|getWait
parameter_list|()
block|{
return|return
name|wait
return|;
block|}
comment|/**      * Time in seconds to wait for a message to become available.       * This enables long polling. Default is 0 (does not wait), maximum is 30.      */
DECL|method|setWait (int wait)
specifier|public
name|void
name|setWait
parameter_list|(
name|int
name|wait
parameter_list|)
block|{
name|this
operator|.
name|wait
operator|=
name|wait
expr_stmt|;
block|}
block|}
end_class

end_unit

