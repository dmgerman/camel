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
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|ExchangePattern
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
name|Header
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
DECL|class|MethodFilterTest
specifier|public
class|class
name|MethodFilterTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testSendMatchingMessage ()
specifier|public
name|void
name|testSendMatchingMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|body
init|=
literal|"<person name='James' city='London'/>"
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|body
argument_list|,
literal|"foo"
argument_list|,
literal|"London"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendNotMatchingMessage ()
specifier|public
name|void
name|testSendNotMatchingMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|body
init|=
literal|"<person name='Hiram' city='Tampa'/>"
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|body
argument_list|,
literal|"foo"
argument_list|,
literal|"Tampa"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// START SNIPPET: example
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|filter
argument_list|()
operator|.
name|method
argument_list|(
literal|"myBean"
argument_list|,
literal|"matches"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|super
operator|.
name|createJndiContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"myBean"
argument_list|,
operator|new
name|MyBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
comment|// START SNIPPET: filter
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|method|matches (@eaderR)String location)
specifier|public
name|boolean
name|matches
parameter_list|(
annotation|@
name|Header
argument_list|(
literal|"foo"
argument_list|)
name|String
name|location
parameter_list|)
block|{
return|return
literal|"London"
operator|.
name|equals
argument_list|(
name|location
argument_list|)
return|;
block|}
block|}
comment|// END SNIPPET: filter
block|}
end_class

end_unit

