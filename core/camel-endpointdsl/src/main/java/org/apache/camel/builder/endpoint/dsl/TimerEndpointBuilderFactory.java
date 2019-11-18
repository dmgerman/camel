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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Timer
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

begin_comment
comment|/**  * The timer component is used for generating message exchanges when a timer  * fires.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|TimerEndpointBuilderFactory
specifier|public
interface|interface
name|TimerEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Timer component.      */
DECL|interface|TimerEndpointBuilder
specifier|public
interface|interface
name|TimerEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedTimerEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedTimerEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|TimerEndpointBuilder
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
name|TimerEndpointBuilder
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
comment|/**          * The number of milliseconds to wait before the first event is          * generated. Should not be used in conjunction with the time option.          * The default value is 1000. You can also specify time values using          * units, such as 60s (60 seconds), 5m30s (5 minutes and 30 seconds),          * and 1h (1 hour).          *           * The option is a:<code>long</code> type.          *           * Group: consumer          */
DECL|method|delay (long delay)
specifier|default
name|TimerEndpointBuilder
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
comment|/**          * The number of milliseconds to wait before the first event is          * generated. Should not be used in conjunction with the time option.          * The default value is 1000. You can also specify time values using          * units, such as 60s (60 seconds), 5m30s (5 minutes and 30 seconds),          * and 1h (1 hour).          *           * The option will be converted to a<code>long</code> type.          *           * Group: consumer          */
DECL|method|delay (String delay)
specifier|default
name|TimerEndpointBuilder
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
comment|/**          * Events take place at approximately regular intervals, separated by          * the specified period.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|fixedRate (boolean fixedRate)
specifier|default
name|TimerEndpointBuilder
name|fixedRate
parameter_list|(
name|boolean
name|fixedRate
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"fixedRate"
argument_list|,
name|fixedRate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Events take place at approximately regular intervals, separated by          * the specified period.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|fixedRate (String fixedRate)
specifier|default
name|TimerEndpointBuilder
name|fixedRate
parameter_list|(
name|String
name|fixedRate
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"fixedRate"
argument_list|,
name|fixedRate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If greater than 0, generate periodic events every period          * milliseconds. The default value is 1000. You can also specify time          * values using units, such as 60s (60 seconds), 5m30s (5 minutes and 30          * seconds), and 1h (1 hour).          *           * The option is a:<code>long</code> type.          *           * Group: consumer          */
DECL|method|period (long period)
specifier|default
name|TimerEndpointBuilder
name|period
parameter_list|(
name|long
name|period
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"period"
argument_list|,
name|period
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If greater than 0, generate periodic events every period          * milliseconds. The default value is 1000. You can also specify time          * values using units, such as 60s (60 seconds), 5m30s (5 minutes and 30          * seconds), and 1h (1 hour).          *           * The option will be converted to a<code>long</code> type.          *           * Group: consumer          */
DECL|method|period (String period)
specifier|default
name|TimerEndpointBuilder
name|period
parameter_list|(
name|String
name|period
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"period"
argument_list|,
name|period
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies a maximum limit of number of fires. So if you set it to 1,          * the timer will only fire once. If you set it to 5, it will only fire          * five times. A value of zero or negative means fire forever.          *           * The option is a:<code>long</code> type.          *           * Group: consumer          */
DECL|method|repeatCount (long repeatCount)
specifier|default
name|TimerEndpointBuilder
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
comment|/**          * Specifies a maximum limit of number of fires. So if you set it to 1,          * the timer will only fire once. If you set it to 5, it will only fire          * five times. A value of zero or negative means fire forever.          *           * The option will be converted to a<code>long</code> type.          *           * Group: consumer          */
DECL|method|repeatCount (String repeatCount)
specifier|default
name|TimerEndpointBuilder
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
block|}
comment|/**      * Advanced builder for endpoint for the Timer component.      */
DECL|interface|AdvancedTimerEndpointBuilder
specifier|public
interface|interface
name|AdvancedTimerEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|TimerEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|TimerEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option is a:<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedTimerEndpointBuilder
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
name|AdvancedTimerEndpointBuilder
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
name|AdvancedTimerEndpointBuilder
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
name|AdvancedTimerEndpointBuilder
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedTimerEndpointBuilder
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
name|AdvancedTimerEndpointBuilder
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
comment|/**          * Specifies whether or not the thread associated with the timer          * endpoint runs as a daemon. The default value is true.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|daemon (boolean daemon)
specifier|default
name|AdvancedTimerEndpointBuilder
name|daemon
parameter_list|(
name|boolean
name|daemon
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"daemon"
argument_list|,
name|daemon
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies whether or not the thread associated with the timer          * endpoint runs as a daemon. The default value is true.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|daemon (String daemon)
specifier|default
name|AdvancedTimerEndpointBuilder
name|daemon
parameter_list|(
name|String
name|daemon
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"daemon"
argument_list|,
name|daemon
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows you to specify a custom Date pattern to use for setting the          * time option using URI syntax.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: advanced          */
DECL|method|pattern (String pattern)
specifier|default
name|AdvancedTimerEndpointBuilder
name|pattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"pattern"
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedTimerEndpointBuilder
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
name|AdvancedTimerEndpointBuilder
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
comment|/**          * A java.util.Date the first event should be generated. If using the          * URI, the pattern expected is: yyyy-MM-dd HH:mm:ss or          * yyyy-MM-dd'T'HH:mm:ss.          *           * The option is a:<code>java.util.Date</code> type.          *           * Group: advanced          */
DECL|method|time (Date time)
specifier|default
name|AdvancedTimerEndpointBuilder
name|time
parameter_list|(
name|Date
name|time
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"time"
argument_list|,
name|time
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A java.util.Date the first event should be generated. If using the          * URI, the pattern expected is: yyyy-MM-dd HH:mm:ss or          * yyyy-MM-dd'T'HH:mm:ss.          *           * The option will be converted to a<code>java.util.Date</code> type.          *           * Group: advanced          */
DECL|method|time (String time)
specifier|default
name|AdvancedTimerEndpointBuilder
name|time
parameter_list|(
name|String
name|time
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"time"
argument_list|,
name|time
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom Timer.          *           * The option is a:<code>java.util.Timer</code> type.          *           * Group: advanced          */
DECL|method|timer (Timer timer)
specifier|default
name|AdvancedTimerEndpointBuilder
name|timer
parameter_list|(
name|Timer
name|timer
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"timer"
argument_list|,
name|timer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom Timer.          *           * The option will be converted to a<code>java.util.Timer</code> type.          *           * Group: advanced          */
DECL|method|timer (String timer)
specifier|default
name|AdvancedTimerEndpointBuilder
name|timer
parameter_list|(
name|String
name|timer
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"timer"
argument_list|,
name|timer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Timer (camel-timer)      * The timer component is used for generating message exchanges when a timer      * fires.      *       * Category: core,scheduling      * Since: 1.0      * Maven coordinates: org.apache.camel:camel-timer      *       * Syntax:<code>timer:timerName</code>      *       * Path parameter: timerName (required)      * The name of the timer      */
DECL|method|timer (String path)
specifier|default
name|TimerEndpointBuilder
name|timer
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|TimerEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|TimerEndpointBuilder
implements|,
name|AdvancedTimerEndpointBuilder
block|{
specifier|public
name|TimerEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"timer"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|TimerEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

