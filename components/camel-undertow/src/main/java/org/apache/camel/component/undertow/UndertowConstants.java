begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
package|;
end_package

begin_class
DECL|class|UndertowConstants
specifier|public
specifier|final
class|class
name|UndertowConstants
block|{
DECL|field|CONNECTION_KEY
specifier|public
specifier|static
specifier|final
name|String
name|CONNECTION_KEY
init|=
literal|"websocket.connectionKey"
decl_stmt|;
DECL|field|CONNECTION_KEY_LIST
specifier|public
specifier|static
specifier|final
name|String
name|CONNECTION_KEY_LIST
init|=
literal|"websocket.connectionKey.list"
decl_stmt|;
DECL|field|SEND_TO_ALL
specifier|public
specifier|static
specifier|final
name|String
name|SEND_TO_ALL
init|=
literal|"websocket.sendToAll"
decl_stmt|;
DECL|field|EVENT_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|EVENT_TYPE
init|=
literal|"websocket.eventType"
decl_stmt|;
DECL|field|EVENT_TYPE_ENUM
specifier|public
specifier|static
specifier|final
name|String
name|EVENT_TYPE_ENUM
init|=
literal|"websocket.eventTypeEnum"
decl_stmt|;
DECL|field|CHANNEL
specifier|public
specifier|static
specifier|final
name|String
name|CHANNEL
init|=
literal|"websocket.channel"
decl_stmt|;
DECL|field|EXCHANGE
specifier|public
specifier|static
specifier|final
name|String
name|EXCHANGE
init|=
literal|"websocket.exchange"
decl_stmt|;
comment|/**      * WebSocket peers related events the {@link UndertowConsumer} sends to the Camel route.      */
DECL|enum|EventType
specifier|public
enum|enum
name|EventType
block|{
comment|/**          * A new peer has connected.          */
DECL|enumConstant|ONOPEN
name|ONOPEN
argument_list|(
literal|1
argument_list|)
block|,
comment|/**          * A peer has disconnected.          */
DECL|enumConstant|ONCLOSE
name|ONCLOSE
argument_list|(
literal|0
argument_list|)
block|,
comment|/**          * Unused in Undertow component. Kept for compatibility with Camel websocket component.          */
DECL|enumConstant|ONERROR
name|ONERROR
argument_list|(
operator|-
literal|1
argument_list|)
block|;
DECL|field|code
specifier|private
specifier|final
name|int
name|code
decl_stmt|;
DECL|method|EventType (int code)
name|EventType
parameter_list|(
name|int
name|code
parameter_list|)
block|{
name|this
operator|.
name|code
operator|=
name|code
expr_stmt|;
block|}
comment|/**          * @return a numeric identifier of this {@link EventType}. Kept for compatibility with Camel websocket          *         component.          */
DECL|method|getCode ()
specifier|public
name|int
name|getCode
parameter_list|()
block|{
return|return
name|code
return|;
block|}
DECL|method|ofCode (int code)
specifier|public
specifier|static
name|EventType
name|ofCode
parameter_list|(
name|int
name|code
parameter_list|)
block|{
switch|switch
condition|(
name|code
condition|)
block|{
case|case
literal|1
case|:
return|return
name|ONOPEN
return|;
case|case
literal|0
case|:
return|return
name|ONCLOSE
return|;
case|case
operator|-
literal|1
case|:
return|return
name|ONERROR
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find an "
operator|+
name|EventType
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" for code "
operator|+
name|code
argument_list|)
throw|;
block|}
block|}
block|}
DECL|field|WS_PROTOCOL
specifier|public
specifier|static
specifier|final
name|String
name|WS_PROTOCOL
init|=
literal|"ws"
decl_stmt|;
DECL|field|WSS_PROTOCOL
specifier|public
specifier|static
specifier|final
name|String
name|WSS_PROTOCOL
init|=
literal|"wss"
decl_stmt|;
DECL|method|UndertowConstants ()
specifier|private
name|UndertowConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

