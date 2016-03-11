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
name|infinispan
operator|.
name|commons
operator|.
name|api
operator|.
name|BasicCacheContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|context
operator|.
name|Flag
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
literal|"put,putAll,putIfAbsent,putAsync,putAllAsync,putIfAbsentAsync,"
operator|+
literal|"get,"
operator|+
literal|"containsKey,containsValue,"
operator|+
literal|"remove,removeAsync,"
operator|+
literal|"replace,replaceAsync,"
operator|+
literal|"size,"
operator|+
literal|"clear,clearAsync,"
operator|+
literal|"query"
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
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
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
DECL|field|clusteredListener
specifier|private
name|boolean
name|clusteredListener
decl_stmt|;
annotation|@
name|UriParam
DECL|field|queryBuilder
specifier|private
name|InfinispanQueryBuilder
name|queryBuilder
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|javaType
operator|=
literal|"java.lang.String"
argument_list|)
DECL|field|flags
specifier|private
name|Flag
index|[]
name|flags
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configurationUri
specifier|private
name|String
name|configurationUri
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
DECL|method|hasCommand ()
specifier|public
name|boolean
name|hasCommand
parameter_list|()
block|{
return|return
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|command
argument_list|)
return|;
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
DECL|method|isClusteredListener ()
specifier|public
name|boolean
name|isClusteredListener
parameter_list|()
block|{
return|return
name|clusteredListener
return|;
block|}
DECL|method|setClusteredListener (boolean clusteredListener)
specifier|public
name|void
name|setClusteredListener
parameter_list|(
name|boolean
name|clusteredListener
parameter_list|)
block|{
name|this
operator|.
name|clusteredListener
operator|=
name|clusteredListener
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
argument_list|<>
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
DECL|method|hasCustomListener ()
specifier|public
name|boolean
name|hasCustomListener
parameter_list|()
block|{
return|return
name|customListener
operator|!=
literal|null
return|;
block|}
DECL|method|getQueryBuilder ()
specifier|public
name|InfinispanQueryBuilder
name|getQueryBuilder
parameter_list|()
block|{
return|return
name|queryBuilder
return|;
block|}
comment|/**      * Specifies the query builder.      */
DECL|method|setQueryBuilder (InfinispanQueryBuilder queryBuilder)
specifier|public
name|void
name|setQueryBuilder
parameter_list|(
name|InfinispanQueryBuilder
name|queryBuilder
parameter_list|)
block|{
name|this
operator|.
name|queryBuilder
operator|=
name|queryBuilder
expr_stmt|;
block|}
DECL|method|hasQueryBuilder ()
specifier|public
name|boolean
name|hasQueryBuilder
parameter_list|()
block|{
return|return
name|queryBuilder
operator|!=
literal|null
return|;
block|}
DECL|method|getFlags ()
specifier|public
name|Flag
index|[]
name|getFlags
parameter_list|()
block|{
return|return
name|flags
return|;
block|}
comment|/**      * A comma separated list of Flag to be applied by default on each cache      * invocation, not applicable to remote caches.      */
DECL|method|setFlags (String flagsAsString)
specifier|public
name|void
name|setFlags
parameter_list|(
name|String
name|flagsAsString
parameter_list|)
block|{
name|String
index|[]
name|flagsArray
init|=
name|flagsAsString
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|this
operator|.
name|flags
operator|=
operator|new
name|Flag
index|[
name|flagsArray
operator|.
name|length
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|flagsArray
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|this
operator|.
name|flags
index|[
name|i
index|]
operator|=
name|Flag
operator|.
name|valueOf
argument_list|(
name|flagsArray
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setFlags (Flag... flags)
specifier|public
name|void
name|setFlags
parameter_list|(
name|Flag
modifier|...
name|flags
parameter_list|)
block|{
name|this
operator|.
name|flags
operator|=
name|flags
expr_stmt|;
block|}
DECL|method|hasFlags ()
specifier|public
name|boolean
name|hasFlags
parameter_list|()
block|{
return|return
name|flags
operator|!=
literal|null
operator|&&
name|flags
operator|.
name|length
operator|>
literal|0
return|;
block|}
comment|/**      * An implementation specific URI for the CacheManager      */
DECL|method|getConfigurationUri ()
specifier|public
name|String
name|getConfigurationUri
parameter_list|()
block|{
return|return
name|configurationUri
return|;
block|}
DECL|method|setConfigurationUri (String configurationUri)
specifier|public
name|void
name|setConfigurationUri
parameter_list|(
name|String
name|configurationUri
parameter_list|)
block|{
name|this
operator|.
name|configurationUri
operator|=
name|configurationUri
expr_stmt|;
block|}
block|}
end_class

end_unit

