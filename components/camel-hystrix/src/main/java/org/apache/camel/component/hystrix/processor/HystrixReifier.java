begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hystrix.processor
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
operator|.
name|processor
package|;
end_package

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
name|HystrixConfigurationDefinition
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
DECL|class|HystrixReifier
specifier|public
class|class
name|HystrixReifier
extends|extends
name|ProcessorReifier
argument_list|<
name|CircuitBreakerDefinition
argument_list|>
block|{
DECL|method|HystrixReifier (CircuitBreakerDefinition definition)
specifier|public
name|HystrixReifier
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
specifier|final
name|HystrixConfigurationDefinition
name|config
init|=
name|buildHystrixConfiguration
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|String
name|id
init|=
name|getId
argument_list|(
name|definition
argument_list|,
name|routeContext
argument_list|)
decl_stmt|;
comment|// group and thread pool keys to use they can be configured on configRef and config, so look there first, and if none then use default
name|String
name|groupKey
init|=
name|config
operator|.
name|getGroupKey
argument_list|()
decl_stmt|;
name|String
name|threadPoolKey
init|=
name|config
operator|.
name|getThreadPoolKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|groupKey
operator|==
literal|null
condition|)
block|{
name|groupKey
operator|=
name|HystrixConfigurationDefinition
operator|.
name|DEFAULT_GROUP_KEY
expr_stmt|;
block|}
if|if
condition|(
name|threadPoolKey
operator|==
literal|null
condition|)
block|{
comment|// by default use the thread pool from the group
name|threadPoolKey
operator|=
name|groupKey
expr_stmt|;
block|}
comment|// use the node id as the command key
name|HystrixCommandKey
name|hcCommandKey
init|=
name|HystrixCommandKey
operator|.
name|Factory
operator|.
name|asKey
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|HystrixCommandKey
name|hcFallbackCommandKey
init|=
name|HystrixCommandKey
operator|.
name|Factory
operator|.
name|asKey
argument_list|(
name|id
operator|+
literal|"-fallback"
argument_list|)
decl_stmt|;
comment|// use the configured group key
name|HystrixCommandGroupKey
name|hcGroupKey
init|=
name|HystrixCommandGroupKey
operator|.
name|Factory
operator|.
name|asKey
argument_list|(
name|groupKey
argument_list|)
decl_stmt|;
name|HystrixThreadPoolKey
name|tpKey
init|=
name|HystrixThreadPoolKey
operator|.
name|Factory
operator|.
name|asKey
argument_list|(
name|threadPoolKey
argument_list|)
decl_stmt|;
comment|// create setter using the default options
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
name|hcGroupKey
argument_list|)
operator|.
name|andCommandKey
argument_list|(
name|hcCommandKey
argument_list|)
operator|.
name|andThreadPoolKey
argument_list|(
name|tpKey
argument_list|)
decl_stmt|;
name|HystrixCommandProperties
operator|.
name|Setter
name|commandSetter
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
name|commandSetter
argument_list|)
expr_stmt|;
name|HystrixThreadPoolProperties
operator|.
name|Setter
name|threadPoolSetter
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
name|threadPoolSetter
argument_list|)
expr_stmt|;
name|configureHystrix
argument_list|(
name|commandSetter
argument_list|,
name|threadPoolSetter
argument_list|,
name|config
argument_list|)
expr_stmt|;
comment|// create setter for fallback via network
name|HystrixCommand
operator|.
name|Setter
name|fallbackSetter
init|=
literal|null
decl_stmt|;
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
comment|// use a different thread pool that is for fallback (should never use the same thread pool as the regular command)
name|HystrixThreadPoolKey
name|tpFallbackKey
init|=
name|HystrixThreadPoolKey
operator|.
name|Factory
operator|.
name|asKey
argument_list|(
name|threadPoolKey
operator|+
literal|"-fallback"
argument_list|)
decl_stmt|;
name|fallbackSetter
operator|=
name|HystrixCommand
operator|.
name|Setter
operator|.
name|withGroupKey
argument_list|(
name|hcGroupKey
argument_list|)
operator|.
name|andCommandKey
argument_list|(
name|hcFallbackCommandKey
argument_list|)
operator|.
name|andThreadPoolKey
argument_list|(
name|tpFallbackKey
argument_list|)
expr_stmt|;
name|HystrixCommandProperties
operator|.
name|Setter
name|commandFallbackSetter
init|=
name|HystrixCommandProperties
operator|.
name|Setter
argument_list|()
decl_stmt|;
name|fallbackSetter
operator|.
name|andCommandPropertiesDefaults
argument_list|(
name|commandFallbackSetter
argument_list|)
expr_stmt|;
name|HystrixThreadPoolProperties
operator|.
name|Setter
name|fallbackThreadPoolSetter
init|=
name|HystrixThreadPoolProperties
operator|.
name|Setter
argument_list|()
decl_stmt|;
name|fallbackSetter
operator|.
name|andThreadPoolPropertiesDefaults
argument_list|(
name|fallbackThreadPoolSetter
argument_list|)
expr_stmt|;
comment|// at first configure any shared options
name|configureHystrix
argument_list|(
name|commandFallbackSetter
argument_list|,
name|fallbackThreadPoolSetter
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|HystrixProcessor
argument_list|(
name|hcGroupKey
argument_list|,
name|hcCommandKey
argument_list|,
name|hcFallbackCommandKey
argument_list|,
name|setter
argument_list|,
name|fallbackSetter
argument_list|,
name|processor
argument_list|,
name|fallback
argument_list|,
name|fallbackViaNetwork
argument_list|)
return|;
block|}
DECL|method|configureHystrix (HystrixCommandProperties.Setter command, HystrixThreadPoolProperties.Setter threadPool, HystrixConfigurationDefinition config)
specifier|private
name|void
name|configureHystrix
parameter_list|(
name|HystrixCommandProperties
operator|.
name|Setter
name|command
parameter_list|,
name|HystrixThreadPoolProperties
operator|.
name|Setter
name|threadPool
parameter_list|,
name|HystrixConfigurationDefinition
name|config
parameter_list|)
block|{
comment|// command
if|if
condition|(
name|config
operator|.
name|getCircuitBreakerEnabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withCircuitBreakerEnabled
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|config
operator|.
name|getCircuitBreakerEnabled
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getCircuitBreakerErrorThresholdPercentage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withCircuitBreakerErrorThresholdPercentage
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getCircuitBreakerErrorThresholdPercentage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getCircuitBreakerForceClosed
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withCircuitBreakerForceClosed
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|config
operator|.
name|getCircuitBreakerForceClosed
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getCircuitBreakerForceOpen
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withCircuitBreakerForceOpen
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|config
operator|.
name|getCircuitBreakerForceOpen
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getCircuitBreakerRequestVolumeThreshold
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withCircuitBreakerRequestVolumeThreshold
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getCircuitBreakerRequestVolumeThreshold
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getCircuitBreakerSleepWindowInMilliseconds
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withCircuitBreakerSleepWindowInMilliseconds
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getCircuitBreakerSleepWindowInMilliseconds
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getExecutionIsolationSemaphoreMaxConcurrentRequests
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withExecutionIsolationSemaphoreMaxConcurrentRequests
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getExecutionIsolationSemaphoreMaxConcurrentRequests
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getExecutionIsolationStrategy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withExecutionIsolationStrategy
argument_list|(
name|HystrixCommandProperties
operator|.
name|ExecutionIsolationStrategy
operator|.
name|valueOf
argument_list|(
name|config
operator|.
name|getExecutionIsolationStrategy
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getExecutionIsolationThreadInterruptOnTimeout
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withExecutionIsolationThreadInterruptOnTimeout
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|config
operator|.
name|getExecutionIsolationThreadInterruptOnTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getExecutionTimeoutInMilliseconds
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withExecutionTimeoutInMilliseconds
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getExecutionTimeoutInMilliseconds
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getExecutionTimeoutEnabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withExecutionTimeoutEnabled
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|config
operator|.
name|getExecutionTimeoutEnabled
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getFallbackIsolationSemaphoreMaxConcurrentRequests
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withFallbackIsolationSemaphoreMaxConcurrentRequests
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getFallbackIsolationSemaphoreMaxConcurrentRequests
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getFallbackEnabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withFallbackEnabled
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|config
operator|.
name|getFallbackEnabled
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getMetricsHealthSnapshotIntervalInMilliseconds
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withMetricsHealthSnapshotIntervalInMilliseconds
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getMetricsHealthSnapshotIntervalInMilliseconds
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getMetricsRollingPercentileBucketSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withMetricsRollingPercentileBucketSize
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getMetricsRollingPercentileBucketSize
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getMetricsRollingPercentileEnabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withMetricsRollingPercentileEnabled
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|config
operator|.
name|getMetricsRollingPercentileEnabled
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getMetricsRollingPercentileWindowInMilliseconds
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withMetricsRollingPercentileWindowInMilliseconds
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getMetricsRollingPercentileWindowInMilliseconds
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getMetricsRollingPercentileWindowBuckets
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withMetricsRollingPercentileWindowBuckets
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getMetricsRollingPercentileWindowBuckets
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getMetricsRollingStatisticalWindowInMilliseconds
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withMetricsRollingStatisticalWindowInMilliseconds
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getMetricsRollingStatisticalWindowInMilliseconds
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getMetricsRollingStatisticalWindowBuckets
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withMetricsRollingStatisticalWindowBuckets
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getMetricsRollingStatisticalWindowBuckets
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getRequestLogEnabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|command
operator|.
name|withRequestLogEnabled
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|config
operator|.
name|getRequestLogEnabled
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getCorePoolSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|threadPool
operator|.
name|withCoreSize
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getCorePoolSize
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getMaximumSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|threadPool
operator|.
name|withMaximumSize
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getMaximumSize
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getKeepAliveTime
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|threadPool
operator|.
name|withKeepAliveTimeMinutes
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getKeepAliveTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getMaxQueueSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|threadPool
operator|.
name|withMaxQueueSize
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getMaxQueueSize
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getQueueSizeRejectionThreshold
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|threadPool
operator|.
name|withQueueSizeRejectionThreshold
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getQueueSizeRejectionThreshold
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getThreadPoolRollingNumberStatisticalWindowInMilliseconds
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|threadPool
operator|.
name|withMetricsRollingStatisticalWindowInMilliseconds
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getThreadPoolRollingNumberStatisticalWindowInMilliseconds
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getThreadPoolRollingNumberStatisticalWindowBuckets
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|threadPool
operator|.
name|withMetricsRollingStatisticalWindowBuckets
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|config
operator|.
name|getThreadPoolRollingNumberStatisticalWindowBuckets
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getAllowMaximumSizeToDivergeFromCoreSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|threadPool
operator|.
name|withAllowMaximumSizeToDivergeFromCoreSize
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|config
operator|.
name|getAllowMaximumSizeToDivergeFromCoreSize
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// *******************************
comment|// Helpers
comment|// *******************************
DECL|method|buildHystrixConfiguration (CamelContext camelContext)
name|HystrixConfigurationDefinition
name|buildHystrixConfiguration
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
name|getHystrixConfiguration
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
name|HystrixConstants
operator|.
name|DEFAULT_HYSTRIX_CONFIGURATION_ID
argument_list|,
name|HystrixConfigurationDefinition
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
name|getHystrixConfiguration
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
name|HystrixConfigurationDefinition
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
name|getHystrixConfiguration
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
name|HystrixConfigurationDefinition
name|config
init|=
operator|new
name|HystrixConfigurationDefinition
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

