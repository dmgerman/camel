begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.solr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|solr
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
name|Collection
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
name|java
operator|.
name|util
operator|.
name|SortedMap
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
name|solr
operator|.
name|client
operator|.
name|solrj
operator|.
name|SolrQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|solr
operator|.
name|client
operator|.
name|solrj
operator|.
name|SolrServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|solr
operator|.
name|client
operator|.
name|solrj
operator|.
name|SolrServerException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|solr
operator|.
name|client
operator|.
name|solrj
operator|.
name|embedded
operator|.
name|JettySolrRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|solr
operator|.
name|client
operator|.
name|solrj
operator|.
name|impl
operator|.
name|HttpSolrServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|solr
operator|.
name|client
operator|.
name|solrj
operator|.
name|response
operator|.
name|QueryResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|servlet
operator|.
name|ServletHolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
DECL|class|SolrComponentTestSupport
specifier|public
class|class
name|SolrComponentTestSupport
extends|extends
name|SolrTestSupport
block|{
DECL|field|TEST_ID
specifier|protected
specifier|static
specifier|final
name|String
name|TEST_ID
init|=
literal|"test1"
decl_stmt|;
DECL|field|TEST_ID2
specifier|protected
specifier|static
specifier|final
name|String
name|TEST_ID2
init|=
literal|"test2"
decl_stmt|;
DECL|field|solrFixtures
specifier|private
name|SolrFixtures
name|solrFixtures
decl_stmt|;
DECL|method|solrInsertTestEntry ()
specifier|protected
name|void
name|solrInsertTestEntry
parameter_list|()
block|{
name|solrInsertTestEntry
argument_list|(
name|TEST_ID
argument_list|)
expr_stmt|;
block|}
DECL|method|secureOrNot ()
specifier|protected
specifier|static
name|Collection
name|secureOrNot
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
index|[]
block|{
block|{
literal|true
block|}
block|,
block|{
literal|false
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|SolrComponentTestSupport (Boolean useHttps)
specifier|public
name|SolrComponentTestSupport
parameter_list|(
name|Boolean
name|useHttps
parameter_list|)
block|{
name|this
operator|.
name|solrFixtures
operator|=
operator|new
name|SolrFixtures
argument_list|(
name|useHttps
argument_list|)
expr_stmt|;
block|}
DECL|method|solrRouteUri ()
name|String
name|solrRouteUri
parameter_list|()
block|{
return|return
name|solrFixtures
operator|.
name|solrRouteUri
argument_list|()
return|;
block|}
DECL|method|solrInsertTestEntry (String id)
specifier|protected
name|void
name|solrInsertTestEntry
parameter_list|(
name|String
name|id
parameter_list|)
block|{
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|SolrConstants
operator|.
name|OPERATION
argument_list|,
name|SolrConstants
operator|.
name|OPERATION_INSERT
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"SolrField.id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|""
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
DECL|method|solrCommit ()
specifier|protected
name|void
name|solrCommit
parameter_list|()
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|""
argument_list|,
name|SolrConstants
operator|.
name|OPERATION
argument_list|,
name|SolrConstants
operator|.
name|OPERATION_COMMIT
argument_list|)
expr_stmt|;
block|}
DECL|method|executeSolrQuery (String query)
specifier|protected
name|QueryResponse
name|executeSolrQuery
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|SolrServerException
block|{
name|SolrQuery
name|solrQuery
init|=
operator|new
name|SolrQuery
argument_list|()
decl_stmt|;
name|solrQuery
operator|.
name|setQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|SolrServer
name|solrServer
init|=
name|solrFixtures
operator|.
name|getServer
argument_list|()
decl_stmt|;
return|return
name|solrServer
operator|.
name|query
argument_list|(
name|solrQuery
argument_list|)
return|;
block|}
annotation|@
name|BeforeClass
DECL|method|beforeClass ()
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
throws|throws
name|Exception
block|{
name|SolrFixtures
operator|.
name|createSolrFixtures
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|afterClass ()
specifier|public
specifier|static
name|void
name|afterClass
parameter_list|()
throws|throws
name|Exception
block|{
name|SolrFixtures
operator|.
name|teardownSolrFixtures
argument_list|()
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
annotation|@
name|Override
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
name|to
argument_list|(
name|solrRouteUri
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:splitThenCommit"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
name|solrRouteUri
argument_list|()
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SolrConstants
operator|.
name|OPERATION
argument_list|,
name|constant
argument_list|(
name|SolrConstants
operator|.
name|OPERATION_COMMIT
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|solrRouteUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Parameters
DECL|method|serverTypes ()
specifier|public
specifier|static
name|Collection
argument_list|<
name|Object
index|[]
argument_list|>
name|serverTypes
parameter_list|()
block|{
name|Object
index|[]
index|[]
name|serverTypes
init|=
block|{
block|{
literal|true
block|}
block|,
block|{
literal|false
block|}
block|}
decl_stmt|;
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|serverTypes
argument_list|)
return|;
block|}
annotation|@
name|Before
DECL|method|clearIndex ()
specifier|public
name|void
name|clearIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|SolrFixtures
operator|.
name|clearIndex
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

