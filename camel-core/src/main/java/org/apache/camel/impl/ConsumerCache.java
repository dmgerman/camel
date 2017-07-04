begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

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
name|FailedToCreateConsumerException
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
name|IsSingleton
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
name|PollingConsumer
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
name|ServicePoolAware
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
name|EndpointUtilizationStatistics
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
name|ServicePool
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
name|ServiceSupport
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
name|CamelContextHelper
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
name|LRUCache
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
name|LRUCacheFactory
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
comment|/**  * Cache containing created {@link org.apache.camel.Consumer}.  *  * @version   */
end_comment

begin_class
DECL|class|ConsumerCache
specifier|public
class|class
name|ConsumerCache
extends|extends
name|ServiceSupport
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
name|ConsumerCache
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|pool
specifier|private
specifier|final
name|ServicePool
argument_list|<
name|Endpoint
argument_list|,
name|PollingConsumer
argument_list|>
name|pool
decl_stmt|;
DECL|field|consumers
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
name|consumers
decl_stmt|;
DECL|field|source
specifier|private
specifier|final
name|Object
name|source
decl_stmt|;
DECL|field|statistics
specifier|private
name|EndpointUtilizationStatistics
name|statistics
decl_stmt|;
DECL|field|extendedStatistics
specifier|private
name|boolean
name|extendedStatistics
decl_stmt|;
DECL|field|maxCacheSize
specifier|private
name|int
name|maxCacheSize
decl_stmt|;
DECL|method|ConsumerCache (Object source, CamelContext camelContext)
specifier|public
name|ConsumerCache
parameter_list|(
name|Object
name|source
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
argument_list|(
name|source
argument_list|,
name|camelContext
argument_list|,
name|CamelContextHelper
operator|.
name|getMaximumCachePoolSize
argument_list|(
name|camelContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|ConsumerCache (Object source, CamelContext camelContext, int cacheSize)
specifier|public
name|ConsumerCache
parameter_list|(
name|Object
name|source
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|,
name|int
name|cacheSize
parameter_list|)
block|{
name|this
argument_list|(
name|source
argument_list|,
name|camelContext
argument_list|,
name|createLRUCache
argument_list|(
name|cacheSize
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|ConsumerCache (Object source, CamelContext camelContext, Map<String, PollingConsumer> cache)
specifier|public
name|ConsumerCache
parameter_list|(
name|Object
name|source
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
name|cache
parameter_list|)
block|{
name|this
argument_list|(
name|source
argument_list|,
name|camelContext
argument_list|,
name|cache
argument_list|,
name|camelContext
operator|.
name|getPollingConsumerServicePool
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|ConsumerCache (Object source, CamelContext camelContext, Map<String, PollingConsumer> cache, ServicePool<Endpoint, PollingConsumer> pool)
specifier|public
name|ConsumerCache
parameter_list|(
name|Object
name|source
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
name|cache
parameter_list|,
name|ServicePool
argument_list|<
name|Endpoint
argument_list|,
name|PollingConsumer
argument_list|>
name|pool
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|consumers
operator|=
name|cache
expr_stmt|;
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
name|this
operator|.
name|pool
operator|=
name|pool
expr_stmt|;
if|if
condition|(
name|consumers
operator|instanceof
name|LRUCache
condition|)
block|{
name|maxCacheSize
operator|=
operator|(
operator|(
name|LRUCache
operator|)
name|consumers
operator|)
operator|.
name|getMaxCacheSize
argument_list|()
expr_stmt|;
block|}
comment|// only if JMX is enabled
if|if
condition|(
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|extendedStatistics
operator|=
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getStatisticsLevel
argument_list|()
operator|.
name|isExtended
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|extendedStatistics
operator|=
literal|false
expr_stmt|;
block|}
block|}
DECL|method|isExtendedStatistics ()
specifier|public
name|boolean
name|isExtendedStatistics
parameter_list|()
block|{
return|return
name|extendedStatistics
return|;
block|}
comment|/**      * Whether extended JMX statistics is enabled for {@link org.apache.camel.spi.EndpointUtilizationStatistics}      */
DECL|method|setExtendedStatistics (boolean extendedStatistics)
specifier|public
name|void
name|setExtendedStatistics
parameter_list|(
name|boolean
name|extendedStatistics
parameter_list|)
block|{
name|this
operator|.
name|extendedStatistics
operator|=
name|extendedStatistics
expr_stmt|;
block|}
comment|/**      * Creates the {@link LRUCache} to be used.      *<p/>      * This implementation returns a {@link LRUCache} instance.       * @param cacheSize the cache size      * @return the cache      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createLRUCache (int cacheSize)
specifier|protected
specifier|static
name|LRUCache
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
name|createLRUCache
parameter_list|(
name|int
name|cacheSize
parameter_list|)
block|{
comment|// Use a regular cache as we want to ensure that the lifecycle of the consumers
comment|// being cache is properly handled, such as they are stopped when being evicted
comment|// or when this cache is stopped. This is needed as some consumers requires to
comment|// be stopped so they can shutdown internal resources that otherwise may cause leaks
return|return
name|LRUCacheFactory
operator|.
name|newLRUCache
argument_list|(
name|cacheSize
argument_list|)
return|;
block|}
comment|/**      * Acquires a pooled PollingConsumer which you<b>must</b> release back again after usage using the      * {@link #releasePollingConsumer(org.apache.camel.Endpoint, org.apache.camel.PollingConsumer)} method.      *      * @param endpoint the endpoint      * @return the PollingConsumer      */
DECL|method|acquirePollingConsumer (Endpoint endpoint)
specifier|public
name|PollingConsumer
name|acquirePollingConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|doGetPollingConsumer
argument_list|(
name|endpoint
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Releases an acquired producer back after usage.      *      * @param endpoint the endpoint      * @param pollingConsumer the pollingConsumer to release      */
DECL|method|releasePollingConsumer (Endpoint endpoint, PollingConsumer pollingConsumer)
specifier|public
name|void
name|releasePollingConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|PollingConsumer
name|pollingConsumer
parameter_list|)
block|{
if|if
condition|(
name|pollingConsumer
operator|instanceof
name|ServicePoolAware
condition|)
block|{
comment|// release back to the pool
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|pollingConsumer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|boolean
name|singleton
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|pollingConsumer
operator|instanceof
name|IsSingleton
condition|)
block|{
name|singleton
operator|=
operator|(
operator|(
name|IsSingleton
operator|)
name|pollingConsumer
operator|)
operator|.
name|isSingleton
argument_list|()
expr_stmt|;
block|}
name|String
name|key
init|=
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
name|boolean
name|cached
init|=
name|consumers
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|singleton
operator|||
operator|!
name|cached
condition|)
block|{
try|try
block|{
comment|// stop and shutdown non-singleton/non-cached consumers as we should not leak resources
if|if
condition|(
operator|!
name|singleton
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Released PollingConsumer: {} is stopped as consumer is not singleton"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Released PollingConsumer: {} is stopped as consumer cache is full"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|stopAndShutdownService
argument_list|(
name|pollingConsumer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|instanceof
name|RuntimeCamelException
condition|)
block|{
throw|throw
operator|(
name|RuntimeCamelException
operator|)
name|ex
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
DECL|method|getConsumer (Endpoint endpoint)
specifier|public
name|PollingConsumer
name|getConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|doGetPollingConsumer
argument_list|(
name|endpoint
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|doGetPollingConsumer (Endpoint endpoint, boolean pooled)
specifier|protected
specifier|synchronized
name|PollingConsumer
name|doGetPollingConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|boolean
name|pooled
parameter_list|)
block|{
name|String
name|key
init|=
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
name|PollingConsumer
name|answer
init|=
name|consumers
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|pooled
operator|&&
name|answer
operator|==
literal|null
condition|)
block|{
name|pool
operator|.
name|acquire
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|answer
operator|=
name|endpoint
operator|.
name|createPollingConsumer
argument_list|()
expr_stmt|;
name|answer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FailedToCreateConsumerException
argument_list|(
name|endpoint
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|pooled
operator|&&
name|answer
operator|instanceof
name|ServicePoolAware
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding to producer service pool with key: {} for producer: {}"
argument_list|,
name|endpoint
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|=
name|pool
operator|.
name|addAndAcquire
argument_list|(
name|endpoint
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|boolean
name|singleton
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|answer
operator|instanceof
name|IsSingleton
condition|)
block|{
name|singleton
operator|=
operator|(
operator|(
name|IsSingleton
operator|)
name|answer
operator|)
operator|.
name|isSingleton
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|singleton
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding to consumer cache with key: {} for consumer: {}"
argument_list|,
name|endpoint
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|consumers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Consumer for endpoint: {} is not singleton and thus not added to consumer cache"
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
comment|// record statistics
if|if
condition|(
name|extendedStatistics
condition|)
block|{
name|statistics
operator|.
name|onHit
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|receive (Endpoint endpoint)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"<<<< {}"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|PollingConsumer
name|consumer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|consumer
operator|=
name|acquirePollingConsumer
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|consumer
operator|.
name|receive
argument_list|()
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|consumer
operator|!=
literal|null
condition|)
block|{
name|releasePollingConsumer
argument_list|(
name|endpoint
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|receive (Endpoint endpoint, long timeout)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"<<<< {}"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|PollingConsumer
name|consumer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|consumer
operator|=
name|acquirePollingConsumer
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|consumer
operator|.
name|receive
argument_list|(
name|timeout
argument_list|)
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|consumer
operator|!=
literal|null
condition|)
block|{
name|releasePollingConsumer
argument_list|(
name|endpoint
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|receiveNoWait (Endpoint endpoint)
specifier|public
name|Exchange
name|receiveNoWait
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"<<<< {}"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|PollingConsumer
name|consumer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|consumer
operator|=
name|doGetPollingConsumer
argument_list|(
name|endpoint
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|consumer
operator|.
name|receiveNoWait
argument_list|()
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|consumer
operator|!=
literal|null
condition|)
block|{
name|releasePollingConsumer
argument_list|(
name|endpoint
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
comment|/**      * Gets the source which uses this cache      *      * @return the source      */
DECL|method|getSource ()
specifier|public
name|Object
name|getSource
parameter_list|()
block|{
return|return
name|source
return|;
block|}
comment|/**      * Returns the current size of the cache      *      * @return the current size      */
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
name|int
name|size
init|=
name|consumers
operator|.
name|size
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"size = {}"
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|size
return|;
block|}
comment|/**      * Gets the maximum cache size (capacity).      *<p/>      * Will return<tt>-1</tt> if it cannot determine this if a custom cache was used.      *      * @return the capacity      */
DECL|method|getCapacity ()
specifier|public
name|int
name|getCapacity
parameter_list|()
block|{
name|int
name|capacity
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|consumers
operator|instanceof
name|LRUCache
condition|)
block|{
name|LRUCache
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
name|cache
init|=
operator|(
name|LRUCache
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
operator|)
name|consumers
decl_stmt|;
name|capacity
operator|=
name|cache
operator|.
name|getMaxCacheSize
argument_list|()
expr_stmt|;
block|}
return|return
name|capacity
return|;
block|}
comment|/**      * Gets the cache hits statistic      *<p/>      * Will return<tt>-1</tt> if it cannot determine this if a custom cache was used.      *      * @return the hits      */
DECL|method|getHits ()
specifier|public
name|long
name|getHits
parameter_list|()
block|{
name|long
name|hits
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|consumers
operator|instanceof
name|LRUCache
condition|)
block|{
name|LRUCache
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
name|cache
init|=
operator|(
name|LRUCache
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
operator|)
name|consumers
decl_stmt|;
name|hits
operator|=
name|cache
operator|.
name|getHits
argument_list|()
expr_stmt|;
block|}
return|return
name|hits
return|;
block|}
comment|/**      * Gets the cache misses statistic      *<p/>      * Will return<tt>-1</tt> if it cannot determine this if a custom cache was used.      *      * @return the misses      */
DECL|method|getMisses ()
specifier|public
name|long
name|getMisses
parameter_list|()
block|{
name|long
name|misses
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|consumers
operator|instanceof
name|LRUCache
condition|)
block|{
name|LRUCache
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
name|cache
init|=
operator|(
name|LRUCache
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
operator|)
name|consumers
decl_stmt|;
name|misses
operator|=
name|cache
operator|.
name|getMisses
argument_list|()
expr_stmt|;
block|}
return|return
name|misses
return|;
block|}
comment|/**      * Gets the cache evicted statistic      *<p/>      * Will return<tt>-1</tt> if it cannot determine this if a custom cache was used.      *      * @return the evicted      */
DECL|method|getEvicted ()
specifier|public
name|long
name|getEvicted
parameter_list|()
block|{
name|long
name|evicted
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|consumers
operator|instanceof
name|LRUCache
condition|)
block|{
name|LRUCache
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
name|cache
init|=
operator|(
name|LRUCache
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
operator|)
name|consumers
decl_stmt|;
name|evicted
operator|=
name|cache
operator|.
name|getEvicted
argument_list|()
expr_stmt|;
block|}
return|return
name|evicted
return|;
block|}
comment|/**      * Resets the cache statistics      */
DECL|method|resetCacheStatistics ()
specifier|public
name|void
name|resetCacheStatistics
parameter_list|()
block|{
if|if
condition|(
name|consumers
operator|instanceof
name|LRUCache
condition|)
block|{
name|LRUCache
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
name|cache
init|=
operator|(
name|LRUCache
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
operator|)
name|consumers
decl_stmt|;
name|cache
operator|.
name|resetStatistics
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|statistics
operator|!=
literal|null
condition|)
block|{
name|statistics
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Purges this cache      */
DECL|method|purge ()
specifier|public
specifier|synchronized
name|void
name|purge
parameter_list|()
block|{
name|consumers
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|statistics
operator|!=
literal|null
condition|)
block|{
name|statistics
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Cleanup the cache (purging stale entries)      */
DECL|method|cleanUp ()
specifier|public
name|void
name|cleanUp
parameter_list|()
block|{
if|if
condition|(
name|consumers
operator|instanceof
name|LRUCache
condition|)
block|{
name|LRUCache
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
name|cache
init|=
operator|(
name|LRUCache
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
operator|)
name|consumers
decl_stmt|;
name|cache
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getEndpointUtilizationStatistics ()
specifier|public
name|EndpointUtilizationStatistics
name|getEndpointUtilizationStatistics
parameter_list|()
block|{
return|return
name|statistics
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
literal|"ConsumerCache for source: "
operator|+
name|source
operator|+
literal|", capacity: "
operator|+
name|getCapacity
argument_list|()
return|;
block|}
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
name|extendedStatistics
condition|)
block|{
name|int
name|max
init|=
name|maxCacheSize
operator|==
literal|0
condition|?
name|CamelContextHelper
operator|.
name|getMaximumCachePoolSize
argument_list|(
name|camelContext
argument_list|)
else|:
name|maxCacheSize
decl_stmt|;
name|statistics
operator|=
operator|new
name|DefaultEndpointUtilizationStatistics
argument_list|(
name|max
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|consumers
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// when stopping we intend to shutdown
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|statistics
argument_list|,
name|pool
argument_list|)
expr_stmt|;
try|try
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|consumers
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// ensure consumers are removed, and also from JMX
for|for
control|(
name|PollingConsumer
name|consumer
range|:
name|consumers
operator|.
name|values
argument_list|()
control|)
block|{
name|getCamelContext
argument_list|()
operator|.
name|removeService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
name|consumers
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|statistics
operator|!=
literal|null
condition|)
block|{
name|statistics
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

