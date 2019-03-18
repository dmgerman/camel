begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.blueprint
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
name|blueprint
package|;
end_package

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
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|ParserContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|mutable
operator|.
name|MutableBeanMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|mutable
operator|.
name|MutablePassThroughMetadata
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
name|blueprint
operator|.
name|AbstractBPBeanDefinitionParser
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
name|helpers
operator|.
name|DOMUtils
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
name|jaxrs
operator|.
name|model
operator|.
name|UserResource
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
name|jaxrs
operator|.
name|utils
operator|.
name|ResourceUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|Metadata
import|;
end_import

begin_class
DECL|class|RsClientDefinitionParser
specifier|public
class|class
name|RsClientDefinitionParser
extends|extends
name|AbstractBeanDefinitionParser
block|{
DECL|method|parse (Element element, ParserContext context)
specifier|public
name|Metadata
name|parse
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
name|MutableBeanMetadata
name|beanMetadata
init|=
name|createBeanMetadata
argument_list|(
name|element
argument_list|,
name|context
argument_list|,
name|RsClientBlueprintBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|NamedNodeMap
name|atts
init|=
name|element
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|String
name|bus
init|=
literal|null
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
name|atts
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Attr
name|node
init|=
operator|(
name|Attr
operator|)
name|atts
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|val
init|=
name|node
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|String
name|pre
init|=
name|node
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|node
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"bus"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|bus
operator|=
name|val
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isAttribute
argument_list|(
name|pre
argument_list|,
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"depends-on"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|beanMetadata
operator|.
name|addDependsOn
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
literal|"name"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|beanMetadata
operator|.
name|addProperty
argument_list|(
name|name
argument_list|,
name|AbstractBPBeanDefinitionParser
operator|.
name|createValue
argument_list|(
name|context
argument_list|,
name|val
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|Element
name|elem
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|element
argument_list|)
init|;
name|elem
operator|!=
literal|null
condition|;
name|elem
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|elem
argument_list|)
control|)
block|{
name|String
name|name
init|=
name|elem
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"properties"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"headers"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|Metadata
name|map
init|=
name|parseMapData
argument_list|(
name|context
argument_list|,
name|beanMetadata
argument_list|,
name|elem
argument_list|)
decl_stmt|;
name|beanMetadata
operator|.
name|addProperty
argument_list|(
name|name
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
name|elem
argument_list|,
name|context
argument_list|,
name|beanMetadata
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
name|Metadata
name|list
init|=
name|parseListData
argument_list|(
name|context
argument_list|,
name|beanMetadata
argument_list|,
name|elem
argument_list|)
decl_stmt|;
name|beanMetadata
operator|.
name|addProperty
argument_list|(
name|name
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"features"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"providers"
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
literal|"modelBeans"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|Metadata
name|list
init|=
name|parseListData
argument_list|(
name|context
argument_list|,
name|beanMetadata
argument_list|,
name|elem
argument_list|)
decl_stmt|;
name|beanMetadata
operator|.
name|addProperty
argument_list|(
name|name
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"model"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|UserResource
argument_list|>
name|resources
init|=
name|ResourceUtils
operator|.
name|getResourcesFromElement
argument_list|(
name|elem
argument_list|)
decl_stmt|;
name|MutablePassThroughMetadata
name|value
init|=
name|context
operator|.
name|createMetadata
argument_list|(
name|MutablePassThroughMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
name|value
operator|.
name|setObject
argument_list|(
name|resources
argument_list|)
expr_stmt|;
name|beanMetadata
operator|.
name|addProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setFirstChildAsProperty
argument_list|(
name|elem
argument_list|,
name|context
argument_list|,
name|beanMetadata
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|bus
argument_list|)
condition|)
block|{
name|bus
operator|=
literal|"cxf"
expr_stmt|;
block|}
comment|//Will create a bus if needed...
name|beanMetadata
operator|.
name|addProperty
argument_list|(
literal|"bus"
argument_list|,
name|getBusRef
argument_list|(
name|context
argument_list|,
name|bus
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|beanMetadata
return|;
block|}
block|}
end_class

end_unit

