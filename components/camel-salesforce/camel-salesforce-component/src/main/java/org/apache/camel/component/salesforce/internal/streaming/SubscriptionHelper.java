begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Service
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

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|client
operator|.
name|BayeuxClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|client
operator|.
name|transport
operator|.
name|ClientTransport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|client
operator|.
name|transport
operator|.
name|LongPollingTransport
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
name|client
operator|.
name|ContentExchange
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
name|client
operator|.
name|HttpClient
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
name|HttpHeaders
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
name|HttpSchemes
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
name|SalesforceComponent
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
name|SalesforceConsumer
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
name|SalesforceSession
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
name|SalesforceSecurityListener
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|CountDownLatch
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|MILLISECONDS
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|Channel
operator|.
name|*
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|Message
operator|.
name|ERROR_FIELD
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|Message
operator|.
name|SUBSCRIPTION_FIELD
import|;
end_import

begin_class
DECL|class|SubscriptionHelper
specifier|public
class|class
name|SubscriptionHelper
implements|implements
name|Service
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
name|SubscriptionHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|CONNECT_TIMEOUT
specifier|private
specifier|static
specifier|final
name|int
name|CONNECT_TIMEOUT
init|=
literal|110
decl_stmt|;
DECL|field|CHANNEL_TIMEOUT
specifier|private
specifier|static
specifier|final
name|int
name|CHANNEL_TIMEOUT
init|=
literal|40
decl_stmt|;
DECL|field|EXCEPTION_FIELD
specifier|private
specifier|static
specifier|final
name|String
name|EXCEPTION_FIELD
init|=
literal|"exception"
decl_stmt|;
DECL|field|component
specifier|private
specifier|final
name|SalesforceComponent
name|component
decl_stmt|;
DECL|field|session
specifier|private
specifier|final
name|SalesforceSession
name|session
decl_stmt|;
DECL|field|client
specifier|private
specifier|final
name|BayeuxClient
name|client
decl_stmt|;
DECL|field|listenerMap
specifier|private
specifier|final
name|Map
argument_list|<
name|SalesforceConsumer
argument_list|,
name|ClientSessionChannel
operator|.
name|MessageListener
argument_list|>
name|listenerMap
decl_stmt|;
DECL|field|started
specifier|private
name|boolean
name|started
decl_stmt|;
DECL|field|handshakeListener
specifier|private
name|ClientSessionChannel
operator|.
name|MessageListener
name|handshakeListener
decl_stmt|;
DECL|field|connectListener
specifier|private
name|ClientSessionChannel
operator|.
name|MessageListener
name|connectListener
decl_stmt|;
DECL|field|handshakeError
specifier|private
name|String
name|handshakeError
decl_stmt|;
DECL|field|handshakeException
specifier|private
name|Exception
name|handshakeException
decl_stmt|;
DECL|field|connectError
specifier|private
name|String
name|connectError
decl_stmt|;
DECL|field|reconnecting
specifier|private
name|boolean
name|reconnecting
decl_stmt|;
DECL|method|SubscriptionHelper (SalesforceComponent component)
specifier|public
name|SubscriptionHelper
parameter_list|(
name|SalesforceComponent
name|component
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
name|this
operator|.
name|session
operator|=
name|component
operator|.
name|getSession
argument_list|()
expr_stmt|;
name|this
operator|.
name|listenerMap
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|SalesforceConsumer
argument_list|,
name|ClientSessionChannel
operator|.
name|MessageListener
argument_list|>
argument_list|()
expr_stmt|;
comment|// create CometD client
name|this
operator|.
name|client
operator|=
name|createClient
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|started
condition|)
block|{
comment|// no need to start again
return|return;
block|}
comment|// listener for handshake error or exception
if|if
condition|(
name|handshakeListener
operator|==
literal|null
condition|)
block|{
comment|// first start
name|handshakeListener
operator|=
operator|new
name|ClientSessionChannel
operator|.
name|MessageListener
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|ClientSessionChannel
name|channel
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"[CHANNEL:META_HANDSHAKE]: {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|message
operator|.
name|isSuccessful
argument_list|()
condition|)
block|{
name|String
name|error
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|ERROR_FIELD
argument_list|)
decl_stmt|;
if|if
condition|(
name|error
operator|!=
literal|null
condition|)
block|{
name|handshakeError
operator|=
name|error
expr_stmt|;
block|}
name|Exception
name|exception
init|=
operator|(
name|Exception
operator|)
name|message
operator|.
name|get
argument_list|(
name|EXCEPTION_FIELD
argument_list|)
decl_stmt|;
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
name|handshakeException
operator|=
name|exception
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|listenerMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|reconnecting
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
block|}
name|client
operator|.
name|getChannel
argument_list|(
name|META_HANDSHAKE
argument_list|)
operator|.
name|addListener
argument_list|(
name|handshakeListener
argument_list|)
expr_stmt|;
comment|// listener for connect error
if|if
condition|(
name|connectListener
operator|==
literal|null
condition|)
block|{
name|connectListener
operator|=
operator|new
name|ClientSessionChannel
operator|.
name|MessageListener
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|ClientSessionChannel
name|channel
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"[CHANNEL:META_CONNECT]: {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|message
operator|.
name|isSuccessful
argument_list|()
condition|)
block|{
name|String
name|error
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|ERROR_FIELD
argument_list|)
decl_stmt|;
if|if
condition|(
name|error
operator|!=
literal|null
condition|)
block|{
name|connectError
operator|=
name|error
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|reconnecting
condition|)
block|{
name|reconnecting
operator|=
literal|false
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Refreshing subscriptions to {} channels on reconnect"
argument_list|,
name|listenerMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// reconnected to Salesforce, subscribe to existing channels
specifier|final
name|Map
argument_list|<
name|SalesforceConsumer
argument_list|,
name|ClientSessionChannel
operator|.
name|MessageListener
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|SalesforceConsumer
argument_list|,
name|ClientSessionChannel
operator|.
name|MessageListener
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|putAll
argument_list|(
name|listenerMap
argument_list|)
expr_stmt|;
name|listenerMap
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|SalesforceConsumer
argument_list|,
name|ClientSessionChannel
operator|.
name|MessageListener
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
specifier|final
name|SalesforceConsumer
name|consumer
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
specifier|final
name|String
name|topicName
init|=
name|consumer
operator|.
name|getTopicName
argument_list|()
decl_stmt|;
try|try
block|{
name|subscribe
argument_list|(
name|topicName
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelException
name|e
parameter_list|)
block|{
comment|// let the consumer handle the exception
name|consumer
operator|.
name|handleException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error refreshing subscription to topic [%s]: %s"
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
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
expr_stmt|;
block|}
name|client
operator|.
name|getChannel
argument_list|(
name|META_CONNECT
argument_list|)
operator|.
name|addListener
argument_list|(
name|connectListener
argument_list|)
expr_stmt|;
comment|// connect to Salesforce cometd endpoint
name|client
operator|.
name|handshake
argument_list|()
expr_stmt|;
specifier|final
name|long
name|waitMs
init|=
name|MILLISECONDS
operator|.
name|convert
argument_list|(
name|CONNECT_TIMEOUT
argument_list|,
name|SECONDS
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|client
operator|.
name|waitFor
argument_list|(
name|waitMs
argument_list|,
name|BayeuxClient
operator|.
name|State
operator|.
name|CONNECTED
argument_list|)
condition|)
block|{
if|if
condition|(
name|handshakeException
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Exception during HANDSHAKE: %s"
argument_list|,
name|handshakeException
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|handshakeException
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|handshakeError
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error during HANDSHAKE: %s"
argument_list|,
name|handshakeError
argument_list|)
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|connectError
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error during CONNECT: %s"
argument_list|,
name|connectError
argument_list|)
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Handshake request timeout after %s seconds"
argument_list|,
name|CONNECT_TIMEOUT
argument_list|)
argument_list|)
throw|;
block|}
block|}
name|started
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
if|if
condition|(
name|started
condition|)
block|{
name|started
operator|=
literal|false
expr_stmt|;
comment|// TODO find and log any disconnect errors
name|client
operator|.
name|disconnect
argument_list|()
expr_stmt|;
name|client
operator|.
name|getChannel
argument_list|(
name|META_CONNECT
argument_list|)
operator|.
name|removeListener
argument_list|(
name|connectListener
argument_list|)
expr_stmt|;
name|client
operator|.
name|getChannel
argument_list|(
name|META_HANDSHAKE
argument_list|)
operator|.
name|removeListener
argument_list|(
name|handshakeListener
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createClient ()
specifier|private
name|BayeuxClient
name|createClient
parameter_list|()
throws|throws
name|Exception
block|{
comment|// use default Jetty client from SalesforceComponent, its shared by all consumers
specifier|final
name|HttpClient
name|httpClient
init|=
name|component
operator|.
name|getConfig
argument_list|()
operator|.
name|getHttpClient
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
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
name|options
operator|.
name|put
argument_list|(
name|ClientTransport
operator|.
name|TIMEOUT_OPTION
argument_list|,
name|httpClient
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
comment|// check login access token
if|if
condition|(
name|session
operator|.
name|getAccessToken
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// lazy login here!
name|session
operator|.
name|login
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|LongPollingTransport
name|transport
init|=
operator|new
name|LongPollingTransport
argument_list|(
name|options
argument_list|,
name|httpClient
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|customize
parameter_list|(
name|ContentExchange
name|exchange
parameter_list|)
block|{
name|super
operator|.
name|customize
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// add SalesforceSecurityListener to handle token expiry
specifier|final
name|String
name|accessToken
init|=
name|session
operator|.
name|getAccessToken
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|boolean
name|isHttps
init|=
name|HttpSchemes
operator|.
name|HTTPS
operator|.
name|equals
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|exchange
operator|.
name|getScheme
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setEventListener
argument_list|(
operator|new
name|SalesforceSecurityListener
argument_list|(
name|httpClient
operator|.
name|getDestination
argument_list|(
name|exchange
operator|.
name|getAddress
argument_list|()
argument_list|,
name|isHttps
argument_list|)
argument_list|,
name|exchange
argument_list|,
name|session
argument_list|,
name|accessToken
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error adding SalesforceSecurityListener to exchange %s"
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
comment|// add current security token obtained from session
name|exchange
operator|.
name|addRequestHeader
argument_list|(
name|HttpHeaders
operator|.
name|AUTHORIZATION
argument_list|,
literal|"OAuth "
operator|+
name|accessToken
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|BayeuxClient
name|client
init|=
operator|new
name|BayeuxClient
argument_list|(
name|getEndpointUrl
argument_list|()
argument_list|,
name|transport
argument_list|)
decl_stmt|;
name|client
operator|.
name|setDebugEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|client
return|;
block|}
DECL|method|subscribe (final String topicName, final SalesforceConsumer consumer)
specifier|public
name|void
name|subscribe
parameter_list|(
specifier|final
name|String
name|topicName
parameter_list|,
specifier|final
name|SalesforceConsumer
name|consumer
parameter_list|)
throws|throws
name|CamelException
block|{
comment|// create subscription for consumer
specifier|final
name|String
name|channelName
init|=
name|getChannelName
argument_list|(
name|topicName
argument_list|)
decl_stmt|;
comment|// channel message listener
name|LOG
operator|.
name|info
argument_list|(
literal|"Subscribing to channel {}..."
argument_list|,
name|channelName
argument_list|)
expr_stmt|;
specifier|final
name|ClientSessionChannel
operator|.
name|MessageListener
name|listener
init|=
operator|new
name|ClientSessionChannel
operator|.
name|MessageListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onMessage
parameter_list|(
name|ClientSessionChannel
name|channel
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received Message: {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
comment|// convert CometD message to Camel Message
name|consumer
operator|.
name|processMessage
argument_list|(
name|channel
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
specifier|final
name|ClientSessionChannel
name|clientChannel
init|=
name|client
operator|.
name|getChannel
argument_list|(
name|channelName
argument_list|)
decl_stmt|;
comment|// listener for subscribe error
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|String
index|[]
name|subscribeError
init|=
block|{
literal|null
block|}
decl_stmt|;
specifier|final
name|ClientSessionChannel
operator|.
name|MessageListener
name|subscriptionListener
init|=
operator|new
name|ClientSessionChannel
operator|.
name|MessageListener
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|ClientSessionChannel
name|channel
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"[CHANNEL:META_SUBSCRIBE]: {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
specifier|final
name|String
name|subscribedChannelName
init|=
name|message
operator|.
name|get
argument_list|(
name|SUBSCRIPTION_FIELD
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|channelName
operator|.
name|equals
argument_list|(
name|subscribedChannelName
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|message
operator|.
name|isSuccessful
argument_list|()
condition|)
block|{
name|String
name|error
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|ERROR_FIELD
argument_list|)
decl_stmt|;
if|if
condition|(
name|error
operator|!=
literal|null
condition|)
block|{
name|subscribeError
index|[
literal|0
index|]
operator|=
name|error
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// remember subscription
name|LOG
operator|.
name|info
argument_list|(
literal|"Subscribed to channel {}"
argument_list|,
name|subscribedChannelName
argument_list|)
expr_stmt|;
block|}
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|client
operator|.
name|getChannel
argument_list|(
name|META_SUBSCRIBE
argument_list|)
operator|.
name|addListener
argument_list|(
name|subscriptionListener
argument_list|)
expr_stmt|;
try|try
block|{
name|clientChannel
operator|.
name|subscribe
argument_list|(
name|listener
argument_list|)
expr_stmt|;
comment|// confirm that a subscription was created
try|try
block|{
if|if
condition|(
operator|!
name|latch
operator|.
name|await
argument_list|(
name|CHANNEL_TIMEOUT
argument_list|,
name|SECONDS
argument_list|)
condition|)
block|{
name|String
name|message
decl_stmt|;
if|if
condition|(
name|subscribeError
index|[
literal|0
index|]
operator|!=
literal|null
condition|)
block|{
name|message
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"Error subscribing to topic %s: %s"
argument_list|,
name|topicName
argument_list|,
name|subscribeError
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"Timeout error subscribing to topic %s after %s seconds"
argument_list|,
name|topicName
argument_list|,
name|CHANNEL_TIMEOUT
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|CamelException
argument_list|(
name|message
argument_list|)
throw|;
block|}
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
comment|// probably shutting down, so forget subscription
block|}
name|listenerMap
operator|.
name|put
argument_list|(
name|consumer
argument_list|,
name|listener
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|client
operator|.
name|getChannel
argument_list|(
name|META_SUBSCRIBE
argument_list|)
operator|.
name|removeListener
argument_list|(
name|subscriptionListener
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getChannelName (String topicName)
specifier|private
name|String
name|getChannelName
parameter_list|(
name|String
name|topicName
parameter_list|)
block|{
return|return
literal|"/topic/"
operator|+
name|topicName
return|;
block|}
DECL|method|unsubscribe (String topicName, SalesforceConsumer consumer)
specifier|public
name|void
name|unsubscribe
parameter_list|(
name|String
name|topicName
parameter_list|,
name|SalesforceConsumer
name|consumer
parameter_list|)
throws|throws
name|CamelException
block|{
comment|// channel name
specifier|final
name|String
name|channelName
init|=
name|getChannelName
argument_list|(
name|topicName
argument_list|)
decl_stmt|;
comment|// listen for unsubscribe error
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|String
index|[]
name|unsubscribeError
init|=
block|{
literal|null
block|}
decl_stmt|;
specifier|final
name|ClientSessionChannel
operator|.
name|MessageListener
name|unsubscribeListener
init|=
operator|new
name|ClientSessionChannel
operator|.
name|MessageListener
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|ClientSessionChannel
name|channel
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"[CHANNEL:META_UNSUBSCRIBE]: {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|String
name|unsubscribedChannelName
init|=
name|message
operator|.
name|get
argument_list|(
name|SUBSCRIPTION_FIELD
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|channelName
operator|.
name|equals
argument_list|(
name|unsubscribedChannelName
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|message
operator|.
name|isSuccessful
argument_list|()
condition|)
block|{
name|String
name|error
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|ERROR_FIELD
argument_list|)
decl_stmt|;
if|if
condition|(
name|error
operator|!=
literal|null
condition|)
block|{
name|unsubscribeError
index|[
literal|0
index|]
operator|=
name|error
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// forget subscription
name|LOG
operator|.
name|info
argument_list|(
literal|"Unsubscribed from channel {}"
argument_list|,
name|unsubscribedChannelName
argument_list|)
expr_stmt|;
block|}
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|client
operator|.
name|getChannel
argument_list|(
name|META_UNSUBSCRIBE
argument_list|)
operator|.
name|addListener
argument_list|(
name|unsubscribeListener
argument_list|)
expr_stmt|;
try|try
block|{
comment|// unsubscribe from channel
specifier|final
name|ClientSessionChannel
operator|.
name|MessageListener
name|listener
init|=
name|listenerMap
operator|.
name|remove
argument_list|(
name|consumer
argument_list|)
decl_stmt|;
if|if
condition|(
name|listener
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Unsubscribing from channel {}..."
argument_list|,
name|channelName
argument_list|)
expr_stmt|;
specifier|final
name|ClientSessionChannel
name|clientChannel
init|=
name|client
operator|.
name|getChannel
argument_list|(
name|channelName
argument_list|)
decl_stmt|;
name|clientChannel
operator|.
name|unsubscribe
argument_list|(
name|listener
argument_list|)
expr_stmt|;
comment|// confirm unsubscribe
try|try
block|{
if|if
condition|(
operator|!
name|latch
operator|.
name|await
argument_list|(
name|CHANNEL_TIMEOUT
argument_list|,
name|SECONDS
argument_list|)
condition|)
block|{
name|String
name|message
decl_stmt|;
if|if
condition|(
name|unsubscribeError
index|[
literal|0
index|]
operator|!=
literal|null
condition|)
block|{
name|message
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"Error unsubscribing from topic %s: %s"
argument_list|,
name|topicName
argument_list|,
name|unsubscribeError
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"Timeout error unsubscribing from topic %s after %s seconds"
argument_list|,
name|topicName
argument_list|,
name|CHANNEL_TIMEOUT
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|CamelException
argument_list|(
name|message
argument_list|)
throw|;
block|}
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
comment|// probably shutting down, forget unsubscribe and return
block|}
block|}
block|}
finally|finally
block|{
name|client
operator|.
name|getChannel
argument_list|(
name|META_UNSUBSCRIBE
argument_list|)
operator|.
name|removeListener
argument_list|(
name|unsubscribeListener
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getEndpointUrl ()
specifier|public
name|String
name|getEndpointUrl
parameter_list|()
block|{
return|return
name|component
operator|.
name|getSession
argument_list|()
operator|.
name|getInstanceUrl
argument_list|()
operator|+
literal|"/cometd/"
operator|+
name|component
operator|.
name|getConfig
argument_list|()
operator|.
name|getApiVersion
argument_list|()
return|;
block|}
block|}
end_class

end_unit

