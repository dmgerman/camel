begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
package|;
end_package

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|FullHttpResponse
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
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpGet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpUriRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|HttpClientBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|util
operator|.
name|EntityUtils
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
DECL|class|NettyHttpSwitchingStatus204Test
specifier|public
class|class
name|NettyHttpSwitchingStatus204Test
extends|extends
name|BaseNettyTest
block|{
annotation|@
name|Test
DECL|method|testSwitchNoBodyTo204ViaHttp ()
specifier|public
name|void
name|testSwitchNoBodyTo204ViaHttp
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpUriRequest
name|request
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/bar"
argument_list|)
decl_stmt|;
name|HttpClient
name|httpClient
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|HttpResponse
name|httpResponse
init|=
name|httpClient
operator|.
name|execute
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|204
argument_list|,
name|httpResponse
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|httpResponse
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSwitchingNoBodyTo204NettyHttpViaCamel ()
specifier|public
name|void
name|testSwitchingNoBodyTo204NettyHttpViaCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|inExchange
init|=
name|this
operator|.
name|createExchangeWithBody
argument_list|(
literal|"Hello World"
argument_list|)
decl_stmt|;
name|Exchange
name|outExchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"netty-http:http://localhost:{{port}}/bar"
argument_list|,
name|inExchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|204
argument_list|,
name|outExchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|outExchange
operator|.
name|getMessage
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
name|NettyHttpMessage
name|message
init|=
name|outExchange
operator|.
name|getIn
argument_list|(
name|NettyHttpMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|FullHttpResponse
name|response
init|=
name|message
operator|.
name|getHttpResponse
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|204
argument_list|,
name|response
operator|.
name|status
argument_list|()
operator|.
name|code
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSwitchingNoBodyTo204ViaCamelRoute ()
specifier|public
name|void
name|testSwitchingNoBodyTo204ViaCamelRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|inExchange
init|=
name|this
operator|.
name|createExchangeWithBody
argument_list|(
literal|"Hello World"
argument_list|)
decl_stmt|;
name|Exchange
name|outExchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:bar"
argument_list|,
name|inExchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|204
argument_list|,
name|outExchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|outExchange
operator|.
name|getMessage
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
name|NettyHttpMessage
name|message
init|=
name|outExchange
operator|.
name|getIn
argument_list|(
name|NettyHttpMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|FullHttpResponse
name|response
init|=
name|message
operator|.
name|getHttpResponse
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|204
argument_list|,
name|response
operator|.
name|status
argument_list|()
operator|.
name|code
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoSwitchingNoCodeViaHttp ()
specifier|public
name|void
name|testNoSwitchingNoCodeViaHttp
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpUriRequest
name|request
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/foo"
argument_list|)
decl_stmt|;
name|HttpClient
name|httpClient
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|HttpResponse
name|httpResponse
init|=
name|httpClient
operator|.
name|execute
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|httpResponse
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|httpResponse
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No Content"
argument_list|,
name|EntityUtils
operator|.
name|toString
argument_list|(
name|httpResponse
operator|.
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoSwitchingNoCodeNettyHttpViaCamel ()
specifier|public
name|void
name|testNoSwitchingNoCodeNettyHttpViaCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|inExchange
init|=
name|this
operator|.
name|createExchangeWithBody
argument_list|(
literal|"Hello World"
argument_list|)
decl_stmt|;
name|Exchange
name|outExchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"netty-http:http://localhost:{{port}}/foo"
argument_list|,
name|inExchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|outExchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No Content"
argument_list|,
name|outExchange
operator|.
name|getMessage
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
name|NettyHttpMessage
name|message
init|=
name|outExchange
operator|.
name|getIn
argument_list|(
name|NettyHttpMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|FullHttpResponse
name|response
init|=
name|message
operator|.
name|getHttpResponse
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|status
argument_list|()
operator|.
name|code
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No Content"
argument_list|,
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoSwitchingNoCodeViaCamelRoute ()
specifier|public
name|void
name|testNoSwitchingNoCodeViaCamelRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|inExchange
init|=
name|this
operator|.
name|createExchangeWithBody
argument_list|(
literal|"Hello World"
argument_list|)
decl_stmt|;
name|Exchange
name|outExchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:foo"
argument_list|,
name|inExchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|outExchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No Content"
argument_list|,
name|outExchange
operator|.
name|getMessage
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
name|NettyHttpMessage
name|message
init|=
name|outExchange
operator|.
name|getIn
argument_list|(
name|NettyHttpMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|FullHttpResponse
name|response
init|=
name|message
operator|.
name|getHttpResponse
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|status
argument_list|()
operator|.
name|code
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No Content"
argument_list|,
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoSwitchingNoBodyViaHttp ()
specifier|public
name|void
name|testNoSwitchingNoBodyViaHttp
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpUriRequest
name|request
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/foobar"
argument_list|)
decl_stmt|;
name|HttpClient
name|httpClient
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|HttpResponse
name|httpResponse
init|=
name|httpClient
operator|.
name|execute
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|httpResponse
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|httpResponse
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|EntityUtils
operator|.
name|toString
argument_list|(
name|httpResponse
operator|.
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoSwitchingNoBodyNettyHttpViaCamel ()
specifier|public
name|void
name|testNoSwitchingNoBodyNettyHttpViaCamel
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|inExchange
init|=
name|this
operator|.
name|createExchangeWithBody
argument_list|(
literal|"Hello World"
argument_list|)
decl_stmt|;
name|Exchange
name|outExchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"netty-http:http://localhost:{{port}}/foobar"
argument_list|,
name|inExchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|outExchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|outExchange
operator|.
name|getMessage
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
name|NettyHttpMessage
name|message
init|=
name|outExchange
operator|.
name|getIn
argument_list|(
name|NettyHttpMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|FullHttpResponse
name|response
init|=
name|message
operator|.
name|getHttpResponse
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|status
argument_list|()
operator|.
name|code
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoSwitchingNoBodyViaCamelRoute ()
specifier|public
name|void
name|testNoSwitchingNoBodyViaCamelRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|inExchange
init|=
name|this
operator|.
name|createExchangeWithBody
argument_list|(
literal|"Hello World"
argument_list|)
decl_stmt|;
name|Exchange
name|outExchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:foobar"
argument_list|,
name|inExchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|outExchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|outExchange
operator|.
name|getMessage
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
name|NettyHttpMessage
name|message
init|=
name|outExchange
operator|.
name|getIn
argument_list|(
name|NettyHttpMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|FullHttpResponse
name|response
init|=
name|message
operator|.
name|getHttpResponse
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|status
argument_list|()
operator|.
name|code
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
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
literal|"netty-http:http://localhost:{{port}}/bar"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"netty-http:http://localhost:{{port}}/bar"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty-http:http://localhost:{{port}}/foo"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"No Content"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"netty-http:http://localhost:{{port}}/foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty-http:http://localhost:{{port}}/foobar"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|constant
argument_list|(
literal|200
argument_list|)
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foobar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"netty-http:http://localhost:{{port}}/foobar"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

