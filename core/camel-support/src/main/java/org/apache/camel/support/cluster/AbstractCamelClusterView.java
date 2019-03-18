begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.cluster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|locks
operator|.
name|StampedLock
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
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
name|cluster
operator|.
name|CamelClusterEventListener
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
name|cluster
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
name|support
operator|.
name|service
operator|.
name|ServiceSupport
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
name|concurrent
operator|.
name|LockHelper
import|;
end_import

begin_class
DECL|class|AbstractCamelClusterView
specifier|public
specifier|abstract
class|class
name|AbstractCamelClusterView
extends|extends
name|ServiceSupport
implements|implements
name|CamelClusterView
block|{
DECL|field|clusterService
specifier|private
specifier|final
name|CamelClusterService
name|clusterService
decl_stmt|;
DECL|field|namespace
specifier|private
specifier|final
name|String
name|namespace
decl_stmt|;
DECL|field|listeners
specifier|private
specifier|final
name|List
argument_list|<
name|CamelClusterEventListener
argument_list|>
name|listeners
decl_stmt|;
DECL|field|lock
specifier|private
specifier|final
name|StampedLock
name|lock
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|AbstractCamelClusterView (CamelClusterService cluster, String namespace)
specifier|protected
name|AbstractCamelClusterView
parameter_list|(
name|CamelClusterService
name|cluster
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|clusterService
operator|=
name|cluster
expr_stmt|;
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
name|this
operator|.
name|listeners
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|lock
operator|=
operator|new
name|StampedLock
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|getClusterService ()
specifier|public
name|CamelClusterService
name|getClusterService
parameter_list|()
block|{
return|return
name|this
operator|.
name|clusterService
return|;
block|}
annotation|@
name|Override
DECL|method|getNamespace ()
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|this
operator|.
name|namespace
return|;
block|}
annotation|@
name|Override
DECL|method|addEventListener (CamelClusterEventListener listener)
specifier|public
name|void
name|addEventListener
parameter_list|(
name|CamelClusterEventListener
name|listener
parameter_list|)
block|{
if|if
condition|(
name|listener
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|LockHelper
operator|.
name|doWithWriteLock
argument_list|(
name|lock
argument_list|,
parameter_list|()
lambda|->
block|{
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
if|if
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
comment|// if the view has already been started, fire known events so
comment|// the consumer can catch up.
if|if
condition|(
name|CamelClusterEventListener
operator|.
name|Leadership
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|listener
argument_list|)
condition|)
block|{
name|CamelClusterEventListener
operator|.
name|Leadership
operator|.
name|class
operator|.
name|cast
argument_list|(
name|listener
argument_list|)
operator|.
name|leadershipChanged
argument_list|(
name|this
argument_list|,
name|getLeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|CamelClusterEventListener
operator|.
name|Membership
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|listener
argument_list|)
condition|)
block|{
name|CamelClusterEventListener
operator|.
name|Membership
name|ml
init|=
name|CamelClusterEventListener
operator|.
name|Membership
operator|.
name|class
operator|.
name|cast
argument_list|(
name|listener
argument_list|)
decl_stmt|;
for|for
control|(
name|CamelClusterMember
name|member
range|:
name|getMembers
argument_list|()
control|)
block|{
name|ml
operator|.
name|memberAdded
argument_list|(
name|this
argument_list|,
name|member
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removeEventListener (CamelClusterEventListener listener)
specifier|public
name|void
name|removeEventListener
parameter_list|(
name|CamelClusterEventListener
name|listener
parameter_list|)
block|{
if|if
condition|(
name|listener
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|LockHelper
operator|.
name|doWithWriteLock
argument_list|(
name|lock
argument_list|,
parameter_list|()
lambda|->
name|listeners
operator|.
name|removeIf
argument_list|(
name|l
lambda|->
name|l
operator|==
name|listener
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// **************************************
comment|// Events
comment|// **************************************
DECL|method|doWithListener (Class<T> type, Consumer<T> consumer)
specifier|private
parameter_list|<
name|T
extends|extends
name|CamelClusterEventListener
parameter_list|>
name|void
name|doWithListener
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Consumer
argument_list|<
name|T
argument_list|>
name|consumer
parameter_list|)
block|{
name|LockHelper
operator|.
name|doWithReadLock
argument_list|(
name|lock
argument_list|,
parameter_list|()
lambda|->
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|listeners
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|CamelClusterEventListener
name|listener
init|=
name|listeners
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|listener
argument_list|)
condition|)
block|{
name|consumer
operator|.
name|accept
argument_list|(
name|type
operator|.
name|cast
argument_list|(
name|listener
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
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
name|doWithListener
argument_list|(
name|CamelClusterEventListener
operator|.
name|Leadership
operator|.
name|class
argument_list|,
name|listener
lambda|->
name|listener
operator|.
name|leadershipChanged
argument_list|(
name|this
argument_list|,
name|leader
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|fireMemberAddedEvent (CamelClusterMember member)
specifier|protected
name|void
name|fireMemberAddedEvent
parameter_list|(
name|CamelClusterMember
name|member
parameter_list|)
block|{
name|doWithListener
argument_list|(
name|CamelClusterEventListener
operator|.
name|Membership
operator|.
name|class
argument_list|,
name|listener
lambda|->
name|listener
operator|.
name|memberAdded
argument_list|(
name|this
argument_list|,
name|member
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|fireMemberRemovedEvent (CamelClusterMember member)
specifier|protected
name|void
name|fireMemberRemovedEvent
parameter_list|(
name|CamelClusterMember
name|member
parameter_list|)
block|{
name|doWithListener
argument_list|(
name|CamelClusterEventListener
operator|.
name|Membership
operator|.
name|class
argument_list|,
name|listener
lambda|->
name|listener
operator|.
name|memberRemoved
argument_list|(
name|this
argument_list|,
name|member
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

