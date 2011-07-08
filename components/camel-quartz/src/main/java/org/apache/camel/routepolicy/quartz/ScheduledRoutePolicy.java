begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.routepolicy.quartz
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|routepolicy
operator|.
name|quartz
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
name|ServiceStatus
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
name|RoutePolicySupport
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
name|Trigger
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

begin_class
DECL|class|ScheduledRoutePolicy
specifier|public
specifier|abstract
class|class
name|ScheduledRoutePolicy
extends|extends
name|RoutePolicySupport
implements|implements
name|ScheduledRoutePolicyConstants
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ScheduledRoutePolicy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|scheduledRouteDetails
specifier|protected
name|ScheduledRouteDetails
name|scheduledRouteDetails
decl_stmt|;
DECL|field|scheduler
specifier|private
name|Scheduler
name|scheduler
decl_stmt|;
DECL|field|routeStopGracePeriod
specifier|private
name|int
name|routeStopGracePeriod
decl_stmt|;
DECL|field|timeUnit
specifier|private
name|TimeUnit
name|timeUnit
decl_stmt|;
DECL|method|createTrigger (Action action, Route route)
specifier|protected
specifier|abstract
name|Trigger
name|createTrigger
parameter_list|(
name|Action
name|action
parameter_list|,
name|Route
name|route
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|onJobExecute (Action action, Route route)
specifier|protected
name|void
name|onJobExecute
parameter_list|(
name|Action
name|action
parameter_list|,
name|Route
name|route
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Scheduled Event notification received. Performing action: {} on route: {}"
argument_list|,
name|action
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|ServiceStatus
name|routeStatus
init|=
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|START
condition|)
block|{
if|if
condition|(
name|routeStatus
operator|==
name|ServiceStatus
operator|.
name|Stopped
condition|)
block|{
name|startRoute
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|routeStatus
operator|==
name|ServiceStatus
operator|.
name|Suspended
condition|)
block|{
name|startConsumer
argument_list|(
name|route
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|STOP
condition|)
block|{
if|if
condition|(
operator|(
name|routeStatus
operator|==
name|ServiceStatus
operator|.
name|Started
operator|)
operator|||
operator|(
name|routeStatus
operator|==
name|ServiceStatus
operator|.
name|Suspended
operator|)
condition|)
block|{
name|stopRoute
argument_list|(
name|route
argument_list|,
name|getRouteStopGracePeriod
argument_list|()
argument_list|,
name|getTimeUnit
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|SUSPEND
condition|)
block|{
if|if
condition|(
name|routeStatus
operator|==
name|ServiceStatus
operator|.
name|Started
condition|)
block|{
name|stopConsumer
argument_list|(
name|route
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Route is not in a started state and cannot be suspended. The current route state is {}"
argument_list|,
name|routeStatus
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|RESUME
condition|)
block|{
if|if
condition|(
name|routeStatus
operator|==
name|ServiceStatus
operator|.
name|Started
condition|)
block|{
name|startConsumer
argument_list|(
name|route
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Route is not in a started state and cannot be resumed. The current route state is {}"
argument_list|,
name|routeStatus
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|scheduleRoute (Action action)
specifier|public
name|void
name|scheduleRoute
parameter_list|(
name|Action
name|action
parameter_list|)
throws|throws
name|Exception
block|{
name|Route
name|route
init|=
name|scheduledRouteDetails
operator|.
name|getRoute
argument_list|()
decl_stmt|;
name|JobDetail
name|jobDetail
init|=
name|createJobDetail
argument_list|(
name|action
argument_list|,
name|route
argument_list|)
decl_stmt|;
name|Trigger
name|trigger
init|=
name|createTrigger
argument_list|(
name|action
argument_list|,
name|route
argument_list|)
decl_stmt|;
name|updateScheduledRouteDetails
argument_list|(
name|action
argument_list|,
name|jobDetail
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
name|loadCallbackDataIntoSchedulerContext
argument_list|(
name|jobDetail
argument_list|,
name|action
argument_list|,
name|route
argument_list|)
expr_stmt|;
name|getScheduler
argument_list|()
operator|.
name|scheduleJob
argument_list|(
name|jobDetail
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
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
literal|"Scheduled trigger: {} for action: {} on route: "
argument_list|,
operator|new
name|Object
index|[]
block|{
name|trigger
operator|.
name|getFullName
argument_list|()
block|,
name|action
block|,
name|route
operator|.
name|getId
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|pauseRouteTrigger (Action action)
specifier|public
name|void
name|pauseRouteTrigger
parameter_list|(
name|Action
name|action
parameter_list|)
throws|throws
name|SchedulerException
block|{
name|String
name|triggerName
init|=
name|retrieveTriggerName
argument_list|(
name|action
argument_list|)
decl_stmt|;
name|String
name|triggerGroup
init|=
name|retrieveTriggerGroup
argument_list|(
name|action
argument_list|)
decl_stmt|;
name|getScheduler
argument_list|()
operator|.
name|pauseTrigger
argument_list|(
name|triggerName
argument_list|,
name|triggerGroup
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Scheduled trigger: {}.{} is paused"
argument_list|,
name|triggerGroup
argument_list|,
name|triggerName
argument_list|)
expr_stmt|;
block|}
DECL|method|resumeRouteTrigger (Action action)
specifier|public
name|void
name|resumeRouteTrigger
parameter_list|(
name|Action
name|action
parameter_list|)
throws|throws
name|SchedulerException
block|{
name|String
name|triggerName
init|=
name|retrieveTriggerName
argument_list|(
name|action
argument_list|)
decl_stmt|;
name|String
name|triggerGroup
init|=
name|retrieveTriggerGroup
argument_list|(
name|action
argument_list|)
decl_stmt|;
name|getScheduler
argument_list|()
operator|.
name|resumeTrigger
argument_list|(
name|triggerName
argument_list|,
name|triggerGroup
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Scheduled trigger: {}.{} is resumed"
argument_list|,
name|triggerGroup
argument_list|,
name|triggerName
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteRouteJob (Action action)
specifier|public
name|void
name|deleteRouteJob
parameter_list|(
name|Action
name|action
parameter_list|)
throws|throws
name|SchedulerException
block|{
name|String
name|jobDetailName
init|=
name|retrieveJobDetailName
argument_list|(
name|action
argument_list|)
decl_stmt|;
name|String
name|jobDetailGroup
init|=
name|retrieveJobDetailGroup
argument_list|(
name|action
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|getScheduler
argument_list|()
operator|.
name|isShutdown
argument_list|()
condition|)
block|{
name|getScheduler
argument_list|()
operator|.
name|deleteJob
argument_list|(
name|jobDetailName
argument_list|,
name|jobDetailGroup
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Scheduled Job: {}.{} is deleted"
argument_list|,
name|jobDetailGroup
argument_list|,
name|jobDetailName
argument_list|)
expr_stmt|;
block|}
DECL|method|createJobDetail (Action action, Route route)
specifier|protected
name|JobDetail
name|createJobDetail
parameter_list|(
name|Action
name|action
parameter_list|,
name|Route
name|route
parameter_list|)
throws|throws
name|Exception
block|{
name|JobDetail
name|jobDetail
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|START
condition|)
block|{
name|jobDetail
operator|=
operator|new
name|JobDetail
argument_list|(
name|JOB_START
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|JOB_GROUP
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|ScheduledJob
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|STOP
condition|)
block|{
name|jobDetail
operator|=
operator|new
name|JobDetail
argument_list|(
name|JOB_STOP
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|JOB_GROUP
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|ScheduledJob
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|SUSPEND
condition|)
block|{
name|jobDetail
operator|=
operator|new
name|JobDetail
argument_list|(
name|JOB_SUSPEND
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|JOB_GROUP
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|ScheduledJob
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|RESUME
condition|)
block|{
name|jobDetail
operator|=
operator|new
name|JobDetail
argument_list|(
name|JOB_RESUME
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|JOB_GROUP
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|ScheduledJob
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|jobDetail
return|;
block|}
DECL|method|updateScheduledRouteDetails (Action action, JobDetail jobDetail, Trigger trigger)
specifier|protected
name|void
name|updateScheduledRouteDetails
parameter_list|(
name|Action
name|action
parameter_list|,
name|JobDetail
name|jobDetail
parameter_list|,
name|Trigger
name|trigger
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|START
condition|)
block|{
name|scheduledRouteDetails
operator|.
name|setStartJobDetail
argument_list|(
name|jobDetail
argument_list|)
expr_stmt|;
name|scheduledRouteDetails
operator|.
name|setStartTrigger
argument_list|(
name|trigger
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|STOP
condition|)
block|{
name|scheduledRouteDetails
operator|.
name|setStopJobDetail
argument_list|(
name|jobDetail
argument_list|)
expr_stmt|;
name|scheduledRouteDetails
operator|.
name|setStopTrigger
argument_list|(
name|trigger
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|SUSPEND
condition|)
block|{
name|scheduledRouteDetails
operator|.
name|setSuspendJobDetail
argument_list|(
name|jobDetail
argument_list|)
expr_stmt|;
name|scheduledRouteDetails
operator|.
name|setSuspendTrigger
argument_list|(
name|trigger
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|RESUME
condition|)
block|{
name|scheduledRouteDetails
operator|.
name|setResumeJobDetail
argument_list|(
name|jobDetail
argument_list|)
expr_stmt|;
name|scheduledRouteDetails
operator|.
name|setResumeTrigger
argument_list|(
name|trigger
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|loadCallbackDataIntoSchedulerContext (JobDetail jobDetail, Action action, Route route)
specifier|protected
name|void
name|loadCallbackDataIntoSchedulerContext
parameter_list|(
name|JobDetail
name|jobDetail
parameter_list|,
name|Action
name|action
parameter_list|,
name|Route
name|route
parameter_list|)
throws|throws
name|SchedulerException
block|{
name|getScheduler
argument_list|()
operator|.
name|getContext
argument_list|()
operator|.
name|put
argument_list|(
name|jobDetail
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|ScheduledJobState
argument_list|(
name|action
argument_list|,
name|route
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|retrieveTriggerName (Action action)
specifier|public
name|String
name|retrieveTriggerName
parameter_list|(
name|Action
name|action
parameter_list|)
block|{
name|String
name|triggerName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|START
condition|)
block|{
name|triggerName
operator|=
name|scheduledRouteDetails
operator|.
name|getStartTrigger
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|STOP
condition|)
block|{
name|triggerName
operator|=
name|scheduledRouteDetails
operator|.
name|getStopTrigger
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|SUSPEND
condition|)
block|{
name|triggerName
operator|=
name|scheduledRouteDetails
operator|.
name|getSuspendTrigger
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|RESUME
condition|)
block|{
name|triggerName
operator|=
name|scheduledRouteDetails
operator|.
name|getResumeTrigger
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
return|return
name|triggerName
return|;
block|}
DECL|method|retrieveTriggerGroup (Action action)
specifier|public
name|String
name|retrieveTriggerGroup
parameter_list|(
name|Action
name|action
parameter_list|)
block|{
name|String
name|triggerGroup
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|START
condition|)
block|{
name|triggerGroup
operator|=
name|scheduledRouteDetails
operator|.
name|getStartTrigger
argument_list|()
operator|.
name|getGroup
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|STOP
condition|)
block|{
name|triggerGroup
operator|=
name|scheduledRouteDetails
operator|.
name|getStopTrigger
argument_list|()
operator|.
name|getGroup
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|SUSPEND
condition|)
block|{
name|triggerGroup
operator|=
name|scheduledRouteDetails
operator|.
name|getSuspendTrigger
argument_list|()
operator|.
name|getGroup
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|RESUME
condition|)
block|{
name|triggerGroup
operator|=
name|scheduledRouteDetails
operator|.
name|getResumeTrigger
argument_list|()
operator|.
name|getGroup
argument_list|()
expr_stmt|;
block|}
return|return
name|triggerGroup
return|;
block|}
DECL|method|retrieveJobDetailName (Action action)
specifier|public
name|String
name|retrieveJobDetailName
parameter_list|(
name|Action
name|action
parameter_list|)
block|{
name|String
name|jobDetailName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|START
condition|)
block|{
name|jobDetailName
operator|=
name|scheduledRouteDetails
operator|.
name|getStartJobDetail
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|STOP
condition|)
block|{
name|jobDetailName
operator|=
name|scheduledRouteDetails
operator|.
name|getStopJobDetail
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|SUSPEND
condition|)
block|{
name|jobDetailName
operator|=
name|scheduledRouteDetails
operator|.
name|getSuspendJobDetail
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|RESUME
condition|)
block|{
name|jobDetailName
operator|=
name|scheduledRouteDetails
operator|.
name|getResumeJobDetail
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
return|return
name|jobDetailName
return|;
block|}
DECL|method|retrieveJobDetailGroup (Action action)
specifier|public
name|String
name|retrieveJobDetailGroup
parameter_list|(
name|Action
name|action
parameter_list|)
block|{
name|String
name|jobDetailGroup
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|START
condition|)
block|{
name|jobDetailGroup
operator|=
name|scheduledRouteDetails
operator|.
name|getStartJobDetail
argument_list|()
operator|.
name|getGroup
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|STOP
condition|)
block|{
name|jobDetailGroup
operator|=
name|scheduledRouteDetails
operator|.
name|getStopJobDetail
argument_list|()
operator|.
name|getGroup
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|SUSPEND
condition|)
block|{
name|jobDetailGroup
operator|=
name|scheduledRouteDetails
operator|.
name|getSuspendJobDetail
argument_list|()
operator|.
name|getGroup
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|action
operator|==
name|Action
operator|.
name|RESUME
condition|)
block|{
name|jobDetailGroup
operator|=
name|scheduledRouteDetails
operator|.
name|getResumeJobDetail
argument_list|()
operator|.
name|getGroup
argument_list|()
expr_stmt|;
block|}
return|return
name|jobDetailGroup
return|;
block|}
DECL|method|getScheduledRouteDetails ()
specifier|public
name|ScheduledRouteDetails
name|getScheduledRouteDetails
parameter_list|()
block|{
return|return
name|scheduledRouteDetails
return|;
block|}
DECL|method|setScheduledRouteDetails (ScheduledRouteDetails scheduledRouteDetails)
specifier|public
name|void
name|setScheduledRouteDetails
parameter_list|(
name|ScheduledRouteDetails
name|scheduledRouteDetails
parameter_list|)
block|{
name|this
operator|.
name|scheduledRouteDetails
operator|=
name|scheduledRouteDetails
expr_stmt|;
block|}
DECL|method|setScheduler (Scheduler scheduler)
specifier|public
name|void
name|setScheduler
parameter_list|(
name|Scheduler
name|scheduler
parameter_list|)
block|{
name|this
operator|.
name|scheduler
operator|=
name|scheduler
expr_stmt|;
block|}
DECL|method|getScheduler ()
specifier|public
name|Scheduler
name|getScheduler
parameter_list|()
block|{
return|return
name|scheduler
return|;
block|}
DECL|method|setRouteStopGracePeriod (int routeStopGracePeriod)
specifier|public
name|void
name|setRouteStopGracePeriod
parameter_list|(
name|int
name|routeStopGracePeriod
parameter_list|)
block|{
name|this
operator|.
name|routeStopGracePeriod
operator|=
name|routeStopGracePeriod
expr_stmt|;
block|}
DECL|method|getRouteStopGracePeriod ()
specifier|public
name|int
name|getRouteStopGracePeriod
parameter_list|()
block|{
return|return
name|routeStopGracePeriod
return|;
block|}
DECL|method|setTimeUnit (TimeUnit timeUnit)
specifier|public
name|void
name|setTimeUnit
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|this
operator|.
name|timeUnit
operator|=
name|timeUnit
expr_stmt|;
block|}
DECL|method|getTimeUnit ()
specifier|public
name|TimeUnit
name|getTimeUnit
parameter_list|()
block|{
return|return
name|timeUnit
return|;
block|}
block|}
end_class

end_unit

