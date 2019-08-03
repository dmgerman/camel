begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|engine
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|RejectedExecutionException
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
name|spi
operator|.
name|ConsumerCache
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
name|support
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
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|service
operator|.
name|ServiceSupport
import|;
end_import

begin_comment
comment|/**  * Cache containing created {@link org.apache.camel.Consumer}.  */
end_comment

begin_class
DECL|class|DefaultConsumerCache
specifier|public
class|class
name|DefaultConsumerCache
extends|extends
name|ServiceSupport
implements|implements
name|ConsumerCache
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|consumers
specifier|private
specifier|final
name|ServicePool
argument_list|<
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
DECL|method|DefaultConsumerCache (Object source, CamelContext camelContext, int cacheSize)
specifier|public
name|DefaultConsumerCache
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
operator|.
name|source
operator|=
name|source
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|maxCacheSize
operator|=
name|cacheSize
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
name|cacheSize
expr_stmt|;
name|this
operator|.
name|consumers
operator|=
operator|new
name|ServicePool
argument_list|<>
argument_list|(
name|Endpoint
operator|::
name|createPollingConsumer
argument_list|,
name|PollingConsumer
operator|::
name|getEndpoint
argument_list|,
name|maxCacheSize
argument_list|)
expr_stmt|;
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
comment|/**      * Releases an acquired producer back after usage.      *      * @param endpoint the endpoint      * @param pollingConsumer the pollingConsumer to release      */
annotation|@
name|Override
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
name|consumers
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|pollingConsumer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Acquires a pooled PollingConsumer which you<b>must</b> release back again after usage using the      * {@link #releasePollingConsumer(org.apache.camel.Endpoint, org.apache.camel.PollingConsumer)} method.      *      * @param endpoint the endpoint      * @return the PollingConsumer      */
annotation|@
name|Override
DECL|method|acquirePollingConsumer (Endpoint endpoint)
specifier|public
name|PollingConsumer
name|acquirePollingConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
try|try
block|{
name|PollingConsumer
name|consumer
init|=
name|consumers
operator|.
name|acquire
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|statistics
operator|!=
literal|null
condition|)
block|{
name|statistics
operator|.
name|onHit
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|consumer
return|;
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
block|}
annotation|@
name|Override
DECL|method|receive (Endpoint endpoint)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
if|if
condition|(
name|camelContext
operator|.
name|isStopped
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RejectedExecutionException
argument_list|(
literal|"CamelContext is stopped"
argument_list|)
throw|;
block|}
name|log
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
annotation|@
name|Override
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
if|if
condition|(
name|camelContext
operator|.
name|isStopped
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RejectedExecutionException
argument_list|(
literal|"CamelContext is stopped"
argument_list|)
throw|;
block|}
name|log
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
annotation|@
name|Override
DECL|method|receiveNoWait (Endpoint endpoint)
specifier|public
name|Exchange
name|receiveNoWait
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
if|if
condition|(
name|camelContext
operator|.
name|isStopped
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RejectedExecutionException
argument_list|(
literal|"CamelContext is stopped"
argument_list|)
throw|;
block|}
name|log
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
annotation|@
name|Override
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
annotation|@
name|Override
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
name|log
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
annotation|@
name|Override
DECL|method|getCapacity ()
specifier|public
name|int
name|getCapacity
parameter_list|()
block|{
return|return
name|consumers
operator|.
name|getMaxCacheSize
argument_list|()
return|;
block|}
comment|/**      * Gets the cache hits statistic      *<p/>      * Will return<tt>-1</tt> if it cannot determine this if a custom cache was used.      *      * @return the hits      */
annotation|@
name|Override
DECL|method|getHits ()
specifier|public
name|long
name|getHits
parameter_list|()
block|{
return|return
name|consumers
operator|.
name|getHits
argument_list|()
return|;
block|}
comment|/**      * Gets the cache misses statistic      *<p/>      * Will return<tt>-1</tt> if it cannot determine this if a custom cache was used.      *      * @return the misses      */
annotation|@
name|Override
DECL|method|getMisses ()
specifier|public
name|long
name|getMisses
parameter_list|()
block|{
return|return
name|consumers
operator|.
name|getMisses
argument_list|()
return|;
block|}
comment|/**      * Gets the cache evicted statistic      *<p/>      * Will return<tt>-1</tt> if it cannot determine this if a custom cache was used.      *      * @return the evicted      */
annotation|@
name|Override
DECL|method|getEvicted ()
specifier|public
name|long
name|getEvicted
parameter_list|()
block|{
return|return
name|consumers
operator|.
name|getEvicted
argument_list|()
return|;
block|}
comment|/**      * Resets the cache statistics      */
annotation|@
name|Override
DECL|method|resetCacheStatistics ()
specifier|public
name|void
name|resetCacheStatistics
parameter_list|()
block|{
name|consumers
operator|.
name|resetStatistics
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
comment|/**      * Purges this cache      */
annotation|@
name|Override
DECL|method|purge ()
specifier|public
specifier|synchronized
name|void
name|purge
parameter_list|()
block|{
try|try
block|{
name|consumers
operator|.
name|stop
argument_list|()
expr_stmt|;
name|consumers
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
name|log
operator|.
name|debug
argument_list|(
literal|"Error restarting consumer pool"
argument_list|,
name|e
argument_list|)
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
comment|/**      * Cleanup the cache (purging stale entries)      */
annotation|@
name|Override
DECL|method|cleanUp ()
specifier|public
name|void
name|cleanUp
parameter_list|()
block|{
name|consumers
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
name|startService
argument_list|(
name|consumers
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
comment|// when stopping we intend to shutdown
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|statistics
argument_list|,
name|consumers
argument_list|)
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

