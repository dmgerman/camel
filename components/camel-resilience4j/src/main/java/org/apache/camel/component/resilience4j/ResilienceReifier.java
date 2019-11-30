begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.resilience4j
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|resilience4j
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Optional
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
name|ExecutorService
import|;
end_import

begin_import
import|import
name|io
operator|.
name|github
operator|.
name|resilience4j
operator|.
name|bulkhead
operator|.
name|BulkheadConfig
import|;
end_import

begin_import
import|import
name|io
operator|.
name|github
operator|.
name|resilience4j
operator|.
name|circuitbreaker
operator|.
name|CircuitBreaker
import|;
end_import

begin_import
import|import
name|io
operator|.
name|github
operator|.
name|resilience4j
operator|.
name|circuitbreaker
operator|.
name|CircuitBreakerConfig
import|;
end_import

begin_import
import|import
name|io
operator|.
name|github
operator|.
name|resilience4j
operator|.
name|timelimiter
operator|.
name|TimeLimiterConfig
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
name|ExtendedCamelContext
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
name|model
operator|.
name|CircuitBreakerDefinition
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
name|model
operator|.
name|Model
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
name|model
operator|.
name|ProcessorDefinitionHelper
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
name|model
operator|.
name|Resilience4jConfigurationCommon
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
name|model
operator|.
name|Resilience4jConfigurationDefinition
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
name|reifier
operator|.
name|ProcessorReifier
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
name|BeanIntrospection
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
name|RouteContext
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
name|PropertyBindingSupport
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
name|function
operator|.
name|Suppliers
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|CamelContextHelper
operator|.
name|lookup
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|CamelContextHelper
operator|.
name|mandatoryLookup
import|;
end_import

