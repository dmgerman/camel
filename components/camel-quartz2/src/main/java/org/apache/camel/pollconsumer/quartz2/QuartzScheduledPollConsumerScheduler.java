begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ObjectHelper
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
name|TriggerBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A quartz based {@link ScheduledPollConsumerScheduler} which uses a {@link CronTrigger} to define when the  * poll should be triggered.  */
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
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|QuartzScheduledPollConsumerScheduler
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|LOG
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
name|ObjectHelper
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
name|ObjectHelper
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
name|JobDataMap
name|map
init|=
operator|new
name|JobDataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"task"
argument_list|,
name|runnable
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
name|LOG
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
condition|)
block|{
name|LOG
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
block|}
end_class

end_unit

