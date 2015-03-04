begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|ConsumerTemplate
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
name|properties
operator|.
name|PropertiesComponent
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
name|properties
operator|.
name|PropertiesParser
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
name|CamelBeanPostProcessor
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
name|SpringCamelContext
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
name|context
operator|.
name|ApplicationContext
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
name|Configuration
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
name|Import
import|;
end_import

begin_class
annotation|@
name|Configuration
annotation|@
name|EnableConfigurationProperties
argument_list|(
name|CamelConfigurationProperties
operator|.
name|class
argument_list|)
annotation|@
name|Import
argument_list|(
name|TypeConversionConfiguration
operator|.
name|class
argument_list|)
DECL|class|CamelAutoConfiguration
specifier|public
class|class
name|CamelAutoConfiguration
block|{
comment|/**      * Spring-aware Camel context for the application. Auto-detects and loads all routes available in the Spring      * context.      */
annotation|@
name|Bean
DECL|method|camelContext (ApplicationContext applicationContext, CamelConfigurationProperties configurationProperties)
name|CamelContext
name|camelContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|,
name|CamelConfigurationProperties
name|configurationProperties
parameter_list|)
block|{
name|CamelContext
name|camelContext
init|=
operator|new
name|SpringCamelContext
argument_list|(
name|applicationContext
argument_list|)
decl_stmt|;
name|SpringCamelContext
operator|.
name|setNoStart
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|configurationProperties
operator|.
name|isJmxEnabled
argument_list|()
condition|)
block|{
name|camelContext
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|configurationProperties
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|SpringCamelContext
operator|)
name|camelContext
operator|)
operator|.
name|setName
argument_list|(
name|configurationProperties
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|camelContext
return|;
block|}
annotation|@
name|Bean
annotation|@
name|ConditionalOnMissingBean
argument_list|(
name|RoutesCollector
operator|.
name|class
argument_list|)
DECL|method|routesCollector (ApplicationContext applicationContext)
name|RoutesCollector
name|routesCollector
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
block|{
name|Collection
argument_list|<
name|CamelContextConfiguration
argument_list|>
name|configurations
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|CamelContextConfiguration
operator|.
name|class
argument_list|)
operator|.
name|values
argument_list|()
decl_stmt|;
return|return
operator|new
name|RoutesCollector
argument_list|(
name|applicationContext
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|CamelContextConfiguration
argument_list|>
argument_list|(
name|configurations
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Default producer template for the bootstrapped Camel context.      */
annotation|@
name|Bean
DECL|method|producerTemplate (CamelContext camelContext, CamelConfigurationProperties configurationProperties)
name|ProducerTemplate
name|producerTemplate
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|CamelConfigurationProperties
name|configurationProperties
parameter_list|)
block|{
return|return
name|camelContext
operator|.
name|createProducerTemplate
argument_list|(
name|configurationProperties
operator|.
name|getProducerTemplateCacheSize
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Default consumer template for the bootstrapped Camel context.      */
annotation|@
name|Bean
DECL|method|consumerTemplate (CamelContext camelContext, CamelConfigurationProperties configurationProperties)
name|ConsumerTemplate
name|consumerTemplate
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|CamelConfigurationProperties
name|configurationProperties
parameter_list|)
block|{
return|return
name|camelContext
operator|.
name|createConsumerTemplate
argument_list|(
name|configurationProperties
operator|.
name|getConsumerTemplateCacheSize
argument_list|()
argument_list|)
return|;
block|}
comment|// SpringCamelContext integration
annotation|@
name|Bean
DECL|method|propertiesParser ()
name|PropertiesParser
name|propertiesParser
parameter_list|()
block|{
return|return
operator|new
name|SpringPropertiesParser
argument_list|()
return|;
block|}
annotation|@
name|Bean
DECL|method|properties ()
name|PropertiesComponent
name|properties
parameter_list|()
block|{
name|PropertiesComponent
name|properties
init|=
operator|new
name|PropertiesComponent
argument_list|()
decl_stmt|;
name|properties
operator|.
name|setPropertiesParser
argument_list|(
name|propertiesParser
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|properties
return|;
block|}
comment|/**      * Camel post processor - required to support Camel annotations.      */
annotation|@
name|Bean
DECL|method|camelBeanPostProcessor (ApplicationContext applicationContext)
name|CamelBeanPostProcessor
name|camelBeanPostProcessor
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
block|{
name|CamelBeanPostProcessor
name|processor
init|=
operator|new
name|CamelBeanPostProcessor
argument_list|()
decl_stmt|;
name|processor
operator|.
name|setApplicationContext
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
return|return
name|processor
return|;
block|}
block|}
end_class

end_unit

