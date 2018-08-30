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
name|RollbackExchangeException
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
name|RuntimeCamelException
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
name|SpringRouteBuilder
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
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

begin_comment
comment|/**  * Transactional client test with rollback in the DSL.  *  * @version   */
end_comment

begin_class
DECL|class|TransactionalClientWithRollbackTest
specifier|public
class|class
name|TransactionalClientWithRollbackTest
extends|extends
name|SpringTestSupport
block|{
DECL|field|jdbc
specifier|protected
name|JdbcTemplate
name|jdbc
decl_stmt|;
DECL|field|useTransactionErrorHandler
specifier|protected
name|boolean
name|useTransactionErrorHandler
init|=
literal|true
decl_stmt|;
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
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
block|}
annotation|@
name|Test
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
name|queryForObject
argument_list|(
literal|"select count(*) from books"
argument_list|,
name|Integer
operator|.
name|class
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
annotation|@
name|Test
DECL|method|testTransactionRollback ()
specifier|public
name|void
name|testTransactionRollback
parameter_list|()
throws|throws
name|Exception
block|{
try|try
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
name|fail
argument_list|(
literal|"Should have thrown a RollbackExchangeException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|RuntimeCamelException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
operator|instanceof
name|RollbackExchangeException
argument_list|)
expr_stmt|;
block|}
name|int
name|count
init|=
name|jdbc
operator|.
name|queryForObject
argument_list|(
literal|"select count(*) from books"
argument_list|,
name|Integer
operator|.
name|class
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
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Notice that we use the SpringRouteBuilder that has a few more features than
comment|// the standard RouteBuilder
return|return
operator|new
name|SpringRouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// setup the transaction policy
name|SpringTransactionPolicy
name|required
init|=
name|lookup
argument_list|(
literal|"PROPAGATION_REQUIRED"
argument_list|,
name|SpringTransactionPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// use transaction error handler
name|errorHandler
argument_list|(
name|transactionErrorHandler
argument_list|(
name|required
argument_list|)
argument_list|)
expr_stmt|;
comment|// must setup policy for each route
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
name|bean
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
name|bean
argument_list|(
literal|"bookService"
argument_list|)
expr_stmt|;
comment|// must setup policy for each route
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
name|bean
argument_list|(
literal|"bookService"
argument_list|)
comment|// force a rollback
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

