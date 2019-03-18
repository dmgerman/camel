begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.springboot.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
operator|.
name|springboot
operator|.
name|health
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|spring
operator|.
name|boot
operator|.
name|health
operator|.
name|AbstractHealthCheckConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
literal|"camel.component.consul.health.check.repository"
argument_list|)
DECL|class|HealthCheckRepositoryConfiguration
specifier|public
class|class
name|HealthCheckRepositoryConfiguration
extends|extends
name|AbstractHealthCheckConfiguration
block|{
comment|/**      * Define the checks to include.      */
DECL|field|checks
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|checks
decl_stmt|;
comment|/**      * Health check configurations.      */
DECL|field|configurations
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|AbstractHealthCheckConfiguration
argument_list|>
name|configurations
decl_stmt|;
comment|// ******************************
comment|// Properties
comment|// ******************************
DECL|method|getChecks ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getChecks
parameter_list|()
block|{
return|return
name|checks
return|;
block|}
DECL|method|setChecks (List<String> checks)
specifier|public
name|void
name|setChecks
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|checks
parameter_list|)
block|{
name|this
operator|.
name|checks
operator|=
name|checks
expr_stmt|;
block|}
DECL|method|getConfigurations ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|AbstractHealthCheckConfiguration
argument_list|>
name|getConfigurations
parameter_list|()
block|{
return|return
name|configurations
return|;
block|}
DECL|method|setConfigurations (Map<String, AbstractHealthCheckConfiguration> configurations)
specifier|public
name|void
name|setConfigurations
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|AbstractHealthCheckConfiguration
argument_list|>
name|configurations
parameter_list|)
block|{
name|this
operator|.
name|configurations
operator|=
name|configurations
expr_stmt|;
block|}
block|}
end_class

end_unit

