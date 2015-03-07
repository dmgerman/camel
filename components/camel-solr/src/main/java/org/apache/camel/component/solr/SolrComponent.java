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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|Endpoint
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
name|impl
operator|.
name|UriEndpointComponent
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
name|impl
operator|.
name|CloudSolrServer
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
name|ConcurrentUpdateSolrServer
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link SolrEndpoint}.  */
end_comment

begin_class
DECL|class|SolrComponent
specifier|public
class|class
name|SolrComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SolrComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|servers
specifier|private
specifier|final
name|Map
argument_list|<
name|SolrEndpoint
argument_list|,
name|SolrServerReference
argument_list|>
name|servers
init|=
operator|new
name|HashMap
argument_list|<
name|SolrEndpoint
argument_list|,
name|SolrServerReference
argument_list|>
argument_list|()
decl_stmt|;
DECL|class|SolrServerReference
specifier|protected
specifier|static
specifier|final
class|class
name|SolrServerReference
block|{
DECL|field|referenceCounter
specifier|private
specifier|final
name|AtomicInteger
name|referenceCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|solrServer
specifier|private
name|HttpSolrServer
name|solrServer
decl_stmt|;
DECL|field|updateSolrServer
specifier|private
name|ConcurrentUpdateSolrServer
name|updateSolrServer
decl_stmt|;
DECL|field|cloudSolrServer
specifier|private
name|CloudSolrServer
name|cloudSolrServer
decl_stmt|;
DECL|method|getSolrServer ()
specifier|public
name|HttpSolrServer
name|getSolrServer
parameter_list|()
block|{
return|return
name|solrServer
return|;
block|}
DECL|method|setSolrServer (HttpSolrServer solrServer)
specifier|public
name|void
name|setSolrServer
parameter_list|(
name|HttpSolrServer
name|solrServer
parameter_list|)
block|{
name|this
operator|.
name|solrServer
operator|=
name|solrServer
expr_stmt|;
block|}
DECL|method|getUpdateSolrServer ()
specifier|public
name|ConcurrentUpdateSolrServer
name|getUpdateSolrServer
parameter_list|()
block|{
return|return
name|updateSolrServer
return|;
block|}
DECL|method|setUpdateSolrServer (ConcurrentUpdateSolrServer updateSolrServer)
specifier|public
name|void
name|setUpdateSolrServer
parameter_list|(
name|ConcurrentUpdateSolrServer
name|updateSolrServer
parameter_list|)
block|{
name|this
operator|.
name|updateSolrServer
operator|=
name|updateSolrServer
expr_stmt|;
block|}
DECL|method|getCloudSolrServer ()
specifier|public
name|CloudSolrServer
name|getCloudSolrServer
parameter_list|()
block|{
return|return
name|cloudSolrServer
return|;
block|}
DECL|method|setCloudSolrServer (CloudSolrServer cloudServer)
specifier|public
name|void
name|setCloudSolrServer
parameter_list|(
name|CloudSolrServer
name|cloudServer
parameter_list|)
block|{
name|cloudSolrServer
operator|=
name|cloudServer
expr_stmt|;
block|}
DECL|method|addReference ()
specifier|public
name|int
name|addReference
parameter_list|()
block|{
return|return
name|referenceCounter
operator|.
name|incrementAndGet
argument_list|()
return|;
block|}
DECL|method|decReference ()
specifier|public
name|int
name|decReference
parameter_list|()
block|{
return|return
name|referenceCounter
operator|.
name|decrementAndGet
argument_list|()
return|;
block|}
block|}
DECL|method|SolrComponent ()
specifier|public
name|SolrComponent
parameter_list|()
block|{
name|super
argument_list|(
name|SolrEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
operator|new
name|SolrEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getSolrServers (SolrEndpoint endpoint)
specifier|public
name|SolrServerReference
name|getSolrServers
parameter_list|(
name|SolrEndpoint
name|endpoint
parameter_list|)
block|{
return|return
name|servers
operator|.
name|get
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
DECL|method|addSolrServers (SolrEndpoint endpoint, SolrServerReference servers)
specifier|public
name|void
name|addSolrServers
parameter_list|(
name|SolrEndpoint
name|endpoint
parameter_list|,
name|SolrServerReference
name|servers
parameter_list|)
block|{
name|this
operator|.
name|servers
operator|.
name|put
argument_list|(
name|endpoint
argument_list|,
name|servers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|SolrServerReference
name|server
range|:
name|servers
operator|.
name|values
argument_list|()
control|)
block|{
name|shutdownServers
argument_list|(
name|server
argument_list|)
expr_stmt|;
block|}
name|servers
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|shutdownServers (SolrServerReference ref)
name|void
name|shutdownServers
parameter_list|(
name|SolrServerReference
name|ref
parameter_list|)
block|{
name|shutdownServers
argument_list|(
name|ref
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|shutdownServer (SolrServer server)
specifier|private
name|void
name|shutdownServer
parameter_list|(
name|SolrServer
name|server
parameter_list|)
block|{
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Shutting down solr server: {}"
argument_list|,
name|server
argument_list|)
expr_stmt|;
name|server
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|shutdownServers (SolrServerReference ref, boolean remove)
name|void
name|shutdownServers
parameter_list|(
name|SolrServerReference
name|ref
parameter_list|,
name|boolean
name|remove
parameter_list|)
block|{
try|try
block|{
name|shutdownServer
argument_list|(
name|ref
operator|.
name|getSolrServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error shutting down solr server. This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|shutdownServer
argument_list|(
name|ref
operator|.
name|getUpdateSolrServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error shutting down streaming solr server. This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|shutdownServer
argument_list|(
name|ref
operator|.
name|getCloudSolrServer
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error shutting down streaming solr server. This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remove
condition|)
block|{
name|SolrEndpoint
name|key
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|SolrEndpoint
argument_list|,
name|SolrServerReference
argument_list|>
name|entry
range|:
name|servers
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|==
name|ref
condition|)
block|{
name|key
operator|=
name|entry
operator|.
name|getKey
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|servers
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

