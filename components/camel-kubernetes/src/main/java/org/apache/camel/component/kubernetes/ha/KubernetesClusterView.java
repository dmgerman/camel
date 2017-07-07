begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.ha
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kubernetes
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
name|HashMap
import|;
end_import

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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Set
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
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|KubernetesClient
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
name|kubernetes
operator|.
name|KubernetesConfiguration
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
name|kubernetes
operator|.
name|KubernetesHelper
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
name|kubernetes
operator|.
name|ha
operator|.
name|lock
operator|.
name|KubernetesClusterEvent
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
name|kubernetes
operator|.
name|ha
operator|.
name|lock
operator|.
name|KubernetesLeaseBasedLeadershipController
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
name|kubernetes
operator|.
name|ha
operator|.
name|lock
operator|.
name|KubernetesLockConfiguration
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

begin_comment
comment|/**  * The cluster view on a specific Camel cluster namespace (not to be confused with Kubernetes namespaces).  * Namespaces are represented as keys in a Kubernetes ConfigMap (values are the current leader pods).  */
end_comment

begin_class
DECL|class|KubernetesClusterView
specifier|public
class|class
name|KubernetesClusterView
extends|extends
name|AbstractCamelClusterView
block|{
DECL|field|kubernetesClient
specifier|private
name|KubernetesClient
name|kubernetesClient
decl_stmt|;
DECL|field|configuration
specifier|private
name|KubernetesConfiguration
name|configuration
decl_stmt|;
DECL|field|lockConfiguration
specifier|private
name|KubernetesLockConfiguration
name|lockConfiguration
decl_stmt|;
DECL|field|localMember
specifier|private
name|KubernetesClusterMember
name|localMember
decl_stmt|;
DECL|field|memberCache
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|KubernetesClusterMember
argument_list|>
name|memberCache
decl_stmt|;
DECL|field|currentLeader
specifier|private
specifier|volatile
name|Optional
argument_list|<
name|CamelClusterMember
argument_list|>
name|currentLeader
init|=
name|Optional
operator|.
name|empty
argument_list|()
decl_stmt|;
DECL|field|currentMembers
specifier|private
specifier|volatile
name|List
argument_list|<
name|CamelClusterMember
argument_list|>
name|currentMembers
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
DECL|field|controller
specifier|private
name|KubernetesLeaseBasedLeadershipController
name|controller
decl_stmt|;
DECL|method|KubernetesClusterView (KubernetesClusterService cluster, KubernetesConfiguration configuration, KubernetesLockConfiguration lockConfiguration)
specifier|public
name|KubernetesClusterView
parameter_list|(
name|KubernetesClusterService
name|cluster
parameter_list|,
name|KubernetesConfiguration
name|configuration
parameter_list|,
name|KubernetesLockConfiguration
name|lockConfiguration
parameter_list|)
block|{
name|super
argument_list|(
name|cluster
argument_list|,
name|lockConfiguration
operator|.
name|getGroupName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|lockConfiguration
operator|=
name|lockConfiguration
expr_stmt|;
name|this
operator|.
name|localMember
operator|=
operator|new
name|KubernetesClusterMember
argument_list|(
name|lockConfiguration
operator|.
name|getPodName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|memberCache
operator|=
operator|new
name|HashMap
argument_list|<>
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
return|return
name|currentLeader
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
return|return
name|currentMembers
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
name|controller
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|kubernetesClient
operator|=
name|KubernetesHelper
operator|.
name|getKubernetesClient
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|controller
operator|=
operator|new
name|KubernetesLeaseBasedLeadershipController
argument_list|(
name|kubernetesClient
argument_list|,
name|this
operator|.
name|lockConfiguration
argument_list|,
name|event
lambda|->
block|{
if|if
condition|(
name|event
operator|instanceof
name|KubernetesClusterEvent
operator|.
name|KubernetesClusterLeaderChangedEvent
condition|)
block|{
comment|// New leader
name|Optional
argument_list|<
name|String
argument_list|>
name|leader
init|=
name|KubernetesClusterEvent
operator|.
name|KubernetesClusterLeaderChangedEvent
operator|.
name|class
operator|.
name|cast
argument_list|(
name|event
argument_list|)
operator|.
name|getData
argument_list|()
decl_stmt|;
name|currentLeader
operator|=
name|leader
operator|.
name|map
argument_list|(
name|this
operator|::
name|toMember
argument_list|)
expr_stmt|;
name|fireLeadershipChangedEvent
argument_list|(
name|currentLeader
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|KubernetesClusterEvent
operator|.
name|KubernetesClusterMemberListChangedEvent
condition|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|members
init|=
name|KubernetesClusterEvent
operator|.
name|KubernetesClusterMemberListChangedEvent
operator|.
name|class
operator|.
name|cast
argument_list|(
name|event
argument_list|)
operator|.
name|getData
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|oldMembers
init|=
name|currentMembers
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|CamelClusterMember
operator|::
name|getId
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
decl_stmt|;
name|currentMembers
operator|=
name|members
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|this
operator|::
name|toMember
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
comment|// Computing differences
name|Set
argument_list|<
name|String
argument_list|>
name|added
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|members
argument_list|)
decl_stmt|;
name|added
operator|.
name|removeAll
argument_list|(
name|oldMembers
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|removed
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|oldMembers
argument_list|)
decl_stmt|;
name|removed
operator|.
name|removeAll
argument_list|(
name|members
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|id
range|:
name|added
control|)
block|{
name|fireMemberAddedEvent
argument_list|(
name|toMember
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|id
range|:
name|removed
control|)
block|{
name|fireMemberRemovedEvent
argument_list|(
name|toMember
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|controller
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
name|controller
operator|!=
literal|null
condition|)
block|{
name|controller
operator|.
name|stop
argument_list|()
expr_stmt|;
name|controller
operator|=
literal|null
expr_stmt|;
name|kubernetesClient
operator|.
name|close
argument_list|()
expr_stmt|;
name|kubernetesClient
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|toMember (String name)
specifier|protected
name|KubernetesClusterMember
name|toMember
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|localMember
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|localMember
return|;
block|}
return|return
name|memberCache
operator|.
name|computeIfAbsent
argument_list|(
name|name
argument_list|,
name|KubernetesClusterMember
operator|::
operator|new
argument_list|)
return|;
block|}
DECL|class|KubernetesClusterMember
class|class
name|KubernetesClusterMember
implements|implements
name|CamelClusterMember
block|{
DECL|field|podName
specifier|private
name|String
name|podName
decl_stmt|;
DECL|method|KubernetesClusterMember (String podName)
specifier|public
name|KubernetesClusterMember
parameter_list|(
name|String
name|podName
parameter_list|)
block|{
name|this
operator|.
name|podName
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|podName
argument_list|,
literal|"podName"
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
name|currentLeader
operator|.
name|isPresent
argument_list|()
operator|&&
name|currentLeader
operator|.
name|get
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|podName
argument_list|)
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
name|podName
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
literal|"KubernetesClusterMember{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"podName='"
argument_list|)
operator|.
name|append
argument_list|(
name|podName
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
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

