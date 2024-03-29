begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.master
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|master
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
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
name|Endpoint
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
name|Processor
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
name|StartupListener
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
name|SuspendableService
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|DefaultConsumer
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
name|ServiceHelper
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
comment|/**  * A consumer which is only really active when the {@link CamelClusterView} has  * the leadership.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Master Consumer"
argument_list|)
DECL|class|MasterConsumer
specifier|public
class|class
name|MasterConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MasterConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|clusterService
specifier|private
specifier|final
name|CamelClusterService
name|clusterService
decl_stmt|;
DECL|field|masterEndpoint
specifier|private
specifier|final
name|MasterEndpoint
name|masterEndpoint
decl_stmt|;
DECL|field|delegatedEndpoint
specifier|private
specifier|final
name|Endpoint
name|delegatedEndpoint
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|leadershipListener
specifier|private
specifier|final
name|CamelClusterEventListener
operator|.
name|Leadership
name|leadershipListener
decl_stmt|;
DECL|field|delegatedConsumer
specifier|private
name|Consumer
name|delegatedConsumer
decl_stmt|;
DECL|field|view
specifier|private
specifier|volatile
name|CamelClusterView
name|view
decl_stmt|;
DECL|method|MasterConsumer (MasterEndpoint masterEndpoint, Processor processor, CamelClusterService clusterService)
specifier|public
name|MasterConsumer
parameter_list|(
name|MasterEndpoint
name|masterEndpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|CamelClusterService
name|clusterService
parameter_list|)
block|{
name|super
argument_list|(
name|masterEndpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|clusterService
operator|=
name|clusterService
expr_stmt|;
name|this
operator|.
name|masterEndpoint
operator|=
name|masterEndpoint
expr_stmt|;
name|this
operator|.
name|delegatedEndpoint
operator|=
name|masterEndpoint
operator|.
name|getEndpoint
argument_list|()
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|this
operator|.
name|leadershipListener
operator|=
operator|new
name|LeadershipListener
argument_list|()
expr_stmt|;
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Using ClusterService instance {} (id={}, type={})"
argument_list|,
name|clusterService
argument_list|,
name|clusterService
operator|.
name|getId
argument_list|()
argument_list|,
name|clusterService
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|=
name|clusterService
operator|.
name|getView
argument_list|(
name|masterEndpoint
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|addEventListener
argument_list|(
name|leadershipListener
argument_list|)
expr_stmt|;
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|view
operator|!=
literal|null
condition|)
block|{
name|view
operator|.
name|removeEventListener
argument_list|(
name|leadershipListener
argument_list|)
expr_stmt|;
name|clusterService
operator|.
name|releaseView
argument_list|(
name|view
argument_list|)
expr_stmt|;
name|view
operator|=
literal|null
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|delegatedConsumer
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|delegatedEndpoint
argument_list|)
expr_stmt|;
name|delegatedConsumer
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doResume ()
specifier|protected
name|void
name|doResume
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|delegatedConsumer
operator|instanceof
name|SuspendableService
condition|)
block|{
operator|(
operator|(
name|SuspendableService
operator|)
name|delegatedConsumer
operator|)
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doResume
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doSuspend ()
specifier|protected
name|void
name|doSuspend
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|delegatedConsumer
operator|instanceof
name|SuspendableService
condition|)
block|{
operator|(
operator|(
name|SuspendableService
operator|)
name|delegatedConsumer
operator|)
operator|.
name|suspend
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doSuspend
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Are we the master"
argument_list|)
DECL|method|isMaster ()
specifier|public
name|boolean
name|isMaster
parameter_list|()
block|{
return|return
name|view
operator|!=
literal|null
condition|?
name|view
operator|.
name|getLocalMember
argument_list|()
operator|.
name|isLeader
argument_list|()
else|:
literal|false
return|;
block|}
comment|// **************************************
comment|// Helpers
comment|// **************************************
DECL|method|onLeadershipTaken ()
specifier|private
specifier|synchronized
name|void
name|onLeadershipTaken
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegatedConsumer
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
name|delegatedConsumer
operator|=
name|delegatedEndpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
expr_stmt|;
if|if
condition|(
name|delegatedConsumer
operator|instanceof
name|StartupListener
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|addStartupListener
argument_list|(
operator|(
name|StartupListener
operator|)
name|delegatedConsumer
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|delegatedEndpoint
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|delegatedConsumer
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Leadership taken: consumer started: {}"
argument_list|,
name|delegatedEndpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|onLeadershipLost ()
specifier|private
specifier|synchronized
name|void
name|onLeadershipLost
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|delegatedConsumer
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|delegatedEndpoint
argument_list|)
expr_stmt|;
name|delegatedConsumer
operator|=
literal|null
expr_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Leadership lost: consumer stopped: {}"
argument_list|,
name|delegatedEndpoint
argument_list|)
expr_stmt|;
block|}
comment|// **************************************
comment|// Listener
comment|// **************************************
DECL|class|LeadershipListener
specifier|private
specifier|final
class|class
name|LeadershipListener
implements|implements
name|CamelClusterEventListener
operator|.
name|Leadership
block|{
annotation|@
name|Override
DECL|method|leadershipChanged (CamelClusterView view, Optional<CamelClusterMember> leader)
specifier|public
name|void
name|leadershipChanged
parameter_list|(
name|CamelClusterView
name|view
parameter_list|,
name|Optional
argument_list|<
name|CamelClusterMember
argument_list|>
name|leader
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
return|return;
block|}
try|try
block|{
if|if
condition|(
name|view
operator|.
name|getLocalMember
argument_list|()
operator|.
name|isLeader
argument_list|()
condition|)
block|{
name|onLeadershipTaken
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|delegatedConsumer
operator|!=
literal|null
condition|)
block|{
name|onLeadershipLost
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
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

