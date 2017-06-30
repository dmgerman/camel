begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.ha.lock
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
operator|.
name|lock
package|;
end_package

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
name|LinkedList
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
name|Set
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
name|ScheduledExecutorService
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
name|TimeUnit
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
name|api
operator|.
name|model
operator|.
name|Pod
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
name|io
operator|.
name|fabric8
operator|.
name|kubernetes
operator|.
name|client
operator|.
name|KubernetesClientException
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
name|Watch
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
name|Watcher
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
name|Service
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

begin_comment
comment|/**  * Monitors the list of participants in a leader election and provides the most recently updated list.  * It calls the callback eventHandlers only when the member set changes w.r.t. the previous invocation.  */
end_comment

begin_class
DECL|class|KubernetesMembersMonitor
class|class
name|KubernetesMembersMonitor
implements|implements
name|Service
block|{
DECL|field|DEFAULT_WATCHER_REFRESH_INTERVAL_SECONDS
specifier|private
specifier|static
specifier|final
name|long
name|DEFAULT_WATCHER_REFRESH_INTERVAL_SECONDS
init|=
literal|1800
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|KubernetesMembersMonitor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|serializedExecutor
specifier|private
name|ScheduledExecutorService
name|serializedExecutor
decl_stmt|;
DECL|field|kubernetesClient
specifier|private
name|KubernetesClient
name|kubernetesClient
decl_stmt|;
DECL|field|lockConfiguration
specifier|private
name|KubernetesLockConfiguration
name|lockConfiguration
decl_stmt|;
DECL|field|eventHandlers
specifier|private
name|List
argument_list|<
name|KubernetesClusterEventHandler
argument_list|>
name|eventHandlers
decl_stmt|;
DECL|field|watch
specifier|private
name|Watch
name|watch
decl_stmt|;
DECL|field|terminated
specifier|private
name|boolean
name|terminated
decl_stmt|;
DECL|field|refreshing
specifier|private
name|boolean
name|refreshing
decl_stmt|;
DECL|field|previousMembers
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|previousMembers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|basePoll
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|basePoll
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|deleted
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|deleted
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|added
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|added
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|KubernetesMembersMonitor (ScheduledExecutorService serializedExecutor, KubernetesClient kubernetesClient, KubernetesLockConfiguration lockConfiguration)
specifier|public
name|KubernetesMembersMonitor
parameter_list|(
name|ScheduledExecutorService
name|serializedExecutor
parameter_list|,
name|KubernetesClient
name|kubernetesClient
parameter_list|,
name|KubernetesLockConfiguration
name|lockConfiguration
parameter_list|)
block|{
name|this
operator|.
name|serializedExecutor
operator|=
name|serializedExecutor
expr_stmt|;
name|this
operator|.
name|kubernetesClient
operator|=
name|kubernetesClient
expr_stmt|;
name|this
operator|.
name|lockConfiguration
operator|=
name|lockConfiguration
expr_stmt|;
name|this
operator|.
name|eventHandlers
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|addClusterEventHandler (KubernetesClusterEventHandler eventHandler)
specifier|public
name|void
name|addClusterEventHandler
parameter_list|(
name|KubernetesClusterEventHandler
name|eventHandler
parameter_list|)
block|{
name|this
operator|.
name|eventHandlers
operator|.
name|add
argument_list|(
name|eventHandler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|serializedExecutor
operator|.
name|execute
argument_list|(
parameter_list|()
lambda|->
name|doPoll
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|serializedExecutor
operator|.
name|execute
argument_list|(
name|this
operator|::
name|createWatch
argument_list|)
expr_stmt|;
name|long
name|recreationDelay
init|=
name|lockConfiguration
operator|.
name|getWatchRefreshIntervalSecondsOrDefault
argument_list|()
decl_stmt|;
if|if
condition|(
name|recreationDelay
operator|>
literal|0
condition|)
block|{
name|serializedExecutor
operator|.
name|scheduleWithFixedDelay
argument_list|(
name|this
operator|::
name|refresh
argument_list|,
name|recreationDelay
argument_list|,
name|recreationDelay
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createWatch ()
specifier|private
name|void
name|createWatch
parameter_list|()
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting cluster members watcher"
argument_list|)
expr_stmt|;
name|this
operator|.
name|watch
operator|=
name|kubernetesClient
operator|.
name|pods
argument_list|()
operator|.
name|inNamespace
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getKubernetesResourcesNamespaceOrDefault
argument_list|(
name|kubernetesClient
argument_list|)
argument_list|)
operator|.
name|withLabels
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getClusterLabels
argument_list|()
argument_list|)
operator|.
name|watch
argument_list|(
operator|new
name|Watcher
argument_list|<
name|Pod
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|eventReceived
parameter_list|(
name|Action
name|action
parameter_list|,
name|Pod
name|pod
parameter_list|)
block|{
switch|switch
condition|(
name|action
condition|)
block|{
case|case
name|DELETED
case|:
name|serializedExecutor
operator|.
name|execute
argument_list|(
parameter_list|()
lambda|->
name|deleteAndNotify
argument_list|(
name|podName
argument_list|(
name|pod
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|ADDED
case|:
name|serializedExecutor
operator|.
name|execute
argument_list|(
parameter_list|()
lambda|->
name|addAndNotify
argument_list|(
name|podName
argument_list|(
name|pod
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
break|break;
default|default:
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onClose
parameter_list|(
name|KubernetesClientException
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|terminated
condition|)
block|{
name|KubernetesMembersMonitor
operator|.
name|this
operator|.
name|watch
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|refreshing
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Refreshing pod list watcher..."
argument_list|)
expr_stmt|;
name|serializedExecutor
operator|.
name|execute
argument_list|(
name|KubernetesMembersMonitor
operator|.
name|this
operator|::
name|createWatch
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Pod list watcher has been closed unexpectedly. Recreating it in 1 second..."
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|serializedExecutor
operator|.
name|schedule
argument_list|(
name|KubernetesMembersMonitor
operator|.
name|this
operator|::
name|createWatch
argument_list|,
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unable to watch for pod list changes. Retrying in 5 seconds..."
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error while trying to watch the pod list"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|serializedExecutor
operator|.
name|schedule
argument_list|(
name|this
operator|::
name|createWatch
argument_list|,
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|terminated
operator|=
literal|true
expr_stmt|;
name|Watch
name|watch
init|=
name|this
operator|.
name|watch
decl_stmt|;
if|if
condition|(
name|watch
operator|!=
literal|null
condition|)
block|{
name|watch
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|refresh ()
specifier|public
name|void
name|refresh
parameter_list|()
block|{
name|serializedExecutor
operator|.
name|execute
argument_list|(
parameter_list|()
lambda|->
block|{
if|if
condition|(
operator|!
name|terminated
condition|)
block|{
name|refreshing
operator|=
literal|true
expr_stmt|;
try|try
block|{
name|doPoll
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Watch
name|w
init|=
name|this
operator|.
name|watch
decl_stmt|;
if|if
condition|(
name|w
operator|!=
literal|null
condition|)
block|{
comment|// It will be recreated
name|w
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|refreshing
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|doPoll (boolean retry)
specifier|private
name|void
name|doPoll
parameter_list|(
name|boolean
name|retry
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting poll to get all cluster members"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Pod
argument_list|>
name|pods
decl_stmt|;
try|try
block|{
name|pods
operator|=
name|pollPods
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
if|if
condition|(
name|retry
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Pods poll failed. Retrying in 5 seconds..."
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|this
operator|.
name|serializedExecutor
operator|.
name|schedule
argument_list|(
parameter_list|()
lambda|->
name|doPoll
argument_list|(
literal|true
argument_list|)
argument_list|,
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Pods poll failed"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
name|this
operator|.
name|basePoll
operator|=
name|pods
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|p
lambda|->
name|Optional
operator|.
name|ofNullable
argument_list|(
name|podName
argument_list|(
name|p
argument_list|)
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|Optional
operator|::
name|isPresent
argument_list|)
operator|.
name|map
argument_list|(
name|Optional
operator|::
name|get
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|added
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|deleted
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Base list of members is {}"
argument_list|,
name|this
operator|.
name|basePoll
argument_list|)
expr_stmt|;
name|checkAndNotify
argument_list|()
expr_stmt|;
block|}
DECL|method|pollPods ()
specifier|private
name|List
argument_list|<
name|Pod
argument_list|>
name|pollPods
parameter_list|()
block|{
return|return
name|kubernetesClient
operator|.
name|pods
argument_list|()
operator|.
name|inNamespace
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getKubernetesResourcesNamespaceOrDefault
argument_list|(
name|kubernetesClient
argument_list|)
argument_list|)
operator|.
name|withLabels
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getClusterLabels
argument_list|()
argument_list|)
operator|.
name|list
argument_list|()
operator|.
name|getItems
argument_list|()
return|;
block|}
DECL|method|podName (Pod pod)
specifier|private
name|String
name|podName
parameter_list|(
name|Pod
name|pod
parameter_list|)
block|{
if|if
condition|(
name|pod
operator|!=
literal|null
operator|&&
name|pod
operator|.
name|getMetadata
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|pod
operator|.
name|getMetadata
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|checkAndNotify ()
specifier|private
name|void
name|checkAndNotify
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|newMembers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|basePoll
argument_list|)
decl_stmt|;
name|newMembers
operator|.
name|addAll
argument_list|(
name|added
argument_list|)
expr_stmt|;
name|newMembers
operator|.
name|removeAll
argument_list|(
name|deleted
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Current list of members is: {}"
argument_list|,
name|newMembers
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|newMembers
operator|.
name|equals
argument_list|(
name|this
operator|.
name|previousMembers
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"List of members changed: sending notifications"
argument_list|)
expr_stmt|;
name|this
operator|.
name|previousMembers
operator|=
name|newMembers
expr_stmt|;
for|for
control|(
name|KubernetesClusterEventHandler
name|eventHandler
range|:
name|eventHandlers
control|)
block|{
name|eventHandler
operator|.
name|onKubernetesClusterEvent
argument_list|(
call|(
name|KubernetesClusterEvent
operator|.
name|KubernetesClusterMemberListChangedEvent
call|)
argument_list|()
operator|->
name|newMembers
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"List of members has not changed"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addAndNotify (String member)
specifier|private
name|void
name|addAndNotify
parameter_list|(
name|String
name|member
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding new member to the list: {}"
argument_list|,
name|member
argument_list|)
expr_stmt|;
if|if
condition|(
name|member
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|added
operator|.
name|add
argument_list|(
name|member
argument_list|)
expr_stmt|;
name|checkAndNotify
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|deleteAndNotify (String member)
specifier|private
name|void
name|deleteAndNotify
parameter_list|(
name|String
name|member
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Deleting member to the list: {}"
argument_list|,
name|member
argument_list|)
expr_stmt|;
if|if
condition|(
name|member
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|deleted
operator|.
name|add
argument_list|(
name|member
argument_list|)
expr_stmt|;
name|checkAndNotify
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

