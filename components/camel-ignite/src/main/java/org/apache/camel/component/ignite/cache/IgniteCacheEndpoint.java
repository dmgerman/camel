begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite.cache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ignite
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|Cache
operator|.
name|Entry
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
name|CamelException
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
name|Producer
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
name|ignite
operator|.
name|AbstractIgniteEndpoint
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
name|ignite
operator|.
name|IgniteComponent
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
name|UriEndpoint
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
name|UriPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|IgniteCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|cache
operator|.
name|CacheEntryEventSerializableFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|cache
operator|.
name|CachePeekMode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|cache
operator|.
name|query
operator|.
name|ContinuousQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|cache
operator|.
name|query
operator|.
name|Query
import|;
end_import

begin_comment
comment|/**  * The Ignite Cache endpoint is one of camel-ignite endpoints which allows you to interact with  * an<a href="https://apacheignite.readme.io/docs/data-grid">Ignite Cache</a>.  * This offers both a Producer (to invoke cache operations on an Ignite cache) and  * a Consumer (to consume changes from a continuous query).  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.17.0"
argument_list|,
name|scheme
operator|=
literal|"ignite-cache"
argument_list|,
name|title
operator|=
literal|"Ignite Cache"
argument_list|,
name|syntax
operator|=
literal|"ignite-cache:cacheName"
argument_list|,
name|label
operator|=
literal|"nosql,cache,compute"
argument_list|,
name|consumerClass
operator|=
name|IgniteCacheContinuousQueryConsumer
operator|.
name|class
argument_list|)
DECL|class|IgniteCacheEndpoint
specifier|public
class|class
name|IgniteCacheEndpoint
extends|extends
name|AbstractIgniteEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
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
argument_list|)
DECL|field|operation
specifier|private
name|IgniteCacheOperation
name|operation
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
literal|"false"
argument_list|)
DECL|field|failIfInexistentCache
specifier|private
name|boolean
name|failIfInexistentCache
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
literal|"ALL"
argument_list|)
DECL|field|cachePeekMode
specifier|private
name|CachePeekMode
name|cachePeekMode
init|=
name|CachePeekMode
operator|.
name|ALL
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer,consumer"
argument_list|)
DECL|field|query
specifier|private
name|Query
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|query
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|remoteFilter
specifier|private
name|CacheEntryEventSerializableFilter
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|remoteFilter
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
DECL|field|oneExchangePerUpdate
specifier|private
name|boolean
name|oneExchangePerUpdate
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
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|fireExistingQueryResults
specifier|private
name|boolean
name|fireExistingQueryResults
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
argument_list|,
name|defaultValueNote
operator|=
literal|"ContinuousQuery.DFLT_AUTO_UNSUBSCRIBE"
argument_list|)
DECL|field|autoUnsubscribe
specifier|private
name|boolean
name|autoUnsubscribe
init|=
name|ContinuousQuery
operator|.
name|DFLT_AUTO_UNSUBSCRIBE
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
literal|"1"
argument_list|,
name|defaultValueNote
operator|=
literal|"ContinuousQuery.DFLT_PAGE_SIZE"
argument_list|)
DECL|field|pageSize
specifier|private
name|int
name|pageSize
init|=
name|ContinuousQuery
operator|.
name|DFLT_PAGE_SIZE
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
literal|"0"
argument_list|,
name|defaultValueNote
operator|=
literal|"ContinuousQuery.DFLT_TIME_INTERVAL"
argument_list|)
DECL|field|timeInterval
specifier|private
name|long
name|timeInterval
init|=
name|ContinuousQuery
operator|.
name|DFLT_TIME_INTERVAL
decl_stmt|;
annotation|@
name|Deprecated
DECL|method|IgniteCacheEndpoint (String endpointUri, URI remainingUri, Map<String, Object> parameters, IgniteComponent igniteComponent)
specifier|public
name|IgniteCacheEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|URI
name|remainingUri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|IgniteComponent
name|igniteComponent
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|igniteComponent
argument_list|)
expr_stmt|;
name|cacheName
operator|=
name|remainingUri
operator|.
name|getHost
argument_list|()
expr_stmt|;
block|}
DECL|method|IgniteCacheEndpoint (String endpointUri, String remaining, Map<String, Object> parameters, IgniteCacheComponent igniteComponent)
specifier|public
name|IgniteCacheEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|IgniteCacheComponent
name|igniteComponent
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|igniteComponent
argument_list|)
expr_stmt|;
name|cacheName
operator|=
name|remaining
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|IgniteCacheProducer
argument_list|(
name|this
argument_list|,
name|obtainCache
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|IgniteCacheContinuousQueryConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|obtainCache
argument_list|()
argument_list|)
return|;
block|}
DECL|method|obtainCache ()
specifier|private
name|IgniteCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|obtainCache
parameter_list|()
throws|throws
name|CamelException
block|{
name|IgniteCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|ignite
argument_list|()
operator|.
name|cache
argument_list|(
name|cacheName
argument_list|)
decl_stmt|;
if|if
condition|(
name|cache
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|failIfInexistentCache
condition|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Ignite cache %s doesn't exist, and failIfInexistentCache is true"
argument_list|,
name|cacheName
argument_list|)
argument_list|)
throw|;
block|}
name|cache
operator|=
name|ignite
argument_list|()
operator|.
name|createCache
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
block|}
return|return
name|cache
return|;
block|}
comment|/**      * Gets the cache name.      *       * @return      */
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
comment|/**      * The cache name.      *       * @param cacheName cache name      */
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
comment|/**      * Gets the cache operation to invoke.      *       * @return cache name      */
DECL|method|getOperation ()
specifier|public
name|IgniteCacheOperation
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * The cache operation to invoke.      *<p>Possible values: GET, PUT, REMOVE, SIZE, REBALANCE, QUERY, CLEAR.</p>      *       * @param operation      */
DECL|method|setOperation (IgniteCacheOperation operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|IgniteCacheOperation
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
comment|/**      * Whether to fail the initialization if the cache doesn't exist.      *       * @return      */
DECL|method|isFailIfInexistentCache ()
specifier|public
name|boolean
name|isFailIfInexistentCache
parameter_list|()
block|{
return|return
name|failIfInexistentCache
return|;
block|}
comment|/**      * Whether to fail the initialization if the cache doesn't exist.      *       * @param failIfInexistentCache      */
DECL|method|setFailIfInexistentCache (boolean failIfInexistentCache)
specifier|public
name|void
name|setFailIfInexistentCache
parameter_list|(
name|boolean
name|failIfInexistentCache
parameter_list|)
block|{
name|this
operator|.
name|failIfInexistentCache
operator|=
name|failIfInexistentCache
expr_stmt|;
block|}
comment|/**      * Gets the {@link CachePeekMode}, only needed for operations that require it ({@link IgniteCacheOperation#SIZE}).      *       * @return      */
DECL|method|getCachePeekMode ()
specifier|public
name|CachePeekMode
name|getCachePeekMode
parameter_list|()
block|{
return|return
name|cachePeekMode
return|;
block|}
comment|/**      * The {@link CachePeekMode}, only needed for operations that require it ({@link IgniteCacheOperation#SIZE}).      *       * @param cachePeekMode      */
DECL|method|setCachePeekMode (CachePeekMode cachePeekMode)
specifier|public
name|void
name|setCachePeekMode
parameter_list|(
name|CachePeekMode
name|cachePeekMode
parameter_list|)
block|{
name|this
operator|.
name|cachePeekMode
operator|=
name|cachePeekMode
expr_stmt|;
block|}
comment|/**      * Gets the query to execute, only needed for operations that require it,       * and for the Continuous Query Consumer.      *       * @return      */
DECL|method|getQuery ()
specifier|public
name|Query
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
comment|/**      * The {@link Query} to execute, only needed for operations that require it,      * and for the Continuous Query Consumer.      *       * @param query      */
DECL|method|setQuery (Query<Entry<Object, Object>> query)
specifier|public
name|void
name|setQuery
parameter_list|(
name|Query
argument_list|<
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
comment|/**      * Gets the remote filter, only used by the Continuous Query Consumer.      *       * @return      */
DECL|method|getRemoteFilter ()
specifier|public
name|CacheEntryEventSerializableFilter
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|getRemoteFilter
parameter_list|()
block|{
return|return
name|remoteFilter
return|;
block|}
comment|/**      * The remote filter, only used by the Continuous Query Consumer.      *       * @param remoteFilter      */
DECL|method|setRemoteFilter (CacheEntryEventSerializableFilter<Object, Object> remoteFilter)
specifier|public
name|void
name|setRemoteFilter
parameter_list|(
name|CacheEntryEventSerializableFilter
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|remoteFilter
parameter_list|)
block|{
name|this
operator|.
name|remoteFilter
operator|=
name|remoteFilter
expr_stmt|;
block|}
comment|/**      * Gets whether to pack each update in an individual Exchange, even if multiple updates are      * received in one batch. Only used by the Continuous Query Consumer.      *       * @return      */
DECL|method|isOneExchangePerUpdate ()
specifier|public
name|boolean
name|isOneExchangePerUpdate
parameter_list|()
block|{
return|return
name|oneExchangePerUpdate
return|;
block|}
comment|/**      * Whether to pack each update in an individual Exchange, even if multiple updates are      * received in one batch. Only used by the Continuous Query Consumer.      *       * @param oneExchangePerUpdate      */
DECL|method|setOneExchangePerUpdate (boolean oneExchangePerUpdate)
specifier|public
name|void
name|setOneExchangePerUpdate
parameter_list|(
name|boolean
name|oneExchangePerUpdate
parameter_list|)
block|{
name|this
operator|.
name|oneExchangePerUpdate
operator|=
name|oneExchangePerUpdate
expr_stmt|;
block|}
comment|/**      * Gets whether auto unsubscribe is enabled in the Continuous Query Consumer.      *       * @return      */
DECL|method|isAutoUnsubscribe ()
specifier|public
name|boolean
name|isAutoUnsubscribe
parameter_list|()
block|{
return|return
name|autoUnsubscribe
return|;
block|}
comment|/**      * Whether auto unsubscribe is enabled in the Continuous Query Consumer.      *       * @param autoUnsubscribe      */
DECL|method|setAutoUnsubscribe (boolean autoUnsubscribe)
specifier|public
name|void
name|setAutoUnsubscribe
parameter_list|(
name|boolean
name|autoUnsubscribe
parameter_list|)
block|{
name|this
operator|.
name|autoUnsubscribe
operator|=
name|autoUnsubscribe
expr_stmt|;
block|}
comment|/**      * Gets the page size. Only used by the Continuous Query Consumer.      *       * @return      */
DECL|method|getPageSize ()
specifier|public
name|int
name|getPageSize
parameter_list|()
block|{
return|return
name|pageSize
return|;
block|}
comment|/**      * The page size. Only used by the Continuous Query Consumer.      *       * @param pageSize      */
DECL|method|setPageSize (int pageSize)
specifier|public
name|void
name|setPageSize
parameter_list|(
name|int
name|pageSize
parameter_list|)
block|{
name|this
operator|.
name|pageSize
operator|=
name|pageSize
expr_stmt|;
block|}
comment|/**      * Gets whether to process existing results that match the query. Used on initialization of       * the Continuous Query Consumer.      *       * @return      */
DECL|method|isFireExistingQueryResults ()
specifier|public
name|boolean
name|isFireExistingQueryResults
parameter_list|()
block|{
return|return
name|fireExistingQueryResults
return|;
block|}
comment|/**      * Whether to process existing results that match the query. Used on initialization of       * the Continuous Query Consumer.      *       * @param fireExistingQueryResults      */
DECL|method|setFireExistingQueryResults (boolean fireExistingQueryResults)
specifier|public
name|void
name|setFireExistingQueryResults
parameter_list|(
name|boolean
name|fireExistingQueryResults
parameter_list|)
block|{
name|this
operator|.
name|fireExistingQueryResults
operator|=
name|fireExistingQueryResults
expr_stmt|;
block|}
comment|/**      * Gets the time interval for the Continuous Query Consumer.      *       * @return      */
DECL|method|getTimeInterval ()
specifier|public
name|long
name|getTimeInterval
parameter_list|()
block|{
return|return
name|timeInterval
return|;
block|}
comment|/**      * The time interval for the Continuous Query Consumer.      *       * @param timeInterval      */
DECL|method|setTimeInterval (long timeInterval)
specifier|public
name|void
name|setTimeInterval
parameter_list|(
name|long
name|timeInterval
parameter_list|)
block|{
name|this
operator|.
name|timeInterval
operator|=
name|timeInterval
expr_stmt|;
block|}
block|}
end_class

end_unit

