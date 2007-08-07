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
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
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
name|builder
operator|.
name|xml
operator|.
name|XPathBuilder
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
name|CamelBeanPostProcessor
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
name|CamelContextFactoryBean
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
name|EndpointFactoryBean
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
name|remoting
operator|.
name|CamelProxyFactoryBean
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
name|remoting
operator|.
name|CamelServiceExporter
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
name|BeanDefinitionStoreException
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
name|config
operator|.
name|RuntimeBeanReference
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
name|parsing
operator|.
name|BeanComponentDefinition
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
name|NamespaceHandlerSupport
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
name|xml
operator|.
name|DomUtils
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
name|util
operator|.
name|ObjectHelper
operator|.
name|isNotNullAndNonEmpty
import|;
end_import

begin_class
DECL|class|CamelNamespaceHandler
specifier|public
class|class
name|CamelNamespaceHandler
extends|extends
name|NamespaceHandlerSupport
block|{
DECL|field|JAXB_PACKAGES
specifier|public
specifier|static
specifier|final
name|String
name|JAXB_PACKAGES
init|=
literal|"org.apache.camel.spring:org.apache.camel.model:org.apache.camel.model.language"
decl_stmt|;
DECL|field|endpointParser
specifier|protected
name|BeanDefinitionParser
name|endpointParser
init|=
operator|new
name|BeanDefinitionParser
argument_list|(
name|EndpointFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|proxyParser
specifier|protected
name|BeanDefinitionParser
name|proxyParser
init|=
operator|new
name|BeanDefinitionParser
argument_list|(
name|CamelProxyFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|exportParser
specifier|protected
name|BeanDefinitionParser
name|exportParser
init|=
operator|new
name|BeanDefinitionParser
argument_list|(
name|CamelServiceExporter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|beanPostProcessorParser
specifier|protected
name|BeanDefinitionParser
name|beanPostProcessorParser
init|=
operator|new
name|BeanDefinitionParser
argument_list|(
name|CamelBeanPostProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|parserElementNames
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|parserElementNames
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|jaxbContext
specifier|private
name|JAXBContext
name|jaxbContext
decl_stmt|;
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
name|registerParser
argument_list|(
literal|"endpoint"
argument_list|,
name|endpointParser
argument_list|)
expr_stmt|;
name|registerParser
argument_list|(
literal|"proxy"
argument_list|,
name|proxyParser
argument_list|)
expr_stmt|;
name|registerParser
argument_list|(
literal|"export"
argument_list|,
name|exportParser
argument_list|)
expr_stmt|;
name|registerParser
argument_list|(
literal|"camelContext"
argument_list|,
operator|new
name|BeanDefinitionParser
argument_list|(
name|CamelContextFactoryBean
operator|.
name|class
argument_list|)
block|{
annotation|@
name|Override
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
name|String
name|contextId
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
comment|// lets avoid folks having to explicitly give an ID to a camel
comment|// context
if|if
condition|(
name|ObjectHelper
operator|.
name|isNullOrBlank
argument_list|(
name|contextId
argument_list|)
condition|)
block|{
name|contextId
operator|=
literal|"camelContext"
expr_stmt|;
name|element
operator|.
name|setAttribute
argument_list|(
literal|"id"
argument_list|,
name|contextId
argument_list|)
expr_stmt|;
block|}
comment|// now lets parse the routes
name|Object
name|value
init|=
name|parseUsingJaxb
argument_list|(
name|element
argument_list|,
name|parserContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|CamelContextFactoryBean
condition|)
block|{
name|CamelContextFactoryBean
name|factoryBean
init|=
operator|(
name|CamelContextFactoryBean
operator|)
name|value
decl_stmt|;
name|builder
operator|.
name|addPropertyValue
argument_list|(
literal|"routes"
argument_list|,
name|factoryBean
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|factoryBean
operator|.
name|getPackages
argument_list|()
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|builder
operator|.
name|addPropertyValue
argument_list|(
literal|"packages"
argument_list|,
name|factoryBean
operator|.
name|getPackages
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|boolean
name|createdBeanPostProcessor
init|=
literal|false
decl_stmt|;
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
if|if
condition|(
name|localName
operator|.
name|equals
argument_list|(
literal|"beanPostProcessor"
argument_list|)
condition|)
block|{
name|createBeanPostProcessor
argument_list|(
name|parserContext
argument_list|,
name|contextId
argument_list|,
name|childElement
argument_list|)
expr_stmt|;
name|createdBeanPostProcessor
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|localName
operator|.
name|equals
argument_list|(
literal|"endpoint"
argument_list|)
condition|)
block|{
name|BeanDefinition
name|definition
init|=
name|endpointParser
operator|.
name|parse
argument_list|(
name|childElement
argument_list|,
name|parserContext
argument_list|)
decl_stmt|;
name|String
name|id
init|=
name|childElement
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
if|if
condition|(
name|isNotNullAndNonEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
comment|// TODO we can zap this?
name|definition
operator|.
name|getPropertyValues
argument_list|()
operator|.
name|addPropertyValue
argument_list|(
literal|"context"
argument_list|,
operator|new
name|RuntimeBeanReference
argument_list|(
name|contextId
argument_list|)
argument_list|)
expr_stmt|;
comment|// definition.getPropertyValues().addPropertyValue("context",
comment|// builder.getBeanDefinition());
name|parserContext
operator|.
name|registerComponent
argument_list|(
operator|new
name|BeanComponentDefinition
argument_list|(
name|definition
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|localName
operator|.
name|equals
argument_list|(
literal|"proxy"
argument_list|)
condition|)
block|{
name|BeanDefinition
name|definition
init|=
name|proxyParser
operator|.
name|parse
argument_list|(
name|childElement
argument_list|,
name|parserContext
argument_list|)
decl_stmt|;
name|String
name|id
init|=
name|childElement
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
if|if
condition|(
name|isNotNullAndNonEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|parserContext
operator|.
name|registerComponent
argument_list|(
operator|new
name|BeanComponentDefinition
argument_list|(
name|definition
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|localName
operator|.
name|equals
argument_list|(
literal|"export"
argument_list|)
condition|)
block|{
name|BeanDefinition
name|definition
init|=
name|exportParser
operator|.
name|parse
argument_list|(
name|childElement
argument_list|,
name|parserContext
argument_list|)
decl_stmt|;
name|String
name|id
init|=
name|childElement
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
if|if
condition|(
name|isNotNullAndNonEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|parserContext
operator|.
name|registerComponent
argument_list|(
operator|new
name|BeanComponentDefinition
argument_list|(
name|definition
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|createdBeanPostProcessor
condition|)
block|{
comment|// no bean processor element so lets add a fake one
name|Element
name|childElement
init|=
name|element
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createElement
argument_list|(
literal|"beanPostProcessor"
argument_list|)
decl_stmt|;
name|element
operator|.
name|appendChild
argument_list|(
name|childElement
argument_list|)
expr_stmt|;
name|createBeanPostProcessor
argument_list|(
name|parserContext
argument_list|,
name|contextId
argument_list|,
name|childElement
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|registerParser
argument_list|(
literal|"xpath"
argument_list|,
operator|new
name|BeanDefinitionParser
argument_list|(
name|XPathBuilder
operator|.
name|class
argument_list|)
block|{
annotation|@
name|Override
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
comment|// lets create a child context
name|String
name|xpath
init|=
name|DomUtils
operator|.
name|getTextValue
argument_list|(
name|element
argument_list|)
decl_stmt|;
name|builder
operator|.
name|addConstructorArg
argument_list|(
name|xpath
argument_list|)
expr_stmt|;
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
name|builder
operator|.
name|addPropertyValue
argument_list|(
literal|"namespacesFromDom"
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|createBeanPostProcessor (ParserContext parserContext, String contextId, Element childElement)
specifier|protected
name|void
name|createBeanPostProcessor
parameter_list|(
name|ParserContext
name|parserContext
parameter_list|,
name|String
name|contextId
parameter_list|,
name|Element
name|childElement
parameter_list|)
block|{
name|String
name|beanPostProcessorId
init|=
name|contextId
operator|+
literal|":beanPostProcessor"
decl_stmt|;
name|childElement
operator|.
name|setAttribute
argument_list|(
literal|"id"
argument_list|,
name|beanPostProcessorId
argument_list|)
expr_stmt|;
name|BeanDefinition
name|definition
init|=
name|beanPostProcessorParser
operator|.
name|parse
argument_list|(
name|childElement
argument_list|,
name|parserContext
argument_list|)
decl_stmt|;
name|definition
operator|.
name|getPropertyValues
argument_list|()
operator|.
name|addPropertyValue
argument_list|(
literal|"camelContext"
argument_list|,
operator|new
name|RuntimeBeanReference
argument_list|(
name|contextId
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|registerScriptParser (String elementName, String engineName)
specifier|protected
name|void
name|registerScriptParser
parameter_list|(
name|String
name|elementName
parameter_list|,
name|String
name|engineName
parameter_list|)
block|{
name|registerParser
argument_list|(
name|elementName
argument_list|,
operator|new
name|ScriptDefinitionParser
argument_list|(
name|engineName
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|registerParser (String name, org.springframework.beans.factory.xml.BeanDefinitionParser parser)
specifier|protected
name|void
name|registerParser
parameter_list|(
name|String
name|name
parameter_list|,
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
name|BeanDefinitionParser
name|parser
parameter_list|)
block|{
name|parserElementNames
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|registerBeanDefinitionParser
argument_list|(
name|name
argument_list|,
name|parser
argument_list|)
expr_stmt|;
block|}
DECL|method|getParserElementNames ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getParserElementNames
parameter_list|()
block|{
return|return
name|parserElementNames
return|;
block|}
DECL|method|parseUsingJaxb (Element element, ParserContext parserContext)
specifier|protected
name|Object
name|parseUsingJaxb
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|parserContext
parameter_list|)
block|{
try|try
block|{
name|Unmarshaller
name|unmarshaller
init|=
name|getJaxbContext
argument_list|()
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
return|return
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|element
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BeanDefinitionStoreException
argument_list|(
literal|"Failed to parse JAXB element: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getJaxbContext ()
specifier|protected
name|JAXBContext
name|getJaxbContext
parameter_list|()
throws|throws
name|JAXBException
block|{
if|if
condition|(
name|jaxbContext
operator|==
literal|null
condition|)
block|{
name|jaxbContext
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|JAXB_PACKAGES
argument_list|)
expr_stmt|;
block|}
return|return
name|jaxbContext
return|;
block|}
block|}
end_class

end_unit

