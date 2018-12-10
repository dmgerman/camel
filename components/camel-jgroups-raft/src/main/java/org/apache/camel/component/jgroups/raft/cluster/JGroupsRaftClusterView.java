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
name|Optional
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
name|cluster
operator|.
name|CamelClusterMember
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
name|cluster
operator|.
name|CamelClusterService
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
name|jgroups
operator|.
name|raft
operator|.
name|JGroupsRaftConstants
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
name|jgroups
operator|.
name|raft
operator|.
name|utils
operator|.
name|NopStateMachine
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
name|AbstractCamelClusterView
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|JChannel
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
DECL|class|JGroupsRaftClusterView
specifier|public
class|class
name|JGroupsRaftClusterView
extends|extends
name|AbstractCamelClusterView
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JGroupsRaftClusterView
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|localMember
specifier|private
specifier|final
name|CamelClusterMember
name|localMember
init|=
operator|new
name|JGropusraftLocalMember
argument_list|()
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
DECL|field|isMaster
specifier|private
specifier|volatile
name|boolean
name|isMaster
decl_stmt|;
DECL|method|JGroupsRaftClusterView (CamelClusterService cluster, String namespace, String jgroupsConfig, String jgroupsClusterName, RaftHandle raftHandle, String raftId)
specifier|protected
name|JGroupsRaftClusterView
parameter_list|(
name|CamelClusterService
name|cluster
parameter_list|,
name|String
name|namespace
parameter_list|,
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
name|super
argument_list|(
name|cluster
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
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
DECL|method|getLeader ()
specifier|public
name|Optional
argument_list|<
name|CamelClusterMember
argument_list|>
name|getLeader
parameter_list|()
block|{
if|if
condition|(
name|isMaster
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|localMember
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getLocalMember ()
specifier|public
name|CamelClusterMember
name|getLocalMember
parameter_list|()
block|{
return|return
name|localMember
return|;
block|}
annotation|@
name|Override
DECL|method|getMembers ()
specifier|public
name|List
argument_list|<
name|CamelClusterMember
argument_list|>
name|getMembers
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|CamelClusterMember
argument_list|>
argument_list|()
block|{
block|{
name|add
parameter_list|(
name|localMember
parameter_list|)
constructor_decl|;
block|}
block|}
return|;
block|}
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
name|raftHandle
operator|==
literal|null
operator|&&
name|jgroupsConfig
operator|!=
literal|null
operator|&&
operator|!
name|jgroupsConfig
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|raftHandle
operator|=
operator|new
name|RaftHandle
argument_list|(
operator|new
name|JChannel
argument_list|(
name|jgroupsConfig
argument_list|)
argument_list|,
operator|new
name|NopStateMachine
argument_list|()
argument_list|)
operator|.
name|raftId
argument_list|(
name|raftId
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|raftHandle
operator|==
literal|null
condition|)
block|{
name|raftHandle
operator|=
operator|new
name|RaftHandle
argument_list|(
operator|new
name|JChannel
argument_list|(
name|JGroupsRaftConstants
operator|.
name|DEFAULT_JGROUPSRAFT_CONFIG
argument_list|)
argument_list|,
operator|new
name|NopStateMachine
argument_list|()
argument_list|)
operator|.
name|raftId
argument_list|(
name|raftId
argument_list|)
expr_stmt|;
block|}
name|fireLeadershipChangedEvent
argument_list|(
name|Optional
operator|.
name|empty
argument_list|()
argument_list|)
expr_stmt|;
name|raftHandle
operator|.
name|addRoleListener
argument_list|(
operator|new
name|ClusterRoleChangeListener
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|raftHandle
operator|.
name|channel
argument_list|()
operator|.
name|connect
argument_list|(
name|jgroupsClusterName
argument_list|)
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
name|isMaster
operator|=
literal|false
expr_stmt|;
name|fireLeadershipChangedEvent
argument_list|(
name|Optional
operator|.
name|empty
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Disconnecting JGroupsraft Channel for JGroupsRaftClusterView with Id {}"
argument_list|,
name|raftId
argument_list|)
expr_stmt|;
name|raftHandle
operator|.
name|channel
argument_list|()
operator|.
name|disconnect
argument_list|()
expr_stmt|;
if|if
condition|(
name|raftHandle
operator|!=
literal|null
operator|&&
name|raftHandle
operator|.
name|log
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|raftHandle
operator|.
name|log
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Closed Log for JGroupsRaftClusterView with Id {}"
argument_list|,
name|raftId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|isMaster
operator|=
literal|false
expr_stmt|;
name|fireLeadershipChangedEvent
argument_list|(
name|Optional
operator|.
name|empty
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|raftHandle
operator|!=
literal|null
condition|)
block|{
name|raftHandle
operator|.
name|channel
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|raftHandle
operator|=
literal|null
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Closing JGroupsraft Channel for JGroupsRaftClusterView with Id {}"
argument_list|,
name|raftId
argument_list|)
expr_stmt|;
if|if
condition|(
name|raftHandle
operator|!=
literal|null
operator|&&
name|raftHandle
operator|.
name|channel
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|raftHandle
operator|.
name|channel
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Closed JGroupsraft Channel Channel for JGroupsRaftClusterView with Id {}"
argument_list|,
name|raftId
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Closing Log for JGroupsRaftClusterView with Id {}"
argument_list|,
name|raftId
argument_list|)
expr_stmt|;
if|if
condition|(
name|raftHandle
operator|!=
literal|null
operator|&&
name|raftHandle
operator|.
name|log
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|raftHandle
operator|.
name|log
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Closed Log for JGroupsRaftClusterView with Id {}"
argument_list|,
name|raftId
argument_list|)
expr_stmt|;
block|}
name|raftHandle
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|isMaster ()
specifier|public
name|boolean
name|isMaster
parameter_list|()
block|{
return|return
name|isMaster
return|;
block|}
DECL|method|setMaster (boolean master)
specifier|public
name|void
name|setMaster
parameter_list|(
name|boolean
name|master
parameter_list|)
block|{
name|isMaster
operator|=
name|master
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|fireLeadershipChangedEvent (Optional<CamelClusterMember> leader)
specifier|protected
name|void
name|fireLeadershipChangedEvent
parameter_list|(
name|Optional
argument_list|<
name|CamelClusterMember
argument_list|>
name|leader
parameter_list|)
block|{
name|super
operator|.
name|fireLeadershipChangedEvent
argument_list|(
name|leader
argument_list|)
expr_stmt|;
block|}
DECL|class|JGropusraftLocalMember
specifier|private
specifier|final
class|class
name|JGropusraftLocalMember
implements|implements
name|CamelClusterMember
block|{
annotation|@
name|Override
DECL|method|isLeader ()
specifier|public
name|boolean
name|isLeader
parameter_list|()
block|{
return|return
name|isMaster
return|;
block|}
annotation|@
name|Override
DECL|method|isLocal ()
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|getClusterService
argument_list|()
operator|.
name|getId
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

