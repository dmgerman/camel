begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.foo.connector.springboot
package|package
name|org
operator|.
name|foo
operator|.
name|connector
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
name|component
operator|.
name|connector
operator|.
name|ConnectorCustomizer
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
name|util
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
name|foo
operator|.
name|connector
operator|.
name|PetStoreComponent
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
name|Lazy
import|;
end_import

begin_comment
comment|/**  * Generated by camel-connector-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.connector.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|Configuration
annotation|@
name|ConditionalOnBean
argument_list|(
name|type
operator|=
literal|"org.apache.camel.spring.boot.CamelAutoConfiguration"
argument_list|)
annotation|@
name|AutoConfigureAfter
argument_list|(
name|name
operator|=
literal|"org.apache.camel.spring.boot.CamelAutoConfiguration"
argument_list|)
annotation|@
name|EnableConfigurationProperties
argument_list|(
name|PetStoreConnectorConfiguration
operator|.
name|class
argument_list|)
DECL|class|PetStoreConnectorAutoConfiguration
specifier|public
class|class
name|PetStoreConnectorAutoConfiguration
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
name|PetStoreConnectorAutoConfiguration
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
name|PetStoreConnectorConfiguration
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
name|ConnectorCustomizer
argument_list|<
name|PetStoreComponent
argument_list|>
argument_list|>
name|customizers
decl_stmt|;
annotation|@
name|Lazy
annotation|@
name|Bean
argument_list|(
name|name
operator|=
literal|"petstore-component"
argument_list|)
annotation|@
name|ConditionalOnClass
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
annotation|@
name|ConditionalOnMissingBean
argument_list|(
name|name
operator|=
literal|"petstore-component"
argument_list|)
DECL|method|configurePetStoreComponent ()
specifier|public
name|PetStoreComponent
name|configurePetStoreComponent
parameter_list|()
throws|throws
name|Exception
block|{
name|PetStoreComponent
name|connector
init|=
operator|new
name|PetStoreComponent
argument_list|()
decl_stmt|;
name|connector
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
name|CamelPropertiesHelper
operator|.
name|setCamelProperties
argument_list|(
name|camelContext
argument_list|,
name|connector
argument_list|,
name|parameters
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setOptions
argument_list|(
name|parameters
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
name|ConnectorCustomizer
argument_list|<
name|PetStoreComponent
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
literal|"camel.connector.customizer"
argument_list|,
literal|"camel.connector.petstore.customizer"
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
literal|"camel.connector.customizer"
argument_list|,
literal|"camel.connector.petstore.customizer"
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
literal|"Configure connector {}, with customizer {}"
argument_list|,
name|connector
argument_list|,
name|customizer
argument_list|)
expr_stmt|;
name|customizer
operator|.
name|customize
argument_list|(
name|connector
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|connector
return|;
block|}
annotation|@
name|PostConstruct
DECL|method|postConstructPetStoreComponent ()
specifier|public
name|void
name|postConstructPetStoreComponent
parameter_list|()
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
name|PetStoreConnectorConfigurationCommon
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
name|parameters
operator|.
name|clear
argument_list|()
expr_stmt|;
name|PetStoreComponent
name|connector
init|=
operator|new
name|PetStoreComponent
argument_list|()
decl_stmt|;
name|connector
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
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
name|CamelPropertiesHelper
operator|.
name|setCamelProperties
argument_list|(
name|camelContext
argument_list|,
name|connector
argument_list|,
name|parameters
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setOptions
argument_list|(
name|parameters
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
name|ConnectorCustomizer
argument_list|<
name|PetStoreComponent
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
literal|"camel.connector.customizer"
argument_list|,
literal|"camel.connector.petstore."
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|".customizer"
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
literal|"camel.connector.customizer"
argument_list|,
literal|"camel.connector.petstore."
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|".customizer"
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
literal|"Configure connector {}, with customizer {}"
argument_list|,
name|connector
argument_list|,
name|customizer
argument_list|)
expr_stmt|;
name|customizer
operator|.
name|customize
argument_list|(
name|connector
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|camelContext
operator|.
name|addComponent
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|connector
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
end_class

end_unit

