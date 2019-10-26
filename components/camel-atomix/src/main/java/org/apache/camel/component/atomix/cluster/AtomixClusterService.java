begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.cluster
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
name|cluster
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
name|support
operator|.
name|cluster
operator|.
name|AbstractCamelClusterService
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

begin_class
DECL|class|AtomixClusterService
specifier|public
specifier|final
class|class
name|AtomixClusterService
extends|extends
name|AbstractCamelClusterService
argument_list|<
name|AtomixClusterView
argument_list|>
block|{
DECL|field|address
specifier|private
name|Address
name|address
decl_stmt|;
DECL|field|configuration
specifier|private
name|AtomixClusterConfiguration
name|configuration
decl_stmt|;
DECL|field|atomix
specifier|private
name|AtomixReplica
name|atomix
decl_stmt|;
DECL|method|AtomixClusterService ()
specifier|public
name|AtomixClusterService
parameter_list|()
block|{
name|this
operator|.
name|configuration
operator|=
operator|new
name|AtomixClusterConfiguration
argument_list|()
expr_stmt|;
block|}
DECL|method|AtomixClusterService (CamelContext camelContext, Address address, AtomixClusterConfiguration configuration)
specifier|public
name|AtomixClusterService
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Address
name|address
parameter_list|,
name|AtomixClusterConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
literal|null
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
comment|// **********************************
comment|// Properties
comment|// **********************************
DECL|method|getAddress ()
specifier|public
name|Address
name|getAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
DECL|method|setAddress (String address)
specifier|public
name|void
name|setAddress
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
operator|new
name|Address
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
DECL|method|setAddress (Address address)
specifier|public
name|void
name|setAddress
parameter_list|(
name|Address
name|address
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|AtomixClusterConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (AtomixClusterConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|AtomixClusterConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
DECL|method|getStoragePath ()
specifier|public
name|String
name|getStoragePath
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getStoragePath
argument_list|()
return|;
block|}
DECL|method|setStoragePath (String storagePath)
specifier|public
name|void
name|setStoragePath
parameter_list|(
name|String
name|storagePath
parameter_list|)
block|{
name|configuration
operator|.
name|setStoragePath
argument_list|(
name|storagePath
argument_list|)
expr_stmt|;
block|}
DECL|method|getStorageLevel ()
specifier|public
name|StorageLevel
name|getStorageLevel
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getStorageLevel
argument_list|()
return|;
block|}
DECL|method|getNodes ()
specifier|public
name|List
argument_list|<
name|Address
argument_list|>
name|getNodes
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getNodes
argument_list|()
return|;
block|}
DECL|method|setNodes (List<Address> nodes)
specifier|public
name|void
name|setNodes
parameter_list|(
name|List
argument_list|<
name|Address
argument_list|>
name|nodes
parameter_list|)
block|{
name|configuration
operator|.
name|setNodes
argument_list|(
name|nodes
argument_list|)
expr_stmt|;
block|}
DECL|method|setStorageLevel (StorageLevel storageLevel)
specifier|public
name|void
name|setStorageLevel
parameter_list|(
name|StorageLevel
name|storageLevel
parameter_list|)
block|{
name|configuration
operator|.
name|setStorageLevel
argument_list|(
name|storageLevel
argument_list|)
expr_stmt|;
block|}
DECL|method|setNodes (String nodes)
specifier|public
name|void
name|setNodes
parameter_list|(
name|String
name|nodes
parameter_list|)
block|{
name|configuration
operator|.
name|setNodes
argument_list|(
name|nodes
argument_list|)
expr_stmt|;
block|}
DECL|method|getTransport ()
specifier|public
name|String
name|getTransport
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getTransportClassName
argument_list|()
return|;
block|}
DECL|method|setTransportClassName (String transport)
specifier|public
name|void
name|setTransportClassName
parameter_list|(
name|String
name|transport
parameter_list|)
block|{
name|configuration
operator|.
name|setTransportClassName
argument_list|(
name|transport
argument_list|)
expr_stmt|;
block|}
DECL|method|getAtomix ()
specifier|public
name|AtomixReplica
name|getAtomix
parameter_list|()
block|{
return|return
operator|(
name|AtomixReplica
operator|)
name|configuration
operator|.
name|getAtomix
argument_list|()
return|;
block|}
DECL|method|setAtomix (AtomixReplica atomix)
specifier|public
name|void
name|setAtomix
parameter_list|(
name|AtomixReplica
name|atomix
parameter_list|)
block|{
name|configuration
operator|.
name|setAtomix
argument_list|(
name|atomix
argument_list|)
expr_stmt|;
block|}
DECL|method|getConfigurationUri ()
specifier|public
name|String
name|getConfigurationUri
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getConfigurationUri
argument_list|()
return|;
block|}
DECL|method|setConfigurationUri (String configurationUri)
specifier|public
name|void
name|setConfigurationUri
parameter_list|(
name|String
name|configurationUri
parameter_list|)
block|{
name|configuration
operator|.
name|setConfigurationUri
argument_list|(
name|configurationUri
argument_list|)
expr_stmt|;
block|}
DECL|method|isEphemeral ()
specifier|public
name|boolean
name|isEphemeral
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|isEphemeral
argument_list|()
return|;
block|}
DECL|method|setEphemeral (boolean ephemeral)
specifier|public
name|void
name|setEphemeral
parameter_list|(
name|boolean
name|ephemeral
parameter_list|)
block|{
name|configuration
operator|.
name|setEphemeral
argument_list|(
name|ephemeral
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
name|atomix
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Leaving atomix cluster replica {}"
argument_list|,
name|atomix
argument_list|)
expr_stmt|;
name|atomix
operator|.
name|leave
argument_list|()
operator|.
name|join
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createView (String namespace)
specifier|protected
name|AtomixClusterView
name|createView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|AtomixClusterView
argument_list|(
name|this
argument_list|,
name|namespace
argument_list|,
name|getOrCreateReplica
argument_list|()
argument_list|,
name|configuration
argument_list|)
return|;
block|}
DECL|method|getOrCreateReplica ()
specifier|private
name|AtomixReplica
name|getOrCreateReplica
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|atomix
operator|==
literal|null
condition|)
block|{
comment|// Validate parameters
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
name|address
argument_list|,
literal|"Atomix Node Address"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
argument_list|,
literal|"Atomix Node Configuration"
argument_list|)
expr_stmt|;
name|atomix
operator|=
name|AtomixClusterHelper
operator|.
name|createReplica
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|address
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getNodes
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Bootstrap cluster on address {} for nodes: {}"
argument_list|,
name|address
argument_list|,
name|configuration
operator|.
name|getNodes
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|atomix
operator|.
name|bootstrap
argument_list|(
name|configuration
operator|.
name|getNodes
argument_list|()
argument_list|)
operator|.
name|join
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Bootstrap cluster done"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Bootstrap cluster on address {}"
argument_list|,
name|address
argument_list|)
expr_stmt|;
name|this
operator|.
name|atomix
operator|.
name|bootstrap
argument_list|()
operator|.
name|join
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Bootstrap cluster done"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|this
operator|.
name|atomix
return|;
block|}
block|}
end_class

end_unit

