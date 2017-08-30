begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atmosphere.websocket
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atmosphere
operator|.
name|websocket
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
name|HashMap
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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|component
operator|.
name|servlet
operator|.
name|ServletConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereResponseImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereRequestImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|ApplicationConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereFramework
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereFrameworkInitializer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|websocket
operator|.
name|WebSocketProtocol
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|WebsocketConsumer
specifier|public
class|class
name|WebsocketConsumer
extends|extends
name|ServletConsumer
block|{
DECL|field|enableEventsResending
specifier|private
name|boolean
name|enableEventsResending
decl_stmt|;
DECL|field|queryMap
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|framework
specifier|private
name|AtmosphereFramework
name|framework
decl_stmt|;
DECL|field|initializer
specifier|private
specifier|final
name|AtmosphereFrameworkInitializer
name|initializer
decl_stmt|;
DECL|method|configureEventsResending (final boolean enableEventsResending)
specifier|public
name|void
name|configureEventsResending
parameter_list|(
specifier|final
name|boolean
name|enableEventsResending
parameter_list|)
block|{
name|this
operator|.
name|enableEventsResending
operator|=
name|enableEventsResending
expr_stmt|;
block|}
DECL|method|configureFramework (ServletConfig config)
specifier|public
name|void
name|configureFramework
parameter_list|(
name|ServletConfig
name|config
parameter_list|)
throws|throws
name|ServletException
block|{
name|initializer
operator|.
name|configureFramework
argument_list|(
name|config
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|AtmosphereFramework
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|framework
operator|=
name|initializer
operator|.
name|framework
argument_list|()
expr_stmt|;
name|this
operator|.
name|framework
operator|.
name|setUseNativeImplementation
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|framework
operator|.
name|addInitParameter
argument_list|(
name|ApplicationConfig
operator|.
name|WEBSOCKET_SUPPORT
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|this
operator|.
name|framework
operator|.
name|addInitParameter
argument_list|(
name|ApplicationConfig
operator|.
name|WEBSOCKET_PROTOCOL
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|isUseStreaming
argument_list|()
condition|?
name|WebsocketStreamHandler
operator|.
name|class
operator|.
name|getName
argument_list|()
else|:
name|WebsocketHandler
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|framework
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|WebSocketProtocol
name|wsp
init|=
name|this
operator|.
name|framework
operator|.
name|getWebSocketProtocol
argument_list|()
decl_stmt|;
if|if
condition|(
name|wsp
operator|instanceof
name|WebsocketHandler
condition|)
block|{
operator|(
operator|(
name|WebsocketHandler
operator|)
name|wsp
operator|)
operator|.
name|setConsumer
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unexpected WebSocketHandler: "
operator|+
name|wsp
argument_list|)
throw|;
block|}
block|}
DECL|method|WebsocketConsumer (WebsocketEndpoint endpoint, Processor processor)
specifier|public
name|WebsocketConsumer
parameter_list|(
name|WebsocketEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|initializer
operator|=
operator|new
name|AtmosphereFrameworkInitializer
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|WebsocketEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|WebsocketEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|service (HttpServletRequest request, HttpServletResponse response)
name|void
name|service
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|this
operator|.
name|queryMap
operator|=
name|getQueryMap
argument_list|(
name|request
operator|.
name|getQueryString
argument_list|()
argument_list|)
expr_stmt|;
name|framework
operator|.
name|doCometSupport
argument_list|(
name|AtmosphereRequestImpl
operator|.
name|wrap
argument_list|(
name|request
argument_list|)
argument_list|,
name|AtmosphereResponseImpl
operator|.
name|wrap
argument_list|(
name|response
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|sendMessage (final String connectionKey, Object message)
specifier|public
name|void
name|sendMessage
parameter_list|(
specifier|final
name|String
name|connectionKey
parameter_list|,
name|Object
name|message
parameter_list|)
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
comment|// set header and body
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|WebsocketConstants
operator|.
name|CONNECTION_KEY
argument_list|,
name|connectionKey
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
comment|// send exchange using the async routing engine
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
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|sendEventNotification (String connectionKey, int eventType)
specifier|public
name|void
name|sendEventNotification
parameter_list|(
name|String
name|connectionKey
parameter_list|,
name|int
name|eventType
parameter_list|)
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
comment|// set header
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|WebsocketConstants
operator|.
name|CONNECTION_KEY
argument_list|,
name|connectionKey
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|WebsocketConstants
operator|.
name|EVENT_TYPE
argument_list|,
name|eventType
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|param
range|:
name|queryMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|param
operator|.
name|getKey
argument_list|()
argument_list|,
name|param
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// send exchange using the async routing engine
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
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|sendNotDeliveredMessage (List<String> failedConnectionKeys, Object message)
specifier|public
name|void
name|sendNotDeliveredMessage
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|failedConnectionKeys
parameter_list|,
name|Object
name|message
parameter_list|)
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
comment|// set header and body
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|WebsocketConstants
operator|.
name|CONNECTION_KEY_LIST
argument_list|,
name|failedConnectionKeys
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|WebsocketConstants
operator|.
name|ERROR_TYPE
argument_list|,
name|WebsocketConstants
operator|.
name|MESSAGE_NOT_SENT_ERROR_TYPE
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
comment|// send exchange using the async routing engine
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
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|isEnableEventsResending ()
specifier|public
name|boolean
name|isEnableEventsResending
parameter_list|()
block|{
return|return
name|enableEventsResending
return|;
block|}
DECL|method|getQueryMap (String query)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getQueryMap
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|params
init|=
name|query
operator|.
name|split
argument_list|(
literal|"&"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|param
range|:
name|params
control|)
block|{
name|String
index|[]
name|nameval
init|=
name|param
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|nameval
index|[
literal|0
index|]
argument_list|,
name|nameval
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
block|}
block|}
end_class

end_unit

