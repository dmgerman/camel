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
name|Collections
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
name|List
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
name|Objects
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
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
name|impl
operator|.
name|cloud
operator|.
name|DefaultServiceDefinition
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
name|cloud
operator|.
name|DefaultServiceDiscovery
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
name|codehaus
operator|.
name|jackson
operator|.
name|map
operator|.
name|annotate
operator|.
name|JsonRootName
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
DECL|class|ZooKeeperServiceDiscovery
specifier|public
class|class
name|ZooKeeperServiceDiscovery
extends|extends
name|DefaultServiceDiscovery
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
name|ZooKeeperServiceDiscovery
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|ZooKeeperCuratorConfiguration
name|configuration
decl_stmt|;
DECL|field|managedInstance
specifier|private
specifier|final
name|boolean
name|managedInstance
decl_stmt|;
DECL|field|curator
specifier|private
name|CuratorFramework
name|curator
decl_stmt|;
DECL|field|serviceDiscovery
specifier|private
name|ServiceDiscovery
argument_list|<
name|MetaData
argument_list|>
name|serviceDiscovery
decl_stmt|;
DECL|method|ZooKeeperServiceDiscovery (ZooKeeperCuratorConfiguration configuration)
specifier|public
name|ZooKeeperServiceDiscovery
parameter_list|(
name|ZooKeeperCuratorConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|curator
operator|=
name|configuration
operator|.
name|getCuratorFramework
argument_list|()
expr_stmt|;
name|this
operator|.
name|managedInstance
operator|=
name|Objects
operator|.
name|isNull
argument_list|(
name|curator
argument_list|)
expr_stmt|;
block|}
comment|// *********************************************
comment|// Lifecycle
comment|// *********************************************
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|curator
operator|==
literal|null
condition|)
block|{
comment|// Validation
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"Camel Context"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
operator|.
name|getBasePath
argument_list|()
argument_list|,
literal|"ZooKeeper base path"
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Starting ZooKeeper Curator with namespace '{}',  nodes: '{}'"
argument_list|,
name|configuration
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|String
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|configuration
operator|.
name|getNodes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|curator
operator|=
name|ZooKeeperCuratorHelper
operator|.
name|createCurator
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|curator
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|serviceDiscovery
operator|==
literal|null
condition|)
block|{
comment|// Validation
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
operator|.
name|getBasePath
argument_list|()
argument_list|,
literal|"ZooKeeper base path"
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Starting ZooKeeper ServiceDiscoveryBuilder with base path '{}'"
argument_list|,
name|configuration
operator|.
name|getBasePath
argument_list|()
argument_list|)
expr_stmt|;
name|serviceDiscovery
operator|=
name|ZooKeeperCuratorHelper
operator|.
name|createServiceDiscovery
argument_list|(
name|configuration
argument_list|,
name|curator
argument_list|,
name|MetaData
operator|.
name|class
argument_list|)
expr_stmt|;
name|serviceDiscovery
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|serviceDiscovery
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|serviceDiscovery
operator|.
name|close
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
literal|"Error closing Curator ServiceDiscovery"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|curator
operator|!=
literal|null
operator|&&
name|managedInstance
condition|)
block|{
name|curator
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|// *********************************************
comment|// Implementation
comment|// *********************************************
annotation|@
name|Override
DECL|method|getServices (String name)
specifier|public
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|getServices
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|serviceDiscovery
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
try|try
block|{
return|return
name|serviceDiscovery
operator|.
name|queryForInstances
argument_list|(
name|name
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|si
lambda|->
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|meta
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|ifNotEmpty
argument_list|(
name|si
operator|.
name|getPayload
argument_list|()
argument_list|,
name|meta
operator|::
name|putAll
argument_list|)
expr_stmt|;
name|meta
operator|.
name|put
argument_list|(
literal|"service_name"
argument_list|,
name|si
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|meta
operator|.
name|put
argument_list|(
literal|"service_id"
argument_list|,
name|si
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|meta
operator|.
name|put
argument_list|(
literal|"service_type"
argument_list|,
name|si
operator|.
name|getServiceType
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|DefaultServiceDefinition
argument_list|(
name|si
operator|.
name|getName
argument_list|()
argument_list|,
name|si
operator|.
name|getAddress
argument_list|()
argument_list|,
name|si
operator|.
name|getSslPort
argument_list|()
operator|!=
literal|null
condition|?
name|si
operator|.
name|getSslPort
argument_list|()
else|:
name|si
operator|.
name|getPort
argument_list|()
argument_list|,
name|meta
argument_list|)
return|;
block|}
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|// *********************************************
comment|// Helpers
comment|// *********************************************
annotation|@
name|JsonRootName
argument_list|(
literal|"meta"
argument_list|)
DECL|class|MetaData
specifier|public
specifier|static
specifier|final
class|class
name|MetaData
extends|extends
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
block|{     }
block|}
end_class

end_unit

