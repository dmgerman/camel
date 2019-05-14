begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.cluster.lock
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
name|cluster
operator|.
name|lock
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|Objects
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
name|ConfigMap
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
comment|/**  * Monitors current status and participate to leader election when no active leaders are present.  * It communicates changes in leadership and cluster members to the given event handler.  */
end_comment

begin_class
DECL|class|KubernetesLeadershipController
specifier|public
class|class
name|KubernetesLeadershipController
implements|implements
name|Service
block|{
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
name|KubernetesLeadershipController
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|enum|State
specifier|private
enum|enum
name|State
block|{
DECL|enumConstant|NOT_LEADER
name|NOT_LEADER
block|,
DECL|enumConstant|BECOMING_LEADER
name|BECOMING_LEADER
block|,
DECL|enumConstant|LEADER
name|LEADER
block|}
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
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
DECL|field|eventHandler
specifier|private
name|KubernetesClusterEventHandler
name|eventHandler
decl_stmt|;
DECL|field|currentState
specifier|private
name|State
name|currentState
init|=
name|State
operator|.
name|NOT_LEADER
decl_stmt|;
DECL|field|serializedExecutor
specifier|private
name|ScheduledExecutorService
name|serializedExecutor
decl_stmt|;
DECL|field|leaderNotifier
specifier|private
name|TimedLeaderNotifier
name|leaderNotifier
decl_stmt|;
DECL|field|latestLeaderInfo
specifier|private
specifier|volatile
name|LeaderInfo
name|latestLeaderInfo
decl_stmt|;
DECL|field|latestConfigMap
specifier|private
specifier|volatile
name|ConfigMap
name|latestConfigMap
decl_stmt|;
DECL|field|latestMembers
specifier|private
specifier|volatile
name|Set
argument_list|<
name|String
argument_list|>
name|latestMembers
decl_stmt|;
DECL|method|KubernetesLeadershipController (CamelContext camelContext, KubernetesClient kubernetesClient, KubernetesLockConfiguration lockConfiguration, KubernetesClusterEventHandler eventHandler)
specifier|public
name|KubernetesLeadershipController
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|KubernetesClient
name|kubernetesClient
parameter_list|,
name|KubernetesLockConfiguration
name|lockConfiguration
parameter_list|,
name|KubernetesClusterEventHandler
name|eventHandler
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
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
name|eventHandler
operator|=
name|eventHandler
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
if|if
condition|(
name|serializedExecutor
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} Starting leadership controller..."
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|serializedExecutor
operator|=
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadScheduledExecutor
argument_list|(
name|this
argument_list|,
literal|"CamelKubernetesLeadershipController"
argument_list|)
expr_stmt|;
name|leaderNotifier
operator|=
operator|new
name|TimedLeaderNotifier
argument_list|(
name|this
operator|.
name|camelContext
argument_list|,
name|this
operator|.
name|eventHandler
argument_list|)
expr_stmt|;
name|leaderNotifier
operator|.
name|start
argument_list|()
expr_stmt|;
name|serializedExecutor
operator|.
name|execute
argument_list|(
name|this
operator|::
name|refreshStatus
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
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} Stopping leadership controller..."
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|serializedExecutor
operator|!=
literal|null
condition|)
block|{
name|serializedExecutor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
name|serializedExecutor
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|leaderNotifier
operator|!=
literal|null
condition|)
block|{
name|leaderNotifier
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|leaderNotifier
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|refreshStatus ()
specifier|private
name|void
name|refreshStatus
parameter_list|()
block|{
switch|switch
condition|(
name|currentState
condition|)
block|{
case|case
name|NOT_LEADER
case|:
name|refreshStatusNotLeader
argument_list|()
expr_stmt|;
break|break;
case|case
name|BECOMING_LEADER
case|:
name|refreshStatusBecomingLeader
argument_list|()
expr_stmt|;
break|break;
case|case
name|LEADER
case|:
name|refreshStatusLeader
argument_list|()
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unsupported state "
operator|+
name|currentState
argument_list|)
throw|;
block|}
block|}
comment|/**      * This pod is currently not leader. It should monitor the leader configuration and try      * to acquire the leadership if possible.      */
DECL|method|refreshStatusNotLeader ()
specifier|private
name|void
name|refreshStatusNotLeader
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} Pod is not leader, pulling new data from the cluster"
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|pulled
init|=
name|lookupNewLeaderInfo
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|pulled
condition|)
block|{
name|rescheduleAfterDelay
argument_list|()
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|this
operator|.
name|latestLeaderInfo
operator|.
name|hasEmptyLeader
argument_list|()
condition|)
block|{
comment|// There is no previous leader
name|LOG
operator|.
name|info
argument_list|(
literal|"{} The cluster has no leaders. Trying to acquire the leadership..."
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|acquired
init|=
name|tryAcquireLeadership
argument_list|()
decl_stmt|;
if|if
condition|(
name|acquired
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"{} Leadership acquired by current pod with immediate effect"
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|currentState
operator|=
name|State
operator|.
name|LEADER
expr_stmt|;
name|this
operator|.
name|serializedExecutor
operator|.
name|execute
argument_list|(
name|this
operator|::
name|refreshStatus
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"{} Unable to acquire the leadership, it may have been acquired by another pod"
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|this
operator|.
name|latestLeaderInfo
operator|.
name|hasValidLeader
argument_list|()
condition|)
block|{
comment|// There's a previous leader and it's invalid
name|LOG
operator|.
name|info
argument_list|(
literal|"{} Leadership has been lost by old owner. Trying to acquire the leadership..."
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|acquired
init|=
name|tryAcquireLeadership
argument_list|()
decl_stmt|;
if|if
condition|(
name|acquired
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"{} Leadership acquired by current pod"
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|currentState
operator|=
name|State
operator|.
name|BECOMING_LEADER
expr_stmt|;
name|this
operator|.
name|serializedExecutor
operator|.
name|execute
argument_list|(
name|this
operator|::
name|refreshStatus
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"{} Unable to acquire the leadership, it may have been acquired by another pod"
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|this
operator|.
name|latestLeaderInfo
operator|.
name|isValidLeader
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getPodName
argument_list|()
argument_list|)
condition|)
block|{
comment|// We are leaders for some reason (e.g. pod restart on failure)
name|LOG
operator|.
name|info
argument_list|(
literal|"{} Leadership is already owned by current pod"
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|currentState
operator|=
name|State
operator|.
name|BECOMING_LEADER
expr_stmt|;
name|this
operator|.
name|serializedExecutor
operator|.
name|execute
argument_list|(
name|this
operator|::
name|refreshStatus
argument_list|)
expr_stmt|;
return|return;
block|}
name|this
operator|.
name|leaderNotifier
operator|.
name|refreshLeadership
argument_list|(
name|Optional
operator|.
name|ofNullable
argument_list|(
name|this
operator|.
name|latestLeaderInfo
operator|.
name|getLeader
argument_list|()
argument_list|)
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getLeaseDurationMillis
argument_list|()
argument_list|,
name|this
operator|.
name|latestLeaderInfo
operator|.
name|getMembers
argument_list|()
argument_list|)
expr_stmt|;
name|rescheduleAfterDelay
argument_list|()
expr_stmt|;
block|}
comment|/**      * This pod has acquired the leadership but it should wait for the old leader      * to tear down resources before starting the local services.      */
DECL|method|refreshStatusBecomingLeader ()
specifier|private
name|void
name|refreshStatusBecomingLeader
parameter_list|()
block|{
comment|// Wait always the same amount of time before becoming the leader
comment|// Even if the current pod is already leader, we should let a possible old version of the pod to shut down
name|long
name|delay
init|=
name|this
operator|.
name|lockConfiguration
operator|.
name|getLeaseDurationMillis
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"{} Current pod owns the leadership, but it will be effective in {} seconds..."
argument_list|,
name|logPrefix
argument_list|()
argument_list|,
operator|new
name|BigDecimal
argument_list|(
name|delay
argument_list|)
operator|.
name|divide
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|1000
argument_list|)
argument_list|,
literal|2
argument_list|,
name|BigDecimal
operator|.
name|ROUND_HALF_UP
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Thread interrupted"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"{} Current pod is becoming the new leader now..."
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|currentState
operator|=
name|State
operator|.
name|LEADER
expr_stmt|;
name|this
operator|.
name|serializedExecutor
operator|.
name|execute
argument_list|(
name|this
operator|::
name|refreshStatus
argument_list|)
expr_stmt|;
block|}
DECL|method|refreshStatusLeader ()
specifier|private
name|void
name|refreshStatusLeader
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} Pod should be the leader, pulling new data from the cluster"
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|long
name|timeBeforePulling
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|boolean
name|pulled
init|=
name|lookupNewLeaderInfo
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|pulled
condition|)
block|{
name|rescheduleAfterDelay
argument_list|()
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|this
operator|.
name|latestLeaderInfo
operator|.
name|isValidLeader
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getPodName
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} Current Pod is still the leader"
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|leaderNotifier
operator|.
name|refreshLeadership
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getPodName
argument_list|()
argument_list|)
argument_list|,
name|timeBeforePulling
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getRenewDeadlineMillis
argument_list|()
argument_list|,
name|this
operator|.
name|latestLeaderInfo
operator|.
name|getMembers
argument_list|()
argument_list|)
expr_stmt|;
name|rescheduleAfterDelay
argument_list|()
expr_stmt|;
return|return;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} Current Pod has lost the leadership"
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|currentState
operator|=
name|State
operator|.
name|NOT_LEADER
expr_stmt|;
comment|// set a empty leader to signal leadership loss
name|this
operator|.
name|leaderNotifier
operator|.
name|refreshLeadership
argument_list|(
name|Optional
operator|.
name|empty
argument_list|()
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|,
name|lockConfiguration
operator|.
name|getLeaseDurationMillis
argument_list|()
argument_list|,
name|this
operator|.
name|latestLeaderInfo
operator|.
name|getMembers
argument_list|()
argument_list|)
expr_stmt|;
comment|// restart from scratch to acquire leadership
name|this
operator|.
name|serializedExecutor
operator|.
name|execute
argument_list|(
name|this
operator|::
name|refreshStatus
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|rescheduleAfterDelay ()
specifier|private
name|void
name|rescheduleAfterDelay
parameter_list|()
block|{
name|this
operator|.
name|serializedExecutor
operator|.
name|schedule
argument_list|(
name|this
operator|::
name|refreshStatus
argument_list|,
name|jitter
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getRetryPeriodMillis
argument_list|()
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getJitterFactor
argument_list|()
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
DECL|method|lookupNewLeaderInfo ()
specifier|private
name|boolean
name|lookupNewLeaderInfo
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} Looking up leadership information..."
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|ConfigMap
name|configMap
decl_stmt|;
try|try
block|{
name|configMap
operator|=
name|pullConfigMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|logPrefix
argument_list|()
operator|+
literal|" Unable to retrieve the current ConfigMap "
operator|+
name|this
operator|.
name|lockConfiguration
operator|.
name|getConfigMapName
argument_list|()
operator|+
literal|" from Kubernetes"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|logPrefix
argument_list|()
operator|+
literal|" Exception thrown during ConfigMap lookup"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|members
decl_stmt|;
try|try
block|{
name|members
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|pullClusterMembers
argument_list|()
argument_list|,
literal|"Retrieved a null set of members"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|logPrefix
argument_list|()
operator|+
literal|" Unable to retrieve the list of cluster members from Kubernetes"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|logPrefix
argument_list|()
operator|+
literal|" Exception thrown during Pod list lookup"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|updateLatestLeaderInfo
argument_list|(
name|configMap
argument_list|,
name|members
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|tryAcquireLeadership ()
specifier|private
name|boolean
name|tryAcquireLeadership
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} Trying to acquire the leadership..."
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|ConfigMap
name|configMap
init|=
name|this
operator|.
name|latestConfigMap
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|members
init|=
name|this
operator|.
name|latestMembers
decl_stmt|;
name|LeaderInfo
name|latestLeaderInfo
init|=
name|this
operator|.
name|latestLeaderInfo
decl_stmt|;
if|if
condition|(
name|latestLeaderInfo
operator|==
literal|null
operator|||
name|members
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|logPrefix
argument_list|()
operator|+
literal|" Unexpected condition. Latest leader info or list of members is empty."
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|members
operator|.
name|contains
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getPodName
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|logPrefix
argument_list|()
operator|+
literal|" The list of cluster members "
operator|+
name|latestLeaderInfo
operator|.
name|getMembers
argument_list|()
operator|+
literal|" does not contain the current Pod. Cannot acquire"
operator|+
literal|" leadership."
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// Info we would set set in the configmap to become leaders
name|LeaderInfo
name|newLeaderInfo
init|=
operator|new
name|LeaderInfo
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getGroupName
argument_list|()
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getPodName
argument_list|()
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|,
name|members
argument_list|)
decl_stmt|;
if|if
condition|(
name|configMap
operator|==
literal|null
condition|)
block|{
comment|// No ConfigMap created so far
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} Lock configmap is not present in the Kubernetes namespace. A new ConfigMap will be created"
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|ConfigMap
name|newConfigMap
init|=
name|ConfigMapLockUtils
operator|.
name|createNewConfigMap
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getConfigMapName
argument_list|()
argument_list|,
name|newLeaderInfo
argument_list|)
decl_stmt|;
try|try
block|{
name|kubernetesClient
operator|.
name|configMaps
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
name|create
argument_list|(
name|newConfigMap
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} ConfigMap {} successfully created"
argument_list|,
name|logPrefix
argument_list|()
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getConfigMapName
argument_list|()
argument_list|)
expr_stmt|;
name|updateLatestLeaderInfo
argument_list|(
name|newConfigMap
argument_list|,
name|members
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// Suppress exception
name|LOG
operator|.
name|warn
argument_list|(
name|logPrefix
argument_list|()
operator|+
literal|" Unable to create the ConfigMap, it may have been created by other cluster members concurrently. If the problem persists, check if the service account has "
operator|+
literal|"the right "
operator|+
literal|"permissions to create it"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|logPrefix
argument_list|()
operator|+
literal|" Exception while trying to create the ConfigMap"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} Lock configmap already present in the Kubernetes namespace. Checking..."
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|LeaderInfo
name|leaderInfo
init|=
name|ConfigMapLockUtils
operator|.
name|getLeaderInfo
argument_list|(
name|configMap
argument_list|,
name|members
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getGroupName
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|canAcquire
init|=
operator|!
name|leaderInfo
operator|.
name|hasValidLeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|canAcquire
condition|)
block|{
comment|// Try to be the new leader
try|try
block|{
name|ConfigMap
name|updatedConfigMap
init|=
name|ConfigMapLockUtils
operator|.
name|getConfigMapWithNewLeader
argument_list|(
name|configMap
argument_list|,
name|newLeaderInfo
argument_list|)
decl_stmt|;
name|kubernetesClient
operator|.
name|configMaps
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
name|withName
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getConfigMapName
argument_list|()
argument_list|)
operator|.
name|lockResourceVersion
argument_list|(
name|configMap
operator|.
name|getMetadata
argument_list|()
operator|.
name|getResourceVersion
argument_list|()
argument_list|)
operator|.
name|replace
argument_list|(
name|updatedConfigMap
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} ConfigMap {} successfully updated"
argument_list|,
name|logPrefix
argument_list|()
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getConfigMapName
argument_list|()
argument_list|)
expr_stmt|;
name|updateLatestLeaderInfo
argument_list|(
name|updatedConfigMap
argument_list|,
name|members
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
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
name|logPrefix
argument_list|()
operator|+
literal|" Unable to update the lock ConfigMap to set leadership information"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|logPrefix
argument_list|()
operator|+
literal|" Error received during configmap lock replace"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
comment|// Another pod is the leader and it's still active
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} Another Pod ({}) is the current leader and it is still active"
argument_list|,
name|logPrefix
argument_list|()
argument_list|,
name|this
operator|.
name|latestLeaderInfo
operator|.
name|getLeader
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
DECL|method|updateLatestLeaderInfo (ConfigMap configMap, Set<String> members)
specifier|private
name|void
name|updateLatestLeaderInfo
parameter_list|(
name|ConfigMap
name|configMap
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|members
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} Updating internal status about the current leader"
argument_list|,
name|logPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|latestConfigMap
operator|=
name|configMap
expr_stmt|;
name|this
operator|.
name|latestMembers
operator|=
name|members
expr_stmt|;
name|this
operator|.
name|latestLeaderInfo
operator|=
name|ConfigMapLockUtils
operator|.
name|getLeaderInfo
argument_list|(
name|configMap
argument_list|,
name|members
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getGroupName
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} Current leader info: {}"
argument_list|,
name|logPrefix
argument_list|()
argument_list|,
name|this
operator|.
name|latestLeaderInfo
argument_list|)
expr_stmt|;
block|}
DECL|method|pullConfigMap ()
specifier|private
name|ConfigMap
name|pullConfigMap
parameter_list|()
block|{
return|return
name|kubernetesClient
operator|.
name|configMaps
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
name|withName
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getConfigMapName
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|pullClusterMembers ()
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|pullClusterMembers
parameter_list|()
block|{
name|List
argument_list|<
name|Pod
argument_list|>
name|pods
init|=
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
decl_stmt|;
return|return
name|pods
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|pod
lambda|->
name|pod
operator|.
name|getMetadata
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|jitter (long num, double factor)
specifier|private
name|long
name|jitter
parameter_list|(
name|long
name|num
parameter_list|,
name|double
name|factor
parameter_list|)
block|{
return|return
call|(
name|long
call|)
argument_list|(
name|num
operator|*
operator|(
literal|1
operator|+
name|Math
operator|.
name|random
argument_list|()
operator|*
operator|(
name|factor
operator|-
literal|1
operator|)
operator|)
argument_list|)
return|;
block|}
DECL|method|logPrefix ()
specifier|private
name|String
name|logPrefix
parameter_list|()
block|{
return|return
literal|"Pod["
operator|+
name|this
operator|.
name|lockConfiguration
operator|.
name|getPodName
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

