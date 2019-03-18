begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Locale
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
name|support
operator|.
name|AsyncProcessorSupport
import|;
end_import

begin_comment
comment|/**  * A<code>SamplingThrottler</code> is a special kind of throttler. It also  * limits the number of exchanges sent to a downstream endpoint. It differs from  * a normal throttler in that it will not queue exchanges above the threshold  * for a given period. Instead these exchanges will be stopped, precluding them  * from being processed at all by downstream consumers.  *<p/>  * This kind of throttling can be useful for taking a sample from  * an exchange stream, rough consolidation of noisy and bursty exchange traffic  * or where queuing of throttled exchanges is undesirable.  */
end_comment

begin_class
DECL|class|SamplingThrottler
specifier|public
class|class
name|SamplingThrottler
extends|extends
name|AsyncProcessorSupport
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
DECL|field|messageFrequency
specifier|private
name|long
name|messageFrequency
decl_stmt|;
DECL|field|currentMessageCount
specifier|private
name|long
name|currentMessageCount
decl_stmt|;
DECL|field|samplePeriod
specifier|private
name|long
name|samplePeriod
decl_stmt|;
DECL|field|periodInMillis
specifier|private
name|long
name|periodInMillis
decl_stmt|;
DECL|field|units
specifier|private
name|TimeUnit
name|units
decl_stmt|;
DECL|field|timeOfLastExchange
specifier|private
name|long
name|timeOfLastExchange
decl_stmt|;
DECL|field|stopper
specifier|private
name|StopProcessor
name|stopper
init|=
operator|new
name|StopProcessor
argument_list|()
decl_stmt|;
DECL|field|calculationLock
specifier|private
specifier|final
name|Object
name|calculationLock
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
DECL|field|sampled
specifier|private
name|SampleStats
name|sampled
init|=
operator|new
name|SampleStats
argument_list|()
decl_stmt|;
DECL|method|SamplingThrottler (long messageFrequency)
specifier|public
name|SamplingThrottler
parameter_list|(
name|long
name|messageFrequency
parameter_list|)
block|{
if|if
condition|(
name|messageFrequency
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A positive value is required for the sampling message frequency"
argument_list|)
throw|;
block|}
name|this
operator|.
name|messageFrequency
operator|=
name|messageFrequency
expr_stmt|;
block|}
DECL|method|SamplingThrottler (long samplePeriod, TimeUnit units)
specifier|public
name|SamplingThrottler
parameter_list|(
name|long
name|samplePeriod
parameter_list|,
name|TimeUnit
name|units
parameter_list|)
block|{
if|if
condition|(
name|samplePeriod
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A positive value is required for the sampling period"
argument_list|)
throw|;
block|}
if|if
condition|(
name|units
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"A invalid null value was supplied for the units of the sampling period"
argument_list|)
throw|;
block|}
name|this
operator|.
name|samplePeriod
operator|=
name|samplePeriod
expr_stmt|;
name|this
operator|.
name|units
operator|=
name|units
expr_stmt|;
name|this
operator|.
name|periodInMillis
operator|=
name|units
operator|.
name|toMillis
argument_list|(
name|samplePeriod
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
if|if
condition|(
name|messageFrequency
operator|>
literal|0
condition|)
block|{
return|return
literal|"SamplingThrottler[1 exchange per: "
operator|+
name|messageFrequency
operator|+
literal|" messages received]"
return|;
block|}
else|else
block|{
return|return
literal|"SamplingThrottler[1 exchange per: "
operator|+
name|samplePeriod
operator|+
literal|" "
operator|+
name|units
operator|.
name|toString
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|+
literal|"]"
return|;
block|}
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
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
if|if
condition|(
name|messageFrequency
operator|>
literal|0
condition|)
block|{
return|return
literal|"samplingThrottler[1 exchange per: "
operator|+
name|messageFrequency
operator|+
literal|" messages received]"
return|;
block|}
else|else
block|{
return|return
literal|"samplingThrottler[1 exchange per: "
operator|+
name|samplePeriod
operator|+
literal|" "
operator|+
name|units
operator|.
name|toString
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|+
literal|"]"
return|;
block|}
block|}
DECL|method|getMessageFrequency ()
specifier|public
name|long
name|getMessageFrequency
parameter_list|()
block|{
return|return
name|messageFrequency
return|;
block|}
DECL|method|getSamplePeriod ()
specifier|public
name|long
name|getSamplePeriod
parameter_list|()
block|{
return|return
name|samplePeriod
return|;
block|}
DECL|method|getUnits ()
specifier|public
name|TimeUnit
name|getUnits
parameter_list|()
block|{
return|return
name|units
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|boolean
name|doSend
init|=
literal|false
decl_stmt|;
synchronized|synchronized
init|(
name|calculationLock
init|)
block|{
if|if
condition|(
name|messageFrequency
operator|>
literal|0
condition|)
block|{
name|currentMessageCount
operator|++
expr_stmt|;
if|if
condition|(
name|currentMessageCount
operator|%
name|messageFrequency
operator|==
literal|0
condition|)
block|{
name|doSend
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
name|long
name|now
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
if|if
condition|(
name|now
operator|>=
name|timeOfLastExchange
operator|+
name|periodInMillis
condition|)
block|{
name|doSend
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
name|sampled
operator|.
name|sample
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|timeOfLastExchange
operator|=
name|now
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
name|sampled
operator|.
name|drop
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|doSend
condition|)
block|{
comment|// will just set a property
try|try
block|{
name|stopper
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|// we are done synchronously
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
DECL|class|SampleStats
specifier|private
specifier|static
class|class
name|SampleStats
block|{
DECL|field|droppedThisPeriod
specifier|private
name|long
name|droppedThisPeriod
decl_stmt|;
DECL|field|totalDropped
specifier|private
name|long
name|totalDropped
decl_stmt|;
DECL|field|totalSampled
specifier|private
name|long
name|totalSampled
decl_stmt|;
DECL|field|totalThisPeriod
specifier|private
name|long
name|totalThisPeriod
decl_stmt|;
DECL|method|drop ()
name|String
name|drop
parameter_list|()
block|{
name|droppedThisPeriod
operator|++
expr_stmt|;
name|totalThisPeriod
operator|++
expr_stmt|;
name|totalDropped
operator|++
expr_stmt|;
return|return
name|getDroppedLog
argument_list|()
return|;
block|}
DECL|method|sample ()
name|String
name|sample
parameter_list|()
block|{
name|totalThisPeriod
operator|=
literal|1
expr_stmt|;
comment|// a new period, reset to 1
name|totalSampled
operator|++
expr_stmt|;
name|droppedThisPeriod
operator|=
literal|0
expr_stmt|;
return|return
name|getSampledLog
argument_list|()
return|;
block|}
DECL|method|getSampledLog ()
name|String
name|getSampledLog
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"Sampled %d of %d total exchanges"
argument_list|,
name|totalSampled
argument_list|,
name|totalSampled
operator|+
name|totalDropped
argument_list|)
return|;
block|}
DECL|method|getDroppedLog ()
name|String
name|getDroppedLog
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"Dropped %d of %d exchanges in this period, totalling %d dropped of %d exchanges overall."
argument_list|,
name|droppedThisPeriod
argument_list|,
name|totalThisPeriod
argument_list|,
name|totalDropped
argument_list|,
name|totalSampled
operator|+
name|totalDropped
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

