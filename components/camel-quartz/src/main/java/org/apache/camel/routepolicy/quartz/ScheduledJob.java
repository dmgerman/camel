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
name|io
operator|.
name|Serializable
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
name|JobExecutionContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|SchedulerContext
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

begin_class
DECL|class|ScheduledJob
specifier|public
class|class
name|ScheduledJob
implements|implements
name|Job
implements|,
name|Serializable
implements|,
name|ScheduledRoutePolicyConstants
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|26L
decl_stmt|;
DECL|field|storedRoute
specifier|private
name|Route
name|storedRoute
decl_stmt|;
comment|/* (non-Javadoc)      * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)      */
DECL|method|execute (JobExecutionContext jobExecutionContext)
specifier|public
name|void
name|execute
parameter_list|(
name|JobExecutionContext
name|jobExecutionContext
parameter_list|)
throws|throws
name|JobExecutionException
block|{
name|SchedulerContext
name|schedulerContext
decl_stmt|;
try|try
block|{
name|schedulerContext
operator|=
name|jobExecutionContext
operator|.
name|getScheduler
argument_list|()
operator|.
name|getContext
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SchedulerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JobExecutionException
argument_list|(
literal|"Failed to obtain scheduler context for job "
operator|+
name|jobExecutionContext
operator|.
name|getJobDetail
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|ScheduledJobState
name|state
init|=
operator|(
name|ScheduledJobState
operator|)
name|schedulerContext
operator|.
name|get
argument_list|(
name|jobExecutionContext
operator|.
name|getJobDetail
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Action
name|storedAction
init|=
name|state
operator|.
name|getAction
argument_list|()
decl_stmt|;
name|storedRoute
operator|=
name|state
operator|.
name|getRoute
argument_list|()
expr_stmt|;
name|ScheduledRoutePolicy
name|policy
init|=
operator|(
name|ScheduledRoutePolicy
operator|)
name|storedRoute
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoutePolicy
argument_list|()
decl_stmt|;
try|try
block|{
name|policy
operator|.
name|onJobExecute
argument_list|(
name|storedAction
argument_list|,
name|storedRoute
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
operator|new
name|JobExecutionException
argument_list|(
literal|"Failed to execute Scheduled Job for route "
operator|+
name|storedRoute
operator|.
name|getId
argument_list|()
operator|+
literal|" with trigger name: "
operator|+
name|jobExecutionContext
operator|.
name|getTrigger
argument_list|()
operator|.
name|getFullName
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

