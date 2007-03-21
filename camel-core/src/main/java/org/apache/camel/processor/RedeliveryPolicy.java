begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|Random
import|;
end_import

begin_comment
comment|// Code taken from the ActiveMQ codebase
end_comment

begin_comment
comment|/**  * The policy used to decide how many times to redeliver and the time between the redeliveries before being sent to a  *<a href="http://activemq.apache.org/camel/dead-letter-channel.html">Dead Letter Channel</a>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RedeliveryPolicy
specifier|public
class|class
name|RedeliveryPolicy
block|{
DECL|field|maximumRedeliveries
specifier|protected
name|int
name|maximumRedeliveries
init|=
literal|6
decl_stmt|;
DECL|field|initialRedeliveryDelay
specifier|protected
name|long
name|initialRedeliveryDelay
init|=
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
init|=
literal|false
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
init|=
literal|false
decl_stmt|;
DECL|field|randomNumberGenerator
specifier|protected
specifier|static
name|Random
name|randomNumberGenerator
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
comment|/**      * Returns true if the policy decides that the message exchange should be redelivered      */
DECL|method|shouldRedeliver (int redeliveryCounter)
specifier|public
name|boolean
name|shouldRedeliver
parameter_list|(
name|int
name|redeliveryCounter
parameter_list|)
block|{
return|return
name|redeliveryCounter
operator|<=
name|getMaximumRedeliveries
argument_list|()
return|;
block|}
comment|// Builder methods
comment|//-------------------------------------------------------------------------
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
comment|/**      * Enables collision avoidence which adds some randomization to the backoff timings to reduce contention probability      */
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
comment|/**      * Enables exponential backof using the {@link #getBackOffMultiplier()} to increase the time between retries      */
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
comment|/**      * Enables exponential backoff and sets the multiplier used to increase the delay between redeliveries      */
DECL|method|backOffMultiplier (double backOffMultiplier)
specifier|public
name|RedeliveryPolicy
name|backOffMultiplier
parameter_list|(
name|double
name|backOffMultiplier
parameter_list|)
block|{
name|useExponentialBackOff
argument_list|()
expr_stmt|;
name|setBackOffMultiplier
argument_list|(
name|backOffMultiplier
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Enables collision avoidence and sets the percentage used      */
DECL|method|collisionAvoidancePercent (short collisionAvoidancePercent)
specifier|public
name|RedeliveryPolicy
name|collisionAvoidancePercent
parameter_list|(
name|short
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
comment|// Properties
comment|//-------------------------------------------------------------------------
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
comment|/**      * Sets the multiplier used to increase the delay between redeliveries if {@link #setUseExponentialBackOff(boolean)} is enabled      */
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
comment|/**      * Sets the percentage used for collision avoidence if enabled via {@link #setUseCollisionAvoidance(boolean)}      */
DECL|method|setCollisionAvoidancePercent (short collisionAvoidancePercent)
specifier|public
name|void
name|setCollisionAvoidancePercent
parameter_list|(
name|short
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
comment|/**      * Sets the factor used for collision avoidence if enabled via {@link #setUseCollisionAvoidance(boolean)}      */
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
comment|/**      * Sets the maximum number of times a message exchange will be redelivered      */
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
if|if
condition|(
name|randomNumberGenerator
operator|==
literal|null
condition|)
block|{
name|initRandomNumberGenerator
argument_list|()
expr_stmt|;
block|}
comment|/*              * First random determines +/-, second random determines how far to              * go in that direction. -cgs              */
name|double
name|variance
init|=
operator|(
name|randomNumberGenerator
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
name|randomNumberGenerator
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
return|return
name|redeliveryDelay
return|;
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
comment|/**      * Enables/disables collision avoidence which adds some randomization to the backoff timings to reduce contention probability      */
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
comment|/**      * Enables/disables exponential backof using the {@link #getBackOffMultiplier()} to increase the time between retries      */
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
DECL|method|initRandomNumberGenerator ()
specifier|protected
specifier|static
specifier|synchronized
name|void
name|initRandomNumberGenerator
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
block|}
block|}
end_class

end_unit

