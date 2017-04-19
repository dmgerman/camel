begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.castor.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|castor
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
name|RuntimeCamelException
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
name|dataformat
operator|.
name|castor
operator|.
name|CastorDataFormat
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
name|DataFormat
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
name|DataFormatFactory
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
name|ConditionMessage
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
name|ConditionOutcome
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
name|SpringBootCondition
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
name|bind
operator|.
name|RelaxedPropertyResolver
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
name|ConditionContext
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
name|core
operator|.
name|type
operator|.
name|AnnotatedTypeMetadata
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
name|Conditional
argument_list|(
name|CastorDataFormatAutoConfiguration
operator|.
name|Condition
operator|.
name|class
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
name|CastorDataFormatConfiguration
operator|.
name|class
argument_list|)
DECL|class|CastorDataFormatAutoConfiguration
specifier|public
class|class
name|CastorDataFormatAutoConfiguration
block|{
annotation|@
name|Bean
argument_list|(
name|name
operator|=
literal|"castor-dataformat-factory"
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
name|CastorDataFormat
operator|.
name|class
argument_list|)
DECL|method|configureCastorDataFormatFactory ( final CamelContext camelContext, final CastorDataFormatConfiguration configuration)
specifier|public
name|DataFormatFactory
name|configureCastorDataFormatFactory
parameter_list|(
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|CastorDataFormatConfiguration
name|configuration
parameter_list|)
block|{
return|return
operator|new
name|DataFormatFactory
argument_list|()
block|{
specifier|public
name|DataFormat
name|newInstance
parameter_list|()
block|{
name|CastorDataFormat
name|dataformat
init|=
operator|new
name|CastorDataFormat
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
name|CastorDataFormat
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
name|dataformat
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
try|try
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
name|dataformat
argument_list|,
name|parameters
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
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|dataformat
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
DECL|class|Condition
specifier|public
specifier|static
class|class
name|Condition
extends|extends
name|SpringBootCondition
block|{
annotation|@
name|Override
DECL|method|getMatchOutcome ( ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata)
specifier|public
name|ConditionOutcome
name|getMatchOutcome
parameter_list|(
name|ConditionContext
name|conditionContext
parameter_list|,
name|AnnotatedTypeMetadata
name|annotatedTypeMetadata
parameter_list|)
block|{
name|boolean
name|groupEnabled
init|=
name|isEnabled
argument_list|(
name|conditionContext
argument_list|,
literal|"camel.dataformat."
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ConditionMessage
operator|.
name|Builder
name|message
init|=
name|ConditionMessage
operator|.
name|forCondition
argument_list|(
literal|"camel.dataformat.castor"
argument_list|)
decl_stmt|;
if|if
condition|(
name|isEnabled
argument_list|(
name|conditionContext
argument_list|,
literal|"camel.dataformat.castor."
argument_list|,
name|groupEnabled
argument_list|)
condition|)
block|{
return|return
name|ConditionOutcome
operator|.
name|match
argument_list|(
name|message
operator|.
name|because
argument_list|(
literal|"enabled"
argument_list|)
argument_list|)
return|;
block|}
return|return
name|ConditionOutcome
operator|.
name|noMatch
argument_list|(
name|message
operator|.
name|because
argument_list|(
literal|"not enabled"
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isEnabled ( org.springframework.context.annotation.ConditionContext context, java.lang.String prefix, boolean defaultValue)
specifier|private
name|boolean
name|isEnabled
parameter_list|(
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|ConditionContext
name|context
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|prefix
parameter_list|,
name|boolean
name|defaultValue
parameter_list|)
block|{
name|RelaxedPropertyResolver
name|resolver
init|=
operator|new
name|RelaxedPropertyResolver
argument_list|(
name|context
operator|.
name|getEnvironment
argument_list|()
argument_list|,
name|prefix
argument_list|)
decl_stmt|;
return|return
name|resolver
operator|.
name|getProperty
argument_list|(
literal|"enabled"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