begin_class
DECL|class|ResilienceReifier
specifier|public
class|class
name|ResilienceReifier
extends|extends
name|ProcessorReifier
argument_list|<
name|CircuitBreakerDefinition
argument_list|>
block|{
DECL|method|ResilienceReifier (CircuitBreakerDefinition definition)
specifier|public
name|ResilienceReifier
parameter_list|(
name|CircuitBreakerDefinition
name|definition
parameter_list|)
block|{
name|super
argument_list|(
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
comment|// create the regular and fallback processors
name|Processor
name|processor
init|=
name|createChildProcessor
argument_list|(
name|routeContext
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Processor
name|fallback
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getOnFallback
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|fallback
operator|=
name|ProcessorReifier
operator|.
name|reifier
argument_list|(
name|definition
operator|.
name|getOnFallback
argument_list|()
argument_list|)
operator|.
name|createProcessor
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
name|boolean
name|fallbackViaNetwork
init|=
name|definition
operator|.
name|getOnFallback
argument_list|()
operator|!=
literal|null
operator|&&
name|parseBoolean
argument_list|(
name|routeContext
argument_list|,
name|definition
operator|.
name|getOnFallback
argument_list|()
operator|.
name|getFallbackViaNetwork
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|fallbackViaNetwork
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"camel-resilience4j does not support onFallbackViaNetwork"
argument_list|)
throw|;
block|}
specifier|final
name|Resilience4jConfigurationCommon
name|config
init|=
name|buildResilience4jConfiguration
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|CircuitBreakerConfig
name|cbConfig
init|=
name|configureCircuitBreaker
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|BulkheadConfig
name|bhConfig
init|=
name|configureBulkHead
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|TimeLimiterConfig
name|tlConfig
init|=
name|configureTimeLimiter
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|ResilienceProcessor
name|answer
init|=
operator|new
name|ResilienceProcessor
argument_list|(
name|cbConfig
argument_list|,
name|bhConfig
argument_list|,
name|tlConfig
argument_list|,
name|processor
argument_list|,
name|fallback
argument_list|)
decl_stmt|;
name|configureTimeoutExecutorService
argument_list|(
name|answer
argument_list|,
name|routeContext
argument_list|,
name|config
argument_list|)
expr_stmt|;
comment|// using any existing circuit breakers?
if|if
condition|(
name|config
operator|.
name|getCircuitBreakerRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|CircuitBreaker
name|cb
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|config
operator|.
name|getCircuitBreakerRef
argument_list|()
argument_list|,
name|CircuitBreaker
operator|.
name|class
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setCircuitBreaker
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|configureCircuitBreaker (Resilience4jConfigurationCommon config)
specifier|private
name|CircuitBreakerConfig
name|configureCircuitBreaker
parameter_list|(
name|Resilience4jConfigurationCommon
name|config
parameter_list|)
block|{
name|CircuitBreakerConfig
operator|.
name|Builder
name|builder
init|=
name|CircuitBreakerConfig
operator|.
name|custom
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getAutomaticTransitionFromOpenToHalfOpenEnabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|automaticTransitionFromOpenToHalfOpenEnabled
argument_list|(
name|config
operator|.
name|getAutomaticTransitionFromOpenToHalfOpenEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getFailureRateThreshold
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|failureRateThreshold
argument_list|(
name|config
operator|.
name|getFailureRateThreshold
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getMinimumNumberOfCalls
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|minimumNumberOfCalls
argument_list|(
name|config
operator|.
name|getMinimumNumberOfCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getPermittedNumberOfCallsInHalfOpenState
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|permittedNumberOfCallsInHalfOpenState
argument_list|(
name|config
operator|.
name|getPermittedNumberOfCallsInHalfOpenState
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getSlidingWindowSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|slidingWindowSize
argument_list|(
name|config
operator|.
name|getSlidingWindowSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getSlidingWindowType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|slidingWindowType
argument_list|(
name|CircuitBreakerConfig
operator|.
name|SlidingWindowType
operator|.
name|valueOf
argument_list|(
name|config
operator|.
name|getSlidingWindowType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getSlowCallDurationThreshold
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|slowCallDurationThreshold
argument_list|(
name|Duration
operator|.
name|ofSeconds
argument_list|(
name|config
operator|.
name|getSlowCallDurationThreshold
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getSlowCallRateThreshold
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|slowCallRateThreshold
argument_list|(
name|config
operator|.
name|getSlowCallRateThreshold
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getWaitDurationInOpenState
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|waitDurationInOpenState
argument_list|(
name|Duration
operator|.
name|ofSeconds
argument_list|(
name|config
operator|.
name|getWaitDurationInOpenState
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getWritableStackTraceEnabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|writableStackTraceEnabled
argument_list|(
name|config
operator|.
name|getWritableStackTraceEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|configureBulkHead (Resilience4jConfigurationCommon config)
specifier|private
name|BulkheadConfig
name|configureBulkHead
parameter_list|(
name|Resilience4jConfigurationCommon
name|config
parameter_list|)
block|{
if|if
condition|(
name|config
operator|.
name|getBulkheadEnabled
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|config
operator|.
name|getBulkheadEnabled
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|BulkheadConfig
operator|.
name|Builder
name|builder
init|=
name|BulkheadConfig
operator|.
name|custom
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getBulkheadMaxConcurrentCalls
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|maxConcurrentCalls
argument_list|(
name|config
operator|.
name|getBulkheadMaxConcurrentCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getBulkheadMaxWaitDuration
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|maxWaitDuration
argument_list|(
name|Duration
operator|.
name|ofMillis
argument_list|(
name|config
operator|.
name|getBulkheadMaxWaitDuration
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|configureTimeLimiter (Resilience4jConfigurationCommon config)
specifier|private
name|TimeLimiterConfig
name|configureTimeLimiter
parameter_list|(
name|Resilience4jConfigurationCommon
name|config
parameter_list|)
block|{
if|if
condition|(
name|config
operator|.
name|getTimeoutEnabled
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|config
operator|.
name|getTimeoutEnabled
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|TimeLimiterConfig
operator|.
name|Builder
name|builder
init|=
name|TimeLimiterConfig
operator|.
name|custom
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getTimeoutDuration
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|timeoutDuration
argument_list|(
name|Duration
operator|.
name|ofMillis
argument_list|(
name|config
operator|.
name|getTimeoutDuration
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getTimeoutCancelRunningFuture
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|cancelRunningFuture
argument_list|(
name|config
operator|.
name|getTimeoutCancelRunningFuture
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|configureTimeoutExecutorService (ResilienceProcessor processor, RouteContext routeContext, Resilience4jConfigurationCommon config)
specifier|private
name|void
name|configureTimeoutExecutorService
parameter_list|(
name|ResilienceProcessor
name|processor
parameter_list|,
name|RouteContext
name|routeContext
parameter_list|,
name|Resilience4jConfigurationCommon
name|config
parameter_list|)
block|{
if|if
condition|(
name|config
operator|.
name|getTimeoutEnabled
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|config
operator|.
name|getTimeoutEnabled
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|config
operator|.
name|getTimeoutExecutorServiceRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|ref
init|=
name|config
operator|.
name|getTimeoutExecutorServiceRef
argument_list|()
decl_stmt|;
name|boolean
name|shutdownThreadPool
init|=
literal|false
decl_stmt|;
name|ExecutorService
name|executorService
init|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|ref
argument_list|,
name|ExecutorService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
name|executorService
operator|=
name|ProcessorDefinitionHelper
operator|.
name|lookupExecutorServiceRef
argument_list|(
name|routeContext
argument_list|,
literal|"CircuitBreaker"
argument_list|,
name|definition
argument_list|,
name|ref
argument_list|)
expr_stmt|;
name|shutdownThreadPool
operator|=
literal|true
expr_stmt|;
block|}
name|processor
operator|.
name|setExecutorService
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setShutdownExecutorService
argument_list|(
name|shutdownThreadPool
argument_list|)
expr_stmt|;
block|}
block|}
comment|// *******************************
comment|// Helpers
comment|// *******************************
DECL|method|buildResilience4jConfiguration (CamelContext camelContext)
name|Resilience4jConfigurationDefinition
name|buildResilience4jConfiguration
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Extract properties from default configuration, the one configured on
comment|// camel context takes the precedence over those in the registry
name|loadProperties
argument_list|(
name|camelContext
argument_list|,
name|properties
argument_list|,
name|Suppliers
operator|.
name|firstNotNull
argument_list|(
parameter_list|()
lambda|->
name|camelContext
operator|.
name|getExtension
argument_list|(
name|Model
operator|.
name|class
argument_list|)
operator|.
name|getResilience4jConfiguration
argument_list|(
literal|null
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|lookup
argument_list|(
name|camelContext
argument_list|,
literal|"Camel"
argument_list|,
name|Resilience4jConfigurationDefinition
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// Extract properties from referenced configuration, the one configured
comment|// on camel context takes the precedence over those in the registry
if|if
condition|(
name|definition
operator|.
name|getConfigurationRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|ref
init|=
name|definition
operator|.
name|getConfigurationRef
argument_list|()
decl_stmt|;
name|loadProperties
argument_list|(
name|camelContext
argument_list|,
name|properties
argument_list|,
name|Suppliers
operator|.
name|firstNotNull
argument_list|(
parameter_list|()
lambda|->
name|camelContext
operator|.
name|getExtension
argument_list|(
name|Model
operator|.
name|class
argument_list|)
operator|.
name|getResilience4jConfiguration
argument_list|(
name|ref
argument_list|)
argument_list|,
parameter_list|()
lambda|->
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|ref
argument_list|,
name|Resilience4jConfigurationDefinition
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Extract properties from local configuration
name|loadProperties
argument_list|(
name|camelContext
argument_list|,
name|properties
argument_list|,
name|Optional
operator|.
name|ofNullable
argument_list|(
name|definition
operator|.
name|getResilience4jConfiguration
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Extract properties from definition
name|BeanIntrospection
name|beanIntrospection
init|=
name|camelContext
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getBeanIntrospection
argument_list|()
decl_stmt|;
name|beanIntrospection
operator|.
name|getProperties
argument_list|(
name|definition
argument_list|,
name|properties
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Resilience4jConfigurationDefinition
name|config
init|=
operator|new
name|Resilience4jConfigurationDefinition
argument_list|()
decl_stmt|;
comment|// Apply properties to a new configuration
name|PropertyBindingSupport
operator|.
name|bindProperties
argument_list|(
name|camelContext
argument_list|,
name|config
argument_list|,
name|properties
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
DECL|method|loadProperties (CamelContext camelContext, Map<String, Object> properties, Optional<?> optional)
specifier|private
name|void
name|loadProperties
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|Optional
argument_list|<
name|?
argument_list|>
name|optional
parameter_list|)
block|{
name|BeanIntrospection
name|beanIntrospection
init|=
name|camelContext
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getBeanIntrospection
argument_list|()
decl_stmt|;
name|optional
operator|.
name|ifPresent
argument_list|(
name|bean
lambda|->
name|beanIntrospection
operator|.
name|getProperties
argument_list|(
name|bean
argument_list|,
name|properties
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

