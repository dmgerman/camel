begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
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
name|ExpressionBuilder
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
DECL|class|HttpGZipEncodingTest
specifier|public
class|class
name|HttpGZipEncodingTest
extends|extends
name|BaseJettyTest
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
name|Test
DECL|method|testHttpProducerWithGzip ()
specifier|public
name|void
name|testHttpProducerWithGzip
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/gzip"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<Hello>World</Hello>"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|,
literal|"gzip"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The response is wrong"
argument_list|,
literal|"<b>Hello World</b>"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGzipProxy ()
specifier|public
name|void
name|testGzipProxy
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/route"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<Hello>World</Hello>"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|,
literal|"gzip"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The response is wrong"
argument_list|,
literal|"<b>Hello World</b>"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGzipProducerWithGzipData ()
specifier|public
name|void
name|testGzipProducerWithGzipData
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:gzip"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"<Hello>World</Hello>"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|,
literal|"gzip"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The response is wrong"
argument_list|,
literal|"<b>Hello World</b>"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGzipGet ()
specifier|public
name|void
name|testGzipGet
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/gzip"
argument_list|,
literal|null
argument_list|,
literal|"Accept-Encoding"
argument_list|,
literal|"gzip"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The response is wrong"
argument_list|,
literal|"<b>Hello World for gzip</b>"
argument_list|,
name|response
argument_list|)
expr_stmt|;
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
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|port1
operator|=
name|getPort
argument_list|()
expr_stmt|;
name|port2
operator|=
name|getNextPort
argument_list|()
expr_stmt|;
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:gzip"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|gzip
argument_list|()
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SKIP_GZIP_ENCODING
argument_list|,
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/gzip"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|gzip
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|port1
operator|+
literal|"/gzip"
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
comment|// check the request method
name|HttpServletRequest
name|request
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
name|HTTP_SERVLET_REQUEST
argument_list|,
name|HttpServletRequest
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"POST"
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getMethod
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|requestBody
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
literal|"Get a wrong request string"
argument_list|,
literal|"<Hello>World</Hello>"
argument_list|,
name|requestBody
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
comment|// check the Accept Encoding header
name|String
name|header
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Accept-Encoding"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|header
operator|!=
literal|null
operator|&&
name|header
operator|.
name|indexOf
argument_list|(
literal|"gzip"
argument_list|)
operator|>
operator|-
literal|1
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<b>Hello World for gzip</b>"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
block|}
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|port2
operator|+
literal|"/route?bridgeEndpoint=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/gzip?bridgeEndpoint=true"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

