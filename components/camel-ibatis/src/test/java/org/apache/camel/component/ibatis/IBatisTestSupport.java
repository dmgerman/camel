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
name|SQLException
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
name|Properties
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|derby
operator|.
name|jdbc
operator|.
name|EmbeddedDriver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_class
DECL|class|IBatisTestSupport
specifier|public
class|class
name|IBatisTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|method|createTestData ()
specifier|protected
name|boolean
name|createTestData
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getStatement ()
specifier|protected
name|String
name|getStatement
parameter_list|()
block|{
return|return
literal|"create table ACCOUNT ( ACC_ID INTEGER , ACC_FIRST_NAME VARCHAR(255), ACC_LAST_NAME VARCHAR(255), ACC_EMAIL VARCHAR(255)  )"
return|;
block|}
annotation|@
name|Override
annotation|@
name|Before
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
name|getStatement
argument_list|()
argument_list|)
expr_stmt|;
name|connection
operator|.
name|commit
argument_list|()
expr_stmt|;
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|createTestData
argument_list|()
condition|)
block|{
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"ibatis:insertAccount?statementType=Insert"
argument_list|,
operator|new
name|Account
index|[]
block|{
name|account1
block|,
name|account2
block|}
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
try|try
block|{
operator|new
name|EmbeddedDriver
argument_list|()
operator|.
name|connect
argument_list|(
literal|"jdbc:derby:memory:ibatis;drop=true"
argument_list|,
operator|new
name|Properties
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ex
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|"08006"
operator|.
name|equals
argument_list|(
name|ex
operator|.
name|getSQLState
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
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

