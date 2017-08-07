begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.backoff
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|backoff
package|;
end_package

begin_comment
comment|/**  * The context associated to a back-off operation.  */
end_comment

begin_class
DECL|class|BackOffContext
specifier|public
specifier|final
class|class
name|BackOffContext
block|{
DECL|field|backOff
specifier|private
specifier|final
name|BackOff
name|backOff
decl_stmt|;
DECL|field|currentAttempts
specifier|private
name|long
name|currentAttempts
decl_stmt|;
DECL|field|currentDelay
specifier|private
name|long
name|currentDelay
decl_stmt|;
DECL|field|currentElapsedTime
specifier|private
name|long
name|currentElapsedTime
decl_stmt|;
DECL|method|BackOffContext (BackOff backOff)
specifier|public
name|BackOffContext
parameter_list|(
name|BackOff
name|backOff
parameter_list|)
block|{
name|this
operator|.
name|backOff
operator|=
name|backOff
expr_stmt|;
name|this
operator|.
name|currentAttempts
operator|=
literal|0
expr_stmt|;
name|this
operator|.
name|currentDelay
operator|=
name|backOff
operator|.
name|getDelay
argument_list|()
operator|.
name|toMillis
argument_list|()
expr_stmt|;
name|this
operator|.
name|currentElapsedTime
operator|=
literal|0
expr_stmt|;
block|}
comment|// *************************************
comment|// Properties
comment|// *************************************
comment|/**      * The back-off associated with this context.      */
DECL|method|backOff ()
specifier|public
name|BackOff
name|backOff
parameter_list|()
block|{
return|return
name|backOff
return|;
block|}
comment|/**      * The number of attempts so far.      */
DECL|method|getCurrentAttempts ()
specifier|public
name|long
name|getCurrentAttempts
parameter_list|()
block|{
return|return
name|currentAttempts
return|;
block|}
comment|/**      * The current computed delay.      */
DECL|method|getCurrentDelay ()
specifier|public
name|long
name|getCurrentDelay
parameter_list|()
block|{
return|return
name|currentDelay
return|;
block|}
comment|/**      * The current elapsed time.      */
DECL|method|getCurrentElapsedTime ()
specifier|public
name|long
name|getCurrentElapsedTime
parameter_list|()
block|{
return|return
name|currentElapsedTime
return|;
block|}
comment|/**      * Inform if the context is exhausted thus not more attempts should be made.      */
DECL|method|isExhausted ()
specifier|public
name|boolean
name|isExhausted
parameter_list|()
block|{
return|return
name|currentDelay
operator|==
name|BackOff
operator|.
name|NEVER
return|;
block|}
comment|// *************************************
comment|// Impl
comment|// *************************************
comment|/**      * Return the number of milliseconds to wait before retrying the operation      * or ${@link BackOff#NEVER} to indicate that no further attempt should be      * made.      */
DECL|method|next ()
specifier|public
name|long
name|next
parameter_list|()
block|{
comment|// A call to next when currentDelay is set to NEVER has no effects
comment|// as this means that either the timer is exhausted or it has explicit
comment|// stopped
if|if
condition|(
name|currentDelay
operator|!=
name|BackOff
operator|.
name|NEVER
condition|)
block|{
name|currentAttempts
operator|++
expr_stmt|;
if|if
condition|(
name|currentAttempts
operator|>
name|backOff
operator|.
name|getMaxAttempts
argument_list|()
condition|)
block|{
name|currentDelay
operator|=
name|BackOff
operator|.
name|NEVER
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|currentElapsedTime
operator|>
name|backOff
operator|.
name|getMaxElapsedTime
argument_list|()
operator|.
name|toMillis
argument_list|()
condition|)
block|{
name|currentDelay
operator|=
name|BackOff
operator|.
name|NEVER
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|currentDelay
operator|<=
name|backOff
operator|.
name|getMaxDelay
argument_list|()
operator|.
name|toMillis
argument_list|()
condition|)
block|{
name|currentDelay
operator|=
call|(
name|long
call|)
argument_list|(
name|currentDelay
operator|*
name|backOff
argument_list|()
operator|.
name|getMultiplier
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|currentElapsedTime
operator|+=
name|currentDelay
expr_stmt|;
block|}
block|}
return|return
name|currentDelay
return|;
block|}
comment|/**      * Reset the context.      */
DECL|method|reset ()
specifier|public
name|BackOffContext
name|reset
parameter_list|()
block|{
name|this
operator|.
name|currentAttempts
operator|=
literal|0
expr_stmt|;
name|this
operator|.
name|currentDelay
operator|=
literal|0
expr_stmt|;
name|this
operator|.
name|currentElapsedTime
operator|=
literal|0
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Mark the context as exhausted to indicate that no further attempt should      * be made.      */
DECL|method|stop ()
specifier|public
name|BackOffContext
name|stop
parameter_list|()
block|{
name|this
operator|.
name|currentAttempts
operator|=
literal|0
expr_stmt|;
name|this
operator|.
name|currentDelay
operator|=
name|BackOff
operator|.
name|NEVER
expr_stmt|;
name|this
operator|.
name|currentElapsedTime
operator|=
literal|0
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

