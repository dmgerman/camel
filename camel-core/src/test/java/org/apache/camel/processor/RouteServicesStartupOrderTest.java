begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

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
name|camel
operator|.
name|impl
operator|.
name|DefaultCamelContext
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
name|RouteStartupOrder
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
name|service
operator|.
name|ServiceSupport
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
DECL|class|RouteServicesStartupOrderTest
specifier|public
class|class
name|RouteServicesStartupOrderTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|startOrder
specifier|private
specifier|static
name|String
name|startOrder
init|=
literal|""
decl_stmt|;
DECL|field|service1
specifier|private
name|MyServiceBean
name|service1
init|=
operator|new
name|MyServiceBean
argument_list|(
literal|"1"
argument_list|)
decl_stmt|;
DECL|field|service2
specifier|private
name|MyServiceBean
name|service2
init|=
operator|new
name|MyServiceBean
argument_list|(
literal|"2"
argument_list|)
decl_stmt|;
DECL|field|service3
specifier|private
name|MyServiceBean
name|service3
init|=
operator|new
name|MyServiceBean
argument_list|(
literal|"3"
argument_list|)
decl_stmt|;
DECL|field|service4
specifier|private
name|MyServiceBean
name|service4
init|=
operator|new
name|MyServiceBean
argument_list|(
literal|"4"
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testRouteServiceStartupOrder ()
specifier|public
name|void
name|testRouteServiceStartupOrder
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// assert correct order
name|DefaultCamelContext
name|dcc
init|=
operator|(
name|DefaultCamelContext
operator|)
name|context
decl_stmt|;
name|List
argument_list|<
name|RouteStartupOrder
argument_list|>
name|order
init|=
name|dcc
operator|.
name|getRouteStartupOrder
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|order
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"seda://foo"
argument_list|,
name|order
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getRoute
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://start"
argument_list|,
name|order
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getRoute
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"seda://bar"
argument_list|,
name|order
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getRoute
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://bar"
argument_list|,
name|order
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getRoute
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
comment|// assert route service was started in order as well
name|assertEquals
argument_list|(
literal|"2143"
argument_list|,
name|startOrder
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
literal|"direct:start"
argument_list|)
operator|.
name|startupOrder
argument_list|(
literal|2
argument_list|)
operator|.
name|process
argument_list|(
name|service1
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:foo"
argument_list|)
operator|.
name|startupOrder
argument_list|(
literal|1
argument_list|)
operator|.
name|process
argument_list|(
name|service2
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bar"
argument_list|)
operator|.
name|startupOrder
argument_list|(
literal|9
argument_list|)
operator|.
name|process
argument_list|(
name|service3
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:bar"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:bar"
argument_list|)
operator|.
name|startupOrder
argument_list|(
literal|5
argument_list|)
operator|.
name|process
argument_list|(
name|service4
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:other"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyServiceBean
specifier|public
class|class
name|MyServiceBean
extends|extends
name|ServiceSupport
implements|implements
name|Processor
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|started
specifier|private
name|boolean
name|started
decl_stmt|;
DECL|method|MyServiceBean (String name)
specifier|public
name|MyServiceBean
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|startOrder
operator|+=
name|name
expr_stmt|;
name|started
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|started
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|isStarted ()
specifier|public
name|boolean
name|isStarted
parameter_list|()
block|{
return|return
name|started
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setStarted (boolean started)
specifier|public
name|void
name|setStarted
parameter_list|(
name|boolean
name|started
parameter_list|)
block|{
name|this
operator|.
name|started
operator|=
name|started
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{         }
block|}
block|}
end_class

end_unit

