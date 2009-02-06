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
import|import static
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
operator|.
name|expectsMessageCount
import|;
end_import

begin_class
DECL|class|LoadBalanceTest
specifier|public
class|class
name|LoadBalanceTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|x
specifier|protected
name|MockEndpoint
name|x
decl_stmt|;
DECL|field|y
specifier|protected
name|MockEndpoint
name|y
decl_stmt|;
DECL|field|z
specifier|protected
name|MockEndpoint
name|z
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|x
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:x"
argument_list|)
expr_stmt|;
name|y
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:y"
argument_list|)
expr_stmt|;
name|z
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:z"
argument_list|)
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
name|loadBalance
argument_list|()
operator|.
name|roundRobin
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:x"
argument_list|,
literal|"mock:y"
argument_list|,
literal|"mock:z"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
return|;
block|}
DECL|method|testRoundRobin ()
specifier|public
name|void
name|testRoundRobin
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|body
init|=
literal|"<one/>"
decl_stmt|;
name|x
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|expectsMessageCount
argument_list|(
literal|0
argument_list|,
name|y
argument_list|,
name|z
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"bar"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|x
operator|.
name|reset
argument_list|()
expr_stmt|;
name|body
operator|=
literal|"<two/>"
expr_stmt|;
name|y
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|expectsMessageCount
argument_list|(
literal|0
argument_list|,
name|x
argument_list|,
name|z
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"bar"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|y
operator|.
name|reset
argument_list|()
expr_stmt|;
name|body
operator|=
literal|"<three/>"
expr_stmt|;
name|z
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|expectsMessageCount
argument_list|(
literal|0
argument_list|,
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"bar"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|sendMessage (final Object headerValue, final Object body)
specifier|protected
name|void
name|sendMessage
parameter_list|(
specifier|final
name|Object
name|headerValue
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|)
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|body
argument_list|,
literal|"foo"
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

