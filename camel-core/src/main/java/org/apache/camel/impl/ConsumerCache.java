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
specifier|transient
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
block|}
comment|/**      * Creates the {@link LRUCache} to be used.      *<p/>      * This implementation returns a {@link LRUCache} instance.       * @param cacheSize the cache size      * @return the cache      */
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
operator|new
name|LRUCache
argument_list|<
name|String
argument_list|,
name|PollingConsumer
argument_list|>
argument_list|(
name|cacheSize
argument_list|)
return|;
block|}
DECL|method|getConsumer (Endpoint endpoint)
specifier|public
specifier|synchronized
name|PollingConsumer
name|getConsumer
parameter_list|(
name|Endpoint
name|endpoint
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
name|Exception
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
name|boolean
name|singleton
init|=
literal|true
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
name|getConsumer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
return|return
name|consumer
operator|.
name|receive
argument_list|()
return|;
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
name|getConsumer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
return|return
name|consumer
operator|.
name|receive
argument_list|(
name|timeout
argument_list|)
return|;
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
name|getConsumer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
return|return
name|consumer
operator|.
name|receiveNoWait
argument_list|()
return|;
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
name|consumers
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|consumers
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

