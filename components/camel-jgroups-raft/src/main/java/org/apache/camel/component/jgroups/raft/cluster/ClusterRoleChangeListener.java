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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|protocols
operator|.
name|raft
operator|.
name|RAFT
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|protocols
operator|.
name|raft
operator|.
name|Role
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
DECL|class|ClusterRoleChangeListener
specifier|public
class|class
name|ClusterRoleChangeListener
implements|implements
name|RAFT
operator|.
name|RoleChange
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
name|ClusterRoleChangeListener
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|jgroupsRaftClusterView
specifier|private
specifier|final
name|JGroupsRaftClusterView
name|jgroupsRaftClusterView
decl_stmt|;
DECL|method|ClusterRoleChangeListener (JGroupsRaftClusterView jgroupsRaftClusterView)
specifier|public
name|ClusterRoleChangeListener
parameter_list|(
name|JGroupsRaftClusterView
name|jgroupsRaftClusterView
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|jgroupsRaftClusterView
argument_list|,
literal|"endpoint"
argument_list|)
expr_stmt|;
name|this
operator|.
name|jgroupsRaftClusterView
operator|=
name|jgroupsRaftClusterView
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|roleChanged (Role role)
specifier|public
name|void
name|roleChanged
parameter_list|(
name|Role
name|role
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Role received {}."
argument_list|,
name|role
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|role
condition|)
block|{
case|case
name|Leader
case|:
if|if
condition|(
operator|!
name|jgroupsRaftClusterView
operator|.
name|isMaster
argument_list|()
condition|)
block|{
name|jgroupsRaftClusterView
operator|.
name|setMaster
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|jgroupsRaftClusterView
operator|.
name|fireLeadershipChangedEvent
argument_list|(
name|Optional
operator|.
name|ofNullable
argument_list|(
name|jgroupsRaftClusterView
operator|.
name|getLocalMember
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|Follower
case|:
if|if
condition|(
name|jgroupsRaftClusterView
operator|.
name|isMaster
argument_list|()
condition|)
block|{
name|jgroupsRaftClusterView
operator|.
name|setMaster
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|jgroupsRaftClusterView
operator|.
name|fireLeadershipChangedEvent
argument_list|(
name|Optional
operator|.
name|empty
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|Candidate
case|:
if|if
condition|(
name|jgroupsRaftClusterView
operator|.
name|isMaster
argument_list|()
condition|)
block|{
name|jgroupsRaftClusterView
operator|.
name|setMaster
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|jgroupsRaftClusterView
operator|.
name|fireLeadershipChangedEvent
argument_list|(
name|Optional
operator|.
name|empty
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
name|LOG
operator|.
name|error
argument_list|(
literal|"Role {} unknown."
argument_list|,
name|role
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Role "
operator|+
name|role
operator|+
literal|" unknown."
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

