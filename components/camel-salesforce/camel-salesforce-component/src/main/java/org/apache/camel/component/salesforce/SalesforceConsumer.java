begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce
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
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|RuntimeCamelException
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
name|DefaultRestClient
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
name|streaming
operator|.
name|PushTopicHelper
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
name|streaming
operator|.
name|SubscriptionHelper
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
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|client
operator|.
name|ClientSessionChannel
import|;
end_import

begin_comment
comment|/**  * The Salesforce consumer.  */
end_comment

begin_class
DECL|class|SalesforceConsumer
specifier|public
class|class
name|SalesforceConsumer
extends|extends
name|DefaultConsumer
block|{
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
DECL|field|EVENT_PROPERTY
specifier|private
specifier|static
specifier|final
name|String
name|EVENT_PROPERTY
init|=
literal|"event"
decl_stmt|;
DECL|field|TYPE_PROPERTY
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_PROPERTY
init|=
literal|"type"
decl_stmt|;
DECL|field|CREATED_DATE_PROPERTY
specifier|private
specifier|static
specifier|final
name|String
name|CREATED_DATE_PROPERTY
init|=
literal|"createdDate"
decl_stmt|;
DECL|field|SOBJECT_PROPERTY
specifier|private
specifier|static
specifier|final
name|String
name|SOBJECT_PROPERTY
init|=
literal|"sobject"
decl_stmt|;
DECL|field|REPLAY_ID_PROPERTY
specifier|private
specifier|static
specifier|final
name|String
name|REPLAY_ID_PROPERTY
init|=
literal|"replayId"
decl_stmt|;
DECL|field|MINIMUM_VERSION
specifier|private
specifier|static
specifier|final
name|double
name|MINIMUM_VERSION
init|=
literal|24.0
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|SalesforceEndpoint
name|endpoint
decl_stmt|;
DECL|field|subscriptionHelper
specifier|private
specifier|final
name|SubscriptionHelper
name|subscriptionHelper
decl_stmt|;
DECL|field|objectMapper
specifier|private
specifier|final
name|ObjectMapper
name|objectMapper
decl_stmt|;
DECL|field|topicName
specifier|private
specifier|final
name|String
name|topicName
decl_stmt|;
DECL|field|sObjectClass
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|sObjectClass
decl_stmt|;
DECL|field|subscribed
specifier|private
name|boolean
name|subscribed
decl_stmt|;
DECL|method|SalesforceConsumer (SalesforceEndpoint endpoint, Processor processor, SubscriptionHelper helper)
specifier|public
name|SalesforceConsumer
parameter_list|(
name|SalesforceEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|SubscriptionHelper
name|helper
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
name|ObjectMapper
name|configuredObjectMapper
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getObjectMapper
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuredObjectMapper
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|objectMapper
operator|=
name|configuredObjectMapper
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|objectMapper
operator|=
name|OBJECT_MAPPER
expr_stmt|;
block|}
comment|// check minimum supported API version
if|if
condition|(
name|Double
operator|.
name|valueOf
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getApiVersion
argument_list|()
argument_list|)
operator|<
name|MINIMUM_VERSION
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Minimum supported API version for consumer endpoints is "
operator|+
literal|24.0
argument_list|)
throw|;
block|}
name|this
operator|.
name|topicName
operator|=
name|endpoint
operator|.
name|getTopicName
argument_list|()
expr_stmt|;
name|this
operator|.
name|subscriptionHelper
operator|=
name|helper
expr_stmt|;
comment|// get sObjectClass to convert to
specifier|final
name|String
name|sObjectName
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSObjectName
argument_list|()
decl_stmt|;
if|if
condition|(
name|sObjectName
operator|!=
literal|null
condition|)
block|{
name|sObjectClass
operator|=
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|getClassMap
argument_list|()
operator|.
name|get
argument_list|(
name|sObjectName
argument_list|)
expr_stmt|;
if|if
condition|(
name|sObjectClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"SObject Class not found for %s"
argument_list|,
name|sObjectName
argument_list|)
argument_list|)
throw|;
block|}
block|}
else|else
block|{
specifier|final
name|String
name|className
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSObjectClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|className
operator|!=
literal|null
condition|)
block|{
name|sObjectClass
operator|=
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|className
argument_list|)
expr_stmt|;
if|if
condition|(
name|sObjectClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"SObject Class not found %s"
argument_list|,
name|className
argument_list|)
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Property sObjectName or sObjectClass NOT set, messages will be of type java.lang.Map"
argument_list|)
expr_stmt|;
name|sObjectClass
operator|=
literal|null
expr_stmt|;
block|}
block|}
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
specifier|final
name|SalesforceEndpointConfig
name|config
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
comment|// is a query configured in the endpoint?
if|if
condition|(
name|config
operator|.
name|getSObjectQuery
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Note that we don't lookup topic if the query is not specified
comment|// create REST client for PushTopic operations
name|SalesforceComponent
name|component
init|=
name|endpoint
operator|.
name|getComponent
argument_list|()
decl_stmt|;
name|RestClient
name|restClient
init|=
operator|new
name|DefaultRestClient
argument_list|(
name|component
operator|.
name|getConfig
argument_list|()
operator|.
name|getHttpClient
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getApiVersion
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFormat
argument_list|()
argument_list|,
name|component
operator|.
name|getSession
argument_list|()
argument_list|)
decl_stmt|;
comment|// don't forget to start the client
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|restClient
argument_list|)
expr_stmt|;
try|try
block|{
name|PushTopicHelper
name|helper
init|=
operator|new
name|PushTopicHelper
argument_list|(
name|config
argument_list|,
name|topicName
argument_list|,
name|restClient
argument_list|)
decl_stmt|;
name|helper
operator|.
name|createOrUpdateTopic
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
comment|// don't forget to stop the client
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|restClient
argument_list|)
expr_stmt|;
block|}
block|}
comment|// subscribe to topic
name|subscriptionHelper
operator|.
name|subscribe
argument_list|(
name|topicName
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|subscribed
operator|=
literal|true
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
if|if
condition|(
name|subscribed
condition|)
block|{
name|subscribed
operator|=
literal|false
expr_stmt|;
comment|// unsubscribe from topic
name|subscriptionHelper
operator|.
name|unsubscribe
argument_list|(
name|topicName
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processMessage (ClientSessionChannel channel, Message message)
specifier|public
name|void
name|processMessage
parameter_list|(
name|ClientSessionChannel
name|channel
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|setHeaders
argument_list|(
name|in
argument_list|,
name|message
argument_list|)
expr_stmt|;
comment|// get event data
comment|// TODO do we need to add NPE checks for message/data.get***???
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|data
init|=
name|message
operator|.
name|getDataAsMap
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|event
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|data
operator|.
name|get
argument_list|(
name|EVENT_PROPERTY
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|eventType
init|=
name|event
operator|.
name|get
argument_list|(
name|TYPE_PROPERTY
argument_list|)
decl_stmt|;
name|Object
name|createdDate
init|=
name|event
operator|.
name|get
argument_list|(
name|CREATED_DATE_PROPERTY
argument_list|)
decl_stmt|;
name|Object
name|replayId
init|=
name|event
operator|.
name|get
argument_list|(
name|REPLAY_ID_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Received event %s on channel %s created on %s"
argument_list|,
name|eventType
argument_list|,
name|channel
operator|.
name|getChannelId
argument_list|()
argument_list|,
name|createdDate
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|in
operator|.
name|setHeader
argument_list|(
literal|"CamelSalesforceEventType"
argument_list|,
name|eventType
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
literal|"CamelSalesforceCreatedDate"
argument_list|,
name|createdDate
argument_list|)
expr_stmt|;
if|if
condition|(
name|replayId
operator|!=
literal|null
condition|)
block|{
name|in
operator|.
name|setHeader
argument_list|(
literal|"CamelSalesforceReplayId"
argument_list|,
name|replayId
argument_list|)
expr_stmt|;
block|}
comment|// get SObject
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|sObject
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|data
operator|.
name|get
argument_list|(
name|SOBJECT_PROPERTY
argument_list|)
decl_stmt|;
try|try
block|{
specifier|final
name|String
name|sObjectString
init|=
name|objectMapper
operator|.
name|writeValueAsString
argument_list|(
name|sObject
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Received SObject: {}"
argument_list|,
name|sObjectString
argument_list|)
expr_stmt|;
if|if
condition|(
name|sObjectClass
operator|==
literal|null
condition|)
block|{
comment|// return sobject map as exchange body
name|in
operator|.
name|setBody
argument_list|(
name|sObject
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// create the expected SObject
name|in
operator|.
name|setBody
argument_list|(
name|objectMapper
operator|.
name|readValue
argument_list|(
operator|new
name|StringReader
argument_list|(
name|sObjectString
argument_list|)
argument_list|,
name|sObjectClass
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
specifier|final
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Error parsing message [%s] from Topic %s: %s"
argument_list|,
name|message
argument_list|,
name|topicName
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|handleException
argument_list|(
name|msg
argument_list|,
operator|new
name|RuntimeCamelException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
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
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// noop
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Done processing event: {} {}"
argument_list|,
name|eventType
operator|.
name|toString
argument_list|()
argument_list|,
name|doneSync
condition|?
literal|"synchronously"
else|:
literal|"asynchronously"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error processing %s: %s"
argument_list|,
name|exchange
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Exception
name|ex
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
name|handleException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unhandled exception: %s"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|setHeaders (org.apache.camel.Message in, Message message)
specifier|private
name|void
name|setHeaders
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|in
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// set topic name
name|headers
operator|.
name|put
argument_list|(
literal|"CamelSalesforceTopicName"
argument_list|,
name|topicName
argument_list|)
expr_stmt|;
comment|// set message properties as headers
name|headers
operator|.
name|put
argument_list|(
literal|"CamelSalesforceChannel"
argument_list|,
name|message
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"CamelSalesforceClientId"
argument_list|,
name|message
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeaders
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handleException (String message, Throwable t)
specifier|public
name|void
name|handleException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|super
operator|.
name|handleException
argument_list|(
name|message
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
DECL|method|getTopicName ()
specifier|public
name|String
name|getTopicName
parameter_list|()
block|{
return|return
name|topicName
return|;
block|}
block|}
end_class

end_unit

