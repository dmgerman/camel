begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The Validator component performs bean validation of the message body using  * the Java Bean Validation API.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|BeanValidatorEndpointBuilderFactory
specifier|public
interface|interface
name|BeanValidatorEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Bean Validator component.      */
DECL|interface|BeanValidatorEndpointBuilder
specifier|public
interface|interface
name|BeanValidatorEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedBeanValidatorEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedBeanValidatorEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Where label is an arbitrary text value describing the endpoint.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|label (String label)
specifier|default
name|BeanValidatorEndpointBuilder
name|label
parameter_list|(
name|String
name|label
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"label"
argument_list|,
name|label
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom ConstraintValidatorFactory.          * The option is a          *<code>javax.validation.ConstraintValidatorFactory</code> type.          * @group producer          */
DECL|method|constraintValidatorFactory ( Object constraintValidatorFactory)
specifier|default
name|BeanValidatorEndpointBuilder
name|constraintValidatorFactory
parameter_list|(
name|Object
name|constraintValidatorFactory
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"constraintValidatorFactory"
argument_list|,
name|constraintValidatorFactory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom ConstraintValidatorFactory.          * The option will be converted to a          *<code>javax.validation.ConstraintValidatorFactory</code> type.          * @group producer          */
DECL|method|constraintValidatorFactory ( String constraintValidatorFactory)
specifier|default
name|BeanValidatorEndpointBuilder
name|constraintValidatorFactory
parameter_list|(
name|String
name|constraintValidatorFactory
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"constraintValidatorFactory"
argument_list|,
name|constraintValidatorFactory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom validation group.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|group (String group)
specifier|default
name|BeanValidatorEndpointBuilder
name|group
parameter_list|(
name|String
name|group
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"group"
argument_list|,
name|group
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom MessageInterpolator.          * The option is a<code>javax.validation.MessageInterpolator</code>          * type.          * @group producer          */
DECL|method|messageInterpolator ( Object messageInterpolator)
specifier|default
name|BeanValidatorEndpointBuilder
name|messageInterpolator
parameter_list|(
name|Object
name|messageInterpolator
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"messageInterpolator"
argument_list|,
name|messageInterpolator
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom MessageInterpolator.          * The option will be converted to a          *<code>javax.validation.MessageInterpolator</code> type.          * @group producer          */
DECL|method|messageInterpolator ( String messageInterpolator)
specifier|default
name|BeanValidatorEndpointBuilder
name|messageInterpolator
parameter_list|(
name|String
name|messageInterpolator
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"messageInterpolator"
argument_list|,
name|messageInterpolator
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom TraversableResolver.          * The option is a<code>javax.validation.TraversableResolver</code>          * type.          * @group producer          */
DECL|method|traversableResolver ( Object traversableResolver)
specifier|default
name|BeanValidatorEndpointBuilder
name|traversableResolver
parameter_list|(
name|Object
name|traversableResolver
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"traversableResolver"
argument_list|,
name|traversableResolver
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom TraversableResolver.          * The option will be converted to a          *<code>javax.validation.TraversableResolver</code> type.          * @group producer          */
DECL|method|traversableResolver ( String traversableResolver)
specifier|default
name|BeanValidatorEndpointBuilder
name|traversableResolver
parameter_list|(
name|String
name|traversableResolver
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"traversableResolver"
argument_list|,
name|traversableResolver
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a a custom ValidationProviderResolver.          * The option is a          *<code>javax.validation.ValidationProviderResolver</code> type.          * @group producer          */
DECL|method|validationProviderResolver ( Object validationProviderResolver)
specifier|default
name|BeanValidatorEndpointBuilder
name|validationProviderResolver
parameter_list|(
name|Object
name|validationProviderResolver
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"validationProviderResolver"
argument_list|,
name|validationProviderResolver
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a a custom ValidationProviderResolver.          * The option will be converted to a          *<code>javax.validation.ValidationProviderResolver</code> type.          * @group producer          */
DECL|method|validationProviderResolver ( String validationProviderResolver)
specifier|default
name|BeanValidatorEndpointBuilder
name|validationProviderResolver
parameter_list|(
name|String
name|validationProviderResolver
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"validationProviderResolver"
argument_list|,
name|validationProviderResolver
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Bean Validator component.      */
DECL|interface|AdvancedBeanValidatorEndpointBuilder
specifier|public
interface|interface
name|AdvancedBeanValidatorEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|BeanValidatorEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|BeanValidatorEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedBeanValidatorEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedBeanValidatorEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedBeanValidatorEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedBeanValidatorEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * The Validator component performs bean validation of the message body      * using the Java Bean Validation API. Creates a builder to build endpoints      * for the Bean Validator component.      */
DECL|method|beanValidator (String path)
specifier|default
name|BeanValidatorEndpointBuilder
name|beanValidator
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|BeanValidatorEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|BeanValidatorEndpointBuilder
implements|,
name|AdvancedBeanValidatorEndpointBuilder
block|{
specifier|public
name|BeanValidatorEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"bean-validator"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|BeanValidatorEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

