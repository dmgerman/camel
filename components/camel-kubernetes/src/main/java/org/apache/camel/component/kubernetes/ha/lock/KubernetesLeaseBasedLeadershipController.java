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
name|Date
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
name|Executors
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
DECL|class|KubernetesLeaseBasedLeadershipController
specifier|public
class|class
name|KubernetesLeaseBasedLeadershipController
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
name|KubernetesLeaseBasedLeadershipController
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|FIXED_ADDITIONAL_DELAY
specifier|private
specifier|static
specifier|final
name|long
name|FIXED_ADDITIONAL_DELAY
init|=
literal|100
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
DECL|field|serializedExecutor
specifier|private
name|ScheduledExecutorService
name|serializedExecutor
decl_stmt|;
DECL|field|eventDispatcherExecutor
specifier|private
name|ScheduledExecutorService
name|eventDispatcherExecutor
decl_stmt|;
DECL|field|membersMonitor
specifier|private
name|KubernetesMembersMonitor
name|membersMonitor
decl_stmt|;
DECL|field|currentLeader
specifier|private
name|Optional
argument_list|<
name|String
argument_list|>
name|currentLeader
init|=
name|Optional
operator|.
name|empty
argument_list|()
decl_stmt|;
DECL|field|latestLeaderInfo
specifier|private
specifier|volatile
name|LeaderInfo
name|latestLeaderInfo
decl_stmt|;
DECL|method|KubernetesLeaseBasedLeadershipController (KubernetesClient kubernetesClient, KubernetesLockConfiguration lockConfiguration, KubernetesClusterEventHandler eventHandler)
specifier|public
name|KubernetesLeaseBasedLeadershipController
parameter_list|(
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
throws|throws
name|Exception
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
literal|"Starting leadership controller..."
argument_list|)
expr_stmt|;
name|serializedExecutor
operator|=
name|Executors
operator|.
name|newSingleThreadScheduledExecutor
argument_list|()
expr_stmt|;
name|eventDispatcherExecutor
operator|=
name|Executors
operator|.
name|newSingleThreadScheduledExecutor
argument_list|()
expr_stmt|;
name|membersMonitor
operator|=
operator|new
name|KubernetesMembersMonitor
argument_list|(
name|this
operator|.
name|serializedExecutor
argument_list|,
name|this
operator|.
name|kubernetesClient
argument_list|,
name|this
operator|.
name|lockConfiguration
argument_list|)
expr_stmt|;
if|if
condition|(
name|eventHandler
operator|!=
literal|null
condition|)
block|{
name|membersMonitor
operator|.
name|addClusterEventHandler
argument_list|(
name|eventHandler
argument_list|)
expr_stmt|;
block|}
name|membersMonitor
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
name|initialization
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopping leadership controller..."
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
if|if
condition|(
name|eventDispatcherExecutor
operator|!=
literal|null
condition|)
block|{
name|eventDispatcherExecutor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|eventDispatcherExecutor
operator|.
name|awaitTermination
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|eventDispatcherExecutor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|membersMonitor
operator|!=
literal|null
condition|)
block|{
name|membersMonitor
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|membersMonitor
operator|=
literal|null
expr_stmt|;
name|eventDispatcherExecutor
operator|=
literal|null
expr_stmt|;
name|serializedExecutor
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Get the first ConfigMap and setup the initial state.      */
DECL|method|initialization ()
specifier|private
name|void
name|initialization
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Reading (with retry) the configmap {} to detect the current leader"
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getConfigMapName
argument_list|()
argument_list|)
expr_stmt|;
name|refreshConfigMapFromCluster
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|isCurrentPodTheActiveLeader
argument_list|()
condition|)
block|{
name|serializedExecutor
operator|.
name|execute
argument_list|(
name|this
operator|::
name|onLeadershipAcquired
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"The current pod ({}) is not the leader of the group '{}' in ConfigMap '{}' at this time"
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getPodName
argument_list|()
argument_list|,
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
name|getConfigMapName
argument_list|()
argument_list|)
expr_stmt|;
name|serializedExecutor
operator|.
name|execute
argument_list|(
name|this
operator|::
name|acquireLeadershipCycle
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Signals the acquisition of the leadership and move to the keep-leadership state.      */
DECL|method|onLeadershipAcquired ()
specifier|private
name|void
name|onLeadershipAcquired
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"The current pod ({}) is now the leader of the group '{}' in ConfigMap '{}'"
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getPodName
argument_list|()
argument_list|,
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
name|getConfigMapName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|eventDispatcherExecutor
operator|.
name|execute
argument_list|(
name|this
operator|::
name|checkAndNotifyNewLeader
argument_list|)
expr_stmt|;
name|long
name|nextDelay
init|=
name|computeNextRenewWaitTime
argument_list|(
name|this
operator|.
name|latestLeaderInfo
operator|.
name|getTimestamp
argument_list|()
argument_list|,
name|this
operator|.
name|latestLeaderInfo
operator|.
name|getTimestamp
argument_list|()
argument_list|)
decl_stmt|;
name|serializedExecutor
operator|.
name|schedule
argument_list|(
name|this
operator|::
name|keepLeadershipCycle
argument_list|,
name|nextDelay
operator|+
name|FIXED_ADDITIONAL_DELAY
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
comment|/**      * While in the keep-leadership state, the controller periodically renews the lease.      * If a renewal deadline is met and it was not possible to renew the lease, the leadership is lost.      */
DECL|method|keepLeadershipCycle ()
specifier|private
name|void
name|keepLeadershipCycle
parameter_list|()
block|{
comment|// renew lease periodically
name|refreshConfigMapFromCluster
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// if possible, update
if|if
condition|(
name|this
operator|.
name|latestLeaderInfo
operator|.
name|isTimeElapsedSeconds
argument_list|(
name|lockConfiguration
operator|.
name|getRenewDeadlineSeconds
argument_list|()
argument_list|)
operator|||
operator|!
name|this
operator|.
name|latestLeaderInfo
operator|.
name|isLeader
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
comment|// Time over for renewal or leadership lost
name|LOG
operator|.
name|debug
argument_list|(
literal|"The current pod ({}) has lost the leadership"
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getPodName
argument_list|()
argument_list|)
expr_stmt|;
name|serializedExecutor
operator|.
name|execute
argument_list|(
name|this
operator|::
name|onLeadershipLost
argument_list|)
expr_stmt|;
return|return;
block|}
name|boolean
name|success
init|=
name|tryAcquireOrRenewLeadership
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Attempted to renew the lease. Success={}"
argument_list|,
name|success
argument_list|)
expr_stmt|;
name|long
name|nextDelay
init|=
name|computeNextRenewWaitTime
argument_list|(
name|this
operator|.
name|latestLeaderInfo
operator|.
name|getTimestamp
argument_list|()
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
decl_stmt|;
name|serializedExecutor
operator|.
name|schedule
argument_list|(
name|this
operator|::
name|keepLeadershipCycle
argument_list|,
name|nextDelay
operator|+
name|FIXED_ADDITIONAL_DELAY
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
comment|/**      * Compute the timestamp of next event while in keep-leadership state.      */
DECL|method|computeNextRenewWaitTime (Date lastRenewal, Date lastRenewalAttempt)
specifier|private
name|long
name|computeNextRenewWaitTime
parameter_list|(
name|Date
name|lastRenewal
parameter_list|,
name|Date
name|lastRenewalAttempt
parameter_list|)
block|{
name|long
name|timeDeadline
init|=
name|lastRenewal
operator|.
name|getTime
argument_list|()
operator|+
name|this
operator|.
name|lockConfiguration
operator|.
name|getRenewDeadlineSeconds
argument_list|()
operator|*
literal|1000
decl_stmt|;
name|long
name|timeRetry
decl_stmt|;
name|long
name|counter
init|=
literal|0
decl_stmt|;
do|do
block|{
name|counter
operator|++
expr_stmt|;
name|timeRetry
operator|=
name|lastRenewal
operator|.
name|getTime
argument_list|()
operator|+
name|counter
operator|*
name|this
operator|.
name|lockConfiguration
operator|.
name|getRetryPeriodSeconds
argument_list|()
operator|*
literal|1000
expr_stmt|;
block|}
do|while
condition|(
name|timeRetry
operator|<
name|lastRenewalAttempt
operator|.
name|getTime
argument_list|()
operator|&&
name|timeRetry
operator|<
name|timeDeadline
condition|)
do|;
name|long
name|time
init|=
name|Math
operator|.
name|min
argument_list|(
name|timeRetry
argument_list|,
name|timeDeadline
argument_list|)
decl_stmt|;
name|long
name|delay
init|=
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|time
operator|-
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Next renewal timeout event will be fired in {} seconds"
argument_list|,
name|delay
operator|/
literal|1000
argument_list|)
expr_stmt|;
return|return
name|delay
return|;
block|}
comment|/**      * Signals the loss of leadership and move to the acquire-leadership state.      */
DECL|method|onLeadershipLost ()
specifier|private
name|void
name|onLeadershipLost
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"The local pod ({}) is no longer the leader of the group '{}' in ConfigMap '{}'"
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getPodName
argument_list|()
argument_list|,
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
name|getConfigMapName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|eventDispatcherExecutor
operator|.
name|execute
argument_list|(
name|this
operator|::
name|checkAndNotifyNewLeader
argument_list|)
expr_stmt|;
name|serializedExecutor
operator|.
name|execute
argument_list|(
name|this
operator|::
name|acquireLeadershipCycle
argument_list|)
expr_stmt|;
block|}
comment|/**      * While in the acquire-leadership state, the controller waits for the current lease to expire before trying to acquire the leadership.      */
DECL|method|acquireLeadershipCycle ()
specifier|private
name|void
name|acquireLeadershipCycle
parameter_list|()
block|{
comment|// wait for the current lease to finish then fire an election
name|refreshConfigMapFromCluster
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// if possible, update
comment|// Notify about changes in current leader if any
name|this
operator|.
name|eventDispatcherExecutor
operator|.
name|execute
argument_list|(
name|this
operator|::
name|checkAndNotifyNewLeader
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|this
operator|.
name|latestLeaderInfo
operator|.
name|isTimeElapsedSeconds
argument_list|(
name|lockConfiguration
operator|.
name|getLeaseDurationSeconds
argument_list|()
argument_list|)
condition|)
block|{
comment|// Wait for the lease to finish before trying leader election
name|long
name|nextDelay
init|=
name|computeNextElectionWaitTime
argument_list|(
name|this
operator|.
name|latestLeaderInfo
operator|.
name|getTimestamp
argument_list|()
argument_list|)
decl_stmt|;
name|serializedExecutor
operator|.
name|schedule
argument_list|(
name|this
operator|::
name|acquireLeadershipCycle
argument_list|,
name|nextDelay
operator|+
name|FIXED_ADDITIONAL_DELAY
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
return|return;
block|}
name|boolean
name|acquired
init|=
name|tryAcquireOrRenewLeadership
argument_list|()
decl_stmt|;
if|if
condition|(
name|acquired
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Leadership acquired for ConfigMap {}. Notification in progress..."
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getConfigMapName
argument_list|()
argument_list|)
expr_stmt|;
name|serializedExecutor
operator|.
name|execute
argument_list|(
name|this
operator|::
name|onLeadershipAcquired
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// Notify about changes in current leader if any
name|this
operator|.
name|eventDispatcherExecutor
operator|.
name|execute
argument_list|(
name|this
operator|::
name|checkAndNotifyNewLeader
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot acquire the leadership for ConfigMap {}"
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getConfigMapName
argument_list|()
argument_list|)
expr_stmt|;
name|long
name|nextDelay
init|=
name|computeNextElectionWaitTime
argument_list|(
name|this
operator|.
name|latestLeaderInfo
operator|.
name|getTimestamp
argument_list|()
argument_list|)
decl_stmt|;
name|serializedExecutor
operator|.
name|schedule
argument_list|(
name|this
operator|::
name|acquireLeadershipCycle
argument_list|,
name|nextDelay
operator|+
name|FIXED_ADDITIONAL_DELAY
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
DECL|method|computeNextElectionWaitTime (Date lastRenewal)
specifier|private
name|long
name|computeNextElectionWaitTime
parameter_list|(
name|Date
name|lastRenewal
parameter_list|)
block|{
if|if
condition|(
name|lastRenewal
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error detected while getting leadership info, next election timeout event will be fired in {} seconds"
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getRetryOnErrorIntervalSeconds
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|lockConfiguration
operator|.
name|getRetryOnErrorIntervalSeconds
argument_list|()
return|;
block|}
name|long
name|time
init|=
name|lastRenewal
operator|.
name|getTime
argument_list|()
operator|+
name|this
operator|.
name|lockConfiguration
operator|.
name|getLeaseDurationSeconds
argument_list|()
operator|*
literal|1000
operator|+
name|jitter
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getRetryPeriodSeconds
argument_list|()
operator|*
literal|1000
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getJitterFactor
argument_list|()
argument_list|)
decl_stmt|;
name|long
name|delay
init|=
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|time
operator|-
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Next election timeout event will be fired in {} seconds"
argument_list|,
name|delay
operator|/
literal|1000
argument_list|)
expr_stmt|;
return|return
name|delay
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
DECL|method|tryAcquireOrRenewLeadership ()
specifier|private
name|boolean
name|tryAcquireOrRenewLeadership
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Trying to acquire or renew the leadership..."
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
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unable to retrieve the current ConfigMap "
operator|+
name|this
operator|.
name|lockConfiguration
operator|.
name|getConfigMapName
argument_list|()
operator|+
literal|" from Kubernetes"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// Info to set in the configmap to become leaders
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
argument_list|)
decl_stmt|;
if|if
condition|(
name|configMap
operator|==
literal|null
condition|)
block|{
comment|// No configmap created so far
name|LOG
operator|.
name|debug
argument_list|(
literal|"Lock configmap is not present in the Kubernetes namespace. A new ConfigMap will be created"
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
literal|"Unable to create the ConfigMap, it may have been created by other cluster members concurrently. If the problem persists, check if the service account has the right "
operator|+
literal|"permissions to create it"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Exception while trying to create the ConfigMap"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
comment|// Try to get the configMap and return the current leader
name|refreshConfigMapFromCluster
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|isCurrentPodTheActiveLeader
argument_list|()
return|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"ConfigMap {} successfully created and local pod is leader"
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
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Lock configmap already present in the Kubernetes namespace. Checking..."
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
name|this
operator|.
name|lockConfiguration
operator|.
name|getGroupName
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|weWereLeader
init|=
name|leaderInfo
operator|.
name|isLeader
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getPodName
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|leaseExpired
init|=
name|leaderInfo
operator|.
name|isTimeElapsedSeconds
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getLeaseDurationSeconds
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|weWereLeader
operator|||
name|leaseExpired
condition|)
block|{
comment|// Renew the lease or set the new leader
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
literal|"ConfigMap {} successfully updated and local pod is leader"
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
literal|"An attempt to become leader has failed. It's possible that the leadership has been taken by another pod"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error received during configmap lock replace"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
comment|// Try to get the configMap and return the current leader
name|refreshConfigMapFromCluster
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|isCurrentPodTheActiveLeader
argument_list|()
return|;
block|}
block|}
else|else
block|{
comment|// Another pod is the leader and lease is not expired
name|LOG
operator|.
name|debug
argument_list|(
literal|"Another pod is the current leader and lease has not expired yet"
argument_list|)
expr_stmt|;
name|updateLatestLeaderInfo
argument_list|(
name|configMap
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
DECL|method|refreshConfigMapFromCluster (boolean retry)
specifier|private
name|void
name|refreshConfigMapFromCluster
parameter_list|(
name|boolean
name|retry
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Retrieving configmap {}"
argument_list|,
name|this
operator|.
name|lockConfiguration
operator|.
name|getConfigMapName
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|updateLatestLeaderInfo
argument_list|(
name|pullConfigMap
argument_list|()
argument_list|)
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
literal|"ConfigMap pull failed. Retrying in "
operator|+
name|this
operator|.
name|lockConfiguration
operator|.
name|getRetryOnErrorIntervalSeconds
argument_list|()
operator|+
literal|" seconds..."
argument_list|,
name|ex
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getRetryOnErrorIntervalSeconds
argument_list|()
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|refreshConfigMapFromCluster
argument_list|(
literal|true
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
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Controller Thread interrupted, shutdown in progress"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot retrieve the ConfigMap: pull failed"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|isCurrentPodTheActiveLeader ()
specifier|private
name|boolean
name|isCurrentPodTheActiveLeader
parameter_list|()
block|{
return|return
name|latestLeaderInfo
operator|!=
literal|null
operator|&&
name|latestLeaderInfo
operator|.
name|isLeader
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getPodName
argument_list|()
argument_list|)
operator|&&
operator|!
name|latestLeaderInfo
operator|.
name|isTimeElapsedSeconds
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getRenewDeadlineSeconds
argument_list|()
argument_list|)
return|;
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
DECL|method|updateLatestLeaderInfo (ConfigMap configMap)
specifier|private
name|void
name|updateLatestLeaderInfo
parameter_list|(
name|ConfigMap
name|configMap
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Updating internal status about the current leader"
argument_list|)
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
name|this
operator|.
name|lockConfiguration
operator|.
name|getGroupName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|checkAndNotifyNewLeader ()
specifier|private
name|void
name|checkAndNotifyNewLeader
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Checking if the current leader has changed to notify the event handler..."
argument_list|)
expr_stmt|;
name|LeaderInfo
name|newLeaderInfo
init|=
name|this
operator|.
name|latestLeaderInfo
decl_stmt|;
if|if
condition|(
name|newLeaderInfo
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|long
name|leaderInfoDurationSeconds
init|=
name|newLeaderInfo
operator|.
name|isLeader
argument_list|(
name|this
operator|.
name|lockConfiguration
operator|.
name|getPodName
argument_list|()
argument_list|)
condition|?
name|this
operator|.
name|lockConfiguration
operator|.
name|getRenewDeadlineSeconds
argument_list|()
else|:
name|this
operator|.
name|lockConfiguration
operator|.
name|getLeaseDurationSeconds
argument_list|()
decl_stmt|;
name|Optional
argument_list|<
name|String
argument_list|>
name|newLeader
decl_stmt|;
if|if
condition|(
name|newLeaderInfo
operator|.
name|getLeader
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|newLeaderInfo
operator|.
name|isTimeElapsedSeconds
argument_list|(
name|leaderInfoDurationSeconds
argument_list|)
condition|)
block|{
name|newLeader
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|newLeaderInfo
operator|.
name|getLeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newLeader
operator|=
name|Optional
operator|.
name|empty
argument_list|()
expr_stmt|;
block|}
comment|// Sending notifications in case of leader change
if|if
condition|(
operator|!
name|newLeader
operator|.
name|equals
argument_list|(
name|this
operator|.
name|currentLeader
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Current leader has changed from {} to {}. Sending notifications..."
argument_list|,
name|this
operator|.
name|currentLeader
argument_list|,
name|newLeader
argument_list|)
expr_stmt|;
name|this
operator|.
name|currentLeader
operator|=
name|newLeader
expr_stmt|;
name|eventHandler
operator|.
name|onKubernetesClusterEvent
argument_list|(
call|(
name|KubernetesClusterEvent
operator|.
name|KubernetesClusterLeaderChangedEvent
call|)
argument_list|()
operator|->
name|newLeader
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Current leader unchanged: {}"
argument_list|,
name|this
operator|.
name|currentLeader
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

