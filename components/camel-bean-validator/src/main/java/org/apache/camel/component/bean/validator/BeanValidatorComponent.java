begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
operator|.
name|validator
package|;
end_package

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
name|validation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ConstraintValidatorFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|MessageInterpolator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|TraversableResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|Validation
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ValidationProviderResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ValidatorFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|bootstrap
operator|.
name|GenericBootstrap
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
name|Endpoint
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
name|DefaultComponent
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
name|ProcessorEndpoint
import|;
end_import

begin_comment
comment|/**  * Bean Validator Component for validating java beans against JSR 303 Validator  *  * @version   */
end_comment

begin_class
DECL|class|BeanValidatorComponent
specifier|public
class|class
name|BeanValidatorComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|BeanValidator
name|beanValidator
init|=
operator|new
name|BeanValidator
argument_list|()
decl_stmt|;
name|ValidationProviderResolver
name|validationProviderResolver
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"validationProviderResolver"
argument_list|,
name|ValidationProviderResolver
operator|.
name|class
argument_list|)
decl_stmt|;
name|MessageInterpolator
name|messageInterpolator
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"messageInterpolator"
argument_list|,
name|MessageInterpolator
operator|.
name|class
argument_list|)
decl_stmt|;
name|TraversableResolver
name|traversableResolver
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"traversableResolver"
argument_list|,
name|TraversableResolver
operator|.
name|class
argument_list|)
decl_stmt|;
name|ConstraintValidatorFactory
name|constraintValidatorFactory
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"constraintValidatorFactory"
argument_list|,
name|ConstraintValidatorFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|group
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"group"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|GenericBootstrap
name|bootstrap
init|=
name|Validation
operator|.
name|byDefaultProvider
argument_list|()
decl_stmt|;
if|if
condition|(
name|validationProviderResolver
operator|!=
literal|null
condition|)
block|{
name|bootstrap
operator|.
name|providerResolver
argument_list|(
name|validationProviderResolver
argument_list|)
expr_stmt|;
block|}
name|Configuration
argument_list|<
name|?
argument_list|>
name|configuration
init|=
name|bootstrap
operator|.
name|configure
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageInterpolator
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|messageInterpolator
argument_list|(
name|messageInterpolator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|traversableResolver
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|traversableResolver
argument_list|(
name|traversableResolver
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|constraintValidatorFactory
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|constraintValidatorFactory
argument_list|(
name|constraintValidatorFactory
argument_list|)
expr_stmt|;
block|}
name|ValidatorFactory
name|validatorFactory
init|=
name|configuration
operator|.
name|buildValidatorFactory
argument_list|()
decl_stmt|;
name|beanValidator
operator|.
name|setValidatorFactory
argument_list|(
name|validatorFactory
argument_list|)
expr_stmt|;
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
name|beanValidator
operator|.
name|setGroup
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|group
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ProcessorEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|beanValidator
argument_list|)
return|;
block|}
block|}
end_class

end_unit

