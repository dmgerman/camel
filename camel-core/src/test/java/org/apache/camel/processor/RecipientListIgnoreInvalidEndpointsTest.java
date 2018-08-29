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
name|junit
operator|.
name|Test
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
name|ResolveEndpointFailedException
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

begin_class
DECL|class|RecipientListIgnoreInvalidEndpointsTest
specifier|public
class|class
name|RecipientListIgnoreInvalidEndpointsTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testRecipientListWithIgnoreInvalidEndpointsOption ()
specifier|public
name|void
name|testRecipientListWithIgnoreInvalidEndpointsOption
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"Hello World"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|endpointA
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:endpointA"
argument_list|)
decl_stmt|;
name|endpointA
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello a"
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:startA"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRecipientListWithoutIgnoreInvalidEndpointsOption ()
specifier|public
name|void
name|testRecipientListWithoutIgnoreInvalidEndpointsOption
parameter_list|()
throws|throws
name|Exception
block|{
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
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|MockEndpoint
name|endpointA
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:endpointA"
argument_list|)
decl_stmt|;
name|endpointA
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:startB"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expect the exception here."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Get a wrong cause of the exception"
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ResolveEndpointFailedException
argument_list|)
expr_stmt|;
block|}
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
name|from
argument_list|(
literal|"direct:startA"
argument_list|)
operator|.
name|recipientList
argument_list|(
name|simple
argument_list|(
literal|"mock:result,fail:endpoint,direct:a"
argument_list|)
argument_list|)
operator|.
name|ignoreInvalidEndpoints
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:startB"
argument_list|)
operator|.
name|recipientList
argument_list|(
name|simple
argument_list|(
literal|"mock:result,fail:endpoint,direct:a"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Hello a"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpointA"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

