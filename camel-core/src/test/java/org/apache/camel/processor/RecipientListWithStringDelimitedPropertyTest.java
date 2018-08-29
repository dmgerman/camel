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
DECL|class|RecipientListWithStringDelimitedPropertyTest
specifier|public
class|class
name|RecipientListWithStringDelimitedPropertyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|BODY
specifier|private
specifier|static
specifier|final
name|String
name|BODY
init|=
literal|"answer"
decl_stmt|;
DECL|field|PROPERTY_VALUE
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_VALUE
init|=
literal|"mock:x, mock:y, mock:z"
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendingAMessageUsingMulticastReceivesItsOwnExchange ()
specifier|public
name|void
name|testSendingAMessageUsingMulticastReceivesItsOwnExchange
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
name|MockEndpoint
name|y
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:y"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|z
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:z"
argument_list|)
decl_stmt|;
name|x
operator|.
name|expectedBodiesReceived
argument_list|(
name|BODY
argument_list|)
expr_stmt|;
name|y
operator|.
name|expectedBodiesReceived
argument_list|(
name|BODY
argument_list|)
expr_stmt|;
name|z
operator|.
name|expectedBodiesReceived
argument_list|(
name|BODY
argument_list|)
expr_stmt|;
name|x
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
literal|"myProperty"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|PROPERTY_VALUE
argument_list|)
expr_stmt|;
name|y
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
literal|"myProperty"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|PROPERTY_VALUE
argument_list|)
expr_stmt|;
name|z
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|exchangeProperty
argument_list|(
literal|"myProperty"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|PROPERTY_VALUE
argument_list|)
expr_stmt|;
name|sendBody
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|sendBody ()
specifier|protected
name|void
name|sendBody
parameter_list|()
block|{
name|template
operator|.
name|sendBodyAndProperty
argument_list|(
literal|"direct:a"
argument_list|,
name|BODY
argument_list|,
literal|"myProperty"
argument_list|,
name|PROPERTY_VALUE
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
literal|"direct:a"
argument_list|)
operator|.
name|recipientList
argument_list|(
name|property
argument_list|(
literal|"myProperty"
argument_list|)
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

