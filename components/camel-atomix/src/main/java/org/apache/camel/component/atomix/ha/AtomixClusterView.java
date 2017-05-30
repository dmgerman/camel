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
name|List
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
name|CamelClusterView
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
specifier|public
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
DECL|field|group
specifier|private
specifier|final
name|DistributedGroup
name|group
decl_stmt|;
DECL|field|localMember
specifier|private
specifier|final
name|AtomixLocalMember
name|localMember
decl_stmt|;
DECL|method|AtomixClusterView (AtomixCluster cluster, String namespace, DistributedGroup group)
name|AtomixClusterView
parameter_list|(
name|AtomixCluster
name|cluster
parameter_list|,
name|String
name|namespace
parameter_list|,
name|DistributedGroup
name|group
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
name|group
operator|=
name|group
expr_stmt|;
name|this
operator|.
name|localMember
operator|=
operator|new
name|AtomixLocalMember
argument_list|(
name|group
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMaster ()
specifier|public
name|CamelClusterMember
name|getMaster
parameter_list|()
block|{
return|return
name|asCamelClusterMember
argument_list|(
name|this
operator|.
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
DECL|method|getMembers ()
specifier|public
name|List
argument_list|<
name|CamelClusterMember
argument_list|>
name|getMembers
parameter_list|()
block|{
comment|// TODO: Dummy implementation for testing purpose
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
name|this
operator|::
name|asCamelClusterMember
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
DECL|method|asCamelClusterMember (GroupMember member)
specifier|private
name|AtomixClusterMember
name|asCamelClusterMember
parameter_list|(
name|GroupMember
name|member
parameter_list|)
block|{
return|return
operator|new
name|AtomixClusterMember
argument_list|(
name|group
argument_list|,
name|member
argument_list|)
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
operator|!
name|this
operator|.
name|localMember
operator|.
name|hasJoined
argument_list|()
condition|)
block|{
name|this
operator|.
name|localMember
operator|.
name|join
argument_list|()
expr_stmt|;
name|this
operator|.
name|group
operator|.
name|election
argument_list|()
operator|.
name|onElection
argument_list|(
name|t
lambda|->
name|fireEvent
argument_list|(
name|CamelClusterView
operator|.
name|Event
operator|.
name|LEADERSHIP_CHANGED
argument_list|,
name|asCamelClusterMember
argument_list|(
name|t
operator|.
name|leader
argument_list|()
argument_list|)
argument_list|)
argument_list|)
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
comment|// ***********************************************
comment|//
comment|// ***********************************************
DECL|class|AtomixLocalMember
class|class
name|AtomixLocalMember
implements|implements
name|CamelClusterMember
block|{
DECL|field|group
specifier|private
specifier|final
name|DistributedGroup
name|group
decl_stmt|;
DECL|field|member
specifier|private
name|LocalMember
name|member
decl_stmt|;
DECL|method|AtomixLocalMember (DistributedGroup group)
name|AtomixLocalMember
parameter_list|(
name|DistributedGroup
name|group
parameter_list|)
block|{
name|this
operator|.
name|group
operator|=
name|group
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
literal|"Cluster not yet joined"
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
DECL|method|isMaster ()
specifier|public
name|boolean
name|isMaster
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
operator|.
name|equals
argument_list|(
name|member
argument_list|)
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
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Joining group {}"
argument_list|,
name|group
argument_list|)
expr_stmt|;
name|member
operator|=
name|this
operator|.
name|group
operator|.
name|join
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
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
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Leaving group {}"
argument_list|,
name|group
argument_list|)
expr_stmt|;
name|member
operator|.
name|leave
argument_list|()
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|get ()
name|LocalMember
name|get
parameter_list|()
block|{
return|return
name|member
return|;
block|}
block|}
DECL|class|AtomixClusterMember
class|class
name|AtomixClusterMember
implements|implements
name|CamelClusterMember
block|{
DECL|field|group
specifier|private
specifier|final
name|DistributedGroup
name|group
decl_stmt|;
DECL|field|member
specifier|private
specifier|final
name|GroupMember
name|member
decl_stmt|;
DECL|method|AtomixClusterMember (DistributedGroup group, GroupMember member)
name|AtomixClusterMember
parameter_list|(
name|DistributedGroup
name|group
parameter_list|,
name|GroupMember
name|member
parameter_list|)
block|{
name|this
operator|.
name|group
operator|=
name|group
expr_stmt|;
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
DECL|method|isMaster ()
specifier|public
name|boolean
name|isMaster
parameter_list|()
block|{
return|return
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
operator|.
name|equals
argument_list|(
name|member
argument_list|)
return|;
block|}
DECL|method|get ()
name|GroupMember
name|get
parameter_list|()
block|{
return|return
name|member
return|;
block|}
block|}
block|}
end_class

end_unit

