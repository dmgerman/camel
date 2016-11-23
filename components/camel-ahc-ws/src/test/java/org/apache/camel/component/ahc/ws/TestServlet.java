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
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|websocket
operator|.
name|api
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|websocket
operator|.
name|api
operator|.
name|annotations
operator|.
name|OnWebSocketConnect
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|websocket
operator|.
name|api
operator|.
name|annotations
operator|.
name|OnWebSocketMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|websocket
operator|.
name|api
operator|.
name|annotations
operator|.
name|WebSocket
import|;
end_import

begin_class
annotation|@
name|WebSocket
DECL|class|TestServlet
specifier|public
class|class
name|TestServlet
block|{
DECL|field|session
specifier|private
name|Session
name|session
decl_stmt|;
DECL|method|TestServlet ()
specifier|public
name|TestServlet
parameter_list|()
block|{     }
annotation|@
name|OnWebSocketConnect
DECL|method|handleConnect (Session session)
specifier|public
name|void
name|handleConnect
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
annotation|@
name|OnWebSocketMessage
DECL|method|handleMessage (String message)
specifier|public
name|void
name|handleMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|TestMessages
operator|.
name|getInstance
argument_list|()
operator|.
name|addMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// send back same data
name|send
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|OnWebSocketMessage
DECL|method|handleMessage (byte[] message, int offset, int length)
specifier|public
name|void
name|handleMessage
parameter_list|(
name|byte
index|[]
name|message
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|TestMessages
operator|.
name|getInstance
argument_list|()
operator|.
name|addMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// send back same data
name|send
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|send (String message)
specifier|private
name|void
name|send
parameter_list|(
name|String
name|message
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|session
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|session
operator|.
name|getRemote
argument_list|()
operator|.
name|sendString
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
DECL|method|send (byte[] bytes)
specifier|private
name|void
name|send
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|session
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|session
operator|.
name|getRemote
argument_list|()
operator|.
name|sendBytes
argument_list|(
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|bytes
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
end_class

end_unit

