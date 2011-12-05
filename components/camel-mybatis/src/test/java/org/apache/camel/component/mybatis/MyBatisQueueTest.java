begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mybatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mybatis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|MyBatisQueueTest
specifier|public
class|class
name|MyBatisQueueTest
extends|extends
name|MyBatisTestSupport
block|{
DECL|method|createTestData ()
specifier|protected
name|boolean
name|createTestData
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|createStatement ()
specifier|protected
name|String
name|createStatement
parameter_list|()
block|{
return|return
literal|"create table ACCOUNT ( ACC_ID INTEGER , ACC_FIRST_NAME VARCHAR(255), ACC_LAST_NAME VARCHAR(255), ACC_EMAIL VARCHAR(255), PROCESSED BOOLEAN DEFAULT false)"
return|;
block|}
annotation|@
name|Test
DECL|method|testConsume ()
specifier|public
name|void
name|testConsume
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:results"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|Account
name|account
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
name|account
operator|.
name|setId
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|account
operator|.
name|setFirstName
argument_list|(
literal|"Bob"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setLastName
argument_list|(
literal|"Denver"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setEmailAddress
argument_list|(
literal|"TryGuessingGilligan@gmail.com"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|account
argument_list|)
expr_stmt|;
name|account
operator|=
operator|new
name|Account
argument_list|()
expr_stmt|;
name|account
operator|.
name|setId
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|account
operator|.
name|setFirstName
argument_list|(
literal|"Alan"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setLastName
argument_list|(
literal|"Hale"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setEmailAddress
argument_list|(
literal|"TryGuessingSkipper@gmail.com"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|account
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// need a delay here on slower machines
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// now lets poll that the account has been inserted
name|List
argument_list|<
name|?
argument_list|>
name|body
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"mybatis:selectProcessedAccounts?statementType=SelectList"
argument_list|,
literal|null
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong size: "
operator|+
name|body
argument_list|,
literal|2
argument_list|,
name|body
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Account
name|actual
init|=
name|assertIsInstanceOf
argument_list|(
name|Account
operator|.
name|class
argument_list|,
name|body
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Account.getFirstName()"
argument_list|,
literal|"Bob"
argument_list|,
name|actual
operator|.
name|getFirstName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Account.getLastName()"
argument_list|,
literal|"Denver"
argument_list|,
name|actual
operator|.
name|getLastName
argument_list|()
argument_list|)
expr_stmt|;
name|body
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"mybatis:selectUnprocessedAccounts?statementType=SelectList"
argument_list|,
literal|null
argument_list|,
name|List
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong size: "
operator|+
name|body
argument_list|,
literal|0
argument_list|,
name|body
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
literal|"mybatis:selectUnprocessedAccounts?consumer.onConsume=consumeAccount"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:results"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mybatis:insertAccount?statementType=Insert"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

