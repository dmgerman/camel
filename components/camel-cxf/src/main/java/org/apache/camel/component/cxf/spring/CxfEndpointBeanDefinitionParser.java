begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|spring
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
name|List
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
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bus
operator|.
name|spring
operator|.
name|BusWiringBeanFactoryPostProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|configuration
operator|.
name|spring
operator|.
name|AbstractBeanDefinitionParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|factory
operator|.
name|ReflectionServiceFactoryBean
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
name|BeansException
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
name|PropertyValue
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
name|support
operator|.
name|AbstractBeanDefinition
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
name|context
operator|.
name|ApplicationContext
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
name|ApplicationContextAware
import|;
end_import

begin_class
DECL|class|CxfEndpointBeanDefinitionParser
specifier|public
class|class
name|CxfEndpointBeanDefinitionParser
extends|extends
name|AbstractBeanDefinitionParser
block|{
annotation|@
name|Override
DECL|method|getBeanClass (Element arg0)
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|getBeanClass
parameter_list|(
name|Element
name|arg0
parameter_list|)
block|{
return|return
name|CxfSpringEndpointBean
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|mapAttribute (BeanDefinitionBuilder bean, Element e, String name, String val)
specifier|protected
name|void
name|mapAttribute
parameter_list|(
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|Element
name|e
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|val
parameter_list|)
block|{
if|if
condition|(
literal|"endpointName"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"serviceName"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|QName
name|q
init|=
name|parseQName
argument_list|(
name|e
argument_list|,
name|val
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
name|name
argument_list|,
name|q
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mapToProperty
argument_list|(
name|bean
argument_list|,
name|name
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|mapElement (ParserContext ctx, BeanDefinitionBuilder bean, Element el, String name)
specifier|protected
name|void
name|mapElement
parameter_list|(
name|ParserContext
name|ctx
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|,
name|Element
name|el
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
literal|"properties"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
name|ctx
operator|.
name|getDelegate
argument_list|()
operator|.
name|parseMapElement
argument_list|(
name|el
argument_list|,
name|bean
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"properties"
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"binding"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|setFirstChildAsProperty
argument_list|(
name|el
argument_list|,
name|ctx
argument_list|,
name|bean
argument_list|,
literal|"bindingConfig"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"inInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"inFaultInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"outInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"outFaultInterceptors"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"features"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"schemaLocations"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"handlers"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|ctx
operator|.
name|getDelegate
argument_list|()
operator|.
name|parseListElement
argument_list|(
name|el
argument_list|,
name|bean
operator|.
name|getBeanDefinition
argument_list|()
argument_list|)
decl_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
name|name
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setFirstChildAsProperty
argument_list|(
name|el
argument_list|,
name|ctx
argument_list|,
name|bean
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doParse (Element element, ParserContext ctx, BeanDefinitionBuilder bean)
specifier|protected
name|void
name|doParse
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|ctx
parameter_list|,
name|BeanDefinitionBuilder
name|bean
parameter_list|)
block|{
name|super
operator|.
name|doParse
argument_list|(
name|element
argument_list|,
name|ctx
argument_list|,
name|bean
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setLazyInit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// put the id into the properties
name|PropertyValue
name|propertyValue
init|=
operator|(
name|PropertyValue
operator|)
name|bean
operator|.
name|getBeanDefinition
argument_list|()
operator|.
name|getPropertyValues
argument_list|()
operator|.
name|getPropertyValue
argument_list|(
literal|"properties"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|propertyValue
operator|==
literal|null
condition|)
block|{
name|map
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|bean
operator|.
name|addPropertyValue
argument_list|(
literal|"properties"
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|map
operator|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|propertyValue
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
name|String
name|id
init|=
name|resolveId
argument_list|(
name|element
argument_list|,
name|bean
operator|.
name|getBeanDefinition
argument_list|()
argument_list|,
name|ctx
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"beanId"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resolveId (Element elem, AbstractBeanDefinition definition, ParserContext ctx)
specifier|protected
name|String
name|resolveId
parameter_list|(
name|Element
name|elem
parameter_list|,
name|AbstractBeanDefinition
name|definition
parameter_list|,
name|ParserContext
name|ctx
parameter_list|)
throws|throws
name|BeanDefinitionStoreException
block|{
name|String
name|id
init|=
name|super
operator|.
name|resolveId
argument_list|(
name|elem
argument_list|,
name|definition
argument_list|,
name|ctx
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BeanDefinitionStoreException
argument_list|(
literal|"The bean id is needed."
argument_list|)
throw|;
block|}
return|return
name|id
return|;
block|}
annotation|@
name|Override
DECL|method|hasBusProperty ()
specifier|protected
name|boolean
name|hasBusProperty
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|// To make the CxfEndpointBean clear without touching any Spring relates class
comment|// , we implements the ApplicationContextAware here
DECL|class|CxfSpringEndpointBean
specifier|public
specifier|static
class|class
name|CxfSpringEndpointBean
extends|extends
name|CxfEndpointBean
implements|implements
name|ApplicationContextAware
block|{
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|beanId
specifier|private
name|String
name|beanId
decl_stmt|;
DECL|method|CxfSpringEndpointBean ()
specifier|public
name|CxfSpringEndpointBean
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
DECL|method|CxfSpringEndpointBean (ReflectionServiceFactoryBean factory)
specifier|public
name|CxfSpringEndpointBean
parameter_list|(
name|ReflectionServiceFactoryBean
name|factory
parameter_list|)
block|{
name|super
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
DECL|method|setApplicationContext (ApplicationContext ctx)
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|ctx
parameter_list|)
throws|throws
name|BeansException
block|{
name|applicationContext
operator|=
name|ctx
expr_stmt|;
if|if
condition|(
name|getBus
argument_list|()
operator|==
literal|null
condition|)
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
decl_stmt|;
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
name|BusWiringBeanFactoryPostProcessor
operator|.
name|updateBusReferencesInContext
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
block|}
DECL|method|getApplicationContext ()
specifier|public
name|ApplicationContext
name|getApplicationContext
parameter_list|()
block|{
return|return
name|applicationContext
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|beanId
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|beanId
return|;
block|}
block|}
block|}
end_class

end_unit

