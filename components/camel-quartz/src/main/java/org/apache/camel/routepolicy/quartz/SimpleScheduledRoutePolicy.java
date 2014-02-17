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
name|Date
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

begin_class
DECL|class|SimpleScheduledRoutePolicy
specifier|public
class|class
name|SimpleScheduledRoutePolicy
extends|extends
name|ScheduledRoutePolicy
block|{
DECL|field|routeStartDate
specifier|private
name|Date
name|routeStartDate
decl_stmt|;
DECL|field|routeStartRepeatCount
specifier|private
name|int
name|routeStartRepeatCount
decl_stmt|;
DECL|field|routeStartRepeatInterval
specifier|private
name|long
name|routeStartRepeatInterval
decl_stmt|;
DECL|field|routeStopDate
specifier|private
name|Date
name|routeStopDate
decl_stmt|;
DECL|field|routeStopRepeatCount
specifier|private
name|int
name|routeStopRepeatCount
decl_stmt|;
DECL|field|routeStopRepeatInterval
specifier|private
name|long
name|routeStopRepeatInterval
decl_stmt|;
DECL|field|routeSuspendDate
specifier|private
name|Date
name|routeSuspendDate
decl_stmt|;
DECL|field|routeSuspendRepeatCount
specifier|private
name|int
name|routeSuspendRepeatCount
decl_stmt|;
DECL|field|routeSuspendRepeatInterval
specifier|private
name|long
name|routeSuspendRepeatInterval
decl_stmt|;
DECL|field|routeResumeDate
specifier|private
name|Date
name|routeResumeDate
decl_stmt|;
DECL|field|routeResumeRepeatCount
specifier|private
name|int
name|routeResumeRepeatCount
decl_stmt|;
DECL|field|routeResumeRepeatInterval
specifier|private
name|long
name|routeResumeRepeatInterval
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
name|getRouteStartDate
argument_list|()
operator|==
literal|null
operator|)
operator|&&
operator|(
name|getRouteStopDate
argument_list|()
operator|==
literal|null
operator|)
operator|&&
operator|(
name|getRouteSuspendDate
argument_list|()
operator|==
literal|null
operator|)
operator|&&
operator|(
name|getRouteResumeDate
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
literal|"Scheduled Route Policy for route {} has no start/stop/suspend/resume times specified"
argument_list|)
throw|;
block|}
name|registerRouteToScheduledRouteDetails
argument_list|(
name|route
argument_list|)
expr_stmt|;
if|if
condition|(
name|getRouteStartDate
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
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getRouteStopDate
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
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getRouteSuspendDate
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
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getRouteResumeDate
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
argument_list|,
name|route
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
name|SimpleTrigger
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
name|SimpleTrigger
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
name|getRouteStartDate
argument_list|()
argument_list|,
literal|null
argument_list|,
name|getRouteStartRepeatCount
argument_list|()
argument_list|,
name|getRouteStartRepeatInterval
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
name|SimpleTrigger
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
name|getRouteStopDate
argument_list|()
argument_list|,
literal|null
argument_list|,
name|getRouteStopRepeatCount
argument_list|()
argument_list|,
name|getRouteStopRepeatInterval
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
name|SimpleTrigger
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
name|getRouteSuspendDate
argument_list|()
argument_list|,
literal|null
argument_list|,
name|getRouteSuspendRepeatCount
argument_list|()
argument_list|,
name|getRouteSuspendRepeatInterval
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
name|SimpleTrigger
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
name|getRouteResumeDate
argument_list|()
argument_list|,
literal|null
argument_list|,
name|getRouteResumeRepeatCount
argument_list|()
argument_list|,
name|getRouteResumeRepeatInterval
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|trigger
return|;
block|}
annotation|@
name|Override
DECL|method|onRemove (Route route)
specifier|public
name|void
name|onRemove
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
try|try
block|{
comment|// stop and un-schedule jobs
name|doStop
argument_list|()
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
DECL|method|getRouteStartDate ()
specifier|public
name|Date
name|getRouteStartDate
parameter_list|()
block|{
return|return
name|routeStartDate
return|;
block|}
DECL|method|setRouteStartDate (Date routeStartDate)
specifier|public
name|void
name|setRouteStartDate
parameter_list|(
name|Date
name|routeStartDate
parameter_list|)
block|{
name|this
operator|.
name|routeStartDate
operator|=
name|routeStartDate
expr_stmt|;
block|}
DECL|method|getRouteStopDate ()
specifier|public
name|Date
name|getRouteStopDate
parameter_list|()
block|{
return|return
name|routeStopDate
return|;
block|}
DECL|method|setRouteStopDate (Date routeStopDate)
specifier|public
name|void
name|setRouteStopDate
parameter_list|(
name|Date
name|routeStopDate
parameter_list|)
block|{
name|this
operator|.
name|routeStopDate
operator|=
name|routeStopDate
expr_stmt|;
block|}
DECL|method|getRouteSuspendDate ()
specifier|public
name|Date
name|getRouteSuspendDate
parameter_list|()
block|{
return|return
name|routeSuspendDate
return|;
block|}
DECL|method|setRouteSuspendDate (Date routeSuspendDate)
specifier|public
name|void
name|setRouteSuspendDate
parameter_list|(
name|Date
name|routeSuspendDate
parameter_list|)
block|{
name|this
operator|.
name|routeSuspendDate
operator|=
name|routeSuspendDate
expr_stmt|;
block|}
DECL|method|getRouteStartRepeatCount ()
specifier|public
name|int
name|getRouteStartRepeatCount
parameter_list|()
block|{
return|return
name|routeStartRepeatCount
return|;
block|}
DECL|method|setRouteStartRepeatCount (int routeStartRepeatCount)
specifier|public
name|void
name|setRouteStartRepeatCount
parameter_list|(
name|int
name|routeStartRepeatCount
parameter_list|)
block|{
name|this
operator|.
name|routeStartRepeatCount
operator|=
name|routeStartRepeatCount
expr_stmt|;
block|}
DECL|method|getRouteStartRepeatInterval ()
specifier|public
name|long
name|getRouteStartRepeatInterval
parameter_list|()
block|{
return|return
name|routeStartRepeatInterval
return|;
block|}
DECL|method|setRouteStartRepeatInterval (long routeStartRepeatInterval)
specifier|public
name|void
name|setRouteStartRepeatInterval
parameter_list|(
name|long
name|routeStartRepeatInterval
parameter_list|)
block|{
name|this
operator|.
name|routeStartRepeatInterval
operator|=
name|routeStartRepeatInterval
expr_stmt|;
block|}
DECL|method|getRouteStopRepeatCount ()
specifier|public
name|int
name|getRouteStopRepeatCount
parameter_list|()
block|{
return|return
name|routeStopRepeatCount
return|;
block|}
DECL|method|setRouteStopRepeatCount (int routeStopRepeatCount)
specifier|public
name|void
name|setRouteStopRepeatCount
parameter_list|(
name|int
name|routeStopRepeatCount
parameter_list|)
block|{
name|this
operator|.
name|routeStopRepeatCount
operator|=
name|routeStopRepeatCount
expr_stmt|;
block|}
DECL|method|getRouteStopRepeatInterval ()
specifier|public
name|long
name|getRouteStopRepeatInterval
parameter_list|()
block|{
return|return
name|routeStopRepeatInterval
return|;
block|}
DECL|method|setRouteStopRepeatInterval (long routeStopRepeatInterval)
specifier|public
name|void
name|setRouteStopRepeatInterval
parameter_list|(
name|long
name|routeStopRepeatInterval
parameter_list|)
block|{
name|this
operator|.
name|routeStopRepeatInterval
operator|=
name|routeStopRepeatInterval
expr_stmt|;
block|}
DECL|method|getRouteSuspendRepeatCount ()
specifier|public
name|int
name|getRouteSuspendRepeatCount
parameter_list|()
block|{
return|return
name|routeSuspendRepeatCount
return|;
block|}
DECL|method|setRouteSuspendRepeatCount (int routeSuspendRepeatCount)
specifier|public
name|void
name|setRouteSuspendRepeatCount
parameter_list|(
name|int
name|routeSuspendRepeatCount
parameter_list|)
block|{
name|this
operator|.
name|routeSuspendRepeatCount
operator|=
name|routeSuspendRepeatCount
expr_stmt|;
block|}
DECL|method|getRouteSuspendRepeatInterval ()
specifier|public
name|long
name|getRouteSuspendRepeatInterval
parameter_list|()
block|{
return|return
name|routeSuspendRepeatInterval
return|;
block|}
DECL|method|setRouteSuspendRepeatInterval (long routeSuspendRepeatInterval)
specifier|public
name|void
name|setRouteSuspendRepeatInterval
parameter_list|(
name|long
name|routeSuspendRepeatInterval
parameter_list|)
block|{
name|this
operator|.
name|routeSuspendRepeatInterval
operator|=
name|routeSuspendRepeatInterval
expr_stmt|;
block|}
DECL|method|setRouteResumeDate (Date routeResumeDate)
specifier|public
name|void
name|setRouteResumeDate
parameter_list|(
name|Date
name|routeResumeDate
parameter_list|)
block|{
name|this
operator|.
name|routeResumeDate
operator|=
name|routeResumeDate
expr_stmt|;
block|}
DECL|method|getRouteResumeDate ()
specifier|public
name|Date
name|getRouteResumeDate
parameter_list|()
block|{
return|return
name|routeResumeDate
return|;
block|}
DECL|method|setRouteResumeRepeatCount (int routeResumeRepeatCount)
specifier|public
name|void
name|setRouteResumeRepeatCount
parameter_list|(
name|int
name|routeResumeRepeatCount
parameter_list|)
block|{
name|this
operator|.
name|routeResumeRepeatCount
operator|=
name|routeResumeRepeatCount
expr_stmt|;
block|}
DECL|method|getRouteResumeRepeatCount ()
specifier|public
name|int
name|getRouteResumeRepeatCount
parameter_list|()
block|{
return|return
name|routeResumeRepeatCount
return|;
block|}
DECL|method|setRouteResumeRepeatInterval (long routeResumeRepeatInterval)
specifier|public
name|void
name|setRouteResumeRepeatInterval
parameter_list|(
name|long
name|routeResumeRepeatInterval
parameter_list|)
block|{
name|this
operator|.
name|routeResumeRepeatInterval
operator|=
name|routeResumeRepeatInterval
expr_stmt|;
block|}
DECL|method|getRouteResumeRepeatInterval ()
specifier|public
name|long
name|getRouteResumeRepeatInterval
parameter_list|()
block|{
return|return
name|routeResumeRepeatInterval
return|;
block|}
block|}
end_class

end_unit

