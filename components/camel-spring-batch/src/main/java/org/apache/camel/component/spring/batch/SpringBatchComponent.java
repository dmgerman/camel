begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.batch
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|batch
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
name|Endpoint
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
name|CamelContextHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|core
operator|.
name|Job
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|core
operator|.
name|launch
operator|.
name|JobLauncher
import|;
end_import

begin_class
DECL|class|SpringBatchComponent
specifier|public
class|class
name|SpringBatchComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|DEFAULT_JOB_LAUNCHER_REF_NAME
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_JOB_LAUNCHER_REF_NAME
init|=
literal|"jobLauncher"
decl_stmt|;
DECL|field|jobLauncher
specifier|private
name|JobLauncher
name|jobLauncher
decl_stmt|;
DECL|field|defaultResolvedJobLauncher
specifier|private
name|JobLauncher
name|defaultResolvedJobLauncher
decl_stmt|;
DECL|field|allResolvedJobLaunchers
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|JobLauncher
argument_list|>
name|allResolvedJobLaunchers
decl_stmt|;
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
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
name|Job
name|resolvedJob
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|remaining
argument_list|,
name|Job
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|new
name|SpringBatchEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|jobLauncher
argument_list|,
name|defaultResolvedJobLauncher
argument_list|,
name|allResolvedJobLaunchers
argument_list|,
name|resolvedJob
argument_list|)
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
name|defaultResolvedJobLauncher
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|DEFAULT_JOB_LAUNCHER_REF_NAME
argument_list|,
name|JobLauncher
operator|.
name|class
argument_list|)
expr_stmt|;
name|allResolvedJobLaunchers
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByType
argument_list|(
name|JobLauncher
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|setJobLauncher (JobLauncher jobLauncher)
specifier|public
name|void
name|setJobLauncher
parameter_list|(
name|JobLauncher
name|jobLauncher
parameter_list|)
block|{
name|this
operator|.
name|jobLauncher
operator|=
name|jobLauncher
expr_stmt|;
block|}
block|}
end_class

end_unit

