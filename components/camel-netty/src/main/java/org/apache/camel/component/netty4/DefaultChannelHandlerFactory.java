begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelHandlerContext
import|;
end_import

begin_class
DECL|class|DefaultChannelHandlerFactory
specifier|public
specifier|abstract
class|class
name|DefaultChannelHandlerFactory
implements|implements
name|ChannelHandlerFactory
block|{
annotation|@
name|Override
DECL|method|handlerAdded (ChannelHandlerContext ctx)
specifier|public
name|void
name|handlerAdded
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
comment|//Do nothing here
block|}
annotation|@
name|Override
DECL|method|handlerRemoved (ChannelHandlerContext ctx)
specifier|public
name|void
name|handlerRemoved
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
comment|//Do nothing here
block|}
annotation|@
name|Override
DECL|method|exceptionCaught (ChannelHandlerContext ctx, Throwable cause)
specifier|public
name|void
name|exceptionCaught
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|Throwable
name|cause
parameter_list|)
throws|throws
name|Exception
block|{
comment|//Do nothing here
block|}
block|}
end_class

end_unit

