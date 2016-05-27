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
name|Component
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|DefaultEndpoint
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
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

begin_comment
comment|/**  * The spring-batch component allows to send messages to Spring Batch for further processing.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"spring-batch"
argument_list|,
name|title
operator|=
literal|"Spring Batch"
argument_list|,
name|syntax
operator|=
literal|"spring-batch:jobName"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"spring,batch,scheduling"
argument_list|)
DECL|class|SpringBatchEndpoint
specifier|public
class|class
name|SpringBatchEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|jobName
specifier|private
name|String
name|jobName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|jobFromHeader
specifier|private
name|Boolean
name|jobFromHeader
decl_stmt|;
comment|/**      * @deprecated will be removed in Camel 3.0      * use jobLauncher instead      */
annotation|@
name|Deprecated
DECL|field|jobLauncherRef
specifier|private
name|String
name|jobLauncherRef
decl_stmt|;
annotation|@
name|UriParam
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
DECL|field|job
specifier|private
name|Job
name|job
decl_stmt|;
DECL|method|SpringBatchEndpoint (String endpointUri, Component component, JobLauncher jobLauncher, JobLauncher defaultResolvedJobLauncher, Map<String, JobLauncher> allResolvedJobLaunchers, String jobName)
specifier|public
name|SpringBatchEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|JobLauncher
name|jobLauncher
parameter_list|,
name|JobLauncher
name|defaultResolvedJobLauncher
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|JobLauncher
argument_list|>
name|allResolvedJobLaunchers
parameter_list|,
name|String
name|jobName
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|jobLauncher
operator|=
name|jobLauncher
expr_stmt|;
name|this
operator|.
name|defaultResolvedJobLauncher
operator|=
name|defaultResolvedJobLauncher
expr_stmt|;
name|this
operator|.
name|allResolvedJobLaunchers
operator|=
name|allResolvedJobLaunchers
expr_stmt|;
name|this
operator|.
name|jobName
operator|=
name|jobName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|SpringBatchProducer
argument_list|(
name|this
argument_list|,
name|jobLauncher
argument_list|,
name|job
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
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
if|if
condition|(
name|jobLauncher
operator|==
literal|null
condition|)
block|{
name|jobLauncher
operator|=
name|resolveJobLauncher
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|job
operator|==
literal|null
operator|&&
name|jobName
operator|!=
literal|null
operator|&&
name|jobFromHeader
operator|==
literal|null
condition|)
block|{
name|job
operator|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|jobName
argument_list|,
name|Job
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|resolveJobLauncher ()
specifier|private
name|JobLauncher
name|resolveJobLauncher
parameter_list|()
block|{
if|if
condition|(
name|jobLauncherRef
operator|!=
literal|null
condition|)
block|{
name|JobLauncher
name|jobLauncher
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|jobLauncherRef
argument_list|,
name|JobLauncher
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|jobLauncher
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"No JobLauncher named %s found in the registry."
argument_list|,
name|jobLauncherRef
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|jobLauncher
return|;
block|}
if|if
condition|(
name|defaultResolvedJobLauncher
operator|!=
literal|null
condition|)
block|{
return|return
name|defaultResolvedJobLauncher
return|;
block|}
if|if
condition|(
name|allResolvedJobLaunchers
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|allResolvedJobLaunchers
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|allResolvedJobLaunchers
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Expected single jobLauncher instance. Found: "
operator|+
name|allResolvedJobLaunchers
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot find Spring Batch JobLauncher."
argument_list|)
throw|;
block|}
DECL|method|getJobName ()
specifier|public
name|String
name|getJobName
parameter_list|()
block|{
return|return
name|jobName
return|;
block|}
comment|/**      * The name of the Spring Batch job located in the registry.      */
DECL|method|setJobName (String jobName)
specifier|public
name|void
name|setJobName
parameter_list|(
name|String
name|jobName
parameter_list|)
block|{
name|this
operator|.
name|jobName
operator|=
name|jobName
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|getJobLauncherRef ()
specifier|public
name|String
name|getJobLauncherRef
parameter_list|()
block|{
return|return
name|jobLauncherRef
return|;
block|}
comment|/**      * Explicitly specifies a JobLauncher to be used looked up from the registry.      */
annotation|@
name|Deprecated
DECL|method|setJobLauncherRef (String jobLauncherRef)
specifier|public
name|void
name|setJobLauncherRef
parameter_list|(
name|String
name|jobLauncherRef
parameter_list|)
block|{
name|this
operator|.
name|jobLauncherRef
operator|=
name|jobLauncherRef
expr_stmt|;
block|}
DECL|method|getJobLauncher ()
specifier|public
name|JobLauncher
name|getJobLauncher
parameter_list|()
block|{
return|return
name|jobLauncher
return|;
block|}
comment|/**      * Explicitly specifies a JobLauncher to be used.      */
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
comment|/**      * Explicitly defines if the jobName shouls be taken from the headers instead of the URI.      */
DECL|method|setJobFromHeader (Boolean jobFromHeader)
specifier|public
name|void
name|setJobFromHeader
parameter_list|(
name|Boolean
name|jobFromHeader
parameter_list|)
block|{
name|this
operator|.
name|jobFromHeader
operator|=
name|jobFromHeader
expr_stmt|;
block|}
DECL|method|getJobFromHeader ()
specifier|public
name|Boolean
name|getJobFromHeader
parameter_list|()
block|{
return|return
name|jobFromHeader
return|;
block|}
block|}
end_class

end_unit

