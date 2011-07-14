begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|http
operator|.
name|Header
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
name|HttpException
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
name|HttpRequest
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
name|HttpStatus
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
name|entity
operator|.
name|StringEntity
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
name|localserver
operator|.
name|LocalTestServer
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
name|protocol
operator|.
name|HTTP
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
name|protocol
operator|.
name|HttpContext
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
name|protocol
operator|.
name|HttpRequestHandler
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
comment|/**  *  */
end_comment

begin_class
DECL|class|HttpProducerTwoParametersWithSameKeyTest
specifier|public
class|class
name|HttpProducerTwoParametersWithSameKeyTest
extends|extends
name|BaseHttpTest
block|{
annotation|@
name|Test
DECL|method|testTwoParametersWithSameKey ()
specifier|public
name|void
name|testTwoParametersWithSameKey
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|out
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http4://"
operator|+
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/myapp?from=me&to=foo&to=bar"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should not fail"
argument_list|,
name|out
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"OK"
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
name|assertEquals
argument_list|(
literal|"yes"
argument_list|,
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|foo
init|=
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"foo"
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|foo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|foo
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|foo
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"456"
argument_list|,
name|foo
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|registerHandler (LocalTestServer server)
specifier|protected
name|void
name|registerHandler
parameter_list|(
name|LocalTestServer
name|server
parameter_list|)
block|{
name|server
operator|.
name|register
argument_list|(
literal|"/myapp"
argument_list|,
operator|new
name|HttpRequestHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|HttpRequest
name|request
parameter_list|,
name|HttpResponse
name|response
parameter_list|,
name|HttpContext
name|context
parameter_list|)
throws|throws
name|HttpException
throws|,
name|IOException
block|{
name|String
name|uri
init|=
name|request
operator|.
name|getRequestLine
argument_list|()
operator|.
name|getUri
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/myapp?from=me&to=foo&to=bar"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|response
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|response
operator|.
name|addHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|response
operator|.
name|addHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"456"
argument_list|)
expr_stmt|;
name|response
operator|.
name|setEntity
argument_list|(
operator|new
name|StringEntity
argument_list|(
literal|"OK"
argument_list|,
name|HTTP
operator|.
name|ASCII
argument_list|)
argument_list|)
expr_stmt|;
name|response
operator|.
name|setStatusCode
argument_list|(
name|HttpStatus
operator|.
name|SC_OK
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

