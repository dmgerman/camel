begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.security.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|security
operator|.
name|config
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|spring
operator|.
name|security
operator|.
name|SpringSecurityAccessPolicy
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
name|spring
operator|.
name|security
operator|.
name|SpringSecurityAuthorizationPolicy
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
name|handler
operator|.
name|BeanDefinitionParser
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|support
operator|.
name|BeanDefinitionBuilder
import|;
end_import

begin_class
DECL|class|SpringSecurityAuthorizationPolicyParser
specifier|public
class|class
name|SpringSecurityAuthorizationPolicyParser
extends|extends
name|BeanDefinitionParser
block|{
DECL|method|SpringSecurityAuthorizationPolicyParser ()
specifier|public
name|SpringSecurityAuthorizationPolicyParser
parameter_list|()
block|{
comment|// true = allow id to be set as there is a setter method on target bean
name|super
argument_list|(
name|SpringSecurityAuthorizationPolicy
operator|.
name|class
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|isEligibleAttribute (String attributeName)
specifier|protected
name|boolean
name|isEligibleAttribute
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
if|if
condition|(
literal|"access"
operator|.
name|equals
argument_list|(
name|attributeName
argument_list|)
operator|||
literal|"accessDecisionManager"
operator|.
name|equals
argument_list|(
name|attributeName
argument_list|)
operator|||
literal|"authenticationManager"
operator|.
name|equals
argument_list|(
name|attributeName
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|isEligibleAttribute
argument_list|(
name|attributeName
argument_list|)
return|;
block|}
block|}
DECL|method|postProcess (BeanDefinitionBuilder builder, Element element)
specifier|protected
name|void
name|postProcess
parameter_list|(
name|BeanDefinitionBuilder
name|builder
parameter_list|,
name|Element
name|element
parameter_list|)
block|{
name|setReferenceIfAttributeDefine
argument_list|(
name|builder
argument_list|,
name|element
argument_list|,
literal|"accessDecisionManager"
argument_list|)
expr_stmt|;
name|setReferenceIfAttributeDefine
argument_list|(
name|builder
argument_list|,
name|element
argument_list|,
literal|"authenticationManager"
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|element
operator|.
name|getAttribute
argument_list|(
literal|"authenticationAdapter"
argument_list|)
argument_list|)
condition|)
block|{
name|builder
operator|.
name|addPropertyReference
argument_list|(
literal|"authenticationAdapter"
argument_list|,
name|element
operator|.
name|getAttribute
argument_list|(
literal|"authenticationAdapter"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|BeanDefinitionBuilder
name|accessPolicyBuilder
init|=
name|BeanDefinitionBuilder
operator|.
name|genericBeanDefinition
argument_list|(
name|SpringSecurityAccessPolicy
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
decl_stmt|;
name|accessPolicyBuilder
operator|.
name|addConstructorArgValue
argument_list|(
name|element
operator|.
name|getAttribute
argument_list|(
literal|"access"
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addPropertyValue
argument_list|(
literal|"springSecurityAccessPolicy"
argument_list|,
name|accessPolicyBuilder
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setReferenceIfAttributeDefine (BeanDefinitionBuilder builder, Element element, String attribute)
specifier|protected
name|void
name|setReferenceIfAttributeDefine
parameter_list|(
name|BeanDefinitionBuilder
name|builder
parameter_list|,
name|Element
name|element
parameter_list|,
name|String
name|attribute
parameter_list|)
block|{
name|String
name|valueRef
init|=
name|attribute
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|element
operator|.
name|getAttribute
argument_list|(
name|attribute
argument_list|)
argument_list|)
condition|)
block|{
name|valueRef
operator|=
name|element
operator|.
name|getAttribute
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|addPropertyReference
argument_list|(
name|attribute
argument_list|,
name|valueRef
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

