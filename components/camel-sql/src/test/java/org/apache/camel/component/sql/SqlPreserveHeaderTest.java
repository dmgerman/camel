begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql
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
package|;
end_package

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
name|SingleConnectionDataSource
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SqlPreserveHeaderTest
specifier|public
class|class
name|SqlPreserveHeaderTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|driverClass
specifier|protected
name|String
name|driverClass
init|=
literal|"org.hsqldb.jdbcDriver"
decl_stmt|;
DECL|field|url
specifier|protected
name|String
name|url
init|=
literal|"jdbc:hsqldb:mem:camel_jdbc"
decl_stmt|;
DECL|field|user
specifier|protected
name|String
name|user
init|=
literal|"sa"
decl_stmt|;
DECL|field|password
specifier|protected
name|String
name|password
init|=
literal|""
decl_stmt|;
DECL|field|ds
specifier|private
name|DataSource
name|ds
decl_stmt|;
DECL|field|jdbcTemplate
specifier|private
name|JdbcTemplate
name|jdbcTemplate
decl_stmt|;
annotation|@
name|Test
DECL|method|testPreserveHeaders ()
specifier|public
name|void
name|testPreserveHeaders
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
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
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
name|Class
operator|.
name|forName
argument_list|(
name|driverClass
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|jdbcTemplate
operator|=
operator|new
name|JdbcTemplate
argument_list|(
name|ds
argument_list|)
expr_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
literal|"create table projects (id integer primary key,"
operator|+
literal|"project varchar(10), license varchar(5))"
argument_list|)
expr_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
literal|"insert into projects values (1, 'Camel', 'ASF')"
argument_list|)
expr_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
literal|"insert into projects values (2, 'AMQ', 'ASF')"
argument_list|)
expr_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
literal|"insert into projects values (3, 'Linux', 'XXX')"
argument_list|)
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
name|jdbcTemplate
init|=
operator|new
name|JdbcTemplate
argument_list|(
name|ds
argument_list|)
decl_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
literal|"drop table projects"
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
block|{
name|ds
operator|=
operator|new
name|SingleConnectionDataSource
argument_list|(
name|url
argument_list|,
name|user
argument_list|,
name|password
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"sql"
argument_list|,
name|SqlComponent
operator|.
name|class
argument_list|)
operator|.
name|setDataSource
argument_list|(
name|ds
argument_list|)
expr_stmt|;
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
name|constant
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"sql:select * from projects"
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

