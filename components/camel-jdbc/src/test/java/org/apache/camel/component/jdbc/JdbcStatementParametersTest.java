begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|impl
operator|.
name|JndiRegistry
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
name|springframework
operator|.
name|jdbc
operator|.
name|core
operator|.
name|JdbcTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|datasource
operator|.
name|DriverManagerDataSource
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JdbcStatementParametersTest
specifier|public
class|class
name|JdbcStatementParametersTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|ds
specifier|protected
name|DataSource
name|ds
decl_stmt|;
DECL|field|driverClass
specifier|private
name|String
name|driverClass
init|=
literal|"org.hsqldb.jdbcDriver"
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
init|=
literal|"jdbc:hsqldb:mem:camel_jdbc"
decl_stmt|;
DECL|field|user
specifier|private
name|String
name|user
init|=
literal|"sa"
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
init|=
literal|""
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
annotation|@
name|Test
DECL|method|testMax2Rows ()
specifier|public
name|void
name|testMax2Rows
parameter_list|()
throws|throws
name|Exception
block|{
name|List
name|rows
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:hello"
argument_list|,
literal|"select * from customer order by id"
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|rows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|context
operator|.
name|getEndpoints
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
annotation|@
name|Test
DECL|method|testMax5Rows ()
specifier|public
name|void
name|testMax5Rows
parameter_list|()
throws|throws
name|Exception
block|{
name|List
name|rows
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"jdbc:testdb?statement.maxRows=5&statement.fetchSize=50"
argument_list|,
literal|"select * from customer order by id"
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|rows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|context
operator|.
name|getEndpoints
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
annotation|@
name|Test
DECL|method|testNoParameters ()
specifier|public
name|void
name|testNoParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|List
name|rows
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"jdbc:testdb"
argument_list|,
literal|"select * from customer order by id"
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|rows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|context
operator|.
name|getEndpoints
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|reg
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|reg
operator|.
name|bind
argument_list|(
literal|"testdb"
argument_list|,
name|ds
argument_list|)
expr_stmt|;
return|return
name|reg
return|;
block|}
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
name|from
argument_list|(
literal|"direct:hello"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jdbc:testdb?statement.maxRows=2"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
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
name|DriverManagerDataSource
name|dataSource
init|=
operator|new
name|DriverManagerDataSource
argument_list|(
name|url
argument_list|,
name|user
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|dataSource
operator|.
name|setDriverClassName
argument_list|(
name|driverClass
argument_list|)
expr_stmt|;
name|ds
operator|=
name|dataSource
expr_stmt|;
name|JdbcTemplate
name|jdbc
init|=
operator|new
name|JdbcTemplate
argument_list|(
name|ds
argument_list|)
decl_stmt|;
name|jdbc
operator|.
name|execute
argument_list|(
literal|"create table customer (id varchar(15), name varchar(10))"
argument_list|)
expr_stmt|;
name|jdbc
operator|.
name|execute
argument_list|(
literal|"insert into customer values('cust1','jstrachan')"
argument_list|)
expr_stmt|;
name|jdbc
operator|.
name|execute
argument_list|(
literal|"insert into customer values('cust2','nsandhu')"
argument_list|)
expr_stmt|;
name|jdbc
operator|.
name|execute
argument_list|(
literal|"insert into customer values('cust3','willem')"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
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
name|JdbcTemplate
name|jdbc
init|=
operator|new
name|JdbcTemplate
argument_list|(
name|ds
argument_list|)
decl_stmt|;
name|jdbc
operator|.
name|execute
argument_list|(
literal|"drop table customer"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

