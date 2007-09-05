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
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * A<a href="http://activemq.apache.org/camel/quartz.html">Quartz Component</a>  *   * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|QuartzComponent
specifier|public
class|class
name|QuartzComponent
extends|extends
name|DefaultComponent
argument_list|<
name|QuartzExchange
argument_list|>
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
DECL|field|triggers
specifier|private
name|Map
argument_list|<
name|Trigger
argument_list|,
name|JobDetail
argument_list|>
name|triggers
decl_stmt|;
DECL|method|QuartzComponent ()
specifier|public
name|QuartzComponent
parameter_list|()
block|{     }
DECL|method|QuartzComponent (CamelContext context)
specifier|public
name|QuartzComponent
parameter_list|(
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
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|QuartzEndpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
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
name|name
decl_stmt|;
name|String
name|group
init|=
literal|"Camel"
decl_stmt|;
name|String
name|path
init|=
name|u
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|CronTrigger
name|cronTrigger
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
name|path
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|int
name|idx
init|=
name|path
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|cronTrigger
operator|=
operator|new
name|CronTrigger
argument_list|()
expr_stmt|;
name|name
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
name|String
name|cronExpression
init|=
name|path
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
decl_stmt|;
comment|// lets allow / instead of spaces and allow $ instead of ?
name|cronExpression
operator|=
name|cronExpression
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|' '
argument_list|)
expr_stmt|;
name|cronExpression
operator|=
name|cronExpression
operator|.
name|replace
argument_list|(
literal|'$'
argument_list|,
literal|'?'
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating cron trigger: "
operator|+
name|cronExpression
argument_list|)
expr_stmt|;
name|cronTrigger
operator|.
name|setCronExpression
argument_list|(
name|cronExpression
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setTrigger
argument_list|(
name|cronTrigger
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|path
expr_stmt|;
block|}
name|group
operator|=
name|u
operator|.
name|getHost
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|u
operator|.
name|getHost
argument_list|()
expr_stmt|;
block|}
comment|/*          * String[] names = ObjectHelper.splitOnCharacter(remaining, "/", 2); if          * (names[1] != null) { group = names[0]; name = names[1]; } else { name =          * names[0]; }          */
name|Trigger
name|trigger
init|=
name|cronTrigger
decl_stmt|;
if|if
condition|(
name|trigger
operator|==
literal|null
condition|)
block|{
name|trigger
operator|=
name|answer
operator|.
name|getTrigger
argument_list|()
expr_stmt|;
block|}
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
name|getScheduler
argument_list|()
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
DECL|method|setFactory (SchedulerFactory factory)
specifier|public
name|void
name|setFactory
parameter_list|(
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
DECL|method|getTriggers ()
specifier|public
name|Map
name|getTriggers
parameter_list|()
block|{
return|return
name|triggers
return|;
block|}
DECL|method|setTriggers (Map triggers)
specifier|public
name|void
name|setTriggers
parameter_list|(
name|Map
name|triggers
parameter_list|)
block|{
name|this
operator|.
name|triggers
operator|=
name|triggers
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
return|return
name|getFactory
argument_list|()
operator|.
name|getScheduler
argument_list|()
return|;
block|}
block|}
end_class

end_unit

