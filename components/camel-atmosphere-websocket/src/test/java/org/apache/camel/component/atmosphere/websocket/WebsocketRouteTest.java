begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atmosphere.websocket
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atmosphere
operator|.
name|websocket
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
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
name|Processor
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|WebsocketRouteTest
specifier|public
class|class
name|WebsocketRouteTest
extends|extends
name|WebsocketCamelRouterTestSupport
block|{
DECL|field|RESPONSE_GREETING
specifier|private
specifier|static
specifier|final
name|String
name|RESPONSE_GREETING
init|=
literal|"Hola "
decl_stmt|;
DECL|field|RESPONSE_GREETING_BYTES
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|RESPONSE_GREETING_BYTES
init|=
block|{
literal|0x48
block|,
literal|0x6f
block|,
literal|0x6c
block|,
literal|0x61
block|,
literal|0x20
block|}
decl_stmt|;
annotation|@
name|Test
DECL|method|testWebsocketSingleClient ()
specifier|public
name|void
name|testWebsocketSingleClient
parameter_list|()
throws|throws
name|Exception
block|{
name|TestClient
name|wsclient
init|=
operator|new
name|TestClient
argument_list|(
literal|"ws://localhost:"
operator|+
name|PORT
operator|+
literal|"/hola"
argument_list|)
decl_stmt|;
name|wsclient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|wsclient
operator|.
name|sendTextMessage
argument_list|(
literal|"Cerveza"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|wsclient
operator|.
name|await
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|received
init|=
name|wsclient
operator|.
name|getReceived
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|received
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hola Cerveza"
argument_list|,
name|received
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|wsclient
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWebsocketSingleClientForBytes ()
specifier|public
name|void
name|testWebsocketSingleClientForBytes
parameter_list|()
throws|throws
name|Exception
block|{
name|TestClient
name|wsclient
init|=
operator|new
name|TestClient
argument_list|(
literal|"ws://localhost:"
operator|+
name|PORT
operator|+
literal|"/hola"
argument_list|)
decl_stmt|;
name|wsclient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|wsclient
operator|.
name|sendBytesMessage
argument_list|(
literal|"Cerveza"
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|wsclient
operator|.
name|await
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|received
init|=
name|wsclient
operator|.
name|getReceived
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|received
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hola Cerveza"
argument_list|,
name|received
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|wsclient
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWebsocketSingleClientForReader ()
specifier|public
name|void
name|testWebsocketSingleClientForReader
parameter_list|()
throws|throws
name|Exception
block|{
name|TestClient
name|wsclient
init|=
operator|new
name|TestClient
argument_list|(
literal|"ws://localhost:"
operator|+
name|PORT
operator|+
literal|"/hola3"
argument_list|)
decl_stmt|;
name|wsclient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|wsclient
operator|.
name|sendTextMessage
argument_list|(
literal|"Cerveza"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|wsclient
operator|.
name|await
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|received
init|=
name|wsclient
operator|.
name|getReceived
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|received
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hola Cerveza"
argument_list|,
name|received
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|wsclient
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWebsocketSingleClientForInputStream ()
specifier|public
name|void
name|testWebsocketSingleClientForInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|TestClient
name|wsclient
init|=
operator|new
name|TestClient
argument_list|(
literal|"ws://localhost:"
operator|+
name|PORT
operator|+
literal|"/hola3"
argument_list|)
decl_stmt|;
name|wsclient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|wsclient
operator|.
name|sendBytesMessage
argument_list|(
literal|"Cerveza"
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|wsclient
operator|.
name|await
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|received
init|=
name|wsclient
operator|.
name|getReceived
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|received
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hola Cerveza"
argument_list|,
name|received
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|wsclient
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWebsocketBroadcastClient ()
specifier|public
name|void
name|testWebsocketBroadcastClient
parameter_list|()
throws|throws
name|Exception
block|{
name|TestClient
name|wsclient1
init|=
operator|new
name|TestClient
argument_list|(
literal|"ws://localhost:"
operator|+
name|PORT
operator|+
literal|"/hola2"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|TestClient
name|wsclient2
init|=
operator|new
name|TestClient
argument_list|(
literal|"ws://localhost:"
operator|+
name|PORT
operator|+
literal|"/hola2"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|wsclient1
operator|.
name|connect
argument_list|()
expr_stmt|;
name|wsclient2
operator|.
name|connect
argument_list|()
expr_stmt|;
name|wsclient1
operator|.
name|sendTextMessage
argument_list|(
literal|"Gambas"
argument_list|)
expr_stmt|;
name|wsclient2
operator|.
name|sendTextMessage
argument_list|(
literal|"Calamares"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|wsclient1
operator|.
name|await
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|wsclient2
operator|.
name|await
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|received1
init|=
name|wsclient1
operator|.
name|getReceived
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|received1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|received1
operator|.
name|contains
argument_list|(
literal|"Hola Gambas"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|received1
operator|.
name|contains
argument_list|(
literal|"Hola Calamares"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|received2
init|=
name|wsclient2
operator|.
name|getReceived
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|received2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|received2
operator|.
name|contains
argument_list|(
literal|"Hola Gambas"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|received2
operator|.
name|contains
argument_list|(
literal|"Hola Calamares"
argument_list|)
argument_list|)
expr_stmt|;
name|wsclient1
operator|.
name|close
argument_list|()
expr_stmt|;
name|wsclient2
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWebsocketEventsResendingDisabled ()
specifier|public
name|void
name|testWebsocketEventsResendingDisabled
parameter_list|()
throws|throws
name|Exception
block|{
name|TestClient
name|wsclient
init|=
operator|new
name|TestClient
argument_list|(
literal|"ws://localhost:"
operator|+
name|PORT
operator|+
literal|"/hola4"
argument_list|)
decl_stmt|;
name|wsclient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|wsclient
operator|.
name|await
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|wsclient
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// START SNIPPET: payload
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// route for a single line
name|from
argument_list|(
literal|"atmosphere-websocket:///hola"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:info"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|createResponse
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"atmosphere-websocket:///hola"
argument_list|)
expr_stmt|;
comment|// route for a broadcast line
name|from
argument_list|(
literal|"atmosphere-websocket:///hola2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:info"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|createResponse
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"atmosphere-websocket:///hola2?sendToAll=true"
argument_list|)
expr_stmt|;
comment|// route for a single stream line
name|from
argument_list|(
literal|"atmosphere-websocket:///hola3?useStreaming=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:info"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|createResponse
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"atmosphere-websocket:///hola3"
argument_list|)
expr_stmt|;
comment|// route for events resending disabled
name|from
argument_list|(
literal|"atmosphere-websocket:///hola4"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:info"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|checkEventsResendingDisabled
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"atmosphere-websocket:///hola4"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createResponse (Exchange exchange, boolean streaming)
specifier|private
specifier|static
name|void
name|createResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|streaming
parameter_list|)
block|{
name|Object
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|streaming
condition|)
block|{
name|assertTrue
argument_list|(
literal|"Expects Reader or InputStream"
argument_list|,
name|msg
operator|instanceof
name|Reader
operator|||
name|msg
operator|instanceof
name|InputStream
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTrue
argument_list|(
literal|"Expects String or byte[]"
argument_list|,
name|msg
operator|instanceof
name|String
operator|||
name|msg
operator|instanceof
name|byte
index|[]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|msg
operator|instanceof
name|String
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|RESPONSE_GREETING
operator|+
name|msg
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|msg
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|createByteResponse
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|msg
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|msg
operator|instanceof
name|Reader
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|StringReader
argument_list|(
name|RESPONSE_GREETING
operator|+
name|readAll
argument_list|(
operator|(
name|Reader
operator|)
name|msg
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|msg
operator|instanceof
name|InputStream
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|createByteResponse
argument_list|(
name|readAll
argument_list|(
operator|(
name|InputStream
operator|)
name|msg
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|checkEventsResendingDisabled (Exchange exchange)
specifier|private
specifier|static
name|void
name|checkEventsResendingDisabled
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|eventType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|WebsocketConstants
operator|.
name|EVENT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|eventType
operator|instanceof
name|Integer
condition|)
block|{
if|if
condition|(
name|eventType
operator|.
name|equals
argument_list|(
name|WebsocketConstants
operator|.
name|ONOPEN_EVENT_TYPE
argument_list|)
operator|||
name|eventType
operator|.
name|equals
argument_list|(
name|WebsocketConstants
operator|.
name|ONCLOSE_EVENT_TYPE
argument_list|)
operator|||
name|eventType
operator|.
name|equals
argument_list|(
name|WebsocketConstants
operator|.
name|ONERROR_EVENT_TYPE
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Error. This place should never be reached."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|createByteResponse (byte[] req)
specifier|private
specifier|static
name|byte
index|[]
name|createByteResponse
parameter_list|(
name|byte
index|[]
name|req
parameter_list|)
block|{
name|byte
index|[]
name|resp
init|=
operator|new
name|byte
index|[
name|req
operator|.
name|length
operator|+
name|RESPONSE_GREETING_BYTES
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|RESPONSE_GREETING_BYTES
argument_list|,
literal|0
argument_list|,
name|resp
argument_list|,
literal|0
argument_list|,
name|RESPONSE_GREETING_BYTES
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|req
argument_list|,
literal|0
argument_list|,
name|resp
argument_list|,
name|RESPONSE_GREETING_BYTES
operator|.
name|length
argument_list|,
name|req
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|resp
return|;
block|}
DECL|method|readAll (Reader reader)
specifier|private
specifier|static
name|String
name|readAll
parameter_list|(
name|Reader
name|reader
parameter_list|)
block|{
name|StringBuffer
name|strbuf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
try|try
block|{
name|char
index|[]
name|buf
init|=
operator|new
name|char
index|[
literal|4024
index|]
decl_stmt|;
name|int
name|n
decl_stmt|;
while|while
condition|(
operator|(
name|n
operator|=
name|reader
operator|.
name|read
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|buf
operator|.
name|length
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|strbuf
operator|.
name|append
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|n
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
finally|finally
block|{
try|try
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
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
return|return
name|strbuf
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|readAll (InputStream is)
specifier|private
specifier|static
name|byte
index|[]
name|readAll
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
name|ByteArrayOutputStream
name|bytebuf
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|4024
index|]
decl_stmt|;
name|int
name|n
decl_stmt|;
while|while
condition|(
operator|(
name|n
operator|=
name|is
operator|.
name|read
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|buf
operator|.
name|length
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|bytebuf
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|n
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
finally|finally
block|{
try|try
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
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
return|return
name|bytebuf
operator|.
name|toByteArray
argument_list|()
return|;
block|}
comment|// END SNIPPET: payload
block|}
end_class

end_unit

