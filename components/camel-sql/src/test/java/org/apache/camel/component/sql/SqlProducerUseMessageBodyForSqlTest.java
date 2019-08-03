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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
DECL|class|SqlProducerUseMessageBodyForSqlTest
specifier|public
class|class
name|SqlProducerUseMessageBodyForSqlTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|db
specifier|private
name|EmbeddedDatabase
name|db
decl_stmt|;
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
name|addScript
argument_list|(
literal|"sql/createAndPopulateDatabase.sql"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
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
name|db
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUseMessageBodyForSqlAndHeaderParams ()
specifier|public
name|void
name|testUseMessageBodyForSqlAndHeaderParams
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
block|{
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
name|db
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"select * from projects where license = :?lic order by id"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"sql://query?useMessageBodyForSql=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
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
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|,
literal|"lic"
argument_list|,
literal|"ASF"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|received
init|=
name|assertIsInstanceOf
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|received
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|row
init|=
name|assertIsInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|received
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Camel"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"PROJECT"
argument_list|)
argument_list|)
expr_stmt|;
name|row
operator|=
name|assertIsInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|received
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AMQ"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"PROJECT"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testUseMessageBodyForSqlAndCamelSqlParameters ()
specifier|public
name|void
name|testUseMessageBodyForSqlAndCamelSqlParameters
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
block|{
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
name|db
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"select * from projects where license = :?lic order by id"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"sql://query?useMessageBodyForSql=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"lic"
argument_list|,
literal|"ASF"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|,
name|SqlConstants
operator|.
name|SQL_PARAMETERS
argument_list|,
name|row
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|received
init|=
name|assertIsInstanceOf
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|received
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|=
name|assertIsInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|received
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"PROJECT"
argument_list|)
argument_list|)
expr_stmt|;
name|row
operator|=
name|assertIsInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|received
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AMQ"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"PROJECT"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testUseMessageBodyForSqlAndCamelSqlParametersBatch ()
specifier|public
name|void
name|testUseMessageBodyForSqlAndCamelSqlParametersBatch
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
block|{
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
name|db
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"insert into projects(id, project, license) values(:?id,:?project,:?lic)"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"sql://query?useMessageBodyForSql=true&batch=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
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
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|rows
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"project"
argument_list|,
literal|"MyProject1"
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"lic"
argument_list|,
literal|"OPEN1"
argument_list|)
expr_stmt|;
name|rows
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|row
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|201
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"project"
argument_list|,
literal|"MyProject2"
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"lic"
argument_list|,
literal|"OPEN1"
argument_list|)
expr_stmt|;
name|rows
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|,
name|SqlConstants
operator|.
name|SQL_PARAMETERS
argument_list|,
name|rows
argument_list|)
expr_stmt|;
name|String
name|origSql
init|=
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"insert into projects(id, project, license) values(:?id,:?project,:?lic)"
argument_list|,
name|origSql
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
comment|// Clear and then use route2 to verify result of above insert select
name|context
operator|.
name|removeRoute
argument_list|(
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
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
block|{
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
name|db
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start2"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"select * from projects where license = :?lic order by id"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"sql://query2?useMessageBodyForSql=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result2"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|mock
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:result2"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start2"
argument_list|,
literal|null
argument_list|,
literal|"lic"
argument_list|,
literal|"OPEN1"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|received
init|=
name|assertIsInstanceOf
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|received
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|=
name|assertIsInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|received
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"MyProject1"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"PROJECT"
argument_list|)
argument_list|)
expr_stmt|;
name|row
operator|=
name|assertIsInstanceOf
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|received
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"MyProject2"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"PROJECT"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

