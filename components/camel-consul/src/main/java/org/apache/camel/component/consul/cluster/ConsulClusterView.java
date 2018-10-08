begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.cluster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
operator|.
name|cluster
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

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
name|atomic
operator|.
name|AtomicReference
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
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|Consul
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|KeyValueClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|SessionClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|async
operator|.
name|ConsulResponseCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|ConsulResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|kv
operator|.
name|Value
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|session
operator|.
name|ImmutableSession
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|session
operator|.
name|SessionInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|option
operator|.
name|QueryOptions
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
name|support
operator|.
name|cluster
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
DECL|class|ConsulClusterView
specifier|final
class|class
name|ConsulClusterView
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
name|ConsulClusterService
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|ConsulClusterConfiguration
name|configuration
decl_stmt|;
DECL|field|localMember
specifier|private
specifier|final
name|ConsulLocalMember
name|localMember
decl_stmt|;
DECL|field|sessionId
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|String
argument_list|>
name|sessionId
decl_stmt|;
DECL|field|watcher
specifier|private
specifier|final
name|Watcher
name|watcher
decl_stmt|;
DECL|field|client
specifier|private
name|Consul
name|client
decl_stmt|;
DECL|field|sessionClient
specifier|private
name|SessionClient
name|sessionClient
decl_stmt|;
DECL|field|keyValueClient
specifier|private
name|KeyValueClient
name|keyValueClient
decl_stmt|;
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
DECL|method|ConsulClusterView (ConsulClusterService service, ConsulClusterConfiguration configuration, String namespace)
name|ConsulClusterView
parameter_list|(
name|ConsulClusterService
name|service
parameter_list|,
name|ConsulClusterConfiguration
name|configuration
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|service
argument_list|,
name|namespace
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
name|localMember
operator|=
operator|new
name|ConsulLocalMember
argument_list|()
expr_stmt|;
name|this
operator|.
name|sessionId
operator|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|watcher
operator|=
operator|new
name|Watcher
argument_list|()
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|configuration
operator|.
name|getRootPath
argument_list|()
operator|+
literal|"/"
operator|+
name|namespace
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLeader ()
specifier|public
name|Optional
argument_list|<
name|CamelClusterMember
argument_list|>
name|getLeader
parameter_list|()
block|{
if|if
condition|(
name|keyValueClient
operator|==
literal|null
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
return|return
name|keyValueClient
operator|.
name|getSession
argument_list|(
name|configuration
operator|.
name|getRootPath
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|ConsulClusterMember
operator|::
operator|new
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
if|if
condition|(
name|sessionClient
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
return|return
name|sessionClient
operator|.
name|listSessions
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|i
lambda|->
name|i
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|getNamespace
argument_list|()
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|ConsulClusterMember
operator|::
operator|new
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
name|sessionId
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
name|client
operator|=
name|configuration
operator|.
name|createConsulClient
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|sessionClient
operator|=
name|client
operator|.
name|sessionClient
argument_list|()
expr_stmt|;
name|keyValueClient
operator|=
name|client
operator|.
name|keyValueClient
argument_list|()
expr_stmt|;
name|sessionId
operator|.
name|set
argument_list|(
name|sessionClient
operator|.
name|createSession
argument_list|(
name|ImmutableSession
operator|.
name|builder
argument_list|()
operator|.
name|name
argument_list|(
name|getNamespace
argument_list|()
argument_list|)
operator|.
name|ttl
argument_list|(
name|configuration
operator|.
name|getSessionTtl
argument_list|()
operator|+
literal|"s"
argument_list|)
operator|.
name|lockDelay
argument_list|(
name|configuration
operator|.
name|getSessionLockDelay
argument_list|()
operator|+
literal|"s"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Acquired session with id '{}'"
argument_list|,
name|sessionId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|lock
init|=
name|acquireLock
argument_list|()
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Acquire lock on path '{}' with id '{}' result '{}'"
argument_list|,
name|path
argument_list|,
name|sessionId
operator|.
name|get
argument_list|()
argument_list|,
name|lock
argument_list|)
expr_stmt|;
name|localMember
operator|.
name|setMaster
argument_list|(
name|lock
argument_list|)
expr_stmt|;
name|watcher
operator|.
name|watch
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
name|sessionId
operator|.
name|get
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|keyValueClient
operator|.
name|releaseLock
argument_list|(
name|this
operator|.
name|path
argument_list|,
name|sessionId
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Successfully released lock on path '{}' with id '{}'"
argument_list|,
name|path
argument_list|,
name|sessionId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
synchronized|synchronized
init|(
name|sessionId
init|)
block|{
name|sessionClient
operator|.
name|destroySession
argument_list|(
name|sessionId
operator|.
name|getAndSet
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|localMember
operator|.
name|setMaster
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|acquireLock ()
specifier|private
name|boolean
name|acquireLock
parameter_list|()
block|{
synchronized|synchronized
init|(
name|sessionId
init|)
block|{
name|String
name|sid
init|=
name|sessionId
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
operator|(
name|sid
operator|!=
literal|null
operator|)
condition|?
name|sessionClient
operator|.
name|getSessionInfo
argument_list|(
name|sid
argument_list|)
operator|.
name|map
argument_list|(
name|si
lambda|->
name|keyValueClient
operator|.
name|acquireLock
argument_list|(
name|path
argument_list|,
name|sid
argument_list|)
argument_list|)
operator|.
name|orElse
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
else|:
literal|false
return|;
block|}
block|}
comment|// ***********************************************
comment|//
comment|// ***********************************************
DECL|class|ConsulLocalMember
specifier|private
specifier|final
class|class
name|ConsulLocalMember
implements|implements
name|CamelClusterMember
block|{
DECL|field|master
specifier|private
name|AtomicBoolean
name|master
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|method|setMaster (boolean master)
name|void
name|setMaster
parameter_list|(
name|boolean
name|master
parameter_list|)
block|{
if|if
condition|(
name|master
operator|&&
name|this
operator|.
name|master
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Leadership taken for session id {}"
argument_list|,
name|sessionId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|fireLeadershipChangedEvent
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|master
operator|&&
name|this
operator|.
name|master
operator|.
name|compareAndSet
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Leadership lost for session id {}"
argument_list|,
name|sessionId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|fireLeadershipChangedEvent
argument_list|(
name|getLeader
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
annotation|@
name|Override
DECL|method|isLeader ()
specifier|public
name|boolean
name|isLeader
parameter_list|()
block|{
return|return
name|master
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isLocal ()
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
return|return
literal|true
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
name|sessionId
operator|.
name|get
argument_list|()
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
return|return
literal|"ConsulLocalMember{"
operator|+
literal|"master="
operator|+
name|master
operator|+
literal|'}'
return|;
block|}
block|}
DECL|class|ConsulClusterMember
specifier|private
specifier|final
class|class
name|ConsulClusterMember
implements|implements
name|CamelClusterMember
block|{
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
DECL|method|ConsulClusterMember ()
name|ConsulClusterMember
parameter_list|()
block|{
name|this
operator|.
name|id
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|ConsulClusterMember (SessionInfo info)
name|ConsulClusterMember
parameter_list|(
name|SessionInfo
name|info
parameter_list|)
block|{
name|this
argument_list|(
name|info
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|ConsulClusterMember (String id)
name|ConsulClusterMember
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
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
name|id
return|;
block|}
annotation|@
name|Override
DECL|method|isLeader ()
specifier|public
name|boolean
name|isLeader
parameter_list|()
block|{
if|if
condition|(
name|keyValueClient
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|id
operator|.
name|equals
argument_list|(
name|keyValueClient
operator|.
name|getSession
argument_list|(
name|path
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isLocal ()
specifier|public
name|boolean
name|isLocal
parameter_list|()
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|id
argument_list|,
name|localMember
operator|.
name|getId
argument_list|()
argument_list|)
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
return|return
literal|"ConsulClusterMember{"
operator|+
literal|"id='"
operator|+
name|id
operator|+
literal|'\''
operator|+
literal|'}'
return|;
block|}
block|}
comment|// *************************************************************************
comment|// Watch
comment|// *************************************************************************
DECL|class|Watcher
specifier|private
class|class
name|Watcher
implements|implements
name|ConsulResponseCallback
argument_list|<
name|Optional
argument_list|<
name|Value
argument_list|>
argument_list|>
block|{
DECL|field|index
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|BigInteger
argument_list|>
name|index
decl_stmt|;
DECL|method|Watcher ()
specifier|public
name|Watcher
parameter_list|()
block|{
name|this
operator|.
name|index
operator|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|(
operator|new
name|BigInteger
argument_list|(
literal|"0"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onComplete (ConsulResponse<Optional<Value>> consulResponse)
specifier|public
name|void
name|onComplete
parameter_list|(
name|ConsulResponse
argument_list|<
name|Optional
argument_list|<
name|Value
argument_list|>
argument_list|>
name|consulResponse
parameter_list|)
block|{
if|if
condition|(
name|isStarting
argument_list|()
operator|||
name|isStarted
argument_list|()
condition|)
block|{
name|Optional
argument_list|<
name|Value
argument_list|>
name|value
init|=
name|consulResponse
operator|.
name|getResponse
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|Optional
argument_list|<
name|String
argument_list|>
name|sid
init|=
name|value
operator|.
name|get
argument_list|()
operator|.
name|getSession
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|sid
operator|.
name|isPresent
argument_list|()
condition|)
block|{
comment|// If the key is not held by any session, try acquire a
comment|// lock (become leader)
name|boolean
name|lock
init|=
name|acquireLock
argument_list|()
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Try to acquire lock on path '{}' with id '{}', result '{}'"
argument_list|,
name|path
argument_list|,
name|sessionId
operator|.
name|get
argument_list|()
argument_list|,
name|lock
argument_list|)
expr_stmt|;
name|localMember
operator|.
name|setMaster
argument_list|(
name|lock
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|boolean
name|master
init|=
name|sid
operator|.
name|get
argument_list|()
operator|.
name|equals
argument_list|(
name|sessionId
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|master
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Path {} is held by session {}, local session is {}"
argument_list|,
name|path
argument_list|,
name|sid
operator|.
name|get
argument_list|()
argument_list|,
name|sessionId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|localMember
operator|.
name|setMaster
argument_list|(
name|sid
operator|.
name|get
argument_list|()
operator|.
name|equals
argument_list|(
name|sessionId
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|index
operator|.
name|set
argument_list|(
name|consulResponse
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
name|watch
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onFailure (Throwable throwable)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|""
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
if|if
condition|(
name|sessionId
operator|.
name|get
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|keyValueClient
operator|.
name|releaseLock
argument_list|(
name|configuration
operator|.
name|getRootPath
argument_list|()
argument_list|,
name|sessionId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|localMember
operator|.
name|setMaster
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|watch
argument_list|()
expr_stmt|;
block|}
DECL|method|watch ()
specifier|public
name|void
name|watch
parameter_list|()
block|{
if|if
condition|(
name|sessionId
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|isStarting
argument_list|()
operator|||
name|isStarted
argument_list|()
condition|)
block|{
comment|// Watch for changes
name|keyValueClient
operator|.
name|getValue
argument_list|(
name|path
argument_list|,
name|QueryOptions
operator|.
name|blockSeconds
argument_list|(
name|configuration
operator|.
name|getSessionRefreshInterval
argument_list|()
argument_list|,
name|index
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|sessionId
operator|.
name|get
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Refresh session
name|sessionClient
operator|.
name|renewSession
argument_list|(
name|sessionId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

