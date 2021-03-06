begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|IOException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|Logger
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
name|SolrClient
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
name|HttpSolrClient
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
name|request
operator|.
name|AbstractUpdateRequest
operator|.
name|ACTION
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
name|request
operator|.
name|UpdateRequest
import|;
end_import

begin_class
DECL|class|SolrFixtures
specifier|public
class|class
name|SolrFixtures
block|{
DECL|field|log
specifier|static
name|Logger
name|log
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|SolrFixtures
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|solrRunner
specifier|private
specifier|static
name|JettySolrRunner
name|solrRunner
decl_stmt|;
DECL|field|solrHttpsRunner
specifier|private
specifier|static
name|JettySolrRunner
name|solrHttpsRunner
decl_stmt|;
DECL|field|solrServer
specifier|private
specifier|static
name|HttpSolrClient
name|solrServer
decl_stmt|;
DECL|field|solrHttpsServer
specifier|private
specifier|static
name|HttpSolrClient
name|solrHttpsServer
decl_stmt|;
DECL|field|cloudFixture
specifier|private
specifier|static
name|SolrCloudFixture
name|cloudFixture
decl_stmt|;
DECL|field|port
specifier|private
specifier|static
name|int
name|port
decl_stmt|;
DECL|field|httpsPort
specifier|private
specifier|static
name|int
name|httpsPort
decl_stmt|;
DECL|enum|TestServerType
specifier|public
enum|enum
name|TestServerType
block|{
DECL|enumConstant|USE_HTTP
DECL|enumConstant|USE_HTTPS
DECL|enumConstant|USE_CLOUD
name|USE_HTTP
block|,
name|USE_HTTPS
block|,
name|USE_CLOUD
block|}
DECL|field|serverType
name|TestServerType
name|serverType
decl_stmt|;
DECL|method|SolrFixtures (TestServerType serverType)
name|SolrFixtures
parameter_list|(
name|TestServerType
name|serverType
parameter_list|)
block|{
name|this
operator|.
name|serverType
operator|=
name|serverType
expr_stmt|;
block|}
DECL|method|solrRouteUri ()
name|String
name|solrRouteUri
parameter_list|()
block|{
if|if
condition|(
name|serverType
operator|==
name|TestServerType
operator|.
name|USE_HTTPS
condition|)
block|{
return|return
literal|"solrs://127.0.0.1:"
operator|+
name|httpsPort
operator|+
literal|"/solr/collection1"
operator|+
literal|"?username=solr&password=SolrRocks"
return|;
block|}
elseif|else
if|if
condition|(
name|serverType
operator|==
name|TestServerType
operator|.
name|USE_CLOUD
condition|)
block|{
name|String
name|zkAddrStr
init|=
name|cloudFixture
operator|.
name|miniCluster
operator|.
name|getZkServer
argument_list|()
operator|.
name|getZkAddress
argument_list|()
decl_stmt|;
return|return
literal|"solrCloud://localhost:"
operator|+
name|httpsPort
operator|+
literal|"/solr?zkHost="
operator|+
name|zkAddrStr
operator|+
literal|"&collection=collection1&username=solr&password=SolrRocks"
return|;
block|}
else|else
block|{
return|return
literal|"solr://localhost:"
operator|+
name|port
operator|+
literal|"/solr/collection1"
operator|+
literal|"?username=solr&password=SolrRocks"
return|;
block|}
block|}
DECL|method|getServer ()
name|SolrClient
name|getServer
parameter_list|()
block|{
if|if
condition|(
name|serverType
operator|==
name|TestServerType
operator|.
name|USE_HTTPS
condition|)
block|{
return|return
name|solrHttpsServer
return|;
block|}
elseif|else
if|if
condition|(
name|serverType
operator|==
name|TestServerType
operator|.
name|USE_CLOUD
condition|)
block|{
return|return
name|cloudFixture
operator|.
name|solrClient
return|;
block|}
else|else
block|{
return|return
name|solrServer
return|;
block|}
block|}
DECL|method|createSolrFixtures ()
specifier|static
name|void
name|createSolrFixtures
parameter_list|()
throws|throws
name|Exception
block|{
name|solrHttpsRunner
operator|=
name|JettySolrFactory
operator|.
name|createJettyTestFixture
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|httpsPort
operator|=
name|solrHttpsRunner
operator|.
name|getLocalPort
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Started Https Test Server: "
operator|+
name|solrHttpsRunner
operator|.
name|getBaseUrl
argument_list|()
argument_list|)
expr_stmt|;
name|solrHttpsServer
operator|=
operator|new
name|HttpSolrClient
operator|.
name|Builder
argument_list|(
literal|"https://127.0.0.1:"
operator|+
name|httpsPort
operator|+
literal|"/solr"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|solrHttpsServer
operator|.
name|setConnectionTimeout
argument_list|(
literal|60000
argument_list|)
expr_stmt|;
name|solrRunner
operator|=
name|JettySolrFactory
operator|.
name|createJettyTestFixture
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|port
operator|=
name|solrRunner
operator|.
name|getLocalPort
argument_list|()
expr_stmt|;
name|solrServer
operator|=
operator|new
name|HttpSolrClient
operator|.
name|Builder
argument_list|(
literal|"http://localhost:"
operator|+
name|port
operator|+
literal|"/solr"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Started Test Server: "
operator|+
name|solrRunner
operator|.
name|getBaseUrl
argument_list|()
argument_list|)
expr_stmt|;
name|cloudFixture
operator|=
operator|new
name|SolrCloudFixture
argument_list|(
literal|"src/test/resources/solr"
argument_list|)
expr_stmt|;
block|}
DECL|method|teardownSolrFixtures ()
specifier|public
specifier|static
name|void
name|teardownSolrFixtures
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|solrRunner
operator|!=
literal|null
condition|)
block|{
name|solrRunner
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|solrHttpsRunner
operator|!=
literal|null
condition|)
block|{
name|solrHttpsRunner
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|cloudFixture
operator|!=
literal|null
condition|)
block|{
name|cloudFixture
operator|.
name|teardown
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|clearIndex ()
specifier|public
specifier|static
name|void
name|clearIndex
parameter_list|()
throws|throws
name|SolrServerException
throws|,
name|IOException
block|{
name|UpdateRequest
name|updateRequest
init|=
operator|new
name|UpdateRequest
argument_list|()
decl_stmt|;
name|updateRequest
operator|.
name|setBasicAuthCredentials
argument_list|(
literal|"solr"
argument_list|,
literal|"SolrRocks"
argument_list|)
expr_stmt|;
name|updateRequest
operator|.
name|deleteByQuery
argument_list|(
literal|"*:*"
argument_list|)
expr_stmt|;
name|updateRequest
operator|.
name|setAction
argument_list|(
name|ACTION
operator|.
name|COMMIT
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|solrServer
operator|!=
literal|null
condition|)
block|{
comment|// Clear the Solr index.
name|updateRequest
operator|.
name|process
argument_list|(
name|solrServer
argument_list|,
literal|"collection1"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|solrHttpsServer
operator|!=
literal|null
condition|)
block|{
name|updateRequest
operator|.
name|process
argument_list|(
name|solrHttpsServer
argument_list|,
literal|"collection1"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cloudFixture
operator|!=
literal|null
condition|)
block|{
name|updateRequest
operator|.
name|process
argument_list|(
name|cloudFixture
operator|.
name|solrClient
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

