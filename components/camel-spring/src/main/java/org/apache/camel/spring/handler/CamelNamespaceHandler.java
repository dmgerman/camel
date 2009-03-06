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
name|HashMap
import|;
end_import

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
name|Map
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
name|Binder
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
name|ExchangePattern
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
name|Namespaces
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
name|model
operator|.
name|FromDefinition
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
name|model
operator|.
name|SendDefinition
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
name|model
operator|.
name|config
operator|.
name|PropertiesDefinition
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
name|model
operator|.
name|dataformat
operator|.
name|ArtixDSDataFormat
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
name|model
operator|.
name|dataformat
operator|.
name|JaxbDataFormat
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
name|model
operator|.
name|dataformat
operator|.
name|SerializationDataFormat
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
name|model
operator|.
name|dataformat
operator|.
name|XMLBeansDataFormat
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
name|model
operator|.
name|loadbalancer
operator|.
name|RandomLoadBalanceStrategy
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
name|model
operator|.
name|loadbalancer
operator|.
name|RoundRobinLoadBalanceStrategy
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
name|model
operator|.
name|loadbalancer
operator|.
name|StickyLoadBalanceStrategy
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
name|model
operator|.
name|loadbalancer
operator|.
name|TopicLoadBalanceStrategy
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
name|NamespaceAware
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
name|CamelJMXAgentDefinition
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
name|CamelTemplateFactoryBean
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
name|apache
operator|.
name|camel
operator|.
name|view
operator|.
name|ModelFileGenerator
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

begin_comment
comment|/**  * Camel namespace for the spring XML configuration file.  */
end_comment

