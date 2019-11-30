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
name|BlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|XmlAttribute
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

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|HystrixConfigurationCommon
specifier|public
class|class
name|HystrixConfigurationCommon
extends|extends
name|IdentifiedType
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"CamelHystrix"
argument_list|)
DECL|field|groupKey
specifier|private
name|String
name|groupKey
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"CamelHystrix"
argument_list|)
DECL|field|threadPoolKey
specifier|private
name|String
name|threadPoolKey
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|circuitBreakerEnabled
specifier|private
name|String
name|circuitBreakerEnabled
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"50"
argument_list|)
DECL|field|circuitBreakerErrorThresholdPercentage
specifier|private
name|String
name|circuitBreakerErrorThresholdPercentage
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|circuitBreakerForceClosed
specifier|private
name|String
name|circuitBreakerForceClosed
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|circuitBreakerForceOpen
specifier|private
name|String
name|circuitBreakerForceOpen
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"20"
argument_list|)
DECL|field|circuitBreakerRequestVolumeThreshold
specifier|private
name|String
name|circuitBreakerRequestVolumeThreshold
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"5000"
argument_list|)
DECL|field|circuitBreakerSleepWindowInMilliseconds
specifier|private
name|String
name|circuitBreakerSleepWindowInMilliseconds
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"20"
argument_list|)
DECL|field|executionIsolationSemaphoreMaxConcurrentRequests
specifier|private
name|String
name|executionIsolationSemaphoreMaxConcurrentRequests
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"THREAD"
argument_list|,
name|enums
operator|=
literal|"THREAD,SEMAPHORE"
argument_list|)
DECL|field|executionIsolationStrategy
specifier|private
name|String
name|executionIsolationStrategy
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|executionIsolationThreadInterruptOnTimeout
specifier|private
name|String
name|executionIsolationThreadInterruptOnTimeout
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"1000"
argument_list|)
DECL|field|executionTimeoutInMilliseconds
specifier|private
name|String
name|executionTimeoutInMilliseconds
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|executionTimeoutEnabled
specifier|private
name|String
name|executionTimeoutEnabled
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"10"
argument_list|)
DECL|field|fallbackIsolationSemaphoreMaxConcurrentRequests
specifier|private
name|String
name|fallbackIsolationSemaphoreMaxConcurrentRequests
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|fallbackEnabled
specifier|private
name|String
name|fallbackEnabled
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"500"
argument_list|)
DECL|field|metricsHealthSnapshotIntervalInMilliseconds
specifier|private
name|String
name|metricsHealthSnapshotIntervalInMilliseconds
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"10"
argument_list|)
DECL|field|metricsRollingPercentileBucketSize
specifier|private
name|String
name|metricsRollingPercentileBucketSize
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|metricsRollingPercentileEnabled
specifier|private
name|String
name|metricsRollingPercentileEnabled
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"10000"
argument_list|)
DECL|field|metricsRollingPercentileWindowInMilliseconds
specifier|private
name|String
name|metricsRollingPercentileWindowInMilliseconds
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"6"
argument_list|)
DECL|field|metricsRollingPercentileWindowBuckets
specifier|private
name|String
name|metricsRollingPercentileWindowBuckets
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"10000"
argument_list|)
DECL|field|metricsRollingStatisticalWindowInMilliseconds
specifier|private
name|String
name|metricsRollingStatisticalWindowInMilliseconds
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"10"
argument_list|)
DECL|field|metricsRollingStatisticalWindowBuckets
specifier|private
name|String
name|metricsRollingStatisticalWindowBuckets
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"command"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|requestLogEnabled
specifier|private
name|String
name|requestLogEnabled
decl_stmt|;
comment|// thread-pool
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"threadpool"
argument_list|,
name|defaultValue
operator|=
literal|"10"
argument_list|)
DECL|field|corePoolSize
specifier|private
name|String
name|corePoolSize
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"threadpool"
argument_list|,
name|defaultValue
operator|=
literal|"10"
argument_list|)
DECL|field|maximumSize
specifier|private
name|String
name|maximumSize
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"threadpool"
argument_list|,
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|keepAliveTime
specifier|private
name|String
name|keepAliveTime
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"threadpool"
argument_list|,
name|defaultValue
operator|=
literal|"-1"
argument_list|)
DECL|field|maxQueueSize
specifier|private
name|String
name|maxQueueSize
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"threadpool"
argument_list|,
name|defaultValue
operator|=
literal|"5"
argument_list|)
DECL|field|queueSizeRejectionThreshold
specifier|private
name|String
name|queueSizeRejectionThreshold
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"threadpool"
argument_list|,
name|defaultValue
operator|=
literal|"10000"
argument_list|)
DECL|field|threadPoolRollingNumberStatisticalWindowInMilliseconds
specifier|private
name|String
name|threadPoolRollingNumberStatisticalWindowInMilliseconds
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"threadpool"
argument_list|,
name|defaultValue
operator|=
literal|"10"
argument_list|)
DECL|field|threadPoolRollingNumberStatisticalWindowBuckets
specifier|private
name|String
name|threadPoolRollingNumberStatisticalWindowBuckets
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"threadpool"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|allowMaximumSizeToDivergeFromCoreSize
specifier|private
name|String
name|allowMaximumSizeToDivergeFromCoreSize
decl_stmt|;
comment|// Getter/Setter
comment|// -------------------------------------------------------------------------
DECL|method|getGroupKey ()
specifier|public
name|String
name|getGroupKey
parameter_list|()
block|{
return|return
name|groupKey
return|;
block|}
comment|/**      * Sets the group key to use. The default value is CamelHystrix.      */
DECL|method|setGroupKey (String groupKey)
specifier|public
name|void
name|setGroupKey
parameter_list|(
name|String
name|groupKey
parameter_list|)
block|{
name|this
operator|.
name|groupKey
operator|=
name|groupKey
expr_stmt|;
block|}
DECL|method|getThreadPoolKey ()
specifier|public
name|String
name|getThreadPoolKey
parameter_list|()
block|{
return|return
name|threadPoolKey
return|;
block|}
comment|/**      * Sets the thread pool key to use. Will by default use the same value as      * groupKey has been configured to use.      */
DECL|method|setThreadPoolKey (String threadPoolKey)
specifier|public
name|void
name|setThreadPoolKey
parameter_list|(
name|String
name|threadPoolKey
parameter_list|)
block|{
name|this
operator|.
name|threadPoolKey
operator|=
name|threadPoolKey
expr_stmt|;
block|}
DECL|method|getCircuitBreakerEnabled ()
specifier|public
name|String
name|getCircuitBreakerEnabled
parameter_list|()
block|{
return|return
name|circuitBreakerEnabled
return|;
block|}
comment|/**      * Whether to use a HystrixCircuitBreaker or not. If false no      * circuit-breaker logic will be used and all requests permitted.      *<p>      * This is similar in effect to circuitBreakerForceClosed() except that      * continues tracking metrics and knowing whether it should be open/closed,      * this property results in not even instantiating a circuit-breaker.      */
DECL|method|setCircuitBreakerEnabled (String circuitBreakerEnabled)
specifier|public
name|void
name|setCircuitBreakerEnabled
parameter_list|(
name|String
name|circuitBreakerEnabled
parameter_list|)
block|{
name|this
operator|.
name|circuitBreakerEnabled
operator|=
name|circuitBreakerEnabled
expr_stmt|;
block|}
DECL|method|getCircuitBreakerErrorThresholdPercentage ()
specifier|public
name|String
name|getCircuitBreakerErrorThresholdPercentage
parameter_list|()
block|{
return|return
name|circuitBreakerErrorThresholdPercentage
return|;
block|}
comment|/**      * Error percentage threshold (as whole number such as 50) at which point      * the circuit breaker will trip open and reject requests.      *<p>      * It will stay tripped for the duration defined in      * circuitBreakerSleepWindowInMilliseconds;      *<p>      * The error percentage this is compared against comes from      * HystrixCommandMetrics.getHealthCounts().      */
DECL|method|setCircuitBreakerErrorThresholdPercentage (String circuitBreakerErrorThresholdPercentage)
specifier|public
name|void
name|setCircuitBreakerErrorThresholdPercentage
parameter_list|(
name|String
name|circuitBreakerErrorThresholdPercentage
parameter_list|)
block|{
name|this
operator|.
name|circuitBreakerErrorThresholdPercentage
operator|=
name|circuitBreakerErrorThresholdPercentage
expr_stmt|;
block|}
DECL|method|getCircuitBreakerForceClosed ()
specifier|public
name|String
name|getCircuitBreakerForceClosed
parameter_list|()
block|{
return|return
name|circuitBreakerForceClosed
return|;
block|}
comment|/**      * If true the HystrixCircuitBreaker#allowRequest() will always return true      * to allow requests regardless of the error percentage from      * HystrixCommandMetrics.getHealthCounts().      *<p>      * The circuitBreakerForceOpen() property takes precedence so if it set to      * true this property does nothing.      */
DECL|method|setCircuitBreakerForceClosed (String circuitBreakerForceClosed)
specifier|public
name|void
name|setCircuitBreakerForceClosed
parameter_list|(
name|String
name|circuitBreakerForceClosed
parameter_list|)
block|{
name|this
operator|.
name|circuitBreakerForceClosed
operator|=
name|circuitBreakerForceClosed
expr_stmt|;
block|}
DECL|method|getCircuitBreakerForceOpen ()
specifier|public
name|String
name|getCircuitBreakerForceOpen
parameter_list|()
block|{
return|return
name|circuitBreakerForceOpen
return|;
block|}
comment|/**      * If true the HystrixCircuitBreaker.allowRequest() will always return      * false, causing the circuit to be open (tripped) and reject all requests.      *<p>      * This property takes precedence over circuitBreakerForceClosed();      */
DECL|method|setCircuitBreakerForceOpen (String circuitBreakerForceOpen)
specifier|public
name|void
name|setCircuitBreakerForceOpen
parameter_list|(
name|String
name|circuitBreakerForceOpen
parameter_list|)
block|{
name|this
operator|.
name|circuitBreakerForceOpen
operator|=
name|circuitBreakerForceOpen
expr_stmt|;
block|}
DECL|method|getCircuitBreakerRequestVolumeThreshold ()
specifier|public
name|String
name|getCircuitBreakerRequestVolumeThreshold
parameter_list|()
block|{
return|return
name|circuitBreakerRequestVolumeThreshold
return|;
block|}
comment|/**      * Minimum number of requests in the      * metricsRollingStatisticalWindowInMilliseconds() that must exist before      * the HystrixCircuitBreaker will trip.      *<p>      * If below this number the circuit will not trip regardless of error      * percentage.      */
DECL|method|setCircuitBreakerRequestVolumeThreshold (String circuitBreakerRequestVolumeThreshold)
specifier|public
name|void
name|setCircuitBreakerRequestVolumeThreshold
parameter_list|(
name|String
name|circuitBreakerRequestVolumeThreshold
parameter_list|)
block|{
name|this
operator|.
name|circuitBreakerRequestVolumeThreshold
operator|=
name|circuitBreakerRequestVolumeThreshold
expr_stmt|;
block|}
DECL|method|getCircuitBreakerSleepWindowInMilliseconds ()
specifier|public
name|String
name|getCircuitBreakerSleepWindowInMilliseconds
parameter_list|()
block|{
return|return
name|circuitBreakerSleepWindowInMilliseconds
return|;
block|}
comment|/**      * The time in milliseconds after a HystrixCircuitBreaker trips open that it      * should wait before trying requests again.      */
DECL|method|setCircuitBreakerSleepWindowInMilliseconds (String circuitBreakerSleepWindowInMilliseconds)
specifier|public
name|void
name|setCircuitBreakerSleepWindowInMilliseconds
parameter_list|(
name|String
name|circuitBreakerSleepWindowInMilliseconds
parameter_list|)
block|{
name|this
operator|.
name|circuitBreakerSleepWindowInMilliseconds
operator|=
name|circuitBreakerSleepWindowInMilliseconds
expr_stmt|;
block|}
DECL|method|getExecutionIsolationSemaphoreMaxConcurrentRequests ()
specifier|public
name|String
name|getExecutionIsolationSemaphoreMaxConcurrentRequests
parameter_list|()
block|{
return|return
name|executionIsolationSemaphoreMaxConcurrentRequests
return|;
block|}
comment|/**      * Number of concurrent requests permitted to HystrixCommand.run(). Requests      * beyond the concurrent limit will be rejected.      *<p>      * Applicable only when executionIsolationStrategy == SEMAPHORE.      */
DECL|method|setExecutionIsolationSemaphoreMaxConcurrentRequests (String executionIsolationSemaphoreMaxConcurrentRequests)
specifier|public
name|void
name|setExecutionIsolationSemaphoreMaxConcurrentRequests
parameter_list|(
name|String
name|executionIsolationSemaphoreMaxConcurrentRequests
parameter_list|)
block|{
name|this
operator|.
name|executionIsolationSemaphoreMaxConcurrentRequests
operator|=
name|executionIsolationSemaphoreMaxConcurrentRequests
expr_stmt|;
block|}
DECL|method|getExecutionIsolationStrategy ()
specifier|public
name|String
name|getExecutionIsolationStrategy
parameter_list|()
block|{
return|return
name|executionIsolationStrategy
return|;
block|}
comment|/**      * What isolation strategy HystrixCommand.run() will be executed with.      *<p>      * If THREAD then it will be executed on a separate thread and concurrent      * requests limited by the number of threads in the thread-pool.      *<p>      * If SEMAPHORE then it will be executed on the calling thread and      * concurrent requests limited by the semaphore count.      */
DECL|method|setExecutionIsolationStrategy (String executionIsolationStrategy)
specifier|public
name|void
name|setExecutionIsolationStrategy
parameter_list|(
name|String
name|executionIsolationStrategy
parameter_list|)
block|{
name|this
operator|.
name|executionIsolationStrategy
operator|=
name|executionIsolationStrategy
expr_stmt|;
block|}
DECL|method|getExecutionIsolationThreadInterruptOnTimeout ()
specifier|public
name|String
name|getExecutionIsolationThreadInterruptOnTimeout
parameter_list|()
block|{
return|return
name|executionIsolationThreadInterruptOnTimeout
return|;
block|}
comment|/**      * Whether the execution thread should attempt an interrupt (using      * {@link Future#cancel}) when a thread times out.      *<p>      * Applicable only when executionIsolationStrategy() == THREAD.      */
DECL|method|setExecutionIsolationThreadInterruptOnTimeout (String executionIsolationThreadInterruptOnTimeout)
specifier|public
name|void
name|setExecutionIsolationThreadInterruptOnTimeout
parameter_list|(
name|String
name|executionIsolationThreadInterruptOnTimeout
parameter_list|)
block|{
name|this
operator|.
name|executionIsolationThreadInterruptOnTimeout
operator|=
name|executionIsolationThreadInterruptOnTimeout
expr_stmt|;
block|}
DECL|method|getExecutionTimeoutInMilliseconds ()
specifier|public
name|String
name|getExecutionTimeoutInMilliseconds
parameter_list|()
block|{
return|return
name|executionTimeoutInMilliseconds
return|;
block|}
comment|/**      * Time in milliseconds at which point the command will timeout and halt      * execution.      *<p>      * If {@link #executionIsolationThreadInterruptOnTimeout} == true and the      * command is thread-isolated, the executing thread will be interrupted. If      * the command is semaphore-isolated and a HystrixObservableCommand, that      * command will get unsubscribed.      */
DECL|method|setExecutionTimeoutInMilliseconds (String executionTimeoutInMilliseconds)
specifier|public
name|void
name|setExecutionTimeoutInMilliseconds
parameter_list|(
name|String
name|executionTimeoutInMilliseconds
parameter_list|)
block|{
name|this
operator|.
name|executionTimeoutInMilliseconds
operator|=
name|executionTimeoutInMilliseconds
expr_stmt|;
block|}
DECL|method|getExecutionTimeoutEnabled ()
specifier|public
name|String
name|getExecutionTimeoutEnabled
parameter_list|()
block|{
return|return
name|executionTimeoutEnabled
return|;
block|}
comment|/**      * Whether the timeout mechanism is enabled for this command      */
DECL|method|setExecutionTimeoutEnabled (String executionTimeoutEnabled)
specifier|public
name|void
name|setExecutionTimeoutEnabled
parameter_list|(
name|String
name|executionTimeoutEnabled
parameter_list|)
block|{
name|this
operator|.
name|executionTimeoutEnabled
operator|=
name|executionTimeoutEnabled
expr_stmt|;
block|}
DECL|method|getFallbackIsolationSemaphoreMaxConcurrentRequests ()
specifier|public
name|String
name|getFallbackIsolationSemaphoreMaxConcurrentRequests
parameter_list|()
block|{
return|return
name|fallbackIsolationSemaphoreMaxConcurrentRequests
return|;
block|}
comment|/**      * Number of concurrent requests permitted to HystrixCommand.getFallback().      * Requests beyond the concurrent limit will fail-fast and not attempt      * retrieving a fallback.      */
DECL|method|setFallbackIsolationSemaphoreMaxConcurrentRequests (String fallbackIsolationSemaphoreMaxConcurrentRequests)
specifier|public
name|void
name|setFallbackIsolationSemaphoreMaxConcurrentRequests
parameter_list|(
name|String
name|fallbackIsolationSemaphoreMaxConcurrentRequests
parameter_list|)
block|{
name|this
operator|.
name|fallbackIsolationSemaphoreMaxConcurrentRequests
operator|=
name|fallbackIsolationSemaphoreMaxConcurrentRequests
expr_stmt|;
block|}
DECL|method|getFallbackEnabled ()
specifier|public
name|String
name|getFallbackEnabled
parameter_list|()
block|{
return|return
name|fallbackEnabled
return|;
block|}
comment|/**      * Whether HystrixCommand.getFallback() should be attempted when failure      * occurs.      */
DECL|method|setFallbackEnabled (String fallbackEnabled)
specifier|public
name|void
name|setFallbackEnabled
parameter_list|(
name|String
name|fallbackEnabled
parameter_list|)
block|{
name|this
operator|.
name|fallbackEnabled
operator|=
name|fallbackEnabled
expr_stmt|;
block|}
DECL|method|getMetricsHealthSnapshotIntervalInMilliseconds ()
specifier|public
name|String
name|getMetricsHealthSnapshotIntervalInMilliseconds
parameter_list|()
block|{
return|return
name|metricsHealthSnapshotIntervalInMilliseconds
return|;
block|}
comment|/**      * Time in milliseconds to wait between allowing health snapshots to be      * taken that calculate success and error percentages and affect      * HystrixCircuitBreaker.isOpen() status.      *<p>      * On high-volume circuits the continual calculation of error percentage can      * become CPU intensive thus this controls how often it is calculated.      */
DECL|method|setMetricsHealthSnapshotIntervalInMilliseconds (String metricsHealthSnapshotIntervalInMilliseconds)
specifier|public
name|void
name|setMetricsHealthSnapshotIntervalInMilliseconds
parameter_list|(
name|String
name|metricsHealthSnapshotIntervalInMilliseconds
parameter_list|)
block|{
name|this
operator|.
name|metricsHealthSnapshotIntervalInMilliseconds
operator|=
name|metricsHealthSnapshotIntervalInMilliseconds
expr_stmt|;
block|}
DECL|method|getMetricsRollingPercentileBucketSize ()
specifier|public
name|String
name|getMetricsRollingPercentileBucketSize
parameter_list|()
block|{
return|return
name|metricsRollingPercentileBucketSize
return|;
block|}
comment|/**      * Maximum number of values stored in each bucket of the rolling percentile.      * This is passed into HystrixRollingPercentile inside      * HystrixCommandMetrics.      */
DECL|method|setMetricsRollingPercentileBucketSize (String metricsRollingPercentileBucketSize)
specifier|public
name|void
name|setMetricsRollingPercentileBucketSize
parameter_list|(
name|String
name|metricsRollingPercentileBucketSize
parameter_list|)
block|{
name|this
operator|.
name|metricsRollingPercentileBucketSize
operator|=
name|metricsRollingPercentileBucketSize
expr_stmt|;
block|}
DECL|method|getMetricsRollingPercentileEnabled ()
specifier|public
name|String
name|getMetricsRollingPercentileEnabled
parameter_list|()
block|{
return|return
name|metricsRollingPercentileEnabled
return|;
block|}
comment|/**      * Whether percentile metrics should be captured using      * HystrixRollingPercentile inside HystrixCommandMetrics.      */
DECL|method|setMetricsRollingPercentileEnabled (String metricsRollingPercentileEnabled)
specifier|public
name|void
name|setMetricsRollingPercentileEnabled
parameter_list|(
name|String
name|metricsRollingPercentileEnabled
parameter_list|)
block|{
name|this
operator|.
name|metricsRollingPercentileEnabled
operator|=
name|metricsRollingPercentileEnabled
expr_stmt|;
block|}
DECL|method|getMetricsRollingPercentileWindowInMilliseconds ()
specifier|public
name|String
name|getMetricsRollingPercentileWindowInMilliseconds
parameter_list|()
block|{
return|return
name|metricsRollingPercentileWindowInMilliseconds
return|;
block|}
comment|/**      * Duration of percentile rolling window in milliseconds. This is passed      * into HystrixRollingPercentile inside HystrixCommandMetrics.      */
DECL|method|setMetricsRollingPercentileWindowInMilliseconds (String metricsRollingPercentileWindowInMilliseconds)
specifier|public
name|void
name|setMetricsRollingPercentileWindowInMilliseconds
parameter_list|(
name|String
name|metricsRollingPercentileWindowInMilliseconds
parameter_list|)
block|{
name|this
operator|.
name|metricsRollingPercentileWindowInMilliseconds
operator|=
name|metricsRollingPercentileWindowInMilliseconds
expr_stmt|;
block|}
DECL|method|getMetricsRollingPercentileWindowBuckets ()
specifier|public
name|String
name|getMetricsRollingPercentileWindowBuckets
parameter_list|()
block|{
return|return
name|metricsRollingPercentileWindowBuckets
return|;
block|}
comment|/**      * Number of buckets the rolling percentile window is broken into. This is      * passed into HystrixRollingPercentile inside HystrixCommandMetrics.      */
DECL|method|setMetricsRollingPercentileWindowBuckets (String metricsRollingPercentileWindowBuckets)
specifier|public
name|void
name|setMetricsRollingPercentileWindowBuckets
parameter_list|(
name|String
name|metricsRollingPercentileWindowBuckets
parameter_list|)
block|{
name|this
operator|.
name|metricsRollingPercentileWindowBuckets
operator|=
name|metricsRollingPercentileWindowBuckets
expr_stmt|;
block|}
DECL|method|getMetricsRollingStatisticalWindowInMilliseconds ()
specifier|public
name|String
name|getMetricsRollingStatisticalWindowInMilliseconds
parameter_list|()
block|{
return|return
name|metricsRollingStatisticalWindowInMilliseconds
return|;
block|}
comment|/**      * This property sets the duration of the statistical rolling window, in      * milliseconds. This is how long metrics are kept for the thread pool. The      * window is divided into buckets and ârollsâ by those increments.      */
DECL|method|setMetricsRollingStatisticalWindowInMilliseconds (String metricsRollingStatisticalWindowInMilliseconds)
specifier|public
name|void
name|setMetricsRollingStatisticalWindowInMilliseconds
parameter_list|(
name|String
name|metricsRollingStatisticalWindowInMilliseconds
parameter_list|)
block|{
name|this
operator|.
name|metricsRollingStatisticalWindowInMilliseconds
operator|=
name|metricsRollingStatisticalWindowInMilliseconds
expr_stmt|;
block|}
DECL|method|getMetricsRollingStatisticalWindowBuckets ()
specifier|public
name|String
name|getMetricsRollingStatisticalWindowBuckets
parameter_list|()
block|{
return|return
name|metricsRollingStatisticalWindowBuckets
return|;
block|}
comment|/**      * Number of buckets the rolling statistical window is broken into. This is      * passed into HystrixRollingNumber inside HystrixCommandMetrics.      */
DECL|method|setMetricsRollingStatisticalWindowBuckets (String metricsRollingStatisticalWindowBuckets)
specifier|public
name|void
name|setMetricsRollingStatisticalWindowBuckets
parameter_list|(
name|String
name|metricsRollingStatisticalWindowBuckets
parameter_list|)
block|{
name|this
operator|.
name|metricsRollingStatisticalWindowBuckets
operator|=
name|metricsRollingStatisticalWindowBuckets
expr_stmt|;
block|}
DECL|method|getRequestLogEnabled ()
specifier|public
name|String
name|getRequestLogEnabled
parameter_list|()
block|{
return|return
name|requestLogEnabled
return|;
block|}
comment|/**      * Whether HystrixCommand execution and events should be logged to      * HystrixRequestLog.      */
DECL|method|setRequestLogEnabled (String requestLogEnabled)
specifier|public
name|void
name|setRequestLogEnabled
parameter_list|(
name|String
name|requestLogEnabled
parameter_list|)
block|{
name|this
operator|.
name|requestLogEnabled
operator|=
name|requestLogEnabled
expr_stmt|;
block|}
DECL|method|getCorePoolSize ()
specifier|public
name|String
name|getCorePoolSize
parameter_list|()
block|{
return|return
name|corePoolSize
return|;
block|}
comment|/**      * Core thread-pool size that gets passed to      * {@link java.util.concurrent.ThreadPoolExecutor#setCorePoolSize(int)}      */
DECL|method|setCorePoolSize (String corePoolSize)
specifier|public
name|void
name|setCorePoolSize
parameter_list|(
name|String
name|corePoolSize
parameter_list|)
block|{
name|this
operator|.
name|corePoolSize
operator|=
name|corePoolSize
expr_stmt|;
block|}
DECL|method|getMaximumSize ()
specifier|public
name|String
name|getMaximumSize
parameter_list|()
block|{
return|return
name|maximumSize
return|;
block|}
comment|/**      * Maximum thread-pool size that gets passed to      * {@link ThreadPoolExecutor#setMaximumPoolSize(int)}. This is the maximum      * amount of concurrency that can be supported without starting to reject      * HystrixCommands. Please note that this setting only takes effect if you      * also set allowMaximumSizeToDivergeFromCoreSize      */
DECL|method|setMaximumSize (String maximumSize)
specifier|public
name|void
name|setMaximumSize
parameter_list|(
name|String
name|maximumSize
parameter_list|)
block|{
name|this
operator|.
name|maximumSize
operator|=
name|maximumSize
expr_stmt|;
block|}
DECL|method|getKeepAliveTime ()
specifier|public
name|String
name|getKeepAliveTime
parameter_list|()
block|{
return|return
name|keepAliveTime
return|;
block|}
comment|/**      * Keep-alive time in minutes that gets passed to      * {@link ThreadPoolExecutor#setKeepAliveTime(long, TimeUnit)}      */
DECL|method|setKeepAliveTime (String keepAliveTime)
specifier|public
name|void
name|setKeepAliveTime
parameter_list|(
name|String
name|keepAliveTime
parameter_list|)
block|{
name|this
operator|.
name|keepAliveTime
operator|=
name|keepAliveTime
expr_stmt|;
block|}
DECL|method|getMaxQueueSize ()
specifier|public
name|String
name|getMaxQueueSize
parameter_list|()
block|{
return|return
name|maxQueueSize
return|;
block|}
comment|/**      * Max queue size that gets passed to {@link BlockingQueue} in      * HystrixConcurrencyStrategy.getBlockingQueue(int) This should only affect      * the instantiation of a threadpool - it is not eliglible to change a queue      * size on the fly. For that, use queueSizeRejectionThreshold().      */
DECL|method|setMaxQueueSize (String maxQueueSize)
specifier|public
name|void
name|setMaxQueueSize
parameter_list|(
name|String
name|maxQueueSize
parameter_list|)
block|{
name|this
operator|.
name|maxQueueSize
operator|=
name|maxQueueSize
expr_stmt|;
block|}
DECL|method|getQueueSizeRejectionThreshold ()
specifier|public
name|String
name|getQueueSizeRejectionThreshold
parameter_list|()
block|{
return|return
name|queueSizeRejectionThreshold
return|;
block|}
comment|/**      * Queue size rejection threshold is an artificial "max" size at which      * rejections will occur even if {@link #maxQueueSize} has not been reached.      * This is done because the {@link #maxQueueSize} of a {@link BlockingQueue}      * can not be dynamically changed and we want to support dynamically      * changing the queue size that affects rejections.      *<p>      * This is used by HystrixCommand when queuing a thread for execution.      */
DECL|method|setQueueSizeRejectionThreshold (String queueSizeRejectionThreshold)
specifier|public
name|void
name|setQueueSizeRejectionThreshold
parameter_list|(
name|String
name|queueSizeRejectionThreshold
parameter_list|)
block|{
name|this
operator|.
name|queueSizeRejectionThreshold
operator|=
name|queueSizeRejectionThreshold
expr_stmt|;
block|}
DECL|method|getThreadPoolRollingNumberStatisticalWindowInMilliseconds ()
specifier|public
name|String
name|getThreadPoolRollingNumberStatisticalWindowInMilliseconds
parameter_list|()
block|{
return|return
name|threadPoolRollingNumberStatisticalWindowInMilliseconds
return|;
block|}
comment|/**      * Duration of statistical rolling window in milliseconds. This is passed      * into HystrixRollingNumber inside each HystrixThreadPoolMetrics instance.      */
DECL|method|setThreadPoolRollingNumberStatisticalWindowInMilliseconds (String threadPoolRollingNumberStatisticalWindowInMilliseconds)
specifier|public
name|void
name|setThreadPoolRollingNumberStatisticalWindowInMilliseconds
parameter_list|(
name|String
name|threadPoolRollingNumberStatisticalWindowInMilliseconds
parameter_list|)
block|{
name|this
operator|.
name|threadPoolRollingNumberStatisticalWindowInMilliseconds
operator|=
name|threadPoolRollingNumberStatisticalWindowInMilliseconds
expr_stmt|;
block|}
DECL|method|getThreadPoolRollingNumberStatisticalWindowBuckets ()
specifier|public
name|String
name|getThreadPoolRollingNumberStatisticalWindowBuckets
parameter_list|()
block|{
return|return
name|threadPoolRollingNumberStatisticalWindowBuckets
return|;
block|}
comment|/**      * Number of buckets the rolling statistical window is broken into. This is      * passed into HystrixRollingNumber inside each HystrixThreadPoolMetrics      * instance.      */
DECL|method|setThreadPoolRollingNumberStatisticalWindowBuckets (String threadPoolRollingNumberStatisticalWindowBuckets)
specifier|public
name|void
name|setThreadPoolRollingNumberStatisticalWindowBuckets
parameter_list|(
name|String
name|threadPoolRollingNumberStatisticalWindowBuckets
parameter_list|)
block|{
name|this
operator|.
name|threadPoolRollingNumberStatisticalWindowBuckets
operator|=
name|threadPoolRollingNumberStatisticalWindowBuckets
expr_stmt|;
block|}
DECL|method|getAllowMaximumSizeToDivergeFromCoreSize ()
specifier|public
name|String
name|getAllowMaximumSizeToDivergeFromCoreSize
parameter_list|()
block|{
return|return
name|allowMaximumSizeToDivergeFromCoreSize
return|;
block|}
comment|/**      * Allows the configuration for maximumSize to take effect. That value can      * then be equal to, or higher, than coreSize      */
DECL|method|setAllowMaximumSizeToDivergeFromCoreSize (String allowMaximumSizeToDivergeFromCoreSize)
specifier|public
name|void
name|setAllowMaximumSizeToDivergeFromCoreSize
parameter_list|(
name|String
name|allowMaximumSizeToDivergeFromCoreSize
parameter_list|)
block|{
name|this
operator|.
name|allowMaximumSizeToDivergeFromCoreSize
operator|=
name|allowMaximumSizeToDivergeFromCoreSize
expr_stmt|;
block|}
block|}
end_class

end_unit

