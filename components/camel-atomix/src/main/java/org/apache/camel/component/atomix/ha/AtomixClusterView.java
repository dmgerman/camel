begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.ha
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
name|ExecutionException
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
name|io
operator|.
name|atomix
operator|.
name|Atomix
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|group
operator|.
name|DistributedGroup
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|group
operator|.
name|GroupMember
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|group
operator|.
name|LocalMember
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
DECL|class|AtomixClusterView
specifier|final
class|class
name|AtomixClusterView
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
name|AtomixClusterView
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|atomix
specifier|private
specifier|final
name|Atomix
name|atomix
decl_stmt|;
DECL|field|localMember
specifier|private
specifier|final
name|AtomixLocalMember
name|localMember
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|AtomixConfiguration
argument_list|<
name|?
argument_list|>
name|configuration
decl_stmt|;
DECL|field|group
specifier|private
name|DistributedGroup
name|group
decl_stmt|;
DECL|method|AtomixClusterView (CamelClusterService cluster, String namespace, Atomix atomix, AtomixConfiguration<?> configuration)
name|AtomixClusterView
parameter_list|(
name|CamelClusterService
name|cluster
parameter_list|,
name|String
name|namespace
parameter_list|,
name|Atomix
name|atomix
parameter_list|,
name|AtomixConfiguration
argument_list|<
name|?
argument_list|>
name|configuration
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
name|atomix
operator|=
name|atomix
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|localMember
operator|=
operator|new
name|AtomixLocalMember
argument_list|()
expr_stmt|;
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
name|group
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
name|GroupMember
name|leader
init|=
name|group
operator|.
name|election
argument_list|()
operator|.
name|term
argument_list|()
operator|.
name|leader
argument_list|()
decl_stmt|;
if|if
condition|(
name|leader
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
return|return
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|AtomixClusterMember
argument_list|(
name|leader
argument_list|)
argument_list|)
return|;
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
if|if
condition|(
name|group
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
return|return
name|this
operator|.
name|group
operator|.
name|members
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|AtomixClusterMember
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
operator|!
name|localMember
operator|.
name|hasJoined
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Get group {}"
argument_list|,
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|group
operator|=
name|this
operator|.
name|atomix
operator|.
name|getGroup
argument_list|(
name|getNamespace
argument_list|()
argument_list|,
operator|new
name|DistributedGroup
operator|.
name|Config
argument_list|(
name|configuration
operator|.
name|getResourceConfig
argument_list|(
name|getNamespace
argument_list|()
argument_list|)
argument_list|)
argument_list|,
operator|new
name|DistributedGroup
operator|.
name|Options
argument_list|(
name|configuration
operator|.
name|getResourceOptions
argument_list|(
name|getNamespace
argument_list|()
argument_list|)
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Listen election events"
argument_list|)
expr_stmt|;
name|group
operator|.
name|election
argument_list|()
operator|.
name|onElection
argument_list|(
name|term
lambda|->
block|{
if|if
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
name|fireLeadershipChangedEvent
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
name|toClusterMember
argument_list|(
name|term
operator|.
name|leader
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Listen join events"
argument_list|)
expr_stmt|;
name|group
operator|.
name|onJoin
argument_list|(
name|member
lambda|->
block|{
if|if
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
name|fireMemberAddedEvent
argument_list|(
name|toClusterMember
argument_list|(
name|member
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Listen leave events"
argument_list|)
expr_stmt|;
name|group
operator|.
name|onLeave
argument_list|(
name|member
lambda|->
block|{
if|if
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
name|fireMemberRemovedEvent
argument_list|(
name|toClusterMember
argument_list|(
name|member
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Join group {}"
argument_list|,
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|localMember
operator|.
name|join
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
name|localMember
operator|.
name|leave
argument_list|()
expr_stmt|;
block|}
DECL|method|toClusterMember (GroupMember member)
specifier|protected
name|CamelClusterMember
name|toClusterMember
parameter_list|(
name|GroupMember
name|member
parameter_list|)
block|{
return|return
name|localMember
operator|!=
literal|null
operator|&&
name|localMember
operator|.
name|is
argument_list|(
name|member
argument_list|)
condition|?
name|localMember
else|:
operator|new
name|AtomixClusterMember
argument_list|(
name|member
argument_list|)
return|;
block|}
comment|// ***********************************************
comment|//
comment|// ***********************************************
DECL|class|AtomixLocalMember
specifier|final
class|class
name|AtomixLocalMember
implements|implements
name|CamelClusterMember
block|{
DECL|field|member
specifier|private
name|LocalMember
name|member
decl_stmt|;
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
name|String
name|id
init|=
name|getClusterService
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
name|id
return|;
block|}
if|if
condition|(
name|member
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The view has not yet joined the cluster"
argument_list|)
throw|;
block|}
return|return
name|member
operator|.
name|id
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isLeader ()
specifier|public
name|boolean
name|isLeader
parameter_list|()
block|{
if|if
condition|(
name|member
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|member
operator|.
name|equals
argument_list|(
name|group
operator|.
name|election
argument_list|()
operator|.
name|term
argument_list|()
operator|.
name|leader
argument_list|()
argument_list|)
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
DECL|method|is (GroupMember member)
name|boolean
name|is
parameter_list|(
name|GroupMember
name|member
parameter_list|)
block|{
return|return
name|this
operator|.
name|member
operator|!=
literal|null
condition|?
name|this
operator|.
name|member
operator|.
name|equals
argument_list|(
name|member
argument_list|)
else|:
literal|false
return|;
block|}
DECL|method|hasJoined ()
name|boolean
name|hasJoined
parameter_list|()
block|{
return|return
name|member
operator|!=
literal|null
return|;
block|}
DECL|method|join ()
name|AtomixLocalMember
name|join
parameter_list|()
throws|throws
name|ExecutionException
throws|,
name|InterruptedException
block|{
if|if
condition|(
name|member
operator|==
literal|null
operator|&&
name|group
operator|!=
literal|null
condition|)
block|{
name|String
name|id
init|=
name|getClusterService
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|id
argument_list|)
operator|||
name|configuration
operator|.
name|isEphemeral
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Joining group: {} "
argument_list|,
name|group
argument_list|)
expr_stmt|;
name|member
operator|=
name|group
operator|.
name|join
argument_list|()
operator|.
name|join
argument_list|()
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Group {} joined with id {}"
argument_list|,
name|group
argument_list|,
name|member
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Joining group: {}, with id: {}"
argument_list|,
name|group
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|member
operator|=
name|group
operator|.
name|join
argument_list|(
name|id
argument_list|)
operator|.
name|join
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|this
return|;
block|}
DECL|method|leave ()
name|AtomixLocalMember
name|leave
parameter_list|()
block|{
if|if
condition|(
name|member
operator|!=
literal|null
condition|)
block|{
name|String
name|id
init|=
name|member
operator|.
name|id
argument_list|()
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Member {} : leave group {}"
argument_list|,
name|id
argument_list|,
name|group
argument_list|)
expr_stmt|;
name|member
operator|.
name|leave
argument_list|()
operator|.
name|join
argument_list|()
expr_stmt|;
name|group
operator|.
name|remove
argument_list|(
name|id
argument_list|)
operator|.
name|join
argument_list|()
expr_stmt|;
name|member
operator|=
literal|null
expr_stmt|;
name|fireLeadershipChangedEvent
argument_list|(
name|Optional
operator|.
name|empty
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"AtomixLocalMember{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"member="
argument_list|)
operator|.
name|append
argument_list|(
name|member
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|class|AtomixClusterMember
specifier|final
class|class
name|AtomixClusterMember
implements|implements
name|CamelClusterMember
block|{
DECL|field|member
specifier|private
specifier|final
name|GroupMember
name|member
decl_stmt|;
DECL|method|AtomixClusterMember (GroupMember member)
name|AtomixClusterMember
parameter_list|(
name|GroupMember
name|member
parameter_list|)
block|{
name|this
operator|.
name|member
operator|=
name|member
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
name|member
operator|.
name|id
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isLeader ()
specifier|public
name|boolean
name|isLeader
parameter_list|()
block|{
if|if
condition|(
name|group
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|member
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|member
operator|.
name|equals
argument_list|(
name|group
operator|.
name|election
argument_list|()
operator|.
name|term
argument_list|()
operator|.
name|leader
argument_list|()
argument_list|)
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
name|localMember
operator|!=
literal|null
condition|?
name|localMember
operator|.
name|is
argument_list|(
name|member
argument_list|)
else|:
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"AtomixClusterMember{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"group="
argument_list|)
operator|.
name|append
argument_list|(
name|group
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", member="
argument_list|)
operator|.
name|append
argument_list|(
name|member
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

