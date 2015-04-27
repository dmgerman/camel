begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.elasticsearch
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|elasticsearch
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|camel
operator|.
name|util
operator|.
name|URISupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|action
operator|.
name|WriteConsistencyLevel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|elasticsearch
operator|.
name|action
operator|.
name|support
operator|.
name|replication
operator|.
name|ReplicationType
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
DECL|class|ElasticsearchConfigurationTest
specifier|public
class|class
name|ElasticsearchConfigurationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|localNode ()
specifier|public
name|void
name|localNode
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"elasticsearch://local?operation=INDEX&indexName=twitter&indexType=tweet"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|ElasticsearchConfiguration
name|conf
init|=
operator|new
name|ElasticsearchConfiguration
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|conf
operator|.
name|isLocal
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"twitter"
argument_list|,
name|conf
operator|.
name|getIndexName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tweet"
argument_list|,
name|conf
operator|.
name|getIndexType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"INDEX"
argument_list|,
name|conf
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|conf
operator|.
name|isData
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|conf
operator|.
name|getClusterName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|localNonDataNodeThrowsIllegalArgumentException ()
specifier|public
name|void
name|localNonDataNodeThrowsIllegalArgumentException
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"elasticsearch://local?operation=INDEX&indexName=twitter&indexType=tweet&data=false"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
operator|new
name|ElasticsearchConfiguration
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|localConfDefaultsToDataNode ()
specifier|public
name|void
name|localConfDefaultsToDataNode
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"elasticsearch://local?operation=INDEX&indexName=twitter&indexType=tweet"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|ElasticsearchConfiguration
name|conf
init|=
operator|new
name|ElasticsearchConfiguration
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"INDEX"
argument_list|,
name|conf
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|conf
operator|.
name|isLocal
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|conf
operator|.
name|isData
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|clusterConfDefaultsToNonDataNode ()
specifier|public
name|void
name|clusterConfDefaultsToNonDataNode
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"elasticsearch://clustername?operation=INDEX&indexName=twitter&indexType=tweet"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|ElasticsearchConfiguration
name|conf
init|=
operator|new
name|ElasticsearchConfiguration
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"clustername"
argument_list|,
name|conf
operator|.
name|getClusterName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"INDEX"
argument_list|,
name|conf
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|conf
operator|.
name|isLocal
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|conf
operator|.
name|isData
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|clusterConfWithIpAddress ()
specifier|public
name|void
name|clusterConfWithIpAddress
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"elasticsearch://clustername?operation=INDEX&indexName=twitter&indexType=tweet&ip=127.0.0.1"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|ElasticsearchConfiguration
name|conf
init|=
operator|new
name|ElasticsearchConfiguration
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"clustername"
argument_list|,
name|conf
operator|.
name|getClusterName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"INDEX"
argument_list|,
name|conf
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|conf
operator|.
name|isLocal
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|conf
operator|.
name|isData
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"127.0.0.1"
argument_list|,
name|conf
operator|.
name|getIp
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9300
argument_list|,
name|conf
operator|.
name|getPort
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|localDataNode ()
specifier|public
name|void
name|localDataNode
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"elasticsearch://local?operation=INDEX&indexName=twitter&indexType=tweet&data=true"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|ElasticsearchConfiguration
name|conf
init|=
operator|new
name|ElasticsearchConfiguration
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|conf
operator|.
name|isLocal
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"INDEX"
argument_list|,
name|conf
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"twitter"
argument_list|,
name|conf
operator|.
name|getIndexName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tweet"
argument_list|,
name|conf
operator|.
name|getIndexType
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|conf
operator|.
name|isData
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|conf
operator|.
name|getClusterName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|writeConsistencyLevelDefaultConfTest ()
specifier|public
name|void
name|writeConsistencyLevelDefaultConfTest
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"elasticsearch://local?operation=INDEX&indexName=twitter&indexType=tweet"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|ElasticsearchConfiguration
name|conf
init|=
operator|new
name|ElasticsearchConfiguration
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|conf
operator|.
name|isLocal
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"INDEX"
argument_list|,
name|conf
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"twitter"
argument_list|,
name|conf
operator|.
name|getIndexName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tweet"
argument_list|,
name|conf
operator|.
name|getIndexType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|WriteConsistencyLevel
operator|.
name|DEFAULT
argument_list|,
name|conf
operator|.
name|getConsistencyLevel
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|conf
operator|.
name|getClusterName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|writeConsistencyLevelConfTest ()
specifier|public
name|void
name|writeConsistencyLevelConfTest
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"elasticsearch://local?operation=INDEX&indexName=twitter&indexType=tweet&consistencyLevel=QUORUM"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|ElasticsearchConfiguration
name|conf
init|=
operator|new
name|ElasticsearchConfiguration
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|conf
operator|.
name|isLocal
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"INDEX"
argument_list|,
name|conf
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"twitter"
argument_list|,
name|conf
operator|.
name|getIndexName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tweet"
argument_list|,
name|conf
operator|.
name|getIndexType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|WriteConsistencyLevel
operator|.
name|QUORUM
argument_list|,
name|conf
operator|.
name|getConsistencyLevel
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|conf
operator|.
name|getClusterName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replicationTypeConfTest ()
specifier|public
name|void
name|replicationTypeConfTest
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"elasticsearch://local?operation=INDEX&indexName=twitter&indexType=tweet&replicationType=ASYNC"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|ElasticsearchConfiguration
name|conf
init|=
operator|new
name|ElasticsearchConfiguration
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|assertDefaultConfigurationParameters
argument_list|(
name|conf
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ReplicationType
operator|.
name|ASYNC
argument_list|,
name|conf
operator|.
name|getReplicationType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replicationTypeDefaultConfTest ()
specifier|public
name|void
name|replicationTypeDefaultConfTest
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"elasticsearch://local?operation=INDEX&indexName=twitter&indexType=tweet"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|ElasticsearchConfiguration
name|conf
init|=
operator|new
name|ElasticsearchConfiguration
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|assertDefaultConfigurationParameters
argument_list|(
name|conf
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ReplicationType
operator|.
name|DEFAULT
argument_list|,
name|conf
operator|.
name|getReplicationType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|transportAddressesSimpleHostnameTest ()
specifier|public
name|void
name|transportAddressesSimpleHostnameTest
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"elasticsearch://local?operation=INDEX&indexName=twitter&"
operator|+
literal|"indexType=tweet&transportAddresses=127.0.0.1"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|ElasticsearchConfiguration
name|conf
init|=
operator|new
name|ElasticsearchConfiguration
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|assertDefaultConfigurationParameters
argument_list|(
name|conf
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"127.0.0.1"
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|address
argument_list|()
operator|.
name|getHostString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9300
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|address
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|transportAddressesMultipleHostnameTest ()
specifier|public
name|void
name|transportAddressesMultipleHostnameTest
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"elasticsearch://local?operation=INDEX&indexName=twitter&"
operator|+
literal|"indexType=tweet&transportAddresses=127.0.0.1,127.0.0.2"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|ElasticsearchConfiguration
name|conf
init|=
operator|new
name|ElasticsearchConfiguration
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|assertDefaultConfigurationParameters
argument_list|(
name|conf
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"127.0.0.1"
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|address
argument_list|()
operator|.
name|getHostString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9300
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|address
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"127.0.0.2"
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|address
argument_list|()
operator|.
name|getHostString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9300
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|address
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|transportAddressesSimpleHostnameAndPortTest ()
specifier|public
name|void
name|transportAddressesSimpleHostnameAndPortTest
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"elasticsearch://local?operation=INDEX&indexName=twitter&"
operator|+
literal|"indexType=tweet&transportAddresses=127.0.0.1:9305"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|ElasticsearchConfiguration
name|conf
init|=
operator|new
name|ElasticsearchConfiguration
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|assertDefaultConfigurationParameters
argument_list|(
name|conf
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"127.0.0.1"
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|address
argument_list|()
operator|.
name|getHostString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9305
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|address
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|transportAddressesMultipleHostnameAndPortTest ()
specifier|public
name|void
name|transportAddressesMultipleHostnameAndPortTest
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"elasticsearch://local?operation=INDEX&indexName=twitter&"
operator|+
literal|"indexType=tweet&transportAddresses=127.0.0.1:9400,127.0.0.2,127.0.0.3:9401"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
name|URISupport
operator|.
name|parseParameters
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|ElasticsearchConfiguration
name|conf
init|=
operator|new
name|ElasticsearchConfiguration
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|assertDefaultConfigurationParameters
argument_list|(
name|conf
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"127.0.0.1"
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|address
argument_list|()
operator|.
name|getHostString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9400
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|address
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"127.0.0.2"
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|address
argument_list|()
operator|.
name|getHostString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9300
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|address
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"127.0.0.3"
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|address
argument_list|()
operator|.
name|getHostString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9401
argument_list|,
name|conf
operator|.
name|getTransportAddresses
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|address
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertDefaultConfigurationParameters (ElasticsearchConfiguration conf)
specifier|private
name|void
name|assertDefaultConfigurationParameters
parameter_list|(
name|ElasticsearchConfiguration
name|conf
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|conf
operator|.
name|isLocal
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"INDEX"
argument_list|,
name|conf
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"twitter"
argument_list|,
name|conf
operator|.
name|getIndexName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tweet"
argument_list|,
name|conf
operator|.
name|getIndexType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|conf
operator|.
name|getClusterName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

