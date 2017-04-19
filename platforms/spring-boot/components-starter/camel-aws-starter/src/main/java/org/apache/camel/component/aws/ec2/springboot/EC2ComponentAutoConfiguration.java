begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ec2.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|ec2
operator|.
name|springboot
package|;
end_package

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
name|aws
operator|.
name|ec2
operator|.
name|EC2Component
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
name|context
operator|.
name|annotation
operator|.
name|Lazy
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
name|EC2ComponentAutoConfiguration
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
DECL|class|EC2ComponentAutoConfiguration
specifier|public
class|class
name|EC2ComponentAutoConfiguration
block|{
annotation|@
name|Lazy
annotation|@
name|Bean
argument_list|(
name|name
operator|=
literal|"aws-ec2-component"
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
name|EC2Component
operator|.
name|class
argument_list|)
DECL|method|configureEC2Component (CamelContext camelContext)
specifier|public
name|EC2Component
name|configureEC2Component
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
name|EC2Component
name|component
init|=
operator|new
name|EC2Component
argument_list|()
decl_stmt|;
name|component
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
return|return
name|component
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
literal|"camel.component."
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
literal|"camel.component.aws-ec2"
argument_list|)
decl_stmt|;
if|if
condition|(
name|isEnabled
argument_list|(
name|conditionContext
argument_list|,
literal|"camel.component.aws-ec2."
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

