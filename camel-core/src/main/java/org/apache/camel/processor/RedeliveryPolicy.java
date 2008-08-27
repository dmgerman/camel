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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|// Code taken from the ActiveMQ codebase
end_comment

begin_comment
comment|/**  * The policy used to decide how many times to redeliver and the time between  * the redeliveries before being sent to a<a  * href="http://activemq.apache.org/camel/dead-letter-channel.html">Dead Letter  * Channel</a>  *<p>  * The default values is:  *<ul>  *<li>maximumRedeliveries = 5</li>  *<li>initialRedeliveryDelay = 1000L</li>  *<li>maximumRedeliveryDelay = 60 * 1000L</li>  *<li>backOffMultiplier = 2</li>  *<li>useExponentialBackOff = false</li>  *<li>collisionAvoidanceFactor = 0.15d</li>  *<li>useCollisionAvoidance = false</li>  *</ul>  *<p/>  * Setting the maximumRedeliveries to a negative value such as -1 will then always redeliver (unlimited).  * Setting the maximumRedeliveries to 0 will disable redelivery.  *  * @version $Revision$  */
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
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|RedeliveryPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|maximumRedeliveries
specifier|protected
name|int
name|maximumRedeliveries
init|=
literal|5
decl_stmt|;
DECL|field|initialRedeliveryDelay
specifier|protected
name|long
name|initialRedeliveryDelay
init|=
literal|1000L
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
comment|/**      * Returns true if the policy decides that the message exchange should be      * redelivered      */
DECL|method|shouldRedeliver (int redeliveryCounter)
specifier|public
name|boolean
name|shouldRedeliver
parameter_list|(
name|int
name|redeliveryCounter
parameter_list|)
block|{
if|if
condition|(
name|getMaximumRedeliveries
argument_list|()
operator|<
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// redeliver until we hitted the max
return|return
name|redeliveryCounter
operator|<=
name|getMaximumRedeliveries
argument_list|()
return|;
block|}
comment|/**      * Calculates the new redelivery delay based on the last one then sleeps for the necessary amount of time      */
DECL|method|sleep (long redeliveryDelay)
specifier|public
name|long
name|sleep
parameter_list|(
name|long
name|redeliveryDelay
parameter_list|)
block|{
name|redeliveryDelay
operator|=
name|getRedeliveryDelay
argument_list|(
name|redeliveryDelay
argument_list|)
expr_stmt|;
if|if
condition|(
name|redeliveryDelay
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sleeping for: "
operator|+
name|redeliveryDelay
operator|+
literal|" millis until attempting redelivery"
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|redeliveryDelay
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Thread interrupted: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|redeliveryDelay
return|;
block|}
DECL|method|getRedeliveryDelay (long previousDelay)
specifier|public
name|long
name|getRedeliveryDelay
parameter_list|(
name|long
name|previousDelay
parameter_list|)
block|{
name|long
name|redeliveryDelay
decl_stmt|;
if|if
condition|(
name|previousDelay
operator|==
literal|0
condition|)
block|{
name|redeliveryDelay
operator|=
name|initialRedeliveryDelay
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
name|redeliveryDelay
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
name|redeliveryDelay
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
name|redeliveryDelay
operator|+=
name|redeliveryDelay
operator|*
name|variance
expr_stmt|;
block|}
if|if
condition|(
name|maximumRedeliveryDelay
operator|>
literal|0
operator|&&
name|redeliveryDelay
operator|>
name|maximumRedeliveryDelay
condition|)
block|{
name|redeliveryDelay
operator|=
name|maximumRedeliveryDelay
expr_stmt|;
block|}
return|return
name|redeliveryDelay
return|;
block|}
comment|// Builder methods
comment|// -------------------------------------------------------------------------
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
comment|/**      * Sets the initial redelivery delay in milliseconds on the first redelivery      */
DECL|method|initialRedeliveryDelay (long initialRedeliveryDelay)
specifier|public
name|RedeliveryPolicy
name|initialRedeliveryDelay
parameter_list|(
name|long
name|initialRedeliveryDelay
parameter_list|)
block|{
name|setInitialRedeliveryDelay
argument_list|(
name|initialRedeliveryDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Enables collision avoidence which adds some randomization to the backoff      * timings to reduce contention probability      */
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
comment|/**      * Enables exponential backof using the {@link #getBackOffMultiplier()} to      * increase the time between retries      */
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
comment|/**      * Enables collision avoidence and sets the percentage used      */
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
comment|// Properties
comment|// -------------------------------------------------------------------------
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
name|short
name|getCollisionAvoidancePercent
parameter_list|()
block|{
return|return
operator|(
name|short
operator|)
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
comment|/**      * Sets the percentage used for collision avoidence if enabled via      * {@link #setUseCollisionAvoidance(boolean)}      */
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
comment|/**      * Sets the factor used for collision avoidence if enabled via      * {@link #setUseCollisionAvoidance(boolean)}      */
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
DECL|method|getInitialRedeliveryDelay ()
specifier|public
name|long
name|getInitialRedeliveryDelay
parameter_list|()
block|{
return|return
name|initialRedeliveryDelay
return|;
block|}
comment|/**      * Sets the initial redelivery delay in milliseconds on the first redelivery      */
DECL|method|setInitialRedeliveryDelay (long initialRedeliveryDelay)
specifier|public
name|void
name|setInitialRedeliveryDelay
parameter_list|(
name|long
name|initialRedeliveryDelay
parameter_list|)
block|{
name|this
operator|.
name|initialRedeliveryDelay
operator|=
name|initialRedeliveryDelay
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
comment|/**      * Sets the maximum redelivery delay if using exponential back off.      * Use -1 if you wish to have no maximum      */
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
comment|/**      * Enables/disables collision avoidence which adds some randomization to the      * backoff timings to reduce contention probability      */
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
comment|/**      * Enables/disables exponential backof using the      * {@link #getBackOffMultiplier()} to increase the time between retries      */
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
block|}
end_class

end_unit

