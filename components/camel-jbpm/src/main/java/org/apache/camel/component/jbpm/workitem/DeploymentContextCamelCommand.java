begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbpm.workitem
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbpm
operator|.
name|workitem
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|ProducerTemplate
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
name|component
operator|.
name|jbpm
operator|.
name|JBPMConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jbpm
operator|.
name|services
operator|.
name|api
operator|.
name|service
operator|.
name|ServiceRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kie
operator|.
name|api
operator|.
name|executor
operator|.
name|CommandContext
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

begin_comment
comment|/**  * CamelCommand that uses the {@link CamelContext} registered on the {@link ServiceRegistry} for this specific deployment.  */
end_comment

begin_class
DECL|class|DeploymentContextCamelCommand
specifier|public
class|class
name|DeploymentContextCamelCommand
extends|extends
name|AbstractCamelCommand
block|{
DECL|field|templates
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ProducerTemplate
argument_list|>
name|templates
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DeploymentContextCamelCommand
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|getProducerTemplate (CommandContext ctx)
specifier|protected
name|ProducerTemplate
name|getProducerTemplate
parameter_list|(
name|CommandContext
name|ctx
parameter_list|)
block|{
name|String
name|deploymentId
init|=
operator|(
name|String
operator|)
name|ctx
operator|.
name|getData
argument_list|(
literal|"deploymentId"
argument_list|)
decl_stmt|;
name|ProducerTemplate
name|template
init|=
name|templates
operator|.
name|get
argument_list|(
name|deploymentId
argument_list|)
decl_stmt|;
if|if
condition|(
name|template
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|template
operator|=
name|templates
operator|.
name|get
argument_list|(
name|deploymentId
argument_list|)
expr_stmt|;
if|if
condition|(
name|template
operator|==
literal|null
condition|)
block|{
name|CamelContext
name|deploymentCamelContext
init|=
operator|(
name|CamelContext
operator|)
name|ServiceRegistry
operator|.
name|get
argument_list|()
operator|.
name|service
argument_list|(
name|deploymentId
operator|+
name|JBPMConstants
operator|.
name|DEPLOYMENT_CAMEL_CONTEXT_SERVICE_KEY_POSTFIX
argument_list|)
decl_stmt|;
name|template
operator|=
name|deploymentCamelContext
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|templates
operator|.
name|put
argument_list|(
name|deploymentId
argument_list|,
name|template
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|template
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
for|for
control|(
name|ProducerTemplate
name|nextTemplate
range|:
name|templates
operator|.
name|values
argument_list|()
control|)
block|{
try|try
block|{
name|nextTemplate
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Error encountered while closing the Camel Producer Template."
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// Not much we can do here, so swallowing exception.
block|}
block|}
block|}
block|}
end_class

end_unit

