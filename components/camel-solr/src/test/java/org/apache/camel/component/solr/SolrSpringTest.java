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
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|assertEquals
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
name|Produce
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
name|ProducerTemplate
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
name|CommonsHttpSolrServer
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
name|common
operator|.
name|SolrDocument
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
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"/SolrSpringTest-context.xml"
block|}
argument_list|)
DECL|class|SolrSpringTest
specifier|public
class|class
name|SolrSpringTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
static|static
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"SolrServer.Port"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|SolrComponentTestSupport
operator|.
name|PORT
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|field|solrRunner
specifier|private
specifier|static
name|JettySolrRunner
name|solrRunner
decl_stmt|;
DECL|field|solrServer
specifier|private
specifier|static
name|CommonsHttpSolrServer
name|solrServer
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:xml-start"
argument_list|)
DECL|field|xmlRoute
specifier|protected
name|ProducerTemplate
name|xmlRoute
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:xml-start-streaming"
argument_list|)
DECL|field|xmlRouteStreaming
specifier|protected
name|ProducerTemplate
name|xmlRouteStreaming
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:pdf-start"
argument_list|)
DECL|field|pdfRoute
specifier|protected
name|ProducerTemplate
name|pdfRoute
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:pdf-start-streaming"
argument_list|)
DECL|field|pdfRouteStreaming
specifier|protected
name|ProducerTemplate
name|pdfRouteStreaming
decl_stmt|;
annotation|@
name|DirtiesContext
annotation|@
name|Test
DECL|method|endToEndIndexXMLDocuments ()
specifier|public
name|void
name|endToEndIndexXMLDocuments
parameter_list|()
throws|throws
name|Exception
block|{
name|xmlRoute
operator|.
name|sendBody
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/data/books.xml"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check things were indexed.
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
literal|4
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
comment|// Check fields were indexed correctly.
name|response
operator|=
name|executeSolrQuery
argument_list|(
literal|"title:Learning XML"
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
literal|"Learning XML"
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
literal|"Web"
argument_list|,
literal|"Technology"
argument_list|,
literal|"Computers"
argument_list|)
argument_list|,
name|doc
operator|.
name|getFieldValue
argument_list|(
literal|"cat"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|DirtiesContext
annotation|@
name|Test
DECL|method|endToEndIndexXMLDocumentsStreaming ()
specifier|public
name|void
name|endToEndIndexXMLDocumentsStreaming
parameter_list|()
throws|throws
name|Exception
block|{
name|xmlRouteStreaming
operator|.
name|sendBody
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/data/books.xml"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check things were indexed.
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
literal|4
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
comment|// Check fields were indexed correctly.
name|response
operator|=
name|executeSolrQuery
argument_list|(
literal|"title:Learning XML"
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
literal|"Learning XML"
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
literal|"Web"
argument_list|,
literal|"Technology"
argument_list|,
literal|"Computers"
argument_list|)
argument_list|,
name|doc
operator|.
name|getFieldValue
argument_list|(
literal|"cat"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|DirtiesContext
annotation|@
name|Test
DECL|method|endToEndIndexPDFDocument ()
specifier|public
name|void
name|endToEndIndexPDFDocument
parameter_list|()
throws|throws
name|Exception
block|{
name|pdfRoute
operator|.
name|sendBody
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/data/tutorial.pdf"
argument_list|)
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
name|DirtiesContext
annotation|@
name|Test
DECL|method|endToEndIndexPDFDocumentStreaming ()
specifier|public
name|void
name|endToEndIndexPDFDocumentStreaming
parameter_list|()
throws|throws
name|Exception
block|{
name|pdfRouteStreaming
operator|.
name|sendBody
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/data/tutorial.pdf"
argument_list|)
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
comment|// Set appropriate paths for Solr to use.
name|System
operator|.
name|setProperty
argument_list|(
literal|"solr.solr.home"
argument_list|,
literal|"src/test/resources/solr"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"solr.data.dir"
argument_list|,
literal|"target/test-classes/solr/data"
argument_list|)
expr_stmt|;
comment|// Instruct Solr to keep the index in memory, for faster testing.
name|System
operator|.
name|setProperty
argument_list|(
literal|"solr.directoryFactory"
argument_list|,
literal|"solr.RAMDirectoryFactory"
argument_list|)
expr_stmt|;
comment|// Start a Solr instance.
name|solrRunner
operator|=
operator|new
name|JettySolrRunner
argument_list|(
literal|"/solr"
argument_list|,
name|SolrComponentTestSupport
operator|.
name|PORT
argument_list|)
expr_stmt|;
name|solrRunner
operator|.
name|start
argument_list|()
expr_stmt|;
name|solrServer
operator|=
operator|new
name|CommonsHttpSolrServer
argument_list|(
literal|"http://localhost:"
operator|+
name|SolrComponentTestSupport
operator|.
name|PORT
operator|+
literal|"/solr"
argument_list|)
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
name|solrRunner
operator|.
name|stop
argument_list|()
expr_stmt|;
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
comment|// Clear the Solr index.
name|solrServer
operator|.
name|deleteByQuery
argument_list|(
literal|"*:*"
argument_list|)
expr_stmt|;
name|solrServer
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
DECL|method|executeSolrQuery (String query)
specifier|private
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
return|return
name|solrServer
operator|.
name|query
argument_list|(
name|solrQuery
argument_list|)
return|;
block|}
block|}
end_class

end_unit

