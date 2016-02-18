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
name|Arrays
import|;
end_import

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
name|Set
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
name|Metadata
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
name|UriParam
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
name|UriParams
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
name|UriPath
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
name|BasicCacheContainer
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|InfinispanConfiguration
specifier|public
class|class
name|InfinispanConfiguration
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|UriParam
DECL|field|cacheContainer
specifier|private
name|BasicCacheContainer
name|cacheContainer
decl_stmt|;
annotation|@
name|UriParam
DECL|field|cacheName
specifier|private
name|String
name|cacheName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"put"
argument_list|,
name|enums
operator|=
literal|"put,putAll,putIfAbsent,putAsync,putAllAsync,putIfAbsentAsync,get,containsKey,containsValue,remove,removeAsync,"
operator|+
literal|"replace,replaceAsync,clear,clearAsync,size"
argument_list|)
DECL|field|command
specifier|private
name|String
name|command
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|sync
specifier|private
name|boolean
name|sync
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|javaType
operator|=
literal|"java.lang.String"
argument_list|)
DECL|field|eventTypes
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|eventTypes
decl_stmt|;
annotation|@
name|UriParam
DECL|field|customListener
specifier|private
name|InfinispanCustomListener
name|customListener
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|clustered
specifier|private
name|boolean
name|clustered
decl_stmt|;
DECL|method|getCommand ()
specifier|public
name|String
name|getCommand
parameter_list|()
block|{
return|return
name|command
return|;
block|}
comment|/**      * The operation to perform.      */
DECL|method|setCommand (String command)
specifier|public
name|void
name|setCommand
parameter_list|(
name|String
name|command
parameter_list|)
block|{
name|this
operator|.
name|command
operator|=
name|command
expr_stmt|;
block|}
comment|/**      * Specifies the host of the cache on Infinispan instance      */
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
comment|/**      * Specifies the cache Container to connect      */
DECL|method|getCacheContainer ()
specifier|public
name|BasicCacheContainer
name|getCacheContainer
parameter_list|()
block|{
return|return
name|cacheContainer
return|;
block|}
DECL|method|setCacheContainer (BasicCacheContainer cacheContainer)
specifier|public
name|void
name|setCacheContainer
parameter_list|(
name|BasicCacheContainer
name|cacheContainer
parameter_list|)
block|{
name|this
operator|.
name|cacheContainer
operator|=
name|cacheContainer
expr_stmt|;
block|}
comment|/**      * Specifies the cache name      */
DECL|method|getCacheName ()
specifier|public
name|String
name|getCacheName
parameter_list|()
block|{
return|return
name|cacheName
return|;
block|}
DECL|method|setCacheName (String cacheName)
specifier|public
name|void
name|setCacheName
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
comment|/**     * If true, the consumer will receive notifications synchronously     */
DECL|method|isSync ()
specifier|public
name|boolean
name|isSync
parameter_list|()
block|{
return|return
name|sync
return|;
block|}
DECL|method|setSync (boolean sync)
specifier|public
name|void
name|setSync
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{
name|this
operator|.
name|sync
operator|=
name|sync
expr_stmt|;
block|}
comment|/**      * If true, the listener will be installed for the entire cluster      */
DECL|method|isClustered ()
specifier|public
name|boolean
name|isClustered
parameter_list|()
block|{
return|return
name|clustered
return|;
block|}
DECL|method|setClustered (boolean clustered)
specifier|public
name|void
name|setClustered
parameter_list|(
name|boolean
name|clustered
parameter_list|)
block|{
name|this
operator|.
name|clustered
operator|=
name|clustered
expr_stmt|;
block|}
DECL|method|getEventTypes ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getEventTypes
parameter_list|()
block|{
return|return
name|eventTypes
return|;
block|}
comment|/**      * Specifies the set of event types to register by the consumer. Multiple event can be separated by comma.      *<p/>      * The possible event types are: CACHE_ENTRY_ACTIVATED, CACHE_ENTRY_PASSIVATED, CACHE_ENTRY_VISITED, CACHE_ENTRY_LOADED,      * CACHE_ENTRY_EVICTED, CACHE_ENTRY_CREATED, CACHE_ENTRY_REMOVED, CACHE_ENTRY_MODIFIED, TRANSACTION_COMPLETED,      * TRANSACTION_REGISTERED, CACHE_ENTRY_INVALIDATED, DATA_REHASHED, TOPOLOGY_CHANGED, PARTITION_STATUS_CHANGED      */
DECL|method|setEventTypes (Set<String> eventTypes)
specifier|public
name|void
name|setEventTypes
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|eventTypes
parameter_list|)
block|{
name|this
operator|.
name|eventTypes
operator|=
name|eventTypes
expr_stmt|;
block|}
comment|/**      * Specifies the set of event types to register by the consumer. Multiple event can be separated by comma.      *<p/>      * The possible event types are: CACHE_ENTRY_ACTIVATED, CACHE_ENTRY_PASSIVATED, CACHE_ENTRY_VISITED, CACHE_ENTRY_LOADED,      * CACHE_ENTRY_EVICTED, CACHE_ENTRY_CREATED, CACHE_ENTRY_REMOVED, CACHE_ENTRY_MODIFIED, TRANSACTION_COMPLETED,      * TRANSACTION_REGISTERED, CACHE_ENTRY_INVALIDATED, DATA_REHASHED, TOPOLOGY_CHANGED, PARTITION_STATUS_CHANGED      */
DECL|method|setEventTypes (String eventTypes)
specifier|public
name|void
name|setEventTypes
parameter_list|(
name|String
name|eventTypes
parameter_list|)
block|{
name|this
operator|.
name|eventTypes
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|eventTypes
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the custom listener in use, if provided      */
DECL|method|getCustomListener ()
specifier|public
name|InfinispanCustomListener
name|getCustomListener
parameter_list|()
block|{
return|return
name|customListener
return|;
block|}
DECL|method|setCustomListener (InfinispanCustomListener customListener)
specifier|public
name|void
name|setCustomListener
parameter_list|(
name|InfinispanCustomListener
name|customListener
parameter_list|)
block|{
name|this
operator|.
name|customListener
operator|=
name|customListener
expr_stmt|;
block|}
DECL|method|isCustom ()
specifier|public
name|boolean
name|isCustom
parameter_list|()
block|{
return|return
name|customListener
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

