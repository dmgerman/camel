begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
package|;
end_package

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
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|Channel
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelHandlerContext
import|;
end_import

begin_comment
comment|/**  * To manage and correlate state of {@link NettyCamelState} when doing request/reply via {@link NettyProducer}.  *<p/>  * This SPI allows custom implementations to correlate the request and replies.  */
end_comment

begin_interface
DECL|interface|NettyCamelStateCorrelationManager
specifier|public
interface|interface
name|NettyCamelStateCorrelationManager
block|{
comment|/**      * Puts the state.      *<p/>      * You can get access to the Camel message from the {@link NettyCamelState} instance.      *      * @param channel the channel      * @param state   the Camel state to be stored      */
DECL|method|putState (Channel channel, NettyCamelState state)
name|void
name|putState
parameter_list|(
name|Channel
name|channel
parameter_list|,
name|NettyCamelState
name|state
parameter_list|)
function_decl|;
comment|/**      * Removes the state when the channel is inactive.      *      * @param ctx netty channel handler context      * @param channel the channel      */
DECL|method|removeState (ChannelHandlerContext ctx, Channel channel)
name|void
name|removeState
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|Channel
name|channel
parameter_list|)
function_decl|;
comment|/**      * Gets the state when a response message has been received.      *<p/>      * If the implementation stores the state temporary in for example a {@link Map} instance      * then this method should remove the state from the map as its no longer needed. In other      * words use the {@link Map#remove(Object)} to get and remove the state.      *      * @param ctx netty channel handler context      * @param channel the channel      * @param msg the response message      */
DECL|method|getState (ChannelHandlerContext ctx, Channel channel, Object msg)
name|NettyCamelState
name|getState
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|Channel
name|channel
parameter_list|,
name|Object
name|msg
parameter_list|)
function_decl|;
comment|/**      * Gets the state when some internal error occurred.      *      * @param ctx netty channel handler context      * @param channel the channel      * @param cause the error      */
DECL|method|getState (ChannelHandlerContext ctx, Channel channel, Throwable cause)
name|NettyCamelState
name|getState
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|Channel
name|channel
parameter_list|,
name|Throwable
name|cause
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

