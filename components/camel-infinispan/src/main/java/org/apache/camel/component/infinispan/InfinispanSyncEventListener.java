begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|notifications
operator|.
name|Listener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|notifications
operator|.
name|cachelistener
operator|.
name|annotation
operator|.
name|CacheEntryActivated
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|notifications
operator|.
name|cachelistener
operator|.
name|annotation
operator|.
name|CacheEntryCreated
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|notifications
operator|.
name|cachelistener
operator|.
name|annotation
operator|.
name|CacheEntryInvalidated
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|notifications
operator|.
name|cachelistener
operator|.
name|annotation
operator|.
name|CacheEntryLoaded
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|notifications
operator|.
name|cachelistener
operator|.
name|annotation
operator|.
name|CacheEntryModified
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|notifications
operator|.
name|cachelistener
operator|.
name|annotation
operator|.
name|CacheEntryPassivated
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|notifications
operator|.
name|cachelistener
operator|.
name|annotation
operator|.
name|CacheEntryRemoved
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|notifications
operator|.
name|cachelistener
operator|.
name|annotation
operator|.
name|CacheEntryVisited
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|notifications
operator|.
name|cachelistener
operator|.
name|event
operator|.
name|CacheEntryEvent
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
annotation|@
name|Listener
argument_list|(
name|sync
operator|=
literal|true
argument_list|)
DECL|class|InfinispanSyncEventListener
specifier|public
class|class
name|InfinispanSyncEventListener
block|{
DECL|field|logger
specifier|private
specifier|final
specifier|transient
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|infinispanConsumer
specifier|private
specifier|final
name|InfinispanConsumer
name|infinispanConsumer
decl_stmt|;
DECL|field|eventTypes
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|eventTypes
decl_stmt|;
DECL|method|InfinispanSyncEventListener (InfinispanConsumer infinispanConsumer, Set<String> eventTypes)
specifier|public
name|InfinispanSyncEventListener
parameter_list|(
name|InfinispanConsumer
name|infinispanConsumer
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|eventTypes
parameter_list|)
block|{
name|this
operator|.
name|infinispanConsumer
operator|=
name|infinispanConsumer
expr_stmt|;
name|this
operator|.
name|eventTypes
operator|=
name|eventTypes
expr_stmt|;
block|}
annotation|@
name|CacheEntryActivated
annotation|@
name|CacheEntryCreated
annotation|@
name|CacheEntryInvalidated
annotation|@
name|CacheEntryLoaded
annotation|@
name|CacheEntryModified
annotation|@
name|CacheEntryPassivated
annotation|@
name|CacheEntryRemoved
annotation|@
name|CacheEntryVisited
DECL|method|processEvent (CacheEntryEvent event)
specifier|public
name|void
name|processEvent
parameter_list|(
name|CacheEntryEvent
name|event
parameter_list|)
block|{
name|logger
operator|.
name|trace
argument_list|(
literal|"Received CacheEntryEvent [{}]"
argument_list|,
name|event
argument_list|)
expr_stmt|;
if|if
condition|(
name|eventTypes
operator|==
literal|null
operator|||
name|eventTypes
operator|.
name|isEmpty
argument_list|()
operator|||
name|eventTypes
operator|.
name|contains
argument_list|(
name|event
operator|.
name|getType
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|infinispanConsumer
operator|.
name|processEvent
argument_list|(
name|event
operator|.
name|getType
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|event
operator|.
name|isPre
argument_list|()
argument_list|,
name|event
operator|.
name|getCache
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|event
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

