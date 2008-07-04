begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|interceptor
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
name|spring
operator|.
name|SpringTestSupport
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
name|spring
operator|.
name|spi
operator|.
name|SpringTransactionPolicy
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
name|spi
operator|.
name|Policy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
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
name|transaction
operator|.
name|support
operator|.
name|TransactionTemplate
import|;
end_import

begin_comment
comment|/**  * Unit test to demonstrate the transactional client pattern.  */
end_comment

begin_class
DECL|class|TransactionalClientDataSourceTest
specifier|public
class|class
name|TransactionalClientDataSourceTest
extends|extends
name|SpringTestSupport
block|{
DECL|field|jdbc
specifier|private
name|JdbcTemplate
name|jdbc
decl_stmt|;
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"/org/apache/camel/spring/interceptor/transactionalClientDataSource.xml"
argument_list|)
return|;
block|}
DECL|method|getExpectedRouteCount ()
specifier|protected
name|int
name|getExpectedRouteCount
parameter_list|()
block|{
return|return
literal|0
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
name|this
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|// START SNIPPET: e5
comment|// create database and insert dummy data
specifier|final
name|DataSource
name|ds
init|=
name|getMandatoryBean
argument_list|(
name|DataSource
operator|.
name|class
argument_list|,
literal|"dataSource"
argument_list|)
decl_stmt|;
name|jdbc
operator|=
operator|new
name|JdbcTemplate
argument_list|(
name|ds
argument_list|)
expr_stmt|;
name|jdbc
operator|.
name|execute
argument_list|(
literal|"create table books (title varchar(50))"
argument_list|)
expr_stmt|;
name|jdbc
operator|.
name|update
argument_list|(
literal|"insert into books (title) values (?)"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"Camel in Action"
block|}
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e5
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
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|jdbc
operator|.
name|execute
argument_list|(
literal|"drop table books"
argument_list|)
expr_stmt|;
name|this
operator|.
name|enableJMX
argument_list|()
expr_stmt|;
block|}
comment|// START SNIPPET: e3
DECL|method|testTransactionSuccess ()
specifier|public
name|void
name|testTransactionSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:okay"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|int
name|count
init|=
name|jdbc
operator|.
name|queryForInt
argument_list|(
literal|"select count(*) from books"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number of books"
argument_list|,
literal|3
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
comment|// END SNIPPET: e3
comment|// START SNIPPET: e4
DECL|method|testTransactionRollback ()
specifier|public
name|void
name|testTransactionRollback
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:fail"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|int
name|count
init|=
name|jdbc
operator|.
name|queryForInt
argument_list|(
literal|"select count(*) from books"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Number of books"
argument_list|,
literal|1
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
comment|// END SNIPPET: e4
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
comment|// setup the transaction policy
name|TransactionTemplate
name|tt
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
literal|"PROPAGATION_REQUIRED"
argument_list|,
name|TransactionTemplate
operator|.
name|class
argument_list|)
decl_stmt|;
name|Policy
name|required
init|=
operator|new
name|SpringTransactionPolicy
argument_list|(
name|tt
argument_list|)
decl_stmt|;
comment|// END SNIPPET: e1
comment|// START SNIPPET: e2
comment|// set the required policy for this route
name|from
argument_list|(
literal|"direct:okay"
argument_list|)
operator|.
name|policy
argument_list|(
name|required
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Tiger in Action"
argument_list|)
argument_list|)
operator|.
name|beanRef
argument_list|(
literal|"bookService"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Elephant in Action"
argument_list|)
argument_list|)
operator|.
name|beanRef
argument_list|(
literal|"bookService"
argument_list|)
expr_stmt|;
comment|// set the required policy for this route
name|from
argument_list|(
literal|"direct:fail"
argument_list|)
operator|.
name|policy
argument_list|(
name|required
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Tiger in Action"
argument_list|)
argument_list|)
operator|.
name|beanRef
argument_list|(
literal|"bookService"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Donkey in Action"
argument_list|)
argument_list|)
operator|.
name|beanRef
argument_list|(
literal|"bookService"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e2
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

