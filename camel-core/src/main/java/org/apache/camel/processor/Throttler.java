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
name|atomic
operator|.
name|AtomicLong
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
name|AsyncCallback
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
name|Expression
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
name|Processor
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
name|RuntimeExchangeException
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
name|Traceable
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
name|IdAware
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
comment|/**  * A<a href="http://camel.apache.org/throttler.html">Throttler</a>  * will set a limit on the maximum number of message exchanges which can be sent  * to a processor within a specific time period.<p/> This pattern can be  * extremely useful if you have some external system which meters access; such  * as only allowing 100 requests per second; or if huge load can cause a  * particular system to malfunction or to reduce its throughput you might want  * to introduce some throttling.  *  * @version  */
end_comment

begin_class
DECL|class|Throttler
specifier|public
class|class
name|Throttler
extends|extends
name|DelayProcessorSupport
implements|implements
name|Traceable
implements|,
name|IdAware
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|maximumRequestsPerPeriod
specifier|private
specifier|volatile
name|long
name|maximumRequestsPerPeriod
decl_stmt|;
DECL|field|maxRequestsPerPeriodExpression
specifier|private
name|Expression
name|maxRequestsPerPeriodExpression
decl_stmt|;
DECL|field|timePeriodMillis
specifier|private
name|AtomicLong
name|timePeriodMillis
init|=
operator|new
name|AtomicLong
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
DECL|field|slot
specifier|private
specifier|volatile
name|TimeSlot
name|slot
decl_stmt|;
DECL|field|rejectExecution
specifier|private
name|boolean
name|rejectExecution
decl_stmt|;
DECL|method|Throttler (CamelContext camelContext, Processor processor, Expression maxRequestsPerPeriodExpression, long timePeriodMillis, ScheduledExecutorService executorService, boolean shutdownExecutorService, boolean rejectExecution)
specifier|public
name|Throttler
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Expression
name|maxRequestsPerPeriodExpression
parameter_list|,
name|long
name|timePeriodMillis
parameter_list|,
name|ScheduledExecutorService
name|executorService
parameter_list|,
name|boolean
name|shutdownExecutorService
parameter_list|,
name|boolean
name|rejectExecution
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|,
name|processor
argument_list|,
name|executorService
argument_list|,
name|shutdownExecutorService
argument_list|)
expr_stmt|;
name|this
operator|.
name|rejectExecution
operator|=
name|rejectExecution
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|maxRequestsPerPeriodExpression
argument_list|,
literal|"maxRequestsPerPeriodExpression"
argument_list|)
expr_stmt|;
name|this
operator|.
name|maxRequestsPerPeriodExpression
operator|=
name|maxRequestsPerPeriodExpression
expr_stmt|;
if|if
condition|(
name|timePeriodMillis
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"TimePeriodMillis should be a positive number, was: "
operator|+
name|timePeriodMillis
argument_list|)
throw|;
block|}
name|this
operator|.
name|timePeriodMillis
operator|.
name|set
argument_list|(
name|timePeriodMillis
argument_list|)
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
literal|"Throttler[requests: "
operator|+
name|maxRequestsPerPeriodExpression
operator|+
literal|" per: "
operator|+
name|timePeriodMillis
operator|+
literal|" (ms) to: "
operator|+
name|getProcessor
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"throttle["
operator|+
name|maxRequestsPerPeriodExpression
operator|+
literal|" per: "
operator|+
name|timePeriodMillis
operator|+
literal|"]"
return|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
comment|// Properties
comment|// -----------------------------------------------------------------------
comment|/**      * Sets the maximum number of requests per time period expression      */
DECL|method|setMaximumRequestsPerPeriodExpression (Expression maxRequestsPerPeriodExpression)
specifier|public
name|void
name|setMaximumRequestsPerPeriodExpression
parameter_list|(
name|Expression
name|maxRequestsPerPeriodExpression
parameter_list|)
block|{
name|this
operator|.
name|maxRequestsPerPeriodExpression
operator|=
name|maxRequestsPerPeriodExpression
expr_stmt|;
block|}
DECL|method|getMaximumRequestsPerPeriodExpression ()
specifier|public
name|Expression
name|getMaximumRequestsPerPeriodExpression
parameter_list|()
block|{
return|return
name|maxRequestsPerPeriodExpression
return|;
block|}
DECL|method|getTimePeriodMillis ()
specifier|public
name|long
name|getTimePeriodMillis
parameter_list|()
block|{
return|return
name|timePeriodMillis
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**      * Gets the current maximum request per period value.      */
DECL|method|getCurrentMaximumRequestsPerPeriod ()
specifier|public
name|long
name|getCurrentMaximumRequestsPerPeriod
parameter_list|()
block|{
return|return
name|maximumRequestsPerPeriod
return|;
block|}
comment|/**      * Sets the time period during which the maximum number of requests apply      */
DECL|method|setTimePeriodMillis (long timePeriodMillis)
specifier|public
name|void
name|setTimePeriodMillis
parameter_list|(
name|long
name|timePeriodMillis
parameter_list|)
block|{
name|this
operator|.
name|timePeriodMillis
operator|.
name|set
argument_list|(
name|timePeriodMillis
argument_list|)
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -----------------------------------------------------------------------
DECL|method|calculateDelay (Exchange exchange)
specifier|protected
name|long
name|calculateDelay
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// evaluate as Object first to see if we get any result at all
name|Object
name|result
init|=
name|maxRequestsPerPeriodExpression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|maximumRequestsPerPeriod
operator|==
literal|0
operator|&&
name|result
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeExchangeException
argument_list|(
literal|"The max requests per period expression was evaluated as null: "
operator|+
name|maxRequestsPerPeriodExpression
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
comment|// then must convert value to long
name|Long
name|longValue
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Long
operator|.
name|class
argument_list|,
name|result
argument_list|)
decl_stmt|;
if|if
condition|(
name|longValue
operator|!=
literal|null
condition|)
block|{
comment|// log if we changed max period after initial setting
if|if
condition|(
name|maximumRequestsPerPeriod
operator|>
literal|0
operator|&&
name|longValue
operator|.
name|longValue
argument_list|()
operator|!=
name|maximumRequestsPerPeriod
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Throttler changed maximum requests per period from {} to {}"
argument_list|,
name|maximumRequestsPerPeriod
argument_list|,
name|longValue
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|maximumRequestsPerPeriod
operator|>
name|longValue
condition|)
block|{
name|slot
operator|.
name|capacity
operator|=
literal|0
expr_stmt|;
block|}
name|maximumRequestsPerPeriod
operator|=
name|longValue
expr_stmt|;
block|}
if|if
condition|(
name|maximumRequestsPerPeriod
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The maximumRequestsPerPeriod must be a positive number, was: "
operator|+
name|maximumRequestsPerPeriod
argument_list|)
throw|;
block|}
name|TimeSlot
name|slot
init|=
name|nextSlot
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|slot
operator|.
name|isActive
argument_list|()
condition|)
block|{
name|long
name|delay
init|=
name|slot
operator|.
name|startTime
operator|-
name|currentSystemTime
argument_list|()
decl_stmt|;
return|return
name|delay
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
comment|/*      * Determine what the next available time slot is for handling an Exchange      */
DECL|method|nextSlot ()
specifier|protected
specifier|synchronized
name|TimeSlot
name|nextSlot
parameter_list|()
throws|throws
name|ThrottlerRejectedExecutionException
block|{
if|if
condition|(
name|slot
operator|==
literal|null
condition|)
block|{
name|slot
operator|=
operator|new
name|TimeSlot
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|rejectExecution
operator|&&
name|slot
operator|.
name|isFull
argument_list|()
operator|&&
operator|!
name|slot
operator|.
name|isPast
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ThrottlerRejectedExecutionException
argument_list|(
literal|"Exceed the max request limit!"
argument_list|)
throw|;
block|}
if|if
condition|(
name|slot
operator|.
name|isFull
argument_list|()
operator|||
name|slot
operator|.
name|isPast
argument_list|()
condition|)
block|{
name|slot
operator|=
name|slot
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
name|slot
operator|.
name|assign
argument_list|()
expr_stmt|;
return|return
name|slot
return|;
block|}
comment|/*     * A time slot is capable of handling a number of exchanges within a certain period of time.     */
DECL|class|TimeSlot
specifier|protected
class|class
name|TimeSlot
block|{
DECL|field|capacity
specifier|private
specifier|volatile
name|long
name|capacity
init|=
name|Throttler
operator|.
name|this
operator|.
name|maximumRequestsPerPeriod
decl_stmt|;
DECL|field|duration
specifier|private
specifier|final
name|long
name|duration
init|=
name|Throttler
operator|.
name|this
operator|.
name|timePeriodMillis
operator|.
name|get
argument_list|()
decl_stmt|;
DECL|field|startTime
specifier|private
specifier|final
name|long
name|startTime
decl_stmt|;
DECL|method|TimeSlot ()
specifier|protected
name|TimeSlot
parameter_list|()
block|{
name|this
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|TimeSlot (long startTime)
specifier|protected
name|TimeSlot
parameter_list|(
name|long
name|startTime
parameter_list|)
block|{
name|this
operator|.
name|startTime
operator|=
name|startTime
expr_stmt|;
block|}
DECL|method|assign ()
specifier|protected
name|void
name|assign
parameter_list|()
block|{
name|capacity
operator|--
expr_stmt|;
block|}
comment|/*          * Start the next time slot either now or in the future          * (no time slots are being created in the past)          */
DECL|method|next ()
specifier|protected
name|TimeSlot
name|next
parameter_list|()
block|{
return|return
operator|new
name|TimeSlot
argument_list|(
name|Math
operator|.
name|max
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|,
name|this
operator|.
name|startTime
operator|+
name|this
operator|.
name|duration
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isPast ()
specifier|protected
name|boolean
name|isPast
parameter_list|()
block|{
name|long
name|current
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
return|return
name|current
operator|>
operator|(
name|startTime
operator|+
name|duration
operator|)
return|;
block|}
DECL|method|isActive ()
specifier|protected
name|boolean
name|isActive
parameter_list|()
block|{
name|long
name|current
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
return|return
name|startTime
operator|<=
name|current
operator|&&
name|current
operator|<
operator|(
name|startTime
operator|+
name|duration
operator|)
return|;
block|}
DECL|method|isFull ()
specifier|protected
name|boolean
name|isFull
parameter_list|()
block|{
return|return
name|capacity
operator|<=
literal|0
return|;
block|}
block|}
DECL|method|getSlot ()
name|TimeSlot
name|getSlot
parameter_list|()
block|{
return|return
name|this
operator|.
name|slot
return|;
block|}
DECL|method|isRejectExecution ()
specifier|public
name|boolean
name|isRejectExecution
parameter_list|()
block|{
return|return
name|rejectExecution
return|;
block|}
DECL|method|setRejectExecution (boolean rejectExecution)
specifier|public
name|void
name|setRejectExecution
parameter_list|(
name|boolean
name|rejectExecution
parameter_list|)
block|{
name|this
operator|.
name|rejectExecution
operator|=
name|rejectExecution
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|processDelay (Exchange exchange, AsyncCallback callback, long delay)
specifier|protected
name|boolean
name|processDelay
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|long
name|delay
parameter_list|)
block|{
if|if
condition|(
name|isRejectExecution
argument_list|()
operator|&&
name|delay
operator|>
literal|0
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|ThrottlerRejectedExecutionException
argument_list|(
literal|"Exceed the max request limit!"
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|processDelay
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|delay
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

