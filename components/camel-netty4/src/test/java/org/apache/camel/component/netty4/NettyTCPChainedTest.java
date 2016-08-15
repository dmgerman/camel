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
name|FileInputStream
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
name|EndpointInject
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|converter
operator|.
name|IOConverter
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
name|Assert
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
comment|/**  * In this test we are checking that same netty endpoint can be safely called twice  * in single route with reconnect. It requires for processing to be fully async otherwise  * {@link io.netty.util.concurrent.BlockingOperationException} is thrown by netty.  */
end_comment

begin_class
DECL|class|NettyTCPChainedTest
specifier|public
class|class
name|NettyTCPChainedTest
extends|extends
name|BaseNettyTest
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
DECL|method|sendFile (String uri)
specifier|private
name|void
name|sendFile
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|asyncSend
argument_list|(
name|uri
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
comment|// Read from an input stream
name|InputStream
name|is
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|FileInputStream
argument_list|(
literal|"src/test/resources/test.txt"
argument_list|)
argument_list|)
decl_stmt|;
name|byte
name|buffer
index|[]
init|=
name|IOConverter
operator|.
name|toBytes
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Set the property of the charset encoding
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
throw|;
block|}
name|Assert
operator|.
name|assertFalse
argument_list|(
name|exchange
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTCPChainedConnectionFromCallbackThread ()
specifier|public
name|void
name|testTCPChainedConnectionFromCallbackThread
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|sendFile
argument_list|(
literal|"direct:chainedCalls"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"netty4:tcp://localhost:{{port}}?sync=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:result"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:nettyCall"
argument_list|)
operator|.
name|to
argument_list|(
literal|"netty4:tcp://localhost:{{port}}?sync=false&disconnect=true&workerCount=1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:chainedCalls"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:nettyCall"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:nettyCall"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

