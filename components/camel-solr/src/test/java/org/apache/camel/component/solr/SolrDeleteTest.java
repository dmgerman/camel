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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|SolrDeleteTest
specifier|public
class|class
name|SolrDeleteTest
extends|extends
name|SolrComponentTestSupport
block|{
DECL|method|SolrDeleteTest (SolrFixtures.TestServerType serverToTest)
specifier|public
name|SolrDeleteTest
parameter_list|(
name|SolrFixtures
operator|.
name|TestServerType
name|serverToTest
parameter_list|)
block|{
name|super
argument_list|(
name|serverToTest
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDeleteById ()
specifier|public
name|void
name|testDeleteById
parameter_list|()
throws|throws
name|Exception
block|{
comment|//insert, commit and verify
name|solrInsertTestEntry
argument_list|()
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"wrong number of entries found"
argument_list|,
literal|1
argument_list|,
name|executeSolrQuery
argument_list|(
literal|"id:"
operator|+
name|TEST_ID
argument_list|)
operator|.
name|getResults
argument_list|()
operator|.
name|getNumFound
argument_list|()
argument_list|)
expr_stmt|;
comment|//delete
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|TEST_ID
argument_list|,
name|SolrConstants
operator|.
name|OPERATION
argument_list|,
name|SolrConstants
operator|.
name|OPERATION_DELETE_BY_ID
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
comment|//verify
name|assertEquals
argument_list|(
literal|"wrong number of entries found"
argument_list|,
literal|0
argument_list|,
name|executeSolrQuery
argument_list|(
literal|"id:"
operator|+
name|TEST_ID
argument_list|)
operator|.
name|getResults
argument_list|()
operator|.
name|getNumFound
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDeleteListOfIDs ()
specifier|public
name|void
name|testDeleteListOfIDs
parameter_list|()
throws|throws
name|Exception
block|{
comment|//insert, commit and verify
name|solrInsertTestEntry
argument_list|(
name|TEST_ID
argument_list|)
expr_stmt|;
name|solrInsertTestEntry
argument_list|(
name|TEST_ID2
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"wrong number of entries found"
argument_list|,
literal|2
argument_list|,
name|executeSolrQuery
argument_list|(
literal|"id:test*"
argument_list|)
operator|.
name|getResults
argument_list|()
operator|.
name|getNumFound
argument_list|()
argument_list|)
expr_stmt|;
comment|//delete
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:splitThenCommit"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|TEST_ID
argument_list|,
name|TEST_ID2
argument_list|)
argument_list|,
name|SolrConstants
operator|.
name|OPERATION
argument_list|,
name|SolrConstants
operator|.
name|OPERATION_DELETE_BY_ID
argument_list|)
expr_stmt|;
comment|//verify
name|assertEquals
argument_list|(
literal|"wrong number of entries found"
argument_list|,
literal|0
argument_list|,
name|executeSolrQuery
argument_list|(
literal|"id:test*"
argument_list|)
operator|.
name|getResults
argument_list|()
operator|.
name|getNumFound
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDeleteByQuery ()
specifier|public
name|void
name|testDeleteByQuery
parameter_list|()
throws|throws
name|Exception
block|{
comment|//insert, commit and verify
name|solrInsertTestEntry
argument_list|(
name|TEST_ID
argument_list|)
expr_stmt|;
name|solrInsertTestEntry
argument_list|(
name|TEST_ID2
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"wrong number of entries found"
argument_list|,
literal|2
argument_list|,
name|executeSolrQuery
argument_list|(
literal|"id:test*"
argument_list|)
operator|.
name|getResults
argument_list|()
operator|.
name|getNumFound
argument_list|()
argument_list|)
expr_stmt|;
comment|//delete
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"id:test*"
argument_list|,
name|SolrConstants
operator|.
name|OPERATION
argument_list|,
name|SolrConstants
operator|.
name|OPERATION_DELETE_BY_QUERY
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
comment|//verify
name|assertEquals
argument_list|(
literal|"wrong number of entries found"
argument_list|,
literal|0
argument_list|,
name|executeSolrQuery
argument_list|(
literal|"id:test*"
argument_list|)
operator|.
name|getResults
argument_list|()
operator|.
name|getNumFound
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

