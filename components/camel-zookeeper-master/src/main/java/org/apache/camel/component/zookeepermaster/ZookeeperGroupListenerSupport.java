begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeepermaster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeepermaster
package|;
end_package

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
name|component
operator|.
name|zookeepermaster
operator|.
name|group
operator|.
name|Group
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
name|zookeepermaster
operator|.
name|group
operator|.
name|GroupListener
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
name|IOHelper
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
DECL|class|ZookeeperGroupListenerSupport
specifier|public
class|class
name|ZookeeperGroupListenerSupport
extends|extends
name|ZookeeperGroupSupport
implements|implements
name|GroupListener
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ZookeeperGroupListenerSupport
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|singleton
specifier|private
name|Group
argument_list|<
name|CamelNodeState
argument_list|>
name|singleton
decl_stmt|;
DECL|field|clusterPath
specifier|private
specifier|final
name|String
name|clusterPath
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|onLockAcquired
specifier|private
specifier|final
name|Runnable
name|onLockAcquired
decl_stmt|;
DECL|field|onDisconnected
specifier|private
specifier|final
name|Runnable
name|onDisconnected
decl_stmt|;
DECL|method|ZookeeperGroupListenerSupport (String clusterPath, Endpoint endpoint, Runnable onLockAcquired, Runnable onDisconnected)
specifier|public
name|ZookeeperGroupListenerSupport
parameter_list|(
name|String
name|clusterPath
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|Runnable
name|onLockAcquired
parameter_list|,
name|Runnable
name|onDisconnected
parameter_list|)
block|{
name|this
operator|.
name|clusterPath
operator|=
name|clusterPath
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|onLockAcquired
operator|=
name|onLockAcquired
expr_stmt|;
name|this
operator|.
name|onDisconnected
operator|=
name|onDisconnected
expr_stmt|;
block|}
DECL|method|updateState (CamelNodeState state)
specifier|public
name|void
name|updateState
parameter_list|(
name|CamelNodeState
name|state
parameter_list|)
block|{
name|singleton
operator|.
name|update
argument_list|(
name|state
argument_list|)
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
name|this
operator|.
name|singleton
operator|=
name|createGroup
argument_list|(
name|clusterPath
argument_list|)
expr_stmt|;
name|this
operator|.
name|singleton
operator|.
name|add
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|singleton
operator|.
name|start
argument_list|()
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
name|IOHelper
operator|.
name|close
argument_list|(
name|singleton
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getClusterPath ()
specifier|public
name|String
name|getClusterPath
parameter_list|()
block|{
return|return
name|clusterPath
return|;
block|}
DECL|method|getGroup ()
specifier|public
name|Group
argument_list|<
name|CamelNodeState
argument_list|>
name|getGroup
parameter_list|()
block|{
return|return
name|singleton
return|;
block|}
annotation|@
name|Override
DECL|method|groupEvent (Group group, GroupEvent event)
specifier|public
name|void
name|groupEvent
parameter_list|(
name|Group
name|group
parameter_list|,
name|GroupEvent
name|event
parameter_list|)
block|{
switch|switch
condition|(
name|event
condition|)
block|{
case|case
name|CONNECTED
case|:
break|break;
case|case
name|CHANGED
case|:
if|if
condition|(
name|singleton
operator|.
name|isConnected
argument_list|()
condition|)
block|{
if|if
condition|(
name|singleton
operator|.
name|isMaster
argument_list|()
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Master/Standby endpoint is Master for:  "
operator|+
name|endpoint
operator|+
literal|" in "
operator|+
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|onLockOwned
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Master/Standby endpoint is Standby for: "
operator|+
name|endpoint
operator|+
literal|" in "
operator|+
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
break|break;
case|case
name|DISCONNECTED
case|:
try|try
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Disconnecting as master. Stopping consumer: {}"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|onDisconnected
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
literal|"Failed to stop master consumer for: "
operator|+
name|endpoint
operator|+
literal|". This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
comment|// noop
block|}
block|}
DECL|method|onDisconnected ()
specifier|protected
name|void
name|onDisconnected
parameter_list|()
block|{
name|onDisconnected
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
DECL|method|onLockOwned ()
specifier|protected
name|void
name|onLockOwned
parameter_list|()
block|{
name|onLockAcquired
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

