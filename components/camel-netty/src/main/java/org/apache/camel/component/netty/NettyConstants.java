begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Netty constants  *  * @version   */
end_comment

begin_class
DECL|class|NettyConstants
specifier|public
specifier|final
class|class
name|NettyConstants
block|{
DECL|field|NETTY_CLOSE_CHANNEL_WHEN_COMPLETE
specifier|public
specifier|static
specifier|final
name|String
name|NETTY_CLOSE_CHANNEL_WHEN_COMPLETE
init|=
literal|"CamelNettyCloseChannelWhenComplete"
decl_stmt|;
DECL|field|NETTY_CHANNEL_HANDLER_CONTEXT
specifier|public
specifier|static
specifier|final
name|String
name|NETTY_CHANNEL_HANDLER_CONTEXT
init|=
literal|"CamelNettyChannelHandlerContext"
decl_stmt|;
DECL|field|NETTY_MESSAGE_EVENT
specifier|public
specifier|static
specifier|final
name|String
name|NETTY_MESSAGE_EVENT
init|=
literal|"CamelNettyMessageEvent"
decl_stmt|;
DECL|field|NETTY_REMOTE_ADDRESS
specifier|public
specifier|static
specifier|final
name|String
name|NETTY_REMOTE_ADDRESS
init|=
literal|"CamelNettyRemoteAddress"
decl_stmt|;
DECL|field|NETTY_LOCAL_ADDRESS
specifier|public
specifier|static
specifier|final
name|String
name|NETTY_LOCAL_ADDRESS
init|=
literal|"CamelNettyLocalAddress"
decl_stmt|;
DECL|method|NettyConstants ()
specifier|private
name|NettyConstants
parameter_list|()
block|{
comment|// Utility class
block|}
block|}
end_class

end_unit

