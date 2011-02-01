begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quartz
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quartz
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|List
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
name|Properties
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
name|StartupListener
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
name|DefaultComponent
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
name|IntrospectionSupport
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
name|SchedulerFactory
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
name|impl
operator|.
name|StdSchedulerFactory
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/quartz.html">Quartz Component</a>  *<p/>  * For a brief tutorial on setting cron expression see  *<a href="http://www.opensymphony.com/quartz/wikidocs/CronTriggers%20Tutorial.html">Quartz cron tutorial</a>.  *  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|QuartzComponent
specifier|public
class|class
name|QuartzComponent
extends|extends
name|DefaultComponent
implements|implements
name|StartupListener
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
name|QuartzComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|JOBS
specifier|private
specifier|static
specifier|final
name|AtomicInteger
name|JOBS
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|scheduler
specifier|private
specifier|static
name|Scheduler
name|scheduler
decl_stmt|;
DECL|field|jobsToAdd
specifier|private
specifier|final
name|List
argument_list|<
name|JobToAdd
argument_list|>
name|jobsToAdd
init|=
operator|new
name|ArrayList
argument_list|<
name|JobToAdd
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|factory
specifier|private
name|SchedulerFactory
name|factory
decl_stmt|;
DECL|field|properties
specifier|private
name|Properties
name|properties
decl_stmt|;
DECL|field|propertiesFile
specifier|private
name|String
name|propertiesFile
decl_stmt|;
DECL|field|startDelayedSeconds
specifier|private
name|int
name|startDelayedSeconds
decl_stmt|;
DECL|field|autoStartScheduler
specifier|private
name|boolean
name|autoStartScheduler
init|=
literal|true
decl_stmt|;
DECL|class|JobToAdd
specifier|private
specifier|final
class|class
name|JobToAdd
block|{
DECL|field|job
specifier|private
specifier|final
name|JobDetail
name|job
decl_stmt|;
DECL|field|trigger
specifier|private
specifier|final
name|Trigger
name|trigger
decl_stmt|;
DECL|method|JobToAdd (JobDetail job, Trigger trigger)
specifier|private
name|JobToAdd
parameter_list|(
name|JobDetail
name|job
parameter_list|,
name|Trigger
name|trigger
parameter_list|)
block|{
name|this
operator|.
name|job
operator|=
name|job
expr_stmt|;
name|this
operator|.
name|trigger
operator|=
name|trigger
expr_stmt|;
block|}
DECL|method|getJob ()
specifier|public
name|JobDetail
name|getJob
parameter_list|()
block|{
return|return
name|job
return|;
block|}
DECL|method|getTrigger ()
specifier|public
name|Trigger
name|getTrigger
parameter_list|()
block|{
return|return
name|trigger
return|;
block|}
block|}
DECL|method|QuartzComponent ()
specifier|public
name|QuartzComponent
parameter_list|()
block|{     }
DECL|method|QuartzComponent (final CamelContext context)
specifier|public
name|QuartzComponent
parameter_list|(
specifier|final
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (final String uri, final String remaining, final Map<String, Object> parameters)
specifier|protected
name|QuartzEndpoint
name|createEndpoint
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|remaining
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// lets split the remaining into a group/name
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|u
operator|.
name|getPath
argument_list|()
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|String
name|host
init|=
name|u
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|String
name|cron
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"cron"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Boolean
name|fireNow
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"fireNow"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
decl_stmt|;
comment|// group can be optional, if so set it to Camel
name|String
name|name
decl_stmt|;
name|String
name|group
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|path
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|host
argument_list|)
condition|)
block|{
name|group
operator|=
name|host
expr_stmt|;
name|name
operator|=
name|path
expr_stmt|;
block|}
else|else
block|{
name|group
operator|=
literal|"Camel"
expr_stmt|;
name|name
operator|=
name|host
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|triggerParameters
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"trigger."
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jobParameters
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"job."
argument_list|)
decl_stmt|;
name|Trigger
name|trigger
decl_stmt|;
comment|// if we're starting up and not running in Quartz clustered mode then check for a name conflict.
if|if
condition|(
operator|!
name|isClustered
argument_list|()
condition|)
block|{
comment|// check to see if this trigger already exists
name|trigger
operator|=
name|getScheduler
argument_list|()
operator|.
name|getTrigger
argument_list|(
name|name
argument_list|,
name|group
argument_list|)
expr_stmt|;
if|if
condition|(
name|trigger
operator|!=
literal|null
condition|)
block|{
name|String
name|msg
init|=
literal|"A Quartz job already exists with the name/group: "
operator|+
name|name
operator|+
literal|"/"
operator|+
name|group
decl_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
comment|// create the trigger either cron or simple
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|cron
argument_list|)
condition|)
block|{
name|trigger
operator|=
name|createCronTrigger
argument_list|(
name|cron
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|trigger
operator|=
operator|new
name|SimpleTrigger
argument_list|()
expr_stmt|;
if|if
condition|(
name|fireNow
condition|)
block|{
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
name|long
name|interval
init|=
name|Long
operator|.
name|valueOf
argument_list|(
name|intervalString
argument_list|)
decl_stmt|;
name|trigger
operator|.
name|setStartTime
argument_list|(
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|interval
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|QuartzEndpoint
name|answer
init|=
operator|new
name|QuartzEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|answer
operator|.
name|getJobDetail
argument_list|()
argument_list|,
name|jobParameters
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|trigger
argument_list|,
name|triggerParameters
argument_list|)
expr_stmt|;
name|trigger
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|trigger
operator|.
name|setGroup
argument_list|(
name|group
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setTrigger
argument_list|(
name|trigger
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|createCronTrigger (String path)
specifier|protected
name|CronTrigger
name|createCronTrigger
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ParseException
block|{
comment|// replace + back to space so it's a cron expression
name|path
operator|=
name|path
operator|.
name|replaceAll
argument_list|(
literal|"\\+"
argument_list|,
literal|" "
argument_list|)
expr_stmt|;
name|CronTrigger
name|cron
init|=
operator|new
name|CronTrigger
argument_list|()
decl_stmt|;
name|cron
operator|.
name|setCronExpression
argument_list|(
name|path
argument_list|)
expr_stmt|;
return|return
name|cron
return|;
block|}
DECL|method|onCamelContextStarted (CamelContext camelContext, boolean alreadyStarted)
specifier|public
name|void
name|onCamelContextStarted
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|boolean
name|alreadyStarted
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|scheduler
operator|!=
literal|null
condition|)
block|{
comment|// register current camel context to scheduler so we can look it up when jobs is being triggered
name|scheduler
operator|.
name|getContext
argument_list|()
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_CAMEL_CONTEXT
operator|+
literal|"-"
operator|+
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// if not configure to auto start then don't start it
if|if
condition|(
operator|!
name|isAutoStartScheduler
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"QuartzComponent configured to not auto start Quartz scheduler."
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// only start scheduler when CamelContext have finished starting
name|startScheduler
argument_list|()
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|scheduler
operator|==
literal|null
condition|)
block|{
name|scheduler
operator|=
name|getScheduler
argument_list|()
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|scheduler
operator|!=
literal|null
condition|)
block|{
name|int
name|number
init|=
name|JOBS
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|number
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Cannot shutdown Quartz scheduler: "
operator|+
name|scheduler
operator|.
name|getSchedulerName
argument_list|()
operator|+
literal|" as there are still "
operator|+
name|number
operator|+
literal|" jobs registered."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// no more jobs then shutdown the scheduler
name|LOG
operator|.
name|info
argument_list|(
literal|"There are no more jobs registered, so shutting down Quartz scheduler: "
operator|+
name|scheduler
operator|.
name|getSchedulerName
argument_list|()
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|scheduler
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
DECL|method|addJob (JobDetail job, Trigger trigger)
specifier|public
name|void
name|addJob
parameter_list|(
name|JobDetail
name|job
parameter_list|,
name|Trigger
name|trigger
parameter_list|)
throws|throws
name|SchedulerException
block|{
if|if
condition|(
name|scheduler
operator|==
literal|null
condition|)
block|{
comment|// add job to internal list because we will defer adding to the scheduler when camel context has been fully started
name|jobsToAdd
operator|.
name|add
argument_list|(
operator|new
name|JobToAdd
argument_list|(
name|job
argument_list|,
name|trigger
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// add job directly to scheduler
name|doAddJob
argument_list|(
name|job
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doAddJob (JobDetail job, Trigger trigger)
specifier|private
name|void
name|doAddJob
parameter_list|(
name|JobDetail
name|job
parameter_list|,
name|Trigger
name|trigger
parameter_list|)
throws|throws
name|SchedulerException
block|{
name|JOBS
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|Trigger
name|existingTrigger
init|=
name|getScheduler
argument_list|()
operator|.
name|getTrigger
argument_list|(
name|trigger
operator|.
name|getName
argument_list|()
argument_list|,
name|trigger
operator|.
name|getGroup
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingTrigger
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding job using trigger: "
operator|+
name|trigger
operator|.
name|getGroup
argument_list|()
operator|+
literal|"/"
operator|+
name|trigger
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getScheduler
argument_list|()
operator|.
name|scheduleJob
argument_list|(
name|job
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
block|}
elseif|else
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
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Trigger: "
operator|+
name|trigger
operator|.
name|getGroup
argument_list|()
operator|+
literal|"/"
operator|+
name|trigger
operator|.
name|getName
argument_list|()
operator|+
literal|" already exists and will be updated by Quartz."
argument_list|)
expr_stmt|;
block|}
name|scheduler
operator|.
name|addJob
argument_list|(
name|job
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|trigger
operator|.
name|setJobName
argument_list|(
name|job
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|rescheduleJob
argument_list|(
name|trigger
operator|.
name|getName
argument_list|()
argument_list|,
name|trigger
operator|.
name|getGroup
argument_list|()
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Trigger: "
operator|+
name|trigger
operator|.
name|getGroup
argument_list|()
operator|+
literal|"/"
operator|+
name|trigger
operator|.
name|getName
argument_list|()
operator|+
literal|" already exists and will be resumed automatically by Quartz."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|isClustered
argument_list|()
condition|)
block|{
name|scheduler
operator|.
name|resumeTrigger
argument_list|(
name|trigger
operator|.
name|getName
argument_list|()
argument_list|,
name|trigger
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|oldTrigger
operator|instanceof
name|CronTrigger
operator|&&
name|oldTrigger
operator|.
name|equals
argument_list|(
name|newTrigger
argument_list|)
condition|)
block|{
name|CronTrigger
name|oldCron
init|=
operator|(
name|CronTrigger
operator|)
name|oldTrigger
decl_stmt|;
name|CronTrigger
name|newCron
init|=
operator|(
name|CronTrigger
operator|)
name|newTrigger
decl_stmt|;
return|return
operator|!
name|oldCron
operator|.
name|getCronExpression
argument_list|()
operator|.
name|equals
argument_list|(
name|newCron
operator|.
name|getCronExpression
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
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
DECL|method|pauseJob (Trigger trigger)
specifier|public
name|void
name|pauseJob
parameter_list|(
name|Trigger
name|trigger
parameter_list|)
throws|throws
name|SchedulerException
block|{
name|JOBS
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
if|if
condition|(
name|isClustered
argument_list|()
condition|)
block|{
comment|// do not pause jobs which are clustered, as we want the jobs to continue running on the other nodes
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot pause job using trigger: "
operator|+
name|trigger
operator|.
name|getGroup
argument_list|()
operator|+
literal|"/"
operator|+
name|trigger
operator|.
name|getName
argument_list|()
operator|+
literal|" as the JobStore is clustered."
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Pausing job using trigger: "
operator|+
name|trigger
operator|.
name|getGroup
argument_list|()
operator|+
literal|"/"
operator|+
name|trigger
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getScheduler
argument_list|()
operator|.
name|pauseTrigger
argument_list|(
name|trigger
operator|.
name|getName
argument_list|()
argument_list|,
name|trigger
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|getScheduler
argument_list|()
operator|.
name|pauseJob
argument_list|(
name|trigger
operator|.
name|getName
argument_list|()
argument_list|,
name|trigger
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|deleteJob (String name, String group)
specifier|public
name|void
name|deleteJob
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|group
parameter_list|)
throws|throws
name|SchedulerException
block|{
if|if
condition|(
name|isClustered
argument_list|()
condition|)
block|{
comment|// do not pause jobs which are clustered, as we want the jobs to continue running on the other nodes
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot delete job using trigger: "
operator|+
name|group
operator|+
literal|"/"
operator|+
name|name
operator|+
literal|" as the JobStore is clustered."
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Trigger
name|trigger
init|=
name|getScheduler
argument_list|()
operator|.
name|getTrigger
argument_list|(
name|name
argument_list|,
name|group
argument_list|)
decl_stmt|;
if|if
condition|(
name|trigger
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Deleting job using trigger: "
operator|+
name|group
operator|+
literal|"/"
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
name|getScheduler
argument_list|()
operator|.
name|unscheduleJob
argument_list|(
name|name
argument_list|,
name|group
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * To force shutdown the quartz scheduler      *      * @throws SchedulerException can be thrown if error shutting down      */
DECL|method|shutdownScheduler ()
specifier|public
name|void
name|shutdownScheduler
parameter_list|()
throws|throws
name|SchedulerException
block|{
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
literal|"Forcing shutdown of Quartz scheduler: "
operator|+
name|scheduler
operator|.
name|getSchedulerName
argument_list|()
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|scheduler
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * Is the quartz scheduler clustered?      */
DECL|method|isClustered ()
specifier|public
name|boolean
name|isClustered
parameter_list|()
throws|throws
name|SchedulerException
block|{
try|try
block|{
return|return
name|getScheduler
argument_list|()
operator|.
name|getMetaData
argument_list|()
operator|.
name|isJobStoreClustered
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodError
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Job clustering is only supported since Quartz 1.7, isClustered returning false"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
comment|/**      * To force starting the quartz scheduler      *      * @throws SchedulerException can be thrown if error starting      */
DECL|method|startScheduler ()
specifier|public
name|void
name|startScheduler
parameter_list|()
throws|throws
name|SchedulerException
block|{
for|for
control|(
name|JobToAdd
name|add
range|:
name|jobsToAdd
control|)
block|{
name|doAddJob
argument_list|(
name|add
operator|.
name|getJob
argument_list|()
argument_list|,
name|add
operator|.
name|getTrigger
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|jobsToAdd
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|getScheduler
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
if|if
condition|(
name|getStartDelayedSeconds
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting Quartz scheduler: "
operator|+
name|getScheduler
argument_list|()
operator|.
name|getSchedulerName
argument_list|()
operator|+
literal|" delayed: "
operator|+
name|getStartDelayedSeconds
argument_list|()
operator|+
literal|" seconds."
argument_list|)
expr_stmt|;
try|try
block|{
name|getScheduler
argument_list|()
operator|.
name|startDelayed
argument_list|(
name|getStartDelayedSeconds
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodError
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Your version of Quartz is too old to support delayed startup! "
operator|+
literal|"Starting Quartz scheduler immediately : "
operator|+
name|getScheduler
argument_list|()
operator|.
name|getSchedulerName
argument_list|()
argument_list|)
expr_stmt|;
name|getScheduler
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting Quartz scheduler: "
operator|+
name|getScheduler
argument_list|()
operator|.
name|getSchedulerName
argument_list|()
argument_list|)
expr_stmt|;
name|getScheduler
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getFactory ()
specifier|public
name|SchedulerFactory
name|getFactory
parameter_list|()
throws|throws
name|SchedulerException
block|{
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|factory
operator|=
name|createSchedulerFactory
argument_list|()
expr_stmt|;
block|}
return|return
name|factory
return|;
block|}
DECL|method|setFactory (final SchedulerFactory factory)
specifier|public
name|void
name|setFactory
parameter_list|(
specifier|final
name|SchedulerFactory
name|factory
parameter_list|)
block|{
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
block|}
DECL|method|getScheduler ()
specifier|public
specifier|synchronized
name|Scheduler
name|getScheduler
parameter_list|()
throws|throws
name|SchedulerException
block|{
if|if
condition|(
name|scheduler
operator|==
literal|null
condition|)
block|{
name|scheduler
operator|=
name|createScheduler
argument_list|()
expr_stmt|;
block|}
return|return
name|scheduler
return|;
block|}
DECL|method|setScheduler (final Scheduler scheduler)
specifier|public
name|void
name|setScheduler
parameter_list|(
specifier|final
name|Scheduler
name|scheduler
parameter_list|)
block|{
name|QuartzComponent
operator|.
name|scheduler
operator|=
name|scheduler
expr_stmt|;
block|}
DECL|method|getProperties ()
specifier|public
name|Properties
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
DECL|method|setProperties (Properties properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
DECL|method|getPropertiesFile ()
specifier|public
name|String
name|getPropertiesFile
parameter_list|()
block|{
return|return
name|propertiesFile
return|;
block|}
DECL|method|setPropertiesFile (String propertiesFile)
specifier|public
name|void
name|setPropertiesFile
parameter_list|(
name|String
name|propertiesFile
parameter_list|)
block|{
name|this
operator|.
name|propertiesFile
operator|=
name|propertiesFile
expr_stmt|;
block|}
DECL|method|getStartDelayedSeconds ()
specifier|public
name|int
name|getStartDelayedSeconds
parameter_list|()
block|{
return|return
name|startDelayedSeconds
return|;
block|}
DECL|method|setStartDelayedSeconds (int startDelayedSeconds)
specifier|public
name|void
name|setStartDelayedSeconds
parameter_list|(
name|int
name|startDelayedSeconds
parameter_list|)
block|{
name|this
operator|.
name|startDelayedSeconds
operator|=
name|startDelayedSeconds
expr_stmt|;
block|}
DECL|method|isAutoStartScheduler ()
specifier|public
name|boolean
name|isAutoStartScheduler
parameter_list|()
block|{
return|return
name|autoStartScheduler
return|;
block|}
DECL|method|setAutoStartScheduler (boolean autoStartScheduler)
specifier|public
name|void
name|setAutoStartScheduler
parameter_list|(
name|boolean
name|autoStartScheduler
parameter_list|)
block|{
name|this
operator|.
name|autoStartScheduler
operator|=
name|autoStartScheduler
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|loadProperties ()
specifier|protected
name|Properties
name|loadProperties
parameter_list|()
throws|throws
name|SchedulerException
block|{
name|Properties
name|answer
init|=
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|getPropertiesFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
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
literal|"Loading Quartz properties file from classpath: "
operator|+
name|getPropertiesFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|InputStream
name|is
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|loadResourceAsStream
argument_list|(
name|getPropertiesFile
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SchedulerException
argument_list|(
literal|"Quartz properties file not found in classpath: "
operator|+
name|getPropertiesFile
argument_list|()
argument_list|)
throw|;
block|}
name|answer
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
try|try
block|{
name|answer
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SchedulerException
argument_list|(
literal|"Error loading Quartz properties file from classpath: "
operator|+
name|getPropertiesFile
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|createSchedulerFactory ()
specifier|protected
name|SchedulerFactory
name|createSchedulerFactory
parameter_list|()
throws|throws
name|SchedulerException
block|{
name|Properties
name|prop
init|=
name|loadProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|prop
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating SchedulerFactory with properties: "
operator|+
name|prop
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|StdSchedulerFactory
argument_list|(
name|prop
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|StdSchedulerFactory
argument_list|()
return|;
block|}
block|}
DECL|method|createScheduler ()
specifier|protected
name|Scheduler
name|createScheduler
parameter_list|()
throws|throws
name|SchedulerException
block|{
name|Scheduler
name|scheduler
init|=
name|getFactory
argument_list|()
operator|.
name|getScheduler
argument_list|()
decl_stmt|;
comment|// register current camel context to scheduler so we can look it up when jobs is being triggered
name|scheduler
operator|.
name|getContext
argument_list|()
operator|.
name|put
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_CAMEL_CONTEXT
operator|+
literal|"-"
operator|+
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|scheduler
return|;
block|}
block|}
end_class

end_unit

