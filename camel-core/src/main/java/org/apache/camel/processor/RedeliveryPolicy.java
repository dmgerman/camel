begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
name|Exchange
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
name|Predicate
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * The policy used to decide how many times to redeliver and the time between  * the redeliveries before being sent to a<a  * href="http://camel.apache.org/dead-letter-channel.html">Dead Letter  * Channel</a>  *<p>  * The default values are:  *<ul>  *<li>maximumRedeliveries = 0</li>  *<li>redeliveryDelay = 1000L (the initial delay)</li>  *<li>maximumRedeliveryDelay = 60 * 1000L</li>  *<li>asyncDelayedRedelivery = false</li>  *<li>backOffMultiplier = 2</li>  *<li>useExponentialBackOff = false</li>  *<li>collisionAvoidanceFactor = 0.15d</li>  *<li>useCollisionAvoidance = false</li>  *<li>retriesExhaustedLogLevel = LoggingLevel.ERROR</li>  *<li>retryAttemptedLogLevel = LoggingLevel.DEBUG</li>  *<li>logRetryAttempted = true</li>  *<li>logRetryStackTrace = false</li>  *<li>logStackTrace = true</li>  *<li>logHandled = false</li>  *<li>logExhausted = true</li>  *</ul>  *<p/>  * Setting the maximumRedeliveries to a negative value such as -1 will then always redeliver (unlimited).  * Setting the maximumRedeliveries to 0 will disable redelivery.  *<p/>  * This policy can be configured either by one of the following two settings:  *<ul>  *<li>using conventional options, using all the options defined above</li>  *<li>using delay pattern to declare intervals for delays</li>  *</ul>  *<p/>  *<b>Note:</b> If using delay patterns then the following options is not used (delay, backOffMultiplier, useExponentialBackOff, useCollisionAvoidance)  *<p/>  *<b>Using delay pattern</b>:  *<br/>The delay pattern syntax is:<tt>limit:delay;limit 2:delay 2;limit 3:delay 3;...;limit N:delay N</tt>.  *<p/>  * How it works is best illustrate with an example with this pattern:<tt>delayPattern=5:1000;10:5000:20:20000</tt>  *<br/>The delays will be for attempt in range 0..4 = 0 millis, 5..9 = 1000 millis, 10..19 = 5000 millis,>= 20 = 20000 millis.  *<p/>  * If you want to set a starting delay, then use 0 as the first limit, eg:<tt>0:1000;5:5000</tt> will use 1 sec delay  * until attempt number 5 where it will use 5 seconds going forward.  *  * @version   */
end_comment

