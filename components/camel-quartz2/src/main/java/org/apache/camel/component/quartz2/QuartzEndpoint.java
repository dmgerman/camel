begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quartz2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|AtomicInteger
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
name|Producer
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
name|impl
operator|.
name|DefaultEndpoint
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
name|loadbalancer
operator|.
name|LoadBalancer
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
name|loadbalancer
operator|.
name|RoundRobinLoadBalancer
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
name|UriEndpoint
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
name|UriParam
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
name|EndpointHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|Job
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

begin_import
import|import static
name|org
operator|.
name|quartz
operator|.
name|CronScheduleBuilder
operator|.
name|cronSchedule
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|quartz
operator|.
name|SimpleScheduleBuilder
operator|.
name|simpleSchedule
import|;
end_import

begin_comment
comment|/**  * This endpoint represent each job to be created in scheduler. When consumer is started or stopped, it will  * call back into {@link #onConsumerStart(QuartzConsumer)} to add/resume or {@link #onConsumerStop(QuartzConsumer)}  * to pause the scheduler trigger.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"quartz2"
argument_list|,
name|consumerClass
operator|=
name|QuartzComponent
operator|.
name|class
argument_list|)
DECL|class|QuartzEndpoint
specifier|public
class|class
name|QuartzEndpoint
extends|extends
name|DefaultEndpoint
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
name|QuartzEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|triggerKey
specifier|private
name|TriggerKey
name|triggerKey
decl_stmt|;
annotation|@
name|UriParam
DECL|field|cron
specifier|private
name|String
name|cron
decl_stmt|;
DECL|field|consumerLoadBalancer
specifier|private
name|LoadBalancer
name|consumerLoadBalancer
decl_stmt|;
DECL|field|triggerParameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|triggerParameters
decl_stmt|;
DECL|field|jobParameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jobParameters
decl_stmt|;
annotation|@
name|UriParam
DECL|field|stateful
specifier|private
name|boolean
name|stateful
decl_stmt|;
annotation|@
name|UriParam
DECL|field|fireNow
specifier|private
name|boolean
name|fireNow
decl_stmt|;
annotation|@
name|UriParam
DECL|field|deleteJob
specifier|private
name|boolean
name|deleteJob
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|pauseJob
specifier|private
name|boolean
name|pauseJob
decl_stmt|;
annotation|@
name|UriParam
DECL|field|durableJob
specifier|private
name|boolean
name|durableJob
decl_stmt|;
annotation|@
name|UriParam
DECL|field|recoverableJob
specifier|private
name|boolean
name|recoverableJob
decl_stmt|;
comment|/** In case of scheduler has already started, we want the trigger start slightly after current time to      * ensure endpoint is fully started before the job kicks in. */
annotation|@
name|UriParam
DECL|field|triggerStartDelay
specifier|private
name|long
name|triggerStartDelay
init|=
literal|500
decl_stmt|;
comment|// in millis second
comment|// An internal variables to track whether a job has been in scheduler or not, and has it paused or not.
DECL|field|jobAdded
specifier|private
specifier|final
name|AtomicBoolean
name|jobAdded
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|jobPaused
specifier|private
specifier|final
name|AtomicBoolean
name|jobPaused
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|method|QuartzEndpoint (String uri, QuartzComponent quartzComponent)
specifier|public
name|QuartzEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|QuartzComponent
name|quartzComponent
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|quartzComponent
argument_list|)
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
DECL|method|isStateful ()
specifier|public
name|boolean
name|isStateful
parameter_list|()
block|{
return|return
name|stateful
return|;
block|}
DECL|method|isFireNow ()
specifier|public
name|boolean
name|isFireNow
parameter_list|()
block|{
return|return
name|fireNow
return|;
block|}
DECL|method|getTriggerStartDelay ()
specifier|public
name|long
name|getTriggerStartDelay
parameter_list|()
block|{
return|return
name|triggerStartDelay
return|;
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
DECL|method|isPauseJob ()
specifier|public
name|boolean
name|isPauseJob
parameter_list|()
block|{
return|return
name|pauseJob
return|;
block|}
DECL|method|setPauseJob (boolean pauseJob)
specifier|public
name|void
name|setPauseJob
parameter_list|(
name|boolean
name|pauseJob
parameter_list|)
block|{
name|this
operator|.
name|pauseJob
operator|=
name|pauseJob
expr_stmt|;
block|}
DECL|method|setTriggerStartDelay (long triggerStartDelay)
specifier|public
name|void
name|setTriggerStartDelay
parameter_list|(
name|long
name|triggerStartDelay
parameter_list|)
block|{
name|this
operator|.
name|triggerStartDelay
operator|=
name|triggerStartDelay
expr_stmt|;
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
DECL|method|setFireNow (boolean fireNow)
specifier|public
name|void
name|setFireNow
parameter_list|(
name|boolean
name|fireNow
parameter_list|)
block|{
name|this
operator|.
name|fireNow
operator|=
name|fireNow
expr_stmt|;
block|}
DECL|method|setStateful (boolean stateful)
specifier|public
name|void
name|setStateful
parameter_list|(
name|boolean
name|stateful
parameter_list|)
block|{
name|this
operator|.
name|stateful
operator|=
name|stateful
expr_stmt|;
block|}
DECL|method|isDurableJob ()
specifier|public
name|boolean
name|isDurableJob
parameter_list|()
block|{
return|return
name|durableJob
return|;
block|}
DECL|method|setDurableJob (boolean durableJob)
specifier|public
name|void
name|setDurableJob
parameter_list|(
name|boolean
name|durableJob
parameter_list|)
block|{
name|this
operator|.
name|durableJob
operator|=
name|durableJob
expr_stmt|;
block|}
DECL|method|isRecoverableJob ()
specifier|public
name|boolean
name|isRecoverableJob
parameter_list|()
block|{
return|return
name|recoverableJob
return|;
block|}
DECL|method|setRecoverableJob (boolean recoverableJob)
specifier|public
name|void
name|setRecoverableJob
parameter_list|(
name|boolean
name|recoverableJob
parameter_list|)
block|{
name|this
operator|.
name|recoverableJob
operator|=
name|recoverableJob
expr_stmt|;
block|}
DECL|method|setTriggerParameters (Map<String, Object> triggerParameters)
specifier|public
name|void
name|setTriggerParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|triggerParameters
parameter_list|)
block|{
name|this
operator|.
name|triggerParameters
operator|=
name|triggerParameters
expr_stmt|;
block|}
DECL|method|setJobParameters (Map<String, Object> jobParameters)
specifier|public
name|void
name|setJobParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jobParameters
parameter_list|)
block|{
name|this
operator|.
name|jobParameters
operator|=
name|jobParameters
expr_stmt|;
block|}
DECL|method|getConsumerLoadBalancer ()
specifier|public
name|LoadBalancer
name|getConsumerLoadBalancer
parameter_list|()
block|{
if|if
condition|(
name|consumerLoadBalancer
operator|==
literal|null
condition|)
block|{
name|consumerLoadBalancer
operator|=
operator|new
name|RoundRobinLoadBalancer
argument_list|()
expr_stmt|;
block|}
return|return
name|consumerLoadBalancer
return|;
block|}
DECL|method|setConsumerLoadBalancer (LoadBalancer consumerLoadBalancer)
specifier|public
name|void
name|setConsumerLoadBalancer
parameter_list|(
name|LoadBalancer
name|consumerLoadBalancer
parameter_list|)
block|{
name|this
operator|.
name|consumerLoadBalancer
operator|=
name|consumerLoadBalancer
expr_stmt|;
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
DECL|method|getTriggerKey ()
specifier|public
name|TriggerKey
name|getTriggerKey
parameter_list|()
block|{
return|return
name|triggerKey
return|;
block|}
DECL|method|setTriggerKey (TriggerKey triggerKey)
specifier|public
name|void
name|setTriggerKey
parameter_list|(
name|TriggerKey
name|triggerKey
parameter_list|)
block|{
name|this
operator|.
name|triggerKey
operator|=
name|triggerKey
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Quartz producer is not supported."
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|QuartzConsumer
name|result
init|=
operator|new
name|QuartzConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
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
if|if
condition|(
name|isDeleteJob
argument_list|()
operator|&&
name|isPauseJob
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot have both options deleteJob and pauseJob enabled"
argument_list|)
throw|;
block|}
name|addJobInScheduler
argument_list|()
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
name|removeJobInScheduler
argument_list|()
expr_stmt|;
block|}
DECL|method|removeJobInScheduler ()
specifier|private
name|void
name|removeJobInScheduler
parameter_list|()
throws|throws
name|Exception
block|{
name|Scheduler
name|scheduler
init|=
name|getComponent
argument_list|()
operator|.
name|getScheduler
argument_list|()
decl_stmt|;
if|if
condition|(
name|scheduler
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|deleteJob
condition|)
block|{
name|boolean
name|isClustered
init|=
name|scheduler
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
name|scheduler
operator|.
name|isShutdown
argument_list|()
operator|&&
operator|!
name|isClustered
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Deleting job {}"
argument_list|,
name|triggerKey
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|unscheduleJob
argument_list|(
name|triggerKey
argument_list|)
expr_stmt|;
name|jobAdded
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|pauseJob
condition|)
block|{
name|pauseTrigger
argument_list|()
expr_stmt|;
block|}
comment|// Decrement camel job count for this endpoint
name|AtomicInteger
name|number
init|=
operator|(
name|AtomicInteger
operator|)
name|scheduler
operator|.
name|getContext
argument_list|()
operator|.
name|get
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_CAMEL_JOBS_COUNT
argument_list|)
decl_stmt|;
if|if
condition|(
name|number
operator|!=
literal|null
condition|)
block|{
name|number
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|addJobInScheduler ()
specifier|private
name|void
name|addJobInScheduler
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Add or use existing trigger to/from scheduler
name|Scheduler
name|scheduler
init|=
name|getComponent
argument_list|()
operator|.
name|getScheduler
argument_list|()
decl_stmt|;
name|JobDetail
name|jobDetail
decl_stmt|;
name|Trigger
name|trigger
init|=
name|scheduler
operator|.
name|getTrigger
argument_list|(
name|triggerKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|trigger
operator|==
literal|null
condition|)
block|{
name|jobDetail
operator|=
name|createJobDetail
argument_list|()
expr_stmt|;
name|trigger
operator|=
name|createTrigger
argument_list|(
name|jobDetail
argument_list|)
expr_stmt|;
name|QuartzHelper
operator|.
name|updateJobDataMap
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|jobDetail
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
comment|// Schedule it now. Remember that scheduler might not be started it, but we can schedule now.
name|Date
name|nextFireDate
init|=
name|scheduler
operator|.
name|scheduleJob
argument_list|(
name|jobDetail
argument_list|,
name|trigger
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|LOG
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
name|jobDetail
operator|.
name|getJobClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
block|,
name|nextFireDate
block|}
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|ensureNoDupTriggerKey
argument_list|()
expr_stmt|;
block|}
comment|// Increase camel job count for this endpoint
name|AtomicInteger
name|number
init|=
operator|(
name|AtomicInteger
operator|)
name|scheduler
operator|.
name|getContext
argument_list|()
operator|.
name|get
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_CAMEL_JOBS_COUNT
argument_list|)
decl_stmt|;
if|if
condition|(
name|number
operator|!=
literal|null
condition|)
block|{
name|number
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
name|jobAdded
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|ensureNoDupTriggerKey ()
specifier|private
name|void
name|ensureNoDupTriggerKey
parameter_list|()
block|{
for|for
control|(
name|Route
name|route
range|:
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
name|getEndpoint
argument_list|()
operator|instanceof
name|QuartzEndpoint
condition|)
block|{
name|QuartzEndpoint
name|quartzEndpoint
init|=
operator|(
name|QuartzEndpoint
operator|)
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|TriggerKey
name|checkTriggerKey
init|=
name|quartzEndpoint
operator|.
name|getTriggerKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|triggerKey
operator|.
name|equals
argument_list|(
name|checkTriggerKey
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Trigger key "
operator|+
name|triggerKey
operator|+
literal|" is already in use by "
operator|+
name|quartzEndpoint
argument_list|)
throw|;
block|}
block|}
block|}
block|}
DECL|method|createTrigger (JobDetail jobDetail)
specifier|private
name|Trigger
name|createTrigger
parameter_list|(
name|JobDetail
name|jobDetail
parameter_list|)
throws|throws
name|Exception
block|{
name|Trigger
name|result
decl_stmt|;
name|Date
name|startTime
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
if|if
condition|(
name|getComponent
argument_list|()
operator|.
name|getScheduler
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
name|startTime
operator|=
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
name|triggerStartDelay
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cron
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating CronTrigger: {}"
argument_list|,
name|cron
argument_list|)
expr_stmt|;
name|String
name|timeZone
init|=
operator|(
name|String
operator|)
name|triggerParameters
operator|.
name|get
argument_list|(
literal|"timeZone"
argument_list|)
decl_stmt|;
if|if
condition|(
name|timeZone
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|TriggerBuilder
operator|.
name|newTrigger
argument_list|()
operator|.
name|withIdentity
argument_list|(
name|triggerKey
argument_list|)
operator|.
name|startAt
argument_list|(
name|startTime
argument_list|)
operator|.
name|withSchedule
argument_list|(
name|cronSchedule
argument_list|(
name|cron
argument_list|)
operator|.
name|withMisfireHandlingInstructionFireAndProceed
argument_list|()
operator|.
name|inTimeZone
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
name|timeZone
argument_list|)
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|jobDetail
operator|.
name|getJobDataMap
argument_list|()
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_TRIGGER_CRON_TIMEZONE
argument_list|,
name|timeZone
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|TriggerBuilder
operator|.
name|newTrigger
argument_list|()
operator|.
name|withIdentity
argument_list|(
name|triggerKey
argument_list|)
operator|.
name|startAt
argument_list|(
name|startTime
argument_list|)
operator|.
name|withSchedule
argument_list|(
name|cronSchedule
argument_list|(
name|cron
argument_list|)
operator|.
name|withMisfireHandlingInstructionFireAndProceed
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
comment|// enrich job map with details
name|jobDetail
operator|.
name|getJobDataMap
argument_list|()
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
name|jobDetail
operator|.
name|getJobDataMap
argument_list|()
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_TRIGGER_CRON_EXPRESSION
argument_list|,
name|cron
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating SimpleTrigger."
argument_list|)
expr_stmt|;
name|int
name|repeat
init|=
name|SimpleTrigger
operator|.
name|REPEAT_INDEFINITELY
decl_stmt|;
name|String
name|repeatString
init|=
operator|(
name|String
operator|)
name|triggerParameters
operator|.
name|get
argument_list|(
literal|"repeatCount"
argument_list|)
decl_stmt|;
if|if
condition|(
name|repeatString
operator|!=
literal|null
condition|)
block|{
name|repeat
operator|=
name|EndpointHelper
operator|.
name|resolveParameter
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|repeatString
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// need to update the parameters
name|triggerParameters
operator|.
name|put
argument_list|(
literal|"repeatCount"
argument_list|,
name|repeat
argument_list|)
expr_stmt|;
block|}
comment|// default use 1 sec interval
name|long
name|interval
init|=
literal|1000
decl_stmt|;
name|String
name|intervalString
init|=
operator|(
name|String
operator|)
name|triggerParameters
operator|.
name|get
argument_list|(
literal|"repeatInterval"
argument_list|)
decl_stmt|;
if|if
condition|(
name|intervalString
operator|!=
literal|null
condition|)
block|{
name|interval
operator|=
name|EndpointHelper
operator|.
name|resolveParameter
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|intervalString
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// need to update the parameters
name|triggerParameters
operator|.
name|put
argument_list|(
literal|"repeatInterval"
argument_list|,
name|interval
argument_list|)
expr_stmt|;
block|}
name|TriggerBuilder
argument_list|<
name|SimpleTrigger
argument_list|>
name|triggerBuilder
init|=
name|TriggerBuilder
operator|.
name|newTrigger
argument_list|()
operator|.
name|withIdentity
argument_list|(
name|triggerKey
argument_list|)
operator|.
name|startAt
argument_list|(
name|startTime
argument_list|)
operator|.
name|withSchedule
argument_list|(
name|simpleSchedule
argument_list|()
operator|.
name|withMisfireHandlingInstructionFireNow
argument_list|()
operator|.
name|withRepeatCount
argument_list|(
name|repeat
argument_list|)
operator|.
name|withIntervalInMilliseconds
argument_list|(
name|interval
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|fireNow
condition|)
block|{
name|triggerBuilder
operator|=
name|triggerBuilder
operator|.
name|startNow
argument_list|()
expr_stmt|;
block|}
name|result
operator|=
name|triggerBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
comment|// enrich job map with details
name|jobDetail
operator|.
name|getJobDataMap
argument_list|()
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_TRIGGER_TYPE
argument_list|,
literal|"simple"
argument_list|)
expr_stmt|;
name|jobDetail
operator|.
name|getJobDataMap
argument_list|()
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_TRIGGER_SIMPLE_REPEAT_COUNTER
argument_list|,
name|repeat
argument_list|)
expr_stmt|;
name|jobDetail
operator|.
name|getJobDataMap
argument_list|()
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_TRIGGER_SIMPLE_REPEAT_INTERVAL
argument_list|,
name|interval
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|triggerParameters
operator|!=
literal|null
operator|&&
name|triggerParameters
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Setting user extra triggerParameters {}"
argument_list|,
name|triggerParameters
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|result
argument_list|,
name|triggerParameters
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created trigger={}"
argument_list|,
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|createJobDetail ()
specifier|private
name|JobDetail
name|createJobDetail
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Camel endpoint timer will assume one to one for JobDetail and Trigger, so let's use same name as trigger
name|String
name|name
init|=
name|triggerKey
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|group
init|=
name|triggerKey
operator|.
name|getGroup
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
extends|extends
name|Job
argument_list|>
name|jobClass
init|=
name|stateful
condition|?
name|StatefulCamelJob
operator|.
name|class
else|:
name|CamelJob
operator|.
name|class
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating new {}."
argument_list|,
name|jobClass
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|JobBuilder
name|builder
init|=
name|JobBuilder
operator|.
name|newJob
argument_list|(
name|jobClass
argument_list|)
operator|.
name|withIdentity
argument_list|(
name|name
argument_list|,
name|group
argument_list|)
decl_stmt|;
if|if
condition|(
name|durableJob
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|storeDurably
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|recoverableJob
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|requestRecovery
argument_list|()
expr_stmt|;
block|}
name|JobDetail
name|result
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
comment|// Let user parameters to further set JobDetail properties.
if|if
condition|(
name|jobParameters
operator|!=
literal|null
operator|&&
name|jobParameters
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Setting user extra jobParameters {}"
argument_list|,
name|jobParameters
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|result
argument_list|,
name|jobParameters
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created jobDetail={}"
argument_list|,
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|QuartzComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|QuartzComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|pauseTrigger ()
specifier|public
name|void
name|pauseTrigger
parameter_list|()
throws|throws
name|Exception
block|{
name|Scheduler
name|scheduler
init|=
name|getComponent
argument_list|()
operator|.
name|getScheduler
argument_list|()
decl_stmt|;
name|boolean
name|isClustered
init|=
name|scheduler
operator|.
name|getMetaData
argument_list|()
operator|.
name|isJobStoreClustered
argument_list|()
decl_stmt|;
if|if
condition|(
name|jobPaused
operator|.
name|get
argument_list|()
operator|||
name|isClustered
condition|)
block|{
return|return;
block|}
name|jobPaused
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|scheduler
operator|.
name|isShutdown
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Pausing trigger {}"
argument_list|,
name|triggerKey
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|pauseTrigger
argument_list|(
name|triggerKey
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|resumeTrigger ()
specifier|public
name|void
name|resumeTrigger
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|jobPaused
operator|.
name|get
argument_list|()
condition|)
block|{
return|return;
block|}
name|jobPaused
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Scheduler
name|scheduler
init|=
name|getComponent
argument_list|()
operator|.
name|getScheduler
argument_list|()
decl_stmt|;
if|if
condition|(
name|scheduler
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Resuming trigger {}"
argument_list|,
name|triggerKey
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|resumeTrigger
argument_list|(
name|triggerKey
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onConsumerStart (QuartzConsumer quartzConsumer)
specifier|public
name|void
name|onConsumerStart
parameter_list|(
name|QuartzConsumer
name|quartzConsumer
parameter_list|)
throws|throws
name|Exception
block|{
name|getConsumerLoadBalancer
argument_list|()
operator|.
name|addProcessor
argument_list|(
name|quartzConsumer
operator|.
name|getProcessor
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|jobAdded
operator|.
name|get
argument_list|()
condition|)
block|{
name|addJobInScheduler
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|resumeTrigger
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|onConsumerStop (QuartzConsumer quartzConsumer)
specifier|public
name|void
name|onConsumerStop
parameter_list|(
name|QuartzConsumer
name|quartzConsumer
parameter_list|)
throws|throws
name|Exception
block|{
name|getConsumerLoadBalancer
argument_list|()
operator|.
name|removeProcessor
argument_list|(
name|quartzConsumer
operator|.
name|getProcessor
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|jobAdded
operator|.
name|get
argument_list|()
condition|)
block|{
name|pauseTrigger
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

