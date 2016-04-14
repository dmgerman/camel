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

begin_interface
DECL|interface|HystrixConstants
specifier|public
interface|interface
name|HystrixConstants
block|{
comment|// in message header
DECL|field|CAMEL_HYSTRIX_RUN_ENDPOINT
name|String
name|CAMEL_HYSTRIX_RUN_ENDPOINT
init|=
literal|"CamelHystrixRunEndpoint"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_FALLBACK_ENDPOINT
name|String
name|CAMEL_HYSTRIX_FALLBACK_ENDPOINT
init|=
literal|"CamelHystrixFallbackEndpoint"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_CLEAR_CACHE_FIRST
name|String
name|CAMEL_HYSTRIX_CLEAR_CACHE_FIRST
init|=
literal|"CamelHystrixClearCacheFirst"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_REQUEST_CONTEXT
name|String
name|CAMEL_HYSTRIX_REQUEST_CONTEXT
init|=
literal|"CamelHystrixRequestContex"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_GROUP_KEY
name|String
name|CAMEL_HYSTRIX_GROUP_KEY
init|=
literal|"CamelHystrixGroupKey"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_COMMAND_KEY
name|String
name|CAMEL_HYSTRIX_COMMAND_KEY
init|=
literal|"CamelHystrixCommandKey"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_THREAD_POOL_KEY
name|String
name|CAMEL_HYSTRIX_THREAD_POOL_KEY
init|=
literal|"CamelHystrixThreadPoolKey"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_CORE_SIZE
name|String
name|CAMEL_HYSTRIX_CORE_SIZE
init|=
literal|"CamelHystrixCoreSize"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_KEEP_ALIVE_TIME
name|String
name|CAMEL_HYSTRIX_KEEP_ALIVE_TIME
init|=
literal|"CamelHystrixKeepAliveTime"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_MAX_QUEUE_SIZE
name|String
name|CAMEL_HYSTRIX_MAX_QUEUE_SIZE
init|=
literal|"CamelHystrixMaxQueueSize"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_QUEUE_SIZE_REJECTION_THRESHOLD
name|String
name|CAMEL_HYSTRIX_QUEUE_SIZE_REJECTION_THRESHOLD
init|=
literal|"CamelHystrixQueueSizeRejectionThreshold"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_THREAD_POOL_METRICS_ROLLING_STATISTICAL_WINDOW_IN_MILLISECONDS
name|String
name|CAMEL_HYSTRIX_THREAD_POOL_METRICS_ROLLING_STATISTICAL_WINDOW_IN_MILLISECONDS
init|=
literal|"CamelHystrixThreadPoolMetricsRollingStatisticalWindowInMilliseconds"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_THREAD_POOL_ROLLING_NUMBER_STATISTICAL_WINDOW_BUCKETS
name|String
name|CAMEL_HYSTRIX_THREAD_POOL_ROLLING_NUMBER_STATISTICAL_WINDOW_BUCKETS
init|=
literal|"CamelHystrixThreadPoolRollingNumberStatisticalWindowBuckets"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_CIRCUIT_BREAKER_ENABLED
name|String
name|CAMEL_HYSTRIX_CIRCUIT_BREAKER_ENABLED
init|=
literal|"CamelHystrixCircuitBreakerEnabled"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE
name|String
name|CAMEL_HYSTRIX_CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE
init|=
literal|"CamelHystrixCircuitBreakerErrorThresholdPercentage"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_CIRCUIT_BREAKER_FORCE_CLOSED
name|String
name|CAMEL_HYSTRIX_CIRCUIT_BREAKER_FORCE_CLOSED
init|=
literal|"CamelHystrixCircuitBreakerForceClosed"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_CIRCUIT_BREAKER_FORCE_OPEN
name|String
name|CAMEL_HYSTRIX_CIRCUIT_BREAKER_FORCE_OPEN
init|=
literal|"CamelHystrixCircuitBreakerForceOpen"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD
name|String
name|CAMEL_HYSTRIX_CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD
init|=
literal|"CamelHystrixCircuitBreakerRequestVolumeThreshold"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_CIRCUIT_BREAKER_SLEEP_WINDOW_IN_MILLISECONDS
name|String
name|CAMEL_HYSTRIX_CIRCUIT_BREAKER_SLEEP_WINDOW_IN_MILLISECONDS
init|=
literal|"CamelHystrixCircuitBreakerSleepWindowInMilliseconds"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_EXECUTION_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS
name|String
name|CAMEL_HYSTRIX_EXECUTION_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS
init|=
literal|"CamelHystrixExecutionIsolationSemaphoreMaxConcurrentRequests"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_EXECUTION_ISOLATION_STRATEGY
name|String
name|CAMEL_HYSTRIX_EXECUTION_ISOLATION_STRATEGY
init|=
literal|"CamelHystrixExecutionIsolationStrategy"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_EXECUTION_ISOLATION_THREAD_INTERRUPTION_ON_TIMEOUT
name|String
name|CAMEL_HYSTRIX_EXECUTION_ISOLATION_THREAD_INTERRUPTION_ON_TIMEOUT
init|=
literal|"CamelHystrixExecutionIsolationThreadInterruptOnTimeout"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_EXECUTION_TIMEOUT_IN_MILLISECONDS
name|String
name|CAMEL_HYSTRIX_EXECUTION_TIMEOUT_IN_MILLISECONDS
init|=
literal|"CamelHystrixExecutionTimeoutInMilliseconds"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_EXECUTION_TIMEOUT_ENABLED
name|String
name|CAMEL_HYSTRIX_EXECUTION_TIMEOUT_ENABLED
init|=
literal|"CamelHystrixExecutionTimeoutEnabled"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_FALLBACK_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS
name|String
name|CAMEL_HYSTRIX_FALLBACK_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS
init|=
literal|"CamelHystrixFallbackIsolationSemaphoreMaxConcurrentRequests"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_FALLBACK_ENABLED
name|String
name|CAMEL_HYSTRIX_FALLBACK_ENABLED
init|=
literal|"CamelHystrixFallbackEnabled"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_METRICS_HEALTH_SNAPSHOT_INTERVAL_IN_MILLISECONDS
name|String
name|CAMEL_HYSTRIX_METRICS_HEALTH_SNAPSHOT_INTERVAL_IN_MILLISECONDS
init|=
literal|"CamelHystrixMetricsHealthSnapshotIntervalInMilliseconds"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_METRICS_ROLLING_PERCENTILE_BUCKET_SIZE
name|String
name|CAMEL_HYSTRIX_METRICS_ROLLING_PERCENTILE_BUCKET_SIZE
init|=
literal|"CamelHystrixMetricsRollingPercentileBucketSize"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_METRICS_ROLLING_PERCENTILE_ENABLED
name|String
name|CAMEL_HYSTRIX_METRICS_ROLLING_PERCENTILE_ENABLED
init|=
literal|"CamelHystrixMetricsRollingPercentileEnabled"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_METRICS_ROLLING_PERCENTILE_WINDOW_IN_MILLISECONDS
name|String
name|CAMEL_HYSTRIX_METRICS_ROLLING_PERCENTILE_WINDOW_IN_MILLISECONDS
init|=
literal|"CamelHystrixMetricsRollingPercentileWindowInMilliseconds"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_METRICS_ROLLING_PERCENTILE_WINDOW_BUCKETS
name|String
name|CAMEL_HYSTRIX_METRICS_ROLLING_PERCENTILE_WINDOW_BUCKETS
init|=
literal|"CamelHystrixMetricsRollingPercentileWindowBuckets"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_METRICS_ROLLING_STATISTICAL_WINDOW_IN_MILLISECONDS
name|String
name|CAMEL_HYSTRIX_METRICS_ROLLING_STATISTICAL_WINDOW_IN_MILLISECONDS
init|=
literal|"CamelHystrixMetricsRollingStatisticalWindowInMilliseconds"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_METRICS_ROLLING_STATISTICAL_WINDOW_BUCKETS
name|String
name|CAMEL_HYSTRIX_METRICS_ROLLING_STATISTICAL_WINDOW_BUCKETS
init|=
literal|"CamelHystrixMetricsRollingStatisticalWindowBuckets"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_REQUEST_CACHE_ENABLED
name|String
name|CAMEL_HYSTRIX_REQUEST_CACHE_ENABLED
init|=
literal|"CamelHystrixRequestCacheEnabled"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_REQUEST_LOG_ENABLED
name|String
name|CAMEL_HYSTRIX_REQUEST_LOG_ENABLED
init|=
literal|"CamelHystrixRequestLogEnabled"
decl_stmt|;
comment|//out message headers
DECL|field|CAMEL_HYSTRIX_COMMAND_METRICS_TOTAL_REQUESTS
name|String
name|CAMEL_HYSTRIX_COMMAND_METRICS_TOTAL_REQUESTS
init|=
literal|"CamelHystrixCommandMetricsTotalRequests"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_COMMAND_METRICS_ERROR_COUNT
name|String
name|CAMEL_HYSTRIX_COMMAND_METRICS_ERROR_COUNT
init|=
literal|"CamelHystrixCommandMetricsErrorCount"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_COMMAND_METRICS_ERROR_PERCENTAGE
name|String
name|CAMEL_HYSTRIX_COMMAND_METRICS_ERROR_PERCENTAGE
init|=
literal|"CamelHystrixCommandMetricsErrorPercentage"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_COMMAND_METRICS_CURRENT_CONCURRENT_EXECUTION_COUNT
name|String
name|CAMEL_HYSTRIX_COMMAND_METRICS_CURRENT_CONCURRENT_EXECUTION_COUNT
init|=
literal|"CamelHystrixCommandMetricsCurrentConcurrentExecutionCount"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_COMMAND_METRICS_EXECUTION_TIME_MEAN
name|String
name|CAMEL_HYSTRIX_COMMAND_METRICS_EXECUTION_TIME_MEAN
init|=
literal|"CamelHystrixCommandMetricsExecutionTimeMean"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_COMMAND_METRICS_ROLLING_MAX_CONCURRENT_EXECUTIONS
name|String
name|CAMEL_HYSTRIX_COMMAND_METRICS_ROLLING_MAX_CONCURRENT_EXECUTIONS
init|=
literal|"CamelHystrixCommandMetricsRollingMaxConcurrentExecutions"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_COMMAND_METRICS_TOTAL_TIME_MEAN
name|String
name|CAMEL_HYSTRIX_COMMAND_METRICS_TOTAL_TIME_MEAN
init|=
literal|"CamelHystrixCommandMetricsTotalTimeMean"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CURRENT_ACTIVE_COUNT
name|String
name|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CURRENT_ACTIVE_COUNT
init|=
literal|"CamelHystrixThreadPoolMetricsCurrentActiveCount"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CUMULATIVE_COUNT_THREADS_EXECUTED
name|String
name|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CUMULATIVE_COUNT_THREADS_EXECUTED
init|=
literal|"CamelHystrixThreadPoolMetricsCumulativeCountThreadsExecuted"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CURRENT_COMPLETED_TASK_COUNT
name|String
name|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CURRENT_COMPLETED_TASK_COUNT
init|=
literal|"CamelHystrixThreadPoolMetricsCurrentCompletedTaskCount"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CURRENT_CORE_POOL_SIZE
name|String
name|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CURRENT_CORE_POOL_SIZE
init|=
literal|"CamelHystrixThreadPoolMetricsCurrentCorePoolSize"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CURRENT_LARGEST_POOL_SIZE
name|String
name|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CURRENT_LARGEST_POOL_SIZE
init|=
literal|"CamelHystrixThreadPoolMetricsCurrentLargestPoolSize"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CURRENT_MAXIMUM_POOL_SIZE
name|String
name|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CURRENT_MAXIMUM_POOL_SIZE
init|=
literal|"CamelHystrixThreadPoolMetricsCurrentMaximumPoolSize"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CURRENT_POOL_SIZE
name|String
name|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CURRENT_POOL_SIZE
init|=
literal|"CamelHystrixThreadPoolMetricsCurrentPoolSize"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CURRENT_QUEUE_SIZE
name|String
name|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CURRENT_QUEUE_SIZE
init|=
literal|"CamelHystrixThreadPoolMetricsCurrentQueueSize"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CURRENT_TASK_COUNT
name|String
name|CAMEL_HYSTRIX_THREAD_POOL_METRICS_CURRENT_TASK_COUNT
init|=
literal|"CamelHystrixThreadPoolMetricsCurrentTaskCount"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_THREAD_POOL_METRICS_ROLLING_COUNT_THREADS_EXECUTED
name|String
name|CAMEL_HYSTRIX_THREAD_POOL_METRICS_ROLLING_COUNT_THREADS_EXECUTED
init|=
literal|"CamelHystrixThreadPoolMetricsRollingCountThreadsExecuted"
decl_stmt|;
DECL|field|CAMEL_HYSTRIX_THREAD_POOL_METRICS_ROLLING_MAX_ACTIVE_THREADS
name|String
name|CAMEL_HYSTRIX_THREAD_POOL_METRICS_ROLLING_MAX_ACTIVE_THREADS
init|=
literal|"CamelHystrixThreadPoolMetricsRollingMaxActiveThreads"
decl_stmt|;
block|}
end_interface

end_unit

