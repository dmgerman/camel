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
name|ArrayList
import|;
end_import

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

begin_class
DECL|class|MyBatisInsertListTest
specifier|public
class|class
name|MyBatisInsertListTest
extends|extends
name|MyBatisTestSupport
block|{
annotation|@
name|Test
DECL|method|testInsertList ()
specifier|public
name|void
name|testInsertList
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
literal|444
argument_list|)
expr_stmt|;
name|account1
operator|.
name|setFirstName
argument_list|(
literal|"Willem"
argument_list|)
expr_stmt|;
name|account1
operator|.
name|setLastName
argument_list|(
literal|"Jiang"
argument_list|)
expr_stmt|;
name|account1
operator|.
name|setEmailAddress
argument_list|(
literal|"Faraway@gmail.com"
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
literal|555
argument_list|)
expr_stmt|;
name|account2
operator|.
name|setFirstName
argument_list|(
literal|"Aaron"
argument_list|)
expr_stmt|;
name|account2
operator|.
name|setLastName
argument_list|(
literal|"Daubman"
argument_list|)
expr_stmt|;
name|account2
operator|.
name|setEmailAddress
argument_list|(
literal|"ReadTheDevList@gmail.com"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Account
argument_list|>
name|accountList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|accountList
operator|.
name|add
argument_list|(
name|account1
argument_list|)
expr_stmt|;
name|accountList
operator|.
name|add
argument_list|(
name|account2
argument_list|)
expr_stmt|;
comment|// insert 2 new rows
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|accountList
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// there should be 4 rows now
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
literal|"There should be 4 rows"
argument_list|,
literal|4
argument_list|,
name|rows
operator|.
name|intValue
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
literal|"mybatis:batchInsertAccount?statementType=InsertList"
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

