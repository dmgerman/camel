begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
package|;
end_package

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelHandler
import|;
end_import

begin_comment
comment|/**  * A {@link ChannelHandlerFactory} returning a shareable {@link ChannelHandler}.  */
end_comment

begin_class
DECL|class|ShareableChannelHandlerFactory
specifier|public
class|class
name|ShareableChannelHandlerFactory
implements|implements
name|ChannelHandlerFactory
block|{
DECL|field|channelHandler
specifier|private
specifier|final
name|ChannelHandler
name|channelHandler
decl_stmt|;
DECL|method|ShareableChannelHandlerFactory (ChannelHandler channelHandler)
specifier|public
name|ShareableChannelHandlerFactory
parameter_list|(
name|ChannelHandler
name|channelHandler
parameter_list|)
block|{
name|this
operator|.
name|channelHandler
operator|=
name|channelHandler
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newChannelHandler ()
specifier|public
name|ChannelHandler
name|newChannelHandler
parameter_list|()
block|{
return|return
name|channelHandler
return|;
block|}
block|}
end_class

end_unit

