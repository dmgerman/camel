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
name|io
operator|.
name|FileInputStream
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
name|util
operator|.
name|IOHelper
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
name|CloudSolrClient
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
name|QueryRequest
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
name|cloud
operator|.
name|MiniSolrCloudCluster
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
name|apache
operator|.
name|solr
operator|.
name|common
operator|.
name|cloud
operator|.
name|SolrZkClient
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
name|params
operator|.
name|CollectionParams
operator|.
name|CollectionAction
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
name|params
operator|.
name|CoreAdminParams
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
name|params
operator|.
name|ModifiableSolrParams
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
name|util
operator|.
name|NamedList
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
DECL|class|SolrCloudFixture
specifier|public
class|class
name|SolrCloudFixture
block|{
comment|/**      * Create indexes in this directory, optimally use a subdir, named after the      * test      */
DECL|field|TEMP_DIR
specifier|public
specifier|static
specifier|final
name|File
name|TEMP_DIR
decl_stmt|;
static|static
block|{
name|String
name|s
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"tempDir"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"To run tests, you need to define system property 'tempDir' or 'java.io.tmpdir'."
argument_list|)
throw|;
block|}
name|TEMP_DIR
operator|=
operator|new
name|File
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|TEMP_DIR
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Created: "
operator|+
name|TEMP_DIR
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|field|log
specifier|static
name|Logger
name|log
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|SolrCloudFixture
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|miniCluster
name|MiniSolrCloudCluster
name|miniCluster
decl_stmt|;
DECL|field|testDir
name|File
name|testDir
decl_stmt|;
DECL|field|zkClient
name|SolrZkClient
name|zkClient
decl_stmt|;
DECL|field|solrClient
name|CloudSolrClient
name|solrClient
decl_stmt|;
DECL|method|SolrCloudFixture (String solrHome)
specifier|public
name|SolrCloudFixture
parameter_list|(
name|String
name|solrHome
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|xml
init|=
name|IOHelper
operator|.
name|loadText
argument_list|(
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|solrHome
argument_list|,
literal|"solr-no-core.xml"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|miniCluster
operator|=
operator|new
name|MiniSolrCloudCluster
argument_list|(
literal|1
argument_list|,
literal|"/solr"
argument_list|,
operator|new
name|File
argument_list|(
literal|"target/tmp"
argument_list|)
operator|.
name|toPath
argument_list|()
argument_list|,
name|xml
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|zkAddr
init|=
name|miniCluster
operator|.
name|getZkServer
argument_list|()
operator|.
name|getZkAddress
argument_list|()
decl_stmt|;
name|String
name|zkHost
init|=
name|miniCluster
operator|.
name|getZkServer
argument_list|()
operator|.
name|getZkHost
argument_list|()
decl_stmt|;
name|buildZooKeeper
argument_list|(
name|zkHost
argument_list|,
name|zkAddr
argument_list|,
operator|new
name|File
argument_list|(
name|solrHome
argument_list|)
argument_list|,
literal|"solrconfig.xml"
argument_list|,
literal|"schema.xml"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JettySolrRunner
argument_list|>
name|jettys
init|=
name|miniCluster
operator|.
name|getJettySolrRunners
argument_list|()
decl_stmt|;
for|for
control|(
name|JettySolrRunner
name|jetty
range|:
name|jettys
control|)
block|{
if|if
condition|(
operator|!
name|jetty
operator|.
name|isRunning
argument_list|()
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"JETTY NOT RUNNING!"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|info
argument_list|(
literal|"JETTY RUNNING AT "
operator|+
name|jetty
operator|.
name|getBaseUrl
argument_list|()
operator|+
literal|" PORT "
operator|+
name|jetty
operator|.
name|getLocalPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|solrClient
operator|=
operator|new
name|CloudSolrClient
argument_list|(
name|zkAddr
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|solrClient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|createCollection
argument_list|(
name|solrClient
argument_list|,
literal|"collection1"
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|"conf1"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// takes some time to setup the collection...
comment|// otherwise you'll get no live solr servers
name|solrClient
operator|.
name|setDefaultCollection
argument_list|(
literal|"collection1"
argument_list|)
expr_stmt|;
name|SolrInputDocument
name|doc
init|=
operator|new
name|SolrInputDocument
argument_list|()
decl_stmt|;
name|doc
operator|.
name|setField
argument_list|(
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|solrClient
operator|.
name|add
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|solrClient
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
DECL|method|putConfig (String confName, SolrZkClient zkClient, File solrhome, final String name)
specifier|public
specifier|static
name|void
name|putConfig
parameter_list|(
name|String
name|confName
parameter_list|,
name|SolrZkClient
name|zkClient
parameter_list|,
name|File
name|solrhome
parameter_list|,
specifier|final
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|putConfig
argument_list|(
name|confName
argument_list|,
name|zkClient
argument_list|,
name|solrhome
argument_list|,
name|name
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|createCollection (CloudSolrClient server, String name, int numShards, int replicationFactor, String configName)
specifier|protected
name|NamedList
argument_list|<
name|Object
argument_list|>
name|createCollection
parameter_list|(
name|CloudSolrClient
name|server
parameter_list|,
name|String
name|name
parameter_list|,
name|int
name|numShards
parameter_list|,
name|int
name|replicationFactor
parameter_list|,
name|String
name|configName
parameter_list|)
throws|throws
name|Exception
block|{
name|ModifiableSolrParams
name|modParams
init|=
operator|new
name|ModifiableSolrParams
argument_list|()
decl_stmt|;
name|modParams
operator|.
name|set
argument_list|(
name|CoreAdminParams
operator|.
name|ACTION
argument_list|,
name|CollectionAction
operator|.
name|CREATE
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|modParams
operator|.
name|set
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|modParams
operator|.
name|set
argument_list|(
literal|"numShards"
argument_list|,
name|numShards
argument_list|)
expr_stmt|;
name|modParams
operator|.
name|set
argument_list|(
literal|"replicationFactor"
argument_list|,
name|replicationFactor
argument_list|)
expr_stmt|;
name|modParams
operator|.
name|set
argument_list|(
literal|"collection.configName"
argument_list|,
name|configName
argument_list|)
expr_stmt|;
name|QueryRequest
name|request
init|=
operator|new
name|QueryRequest
argument_list|(
name|modParams
argument_list|)
decl_stmt|;
name|request
operator|.
name|setPath
argument_list|(
literal|"/admin/collections"
argument_list|)
expr_stmt|;
return|return
name|server
operator|.
name|request
argument_list|(
name|request
argument_list|)
return|;
block|}
DECL|method|putConfig (String confName, SolrZkClient zkClient, File solrhome, final String srcName, String destName)
specifier|public
specifier|static
name|void
name|putConfig
parameter_list|(
name|String
name|confName
parameter_list|,
name|SolrZkClient
name|zkClient
parameter_list|,
name|File
name|solrhome
parameter_list|,
specifier|final
name|String
name|srcName
parameter_list|,
name|String
name|destName
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|solrhome
argument_list|,
literal|"collection1"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"conf"
operator|+
name|File
operator|.
name|separator
operator|+
name|srcName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"zk skipping "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" because it doesn't exist"
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|destPath
init|=
literal|"/configs/"
operator|+
name|confName
operator|+
literal|"/"
operator|+
name|destName
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"zk put "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" to "
operator|+
name|destPath
argument_list|)
expr_stmt|;
name|zkClient
operator|.
name|makePath
argument_list|(
name|destPath
argument_list|,
name|file
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// static to share with distrib test
DECL|method|buildZooKeeper (String zkHost, String zkAddress, File solrhome, String config, String schema)
specifier|public
name|void
name|buildZooKeeper
parameter_list|(
name|String
name|zkHost
parameter_list|,
name|String
name|zkAddress
parameter_list|,
name|File
name|solrhome
parameter_list|,
name|String
name|config
parameter_list|,
name|String
name|schema
parameter_list|)
throws|throws
name|Exception
block|{
name|zkClient
operator|=
operator|new
name|SolrZkClient
argument_list|(
name|zkAddress
argument_list|,
literal|60000
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"configName"
argument_list|,
literal|"conf1"
argument_list|)
expr_stmt|;
comment|// for now, always upload the config and schema to the canonical names
name|putConfig
argument_list|(
literal|"conf1"
argument_list|,
name|zkClient
argument_list|,
name|solrhome
argument_list|,
name|config
argument_list|,
literal|"solrconfig.xml"
argument_list|)
expr_stmt|;
name|putConfig
argument_list|(
literal|"conf1"
argument_list|,
name|zkClient
argument_list|,
name|solrhome
argument_list|,
name|schema
argument_list|,
literal|"schema.xml"
argument_list|)
expr_stmt|;
name|putConfig
argument_list|(
literal|"conf1"
argument_list|,
name|zkClient
argument_list|,
name|solrhome
argument_list|,
literal|"stopwords.txt"
argument_list|)
expr_stmt|;
name|putConfig
argument_list|(
literal|"conf1"
argument_list|,
name|zkClient
argument_list|,
name|solrhome
argument_list|,
literal|"stopwords_en.txt"
argument_list|)
expr_stmt|;
name|putConfig
argument_list|(
literal|"conf1"
argument_list|,
name|zkClient
argument_list|,
name|solrhome
argument_list|,
literal|"protwords.txt"
argument_list|)
expr_stmt|;
name|putConfig
argument_list|(
literal|"conf1"
argument_list|,
name|zkClient
argument_list|,
name|solrhome
argument_list|,
literal|"currency.xml"
argument_list|)
expr_stmt|;
name|putConfig
argument_list|(
literal|"conf1"
argument_list|,
name|zkClient
argument_list|,
name|solrhome
argument_list|,
literal|"enumsConfig.xml"
argument_list|)
expr_stmt|;
name|putConfig
argument_list|(
literal|"conf1"
argument_list|,
name|zkClient
argument_list|,
name|solrhome
argument_list|,
literal|"open-exchange-rates.json"
argument_list|)
expr_stmt|;
name|putConfig
argument_list|(
literal|"conf1"
argument_list|,
name|zkClient
argument_list|,
name|solrhome
argument_list|,
literal|"mapping-ISOLatin1Accent.txt"
argument_list|)
expr_stmt|;
name|putConfig
argument_list|(
literal|"conf1"
argument_list|,
name|zkClient
argument_list|,
name|solrhome
argument_list|,
literal|"old_synonyms.txt"
argument_list|)
expr_stmt|;
name|putConfig
argument_list|(
literal|"conf1"
argument_list|,
name|zkClient
argument_list|,
name|solrhome
argument_list|,
literal|"synonyms.txt"
argument_list|)
expr_stmt|;
name|putConfig
argument_list|(
literal|"conf1"
argument_list|,
name|zkClient
argument_list|,
name|solrhome
argument_list|,
literal|"elevate.xml"
argument_list|)
expr_stmt|;
name|zkClient
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|teardown ()
specifier|public
name|void
name|teardown
parameter_list|()
throws|throws
name|Exception
block|{
name|solrClient
operator|.
name|close
argument_list|()
expr_stmt|;
name|miniCluster
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|solrClient
operator|=
literal|null
expr_stmt|;
name|miniCluster
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

