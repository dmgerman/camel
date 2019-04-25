begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
operator|.
name|dataformat
package|;
end_package

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
name|DataFormatDefinition
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
name|support
operator|.
name|CamelContextHelper
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

begin_class
DECL|class|JaxbDataFormatReifier
specifier|public
class|class
name|JaxbDataFormatReifier
extends|extends
name|DataFormatReifier
argument_list|<
name|JaxbDataFormat
argument_list|>
block|{
DECL|method|JaxbDataFormatReifier (DataFormatDefinition definition)
specifier|public
name|JaxbDataFormatReifier
parameter_list|(
name|DataFormatDefinition
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|JaxbDataFormat
operator|)
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureDataFormat (DataFormat dataFormat, CamelContext camelContext)
specifier|protected
name|void
name|configureDataFormat
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|Boolean
name|answer
init|=
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|definition
operator|.
name|getPrettyPrint
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
operator|&&
operator|!
name|answer
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"prettyPrint"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// the default value is true
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"prettyPrint"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|definition
operator|.
name|getObjectFactory
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
operator|&&
operator|!
name|answer
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"objectFactory"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// the default value is true
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"objectFactory"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|definition
operator|.
name|getIgnoreJAXBElement
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
operator|&&
operator|!
name|answer
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"ignoreJAXBElement"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// the default value is true
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"ignoreJAXBElement"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|definition
operator|.
name|getMustBeJAXBElement
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
operator|&&
name|answer
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"mustBeJAXBElement"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// the default value is false
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"mustBeJAXBElement"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|definition
operator|.
name|getFilterNonXmlChars
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
operator|&&
name|answer
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"filterNonXmlChars"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// the default value is false
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"filterNonXmlChars"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|definition
operator|.
name|getFragment
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
operator|&&
name|answer
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"fragment"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// the default value is false
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"fragment"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"contextPath"
argument_list|,
name|definition
operator|.
name|getContextPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|definition
operator|.
name|getPartClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"partClass"
argument_list|,
name|definition
operator|.
name|getPartClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getPartNamespace
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"partNamespace"
argument_list|,
name|QName
operator|.
name|valueOf
argument_list|(
name|definition
operator|.
name|getPartNamespace
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getEncoding
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"encoding"
argument_list|,
name|definition
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getNamespacePrefixRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"namespacePrefixRef"
argument_list|,
name|definition
operator|.
name|getNamespacePrefixRef
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getSchema
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"schema"
argument_list|,
name|definition
operator|.
name|getSchema
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getSchemaSeverityLevel
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"schemaSeverityLevel"
argument_list|,
name|definition
operator|.
name|getSchemaSeverityLevel
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getXmlStreamWriterWrapper
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"xmlStreamWriterWrapper"
argument_list|,
name|definition
operator|.
name|getXmlStreamWriterWrapper
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getSchemaLocation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"schemaLocation"
argument_list|,
name|definition
operator|.
name|getSchemaLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getNoNamespaceSchemaLocation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"noNamespaceSchemaLocation"
argument_list|,
name|definition
operator|.
name|getNoNamespaceSchemaLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getJaxbProviderProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Map
name|map
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|definition
operator|.
name|getJaxbProviderProperties
argument_list|()
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"jaxbProviderProperties"
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

