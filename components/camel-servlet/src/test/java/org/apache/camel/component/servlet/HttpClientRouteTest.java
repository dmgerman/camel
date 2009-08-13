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
literal|"servlet:///hello"
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
literal|"Get a wrong charset name"
argument_list|,
literal|"UTF-8"
argument_list|,
name|charsetEncoding
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

