begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
operator|.
name|cloud
package|;
end_package

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
name|Collections
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
name|UUID
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
name|cloud
operator|.
name|ServiceDefinition
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
name|component
operator|.
name|zookeeper
operator|.
name|ZooKeeperTestSupport
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
name|curator
operator|.
name|framework
operator|.
name|CuratorFramework
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|curator
operator|.
name|framework
operator|.
name|CuratorFrameworkFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|curator
operator|.
name|retry
operator|.
name|ExponentialBackoffRetry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|curator
operator|.
name|utils
operator|.
name|CloseableUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|curator
operator|.
name|x
operator|.
name|discovery
operator|.
name|ServiceDiscovery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|curator
operator|.
name|x
operator|.
name|discovery
operator|.
name|ServiceDiscoveryBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|curator
operator|.
name|x
operator|.
name|discovery
operator|.
name|ServiceInstance
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|curator
operator|.
name|x
operator|.
name|discovery
operator|.
name|details
operator|.
name|JsonInstanceSerializer
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
DECL|class|ZooKeeperServiceRegistrationTestBase
specifier|public
specifier|abstract
class|class
name|ZooKeeperServiceRegistrationTestBase
extends|extends
name|CamelTestSupport
block|{
DECL|field|SERVICE_ID
specifier|protected
specifier|final
specifier|static
name|String
name|SERVICE_ID
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
DECL|field|SERVICE_NAME
specifier|protected
specifier|final
specifier|static
name|String
name|SERVICE_NAME
init|=
literal|"my-service"
decl_stmt|;
DECL|field|SERVICE_HOST
specifier|protected
specifier|final
specifier|static
name|String
name|SERVICE_HOST
init|=
literal|"localhost"
decl_stmt|;
DECL|field|SERVICE_PATH
specifier|protected
specifier|static
specifier|final
name|String
name|SERVICE_PATH
init|=
literal|"/camel"
decl_stmt|;
DECL|field|SERVICE_PORT
specifier|protected
specifier|final
specifier|static
name|int
name|SERVICE_PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|SERVER_PORT
specifier|protected
specifier|final
specifier|static
name|int
name|SERVER_PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|server
specifier|protected
name|ZooKeeperTestSupport
operator|.
name|TestZookeeperServer
name|server
decl_stmt|;
DECL|field|curator
specifier|protected
name|CuratorFramework
name|curator
decl_stmt|;
DECL|field|discovery
specifier|protected
name|ServiceDiscovery
argument_list|<
name|ZooKeeperServiceRegistry
operator|.
name|MetaData
argument_list|>
name|discovery
decl_stmt|;
comment|// ***********************
comment|// Lifecycle
comment|// ***********************
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|protected
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doPreSetup
argument_list|()
expr_stmt|;
name|server
operator|=
operator|new
name|ZooKeeperTestSupport
operator|.
name|TestZookeeperServer
argument_list|(
name|SERVER_PORT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|ZooKeeperTestSupport
operator|.
name|waitForServerUp
argument_list|(
literal|"127.0.0.1:"
operator|+
name|SERVER_PORT
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|curator
operator|=
name|CuratorFrameworkFactory
operator|.
name|builder
argument_list|()
operator|.
name|connectString
argument_list|(
literal|"127.0.0.1:"
operator|+
name|SERVER_PORT
argument_list|)
operator|.
name|retryPolicy
argument_list|(
operator|new
name|ExponentialBackoffRetry
argument_list|(
literal|1000
argument_list|,
literal|3
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|discovery
operator|=
name|ServiceDiscoveryBuilder
operator|.
name|builder
argument_list|(
name|ZooKeeperServiceRegistry
operator|.
name|MetaData
operator|.
name|class
argument_list|)
operator|.
name|client
argument_list|(
name|curator
argument_list|)
operator|.
name|basePath
argument_list|(
name|SERVICE_PATH
argument_list|)
operator|.
name|serializer
argument_list|(
operator|new
name|JsonInstanceSerializer
argument_list|<>
argument_list|(
name|ZooKeeperServiceRegistry
operator|.
name|MetaData
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|curator
operator|.
name|start
argument_list|()
expr_stmt|;
name|discovery
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|CloseableUtils
operator|.
name|closeQuietly
argument_list|(
name|discovery
argument_list|)
expr_stmt|;
name|CloseableUtils
operator|.
name|closeQuietly
argument_list|(
name|curator
argument_list|)
expr_stmt|;
name|server
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
DECL|method|getMetadata ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getMetadata
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
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
specifier|final
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|ZooKeeperServiceRegistry
name|registry
init|=
operator|new
name|ZooKeeperServiceRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|setId
argument_list|(
name|context
operator|.
name|getUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|setNodes
argument_list|(
literal|"localhost:"
operator|+
name|SERVER_PORT
argument_list|)
expr_stmt|;
name|registry
operator|.
name|setBasePath
argument_list|(
name|SERVICE_PATH
argument_list|)
expr_stmt|;
name|registry
operator|.
name|setServiceHost
argument_list|(
name|SERVICE_HOST
argument_list|)
expr_stmt|;
name|registry
operator|.
name|setOverrideServiceHost
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|registry
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testRegistrationFromRoute ()
specifier|public
name|void
name|testRegistrationFromRoute
parameter_list|()
throws|throws
name|Exception
block|{
comment|// the service should not be registered as the route is not running
name|assertTrue
argument_list|(
name|discovery
operator|.
name|queryForInstances
argument_list|(
name|SERVICE_NAME
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// let start the route
name|context
argument_list|()
operator|.
name|startRoute
argument_list|(
name|SERVICE_ID
argument_list|)
expr_stmt|;
comment|// check that service has been registered
name|Collection
argument_list|<
name|ServiceInstance
argument_list|<
name|ZooKeeperServiceRegistry
operator|.
name|MetaData
argument_list|>
argument_list|>
name|services
init|=
name|discovery
operator|.
name|queryForInstances
argument_list|(
name|SERVICE_NAME
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|services
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ServiceInstance
argument_list|<
name|ZooKeeperServiceRegistry
operator|.
name|MetaData
argument_list|>
name|instance
init|=
name|services
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|SERVICE_PORT
argument_list|,
operator|(
name|int
operator|)
name|instance
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost"
argument_list|,
name|instance
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http"
argument_list|,
name|instance
operator|.
name|getPayload
argument_list|()
operator|.
name|get
argument_list|(
name|ServiceDefinition
operator|.
name|SERVICE_META_PROTOCOL
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/service/endpoint"
argument_list|,
name|instance
operator|.
name|getPayload
argument_list|()
operator|.
name|get
argument_list|(
name|ServiceDefinition
operator|.
name|SERVICE_META_PATH
argument_list|)
argument_list|)
expr_stmt|;
name|getMetadata
argument_list|()
operator|.
name|forEach
argument_list|(
parameter_list|(
name|k
parameter_list|,
name|v
parameter_list|)
lambda|->
block|{
name|assertEquals
argument_list|(
name|v
argument_list|,
name|instance
operator|.
name|getPayload
argument_list|()
operator|.
name|get
argument_list|(
name|k
argument_list|)
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
comment|// let stop the route
name|context
argument_list|()
operator|.
name|stopRoute
argument_list|(
name|SERVICE_ID
argument_list|)
expr_stmt|;
comment|// the service should be removed once the route is stopped
name|assertTrue
argument_list|(
name|discovery
operator|.
name|queryForInstances
argument_list|(
name|SERVICE_NAME
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

