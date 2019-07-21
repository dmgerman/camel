begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
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
name|Exchange
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
name|component
operator|.
name|infinispan
operator|.
name|embedded
operator|.
name|InfinispanConsumerEmbeddedHandler
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
name|infinispan
operator|.
name|remote
operator|.
name|InfinispanConsumerRemoteHandler
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
name|infinispan
operator|.
name|remote
operator|.
name|InfinispanRemoteOperation
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
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|RemoteCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|Search
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|commons
operator|.
name|api
operator|.
name|BasicCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|query
operator|.
name|api
operator|.
name|continuous
operator|.
name|ContinuousQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|query
operator|.
name|api
operator|.
name|continuous
operator|.
name|ContinuousQueryListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|query
operator|.
name|dsl
operator|.
name|Query
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
DECL|class|InfinispanConsumer
specifier|public
class|class
name|InfinispanConsumer
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
name|InfinispanProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|InfinispanConfiguration
name|configuration
decl_stmt|;
DECL|field|manager
specifier|private
specifier|final
name|InfinispanManager
name|manager
decl_stmt|;
DECL|field|cacheName
specifier|private
specifier|final
name|String
name|cacheName
decl_stmt|;
DECL|field|listener
specifier|private
name|InfinispanEventListener
name|listener
decl_stmt|;
DECL|field|consumerHandler
specifier|private
name|InfinispanConsumerHandler
name|consumerHandler
decl_stmt|;
DECL|field|cache
specifier|private
name|BasicCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
decl_stmt|;
DECL|field|continuousQuery
specifier|private
name|ContinuousQuery
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|continuousQuery
decl_stmt|;
DECL|method|InfinispanConsumer (InfinispanEndpoint endpoint, Processor processor, String cacheName, InfinispanManager manager, InfinispanConfiguration configuration)
specifier|public
name|InfinispanConsumer
parameter_list|(
name|InfinispanEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|cacheName
parameter_list|,
name|InfinispanManager
name|manager
parameter_list|,
name|InfinispanConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|cacheName
operator|=
name|cacheName
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
block|}
DECL|method|processEvent (String eventType, boolean isPre, String cacheName, Object key)
specifier|public
name|void
name|processEvent
parameter_list|(
name|String
name|eventType
parameter_list|,
name|boolean
name|isPre
parameter_list|,
name|String
name|cacheName
parameter_list|,
name|Object
name|key
parameter_list|)
block|{
name|processEvent
argument_list|(
name|eventType
argument_list|,
name|isPre
argument_list|,
name|cacheName
argument_list|,
name|key
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|processEvent (String eventType, boolean isPre, String cacheName, Object key, Object eventData)
specifier|public
name|void
name|processEvent
parameter_list|(
name|String
name|eventType
parameter_list|,
name|boolean
name|isPre
parameter_list|,
name|String
name|cacheName
parameter_list|,
name|Object
name|key
parameter_list|,
name|Object
name|eventData
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|EVENT_TYPE
argument_list|,
name|eventType
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|IS_PRE
argument_list|,
name|isPre
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|CACHE_NAME
argument_list|,
name|cacheName
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|eventData
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|InfinispanConstants
operator|.
name|EVENT_DATA
argument_list|,
name|eventData
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Error processing event "
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
name|manager
operator|.
name|start
argument_list|()
expr_stmt|;
name|cache
operator|=
name|manager
operator|.
name|getCache
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|hasQueryBuilder
argument_list|()
condition|)
block|{
if|if
condition|(
name|InfinispanUtil
operator|.
name|isRemote
argument_list|(
name|cache
argument_list|)
condition|)
block|{
name|RemoteCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|remoteCache
init|=
name|InfinispanUtil
operator|.
name|asRemote
argument_list|(
name|cache
argument_list|)
decl_stmt|;
name|Query
name|query
init|=
name|InfinispanRemoteOperation
operator|.
name|buildQuery
argument_list|(
name|configuration
operator|.
name|getQueryBuilder
argument_list|()
argument_list|,
name|remoteCache
argument_list|)
decl_stmt|;
name|continuousQuery
operator|=
name|Search
operator|.
name|getContinuousQuery
argument_list|(
name|remoteCache
argument_list|)
expr_stmt|;
name|continuousQuery
operator|.
name|addContinuousQueryListener
argument_list|(
name|query
argument_list|,
operator|new
name|ContinuousQueryEventListener
argument_list|(
name|cache
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Can't run continuous queries against embedded cache ("
operator|+
name|cache
operator|.
name|getName
argument_list|()
operator|+
literal|")"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|manager
operator|.
name|isCacheContainerEmbedded
argument_list|()
condition|)
block|{
name|consumerHandler
operator|=
name|InfinispanConsumerEmbeddedHandler
operator|.
name|INSTANCE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|manager
operator|.
name|isCacheContainerRemote
argument_list|()
condition|)
block|{
name|consumerHandler
operator|=
name|InfinispanConsumerRemoteHandler
operator|.
name|INSTANCE
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Unsupported CacheContainer type "
operator|+
name|manager
operator|.
name|getCacheContainer
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|listener
operator|=
name|consumerHandler
operator|.
name|start
argument_list|(
name|this
argument_list|)
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
name|continuousQuery
operator|!=
literal|null
condition|)
block|{
name|continuousQuery
operator|.
name|removeAllListeners
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|consumerHandler
operator|!=
literal|null
condition|)
block|{
name|consumerHandler
operator|.
name|stop
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|manager
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getCache ()
specifier|public
name|BasicCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|getCache
parameter_list|()
block|{
return|return
name|cache
return|;
block|}
DECL|method|getListener ()
specifier|public
name|InfinispanEventListener
name|getListener
parameter_list|()
block|{
return|return
name|listener
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|InfinispanConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|class|ContinuousQueryEventListener
specifier|private
class|class
name|ContinuousQueryEventListener
implements|implements
name|ContinuousQueryListener
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
block|{
DECL|field|cacheName
specifier|private
specifier|final
name|String
name|cacheName
decl_stmt|;
DECL|method|ContinuousQueryEventListener (String cacheName)
name|ContinuousQueryEventListener
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
name|this
operator|.
name|cacheName
operator|=
name|cacheName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resultJoining (Object key, Object value)
specifier|public
name|void
name|resultJoining
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|processEvent
argument_list|(
name|InfinispanConstants
operator|.
name|CACHE_ENTRY_JOINING
argument_list|,
literal|false
argument_list|,
name|cacheName
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resultUpdated (Object key, Object value)
specifier|public
name|void
name|resultUpdated
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|processEvent
argument_list|(
name|InfinispanConstants
operator|.
name|CACHE_ENTRY_UPDATED
argument_list|,
literal|false
argument_list|,
name|cacheName
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resultLeaving (Object key)
specifier|public
name|void
name|resultLeaving
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|processEvent
argument_list|(
name|InfinispanConstants
operator|.
name|CACHE_ENTRY_LEAVING
argument_list|,
literal|false
argument_list|,
name|cacheName
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

