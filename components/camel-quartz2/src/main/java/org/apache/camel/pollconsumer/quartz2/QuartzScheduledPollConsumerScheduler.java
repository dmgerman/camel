begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.pollconsumer.quartz2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|pollconsumer
operator|.
name|quartz2
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
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
name|NonManagedService
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
name|Route
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
name|RuntimeCamelException
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
name|component
operator|.
name|quartz2
operator|.
name|QuartzComponent
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
name|component
operator|.
name|quartz2
operator|.
name|QuartzConstants
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
name|component
operator|.
name|quartz2
operator|.
name|QuartzHelper
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
name|ScheduledPollConsumerScheduler
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
name|service
operator|.
name|ServiceSupport
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
name|StringHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|CronScheduleBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|CronTrigger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobDataMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobDetail
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|ObjectAlreadyExistsException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|Scheduler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|SchedulerException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|SimpleTrigger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|Trigger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|TriggerBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|TriggerKey
import|;
end_import

begin_comment
comment|/**  * A quartz based {@link ScheduledPollConsumerScheduler} which uses a  * {@link CronTrigger} to define when the poll should be triggered.  */
end_comment

begin_class
DECL|class|QuartzScheduledPollConsumerScheduler
specifier|public
class|class
name|QuartzScheduledPollConsumerScheduler
extends|extends
name|ServiceSupport
implements|implements
name|ScheduledPollConsumerScheduler
implements|,
name|NonManagedService
block|{
DECL|field|quartzScheduler
specifier|private
name|Scheduler
name|quartzScheduler
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|routeId
specifier|private
name|String
name|routeId
decl_stmt|;
DECL|field|consumer
specifier|private
name|Consumer
name|consumer
decl_stmt|;
DECL|field|runnable
specifier|private
name|Runnable
name|runnable
decl_stmt|;
DECL|field|cron
specifier|private
name|String
name|cron
decl_stmt|;
DECL|field|triggerId
specifier|private
name|String
name|triggerId
decl_stmt|;
DECL|field|triggerGroup
specifier|private
name|String
name|triggerGroup
init|=
literal|"QuartzScheduledPollConsumerScheduler"
decl_stmt|;
DECL|field|timeZone
specifier|private
name|TimeZone
name|timeZone
init|=
name|TimeZone
operator|.
name|getDefault
argument_list|()
decl_stmt|;
DECL|field|deleteJob
specifier|private
name|boolean
name|deleteJob
init|=
literal|true
decl_stmt|;
DECL|field|trigger
specifier|private
specifier|volatile
name|CronTrigger
name|trigger
decl_stmt|;
DECL|field|job
specifier|private
specifier|volatile
name|JobDetail
name|job
decl_stmt|;
annotation|@
name|Override
DECL|method|onInit (Consumer consumer)
specifier|public
name|void
name|onInit
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
comment|// find the route of the consumer
for|for
control|(
name|Route
name|route
range|:
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRoutes
argument_list|()
control|)
block|{
if|if
condition|(
name|route
operator|.
name|getConsumer
argument_list|()
operator|==
name|consumer
condition|)
block|{
name|this
operator|.
name|routeId
operator|=
name|route
operator|.
name|getId
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|scheduleTask (Runnable runnable)
specifier|public
name|void
name|scheduleTask
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
name|this
operator|.
name|runnable
operator|=
name|runnable
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unscheduleTask ()
specifier|public
name|void
name|unscheduleTask
parameter_list|()
block|{
if|if
condition|(
name|trigger
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Unscheduling trigger: {}"
argument_list|,
name|trigger
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|quartzScheduler
operator|.
name|unscheduleJob
argument_list|(
name|trigger
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SchedulerException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|startScheduler ()
specifier|public
name|void
name|startScheduler
parameter_list|()
block|{
comment|// the quartz component starts the scheduler
block|}
annotation|@
name|Override
DECL|method|isSchedulerStarted ()
specifier|public
name|boolean
name|isSchedulerStarted
parameter_list|()
block|{
try|try
block|{
return|return
name|quartzScheduler
operator|!=
literal|null
operator|&&
name|quartzScheduler
operator|.
name|isStarted
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|SchedulerException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|getQuartzScheduler ()
specifier|public
name|Scheduler
name|getQuartzScheduler
parameter_list|()
block|{
return|return
name|quartzScheduler
return|;
block|}
DECL|method|setQuartzScheduler (Scheduler scheduler)
specifier|public
name|void
name|setQuartzScheduler
parameter_list|(
name|Scheduler
name|scheduler
parameter_list|)
block|{
name|this
operator|.
name|quartzScheduler
operator|=
name|scheduler
expr_stmt|;
block|}
DECL|method|getCron ()
specifier|public
name|String
name|getCron
parameter_list|()
block|{
return|return
name|cron
return|;
block|}
DECL|method|setCron (String cron)
specifier|public
name|void
name|setCron
parameter_list|(
name|String
name|cron
parameter_list|)
block|{
name|this
operator|.
name|cron
operator|=
name|cron
expr_stmt|;
block|}
DECL|method|getTimeZone ()
specifier|public
name|TimeZone
name|getTimeZone
parameter_list|()
block|{
return|return
name|timeZone
return|;
block|}
DECL|method|setTimeZone (TimeZone timeZone)
specifier|public
name|void
name|setTimeZone
parameter_list|(
name|TimeZone
name|timeZone
parameter_list|)
block|{
name|this
operator|.
name|timeZone
operator|=
name|timeZone
expr_stmt|;
block|}
DECL|method|getTriggerId ()
specifier|public
name|String
name|getTriggerId
parameter_list|()
block|{
return|return
name|triggerId
return|;
block|}
DECL|method|setTriggerId (String triggerId)
specifier|public
name|void
name|setTriggerId
parameter_list|(
name|String
name|triggerId
parameter_list|)
block|{
name|this
operator|.
name|triggerId
operator|=
name|triggerId
expr_stmt|;
block|}
DECL|method|getTriggerGroup ()
specifier|public
name|String
name|getTriggerGroup
parameter_list|()
block|{
return|return
name|triggerGroup
return|;
block|}
DECL|method|setTriggerGroup (String triggerGroup)
specifier|public
name|void
name|setTriggerGroup
parameter_list|(
name|String
name|triggerGroup
parameter_list|)
block|{
name|this
operator|.
name|triggerGroup
operator|=
name|triggerGroup
expr_stmt|;
block|}
DECL|method|isDeleteJob ()
specifier|public
name|boolean
name|isDeleteJob
parameter_list|()
block|{
return|return
name|deleteJob
return|;
block|}
DECL|method|setDeleteJob (boolean deleteJob)
specifier|public
name|void
name|setDeleteJob
parameter_list|(
name|boolean
name|deleteJob
parameter_list|)
block|{
name|this
operator|.
name|deleteJob
operator|=
name|deleteJob
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|cron
argument_list|,
literal|"cron"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|quartzScheduler
operator|==
literal|null
condition|)
block|{
comment|// get the scheduler form the quartz component
name|QuartzComponent
name|quartz
init|=
name|getCamelContext
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"quartz2"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|setQuartzScheduler
argument_list|(
name|quartz
operator|.
name|getScheduler
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|id
init|=
name|triggerId
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|id
operator|=
literal|"trigger-"
operator|+
name|getCamelContext
argument_list|()
operator|.
name|getUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
expr_stmt|;
block|}
name|CronTrigger
name|existingTrigger
init|=
literal|null
decl_stmt|;
name|TriggerKey
name|triggerKey
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|triggerId
operator|!=
literal|null
operator|&&
name|triggerGroup
operator|!=
literal|null
condition|)
block|{
name|triggerKey
operator|=
operator|new
name|TriggerKey
argument_list|(
name|triggerId
argument_list|,
name|triggerGroup
argument_list|)
expr_stmt|;
name|existingTrigger
operator|=
operator|(
name|CronTrigger
operator|)
name|quartzScheduler
operator|.
name|getTrigger
argument_list|(
name|triggerKey
argument_list|)
expr_stmt|;
block|}
comment|// Is an trigger already exist for this triggerId ?
if|if
condition|(
name|existingTrigger
operator|==
literal|null
condition|)
block|{
name|JobDataMap
name|map
init|=
operator|new
name|JobDataMap
argument_list|()
decl_stmt|;
comment|// do not store task as its not serializable, if we have route id
if|if
condition|(
name|routeId
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
literal|"routeId"
argument_list|,
name|routeId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|map
operator|.
name|put
argument_list|(
literal|"task"
argument_list|,
name|runnable
argument_list|)
expr_stmt|;
block|}
name|map
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_TRIGGER_TYPE
argument_list|,
literal|"cron"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_TRIGGER_CRON_EXPRESSION
argument_list|,
name|getCron
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_TRIGGER_CRON_TIMEZONE
argument_list|,
name|getTimeZone
argument_list|()
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
name|job
operator|=
name|JobBuilder
operator|.
name|newJob
argument_list|(
name|QuartzScheduledPollConsumerJob
operator|.
name|class
argument_list|)
operator|.
name|usingJobData
argument_list|(
name|map
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
comment|// store additional information on job such as camel context etc
name|QuartzHelper
operator|.
name|updateJobDataMap
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|job
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|trigger
operator|=
name|TriggerBuilder
operator|.
name|newTrigger
argument_list|()
operator|.
name|withIdentity
argument_list|(
name|id
argument_list|,
name|triggerGroup
argument_list|)
operator|.
name|withSchedule
argument_list|(
name|CronScheduleBuilder
operator|.
name|cronSchedule
argument_list|(
name|getCron
argument_list|()
argument_list|)
operator|.
name|inTimeZone
argument_list|(
name|getTimeZone
argument_list|()
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Scheduling job: {} with trigger: {}"
argument_list|,
name|job
argument_list|,
name|trigger
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|quartzScheduler
operator|.
name|scheduleJob
argument_list|(
name|job
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|checkTriggerIsNonConflicting
argument_list|(
name|existingTrigger
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Trigger with key {} is already present in scheduler. Only updating it."
argument_list|,
name|triggerKey
argument_list|)
expr_stmt|;
name|job
operator|=
name|quartzScheduler
operator|.
name|getJobDetail
argument_list|(
name|existingTrigger
operator|.
name|getJobKey
argument_list|()
argument_list|)
expr_stmt|;
name|JobDataMap
name|jobData
init|=
name|job
operator|.
name|getJobDataMap
argument_list|()
decl_stmt|;
name|jobData
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_TRIGGER_CRON_EXPRESSION
argument_list|,
name|getCron
argument_list|()
argument_list|)
expr_stmt|;
name|jobData
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_TRIGGER_CRON_TIMEZONE
argument_list|,
name|getTimeZone
argument_list|()
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
comment|// store additional information on job such as camel context etc
name|QuartzHelper
operator|.
name|updateJobDataMap
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|job
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Updated jobData map to {}"
argument_list|,
name|jobData
argument_list|)
expr_stmt|;
name|trigger
operator|=
name|existingTrigger
operator|.
name|getTriggerBuilder
argument_list|()
operator|.
name|withSchedule
argument_list|(
name|CronScheduleBuilder
operator|.
name|cronSchedule
argument_list|(
name|getCron
argument_list|()
argument_list|)
operator|.
name|inTimeZone
argument_list|(
name|getTimeZone
argument_list|()
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
comment|// Reschedule job if trigger settings were changed
if|if
condition|(
name|hasTriggerChanged
argument_list|(
name|existingTrigger
argument_list|,
name|trigger
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Re-scheduling job: {} with trigger: {}"
argument_list|,
name|job
argument_list|,
name|trigger
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|quartzScheduler
operator|.
name|rescheduleJob
argument_list|(
name|triggerKey
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Schedule it now. Remember that scheduler might not be started it, but we can schedule now.
name|log
operator|.
name|debug
argument_list|(
literal|"Scheduling job: {} with trigger: {}"
argument_list|,
name|job
argument_list|,
name|trigger
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
comment|// Schedule it now. Remember that scheduler might not be started it, but we can schedule now.
name|quartzScheduler
operator|.
name|scheduleJob
argument_list|(
name|job
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ObjectAlreadyExistsException
name|ex
parameter_list|)
block|{
comment|// some other VM might may have stored the job& trigger in DB in clustered mode, in the mean time
name|QuartzComponent
name|quartz
init|=
name|getCamelContext
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"quartz2"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|quartz
operator|.
name|isClustered
argument_list|()
operator|)
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
else|else
block|{
name|trigger
operator|=
operator|(
name|CronTrigger
operator|)
name|quartzScheduler
operator|.
name|getTrigger
argument_list|(
name|triggerKey
argument_list|)
expr_stmt|;
if|if
condition|(
name|trigger
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SchedulerException
argument_list|(
literal|"Trigger could not be found in quartz scheduler."
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
if|if
condition|(
name|log
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Job {} (triggerType={}, jobClass={}) is scheduled. Next fire date is {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|trigger
operator|.
name|getKey
argument_list|()
block|,
name|trigger
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
block|,
name|job
operator|.
name|getJobClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
block|,
name|trigger
operator|.
name|getNextFireTime
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|trigger
operator|!=
literal|null
operator|&&
name|deleteJob
condition|)
block|{
name|boolean
name|isClustered
init|=
name|quartzScheduler
operator|.
name|getMetaData
argument_list|()
operator|.
name|isJobStoreClustered
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|quartzScheduler
operator|.
name|isShutdown
argument_list|()
operator|&&
operator|!
name|isClustered
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Deleting job {}"
argument_list|,
name|trigger
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|quartzScheduler
operator|.
name|unscheduleJob
argument_list|(
name|trigger
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{     }
DECL|method|checkTriggerIsNonConflicting (Trigger trigger)
specifier|private
name|void
name|checkTriggerIsNonConflicting
parameter_list|(
name|Trigger
name|trigger
parameter_list|)
block|{
name|JobDataMap
name|jobDataMap
init|=
name|trigger
operator|.
name|getJobDataMap
argument_list|()
decl_stmt|;
name|String
name|routeIdFromTrigger
init|=
name|jobDataMap
operator|.
name|getString
argument_list|(
literal|"routeId"
argument_list|)
decl_stmt|;
if|if
condition|(
name|routeIdFromTrigger
operator|!=
literal|null
operator|&&
operator|!
name|routeIdFromTrigger
operator|.
name|equals
argument_list|(
name|routeId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Trigger key "
operator|+
name|trigger
operator|.
name|getKey
argument_list|()
operator|+
literal|" is already used by route: "
operator|+
name|routeIdFromTrigger
operator|+
literal|". Cannot re-use it for another route: "
operator|+
name|routeId
argument_list|)
throw|;
block|}
block|}
DECL|method|hasTriggerChanged (Trigger oldTrigger, Trigger newTrigger)
specifier|private
name|boolean
name|hasTriggerChanged
parameter_list|(
name|Trigger
name|oldTrigger
parameter_list|,
name|Trigger
name|newTrigger
parameter_list|)
block|{
if|if
condition|(
name|newTrigger
operator|instanceof
name|CronTrigger
operator|&&
name|oldTrigger
operator|instanceof
name|CronTrigger
condition|)
block|{
name|CronTrigger
name|newCron
init|=
operator|(
name|CronTrigger
operator|)
name|newTrigger
decl_stmt|;
name|CronTrigger
name|oldCron
init|=
operator|(
name|CronTrigger
operator|)
name|oldTrigger
decl_stmt|;
return|return
operator|!
name|newCron
operator|.
name|getCronExpression
argument_list|()
operator|.
name|equals
argument_list|(
name|oldCron
operator|.
name|getCronExpression
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|newTrigger
operator|instanceof
name|SimpleTrigger
operator|&&
name|oldTrigger
operator|instanceof
name|SimpleTrigger
condition|)
block|{
name|SimpleTrigger
name|newSimple
init|=
operator|(
name|SimpleTrigger
operator|)
name|newTrigger
decl_stmt|;
name|SimpleTrigger
name|oldSimple
init|=
operator|(
name|SimpleTrigger
operator|)
name|oldTrigger
decl_stmt|;
return|return
name|newSimple
operator|.
name|getRepeatInterval
argument_list|()
operator|!=
name|oldSimple
operator|.
name|getRepeatInterval
argument_list|()
operator|||
name|newSimple
operator|.
name|getRepeatCount
argument_list|()
operator|!=
name|oldSimple
operator|.
name|getRepeatCount
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|!
name|newTrigger
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|oldTrigger
operator|.
name|getClass
argument_list|()
argument_list|)
operator|||
operator|!
name|newTrigger
operator|.
name|equals
argument_list|(
name|oldTrigger
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

