begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.xquery.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|xquery
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
name|CamelContextAware
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
name|language
operator|.
name|xquery
operator|.
name|XQueryLanguage
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
name|LanguageCustomizer
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
name|LanguageConfigurationProperties
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
name|AllNestedConditions
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
name|Scope
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
name|XQueryLanguageAutoConfiguration
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
block|{
name|LanguageConfigurationProperties
operator|.
name|class
block|,
name|XQueryLanguageConfiguration
operator|.
name|class
block|}
argument_list|)
DECL|class|XQueryLanguageAutoConfiguration
specifier|public
class|class
name|XQueryLanguageAutoConfiguration
extends|extends
name|AllNestedConditions
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
name|XQueryLanguageAutoConfiguration
operator|.
name|class
argument_list|)
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
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|customizers
specifier|private
name|List
argument_list|<
name|LanguageCustomizer
argument_list|<
name|XQueryLanguage
argument_list|>
argument_list|>
name|customizers
decl_stmt|;
annotation|@
name|Autowired
DECL|field|globalConfiguration
specifier|private
name|LanguageConfigurationProperties
name|globalConfiguration
decl_stmt|;
annotation|@
name|Autowired
DECL|field|languageConfiguration
specifier|private
name|XQueryLanguageConfiguration
name|languageConfiguration
decl_stmt|;
DECL|method|XQueryLanguageAutoConfiguration ()
specifier|public
name|XQueryLanguageAutoConfiguration
parameter_list|()
block|{
name|super
argument_list|(
name|ConfigurationPhase
operator|.
name|REGISTER_BEAN
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ConditionalOnBean
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
DECL|class|OnCamelContext
specifier|public
specifier|static
class|class
name|OnCamelContext
block|{     }
annotation|@
name|ConditionalOnBean
argument_list|(
name|CamelAutoConfiguration
operator|.
name|class
argument_list|)
DECL|class|OnCamelAutoConfiguration
specifier|public
specifier|static
class|class
name|OnCamelAutoConfiguration
block|{     }
annotation|@
name|ConditionalOnBean
argument_list|(
name|CamelAutoConfiguration
operator|.
name|class
argument_list|)
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
literal|"camel.component"
argument_list|,
literal|"camel.component.xquery"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Bean
argument_list|(
name|name
operator|=
literal|"xquery-language"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
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
name|XQueryLanguage
operator|.
name|class
argument_list|)
DECL|method|configureXQueryLanguage ()
specifier|public
name|XQueryLanguage
name|configureXQueryLanguage
parameter_list|()
throws|throws
name|Exception
block|{
name|XQueryLanguage
name|language
init|=
operator|new
name|XQueryLanguage
argument_list|()
decl_stmt|;
if|if
condition|(
name|CamelContextAware
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|XQueryLanguage
operator|.
name|class
argument_list|)
condition|)
block|{
name|CamelContextAware
name|contextAware
init|=
name|CamelContextAware
operator|.
name|class
operator|.
name|cast
argument_list|(
name|language
argument_list|)
decl_stmt|;
if|if
condition|(
name|contextAware
operator|!=
literal|null
condition|)
block|{
name|contextAware
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
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
name|languageConfiguration
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
name|language
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|boolean
name|useConfigurers
init|=
name|globalConfiguration
operator|.
name|getConfigurer
argument_list|()
operator|.
name|isEnabled
argument_list|()
operator|&&
name|languageConfiguration
operator|.
name|getConfigurer
argument_list|()
operator|.
name|isEnabled
argument_list|()
decl_stmt|;
if|if
condition|(
name|useConfigurers
operator|&&
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
name|LanguageCustomizer
argument_list|<
name|XQueryLanguage
argument_list|>
name|configurer
range|:
name|customizers
control|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Configure language {}, with configurer {}"
argument_list|,
name|language
argument_list|,
name|configurer
argument_list|)
expr_stmt|;
name|configurer
operator|.
name|customize
argument_list|(
name|language
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|language
return|;
block|}
block|}
end_class

end_unit

