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
name|component
operator|.
name|quartz
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
name|CronTrigger
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

begin_class
DECL|class|CronScheduledRoutePolicy
specifier|public
class|class
name|CronScheduledRoutePolicy
extends|extends
name|ScheduledRoutePolicy
implements|implements
name|ScheduledRoutePolicyConstants
block|{
DECL|field|routeStartTime
specifier|private
name|String
name|routeStartTime
decl_stmt|;
DECL|field|routeStopTime
specifier|private
name|String
name|routeStopTime
decl_stmt|;
DECL|field|routeSuspendTime
specifier|private
name|String
name|routeSuspendTime
decl_stmt|;
DECL|field|routeResumeTime
specifier|private
name|String
name|routeResumeTime
decl_stmt|;
DECL|method|onInit (Route route)
specifier|public
name|void
name|onInit
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
try|try
block|{
name|doOnInit
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
DECL|method|doOnInit (Route route)
specifier|protected
name|void
name|doOnInit
parameter_list|(
name|Route
name|route
parameter_list|)
throws|throws
name|Exception
block|{
name|QuartzComponent
name|quartz
init|=
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"quartz"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|setScheduler
argument_list|(
name|quartz
operator|.
name|getScheduler
argument_list|()
argument_list|)
expr_stmt|;
comment|// Important: do not start scheduler as QuartzComponent does that automatic
comment|// when CamelContext has been fully initialized and started
if|if
condition|(
name|getRouteStopGracePeriod
argument_list|()
operator|==
literal|0
condition|)
block|{
name|setRouteStopGracePeriod
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getTimeUnit
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setTimeUnit
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
comment|// validate time options has been configured
if|if
condition|(
operator|(
name|getRouteStartTime
argument_list|()
operator|==
literal|null
operator|)
operator|&&
operator|(
name|getRouteStopTime
argument_list|()
operator|==
literal|null
operator|)
operator|&&
operator|(
name|getRouteSuspendTime
argument_list|()
operator|==
literal|null
operator|)
operator|&&
operator|(
name|getRouteResumeTime
argument_list|()
operator|==
literal|null
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Scheduled Route Policy for route {} has no stop/stop/suspend/resume times specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|scheduledRouteDetails
operator|==
literal|null
condition|)
block|{
name|scheduledRouteDetails
operator|=
operator|new
name|ScheduledRouteDetails
argument_list|()
expr_stmt|;
name|scheduledRouteDetails
operator|.
name|setRoute
argument_list|(
name|route
argument_list|)
expr_stmt|;
if|if
condition|(
name|getRouteStartTime
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|scheduleRoute
argument_list|(
name|Action
operator|.
name|START
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getRouteStopTime
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|scheduleRoute
argument_list|(
name|Action
operator|.
name|STOP
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getRouteSuspendTime
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|scheduleRoute
argument_list|(
name|Action
operator|.
name|SUSPEND
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getRouteResumeTime
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|scheduleRoute
argument_list|(
name|Action
operator|.
name|RESUME
argument_list|)
expr_stmt|;
block|}
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
name|scheduledRouteDetails
operator|.
name|getStartJobDetail
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|deleteRouteJob
argument_list|(
name|Action
operator|.
name|START
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|scheduledRouteDetails
operator|.
name|getStopJobDetail
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|deleteRouteJob
argument_list|(
name|Action
operator|.
name|STOP
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|scheduledRouteDetails
operator|.
name|getSuspendJobDetail
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|deleteRouteJob
argument_list|(
name|Action
operator|.
name|SUSPEND
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|scheduledRouteDetails
operator|.
name|getResumeJobDetail
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|deleteRouteJob
argument_list|(
name|Action
operator|.
name|RESUME
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createTrigger (Action action, Route route)
specifier|protected
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
block|{
name|CronTrigger
name|trigger
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
name|trigger
operator|=
operator|new
name|CronTrigger
argument_list|(
name|TRIGGER_START
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|TRIGGER_GROUP
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|getRouteStartTime
argument_list|()
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
name|trigger
operator|=
operator|new
name|CronTrigger
argument_list|(
name|TRIGGER_STOP
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|TRIGGER_GROUP
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|getRouteStopTime
argument_list|()
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
name|trigger
operator|=
operator|new
name|CronTrigger
argument_list|(
name|TRIGGER_SUSPEND
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|TRIGGER_GROUP
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|getRouteSuspendTime
argument_list|()
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
name|trigger
operator|=
operator|new
name|CronTrigger
argument_list|(
name|TRIGGER_RESUME
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|TRIGGER_GROUP
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|getRouteResumeTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|trigger
return|;
block|}
DECL|method|setRouteStartTime (String routeStartTime)
specifier|public
name|void
name|setRouteStartTime
parameter_list|(
name|String
name|routeStartTime
parameter_list|)
block|{
name|this
operator|.
name|routeStartTime
operator|=
name|routeStartTime
expr_stmt|;
block|}
DECL|method|getRouteStartTime ()
specifier|public
name|String
name|getRouteStartTime
parameter_list|()
block|{
return|return
name|routeStartTime
return|;
block|}
DECL|method|setRouteStopTime (String routeStopTime)
specifier|public
name|void
name|setRouteStopTime
parameter_list|(
name|String
name|routeStopTime
parameter_list|)
block|{
name|this
operator|.
name|routeStopTime
operator|=
name|routeStopTime
expr_stmt|;
block|}
DECL|method|getRouteStopTime ()
specifier|public
name|String
name|getRouteStopTime
parameter_list|()
block|{
return|return
name|routeStopTime
return|;
block|}
DECL|method|setRouteSuspendTime (String routeSuspendTime)
specifier|public
name|void
name|setRouteSuspendTime
parameter_list|(
name|String
name|routeSuspendTime
parameter_list|)
block|{
name|this
operator|.
name|routeSuspendTime
operator|=
name|routeSuspendTime
expr_stmt|;
block|}
DECL|method|getRouteSuspendTime ()
specifier|public
name|String
name|getRouteSuspendTime
parameter_list|()
block|{
return|return
name|routeSuspendTime
return|;
block|}
DECL|method|setRouteResumeTime (String routeResumeTime)
specifier|public
name|void
name|setRouteResumeTime
parameter_list|(
name|String
name|routeResumeTime
parameter_list|)
block|{
name|this
operator|.
name|routeResumeTime
operator|=
name|routeResumeTime
expr_stmt|;
block|}
DECL|method|getRouteResumeTime ()
specifier|public
name|String
name|getRouteResumeTime
parameter_list|()
block|{
return|return
name|routeResumeTime
return|;
block|}
block|}
end_class

end_unit

