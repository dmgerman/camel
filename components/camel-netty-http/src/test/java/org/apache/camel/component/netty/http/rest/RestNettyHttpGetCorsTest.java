begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http.rest
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
operator|.
name|rest
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
name|component
operator|.
name|netty
operator|.
name|http
operator|.
name|BaseNettyTest
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
name|spi
operator|.
name|RestConfiguration
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
DECL|class|RestNettyHttpGetCorsTest
specifier|public
class|class
name|RestNettyHttpGetCorsTest
extends|extends
name|BaseNettyTest
block|{
annotation|@
name|Test
DECL|method|testCors ()
specifier|public
name|void
name|testCors
parameter_list|()
throws|throws
name|Exception
block|{
comment|// send OPTIONS first which should not be routed
name|getMockEndpoint
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|template
operator|.
name|request
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/users/123/basic"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
literal|"OPTIONS"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|RestConfiguration
operator|.
name|CORS_ACCESS_CONTROL_ALLOW_ORIGIN
argument_list|,
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Access-Control-Allow-Origin"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RestConfiguration
operator|.
name|CORS_ACCESS_CONTROL_ALLOW_METHODS
argument_list|,
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Access-Control-Allow-Methods"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RestConfiguration
operator|.
name|CORS_ACCESS_CONTROL_ALLOW_HEADERS
argument_list|,
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Access-Control-Allow-Headers"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RestConfiguration
operator|.
name|CORS_ACCESS_CONTROL_MAX_AGE
argument_list|,
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Access-Control-Max-Age"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|resetMocks
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// send GET request which should be routed
name|String
name|out2
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/users/123/basic"
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
literal|"123;Donald Duck"
argument_list|,
name|out2
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// configure to use netty-http on localhost with the given port
name|restConfiguration
argument_list|()
operator|.
name|component
argument_list|(
literal|"netty-http"
argument_list|)
operator|.
name|host
argument_list|(
literal|"localhost"
argument_list|)
operator|.
name|port
argument_list|(
name|getPort
argument_list|()
argument_list|)
operator|.
name|enableCORS
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// use the rest DSL to define the rest services
name|rest
argument_list|(
literal|"/users/"
argument_list|)
operator|.
name|get
argument_list|(
literal|"{id}/basic"
argument_list|)
operator|.
name|route
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:input"
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
name|id
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"id"
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
name|setBody
argument_list|(
name|id
operator|+
literal|";Donald Duck"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

