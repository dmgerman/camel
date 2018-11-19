begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jgroups.raft.cluster
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
name|cluster
package|;
end_package

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
name|cluster
operator|.
name|AbstractCamelClusterService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|raft
operator|.
name|RaftHandle
import|;
end_import

begin_class
DECL|class|JGroupsRaftClusterService
specifier|public
class|class
name|JGroupsRaftClusterService
extends|extends
name|AbstractCamelClusterService
argument_list|<
name|JGroupsRaftClusterView
argument_list|>
block|{
DECL|field|DEFAULT_JGROUPS_CONFIG
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_JGROUPS_CONFIG
init|=
literal|"raft.xml"
decl_stmt|;
DECL|field|DEFAULT_JGROUPS_CLUSTERNAME
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_JGROUPS_CLUSTERNAME
init|=
literal|"jgroupsraft-master"
decl_stmt|;
DECL|field|jgroupsConfig
specifier|private
name|String
name|jgroupsConfig
decl_stmt|;
DECL|field|jgroupsClusterName
specifier|private
name|String
name|jgroupsClusterName
decl_stmt|;
DECL|field|raftHandle
specifier|private
name|RaftHandle
name|raftHandle
decl_stmt|;
DECL|field|raftId
specifier|private
name|String
name|raftId
decl_stmt|;
DECL|method|JGroupsRaftClusterService ()
specifier|public
name|JGroupsRaftClusterService
parameter_list|()
block|{
name|this
operator|.
name|jgroupsConfig
operator|=
name|DEFAULT_JGROUPS_CONFIG
expr_stmt|;
name|this
operator|.
name|jgroupsClusterName
operator|=
name|DEFAULT_JGROUPS_CLUSTERNAME
expr_stmt|;
block|}
DECL|method|JGroupsRaftClusterService (String jgroupsConfig, String jgroupsClusterName, RaftHandle raftHandle, String raftId)
specifier|public
name|JGroupsRaftClusterService
parameter_list|(
name|String
name|jgroupsConfig
parameter_list|,
name|String
name|jgroupsClusterName
parameter_list|,
name|RaftHandle
name|raftHandle
parameter_list|,
name|String
name|raftId
parameter_list|)
block|{
name|this
operator|.
name|jgroupsConfig
operator|=
name|jgroupsConfig
expr_stmt|;
name|this
operator|.
name|jgroupsClusterName
operator|=
name|jgroupsClusterName
expr_stmt|;
name|this
operator|.
name|raftHandle
operator|=
name|raftHandle
expr_stmt|;
name|this
operator|.
name|raftId
operator|=
name|raftId
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createView (String namespace)
specifier|protected
name|JGroupsRaftClusterView
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
name|JGroupsRaftClusterView
argument_list|(
name|this
argument_list|,
name|namespace
argument_list|,
name|jgroupsConfig
argument_list|,
name|jgroupsClusterName
argument_list|,
name|raftHandle
argument_list|,
name|raftId
argument_list|)
return|;
block|}
DECL|method|getRaftHandle ()
specifier|public
name|RaftHandle
name|getRaftHandle
parameter_list|()
block|{
return|return
name|raftHandle
return|;
block|}
DECL|method|setRaftHandle (RaftHandle raftHandle)
specifier|public
name|void
name|setRaftHandle
parameter_list|(
name|RaftHandle
name|raftHandle
parameter_list|)
block|{
name|this
operator|.
name|raftHandle
operator|=
name|raftHandle
expr_stmt|;
block|}
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
DECL|method|getJgroupsConfig ()
specifier|public
name|String
name|getJgroupsConfig
parameter_list|()
block|{
return|return
name|jgroupsConfig
return|;
block|}
DECL|method|setJgroupsConfig (String jgroupsConfig)
specifier|public
name|void
name|setJgroupsConfig
parameter_list|(
name|String
name|jgroupsConfig
parameter_list|)
block|{
name|this
operator|.
name|jgroupsConfig
operator|=
name|jgroupsConfig
expr_stmt|;
block|}
DECL|method|getJgroupsClusterName ()
specifier|public
name|String
name|getJgroupsClusterName
parameter_list|()
block|{
return|return
name|jgroupsClusterName
return|;
block|}
DECL|method|setJgroupsClusterName (String jgroupsClusterName)
specifier|public
name|void
name|setJgroupsClusterName
parameter_list|(
name|String
name|jgroupsClusterName
parameter_list|)
block|{
name|this
operator|.
name|jgroupsClusterName
operator|=
name|jgroupsClusterName
expr_stmt|;
block|}
block|}
end_class

end_unit

