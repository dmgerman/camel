begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.elasticsearch5
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|elasticsearch5
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
name|java
operator|.
name|net
operator|.
name|InetAddress
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|AvailablePortFinder
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
name|apache
operator|.
name|http
operator|.
name|HttpHost
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codelibs
operator|.
name|elasticsearch
operator|.
name|runner
operator|.
name|ElasticsearchClusterRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|client
operator|.
name|RestClient
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
name|BeforeClass
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|codelibs
operator|.
name|elasticsearch
operator|.
name|runner
operator|.
name|ElasticsearchClusterRunner
operator|.
name|newConfigs
import|;
end_import

begin_class
DECL|class|ElasticsearchBaseTest
specifier|public
class|class
name|ElasticsearchBaseTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|runner
specifier|public
specifier|static
name|ElasticsearchClusterRunner
name|runner
decl_stmt|;
DECL|field|clusterName
specifier|public
specifier|static
name|String
name|clusterName
decl_stmt|;
DECL|field|client
specifier|public
specifier|static
name|RestClient
name|client
decl_stmt|;
DECL|field|ES_BASE_TRANSPORT_PORT
specifier|protected
specifier|static
specifier|final
name|int
name|ES_BASE_TRANSPORT_PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|ES_BASE_HTTP_PORT
specifier|protected
specifier|static
specifier|final
name|int
name|ES_BASE_HTTP_PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
name|ES_BASE_TRANSPORT_PORT
operator|+
literal|1
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"resource"
argument_list|)
annotation|@
name|BeforeClass
DECL|method|cleanupOnce ()
specifier|public
specifier|static
name|void
name|cleanupOnce
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/testcluster/"
argument_list|)
expr_stmt|;
name|clusterName
operator|=
literal|"es-cl-run-"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
name|runner
operator|=
operator|new
name|ElasticsearchClusterRunner
argument_list|()
expr_stmt|;
comment|// create ES nodes
name|runner
operator|.
name|onBuild
argument_list|(
parameter_list|(
name|number
parameter_list|,
name|settingsBuilder
parameter_list|)
lambda|->
block|{
name|settingsBuilder
operator|.
name|put
argument_list|(
literal|"http.cors.enabled"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|settingsBuilder
operator|.
name|put
argument_list|(
literal|"http.cors.allow-origin"
argument_list|,
literal|"*"
argument_list|)
expr_stmt|;
block|}
argument_list|)
operator|.
name|build
argument_list|(
name|newConfigs
argument_list|()
operator|.
name|clusterName
argument_list|(
name|clusterName
argument_list|)
operator|.
name|numOfNode
argument_list|(
literal|1
argument_list|)
operator|.
name|baseHttpPort
argument_list|(
name|ES_BASE_TRANSPORT_PORT
argument_list|)
operator|.
name|basePath
argument_list|(
literal|"target/testcluster/"
argument_list|)
argument_list|)
expr_stmt|;
comment|// wait for green status
name|runner
operator|.
name|ensureGreen
argument_list|()
expr_stmt|;
name|client
operator|=
name|RestClient
operator|.
name|builder
argument_list|(
operator|new
name|HttpHost
argument_list|(
name|InetAddress
operator|.
name|getByName
argument_list|(
literal|"localhost"
argument_list|)
argument_list|,
name|ES_BASE_HTTP_PORT
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|teardownOnce ()
specifier|public
specifier|static
name|void
name|teardownOnce
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|runner
operator|!=
literal|null
condition|)
block|{
name|runner
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
comment|// let's speed up the tests using the same context
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|ElasticsearchComponent
name|elasticsearchComponent
init|=
operator|new
name|ElasticsearchComponent
argument_list|()
decl_stmt|;
name|elasticsearchComponent
operator|.
name|setHostAddresses
argument_list|(
literal|"localhost:"
operator|+
name|ES_BASE_HTTP_PORT
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"elasticsearch5-rest"
argument_list|,
name|elasticsearchComponent
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
comment|/**      * As we don't delete the {@code target/data} folder for<b>each</b> test      * below (otherwise they would run much slower), we need to make sure      * there's no side effect of the same used data through creating unique      * indexes.      */
DECL|method|createIndexedData (String... additionalPrefixes)
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|createIndexedData
parameter_list|(
name|String
modifier|...
name|additionalPrefixes
parameter_list|)
block|{
name|String
name|prefix
init|=
name|createPrefix
argument_list|()
decl_stmt|;
comment|// take over any potential prefixes we may have been asked for
if|if
condition|(
name|additionalPrefixes
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|additionalPrefix
range|:
name|additionalPrefixes
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|additionalPrefix
argument_list|)
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
expr_stmt|;
block|}
name|prefix
operator|=
name|sb
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|String
name|key
init|=
name|prefix
operator|+
literal|"key"
decl_stmt|;
name|String
name|value
init|=
name|prefix
operator|+
literal|"value"
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Creating indexed data using the key/value pair {} => {}"
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
DECL|method|createPrefix ()
name|String
name|createPrefix
parameter_list|()
block|{
comment|// make use of the test method name to avoid collision
return|return
name|getTestMethodName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|+
literal|"-"
return|;
block|}
block|}
end_class

end_unit

