begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.testcontainers
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|testcontainers
package|;
end_package

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
name|TimeUnit
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
name|ObjectHelper
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

begin_import
import|import
name|org
operator|.
name|testcontainers
operator|.
name|containers
operator|.
name|GenericContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testcontainers
operator|.
name|containers
operator|.
name|Network
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testcontainers
operator|.
name|containers
operator|.
name|output
operator|.
name|Slf4jLogConsumer
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|joining
import|;
end_import

begin_class
DECL|class|Containers
specifier|public
specifier|final
class|class
name|Containers
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
name|Containers
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|Containers ()
specifier|private
name|Containers
parameter_list|()
block|{     }
DECL|method|start (List<GenericContainer<?>> containers, Network network, long timeout)
specifier|public
specifier|static
name|void
name|start
parameter_list|(
name|List
argument_list|<
name|GenericContainer
argument_list|<
name|?
argument_list|>
argument_list|>
name|containers
parameter_list|,
name|Network
name|network
parameter_list|,
name|long
name|timeout
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|containers
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|GenericContainer
argument_list|<
name|?
argument_list|>
name|container
range|:
name|containers
control|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|container
operator|.
name|getNetworkAliases
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Container should have at least a network alias"
argument_list|)
throw|;
block|}
if|if
condition|(
name|network
operator|!=
literal|null
condition|)
block|{
name|container
operator|.
name|withNetwork
argument_list|(
name|network
argument_list|)
expr_stmt|;
block|}
comment|// Add custom logger
name|container
operator|.
name|withLogConsumer
argument_list|(
operator|new
name|Slf4jLogConsumer
argument_list|(
name|LOGGER
argument_list|)
operator|.
name|withPrefix
argument_list|(
name|container
operator|.
name|getNetworkAliases
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|","
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
block|{
name|container
operator|.
name|start
argument_list|()
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
name|latch
operator|.
name|await
argument_list|(
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
DECL|method|stop (List<GenericContainer<?>> containers, long timeout)
specifier|public
specifier|static
name|void
name|stop
parameter_list|(
name|List
argument_list|<
name|GenericContainer
argument_list|<
name|?
argument_list|>
argument_list|>
name|containers
parameter_list|,
name|long
name|timeout
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|containers
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|GenericContainer
argument_list|<
name|?
argument_list|>
name|container
range|:
name|containers
control|)
block|{
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
block|{
name|container
operator|.
name|stop
argument_list|()
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
name|latch
operator|.
name|await
argument_list|(
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
DECL|method|lookup (List<GenericContainer<?>> containers, String containerName)
specifier|public
specifier|static
name|GenericContainer
argument_list|<
name|?
argument_list|>
name|lookup
parameter_list|(
name|List
argument_list|<
name|GenericContainer
argument_list|<
name|?
argument_list|>
argument_list|>
name|containers
parameter_list|,
name|String
name|containerName
parameter_list|)
block|{
for|for
control|(
name|GenericContainer
argument_list|<
name|?
argument_list|>
name|container
range|:
name|containers
control|)
block|{
if|if
condition|(
name|container
operator|.
name|getNetworkAliases
argument_list|()
operator|.
name|contains
argument_list|(
name|containerName
argument_list|)
condition|)
block|{
return|return
name|container
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No container with name "
operator|+
name|containerName
operator|+
literal|" found"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

