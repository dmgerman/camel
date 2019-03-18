begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.etcd.springboot.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|etcd
operator|.
name|springboot
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
name|HashMap
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
name|cloud
operator|.
name|ServiceDiscovery
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
name|etcd
operator|.
name|cloud
operator|.
name|EtcdServiceDiscoveryFactory
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
name|model
operator|.
name|cloud
operator|.
name|springboot
operator|.
name|EtcdServiceCallServiceDiscoveryConfigurationCommon
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
name|model
operator|.
name|cloud
operator|.
name|springboot
operator|.
name|EtcdServiceCallServiceDiscoveryConfigurationProperties
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
name|support
operator|.
name|IntrospectionSupport
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
name|BeanCreationException
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
name|ConditionalOnClass
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
name|CamelAutoConfiguration
operator|.
name|class
argument_list|)
annotation|@
name|Conditional
argument_list|(
name|EtcdCloudAutoConfiguration
operator|.
name|Condition
operator|.
name|class
argument_list|)
annotation|@
name|AutoConfigureAfter
argument_list|(
name|CamelAutoConfiguration
operator|.
name|class
argument_list|)
annotation|@
name|EnableConfigurationProperties
argument_list|(
name|EtcdServiceCallServiceDiscoveryConfigurationProperties
operator|.
name|class
argument_list|)
DECL|class|EtcdCloudAutoConfiguration
specifier|public
class|class
name|EtcdCloudAutoConfiguration
block|{
annotation|@
name|Autowired
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Autowired
DECL|field|configuration
specifier|private
name|EtcdServiceCallServiceDiscoveryConfigurationProperties
name|configuration
decl_stmt|;
annotation|@
name|Autowired
DECL|field|beanFactory
specifier|private
name|ConfigurableBeanFactory
name|beanFactory
decl_stmt|;
annotation|@
name|Lazy
annotation|@
name|Bean
argument_list|(
name|name
operator|=
literal|"etcd-service-discovery"
argument_list|)
annotation|@
name|ConditionalOnClass
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
DECL|method|configureServiceDiscoveryFactory ()
specifier|public
name|ServiceDiscovery
name|configureServiceDiscoveryFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|EtcdServiceDiscoveryFactory
name|factory
init|=
operator|new
name|EtcdServiceDiscoveryFactory
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|camelContext
argument_list|,
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|factory
argument_list|,
name|IntrospectionSupport
operator|.
name|getNonNullProperties
argument_list|(
name|configuration
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|newInstance
argument_list|(
name|camelContext
argument_list|)
return|;
block|}
annotation|@
name|PostConstruct
DECL|method|postConstruct ()
specifier|public
name|void
name|postConstruct
parameter_list|()
block|{
if|if
condition|(
name|beanFactory
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<>
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
name|EtcdServiceCallServiceDiscoveryConfigurationCommon
argument_list|>
name|entry
range|:
name|configuration
operator|.
name|getConfigurations
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
comment|// clean up params
name|parameters
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// The instance factory
name|EtcdServiceDiscoveryFactory
name|factory
init|=
operator|new
name|EtcdServiceDiscoveryFactory
argument_list|()
decl_stmt|;
try|try
block|{
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|parameters
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|camelContext
argument_list|,
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|factory
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|beanFactory
operator|.
name|registerSingleton
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|factory
operator|.
name|newInstance
argument_list|(
name|camelContext
argument_list|)
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
name|BeanCreationException
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
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
literal|"camel.cloud.etcd"
argument_list|,
literal|"camel.cloud.etcd.service-discovery"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

