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
name|Collections
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
name|CopyOnWriteArrayList
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
name|component
operator|.
name|properties
operator|.
name|PropertiesComponent
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
name|Assume
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
name|utility
operator|.
name|DockerMachineClient
import|;
end_import

begin_class
DECL|class|ContainerAwareTestSupport
specifier|public
class|class
name|ContainerAwareTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|containers
specifier|private
name|List
argument_list|<
name|GenericContainer
argument_list|<
name|?
argument_list|>
argument_list|>
name|containers
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// ******************
comment|// Setup
comment|// ******************
annotation|@
name|Override
DECL|method|setupResources ()
specifier|protected
name|void
name|setupResources
parameter_list|()
throws|throws
name|Exception
block|{
name|Assume
operator|.
name|assumeTrue
argument_list|(
literal|"Skipping test because docker not installed"
argument_list|,
name|DockerMachineClient
operator|.
name|instance
argument_list|()
operator|.
name|isInstalled
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|setupResources
argument_list|()
expr_stmt|;
name|containers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|containers
operator|.
name|addAll
argument_list|(
name|createContainers
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Network
name|network
init|=
name|containerNetwork
argument_list|()
decl_stmt|;
specifier|final
name|long
name|timeout
init|=
name|containersStartupTimeout
argument_list|()
decl_stmt|;
name|Containers
operator|.
name|start
argument_list|(
name|containers
argument_list|,
name|network
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|cleanupResources ()
specifier|protected
name|void
name|cleanupResources
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|cleanupResources
argument_list|()
expr_stmt|;
name|Containers
operator|.
name|stop
argument_list|(
name|containers
argument_list|,
name|containerShutdownTimeout
argument_list|()
argument_list|)
expr_stmt|;
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
specifier|final
name|PropertiesComponent
name|pc
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"properties"
argument_list|,
name|PropertiesComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|pc
operator|.
name|addFunction
argument_list|(
operator|new
name|ContainerPropertiesFunction
argument_list|(
name|containers
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
comment|// ******************
comment|// Containers set-up
comment|// ******************
DECL|method|createContainer ()
specifier|protected
name|GenericContainer
argument_list|<
name|?
argument_list|>
name|createContainer
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|createContainers ()
specifier|protected
name|List
argument_list|<
name|GenericContainer
argument_list|<
name|?
argument_list|>
argument_list|>
name|createContainers
parameter_list|()
block|{
name|GenericContainer
argument_list|<
name|?
argument_list|>
name|container
init|=
name|createContainer
argument_list|()
decl_stmt|;
return|return
name|container
operator|==
literal|null
condition|?
name|Collections
operator|.
name|emptyList
argument_list|()
else|:
name|Collections
operator|.
name|singletonList
argument_list|(
name|container
argument_list|)
return|;
block|}
DECL|method|containersStartupTimeout ()
specifier|protected
name|long
name|containersStartupTimeout
parameter_list|()
block|{
return|return
name|TimeUnit
operator|.
name|MINUTES
operator|.
name|toSeconds
argument_list|(
literal|1
argument_list|)
return|;
block|}
DECL|method|containerShutdownTimeout ()
specifier|protected
name|long
name|containerShutdownTimeout
parameter_list|()
block|{
return|return
name|TimeUnit
operator|.
name|MINUTES
operator|.
name|toSeconds
argument_list|(
literal|1
argument_list|)
return|;
block|}
DECL|method|containerNetwork ()
specifier|protected
name|Network
name|containerNetwork
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|// ******************
comment|// Helpers
comment|// ******************
DECL|method|getContainer (String containerName)
specifier|protected
name|GenericContainer
argument_list|<
name|?
argument_list|>
name|getContainer
parameter_list|(
name|String
name|containerName
parameter_list|)
block|{
return|return
name|Containers
operator|.
name|lookup
argument_list|(
name|containers
argument_list|,
name|containerName
argument_list|)
return|;
block|}
DECL|method|getContainerHost (String containerName)
specifier|protected
name|String
name|getContainerHost
parameter_list|(
name|String
name|containerName
parameter_list|)
block|{
return|return
name|getContainer
argument_list|(
name|containerName
argument_list|)
operator|.
name|getContainerIpAddress
argument_list|()
return|;
block|}
DECL|method|getContainerPort (String containerName, int originalPort)
specifier|protected
name|int
name|getContainerPort
parameter_list|(
name|String
name|containerName
parameter_list|,
name|int
name|originalPort
parameter_list|)
block|{
return|return
name|getContainer
argument_list|(
name|containerName
argument_list|)
operator|.
name|getMappedPort
argument_list|(
name|originalPort
argument_list|)
return|;
block|}
block|}
end_class

end_unit

