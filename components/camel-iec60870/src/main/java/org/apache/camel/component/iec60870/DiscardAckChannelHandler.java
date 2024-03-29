begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.iec60870
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|iec60870
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|ChannelInboundHandlerAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|asdu
operator|.
name|message
operator|.
name|AbstractMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|asdu
operator|.
name|types
operator|.
name|Cause
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|neoscada
operator|.
name|protocol
operator|.
name|iec60870
operator|.
name|asdu
operator|.
name|types
operator|.
name|StandardCause
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

begin_class
DECL|class|DiscardAckChannelHandler
specifier|public
class|class
name|DiscardAckChannelHandler
extends|extends
name|ChannelInboundHandlerAdapter
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
name|DiscardAckChannelHandler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|discards
specifier|private
specifier|final
name|Set
argument_list|<
name|Cause
argument_list|>
name|discards
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|DiscardAckChannelHandler ()
specifier|public
name|DiscardAckChannelHandler
parameter_list|()
block|{
name|this
operator|.
name|discards
operator|.
name|add
argument_list|(
name|StandardCause
operator|.
name|ACTIVATION_CONFIRM
argument_list|)
expr_stmt|;
name|this
operator|.
name|discards
operator|.
name|add
argument_list|(
name|StandardCause
operator|.
name|ACTIVATION_TERMINATION
argument_list|)
expr_stmt|;
name|this
operator|.
name|discards
operator|.
name|add
argument_list|(
name|StandardCause
operator|.
name|DEACTIVATION_CONFIRM
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|channelRead (final ChannelHandlerContext ctx, final Object msg)
specifier|public
name|void
name|channelRead
parameter_list|(
specifier|final
name|ChannelHandlerContext
name|ctx
parameter_list|,
specifier|final
name|Object
name|msg
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|msg
operator|instanceof
name|AbstractMessage
condition|)
block|{
specifier|final
name|AbstractMessage
name|amsg
init|=
operator|(
name|AbstractMessage
operator|)
name|msg
decl_stmt|;
specifier|final
name|Cause
name|cause
init|=
name|amsg
operator|.
name|getHeader
argument_list|()
operator|.
name|getCauseOfTransmission
argument_list|()
operator|.
name|getCause
argument_list|()
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|discards
operator|.
name|contains
argument_list|(
name|cause
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Discarding: {}"
argument_list|,
name|cause
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|super
operator|.
name|channelRead
argument_list|(
name|ctx
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

