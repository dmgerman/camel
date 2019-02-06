begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jgroups.raft.springboot.cluster.springboot
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
name|raft
operator|.
name|springboot
operator|.
name|cluster
operator|.
name|springboot
package|;
end_package

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
literal|"camel.component.jgroups.raft.cluster.service"
argument_list|)
DECL|class|JGroupsRaftClusterServiceConfiguration
specifier|public
class|class
name|JGroupsRaftClusterServiceConfiguration
block|{
comment|/**      * Sets if the jgroups raft cluster service should be enabled or not, default is false.      */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
decl_stmt|;
comment|/**      * Cluster Service ID      */
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * JGroups-raft ID      */
DECL|field|raftId
specifier|private
name|String
name|raftId
decl_stmt|;
comment|/**      * JGrups-raft configuration File name      */
DECL|field|jgroupsRaftConfig
specifier|private
name|String
name|jgroupsRaftConfig
decl_stmt|;
comment|/**      * JGroups Cluster name      */
DECL|field|jgroupsRaftClusterName
specifier|private
name|String
name|jgroupsRaftClusterName
decl_stmt|;
DECL|method|getRaftId ()
specifier|public
name|String
name|getRaftId
parameter_list|()
block|{
return|return
name|raftId
return|;
block|}
DECL|method|setRaftId (String raftId)
specifier|public
name|void
name|setRaftId
parameter_list|(
name|String
name|raftId
parameter_list|)
block|{
name|this
operator|.
name|raftId
operator|=
name|raftId
expr_stmt|;
block|}
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
DECL|method|getJgroupsRaftConfig ()
specifier|public
name|String
name|getJgroupsRaftConfig
parameter_list|()
block|{
return|return
name|jgroupsRaftConfig
return|;
block|}
DECL|method|setJgroupsRaftConfig (String jgroupsRaftConfig)
specifier|public
name|void
name|setJgroupsRaftConfig
parameter_list|(
name|String
name|jgroupsRaftConfig
parameter_list|)
block|{
name|this
operator|.
name|jgroupsRaftConfig
operator|=
name|jgroupsRaftConfig
expr_stmt|;
block|}
DECL|method|getJgroupsRaftClusterName ()
specifier|public
name|String
name|getJgroupsRaftClusterName
parameter_list|()
block|{
return|return
name|jgroupsRaftClusterName
return|;
block|}
DECL|method|setJgroupsRaftClusterName (String jgroupsRaftClusterName)
specifier|public
name|void
name|setJgroupsRaftClusterName
parameter_list|(
name|String
name|jgroupsRaftClusterName
parameter_list|)
block|{
name|this
operator|.
name|jgroupsRaftClusterName
operator|=
name|jgroupsRaftClusterName
expr_stmt|;
block|}
block|}
end_class

end_unit

