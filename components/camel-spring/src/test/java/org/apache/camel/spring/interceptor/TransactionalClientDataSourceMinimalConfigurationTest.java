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
name|spring
operator|.
name|SpringTestSupport
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
comment|/**  * Easier transaction configuration as we only setup Spring TX stuff.  */
end_comment

begin_class
DECL|class|TransactionalClientDataSourceMinimalConfigurationTest
specifier|public
class|class
name|TransactionalClientDataSourceMinimalConfigurationTest
extends|extends
name|SpringTestSupport
block|{
DECL|field|jdbc
specifier|protected
name|JdbcTemplate
name|jdbc
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
literal|"/org/apache/camel/spring/interceptor/springTransactionalClientDataSourceMinimalConfiguration.xml"
argument_list|)
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
block|}
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
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
block|{
comment|// expeced as we fail
name|assertTrue
argument_list|(
name|e
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
block|}
end_class

end_unit

