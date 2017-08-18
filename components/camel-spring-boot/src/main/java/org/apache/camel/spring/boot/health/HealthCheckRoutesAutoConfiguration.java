begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|health
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|TimePatternConverter
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
name|health
operator|.
name|HealthCheckRepository
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
name|health
operator|.
name|RoutePerformanceCounterEvaluators
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
name|health
operator|.
name|RoutesHealthCheckRepository
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
name|spring
operator|.
name|boot
operator|.
name|CamelAutoConfiguration
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
name|spring
operator|.
name|boot
operator|.
name|util
operator|.
name|GroupCondition
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
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|config
operator|.
name|ConfigurableBeanFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|AutoConfigureAfter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnMissingBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|EnableConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Conditional
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Scope
import|;
end_import

begin_class
annotation|@
name|Configuration
annotation|@
name|AutoConfigureAfter
argument_list|(
name|CamelAutoConfiguration
operator|.
name|class
argument_list|)
annotation|@
name|Conditional
argument_list|(
name|HealthCheckRoutesAutoConfiguration
operator|.
name|Condition
operator|.
name|class
argument_list|)
annotation|@
name|EnableConfigurationProperties
argument_list|(
name|HealthCheckRoutesConfiguration
operator|.
name|class
argument_list|)
DECL|class|HealthCheckRoutesAutoConfiguration
specifier|public
class|class
name|HealthCheckRoutesAutoConfiguration
block|{
annotation|@
name|Autowired
DECL|field|configuration
specifier|private
name|HealthCheckRoutesConfiguration
name|configuration
decl_stmt|;
annotation|@
name|Bean
annotation|@
name|Scope
argument_list|(
name|ConfigurableBeanFactory
operator|.
name|SCOPE_SINGLETON
argument_list|)
annotation|@
name|ConditionalOnMissingBean
argument_list|(
name|RoutesHealthCheckRepository
operator|.
name|class
argument_list|)
DECL|method|routesHealthCheckRepository ()
specifier|public
name|HealthCheckRepository
name|routesHealthCheckRepository
parameter_list|()
block|{
specifier|final
name|RoutesHealthCheckRepository
name|repository
init|=
operator|new
name|RoutesHealthCheckRepository
argument_list|()
decl_stmt|;
specifier|final
name|HealthCheckRoutesConfiguration
operator|.
name|ThresholdsConfiguration
name|thresholds
init|=
name|configuration
operator|.
name|getThresholds
argument_list|()
decl_stmt|;
if|if
condition|(
name|thresholds
operator|.
name|getExchangesFailed
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|repository
operator|.
name|addEvaluator
argument_list|(
name|RoutePerformanceCounterEvaluators
operator|.
name|exchangesFailed
argument_list|(
name|thresholds
operator|.
name|getExchangesFailed
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|thresholds
operator|.
name|getExchangesInflight
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|repository
operator|.
name|addEvaluator
argument_list|(
name|RoutePerformanceCounterEvaluators
operator|.
name|exchangesInflight
argument_list|(
name|thresholds
operator|.
name|getExchangesInflight
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|thresholds
operator|.
name|getRedeliveries
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|repository
operator|.
name|addEvaluator
argument_list|(
name|RoutePerformanceCounterEvaluators
operator|.
name|redeliveries
argument_list|(
name|thresholds
operator|.
name|getRedeliveries
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|thresholds
operator|.
name|getExternalRedeliveries
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|repository
operator|.
name|addEvaluator
argument_list|(
name|RoutePerformanceCounterEvaluators
operator|.
name|redeliveries
argument_list|(
name|thresholds
operator|.
name|getExternalRedeliveries
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|thresholds
operator|.
name|getLastProcessingTime
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|time
init|=
name|thresholds
operator|.
name|getLastProcessingTime
argument_list|()
operator|.
name|getThreshold
argument_list|()
decl_stmt|;
specifier|final
name|Integer
name|failures
init|=
name|thresholds
operator|.
name|getLastProcessingTime
argument_list|()
operator|.
name|getFailures
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|time
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|failures
argument_list|)
condition|)
block|{
name|repository
operator|.
name|addEvaluator
argument_list|(
name|RoutePerformanceCounterEvaluators
operator|.
name|lastProcessingTime
argument_list|(
name|TimePatternConverter
operator|.
name|toMilliSeconds
argument_list|(
name|time
argument_list|)
argument_list|,
name|failures
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|thresholds
operator|.
name|getMinProcessingTime
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|time
init|=
name|thresholds
operator|.
name|getMinProcessingTime
argument_list|()
operator|.
name|getThreshold
argument_list|()
decl_stmt|;
specifier|final
name|Integer
name|failures
init|=
name|thresholds
operator|.
name|getMinProcessingTime
argument_list|()
operator|.
name|getFailures
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|time
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|failures
argument_list|)
condition|)
block|{
name|repository
operator|.
name|addEvaluator
argument_list|(
name|RoutePerformanceCounterEvaluators
operator|.
name|minProcessingTime
argument_list|(
name|TimePatternConverter
operator|.
name|toMilliSeconds
argument_list|(
name|time
argument_list|)
argument_list|,
name|failures
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|thresholds
operator|.
name|getMeanProcessingTime
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|time
init|=
name|thresholds
operator|.
name|getMeanProcessingTime
argument_list|()
operator|.
name|getThreshold
argument_list|()
decl_stmt|;
specifier|final
name|Integer
name|failures
init|=
name|thresholds
operator|.
name|getMeanProcessingTime
argument_list|()
operator|.
name|getFailures
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|time
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|failures
argument_list|)
condition|)
block|{
name|repository
operator|.
name|addEvaluator
argument_list|(
name|RoutePerformanceCounterEvaluators
operator|.
name|meanProcessingTime
argument_list|(
name|TimePatternConverter
operator|.
name|toMilliSeconds
argument_list|(
name|time
argument_list|)
argument_list|,
name|failures
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|thresholds
operator|.
name|getMaxProcessingTime
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|time
init|=
name|thresholds
operator|.
name|getMaxProcessingTime
argument_list|()
operator|.
name|getThreshold
argument_list|()
decl_stmt|;
specifier|final
name|Integer
name|failures
init|=
name|thresholds
operator|.
name|getMaxProcessingTime
argument_list|()
operator|.
name|getFailures
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|time
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|failures
argument_list|)
condition|)
block|{
name|repository
operator|.
name|addEvaluator
argument_list|(
name|RoutePerformanceCounterEvaluators
operator|.
name|maxProcessingTime
argument_list|(
name|TimePatternConverter
operator|.
name|toMilliSeconds
argument_list|(
name|time
argument_list|)
argument_list|,
name|failures
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|configuration
operator|.
name|getThreshold
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|key
range|:
name|configuration
operator|.
name|getThreshold
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
specifier|final
name|HealthCheckRoutesConfiguration
operator|.
name|ThresholdsConfiguration
name|threshold
init|=
name|configuration
operator|.
name|getThreshold
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|threshold
operator|.
name|getExchangesFailed
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|repository
operator|.
name|addRouteEvaluator
argument_list|(
name|key
argument_list|,
name|RoutePerformanceCounterEvaluators
operator|.
name|exchangesFailed
argument_list|(
name|threshold
operator|.
name|getExchangesFailed
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|threshold
operator|.
name|getExchangesInflight
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|repository
operator|.
name|addRouteEvaluator
argument_list|(
name|key
argument_list|,
name|RoutePerformanceCounterEvaluators
operator|.
name|exchangesInflight
argument_list|(
name|threshold
operator|.
name|getExchangesInflight
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|threshold
operator|.
name|getRedeliveries
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|repository
operator|.
name|addRouteEvaluator
argument_list|(
name|key
argument_list|,
name|RoutePerformanceCounterEvaluators
operator|.
name|redeliveries
argument_list|(
name|threshold
operator|.
name|getRedeliveries
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|threshold
operator|.
name|getExternalRedeliveries
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|repository
operator|.
name|addRouteEvaluator
argument_list|(
name|key
argument_list|,
name|RoutePerformanceCounterEvaluators
operator|.
name|redeliveries
argument_list|(
name|threshold
operator|.
name|getExternalRedeliveries
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|threshold
operator|.
name|getLastProcessingTime
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|time
init|=
name|threshold
operator|.
name|getLastProcessingTime
argument_list|()
operator|.
name|getThreshold
argument_list|()
decl_stmt|;
specifier|final
name|Integer
name|failures
init|=
name|threshold
operator|.
name|getLastProcessingTime
argument_list|()
operator|.
name|getFailures
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|time
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|failures
argument_list|)
condition|)
block|{
name|repository
operator|.
name|addRouteEvaluator
argument_list|(
name|key
argument_list|,
name|RoutePerformanceCounterEvaluators
operator|.
name|lastProcessingTime
argument_list|(
name|TimePatternConverter
operator|.
name|toMilliSeconds
argument_list|(
name|time
argument_list|)
argument_list|,
name|failures
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|threshold
operator|.
name|getMinProcessingTime
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|time
init|=
name|threshold
operator|.
name|getMinProcessingTime
argument_list|()
operator|.
name|getThreshold
argument_list|()
decl_stmt|;
specifier|final
name|Integer
name|failures
init|=
name|threshold
operator|.
name|getMinProcessingTime
argument_list|()
operator|.
name|getFailures
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|time
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|failures
argument_list|)
condition|)
block|{
name|repository
operator|.
name|addRouteEvaluator
argument_list|(
name|key
argument_list|,
name|RoutePerformanceCounterEvaluators
operator|.
name|minProcessingTime
argument_list|(
name|TimePatternConverter
operator|.
name|toMilliSeconds
argument_list|(
name|time
argument_list|)
argument_list|,
name|failures
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|threshold
operator|.
name|getMeanProcessingTime
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|time
init|=
name|threshold
operator|.
name|getMeanProcessingTime
argument_list|()
operator|.
name|getThreshold
argument_list|()
decl_stmt|;
specifier|final
name|Integer
name|failures
init|=
name|threshold
operator|.
name|getMeanProcessingTime
argument_list|()
operator|.
name|getFailures
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|time
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|failures
argument_list|)
condition|)
block|{
name|repository
operator|.
name|addRouteEvaluator
argument_list|(
name|key
argument_list|,
name|RoutePerformanceCounterEvaluators
operator|.
name|meanProcessingTime
argument_list|(
name|TimePatternConverter
operator|.
name|toMilliSeconds
argument_list|(
name|time
argument_list|)
argument_list|,
name|failures
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|threshold
operator|.
name|getMaxProcessingTime
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|time
init|=
name|threshold
operator|.
name|getMaxProcessingTime
argument_list|()
operator|.
name|getThreshold
argument_list|()
decl_stmt|;
specifier|final
name|Integer
name|failures
init|=
name|threshold
operator|.
name|getMaxProcessingTime
argument_list|()
operator|.
name|getFailures
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|time
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|failures
argument_list|)
condition|)
block|{
name|repository
operator|.
name|addRouteEvaluator
argument_list|(
name|key
argument_list|,
name|RoutePerformanceCounterEvaluators
operator|.
name|maxProcessingTime
argument_list|(
name|TimePatternConverter
operator|.
name|toMilliSeconds
argument_list|(
name|time
argument_list|)
argument_list|,
name|failures
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|repository
return|;
block|}
comment|// ***************************************
comment|// Condition
comment|// ***************************************
DECL|class|Condition
specifier|public
specifier|static
class|class
name|Condition
extends|extends
name|GroupCondition
block|{
DECL|method|Condition ()
specifier|public
name|Condition
parameter_list|()
block|{
name|super
argument_list|(
name|HealthConstants
operator|.
name|HEALTH_PREFIX
argument_list|,
name|HealthConstants
operator|.
name|HEALTH_CHECK_ROUTES_PREFIX
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

