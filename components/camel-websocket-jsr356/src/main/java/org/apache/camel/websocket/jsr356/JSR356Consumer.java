begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|function
operator|.
name|BiConsumer
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Optional
operator|.
name|ofNullable
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
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|websocket
operator|.
name|server
operator|.
name|ServerEndpointConfig
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
name|impl
operator|.
name|DefaultConsumer
import|;
end_import

begin_class
DECL|class|JSR356Consumer
specifier|public
class|class
name|JSR356Consumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|sessionCount
specifier|private
specifier|final
name|int
name|sessionCount
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|String
name|context
decl_stmt|;
DECL|field|manager
specifier|private
name|ClientSessions
name|manager
decl_stmt|;
DECL|field|closeTask
specifier|private
name|Runnable
name|closeTask
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
init|=
parameter_list|(
name|session
parameter_list|,
name|message
parameter_list|)
lambda|->
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|JSR356Constants
operator|.
name|SESSION
argument_list|,
name|session
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|doneSync
lambda|->
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
decl_stmt|;
empty_stmt|;
DECL|method|JSR356Consumer (final JSR356Endpoint jsr356Endpoint, final Processor processor, final int sessionCount, final String context)
name|JSR356Consumer
parameter_list|(
specifier|final
name|JSR356Endpoint
name|jsr356Endpoint
parameter_list|,
specifier|final
name|Processor
name|processor
parameter_list|,
specifier|final
name|int
name|sessionCount
parameter_list|,
specifier|final
name|String
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|jsr356Endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|sessionCount
operator|=
name|sessionCount
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|JSR356Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|JSR356Endpoint
operator|.
name|class
operator|.
name|cast
argument_list|(
name|super
operator|.
name|getEndpoint
argument_list|()
argument_list|)
return|;
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
name|String
name|endpointKey
init|=
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
operator|.
name|substring
argument_list|(
literal|"websocket-jsr356://"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpointKey
operator|.
name|contains
argument_list|(
literal|"://"
argument_list|)
condition|)
block|{
comment|// we act as a client
specifier|final
name|ClientEndpointConfig
operator|.
name|Builder
name|clientConfig
init|=
name|ClientEndpointConfig
operator|.
name|Builder
operator|.
name|create
argument_list|()
decl_stmt|;
comment|// todo:
comment|// config
name|manager
operator|=
operator|new
name|ClientSessions
argument_list|(
name|sessionCount
argument_list|,
name|URI
operator|.
name|create
argument_list|(
name|endpointKey
argument_list|)
argument_list|,
name|clientConfig
operator|.
name|build
argument_list|()
argument_list|,
name|onMessage
argument_list|)
expr_stmt|;
name|manager
operator|.
name|prepare
argument_list|()
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|JSR356WebSocketComponent
operator|.
name|ContextBag
name|bag
init|=
name|JSR356WebSocketComponent
operator|.
name|getContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|CamelServerEndpoint
name|endpoint
init|=
name|bag
operator|.
name|getEndpoints
argument_list|()
operator|.
name|get
argument_list|(
name|endpointKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
comment|// todo: make it customizable (the endpoint config)
specifier|final
name|ServerEndpointConfig
operator|.
name|Builder
name|configBuilder
init|=
name|ServerEndpointConfig
operator|.
name|Builder
operator|.
name|create
argument_list|(
name|CamelServerEndpoint
operator|.
name|class
argument_list|,
name|endpointKey
argument_list|)
decl_stmt|;
specifier|final
name|CamelServerEndpoint
name|serverEndpoint
init|=
operator|new
name|CamelServerEndpoint
argument_list|()
decl_stmt|;
name|bag
operator|.
name|getEndpoints
argument_list|()
operator|.
name|put
argument_list|(
name|endpointKey
argument_list|,
name|serverEndpoint
argument_list|)
expr_stmt|;
name|closeTask
operator|=
name|addObserver
argument_list|(
name|serverEndpoint
argument_list|)
expr_stmt|;
name|configBuilder
operator|.
name|configurator
argument_list|(
operator|new
name|ServerEndpointConfig
operator|.
name|Configurator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getEndpointInstance
parameter_list|(
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|serverEndpoint
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|bag
operator|.
name|getContainer
argument_list|()
operator|.
name|addEndpoint
argument_list|(
name|configBuilder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|closeTask
operator|=
name|addObserver
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|addObserver (final CamelServerEndpoint endpoint)
specifier|private
name|Runnable
name|addObserver
parameter_list|(
specifier|final
name|CamelServerEndpoint
name|endpoint
parameter_list|)
block|{
name|endpoint
operator|.
name|getEndpoints
argument_list|()
operator|.
name|add
argument_list|(
name|onMessage
argument_list|)
expr_stmt|;
return|return
parameter_list|()
lambda|->
name|endpoint
operator|.
name|getEndpoints
argument_list|()
operator|.
name|remove
argument_list|(
name|onMessage
argument_list|)
return|;
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
name|ofNullable
argument_list|(
name|manager
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|ClientSessions
operator|::
name|close
argument_list|)
expr_stmt|;
name|ofNullable
argument_list|(
name|closeTask
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|Runnable
operator|::
name|run
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

