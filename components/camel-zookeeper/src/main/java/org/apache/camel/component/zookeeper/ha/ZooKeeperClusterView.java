begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper.ha
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
name|ha
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
name|Optional
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
name|atomic
operator|.
name|AtomicBoolean
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
name|ha
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
name|ha
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
name|impl
operator|.
name|ha
operator|.
name|AbstractCamelClusterView
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
name|framework
operator|.
name|recipes
operator|.
name|leader
operator|.
name|LeaderSelector
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
name|recipes
operator|.
name|leader
operator|.
name|LeaderSelectorListenerAdapter
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
name|recipes
operator|.
name|leader
operator|.
name|Participant
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
DECL|class|ZooKeeperClusterView
specifier|final
class|class
name|ZooKeeperClusterView
extends|extends
name|AbstractCamelClusterView
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
name|ZooKeeperClusterView
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
DECL|field|client
specifier|private
specifier|final
name|CuratorFramework
name|client
decl_stmt|;
DECL|field|localMember
specifier|private
specifier|final
name|CuratorLocalMember
name|localMember
decl_stmt|;
DECL|field|leaderSelector
specifier|private
name|LeaderSelector
name|leaderSelector
decl_stmt|;
DECL|method|ZooKeeperClusterView (CamelClusterService cluster, ZooKeeperCuratorConfiguration configuration, CuratorFramework client, String namespace)
specifier|public
name|ZooKeeperClusterView
parameter_list|(
name|CamelClusterService
name|cluster
parameter_list|,
name|ZooKeeperCuratorConfiguration
name|configuration
parameter_list|,
name|CuratorFramework
name|client
parameter_list|,
name|String
name|namespace
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
name|localMember
operator|=
operator|new
name|CuratorLocalMember
argument_list|()
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
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
name|this
operator|.
name|localMember
return|;
block|}
annotation|@
name|Override
DECL|method|getMaster ()
specifier|public
name|Optional
argument_list|<
name|CamelClusterMember
argument_list|>
name|getMaster
parameter_list|()
block|{
if|if
condition|(
name|leaderSelector
operator|==
literal|null
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
try|try
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|CuratorClusterMember
argument_list|(
name|leaderSelector
operator|.
name|getLeader
argument_list|()
argument_list|)
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
if|if
condition|(
name|leaderSelector
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
name|leaderSelector
operator|.
name|getParticipants
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|CuratorClusterMember
operator|::
operator|new
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
name|leaderSelector
operator|==
literal|null
condition|)
block|{
name|leaderSelector
operator|=
operator|new
name|LeaderSelector
argument_list|(
name|client
argument_list|,
name|configuration
operator|.
name|getBasePath
argument_list|()
argument_list|,
operator|new
name|CamelLeaderElectionListener
argument_list|()
argument_list|)
expr_stmt|;
name|leaderSelector
operator|.
name|setId
argument_list|(
name|getClusterService
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|leaderSelector
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
name|leaderSelector
operator|!=
literal|null
condition|)
block|{
name|leaderSelector
operator|.
name|interruptLeadership
argument_list|()
expr_stmt|;
name|leaderSelector
operator|.
name|close
argument_list|()
expr_stmt|;
name|leaderSelector
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|class|CamelLeaderElectionListener
class|class
name|CamelLeaderElectionListener
extends|extends
name|LeaderSelectorListenerAdapter
block|{
annotation|@
name|Override
DECL|method|takeLeadership (CuratorFramework curatorFramework)
specifier|public
name|void
name|takeLeadership
parameter_list|(
name|CuratorFramework
name|curatorFramework
parameter_list|)
throws|throws
name|Exception
block|{
name|localMember
operator|.
name|setMaster
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|fireLeadershipChangedEvent
argument_list|(
name|localMember
argument_list|)
expr_stmt|;
while|while
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|Thread
operator|.
name|interrupted
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
name|localMember
operator|.
name|setMaster
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|getMaster
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|leader
lambda|->
name|fireLeadershipChangedEvent
argument_list|(
name|leader
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// ***********************************************
comment|//
comment|// ***********************************************
DECL|class|CuratorLocalMember
specifier|private
specifier|final
class|class
name|CuratorLocalMember
implements|implements
name|CamelClusterMember
block|{
DECL|field|master
specifier|private
name|AtomicBoolean
name|master
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|method|setMaster (boolean master)
name|void
name|setMaster
parameter_list|(
name|boolean
name|master
parameter_list|)
block|{
name|this
operator|.
name|master
operator|.
name|set
argument_list|(
name|master
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isMaster ()
specifier|public
name|boolean
name|isMaster
parameter_list|()
block|{
return|return
name|master
operator|.
name|get
argument_list|()
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
DECL|class|CuratorClusterMember
specifier|private
specifier|final
class|class
name|CuratorClusterMember
implements|implements
name|CamelClusterMember
block|{
DECL|field|participant
specifier|private
specifier|final
name|Participant
name|participant
decl_stmt|;
DECL|method|CuratorClusterMember (Participant participant)
name|CuratorClusterMember
parameter_list|(
name|Participant
name|participant
parameter_list|)
block|{
name|this
operator|.
name|participant
operator|=
name|participant
expr_stmt|;
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
name|participant
operator|.
name|getId
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isMaster ()
specifier|public
name|boolean
name|isMaster
parameter_list|()
block|{
try|try
block|{
return|return
name|leaderSelector
operator|.
name|getLeader
argument_list|()
operator|.
name|equals
argument_list|(
name|this
operator|.
name|participant
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|""
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

