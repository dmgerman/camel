begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Component
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|Metadata
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
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
name|DefaultEndpoint
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
name|PlatformHelper
import|;
end_import

begin_import
import|import static
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
operator|.
name|ValidatorFactories
operator|.
name|buildValidatorFactory
import|;
end_import

begin_comment
comment|/**  * The Validator component performs bean validation of the message body using the Java Bean Validation API.  *  * Camel uses the reference implementation, which is Hibernate Validator.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.3.0"
argument_list|,
name|scheme
operator|=
literal|"bean-validator"
argument_list|,
name|title
operator|=
literal|"Bean Validator"
argument_list|,
name|syntax
operator|=
literal|"bean-validator:label"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"validation"
argument_list|)
DECL|class|BeanValidatorEndpoint
specifier|public
class|class
name|BeanValidatorEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Where label is an arbitrary text value describing the endpoint"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|label
specifier|private
name|String
name|label
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"javax.validation.groups.Default"
argument_list|)
DECL|field|group
specifier|private
name|String
name|group
decl_stmt|;
annotation|@
name|UriParam
DECL|field|ignoreXmlConfiguration
specifier|private
name|boolean
name|ignoreXmlConfiguration
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|validationProviderResolver
specifier|private
name|ValidationProviderResolver
name|validationProviderResolver
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|messageInterpolator
specifier|private
name|MessageInterpolator
name|messageInterpolator
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|traversableResolver
specifier|private
name|TraversableResolver
name|traversableResolver
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|constraintValidatorFactory
specifier|private
name|ConstraintValidatorFactory
name|constraintValidatorFactory
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|validatorFactory
specifier|private
name|ValidatorFactory
name|validatorFactory
decl_stmt|;
DECL|method|BeanValidatorEndpoint (String endpointUri, Component component)
specifier|public
name|BeanValidatorEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanValidatorProducer
name|producer
init|=
operator|new
name|BeanValidatorProducer
argument_list|(
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
name|producer
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
name|ValidatorFactory
name|validatorFactory
init|=
name|this
operator|.
name|validatorFactory
decl_stmt|;
if|if
condition|(
name|validatorFactory
operator|==
literal|null
condition|)
block|{
name|validatorFactory
operator|=
name|buildValidatorFactory
argument_list|(
name|isOsgiContext
argument_list|()
argument_list|,
name|isIgnoreXmlConfiguration
argument_list|()
argument_list|,
name|validationProviderResolver
argument_list|,
name|messageInterpolator
argument_list|,
name|traversableResolver
argument_list|,
name|constraintValidatorFactory
argument_list|)
expr_stmt|;
block|}
name|producer
operator|.
name|setValidatorFactory
argument_list|(
name|validatorFactory
argument_list|)
expr_stmt|;
return|return
name|producer
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Consumer is not supported"
argument_list|)
throw|;
block|}
comment|/**      * Recognizes if component is executed in the OSGi environment.      *      * @return true if component is executed in the OSGi environment. False otherwise.      */
DECL|method|isOsgiContext ()
specifier|protected
name|boolean
name|isOsgiContext
parameter_list|()
block|{
return|return
name|PlatformHelper
operator|.
name|isOsgiContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
name|label
return|;
block|}
DECL|method|setLabel (String label)
specifier|public
name|void
name|setLabel
parameter_list|(
name|String
name|label
parameter_list|)
block|{
name|this
operator|.
name|label
operator|=
name|label
expr_stmt|;
block|}
DECL|method|getGroup ()
specifier|public
name|String
name|getGroup
parameter_list|()
block|{
return|return
name|group
return|;
block|}
comment|/**      * To use a custom validation group      */
DECL|method|setGroup (String group)
specifier|public
name|void
name|setGroup
parameter_list|(
name|String
name|group
parameter_list|)
block|{
name|this
operator|.
name|group
operator|=
name|group
expr_stmt|;
block|}
DECL|method|isIgnoreXmlConfiguration ()
specifier|public
name|boolean
name|isIgnoreXmlConfiguration
parameter_list|()
block|{
return|return
name|ignoreXmlConfiguration
return|;
block|}
comment|/**      * Whether to ignore data from the META-INF/validation.xml file.      */
DECL|method|setIgnoreXmlConfiguration (boolean ignoreXmlConfiguration)
specifier|public
name|void
name|setIgnoreXmlConfiguration
parameter_list|(
name|boolean
name|ignoreXmlConfiguration
parameter_list|)
block|{
name|this
operator|.
name|ignoreXmlConfiguration
operator|=
name|ignoreXmlConfiguration
expr_stmt|;
block|}
DECL|method|getValidationProviderResolver ()
specifier|public
name|ValidationProviderResolver
name|getValidationProviderResolver
parameter_list|()
block|{
return|return
name|validationProviderResolver
return|;
block|}
comment|/**      * To use a a custom {@link ValidationProviderResolver}      */
DECL|method|setValidationProviderResolver (ValidationProviderResolver validationProviderResolver)
specifier|public
name|void
name|setValidationProviderResolver
parameter_list|(
name|ValidationProviderResolver
name|validationProviderResolver
parameter_list|)
block|{
name|this
operator|.
name|validationProviderResolver
operator|=
name|validationProviderResolver
expr_stmt|;
block|}
DECL|method|getMessageInterpolator ()
specifier|public
name|MessageInterpolator
name|getMessageInterpolator
parameter_list|()
block|{
return|return
name|messageInterpolator
return|;
block|}
comment|/**      * To use a custom {@link MessageInterpolator}      */
DECL|method|setMessageInterpolator (MessageInterpolator messageInterpolator)
specifier|public
name|void
name|setMessageInterpolator
parameter_list|(
name|MessageInterpolator
name|messageInterpolator
parameter_list|)
block|{
name|this
operator|.
name|messageInterpolator
operator|=
name|messageInterpolator
expr_stmt|;
block|}
DECL|method|getTraversableResolver ()
specifier|public
name|TraversableResolver
name|getTraversableResolver
parameter_list|()
block|{
return|return
name|traversableResolver
return|;
block|}
comment|/**      * To use a custom {@link TraversableResolver}      */
DECL|method|setTraversableResolver (TraversableResolver traversableResolver)
specifier|public
name|void
name|setTraversableResolver
parameter_list|(
name|TraversableResolver
name|traversableResolver
parameter_list|)
block|{
name|this
operator|.
name|traversableResolver
operator|=
name|traversableResolver
expr_stmt|;
block|}
DECL|method|getConstraintValidatorFactory ()
specifier|public
name|ConstraintValidatorFactory
name|getConstraintValidatorFactory
parameter_list|()
block|{
return|return
name|constraintValidatorFactory
return|;
block|}
comment|/**      * To use a custom {@link ConstraintValidatorFactory}      */
DECL|method|setConstraintValidatorFactory (ConstraintValidatorFactory constraintValidatorFactory)
specifier|public
name|void
name|setConstraintValidatorFactory
parameter_list|(
name|ConstraintValidatorFactory
name|constraintValidatorFactory
parameter_list|)
block|{
name|this
operator|.
name|constraintValidatorFactory
operator|=
name|constraintValidatorFactory
expr_stmt|;
block|}
DECL|method|getValidatorFactory ()
specifier|public
name|ValidatorFactory
name|getValidatorFactory
parameter_list|()
block|{
return|return
name|validatorFactory
return|;
block|}
comment|/**      * To use a custom {@link ValidatorFactory}      */
DECL|method|setValidatorFactory (ValidatorFactory validatorFactory)
specifier|public
name|void
name|setValidatorFactory
parameter_list|(
name|ValidatorFactory
name|validatorFactory
parameter_list|)
block|{
name|this
operator|.
name|validatorFactory
operator|=
name|validatorFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