begin_class
DECL|class|CamelNamespaceHandler
specifier|public
class|class
name|CamelNamespaceHandler
extends|extends
name|NamespaceHandlerSupport
block|{
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
DECL|field|binder
specifier|protected
name|Binder
argument_list|<
name|Node
argument_list|>
name|binder
decl_stmt|;
DECL|field|jaxbContext
specifier|private
name|JAXBContext
name|jaxbContext
decl_stmt|;
DECL|field|parserMap
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|BeanDefinitionParser
argument_list|>
name|parserMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|BeanDefinitionParser
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|createModelFileGenerator ()
specifier|public
name|ModelFileGenerator
name|createModelFileGenerator
parameter_list|()
throws|throws
name|JAXBException
block|{
return|return
operator|new
name|ModelFileGenerator
argument_list|(
name|getJaxbContext
argument_list|()
argument_list|)
return|;
block|}
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
comment|// remoting
name|addBeanDefinitionParser
argument_list|(
literal|"proxy"
argument_list|,
name|CamelProxyFactoryBean
operator|.
name|class
argument_list|)
expr_stmt|;
name|addBeanDefinitionParser
argument_list|(
literal|"template"
argument_list|,
name|CamelTemplateFactoryBean
operator|.
name|class
argument_list|)
expr_stmt|;
name|addBeanDefinitionParser
argument_list|(
literal|"export"
argument_list|,
name|CamelServiceExporter
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// data types
name|addBeanDefinitionParser
argument_list|(
literal|"artixDS"
argument_list|,
name|ArtixDSDataFormat
operator|.
name|class
argument_list|)
expr_stmt|;
name|addBeanDefinitionParser
argument_list|(
literal|"jaxb"
argument_list|,
name|JaxbDataFormat
operator|.
name|class
argument_list|)
expr_stmt|;
name|addBeanDefinitionParser
argument_list|(
literal|"serialization"
argument_list|,
name|SerializationDataFormat
operator|.
name|class
argument_list|)
expr_stmt|;
name|addBeanDefinitionParser
argument_list|(
literal|"xmlBeans"
argument_list|,
name|XMLBeansDataFormat
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// load balancers
name|addBeanDefinitionParser
argument_list|(
literal|"roundRobin"
argument_list|,
name|RoundRobinLoadBalanceStrategy
operator|.
name|class
argument_list|)
expr_stmt|;
name|addBeanDefinitionParser
argument_list|(
literal|"random"
argument_list|,
name|RandomLoadBalanceStrategy
operator|.
name|class
argument_list|)
expr_stmt|;
name|addBeanDefinitionParser
argument_list|(
literal|"sticky"
argument_list|,
name|StickyLoadBalanceStrategy
operator|.
name|class
argument_list|)
expr_stmt|;
name|addBeanDefinitionParser
argument_list|(
literal|"topic"
argument_list|,
name|TopicLoadBalanceStrategy
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// jmx agent
name|addBeanDefinitionParser
argument_list|(
literal|"jmxAgent"
argument_list|,
name|CamelJMXAgentDefinition
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// endpoint
name|addBeanDefinitionParser
argument_list|(
literal|"endpoint"
argument_list|,
name|EndpointFactoryBean
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// camel context
name|Class
name|cl
init|=
name|CamelContextFactoryBean
operator|.
name|class
decl_stmt|;
try|try
block|{
name|cl
operator|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.apache.camel.osgi.CamelContextFactoryBean"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// not running with camel-osgi so we fallback to the regular factory bean
block|}
name|registerParser
argument_list|(
literal|"camelContext"
argument_list|,
operator|new
name|CamelContextBeanDefinitionParser
argument_list|(
name|cl
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|addBeanDefinitionParser (String elementName, Class<?> type)
specifier|private
name|void
name|addBeanDefinitionParser
parameter_list|(
name|String
name|elementName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|BeanDefinitionParser
name|parser
init|=
operator|new
name|BeanDefinitionParser
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|registerParser
argument_list|(
name|elementName
argument_list|,
name|parser
argument_list|)
expr_stmt|;
name|parserMap
operator|.
name|put
argument_list|(
name|elementName
argument_list|,
name|parser
argument_list|)
expr_stmt|;
block|}
DECL|method|createBeanPostProcessor (ParserContext parserContext, String contextId, Element childElement, BeanDefinitionBuilder parentBuilder)
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
parameter_list|,
name|BeanDefinitionBuilder
name|parentBuilder
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
name|parentBuilder
operator|.
name|addPropertyReference
argument_list|(
literal|"beanPostProcessor"
argument_list|,
name|beanPostProcessorId
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
name|binder
operator|=
name|getJaxbContext
argument_list|()
operator|.
name|createBinder
argument_list|()
expr_stmt|;
return|return
name|binder
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
specifier|public
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
name|createJaxbContext
argument_list|()
expr_stmt|;
block|}
return|return
name|jaxbContext
return|;
block|}
DECL|method|createJaxbContext ()
specifier|protected
name|JAXBContext
name|createJaxbContext
parameter_list|()
throws|throws
name|JAXBException
block|{
name|StringBuilder
name|packages
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
name|cl
range|:
name|getJaxbPackages
argument_list|()
control|)
block|{
if|if
condition|(
name|packages
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|packages
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
block|}
name|packages
operator|.
name|append
argument_list|(
name|cl
operator|.
name|getName
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|cl
operator|.
name|getName
argument_list|()
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|packages
operator|.
name|toString
argument_list|()
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getJaxbPackages ()
specifier|protected
name|Set
argument_list|<
name|Class
argument_list|>
name|getJaxbPackages
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|>
name|classes
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|>
argument_list|()
decl_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|CamelContextFactoryBean
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|ExchangePattern
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|RouteDefinition
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|config
operator|.
name|StreamResequencerConfig
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
operator|.
name|DataFormatDefinition
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|language
operator|.
name|ExpressionDefinition
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|loadbalancer
operator|.
name|LoadBalancerDefinition
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|classes
return|;
block|}
DECL|class|CamelContextBeanDefinitionParser
specifier|protected
class|class
name|CamelContextBeanDefinitionParser
extends|extends
name|BeanDefinitionParser
block|{
DECL|method|CamelContextBeanDefinitionParser (Class type)
specifier|public
name|CamelContextBeanDefinitionParser
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
comment|// lets avoid folks having to explicitly give an ID to a camel context
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
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
literal|"id"
argument_list|,
name|contextId
argument_list|)
expr_stmt|;
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
name|builder
operator|.
name|addPropertyValue
argument_list|(
literal|"intercepts"
argument_list|,
name|factoryBean
operator|.
name|getIntercepts
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addPropertyValue
argument_list|(
literal|"dataFormats"
argument_list|,
name|factoryBean
operator|.
name|getDataFormats
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addPropertyValue
argument_list|(
literal|"exceptionClauses"
argument_list|,
name|factoryBean
operator|.
name|getExceptionClauses
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addPropertyValue
argument_list|(
literal|"builderRefs"
argument_list|,
name|factoryBean
operator|.
name|getBuilderRefs
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|addPropertyValue
argument_list|(
literal|"properties"
argument_list|,
name|factoryBean
operator|.
name|getProperties
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
argument_list|,
name|builder
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
name|registerEndpoint
argument_list|(
name|childElement
argument_list|,
name|parserContext
argument_list|,
name|contextId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|BeanDefinitionParser
name|parser
init|=
name|parserMap
operator|.
name|get
argument_list|(
name|localName
argument_list|)
decl_stmt|;
if|if
condition|(
name|parser
operator|!=
literal|null
condition|)
block|{
name|BeanDefinition
name|definition
init|=
name|parser
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
name|ObjectHelper
operator|.
name|isNotEmpty
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
if|if
condition|(
name|localName
operator|.
name|equals
argument_list|(
literal|"jmxAgent"
argument_list|)
condition|)
block|{
name|builder
operator|.
name|addPropertyReference
argument_list|(
literal|"camelJMXAgent"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
comment|// register as endpoint defined indirectly in the routes by from/to types having id explict set
name|registerEndpointsWithIdsDefinedInFromToTypes
argument_list|(
name|element
argument_list|,
name|parserContext
argument_list|,
name|contextId
argument_list|)
expr_stmt|;
comment|// lets inject the namespaces into any namespace aware POJOs
name|injectNamespaces
argument_list|(
name|element
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|createdBeanPostProcessor
condition|)
block|{
comment|// no bean processor element so lets create it by ourself
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
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|injectNamespaces (Element element)
specifier|protected
name|void
name|injectNamespaces
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|NodeList
name|list
init|=
name|element
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
name|Namespaces
name|namespaces
init|=
literal|null
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
name|Object
name|object
init|=
name|binder
operator|.
name|getJAXBNode
argument_list|(
name|child
argument_list|)
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|NamespaceAware
condition|)
block|{
name|NamespaceAware
name|namespaceAware
init|=
operator|(
name|NamespaceAware
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|namespaces
operator|==
literal|null
condition|)
block|{
name|namespaces
operator|=
operator|new
name|Namespaces
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
name|namespaces
operator|.
name|configure
argument_list|(
name|namespaceAware
argument_list|)
expr_stmt|;
block|}
name|injectNamespaces
argument_list|(
name|childElement
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|registerEndpointsWithIdsDefinedInFromToTypes (Element element, ParserContext parserContext, String contextId)
specifier|protected
name|void
name|registerEndpointsWithIdsDefinedInFromToTypes
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|parserContext
parameter_list|,
name|String
name|contextId
parameter_list|)
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
name|Object
name|object
init|=
name|binder
operator|.
name|getJAXBNode
argument_list|(
name|child
argument_list|)
decl_stmt|;
comment|// we only want from/to types to be registered as endpoints
if|if
condition|(
name|object
operator|instanceof
name|FromDefinition
operator|||
name|object
operator|instanceof
name|SendDefinition
condition|)
block|{
name|registerEndpoint
argument_list|(
name|childElement
argument_list|,
name|parserContext
argument_list|,
name|contextId
argument_list|)
expr_stmt|;
block|}
comment|// recursive
name|registerEndpointsWithIdsDefinedInFromToTypes
argument_list|(
name|childElement
argument_list|,
name|parserContext
argument_list|,
name|contextId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|registerEndpoint (Element childElement, ParserContext parserContext, String contextId)
specifier|private
name|void
name|registerEndpoint
parameter_list|(
name|Element
name|childElement
parameter_list|,
name|ParserContext
name|parserContext
parameter_list|,
name|String
name|contextId
parameter_list|)
block|{
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
comment|// must have an id to be registered
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|id
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
end_class

end_unit

