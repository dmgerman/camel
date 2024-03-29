begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Endpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|JobKey
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
name|TriggerKey
import|;
end_import

begin_class
DECL|class|QuartzEndpointConfigureTest
specifier|public
class|class
name|QuartzEndpointConfigureTest
extends|extends
name|BaseQuartzTest
block|{
annotation|@
name|Test
DECL|method|testConfigureGroupAndName ()
specifier|public
name|void
name|testConfigureGroupAndName
parameter_list|()
throws|throws
name|Exception
block|{
name|QuartzEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"quartz://myGroup/myName?trigger.repeatCount=3&trigger.repeatInterval=1000"
argument_list|)
decl_stmt|;
name|Scheduler
name|scheduler
init|=
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|getScheduler
argument_list|()
decl_stmt|;
name|TriggerKey
name|triggerKey
init|=
name|endpoint
operator|.
name|getTriggerKey
argument_list|()
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
name|JobDetail
name|jobDetail
init|=
name|scheduler
operator|.
name|getJobDetail
argument_list|(
name|JobKey
operator|.
name|jobKey
argument_list|(
name|triggerKey
operator|.
name|getName
argument_list|()
argument_list|,
name|triggerKey
operator|.
name|getGroup
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getName()"
argument_list|,
literal|"myName"
argument_list|,
name|triggerKey
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getGroup()"
argument_list|,
literal|"myGroup"
argument_list|,
name|triggerKey
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getJobName"
argument_list|,
literal|"myName"
argument_list|,
name|jobDetail
operator|.
name|getKey
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getJobGroup"
argument_list|,
literal|"myGroup"
argument_list|,
name|jobDetail
operator|.
name|getKey
argument_list|()
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleTrigger
name|simpleTrigger
init|=
name|assertIsInstanceOf
argument_list|(
name|SimpleTrigger
operator|.
name|class
argument_list|,
name|trigger
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getRepeatCount()"
argument_list|,
literal|3
argument_list|,
name|simpleTrigger
operator|.
name|getRepeatCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfigureName ()
specifier|public
name|void
name|testConfigureName
parameter_list|()
throws|throws
name|Exception
block|{
name|QuartzEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"quartz://myName"
argument_list|)
decl_stmt|;
name|Scheduler
name|scheduler
init|=
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|getScheduler
argument_list|()
decl_stmt|;
name|TriggerKey
name|triggerKey
init|=
name|endpoint
operator|.
name|getTriggerKey
argument_list|()
decl_stmt|;
name|JobDetail
name|jobDetail
init|=
name|scheduler
operator|.
name|getJobDetail
argument_list|(
name|JobKey
operator|.
name|jobKey
argument_list|(
name|triggerKey
operator|.
name|getName
argument_list|()
argument_list|,
name|triggerKey
operator|.
name|getGroup
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getName()"
argument_list|,
literal|"myName"
argument_list|,
name|triggerKey
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getGroup()"
argument_list|,
literal|"Camel_"
operator|+
name|context
operator|.
name|getManagementName
argument_list|()
argument_list|,
name|triggerKey
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getJobName"
argument_list|,
literal|"myName"
argument_list|,
name|jobDetail
operator|.
name|getKey
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getJobGroup"
argument_list|,
literal|"Camel_"
operator|+
name|context
operator|.
name|getManagementName
argument_list|()
argument_list|,
name|jobDetail
operator|.
name|getKey
argument_list|()
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfigureCronExpression ()
specifier|public
name|void
name|testConfigureCronExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|QuartzEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"quartz://myGroup/myTimerName?cron=0+0/5+12-18+?+*+MON-FRI"
argument_list|)
decl_stmt|;
name|Scheduler
name|scheduler
init|=
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|getScheduler
argument_list|()
decl_stmt|;
name|TriggerKey
name|triggerKey
init|=
name|endpoint
operator|.
name|getTriggerKey
argument_list|()
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
name|JobDetail
name|jobDetail
init|=
name|scheduler
operator|.
name|getJobDetail
argument_list|(
name|JobKey
operator|.
name|jobKey
argument_list|(
name|triggerKey
operator|.
name|getName
argument_list|()
argument_list|,
name|triggerKey
operator|.
name|getGroup
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getName()"
argument_list|,
literal|"myTimerName"
argument_list|,
name|triggerKey
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getGroup()"
argument_list|,
literal|"myGroup"
argument_list|,
name|triggerKey
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getJobName"
argument_list|,
literal|"myTimerName"
argument_list|,
name|jobDetail
operator|.
name|getKey
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getJobGroup"
argument_list|,
literal|"myGroup"
argument_list|,
name|jobDetail
operator|.
name|getKey
argument_list|()
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|CronTrigger
operator|.
name|class
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
name|CronTrigger
name|cronTrigger
init|=
operator|(
name|CronTrigger
operator|)
name|trigger
decl_stmt|;
name|assertEquals
argument_list|(
literal|"cron expression"
argument_list|,
literal|"0 0/5 12-18 ? * MON-FRI"
argument_list|,
name|cronTrigger
operator|.
name|getCronExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfigureAnotherCronExpression ()
specifier|public
name|void
name|testConfigureAnotherCronExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|QuartzEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"quartz://myGroup/myTimerName?cron=0+0+*+*+*+?"
argument_list|)
decl_stmt|;
name|Scheduler
name|scheduler
init|=
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|getScheduler
argument_list|()
decl_stmt|;
name|TriggerKey
name|triggerKey
init|=
name|endpoint
operator|.
name|getTriggerKey
argument_list|()
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
name|JobDetail
name|jobDetail
init|=
name|scheduler
operator|.
name|getJobDetail
argument_list|(
name|JobKey
operator|.
name|jobKey
argument_list|(
name|triggerKey
operator|.
name|getName
argument_list|()
argument_list|,
name|triggerKey
operator|.
name|getGroup
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getName()"
argument_list|,
literal|"myTimerName"
argument_list|,
name|triggerKey
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getGroup()"
argument_list|,
literal|"myGroup"
argument_list|,
name|triggerKey
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getJobName"
argument_list|,
literal|"myTimerName"
argument_list|,
name|jobDetail
operator|.
name|getKey
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getJobGroup"
argument_list|,
literal|"myGroup"
argument_list|,
name|jobDetail
operator|.
name|getKey
argument_list|()
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|CronTrigger
operator|.
name|class
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
name|CronTrigger
name|cronTrigger
init|=
operator|(
name|CronTrigger
operator|)
name|trigger
decl_stmt|;
name|assertEquals
argument_list|(
literal|"cron expression"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
name|cronTrigger
operator|.
name|getCronExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfigureJobName ()
specifier|public
name|void
name|testConfigureJobName
parameter_list|()
throws|throws
name|Exception
block|{
name|QuartzEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"quartz://myGroup/myTimerName?job.name=hadrian&cron=0+0+*+*+*+?"
argument_list|)
decl_stmt|;
name|Scheduler
name|scheduler
init|=
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|getScheduler
argument_list|()
decl_stmt|;
name|TriggerKey
name|triggerKey
init|=
name|endpoint
operator|.
name|getTriggerKey
argument_list|()
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
name|JobDetail
name|jobDetail
init|=
name|scheduler
operator|.
name|getJobDetail
argument_list|(
name|JobKey
operator|.
name|jobKey
argument_list|(
literal|"hadrian"
argument_list|,
name|triggerKey
operator|.
name|getGroup
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getName()"
argument_list|,
literal|"myTimerName"
argument_list|,
name|triggerKey
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getGroup()"
argument_list|,
literal|"myGroup"
argument_list|,
name|triggerKey
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getJobName"
argument_list|,
literal|"hadrian"
argument_list|,
name|jobDetail
operator|.
name|getKey
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getJobGroup"
argument_list|,
literal|"myGroup"
argument_list|,
name|jobDetail
operator|.
name|getKey
argument_list|()
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|CronTrigger
operator|.
name|class
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfigureNoDoubleSlashNoCron ()
specifier|public
name|void
name|testConfigureNoDoubleSlashNoCron
parameter_list|()
throws|throws
name|Exception
block|{
name|QuartzEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"quartz:myGroup/myTimerName"
argument_list|)
decl_stmt|;
name|TriggerKey
name|triggerKey
init|=
name|endpoint
operator|.
name|getTriggerKey
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getName()"
argument_list|,
literal|"myTimerName"
argument_list|,
name|triggerKey
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getGroup()"
argument_list|,
literal|"myGroup"
argument_list|,
name|triggerKey
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfigureNoDoubleSlashQuestionCron ()
specifier|public
name|void
name|testConfigureNoDoubleSlashQuestionCron
parameter_list|()
throws|throws
name|Exception
block|{
name|QuartzEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"quartz:myGroup/myTimerName?cron=0+0+*+*+*+?"
argument_list|)
decl_stmt|;
name|Scheduler
name|scheduler
init|=
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|getScheduler
argument_list|()
decl_stmt|;
name|TriggerKey
name|triggerKey
init|=
name|endpoint
operator|.
name|getTriggerKey
argument_list|()
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
name|JobDetail
name|jobDetail
init|=
name|scheduler
operator|.
name|getJobDetail
argument_list|(
name|JobKey
operator|.
name|jobKey
argument_list|(
name|triggerKey
operator|.
name|getName
argument_list|()
argument_list|,
name|triggerKey
operator|.
name|getGroup
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getName()"
argument_list|,
literal|"myTimerName"
argument_list|,
name|triggerKey
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getGroup()"
argument_list|,
literal|"myGroup"
argument_list|,
name|triggerKey
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getJobName"
argument_list|,
literal|"myTimerName"
argument_list|,
name|jobDetail
operator|.
name|getKey
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getJobGroup"
argument_list|,
literal|"myGroup"
argument_list|,
name|jobDetail
operator|.
name|getKey
argument_list|()
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|CronTrigger
operator|.
name|class
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
name|CronTrigger
name|cronTrigger
init|=
operator|(
name|CronTrigger
operator|)
name|trigger
decl_stmt|;
name|assertEquals
argument_list|(
literal|"cron expression"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
name|cronTrigger
operator|.
name|getCronExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfigureDeleteJob ()
specifier|public
name|void
name|testConfigureDeleteJob
parameter_list|()
throws|throws
name|Exception
block|{
name|QuartzEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"quartz:myGroup/myTimerName?cron=0+0+*+*+*+?"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"cron expression"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
name|endpoint
operator|.
name|getCron
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"deleteJob"
argument_list|,
literal|true
argument_list|,
name|endpoint
operator|.
name|isDeleteJob
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"quartz:myGroup/myTimerName2?cron=1+0+*+*+*+?&deleteJob=false"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cron expression"
argument_list|,
literal|"1 0 * * * ?"
argument_list|,
name|endpoint
operator|.
name|getCron
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"deleteJob"
argument_list|,
literal|false
argument_list|,
name|endpoint
operator|.
name|isDeleteJob
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resolveMandatoryEndpoint (String uri)
specifier|protected
name|QuartzEndpoint
name|resolveMandatoryEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
name|super
operator|.
name|resolveMandatoryEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
return|return
name|assertIsInstanceOf
argument_list|(
name|QuartzEndpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
return|;
block|}
block|}
end_class

end_unit

