begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.main
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
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
name|ForkJoinPool
import|;
end_import

begin_comment
comment|/**  * Global configuration for Resilience EIP circuit breaker.  */
end_comment

begin_class
DECL|class|Resilience4jConfigurationProperties
specifier|public
class|class
name|Resilience4jConfigurationProperties
block|{
DECL|field|parent
specifier|private
specifier|final
name|MainConfigurationProperties
name|parent
decl_stmt|;
DECL|field|circuitBreakerRef
specifier|private
name|String
name|circuitBreakerRef
decl_stmt|;
DECL|field|configRef
specifier|private
name|String
name|configRef
decl_stmt|;
DECL|field|failureRateThreshold
specifier|private
name|Float
name|failureRateThreshold
decl_stmt|;
DECL|field|permittedNumberOfCallsInHalfOpenState
specifier|private
name|Integer
name|permittedNumberOfCallsInHalfOpenState
decl_stmt|;
DECL|field|slidingWindowSize
specifier|private
name|Integer
name|slidingWindowSize
decl_stmt|;
DECL|field|slidingWindowType
specifier|private
name|String
name|slidingWindowType
decl_stmt|;
DECL|field|minimumNumberOfCalls
specifier|private
name|Integer
name|minimumNumberOfCalls
decl_stmt|;
DECL|field|writableStackTraceEnabled
specifier|private
name|Boolean
name|writableStackTraceEnabled
decl_stmt|;
DECL|field|waitDurationInOpenState
specifier|private
name|Integer
name|waitDurationInOpenState
decl_stmt|;
DECL|field|automaticTransitionFromOpenToHalfOpenEnabled
specifier|private
name|Boolean
name|automaticTransitionFromOpenToHalfOpenEnabled
decl_stmt|;
DECL|field|slowCallRateThreshold
specifier|private
name|Float
name|slowCallRateThreshold
decl_stmt|;
DECL|field|slowCallDurationThreshold
specifier|private
name|Integer
name|slowCallDurationThreshold
decl_stmt|;
DECL|field|bulkheadEnabled
specifier|private
name|Boolean
name|bulkheadEnabled
decl_stmt|;
DECL|field|bulkheadMaxConcurrentCalls
specifier|private
name|Integer
name|bulkheadMaxConcurrentCalls
decl_stmt|;
DECL|field|bulkheadMaxWaitDuration
specifier|private
name|Integer
name|bulkheadMaxWaitDuration
decl_stmt|;
DECL|field|timeoutEnabled
specifier|private
name|Boolean
name|timeoutEnabled
decl_stmt|;
DECL|field|timeoutExecutorServiceRef
specifier|private
name|String
name|timeoutExecutorServiceRef
decl_stmt|;
DECL|field|timeoutDuration
specifier|private
name|Integer
name|timeoutDuration
decl_stmt|;
DECL|field|timeoutCancelRunningFuture
specifier|private
name|Boolean
name|timeoutCancelRunningFuture
decl_stmt|;
DECL|method|Resilience4jConfigurationProperties (MainConfigurationProperties parent)
specifier|public
name|Resilience4jConfigurationProperties
parameter_list|(
name|MainConfigurationProperties
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
DECL|method|end ()
specifier|public
name|MainConfigurationProperties
name|end
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
comment|// getter and setters
comment|// --------------------------------------------------------------
DECL|method|getCircuitBreakerRef ()
specifier|public
name|String
name|getCircuitBreakerRef
parameter_list|()
block|{
return|return
name|circuitBreakerRef
return|;
block|}
comment|/**      * Refers to an existing io.github.resilience4j.circuitbreaker.CircuitBreaker instance      * to lookup and use from the registry. When using this, then any other circuit breaker options      * are not in use.      */
DECL|method|setCircuitBreakerRef (String circuitBreakerRef)
specifier|public
name|void
name|setCircuitBreakerRef
parameter_list|(
name|String
name|circuitBreakerRef
parameter_list|)
block|{
name|this
operator|.
name|circuitBreakerRef
operator|=
name|circuitBreakerRef
expr_stmt|;
block|}
DECL|method|getConfigRef ()
specifier|public
name|String
name|getConfigRef
parameter_list|()
block|{
return|return
name|configRef
return|;
block|}
comment|/**      * Refers to an existing io.github.resilience4j.circuitbreaker.CircuitBreakerConfig instance      * to lookup and use from the registry.      */
DECL|method|setConfigRef (String configRef)
specifier|public
name|void
name|setConfigRef
parameter_list|(
name|String
name|configRef
parameter_list|)
block|{
name|this
operator|.
name|configRef
operator|=
name|configRef
expr_stmt|;
block|}
DECL|method|getFailureRateThreshold ()
specifier|public
name|Float
name|getFailureRateThreshold
parameter_list|()
block|{
return|return
name|failureRateThreshold
return|;
block|}
comment|/**      * Configures the failure rate threshold in percentage.      * If the failure rate is equal or greater than the threshold the CircuitBreaker transitions to open and starts short-circuiting calls.      *<p>      * The threshold must be greater than 0 and not greater than 100. Default value is 50 percentage.      */
DECL|method|setFailureRateThreshold (Float failureRateThreshold)
specifier|public
name|void
name|setFailureRateThreshold
parameter_list|(
name|Float
name|failureRateThreshold
parameter_list|)
block|{
name|this
operator|.
name|failureRateThreshold
operator|=
name|failureRateThreshold
expr_stmt|;
block|}
DECL|method|getPermittedNumberOfCallsInHalfOpenState ()
specifier|public
name|Integer
name|getPermittedNumberOfCallsInHalfOpenState
parameter_list|()
block|{
return|return
name|permittedNumberOfCallsInHalfOpenState
return|;
block|}
comment|/**      * Configures the number of permitted calls when the CircuitBreaker is half open.      *<p>      * The size must be greater than 0. Default size is 10.      */
DECL|method|setPermittedNumberOfCallsInHalfOpenState (Integer permittedNumberOfCallsInHalfOpenState)
specifier|public
name|void
name|setPermittedNumberOfCallsInHalfOpenState
parameter_list|(
name|Integer
name|permittedNumberOfCallsInHalfOpenState
parameter_list|)
block|{
name|this
operator|.
name|permittedNumberOfCallsInHalfOpenState
operator|=
name|permittedNumberOfCallsInHalfOpenState
expr_stmt|;
block|}
DECL|method|getSlidingWindowSize ()
specifier|public
name|Integer
name|getSlidingWindowSize
parameter_list|()
block|{
return|return
name|slidingWindowSize
return|;
block|}
comment|/**      * Configures the size of the sliding window which is used to record the outcome of calls when the CircuitBreaker is closed.      * {@code slidingWindowSize} configures the size of the sliding window. Sliding window can either be count-based or time-based.      *      * If {@code slidingWindowType} is COUNT_BASED, the last {@code slidingWindowSize} calls are recorded and aggregated.      * If {@code slidingWindowType} is TIME_BASED, the calls of the last {@code slidingWindowSize} seconds are recorded and aggregated.      *<p>      * The {@code slidingWindowSize} must be greater than 0.      * The {@code minimumNumberOfCalls} must be greater than 0.      * If the slidingWindowType is COUNT_BASED, the {@code minimumNumberOfCalls} cannot be greater than {@code slidingWindowSize}.      * If the slidingWindowType is TIME_BASED, you can pick whatever you want.      *      * Default slidingWindowSize is 100.      */
DECL|method|setSlidingWindowSize (Integer slidingWindowSize)
specifier|public
name|void
name|setSlidingWindowSize
parameter_list|(
name|Integer
name|slidingWindowSize
parameter_list|)
block|{
name|this
operator|.
name|slidingWindowSize
operator|=
name|slidingWindowSize
expr_stmt|;
block|}
DECL|method|getSlidingWindowType ()
specifier|public
name|String
name|getSlidingWindowType
parameter_list|()
block|{
return|return
name|slidingWindowType
return|;
block|}
comment|/**      * Configures the type of the sliding window which is used to record the outcome of calls when the CircuitBreaker is closed.      * Sliding window can either be count-based or time-based.      *      * If {@code slidingWindowType} is COUNT_BASED, the last {@code slidingWindowSize} calls are recorded and aggregated.      * If {@code slidingWindowType} is TIME_BASED, the calls of the last {@code slidingWindowSize} seconds are recorded and aggregated.      *      * Default slidingWindowType is COUNT_BASED.      */
DECL|method|setSlidingWindowType (String slidingWindowType)
specifier|public
name|void
name|setSlidingWindowType
parameter_list|(
name|String
name|slidingWindowType
parameter_list|)
block|{
name|this
operator|.
name|slidingWindowType
operator|=
name|slidingWindowType
expr_stmt|;
block|}
DECL|method|getMinimumNumberOfCalls ()
specifier|public
name|Integer
name|getMinimumNumberOfCalls
parameter_list|()
block|{
return|return
name|minimumNumberOfCalls
return|;
block|}
comment|/**      * Configures configures the minimum number of calls which are required (per sliding window period) before the CircuitBreaker can calculate the error rate.      * For example, if {@code minimumNumberOfCalls} is 10, then at least 10 calls must be recorded, before the failure rate can be calculated.      * If only 9 calls have been recorded the CircuitBreaker will not transition to open even if all 9 calls have failed.      *      * Default minimumNumberOfCalls is 100      */
DECL|method|setMinimumNumberOfCalls (Integer minimumNumberOfCalls)
specifier|public
name|void
name|setMinimumNumberOfCalls
parameter_list|(
name|Integer
name|minimumNumberOfCalls
parameter_list|)
block|{
name|this
operator|.
name|minimumNumberOfCalls
operator|=
name|minimumNumberOfCalls
expr_stmt|;
block|}
DECL|method|getWritableStackTraceEnabled ()
specifier|public
name|Boolean
name|getWritableStackTraceEnabled
parameter_list|()
block|{
return|return
name|writableStackTraceEnabled
return|;
block|}
comment|/**      * Enables writable stack traces. When set to false, Exception.getStackTrace returns a zero length array.      * This may be used to reduce log spam when the circuit breaker is open as the cause of the exceptions is already known (the circuit breaker is short-circuiting calls).      */
DECL|method|setWritableStackTraceEnabled (Boolean writableStackTraceEnabled)
specifier|public
name|void
name|setWritableStackTraceEnabled
parameter_list|(
name|Boolean
name|writableStackTraceEnabled
parameter_list|)
block|{
name|this
operator|.
name|writableStackTraceEnabled
operator|=
name|writableStackTraceEnabled
expr_stmt|;
block|}
DECL|method|getWaitDurationInOpenState ()
specifier|public
name|Integer
name|getWaitDurationInOpenState
parameter_list|()
block|{
return|return
name|waitDurationInOpenState
return|;
block|}
comment|/**      * Configures the wait duration (in seconds) which specifies how long the CircuitBreaker should stay open, before it switches to half open.      * Default value is 60 seconds.      */
DECL|method|setWaitDurationInOpenState (Integer waitDurationInOpenState)
specifier|public
name|void
name|setWaitDurationInOpenState
parameter_list|(
name|Integer
name|waitDurationInOpenState
parameter_list|)
block|{
name|this
operator|.
name|waitDurationInOpenState
operator|=
name|waitDurationInOpenState
expr_stmt|;
block|}
DECL|method|getAutomaticTransitionFromOpenToHalfOpenEnabled ()
specifier|public
name|Boolean
name|getAutomaticTransitionFromOpenToHalfOpenEnabled
parameter_list|()
block|{
return|return
name|automaticTransitionFromOpenToHalfOpenEnabled
return|;
block|}
comment|/**      * Enables automatic transition from OPEN to HALF_OPEN state once the waitDurationInOpenState has passed.      */
DECL|method|setAutomaticTransitionFromOpenToHalfOpenEnabled (Boolean automaticTransitionFromOpenToHalfOpenEnabled)
specifier|public
name|void
name|setAutomaticTransitionFromOpenToHalfOpenEnabled
parameter_list|(
name|Boolean
name|automaticTransitionFromOpenToHalfOpenEnabled
parameter_list|)
block|{
name|this
operator|.
name|automaticTransitionFromOpenToHalfOpenEnabled
operator|=
name|automaticTransitionFromOpenToHalfOpenEnabled
expr_stmt|;
block|}
DECL|method|getSlowCallRateThreshold ()
specifier|public
name|Float
name|getSlowCallRateThreshold
parameter_list|()
block|{
return|return
name|slowCallRateThreshold
return|;
block|}
comment|/**      * Configures a threshold in percentage. The CircuitBreaker considers a call as slow when the call duration is greater than slowCallDurationThreshold(Duration.      * When the percentage of slow calls is equal or greater the threshold, the CircuitBreaker transitions to open and starts short-circuiting calls.      *<p>      * The threshold must be greater than 0 and not greater than 100.      * Default value is 100 percentage which means that all recorded calls must be slower than slowCallDurationThreshold.      */
DECL|method|setSlowCallRateThreshold (Float slowCallRateThreshold)
specifier|public
name|void
name|setSlowCallRateThreshold
parameter_list|(
name|Float
name|slowCallRateThreshold
parameter_list|)
block|{
name|this
operator|.
name|slowCallRateThreshold
operator|=
name|slowCallRateThreshold
expr_stmt|;
block|}
DECL|method|getSlowCallDurationThreshold ()
specifier|public
name|Integer
name|getSlowCallDurationThreshold
parameter_list|()
block|{
return|return
name|slowCallDurationThreshold
return|;
block|}
comment|/**      * Configures the duration threshold (seconds) above which calls are considered as slow and increase the slow calls percentage.      * Default value is 60 seconds.      */
DECL|method|setSlowCallDurationThreshold (Integer slowCallDurationThreshold)
specifier|public
name|void
name|setSlowCallDurationThreshold
parameter_list|(
name|Integer
name|slowCallDurationThreshold
parameter_list|)
block|{
name|this
operator|.
name|slowCallDurationThreshold
operator|=
name|slowCallDurationThreshold
expr_stmt|;
block|}
DECL|method|getBulkheadEnabled ()
specifier|public
name|Boolean
name|getBulkheadEnabled
parameter_list|()
block|{
return|return
name|bulkheadEnabled
return|;
block|}
comment|/**      * Whether bulkhead is enabled or not on the circuit breaker.      */
DECL|method|setBulkheadEnabled (Boolean bulkheadEnabled)
specifier|public
name|void
name|setBulkheadEnabled
parameter_list|(
name|Boolean
name|bulkheadEnabled
parameter_list|)
block|{
name|this
operator|.
name|bulkheadEnabled
operator|=
name|bulkheadEnabled
expr_stmt|;
block|}
DECL|method|getBulkheadMaxConcurrentCalls ()
specifier|public
name|Integer
name|getBulkheadMaxConcurrentCalls
parameter_list|()
block|{
return|return
name|bulkheadMaxConcurrentCalls
return|;
block|}
comment|/**      * Configures the max amount of concurrent calls the bulkhead will support.      */
DECL|method|setBulkheadMaxConcurrentCalls (Integer bulkheadMaxConcurrentCalls)
specifier|public
name|void
name|setBulkheadMaxConcurrentCalls
parameter_list|(
name|Integer
name|bulkheadMaxConcurrentCalls
parameter_list|)
block|{
name|this
operator|.
name|bulkheadMaxConcurrentCalls
operator|=
name|bulkheadMaxConcurrentCalls
expr_stmt|;
block|}
DECL|method|getBulkheadMaxWaitDuration ()
specifier|public
name|Integer
name|getBulkheadMaxWaitDuration
parameter_list|()
block|{
return|return
name|bulkheadMaxWaitDuration
return|;
block|}
comment|/**      * Configures a maximum amount of time which the calling thread will wait to enter the bulkhead. If bulkhead has space available, entry      * is guaranteed and immediate. If bulkhead is full, calling threads will contest for space, if it becomes available. maxWaitDuration can be set to 0.      *<p>      * Note: for threads running on an event-loop or equivalent (rx computation pool, etc), setting maxWaitDuration to 0 is highly recommended. Blocking      * an event-loop thread will most likely have a negative effect on application throughput.      */
DECL|method|setBulkheadMaxWaitDuration (Integer bulkheadMaxWaitDuration)
specifier|public
name|void
name|setBulkheadMaxWaitDuration
parameter_list|(
name|Integer
name|bulkheadMaxWaitDuration
parameter_list|)
block|{
name|this
operator|.
name|bulkheadMaxWaitDuration
operator|=
name|bulkheadMaxWaitDuration
expr_stmt|;
block|}
DECL|method|getTimeoutEnabled ()
specifier|public
name|Boolean
name|getTimeoutEnabled
parameter_list|()
block|{
return|return
name|timeoutEnabled
return|;
block|}
comment|/**      * Whether timeout is enabled or not on the circuit breaker.      * Default is false.      */
DECL|method|setTimeoutEnabled (Boolean timeoutEnabled)
specifier|public
name|void
name|setTimeoutEnabled
parameter_list|(
name|Boolean
name|timeoutEnabled
parameter_list|)
block|{
name|this
operator|.
name|timeoutEnabled
operator|=
name|timeoutEnabled
expr_stmt|;
block|}
DECL|method|getTimeoutExecutorServiceRef ()
specifier|public
name|String
name|getTimeoutExecutorServiceRef
parameter_list|()
block|{
return|return
name|timeoutExecutorServiceRef
return|;
block|}
comment|/**      * References to a custom thread pool to use when timeout is enabled (uses {@link ForkJoinPool#commonPool()} by default)      */
DECL|method|setTimeoutExecutorServiceRef (String timeoutExecutorServiceRef)
specifier|public
name|void
name|setTimeoutExecutorServiceRef
parameter_list|(
name|String
name|timeoutExecutorServiceRef
parameter_list|)
block|{
name|this
operator|.
name|timeoutExecutorServiceRef
operator|=
name|timeoutExecutorServiceRef
expr_stmt|;
block|}
DECL|method|getTimeoutDuration ()
specifier|public
name|Integer
name|getTimeoutDuration
parameter_list|()
block|{
return|return
name|timeoutDuration
return|;
block|}
comment|/**      * Configures the thread execution timeout (millis).      * Default value is 1000 millis (1 second).      */
DECL|method|setTimeoutDuration (Integer timeoutDuration)
specifier|public
name|void
name|setTimeoutDuration
parameter_list|(
name|Integer
name|timeoutDuration
parameter_list|)
block|{
name|this
operator|.
name|timeoutDuration
operator|=
name|timeoutDuration
expr_stmt|;
block|}
DECL|method|getTimeoutCancelRunningFuture ()
specifier|public
name|Boolean
name|getTimeoutCancelRunningFuture
parameter_list|()
block|{
return|return
name|timeoutCancelRunningFuture
return|;
block|}
comment|/**      * Configures whether cancel is called on the running future.      * Defaults to true.      */
DECL|method|setTimeoutCancelRunningFuture (Boolean timeoutCancelRunningFuture)
specifier|public
name|void
name|setTimeoutCancelRunningFuture
parameter_list|(
name|Boolean
name|timeoutCancelRunningFuture
parameter_list|)
block|{
name|this
operator|.
name|timeoutCancelRunningFuture
operator|=
name|timeoutCancelRunningFuture
expr_stmt|;
block|}
comment|/**      * Refers to an existing io.github.resilience4j.circuitbreaker.CircuitBreaker instance      * to lookup and use from the registry. When using this, then any other circuit breaker options      * are not in use.      */
DECL|method|withCircuitBreakerRef (String circuitBreakerRef)
specifier|public
name|Resilience4jConfigurationProperties
name|withCircuitBreakerRef
parameter_list|(
name|String
name|circuitBreakerRef
parameter_list|)
block|{
name|this
operator|.
name|circuitBreakerRef
operator|=
name|circuitBreakerRef
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Refers to an existing io.github.resilience4j.circuitbreaker.CircuitBreakerConfig instance      * to lookup and use from the registry.      */
DECL|method|withConfigRef (String configRef)
specifier|public
name|Resilience4jConfigurationProperties
name|withConfigRef
parameter_list|(
name|String
name|configRef
parameter_list|)
block|{
name|this
operator|.
name|configRef
operator|=
name|configRef
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the failure rate threshold in percentage.      * If the failure rate is equal or greater than the threshold the CircuitBreaker transitions to open and starts short-circuiting calls.      *<p>      * The threshold must be greater than 0 and not greater than 100. Default value is 50 percentage.      */
DECL|method|withFailureRateThreshold (Float failureRateThreshold)
specifier|public
name|Resilience4jConfigurationProperties
name|withFailureRateThreshold
parameter_list|(
name|Float
name|failureRateThreshold
parameter_list|)
block|{
name|this
operator|.
name|failureRateThreshold
operator|=
name|failureRateThreshold
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the number of permitted calls when the CircuitBreaker is half open.      *<p>      * The size must be greater than 0. Default size is 10.      */
DECL|method|withPermittedNumberOfCallsInHalfOpenState (Integer permittedNumberOfCallsInHalfOpenState)
specifier|public
name|Resilience4jConfigurationProperties
name|withPermittedNumberOfCallsInHalfOpenState
parameter_list|(
name|Integer
name|permittedNumberOfCallsInHalfOpenState
parameter_list|)
block|{
name|this
operator|.
name|permittedNumberOfCallsInHalfOpenState
operator|=
name|permittedNumberOfCallsInHalfOpenState
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the size of the sliding window which is used to record the outcome of calls when the CircuitBreaker is closed.      * {@code slidingWindowSize} configures the size of the sliding window. Sliding window can either be count-based or time-based.      *      * If {@code slidingWindowType} is COUNT_BASED, the last {@code slidingWindowSize} calls are recorded and aggregated.      * If {@code slidingWindowType} is TIME_BASED, the calls of the last {@code slidingWindowSize} seconds are recorded and aggregated.      *<p>      * The {@code slidingWindowSize} must be greater than 0.      * The {@code minimumNumberOfCalls} must be greater than 0.      * If the slidingWindowType is COUNT_BASED, the {@code minimumNumberOfCalls} cannot be greater than {@code slidingWindowSize}.      * If the slidingWindowType is TIME_BASED, you can pick whatever you want.      *      * Default slidingWindowSize is 100.      */
DECL|method|withSlidingWindowSize (Integer slidingWindowSize)
specifier|public
name|Resilience4jConfigurationProperties
name|withSlidingWindowSize
parameter_list|(
name|Integer
name|slidingWindowSize
parameter_list|)
block|{
name|this
operator|.
name|slidingWindowSize
operator|=
name|slidingWindowSize
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the type of the sliding window which is used to record the outcome of calls when the CircuitBreaker is closed.      * Sliding window can either be count-based or time-based.      *      * If {@code slidingWindowType} is COUNT_BASED, the last {@code slidingWindowSize} calls are recorded and aggregated.      * If {@code slidingWindowType} is TIME_BASED, the calls of the last {@code slidingWindowSize} seconds are recorded and aggregated.      *      * Default slidingWindowType is COUNT_BASED.      */
DECL|method|withSlidingWindowType (String slidingWindowType)
specifier|public
name|Resilience4jConfigurationProperties
name|withSlidingWindowType
parameter_list|(
name|String
name|slidingWindowType
parameter_list|)
block|{
name|this
operator|.
name|slidingWindowType
operator|=
name|slidingWindowType
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures configures the minimum number of calls which are required (per sliding window period) before the CircuitBreaker can calculate the error rate.      * For example, if {@code minimumNumberOfCalls} is 10, then at least 10 calls must be recorded, before the failure rate can be calculated.      * If only 9 calls have been recorded the CircuitBreaker will not transition to open even if all 9 calls have failed.      *      * Default minimumNumberOfCalls is 100      */
DECL|method|withMinimumNumberOfCalls (Integer minimumNumberOfCalls)
specifier|public
name|Resilience4jConfigurationProperties
name|withMinimumNumberOfCalls
parameter_list|(
name|Integer
name|minimumNumberOfCalls
parameter_list|)
block|{
name|this
operator|.
name|minimumNumberOfCalls
operator|=
name|minimumNumberOfCalls
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Enables writable stack traces. When set to false, Exception.getStackTrace returns a zero length array.      * This may be used to reduce log spam when the circuit breaker is open as the cause of the exceptions is already known (the circuit breaker is short-circuiting calls).      */
DECL|method|withWritableStackTraceEnabled (Boolean writableStackTraceEnabled)
specifier|public
name|Resilience4jConfigurationProperties
name|withWritableStackTraceEnabled
parameter_list|(
name|Boolean
name|writableStackTraceEnabled
parameter_list|)
block|{
name|this
operator|.
name|writableStackTraceEnabled
operator|=
name|writableStackTraceEnabled
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the wait duration (in seconds) which specifies how long the CircuitBreaker should stay open, before it switches to half open.      * Default value is 60 seconds.      */
DECL|method|withWaitDurationInOpenState (Integer waitDurationInOpenState)
specifier|public
name|Resilience4jConfigurationProperties
name|withWaitDurationInOpenState
parameter_list|(
name|Integer
name|waitDurationInOpenState
parameter_list|)
block|{
name|this
operator|.
name|waitDurationInOpenState
operator|=
name|waitDurationInOpenState
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withAutomaticTransitionFromOpenToHalfOpenEnabled (Boolean automaticTransitionFromOpenToHalfOpenEnabled)
specifier|public
name|Resilience4jConfigurationProperties
name|withAutomaticTransitionFromOpenToHalfOpenEnabled
parameter_list|(
name|Boolean
name|automaticTransitionFromOpenToHalfOpenEnabled
parameter_list|)
block|{
name|this
operator|.
name|automaticTransitionFromOpenToHalfOpenEnabled
operator|=
name|automaticTransitionFromOpenToHalfOpenEnabled
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures a threshold in percentage. The CircuitBreaker considers a call as slow when the call duration is greater than slowCallDurationThreshold(Duration.      * When the percentage of slow calls is equal or greater the threshold, the CircuitBreaker transitions to open and starts short-circuiting calls.      *<p>      * The threshold must be greater than 0 and not greater than 100.      * Default value is 100 percentage which means that all recorded calls must be slower than slowCallDurationThreshold.      */
DECL|method|withSlowCallRateThreshold (Float slowCallRateThreshold)
specifier|public
name|Resilience4jConfigurationProperties
name|withSlowCallRateThreshold
parameter_list|(
name|Float
name|slowCallRateThreshold
parameter_list|)
block|{
name|this
operator|.
name|slowCallRateThreshold
operator|=
name|slowCallRateThreshold
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the duration threshold (seconds) above which calls are considered as slow and increase the slow calls percentage.      * Default value is 60 seconds.      */
DECL|method|withSlowCallDurationThreshold (Integer slowCallDurationThreshold)
specifier|public
name|Resilience4jConfigurationProperties
name|withSlowCallDurationThreshold
parameter_list|(
name|Integer
name|slowCallDurationThreshold
parameter_list|)
block|{
name|this
operator|.
name|slowCallDurationThreshold
operator|=
name|slowCallDurationThreshold
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether bulkhead is enabled or not on the circuit breaker.      */
DECL|method|withBulkheadEnabled (Boolean bulkheadEnabled)
specifier|public
name|Resilience4jConfigurationProperties
name|withBulkheadEnabled
parameter_list|(
name|Boolean
name|bulkheadEnabled
parameter_list|)
block|{
name|this
operator|.
name|bulkheadEnabled
operator|=
name|bulkheadEnabled
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the max amount of concurrent calls the bulkhead will support.      */
DECL|method|withBulkheadMaxConcurrentCalls (Integer bulkheadMaxConcurrentCalls)
specifier|public
name|Resilience4jConfigurationProperties
name|withBulkheadMaxConcurrentCalls
parameter_list|(
name|Integer
name|bulkheadMaxConcurrentCalls
parameter_list|)
block|{
name|this
operator|.
name|bulkheadMaxConcurrentCalls
operator|=
name|bulkheadMaxConcurrentCalls
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures a maximum amount of time which the calling thread will wait to enter the bulkhead. If bulkhead has space available, entry      * is guaranteed and immediate. If bulkhead is full, calling threads will contest for space, if it becomes available. maxWaitDuration can be set to 0.      *<p>      * Note: for threads running on an event-loop or equivalent (rx computation pool, etc), setting maxWaitDuration to 0 is highly recommended. Blocking      * an event-loop thread will most likely have a negative effect on application throughput.      */
DECL|method|withBulkheadMaxWaitDuration (Integer bulkheadMaxWaitDuration)
specifier|public
name|Resilience4jConfigurationProperties
name|withBulkheadMaxWaitDuration
parameter_list|(
name|Integer
name|bulkheadMaxWaitDuration
parameter_list|)
block|{
name|this
operator|.
name|bulkheadMaxWaitDuration
operator|=
name|bulkheadMaxWaitDuration
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether timeout is enabled or not on the circuit breaker.      * Default is false.      */
DECL|method|withTimeoutEnabled (Boolean timeoutEnabled)
specifier|public
name|Resilience4jConfigurationProperties
name|withTimeoutEnabled
parameter_list|(
name|Boolean
name|timeoutEnabled
parameter_list|)
block|{
name|this
operator|.
name|timeoutEnabled
operator|=
name|timeoutEnabled
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * References to a custom thread pool to use when timeout is enabled (uses {@link ForkJoinPool#commonPool()} by default)      */
DECL|method|withTimeoutExecutorServiceRef (String timeoutExecutorServiceRef)
specifier|public
name|Resilience4jConfigurationProperties
name|withTimeoutExecutorServiceRef
parameter_list|(
name|String
name|timeoutExecutorServiceRef
parameter_list|)
block|{
name|this
operator|.
name|timeoutExecutorServiceRef
operator|=
name|timeoutExecutorServiceRef
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the thread execution timeout (millis).      * Default value is 1000 millis (1 second).      */
DECL|method|withTimeoutDuration (Integer timeoutDuration)
specifier|public
name|Resilience4jConfigurationProperties
name|withTimeoutDuration
parameter_list|(
name|Integer
name|timeoutDuration
parameter_list|)
block|{
name|this
operator|.
name|timeoutDuration
operator|=
name|timeoutDuration
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures whether cancel is called on the running future.      * Defaults to true.      */
DECL|method|withTimeoutCancelRunningFuture (Boolean timeoutCancelRunningFuture)
specifier|public
name|Resilience4jConfigurationProperties
name|withTimeoutCancelRunningFuture
parameter_list|(
name|Boolean
name|timeoutCancelRunningFuture
parameter_list|)
block|{
name|this
operator|.
name|timeoutCancelRunningFuture
operator|=
name|timeoutCancelRunningFuture
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

