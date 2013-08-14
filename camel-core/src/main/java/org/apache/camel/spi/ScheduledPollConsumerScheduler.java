begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
name|CamelContextAware
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
name|Consumer
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
name|ShutdownableService
import|;
end_import

begin_comment
comment|/**  * A pluggable scheduler for {@link org.apache.camel.impl.ScheduledPollConsumer} consumers.  *<p/>  * The default implementation {@link org.apache.camel.impl.DefaultScheduledPollConsumerScheduler} is  * using the {@link java.util.concurrent.ScheduledExecutorService} from the JDK to schedule and run the poll task.  *<p/>  * An alternative implementation is in<tt>camel-quartz</tt> component that allows to use CRON expression  * to define when the scheduler should run.  */
end_comment

begin_interface
DECL|interface|ScheduledPollConsumerScheduler
specifier|public
interface|interface
name|ScheduledPollConsumerScheduler
extends|extends
name|ShutdownableService
extends|,
name|CamelContextAware
block|{
comment|/**      * Schedules the task to run.      *      * @param consumer the consumer.      * @param task the task to run.      */
DECL|method|scheduleTask (Consumer consumer, Runnable task)
name|void
name|scheduleTask
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Runnable
name|task
parameter_list|)
function_decl|;
comment|/**      * Attempts to unschedules the last task which was scheduled.      *<p/>      * An implementation may not implement this method.      */
DECL|method|unscheduleTask ()
name|void
name|unscheduleTask
parameter_list|()
function_decl|;
comment|/**      * Starts the scheduler.      *<p/>      * If the scheduler is already started, then this is a noop method call.      */
DECL|method|startScheduler ()
name|void
name|startScheduler
parameter_list|()
function_decl|;
comment|/**      * Whether the scheduler has been started.      *      * @return<tt>true</tt> if started,<tt>false</tt> otherwise.      */
DECL|method|isSchedulerStarted ()
name|boolean
name|isSchedulerStarted
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

