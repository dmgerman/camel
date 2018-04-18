begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.jdbc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
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
name|Arrays
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
name|Map
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
name|spring
operator|.
name|CamelSpringTestSupport
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
comment|/**  * Tests to ensure that arbitrary headers can be stored as raw text within a dataSource  * Tests to ensure the body can be stored as readable text within a dataSource  */
end_comment

begin_class
DECL|class|JdbcAggregateStoreAsText
specifier|public
class|class
name|JdbcAggregateStoreAsText
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|repo
specifier|protected
name|JdbcAggregationRepository
name|repo
decl_stmt|;
DECL|field|jdbcTemplate
specifier|protected
name|JdbcTemplate
name|jdbcTemplate
decl_stmt|;
DECL|field|dataSource
specifier|protected
name|DataSource
name|dataSource
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
literal|"org/apache/camel/processor/aggregate/jdbc/JdbcSpringAggregateStoreAsText.xml"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|postProcessTest ()
specifier|public
name|void
name|postProcessTest
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|postProcessTest
argument_list|()
expr_stmt|;
name|repo
operator|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"repo3"
argument_list|,
name|JdbcAggregationRepository
operator|.
name|class
argument_list|)
expr_stmt|;
name|dataSource
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"dataSource3"
argument_list|,
name|DataSource
operator|.
name|class
argument_list|)
expr_stmt|;
name|jdbcTemplate
operator|=
operator|new
name|JdbcTemplate
argument_list|(
name|dataSource
argument_list|)
expr_stmt|;
name|jdbcTemplate
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStoreBodyAsTextAndCompanyNameHeaderAndAccountNameHeader ()
specifier|public
name|void
name|testStoreBodyAsTextAndCompanyNameHeaderAndAccountNameHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"ABCDE"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setStoreBodyAsText
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setHeadersToStoreAsText
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"companyName"
argument_list|,
literal|"accountName"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"companyName"
argument_list|,
literal|"Acme"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"accountName"
argument_list|,
literal|"Alan"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|getAggregationRepositoryBody
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Acme"
argument_list|,
name|getAggregationRepositoryCompanyName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Alan"
argument_list|,
name|getAggregationRepositoryAccountName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"B"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AB"
argument_list|,
name|getAggregationRepositoryBody
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Acme"
argument_list|,
name|getAggregationRepositoryCompanyName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Alan"
argument_list|,
name|getAggregationRepositoryAccountName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"C"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ABC"
argument_list|,
name|getAggregationRepositoryBody
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Acme"
argument_list|,
name|getAggregationRepositoryCompanyName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Alan"
argument_list|,
name|getAggregationRepositoryAccountName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"D"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ABCD"
argument_list|,
name|getAggregationRepositoryBody
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Acme"
argument_list|,
name|getAggregationRepositoryCompanyName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Alan"
argument_list|,
name|getAggregationRepositoryAccountName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"E"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStoreBodyAsTextAndNoHeaders ()
specifier|public
name|void
name|testStoreBodyAsTextAndNoHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"ABCDE"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setStoreBodyAsText
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setHeadersToStoreAsText
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"companyName"
argument_list|,
literal|"Acme"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"accountName"
argument_list|,
literal|"Alan"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|getAggregationRepositoryBody
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|getAggregationRepositoryCompanyName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|getAggregationRepositoryAccountName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"B"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AB"
argument_list|,
name|getAggregationRepositoryBody
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|getAggregationRepositoryCompanyName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|getAggregationRepositoryAccountName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"C"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ABC"
argument_list|,
name|getAggregationRepositoryBody
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|getAggregationRepositoryCompanyName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|getAggregationRepositoryAccountName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"D"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ABCD"
argument_list|,
name|getAggregationRepositoryBody
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|getAggregationRepositoryCompanyName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|getAggregationRepositoryAccountName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"E"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOnlyAccountNameHeaders ()
specifier|public
name|void
name|testOnlyAccountNameHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"ABCDE"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setStoreBodyAsText
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setHeadersToStoreAsText
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"accountName"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"companyName"
argument_list|,
literal|"Acme"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"accountName"
argument_list|,
literal|"Alan"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|getAggregationRepositoryBody
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|getAggregationRepositoryCompanyName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Alan"
argument_list|,
name|getAggregationRepositoryAccountName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"B"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|getAggregationRepositoryBody
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|getAggregationRepositoryCompanyName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Alan"
argument_list|,
name|getAggregationRepositoryAccountName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"C"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|getAggregationRepositoryBody
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|getAggregationRepositoryCompanyName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Alan"
argument_list|,
name|getAggregationRepositoryAccountName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"D"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|getAggregationRepositoryBody
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|getAggregationRepositoryCompanyName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Alan"
argument_list|,
name|getAggregationRepositoryAccountName
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"E"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|getAggregationRepositoryBody (int id)
specifier|public
name|String
name|getAggregationRepositoryBody
parameter_list|(
name|int
name|id
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|getAggregationRepositoryColumn
argument_list|(
name|id
argument_list|,
literal|"body"
argument_list|)
return|;
block|}
DECL|method|getAggregationRepositoryCompanyName (int id)
specifier|public
name|String
name|getAggregationRepositoryCompanyName
parameter_list|(
name|int
name|id
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|getAggregationRepositoryColumn
argument_list|(
name|id
argument_list|,
literal|"companyName"
argument_list|)
return|;
block|}
DECL|method|getAggregationRepositoryAccountName (int id)
specifier|public
name|String
name|getAggregationRepositoryAccountName
parameter_list|(
name|int
name|id
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|getAggregationRepositoryColumn
argument_list|(
name|id
argument_list|,
literal|"accountName"
argument_list|)
return|;
block|}
DECL|method|getAggregationRepositoryColumn (int id, String columnName)
specifier|public
name|String
name|getAggregationRepositoryColumn
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|columnName
parameter_list|)
block|{
return|return
name|jdbcTemplate
operator|.
name|queryForObject
argument_list|(
literal|"SELECT "
operator|+
name|columnName
operator|+
literal|" from aggregationRepo3 where id = ?"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|id
argument_list|)
return|;
block|}
block|}
end_class

end_unit

