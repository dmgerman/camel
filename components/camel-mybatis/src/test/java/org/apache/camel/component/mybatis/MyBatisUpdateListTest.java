begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
DECL|class|MyBatisUpdateListTest
specifier|public
class|class
name|MyBatisUpdateListTest
extends|extends
name|MyBatisTestSupport
block|{
annotation|@
name|Test
DECL|method|testUpdateList ()
specifier|public
name|void
name|testUpdateList
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Account
name|account1
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
name|account1
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|account1
operator|.
name|setFirstName
argument_list|(
literal|"James"
argument_list|)
expr_stmt|;
name|account1
operator|.
name|setLastName
argument_list|(
literal|"Strachan"
argument_list|)
expr_stmt|;
name|account1
operator|.
name|setEmailAddress
argument_list|(
literal|"TryGuessing@gmail.com"
argument_list|)
expr_stmt|;
name|Account
name|account2
init|=
operator|new
name|Account
argument_list|()
decl_stmt|;
name|account2
operator|.
name|setId
argument_list|(
literal|456
argument_list|)
expr_stmt|;
name|account2
operator|.
name|setFirstName
argument_list|(
literal|"Claus"
argument_list|)
expr_stmt|;
name|account2
operator|.
name|setLastName
argument_list|(
literal|"Ibsen"
argument_list|)
expr_stmt|;
name|account2
operator|.
name|setEmailAddress
argument_list|(
literal|"Noname@gmail.com"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"list"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|account1
argument_list|,
name|account2
argument_list|)
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"emailAddress"
argument_list|,
literal|"Other@gmail.com"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// there should be 2 rows now
name|Integer
name|rows
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"mybatis:count?statementType=SelectOne"
argument_list|,
literal|null
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"There should be 2 rows"
argument_list|,
literal|2
argument_list|,
name|rows
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Account
name|james
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"mybatis:selectAccountById?statementType=SelectOne"
argument_list|,
literal|123
argument_list|,
name|Account
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"James"
argument_list|,
name|james
operator|.
name|getFirstName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Strachan"
argument_list|,
name|james
operator|.
name|getLastName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Other@gmail.com"
argument_list|,
name|james
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
expr_stmt|;
name|Account
name|claus
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"mybatis:selectAccountById?statementType=SelectOne"
argument_list|,
literal|456
argument_list|,
name|Account
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|claus
operator|.
name|getFirstName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Ibsen"
argument_list|,
name|claus
operator|.
name|getLastName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Other@gmail.com"
argument_list|,
name|claus
operator|.
name|getEmailAddress
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
annotation|@
name|Override
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mybatis:batchUpdateAccount?statementType=UpdateList"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

