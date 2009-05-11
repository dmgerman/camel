begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ibatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ibatis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
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
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|IBatisRouteTest
specifier|public
class|class
name|IBatisRouteTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testSendAccountBean ()
specifier|public
name|void
name|testSendAccountBean
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
literal|1
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
literal|123
argument_list|)
expr_stmt|;
name|account
operator|.
name|setFirstName
argument_list|(
literal|"James"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setLastName
argument_list|(
literal|"Strachan"
argument_list|)
expr_stmt|;
name|account
operator|.
name|setEmailAddress
argument_list|(
literal|"TryGuessing@gmail.com"
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
comment|// now lets poll that the account has been inserted
name|List
name|body
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"ibatis:selectAllAccounts?statementType=QueryForList"
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
literal|1
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
literal|"James"
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
literal|"Strachan"
argument_list|,
name|actual
operator|.
name|getLastName
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Found: "
operator|+
name|actual
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
comment|//Delaying the query so we will not get the "java.sql.SQLException: Table not found in statement" on the slower box
name|from
argument_list|(
literal|"timer://pollTheDatabase?delay=2000"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ibatis:selectAllAccounts?statementType=QueryForList"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:results"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ibatis:insertAccount?statementType=Insert"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
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
comment|// lets create the database...
name|Connection
name|connection
init|=
name|createConnection
argument_list|()
decl_stmt|;
name|Statement
name|statement
init|=
name|connection
operator|.
name|createStatement
argument_list|()
decl_stmt|;
name|statement
operator|.
name|execute
argument_list|(
literal|"create table ACCOUNT ( ACC_ID INTEGER , ACC_FIRST_NAME VARCHAR(255), ACC_LAST_NAME VARCHAR(255), ACC_EMAIL VARCHAR(255)  )"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|Connection
name|connection
init|=
name|createConnection
argument_list|()
decl_stmt|;
name|Statement
name|statement
init|=
name|connection
operator|.
name|createStatement
argument_list|()
decl_stmt|;
name|statement
operator|.
name|execute
argument_list|(
literal|"drop table ACCOUNT"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|createConnection ()
specifier|private
name|Connection
name|createConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|IBatisEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"ibatis:Account"
argument_list|,
name|IBatisEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|endpoint
operator|.
name|getSqlMapClient
argument_list|()
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
return|;
block|}
block|}
end_class

end_unit

