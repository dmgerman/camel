begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|Exchange
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
name|Processor
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
name|spi
operator|.
name|Registry
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
name|support
operator|.
name|SimpleRegistry
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
name|DataSourceTransactionManager
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
name|embedded
operator|.
name|EmbeddedDatabase
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
name|embedded
operator|.
name|EmbeddedDatabaseBuilder
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
name|embedded
operator|.
name|EmbeddedDatabaseType
import|;
end_import

begin_class
DECL|class|SqlTransactedRouteTest
specifier|public
class|class
name|SqlTransactedRouteTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|db
specifier|private
name|EmbeddedDatabase
name|db
decl_stmt|;
DECL|field|jdbc
specifier|private
name|JdbcTemplate
name|jdbc
decl_stmt|;
DECL|field|startEndpoint
specifier|private
name|String
name|startEndpoint
init|=
literal|"direct:start"
decl_stmt|;
DECL|field|sqlEndpoint
specifier|private
name|String
name|sqlEndpoint
init|=
literal|"sql:overriddenByTheHeader?dataSource=#testdb"
decl_stmt|;
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
name|jdbc
operator|=
operator|new
name|JdbcTemplate
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|jdbc
operator|.
name|execute
argument_list|(
literal|"CREATE TABLE CUSTOMER (ID VARCHAR(15) NOT NULL PRIMARY KEY, NAME VARCHAR(100))"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelRegistry ()
specifier|protected
name|Registry
name|createCamelRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|Registry
name|reg
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|db
operator|=
operator|new
name|EmbeddedDatabaseBuilder
argument_list|()
operator|.
name|setType
argument_list|(
name|EmbeddedDatabaseType
operator|.
name|DERBY
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|reg
operator|.
name|bind
argument_list|(
literal|"testdb"
argument_list|,
name|db
argument_list|)
expr_stmt|;
name|DataSourceTransactionManager
name|txMgr
init|=
operator|new
name|DataSourceTransactionManager
argument_list|()
decl_stmt|;
name|txMgr
operator|.
name|setDataSource
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|reg
operator|.
name|bind
argument_list|(
literal|"txManager"
argument_list|,
name|txMgr
argument_list|)
expr_stmt|;
name|SpringTransactionPolicy
name|txPolicy
init|=
operator|new
name|SpringTransactionPolicy
argument_list|()
decl_stmt|;
name|txPolicy
operator|.
name|setTransactionManager
argument_list|(
name|txMgr
argument_list|)
expr_stmt|;
name|txPolicy
operator|.
name|setPropagationBehaviorName
argument_list|(
literal|"PROPAGATION_REQUIRED"
argument_list|)
expr_stmt|;
name|reg
operator|.
name|bind
argument_list|(
literal|"required"
argument_list|,
name|txPolicy
argument_list|)
expr_stmt|;
return|return
name|reg
return|;
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
name|db
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCommit ()
specifier|public
name|void
name|testCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"commit"
argument_list|)
operator|.
name|transacted
argument_list|(
literal|"required"
argument_list|)
operator|.
name|to
argument_list|(
name|sqlEndpoint
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_QUERY
argument_list|,
literal|"insert into customer values('cust2','muellerc')"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
name|sqlEndpoint
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
name|startEndpoint
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_QUERY
argument_list|,
literal|"insert into customer values('cust1','cmueller')"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|exchange
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
name|long
name|count
init|=
name|jdbc
operator|.
name|queryForObject
argument_list|(
literal|"select count(*) from customer"
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|jdbc
operator|.
name|queryForMap
argument_list|(
literal|"select * from customer where id = 'cust1'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cust1"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cmueller"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"NAME"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|=
name|jdbc
operator|.
name|queryForMap
argument_list|(
literal|"select * from customer where id = 'cust2'"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cust2"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"muellerc"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"NAME"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRollbackAfterExceptionInSecondStatement ()
specifier|public
name|void
name|testRollbackAfterExceptionInSecondStatement
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"rollback"
argument_list|)
operator|.
name|transacted
argument_list|(
literal|"required"
argument_list|)
operator|.
name|to
argument_list|(
name|sqlEndpoint
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// primary key violation
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_QUERY
argument_list|,
literal|"insert into customer values('cust1','muellerc')"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
name|sqlEndpoint
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
name|startEndpoint
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_QUERY
argument_list|,
literal|"insert into customer values('cust1','cmueller')"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exchange
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
name|long
name|count
init|=
name|jdbc
operator|.
name|queryForObject
argument_list|(
literal|"select count(*) from customer"
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRollbackAfterAnException ()
specifier|public
name|void
name|testRollbackAfterAnException
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"rollback2"
argument_list|)
operator|.
name|transacted
argument_list|(
literal|"required"
argument_list|)
operator|.
name|to
argument_list|(
name|sqlEndpoint
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"forced Exception"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
name|startEndpoint
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_QUERY
argument_list|,
literal|"insert into customer values('cust1','cmueller')"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|exchange
operator|.
name|isFailed
argument_list|()
argument_list|)
expr_stmt|;
name|long
name|count
init|=
name|jdbc
operator|.
name|queryForObject
argument_list|(
literal|"select count(*) from customer"
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

