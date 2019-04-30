begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
import|;
end_import

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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|StatefulService
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
name|engine
operator|.
name|JavaUuidGenerator
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
name|spi
operator|.
name|UuidGenerator
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
name|CuratorFrameworkFactory
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
name|locks
operator|.
name|InterProcessSemaphoreV2
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
name|locks
operator|.
name|Lease
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
name|state
operator|.
name|ConnectionState
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
name|state
operator|.
name|ConnectionStateListener
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
name|retry
operator|.
name|ExponentialBackoffRetry
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
comment|/**  *<code>CuratorMultiMasterLeaderElection</code> uses the leader election capabilities of a  * ZooKeeper cluster to control which nodes are enabled. It is typically used in  * fail-over scenarios controlling identical instances of an application across  * a cluster of Camel based servers.<p> The election is configured providing the number of instances that are required  * to be active..  *<p> All instances of the election must also be configured with the same path on the ZooKeeper  * cluster where the election will be carried out. It is good practice for this  * to indicate the application e.g.<tt>/someapplication/someroute/</tt> note  * that these nodes should exist before using the election.<p> See<a  * href="http://hadoop.apache.org/zookeeper/docs/current/recipes.html#sc_leaderElection">  * for more on how Leader election</a> is archived with ZooKeeper.  */
end_comment

