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
import|import static
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|singleton
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
name|EndpointInject
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
operator|.
name|HTTP_QUERY
import|;
end_import

begin_class
DECL|class|NettyHttpHeaderFilterStrategyRemovalTest
specifier|public
class|class
name|NettyHttpHeaderFilterStrategyRemovalTest
extends|extends
name|BaseNettyTest
block|{
DECL|field|headerFilterStrategy
name|NettyHttpHeaderFilterStrategy
name|headerFilterStrategy
init|=
operator|new
name|NettyHttpHeaderFilterStrategy
argument_list|()
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:test"
argument_list|)
DECL|field|mockEndpoint
name|MockEndpoint
name|mockEndpoint
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldRemoveStrategyOption ()
specifier|public
name|void
name|shouldRemoveStrategyOption
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|options
init|=
literal|"headerFilterStrategy=#headerFilterStrategy"
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|HTTP_QUERY
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"netty4-http:http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/?"
operator|+
name|options
argument_list|,
literal|"message"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldResolveStrategyFromParameter ()
specifier|public
name|void
name|shouldResolveStrategyFromParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|headerToFilter
init|=
literal|"foo"
decl_stmt|;
name|headerFilterStrategy
operator|.
name|setOutFilter
argument_list|(
name|singleton
argument_list|(
name|headerToFilter
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|options
init|=
literal|"headerFilterStrategy=#headerFilterStrategy"
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|headerToFilter
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"netty4-http:http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/?"
operator|+
name|options
argument_list|,
literal|"message"
argument_list|,
name|headerToFilter
argument_list|,
literal|"headerValue"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"headerFilterStrategy"
argument_list|,
name|headerFilterStrategy
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
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
literal|"netty4-http:http://0.0.0.0:{{port}}/"
argument_list|)
operator|.
name|to
argument_list|(
name|mockEndpoint
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

