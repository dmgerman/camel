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
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|RecipientListFunctionalTest
specifier|public
class|class
name|RecipientListFunctionalTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testRecipientList ()
specifier|public
name|void
name|testRecipientList
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|x
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:x"
argument_list|)
decl_stmt|;
name|x
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"answer"
argument_list|)
expr_stmt|;
name|x
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"OnPrepare"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|MockEndpoint
name|y
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:y"
argument_list|)
decl_stmt|;
name|y
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"answer"
argument_list|)
expr_stmt|;
name|y
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"OnPrepare"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|MockEndpoint
name|z
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:z"
argument_list|)
decl_stmt|;
name|z
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"answer"
argument_list|)
expr_stmt|;
name|z
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"OnPrepare"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:a"
argument_list|,
literal|"answer"
argument_list|,
literal|"Endpoints"
argument_list|,
literal|"mock:x,mock:y,mock:z"
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
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|recipientList
argument_list|()
operator|.
name|message
argument_list|(
name|m
lambda|->
name|m
operator|.
name|getHeader
argument_list|(
literal|"Endpoints"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
operator|.
name|onPrepare
argument_list|()
operator|.
name|message
argument_list|(
name|m
lambda|->
name|m
operator|.
name|setHeader
argument_list|(
literal|"OnPrepare"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

