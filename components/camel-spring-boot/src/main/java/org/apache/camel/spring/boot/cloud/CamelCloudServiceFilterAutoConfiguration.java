begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.cloud
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
name|Arrays
import|;
end_import

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
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
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
name|impl
operator|.
name|cloud
operator|.
name|BlacklistServiceFilter
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
name|cloud
operator|.
name|HealthyServiceFilter
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
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
name|StringHelper
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|BeansException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|BeanFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|BeanFactoryAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|config
operator|.
name|ConfigurableBeanFactory
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
name|Lazy
import|;
end_import

begin_class
annotation|@
name|Configuration
annotation|@
name|ConditionalOnBean
argument_list|(
name|CamelCloudAutoConfiguration
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
annotation|@
name|Conditional
argument_list|(
name|CamelCloudServiceFilterAutoConfiguration
operator|.
name|Condition
operator|.
name|class
argument_list|)
DECL|class|CamelCloudServiceFilterAutoConfiguration
specifier|public
class|class
name|CamelCloudServiceFilterAutoConfiguration
implements|implements
name|BeanFactoryAware
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelCloudServiceFilterAutoConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|beanFactory
specifier|private
name|BeanFactory
name|beanFactory
decl_stmt|;
annotation|@
name|Autowired
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Autowired
DECL|field|configurationProperties
specifier|private
name|CamelCloudConfigurationProperties
name|configurationProperties
decl_stmt|;
annotation|@
name|Override
DECL|method|setBeanFactory (BeanFactory factory)
specifier|public
name|void
name|setBeanFactory
parameter_list|(
name|BeanFactory
name|factory
parameter_list|)
throws|throws
name|BeansException
block|{
name|beanFactory
operator|=
name|factory
expr_stmt|;
block|}
annotation|@
name|Lazy
annotation|@
name|Bean
argument_list|(
name|name
operator|=
literal|"service-filter"
argument_list|)
DECL|method|serviceFilter ()
specifier|public
name|CamelCloudServiceFilter
name|serviceFilter
parameter_list|()
block|{
return|return
name|createServiceFilter
argument_list|(
name|configurationProperties
operator|.
name|getServiceFilter
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|PostConstruct
DECL|method|addServiceFilterConfigurations ()
specifier|public
name|void
name|addServiceFilterConfigurations
parameter_list|()
block|{
if|if
condition|(
operator|!
operator|(
name|beanFactory
operator|instanceof
name|ConfigurableBeanFactory
operator|)
condition|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"BeanFactory is not of type ConfigurableBeanFactory"
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|ConfigurableBeanFactory
name|factory
init|=
operator|(
name|ConfigurableBeanFactory
operator|)
name|beanFactory
decl_stmt|;
name|configurationProperties
operator|.
name|getServiceFilter
argument_list|()
operator|.
name|getConfigurations
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|forEach
argument_list|(
name|entry
lambda|->
name|registerBean
argument_list|(
name|factory
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// *******************************
comment|// Condition
comment|// *******************************
DECL|class|Condition
specifier|public
specifier|static
class|class
name|Condition
extends|extends
name|GroupCondition
block|{
DECL|method|Condition ()
specifier|public
name|Condition
parameter_list|()
block|{
name|super
argument_list|(
literal|"camel.cloud"
argument_list|,
literal|"camel.cloud.service-filter"
argument_list|)
expr_stmt|;
block|}
block|}
comment|// *******************************
comment|// Helper
comment|// *******************************
DECL|method|registerBean (ConfigurableBeanFactory factory, String name, CamelCloudConfigurationProperties.ServiceFilterConfiguration configuration)
specifier|private
name|void
name|registerBean
parameter_list|(
name|ConfigurableBeanFactory
name|factory
parameter_list|,
name|String
name|name
parameter_list|,
name|CamelCloudConfigurationProperties
operator|.
name|ServiceFilterConfiguration
name|configuration
parameter_list|)
block|{
name|factory
operator|.
name|registerSingleton
argument_list|(
name|name
argument_list|,
name|createServiceFilter
argument_list|(
name|configuration
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createServiceFilter (CamelCloudConfigurationProperties.ServiceFilterConfiguration configuration)
specifier|private
name|CamelCloudServiceFilter
name|createServiceFilter
parameter_list|(
name|CamelCloudConfigurationProperties
operator|.
name|ServiceFilterConfiguration
name|configuration
parameter_list|)
block|{
name|BlacklistServiceFilter
name|blacklist
init|=
operator|new
name|BlacklistServiceFilter
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|services
init|=
name|configuration
operator|.
name|getBlacklist
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|services
operator|.
name|entrySet
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|part
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|String
name|host
init|=
name|StringHelper
operator|.
name|before
argument_list|(
name|part
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|String
name|port
init|=
name|StringHelper
operator|.
name|after
argument_list|(
name|part
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|host
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|port
argument_list|)
condition|)
block|{
name|blacklist
operator|.
name|addServer
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|host
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
operator|new
name|CamelCloudServiceFilter
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|HealthyServiceFilter
argument_list|()
argument_list|,
name|blacklist
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

