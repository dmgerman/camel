begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|CamelContext
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
name|ContextTestSupport
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
name|NamedNode
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
name|Route
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
name|spi
operator|.
name|RoutePolicy
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
name|RoutePolicyFactory
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
name|RoutePolicySupport
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
DECL|class|RoutePolicyFactoryTest
specifier|public
class|class
name|RoutePolicyFactoryTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testRoutePolicyFactory ()
specifier|public
name|void
name|testRoutePolicyFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"RoutePolicy"
argument_list|,
literal|"foo-route"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"RoutePolicy"
argument_list|,
literal|"bar-route"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello Foo"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bar"
argument_list|,
literal|"Hello Bar"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|class|MyRoutePolicyFactory
specifier|public
specifier|static
specifier|final
class|class
name|MyRoutePolicyFactory
implements|implements
name|RoutePolicyFactory
block|{
DECL|method|MyRoutePolicyFactory ()
specifier|public
name|MyRoutePolicyFactory
parameter_list|()
block|{         }
annotation|@
name|Override
DECL|method|createRoutePolicy (CamelContext camelContext, String routeId, NamedNode route)
specifier|public
name|RoutePolicy
name|createRoutePolicy
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|routeId
parameter_list|,
name|NamedNode
name|route
parameter_list|)
block|{
return|return
operator|new
name|MyRoutePolicy
argument_list|(
name|routeId
argument_list|)
return|;
block|}
block|}
DECL|class|MyRoutePolicy
specifier|private
specifier|static
specifier|final
class|class
name|MyRoutePolicy
extends|extends
name|RoutePolicySupport
block|{
DECL|field|routeId
specifier|private
specifier|final
name|String
name|routeId
decl_stmt|;
DECL|method|MyRoutePolicy (String routeId)
specifier|private
name|MyRoutePolicy
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{
name|this
operator|.
name|routeId
operator|=
name|routeId
expr_stmt|;
block|}
DECL|method|getRouteId ()
specifier|public
name|String
name|getRouteId
parameter_list|()
block|{
return|return
name|routeId
return|;
block|}
annotation|@
name|Override
DECL|method|onExchangeBegin (Route route, Exchange exchange)
specifier|public
name|void
name|onExchangeBegin
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"RoutePolicy"
argument_list|,
name|routeId
argument_list|)
expr_stmt|;
block|}
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
name|context
operator|.
name|addRoutePolicyFactory
argument_list|(
operator|new
name|MyRoutePolicyFactory
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo-route"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bar"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar-route"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

