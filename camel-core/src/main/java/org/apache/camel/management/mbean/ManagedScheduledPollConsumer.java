begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedSchedulePollConsumerMBean
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
name|ScheduledPollConsumer
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Scheduled Polling Consumer"
argument_list|)
DECL|class|ManagedScheduledPollConsumer
specifier|public
class|class
name|ManagedScheduledPollConsumer
extends|extends
name|ManagedConsumer
implements|implements
name|ManagedSchedulePollConsumerMBean
block|{
DECL|field|consumer
specifier|private
specifier|final
name|ScheduledPollConsumer
name|consumer
decl_stmt|;
DECL|method|ManagedScheduledPollConsumer (CamelContext context, ScheduledPollConsumer consumer)
specifier|public
name|ManagedScheduledPollConsumer
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ScheduledPollConsumer
name|consumer
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
DECL|method|getConsumer ()
specifier|public
name|ScheduledPollConsumer
name|getConsumer
parameter_list|()
block|{
return|return
name|consumer
return|;
block|}
DECL|method|getDelay ()
specifier|public
name|long
name|getDelay
parameter_list|()
block|{
return|return
name|getConsumer
argument_list|()
operator|.
name|getDelay
argument_list|()
return|;
block|}
DECL|method|setDelay (long delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|getConsumer
argument_list|()
operator|.
name|setDelay
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
DECL|method|getInitialDelay ()
specifier|public
name|long
name|getInitialDelay
parameter_list|()
block|{
return|return
name|getConsumer
argument_list|()
operator|.
name|getInitialDelay
argument_list|()
return|;
block|}
DECL|method|setInitialDelay (long initialDelay)
specifier|public
name|void
name|setInitialDelay
parameter_list|(
name|long
name|initialDelay
parameter_list|)
block|{
name|getConsumer
argument_list|()
operator|.
name|setInitialDelay
argument_list|(
name|initialDelay
argument_list|)
expr_stmt|;
block|}
DECL|method|isUseFixedDelay ()
specifier|public
name|boolean
name|isUseFixedDelay
parameter_list|()
block|{
return|return
name|getConsumer
argument_list|()
operator|.
name|isUseFixedDelay
argument_list|()
return|;
block|}
DECL|method|setUseFixedDelay (boolean useFixedDelay)
specifier|public
name|void
name|setUseFixedDelay
parameter_list|(
name|boolean
name|useFixedDelay
parameter_list|)
block|{
name|getConsumer
argument_list|()
operator|.
name|setUseFixedDelay
argument_list|(
name|useFixedDelay
argument_list|)
expr_stmt|;
block|}
DECL|method|getTimeUnit ()
specifier|public
name|String
name|getTimeUnit
parameter_list|()
block|{
return|return
name|getConsumer
argument_list|()
operator|.
name|getTimeUnit
argument_list|()
operator|.
name|name
argument_list|()
return|;
block|}
DECL|method|setTimeUnit (String timeUnit)
specifier|public
name|void
name|setTimeUnit
parameter_list|(
name|String
name|timeUnit
parameter_list|)
block|{
name|getConsumer
argument_list|()
operator|.
name|setTimeUnit
argument_list|(
name|TimeUnit
operator|.
name|valueOf
argument_list|(
name|timeUnit
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|isSchedulerStarted ()
specifier|public
name|boolean
name|isSchedulerStarted
parameter_list|()
block|{
return|return
name|getConsumer
argument_list|()
operator|.
name|isSchedulerStarted
argument_list|()
return|;
block|}
DECL|method|startScheduler ()
specifier|public
name|void
name|startScheduler
parameter_list|()
block|{
name|getConsumer
argument_list|()
operator|.
name|startScheduler
argument_list|()
expr_stmt|;
block|}
DECL|method|getSchedulerClassName ()
specifier|public
name|String
name|getSchedulerClassName
parameter_list|()
block|{
return|return
name|getConsumer
argument_list|()
operator|.
name|getScheduler
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
DECL|method|getBackoffMultiplier ()
specifier|public
name|int
name|getBackoffMultiplier
parameter_list|()
block|{
return|return
name|getConsumer
argument_list|()
operator|.
name|getBackoffMultiplier
argument_list|()
return|;
block|}
DECL|method|getBackoffIdleThreshold ()
specifier|public
name|int
name|getBackoffIdleThreshold
parameter_list|()
block|{
return|return
name|getConsumer
argument_list|()
operator|.
name|getBackoffIdleThreshold
argument_list|()
return|;
block|}
DECL|method|getBackoffErrorThreshold ()
specifier|public
name|int
name|getBackoffErrorThreshold
parameter_list|()
block|{
return|return
name|getConsumer
argument_list|()
operator|.
name|getBackoffErrorThreshold
argument_list|()
return|;
block|}
DECL|method|getBackoffCounter ()
specifier|public
name|int
name|getBackoffCounter
parameter_list|()
block|{
return|return
name|getConsumer
argument_list|()
operator|.
name|getBackoffCounter
argument_list|()
return|;
block|}
block|}
end_class

end_unit

