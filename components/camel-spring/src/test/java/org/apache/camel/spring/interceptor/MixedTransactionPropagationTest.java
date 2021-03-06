begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|SpringTestSupport
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
comment|/**  * Unit test to demonstrate the transactional client pattern.  */
end_comment

begin_class
DECL|class|MixedTransactionPropagationTest
specifier|public
class|class
name|MixedTransactionPropagationTest
extends|extends
name|SpringTestSupport
block|{
DECL|field|jdbc
specifier|protected
name|JdbcTemplate
name|jdbc
decl_stmt|;
annotation|@
name|Override
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
literal|"/org/apache/camel/spring/interceptor/MixedTransactionPropagationTest.xml"
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
DECL|method|testOkay ()
specifier|public
name|void
name|testOkay
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
DECL|method|testFail ()
specifier|public
name|void
name|testFail
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
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
comment|// expected as we fail
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
name|IllegalArgumentException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"We don't have Donkeys, only Camels"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
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
annotation|@
name|Test
DECL|method|testMixedRollbackOnlyLast ()
specifier|public
name|void
name|testMixedRollbackOnlyLast
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:mixed"
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
comment|// assert correct books in database
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|,
name|jdbc
operator|.
name|queryForObject
argument_list|(
literal|"select count(*) from books where title = 'Camel in Action'"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|,
name|jdbc
operator|.
name|queryForObject
argument_list|(
literal|"select count(*) from books where title = 'Tiger in Action'"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|,
name|jdbc
operator|.
name|queryForObject
argument_list|(
literal|"select count(*) from books where title = 'Elephant in Action'"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|0
argument_list|)
argument_list|,
name|jdbc
operator|.
name|queryForObject
argument_list|(
literal|"select count(*) from books where title = 'Lion in Action'"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|0
argument_list|)
argument_list|,
name|jdbc
operator|.
name|queryForObject
argument_list|(
literal|"select count(*) from books where title = 'Donkey in Action'"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMixedCommit ()
specifier|public
name|void
name|testMixedCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:mixed3"
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
literal|5
argument_list|,
name|count
argument_list|)
expr_stmt|;
comment|// assert correct books in database
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|,
name|jdbc
operator|.
name|queryForObject
argument_list|(
literal|"select count(*) from books where title = 'Camel in Action'"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|,
name|jdbc
operator|.
name|queryForObject
argument_list|(
literal|"select count(*) from books where title = 'Tiger in Action'"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|,
name|jdbc
operator|.
name|queryForObject
argument_list|(
literal|"select count(*) from books where title = 'Elephant in Action'"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|,
name|jdbc
operator|.
name|queryForObject
argument_list|(
literal|"select count(*) from books where title = 'Lion in Action'"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|,
name|jdbc
operator|.
name|queryForObject
argument_list|(
literal|"select count(*) from books where title = 'Crocodile in Action'"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
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
name|from
argument_list|(
literal|"direct:okay"
argument_list|)
operator|.
name|transacted
argument_list|(
literal|"PROPAGATION_REQUIRED"
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
name|from
argument_list|(
literal|"direct:fail"
argument_list|)
operator|.
name|transacted
argument_list|(
literal|"PROPAGATION_REQUIRED"
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
literal|"Donkey in Action"
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
literal|"bookService"
argument_list|)
expr_stmt|;
comment|// START SNIPPET: e1
name|from
argument_list|(
literal|"direct:mixed"
argument_list|)
comment|// using required
operator|.
name|transacted
argument_list|(
literal|"PROPAGATION_REQUIRED"
argument_list|)
comment|// all these steps will be okay
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
comment|// continue on route 2
operator|.
name|to
argument_list|(
literal|"direct:mixed2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:mixed2"
argument_list|)
comment|// tell Camel that if this route fails then only rollback this last route
comment|// by using (rollback only *last*)
operator|.
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|markRollbackOnlyLast
argument_list|()
operator|.
name|end
argument_list|()
comment|// using a different propagation which is requires new
operator|.
name|transacted
argument_list|(
literal|"PROPAGATION_REQUIRES_NEW"
argument_list|)
comment|// this step will be okay
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Lion in Action"
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
literal|"bookService"
argument_list|)
comment|// this step will fail with donkey
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Donkey in Action"
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
literal|"bookService"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
name|from
argument_list|(
literal|"direct:mixed3"
argument_list|)
comment|// using required
operator|.
name|transacted
argument_list|(
literal|"PROPAGATION_REQUIRED"
argument_list|)
comment|// all these steps will be okay
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
comment|// continue on route 4
operator|.
name|to
argument_list|(
literal|"direct:mixed4"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:mixed4"
argument_list|)
comment|// tell Camel that if this route fails then only rollback this last route
comment|// by using (rollback only *last*)
operator|.
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|markRollbackOnlyLast
argument_list|()
operator|.
name|end
argument_list|()
comment|// using a different propagation which is requires new
operator|.
name|transacted
argument_list|(
literal|"PROPAGATION_REQUIRES_NEW"
argument_list|)
comment|// this step will be okay
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Lion in Action"
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
literal|"bookService"
argument_list|)
comment|// this step will be okay
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Crocodile in Action"
argument_list|)
argument_list|)
operator|.
name|bean
argument_list|(
literal|"bookService"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

