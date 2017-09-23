begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.ha.springboot
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
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.atomix.cluster.service"
argument_list|)
DECL|class|AtomixClusterServiceConfiguration
specifier|public
class|class
name|AtomixClusterServiceConfiguration
block|{
DECL|enum|Mode
enum|enum
name|Mode
block|{
DECL|enumConstant|node
name|node
block|,
DECL|enumConstant|client
name|client
block|}
comment|/**      * Sets if the atomix cluster service should be enabled or not, default is false.      */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
decl_stmt|;
comment|/**      * Sets the cluster mode.      */
DECL|field|mode
specifier|private
name|Mode
name|mode
decl_stmt|;
comment|/**      * The address of the nodes composing the cluster.      */
DECL|field|nodes
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|nodes
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * The cluster id.      */
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * The address of the node - node only.      */
DECL|field|address
specifier|private
name|String
name|address
decl_stmt|;
comment|/**      * Sets if the local member should join groups as PersistentMember or not (node only).      */
DECL|field|ephemeral
specifier|private
name|Boolean
name|ephemeral
decl_stmt|;
comment|/**      * The storage directory - node only.      */
DECL|field|storagePath
specifier|private
name|String
name|storagePath
decl_stmt|;
comment|/**      * The storage mode - node only.      */
DECL|field|storageLevel
specifier|private
name|StorageLevel
name|storageLevel
init|=
name|StorageLevel
operator|.
name|MEMORY
decl_stmt|;
comment|/**      * The Atomix configuration uri.      */
DECL|field|configurationUri
specifier|private
name|String
name|configurationUri
decl_stmt|;
comment|// *********************************
comment|// Properties
comment|// *********************************
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
DECL|method|getMode ()
specifier|public
name|Mode
name|getMode
parameter_list|()
block|{
return|return
name|mode
return|;
block|}
DECL|method|getAddress ()
specifier|public
name|String
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
name|address
expr_stmt|;
block|}
DECL|method|setNodes (Set<String> nodes)
specifier|public
name|void
name|setNodes
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|nodes
parameter_list|)
block|{
name|this
operator|.
name|nodes
operator|=
name|nodes
expr_stmt|;
block|}
DECL|method|setMode (Mode mode)
specifier|public
name|void
name|setMode
parameter_list|(
name|Mode
name|mode
parameter_list|)
block|{
name|this
operator|.
name|mode
operator|=
name|mode
expr_stmt|;
block|}
DECL|method|getNodes ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getNodes
parameter_list|()
block|{
return|return
name|nodes
return|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|isEphemeral ()
specifier|public
name|Boolean
name|isEphemeral
parameter_list|()
block|{
return|return
name|ephemeral
return|;
block|}
DECL|method|setEphemeral (Boolean ephemeral)
specifier|public
name|void
name|setEphemeral
parameter_list|(
name|Boolean
name|ephemeral
parameter_list|)
block|{
name|this
operator|.
name|ephemeral
operator|=
name|ephemeral
expr_stmt|;
block|}
DECL|method|getStoragePath ()
specifier|public
name|String
name|getStoragePath
parameter_list|()
block|{
return|return
name|storagePath
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
name|this
operator|.
name|storagePath
operator|=
name|storagePath
expr_stmt|;
block|}
DECL|method|getStorageLevel ()
specifier|public
name|StorageLevel
name|getStorageLevel
parameter_list|()
block|{
return|return
name|storageLevel
return|;
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
name|this
operator|.
name|storageLevel
operator|=
name|storageLevel
expr_stmt|;
block|}
DECL|method|getConfigurationUri ()
specifier|public
name|String
name|getConfigurationUri
parameter_list|()
block|{
return|return
name|configurationUri
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
name|this
operator|.
name|configurationUri
operator|=
name|configurationUri
expr_stmt|;
block|}
block|}
end_class

end_unit

