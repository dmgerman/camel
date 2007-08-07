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
name|Processor
import|;
end_import

begin_comment
comment|/**  * A<a href="http://activemq.apache.org/camel/throttler.html">Throttler</a>  * will set a limit on the maximum number of message exchanges which can be sent  * to a processor within a specific time period.<p/> This pattern can be  * extremely useful if you have some external system which meters access; such  * as only allowing 100 requests per second; or if huge load can cause a  * particular systme to malfunction or to reduce its throughput you might want  * to introduce some throttling.  *   * @version $Revision: $  */
end_comment

begin_class
DECL|class|Throttler
specifier|public
class|class
name|Throttler
extends|extends
name|DelayProcessorSupport
block|{
DECL|field|maximumRequestsPerPeriod
specifier|private
name|long
name|maximumRequestsPerPeriod
decl_stmt|;
DECL|field|timePeriodMillis
specifier|private
name|long
name|timePeriodMillis
decl_stmt|;
DECL|field|startTimeMillis
specifier|private
name|long
name|startTimeMillis
decl_stmt|;
DECL|field|requestCount
specifier|private
name|long
name|requestCount
decl_stmt|;
DECL|method|Throttler (Processor processor, long maximumRequestsPerPeriod)
specifier|public
name|Throttler
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|long
name|maximumRequestsPerPeriod
parameter_list|)
block|{
name|this
argument_list|(
name|processor
argument_list|,
name|maximumRequestsPerPeriod
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
block|}
DECL|method|Throttler (Processor processor, long maximumRequestsPerPeriod, long timePeriodMillis)
specifier|public
name|Throttler
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|long
name|maximumRequestsPerPeriod
parameter_list|,
name|long
name|timePeriodMillis
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|maximumRequestsPerPeriod
operator|=
name|maximumRequestsPerPeriod
expr_stmt|;
name|this
operator|.
name|timePeriodMillis
operator|=
name|timePeriodMillis
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
name|maximumRequestsPerPeriod
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
comment|// Properties
comment|// -----------------------------------------------------------------------
DECL|method|getMaximumRequestsPerPeriod ()
specifier|public
name|long
name|getMaximumRequestsPerPeriod
parameter_list|()
block|{
return|return
name|maximumRequestsPerPeriod
return|;
block|}
comment|/**      * Sets the maximum number of requests per time period      */
DECL|method|setMaximumRequestsPerPeriod (long maximumRequestsPerPeriod)
specifier|public
name|void
name|setMaximumRequestsPerPeriod
parameter_list|(
name|long
name|maximumRequestsPerPeriod
parameter_list|)
block|{
name|this
operator|.
name|maximumRequestsPerPeriod
operator|=
name|maximumRequestsPerPeriod
expr_stmt|;
block|}
DECL|method|getTimePeriodMillis ()
specifier|public
name|long
name|getTimePeriodMillis
parameter_list|()
block|{
return|return
name|timePeriodMillis
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
operator|=
name|timePeriodMillis
expr_stmt|;
block|}
comment|/**      * The number of requests which have taken place so far within this time      * period      */
DECL|method|getRequestCount ()
specifier|public
name|long
name|getRequestCount
parameter_list|()
block|{
return|return
name|requestCount
return|;
block|}
comment|/**      * The start time when this current period began      */
DECL|method|getStartTimeMillis ()
specifier|public
name|long
name|getStartTimeMillis
parameter_list|()
block|{
return|return
name|startTimeMillis
return|;
block|}
comment|// Implementation methods
comment|// -----------------------------------------------------------------------
DECL|method|delay (Exchange exchange)
specifier|protected
name|void
name|delay
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|now
init|=
name|currentSystemTime
argument_list|()
decl_stmt|;
if|if
condition|(
name|startTimeMillis
operator|==
literal|0
condition|)
block|{
name|startTimeMillis
operator|=
name|now
expr_stmt|;
block|}
if|if
condition|(
name|now
operator|-
name|startTimeMillis
operator|>
name|timePeriodMillis
condition|)
block|{
comment|// we're at the start of a new time period
comment|// so lets reset things
name|requestCount
operator|=
literal|1
expr_stmt|;
name|startTimeMillis
operator|=
name|now
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|++
name|requestCount
operator|>
name|maximumRequestsPerPeriod
condition|)
block|{
comment|// lets sleep until the start of the next time period
name|long
name|time
init|=
name|startTimeMillis
operator|+
name|timePeriodMillis
decl_stmt|;
name|waitUntil
argument_list|(
name|time
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

