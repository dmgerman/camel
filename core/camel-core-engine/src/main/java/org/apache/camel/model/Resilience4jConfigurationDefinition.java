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
name|ForkJoinPool
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
comment|/**  * Resilience4j Circuit Breaker EIP configuration  */
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
literal|"resilience4jConfiguration"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|Resilience4jConfigurationDefinition
specifier|public
class|class
name|Resilience4jConfigurationDefinition
extends|extends
name|Resilience4jConfigurationCommon
block|{
annotation|@
name|XmlTransient
DECL|field|parent
specifier|private
name|CircuitBreakerDefinition
name|parent
decl_stmt|;
DECL|method|Resilience4jConfigurationDefinition ()
specifier|public
name|Resilience4jConfigurationDefinition
parameter_list|()
block|{     }
DECL|method|Resilience4jConfigurationDefinition (CircuitBreakerDefinition parent)
specifier|public
name|Resilience4jConfigurationDefinition
parameter_list|(
name|CircuitBreakerDefinition
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
comment|/**      * Refers to an existing io.github.resilience4j.circuitbreaker.CircuitBreaker instance      * to lookup and use from the registry. When using this, then any other circuit breaker options      * are not in use.      */
DECL|method|circuitBreakerRef (String circuitBreakerRef)
specifier|public
name|Resilience4jConfigurationDefinition
name|circuitBreakerRef
parameter_list|(
name|String
name|circuitBreakerRef
parameter_list|)
block|{
name|setCircuitBreakerRef
argument_list|(
name|circuitBreakerRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Refers to an existing io.github.resilience4j.circuitbreaker.CircuitBreakerConfig instance      * to lookup and use from the registry.      */
DECL|method|configRef (String ref)
specifier|public
name|Resilience4jConfigurationDefinition
name|configRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|setConfigRef
argument_list|(
name|ref
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the failure rate threshold in percentage.      * If the failure rate is equal or greater than the threshold the CircuitBreaker transitions to open and starts short-circuiting calls.      *<p>      * The threshold must be greater than 0 and not greater than 100. Default value is 50 percentage.      */
DECL|method|failureRateThreshold (Float failureRateThreshold)
specifier|public
name|Resilience4jConfigurationDefinition
name|failureRateThreshold
parameter_list|(
name|Float
name|failureRateThreshold
parameter_list|)
block|{
name|setFailureRateThreshold
argument_list|(
name|failureRateThreshold
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the number of permitted calls when the CircuitBreaker is half open.      *<p>      * The size must be greater than 0. Default size is 10.      */
DECL|method|permittedNumberOfCallsInHalfOpenState (Integer permittedNumberOfCallsInHalfOpenState)
specifier|public
name|Resilience4jConfigurationDefinition
name|permittedNumberOfCallsInHalfOpenState
parameter_list|(
name|Integer
name|permittedNumberOfCallsInHalfOpenState
parameter_list|)
block|{
name|setPermittedNumberOfCallsInHalfOpenState
argument_list|(
name|permittedNumberOfCallsInHalfOpenState
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the size of the sliding window which is used to record the outcome of calls when the CircuitBreaker is closed.      * {@code slidingWindowSize} configures the size of the sliding window. Sliding window can either be count-based or time-based.      *      * If {@code slidingWindowType} is COUNT_BASED, the last {@code slidingWindowSize} calls are recorded and aggregated.      * If {@code slidingWindowType} is TIME_BASED, the calls of the last {@code slidingWindowSize} seconds are recorded and aggregated.      *<p>      * The {@code slidingWindowSize} must be greater than 0.      * The {@code minimumNumberOfCalls} must be greater than 0.      * If the slidingWindowType is COUNT_BASED, the {@code minimumNumberOfCalls} cannot be greater than {@code slidingWindowSize}.      * If the slidingWindowType is TIME_BASED, you can pick whatever you want.      *      * Default slidingWindowSize is 100.      */
DECL|method|slidingWindowSize (Integer slidingWindowSize)
specifier|public
name|Resilience4jConfigurationDefinition
name|slidingWindowSize
parameter_list|(
name|Integer
name|slidingWindowSize
parameter_list|)
block|{
name|setSlidingWindowSize
argument_list|(
name|slidingWindowSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the type of the sliding window which is used to record the outcome of calls when the CircuitBreaker is closed.      * Sliding window can either be count-based or time-based.      *      * If {@code slidingWindowType} is COUNT_BASED, the last {@code slidingWindowSize} calls are recorded and aggregated.      * If {@code slidingWindowType} is TIME_BASED, the calls of the last {@code slidingWindowSize} seconds are recorded and aggregated.      *      * Default slidingWindowType is COUNT_BASED.      */
DECL|method|slidingWindowType (String slidingWindowType)
specifier|public
name|Resilience4jConfigurationDefinition
name|slidingWindowType
parameter_list|(
name|String
name|slidingWindowType
parameter_list|)
block|{
name|setSlidingWindowType
argument_list|(
name|slidingWindowType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures configures the minimum number of calls which are required (per sliding window period) before the CircuitBreaker can calculate the error rate.      * For example, if {@code minimumNumberOfCalls} is 10, then at least 10 calls must be recorded, before the failure rate can be calculated.      * If only 9 calls have been recorded the CircuitBreaker will not transition to open even if all 9 calls have failed.      *      * Default minimumNumberOfCalls is 100      */
DECL|method|minimumNumberOfCalls (Integer minimumNumberOfCalls)
specifier|public
name|Resilience4jConfigurationDefinition
name|minimumNumberOfCalls
parameter_list|(
name|Integer
name|minimumNumberOfCalls
parameter_list|)
block|{
name|setMinimumNumberOfCalls
argument_list|(
name|minimumNumberOfCalls
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Enables writable stack traces. When set to false, Exception.getStackTrace returns a zero length array.      * This may be used to reduce log spam when the circuit breaker is open as the cause of the exceptions is already known (the circuit breaker is short-circuiting calls).      */
DECL|method|writableStackTraceEnabled (Boolean writableStackTraceEnabled)
specifier|public
name|Resilience4jConfigurationDefinition
name|writableStackTraceEnabled
parameter_list|(
name|Boolean
name|writableStackTraceEnabled
parameter_list|)
block|{
name|setWritableStackTraceEnabled
argument_list|(
name|writableStackTraceEnabled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the wait duration (in seconds) which specifies how long the CircuitBreaker should stay open, before it switches to half open.      * Default value is 60 seconds.      */
DECL|method|waitDurationInOpenState (Integer waitDurationInOpenState)
specifier|public
name|Resilience4jConfigurationDefinition
name|waitDurationInOpenState
parameter_list|(
name|Integer
name|waitDurationInOpenState
parameter_list|)
block|{
name|setWaitDurationInOpenState
argument_list|(
name|waitDurationInOpenState
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Enables automatic transition from OPEN to HALF_OPEN state once the waitDurationInOpenState has passed.      */
DECL|method|automaticTransitionFromOpenToHalfOpenEnabled (Boolean automaticTransitionFromOpenToHalfOpenEnabled)
specifier|public
name|Resilience4jConfigurationDefinition
name|automaticTransitionFromOpenToHalfOpenEnabled
parameter_list|(
name|Boolean
name|automaticTransitionFromOpenToHalfOpenEnabled
parameter_list|)
block|{
name|setAutomaticTransitionFromOpenToHalfOpenEnabled
argument_list|(
name|automaticTransitionFromOpenToHalfOpenEnabled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures a threshold in percentage. The CircuitBreaker considers a call as slow when the call duration is greater than slowCallDurationThreshold(Duration.      * When the percentage of slow calls is equal or greater the threshold, the CircuitBreaker transitions to open and starts short-circuiting calls.      *<p>      * The threshold must be greater than 0 and not greater than 100.      * Default value is 100 percentage which means that all recorded calls must be slower than slowCallDurationThreshold.      */
DECL|method|slowCallRateThreshold (Float slowCallRateThreshold)
specifier|public
name|Resilience4jConfigurationDefinition
name|slowCallRateThreshold
parameter_list|(
name|Float
name|slowCallRateThreshold
parameter_list|)
block|{
name|setSlowCallRateThreshold
argument_list|(
name|slowCallRateThreshold
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the duration threshold (seconds) above which calls are considered as slow and increase the slow calls percentage.      * Default value is 60 seconds.      */
DECL|method|slowCallDurationThreshold (Integer slowCallDurationThreshold)
specifier|public
name|Resilience4jConfigurationDefinition
name|slowCallDurationThreshold
parameter_list|(
name|Integer
name|slowCallDurationThreshold
parameter_list|)
block|{
name|setSlowCallDurationThreshold
argument_list|(
name|slowCallDurationThreshold
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether bulkhead is enabled or not on the circuit breaker.      * Default is false.      */
DECL|method|bulkheadEnabled (Boolean bulkheadEnabled)
specifier|public
name|Resilience4jConfigurationDefinition
name|bulkheadEnabled
parameter_list|(
name|Boolean
name|bulkheadEnabled
parameter_list|)
block|{
name|setBulkheadEnabled
argument_list|(
name|bulkheadEnabled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the max amount of concurrent calls the bulkhead will support.      */
DECL|method|bulkheadMaxConcurrentCalls (Integer bulkheadMaxConcurrentCalls)
specifier|public
name|Resilience4jConfigurationDefinition
name|bulkheadMaxConcurrentCalls
parameter_list|(
name|Integer
name|bulkheadMaxConcurrentCalls
parameter_list|)
block|{
name|setBulkheadMaxWaitDuration
argument_list|(
name|bulkheadMaxConcurrentCalls
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures a maximum amount of time which the calling thread will wait to enter the bulkhead. If bulkhead has space available, entry      * is guaranteed and immediate. If bulkhead is full, calling threads will contest for space, if it becomes available. maxWaitDuration can be set to 0.      *<p>      * Note: for threads running on an event-loop or equivalent (rx computation pool, etc), setting maxWaitDuration to 0 is highly recommended. Blocking      * an event-loop thread will most likely have a negative effect on application throughput.      */
DECL|method|bulkheadMaxWaitDuration (Integer bulkheadMaxWaitDuration)
specifier|public
name|Resilience4jConfigurationDefinition
name|bulkheadMaxWaitDuration
parameter_list|(
name|Integer
name|bulkheadMaxWaitDuration
parameter_list|)
block|{
name|setBulkheadMaxWaitDuration
argument_list|(
name|bulkheadMaxWaitDuration
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Whether timeout is enabled or not on the circuit breaker.      * Default is false.      */
DECL|method|timeoutEnabled (Boolean timeoutEnabled)
specifier|public
name|Resilience4jConfigurationDefinition
name|timeoutEnabled
parameter_list|(
name|Boolean
name|timeoutEnabled
parameter_list|)
block|{
name|setTimeoutEnabled
argument_list|(
name|timeoutEnabled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * References to a custom thread pool to use when timeout is enabled (uses {@link ForkJoinPool#commonPool()} by default)      */
DECL|method|timeoutExecutorServiceRef (String executorServiceRef)
specifier|public
name|Resilience4jConfigurationDefinition
name|timeoutExecutorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
name|setTimeoutExecutorServiceRef
argument_list|(
name|executorServiceRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the thread execution timeout (millis).      * Default value is 1000 millis (1 second).      */
DECL|method|timeoutDuration (Integer timeoutDuration)
specifier|public
name|Resilience4jConfigurationDefinition
name|timeoutDuration
parameter_list|(
name|Integer
name|timeoutDuration
parameter_list|)
block|{
name|setTimeoutDuration
argument_list|(
name|timeoutDuration
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures whether cancel is called on the running future.      * Defaults to true.      */
DECL|method|timeoutCancelRunningFuture (Boolean timeoutCancelRunningFuture)
specifier|public
name|Resilience4jConfigurationDefinition
name|timeoutCancelRunningFuture
parameter_list|(
name|Boolean
name|timeoutCancelRunningFuture
parameter_list|)
block|{
name|setTimeoutCancelRunningFuture
argument_list|(
name|timeoutCancelRunningFuture
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * End of configuration.      */
DECL|method|end ()
specifier|public
name|CircuitBreakerDefinition
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

