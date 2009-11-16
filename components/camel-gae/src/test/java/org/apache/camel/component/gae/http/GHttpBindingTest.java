begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|http
package|;
end_package

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
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|urlfetch
operator|.
name|HTTPMethod
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|urlfetch
operator|.
name|HTTPRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|urlfetch
operator|.
name|MockHttpResponse
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
name|impl
operator|.
name|DefaultExchange
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
name|BeforeClass
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|http
operator|.
name|GHttpTestUtils
operator|.
name|createEndpoint
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|http
operator|.
name|GHttpTestUtils
operator|.
name|createRequest
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|http
operator|.
name|GHttpTestUtils
operator|.
name|getCamelContext
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_class
DECL|class|GHttpBindingTest
specifier|public
class|class
name|GHttpBindingTest
block|{
DECL|field|binding
specifier|private
specifier|static
name|GHttpBinding
name|binding
decl_stmt|;
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setUpClass ()
specifier|public
specifier|static
name|void
name|setUpClass
parameter_list|()
block|{
name|binding
operator|=
operator|new
name|GHttpBinding
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
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetRequestMethod ()
specifier|public
name|void
name|testGetRequestMethod
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|HTTPMethod
operator|.
name|GET
argument_list|,
name|binding
operator|.
name|getRequestMethod
argument_list|(
literal|null
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HTTPMethod
operator|.
name|POST
argument_list|,
name|binding
operator|.
name|getRequestMethod
argument_list|(
literal|null
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"DELETE"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HTTPMethod
operator|.
name|DELETE
argument_list|,
name|binding
operator|.
name|getRequestMethod
argument_list|(
literal|null
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetRequestUrl ()
specifier|public
name|void
name|testGetRequestUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|GHttpEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
literal|"ghttp://somewhere.com:9090/path"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://somewhere.com:9090/path"
argument_list|,
name|binding
operator|.
name|getRequestUrl
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|,
literal|"http://custom.org:8080/path"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://somewhere.com:9090/path"
argument_list|,
name|binding
operator|.
name|getRequestUrl
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|createEndpoint
argument_list|(
literal|"ghttp://somewhere.com:9090/path?bridgeEndpoint=false"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://custom.org:8080/path"
argument_list|,
name|binding
operator|.
name|getRequestUrl
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|,
literal|"ghttp://another.org:8080/path"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://another.org:8080/path"
argument_list|,
name|binding
operator|.
name|getRequestUrl
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
literal|"a=b"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://somewhere.com:9090/path?a=b"
argument_list|,
name|binding
operator|.
name|getRequestUrl
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|,
literal|"http://custom.org:8080/path"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://custom.org:8080/path?a=b"
argument_list|,
name|binding
operator|.
name|getRequestUrl
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetRequestUrlEncoding ()
specifier|public
name|void
name|testGetRequestUrlEncoding
parameter_list|()
throws|throws
name|Exception
block|{
name|GHttpEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
literal|"ghttp://somewhere.com:9090/path?bridgeEndpoint=false&a=b c"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://somewhere.com:9090/path?a=b+c"
argument_list|,
name|binding
operator|.
name|getRequestUrl
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
literal|"x=y z"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://somewhere.com:9090/path?x=y+z"
argument_list|,
name|binding
operator|.
name|getRequestUrl
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|,
literal|"http://custom.org:8080/path?d=e f"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://custom.org:8080/path?d=e+f"
argument_list|,
name|binding
operator|.
name|getRequestUrl
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
literal|"x=y z"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://custom.org:8080/path?x=y+z"
argument_list|,
name|binding
operator|.
name|getRequestUrl
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWriteRequestHeaders ()
specifier|public
name|void
name|testWriteRequestHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|GHttpEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
literal|"ghttp://somewhere.com:9090/path"
argument_list|)
decl_stmt|;
name|HTTPRequest
name|request
init|=
name|createRequest
argument_list|()
decl_stmt|;
comment|// Shouldn't be filtered out
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"test"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
comment|// Should be filtered out
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"org.apache.camel.whatever"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"CamelWhatever"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
literal|"x=y"
argument_list|)
expr_stmt|;
name|binding
operator|.
name|writeRequestHeaders
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|request
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|request
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|request
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWriteRequestBody ()
specifier|public
name|void
name|testWriteRequestBody
parameter_list|()
throws|throws
name|Exception
block|{
name|HTTPRequest
name|request
init|=
name|createRequest
argument_list|()
decl_stmt|;
name|String
name|body
init|=
literal|"abc"
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|binding
operator|.
name|writeRequestBody
argument_list|(
literal|null
argument_list|,
name|exchange
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|body
operator|.
name|getBytes
argument_list|()
argument_list|,
name|request
operator|.
name|getPayload
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWriteRequest ()
specifier|public
name|void
name|testWriteRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|GHttpEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
literal|"ghttp://somewhere.com:9090/path"
argument_list|)
decl_stmt|;
name|HTTPRequest
name|request
init|=
name|binding
operator|.
name|writeRequest
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://somewhere.com:9090/path"
argument_list|,
name|request
operator|.
name|getURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HTTPMethod
operator|.
name|GET
argument_list|,
name|request
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadResponseHeaders ()
specifier|public
name|void
name|testReadResponseHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|GHttpEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
literal|"ghttp://somewhere.com:9090/path"
argument_list|)
decl_stmt|;
name|MockHttpResponse
name|response
init|=
operator|new
name|MockHttpResponse
argument_list|(
literal|200
argument_list|)
decl_stmt|;
name|response
operator|.
name|addHeader
argument_list|(
literal|"test"
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|response
operator|.
name|addHeader
argument_list|(
literal|"content-type"
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|binding
operator|.
name|readResponseHeaders
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|exchange
operator|.
name|getOut
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
literal|"abc"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/plain"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"content-type"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadResponseBody ()
specifier|public
name|void
name|testReadResponseBody
parameter_list|()
throws|throws
name|Exception
block|{
name|MockHttpResponse
name|response
init|=
operator|new
name|MockHttpResponse
argument_list|(
literal|200
argument_list|)
decl_stmt|;
name|response
operator|.
name|setContent
argument_list|(
literal|"abc"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|readResponseBody
argument_list|(
literal|null
argument_list|,
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|InputStream
name|stream
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
operator|.
name|getBytes
argument_list|()
index|[
literal|0
index|]
argument_list|,
name|stream
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReadNullResponseBody ()
specifier|public
name|void
name|testReadNullResponseBody
parameter_list|()
throws|throws
name|Exception
block|{
name|MockHttpResponse
name|response
init|=
operator|new
name|MockHttpResponse
argument_list|(
literal|200
argument_list|)
decl_stmt|;
name|binding
operator|.
name|readResponseBody
argument_list|(
literal|null
argument_list|,
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|InputStream
name|stream
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|GHttpException
operator|.
name|class
argument_list|)
DECL|method|testFailureException ()
specifier|public
name|void
name|testFailureException
parameter_list|()
throws|throws
name|Exception
block|{
name|GHttpEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
literal|"ghttp://somewhere.com:9090/path"
argument_list|)
decl_stmt|;
name|MockHttpResponse
name|response
init|=
operator|new
name|MockHttpResponse
argument_list|(
literal|500
argument_list|)
decl_stmt|;
name|binding
operator|.
name|readResponse
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFailureNoException ()
specifier|public
name|void
name|testFailureNoException
parameter_list|()
throws|throws
name|Exception
block|{
name|GHttpEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
literal|"ghttp://somewhere.com:9090/path?throwExceptionOnFailure=false"
argument_list|)
decl_stmt|;
name|MockHttpResponse
name|response
init|=
operator|new
name|MockHttpResponse
argument_list|(
literal|500
argument_list|)
decl_stmt|;
name|binding
operator|.
name|readResponse
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|exchange
operator|.
name|getOut
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
block|}
block|}
end_class

end_unit

