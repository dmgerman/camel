begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jgroups.cluster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jgroups
operator|.
name|cluster
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
name|ThreadLocalRandom
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|IntStream
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
name|impl
operator|.
name|DefaultCamelContext
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
DECL|class|JGroupsLockMasterTest
specifier|public
class|class
name|JGroupsLockMasterTest
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
name|JGroupsLockMasterTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|CLIENTS
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|CLIENTS
init|=
name|IntStream
operator|.
name|range
argument_list|(
literal|0
argument_list|,
literal|3
argument_list|)
operator|.
name|mapToObj
argument_list|(
name|Integer
operator|::
name|toString
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|RESULTS
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|RESULTS
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|SCHEDULER
specifier|private
specifier|static
specifier|final
name|ScheduledExecutorService
name|SCHEDULER
init|=
name|Executors
operator|.
name|newScheduledThreadPool
argument_list|(
name|CLIENTS
operator|.
name|size
argument_list|()
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
name|CLIENTS
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
comment|// ************************************
comment|// Test
comment|// ************************************
annotation|@
name|Test
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|String
name|id
range|:
name|CLIENTS
control|)
block|{
name|SCHEDULER
operator|.
name|submit
argument_list|(
parameter_list|()
lambda|->
name|run
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|LATCH
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
expr_stmt|;
name|SCHEDULER
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|CLIENTS
operator|.
name|size
argument_list|()
argument_list|,
name|RESULTS
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|RESULTS
operator|.
name|containsAll
argument_list|(
name|CLIENTS
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// ************************************
comment|// Run a Camel node
comment|// ************************************
DECL|method|run (String id)
specifier|private
specifier|static
name|void
name|run
parameter_list|(
name|String
name|id
parameter_list|)
block|{
try|try
block|{
name|int
name|events
init|=
name|ThreadLocalRandom
operator|.
name|current
argument_list|()
operator|.
name|nextInt
argument_list|(
literal|2
argument_list|,
literal|6
argument_list|)
decl_stmt|;
name|CountDownLatch
name|contextLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|events
argument_list|)
decl_stmt|;
name|JGroupsLockClusterService
name|service
init|=
operator|new
name|JGroupsLockClusterService
argument_list|()
decl_stmt|;
name|service
operator|.
name|setId
argument_list|(
literal|"node-"
operator|+
name|id
argument_list|)
expr_stmt|;
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
name|context
operator|.
name|setName
argument_list|(
literal|"context-"
operator|+
name|id
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|service
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
name|from
argument_list|(
literal|"master:jgl:timer:master?delay=1s&period=1s"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route-"
operator|+
name|id
argument_list|)
operator|.
name|log
argument_list|(
literal|"From ${routeId}"
argument_list|)
operator|.
name|process
argument_list|(
name|e
lambda|->
name|contextLatch
operator|.
name|countDown
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// Start the context after some random time so the startup order
comment|// changes for each test.
name|Thread
operator|.
name|sleep
argument_list|(
name|ThreadLocalRandom
operator|.
name|current
argument_list|()
operator|.
name|nextInt
argument_list|(
literal|500
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|contextLatch
operator|.
name|await
argument_list|()
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Shutting down node {}"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|RESULTS
operator|.
name|add
argument_list|(
name|id
argument_list|)
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
name|LOGGER
operator|.
name|warn
argument_list|(
literal|""
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

