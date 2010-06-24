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
name|Serializable
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelJob
specifier|public
class|class
name|CamelJob
implements|implements
name|Job
implements|,
name|Serializable
block|{
DECL|method|execute (JobExecutionContext context)
specifier|public
name|void
name|execute
parameter_list|(
name|JobExecutionContext
name|context
parameter_list|)
throws|throws
name|JobExecutionException
block|{
name|QuartzEndpoint
name|endpoint
init|=
operator|(
name|QuartzEndpoint
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
name|QuartzConstants
operator|.
name|QUARTZ_ENDPOINT
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|JobExecutionException
argument_list|(
literal|"No quartz endpoint available for key: "
operator|+
name|QuartzConstants
operator|.
name|QUARTZ_ENDPOINT
argument_list|)
throw|;
block|}
name|endpoint
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

