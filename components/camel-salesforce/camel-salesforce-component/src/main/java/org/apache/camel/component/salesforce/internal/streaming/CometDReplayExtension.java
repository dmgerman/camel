begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/**  * Copyright (c) 2016, Salesforce Developers  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright  *    notice, this list of conditions and the following disclaimer in the  *    documentation and/or other materials provided with the distribution.  * 3. Neither the name of the copyright holder nor the names of its  *    contributors may be used to endorse or promote products derived from  *    this software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE  * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE  * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR  * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF  * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS  * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN  * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)  * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE  * POSSIBILITY OF SUCH DAMAGE.  **/
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.streaming
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|internal
operator|.
name|streaming
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|Channel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|client
operator|.
name|ClientSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|client
operator|.
name|ClientSession
operator|.
name|Extension
operator|.
name|Adapter
import|;
end_import

begin_comment
comment|/**  * CometDReplayExtension, typical usages are the following:  * {@code client.addExtension(new CometDReplayExtension<>(replayMap));}  *  * @author yzhao  * @since 198 (Winter '16)  */
end_comment

begin_class
DECL|class|CometDReplayExtension
specifier|public
class|class
name|CometDReplayExtension
extends|extends
name|Adapter
block|{
DECL|field|EXTENSION_NAME
specifier|private
specifier|static
specifier|final
name|String
name|EXTENSION_NAME
init|=
literal|"replay"
decl_stmt|;
DECL|field|dataMap
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|dataMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|supported
specifier|private
specifier|final
name|AtomicBoolean
name|supported
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
DECL|method|addChannelReplayId (final String channelName, final long replayId)
specifier|public
name|void
name|addChannelReplayId
parameter_list|(
specifier|final
name|String
name|channelName
parameter_list|,
specifier|final
name|long
name|replayId
parameter_list|)
block|{
name|dataMap
operator|.
name|put
argument_list|(
name|channelName
argument_list|,
name|replayId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|rcv (ClientSession session, Message.Mutable message)
specifier|public
name|boolean
name|rcv
parameter_list|(
name|ClientSession
name|session
parameter_list|,
name|Message
operator|.
name|Mutable
name|message
parameter_list|)
block|{
specifier|final
name|Object
name|value
init|=
name|message
operator|.
name|get
argument_list|(
name|EXTENSION_NAME
argument_list|)
decl_stmt|;
specifier|final
name|Long
name|replayId
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Long
condition|)
block|{
name|replayId
operator|=
operator|(
name|Long
operator|)
name|value
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Number
condition|)
block|{
name|replayId
operator|=
operator|(
operator|(
name|Number
operator|)
name|value
operator|)
operator|.
name|longValue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|replayId
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|supported
operator|.
name|get
argument_list|()
operator|&&
name|replayId
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|dataMap
operator|.
name|put
argument_list|(
name|message
operator|.
name|getChannel
argument_list|()
argument_list|,
name|replayId
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|rcvMeta (ClientSession session, Message.Mutable message)
specifier|public
name|boolean
name|rcvMeta
parameter_list|(
name|ClientSession
name|session
parameter_list|,
name|Message
operator|.
name|Mutable
name|message
parameter_list|)
block|{
switch|switch
condition|(
name|message
operator|.
name|getChannel
argument_list|()
condition|)
block|{
case|case
name|Channel
operator|.
name|META_HANDSHAKE
case|:
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ext
init|=
name|message
operator|.
name|getExt
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|this
operator|.
name|supported
operator|.
name|set
argument_list|(
name|ext
operator|!=
literal|null
operator|&&
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|ext
operator|.
name|get
argument_list|(
name|EXTENSION_NAME
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|sendMeta (ClientSession session, Message.Mutable message)
specifier|public
name|boolean
name|sendMeta
parameter_list|(
name|ClientSession
name|session
parameter_list|,
name|Message
operator|.
name|Mutable
name|message
parameter_list|)
block|{
switch|switch
condition|(
name|message
operator|.
name|getChannel
argument_list|()
condition|)
block|{
case|case
name|Channel
operator|.
name|META_HANDSHAKE
case|:
name|message
operator|.
name|getExt
argument_list|(
literal|true
argument_list|)
operator|.
name|put
argument_list|(
name|EXTENSION_NAME
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
break|break;
case|case
name|Channel
operator|.
name|META_SUBSCRIBE
case|:
if|if
condition|(
name|supported
operator|.
name|get
argument_list|()
condition|)
block|{
name|message
operator|.
name|getExt
argument_list|(
literal|true
argument_list|)
operator|.
name|put
argument_list|(
name|EXTENSION_NAME
argument_list|,
name|dataMap
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
break|break;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

