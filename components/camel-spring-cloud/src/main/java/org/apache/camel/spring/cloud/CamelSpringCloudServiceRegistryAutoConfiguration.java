begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|cloud
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|cloud
operator|.
name|ServiceRegistry
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
name|CamelAutoConfiguration
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
name|cloud
operator|.
name|CamelCloudConfigurationProperties
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
name|util
operator|.
name|GroupCondition
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
name|autoconfigure
operator|.
name|AutoConfigureAfter
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
name|autoconfigure
operator|.
name|AutoConfigureBefore
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
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnBean
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
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnMissingBean
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
name|EnableConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|ServiceRegistryAutoConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Conditional
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_class
annotation|@
name|Configuration
annotation|@
name|AutoConfigureAfter
argument_list|(
name|ServiceRegistryAutoConfiguration
operator|.
name|class
argument_list|)
annotation|@
name|AutoConfigureBefore
argument_list|(
name|CamelAutoConfiguration
operator|.
name|class
argument_list|)
annotation|@
name|ConditionalOnBean
argument_list|(
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|ServiceRegistry
operator|.
name|class
argument_list|)
annotation|@
name|Conditional
argument_list|(
name|CamelSpringCloudServiceRegistryAutoConfiguration
operator|.
name|ServiceRegistryCondition
operator|.
name|class
argument_list|)
annotation|@
name|EnableConfigurationProperties
argument_list|(
name|CamelCloudConfigurationProperties
operator|.
name|class
argument_list|)
DECL|class|CamelSpringCloudServiceRegistryAutoConfiguration
specifier|public
class|class
name|CamelSpringCloudServiceRegistryAutoConfiguration
block|{
annotation|@
name|Bean
annotation|@
name|ConditionalOnMissingBean
DECL|method|camelServiceRegistry ( Collection<org.springframework.core.convert.ConversionService> conversionServices, org.springframework.cloud.client.serviceregistry.ServiceRegistry serviceRegistry)
specifier|public
name|ServiceRegistry
name|camelServiceRegistry
parameter_list|(
name|Collection
argument_list|<
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|convert
operator|.
name|ConversionService
argument_list|>
name|conversionServices
parameter_list|,
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|serviceregistry
operator|.
name|ServiceRegistry
name|serviceRegistry
parameter_list|)
block|{
return|return
operator|new
name|CamelSpringCloudServiceRegistry
argument_list|(
name|conversionServices
argument_list|,
name|serviceRegistry
argument_list|)
return|;
block|}
comment|// *******************************
comment|// Condition
comment|// *******************************
DECL|class|ServiceRegistryCondition
specifier|public
specifier|static
class|class
name|ServiceRegistryCondition
extends|extends
name|GroupCondition
block|{
DECL|method|ServiceRegistryCondition ()
specifier|public
name|ServiceRegistryCondition
parameter_list|()
block|{
name|super
argument_list|(
literal|"camel.cloud"
argument_list|,
literal|"camel.cloud.service-registry"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