begin_class
DECL|class|CuratorMultiMasterLeaderElection
specifier|public
class|class
name|CuratorMultiMasterLeaderElection
implements|implements
name|ConnectionStateListener
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
name|CuratorMultiMasterLeaderElection
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|candidateName
specifier|private
specifier|final
name|String
name|candidateName
decl_stmt|;
DECL|field|watchers
specifier|private
specifier|final
name|List
argument_list|<
name|ElectionWatcher
argument_list|>
name|watchers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|desiredActiveNodes
specifier|private
specifier|final
name|int
name|desiredActiveNodes
decl_stmt|;
DECL|field|activeNode
specifier|private
name|AtomicBoolean
name|activeNode
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|uuidGenerator
specifier|private
name|UuidGenerator
name|uuidGenerator
init|=
operator|new
name|JavaUuidGenerator
argument_list|()
decl_stmt|;
DECL|field|leaderSelector
specifier|private
name|InterProcessSemaphoreV2
name|leaderSelector
decl_stmt|;
DECL|field|client
specifier|private
name|CuratorFramework
name|client
decl_stmt|;
DECL|field|lease
specifier|private
name|Lease
name|lease
decl_stmt|;
DECL|method|CuratorMultiMasterLeaderElection (String uri, int desiredActiveNodes)
specifier|public
name|CuratorMultiMasterLeaderElection
parameter_list|(
name|String
name|uri
parameter_list|,
name|int
name|desiredActiveNodes
parameter_list|)
block|{
name|this
operator|.
name|candidateName
operator|=
name|createCandidateName
argument_list|()
expr_stmt|;
name|this
operator|.
name|desiredActiveNodes
operator|=
name|desiredActiveNodes
expr_stmt|;
name|String
name|connectionString
init|=
name|uri
operator|.
name|substring
argument_list|(
literal|1
operator|+
name|uri
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
argument_list|)
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
index|[
literal|0
index|]
decl_stmt|;
name|String
name|protocol
init|=
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|uri
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|uri
operator|.
name|replace
argument_list|(
name|protocol
operator|+
literal|":"
operator|+
name|connectionString
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|client
operator|=
name|CuratorFrameworkFactory
operator|.
name|newClient
argument_list|(
name|connectionString
argument_list|,
operator|new
name|ExponentialBackoffRetry
argument_list|(
literal|1000
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|.
name|getConnectionStateListenable
argument_list|()
operator|.
name|addListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|leaderSelector
operator|=
operator|new
name|InterProcessSemaphoreV2
argument_list|(
name|client
argument_list|,
name|path
argument_list|,
name|this
operator|.
name|desiredActiveNodes
argument_list|)
expr_stmt|;
name|client
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
comment|// stolen from org/apache/camel/processor/CamelInternalProcessor
DECL|method|isCamelStopping (CamelContext context)
specifier|public
specifier|static
name|boolean
name|isCamelStopping
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|context
operator|instanceof
name|StatefulService
condition|)
block|{
name|StatefulService
name|ss
init|=
operator|(
name|StatefulService
operator|)
name|context
decl_stmt|;
return|return
name|ss
operator|.
name|isStopping
argument_list|()
operator|||
name|ss
operator|.
name|isStopped
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|shutdownClients ()
specifier|public
name|void
name|shutdownClients
parameter_list|()
block|{
try|try
block|{
name|leaderSelector
operator|.
name|returnLease
argument_list|(
name|lease
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/*      * Blocking method      */
DECL|method|requestResource ()
specifier|public
name|void
name|requestResource
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Requested to become active from {}"
argument_list|,
name|candidateName
argument_list|)
expr_stmt|;
try|try
block|{
name|lease
operator|=
name|leaderSelector
operator|.
name|acquire
argument_list|()
expr_stmt|;
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
literal|"Unable to obtain access to become a leader node."
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"{} is now active"
argument_list|,
name|candidateName
argument_list|)
expr_stmt|;
name|activeNode
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|notifyElectionWatchers
argument_list|()
expr_stmt|;
block|}
DECL|method|isMaster ()
specifier|public
name|boolean
name|isMaster
parameter_list|()
block|{
return|return
name|activeNode
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|createCandidateName ()
specifier|private
name|String
name|createCandidateName
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
try|try
block|{
comment|/* UUID would be enough, also using hostname for human readability */
name|builder
operator|.
name|append
argument_list|(
name|InetAddress
operator|.
name|getLocalHost
argument_list|()
operator|.
name|getCanonicalHostName
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownHostException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed to get the local hostname."
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"unknown-host"
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
operator|.
name|append
argument_list|(
name|uuidGenerator
operator|.
name|generateUuid
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getCandidateName ()
specifier|public
name|String
name|getCandidateName
parameter_list|()
block|{
return|return
name|candidateName
return|;
block|}
DECL|method|notifyElectionWatchers ()
specifier|private
name|void
name|notifyElectionWatchers
parameter_list|()
block|{
for|for
control|(
name|ElectionWatcher
name|watcher
range|:
name|watchers
control|)
block|{
try|try
block|{
name|watcher
operator|.
name|electionResultChanged
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
literal|"Election watcher "
operator|+
name|watcher
operator|+
literal|" of type "
operator|+
name|watcher
operator|.
name|getClass
argument_list|()
operator|+
literal|" threw an exception."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|addElectionWatcher (ElectionWatcher e)
specifier|public
name|boolean
name|addElectionWatcher
parameter_list|(
name|ElectionWatcher
name|e
parameter_list|)
block|{
return|return
name|watchers
operator|.
name|add
argument_list|(
name|e
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|stateChanged (CuratorFramework curatorFramework, ConnectionState connectionState)
specifier|public
name|void
name|stateChanged
parameter_list|(
name|CuratorFramework
name|curatorFramework
parameter_list|,
name|ConnectionState
name|connectionState
parameter_list|)
block|{
switch|switch
condition|(
name|connectionState
condition|)
block|{
case|case
name|SUSPENDED
case|:
case|case
name|LOST
case|:
name|LOG
operator|.
name|info
argument_list|(
literal|"Received {} state from connection. Giving up lock."
argument_list|,
name|connectionState
argument_list|)
expr_stmt|;
try|try
block|{
name|leaderSelector
operator|.
name|returnLease
argument_list|(
name|lease
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|this
operator|.
name|activeNode
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|notifyElectionWatchers
argument_list|()
expr_stmt|;
block|}
break|break;
default|default:
name|LOG
operator|.
name|info
argument_list|(
literal|"Connection state changed: {}"
argument_list|,
name|connectionState
argument_list|)
expr_stmt|;
name|requestResource
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

