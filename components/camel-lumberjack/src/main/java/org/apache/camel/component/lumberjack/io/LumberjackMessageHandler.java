begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.lumberjack.io
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|lumberjack
operator|.
name|io
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
name|SimpleChannelInboundHandler
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

begin_comment
comment|/**  * This handler connects the Netty pipeline to the Camel endpoint.  */
end_comment

begin_class
DECL|class|LumberjackMessageHandler
specifier|final
class|class
name|LumberjackMessageHandler
extends|extends
name|SimpleChannelInboundHandler
argument_list|<
name|LumberjackMessage
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|LumberjackMessageHandler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|sessionHandler
specifier|private
specifier|final
name|LumberjackSessionHandler
name|sessionHandler
decl_stmt|;
DECL|field|messageProcessor
specifier|private
specifier|final
name|LumberjackMessageProcessor
name|messageProcessor
decl_stmt|;
DECL|field|process
specifier|private
specifier|volatile
name|boolean
name|process
init|=
literal|true
decl_stmt|;
DECL|method|LumberjackMessageHandler (LumberjackSessionHandler sessionHandler, LumberjackMessageProcessor messageProcessor)
name|LumberjackMessageHandler
parameter_list|(
name|LumberjackSessionHandler
name|sessionHandler
parameter_list|,
name|LumberjackMessageProcessor
name|messageProcessor
parameter_list|)
block|{
name|this
operator|.
name|sessionHandler
operator|=
name|sessionHandler
expr_stmt|;
name|this
operator|.
name|messageProcessor
operator|=
name|messageProcessor
expr_stmt|;
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
if|if
condition|(
name|cause
operator|instanceof
name|IOException
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"IO exception (client connection closed ?)"
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Caught an exception while reading, closing channel."
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|channelRead0 (ChannelHandlerContext ctx, LumberjackMessage msg)
specifier|protected
name|void
name|channelRead0
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|LumberjackMessage
name|msg
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|process
condition|)
block|{
name|messageProcessor
operator|.
name|onMessageReceived
argument_list|(
name|msg
operator|.
name|getPayload
argument_list|()
argument_list|,
name|success
lambda|->
block|{
if|if
condition|(
name|success
condition|)
block|{
name|sessionHandler
operator|.
name|notifyMessageProcessed
argument_list|(
name|ctx
argument_list|,
name|msg
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Mark that we shouldn't process the next messages that are already decoded and are waiting in netty queues
name|process
operator|=
literal|false
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

