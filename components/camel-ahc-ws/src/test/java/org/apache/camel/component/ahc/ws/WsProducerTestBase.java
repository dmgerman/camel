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
name|ByteArrayInputStream
import|;
end_import

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
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|CamelContext
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
name|ProducerTemplate
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
name|impl
operator|.
name|DefaultCamelContext
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
name|test
operator|.
name|AvailablePortFinder
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
name|server
operator|.
name|Connector
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
name|server
operator|.
name|Server
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
name|servlet
operator|.
name|ServletContextHandler
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
name|servlet
operator|.
name|ServletHolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|WsProducerTestBase
specifier|public
specifier|abstract
class|class
name|WsProducerTestBase
extends|extends
name|Assert
block|{
DECL|field|TEST_MESSAGE
specifier|protected
specifier|static
specifier|final
name|String
name|TEST_MESSAGE
init|=
literal|"Hello World!"
decl_stmt|;
DECL|field|PORT
specifier|protected
specifier|static
specifier|final
name|int
name|PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|server
specifier|protected
name|Server
name|server
decl_stmt|;
DECL|field|messages
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|messages
decl_stmt|;
DECL|method|startTestServer ()
specifier|public
name|void
name|startTestServer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// start a simple websocket echo service
name|server
operator|=
operator|new
name|Server
argument_list|()
expr_stmt|;
name|Connector
name|connector
init|=
name|getConnector
argument_list|()
decl_stmt|;
name|connector
operator|.
name|setHost
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setPort
argument_list|(
name|PORT
argument_list|)
expr_stmt|;
name|server
operator|.
name|addConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
name|ServletContextHandler
name|context
init|=
operator|new
name|ServletContextHandler
argument_list|(
name|ServletContextHandler
operator|.
name|SESSIONS
argument_list|)
decl_stmt|;
name|context
operator|.
name|setContextPath
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|server
operator|.
name|setHandler
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|messages
operator|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|server
operator|.
name|setHandler
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|ServletHolder
name|servletHolder
init|=
operator|new
name|ServletHolder
argument_list|(
operator|new
name|TestServlet
argument_list|(
name|messages
argument_list|)
argument_list|)
decl_stmt|;
name|context
operator|.
name|addServlet
argument_list|(
name|servletHolder
argument_list|,
literal|"/*"
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|server
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|stopTestServer ()
specifier|public
name|void
name|stopTestServer
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|startTestServer
argument_list|()
expr_stmt|;
name|camelContext
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|setUpComponent
argument_list|()
expr_stmt|;
name|template
operator|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
name|stopTestServer
argument_list|()
expr_stmt|;
block|}
DECL|method|setUpComponent ()
specifier|protected
specifier|abstract
name|void
name|setUpComponent
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|getConnector ()
specifier|protected
specifier|abstract
name|Connector
name|getConnector
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|getTargetURL ()
specifier|protected
specifier|abstract
name|String
name|getTargetURL
parameter_list|()
function_decl|;
DECL|method|getTextTestMessage ()
specifier|protected
name|String
name|getTextTestMessage
parameter_list|()
block|{
return|return
name|TEST_MESSAGE
return|;
block|}
DECL|method|getByteTestMessage ()
specifier|protected
name|byte
index|[]
name|getByteTestMessage
parameter_list|()
throws|throws
name|UnsupportedEncodingException
block|{
return|return
name|TEST_MESSAGE
operator|.
name|getBytes
argument_list|(
literal|"utf-8"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testWriteToWebsocket ()
specifier|public
name|void
name|testWriteToWebsocket
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testMessage
init|=
name|getTextTestMessage
argument_list|()
decl_stmt|;
name|testWriteToWebsocket
argument_list|(
name|testMessage
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|messages
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|verifyMessage
argument_list|(
name|testMessage
argument_list|,
name|messages
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWriteBytesToWebsocket ()
specifier|public
name|void
name|testWriteBytesToWebsocket
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|testMessageBytes
init|=
name|getByteTestMessage
argument_list|()
decl_stmt|;
name|testWriteToWebsocket
argument_list|(
name|testMessageBytes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|messages
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|verifyMessage
argument_list|(
name|testMessageBytes
argument_list|,
name|messages
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWriteStreamToWebsocket ()
specifier|public
name|void
name|testWriteStreamToWebsocket
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|testMessageBytes
init|=
name|createLongByteTestMessage
argument_list|()
decl_stmt|;
name|testWriteToWebsocket
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|testMessageBytes
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|messages
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|verifyMessage
argument_list|(
name|testMessageBytes
argument_list|,
name|messages
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testWriteToWebsocket (Object message)
specifier|private
name|void
name|testWriteToWebsocket
parameter_list|(
name|Object
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|sendMessage
argument_list|(
name|getTargetURL
argument_list|()
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|long
name|towait
init|=
literal|5000
decl_stmt|;
while|while
condition|(
name|towait
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|messages
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
break|break;
block|}
name|towait
operator|-=
literal|500
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|sendMessage (String endpointUri, final Object msg)
specifier|private
name|Exchange
name|sendMessage
parameter_list|(
name|String
name|endpointUri
parameter_list|,
specifier|final
name|Object
name|msg
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
name|endpointUri
argument_list|,
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
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|verifyMessage (Object original, Object result)
specifier|private
name|void
name|verifyMessage
parameter_list|(
name|Object
name|original
parameter_list|,
name|Object
name|result
parameter_list|)
block|{
if|if
condition|(
name|original
operator|instanceof
name|String
operator|&&
name|result
operator|instanceof
name|String
condition|)
block|{
name|assertEquals
argument_list|(
name|original
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|original
operator|instanceof
name|byte
index|[]
operator|&&
name|result
operator|instanceof
name|byte
index|[]
condition|)
block|{
comment|// use string-equals as our bytes are string'able
name|assertEquals
argument_list|(
operator|new
name|String
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|original
argument_list|)
argument_list|,
operator|new
name|String
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|original
operator|instanceof
name|InputStream
condition|)
block|{
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|byte
index|[]
operator|||
name|result
operator|instanceof
name|InputStream
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|result
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|result
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|int
name|oc
init|=
literal|0
decl_stmt|;
name|int
name|or
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|oc
operator|!=
operator|-
literal|1
condition|)
block|{
name|oc
operator|=
operator|(
operator|(
name|InputStream
operator|)
name|original
operator|)
operator|.
name|read
argument_list|()
expr_stmt|;
name|or
operator|=
operator|(
operator|(
name|InputStream
operator|)
name|result
operator|)
operator|.
name|read
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|oc
argument_list|,
name|or
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|or
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"unable to verify data: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|fail
argument_list|(
literal|"unexpected message type for input "
operator|+
name|original
operator|+
literal|": "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createLongByteTestMessage ()
specifier|protected
name|byte
index|[]
name|createLongByteTestMessage
parameter_list|()
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bs
init|=
name|TEST_MESSAGE
operator|.
name|getBytes
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|baos
operator|.
name|write
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|i
argument_list|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|baos
operator|.
name|write
argument_list|(
literal|0x20
argument_list|)
expr_stmt|;
name|baos
operator|.
name|write
argument_list|(
name|bs
argument_list|)
expr_stmt|;
name|baos
operator|.
name|write
argument_list|(
literal|';'
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
return|return
name|baos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
DECL|method|createLongTextTestMessage ()
specifier|protected
name|String
name|createLongTextTestMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|';'
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

