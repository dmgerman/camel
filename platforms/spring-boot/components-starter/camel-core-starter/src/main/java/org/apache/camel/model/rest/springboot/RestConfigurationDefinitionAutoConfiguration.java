begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.rest.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|rest
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
name|model
operator|.
name|rest
operator|.
name|RestConstants
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
name|RestConfiguration
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
name|CollectionHelper
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
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnProperty
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
name|ConditionalOnBean
argument_list|(
name|type
operator|=
literal|"org.apache.camel.spring.boot.CamelAutoConfiguration"
argument_list|)
annotation|@
name|ConditionalOnProperty
argument_list|(
name|name
operator|=
literal|"camel.rest.enabled"
argument_list|,
name|matchIfMissing
operator|=
literal|true
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
name|RestConfigurationDefinitionProperties
operator|.
name|class
argument_list|)
DECL|class|RestConfigurationDefinitionAutoConfiguration
specifier|public
class|class
name|RestConfigurationDefinitionAutoConfiguration
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
DECL|field|config
specifier|private
name|RestConfigurationDefinitionProperties
name|config
decl_stmt|;
annotation|@
name|Lazy
annotation|@
name|Bean
argument_list|(
name|name
operator|=
name|RestConstants
operator|.
name|DEFAULT_REST_CONFIGURATION_ID
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
DECL|method|configureRestConfigurationDefinition ()
specifier|public
name|RestConfiguration
name|configureRestConfigurationDefinition
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
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
name|config
argument_list|,
name|properties
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// These options is configured specially further below, so remove them first
name|properties
operator|.
name|remove
argument_list|(
literal|"enableCors"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|remove
argument_list|(
literal|"apiProperty"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|remove
argument_list|(
literal|"componentProperty"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|remove
argument_list|(
literal|"consumerProperty"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|remove
argument_list|(
literal|"dataFormatProperty"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|remove
argument_list|(
literal|"endpointProperty"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|remove
argument_list|(
literal|"corsHeaders"
argument_list|)
expr_stmt|;
name|RestConfiguration
name|definition
init|=
operator|new
name|RestConfiguration
argument_list|()
decl_stmt|;
name|CamelPropertiesHelper
operator|.
name|setCamelProperties
argument_list|(
name|camelContext
argument_list|,
name|definition
argument_list|,
name|properties
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// Workaround for spring-boot properties name as It would appear
comment|// as enable-c-o-r-s if left uppercase in Configuration
name|definition
operator|.
name|setEnableCORS
argument_list|(
name|config
operator|.
name|getEnableCors
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|getApiProperty
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|definition
operator|.
name|setApiProperties
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|CollectionHelper
operator|.
name|flattenKeysInMap
argument_list|(
name|config
operator|.
name|getApiProperty
argument_list|()
argument_list|,
literal|"."
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getComponentProperty
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|definition
operator|.
name|setComponentProperties
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|CollectionHelper
operator|.
name|flattenKeysInMap
argument_list|(
name|config
operator|.
name|getComponentProperty
argument_list|()
argument_list|,
literal|"."
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getConsumerProperty
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|definition
operator|.
name|setConsumerProperties
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|CollectionHelper
operator|.
name|flattenKeysInMap
argument_list|(
name|config
operator|.
name|getConsumerProperty
argument_list|()
argument_list|,
literal|"."
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getDataFormatProperty
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|definition
operator|.
name|setDataFormatProperties
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|CollectionHelper
operator|.
name|flattenKeysInMap
argument_list|(
name|config
operator|.
name|getDataFormatProperty
argument_list|()
argument_list|,
literal|"."
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getEndpointProperty
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|definition
operator|.
name|setEndpointProperties
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|CollectionHelper
operator|.
name|flattenKeysInMap
argument_list|(
name|config
operator|.
name|getEndpointProperty
argument_list|()
argument_list|,
literal|"."
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getCorsHeaders
argument_list|()
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
name|map
init|=
name|CollectionHelper
operator|.
name|flattenKeysInMap
argument_list|(
name|config
operator|.
name|getCorsHeaders
argument_list|()
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|target
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|forEach
argument_list|(
parameter_list|(
name|k
parameter_list|,
name|v
parameter_list|)
lambda|->
name|target
operator|.
name|put
argument_list|(
name|k
argument_list|,
name|v
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|definition
operator|.
name|setCorsHeaders
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
return|return
name|definition
return|;
block|}
block|}
end_class

end_unit

