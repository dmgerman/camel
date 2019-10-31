begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

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
name|concurrent
operator|.
name|ScheduledExecutorService
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
name|annotation
operator|.
name|Generated
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
name|ExchangePattern
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
name|LoggingLevel
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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
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
name|ExceptionHandler
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
name|PollingConsumerPollStrategy
import|;
end_import

begin_comment
comment|/**  * The scheduler component is used for generating message exchanges when a  * scheduler fires.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|SchedulerEndpointBuilderFactory
specifier|public
interface|interface
name|SchedulerEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Scheduler component.      */
DECL|interface|SchedulerEndpointBuilder
specifier|public
interface|interface
name|SchedulerEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedSchedulerEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedSchedulerEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|SchedulerEndpointBuilder
name|bridgeErrorHandler
parameter_list|(
name|boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"bridgeErrorHandler"
argument_list|,
name|bridgeErrorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( String bridgeErrorHandler)
specifier|default
name|SchedulerEndpointBuilder
name|bridgeErrorHandler
parameter_list|(
name|String
name|bridgeErrorHandler
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"bridgeErrorHandler"
argument_list|,
name|bridgeErrorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If the polling consumer did not poll any files, you can enable this          * option to send an empty message (no body) instead.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|sendEmptyMessageWhenIdle ( boolean sendEmptyMessageWhenIdle)
specifier|default
name|SchedulerEndpointBuilder
name|sendEmptyMessageWhenIdle
parameter_list|(
name|boolean
name|sendEmptyMessageWhenIdle
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"sendEmptyMessageWhenIdle"
argument_list|,
name|sendEmptyMessageWhenIdle
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If the polling consumer did not poll any files, you can enable this          * option to send an empty message (no body) instead.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|sendEmptyMessageWhenIdle ( String sendEmptyMessageWhenIdle)
specifier|default
name|SchedulerEndpointBuilder
name|sendEmptyMessageWhenIdle
parameter_list|(
name|String
name|sendEmptyMessageWhenIdle
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"sendEmptyMessageWhenIdle"
argument_list|,
name|sendEmptyMessageWhenIdle
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The number of subsequent error polls (failed due some error) that          * should happen before the backoffMultipler should kick-in.          *           * The option is a:<code>int</code> type.          *           * Group: scheduler          */
DECL|method|backoffErrorThreshold ( int backoffErrorThreshold)
specifier|default
name|SchedulerEndpointBuilder
name|backoffErrorThreshold
parameter_list|(
name|int
name|backoffErrorThreshold
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"backoffErrorThreshold"
argument_list|,
name|backoffErrorThreshold
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The number of subsequent error polls (failed due some error) that          * should happen before the backoffMultipler should kick-in.          *           * The option will be converted to a<code>int</code> type.          *           * Group: scheduler          */
DECL|method|backoffErrorThreshold ( String backoffErrorThreshold)
specifier|default
name|SchedulerEndpointBuilder
name|backoffErrorThreshold
parameter_list|(
name|String
name|backoffErrorThreshold
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"backoffErrorThreshold"
argument_list|,
name|backoffErrorThreshold
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The number of subsequent idle polls that should happen before the          * backoffMultipler should kick-in.          *           * The option is a:<code>int</code> type.          *           * Group: scheduler          */
DECL|method|backoffIdleThreshold ( int backoffIdleThreshold)
specifier|default
name|SchedulerEndpointBuilder
name|backoffIdleThreshold
parameter_list|(
name|int
name|backoffIdleThreshold
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"backoffIdleThreshold"
argument_list|,
name|backoffIdleThreshold
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The number of subsequent idle polls that should happen before the          * backoffMultipler should kick-in.          *           * The option will be converted to a<code>int</code> type.          *           * Group: scheduler          */
DECL|method|backoffIdleThreshold ( String backoffIdleThreshold)
specifier|default
name|SchedulerEndpointBuilder
name|backoffIdleThreshold
parameter_list|(
name|String
name|backoffIdleThreshold
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"backoffIdleThreshold"
argument_list|,
name|backoffIdleThreshold
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To let the scheduled polling consumer backoff if there has been a          * number of subsequent idles/errors in a row. The multiplier is then          * the number of polls that will be skipped before the next actual          * attempt is happening again. When this option is in use then          * backoffIdleThreshold and/or backoffErrorThreshold must also be          * configured.          *           * The option is a:<code>int</code> type.          *           * Group: scheduler          */
DECL|method|backoffMultiplier (int backoffMultiplier)
specifier|default
name|SchedulerEndpointBuilder
name|backoffMultiplier
parameter_list|(
name|int
name|backoffMultiplier
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"backoffMultiplier"
argument_list|,
name|backoffMultiplier
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To let the scheduled polling consumer backoff if there has been a          * number of subsequent idles/errors in a row. The multiplier is then          * the number of polls that will be skipped before the next actual          * attempt is happening again. When this option is in use then          * backoffIdleThreshold and/or backoffErrorThreshold must also be          * configured.          *           * The option will be converted to a<code>int</code> type.          *           * Group: scheduler          */
DECL|method|backoffMultiplier ( String backoffMultiplier)
specifier|default
name|SchedulerEndpointBuilder
name|backoffMultiplier
parameter_list|(
name|String
name|backoffMultiplier
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"backoffMultiplier"
argument_list|,
name|backoffMultiplier
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Number of threads used by the scheduling thread pool. Is by default          * using a single thread.          *           * The option is a:<code>int</code> type.          *           * Group: scheduler          */
DECL|method|concurrentTasks (int concurrentTasks)
specifier|default
name|SchedulerEndpointBuilder
name|concurrentTasks
parameter_list|(
name|int
name|concurrentTasks
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"concurrentTasks"
argument_list|,
name|concurrentTasks
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Number of threads used by the scheduling thread pool. Is by default          * using a single thread.          *           * The option will be converted to a<code>int</code> type.          *           * Group: scheduler          */
DECL|method|concurrentTasks (String concurrentTasks)
specifier|default
name|SchedulerEndpointBuilder
name|concurrentTasks
parameter_list|(
name|String
name|concurrentTasks
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"concurrentTasks"
argument_list|,
name|concurrentTasks
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Milliseconds before the next poll. You can also specify time values          * using units, such as 60s (60 seconds), 5m30s (5 minutes and 30          * seconds), and 1h (1 hour).          *           * The option is a:<code>long</code> type.          *           * Group: scheduler          */
DECL|method|delay (long delay)
specifier|default
name|SchedulerEndpointBuilder
name|delay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"delay"
argument_list|,
name|delay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Milliseconds before the next poll. You can also specify time values          * using units, such as 60s (60 seconds), 5m30s (5 minutes and 30          * seconds), and 1h (1 hour).          *           * The option will be converted to a<code>long</code> type.          *           * Group: scheduler          */
DECL|method|delay (String delay)
specifier|default
name|SchedulerEndpointBuilder
name|delay
parameter_list|(
name|String
name|delay
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"delay"
argument_list|,
name|delay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If greedy is enabled, then the ScheduledPollConsumer will run          * immediately again, if the previous run polled 1 or more messages.          *           * The option is a:<code>boolean</code> type.          *           * Group: scheduler          */
DECL|method|greedy (boolean greedy)
specifier|default
name|SchedulerEndpointBuilder
name|greedy
parameter_list|(
name|boolean
name|greedy
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"greedy"
argument_list|,
name|greedy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If greedy is enabled, then the ScheduledPollConsumer will run          * immediately again, if the previous run polled 1 or more messages.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: scheduler          */
DECL|method|greedy (String greedy)
specifier|default
name|SchedulerEndpointBuilder
name|greedy
parameter_list|(
name|String
name|greedy
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"greedy"
argument_list|,
name|greedy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Milliseconds before the first poll starts. You can also specify time          * values using units, such as 60s (60 seconds), 5m30s (5 minutes and 30          * seconds), and 1h (1 hour).          *           * The option is a:<code>long</code> type.          *           * Group: scheduler          */
DECL|method|initialDelay (long initialDelay)
specifier|default
name|SchedulerEndpointBuilder
name|initialDelay
parameter_list|(
name|long
name|initialDelay
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"initialDelay"
argument_list|,
name|initialDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Milliseconds before the first poll starts. You can also specify time          * values using units, such as 60s (60 seconds), 5m30s (5 minutes and 30          * seconds), and 1h (1 hour).          *           * The option will be converted to a<code>long</code> type.          *           * Group: scheduler          */
DECL|method|initialDelay (String initialDelay)
specifier|default
name|SchedulerEndpointBuilder
name|initialDelay
parameter_list|(
name|String
name|initialDelay
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"initialDelay"
argument_list|,
name|initialDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies a maximum limit of number of fires. So if you set it to 1,          * the scheduler will only fire once. If you set it to 5, it will only          * fire five times. A value of zero or negative means fire forever.          *           * The option is a:<code>long</code> type.          *           * Group: scheduler          */
DECL|method|repeatCount (long repeatCount)
specifier|default
name|SchedulerEndpointBuilder
name|repeatCount
parameter_list|(
name|long
name|repeatCount
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"repeatCount"
argument_list|,
name|repeatCount
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies a maximum limit of number of fires. So if you set it to 1,          * the scheduler will only fire once. If you set it to 5, it will only          * fire five times. A value of zero or negative means fire forever.          *           * The option will be converted to a<code>long</code> type.          *           * Group: scheduler          */
DECL|method|repeatCount (String repeatCount)
specifier|default
name|SchedulerEndpointBuilder
name|repeatCount
parameter_list|(
name|String
name|repeatCount
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"repeatCount"
argument_list|,
name|repeatCount
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The consumer logs a start/complete log line when it polls. This          * option allows you to configure the logging level for that.          *           * The option is a:<code>org.apache.camel.LoggingLevel</code> type.          *           * Group: scheduler          */
DECL|method|runLoggingLevel ( LoggingLevel runLoggingLevel)
specifier|default
name|SchedulerEndpointBuilder
name|runLoggingLevel
parameter_list|(
name|LoggingLevel
name|runLoggingLevel
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"runLoggingLevel"
argument_list|,
name|runLoggingLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The consumer logs a start/complete log line when it polls. This          * option allows you to configure the logging level for that.          *           * The option will be converted to a          *<code>org.apache.camel.LoggingLevel</code> type.          *           * Group: scheduler          */
DECL|method|runLoggingLevel (String runLoggingLevel)
specifier|default
name|SchedulerEndpointBuilder
name|runLoggingLevel
parameter_list|(
name|String
name|runLoggingLevel
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"runLoggingLevel"
argument_list|,
name|runLoggingLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for configuring a custom/shared thread pool to use for the          * consumer. By default each consumer has its own single threaded thread          * pool.          *           * The option is a:          *<code>java.util.concurrent.ScheduledExecutorService</code> type.          *           * Group: scheduler          */
DECL|method|scheduledExecutorService ( ScheduledExecutorService scheduledExecutorService)
specifier|default
name|SchedulerEndpointBuilder
name|scheduledExecutorService
parameter_list|(
name|ScheduledExecutorService
name|scheduledExecutorService
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"scheduledExecutorService"
argument_list|,
name|scheduledExecutorService
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for configuring a custom/shared thread pool to use for the          * consumer. By default each consumer has its own single threaded thread          * pool.          *           * The option will be converted to a          *<code>java.util.concurrent.ScheduledExecutorService</code> type.          *           * Group: scheduler          */
DECL|method|scheduledExecutorService ( String scheduledExecutorService)
specifier|default
name|SchedulerEndpointBuilder
name|scheduledExecutorService
parameter_list|(
name|String
name|scheduledExecutorService
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"scheduledExecutorService"
argument_list|,
name|scheduledExecutorService
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a cron scheduler from either camel-spring or camel-quartz          * component.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: scheduler          */
DECL|method|scheduler (String scheduler)
specifier|default
name|SchedulerEndpointBuilder
name|scheduler
parameter_list|(
name|String
name|scheduler
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"scheduler"
argument_list|,
name|scheduler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To configure additional properties when using a custom scheduler or          * any of the Quartz, Spring based scheduler.          *           * The option is a:<code>java.util.Map&lt;java.lang.String,          * java.lang.Object&gt;</code> type.          *           * Group: scheduler          */
DECL|method|schedulerProperties ( Map<String, Object> schedulerProperties)
specifier|default
name|SchedulerEndpointBuilder
name|schedulerProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|schedulerProperties
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"schedulerProperties"
argument_list|,
name|schedulerProperties
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To configure additional properties when using a custom scheduler or          * any of the Quartz, Spring based scheduler.          *           * The option will be converted to a          *<code>java.util.Map&lt;java.lang.String, java.lang.Object&gt;</code>          * type.          *           * Group: scheduler          */
DECL|method|schedulerProperties ( String schedulerProperties)
specifier|default
name|SchedulerEndpointBuilder
name|schedulerProperties
parameter_list|(
name|String
name|schedulerProperties
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"schedulerProperties"
argument_list|,
name|schedulerProperties
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the scheduler should be auto started.          *           * The option is a:<code>boolean</code> type.          *           * Group: scheduler          */
DECL|method|startScheduler (boolean startScheduler)
specifier|default
name|SchedulerEndpointBuilder
name|startScheduler
parameter_list|(
name|boolean
name|startScheduler
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"startScheduler"
argument_list|,
name|startScheduler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the scheduler should be auto started.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: scheduler          */
DECL|method|startScheduler (String startScheduler)
specifier|default
name|SchedulerEndpointBuilder
name|startScheduler
parameter_list|(
name|String
name|startScheduler
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"startScheduler"
argument_list|,
name|startScheduler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Time unit for initialDelay and delay options.          *           * The option is a:<code>java.util.concurrent.TimeUnit</code> type.          *           * Group: scheduler          */
DECL|method|timeUnit (TimeUnit timeUnit)
specifier|default
name|SchedulerEndpointBuilder
name|timeUnit
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"timeUnit"
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Time unit for initialDelay and delay options.          *           * The option will be converted to a          *<code>java.util.concurrent.TimeUnit</code> type.          *           * Group: scheduler          */
DECL|method|timeUnit (String timeUnit)
specifier|default
name|SchedulerEndpointBuilder
name|timeUnit
parameter_list|(
name|String
name|timeUnit
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"timeUnit"
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Controls if fixed delay or fixed rate is used. See          * ScheduledExecutorService in JDK for details.          *           * The option is a:<code>boolean</code> type.          *           * Group: scheduler          */
DECL|method|useFixedDelay (boolean useFixedDelay)
specifier|default
name|SchedulerEndpointBuilder
name|useFixedDelay
parameter_list|(
name|boolean
name|useFixedDelay
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"useFixedDelay"
argument_list|,
name|useFixedDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Controls if fixed delay or fixed rate is used. See          * ScheduledExecutorService in JDK for details.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: scheduler          */
DECL|method|useFixedDelay (String useFixedDelay)
specifier|default
name|SchedulerEndpointBuilder
name|useFixedDelay
parameter_list|(
name|String
name|useFixedDelay
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"useFixedDelay"
argument_list|,
name|useFixedDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Scheduler component.      */
DECL|interface|AdvancedSchedulerEndpointBuilder
specifier|public
interface|interface
name|AdvancedSchedulerEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|SchedulerEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|SchedulerEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option is a:<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedSchedulerEndpointBuilder
name|exceptionHandler
parameter_list|(
name|ExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"exceptionHandler"
argument_list|,
name|exceptionHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option will be converted to a          *<code>org.apache.camel.spi.ExceptionHandler</code> type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( String exceptionHandler)
specifier|default
name|AdvancedSchedulerEndpointBuilder
name|exceptionHandler
parameter_list|(
name|String
name|exceptionHandler
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"exceptionHandler"
argument_list|,
name|exceptionHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the exchange pattern when the consumer creates an exchange.          *           * The option is a:<code>org.apache.camel.ExchangePattern</code> type.          *           * Group: consumer (advanced)          */
DECL|method|exchangePattern ( ExchangePattern exchangePattern)
specifier|default
name|AdvancedSchedulerEndpointBuilder
name|exchangePattern
parameter_list|(
name|ExchangePattern
name|exchangePattern
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"exchangePattern"
argument_list|,
name|exchangePattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the exchange pattern when the consumer creates an exchange.          *           * The option will be converted to a          *<code>org.apache.camel.ExchangePattern</code> type.          *           * Group: consumer (advanced)          */
DECL|method|exchangePattern ( String exchangePattern)
specifier|default
name|AdvancedSchedulerEndpointBuilder
name|exchangePattern
parameter_list|(
name|String
name|exchangePattern
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"exchangePattern"
argument_list|,
name|exchangePattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A pluggable org.apache.camel.PollingConsumerPollingStrategy allowing          * you to provide your custom implementation to control error handling          * usually occurred during the poll operation before an Exchange have          * been created and being routed in Camel.          *           * The option is a:          *<code>org.apache.camel.spi.PollingConsumerPollStrategy</code> type.          *           * Group: consumer (advanced)          */
DECL|method|pollStrategy ( PollingConsumerPollStrategy pollStrategy)
specifier|default
name|AdvancedSchedulerEndpointBuilder
name|pollStrategy
parameter_list|(
name|PollingConsumerPollStrategy
name|pollStrategy
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"pollStrategy"
argument_list|,
name|pollStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A pluggable org.apache.camel.PollingConsumerPollingStrategy allowing          * you to provide your custom implementation to control error handling          * usually occurred during the poll operation before an Exchange have          * been created and being routed in Camel.          *           * The option will be converted to a          *<code>org.apache.camel.spi.PollingConsumerPollStrategy</code> type.          *           * Group: consumer (advanced)          */
DECL|method|pollStrategy ( String pollStrategy)
specifier|default
name|AdvancedSchedulerEndpointBuilder
name|pollStrategy
parameter_list|(
name|String
name|pollStrategy
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"pollStrategy"
argument_list|,
name|pollStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedSchedulerEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedSchedulerEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedSchedulerEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedSchedulerEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Scheduler (camel-scheduler)      * The scheduler component is used for generating message exchanges when a      * scheduler fires.      *       * Category: core,scheduling      * Available as of version: 2.15      * Maven coordinates: org.apache.camel:camel-scheduler      *       * Syntax:<code>scheduler:name</code>      *       * Path parameter: name (required)      * The name of the scheduler      */
DECL|method|scheduler (String path)
specifier|default
name|SchedulerEndpointBuilder
name|scheduler
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|SchedulerEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|SchedulerEndpointBuilder
implements|,
name|AdvancedSchedulerEndpointBuilder
block|{
specifier|public
name|SchedulerEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"scheduler"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|SchedulerEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

