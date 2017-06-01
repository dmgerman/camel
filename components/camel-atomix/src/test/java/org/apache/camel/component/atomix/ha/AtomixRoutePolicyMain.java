begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.ha
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|ha
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
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
name|Executors
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
name|ScheduledExecutorService
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
name|TimeUnit
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|Atomix
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|AtomixReplica
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|catalyst
operator|.
name|transport
operator|.
name|Address
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|catalyst
operator|.
name|transport
operator|.
name|netty
operator|.
name|NettyTransport
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|copycat
operator|.
name|server
operator|.
name|storage
operator|.
name|Storage
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|copycat
operator|.
name|server
operator|.
name|storage
operator|.
name|StorageLevel
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
name|builder
operator|.
name|RouteBuilder
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
name|ha
operator|.
name|CamelCluster
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
name|ha
operator|.
name|CamelClusterView
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
name|DefaultCamelContext
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
name|ha
operator|.
name|ClusteredRoutePolicy
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
name|spi
operator|.
name|RoutePolicy
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
name|util
operator|.
name|FileUtil
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

begin_class
DECL|class|AtomixRoutePolicyMain
specifier|public
specifier|final
class|class
name|AtomixRoutePolicyMain
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AtomixRoutePolicyMain
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|ADDRESSES
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|Address
argument_list|>
name|ADDRESSES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Address
argument_list|(
literal|"127.0.0.1"
argument_list|,
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
argument_list|)
argument_list|,
operator|new
name|Address
argument_list|(
literal|"127.0.0.1"
argument_list|,
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
argument_list|)
argument_list|,
operator|new
name|Address
argument_list|(
literal|"127.0.0.1"
argument_list|,
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|LATCH
specifier|private
specifier|static
specifier|final
name|CountDownLatch
name|LATCH
init|=
operator|new
name|CountDownLatch
argument_list|(
name|ADDRESSES
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|EXECUTOR
specifier|private
specifier|static
specifier|final
name|ScheduledExecutorService
name|EXECUTOR
init|=
name|Executors
operator|.
name|newScheduledThreadPool
argument_list|(
name|ADDRESSES
operator|.
name|size
argument_list|()
operator|*
literal|2
argument_list|)
decl_stmt|;
DECL|method|main (final String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
specifier|final
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Address
name|address
range|:
name|ADDRESSES
control|)
block|{
name|EXECUTOR
operator|.
name|submit
argument_list|(
parameter_list|()
lambda|->
name|run
argument_list|(
name|address
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|LATCH
operator|.
name|await
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|run (Address address)
specifier|static
name|void
name|run
parameter_list|(
name|Address
name|address
parameter_list|)
block|{
try|try
block|{
specifier|final
name|String
name|id
init|=
name|String
operator|.
name|format
argument_list|(
literal|"atomix-%d"
argument_list|,
name|address
operator|.
name|port
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|File
name|path
init|=
operator|new
name|File
argument_list|(
literal|"target"
argument_list|,
name|id
argument_list|)
decl_stmt|;
comment|// Cleanup
name|FileUtil
operator|.
name|removeDir
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|Atomix
name|atomix
init|=
name|AtomixReplica
operator|.
name|builder
argument_list|(
name|address
argument_list|)
operator|.
name|withTransport
argument_list|(
operator|new
name|NettyTransport
argument_list|()
argument_list|)
operator|.
name|withStorage
argument_list|(
name|Storage
operator|.
name|builder
argument_list|()
operator|.
name|withDirectory
argument_list|(
name|path
argument_list|)
operator|.
name|withStorageLevel
argument_list|(
name|StorageLevel
operator|.
name|MEMORY
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|bootstrap
argument_list|(
name|ADDRESSES
argument_list|)
operator|.
name|join
argument_list|()
decl_stmt|;
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|CamelCluster
name|cluster
init|=
operator|new
name|AtomixCluster
argument_list|(
name|atomix
argument_list|)
decl_stmt|;
name|CamelClusterView
name|view
init|=
name|cluster
operator|.
name|createView
argument_list|(
literal|"my-view"
argument_list|)
decl_stmt|;
name|view
operator|.
name|addEventListener
argument_list|(
parameter_list|(
name|e
parameter_list|,
name|p
parameter_list|)
lambda|->
block|{
if|if
condition|(
name|view
operator|.
name|getLocalMember
argument_list|()
operator|.
name|isMaster
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Member {} ({}), is now master"
argument_list|,
name|view
operator|.
name|getLocalMember
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|address
argument_list|)
expr_stmt|;
comment|// Shutdown the context later on so the next one should take
comment|// the leadership
name|EXECUTOR
operator|.
name|schedule
argument_list|(
name|latch
operator|::
name|countDown
argument_list|,
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|cluster
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|RoutePolicy
name|policy
init|=
name|ClusteredRoutePolicy
operator|.
name|forView
argument_list|(
name|view
argument_list|)
decl_stmt|;
name|fromF
argument_list|(
literal|"timer:%s-1?period=2s"
argument_list|,
name|id
argument_list|)
operator|.
name|routeId
argument_list|(
name|id
operator|+
literal|"-1"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"ClusterMaster"
argument_list|)
operator|.
name|body
argument_list|(
name|b
lambda|->
name|view
operator|.
name|getMaster
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
literal|"${routeId} (1) - master is: ${header.ClusterMaster}"
argument_list|)
expr_stmt|;
name|fromF
argument_list|(
literal|"timer:%s-2?period=5s"
argument_list|,
name|id
argument_list|)
operator|.
name|routeId
argument_list|(
name|id
operator|+
literal|"-2"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"ClusterMaster"
argument_list|)
operator|.
name|body
argument_list|(
name|b
lambda|->
name|view
operator|.
name|getMaster
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
literal|"${routeId} (2) - master is: ${header.ClusterMaster}"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|LATCH
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Done {}"
argument_list|,
name|address
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

