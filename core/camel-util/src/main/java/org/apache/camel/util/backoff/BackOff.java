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

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
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

begin_comment
comment|/**  * A back-off policy.  */
end_comment

begin_class
DECL|class|BackOff
specifier|public
specifier|final
class|class
name|BackOff
block|{
DECL|field|NEVER
specifier|public
specifier|static
specifier|final
name|long
name|NEVER
init|=
operator|-
literal|1L
decl_stmt|;
DECL|field|MAX_DURATION
specifier|public
specifier|static
specifier|final
name|Duration
name|MAX_DURATION
init|=
name|Duration
operator|.
name|ofMillis
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_DELAY
specifier|public
specifier|static
specifier|final
name|Duration
name|DEFAULT_DELAY
init|=
name|Duration
operator|.
name|ofSeconds
argument_list|(
literal|2
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_MULTIPLIER
specifier|public
specifier|static
specifier|final
name|double
name|DEFAULT_MULTIPLIER
init|=
literal|1f
decl_stmt|;
DECL|field|delay
specifier|private
name|Duration
name|delay
decl_stmt|;
DECL|field|maxDelay
specifier|private
name|Duration
name|maxDelay
decl_stmt|;
DECL|field|maxElapsedTime
specifier|private
name|Duration
name|maxElapsedTime
decl_stmt|;
DECL|field|maxAttempts
specifier|private
name|Long
name|maxAttempts
decl_stmt|;
DECL|field|multiplier
specifier|private
name|Double
name|multiplier
decl_stmt|;
DECL|method|BackOff ()
specifier|public
name|BackOff
parameter_list|()
block|{
name|this
argument_list|(
name|DEFAULT_DELAY
argument_list|,
name|MAX_DURATION
argument_list|,
name|MAX_DURATION
argument_list|,
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|DEFAULT_MULTIPLIER
argument_list|)
expr_stmt|;
block|}
DECL|method|BackOff (Duration delay, Duration maxDelay, Duration maxElapsedTime, Long maxAttempts, Double multiplier)
specifier|public
name|BackOff
parameter_list|(
name|Duration
name|delay
parameter_list|,
name|Duration
name|maxDelay
parameter_list|,
name|Duration
name|maxElapsedTime
parameter_list|,
name|Long
name|maxAttempts
parameter_list|,
name|Double
name|multiplier
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|ObjectHelper
operator|.
name|supplyIfEmpty
argument_list|(
name|delay
argument_list|,
parameter_list|()
lambda|->
name|DEFAULT_DELAY
argument_list|)
expr_stmt|;
name|this
operator|.
name|maxDelay
operator|=
name|ObjectHelper
operator|.
name|supplyIfEmpty
argument_list|(
name|maxDelay
argument_list|,
parameter_list|()
lambda|->
name|MAX_DURATION
argument_list|)
expr_stmt|;
name|this
operator|.
name|maxElapsedTime
operator|=
name|ObjectHelper
operator|.
name|supplyIfEmpty
argument_list|(
name|maxElapsedTime
argument_list|,
parameter_list|()
lambda|->
name|MAX_DURATION
argument_list|)
expr_stmt|;
name|this
operator|.
name|maxAttempts
operator|=
name|ObjectHelper
operator|.
name|supplyIfEmpty
argument_list|(
name|maxAttempts
argument_list|,
parameter_list|()
lambda|->
name|Long
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|this
operator|.
name|multiplier
operator|=
name|ObjectHelper
operator|.
name|supplyIfEmpty
argument_list|(
name|multiplier
argument_list|,
parameter_list|()
lambda|->
name|DEFAULT_MULTIPLIER
argument_list|)
expr_stmt|;
block|}
comment|// *************************************
comment|// Properties
comment|// *************************************
comment|/**      * @return the delay to wait before retry the operation.      */
DECL|method|getDelay ()
specifier|public
name|Duration
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
comment|/**      * The delay to wait before retry the operation.      */
DECL|method|setDelay (Duration delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|Duration
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
DECL|method|getMaxDelay ()
specifier|public
name|Duration
name|getMaxDelay
parameter_list|()
block|{
return|return
name|maxDelay
return|;
block|}
comment|/**      * The maximum back-off time after which the delay is not more increased.      */
DECL|method|setMaxDelay (Duration maxDelay)
specifier|public
name|void
name|setMaxDelay
parameter_list|(
name|Duration
name|maxDelay
parameter_list|)
block|{
name|this
operator|.
name|maxDelay
operator|=
name|maxDelay
expr_stmt|;
block|}
DECL|method|getMaxElapsedTime ()
specifier|public
name|Duration
name|getMaxElapsedTime
parameter_list|()
block|{
return|return
name|maxElapsedTime
return|;
block|}
comment|/**      * The maximum elapsed time after which the back-off should be considered      * exhausted and no more attempts should be made.      */
DECL|method|setMaxElapsedTime (Duration maxElapsedTime)
specifier|public
name|void
name|setMaxElapsedTime
parameter_list|(
name|Duration
name|maxElapsedTime
parameter_list|)
block|{
name|this
operator|.
name|maxElapsedTime
operator|=
name|maxElapsedTime
expr_stmt|;
block|}
DECL|method|getMaxAttempts ()
specifier|public
name|Long
name|getMaxAttempts
parameter_list|()
block|{
return|return
name|maxAttempts
return|;
block|}
comment|/**      * The maximum number of attempts after which the back-off should be considered      * exhausted and no more attempts should be made.      *      * @param maxAttempts      */
DECL|method|setMaxAttempts (Long maxAttempts)
specifier|public
name|void
name|setMaxAttempts
parameter_list|(
name|Long
name|maxAttempts
parameter_list|)
block|{
name|this
operator|.
name|maxAttempts
operator|=
name|maxAttempts
expr_stmt|;
block|}
DECL|method|getMultiplier ()
specifier|public
name|Double
name|getMultiplier
parameter_list|()
block|{
return|return
name|multiplier
return|;
block|}
comment|/**      * The value to multiply the current interval by for each retry attempt.      */
DECL|method|setMultiplier (Double multiplier)
specifier|public
name|void
name|setMultiplier
parameter_list|(
name|Double
name|multiplier
parameter_list|)
block|{
name|this
operator|.
name|multiplier
operator|=
name|multiplier
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"BackOff{"
operator|+
literal|"delay="
operator|+
name|delay
operator|+
literal|", maxDelay="
operator|+
name|maxDelay
operator|+
literal|", maxElapsedTime="
operator|+
name|maxElapsedTime
operator|+
literal|", maxAttempts="
operator|+
name|maxAttempts
operator|+
literal|", multiplier="
operator|+
name|multiplier
operator|+
literal|'}'
return|;
block|}
comment|// *****************************************
comment|// Builder
comment|// *****************************************
DECL|method|builder ()
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|Builder
argument_list|()
return|;
block|}
DECL|method|builder (BackOff template)
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|(
name|BackOff
name|template
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|()
operator|.
name|read
argument_list|(
name|template
argument_list|)
return|;
block|}
comment|/**      * A builder for {@link BackOff}      */
DECL|class|Builder
specifier|public
specifier|static
specifier|final
class|class
name|Builder
block|{
DECL|field|delay
specifier|private
name|Duration
name|delay
init|=
name|BackOff
operator|.
name|DEFAULT_DELAY
decl_stmt|;
DECL|field|maxDelay
specifier|private
name|Duration
name|maxDelay
init|=
name|BackOff
operator|.
name|MAX_DURATION
decl_stmt|;
DECL|field|maxElapsedTime
specifier|private
name|Duration
name|maxElapsedTime
init|=
name|BackOff
operator|.
name|MAX_DURATION
decl_stmt|;
DECL|field|maxAttempts
specifier|private
name|Long
name|maxAttempts
init|=
name|Long
operator|.
name|MAX_VALUE
decl_stmt|;
DECL|field|multiplier
specifier|private
name|Double
name|multiplier
init|=
name|BackOff
operator|.
name|DEFAULT_MULTIPLIER
decl_stmt|;
comment|/**          * Read values from the given {@link BackOff}          */
DECL|method|read (BackOff template)
specifier|public
name|Builder
name|read
parameter_list|(
name|BackOff
name|template
parameter_list|)
block|{
name|delay
operator|=
name|template
operator|.
name|delay
expr_stmt|;
name|maxDelay
operator|=
name|template
operator|.
name|maxDelay
expr_stmt|;
name|maxElapsedTime
operator|=
name|template
operator|.
name|maxElapsedTime
expr_stmt|;
name|maxAttempts
operator|=
name|template
operator|.
name|maxAttempts
expr_stmt|;
name|multiplier
operator|=
name|template
operator|.
name|multiplier
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|delay (Duration delay)
specifier|public
name|Builder
name|delay
parameter_list|(
name|Duration
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|delay (long delay, TimeUnit unit)
specifier|public
name|Builder
name|delay
parameter_list|(
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|delay
argument_list|(
name|Duration
operator|.
name|ofMillis
argument_list|(
name|unit
operator|.
name|toMillis
argument_list|(
name|delay
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|delay (long delay)
specifier|public
name|Builder
name|delay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
return|return
name|delay
argument_list|(
name|Duration
operator|.
name|ofMillis
argument_list|(
name|delay
argument_list|)
argument_list|)
return|;
block|}
DECL|method|maxDelay (Duration maxDelay)
specifier|public
name|Builder
name|maxDelay
parameter_list|(
name|Duration
name|maxDelay
parameter_list|)
block|{
name|this
operator|.
name|maxDelay
operator|=
name|maxDelay
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|maxDelay (long maxDelay, TimeUnit unit)
specifier|public
name|Builder
name|maxDelay
parameter_list|(
name|long
name|maxDelay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|maxDelay
argument_list|(
name|Duration
operator|.
name|ofMillis
argument_list|(
name|unit
operator|.
name|toMillis
argument_list|(
name|maxDelay
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|maxDelay (long maxDelay)
specifier|public
name|Builder
name|maxDelay
parameter_list|(
name|long
name|maxDelay
parameter_list|)
block|{
return|return
name|maxDelay
argument_list|(
name|Duration
operator|.
name|ofMillis
argument_list|(
name|maxDelay
argument_list|)
argument_list|)
return|;
block|}
DECL|method|maxElapsedTime (Duration maxElapsedTime)
specifier|public
name|Builder
name|maxElapsedTime
parameter_list|(
name|Duration
name|maxElapsedTime
parameter_list|)
block|{
name|this
operator|.
name|maxElapsedTime
operator|=
name|maxElapsedTime
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|maxElapsedTime (long maxElapsedTime, TimeUnit unit)
specifier|public
name|Builder
name|maxElapsedTime
parameter_list|(
name|long
name|maxElapsedTime
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|maxElapsedTime
argument_list|(
name|Duration
operator|.
name|ofMillis
argument_list|(
name|unit
operator|.
name|toMillis
argument_list|(
name|maxElapsedTime
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|maxElapsedTime (long maxElapsedTime)
specifier|public
name|Builder
name|maxElapsedTime
parameter_list|(
name|long
name|maxElapsedTime
parameter_list|)
block|{
return|return
name|maxElapsedTime
argument_list|(
name|Duration
operator|.
name|ofMillis
argument_list|(
name|maxElapsedTime
argument_list|)
argument_list|)
return|;
block|}
DECL|method|maxAttempts (Long attempts)
specifier|public
name|Builder
name|maxAttempts
parameter_list|(
name|Long
name|attempts
parameter_list|)
block|{
name|this
operator|.
name|maxAttempts
operator|=
name|attempts
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|multiplier (Double multiplier)
specifier|public
name|Builder
name|multiplier
parameter_list|(
name|Double
name|multiplier
parameter_list|)
block|{
name|this
operator|.
name|multiplier
operator|=
name|multiplier
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Build a new instance of {@link BackOff}          */
DECL|method|build ()
specifier|public
name|BackOff
name|build
parameter_list|()
block|{
return|return
operator|new
name|BackOff
argument_list|(
name|delay
argument_list|,
name|maxDelay
argument_list|,
name|maxElapsedTime
argument_list|,
name|maxAttempts
argument_list|,
name|multiplier
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit
