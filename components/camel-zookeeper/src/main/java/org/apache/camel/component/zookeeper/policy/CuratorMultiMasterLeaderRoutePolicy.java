begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper.policy
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
name|policy
package|;
end_package

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
name|CopyOnWriteArraySet
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
name|concurrent
operator|.
name|locks
operator|.
name|Lock
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
name|ReentrantLock
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
name|NonManagedService
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
name|Route
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
name|DefaultCamelContext
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
name|RoutePolicySupport
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
comment|/**  *<code>CuratorMultiMasterLeaderRoutePolicy</code> uses Apache Curator InterProcessSemaphoreV2 receipe to implement the behavior of having  * at multiple active instances of  a route, controlled by a specific policy, running. It is typically used in  * fail-over scenarios controlling identical instances of a route across a cluster of Camel based servers.  *<p>  * The policy affects the normal startup lifecycle of CamelContext and Routes, automatically set autoStart property of  * routes controlled by this policy to false.  * After Curator receipe identifies the current Policy instance as the Leader between a set of clients that are  * competing for the role, it will start the route, and only at that moment the route will start its business.  * This specific behavior is designed to avoid scenarios where such a policy would kick in only after a route had  * already been started, with the risk, for consumers for example, that some source event might have already been  * consumed.  *<p>  * All instances of the policy must also be configured with the same path on the  * ZooKeeper cluster where the election will be carried out. It is good practice  * for this to indicate the application e.g.<tt>/someapplication/someroute/</tt> note  * that these nodes should exist before using the policy.  *<p>  * See<a href="http://hadoop.apache.org/zookeeper/docs/current/recipes.html#sc_leaderElection">  *     for more on how Leader election</a> is archived with ZooKeeper.  */
end_comment

begin_class
DECL|class|CuratorMultiMasterLeaderRoutePolicy
specifier|public
class|class
name|CuratorMultiMasterLeaderRoutePolicy
extends|extends
name|RoutePolicySupport
implements|implements
name|ElectionWatcher
implements|,
name|NonManagedService
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
name|CuratorMultiMasterLeaderRoutePolicy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|uri
specifier|private
specifier|final
name|String
name|uri
decl_stmt|;
DECL|field|lock
specifier|private
specifier|final
name|Lock
name|lock
init|=
operator|new
name|ReentrantLock
argument_list|()
decl_stmt|;
DECL|field|suspendedRoutes
specifier|private
specifier|final
name|Set
argument_list|<
name|Route
argument_list|>
name|suspendedRoutes
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|shouldProcessExchanges
specifier|private
specifier|final
name|AtomicBoolean
name|shouldProcessExchanges
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
DECL|field|shouldStopRoute
specifier|private
specifier|volatile
name|boolean
name|shouldStopRoute
init|=
literal|true
decl_stmt|;
DECL|field|enabledCount
specifier|private
specifier|final
name|int
name|enabledCount
decl_stmt|;
DECL|field|electionLock
specifier|private
specifier|final
name|Lock
name|electionLock
init|=
operator|new
name|ReentrantLock
argument_list|()
decl_stmt|;
DECL|field|election
specifier|private
name|CuratorMultiMasterLeaderElection
name|election
decl_stmt|;
DECL|method|CuratorMultiMasterLeaderRoutePolicy (String uri, int enabledCount)
specifier|public
name|CuratorMultiMasterLeaderRoutePolicy
parameter_list|(
name|String
name|uri
parameter_list|,
name|int
name|enabledCount
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
name|this
operator|.
name|enabledCount
operator|=
name|enabledCount
expr_stmt|;
block|}
DECL|method|CuratorMultiMasterLeaderRoutePolicy (String uri)
specifier|public
name|CuratorMultiMasterLeaderRoutePolicy
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
argument_list|(
name|uri
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onInit (Route route)
specifier|public
name|void
name|onInit
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|ensureElectionIsCreated
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Route managed by {}. Setting route [{}] AutoStartup flag to false."
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoute
argument_list|()
operator|.
name|setAutoStartup
argument_list|(
literal|"false"
argument_list|)
expr_stmt|;
if|if
condition|(
name|election
operator|.
name|isMaster
argument_list|()
condition|)
block|{
if|if
condition|(
name|shouldStopRoute
condition|)
block|{
name|startManagedRoute
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|shouldStopRoute
condition|)
block|{
name|stopManagedRoute
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|ensureElectionIsCreated ()
specifier|private
name|void
name|ensureElectionIsCreated
parameter_list|()
block|{
if|if
condition|(
name|election
operator|==
literal|null
condition|)
block|{
name|electionLock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|election
operator|==
literal|null
condition|)
block|{
comment|// re-test
name|election
operator|=
operator|new
name|CuratorMultiMasterLeaderElection
argument_list|(
name|uri
argument_list|,
name|enabledCount
argument_list|)
expr_stmt|;
name|election
operator|.
name|addElectionWatcher
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|electionLock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|startManagedRoute (Route route)
specifier|private
name|void
name|startManagedRoute
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
try|try
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
if|if
condition|(
name|suspendedRoutes
operator|.
name|contains
argument_list|(
name|route
argument_list|)
condition|)
block|{
name|startRoute
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|suspendedRoutes
operator|.
name|remove
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|stopManagedRoute (Route route)
specifier|private
name|void
name|stopManagedRoute
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
try|try
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
comment|// check that we should still suspend once the lock is acquired
if|if
condition|(
operator|!
name|suspendedRoutes
operator|.
name|contains
argument_list|(
name|route
argument_list|)
operator|&&
operator|!
name|shouldProcessExchanges
operator|.
name|get
argument_list|()
condition|)
block|{
name|stopRoute
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|suspendedRoutes
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|electionResultChanged ()
specifier|public
name|void
name|electionResultChanged
parameter_list|()
block|{
if|if
condition|(
name|election
operator|.
name|isMaster
argument_list|()
condition|)
block|{
name|startAllStoppedRoutes
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|startAllStoppedRoutes ()
specifier|private
name|void
name|startAllStoppedRoutes
parameter_list|()
block|{
try|try
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|suspendedRoutes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"{} route(s) have been stopped previously by policy, restarting."
argument_list|,
name|suspendedRoutes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Route
name|suspended
range|:
name|suspendedRoutes
control|)
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|(
name|DefaultCamelContext
operator|)
name|suspended
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|ctx
operator|.
name|isStarted
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Context {} is not started yet. Sleeping for a bit."
argument_list|,
name|ctx
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Starting route [{}] defined in context [{}]."
argument_list|,
name|suspended
operator|.
name|getId
argument_list|()
argument_list|,
name|ctx
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|startRoute
argument_list|(
name|suspended
argument_list|)
expr_stmt|;
block|}
name|suspendedRoutes
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
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
try|try
block|{
name|electionLock
operator|.
name|lock
argument_list|()
expr_stmt|;
name|election
operator|.
name|shutdownClients
argument_list|()
expr_stmt|;
name|election
operator|=
literal|null
expr_stmt|;
block|}
finally|finally
block|{
name|electionLock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getElection ()
specifier|public
name|CuratorMultiMasterLeaderElection
name|getElection
parameter_list|()
block|{
return|return
name|election
return|;
block|}
block|}
end_class

end_unit

