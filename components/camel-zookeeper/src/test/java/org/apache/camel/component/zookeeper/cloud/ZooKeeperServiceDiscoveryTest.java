begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ArrayList
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
name|ZooKeeperCuratorConfiguration
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
name|ZooKeeperCuratorHelper
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
name|ServiceInstance
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
DECL|class|ZooKeeperServiceDiscoveryTest
specifier|public
class|class
name|ZooKeeperServiceDiscoveryTest
block|{
annotation|@
name|Test
DECL|method|testServiceDiscovery ()
specifier|public
name|void
name|testServiceDiscovery
parameter_list|()
throws|throws
name|Exception
block|{
name|ZooKeeperCuratorConfiguration
name|configuration
init|=
operator|new
name|ZooKeeperCuratorConfiguration
argument_list|()
decl_stmt|;
name|ServiceDiscovery
argument_list|<
name|ZooKeeperServiceDiscovery
operator|.
name|MetaData
argument_list|>
name|zkDiscovery
init|=
literal|null
decl_stmt|;
name|ZooKeeperTestSupport
operator|.
name|TestZookeeperServer
name|server
init|=
literal|null
decl_stmt|;
name|int
name|port
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
try|try
block|{
name|server
operator|=
operator|new
name|ZooKeeperTestSupport
operator|.
name|TestZookeeperServer
argument_list|(
name|port
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|ZooKeeperTestSupport
operator|.
name|waitForServerUp
argument_list|(
literal|"localhost:"
operator|+
name|port
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setBasePath
argument_list|(
literal|"/camel"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setCuratorFramework
argument_list|(
name|CuratorFrameworkFactory
operator|.
name|builder
argument_list|()
operator|.
name|connectString
argument_list|(
literal|"localhost:"
operator|+
name|port
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
argument_list|)
expr_stmt|;
name|zkDiscovery
operator|=
name|ZooKeeperCuratorHelper
operator|.
name|createServiceDiscovery
argument_list|(
name|configuration
argument_list|,
name|configuration
operator|.
name|getCuratorFramework
argument_list|()
argument_list|,
name|ZooKeeperServiceDiscovery
operator|.
name|MetaData
operator|.
name|class
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getCuratorFramework
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
name|zkDiscovery
operator|.
name|start
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ServiceInstance
argument_list|<
name|ZooKeeperServiceDiscovery
operator|.
name|MetaData
argument_list|>
argument_list|>
name|instances
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|ServiceInstance
argument_list|<
name|ZooKeeperServiceDiscovery
operator|.
name|MetaData
argument_list|>
name|instance
init|=
name|ServiceInstance
operator|.
expr|<
name|ZooKeeperServiceDiscovery
operator|.
name|MetaData
operator|>
name|builder
argument_list|()
operator|.
name|address
argument_list|(
literal|"127.0.0.1"
argument_list|)
operator|.
name|port
argument_list|(
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
argument_list|)
operator|.
name|name
argument_list|(
literal|"my-service"
argument_list|)
operator|.
name|id
argument_list|(
literal|"service-"
operator|+
name|i
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|zkDiscovery
operator|.
name|registerService
argument_list|(
name|instance
argument_list|)
expr_stmt|;
name|instances
operator|.
name|add
argument_list|(
name|instance
argument_list|)
expr_stmt|;
block|}
name|ZooKeeperServiceDiscovery
name|discovery
init|=
operator|new
name|ZooKeeperServiceDiscovery
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
name|discovery
operator|.
name|start
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|services
init|=
name|discovery
operator|.
name|getServices
argument_list|(
literal|"my-service"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|services
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|services
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ServiceDefinition
name|service
range|:
name|services
control|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|instances
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|i
lambda|->
block|{
return|return
name|i
operator|.
name|getPort
argument_list|()
operator|==
name|service
operator|.
name|getPort
argument_list|()
operator|&&
name|i
operator|.
name|getAddress
argument_list|()
operator|.
name|equals
argument_list|(
name|service
operator|.
name|getHost
argument_list|()
argument_list|)
operator|&&
name|i
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|service
operator|.
name|getMetadata
argument_list|()
operator|.
name|get
argument_list|(
name|ServiceDefinition
operator|.
name|SERVICE_META_ID
argument_list|)
argument_list|)
operator|&&
name|i
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|service
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
argument_list|)
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|CloseableUtils
operator|.
name|closeQuietly
argument_list|(
name|zkDiscovery
argument_list|)
expr_stmt|;
name|CloseableUtils
operator|.
name|closeQuietly
argument_list|(
name|configuration
operator|.
name|getCuratorFramework
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

