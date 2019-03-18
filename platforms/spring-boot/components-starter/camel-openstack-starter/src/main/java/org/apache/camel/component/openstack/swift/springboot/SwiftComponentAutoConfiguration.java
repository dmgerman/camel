begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.swift.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|openstack
operator|.
name|swift
operator|.
name|springboot
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
name|Generated
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
name|component
operator|.
name|openstack
operator|.
name|swift
operator|.
name|SwiftComponent
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
name|ComponentCustomizer
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
name|HasId
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
name|ComponentConfigurationProperties
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
name|CamelPropertiesHelper
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
name|ConditionalOnCamelContextAndAutoConfigurationBeans
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
name|spring
operator|.
name|boot
operator|.
name|util
operator|.
name|HierarchicalPropertiesEvaluator
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

begin_comment
comment|/**  * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|Configuration
annotation|@
name|Conditional
argument_list|(
block|{
name|ConditionalOnCamelContextAndAutoConfigurationBeans
operator|.
name|class
block|,
name|SwiftComponentAutoConfiguration
operator|.
name|GroupConditions
operator|.
name|class
block|}
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
block|{
name|ComponentConfigurationProperties
operator|.
name|class
block|,
name|SwiftComponentConfiguration
operator|.
name|class
block|}
argument_list|)
DECL|class|SwiftComponentAutoConfiguration
specifier|public
class|class
name|SwiftComponentAutoConfiguration
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
name|SwiftComponentAutoConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Autowired
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
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
DECL|field|configuration
specifier|private
name|SwiftComponentConfiguration
name|configuration
decl_stmt|;
annotation|@
name|Autowired
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|customizers
specifier|private
name|List
argument_list|<
name|ComponentCustomizer
argument_list|<
name|SwiftComponent
argument_list|>
argument_list|>
name|customizers
decl_stmt|;
DECL|class|GroupConditions
specifier|static
class|class
name|GroupConditions
extends|extends
name|GroupCondition
block|{
DECL|method|GroupConditions ()
specifier|public
name|GroupConditions
parameter_list|()
block|{
name|super
argument_list|(
literal|"camel.component"
argument_list|,
literal|"camel.component.openstack-swift"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Lazy
annotation|@
name|Bean
argument_list|(
name|name
operator|=
literal|"openstack-swift-component"
argument_list|)
annotation|@
name|ConditionalOnMissingBean
argument_list|(
name|SwiftComponent
operator|.
name|class
argument_list|)
DECL|method|configureSwiftComponent ()
specifier|public
name|SwiftComponent
name|configureSwiftComponent
parameter_list|()
throws|throws
name|Exception
block|{
name|SwiftComponent
name|component
init|=
operator|new
name|SwiftComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
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
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|parameters
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|paramClass
init|=
name|value
operator|.
name|getClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|paramClass
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"NestedConfiguration"
argument_list|)
condition|)
block|{
name|Class
name|nestedClass
init|=
literal|null
decl_stmt|;
try|try
block|{
name|nestedClass
operator|=
operator|(
name|Class
operator|)
name|paramClass
operator|.
name|getDeclaredField
argument_list|(
literal|"CAMEL_NESTED_CLASS"
argument_list|)
operator|.
name|get
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|nestedParameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|value
argument_list|,
name|nestedParameters
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Object
name|nestedProperty
init|=
name|nestedClass
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|CamelPropertiesHelper
operator|.
name|setCamelProperties
argument_list|(
name|camelContext
argument_list|,
name|nestedProperty
argument_list|,
name|nestedParameters
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setValue
argument_list|(
name|nestedProperty
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchFieldException
name|e
parameter_list|)
block|{                 }
block|}
block|}
name|CamelPropertiesHelper
operator|.
name|setCamelProperties
argument_list|(
name|camelContext
argument_list|,
name|component
argument_list|,
name|parameters
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|customizers
argument_list|)
condition|)
block|{
for|for
control|(
name|ComponentCustomizer
argument_list|<
name|SwiftComponent
argument_list|>
name|customizer
range|:
name|customizers
control|)
block|{
name|boolean
name|useCustomizer
init|=
operator|(
name|customizer
operator|instanceof
name|HasId
operator|)
condition|?
name|HierarchicalPropertiesEvaluator
operator|.
name|evaluate
argument_list|(
name|applicationContext
operator|.
name|getEnvironment
argument_list|()
argument_list|,
literal|"camel.component.customizer"
argument_list|,
literal|"camel.component.openstack-swift.customizer"
argument_list|,
operator|(
operator|(
name|HasId
operator|)
name|customizer
operator|)
operator|.
name|getId
argument_list|()
argument_list|)
else|:
name|HierarchicalPropertiesEvaluator
operator|.
name|evaluate
argument_list|(
name|applicationContext
operator|.
name|getEnvironment
argument_list|()
argument_list|,
literal|"camel.component.customizer"
argument_list|,
literal|"camel.component.openstack-swift.customizer"
argument_list|)
decl_stmt|;
if|if
condition|(
name|useCustomizer
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Configure component {}, with customizer {}"
argument_list|,
name|component
argument_list|,
name|customizer
argument_list|)
expr_stmt|;
name|customizer
operator|.
name|customize
argument_list|(
name|component
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|component
return|;
block|}
block|}
end_class

end_unit

