begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.codec
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
operator|.
name|codec
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
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
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelHandler
operator|.
name|Sharable
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

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|DefaultAddressedEnvelope
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
name|socket
operator|.
name|DatagramPacket
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|MessageToMessageDecoder
import|;
end_import

begin_class
annotation|@
name|Sharable
DECL|class|DatagramPacketDecoder
specifier|public
class|class
name|DatagramPacketDecoder
extends|extends
name|MessageToMessageDecoder
argument_list|<
name|DatagramPacket
argument_list|>
block|{
annotation|@
name|Override
DECL|method|decode (ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out)
specifier|protected
name|void
name|decode
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|DatagramPacket
name|msg
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|out
parameter_list|)
throws|throws
name|Exception
block|{
comment|// decode the DatagramPackage to AddressedEnvelope
name|DefaultAddressedEnvelope
argument_list|<
name|Object
argument_list|,
name|InetSocketAddress
argument_list|>
name|addressEvelop
init|=
operator|new
name|DefaultAddressedEnvelope
argument_list|<>
argument_list|(
name|msg
operator|.
name|content
argument_list|()
operator|.
name|retain
argument_list|()
argument_list|,
name|msg
operator|.
name|recipient
argument_list|()
argument_list|,
name|msg
operator|.
name|sender
argument_list|()
argument_list|)
decl_stmt|;
name|out
operator|.
name|add
argument_list|(
name|addressEvelop
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

