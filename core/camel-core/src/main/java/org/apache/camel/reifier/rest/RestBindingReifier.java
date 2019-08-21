begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
operator|.
name|rest
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
name|Map
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|rest
operator|.
name|RestBindingDefinition
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
name|RestBindingAdvice
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
name|DataFormat
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
name|RestConfiguration
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
name|RouteContext
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
name|IntrospectionSupport
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
name|PropertyBindingSupport
import|;
end_import

begin_class
DECL|class|RestBindingReifier
specifier|public
class|class
name|RestBindingReifier
block|{
DECL|field|definition
specifier|private
specifier|final
name|RestBindingDefinition
name|definition
decl_stmt|;
DECL|method|RestBindingReifier (RestBindingDefinition definition)
specifier|public
name|RestBindingReifier
parameter_list|(
name|RestBindingDefinition
name|definition
parameter_list|)
block|{
name|this
operator|.
name|definition
operator|=
name|definition
expr_stmt|;
block|}
DECL|method|createRestBindingAdvice (RouteContext routeContext)
specifier|public
name|RestBindingAdvice
name|createRestBindingAdvice
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|RestConfiguration
name|config
init|=
name|context
operator|.
name|getRestConfiguration
argument_list|(
name|definition
operator|.
name|getComponent
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// these options can be overridden per rest verb
name|String
name|mode
init|=
name|config
operator|.
name|getBindingMode
argument_list|()
operator|.
name|name
argument_list|()
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getBindingMode
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|mode
operator|=
name|definition
operator|.
name|getBindingMode
argument_list|()
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
name|boolean
name|cors
init|=
name|config
operator|.
name|isEnableCORS
argument_list|()
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getEnableCORS
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cors
operator|=
name|definition
operator|.
name|getEnableCORS
argument_list|()
expr_stmt|;
block|}
name|boolean
name|skip
init|=
name|config
operator|.
name|isSkipBindingOnErrorCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getSkipBindingOnErrorCode
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|skip
operator|=
name|definition
operator|.
name|getSkipBindingOnErrorCode
argument_list|()
expr_stmt|;
block|}
name|boolean
name|validation
init|=
name|config
operator|.
name|isClientRequestValidation
argument_list|()
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getClientRequestValidation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|validation
operator|=
name|definition
operator|.
name|getClientRequestValidation
argument_list|()
expr_stmt|;
block|}
comment|// cors headers
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|corsHeaders
init|=
name|config
operator|.
name|getCorsHeaders
argument_list|()
decl_stmt|;
if|if
condition|(
name|mode
operator|==
literal|null
operator|||
literal|"off"
operator|.
name|equals
argument_list|(
name|mode
argument_list|)
condition|)
block|{
comment|// binding mode is off, so create a off mode binding processor
return|return
operator|new
name|RestBindingAdvice
argument_list|(
name|context
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|definition
operator|.
name|getConsumes
argument_list|()
argument_list|,
name|definition
operator|.
name|getProduces
argument_list|()
argument_list|,
name|mode
argument_list|,
name|skip
argument_list|,
name|validation
argument_list|,
name|cors
argument_list|,
name|corsHeaders
argument_list|,
name|definition
operator|.
name|getDefaultValues
argument_list|()
argument_list|,
name|definition
operator|.
name|getRequiredBody
argument_list|()
operator|!=
literal|null
condition|?
name|definition
operator|.
name|getRequiredBody
argument_list|()
else|:
literal|false
argument_list|,
name|definition
operator|.
name|getRequiredQueryParameters
argument_list|()
argument_list|,
name|definition
operator|.
name|getRequiredHeaders
argument_list|()
argument_list|)
return|;
block|}
comment|// setup json data format
name|DataFormat
name|json
init|=
literal|null
decl_stmt|;
name|DataFormat
name|outJson
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|mode
operator|.
name|contains
argument_list|(
literal|"json"
argument_list|)
operator|||
literal|"auto"
operator|.
name|equals
argument_list|(
name|mode
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|config
operator|.
name|getJsonDataFormat
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
comment|// must only be a name, not refer to an existing instance
name|Object
name|instance
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"JsonDataFormat name: "
operator|+
name|name
operator|+
literal|" must not be an existing bean instance from the registry"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|name
operator|=
literal|"json-jackson"
expr_stmt|;
block|}
comment|// this will create a new instance as the name was not already
comment|// pre-created
name|json
operator|=
name|context
operator|.
name|resolveDataFormat
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|outJson
operator|=
name|context
operator|.
name|resolveDataFormat
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|json
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
name|definition
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|String
name|typeName
init|=
name|type
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
condition|?
name|type
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|type
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
else|:
name|type
decl_stmt|;
name|clazz
operator|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|typeName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|context
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|json
argument_list|,
literal|"unmarshalType"
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|context
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|json
argument_list|,
literal|"useList"
argument_list|,
name|type
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setAdditionalConfiguration
argument_list|(
name|config
argument_list|,
name|context
argument_list|,
name|json
argument_list|,
literal|"json.in."
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|outClazz
init|=
literal|null
decl_stmt|;
name|String
name|outType
init|=
name|definition
operator|.
name|getOutType
argument_list|()
decl_stmt|;
if|if
condition|(
name|outType
operator|!=
literal|null
condition|)
block|{
name|String
name|typeName
init|=
name|outType
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
condition|?
name|outType
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|outType
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
else|:
name|outType
decl_stmt|;
name|outClazz
operator|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|typeName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|outClazz
operator|!=
literal|null
condition|)
block|{
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|context
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|outJson
argument_list|,
literal|"unmarshalType"
argument_list|,
name|outClazz
argument_list|)
expr_stmt|;
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|context
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|outJson
argument_list|,
literal|"useList"
argument_list|,
name|outType
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setAdditionalConfiguration
argument_list|(
name|config
argument_list|,
name|context
argument_list|,
name|outJson
argument_list|,
literal|"json.out."
argument_list|)
expr_stmt|;
block|}
block|}
comment|// setup xml data format
name|DataFormat
name|jaxb
init|=
literal|null
decl_stmt|;
name|DataFormat
name|outJaxb
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|mode
operator|.
name|contains
argument_list|(
literal|"xml"
argument_list|)
operator|||
literal|"auto"
operator|.
name|equals
argument_list|(
name|mode
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|config
operator|.
name|getXmlDataFormat
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
comment|// must only be a name, not refer to an existing instance
name|Object
name|instance
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"XmlDataFormat name: "
operator|+
name|name
operator|+
literal|" must not be an existing bean instance from the registry"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|name
operator|=
literal|"jaxb"
expr_stmt|;
block|}
comment|// this will create a new instance as the name was not already
comment|// pre-created
name|jaxb
operator|=
name|context
operator|.
name|resolveDataFormat
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|outJaxb
operator|=
name|context
operator|.
name|resolveDataFormat
argument_list|(
name|name
argument_list|)
expr_stmt|;
comment|// is xml binding required?
if|if
condition|(
name|mode
operator|.
name|contains
argument_list|(
literal|"xml"
argument_list|)
operator|&&
name|jaxb
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"XML DataFormat "
operator|+
name|name
operator|+
literal|" not found."
argument_list|)
throw|;
block|}
if|if
condition|(
name|jaxb
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
name|definition
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|String
name|typeName
init|=
name|type
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
condition|?
name|type
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|type
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
else|:
name|type
decl_stmt|;
name|clazz
operator|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|typeName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
name|JAXBContext
name|jc
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|context
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|jaxb
argument_list|,
literal|"context"
argument_list|,
name|jc
argument_list|)
expr_stmt|;
block|}
name|setAdditionalConfiguration
argument_list|(
name|config
argument_list|,
name|context
argument_list|,
name|jaxb
argument_list|,
literal|"xml.in."
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|outClazz
init|=
literal|null
decl_stmt|;
name|String
name|outType
init|=
name|definition
operator|.
name|getOutType
argument_list|()
decl_stmt|;
if|if
condition|(
name|outType
operator|!=
literal|null
condition|)
block|{
name|String
name|typeName
init|=
name|outType
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
condition|?
name|outType
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|outType
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
else|:
name|outType
decl_stmt|;
name|outClazz
operator|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|typeName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|outClazz
operator|!=
literal|null
condition|)
block|{
name|JAXBContext
name|jc
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|outClazz
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|context
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|outJaxb
argument_list|,
literal|"context"
argument_list|,
name|jc
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
comment|// fallback and use the context from the input
name|JAXBContext
name|jc
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|context
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|outJaxb
argument_list|,
literal|"context"
argument_list|,
name|jc
argument_list|)
expr_stmt|;
block|}
name|setAdditionalConfiguration
argument_list|(
name|config
argument_list|,
name|context
argument_list|,
name|outJaxb
argument_list|,
literal|"xml.out."
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|RestBindingAdvice
argument_list|(
name|context
argument_list|,
name|json
argument_list|,
name|jaxb
argument_list|,
name|outJson
argument_list|,
name|outJaxb
argument_list|,
name|definition
operator|.
name|getConsumes
argument_list|()
argument_list|,
name|definition
operator|.
name|getProduces
argument_list|()
argument_list|,
name|mode
argument_list|,
name|skip
argument_list|,
name|validation
argument_list|,
name|cors
argument_list|,
name|corsHeaders
argument_list|,
name|definition
operator|.
name|getDefaultValues
argument_list|()
argument_list|,
name|definition
operator|.
name|getRequiredBody
argument_list|()
operator|!=
literal|null
condition|?
name|definition
operator|.
name|getRequiredBody
argument_list|()
else|:
literal|false
argument_list|,
name|definition
operator|.
name|getRequiredQueryParameters
argument_list|()
argument_list|,
name|definition
operator|.
name|getRequiredHeaders
argument_list|()
argument_list|)
return|;
block|}
DECL|method|setAdditionalConfiguration (RestConfiguration config, CamelContext context, DataFormat dataFormat, String prefix)
specifier|private
name|void
name|setAdditionalConfiguration
parameter_list|(
name|RestConfiguration
name|config
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|DataFormat
name|dataFormat
parameter_list|,
name|String
name|prefix
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|config
operator|.
name|getDataFormatProperties
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|config
operator|.
name|getDataFormatProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// must use a copy as otherwise the options gets removed during
comment|// introspection setProperties
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|copy
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// filter keys on prefix
comment|// - either its a known prefix and must match the prefix parameter
comment|// - or its a common configuration that we should always use
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|config
operator|.
name|getDataFormatProperties
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|copyKey
decl_stmt|;
name|boolean
name|known
init|=
name|isKeyKnownPrefix
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|known
condition|)
block|{
comment|// remove the prefix from the key to use
name|copyKey
operator|=
name|key
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use the key as is
name|copyKey
operator|=
name|key
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|known
operator|||
name|key
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|copy
operator|.
name|put
argument_list|(
name|copyKey
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|PropertyBindingSupport
operator|.
name|build
argument_list|()
operator|.
name|bind
argument_list|(
name|context
argument_list|,
name|dataFormat
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isKeyKnownPrefix (String key)
specifier|private
name|boolean
name|isKeyKnownPrefix
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|key
operator|.
name|startsWith
argument_list|(
literal|"json.in."
argument_list|)
operator|||
name|key
operator|.
name|startsWith
argument_list|(
literal|"json.out."
argument_list|)
operator|||
name|key
operator|.
name|startsWith
argument_list|(
literal|"xml.in."
argument_list|)
operator|||
name|key
operator|.
name|startsWith
argument_list|(
literal|"xml.out."
argument_list|)
return|;
block|}
block|}
end_class

end_unit

