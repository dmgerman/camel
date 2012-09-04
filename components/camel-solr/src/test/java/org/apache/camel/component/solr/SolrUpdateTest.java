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
name|io
operator|.
name|File
import|;
end_import

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
name|List
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
name|response
operator|.
name|QueryResponse
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
name|util
operator|.
name|ClientUtils
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
name|common
operator|.
name|SolrDocument
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
name|common
operator|.
name|SolrInputDocument
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
name|Ignore
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
DECL|class|SolrUpdateTest
specifier|public
class|class
name|SolrUpdateTest
extends|extends
name|SolrComponentTestSupport
block|{
DECL|field|solrEndpoint
specifier|private
name|SolrEndpoint
name|solrEndpoint
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|solrEndpoint
operator|=
name|getMandatoryEndpoint
argument_list|(
name|SOLR_ROUTE_URI
argument_list|,
name|SolrEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInsertSolrInputDocumentAsXMLWithoutAddRoot ()
specifier|public
name|void
name|testInsertSolrInputDocumentAsXMLWithoutAddRoot
parameter_list|()
throws|throws
name|Exception
block|{
name|SolrInputDocument
name|doc
init|=
operator|new
name|SolrInputDocument
argument_list|()
decl_stmt|;
name|doc
operator|.
name|addField
argument_list|(
literal|"id"
argument_list|,
literal|"MA147LL/A"
argument_list|,
literal|1.0f
argument_list|)
expr_stmt|;
name|String
name|docAsXml
init|=
name|ClientUtils
operator|.
name|toXML
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|docAsXml
argument_list|,
name|SolrConstants
operator|.
name|OPERATION
argument_list|,
name|SolrConstants
operator|.
name|OPERATION_INSERT
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
name|QueryResponse
name|response
init|=
name|executeSolrQuery
argument_list|(
literal|"id:MA147LL/A"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|response
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
DECL|method|testInsertSolrInputDocumentAsXMLWithAddRoot ()
specifier|public
name|void
name|testInsertSolrInputDocumentAsXMLWithAddRoot
parameter_list|()
throws|throws
name|Exception
block|{
name|SolrInputDocument
name|doc
init|=
operator|new
name|SolrInputDocument
argument_list|()
decl_stmt|;
name|doc
operator|.
name|addField
argument_list|(
literal|"id"
argument_list|,
literal|"MA147LL/A"
argument_list|,
literal|1.0f
argument_list|)
expr_stmt|;
name|String
name|docAsXml
init|=
literal|"<add>"
operator|+
name|ClientUtils
operator|.
name|toXML
argument_list|(
name|doc
argument_list|)
operator|+
literal|"</add>"
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|docAsXml
argument_list|,
name|SolrConstants
operator|.
name|OPERATION
argument_list|,
name|SolrConstants
operator|.
name|OPERATION_INSERT
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
name|QueryResponse
name|response
init|=
name|executeSolrQuery
argument_list|(
literal|"id:MA147LL/A"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|response
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
DECL|method|testInsertSolrInputDocument ()
specifier|public
name|void
name|testInsertSolrInputDocument
parameter_list|()
throws|throws
name|Exception
block|{
name|SolrInputDocument
name|doc
init|=
operator|new
name|SolrInputDocument
argument_list|()
decl_stmt|;
name|doc
operator|.
name|addField
argument_list|(
literal|"id"
argument_list|,
literal|"MA147LL/A"
argument_list|,
literal|1.0f
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|doc
argument_list|,
name|SolrConstants
operator|.
name|OPERATION
argument_list|,
name|SolrConstants
operator|.
name|OPERATION_INSERT
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
name|QueryResponse
name|response
init|=
name|executeSolrQuery
argument_list|(
literal|"id:MA147LL/A"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|response
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
DECL|method|testInsertStreaming ()
specifier|public
name|void
name|testInsertStreaming
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SolrConstants
operator|.
name|OPERATION
argument_list|,
name|SolrConstants
operator|.
name|OPERATION_INSERT_STREAMING
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrField.id"
argument_list|,
literal|"MA147LL/A"
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
name|QueryResponse
name|response
init|=
name|executeSolrQuery
argument_list|(
literal|"id:MA147LL/A"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|response
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
DECL|method|indexSingleDocumentOnlyWithId ()
specifier|public
name|void
name|indexSingleDocumentOnlyWithId
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrField.id"
argument_list|,
literal|"MA147LL/A"
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
comment|// Check things were indexed.
name|QueryResponse
name|response
init|=
name|executeSolrQuery
argument_list|(
literal|"id:MA147LL/A"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|response
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
DECL|method|caughtSolrExceptionIsHandledElegantly ()
specifier|public
name|void
name|caughtSolrExceptionIsHandledElegantly
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrField.name"
argument_list|,
literal|"Missing required field throws exception."
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SolrServerException
operator|.
name|class
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setHeadersAsSolrFields ()
specifier|public
name|void
name|setHeadersAsSolrFields
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|"Body is ignored"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrField.id"
argument_list|,
literal|"MA147LL/A"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrField.name"
argument_list|,
literal|"Apple 60 GB iPod with Video Playback Black"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrField.manu"
argument_list|,
literal|"Apple Computer Inc."
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
name|QueryResponse
name|response
init|=
name|executeSolrQuery
argument_list|(
literal|"id:MA147LL/A"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|response
operator|.
name|getResults
argument_list|()
operator|.
name|getNumFound
argument_list|()
argument_list|)
expr_stmt|;
name|SolrDocument
name|doc
init|=
name|response
operator|.
name|getResults
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Apple 60 GB iPod with Video Playback Black"
argument_list|,
name|doc
operator|.
name|getFieldValue
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Apple Computer Inc."
argument_list|,
name|doc
operator|.
name|getFieldValue
argument_list|(
literal|"manu"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setMultiValuedFieldInHeader ()
specifier|public
name|void
name|setMultiValuedFieldInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|categories
init|=
block|{
literal|"electronics"
block|,
literal|"apple"
block|}
decl_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|"Test body for iPod."
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrField.id"
argument_list|,
literal|"MA147LL/A"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrField.cat"
argument_list|,
name|categories
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
comment|// Check things were indexed.
name|QueryResponse
name|response
init|=
name|executeSolrQuery
argument_list|(
literal|"id:MA147LL/A"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|response
operator|.
name|getResults
argument_list|()
operator|.
name|getNumFound
argument_list|()
argument_list|)
expr_stmt|;
name|SolrDocument
name|doc
init|=
name|response
operator|.
name|getResults
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
name|categories
argument_list|,
operator|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|doc
operator|.
name|getFieldValue
argument_list|(
literal|"cat"
argument_list|)
operator|)
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|indexDocumentsAndThenCommit ()
specifier|public
name|void
name|indexDocumentsAndThenCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrField.id"
argument_list|,
literal|"MA147LL/A"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrField.name"
argument_list|,
literal|"Apple 60 GB iPod with Video Playback Black"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrField.manu"
argument_list|,
literal|"Apple Computer Inc."
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|QueryResponse
name|response
init|=
name|executeSolrQuery
argument_list|(
literal|"*:*"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|response
operator|.
name|getResults
argument_list|()
operator|.
name|getNumFound
argument_list|()
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
name|QueryResponse
name|afterCommitResponse
init|=
name|executeSolrQuery
argument_list|(
literal|"*:*"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|afterCommitResponse
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|afterCommitResponse
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
DECL|method|invalidSolrParametersAreIgnored ()
specifier|public
name|void
name|invalidSolrParametersAreIgnored
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrField.id"
argument_list|,
literal|"MA147LL/A"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrField.name"
argument_list|,
literal|"Apple 60 GB iPod with Video Playback Black"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrParam.invalid-param"
argument_list|,
literal|"this is ignored"
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
name|QueryResponse
name|response
init|=
name|executeSolrQuery
argument_list|(
literal|"*:*"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|response
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
DECL|method|indexDocumentsToCSVUpdateHandlerWithoutParameters ()
specifier|public
name|void
name|indexDocumentsToCSVUpdateHandlerWithoutParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|solrEndpoint
operator|.
name|setRequestHandler
argument_list|(
literal|"/update/csv"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/data/books.csv"
argument_list|)
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
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
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
name|QueryResponse
name|response
init|=
name|executeSolrQuery
argument_list|(
literal|"*:*"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|response
operator|.
name|getResults
argument_list|()
operator|.
name|getNumFound
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|=
name|executeSolrQuery
argument_list|(
literal|"id:0553573403"
argument_list|)
expr_stmt|;
name|SolrDocument
name|doc
init|=
name|response
operator|.
name|getResults
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A Game of Thrones"
argument_list|,
name|doc
operator|.
name|getFieldValue
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7.99f
argument_list|,
name|doc
operator|.
name|getFieldValue
argument_list|(
literal|"price"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|indexDocumentsToCSVUpdateHandlerWithParameters ()
specifier|public
name|void
name|indexDocumentsToCSVUpdateHandlerWithParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|solrEndpoint
operator|.
name|setRequestHandler
argument_list|(
literal|"/update/csv"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/data/books.csv"
argument_list|)
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrParam.fieldnames"
argument_list|,
literal|"id,cat,name,price,inStock,author_t,series_t,sequence_i,genre_s"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrParam.skip"
argument_list|,
literal|"cat,sequence_i,genre_s"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrParam.skipLines"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
name|QueryResponse
name|response
init|=
name|executeSolrQuery
argument_list|(
literal|"*:*"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|response
operator|.
name|getResults
argument_list|()
operator|.
name|getNumFound
argument_list|()
argument_list|)
expr_stmt|;
name|SolrDocument
name|doc
init|=
name|response
operator|.
name|getResults
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|doc
operator|.
name|getFieldNames
argument_list|()
operator|.
name|contains
argument_list|(
literal|"cat"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|indexPDFDocumentToExtractingRequestHandler ()
specifier|public
name|void
name|indexPDFDocumentToExtractingRequestHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|solrEndpoint
operator|.
name|setRequestHandler
argument_list|(
literal|"/update/extract"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/data/tutorial.pdf"
argument_list|)
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrParam.literal.id"
argument_list|,
literal|"tutorial.pdf"
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
name|QueryResponse
name|response
init|=
name|executeSolrQuery
argument_list|(
literal|"*:*"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|response
operator|.
name|getResults
argument_list|()
operator|.
name|getNumFound
argument_list|()
argument_list|)
expr_stmt|;
name|SolrDocument
name|doc
init|=
name|response
operator|.
name|getResults
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Solr"
argument_list|,
name|doc
operator|.
name|getFieldValue
argument_list|(
literal|"subject"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tutorial.pdf"
argument_list|,
name|doc
operator|.
name|getFieldValue
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"application/pdf"
argument_list|)
argument_list|,
name|doc
operator|.
name|getFieldValue
argument_list|(
literal|"content_type"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"No real advantage has yet been discovered to specifying the file in a header."
argument_list|)
DECL|method|indexPDFDocumentSpecifyingFileInParameters ()
specifier|public
name|void
name|indexPDFDocumentSpecifyingFileInParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|solrEndpoint
operator|.
name|setRequestHandler
argument_list|(
literal|"/update/extract"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|createExchangeWithBody
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrParam.stream.file"
argument_list|,
literal|"src/test/resources/data/tutorial.pdf"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"SolrParam.literal.id"
argument_list|,
literal|"tutorial.pdf"
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|solrCommit
argument_list|()
expr_stmt|;
name|QueryResponse
name|response
init|=
name|executeSolrQuery
argument_list|(
literal|"*:*"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|response
operator|.
name|getResults
argument_list|()
operator|.
name|getNumFound
argument_list|()
argument_list|)
expr_stmt|;
name|SolrDocument
name|doc
init|=
name|response
operator|.
name|getResults
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Solr"
argument_list|,
name|doc
operator|.
name|getFieldValue
argument_list|(
literal|"subject"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tutorial.pdf"
argument_list|,
name|doc
operator|.
name|getFieldValue
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"application/pdf"
argument_list|)
argument_list|,
name|doc
operator|.
name|getFieldValue
argument_list|(
literal|"content_type"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

