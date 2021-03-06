begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.nettyhttp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|nettyhttp
package|;
end_package

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
name|test
operator|.
name|AvailablePortFinder
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
name|Ignore
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
DECL|class|NettyHttpClientChunkedResponseTest
specifier|public
class|class
name|NettyHttpClientChunkedResponseTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port1
specifier|private
name|int
name|port1
decl_stmt|;
DECL|field|port2
specifier|private
name|int
name|port2
decl_stmt|;
annotation|@
name|Ignore
argument_list|(
literal|"TODO: investigate for Camel 3.0"
argument_list|)
annotation|@
name|Test
DECL|method|testNettyHttpClientChunked ()
specifier|public
name|void
name|testNettyHttpClientChunked
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeService
argument_list|(
name|port1
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNettyHttpRouteClientChunked ()
specifier|public
name|void
name|testNettyHttpRouteClientChunked
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeService
argument_list|(
name|port2
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|invokeService (int port, boolean checkChunkedHeader)
specifier|private
name|void
name|invokeService
parameter_list|(
name|int
name|port
parameter_list|,
name|boolean
name|checkChunkedHeader
parameter_list|)
block|{
name|Exchange
name|out
init|=
name|template
operator|.
name|request
argument_list|(
literal|"netty-http:http://localhost:"
operator|+
name|port
operator|+
literal|"/test"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Camel in chunks."
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye Camel in chunks."
argument_list|,
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|checkChunkedHeader
condition|)
block|{
name|assertEquals
argument_list|(
literal|"chunked"
argument_list|,
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Transfer-Encoding"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|port1
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
name|port2
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
comment|// use jetty as server as it supports sending response as chunked encoding
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|port1
operator|+
literal|"/test"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"Transfer-Encoding"
argument_list|,
name|constant
argument_list|(
literal|"chunked"
argument_list|)
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|simple
argument_list|(
literal|"Bye ${body}"
argument_list|)
expr_stmt|;
comment|// set up a netty http proxy
name|from
argument_list|(
literal|"netty-http:http://localhost:"
operator|+
name|port2
operator|+
literal|"/test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"netty-http:http://localhost:"
operator|+
name|port1
operator|+
literal|"/test?bridgeEndpoint=true&throwExceptionOnFailure=false"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

