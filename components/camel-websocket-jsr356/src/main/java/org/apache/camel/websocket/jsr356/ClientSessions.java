begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.websocket.jsr356
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|websocket
operator|.
name|jsr356
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|io
operator|.
name|InputStream
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ArrayBlockingQueue
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
name|BlockingQueue
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
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BiConsumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|IntStream
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|websocket
operator|.
name|ClientEndpointConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|websocket
operator|.
name|CloseReason
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|websocket
operator|.
name|ContainerProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|websocket
operator|.
name|DeploymentException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|websocket
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|websocket
operator|.
name|EndpointConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|websocket
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|websocket
operator|.
name|WebSocketContainer
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
DECL|class|ClientSessions
class|class
name|ClientSessions
implements|implements
name|Closeable
block|{
DECL|field|log
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ClientSessions
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|expectedCount
specifier|private
specifier|final
name|int
name|expectedCount
decl_stmt|;
DECL|field|uri
specifier|private
specifier|final
name|URI
name|uri
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|ClientEndpointConfig
name|config
decl_stmt|;
DECL|field|container
specifier|private
specifier|final
name|WebSocketContainer
name|container
decl_stmt|;
DECL|field|sessions
specifier|private
specifier|final
name|BlockingQueue
argument_list|<
name|Session
argument_list|>
name|sessions
decl_stmt|;
DECL|field|closed
specifier|private
specifier|final
name|AtomicBoolean
name|closed
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
DECL|field|onMessage
specifier|private
specifier|final
name|BiConsumer
argument_list|<
name|Session
argument_list|,
name|Object
argument_list|>
name|onMessage
decl_stmt|;
DECL|method|ClientSessions (final int count, final URI uri, final ClientEndpointConfig config, final BiConsumer<Session, Object> onMessage)
name|ClientSessions
parameter_list|(
specifier|final
name|int
name|count
parameter_list|,
specifier|final
name|URI
name|uri
parameter_list|,
specifier|final
name|ClientEndpointConfig
name|config
parameter_list|,
specifier|final
name|BiConsumer
argument_list|<
name|Session
argument_list|,
name|Object
argument_list|>
name|onMessage
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
name|this
operator|.
name|expectedCount
operator|=
name|count
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|onMessage
operator|=
name|onMessage
expr_stmt|;
name|this
operator|.
name|sessions
operator|=
operator|new
name|ArrayBlockingQueue
argument_list|<>
argument_list|(
name|expectedCount
argument_list|)
expr_stmt|;
comment|// todo: grab it from the context?
name|this
operator|.
name|container
operator|=
name|ContainerProvider
operator|.
name|getWebSocketContainer
argument_list|()
expr_stmt|;
block|}
DECL|method|prepare ()
specifier|public
name|void
name|prepare
parameter_list|()
block|{
name|sessions
operator|.
name|addAll
argument_list|(
name|IntStream
operator|.
name|range
argument_list|(
literal|0
argument_list|,
name|expectedCount
argument_list|)
operator|.
name|mapToObj
argument_list|(
name|idx
lambda|->
name|doConnect
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|execute (final Consumer<Session> apply)
specifier|public
name|void
name|execute
parameter_list|(
specifier|final
name|Consumer
argument_list|<
name|Session
argument_list|>
name|apply
parameter_list|)
block|{
name|Session
name|session
init|=
literal|null
decl_stmt|;
try|try
block|{
name|session
operator|=
name|sessions
operator|.
name|take
argument_list|()
expr_stmt|;
name|apply
operator|.
name|accept
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|RuntimeException
name|re
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|re
operator|.
name|getMessage
argument_list|()
argument_list|,
name|re
argument_list|)
expr_stmt|;
if|if
condition|(
name|session
operator|.
name|isOpen
argument_list|()
condition|)
block|{
try|try
block|{
name|session
operator|.
name|close
argument_list|(
operator|new
name|CloseReason
argument_list|(
name|CloseReason
operator|.
name|CloseCodes
operator|.
name|CLOSED_ABNORMALLY
argument_list|,
name|re
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IOException
name|errorOnClose
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|errorOnClose
operator|.
name|getMessage
argument_list|()
argument_list|,
name|errorOnClose
argument_list|)
expr_stmt|;
block|}
block|}
name|session
operator|=
literal|null
expr_stmt|;
throw|throw
name|re
throw|;
block|}
catch|catch
parameter_list|(
specifier|final
name|InterruptedException
name|ex
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
block|}
finally|finally
block|{
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|sessions
operator|.
name|offer
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|doConnect ()
specifier|private
name|Session
name|doConnect
parameter_list|()
block|{
try|try
block|{
specifier|final
name|Session
name|session
init|=
name|container
operator|.
name|connectToServer
argument_list|(
operator|new
name|Endpoint
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onOpen
parameter_list|(
specifier|final
name|Session
name|session
parameter_list|,
specifier|final
name|EndpointConfig
name|endpointConfig
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Session opened #{}"
argument_list|,
name|session
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onClose
parameter_list|(
specifier|final
name|Session
name|session
parameter_list|,
specifier|final
name|CloseReason
name|closeReason
parameter_list|)
block|{
name|sessions
operator|.
name|remove
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Session closed #{}"
argument_list|,
name|session
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onError
parameter_list|(
specifier|final
name|Session
name|session
parameter_list|,
specifier|final
name|Throwable
name|throwable
parameter_list|)
block|{
if|if
condition|(
name|session
operator|.
name|isOpen
argument_list|()
condition|)
block|{
try|try
block|{
name|session
operator|.
name|close
argument_list|(
operator|new
name|CloseReason
argument_list|(
name|CloseReason
operator|.
name|CloseCodes
operator|.
name|CLOSED_ABNORMALLY
argument_list|,
literal|"an exception occured"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Error closing session #{}"
argument_list|,
name|session
operator|.
name|getId
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|sessions
operator|.
name|remove
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Error on session #{}"
argument_list|,
name|session
operator|.
name|getId
argument_list|()
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|closed
operator|.
name|get
argument_list|()
condition|)
block|{
comment|// try to repopulate it
name|sessions
operator|.
name|offer
argument_list|(
name|doConnect
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|,
name|config
argument_list|,
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|onMessage
operator|!=
literal|null
condition|)
block|{
name|session
operator|.
name|addMessageHandler
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|message
lambda|->
name|onMessage
operator|.
name|accept
argument_list|(
name|session
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|session
operator|.
name|addMessageHandler
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|message
lambda|->
name|onMessage
operator|.
name|accept
argument_list|(
name|session
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|session
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|DeploymentException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
name|closed
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sessions
operator|.
name|forEach
argument_list|(
name|it
lambda|->
block|{
if|if
condition|(
name|it
operator|.
name|isOpen
argument_list|()
condition|)
block|{
try|try
block|{
name|it
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|sessions
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

