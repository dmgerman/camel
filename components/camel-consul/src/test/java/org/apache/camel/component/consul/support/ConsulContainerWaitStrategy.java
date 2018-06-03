begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
operator|.
name|support
package|;
end_package

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
name|concurrent
operator|.
name|TimeoutException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|dockerjava
operator|.
name|api
operator|.
name|DockerClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testcontainers
operator|.
name|DockerClientFactory
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
name|ContainerLaunchException
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
name|WaitingConsumer
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
name|wait
operator|.
name|strategy
operator|.
name|AbstractWaitStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testcontainers
operator|.
name|utility
operator|.
name|LogUtils
import|;
end_import

begin_class
DECL|class|ConsulContainerWaitStrategy
specifier|public
specifier|final
class|class
name|ConsulContainerWaitStrategy
extends|extends
name|AbstractWaitStrategy
block|{
annotation|@
name|Override
DECL|method|waitUntilReady ()
specifier|protected
name|void
name|waitUntilReady
parameter_list|()
block|{
specifier|final
name|DockerClient
name|client
init|=
name|DockerClientFactory
operator|.
name|instance
argument_list|()
operator|.
name|client
argument_list|()
decl_stmt|;
specifier|final
name|WaitingConsumer
name|waitingConsumer
init|=
operator|new
name|WaitingConsumer
argument_list|()
decl_stmt|;
name|LogUtils
operator|.
name|followOutput
argument_list|(
name|client
argument_list|,
name|waitStrategyTarget
operator|.
name|getContainerId
argument_list|()
argument_list|,
name|waitingConsumer
argument_list|)
expr_stmt|;
try|try
block|{
name|waitingConsumer
operator|.
name|waitUntil
argument_list|(
name|f
lambda|->
name|f
operator|.
name|getUtf8String
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Synced node info"
argument_list|)
argument_list|,
name|startupTimeout
operator|.
name|getSeconds
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ContainerLaunchException
argument_list|(
literal|"Timed out"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

