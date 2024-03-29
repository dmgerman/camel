begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|net
operator|.
name|URLEncoder
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
name|commons
operator|.
name|httpclient
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
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|GetMethod
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
annotation|@
name|Ignore
argument_list|(
literal|"TODO: fails locally and on CI server"
argument_list|)
DECL|class|HttpBridgeEncodedPathTest
specifier|public
class|class
name|HttpBridgeEncodedPathTest
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
DECL|field|port3
specifier|private
name|int
name|port3
decl_stmt|;
DECL|field|port4
specifier|private
name|int
name|port4
decl_stmt|;
annotation|@
name|Test
DECL|method|testEncodedQuery ()
specifier|public
name|void
name|testEncodedQuery
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
literal|"/test/hello?param1=%2B447777111222"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
literal|"This is a test"
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
literal|"Content-Type"
argument_list|,
literal|"text/plain"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response"
argument_list|,
literal|"param1=+447777111222"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEncodedPath ()
specifier|public
name|void
name|testEncodedPath
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
name|URLEncoder
operator|.
name|encode
argument_list|(
literal|" :/?#[]@!$"
argument_list|,
literal|"UTF-8"
argument_list|)
operator|+
literal|"/"
operator|+
name|URLEncoder
operator|.
name|encode
argument_list|(
literal|"&'()+,;="
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:encodedPath"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"/"
operator|+
name|path
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|HTTP_RAW_QUERY
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
comment|// cannot use template as it automatically decodes some chars in the
comment|// path
name|HttpClient
name|httpClient
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
name|GetMethod
name|httpGet
init|=
operator|new
name|GetMethod
argument_list|(
literal|"http://localhost:"
operator|+
name|port4
operator|+
literal|"/test/"
operator|+
name|path
argument_list|)
decl_stmt|;
name|int
name|status
init|=
name|httpClient
operator|.
name|executeMethod
argument_list|(
name|httpGet
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response status"
argument_list|,
literal|200
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
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
name|getPort2
argument_list|()
expr_stmt|;
name|port3
operator|=
name|getNextPort
argument_list|()
expr_stmt|;
name|port4
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
name|Processor
name|serviceProc
init|=
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
comment|// %2B becomes decoded to a space
name|assertEquals
argument_list|(
literal|" 447777111222"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"param1"
argument_list|)
argument_list|)
expr_stmt|;
comment|// and in the http query %20 becomes a + sign
name|assertEquals
argument_list|(
literal|"param1=+447777111222"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|)
argument_list|)
expr_stmt|;
comment|// send back the query
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|port2
operator|+
literal|"/test/hello"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"?throwExceptionOnFailure=false&bridgeEndpoint=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|port1
operator|+
literal|"?matchOnUriPrefix=true"
argument_list|)
operator|.
name|process
argument_list|(
name|serviceProc
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|port4
operator|+
literal|"/test?matchOnUriPrefix=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://localhost:"
operator|+
name|port3
operator|+
literal|"?throwExceptionOnFailure=false&bridgeEndpoint=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:"
operator|+
name|port3
operator|+
literal|"?matchOnUriPrefix=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encodedPath"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

