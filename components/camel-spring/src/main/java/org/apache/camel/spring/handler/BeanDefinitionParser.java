begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.handler
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|handler
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
name|Attr
import|;
end_import

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
name|w3c
operator|.
name|dom
operator|.
name|NamedNodeMap
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
name|xml
operator|.
name|AbstractSingleBeanDefinitionParser
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
name|Conventions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * A base class for a parser for a bean.  *  * @version   */
end_comment

begin_class
DECL|class|BeanDefinitionParser
specifier|public
class|class
name|BeanDefinitionParser
extends|extends
name|AbstractSingleBeanDefinitionParser
block|{
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|field|assignId
specifier|private
specifier|final
name|boolean
name|assignId
decl_stmt|;
comment|/**      * Bean definition parser      *      * @param type     the type, can be null      * @param assignId whether to allow assigning id from the id attribute on the type      *                 (there must be getter/setter id on type class).      */
DECL|method|BeanDefinitionParser (Class<?> type, boolean assignId)
specifier|public
name|BeanDefinitionParser
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|boolean
name|assignId
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|assignId
operator|=
name|assignId
expr_stmt|;
block|}
DECL|method|getBeanClass (Element element)
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|getBeanClass
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
return|return
name|type
return|;
block|}
DECL|method|isAssignId ()
specifier|protected
name|boolean
name|isAssignId
parameter_list|()
block|{
return|return
name|assignId
return|;
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
return|return
name|attributeName
operator|!=
literal|null
operator|&&
operator|!
name|ID_ATTRIBUTE
operator|.
name|equals
argument_list|(
name|attributeName
argument_list|)
operator|&&
operator|!
name|attributeName
operator|.
name|equals
argument_list|(
literal|"xmlns"
argument_list|)
operator|&&
operator|!
name|attributeName
operator|.
name|startsWith
argument_list|(
literal|"xmlns:"
argument_list|)
return|;
block|}
DECL|method|doParse (Element element, BeanDefinitionBuilder builder)
specifier|protected
name|void
name|doParse
parameter_list|(
name|Element
name|element
parameter_list|,
name|BeanDefinitionBuilder
name|builder
parameter_list|)
block|{
name|NamedNodeMap
name|attributes
init|=
name|element
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|attributes
operator|.
name|getLength
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|Attr
name|attribute
init|=
operator|(
name|Attr
operator|)
name|attributes
operator|.
name|item
argument_list|(
name|x
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|attribute
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|String
name|fullName
init|=
name|attribute
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// assign id if we want them
if|if
condition|(
name|fullName
operator|.
name|equals
argument_list|(
literal|"id"
argument_list|)
operator|&&
name|isAssignId
argument_list|()
condition|)
block|{
if|if
condition|(
name|attribute
operator|.
name|getValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|addPropertyValue
argument_list|(
literal|"id"
argument_list|,
name|attribute
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// assign other attributes if eligible
block|}
elseif|else
if|if
condition|(
operator|!
name|fullName
operator|.
name|startsWith
argument_list|(
literal|"xmlns:"
argument_list|)
operator|&&
operator|!
name|fullName
operator|.
name|equals
argument_list|(
literal|"xmlns"
argument_list|)
operator|&&
name|isEligibleAttribute
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|String
name|propertyName
init|=
name|extractPropertyName
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|state
argument_list|(
name|StringUtils
operator|.
name|hasText
argument_list|(
name|propertyName
argument_list|)
argument_list|,
literal|"Illegal property name returned from 'extractPropertyName(String)': cannot be null or empty."
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addPropertyValue
argument_list|(
name|propertyName
argument_list|,
name|attribute
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|postProcess
argument_list|(
name|builder
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
comment|/**      * Extract a JavaBean property name from the supplied attribute name.      *<p>The default implementation uses the      * {@link Conventions#attributeNameToPropertyName(String)}      * method to perform the extraction.      *<p>The name returned must obey the standard JavaBean property name      * conventions. For example for a class with a setter method      * '<code>setBingoHallFavourite(String)</code>', the name returned had      * better be '<code>bingoHallFavourite</code>' (with that exact casing).      *      * @param attributeName the attribute name taken straight from the      *                      XML element being parsed (never<code>null</code>)      * @return the extracted JavaBean property name (must never be<code>null</code>)      */
DECL|method|extractPropertyName (String attributeName)
specifier|protected
name|String
name|extractPropertyName
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
return|return
name|Conventions
operator|.
name|attributeNameToPropertyName
argument_list|(
name|attributeName
argument_list|)
return|;
block|}
comment|/**      * Hook method that derived classes can implement to inspect/change a      * bean definition after parsing is complete.      *<p>The default implementation does nothing.      *      * @param beanDefinition the parsed (and probably totally defined) bean definition being built      * @param element        the XML element that was the source of the bean definition's metadata      */
DECL|method|postProcess (BeanDefinitionBuilder beanDefinition, Element element)
specifier|protected
name|void
name|postProcess
parameter_list|(
name|BeanDefinitionBuilder
name|beanDefinition
parameter_list|,
name|Element
name|element
parameter_list|)
block|{     }
block|}
end_class

end_unit

