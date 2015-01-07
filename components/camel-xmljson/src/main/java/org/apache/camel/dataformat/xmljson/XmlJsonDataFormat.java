begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.xmljson
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|xmljson
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|json
operator|.
name|JSON
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|json
operator|.
name|JSONSerializer
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|json
operator|.
name|xml
operator|.
name|XMLSerializer
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
name|Exchange
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
name|ServiceSupport
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
name|IOHelper
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a> ({@link DataFormat}) using   *<a href="http://json-lib.sourceforge.net/">json-lib</a> to convert between XML  * and JSON directly.  */
end_comment

begin_class
DECL|class|XmlJsonDataFormat
specifier|public
class|class
name|XmlJsonDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
block|{
DECL|field|serializer
specifier|private
name|XMLSerializer
name|serializer
decl_stmt|;
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
DECL|field|elementName
specifier|private
name|String
name|elementName
decl_stmt|;
DECL|field|arrayName
specifier|private
name|String
name|arrayName
decl_stmt|;
DECL|field|forceTopLevelObject
specifier|private
name|Boolean
name|forceTopLevelObject
decl_stmt|;
DECL|field|namespaceLenient
specifier|private
name|Boolean
name|namespaceLenient
decl_stmt|;
DECL|field|namespaceMappings
specifier|private
name|List
argument_list|<
name|NamespacesPerElementMapping
argument_list|>
name|namespaceMappings
decl_stmt|;
DECL|field|rootName
specifier|private
name|String
name|rootName
decl_stmt|;
DECL|field|skipWhitespace
specifier|private
name|Boolean
name|skipWhitespace
decl_stmt|;
DECL|field|trimSpaces
specifier|private
name|Boolean
name|trimSpaces
decl_stmt|;
DECL|field|skipNamespaces
specifier|private
name|Boolean
name|skipNamespaces
decl_stmt|;
DECL|field|removeNamespacePrefixes
specifier|private
name|Boolean
name|removeNamespacePrefixes
decl_stmt|;
DECL|field|expandableProperties
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|expandableProperties
decl_stmt|;
DECL|field|typeHints
specifier|private
name|TypeHintsEnum
name|typeHints
decl_stmt|;
DECL|method|XmlJsonDataFormat ()
specifier|public
name|XmlJsonDataFormat
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|serializer
operator|=
operator|new
name|XMLSerializer
argument_list|()
expr_stmt|;
if|if
condition|(
name|forceTopLevelObject
operator|!=
literal|null
condition|)
block|{
name|serializer
operator|.
name|setForceTopLevelObject
argument_list|(
name|forceTopLevelObject
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|namespaceLenient
operator|!=
literal|null
condition|)
block|{
name|serializer
operator|.
name|setNamespaceLenient
argument_list|(
name|namespaceLenient
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|namespaceMappings
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|NamespacesPerElementMapping
name|nsMapping
range|:
name|namespaceMappings
control|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|nsMapping
operator|.
name|namespaces
operator|.
name|entrySet
argument_list|()
control|)
block|{
comment|// prefix, URI, elementName (which can be null or empty
comment|// string, in which case the
comment|// mapping is added to the root element
name|serializer
operator|.
name|addNamespace
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|nsMapping
operator|.
name|element
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|rootName
operator|!=
literal|null
condition|)
block|{
name|serializer
operator|.
name|setRootName
argument_list|(
name|rootName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|elementName
operator|!=
literal|null
condition|)
block|{
name|serializer
operator|.
name|setElementName
argument_list|(
name|elementName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|arrayName
operator|!=
literal|null
condition|)
block|{
name|serializer
operator|.
name|setArrayName
argument_list|(
name|arrayName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|expandableProperties
operator|!=
literal|null
operator|&&
name|expandableProperties
operator|.
name|size
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|serializer
operator|.
name|setExpandableProperties
argument_list|(
name|expandableProperties
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|expandableProperties
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|skipWhitespace
operator|!=
literal|null
condition|)
block|{
name|serializer
operator|.
name|setSkipWhitespace
argument_list|(
name|skipWhitespace
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|trimSpaces
operator|!=
literal|null
condition|)
block|{
name|serializer
operator|.
name|setTrimSpaces
argument_list|(
name|trimSpaces
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|skipNamespaces
operator|!=
literal|null
condition|)
block|{
name|serializer
operator|.
name|setSkipNamespaces
argument_list|(
name|skipNamespaces
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|removeNamespacePrefixes
operator|!=
literal|null
condition|)
block|{
name|serializer
operator|.
name|setRemoveNamespacePrefixFromElements
argument_list|(
name|removeNamespacePrefixes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|typeHints
operator|==
name|TypeHintsEnum
operator|.
name|YES
operator|||
name|typeHints
operator|==
name|TypeHintsEnum
operator|.
name|WITH_PREFIX
condition|)
block|{
name|serializer
operator|.
name|setTypeHintsEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|typeHints
operator|==
name|TypeHintsEnum
operator|.
name|WITH_PREFIX
condition|)
block|{
name|serializer
operator|.
name|setTypeHintsCompatibility
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|serializer
operator|.
name|setTypeHintsCompatibility
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|serializer
operator|.
name|setTypeHintsEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|serializer
operator|.
name|setTypeHintsCompatibility
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
comment|/**      * Marshal from XML to JSON      */
annotation|@
name|Override
DECL|method|marshal (Exchange exchange, Object graph, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|streamTreatment
init|=
literal|true
decl_stmt|;
comment|// try to process as an InputStream if it's not a String
name|Object
name|xml
init|=
name|graph
operator|instanceof
name|String
condition|?
literal|null
else|:
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|graph
argument_list|)
decl_stmt|;
comment|// if conversion to InputStream was unfeasible, fall back to String
if|if
condition|(
name|xml
operator|==
literal|null
condition|)
block|{
name|xml
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|graph
argument_list|)
expr_stmt|;
name|streamTreatment
operator|=
literal|false
expr_stmt|;
block|}
name|JSON
name|json
decl_stmt|;
comment|// perform the marshaling to JSON
if|if
condition|(
name|streamTreatment
condition|)
block|{
name|json
operator|=
name|serializer
operator|.
name|readFromStream
argument_list|(
operator|(
name|InputStream
operator|)
name|xml
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|json
operator|=
name|serializer
operator|.
name|read
argument_list|(
operator|(
name|String
operator|)
name|xml
argument_list|)
expr_stmt|;
block|}
comment|// don't return the default setting here
name|String
name|encoding
init|=
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
name|encoding
operator|=
name|getEncoding
argument_list|()
expr_stmt|;
block|}
name|OutputStreamWriter
name|osw
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
name|osw
operator|=
operator|new
name|OutputStreamWriter
argument_list|(
name|stream
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|osw
operator|=
operator|new
name|OutputStreamWriter
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
name|json
operator|.
name|write
argument_list|(
name|osw
argument_list|)
expr_stmt|;
name|osw
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
comment|/**      * Convert from JSON to XML      */
annotation|@
name|Override
DECL|method|unmarshal (Exchange exchange, InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|inBody
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|JSON
name|toConvert
decl_stmt|;
comment|// if the incoming object is already a JSON object, process as-is,
comment|// otherwise parse it as a String
if|if
condition|(
name|inBody
operator|instanceof
name|JSON
condition|)
block|{
name|toConvert
operator|=
operator|(
name|JSON
operator|)
name|inBody
expr_stmt|;
block|}
else|else
block|{
name|String
name|jsonString
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|inBody
argument_list|)
decl_stmt|;
name|toConvert
operator|=
name|JSONSerializer
operator|.
name|toJSON
argument_list|(
name|jsonString
argument_list|)
expr_stmt|;
block|}
return|return
name|convertToXMLUsingEncoding
argument_list|(
name|toConvert
argument_list|)
return|;
block|}
DECL|method|convertToXMLUsingEncoding (JSON json)
specifier|private
name|String
name|convertToXMLUsingEncoding
parameter_list|(
name|JSON
name|json
parameter_list|)
block|{
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
return|return
name|serializer
operator|.
name|write
argument_list|(
name|json
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|serializer
operator|.
name|write
argument_list|(
name|json
argument_list|,
name|encoding
argument_list|)
return|;
block|}
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getSerializer ()
specifier|public
name|XMLSerializer
name|getSerializer
parameter_list|()
block|{
return|return
name|serializer
return|;
block|}
DECL|method|getEncoding ()
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
comment|/**      * Sets the encoding for the call to {@link XMLSerializer#write(JSON, String)}      */
DECL|method|setEncoding (String encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
DECL|method|getForceTopLevelObject ()
specifier|public
name|Boolean
name|getForceTopLevelObject
parameter_list|()
block|{
return|return
name|forceTopLevelObject
return|;
block|}
comment|/**      * See {@link XMLSerializer#setForceTopLevelObject(boolean)}      */
DECL|method|setForceTopLevelObject (Boolean forceTopLevelObject)
specifier|public
name|void
name|setForceTopLevelObject
parameter_list|(
name|Boolean
name|forceTopLevelObject
parameter_list|)
block|{
name|this
operator|.
name|forceTopLevelObject
operator|=
name|forceTopLevelObject
expr_stmt|;
block|}
DECL|method|getNamespaceLenient ()
specifier|public
name|Boolean
name|getNamespaceLenient
parameter_list|()
block|{
return|return
name|namespaceLenient
return|;
block|}
comment|/**      * See {@link XMLSerializer#setNamespaceLenient(boolean)}      */
DECL|method|setNamespaceLenient (Boolean namespaceLenient)
specifier|public
name|void
name|setNamespaceLenient
parameter_list|(
name|Boolean
name|namespaceLenient
parameter_list|)
block|{
name|this
operator|.
name|namespaceLenient
operator|=
name|namespaceLenient
expr_stmt|;
block|}
DECL|method|getNamespaceMappings ()
specifier|public
name|List
argument_list|<
name|NamespacesPerElementMapping
argument_list|>
name|getNamespaceMappings
parameter_list|()
block|{
return|return
name|namespaceMappings
return|;
block|}
comment|/**      * Sets associations between elements and namespace mappings. Will only be used when converting from JSON to XML.      * For every association, the whenever a JSON element is found that matches {@link NamespacesPerElementMapping#element},      * the namespaces declarations specified by {@link NamespacesPerElementMapping#namespaces} will be output.      * @see {@link XMLSerializer#addNamespace(String, String, String)}      */
DECL|method|setNamespaceMappings (List<NamespacesPerElementMapping> namespaceMappings)
specifier|public
name|void
name|setNamespaceMappings
parameter_list|(
name|List
argument_list|<
name|NamespacesPerElementMapping
argument_list|>
name|namespaceMappings
parameter_list|)
block|{
name|this
operator|.
name|namespaceMappings
operator|=
name|namespaceMappings
expr_stmt|;
block|}
DECL|method|getRootName ()
specifier|public
name|String
name|getRootName
parameter_list|()
block|{
return|return
name|rootName
return|;
block|}
comment|/**      * See {@link XMLSerializer#setRootName(String)}      */
DECL|method|setRootName (String rootName)
specifier|public
name|void
name|setRootName
parameter_list|(
name|String
name|rootName
parameter_list|)
block|{
name|this
operator|.
name|rootName
operator|=
name|rootName
expr_stmt|;
block|}
DECL|method|getSkipWhitespace ()
specifier|public
name|Boolean
name|getSkipWhitespace
parameter_list|()
block|{
return|return
name|skipWhitespace
return|;
block|}
comment|/**      * See {@link XMLSerializer#setSkipWhitespace(boolean)}      */
DECL|method|setSkipWhitespace (Boolean skipWhitespace)
specifier|public
name|void
name|setSkipWhitespace
parameter_list|(
name|Boolean
name|skipWhitespace
parameter_list|)
block|{
name|this
operator|.
name|skipWhitespace
operator|=
name|skipWhitespace
expr_stmt|;
block|}
DECL|method|getTrimSpaces ()
specifier|public
name|Boolean
name|getTrimSpaces
parameter_list|()
block|{
return|return
name|trimSpaces
return|;
block|}
comment|/**      * See {@link XMLSerializer#setTrimSpaces(boolean)}      */
DECL|method|setTrimSpaces (Boolean trimSpaces)
specifier|public
name|void
name|setTrimSpaces
parameter_list|(
name|Boolean
name|trimSpaces
parameter_list|)
block|{
name|this
operator|.
name|trimSpaces
operator|=
name|trimSpaces
expr_stmt|;
block|}
DECL|method|getTypeHints ()
specifier|public
name|TypeHintsEnum
name|getTypeHints
parameter_list|()
block|{
return|return
name|typeHints
return|;
block|}
comment|/**      * See {@link XMLSerializer#setTypeHintsEnabled(boolean)} and {@link XMLSerializer#setTypeHintsCompatibility(boolean)}      * @param typeHints a key in the {@link TypeHintsEnum} enumeration      */
DECL|method|setTypeHints (String typeHints)
specifier|public
name|void
name|setTypeHints
parameter_list|(
name|String
name|typeHints
parameter_list|)
block|{
name|this
operator|.
name|typeHints
operator|=
name|TypeHintsEnum
operator|.
name|valueOf
argument_list|(
name|typeHints
argument_list|)
expr_stmt|;
block|}
DECL|method|getSkipNamespaces ()
specifier|public
name|Boolean
name|getSkipNamespaces
parameter_list|()
block|{
return|return
name|skipNamespaces
return|;
block|}
comment|/**      * See {@link XMLSerializer#setSkipNamespaces(boolean)}      */
DECL|method|setSkipNamespaces (Boolean skipNamespaces)
specifier|public
name|void
name|setSkipNamespaces
parameter_list|(
name|Boolean
name|skipNamespaces
parameter_list|)
block|{
name|this
operator|.
name|skipNamespaces
operator|=
name|skipNamespaces
expr_stmt|;
block|}
comment|/**      * See {@link XMLSerializer#setElementName(String)}      */
DECL|method|setElementName (String elementName)
specifier|public
name|void
name|setElementName
parameter_list|(
name|String
name|elementName
parameter_list|)
block|{
name|this
operator|.
name|elementName
operator|=
name|elementName
expr_stmt|;
block|}
DECL|method|getElementName ()
specifier|public
name|String
name|getElementName
parameter_list|()
block|{
return|return
name|elementName
return|;
block|}
comment|/**      * See {@link XMLSerializer#setArrayName(String)}      */
DECL|method|setArrayName (String arrayName)
specifier|public
name|void
name|setArrayName
parameter_list|(
name|String
name|arrayName
parameter_list|)
block|{
name|this
operator|.
name|arrayName
operator|=
name|arrayName
expr_stmt|;
block|}
DECL|method|getArrayName ()
specifier|public
name|String
name|getArrayName
parameter_list|()
block|{
return|return
name|arrayName
return|;
block|}
comment|/**      * See {@link XMLSerializer#setExpandableProperties(String[])}      */
DECL|method|setExpandableProperties (List<String> expandableProperties)
specifier|public
name|void
name|setExpandableProperties
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|expandableProperties
parameter_list|)
block|{
name|this
operator|.
name|expandableProperties
operator|=
name|expandableProperties
expr_stmt|;
block|}
DECL|method|getExpandableProperties ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExpandableProperties
parameter_list|()
block|{
return|return
name|expandableProperties
return|;
block|}
comment|/**      * See {@link XMLSerializer#setRemoveNamespacePrefixFromElements(boolean)}      */
DECL|method|setRemoveNamespacePrefixes (Boolean removeNamespacePrefixes)
specifier|public
name|void
name|setRemoveNamespacePrefixes
parameter_list|(
name|Boolean
name|removeNamespacePrefixes
parameter_list|)
block|{
name|this
operator|.
name|removeNamespacePrefixes
operator|=
name|removeNamespacePrefixes
expr_stmt|;
block|}
DECL|method|getRemoveNamespacePrefixes ()
specifier|public
name|Boolean
name|getRemoveNamespacePrefixes
parameter_list|()
block|{
return|return
name|removeNamespacePrefixes
return|;
block|}
comment|/**      * Encapsulates the information needed to bind namespace declarations to XML elements when performing JSON to XML conversions      * Given the following JSON: { "root:": { "element": "value", "element2": "value2" }}, it will produce the following XML when "element" is      * bound to prefix "ns1" and namespace URI "http://mynamespace.org":      *<root><element xmlns:ns1="http://mynamespace.org">value</element><element2>value2</element2></root>      * For convenience, the {@link NamespacesPerElementMapping#NamespacesPerElementMapping(String, String)} constructor allows to specify      * multiple prefix-namespaceURI pairs in just one String line, the format being: |ns1|http://mynamespace.org|ns2|http://mynamespace2.org|      *      */
DECL|class|NamespacesPerElementMapping
specifier|public
specifier|static
class|class
name|NamespacesPerElementMapping
block|{
DECL|field|element
specifier|public
name|String
name|element
decl_stmt|;
DECL|field|namespaces
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
decl_stmt|;
DECL|method|NamespacesPerElementMapping (String element, Map<String, String> namespaces)
specifier|public
name|NamespacesPerElementMapping
parameter_list|(
name|String
name|element
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
name|this
operator|.
name|element
operator|=
name|element
expr_stmt|;
name|this
operator|.
name|namespaces
operator|=
name|namespaces
expr_stmt|;
block|}
DECL|method|NamespacesPerElementMapping (String element, String prefix, String uri)
specifier|public
name|NamespacesPerElementMapping
parameter_list|(
name|String
name|element
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|element
operator|=
name|element
expr_stmt|;
name|this
operator|.
name|namespaces
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|namespaces
operator|.
name|put
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
DECL|method|NamespacesPerElementMapping (String element, String pipeSeparatedMappings)
specifier|public
name|NamespacesPerElementMapping
parameter_list|(
name|String
name|element
parameter_list|,
name|String
name|pipeSeparatedMappings
parameter_list|)
block|{
name|this
operator|.
name|element
operator|=
name|element
expr_stmt|;
name|this
operator|.
name|namespaces
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|String
index|[]
name|origTokens
init|=
name|pipeSeparatedMappings
operator|.
name|split
argument_list|(
literal|"\\|"
argument_list|)
decl_stmt|;
comment|// drop the first token
name|String
index|[]
name|tokens
init|=
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|origTokens
argument_list|,
literal|1
argument_list|,
name|origTokens
operator|.
name|length
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokens
operator|.
name|length
operator|%
literal|2
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Even number of prefix-namespace tokens is expected, number of tokens parsed: "
operator|+
name|tokens
operator|.
name|length
argument_list|)
throw|;
block|}
name|int
name|i
init|=
literal|0
decl_stmt|;
comment|// |ns1|http://test.org|ns2|http://test2.org|
while|while
condition|(
name|i
operator|<
operator|(
name|tokens
operator|.
name|length
operator|-
literal|1
operator|)
condition|)
block|{
name|this
operator|.
name|namespaces
operator|.
name|put
argument_list|(
name|tokens
index|[
name|i
index|]
argument_list|,
name|tokens
index|[
operator|++
name|i
index|]
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

