begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mock
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mock
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
name|ExpressionBuilder
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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|MockAsBeanTest
specifier|public
class|class
name|MockAsBeanTest
extends|extends
name|ContextTestSupport
block|{
comment|// create foo bean as a mock endpoint
DECL|field|foo
specifier|private
name|MockEndpoint
name|foo
init|=
operator|new
name|MockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
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
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"foo"
argument_list|,
name|foo
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
comment|// START SNIPPET: e1
DECL|method|testMockAsBeanWithWhenAnyExchangeReceived ()
specifier|public
name|void
name|testMockAsBeanWithWhenAnyExchangeReceived
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we should expect to receive the transformed message
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
comment|// the foo bean is a MockEndpoint which we use in this test to transform
comment|// the message
name|foo
operator|.
name|whenAnyExchangeReceived
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
name|in
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
name|in
argument_list|)
expr_stmt|;
block|}
block|}
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
block|}
comment|// END SNIPPET: e1
annotation|@
name|Override
comment|// START SNIPPET: e2
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
comment|// send to foo bean
operator|.
name|beanRef
argument_list|(
literal|"foo"
argument_list|)
comment|// and then to result mock
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// END SNIPPET: e2
comment|// START SNIPPET: e3
DECL|method|testMockAsBeanWithReplyBody ()
specifier|public
name|void
name|testMockAsBeanWithReplyBody
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we should expect to receive the transformed message
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|foo
operator|.
name|returnReplyBody
argument_list|(
name|ExpressionBuilder
operator|.
name|simpleExpression
argument_list|(
literal|"Bye ${body}"
argument_list|)
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
block|}
comment|// END SNIPPET: e3
comment|// START SNIPPET: e4
DECL|method|testMockAsBeanWithReplyHeader ()
specifier|public
name|void
name|testMockAsBeanWithReplyHeader
parameter_list|()
throws|throws
name|Exception
block|{
comment|// we should expect to receive the transformed message
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"myHeader"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|foo
operator|.
name|returnReplyHeader
argument_list|(
literal|"myHeader"
argument_list|,
name|ExpressionBuilder
operator|.
name|simpleExpression
argument_list|(
literal|"Bye ${body}"
argument_list|)
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
block|}
comment|// END SNIPPET: e4
block|}
end_class

end_unit

