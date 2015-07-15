begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|UndertowHeaderTest
specifier|public
class|class
name|UndertowHeaderTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testHttpHeaders ()
specifier|public
name|void
name|testHttpHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"param"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
name|Exchange
operator|.
name|HTTP_URL
argument_list|,
literal|"http://localhost:8888/headers"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|,
literal|"/headers"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
literal|"param=true"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
literal|"/headers"
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:8888/headers?param=true"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
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
name|from
argument_list|(
literal|"undertow:http://localhost:8888/headers"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

