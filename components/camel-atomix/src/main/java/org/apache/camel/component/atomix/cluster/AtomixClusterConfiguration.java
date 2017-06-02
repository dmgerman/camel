begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  */
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
name|component
operator|.
name|atomix
operator|.
name|AtomixConfiguration
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
name|UriParam
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
name|UriParams
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|AtomixClusterConfiguration
specifier|public
class|class
name|AtomixClusterConfiguration
extends|extends
name|AtomixConfiguration
implements|implements
name|Cloneable
block|{
annotation|@
name|UriParam
DECL|field|storagePath
specifier|private
name|String
name|storagePath
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"MEMORY"
argument_list|)
DECL|field|storageLevel
specifier|private
name|StorageLevel
name|storageLevel
init|=
name|StorageLevel
operator|.
name|MEMORY
decl_stmt|;
annotation|@
name|UriParam
DECL|field|replicaRef
specifier|private
name|String
name|replicaRef
decl_stmt|;
annotation|@
name|UriParam
DECL|field|replica
specifier|private
name|AtomixReplica
name|replica
decl_stmt|;
DECL|method|AtomixClusterConfiguration ()
specifier|public
name|AtomixClusterConfiguration
parameter_list|()
block|{     }
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
comment|/**      * Sets the log directory.      */
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
comment|/**      * Sets the log storage level.      */
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
DECL|method|getReplicaRef ()
specifier|public
name|String
name|getReplicaRef
parameter_list|()
block|{
return|return
name|replicaRef
return|;
block|}
comment|/**      * Set the reference of an instance of {@link AtomixReplica}.      */
DECL|method|setReplicaRef (String clusterref)
specifier|public
name|void
name|setReplicaRef
parameter_list|(
name|String
name|clusterref
parameter_list|)
block|{
name|this
operator|.
name|replicaRef
operator|=
name|clusterref
expr_stmt|;
block|}
DECL|method|getReplica ()
specifier|public
name|AtomixReplica
name|getReplica
parameter_list|()
block|{
return|return
name|replica
return|;
block|}
comment|/**      * Set an instance of {@link AtomixReplica}.      */
DECL|method|setReplica (AtomixReplica replica)
specifier|public
name|void
name|setReplica
parameter_list|(
name|AtomixReplica
name|replica
parameter_list|)
block|{
name|this
operator|.
name|replica
operator|=
name|replica
expr_stmt|;
block|}
comment|// ****************************************
comment|// Copy
comment|// ****************************************
DECL|method|copy ()
specifier|public
name|AtomixClusterConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|AtomixClusterConfiguration
operator|)
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
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
block|}
end_class

end_unit

