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
name|processor
operator|.
name|aggregate
operator|.
name|OptimisticLockRetryPolicy
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
comment|/**  * To configure optimistic locking  *  * @version  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"configuration"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"optimisticLockRetryPolicy"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|OptimisticLockRetryPolicyDefinition
specifier|public
class|class
name|OptimisticLockRetryPolicyDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|maximumRetries
specifier|private
name|Integer
name|maximumRetries
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"50"
argument_list|)
DECL|field|retryDelay
specifier|private
name|Long
name|retryDelay
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"1000"
argument_list|)
DECL|field|maximumRetryDelay
specifier|private
name|Long
name|maximumRetryDelay
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|exponentialBackOff
specifier|private
name|Boolean
name|exponentialBackOff
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|randomBackOff
specifier|private
name|Boolean
name|randomBackOff
decl_stmt|;
DECL|method|OptimisticLockRetryPolicyDefinition ()
specifier|public
name|OptimisticLockRetryPolicyDefinition
parameter_list|()
block|{     }
DECL|method|createOptimisticLockRetryPolicy ()
specifier|public
name|OptimisticLockRetryPolicy
name|createOptimisticLockRetryPolicy
parameter_list|()
block|{
name|OptimisticLockRetryPolicy
name|policy
init|=
operator|new
name|OptimisticLockRetryPolicy
argument_list|()
decl_stmt|;
if|if
condition|(
name|maximumRetries
operator|!=
literal|null
condition|)
block|{
name|policy
operator|.
name|setMaximumRetries
argument_list|(
name|maximumRetries
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|retryDelay
operator|!=
literal|null
condition|)
block|{
name|policy
operator|.
name|setRetryDelay
argument_list|(
name|retryDelay
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|maximumRetryDelay
operator|!=
literal|null
condition|)
block|{
name|policy
operator|.
name|setMaximumRetryDelay
argument_list|(
name|maximumRetryDelay
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exponentialBackOff
operator|!=
literal|null
condition|)
block|{
name|policy
operator|.
name|setExponentialBackOff
argument_list|(
name|exponentialBackOff
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|randomBackOff
operator|!=
literal|null
condition|)
block|{
name|policy
operator|.
name|setRandomBackOff
argument_list|(
name|randomBackOff
argument_list|)
expr_stmt|;
block|}
return|return
name|policy
return|;
block|}
comment|/**      * Sets the maximum number of retries      */
DECL|method|maximumRetries (int maximumRetries)
specifier|public
name|OptimisticLockRetryPolicyDefinition
name|maximumRetries
parameter_list|(
name|int
name|maximumRetries
parameter_list|)
block|{
name|setMaximumRetries
argument_list|(
name|maximumRetries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getMaximumRetries ()
specifier|public
name|Integer
name|getMaximumRetries
parameter_list|()
block|{
return|return
name|maximumRetries
return|;
block|}
DECL|method|setMaximumRetries (Integer maximumRetries)
specifier|public
name|void
name|setMaximumRetries
parameter_list|(
name|Integer
name|maximumRetries
parameter_list|)
block|{
name|this
operator|.
name|maximumRetries
operator|=
name|maximumRetries
expr_stmt|;
block|}
comment|/**      * Sets the delay in millis between retries      */
DECL|method|retryDelay (long retryDelay)
specifier|public
name|OptimisticLockRetryPolicyDefinition
name|retryDelay
parameter_list|(
name|long
name|retryDelay
parameter_list|)
block|{
name|setRetryDelay
argument_list|(
name|retryDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getRetryDelay ()
specifier|public
name|Long
name|getRetryDelay
parameter_list|()
block|{
return|return
name|retryDelay
return|;
block|}
DECL|method|setRetryDelay (Long retryDelay)
specifier|public
name|void
name|setRetryDelay
parameter_list|(
name|Long
name|retryDelay
parameter_list|)
block|{
name|this
operator|.
name|retryDelay
operator|=
name|retryDelay
expr_stmt|;
block|}
comment|/**      * Sets the upper value of retry in millis between retries, when using exponential or random backoff      */
DECL|method|maximumRetryDelay (long maximumRetryDelay)
specifier|public
name|OptimisticLockRetryPolicyDefinition
name|maximumRetryDelay
parameter_list|(
name|long
name|maximumRetryDelay
parameter_list|)
block|{
name|setMaximumRetryDelay
argument_list|(
name|maximumRetryDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getMaximumRetryDelay ()
specifier|public
name|Long
name|getMaximumRetryDelay
parameter_list|()
block|{
return|return
name|maximumRetryDelay
return|;
block|}
DECL|method|setMaximumRetryDelay (Long maximumRetryDelay)
specifier|public
name|void
name|setMaximumRetryDelay
parameter_list|(
name|Long
name|maximumRetryDelay
parameter_list|)
block|{
name|this
operator|.
name|maximumRetryDelay
operator|=
name|maximumRetryDelay
expr_stmt|;
block|}
comment|/**      * Enable exponential backoff      */
DECL|method|exponentialBackOff ()
specifier|public
name|OptimisticLockRetryPolicyDefinition
name|exponentialBackOff
parameter_list|()
block|{
return|return
name|exponentialBackOff
argument_list|(
literal|true
argument_list|)
return|;
block|}
DECL|method|exponentialBackOff (boolean exponentialBackOff)
specifier|public
name|OptimisticLockRetryPolicyDefinition
name|exponentialBackOff
parameter_list|(
name|boolean
name|exponentialBackOff
parameter_list|)
block|{
name|setExponentialBackOff
argument_list|(
name|exponentialBackOff
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getExponentialBackOff ()
specifier|public
name|Boolean
name|getExponentialBackOff
parameter_list|()
block|{
return|return
name|exponentialBackOff
return|;
block|}
DECL|method|setExponentialBackOff (Boolean exponentialBackOff)
specifier|public
name|void
name|setExponentialBackOff
parameter_list|(
name|Boolean
name|exponentialBackOff
parameter_list|)
block|{
name|this
operator|.
name|exponentialBackOff
operator|=
name|exponentialBackOff
expr_stmt|;
block|}
DECL|method|randomBackOff ()
specifier|public
name|OptimisticLockRetryPolicyDefinition
name|randomBackOff
parameter_list|()
block|{
return|return
name|randomBackOff
argument_list|(
literal|true
argument_list|)
return|;
block|}
comment|/**      * Enables random backoff      */
DECL|method|randomBackOff (boolean randomBackOff)
specifier|public
name|OptimisticLockRetryPolicyDefinition
name|randomBackOff
parameter_list|(
name|boolean
name|randomBackOff
parameter_list|)
block|{
name|setRandomBackOff
argument_list|(
name|randomBackOff
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getRandomBackOff ()
specifier|public
name|Boolean
name|getRandomBackOff
parameter_list|()
block|{
return|return
name|randomBackOff
return|;
block|}
DECL|method|setRandomBackOff (Boolean randomBackOff)
specifier|public
name|void
name|setRandomBackOff
parameter_list|(
name|Boolean
name|randomBackOff
parameter_list|)
block|{
name|this
operator|.
name|randomBackOff
operator|=
name|randomBackOff
expr_stmt|;
block|}
block|}
end_class

end_unit

