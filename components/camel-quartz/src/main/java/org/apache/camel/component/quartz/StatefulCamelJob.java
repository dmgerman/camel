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

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|StatefulJob
import|;
end_import

begin_comment
comment|/**  * @author martin.gilday  *  */
end_comment

begin_class
DECL|class|StatefulCamelJob
specifier|public
class|class
name|StatefulCamelJob
implements|implements
name|StatefulJob
block|{
comment|/* (non-Javadoc) 	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext) 	 */
DECL|method|execute (final JobExecutionContext context)
specifier|public
name|void
name|execute
parameter_list|(
specifier|final
name|JobExecutionContext
name|context
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
name|context
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
name|context
operator|.
name|getJobDetail
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|CamelContext
name|camelContext
init|=
operator|(
name|CamelContext
operator|)
name|schedulerContext
operator|.
name|get
argument_list|(
name|QuartzEndpoint
operator|.
name|CONTEXT_KEY
argument_list|)
decl_stmt|;
name|String
name|endpointUri
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|getJobDetail
argument_list|()
operator|.
name|getJobDataMap
argument_list|()
operator|.
name|get
argument_list|(
name|QuartzEndpoint
operator|.
name|ENDPOINT_KEY
argument_list|)
decl_stmt|;
name|QuartzEndpoint
name|quartzEndpoint
init|=
operator|(
name|QuartzEndpoint
operator|)
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
name|quartzEndpoint
operator|.
name|onJobExecute
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

