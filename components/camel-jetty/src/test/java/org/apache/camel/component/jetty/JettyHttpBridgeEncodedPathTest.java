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
DECL|class|JettyHttpBridgeEncodedPathTest
specifier|public
class|class
name|JettyHttpBridgeEncodedPathTest
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
DECL|method|testJettyHttpClient ()
specifier|public
name|void
name|testJettyHttpClient
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
literal|"/jettyTestRouteA?param1=%2B447777111222"
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
name|Object
name|s
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"param1"
argument_list|)
decl_stmt|;
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
literal|"jetty://http://localhost:"
operator|+
name|port2
operator|+
literal|"/jettyTestRouteA?matchOnUriPrefix=true"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Using JettyTestRouteA route: CamelHttpPath=[${header.CamelHttpPath}], CamelHttpUri=[${header.CamelHttpUri}]"
argument_list|)
comment|// TODO: Jetty has a bug in its client so use http for now
comment|// .to("jetty://http://localhost:" + port1 + "/jettyTestRouteB?throwExceptionOnFailure=false&bridgeEndpoint=true");
operator|.
name|to
argument_list|(
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/jettyTestRouteB?throwExceptionOnFailure=false&bridgeEndpoint=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty://http://localhost:"
operator|+
name|port1
operator|+
literal|"/jettyTestRouteB?matchOnUriPrefix=true"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Using JettyTestRouteB route: CamelHttpPath=[${header.CamelHttpPath}], CamelHttpUri=[${header.CamelHttpUri}]"
argument_list|)
operator|.
name|process
argument_list|(
name|serviceProc
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

