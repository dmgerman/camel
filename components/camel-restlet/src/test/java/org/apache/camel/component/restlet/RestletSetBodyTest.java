begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
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
name|impl
operator|.
name|client
operator|.
name|CloseableHttpClient
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Encoding
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|engine
operator|.
name|application
operator|.
name|EncodeRepresentation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|representation
operator|.
name|InputRepresentation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|representation
operator|.
name|StringRepresentation
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assume
operator|.
name|assumeThat
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|RestletSetBodyTest
specifier|public
class|class
name|RestletSetBodyTest
extends|extends
name|RestletTestSupport
block|{
DECL|field|portNum2
specifier|protected
specifier|static
name|int
name|portNum2
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|4000
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testSetBody ()
specifier|public
name|void
name|testSetBody
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/stock/ORCL?restletMethod=get"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"110"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSetBodyRepresentation ()
specifier|public
name|void
name|testSetBodyRepresentation
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpGet
name|get
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:"
operator|+
name|portNum
operator|+
literal|"/images/123"
argument_list|)
decl_stmt|;
name|CloseableHttpClient
name|httpclient
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|get
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"image/png"
argument_list|,
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContentType
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|is
operator|=
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContent
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get wrong available size"
argument_list|,
literal|256
argument_list|,
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContentLength
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|256
index|]
decl_stmt|;
name|assumeThat
argument_list|(
literal|"Should read all data"
argument_list|,
name|is
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|256
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|"Data should match"
argument_list|,
name|buffer
argument_list|,
name|equalTo
argument_list|(
name|getAllBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|httpclient
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|consumerShouldReturnByteArray ()
specifier|public
name|void
name|consumerShouldReturnByteArray
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpGet
name|get
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:"
operator|+
name|portNum
operator|+
literal|"/music/123"
argument_list|)
decl_stmt|;
name|CloseableHttpClient
name|httpclient
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|get
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"audio/mpeg"
argument_list|,
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContentType
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|is
operator|=
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContent
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Content length should match returned data"
argument_list|,
literal|256
argument_list|,
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContentLength
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|256
index|]
decl_stmt|;
name|assumeThat
argument_list|(
literal|"Should read all data"
argument_list|,
name|is
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|256
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|"Binary content should match"
argument_list|,
name|buffer
argument_list|,
name|equalTo
argument_list|(
name|getAllBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|httpclient
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|consumerShouldReturnInputStream ()
specifier|public
name|void
name|consumerShouldReturnInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpGet
name|get
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:"
operator|+
name|portNum
operator|+
literal|"/video/123"
argument_list|)
decl_stmt|;
name|CloseableHttpClient
name|httpclient
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|get
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"video/mp4"
argument_list|,
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContentType
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Content should be streamed"
argument_list|,
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|isChunked
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Content length should be unknown"
argument_list|,
operator|-
literal|1
argument_list|,
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContentLength
argument_list|()
argument_list|)
expr_stmt|;
name|is
operator|=
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContent
argument_list|()
expr_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|256
index|]
decl_stmt|;
name|assumeThat
argument_list|(
literal|"Should read all data"
argument_list|,
name|is
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|256
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|"Binary content should match"
argument_list|,
name|buffer
argument_list|,
name|equalTo
argument_list|(
name|getAllBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|httpclient
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testGzipEntity ()
specifier|public
name|void
name|testGzipEntity
parameter_list|()
block|{
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/gzip/data?restletMethod=get"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World!"
argument_list|,
name|response
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
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/stock/{symbol}?restletMethods=get"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:"
operator|+
name|portNum2
operator|+
literal|"/test?bridgeEndpoint=true"
argument_list|)
comment|//.removeHeader("Transfer-Encoding")
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"110"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|portNum2
operator|+
literal|"/test"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"response is back"
argument_list|)
expr_stmt|;
comment|// create ByteArrayRepresentation for response
name|from
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/images/{symbol}?restletMethods=get"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
operator|new
name|InputRepresentation
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|getAllBytes
argument_list|()
argument_list|)
argument_list|,
name|MediaType
operator|.
name|IMAGE_PNG
argument_list|,
literal|256
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/music/{symbol}?restletMethods=get"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|)
operator|.
name|constant
argument_list|(
literal|"audio/mpeg"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
name|getAllBytes
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/video/{symbol}?restletMethods=get"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|)
operator|.
name|constant
argument_list|(
literal|"video/mp4"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|getAllBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/gzip/data?restletMethods=get"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
operator|new
name|EncodeRepresentation
argument_list|(
name|Encoding
operator|.
name|GZIP
argument_list|,
operator|new
name|StringRepresentation
argument_list|(
literal|"Hello World!"
argument_list|,
name|MediaType
operator|.
name|TEXT_XML
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|getAllBytes ()
specifier|private
specifier|static
name|byte
index|[]
name|getAllBytes
parameter_list|()
block|{
name|byte
index|[]
name|data
init|=
operator|new
name|byte
index|[
literal|256
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|256
condition|;
name|i
operator|++
control|)
block|{
name|data
index|[
name|i
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|Byte
operator|.
name|MIN_VALUE
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
return|return
name|data
return|;
block|}
block|}
end_class

end_unit

