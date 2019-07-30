begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
operator|.
name|http
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
name|BindToRegistry
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
name|camel
operator|.
name|impl
operator|.
name|JndiRegistry
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
name|support
operator|.
name|DefaultHeaderFilterStrategy
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
DECL|class|NettyHttpEndpointUriCustomHeaderFilterStrategyTest
specifier|public
class|class
name|NettyHttpEndpointUriCustomHeaderFilterStrategyTest
extends|extends
name|BaseNettyTest
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"customHeaderFilterStrategy"
argument_list|)
DECL|field|customHeaderFilterStrategy
specifier|private
name|CustomHeaderFilterStrategy
name|customHeaderFilterStrategy
init|=
operator|new
name|CustomHeaderFilterStrategy
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testEndpointUriWithCustomHeaderStrategy ()
specifier|public
name|void
name|testEndpointUriWithCustomHeaderStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:outbound"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"Date"
argument_list|,
literal|"31-03-2014"
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:request"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|String
name|date
init|=
name|out
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"sub-date"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|date
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
literal|"direct:request"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"Date"
argument_list|,
name|constant
argument_list|(
literal|"31-03-2014"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"netty4-http:http://localhost:{{port}}/myapp/mytest?headerFilterStrategy=#customHeaderFilterStrategy"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty4-http:http://localhost:{{port}}/myapp/mytest"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:outbound"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"sub-date"
argument_list|,
name|constant
argument_list|(
literal|"31-05-2014"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|CustomHeaderFilterStrategy
specifier|private
class|class
name|CustomHeaderFilterStrategy
extends|extends
name|DefaultHeaderFilterStrategy
block|{
DECL|method|CustomHeaderFilterStrategy ()
name|CustomHeaderFilterStrategy
parameter_list|()
block|{
comment|// allow all outbound headers to pass through but only filter out
comment|// below inbound header
name|getInFilter
argument_list|()
operator|.
name|add
argument_list|(
literal|"sub-date"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

