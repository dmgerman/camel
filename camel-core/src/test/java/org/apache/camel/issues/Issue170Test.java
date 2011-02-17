begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|issues
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|Issue170Test
specifier|public
class|class
name|Issue170Test
extends|extends
name|ContextTestSupport
block|{
DECL|field|qOne
specifier|protected
name|String
name|qOne
init|=
literal|"seda:Q1"
decl_stmt|;
DECL|field|qTwo
specifier|protected
name|String
name|qTwo
init|=
literal|"mock:Q2"
decl_stmt|;
DECL|field|qThree
specifier|protected
name|String
name|qThree
init|=
literal|"mock:Q3"
decl_stmt|;
DECL|method|testSendMessagesGetCorrectCounts ()
specifier|public
name|void
name|testSendMessagesGetCorrectCounts
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|q2
init|=
name|getMockEndpoint
argument_list|(
name|qTwo
argument_list|)
decl_stmt|;
name|MockEndpoint
name|q3
init|=
name|getMockEndpoint
argument_list|(
name|qThree
argument_list|)
decl_stmt|;
name|String
name|body1
init|=
literal|"<message id='1'/>"
decl_stmt|;
name|String
name|body2
init|=
literal|"<message id='2'/>"
decl_stmt|;
name|q2
operator|.
name|expectedBodiesReceived
argument_list|(
name|body1
argument_list|,
name|body2
argument_list|)
expr_stmt|;
name|q3
operator|.
name|expectedBodiesReceived
argument_list|(
name|body1
argument_list|,
name|body2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|body1
argument_list|,
literal|"counter"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|body2
argument_list|,
literal|"counter"
argument_list|,
literal|2
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
name|qOne
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|qOne
argument_list|)
operator|.
name|to
argument_list|(
name|qTwo
argument_list|,
name|qThree
argument_list|)
expr_stmt|;
comment|// write to Q3 but not to Q2
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

