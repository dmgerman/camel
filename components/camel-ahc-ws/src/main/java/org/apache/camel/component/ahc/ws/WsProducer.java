begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc.ws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
operator|.
name|ws
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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|DefaultProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|ws
operator|.
name|WebSocket
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|WsProducer
specifier|public
class|class
name|WsProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|DEFAULT_STREAM_BUFFER_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_STREAM_BUFFER_SIZE
init|=
literal|127
decl_stmt|;
DECL|field|streamBufferSize
specifier|private
name|int
name|streamBufferSize
init|=
name|DEFAULT_STREAM_BUFFER_SIZE
decl_stmt|;
DECL|method|WsProducer (WsEndpoint endpoint)
specifier|public
name|WsProducer
parameter_list|(
name|WsEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|WsEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|WsEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Object
name|message
init|=
name|in
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sending out {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|instanceof
name|String
condition|)
block|{
name|sendMessage
argument_list|(
name|getWebSocket
argument_list|()
argument_list|,
operator|(
name|String
operator|)
name|message
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|isUseStreaming
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|sendMessage
argument_list|(
name|getWebSocket
argument_list|()
argument_list|,
operator|(
name|byte
index|[]
operator|)
name|message
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|isUseStreaming
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|InputStream
condition|)
block|{
name|sendStreamMessage
argument_list|(
name|getWebSocket
argument_list|()
argument_list|,
operator|(
name|InputStream
operator|)
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//TODO provide other binding option, for now use the converted string
name|getWebSocket
argument_list|()
operator|.
name|sendTextFrame
argument_list|(
name|in
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|sendMessage (WebSocket webSocket, String msg, boolean streaming)
specifier|private
name|void
name|sendMessage
parameter_list|(
name|WebSocket
name|webSocket
parameter_list|,
name|String
name|msg
parameter_list|,
name|boolean
name|streaming
parameter_list|)
block|{
if|if
condition|(
name|streaming
condition|)
block|{
name|int
name|p
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|p
operator|<
name|msg
operator|.
name|length
argument_list|()
condition|)
block|{
if|if
condition|(
name|msg
operator|.
name|length
argument_list|()
operator|-
name|p
operator|<
name|streamBufferSize
condition|)
block|{
name|webSocket
operator|.
name|sendTextFrame
argument_list|(
name|msg
operator|.
name|substring
argument_list|(
name|p
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|p
operator|=
name|msg
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|webSocket
operator|.
name|sendTextFrame
argument_list|(
name|msg
operator|.
name|substring
argument_list|(
name|p
argument_list|,
name|streamBufferSize
argument_list|)
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|p
operator|+=
name|streamBufferSize
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|webSocket
operator|.
name|sendTextFrame
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|sendMessage (WebSocket webSocket, byte[] msg, boolean streaming)
specifier|private
name|void
name|sendMessage
parameter_list|(
name|WebSocket
name|webSocket
parameter_list|,
name|byte
index|[]
name|msg
parameter_list|,
name|boolean
name|streaming
parameter_list|)
block|{
if|if
condition|(
name|streaming
condition|)
block|{
name|int
name|p
init|=
literal|0
decl_stmt|;
name|byte
index|[]
name|writebuf
init|=
operator|new
name|byte
index|[
name|streamBufferSize
index|]
decl_stmt|;
while|while
condition|(
name|p
operator|<
name|msg
operator|.
name|length
condition|)
block|{
if|if
condition|(
name|msg
operator|.
name|length
operator|-
name|p
operator|<
name|streamBufferSize
condition|)
block|{
name|int
name|rest
init|=
name|msg
operator|.
name|length
operator|-
name|p
decl_stmt|;
comment|// bug in grizzly? we need to create a byte array with the exact length
comment|//webSocket.stream(msg, p, rest, true);
name|System
operator|.
name|arraycopy
argument_list|(
name|msg
argument_list|,
name|p
argument_list|,
name|writebuf
argument_list|,
literal|0
argument_list|,
name|rest
argument_list|)
expr_stmt|;
name|byte
index|[]
name|tmpbuf
init|=
operator|new
name|byte
index|[
name|rest
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|writebuf
argument_list|,
literal|0
argument_list|,
name|tmpbuf
argument_list|,
literal|0
argument_list|,
name|rest
argument_list|)
expr_stmt|;
name|webSocket
operator|.
name|sendBinaryFrame
argument_list|(
name|tmpbuf
argument_list|,
literal|true
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// ends
name|p
operator|=
name|msg
operator|.
name|length
expr_stmt|;
block|}
else|else
block|{
comment|// bug in grizzly? we need to create a byte array with the exact length
comment|//webSocket.stream(msg, p, streamBufferSize, false);
name|System
operator|.
name|arraycopy
argument_list|(
name|msg
argument_list|,
name|p
argument_list|,
name|writebuf
argument_list|,
literal|0
argument_list|,
name|streamBufferSize
argument_list|)
expr_stmt|;
name|webSocket
operator|.
name|sendBinaryFrame
argument_list|(
name|writebuf
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// ends
name|p
operator|+=
name|streamBufferSize
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|webSocket
operator|.
name|sendBinaryFrame
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|sendStreamMessage (WebSocket webSocket, InputStream in)
specifier|private
name|void
name|sendStreamMessage
parameter_list|(
name|WebSocket
name|webSocket
parameter_list|,
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|readbuf
init|=
operator|new
name|byte
index|[
name|streamBufferSize
index|]
decl_stmt|;
name|byte
index|[]
name|writebuf
init|=
operator|new
name|byte
index|[
name|streamBufferSize
index|]
decl_stmt|;
name|int
name|rn
init|=
literal|0
decl_stmt|;
name|int
name|wn
init|=
literal|0
decl_stmt|;
try|try
block|{
while|while
condition|(
operator|(
name|rn
operator|=
name|in
operator|.
name|read
argument_list|(
name|readbuf
argument_list|,
literal|0
argument_list|,
name|readbuf
operator|.
name|length
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|wn
operator|>
literal|0
condition|)
block|{
name|webSocket
operator|.
name|sendBinaryFrame
argument_list|(
name|writebuf
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|arraycopy
argument_list|(
name|readbuf
argument_list|,
literal|0
argument_list|,
name|writebuf
argument_list|,
literal|0
argument_list|,
name|rn
argument_list|)
expr_stmt|;
name|wn
operator|=
name|rn
expr_stmt|;
block|}
comment|// a bug in grizzly? we need to create a byte array with the exact length
if|if
condition|(
name|wn
operator|<
name|writebuf
operator|.
name|length
condition|)
block|{
name|byte
index|[]
name|tmpbuf
init|=
name|writebuf
decl_stmt|;
name|writebuf
operator|=
operator|new
name|byte
index|[
name|wn
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|tmpbuf
argument_list|,
literal|0
argument_list|,
name|writebuf
argument_list|,
literal|0
argument_list|,
name|wn
argument_list|)
expr_stmt|;
block|}
comment|// ends
name|webSocket
operator|.
name|sendBinaryFrame
argument_list|(
name|writebuf
argument_list|,
literal|true
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getWebSocket ()
specifier|private
name|WebSocket
name|getWebSocket
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getWebSocket
argument_list|()
return|;
block|}
block|}
end_class

end_unit

