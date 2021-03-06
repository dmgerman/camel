begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.couchbase
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchbase
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CouchbaseComponentTest
specifier|public
class|class
name|CouchbaseComponentTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|component
specifier|private
name|CouchbaseComponent
name|component
decl_stmt|;
annotation|@
name|Override
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
name|component
operator|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"couchbase"
argument_list|,
name|CouchbaseComponent
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testEndpointCreated ()
specifier|public
name|void
name|testEndpointCreated
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|uri
init|=
literal|"couchbase:http://localhost:9191/bucket"
decl_stmt|;
name|String
name|remaining
init|=
literal|"http://localhost:9191/bucket"
decl_stmt|;
name|CouchbaseEndpoint
name|endpoint
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesSet ()
specifier|public
name|void
name|testPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"username"
argument_list|,
literal|"ugol"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
literal|"pwd"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"additionalHosts"
argument_list|,
literal|"127.0.0.1,example.com,another-host"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"persistTo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"replicateTo"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
literal|"couchdb:http://localhost:91234/bucket"
decl_stmt|;
name|String
name|remaining
init|=
literal|"http://localhost:91234/bucket"
decl_stmt|;
name|CouchbaseEndpoint
name|endpoint
init|=
name|component
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http"
argument_list|,
name|endpoint
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost"
argument_list|,
name|endpoint
operator|.
name|getHostname
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bucket"
argument_list|,
name|endpoint
operator|.
name|getBucket
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|91234
argument_list|,
name|endpoint
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ugol"
argument_list|,
name|endpoint
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pwd"
argument_list|,
name|endpoint
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"127.0.0.1,example.com,another-host"
argument_list|,
name|endpoint
operator|.
name|getAdditionalHosts
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|endpoint
operator|.
name|getPersistTo
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|endpoint
operator|.
name|getReplicateTo
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCouchbaseURI ()
specifier|public
name|void
name|testCouchbaseURI
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|uri
init|=
literal|"couchbase:http://localhost/bucket?param=true"
decl_stmt|;
name|String
name|remaining
init|=
literal|"http://localhost/bucket?param=true"
decl_stmt|;
name|CouchbaseEndpoint
name|endpoint
init|=
operator|new
name|CouchbaseComponent
argument_list|(
name|context
argument_list|)
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://localhost:8091/pools"
argument_list|)
argument_list|,
name|endpoint
operator|.
name|makeBootstrapURI
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCouchbaseAdditionalHosts ()
specifier|public
name|void
name|testCouchbaseAdditionalHosts
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"additionalHosts"
argument_list|,
literal|"127.0.0.1,example.com,another-host"
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
literal|"couchbase:http://localhost/bucket?param=true"
decl_stmt|;
name|String
name|remaining
init|=
literal|"http://localhost/bucket?param=true"
decl_stmt|;
name|CouchbaseEndpoint
name|endpoint
init|=
operator|new
name|CouchbaseComponent
argument_list|(
name|context
argument_list|)
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|URI
index|[]
name|endpointArray
init|=
name|endpoint
operator|.
name|makeBootstrapURI
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://localhost:8091/pools"
argument_list|)
argument_list|,
name|endpointArray
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://127.0.0.1:8091/pools"
argument_list|)
argument_list|,
name|endpointArray
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://example.com:8091/pools"
argument_list|)
argument_list|,
name|endpointArray
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://another-host:8091/pools"
argument_list|)
argument_list|,
name|endpointArray
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|endpointArray
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCouchbaseAdditionalHostsWithSpaces ()
specifier|public
name|void
name|testCouchbaseAdditionalHostsWithSpaces
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"additionalHosts"
argument_list|,
literal|" 127.0.0.1, example.com, another-host "
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
literal|"couchbase:http://localhost/bucket?param=true"
decl_stmt|;
name|String
name|remaining
init|=
literal|"http://localhost/bucket?param=true"
decl_stmt|;
name|CouchbaseEndpoint
name|endpoint
init|=
operator|new
name|CouchbaseComponent
argument_list|(
name|context
argument_list|)
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|URI
index|[]
name|endpointArray
init|=
name|endpoint
operator|.
name|makeBootstrapURI
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://localhost:8091/pools"
argument_list|)
argument_list|,
name|endpointArray
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://127.0.0.1:8091/pools"
argument_list|)
argument_list|,
name|endpointArray
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://example.com:8091/pools"
argument_list|)
argument_list|,
name|endpointArray
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://another-host:8091/pools"
argument_list|)
argument_list|,
name|endpointArray
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|endpointArray
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCouchbaseDuplicateAdditionalHosts ()
specifier|public
name|void
name|testCouchbaseDuplicateAdditionalHosts
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"additionalHosts"
argument_list|,
literal|"127.0.0.1,localhost, localhost"
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
literal|"couchbase:http://localhost/bucket?param=true"
decl_stmt|;
name|String
name|remaining
init|=
literal|"http://localhost/bucket?param=true"
decl_stmt|;
name|CouchbaseEndpoint
name|endpoint
init|=
operator|new
name|CouchbaseComponent
argument_list|(
name|context
argument_list|)
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|URI
index|[]
name|endpointArray
init|=
name|endpoint
operator|.
name|makeBootstrapURI
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|endpointArray
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://localhost:8091/pools"
argument_list|)
argument_list|,
name|endpointArray
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://127.0.0.1:8091/pools"
argument_list|)
argument_list|,
name|endpointArray
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCouchbaseNullAdditionalHosts ()
specifier|public
name|void
name|testCouchbaseNullAdditionalHosts
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"additionalHosts"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
literal|"couchbase:http://localhost/bucket?param=true"
decl_stmt|;
name|String
name|remaining
init|=
literal|"http://localhost/bucket?param=true"
decl_stmt|;
name|CouchbaseEndpoint
name|endpoint
init|=
operator|new
name|CouchbaseComponent
argument_list|(
name|context
argument_list|)
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|URI
index|[]
name|endpointArray
init|=
name|endpoint
operator|.
name|makeBootstrapURI
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|endpointArray
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

