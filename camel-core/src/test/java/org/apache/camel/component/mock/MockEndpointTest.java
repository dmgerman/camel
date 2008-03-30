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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|MockEndpointTest
specifier|public
class|class
name|MockEndpointTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testAscendingMessagesPass ()
specifier|public
name|void
name|testAscendingMessagesPass
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectsAscending
argument_list|(
name|header
argument_list|(
literal|"counter"
argument_list|)
operator|.
name|convertTo
argument_list|(
name|Number
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|sendMessages
argument_list|(
literal|11
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|15
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testAscendingMessagesFail ()
specifier|public
name|void
name|testAscendingMessagesFail
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectsAscending
argument_list|(
name|header
argument_list|(
literal|"counter"
argument_list|)
operator|.
name|convertTo
argument_list|(
name|Number
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|sendMessages
argument_list|(
literal|11
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|,
literal|15
argument_list|,
literal|14
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testDescendingMessagesPass ()
specifier|public
name|void
name|testDescendingMessagesPass
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectsDescending
argument_list|(
name|header
argument_list|(
literal|"counter"
argument_list|)
operator|.
name|convertTo
argument_list|(
name|Number
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|sendMessages
argument_list|(
literal|15
argument_list|,
literal|14
argument_list|,
literal|13
argument_list|,
literal|12
argument_list|,
literal|11
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testDescendingMessagesFail ()
specifier|public
name|void
name|testDescendingMessagesFail
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectsDescending
argument_list|(
name|header
argument_list|(
literal|"counter"
argument_list|)
operator|.
name|convertTo
argument_list|(
name|Number
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|sendMessages
argument_list|(
literal|15
argument_list|,
literal|14
argument_list|,
literal|13
argument_list|,
literal|11
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testNoDuplicateMessagesPass ()
specifier|public
name|void
name|testNoDuplicateMessagesPass
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectsNoDuplicates
argument_list|(
name|header
argument_list|(
literal|"counter"
argument_list|)
argument_list|)
expr_stmt|;
name|sendMessages
argument_list|(
literal|11
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|15
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testDuplicateMessagesFail ()
specifier|public
name|void
name|testDuplicateMessagesFail
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectsNoDuplicates
argument_list|(
name|header
argument_list|(
literal|"counter"
argument_list|)
argument_list|)
expr_stmt|;
name|sendMessages
argument_list|(
literal|11
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testExpectationsAfterMessagesArrivePass ()
specifier|public
name|void
name|testExpectationsAfterMessagesArrivePass
parameter_list|()
throws|throws
name|Exception
block|{
name|sendMessages
argument_list|(
literal|11
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testExpectationsAfterMessagesArriveFail ()
specifier|public
name|void
name|testExpectationsAfterMessagesArriveFail
parameter_list|()
throws|throws
name|Exception
block|{
name|sendMessages
argument_list|(
literal|11
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|,
literal|14
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testReset ()
specifier|public
name|void
name|testReset
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|sendMessages
argument_list|(
literal|11
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|sendMessages
argument_list|(
literal|11
argument_list|,
literal|12
argument_list|,
literal|13
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testExpectationOfHeader ()
specifier|public
name|void
name|testExpectationOfHeader
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// assert header& value are same
name|resultEndpoint
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"header"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|sendHeader
argument_list|(
literal|"header"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// assert failure when value is different
name|resultEndpoint
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"header"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|sendHeader
argument_list|(
literal|"header"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// assert failure when header name is different
name|resultEndpoint
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"header1"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|sendHeader
argument_list|(
literal|"header"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
name|resultEndpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// assert failure when both header name& value are different
name|resultEndpoint
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"header1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|sendHeader
argument_list|(
literal|"header"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|sendMessages (int... counters)
specifier|protected
name|void
name|sendMessages
parameter_list|(
name|int
modifier|...
name|counters
parameter_list|)
block|{
for|for
control|(
name|int
name|counter
range|:
name|counters
control|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:a"
argument_list|,
literal|"<message>"
operator|+
name|counter
operator|+
literal|"</message>"
argument_list|,
literal|"counter"
argument_list|,
name|counter
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|sendHeader (String name, String value)
specifier|protected
name|void
name|sendHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:a"
argument_list|,
literal|"body"
argument_list|,
name|name
argument_list|,
name|value
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
name|from
argument_list|(
literal|"direct:a"
argument_list|)
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
block|}
end_class

end_unit

