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
name|io
operator|.
name|netty
operator|.
name|buffer
operator|.
name|ByteBuf
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

begin_import
import|import static
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
operator|.
name|LumberjackConstants
operator|.
name|FRAME_ACKNOWLEDGE_LENGTH
import|;
end_import

begin_import
import|import static
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
operator|.
name|LumberjackConstants
operator|.
name|TYPE_ACKNOWLEDGE
import|;
end_import

begin_import
import|import static
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
operator|.
name|LumberjackConstants
operator|.
name|VERSION_V1
import|;
end_import

begin_import
import|import static
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
operator|.
name|LumberjackConstants
operator|.
name|VERSION_V2
import|;
end_import

begin_comment
comment|/**  * Handles lumberjack window and send acknowledge when required.  */
end_comment

begin_class
DECL|class|LumberjackSessionHandler
specifier|final
class|class
name|LumberjackSessionHandler
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
name|LumberjackSessionHandler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|version
specifier|private
specifier|volatile
name|int
name|version
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|windowSize
specifier|private
specifier|volatile
name|int
name|windowSize
init|=
literal|1
decl_stmt|;
DECL|method|versionRead (int version)
name|void
name|versionRead
parameter_list|(
name|int
name|version
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|version
operator|==
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|version
operator|!=
name|VERSION_V1
operator|&&
name|version
operator|!=
name|VERSION_V2
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unsupported frame version="
operator|+
name|version
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Lumberjack protocol version is {}"
argument_list|,
operator|(
name|char
operator|)
name|version
argument_list|)
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|this
operator|.
name|version
operator|!=
name|version
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Protocol version changed during session from "
operator|+
name|this
operator|.
name|version
operator|+
literal|" to "
operator|+
name|version
argument_list|)
throw|;
block|}
block|}
DECL|method|windowSizeRead (int windowSize)
name|void
name|windowSizeRead
parameter_list|(
name|int
name|windowSize
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Lumberjack window size is {}"
argument_list|,
name|windowSize
argument_list|)
expr_stmt|;
name|this
operator|.
name|windowSize
operator|=
name|windowSize
expr_stmt|;
block|}
DECL|method|notifyMessageProcessed (ChannelHandlerContext ctx, int sequenceNumber)
name|void
name|notifyMessageProcessed
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|int
name|sequenceNumber
parameter_list|)
block|{
if|if
condition|(
name|sequenceNumber
operator|==
name|windowSize
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sequence number is {}. Sending ACK"
argument_list|,
name|sequenceNumber
argument_list|)
expr_stmt|;
name|ByteBuf
name|response
init|=
name|ctx
operator|.
name|alloc
argument_list|()
operator|.
name|heapBuffer
argument_list|(
name|FRAME_ACKNOWLEDGE_LENGTH
argument_list|,
name|FRAME_ACKNOWLEDGE_LENGTH
argument_list|)
decl_stmt|;
name|response
operator|.
name|writeByte
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|response
operator|.
name|writeByte
argument_list|(
name|TYPE_ACKNOWLEDGE
argument_list|)
expr_stmt|;
name|response
operator|.
name|writeInt
argument_list|(
name|sequenceNumber
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|writeAndFlush
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

