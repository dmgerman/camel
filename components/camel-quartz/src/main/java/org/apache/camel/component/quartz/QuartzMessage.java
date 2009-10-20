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
name|Exchange
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
name|DefaultMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobExecutionContext
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|QuartzMessage
specifier|public
class|class
name|QuartzMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|jobExecutionContext
specifier|private
specifier|final
name|JobExecutionContext
name|jobExecutionContext
decl_stmt|;
DECL|method|QuartzMessage (Exchange exchange, JobExecutionContext jobExecutionContext)
specifier|public
name|QuartzMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|JobExecutionContext
name|jobExecutionContext
parameter_list|)
block|{
name|this
operator|.
name|jobExecutionContext
operator|=
name|jobExecutionContext
expr_stmt|;
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// do not set body as it should be null
block|}
DECL|method|getJobExecutionContext ()
specifier|public
name|JobExecutionContext
name|getJobExecutionContext
parameter_list|()
block|{
return|return
name|jobExecutionContext
return|;
block|}
annotation|@
name|Override
DECL|method|populateInitialHeaders (Map<String, Object> map)
specifier|protected
name|void
name|populateInitialHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
name|super
operator|.
name|populateInitialHeaders
argument_list|(
name|map
argument_list|)
expr_stmt|;
if|if
condition|(
name|jobExecutionContext
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
literal|"calendar"
argument_list|,
name|jobExecutionContext
operator|.
name|getCalendar
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"fireTime"
argument_list|,
name|jobExecutionContext
operator|.
name|getFireTime
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"jobDetail"
argument_list|,
name|jobExecutionContext
operator|.
name|getJobDetail
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"jobInstance"
argument_list|,
name|jobExecutionContext
operator|.
name|getJobInstance
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"jobRunTime"
argument_list|,
name|jobExecutionContext
operator|.
name|getJobRunTime
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"mergedJobDataMap"
argument_list|,
name|jobExecutionContext
operator|.
name|getMergedJobDataMap
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"nextFireTime"
argument_list|,
name|jobExecutionContext
operator|.
name|getNextFireTime
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"previousFireTime"
argument_list|,
name|jobExecutionContext
operator|.
name|getPreviousFireTime
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"refireCount"
argument_list|,
name|jobExecutionContext
operator|.
name|getRefireCount
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"result"
argument_list|,
name|jobExecutionContext
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"scheduledFireTime"
argument_list|,
name|jobExecutionContext
operator|.
name|getScheduledFireTime
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"scheduler"
argument_list|,
name|jobExecutionContext
operator|.
name|getScheduler
argument_list|()
argument_list|)
expr_stmt|;
name|Trigger
name|trigger
init|=
name|jobExecutionContext
operator|.
name|getTrigger
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"trigger"
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"triggerName"
argument_list|,
name|trigger
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"triggerGroup"
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
end_class

end_unit

