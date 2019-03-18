begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina2
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
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

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
name|net
operator|.
name|Socket
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|IOHelper
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
comment|/**  * To test camel-mina2 component using a TCP client that communicates using TCP socket communication.  */
end_comment

begin_class
DECL|class|Mina2TcpLineDelimiterUsingPlainSocketTest
specifier|public
class|class
name|Mina2TcpLineDelimiterUsingPlainSocketTest
extends|extends
name|BaseMina2Test
block|{
annotation|@
name|Test
DECL|method|testSendAndReceiveOnce ()
specifier|public
name|void
name|testSendAndReceiveOnce
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
name|sendAndReceive
argument_list|(
literal|"World"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Nothing received from Mina"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendAndReceiveTwice ()
specifier|public
name|void
name|testSendAndReceiveTwice
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|london
init|=
name|sendAndReceive
argument_list|(
literal|"London"
argument_list|)
decl_stmt|;
name|String
name|paris
init|=
name|sendAndReceive
argument_list|(
literal|"Paris"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Nothing received from Mina"
argument_list|,
name|london
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Nothing received from Mina"
argument_list|,
name|paris
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello London"
argument_list|,
name|london
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Paris"
argument_list|,
name|paris
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReceiveNoResponseSinceOutBodyIsNull ()
specifier|public
name|void
name|testReceiveNoResponseSinceOutBodyIsNull
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|sendAndReceive
argument_list|(
literal|"force-null-out-body"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"no data should be recieved"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReceiveNoResponseSinceOutBodyIsNullTwice ()
specifier|public
name|void
name|testReceiveNoResponseSinceOutBodyIsNullTwice
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|sendAndReceive
argument_list|(
literal|"force-null-out-body"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"no data should be recieved"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
name|sendAndReceive
argument_list|(
literal|"force-null-out-body"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"no data should be recieved"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExchangeFailedOutShouldBeNull ()
specifier|public
name|void
name|testExchangeFailedOutShouldBeNull
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|sendAndReceive
argument_list|(
literal|"force-exception"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"out should not be the same as in when the exchange has failed"
argument_list|,
operator|!
literal|"force-exception"
operator|.
name|equals
argument_list|(
name|out
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"should get the exception here"
argument_list|,
name|out
argument_list|,
literal|"java.lang.IllegalArgumentException: Forced exception"
argument_list|)
expr_stmt|;
block|}
DECL|method|sendAndReceive (String input)
specifier|private
name|String
name|sendAndReceive
parameter_list|(
name|String
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
name|buf
index|[]
init|=
operator|new
name|byte
index|[
literal|128
index|]
decl_stmt|;
name|Socket
name|soc
init|=
operator|new
name|Socket
argument_list|()
decl_stmt|;
name|soc
operator|.
name|connect
argument_list|(
operator|new
name|InetSocketAddress
argument_list|(
literal|"localhost"
argument_list|,
name|getPort
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Send message using plain Socket to test if this works
name|OutputStream
name|os
init|=
literal|null
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|os
operator|=
name|soc
operator|.
name|getOutputStream
argument_list|()
expr_stmt|;
comment|// must append MAC newline at the end to flag end of textline to camel-mina2
name|os
operator|.
name|write
argument_list|(
operator|(
name|input
operator|+
literal|"\r"
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|is
operator|=
name|soc
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|int
name|len
init|=
name|is
operator|.
name|read
argument_list|(
name|buf
argument_list|)
decl_stmt|;
if|if
condition|(
name|len
operator|==
operator|-
literal|1
condition|)
block|{
comment|// no data received
return|return
literal|null
return|;
block|}
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|soc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// convert the buffer to chars
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|buf
control|)
block|{
name|char
name|ch
init|=
operator|(
name|char
operator|)
name|b
decl_stmt|;
if|if
condition|(
name|ch
operator|==
literal|'\r'
operator|||
name|ch
operator|==
literal|0
condition|)
block|{
comment|// use MAC delimiter denotes end of text (added in the end in the processor below)
break|break;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
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
comment|// use no delay for fast unit testing
name|errorHandler
argument_list|(
name|defaultErrorHandler
argument_list|()
operator|.
name|maximumRedeliveries
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"mina2:tcp://localhost:%1$s?textline=true&minaLogger=true&textlineDelimiter=MAC&sync=true"
argument_list|,
name|getPort
argument_list|()
argument_list|)
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
name|Exchange
name|e
parameter_list|)
block|{
name|String
name|in
init|=
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"force-null-out-body"
operator|.
name|equals
argument_list|(
name|in
argument_list|)
condition|)
block|{
comment|// forcing a null out body
name|e
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"force-exception"
operator|.
name|equals
argument_list|(
name|in
argument_list|)
condition|)
block|{
comment|// clear out before throwing exception
name|e
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Forced exception"
argument_list|)
throw|;
block|}
else|else
block|{
name|e
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello "
operator|+
name|in
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

