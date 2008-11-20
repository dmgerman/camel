begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|XmlAttribute
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
name|processor
operator|.
name|RedeliveryPolicy
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
name|CamelContextHelper
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;redeliveryPolicy/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"redeliveryPolicy"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RedeliveryPolicyType
specifier|public
class|class
name|RedeliveryPolicyType
block|{
annotation|@
name|XmlAttribute
argument_list|()
DECL|field|ref
specifier|private
name|String
name|ref
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|maximumRedeliveries
specifier|private
name|Integer
name|maximumRedeliveries
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|delay
specifier|private
name|Long
name|delay
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|backOffMultiplier
specifier|private
name|Double
name|backOffMultiplier
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|useExponentialBackOff
specifier|private
name|Boolean
name|useExponentialBackOff
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|collisionAvoidanceFactor
specifier|private
name|Double
name|collisionAvoidanceFactor
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|useCollisionAvoidance
specifier|private
name|Boolean
name|useCollisionAvoidance
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|maximumRedeliveryDelay
specifier|private
name|Long
name|maximumRedeliveryDelay
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|retriesExhaustedLogLevel
specifier|private
name|LoggingLevel
name|retriesExhaustedLogLevel
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|retryAttemptedLogLevel
specifier|private
name|LoggingLevel
name|retryAttemptedLogLevel
decl_stmt|;
DECL|method|createRedeliveryPolicy (CamelContext context, RedeliveryPolicy parentPolicy)
specifier|public
name|RedeliveryPolicy
name|createRedeliveryPolicy
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|RedeliveryPolicy
name|parentPolicy
parameter_list|)
block|{
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
comment|// lookup in registry if ref provided
return|return
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|context
argument_list|,
name|ref
argument_list|,
name|RedeliveryPolicy
operator|.
name|class
argument_list|)
return|;
block|}
name|RedeliveryPolicy
name|answer
init|=
name|parentPolicy
operator|.
name|copy
argument_list|()
decl_stmt|;
comment|// copy across the properties - if they are set
if|if
condition|(
name|maximumRedeliveries
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setMaximumRedeliveries
argument_list|(
name|maximumRedeliveries
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|delay
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setDelay
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|retriesExhaustedLogLevel
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setRetriesExhaustedLogLevel
argument_list|(
name|retriesExhaustedLogLevel
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|retryAttemptedLogLevel
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setRetryAttemptedLogLevel
argument_list|(
name|retryAttemptedLogLevel
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|backOffMultiplier
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setBackOffMultiplier
argument_list|(
name|backOffMultiplier
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|useExponentialBackOff
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setUseExponentialBackOff
argument_list|(
name|useExponentialBackOff
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|collisionAvoidanceFactor
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setCollisionAvoidanceFactor
argument_list|(
name|collisionAvoidanceFactor
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|useCollisionAvoidance
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setUseCollisionAvoidance
argument_list|(
name|useCollisionAvoidance
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|maximumRedeliveryDelay
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setMaximumRedeliveryDelay
argument_list|(
name|maximumRedeliveryDelay
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"RedeliveryPolicy[maximumRedeliveries: "
operator|+
name|maximumRedeliveries
operator|+
literal|"]"
return|;
block|}
comment|// Fluent API
comment|//-------------------------------------------------------------------------
DECL|method|backOffMultiplier (double backOffMultiplier)
specifier|public
name|RedeliveryPolicyType
name|backOffMultiplier
parameter_list|(
name|double
name|backOffMultiplier
parameter_list|)
block|{
name|setBackOffMultiplier
argument_list|(
name|backOffMultiplier
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|collisionAvoidancePercent (double collisionAvoidancePercent)
specifier|public
name|RedeliveryPolicyType
name|collisionAvoidancePercent
parameter_list|(
name|double
name|collisionAvoidancePercent
parameter_list|)
block|{
name|setCollisionAvoidanceFactor
argument_list|(
name|collisionAvoidancePercent
operator|*
literal|0.01d
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|collisionAvoidanceFactor (double collisionAvoidanceFactor)
specifier|public
name|RedeliveryPolicyType
name|collisionAvoidanceFactor
parameter_list|(
name|double
name|collisionAvoidanceFactor
parameter_list|)
block|{
name|setCollisionAvoidanceFactor
argument_list|(
name|collisionAvoidanceFactor
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|delay (long delay)
specifier|public
name|RedeliveryPolicyType
name|delay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|setDelay
argument_list|(
name|delay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|retriesExhaustedLogLevel (LoggingLevel retriesExhaustedLogLevel)
specifier|public
name|RedeliveryPolicyType
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
DECL|method|retryAttemptedLogLevel (LoggingLevel retryAttemptedLogLevel)
specifier|public
name|RedeliveryPolicyType
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
DECL|method|maximumRedeliveries (int maximumRedeliveries)
specifier|public
name|RedeliveryPolicyType
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
DECL|method|useCollisionAvoidance ()
specifier|public
name|RedeliveryPolicyType
name|useCollisionAvoidance
parameter_list|()
block|{
name|setUseCollisionAvoidance
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|useExponentialBackOff ()
specifier|public
name|RedeliveryPolicyType
name|useExponentialBackOff
parameter_list|()
block|{
name|setUseExponentialBackOff
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|maximumRedeliveryDelay (long maximumRedeliveryDelay)
specifier|public
name|RedeliveryPolicyType
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
DECL|method|ref (String ref)
specifier|public
name|RedeliveryPolicyType
name|ref
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|setRef
argument_list|(
name|ref
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
name|Double
name|getBackOffMultiplier
parameter_list|()
block|{
return|return
name|backOffMultiplier
return|;
block|}
DECL|method|setBackOffMultiplier (Double backOffMultiplier)
specifier|public
name|void
name|setBackOffMultiplier
parameter_list|(
name|Double
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
DECL|method|getCollisionAvoidanceFactor ()
specifier|public
name|Double
name|getCollisionAvoidanceFactor
parameter_list|()
block|{
return|return
name|collisionAvoidanceFactor
return|;
block|}
DECL|method|setCollisionAvoidanceFactor (Double collisionAvoidanceFactor)
specifier|public
name|void
name|setCollisionAvoidanceFactor
parameter_list|(
name|Double
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
DECL|method|getDelay ()
specifier|public
name|Long
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
DECL|method|setDelay (Long delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|Long
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
block|}
DECL|method|getMaximumRedeliveries ()
specifier|public
name|Integer
name|getMaximumRedeliveries
parameter_list|()
block|{
return|return
name|maximumRedeliveries
return|;
block|}
DECL|method|setMaximumRedeliveries (Integer maximumRedeliveries)
specifier|public
name|void
name|setMaximumRedeliveries
parameter_list|(
name|Integer
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
DECL|method|getUseCollisionAvoidance ()
specifier|public
name|Boolean
name|getUseCollisionAvoidance
parameter_list|()
block|{
return|return
name|useCollisionAvoidance
return|;
block|}
DECL|method|setUseCollisionAvoidance (Boolean useCollisionAvoidance)
specifier|public
name|void
name|setUseCollisionAvoidance
parameter_list|(
name|Boolean
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
DECL|method|getUseExponentialBackOff ()
specifier|public
name|Boolean
name|getUseExponentialBackOff
parameter_list|()
block|{
return|return
name|useExponentialBackOff
return|;
block|}
DECL|method|setUseExponentialBackOff (Boolean useExponentialBackOff)
specifier|public
name|void
name|setUseExponentialBackOff
parameter_list|(
name|Boolean
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
DECL|method|getMaximumRedeliveryDelay ()
specifier|public
name|Long
name|getMaximumRedeliveryDelay
parameter_list|()
block|{
return|return
name|maximumRedeliveryDelay
return|;
block|}
DECL|method|setMaximumRedeliveryDelay (Long maximumRedeliveryDelay)
specifier|public
name|void
name|setMaximumRedeliveryDelay
parameter_list|(
name|Long
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
DECL|method|getRef ()
specifier|public
name|String
name|getRef
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
DECL|method|setRef (String ref)
specifier|public
name|void
name|setRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
block|}
end_class

end_unit

