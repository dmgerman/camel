begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql.stored
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|stored
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|DefaultSqlPrepareStatementStrategy
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
DECL|class|DefaultSqlPrepareStatementStrategyTest
specifier|public
class|class
name|DefaultSqlPrepareStatementStrategyTest
block|{
annotation|@
name|Test
DECL|method|testReplaceNestedExpressions ()
specifier|public
name|void
name|testReplaceNestedExpressions
parameter_list|()
throws|throws
name|SQLException
block|{
name|DefaultSqlPrepareStatementStrategy
name|strategy
init|=
operator|new
name|DefaultSqlPrepareStatementStrategy
argument_list|()
decl_stmt|;
name|String
name|sql
init|=
literal|"INSERT INTO example VALUES (:?${array[${index}]})"
decl_stmt|;
name|String
name|expected
init|=
literal|"INSERT INTO example VALUES (?)"
decl_stmt|;
name|String
name|query
init|=
name|strategy
operator|.
name|prepareQuery
argument_list|(
name|sql
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|query
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

