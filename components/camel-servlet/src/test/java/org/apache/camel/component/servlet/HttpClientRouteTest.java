begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servlet
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|PostMethodWebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|servletunit
operator|.
name|ServletUnitClient
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
name|FailedToCreateProducerException
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
DECL|class|HttpClientRouteTest
specifier|public
class|class
name|HttpClientRouteTest
extends|extends
name|ServletCamelRouterTestSupport
block|{
DECL|field|POST_DATA
specifier|private
specifier|static
specifier|final
name|String
name|POST_DATA
init|=
literal|"<request> hello world</request>"
decl_stmt|;
DECL|field|CONTENT_TYPE
specifier|private
specifier|static
specifier|final
name|String
name|CONTENT_TYPE
init|=
literal|"text/xml"
decl_stmt|;
DECL|field|UNICODE_TEXT
specifier|private
specifier|static
specifier|final
name|String
name|UNICODE_TEXT
init|=
literal|"B\u00FCe W\u00F6rld"
decl_stmt|;
annotation|@
name|Test
DECL|method|testHttpClient ()
specifier|public
name|void
name|testHttpClient
parameter_list|()
throws|throws
name|Exception
block|{
name|WebRequest
name|req
init|=
operator|new
name|PostMethodWebRequest
argument_list|(
name|CONTEXT_URL
operator|+
literal|"/services/hello"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|POST_DATA
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
literal|"text/xml; charset=UTF-8"
argument_list|)
decl_stmt|;
name|ServletUnitClient
name|client
init|=
name|newClient
argument_list|()
decl_stmt|;
name|WebResponse
name|response
init|=
name|client
operator|.
name|getResponse
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get wrong content type"
argument_list|,
literal|"text/xml"
argument_list|,
name|response
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"UTF-8"
operator|.
name|equalsIgnoreCase
argument_list|(
name|response
operator|.
name|getCharacterSet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong message header"
argument_list|,
literal|"/hello"
argument_list|,
name|response
operator|.
name|getHeaderField
argument_list|(
literal|"PATH"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The response message is wrong "
argument_list|,
literal|"OK"
argument_list|,
name|response
operator|.
name|getResponseMessage
argument_list|()
argument_list|)
expr_stmt|;
name|req
operator|=
operator|new
name|PostMethodWebRequest
argument_list|(
name|CONTEXT_URL
operator|+
literal|"/services/helloworld"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|POST_DATA
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
literal|"text/xml; charset=UTF-8"
argument_list|)
expr_stmt|;
name|response
operator|=
name|client
operator|.
name|getResponse
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get wrong content type"
argument_list|,
literal|"text/xml"
argument_list|,
name|response
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"UTF-8"
operator|.
name|equalsIgnoreCase
argument_list|(
name|response
operator|.
name|getCharacterSet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong message header"
argument_list|,
literal|"/helloworld"
argument_list|,
name|response
operator|.
name|getHeaderField
argument_list|(
literal|"PATH"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The response message is wrong "
argument_list|,
literal|"OK"
argument_list|,
name|response
operator|.
name|getResponseMessage
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|setExceptionsThrownOnErrorStatus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpConverter ()
specifier|public
name|void
name|testHttpConverter
parameter_list|()
throws|throws
name|Exception
block|{
name|WebRequest
name|req
init|=
operator|new
name|PostMethodWebRequest
argument_list|(
name|CONTEXT_URL
operator|+
literal|"/services/testConverter"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|POST_DATA
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
literal|"text/xml; charset=UTF-8"
argument_list|)
decl_stmt|;
name|ServletUnitClient
name|client
init|=
name|newClient
argument_list|()
decl_stmt|;
name|client
operator|.
name|setExceptionsThrownOnErrorStatus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|WebResponse
name|response
init|=
name|client
operator|.
name|getResponse
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The response message is wrong "
argument_list|,
literal|"OK"
argument_list|,
name|response
operator|.
name|getResponseMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The response body is wrong"
argument_list|,
literal|"Bye World"
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpUnicodeResponseWithStringResponse ()
specifier|public
name|void
name|testHttpUnicodeResponseWithStringResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|WebRequest
name|req
init|=
operator|new
name|PostMethodWebRequest
argument_list|(
name|CONTEXT_URL
operator|+
literal|"/services/testUnicodeWithStringResponse"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|POST_DATA
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
literal|"text/xml; charset=UTF-8"
argument_list|)
decl_stmt|;
name|ServletUnitClient
name|client
init|=
name|newClient
argument_list|()
decl_stmt|;
name|client
operator|.
name|setExceptionsThrownOnErrorStatus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|WebResponse
name|response
init|=
name|client
operator|.
name|getResponse
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The response message is wrong "
argument_list|,
literal|"OK"
argument_list|,
name|response
operator|.
name|getResponseMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The response body is wrong"
argument_list|,
name|UNICODE_TEXT
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpUnicodeResponseWithObjectResponse ()
specifier|public
name|void
name|testHttpUnicodeResponseWithObjectResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|WebRequest
name|req
init|=
operator|new
name|PostMethodWebRequest
argument_list|(
name|CONTEXT_URL
operator|+
literal|"/services/testUnicodeWithObjectResponse"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|POST_DATA
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
literal|"text/xml; charset=UTF-8"
argument_list|)
decl_stmt|;
name|ServletUnitClient
name|client
init|=
name|newClient
argument_list|()
decl_stmt|;
name|client
operator|.
name|setExceptionsThrownOnErrorStatus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|WebResponse
name|response
init|=
name|client
operator|.
name|getResponse
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The response message is wrong "
argument_list|,
literal|"OK"
argument_list|,
name|response
operator|.
name|getResponseMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The response body is wrong"
argument_list|,
name|UNICODE_TEXT
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateSerlvetEndpointProducer ()
specifier|public
name|void
name|testCreateSerlvetEndpointProducer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|startCamelContext
condition|)
block|{
comment|// don't test it with web.xml configure
return|return;
block|}
try|try
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"servlet:///testworld"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Excepts exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Get a wrong exception."
argument_list|,
name|ex
operator|instanceof
name|FailedToCreateProducerException
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong cause of exception."
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|UnsupportedOperationException
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|MyServletRoute
specifier|public
specifier|static
class|class
name|MyServletRoute
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
comment|// START SNIPPET: route
name|from
argument_list|(
literal|"servlet:///hello?matchOnUriPrefix=true"
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
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|contentType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong content type"
argument_list|,
name|CONTENT_TYPE
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
comment|// assert camel http header
name|String
name|charsetEncoding
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_CHARACTER_ENCODING
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong charset name from the message heaer"
argument_list|,
literal|"UTF-8"
argument_list|,
name|charsetEncoding
argument_list|)
expr_stmt|;
comment|// assert exchange charset
name|assertEquals
argument_list|(
literal|"Get a wrong charset naem from the exchange property"
argument_list|,
literal|"UTF-8"
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|contentType
operator|+
literal|"; charset=UTF-8"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"PATH"
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<b>Hello World</b>"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// END SNIPPET: route
name|from
argument_list|(
literal|"servlet:///testConverter?matchOnUriPrefix=true"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
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
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|HttpServletRequest
name|request
init|=
name|exchange
operator|.
name|getIn
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get request object here"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|HttpServletResponse
name|response
init|=
name|exchange
operator|.
name|getIn
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get response object here"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|exchange
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
name|assertEquals
argument_list|(
literal|"<request> hello world</request>"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"servlet:///testUnicodeWithStringResponse?matchOnUriPrefix=true"
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
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|contentType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|contentType
operator|+
literal|"; charset=UTF-8"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
name|UNICODE_TEXT
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"servlet:///testUnicodeWithObjectResponse?matchOnUriPrefix=true"
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
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|contentType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|contentType
operator|+
literal|"; charset=UTF-8"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
operator|new
name|Object
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|UNICODE_TEXT
return|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
name|MyServletRoute
argument_list|()
return|;
block|}
block|}
end_class

end_unit

