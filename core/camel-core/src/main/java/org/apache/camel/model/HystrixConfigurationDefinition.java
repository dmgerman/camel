begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|ThreadPoolExecutor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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

begin_comment
comment|/**  * Hystrix Circuit Breaker EIP configuration  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,routing,circuitbreaker"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"hystrixConfiguration"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|HystrixConfigurationDefinition
specifier|public
class|class
name|HystrixConfigurationDefinition
extends|extends
name|HystrixConfigurationCommon
block|{
DECL|field|DEFAULT_GROUP_KEY
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_GROUP_KEY
init|=
literal|"CamelHystrix"
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|parent
specifier|private
name|HystrixDefinition
name|parent
decl_stmt|;
DECL|method|HystrixConfigurationDefinition ()
specifier|public
name|HystrixConfigurationDefinition
parameter_list|()
block|{     }
DECL|method|HystrixConfigurationDefinition (HystrixDefinition parent)
specifier|public
name|HystrixConfigurationDefinition
parameter_list|(
name|HystrixDefinition
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Sets the group key to use. The default value is CamelHystrix.      */
DECL|method|groupKey (String groupKey)
specifier|public
name|HystrixConfigurationDefinition
name|groupKey
parameter_list|(
name|String
name|groupKey
parameter_list|)
block|{
name|setGroupKey
argument_list|(
name|groupKey
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the thread pool key to use. Will by default use the same value as groupKey has been configured to use.      */
DECL|method|threadPoolKey (String threadPoolKey)
specifier|public
name|HystrixConfigurationDefinition
name|threadPoolKey
parameter_list|(
name|String
name|threadPoolKey
parameter_list|)
block|{
name|setThreadPoolKey
argument_list|(
name|threadPoolKey
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether to use a HystrixCircuitBreaker or not. If false no circuit-breaker logic will be used and all requests permitted.      *<p>      * This is similar in effect to circuitBreakerForceClosed() except that continues tracking metrics and knowing whether it      * should be open/closed, this property results in not even instantiating a circuit-breaker.      */
DECL|method|circuitBreakerEnabled (Boolean circuitBreakerEnabled)
specifier|public
name|HystrixConfigurationDefinition
name|circuitBreakerEnabled
parameter_list|(
name|Boolean
name|circuitBreakerEnabled
parameter_list|)
block|{
name|setCircuitBreakerEnabled
argument_list|(
name|circuitBreakerEnabled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Error percentage threshold (as whole number such as 50) at which point the circuit breaker will trip open and reject requests.      *<p>      * It will stay tripped for the duration defined in circuitBreakerSleepWindowInMilliseconds;      *<p>      * The error percentage this is compared against comes from HystrixCommandMetrics.getHealthCounts().      */
DECL|method|circuitBreakerErrorThresholdPercentage (Integer circuitBreakerErrorThresholdPercentage)
specifier|public
name|HystrixConfigurationDefinition
name|circuitBreakerErrorThresholdPercentage
parameter_list|(
name|Integer
name|circuitBreakerErrorThresholdPercentage
parameter_list|)
block|{
name|setCircuitBreakerErrorThresholdPercentage
argument_list|(
name|circuitBreakerErrorThresholdPercentage
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * If true the HystrixCircuitBreaker.allowRequest() will always return true to allow requests regardless of      * the error percentage from HystrixCommandMetrics.getHealthCounts().      *<p>      * The circuitBreakerForceOpen() property takes precedence so if it set to true this property does nothing.      */
DECL|method|circuitBreakerForceClosed (Boolean circuitBreakerForceClosed)
specifier|public
name|HystrixConfigurationDefinition
name|circuitBreakerForceClosed
parameter_list|(
name|Boolean
name|circuitBreakerForceClosed
parameter_list|)
block|{
name|setCircuitBreakerForceClosed
argument_list|(
name|circuitBreakerForceClosed
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * If true the HystrixCircuitBreaker.allowRequest() will always return false, causing the circuit to be open (tripped) and reject all requests.      *<p>      * This property takes precedence over circuitBreakerForceClosed();      */
DECL|method|circuitBreakerForceOpen (Boolean circuitBreakerForceOpen)
specifier|public
name|HystrixConfigurationDefinition
name|circuitBreakerForceOpen
parameter_list|(
name|Boolean
name|circuitBreakerForceOpen
parameter_list|)
block|{
name|setCircuitBreakerForceOpen
argument_list|(
name|circuitBreakerForceOpen
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Minimum number of requests in the metricsRollingStatisticalWindowInMilliseconds() that must exist before the HystrixCircuitBreaker will trip.      *<p>      * If below this number the circuit will not trip regardless of error percentage.      */
DECL|method|circuitBreakerRequestVolumeThreshold (Integer circuitBreakerRequestVolumeThreshold)
specifier|public
name|HystrixConfigurationDefinition
name|circuitBreakerRequestVolumeThreshold
parameter_list|(
name|Integer
name|circuitBreakerRequestVolumeThreshold
parameter_list|)
block|{
name|setCircuitBreakerRequestVolumeThreshold
argument_list|(
name|circuitBreakerRequestVolumeThreshold
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The time in milliseconds after a HystrixCircuitBreaker trips open that it should wait before trying requests again.      */
DECL|method|circuitBreakerSleepWindowInMilliseconds (Integer circuitBreakerSleepWindowInMilliseconds)
specifier|public
name|HystrixConfigurationDefinition
name|circuitBreakerSleepWindowInMilliseconds
parameter_list|(
name|Integer
name|circuitBreakerSleepWindowInMilliseconds
parameter_list|)
block|{
name|setCircuitBreakerSleepWindowInMilliseconds
argument_list|(
name|circuitBreakerSleepWindowInMilliseconds
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Number of concurrent requests permitted to HystrixCommand.run(). Requests beyond the concurrent limit will be rejected.      *<p>      * Applicable only when executionIsolationStrategy is SEMAPHORE.      */
DECL|method|executionIsolationSemaphoreMaxConcurrentRequests (Integer executionIsolationSemaphoreMaxConcurrentRequests)
specifier|public
name|HystrixConfigurationDefinition
name|executionIsolationSemaphoreMaxConcurrentRequests
parameter_list|(
name|Integer
name|executionIsolationSemaphoreMaxConcurrentRequests
parameter_list|)
block|{
name|setExecutionIsolationSemaphoreMaxConcurrentRequests
argument_list|(
name|executionIsolationSemaphoreMaxConcurrentRequests
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * What isolation strategy HystrixCommand.run() will be executed with.      *<p>      * If THREAD then it will be executed on a separate thread and concurrent requests limited by the number of threads in the thread-pool.      *<p>      * If SEMAPHORE then it will be executed on the calling thread and concurrent requests limited by the semaphore count.      */
DECL|method|executionIsolationStrategy (String executionIsolationStrategy)
specifier|public
name|HystrixConfigurationDefinition
name|executionIsolationStrategy
parameter_list|(
name|String
name|executionIsolationStrategy
parameter_list|)
block|{
name|setExecutionIsolationStrategy
argument_list|(
name|executionIsolationStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether the execution thread should attempt an interrupt (using Future cancel) when a thread times out.      *<p>      * Applicable only when executionIsolationStrategy() is set to THREAD.      */
DECL|method|executionIsolationThreadInterruptOnTimeout (Boolean executionIsolationThreadInterruptOnTimeout)
specifier|public
name|HystrixConfigurationDefinition
name|executionIsolationThreadInterruptOnTimeout
parameter_list|(
name|Boolean
name|executionIsolationThreadInterruptOnTimeout
parameter_list|)
block|{
name|setExecutionIsolationThreadInterruptOnTimeout
argument_list|(
name|executionIsolationThreadInterruptOnTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Time in milliseconds at which point the command will timeout and halt execution.      *<p>      * If executionIsolationThreadInterruptOnTimeout is true and the command is thread-isolated, the executing thread will be interrupted.      * If the command is semaphore-isolated and a HystrixObservableCommand, that command will get unsubscribed.      */
DECL|method|executionTimeoutInMilliseconds (Integer executionTimeoutInMilliseconds)
specifier|public
name|HystrixConfigurationDefinition
name|executionTimeoutInMilliseconds
parameter_list|(
name|Integer
name|executionTimeoutInMilliseconds
parameter_list|)
block|{
name|setExecutionTimeoutInMilliseconds
argument_list|(
name|executionTimeoutInMilliseconds
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether the timeout mechanism is enabled for this command      */
DECL|method|executionTimeoutEnabled (Boolean executionTimeoutEnabled)
specifier|public
name|HystrixConfigurationDefinition
name|executionTimeoutEnabled
parameter_list|(
name|Boolean
name|executionTimeoutEnabled
parameter_list|)
block|{
name|setExecutionTimeoutEnabled
argument_list|(
name|executionTimeoutEnabled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Number of concurrent requests permitted to HystrixCommand.getFallback().      * Requests beyond the concurrent limit will fail-fast and not attempt retrieving a fallback.      */
DECL|method|fallbackIsolationSemaphoreMaxConcurrentRequests (Integer fallbackIsolationSemaphoreMaxConcurrentRequests)
specifier|public
name|HystrixConfigurationDefinition
name|fallbackIsolationSemaphoreMaxConcurrentRequests
parameter_list|(
name|Integer
name|fallbackIsolationSemaphoreMaxConcurrentRequests
parameter_list|)
block|{
name|setFallbackIsolationSemaphoreMaxConcurrentRequests
argument_list|(
name|fallbackIsolationSemaphoreMaxConcurrentRequests
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether HystrixCommand.getFallback() should be attempted when failure occurs.      */
DECL|method|fallbackEnabled (Boolean fallbackEnabled)
specifier|public
name|HystrixConfigurationDefinition
name|fallbackEnabled
parameter_list|(
name|Boolean
name|fallbackEnabled
parameter_list|)
block|{
name|setFallbackEnabled
argument_list|(
name|fallbackEnabled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Time in milliseconds to wait between allowing health snapshots to be taken that calculate success and error      * percentages and affect HystrixCircuitBreaker.isOpen() status.      *<p>      * On high-volume circuits the continual calculation of error percentage can become CPU intensive thus this controls how often it is calculated.      */
DECL|method|metricsHealthSnapshotIntervalInMilliseconds (Integer metricsHealthSnapshotIntervalInMilliseconds)
specifier|public
name|HystrixConfigurationDefinition
name|metricsHealthSnapshotIntervalInMilliseconds
parameter_list|(
name|Integer
name|metricsHealthSnapshotIntervalInMilliseconds
parameter_list|)
block|{
name|setMetricsHealthSnapshotIntervalInMilliseconds
argument_list|(
name|metricsHealthSnapshotIntervalInMilliseconds
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Maximum number of values stored in each bucket of the rolling percentile.      * This is passed into HystrixRollingPercentile inside HystrixCommandMetrics.      */
DECL|method|metricsRollingPercentileBucketSize (Integer metricsRollingPercentileBucketSize)
specifier|public
name|HystrixConfigurationDefinition
name|metricsRollingPercentileBucketSize
parameter_list|(
name|Integer
name|metricsRollingPercentileBucketSize
parameter_list|)
block|{
name|setMetricsRollingPercentileBucketSize
argument_list|(
name|metricsRollingPercentileBucketSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether percentile metrics should be captured using HystrixRollingPercentile inside HystrixCommandMetrics.      */
DECL|method|metricsRollingPercentileEnabled (Boolean metricsRollingPercentileEnabled)
specifier|public
name|HystrixConfigurationDefinition
name|metricsRollingPercentileEnabled
parameter_list|(
name|Boolean
name|metricsRollingPercentileEnabled
parameter_list|)
block|{
name|setMetricsRollingPercentileEnabled
argument_list|(
name|metricsRollingPercentileEnabled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Duration of percentile rolling window in milliseconds.      * This is passed into HystrixRollingPercentile inside HystrixCommandMetrics.      */
DECL|method|metricsRollingPercentileWindowInMilliseconds (Integer metricsRollingPercentileWindowInMilliseconds)
specifier|public
name|HystrixConfigurationDefinition
name|metricsRollingPercentileWindowInMilliseconds
parameter_list|(
name|Integer
name|metricsRollingPercentileWindowInMilliseconds
parameter_list|)
block|{
name|setMetricsRollingPercentileWindowInMilliseconds
argument_list|(
name|metricsRollingPercentileWindowInMilliseconds
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Number of buckets the rolling percentile window is broken into.      * This is passed into HystrixRollingPercentile inside HystrixCommandMetrics.      */
DECL|method|metricsRollingPercentileWindowBuckets (Integer metricsRollingPercentileWindowBuckets)
specifier|public
name|HystrixConfigurationDefinition
name|metricsRollingPercentileWindowBuckets
parameter_list|(
name|Integer
name|metricsRollingPercentileWindowBuckets
parameter_list|)
block|{
name|setMetricsRollingPercentileWindowBuckets
argument_list|(
name|metricsRollingPercentileWindowBuckets
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * This property sets the duration of the statistical rolling window, in milliseconds. This is how long metrics are kept for the thread pool.      *      * The window is divided into buckets and ârollsâ by those increments.      */
DECL|method|metricsRollingStatisticalWindowInMilliseconds (Integer metricsRollingStatisticalWindowInMilliseconds)
specifier|public
name|HystrixConfigurationDefinition
name|metricsRollingStatisticalWindowInMilliseconds
parameter_list|(
name|Integer
name|metricsRollingStatisticalWindowInMilliseconds
parameter_list|)
block|{
name|setMetricsRollingStatisticalWindowInMilliseconds
argument_list|(
name|metricsRollingStatisticalWindowInMilliseconds
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Number of buckets the rolling statistical window is broken into.      * This is passed into HystrixRollingNumber inside HystrixCommandMetrics.      */
DECL|method|metricsRollingStatisticalWindowBuckets (Integer metricsRollingStatisticalWindowBuckets)
specifier|public
name|HystrixConfigurationDefinition
name|metricsRollingStatisticalWindowBuckets
parameter_list|(
name|Integer
name|metricsRollingStatisticalWindowBuckets
parameter_list|)
block|{
name|setMetricsRollingStatisticalWindowBuckets
argument_list|(
name|metricsRollingStatisticalWindowBuckets
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether HystrixCommand execution and events should be logged to HystrixRequestLog.      */
DECL|method|requestLogEnabled (Boolean requestLogEnabled)
specifier|public
name|HystrixConfigurationDefinition
name|requestLogEnabled
parameter_list|(
name|Boolean
name|requestLogEnabled
parameter_list|)
block|{
name|setRequestLogEnabled
argument_list|(
name|requestLogEnabled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Core thread-pool size.      */
DECL|method|corePoolSize (Integer corePoolSize)
specifier|public
name|HystrixConfigurationDefinition
name|corePoolSize
parameter_list|(
name|Integer
name|corePoolSize
parameter_list|)
block|{
name|setCorePoolSize
argument_list|(
name|corePoolSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Keep-alive time in minutes.      */
DECL|method|keepAliveTime (Integer keepAliveTime)
specifier|public
name|HystrixConfigurationDefinition
name|keepAliveTime
parameter_list|(
name|Integer
name|keepAliveTime
parameter_list|)
block|{
name|setKeepAliveTime
argument_list|(
name|keepAliveTime
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Max queue size.      *      * This should only affect the instantiation of the thread-pool - it is not eligible to change a queue size on the fly.      */
DECL|method|maxQueueSize (Integer maxQueueSize)
specifier|public
name|HystrixConfigurationDefinition
name|maxQueueSize
parameter_list|(
name|Integer
name|maxQueueSize
parameter_list|)
block|{
name|setMaxQueueSize
argument_list|(
name|maxQueueSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Maximum thread-pool size that gets passed to {@link ThreadPoolExecutor#setMaximumPoolSize(int)}.      * This is the maximum amount of concurrency that can be supported without starting to reject HystrixCommands.      * Please note that this setting only takes effect if you also set allowMaximumSizeToDivergeFromCoreSize      */
DECL|method|maximumSize (Integer maximumSize)
specifier|public
name|HystrixConfigurationDefinition
name|maximumSize
parameter_list|(
name|Integer
name|maximumSize
parameter_list|)
block|{
name|setMaximumSize
argument_list|(
name|maximumSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Queue size rejection threshold is an artificial max size at which rejections will occur even      * if maxQueueSize has not been reached. This is done because the maxQueueSize      * of a blocking queue can not be dynamically changed and we want to support dynamically      * changing the queue size that affects rejections.      *<p>      * This is used by HystrixCommand when queuing a thread for execution.      */
DECL|method|queueSizeRejectionThreshold (Integer queueSizeRejectionThreshold)
specifier|public
name|HystrixConfigurationDefinition
name|queueSizeRejectionThreshold
parameter_list|(
name|Integer
name|queueSizeRejectionThreshold
parameter_list|)
block|{
name|setQueueSizeRejectionThreshold
argument_list|(
name|queueSizeRejectionThreshold
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Duration of statistical rolling window in milliseconds.      * This is passed into HystrixRollingNumber inside each HystrixThreadPoolMetrics instance.      */
DECL|method|threadPoolRollingNumberStatisticalWindowInMilliseconds (Integer threadPoolRollingNumberStatisticalWindowInMilliseconds)
specifier|public
name|HystrixConfigurationDefinition
name|threadPoolRollingNumberStatisticalWindowInMilliseconds
parameter_list|(
name|Integer
name|threadPoolRollingNumberStatisticalWindowInMilliseconds
parameter_list|)
block|{
name|setThreadPoolRollingNumberStatisticalWindowInMilliseconds
argument_list|(
name|threadPoolRollingNumberStatisticalWindowInMilliseconds
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Number of buckets the rolling statistical window is broken into.      * This is passed into HystrixRollingNumber inside each HystrixThreadPoolMetrics instance.      */
DECL|method|threadPoolRollingNumberStatisticalWindowBuckets (Integer threadPoolRollingNumberStatisticalWindowBuckets)
specifier|public
name|HystrixConfigurationDefinition
name|threadPoolRollingNumberStatisticalWindowBuckets
parameter_list|(
name|Integer
name|threadPoolRollingNumberStatisticalWindowBuckets
parameter_list|)
block|{
name|setThreadPoolRollingNumberStatisticalWindowBuckets
argument_list|(
name|threadPoolRollingNumberStatisticalWindowBuckets
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Allows the configuration for maximumSize to take effect. That value can then be equal to, or higher, than coreSize      */
DECL|method|allowMaximumSizeToDivergeFromCoreSize (Boolean allowMaximumSizeToDivergeFromCoreSize)
specifier|public
name|HystrixConfigurationDefinition
name|allowMaximumSizeToDivergeFromCoreSize
parameter_list|(
name|Boolean
name|allowMaximumSizeToDivergeFromCoreSize
parameter_list|)
block|{
name|setAllowMaximumSizeToDivergeFromCoreSize
argument_list|(
name|allowMaximumSizeToDivergeFromCoreSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * End of configuration.      */
DECL|method|end ()
specifier|public
name|HystrixDefinition
name|end
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
block|}
end_class

end_unit

