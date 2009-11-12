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
name|Map
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
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
comment|/**  * A<a href="http://camel.apache.org/quartz.html">Quartz Component</a>  *<p/>  * For a bried tutorial on setting cron expression see  *<a href="http://www.opensymphony.com/quartz/wikidocs/CronTriggers%20Tutorial.html">Quartz cron tutorial</a>.  *   * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|QuartzComponent
specifier|public
class|class
name|QuartzComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|QuartzComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|factory
specifier|private
name|SchedulerFactory
name|factory
decl_stmt|;
DECL|field|scheduler
specifier|private
name|Scheduler
name|scheduler
decl_stmt|;
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
DECL|method|createEndpoint (final String uri, final String remaining, final Map parameters)
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
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|QuartzEndpoint
name|answer
init|=
operator|new
name|QuartzEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|getScheduler
argument_list|()
argument_list|)
decl_stmt|;
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
comment|// create the trigger either cron or simple
name|Trigger
name|trigger
decl_stmt|;
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
block|}
name|answer
operator|.
name|setTrigger
argument_list|(
name|trigger
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
name|Map
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
name|setProperties
argument_list|(
name|trigger
argument_list|,
name|triggerParameters
argument_list|)
expr_stmt|;
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
comment|// replace + back to space so its a cron expression
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
literal|"Starting Quartz scheduler: "
operator|+
name|scheduler
operator|.
name|getSchedulerName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|scheduler
operator|.
name|start
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
if|if
condition|(
name|scheduler
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
literal|"Shutting down Quartz scheduler: "
operator|+
name|scheduler
operator|.
name|getSchedulerName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|scheduler
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getFactory ()
specifier|public
name|SchedulerFactory
name|getFactory
parameter_list|()
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
name|this
operator|.
name|scheduler
operator|=
name|scheduler
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|createSchedulerFactory ()
specifier|protected
name|SchedulerFactory
name|createSchedulerFactory
parameter_list|()
block|{
return|return
operator|new
name|StdSchedulerFactory
argument_list|()
return|;
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

