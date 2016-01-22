begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ServerSocket
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
DECL|class|NettyProducerHangTest
specifier|public
class|class
name|NettyProducerHangTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|PORT
specifier|private
specifier|static
name|int
name|PORT
init|=
literal|4093
decl_stmt|;
annotation|@
name|Test
DECL|method|nettyProducerHangsOnTheSecondRequestToTheSocketWhichIsClosed ()
specifier|public
name|void
name|nettyProducerHangsOnTheSecondRequestToTheSocketWhichIsClosed
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|acceptReplyAcceptClose
argument_list|()
expr_stmt|;
name|acceptReplyAcceptClose
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Exception occured: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|response1
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4:tcp://localhost:"
operator|+
name|PORT
operator|+
literal|"?textline=true&sync=true"
argument_list|,
literal|"request1"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Received first response<"
operator|+
name|response1
operator|+
literal|">"
argument_list|)
expr_stmt|;
try|try
block|{
comment|// our test server will close the socket now so we should get an error
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4:tcp://localhost:"
operator|+
name|PORT
operator|+
literal|"?textline=true&sync=true"
argument_list|,
literal|"request2"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertStringContains
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"No response received from remote server"
argument_list|)
expr_stmt|;
block|}
name|String
name|response2
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4:tcp://localhost:"
operator|+
name|PORT
operator|+
literal|"?textline=true&sync=true"
argument_list|,
literal|"request3"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Received 2nd response<"
operator|+
name|response2
operator|+
literal|">"
argument_list|)
expr_stmt|;
try|try
block|{
comment|// our test server will close the socket now so we should get an error
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4:tcp://localhost:"
operator|+
name|PORT
operator|+
literal|"?textline=true&sync=true"
argument_list|,
literal|"request4"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertStringContains
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"No response received from remote server"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|acceptReplyAcceptClose ()
specifier|private
name|void
name|acceptReplyAcceptClose
parameter_list|()
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
name|ServerSocket
name|serverSocket
init|=
operator|new
name|ServerSocket
argument_list|(
name|PORT
argument_list|)
decl_stmt|;
name|Socket
name|soc
init|=
name|serverSocket
operator|.
name|accept
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Open socket and accept data"
argument_list|)
expr_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|soc
operator|.
name|getInputStream
argument_list|()
init|;
name|OutputStream
name|os
operator|=
name|soc
operator|.
name|getOutputStream
argument_list|()
init|)
block|{
comment|// read first message
name|is
operator|.
name|read
argument_list|(
name|buf
argument_list|)
expr_stmt|;
comment|// reply to the first message
name|os
operator|.
name|write
argument_list|(
literal|"response\n"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
comment|// read second message
name|is
operator|.
name|read
argument_list|(
name|buf
argument_list|)
expr_stmt|;
comment|// do not reply, just close socket (emulate network problem)
block|}
finally|finally
block|{
name|soc
operator|.
name|close
argument_list|()
expr_stmt|;
name|serverSocket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Close socket"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

