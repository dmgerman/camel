begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|WireTapUsingFireAndForgetCopyTest
specifier|public
class|class
name|WireTapUsingFireAndForgetCopyTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testFireAndForgetUsingProcessor ()
specifier|public
name|void
name|testFireAndForgetUsingProcessor
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
comment|// START SNIPPET: e1
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|wireTap
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|copy
argument_list|()
operator|.
name|newExchange
argument_list|(
name|exchange
lambda|->
block|{
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye "
operator|+
name|body
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"World"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|foo
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|foo
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|foo
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// should be different exchange instances
name|Exchange
name|e1
init|=
name|result
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Exchange
name|e2
init|=
name|foo
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
literal|"Should not be same Exchange"
argument_list|,
name|e1
argument_list|,
name|e2
argument_list|)
expr_stmt|;
comment|// should have same from endpoint
name|assertEquals
argument_list|(
literal|"direct://start"
argument_list|,
name|e1
operator|.
name|getFromEndpoint
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
name|e2
operator|.
name|getFromEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFireAndForgetUsingProcessor2 ()
specifier|public
name|void
name|testFireAndForgetUsingProcessor2
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|wireTap
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|copy
argument_list|()
operator|.
name|newExchange
argument_list|(
name|exchange
lambda|->
block|{
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye "
operator|+
name|body
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"World"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|foo
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|foo
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|foo
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// should be different exchange instances
name|Exchange
name|e1
init|=
name|result
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Exchange
name|e2
init|=
name|foo
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
literal|"Should not be same Exchange"
argument_list|,
name|e1
argument_list|,
name|e2
argument_list|)
expr_stmt|;
comment|// should have same from endpoint
name|assertEquals
argument_list|(
literal|"direct://start"
argument_list|,
name|e1
operator|.
name|getFromEndpoint
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
name|e2
operator|.
name|getFromEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFireAndForgetUsingExpression ()
specifier|public
name|void
name|testFireAndForgetUsingExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
comment|// START SNIPPET: e2
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|wireTap
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|copy
argument_list|(
literal|true
argument_list|)
operator|.
name|newExchangeBody
argument_list|(
name|simple
argument_list|(
literal|"Bye ${body}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e2
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"World"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|foo
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|foo
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// should be different exchange instances
name|Exchange
name|e1
init|=
name|result
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Exchange
name|e2
init|=
name|foo
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
literal|"Should not be same Exchange"
argument_list|,
name|e1
argument_list|,
name|e2
argument_list|)
expr_stmt|;
comment|// should have same from endpoint
name|assertEquals
argument_list|(
literal|"direct://start"
argument_list|,
name|e1
operator|.
name|getFromEndpoint
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
name|e2
operator|.
name|getFromEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFireAndForgetUsingExpression2 ()
specifier|public
name|void
name|testFireAndForgetUsingExpression2
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|wireTap
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|copy
argument_list|(
literal|true
argument_list|)
operator|.
name|newExchangeBody
argument_list|(
name|simple
argument_list|(
literal|"Bye ${body}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"World"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|foo
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|foo
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// should be different exchange instances
name|Exchange
name|e1
init|=
name|result
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Exchange
name|e2
init|=
name|foo
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
literal|"Should not be same Exchange"
argument_list|,
name|e1
argument_list|,
name|e2
argument_list|)
expr_stmt|;
comment|// should have same from endpoint
name|assertEquals
argument_list|(
literal|"direct://start"
argument_list|,
name|e1
operator|.
name|getFromEndpoint
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
name|e2
operator|.
name|getFromEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

