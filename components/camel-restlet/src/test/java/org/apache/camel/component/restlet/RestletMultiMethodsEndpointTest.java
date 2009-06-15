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
name|junit4
operator|.
name|CamelTestSupport
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
name|HttpMethod
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
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|PostMethod
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
comment|/**  * This unit test verifies a single route can service multiple methods.  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|RestletMultiMethodsEndpointTest
specifier|public
class|class
name|RestletMultiMethodsEndpointTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testPostMethod ()
specifier|public
name|void
name|testPostMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpMethod
name|method
init|=
operator|new
name|PostMethod
argument_list|(
literal|"http://localhost:9080/users/homer"
argument_list|)
decl_stmt|;
try|try
block|{
name|HttpClient
name|client
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|client
operator|.
name|executeMethod
argument_list|(
name|method
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|method
operator|.
name|getResponseHeader
argument_list|(
literal|"Content-Type"
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"text/plain"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"POST"
argument_list|,
name|method
operator|.
name|getResponseBodyAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|method
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testGetMethod ()
specifier|public
name|void
name|testGetMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpMethod
name|method
init|=
operator|new
name|GetMethod
argument_list|(
literal|"http://localhost:9080/users/homer"
argument_list|)
decl_stmt|;
try|try
block|{
name|HttpClient
name|client
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|client
operator|.
name|executeMethod
argument_list|(
name|method
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|method
operator|.
name|getResponseHeader
argument_list|(
literal|"Content-Type"
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"text/plain"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GET"
argument_list|,
name|method
operator|.
name|getResponseBodyAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|method
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
comment|// START SNIPPET: routeDefinition
name|from
argument_list|(
literal|"restlet:http://localhost:9080/users/{username}?restletMethods=post,get"
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
comment|// echo the method
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
name|HTTP_METHOD
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// END SNIPPET: routeDefinition
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

