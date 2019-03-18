begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jdbc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jdbc
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
name|builder
operator|.
name|RouteBuilder
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
DECL|class|JdbcGeneratedKeysTest
specifier|public
class|class
name|JdbcGeneratedKeysTest
extends|extends
name|AbstractJdbcGeneratedKeysTest
block|{
annotation|@
name|Test
DECL|method|testRetrieveGeneratedKeys ()
specifier|public
name|void
name|testRetrieveGeneratedKeys
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|testRetrieveGeneratedKeys
argument_list|(
literal|"insert into tableWithAutoIncr (content) values ('value2')"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRetrieveGeneratedKeysWithStringGeneratedColumns ()
specifier|public
name|void
name|testRetrieveGeneratedKeysWithStringGeneratedColumns
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|testRetrieveGeneratedKeysWithStringGeneratedColumns
argument_list|(
literal|"insert into tableWithAutoIncr (content) values ('value2')"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRetrieveGeneratedKeysWithIntGeneratedColumns ()
specifier|public
name|void
name|testRetrieveGeneratedKeysWithIntGeneratedColumns
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|testRetrieveGeneratedKeysWithIntGeneratedColumns
argument_list|(
literal|"insert into tableWithAutoIncr (content) values ('value2')"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGivenAnInvalidGeneratedColumnsHeaderThenAnExceptionIsThrown ()
specifier|public
name|void
name|testGivenAnInvalidGeneratedColumnsHeaderThenAnExceptionIsThrown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|testGivenAnInvalidGeneratedColumnsHeaderThenAnExceptionIsThrown
argument_list|(
literal|"insert into tableWithAutoIncr (content) values ('value2')"
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
comment|// START SNIPPET: route
comment|// lets add simple route
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:hello"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jdbc:testdb?readSize=100"
argument_list|)
expr_stmt|;
block|}
comment|// END SNIPPET: route
block|}
return|;
block|}
block|}
end_class

end_unit