begin_class
DECL|class|RedeliveryPolicy
specifier|public
class|class
name|RedeliveryPolicy
implements|implements
name|Cloneable
implements|,
name|Serializable
block|{
DECL|field|randomNumberGenerator
specifier|protected
specifier|static
specifier|transient
name|Random
name|randomNumberGenerator
decl_stmt|;
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|338222777701473252L
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RedeliveryPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|redeliveryDelay
specifier|protected
name|long
name|redeliveryDelay
init|=
literal|1000L
decl_stmt|;
DECL|field|maximumRedeliveries
specifier|protected
name|int
name|maximumRedeliveries
decl_stmt|;
DECL|field|maximumRedeliveryDelay
specifier|protected
name|long
name|maximumRedeliveryDelay
init|=
literal|60
operator|*
literal|1000L
decl_stmt|;
DECL|field|backOffMultiplier
specifier|protected
name|double
name|backOffMultiplier
init|=
literal|2
decl_stmt|;
DECL|field|useExponentialBackOff
specifier|protected
name|boolean
name|useExponentialBackOff
decl_stmt|;
comment|// +/-15% for a 30% spread -cgs
DECL|field|collisionAvoidanceFactor
specifier|protected
name|double
name|collisionAvoidanceFactor
init|=
literal|0.15d
decl_stmt|;
DECL|field|useCollisionAvoidance
specifier|protected
name|boolean
name|useCollisionAvoidance
decl_stmt|;
DECL|field|retriesExhaustedLogLevel
specifier|protected
name|LoggingLevel
name|retriesExhaustedLogLevel
init|=
name|LoggingLevel
operator|.
name|ERROR
decl_stmt|;
DECL|field|retryAttemptedLogLevel
specifier|protected
name|LoggingLevel
name|retryAttemptedLogLevel
init|=
name|LoggingLevel
operator|.
name|DEBUG
decl_stmt|;
DECL|field|logStackTrace
specifier|protected
name|boolean
name|logStackTrace
init|=
literal|true
decl_stmt|;
DECL|field|logRetryStackTrace
specifier|protected
name|boolean
name|logRetryStackTrace
decl_stmt|;
DECL|field|logHandled
specifier|protected
name|boolean
name|logHandled
decl_stmt|;
DECL|field|logContinued
specifier|protected
name|boolean
name|logContinued
decl_stmt|;
DECL|field|logExhausted
specifier|protected
name|boolean
name|logExhausted
init|=
literal|true
decl_stmt|;
DECL|field|logRetryAttempted
specifier|protected
name|boolean
name|logRetryAttempted
init|=
literal|true
decl_stmt|;
DECL|field|delayPattern
specifier|protected
name|String
name|delayPattern
decl_stmt|;
DECL|field|asyncDelayedRedelivery
specifier|protected
name|boolean
name|asyncDelayedRedelivery
decl_stmt|;
DECL|method|RedeliveryPolicy ()
specifier|public
name|RedeliveryPolicy
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"RedeliveryPolicy[maximumRedeliveries="
operator|+
name|maximumRedeliveries
operator|+
literal|", redeliveryDelay="
operator|+
name|redeliveryDelay
operator|+
literal|", maximumRedeliveryDelay="
operator|+
name|maximumRedeliveryDelay
operator|+
literal|", asyncDelayedRedelivery="
operator|+
name|asyncDelayedRedelivery
operator|+
literal|", retriesExhaustedLogLevel="
operator|+
name|retriesExhaustedLogLevel
operator|+
literal|", retryAttemptedLogLevel="
operator|+
name|retryAttemptedLogLevel
operator|+
literal|", logRetryAttempted="
operator|+
name|logRetryAttempted
operator|+
literal|", logStackTrace="
operator|+
name|logStackTrace
operator|+
literal|", logRetryStackTrace="
operator|+
name|logRetryStackTrace
operator|+
literal|", logHandled="
operator|+
name|logHandled
operator|+
literal|", logContinued="
operator|+
name|logContinued
operator|+
literal|", logExhausted="
operator|+
name|logExhausted
operator|+
literal|", useExponentialBackOff="
operator|+
name|useExponentialBackOff
operator|+
literal|", backOffMultiplier="
operator|+
name|backOffMultiplier
operator|+
literal|", useCollisionAvoidance="
operator|+
name|useCollisionAvoidance
operator|+
literal|", collisionAvoidanceFactor="
operator|+
name|collisionAvoidanceFactor
operator|+
literal|", delayPattern="
operator|+
name|delayPattern
operator|+
literal|"]"
return|;
block|}
DECL|method|copy ()
specifier|public
name|RedeliveryPolicy
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|RedeliveryPolicy
operator|)
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not clone: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns true if the policy decides that the message exchange should be      * redelivered.      *      * @param exchange  the current exchange      * @param redeliveryCounter  the current retry counter      * @param retryWhile  an optional predicate to determine if we should redeliver or not      * @return true to redeliver, false to stop      */
DECL|method|shouldRedeliver (Exchange exchange, int redeliveryCounter, Predicate retryWhile)
specifier|public
name|boolean
name|shouldRedeliver
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|int
name|redeliveryCounter
parameter_list|,
name|Predicate
name|retryWhile
parameter_list|)
block|{
comment|// predicate is always used if provided
if|if
condition|(
name|retryWhile
operator|!=
literal|null
condition|)
block|{
return|return
name|retryWhile
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
return|;
block|}
if|if
condition|(
name|getMaximumRedeliveries
argument_list|()
operator|<
literal|0
condition|)
block|{
comment|// retry forever if negative value
return|return
literal|true
return|;
block|}
comment|// redeliver until we hit the max
return|return
name|redeliveryCounter
operator|<=
name|getMaximumRedeliveries
argument_list|()
return|;
block|}
comment|/**      * Calculates the new redelivery delay based on the last one and then<b>sleeps</b> for the necessary amount of time.      *<p/>      * This implementation will block while sleeping.      *      * @param redeliveryDelay  previous redelivery delay      * @param redeliveryCounter  number of previous redelivery attempts      * @return the calculate delay      * @throws InterruptedException is thrown if the sleep is interrupted likely because of shutdown      */
DECL|method|sleep (long redeliveryDelay, int redeliveryCounter)
specifier|public
name|long
name|sleep
parameter_list|(
name|long
name|redeliveryDelay
parameter_list|,
name|int
name|redeliveryCounter
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|redeliveryDelay
operator|=
name|calculateRedeliveryDelay
argument_list|(
name|redeliveryDelay
argument_list|,
name|redeliveryCounter
argument_list|)
expr_stmt|;
if|if
condition|(
name|redeliveryDelay
operator|>
literal|0
condition|)
block|{
name|sleep
argument_list|(
name|redeliveryDelay
argument_list|)
expr_stmt|;
block|}
return|return
name|redeliveryDelay
return|;
block|}
comment|/**      * Sleeps for the given delay      *      * @param redeliveryDelay  the delay      * @throws InterruptedException is thrown if the sleep is interrupted likely because of shutdown      */
DECL|method|sleep (long redeliveryDelay)
specifier|public
name|void
name|sleep
parameter_list|(
name|long
name|redeliveryDelay
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sleeping for: {} millis until attempting redelivery"
argument_list|,
name|redeliveryDelay
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|redeliveryDelay
argument_list|)
expr_stmt|;
block|}
comment|/**      * Calculates the new redelivery delay based on the last one      *      * @param previousDelay  previous redelivery delay      * @param redeliveryCounter  number of previous redelivery attempts      * @return the calculate delay      */
DECL|method|calculateRedeliveryDelay (long previousDelay, int redeliveryCounter)
specifier|public
name|long
name|calculateRedeliveryDelay
parameter_list|(
name|long
name|previousDelay
parameter_list|,
name|int
name|redeliveryCounter
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|delayPattern
argument_list|)
condition|)
block|{
comment|// calculate delay using the pattern
return|return
name|calculateRedeliverDelayUsingPattern
argument_list|(
name|delayPattern
argument_list|,
name|redeliveryCounter
argument_list|)
return|;
block|}
comment|// calculate the delay using the conventional parameters
name|long
name|redeliveryDelayResult
decl_stmt|;
if|if
condition|(
name|previousDelay
operator|==
literal|0
condition|)
block|{
name|redeliveryDelayResult
operator|=
name|redeliveryDelay
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|useExponentialBackOff
operator|&&
name|backOffMultiplier
operator|>
literal|1
condition|)
block|{
name|redeliveryDelayResult
operator|=
name|Math
operator|.
name|round
argument_list|(
name|backOffMultiplier
operator|*
name|previousDelay
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|redeliveryDelayResult
operator|=
name|previousDelay
expr_stmt|;
block|}
if|if
condition|(
name|useCollisionAvoidance
condition|)
block|{
comment|/*              * First random determines +/-, second random determines how far to              * go in that direction. -cgs              */
name|Random
name|random
init|=
name|getRandomNumberGenerator
argument_list|()
decl_stmt|;
name|double
name|variance
init|=
operator|(
name|random
operator|.
name|nextBoolean
argument_list|()
condition|?
name|collisionAvoidanceFactor
else|:
operator|-
name|collisionAvoidanceFactor
operator|)
operator|*
name|random
operator|.
name|nextDouble
argument_list|()
decl_stmt|;
name|redeliveryDelayResult
operator|+=
name|redeliveryDelayResult
operator|*
name|variance
expr_stmt|;
block|}
comment|// ensure the calculated result is not bigger than the max delay (if configured)
if|if
condition|(
name|maximumRedeliveryDelay
operator|>
literal|0
operator|&&
name|redeliveryDelayResult
operator|>
name|maximumRedeliveryDelay
condition|)
block|{
name|redeliveryDelayResult
operator|=
name|maximumRedeliveryDelay
expr_stmt|;
block|}
return|return
name|redeliveryDelayResult
return|;
block|}
comment|/**      * Calculates the delay using the delay pattern      */
DECL|method|calculateRedeliverDelayUsingPattern (String delayPattern, int redeliveryCounter)
specifier|protected
specifier|static
name|long
name|calculateRedeliverDelayUsingPattern
parameter_list|(
name|String
name|delayPattern
parameter_list|,
name|int
name|redeliveryCounter
parameter_list|)
block|{
name|String
index|[]
name|groups
init|=
name|delayPattern
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
comment|// find the group where the redelivery counter matches
name|long
name|answer
init|=
literal|0
decl_stmt|;
for|for
control|(
name|String
name|group
range|:
name|groups
control|)
block|{
name|long
name|delay
init|=
name|Long
operator|.
name|valueOf
argument_list|(
name|ObjectHelper
operator|.
name|after
argument_list|(
name|group
argument_list|,
literal|":"
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|count
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|ObjectHelper
operator|.
name|before
argument_list|(
name|group
argument_list|,
literal|":"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|count
operator|>
name|redeliveryCounter
condition|)
block|{
break|break;
block|}
else|else
block|{
name|answer
operator|=
name|delay
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|// Builder methods
comment|// -------------------------------------------------------------------------
comment|/**      * Sets the initial redelivery delay in milliseconds      *      * @deprecated use redeliveryDelay instead      */
annotation|@
name|Deprecated
DECL|method|redeliverDelay (long delay)
specifier|public
name|RedeliveryPolicy
name|redeliverDelay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
return|return
name|redeliveryDelay
argument_list|(
name|delay
argument_list|)
return|;
block|}
comment|/**      * Sets the initial redelivery delay in milliseconds      */
DECL|method|redeliveryDelay (long delay)
specifier|public
name|RedeliveryPolicy
name|redeliveryDelay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|setRedeliveryDelay
argument_list|(
name|delay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the maximum number of times a message exchange will be redelivered      */
DECL|method|maximumRedeliveries (int maximumRedeliveries)
specifier|public
name|RedeliveryPolicy
name|maximumRedeliveries
parameter_list|(
name|int
name|maximumRedeliveries
parameter_list|)
block|{
name|setMaximumRedeliveries
argument_list|(
name|maximumRedeliveries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Enables collision avoidance which adds some randomization to the backoff      * timings to reduce contention probability      */
DECL|method|useCollisionAvoidance ()
specifier|public
name|RedeliveryPolicy
name|useCollisionAvoidance
parameter_list|()
block|{
name|setUseCollisionAvoidance
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Enables exponential backoff using the {@link #getBackOffMultiplier()} to      * increase the time between retries      */
DECL|method|useExponentialBackOff ()
specifier|public
name|RedeliveryPolicy
name|useExponentialBackOff
parameter_list|()
block|{
name|setUseExponentialBackOff
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Enables exponential backoff and sets the multiplier used to increase the      * delay between redeliveries      */
DECL|method|backOffMultiplier (double multiplier)
specifier|public
name|RedeliveryPolicy
name|backOffMultiplier
parameter_list|(
name|double
name|multiplier
parameter_list|)
block|{
name|useExponentialBackOff
argument_list|()
expr_stmt|;
name|setBackOffMultiplier
argument_list|(
name|multiplier
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Enables collision avoidance and sets the percentage used      */
DECL|method|collisionAvoidancePercent (double collisionAvoidancePercent)
specifier|public
name|RedeliveryPolicy
name|collisionAvoidancePercent
parameter_list|(
name|double
name|collisionAvoidancePercent
parameter_list|)
block|{
name|useCollisionAvoidance
argument_list|()
expr_stmt|;
name|setCollisionAvoidancePercent
argument_list|(
name|collisionAvoidancePercent
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the maximum redelivery delay if using exponential back off.      * Use -1 if you wish to have no maximum      */
DECL|method|maximumRedeliveryDelay (long maximumRedeliveryDelay)
specifier|public
name|RedeliveryPolicy
name|maximumRedeliveryDelay
parameter_list|(
name|long
name|maximumRedeliveryDelay
parameter_list|)
block|{
name|setMaximumRedeliveryDelay
argument_list|(
name|maximumRedeliveryDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the logging level to use for log messages when retries have been exhausted.      */
DECL|method|retriesExhaustedLogLevel (LoggingLevel retriesExhaustedLogLevel)
specifier|public
name|RedeliveryPolicy
name|retriesExhaustedLogLevel
parameter_list|(
name|LoggingLevel
name|retriesExhaustedLogLevel
parameter_list|)
block|{
name|setRetriesExhaustedLogLevel
argument_list|(
name|retriesExhaustedLogLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the logging level to use for log messages when retries are attempted.      */
DECL|method|retryAttemptedLogLevel (LoggingLevel retryAttemptedLogLevel)
specifier|public
name|RedeliveryPolicy
name|retryAttemptedLogLevel
parameter_list|(
name|LoggingLevel
name|retryAttemptedLogLevel
parameter_list|)
block|{
name|setRetryAttemptedLogLevel
argument_list|(
name|retryAttemptedLogLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets whether to log retry attempts      */
DECL|method|logRetryAttempted (boolean logRetryAttempted)
specifier|public
name|RedeliveryPolicy
name|logRetryAttempted
parameter_list|(
name|boolean
name|logRetryAttempted
parameter_list|)
block|{
name|setLogRetryAttempted
argument_list|(
name|logRetryAttempted
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets whether to log stacktrace for failed messages.      */
DECL|method|logStackTrace (boolean logStackTrace)
specifier|public
name|RedeliveryPolicy
name|logStackTrace
parameter_list|(
name|boolean
name|logStackTrace
parameter_list|)
block|{
name|setLogStackTrace
argument_list|(
name|logStackTrace
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets whether to log stacktrace for failed redelivery attempts      */
DECL|method|logRetryStackTrace (boolean logRetryStackTrace)
specifier|public
name|RedeliveryPolicy
name|logRetryStackTrace
parameter_list|(
name|boolean
name|logRetryStackTrace
parameter_list|)
block|{
name|setLogRetryStackTrace
argument_list|(
name|logRetryStackTrace
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets whether to log errors even if its handled      */
DECL|method|logHandled (boolean logHandled)
specifier|public
name|RedeliveryPolicy
name|logHandled
parameter_list|(
name|boolean
name|logHandled
parameter_list|)
block|{
name|setLogHandled
argument_list|(
name|logHandled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets whether to log exhausted errors      */
DECL|method|logExhausted (boolean logExhausted)
specifier|public
name|RedeliveryPolicy
name|logExhausted
parameter_list|(
name|boolean
name|logExhausted
parameter_list|)
block|{
name|setLogExhausted
argument_list|(
name|logExhausted
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the delay pattern with delay intervals.      */
DECL|method|delayPattern (String delayPattern)
specifier|public
name|RedeliveryPolicy
name|delayPattern
parameter_list|(
name|String
name|delayPattern
parameter_list|)
block|{
name|setDelayPattern
argument_list|(
name|delayPattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Disables redelivery by setting maximum redeliveries to 0.      */
DECL|method|disableRedelivery ()
specifier|public
name|RedeliveryPolicy
name|disableRedelivery
parameter_list|()
block|{
name|setMaximumRedeliveries
argument_list|(
literal|0
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Allow asynchronous delayed redelivery.      *      * @see #setAsyncDelayedRedelivery(boolean)      */
DECL|method|asyncDelayedRedelivery ()
specifier|public
name|RedeliveryPolicy
name|asyncDelayedRedelivery
parameter_list|()
block|{
name|setAsyncDelayedRedelivery
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
annotation|@
name|Deprecated
DECL|method|getRedeliverDelay ()
specifier|public
name|long
name|getRedeliverDelay
parameter_list|()
block|{
return|return
name|getRedeliveryDelay
argument_list|()
return|;
block|}
annotation|@
name|Deprecated
DECL|method|setRedeliverDelay (long redeliveryDelay)
specifier|public
name|void
name|setRedeliverDelay
parameter_list|(
name|long
name|redeliveryDelay
parameter_list|)
block|{
name|setRedeliveryDelay
argument_list|(
name|redeliveryDelay
argument_list|)
expr_stmt|;
block|}
DECL|method|getRedeliveryDelay ()
specifier|public
name|long
name|getRedeliveryDelay
parameter_list|()
block|{
return|return
name|redeliveryDelay
return|;
block|}
comment|/**      * Sets the initial redelivery delay in milliseconds      */
DECL|method|setRedeliveryDelay (long redeliverDelay)
specifier|public
name|void
name|setRedeliveryDelay
parameter_list|(
name|long
name|redeliverDelay
parameter_list|)
block|{
name|this
operator|.
name|redeliveryDelay
operator|=
name|redeliverDelay
expr_stmt|;
comment|// if max enabled then also set max to this value in case max was too low
if|if
condition|(
name|maximumRedeliveryDelay
operator|>
literal|0
operator|&&
name|redeliverDelay
operator|>
name|maximumRedeliveryDelay
condition|)
block|{
name|this
operator|.
name|maximumRedeliveryDelay
operator|=
name|redeliverDelay
expr_stmt|;
block|}
block|}
DECL|method|getBackOffMultiplier ()
specifier|public
name|double
name|getBackOffMultiplier
parameter_list|()
block|{
return|return
name|backOffMultiplier
return|;
block|}
comment|/**      * Sets the multiplier used to increase the delay between redeliveries if      * {@link #setUseExponentialBackOff(boolean)} is enabled      */
DECL|method|setBackOffMultiplier (double backOffMultiplier)
specifier|public
name|void
name|setBackOffMultiplier
parameter_list|(
name|double
name|backOffMultiplier
parameter_list|)
block|{
name|this
operator|.
name|backOffMultiplier
operator|=
name|backOffMultiplier
expr_stmt|;
block|}
DECL|method|getCollisionAvoidancePercent ()
specifier|public
name|long
name|getCollisionAvoidancePercent
parameter_list|()
block|{
return|return
name|Math
operator|.
name|round
argument_list|(
name|collisionAvoidanceFactor
operator|*
literal|100
argument_list|)
return|;
block|}
comment|/**      * Sets the percentage used for collision avoidance if enabled via      * {@link #setUseCollisionAvoidance(boolean)}      */
DECL|method|setCollisionAvoidancePercent (double collisionAvoidancePercent)
specifier|public
name|void
name|setCollisionAvoidancePercent
parameter_list|(
name|double
name|collisionAvoidancePercent
parameter_list|)
block|{
name|this
operator|.
name|collisionAvoidanceFactor
operator|=
name|collisionAvoidancePercent
operator|*
literal|0.01d
expr_stmt|;
block|}
DECL|method|getCollisionAvoidanceFactor ()
specifier|public
name|double
name|getCollisionAvoidanceFactor
parameter_list|()
block|{
return|return
name|collisionAvoidanceFactor
return|;
block|}
comment|/**      * Sets the factor used for collision avoidance if enabled via      * {@link #setUseCollisionAvoidance(boolean)}      */
DECL|method|setCollisionAvoidanceFactor (double collisionAvoidanceFactor)
specifier|public
name|void
name|setCollisionAvoidanceFactor
parameter_list|(
name|double
name|collisionAvoidanceFactor
parameter_list|)
block|{
name|this
operator|.
name|collisionAvoidanceFactor
operator|=
name|collisionAvoidanceFactor
expr_stmt|;
block|}
DECL|method|getMaximumRedeliveries ()
specifier|public
name|int
name|getMaximumRedeliveries
parameter_list|()
block|{
return|return
name|maximumRedeliveries
return|;
block|}
comment|/**      * Sets the maximum number of times a message exchange will be redelivered.      * Setting a negative value will retry forever.      */
DECL|method|setMaximumRedeliveries (int maximumRedeliveries)
specifier|public
name|void
name|setMaximumRedeliveries
parameter_list|(
name|int
name|maximumRedeliveries
parameter_list|)
block|{
name|this
operator|.
name|maximumRedeliveries
operator|=
name|maximumRedeliveries
expr_stmt|;
block|}
DECL|method|getMaximumRedeliveryDelay ()
specifier|public
name|long
name|getMaximumRedeliveryDelay
parameter_list|()
block|{
return|return
name|maximumRedeliveryDelay
return|;
block|}
comment|/**      * Sets the maximum redelivery delay.      * Use -1 if you wish to have no maximum      */
DECL|method|setMaximumRedeliveryDelay (long maximumRedeliveryDelay)
specifier|public
name|void
name|setMaximumRedeliveryDelay
parameter_list|(
name|long
name|maximumRedeliveryDelay
parameter_list|)
block|{
name|this
operator|.
name|maximumRedeliveryDelay
operator|=
name|maximumRedeliveryDelay
expr_stmt|;
block|}
DECL|method|isUseCollisionAvoidance ()
specifier|public
name|boolean
name|isUseCollisionAvoidance
parameter_list|()
block|{
return|return
name|useCollisionAvoidance
return|;
block|}
comment|/**      * Enables/disables collision avoidance which adds some randomization to the      * backoff timings to reduce contention probability      */
DECL|method|setUseCollisionAvoidance (boolean useCollisionAvoidance)
specifier|public
name|void
name|setUseCollisionAvoidance
parameter_list|(
name|boolean
name|useCollisionAvoidance
parameter_list|)
block|{
name|this
operator|.
name|useCollisionAvoidance
operator|=
name|useCollisionAvoidance
expr_stmt|;
block|}
DECL|method|isUseExponentialBackOff ()
specifier|public
name|boolean
name|isUseExponentialBackOff
parameter_list|()
block|{
return|return
name|useExponentialBackOff
return|;
block|}
comment|/**      * Enables/disables exponential backoff using the      * {@link #getBackOffMultiplier()} to increase the time between retries      */
DECL|method|setUseExponentialBackOff (boolean useExponentialBackOff)
specifier|public
name|void
name|setUseExponentialBackOff
parameter_list|(
name|boolean
name|useExponentialBackOff
parameter_list|)
block|{
name|this
operator|.
name|useExponentialBackOff
operator|=
name|useExponentialBackOff
expr_stmt|;
block|}
DECL|method|getRandomNumberGenerator ()
specifier|protected
specifier|static
specifier|synchronized
name|Random
name|getRandomNumberGenerator
parameter_list|()
block|{
if|if
condition|(
name|randomNumberGenerator
operator|==
literal|null
condition|)
block|{
name|randomNumberGenerator
operator|=
operator|new
name|Random
argument_list|()
expr_stmt|;
block|}
return|return
name|randomNumberGenerator
return|;
block|}
comment|/**      * Sets the logging level to use for log messages when retries have been exhausted.      */
DECL|method|setRetriesExhaustedLogLevel (LoggingLevel retriesExhaustedLogLevel)
specifier|public
name|void
name|setRetriesExhaustedLogLevel
parameter_list|(
name|LoggingLevel
name|retriesExhaustedLogLevel
parameter_list|)
block|{
name|this
operator|.
name|retriesExhaustedLogLevel
operator|=
name|retriesExhaustedLogLevel
expr_stmt|;
block|}
DECL|method|getRetriesExhaustedLogLevel ()
specifier|public
name|LoggingLevel
name|getRetriesExhaustedLogLevel
parameter_list|()
block|{
return|return
name|retriesExhaustedLogLevel
return|;
block|}
comment|/**      * Sets the logging level to use for log messages when retries are attempted.      */
DECL|method|setRetryAttemptedLogLevel (LoggingLevel retryAttemptedLogLevel)
specifier|public
name|void
name|setRetryAttemptedLogLevel
parameter_list|(
name|LoggingLevel
name|retryAttemptedLogLevel
parameter_list|)
block|{
name|this
operator|.
name|retryAttemptedLogLevel
operator|=
name|retryAttemptedLogLevel
expr_stmt|;
block|}
DECL|method|getRetryAttemptedLogLevel ()
specifier|public
name|LoggingLevel
name|getRetryAttemptedLogLevel
parameter_list|()
block|{
return|return
name|retryAttemptedLogLevel
return|;
block|}
DECL|method|getDelayPattern ()
specifier|public
name|String
name|getDelayPattern
parameter_list|()
block|{
return|return
name|delayPattern
return|;
block|}
comment|/**      * Sets an optional delay pattern to use instead of fixed delay.      */
DECL|method|setDelayPattern (String delayPattern)
specifier|public
name|void
name|setDelayPattern
parameter_list|(
name|String
name|delayPattern
parameter_list|)
block|{
name|this
operator|.
name|delayPattern
operator|=
name|delayPattern
expr_stmt|;
block|}
DECL|method|isLogStackTrace ()
specifier|public
name|boolean
name|isLogStackTrace
parameter_list|()
block|{
return|return
name|logStackTrace
return|;
block|}
comment|/**      * Sets whether stack traces should be logged or not      */
DECL|method|setLogStackTrace (boolean logStackTrace)
specifier|public
name|void
name|setLogStackTrace
parameter_list|(
name|boolean
name|logStackTrace
parameter_list|)
block|{
name|this
operator|.
name|logStackTrace
operator|=
name|logStackTrace
expr_stmt|;
block|}
DECL|method|isLogRetryStackTrace ()
specifier|public
name|boolean
name|isLogRetryStackTrace
parameter_list|()
block|{
return|return
name|logRetryStackTrace
return|;
block|}
comment|/**      * Sets whether stack traces should be logged or not      */
DECL|method|setLogRetryStackTrace (boolean logRetryStackTrace)
specifier|public
name|void
name|setLogRetryStackTrace
parameter_list|(
name|boolean
name|logRetryStackTrace
parameter_list|)
block|{
name|this
operator|.
name|logRetryStackTrace
operator|=
name|logRetryStackTrace
expr_stmt|;
block|}
DECL|method|isLogHandled ()
specifier|public
name|boolean
name|isLogHandled
parameter_list|()
block|{
return|return
name|logHandled
return|;
block|}
comment|/**      * Sets whether errors should be logged even if its handled      */
DECL|method|setLogHandled (boolean logHandled)
specifier|public
name|void
name|setLogHandled
parameter_list|(
name|boolean
name|logHandled
parameter_list|)
block|{
name|this
operator|.
name|logHandled
operator|=
name|logHandled
expr_stmt|;
block|}
DECL|method|isLogContinued ()
specifier|public
name|boolean
name|isLogContinued
parameter_list|()
block|{
return|return
name|logContinued
return|;
block|}
comment|/**      * Sets whether errors should be logged even if its continued      */
DECL|method|setLogContinued (boolean logContinued)
specifier|public
name|void
name|setLogContinued
parameter_list|(
name|boolean
name|logContinued
parameter_list|)
block|{
name|this
operator|.
name|logContinued
operator|=
name|logContinued
expr_stmt|;
block|}
DECL|method|isLogRetryAttempted ()
specifier|public
name|boolean
name|isLogRetryAttempted
parameter_list|()
block|{
return|return
name|logRetryAttempted
return|;
block|}
comment|/**      * Sets whether retry attempts should be logged or not      */
DECL|method|setLogRetryAttempted (boolean logRetryAttempted)
specifier|public
name|void
name|setLogRetryAttempted
parameter_list|(
name|boolean
name|logRetryAttempted
parameter_list|)
block|{
name|this
operator|.
name|logRetryAttempted
operator|=
name|logRetryAttempted
expr_stmt|;
block|}
DECL|method|isLogExhausted ()
specifier|public
name|boolean
name|isLogExhausted
parameter_list|()
block|{
return|return
name|logExhausted
return|;
block|}
comment|/**      * Sets whether exhausted exceptions should be logged or not      */
DECL|method|setLogExhausted (boolean logExhausted)
specifier|public
name|void
name|setLogExhausted
parameter_list|(
name|boolean
name|logExhausted
parameter_list|)
block|{
name|this
operator|.
name|logExhausted
operator|=
name|logExhausted
expr_stmt|;
block|}
DECL|method|isAsyncDelayedRedelivery ()
specifier|public
name|boolean
name|isAsyncDelayedRedelivery
parameter_list|()
block|{
return|return
name|asyncDelayedRedelivery
return|;
block|}
comment|/**      * Sets whether asynchronous delayed redelivery is allowed.      *<p/>      * This is disabled by default.      *<p/>      * When enabled it allows Camel to schedule a future task for delayed      * redelivery which prevents current thread from blocking while waiting.      *<p/>      * Exchange which is transacted will however always use synchronous delayed redelivery      * because the transaction must execute in the same thread context.      *      * @param asyncDelayedRedelivery whether asynchronous delayed redelivery is allowed      */
DECL|method|setAsyncDelayedRedelivery (boolean asyncDelayedRedelivery)
specifier|public
name|void
name|setAsyncDelayedRedelivery
parameter_list|(
name|boolean
name|asyncDelayedRedelivery
parameter_list|)
block|{
name|this
operator|.
name|asyncDelayedRedelivery
operator|=
name|asyncDelayedRedelivery
expr_stmt|;
block|}
block|}
end_class

end_unit

