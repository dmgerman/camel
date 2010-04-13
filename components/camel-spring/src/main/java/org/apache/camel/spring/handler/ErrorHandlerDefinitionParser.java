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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|NodeList
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
name|processor
operator|.
name|RedeliveryPolicy
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
name|ErrorHandlerType
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
name|config
operator|.
name|BeanDefinition
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
name|ParserContext
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
comment|/**  * The DefinitionParser to deal with the ErrorHandler  */
end_comment

begin_class
DECL|class|ErrorHandlerDefinitionParser
specifier|public
class|class
name|ErrorHandlerDefinitionParser
extends|extends
name|BeanDefinitionParser
block|{
DECL|field|redeliveryPolicyParser
specifier|protected
name|BeanDefinitionParser
name|redeliveryPolicyParser
init|=
operator|new
name|RedeliveryPolicyDefinitionParser
argument_list|(
name|RedeliveryPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|ErrorHandlerDefinitionParser ()
specifier|public
name|ErrorHandlerDefinitionParser
parameter_list|()
block|{
comment|// need to override the default
name|super
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|getBeanClass (Element element)
specifier|protected
name|Class
name|getBeanClass
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|ErrorHandlerType
name|type
init|=
name|ErrorHandlerType
operator|.
name|DefaultErrorHandler
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
literal|"type"
argument_list|)
argument_list|)
condition|)
block|{
name|type
operator|=
name|ErrorHandlerType
operator|.
name|valueOf
argument_list|(
name|element
operator|.
name|getAttribute
argument_list|(
literal|"type"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|type
operator|.
name|getTypeAsClass
argument_list|()
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
if|if
condition|(
name|attributeName
operator|==
literal|null
operator|||
name|ID_ATTRIBUTE
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
return|return
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
operator|&&
operator|!
name|attributeName
operator|.
name|equals
argument_list|(
literal|"type"
argument_list|)
operator|&&
operator|!
name|attributeName
operator|.
name|equals
argument_list|(
literal|"onRedeliveryRef"
argument_list|)
operator|&&
operator|!
name|attributeName
operator|.
name|equals
argument_list|(
literal|"transactionTemplateRef"
argument_list|)
operator|&&
operator|!
name|attributeName
operator|.
name|equals
argument_list|(
literal|"transactionManagerRef"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doParse (Element element, ParserContext parserContext, BeanDefinitionBuilder builder)
specifier|protected
name|void
name|doParse
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|parserContext
parameter_list|,
name|BeanDefinitionBuilder
name|builder
parameter_list|)
block|{
name|super
operator|.
name|doParse
argument_list|(
name|element
argument_list|,
name|parserContext
argument_list|,
name|builder
argument_list|)
expr_stmt|;
name|ErrorHandlerType
name|type
init|=
name|ErrorHandlerType
operator|.
name|DefaultErrorHandler
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
literal|"type"
argument_list|)
argument_list|)
condition|)
block|{
name|type
operator|=
name|ErrorHandlerType
operator|.
name|valueOf
argument_list|(
name|element
operator|.
name|getAttribute
argument_list|(
literal|"type"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|.
name|equals
argument_list|(
name|ErrorHandlerType
operator|.
name|NoErrorHandler
argument_list|)
operator|||
name|type
operator|.
name|equals
argument_list|(
name|ErrorHandlerType
operator|.
name|LoggingErrorHandler
argument_list|)
condition|)
block|{
comment|// don't need to parser other stuff
return|return;
block|}
if|if
condition|(
name|type
operator|.
name|equals
argument_list|(
name|ErrorHandlerType
operator|.
name|DefaultErrorHandler
argument_list|)
operator|||
name|type
operator|.
name|equals
argument_list|(
name|ErrorHandlerType
operator|.
name|DeadLetterChannel
argument_list|)
operator|||
name|type
operator|.
name|equals
argument_list|(
name|ErrorHandlerType
operator|.
name|TransactionErrorHandler
argument_list|)
condition|)
block|{
name|NodeList
name|list
init|=
name|element
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
name|int
name|size
init|=
name|list
operator|.
name|getLength
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|childElement
init|=
operator|(
name|Element
operator|)
name|child
decl_stmt|;
name|String
name|localName
init|=
name|child
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
comment|// set the redeliveryPolicy
if|if
condition|(
name|localName
operator|.
name|equals
argument_list|(
literal|"redeliveryPolicy"
argument_list|)
condition|)
block|{
name|BeanDefinition
name|redeliveryPolicyDefinition
init|=
name|redeliveryPolicyParser
operator|.
name|parse
argument_list|(
name|childElement
argument_list|,
name|parserContext
argument_list|)
decl_stmt|;
name|builder
operator|.
name|addPropertyValue
argument_list|(
name|localName
argument_list|,
name|redeliveryPolicyDefinition
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// deal with onRedeliveryRef
name|parserRefAttribute
argument_list|(
name|element
argument_list|,
literal|"onRedeliveryRef"
argument_list|,
literal|"onRedelivery"
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|.
name|equals
argument_list|(
name|ErrorHandlerType
operator|.
name|TransactionErrorHandler
argument_list|)
condition|)
block|{
comment|// deal with transactionTemplateRef
name|parserRefAttribute
argument_list|(
name|element
argument_list|,
literal|"transactionTemplateRef"
argument_list|,
literal|"transactionTemplate"
argument_list|,
name|builder
argument_list|)
expr_stmt|;
name|parserRefAttribute
argument_list|(
name|element
argument_list|,
literal|"transactionManagerRef"
argument_list|,
literal|"transactionManager"
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|parserRefAttribute (Element element, String attributeName, String propertyName, BeanDefinitionBuilder builder)
specifier|private
name|void
name|parserRefAttribute
parameter_list|(
name|Element
name|element
parameter_list|,
name|String
name|attributeName
parameter_list|,
name|String
name|propertyName
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
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|attributeName
argument_list|)
condition|)
block|{
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
name|addPropertyReference
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
block|}
DECL|class|RedeliveryPolicyDefinitionParser
specifier|private
specifier|final
class|class
name|RedeliveryPolicyDefinitionParser
extends|extends
name|BeanDefinitionParser
block|{
DECL|method|RedeliveryPolicyDefinitionParser (Class type)
specifier|public
name|RedeliveryPolicyDefinitionParser
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
name|super
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
DECL|method|shouldGenerateId ()
specifier|protected
name|boolean
name|shouldGenerateId
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

