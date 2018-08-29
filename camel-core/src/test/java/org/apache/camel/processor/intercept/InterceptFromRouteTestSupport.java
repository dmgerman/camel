begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.intercept
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|intercept
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|InterceptFromRouteTestSupport
specifier|public
specifier|abstract
class|class
name|InterceptFromRouteTestSupport
extends|extends
name|ContextTestSupport
block|{
DECL|field|a
specifier|protected
name|MockEndpoint
name|a
decl_stmt|;
DECL|field|b
specifier|protected
name|MockEndpoint
name|b
decl_stmt|;
DECL|method|testSendMatchingMessage ()
specifier|public
name|void
name|testSendMatchingMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareMatchingTest
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<matched/>"
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testSendNonMatchingMessage ()
specifier|public
name|void
name|testSendNonMatchingMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareNonMatchingTest
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<notMatched/>"
argument_list|,
literal|"foo"
argument_list|,
literal|"notMatchedHeaderValue"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
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
name|a
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
name|b
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
block|}
DECL|method|prepareMatchingTest ()
specifier|protected
specifier|abstract
name|void
name|prepareMatchingTest
parameter_list|()
function_decl|;
DECL|method|prepareNonMatchingTest ()
specifier|protected
specifier|abstract
name|void
name|prepareNonMatchingTest
parameter_list|()
function_decl|;
block|}
end_class

end_unit

