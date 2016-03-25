begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hystrix
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hystrix
package|;
end_package

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|HystrixCommand
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|HystrixCommandGroupKey
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|HystrixCommandKey
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|HystrixCommandProperties
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|HystrixRequestCache
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|HystrixThreadPoolKey
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|HystrixThreadPoolProperties
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|strategy
operator|.
name|HystrixPlugins
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|hystrix
operator|.
name|strategy
operator|.
name|concurrency
operator|.
name|HystrixRequestContext
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
name|impl
operator|.
name|DefaultProducer
import|;
end_import

begin_comment
comment|/**  * The Hystrix producer.  */
end_comment

begin_class
DECL|class|HystrixProducer
specifier|public
class|class
name|HystrixProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|configuration
specifier|private
name|HystrixConfiguration
name|configuration
decl_stmt|;
DECL|field|requestContext
specifier|private
name|HystrixRequestContext
name|requestContext
decl_stmt|;
DECL|method|HystrixProducer (HystrixEndpoint endpoint, HystrixConfiguration configuration)
specifier|public
name|HystrixProducer
parameter_list|(
name|HystrixEndpoint
name|endpoint
parameter_list|,
name|HystrixConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|HystrixCommand
operator|.
name|Setter
name|setter
init|=
name|HystrixCommand
operator|.
name|Setter
operator|.
name|withGroupKey
argument_list|(
name|HystrixCommandGroupKey
operator|.
name|Factory
operator|.
name|asKey
argument_list|(
name|configuration
operator|.
name|getGroupKey
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|setCommandPropertiesDefaults
argument_list|(
name|setter
argument_list|)
expr_stmt|;
name|setThreadPoolPropertiesDefaults
argument_list|(
name|setter
argument_list|)
expr_stmt|;
name|CamelHystrixCommand
name|camelHystrixCommand
init|=
operator|new
name|CamelHystrixCommand
argument_list|(
name|setter
argument_list|,
name|exchange
argument_list|,
name|getCacheKey
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|configuration
operator|.
name|getRunEndpointId
argument_list|()
argument_list|,
name|configuration
operator|.
name|getFallbackEndpointId
argument_list|()
argument_list|)
decl_stmt|;
name|checkRequestContextPresent
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|clearCache
argument_list|(
name|camelHystrixCommand
operator|.
name|getCommandKey
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|camelHystrixCommand
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
DECL|method|setCommandPropertiesDefaults (HystrixCommand.Setter setter)
specifier|private
name|void
name|setCommandPropertiesDefaults
parameter_list|(
name|HystrixCommand
operator|.
name|Setter
name|setter
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|.
name|getCommandKey
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setter
operator|.
name|andCommandKey
argument_list|(
name|HystrixCommandKey
operator|.
name|Factory
operator|.
name|asKey
argument_list|(
name|configuration
operator|.
name|getCommandKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|HystrixCommandProperties
operator|.
name|Setter
name|commandDefaults
init|=
name|HystrixCommandProperties
operator|.
name|Setter
argument_list|()
decl_stmt|;
name|setter
operator|.
name|andCommandPropertiesDefaults
argument_list|(
name|commandDefaults
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getCircuitBreakerEnabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withCircuitBreakerEnabled
argument_list|(
name|configuration
operator|.
name|getCircuitBreakerEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getCircuitBreakerErrorThresholdPercentage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withCircuitBreakerErrorThresholdPercentage
argument_list|(
name|configuration
operator|.
name|getCircuitBreakerErrorThresholdPercentage
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getCircuitBreakerForceClosed
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withCircuitBreakerForceClosed
argument_list|(
name|configuration
operator|.
name|getCircuitBreakerForceClosed
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getCircuitBreakerForceOpen
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withCircuitBreakerForceOpen
argument_list|(
name|configuration
operator|.
name|getCircuitBreakerForceOpen
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getCircuitBreakerRequestVolumeThreshold
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withCircuitBreakerRequestVolumeThreshold
argument_list|(
name|configuration
operator|.
name|getCircuitBreakerRequestVolumeThreshold
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getCircuitBreakerSleepWindowInMilliseconds
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withCircuitBreakerSleepWindowInMilliseconds
argument_list|(
name|configuration
operator|.
name|getCircuitBreakerSleepWindowInMilliseconds
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getExecutionIsolationSemaphoreMaxConcurrentRequests
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withExecutionIsolationSemaphoreMaxConcurrentRequests
argument_list|(
name|configuration
operator|.
name|getExecutionIsolationSemaphoreMaxConcurrentRequests
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getExecutionIsolationStrategy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withExecutionIsolationStrategy
argument_list|(
name|HystrixCommandProperties
operator|.
name|ExecutionIsolationStrategy
operator|.
name|valueOf
argument_list|(
name|configuration
operator|.
name|getExecutionIsolationStrategy
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getExecutionIsolationThreadInterruptOnTimeout
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withExecutionIsolationThreadInterruptOnTimeout
argument_list|(
name|configuration
operator|.
name|getExecutionIsolationThreadInterruptOnTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getExecutionTimeoutInMilliseconds
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withExecutionTimeoutInMilliseconds
argument_list|(
name|configuration
operator|.
name|getExecutionTimeoutInMilliseconds
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getExecutionTimeoutEnabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withExecutionTimeoutEnabled
argument_list|(
name|configuration
operator|.
name|getExecutionTimeoutEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getFallbackIsolationSemaphoreMaxConcurrentRequests
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withFallbackIsolationSemaphoreMaxConcurrentRequests
argument_list|(
name|configuration
operator|.
name|getFallbackIsolationSemaphoreMaxConcurrentRequests
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getFallbackEnabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withFallbackEnabled
argument_list|(
name|configuration
operator|.
name|getFallbackEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getMetricsHealthSnapshotIntervalInMilliseconds
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withMetricsHealthSnapshotIntervalInMilliseconds
argument_list|(
name|configuration
operator|.
name|getMetricsHealthSnapshotIntervalInMilliseconds
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getMetricsRollingPercentileBucketSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withMetricsRollingPercentileBucketSize
argument_list|(
name|configuration
operator|.
name|getMetricsRollingPercentileBucketSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getMetricsRollingPercentileEnabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withMetricsRollingPercentileEnabled
argument_list|(
name|configuration
operator|.
name|getMetricsRollingPercentileEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getMetricsRollingPercentileWindowInMilliseconds
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withMetricsRollingPercentileWindowInMilliseconds
argument_list|(
name|configuration
operator|.
name|getMetricsRollingPercentileWindowInMilliseconds
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getMetricsRollingPercentileWindowBuckets
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withMetricsRollingPercentileWindowBuckets
argument_list|(
name|configuration
operator|.
name|getMetricsRollingPercentileWindowBuckets
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getMetricsRollingStatisticalWindowInMilliseconds
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withMetricsRollingStatisticalWindowInMilliseconds
argument_list|(
name|configuration
operator|.
name|getMetricsRollingStatisticalWindowInMilliseconds
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getMetricsRollingStatisticalWindowBuckets
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withMetricsRollingStatisticalWindowBuckets
argument_list|(
name|configuration
operator|.
name|getMetricsRollingStatisticalWindowBuckets
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getRequestCacheEnabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withRequestCacheEnabled
argument_list|(
name|configuration
operator|.
name|getRequestCacheEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getRequestLogEnabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commandDefaults
operator|.
name|withRequestLogEnabled
argument_list|(
name|configuration
operator|.
name|getRequestLogEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setThreadPoolPropertiesDefaults (HystrixCommand.Setter setter)
specifier|private
name|void
name|setThreadPoolPropertiesDefaults
parameter_list|(
name|HystrixCommand
operator|.
name|Setter
name|setter
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|.
name|getThreadPoolKey
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setter
operator|.
name|andThreadPoolKey
argument_list|(
name|HystrixThreadPoolKey
operator|.
name|Factory
operator|.
name|asKey
argument_list|(
name|configuration
operator|.
name|getThreadPoolKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|HystrixThreadPoolProperties
operator|.
name|Setter
name|threadPoolProperties
init|=
name|HystrixThreadPoolProperties
operator|.
name|Setter
argument_list|()
decl_stmt|;
name|setter
operator|.
name|andThreadPoolPropertiesDefaults
argument_list|(
name|threadPoolProperties
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getCoreSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|threadPoolProperties
operator|.
name|withCoreSize
argument_list|(
name|configuration
operator|.
name|getCoreSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getKeepAliveTimeMinutes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|threadPoolProperties
operator|.
name|withKeepAliveTimeMinutes
argument_list|(
name|configuration
operator|.
name|getKeepAliveTimeMinutes
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getMaxQueueSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|threadPoolProperties
operator|.
name|withMaxQueueSize
argument_list|(
name|configuration
operator|.
name|getMaxQueueSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getQueueSizeRejectionThreshold
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|threadPoolProperties
operator|.
name|withQueueSizeRejectionThreshold
argument_list|(
name|configuration
operator|.
name|getQueueSizeRejectionThreshold
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getMetricsRollingStatisticalWindowInMilliseconds
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|threadPoolProperties
operator|.
name|withMetricsRollingStatisticalWindowInMilliseconds
argument_list|(
name|configuration
operator|.
name|getMetricsRollingStatisticalWindowInMilliseconds
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getMetricsRollingStatisticalWindowBuckets
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|threadPoolProperties
operator|.
name|withMetricsRollingStatisticalWindowBuckets
argument_list|(
name|configuration
operator|.
name|getMetricsRollingStatisticalWindowBuckets
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getCacheKey (Exchange exchange)
specifier|private
name|String
name|getCacheKey
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|configuration
operator|.
name|getCacheKeyExpression
argument_list|()
operator|!=
literal|null
condition|?
name|configuration
operator|.
name|getCacheKeyExpression
argument_list|()
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|checkRequestContextPresent (Exchange exchange)
specifier|private
specifier|synchronized
name|void
name|checkRequestContextPresent
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
operator|!
name|HystrixRequestContext
operator|.
name|isCurrentThreadInitialized
argument_list|()
condition|)
block|{
name|HystrixRequestContext
name|customRequestContext
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HystrixConstants
operator|.
name|CAMEL_HYSTRIX_REQUEST_CONTEXT_KEY
argument_list|,
name|HystrixRequestContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|customRequestContext
operator|!=
literal|null
condition|)
block|{
name|HystrixRequestContext
operator|.
name|setContextOnCurrentThread
argument_list|(
name|customRequestContext
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|HystrixRequestContext
operator|.
name|setContextOnCurrentThread
argument_list|(
name|requestContext
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HystrixConstants
operator|.
name|CAMEL_HYSTRIX_REQUEST_CONTEXT_KEY
argument_list|,
name|requestContext
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|clearCache (HystrixCommandKey camelHystrixCommand, Exchange exchange)
specifier|private
name|void
name|clearCache
parameter_list|(
name|HystrixCommandKey
name|camelHystrixCommand
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Boolean
name|clearCache
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HystrixConstants
operator|.
name|CAMEL_HYSTRIX_CLEAR_CACHE_FIRST
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|clearCache
operator|!=
literal|null
operator|&&
name|clearCache
condition|)
block|{
name|HystrixRequestCache
operator|.
name|getInstance
argument_list|(
name|camelHystrixCommand
argument_list|,
name|HystrixPlugins
operator|.
name|getInstance
argument_list|()
operator|.
name|getConcurrencyStrategy
argument_list|()
argument_list|)
operator|.
name|clear
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|getCacheKey
argument_list|(
name|exchange
argument_list|)
argument_list|)
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
if|if
condition|(
name|configuration
operator|.
name|getPropagateRequestContext
argument_list|()
operator|!=
literal|null
operator|&&
name|configuration
operator|.
name|getPropagateRequestContext
argument_list|()
condition|)
block|{
name|requestContext
operator|=
name|HystrixRequestContext
operator|.
name|initializeContext
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStart
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
if|if
condition|(
name|requestContext
operator|!=
literal|null
condition|)
block|{
name|requestContext
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

