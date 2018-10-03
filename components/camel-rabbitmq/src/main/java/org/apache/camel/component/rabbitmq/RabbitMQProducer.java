begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rabbitmq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rabbitmq
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
name|concurrent
operator|.
name|ExecutorService
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
name|RejectedExecutionException
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
name|ScheduledExecutorService
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
name|atomic
operator|.
name|AtomicBoolean
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|AMQP
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|Channel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|Connection
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
name|FailedToCreateProducerException
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
name|rabbitmq
operator|.
name|pool
operator|.
name|PoolableChannelFactory
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
name|rabbitmq
operator|.
name|reply
operator|.
name|ReplyManager
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
name|rabbitmq
operator|.
name|reply
operator|.
name|TemporaryQueueReplyManager
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
name|DefaultAsyncProducer
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
name|support
operator|.
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|pool
operator|.
name|ObjectPool
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|pool
operator|.
name|impl
operator|.
name|GenericObjectPool
import|;
end_import

begin_class
DECL|class|RabbitMQProducer
specifier|public
class|class
name|RabbitMQProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|GENERATED_CORRELATION_ID_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|GENERATED_CORRELATION_ID_PREFIX
init|=
literal|"Camel-"
decl_stmt|;
DECL|field|conn
specifier|private
name|Connection
name|conn
decl_stmt|;
DECL|field|channelPool
specifier|private
name|ObjectPool
argument_list|<
name|Channel
argument_list|>
name|channelPool
decl_stmt|;
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|closeTimeout
specifier|private
name|int
name|closeTimeout
init|=
literal|30
operator|*
literal|1000
decl_stmt|;
DECL|field|started
specifier|private
specifier|final
name|AtomicBoolean
name|started
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|replyManager
specifier|private
name|ReplyManager
name|replyManager
decl_stmt|;
DECL|method|RabbitMQProducer (RabbitMQEndpoint endpoint)
specifier|public
name|RabbitMQProducer
parameter_list|(
name|RabbitMQEndpoint
name|endpoint
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|RabbitMQEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|RabbitMQEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
comment|/**      * Channel callback (similar to Spring JDBC ConnectionCallback)      */
DECL|interface|ChannelCallback
specifier|private
interface|interface
name|ChannelCallback
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|doWithChannel (Channel channel)
name|T
name|doWithChannel
parameter_list|(
name|Channel
name|channel
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
comment|/**      * Do something with a pooled channel (similar to Spring JDBC TransactionTemplate#execute)      */
DECL|method|execute (ChannelCallback<T> callback)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|execute
parameter_list|(
name|ChannelCallback
argument_list|<
name|T
argument_list|>
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
name|Channel
name|channel
decl_stmt|;
try|try
block|{
name|channel
operator|=
name|channelPool
operator|.
name|borrowObject
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
comment|// Since this method is not synchronized its possible the
comment|// channelPool has been cleared by another thread
name|checkConnectionAndChannelPool
argument_list|()
expr_stmt|;
name|channel
operator|=
name|channelPool
operator|.
name|borrowObject
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|channel
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Got a closed channel from the pool. Invalidating and borrowing a new one from the pool."
argument_list|)
expr_stmt|;
name|channelPool
operator|.
name|invalidateObject
argument_list|(
name|channel
argument_list|)
expr_stmt|;
comment|// Reconnect if another thread hasn't yet
name|checkConnectionAndChannelPool
argument_list|()
expr_stmt|;
name|attemptDeclaration
argument_list|()
expr_stmt|;
name|channel
operator|=
name|channelPool
operator|.
name|borrowObject
argument_list|()
expr_stmt|;
block|}
try|try
block|{
return|return
name|callback
operator|.
name|doWithChannel
argument_list|(
name|channel
argument_list|)
return|;
block|}
finally|finally
block|{
name|channelPool
operator|.
name|returnObject
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Open connection and initialize channel pool      * @throws Exception      */
DECL|method|openConnectionAndChannelPool ()
specifier|private
specifier|synchronized
name|void
name|openConnectionAndChannelPool
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Creating connection..."
argument_list|)
expr_stmt|;
name|this
operator|.
name|conn
operator|=
name|getEndpoint
argument_list|()
operator|.
name|connect
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created connection: {}"
argument_list|,
name|conn
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Creating channel pool..."
argument_list|)
expr_stmt|;
name|channelPool
operator|=
operator|new
name|GenericObjectPool
argument_list|<>
argument_list|(
operator|new
name|PoolableChannelFactory
argument_list|(
name|this
operator|.
name|conn
argument_list|)
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getChannelPoolMaxSize
argument_list|()
argument_list|,
name|GenericObjectPool
operator|.
name|WHEN_EXHAUSTED_BLOCK
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getChannelPoolMaxWait
argument_list|()
argument_list|)
expr_stmt|;
name|attemptDeclaration
argument_list|()
expr_stmt|;
block|}
DECL|method|attemptDeclaration ()
specifier|private
specifier|synchronized
name|void
name|attemptDeclaration
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isDeclare
argument_list|()
condition|)
block|{
name|execute
argument_list|(
operator|new
name|ChannelCallback
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|doWithChannel
parameter_list|(
name|Channel
name|channel
parameter_list|)
throws|throws
name|Exception
block|{
name|getEndpoint
argument_list|()
operator|.
name|declareExchangeAndQueue
argument_list|(
name|channel
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will reconnect only if the connection is closed.      * @throws Exception      */
DECL|method|checkConnectionAndChannelPool ()
specifier|private
specifier|synchronized
name|void
name|checkConnectionAndChannelPool
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|this
operator|.
name|conn
operator|==
literal|null
operator|||
operator|!
name|this
operator|.
name|conn
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Reconnecting to RabbitMQ"
argument_list|)
expr_stmt|;
try|try
block|{
name|closeConnectionAndChannel
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// no op
block|}
name|openConnectionAndChannelPool
argument_list|()
expr_stmt|;
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
name|this
operator|.
name|executorService
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
literal|"CamelRabbitMQProducer["
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getQueue
argument_list|()
operator|+
literal|"]"
argument_list|)
expr_stmt|;
try|try
block|{
name|openConnectionAndChannelPool
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to create connection. It will attempt to connect again when publishing a message."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * If needed, close Connection and Channel      * @throws IOException      */
DECL|method|closeConnectionAndChannel ()
specifier|private
specifier|synchronized
name|void
name|closeConnectionAndChannel
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|channelPool
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|channelPool
operator|.
name|close
argument_list|()
expr_stmt|;
name|channelPool
operator|=
literal|null
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
name|IOException
argument_list|(
literal|"Error closing channelPool"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|conn
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Closing connection: {} with timeout: {} ms."
argument_list|,
name|conn
argument_list|,
name|closeTimeout
argument_list|)
expr_stmt|;
name|conn
operator|.
name|close
argument_list|(
name|closeTimeout
argument_list|)
expr_stmt|;
name|conn
operator|=
literal|null
expr_stmt|;
block|}
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
name|unInitReplyManager
argument_list|()
expr_stmt|;
name|closeConnectionAndChannel
argument_list|()
expr_stmt|;
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|executorService
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
comment|// deny processing if we are not started
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RejectedExecutionException
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// we cannot process so invoke callback
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
try|try
block|{
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
comment|// in out requires a bit more work than in only
return|return
name|processInOut
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
else|else
block|{
comment|// in only
return|return
name|processInOnly
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// must catch exception to ensure callback is invoked as expected
comment|// to let Camel error handling deal with this
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
DECL|method|processInOut (final Exchange exchange, final AsyncCallback callback)
specifier|protected
name|boolean
name|processInOut
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
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
name|initReplyManager
argument_list|()
expr_stmt|;
comment|// the request timeout can be overruled by a header otherwise the endpoint configured value is used
specifier|final
name|long
name|timeout
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|REQUEST_TIMEOUT
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getRequestTimeout
argument_list|()
argument_list|,
name|long
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|originalCorrelationId
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|CORRELATIONID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// we append the 'Camel-' prefix to know it was generated by us
name|String
name|correlationId
init|=
name|GENERATED_CORRELATION_ID_PREFIX
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
decl_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|CORRELATIONID
argument_list|,
name|correlationId
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|REPLY_TO
argument_list|,
name|replyManager
operator|.
name|getReplyTo
argument_list|()
argument_list|)
expr_stmt|;
comment|// remove the OVERRIDE header so it does not propagate
name|String
name|exchangeName
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|EXCHANGE_OVERRIDE_NAME
argument_list|)
decl_stmt|;
comment|// If it is BridgeEndpoint we should ignore the message header of EXCHANGE_OVERRIDE_NAME
if|if
condition|(
name|exchangeName
operator|==
literal|null
operator|||
name|getEndpoint
argument_list|()
operator|.
name|isBridgeEndpoint
argument_list|()
condition|)
block|{
name|exchangeName
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getExchangeName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Overriding header: {} detected sending message to exchange: {}"
argument_list|,
name|RabbitMQConstants
operator|.
name|EXCHANGE_OVERRIDE_NAME
argument_list|,
name|exchangeName
argument_list|)
expr_stmt|;
block|}
name|String
name|key
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|ROUTING_KEY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// we just need to make sure RoutingKey option take effect if it is not BridgeEndpoint
if|if
condition|(
name|key
operator|==
literal|null
operator|||
name|getEndpoint
argument_list|()
operator|.
name|isBridgeEndpoint
argument_list|()
condition|)
block|{
name|key
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getRoutingKey
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
name|getEndpoint
argument_list|()
operator|.
name|getRoutingKey
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|key
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|exchangeName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ExchangeName and RoutingKey is not provided in the endpoint: "
operator|+
name|getEndpoint
argument_list|()
argument_list|)
throw|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Registering reply for {}"
argument_list|,
name|correlationId
argument_list|)
expr_stmt|;
name|replyManager
operator|.
name|registerReply
argument_list|(
name|replyManager
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|,
name|originalCorrelationId
argument_list|,
name|correlationId
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
try|try
block|{
name|basicPublish
argument_list|(
name|exchange
argument_list|,
name|exchangeName
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|replyManager
operator|.
name|cancelCorrelationId
argument_list|(
name|correlationId
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// continue routing asynchronously (reply will be processed async when its received)
return|return
literal|false
return|;
block|}
DECL|method|processInOnly (Exchange exchange, AsyncCallback callback)
specifier|private
name|boolean
name|processInOnly
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
comment|// remove the OVERRIDE header so it does not propagate
name|String
name|exchangeName
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|EXCHANGE_OVERRIDE_NAME
argument_list|)
decl_stmt|;
comment|// If it is BridgeEndpoint we should ignore the message header of EXCHANGE_OVERRIDE_NAME
if|if
condition|(
name|exchangeName
operator|==
literal|null
operator|||
name|getEndpoint
argument_list|()
operator|.
name|isBridgeEndpoint
argument_list|()
condition|)
block|{
name|exchangeName
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getExchangeName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Overriding header: {} detected sending message to exchange: {}"
argument_list|,
name|RabbitMQConstants
operator|.
name|EXCHANGE_OVERRIDE_NAME
argument_list|,
name|exchangeName
argument_list|)
expr_stmt|;
block|}
name|String
name|key
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|ROUTING_KEY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// we just need to make sure RoutingKey option take effect if it is not BridgeEndpoint
if|if
condition|(
name|key
operator|==
literal|null
operator|||
name|getEndpoint
argument_list|()
operator|.
name|isBridgeEndpoint
argument_list|()
condition|)
block|{
name|key
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getRoutingKey
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
name|getEndpoint
argument_list|()
operator|.
name|getRoutingKey
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|key
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|exchangeName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ExchangeName and RoutingKey is not provided in the endpoint: "
operator|+
name|getEndpoint
argument_list|()
argument_list|)
throw|;
block|}
name|basicPublish
argument_list|(
name|exchange
argument_list|,
name|exchangeName
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**      * Send a message borrowing a channel from the pool.      */
DECL|method|basicPublish (final Exchange camelExchange, final String rabbitExchange, final String routingKey)
specifier|private
name|void
name|basicPublish
parameter_list|(
specifier|final
name|Exchange
name|camelExchange
parameter_list|,
specifier|final
name|String
name|rabbitExchange
parameter_list|,
specifier|final
name|String
name|routingKey
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|channelPool
operator|==
literal|null
condition|)
block|{
comment|// Open connection and channel lazily if another thread hasn't
name|checkConnectionAndChannelPool
argument_list|()
expr_stmt|;
block|}
name|execute
argument_list|(
operator|new
name|ChannelCallback
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|doWithChannel
parameter_list|(
name|Channel
name|channel
parameter_list|)
throws|throws
name|Exception
block|{
name|getEndpoint
argument_list|()
operator|.
name|publishExchangeToChannel
argument_list|(
name|camelExchange
argument_list|,
name|channel
argument_list|,
name|routingKey
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|buildProperties (Exchange exchange)
name|AMQP
operator|.
name|BasicProperties
operator|.
name|Builder
name|buildProperties
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getMessageConverter
argument_list|()
operator|.
name|buildProperties
argument_list|(
name|exchange
argument_list|)
return|;
block|}
DECL|method|getCloseTimeout ()
specifier|public
name|int
name|getCloseTimeout
parameter_list|()
block|{
return|return
name|closeTimeout
return|;
block|}
DECL|method|setCloseTimeout (int closeTimeout)
specifier|public
name|void
name|setCloseTimeout
parameter_list|(
name|int
name|closeTimeout
parameter_list|)
block|{
name|this
operator|.
name|closeTimeout
operator|=
name|closeTimeout
expr_stmt|;
block|}
DECL|method|initReplyManager ()
specifier|protected
name|void
name|initReplyManager
parameter_list|()
block|{
if|if
condition|(
operator|!
name|started
operator|.
name|get
argument_list|()
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|started
operator|.
name|get
argument_list|()
condition|)
block|{
return|return;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Starting reply manager"
argument_list|)
expr_stmt|;
comment|// must use the classloader from the application context when creating reply manager,
comment|// as it should inherit the classloader from app context and not the current which may be
comment|// a different classloader
name|ClassLoader
name|current
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|ClassLoader
name|ac
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getApplicationContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|ac
operator|!=
literal|null
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|ac
argument_list|)
expr_stmt|;
block|}
comment|// validate that replyToType and replyTo is configured accordingly
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getReplyToType
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// setting temporary with a fixed replyTo is not supported
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getReplyTo
argument_list|()
operator|!=
literal|null
operator|&&
name|getEndpoint
argument_list|()
operator|.
name|getReplyToType
argument_list|()
operator|.
name|equals
argument_list|(
name|ReplyToType
operator|.
name|Temporary
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ReplyToType "
operator|+
name|ReplyToType
operator|.
name|Temporary
operator|+
literal|" is not supported when replyTo "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getReplyTo
argument_list|()
operator|+
literal|" is also configured."
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getReplyTo
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// specifying reply queues is not currently supported
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Specifying replyTo "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getReplyTo
argument_list|()
operator|+
literal|" is currently not supported."
argument_list|)
throw|;
block|}
else|else
block|{
name|replyManager
operator|=
name|createReplyManager
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Using RabbitMQReplyManager: {} to process replies from temporary queue"
argument_list|,
name|replyManager
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FailedToCreateProducerException
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|ac
operator|!=
literal|null
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|current
argument_list|)
expr_stmt|;
block|}
block|}
name|started
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|unInitReplyManager ()
specifier|protected
name|void
name|unInitReplyManager
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|replyManager
operator|!=
literal|null
condition|)
block|{
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
literal|"Stopping RabbitMQReplyManager: {} from processing replies from: {}"
argument_list|,
name|replyManager
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getReplyTo
argument_list|()
operator|!=
literal|null
condition|?
name|getEndpoint
argument_list|()
operator|.
name|getReplyTo
argument_list|()
else|:
literal|"temporary queue"
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|replyManager
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|started
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createReplyManager ()
specifier|protected
name|ReplyManager
name|createReplyManager
parameter_list|()
throws|throws
name|Exception
block|{
comment|// use a temporary queue
name|ReplyManager
name|replyManager
init|=
operator|new
name|TemporaryQueueReplyManager
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|replyManager
operator|.
name|setEndpoint
argument_list|(
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|name
init|=
literal|"RabbitMQReplyManagerTimeoutChecker["
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getExchangeName
argument_list|()
operator|+
literal|"]"
decl_stmt|;
name|ScheduledExecutorService
name|replyManagerExecutorService
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadScheduledExecutor
argument_list|(
name|name
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|replyManager
operator|.
name|setScheduledExecutorService
argument_list|(
name|replyManagerExecutorService
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Staring ReplyManager: {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|replyManager
argument_list|)
expr_stmt|;
return|return
name|replyManager
return|;
block|}
block|}
end_class

end_unit

