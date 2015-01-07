begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|Arrays
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
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
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
name|annotation
operator|.
name|XmlAccessorType
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
name|annotation
operator|.
name|XmlAttribute
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
name|annotation
operator|.
name|XmlList
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
name|annotation
operator|.
name|XmlRootElement
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
name|Label
import|;
end_import

begin_comment
comment|/**  * Represents a<a href="http://camel.apache.org/xmljson.html">XML-JSON</a> {@link org.apache.camel.spi.DataFormat}.  *  * @version   */
end_comment

begin_class
annotation|@
name|Label
argument_list|(
literal|"dataformat,transformation"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"xmljson"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|XmlJsonDataFormat
specifier|public
class|class
name|XmlJsonDataFormat
extends|extends
name|DataFormatDefinition
block|{
DECL|field|TYPE_HINTS
specifier|public
specifier|static
specifier|final
name|String
name|TYPE_HINTS
init|=
literal|"typeHints"
decl_stmt|;
DECL|field|REMOVE_NAMESPACE_PREFIXES
specifier|public
specifier|static
specifier|final
name|String
name|REMOVE_NAMESPACE_PREFIXES
init|=
literal|"removeNamespacePrefixes"
decl_stmt|;
DECL|field|SKIP_NAMESPACES
specifier|public
specifier|static
specifier|final
name|String
name|SKIP_NAMESPACES
init|=
literal|"skipNamespaces"
decl_stmt|;
DECL|field|TRIM_SPACES
specifier|public
specifier|static
specifier|final
name|String
name|TRIM_SPACES
init|=
literal|"trimSpaces"
decl_stmt|;
DECL|field|SKIP_WHITESPACE
specifier|public
specifier|static
specifier|final
name|String
name|SKIP_WHITESPACE
init|=
literal|"skipWhitespace"
decl_stmt|;
DECL|field|EXPANDABLE_PROPERTIES
specifier|public
specifier|static
specifier|final
name|String
name|EXPANDABLE_PROPERTIES
init|=
literal|"expandableProperties"
decl_stmt|;
DECL|field|ARRAY_NAME
specifier|public
specifier|static
specifier|final
name|String
name|ARRAY_NAME
init|=
literal|"arrayName"
decl_stmt|;
DECL|field|ELEMENT_NAME
specifier|public
specifier|static
specifier|final
name|String
name|ELEMENT_NAME
init|=
literal|"elementName"
decl_stmt|;
DECL|field|ROOT_NAME
specifier|public
specifier|static
specifier|final
name|String
name|ROOT_NAME
init|=
literal|"rootName"
decl_stmt|;
DECL|field|NAMESPACE_LENIENT
specifier|public
specifier|static
specifier|final
name|String
name|NAMESPACE_LENIENT
init|=
literal|"namespaceLenient"
decl_stmt|;
DECL|field|FORCE_TOP_LEVEL_OBJECT
specifier|public
specifier|static
specifier|final
name|String
name|FORCE_TOP_LEVEL_OBJECT
init|=
literal|"forceTopLevelObject"
decl_stmt|;
DECL|field|ENCODING
specifier|public
specifier|static
specifier|final
name|String
name|ENCODING
init|=
literal|"encoding"
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|elementName
specifier|private
name|String
name|elementName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|arrayName
specifier|private
name|String
name|arrayName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|forceTopLevelObject
specifier|private
name|Boolean
name|forceTopLevelObject
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|namespaceLenient
specifier|private
name|Boolean
name|namespaceLenient
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|rootName
specifier|private
name|String
name|rootName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|skipWhitespace
specifier|private
name|Boolean
name|skipWhitespace
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|trimSpaces
specifier|private
name|Boolean
name|trimSpaces
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|skipNamespaces
specifier|private
name|Boolean
name|skipNamespaces
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|removeNamespacePrefixes
specifier|private
name|Boolean
name|removeNamespacePrefixes
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|XmlList
DECL|field|expandableProperties
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|expandableProperties
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|typeHints
specifier|private
name|String
name|typeHints
decl_stmt|;
DECL|method|XmlJsonDataFormat ()
specifier|public
name|XmlJsonDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"xmljson"
argument_list|)
expr_stmt|;
block|}
DECL|method|XmlJsonDataFormat (Map<String, String> options)
specifier|public
name|XmlJsonDataFormat
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
parameter_list|)
block|{
name|super
argument_list|(
literal|"xmljson"
argument_list|)
expr_stmt|;
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|ENCODING
argument_list|)
condition|)
block|{
name|encoding
operator|=
name|options
operator|.
name|get
argument_list|(
name|ENCODING
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|FORCE_TOP_LEVEL_OBJECT
argument_list|)
condition|)
block|{
name|forceTopLevelObject
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|options
operator|.
name|get
argument_list|(
name|FORCE_TOP_LEVEL_OBJECT
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|NAMESPACE_LENIENT
argument_list|)
condition|)
block|{
name|namespaceLenient
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|options
operator|.
name|get
argument_list|(
name|NAMESPACE_LENIENT
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|ROOT_NAME
argument_list|)
condition|)
block|{
name|rootName
operator|=
name|options
operator|.
name|get
argument_list|(
name|ROOT_NAME
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|ELEMENT_NAME
argument_list|)
condition|)
block|{
name|elementName
operator|=
name|options
operator|.
name|get
argument_list|(
name|ELEMENT_NAME
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|ARRAY_NAME
argument_list|)
condition|)
block|{
name|arrayName
operator|=
name|options
operator|.
name|get
argument_list|(
name|ARRAY_NAME
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|EXPANDABLE_PROPERTIES
argument_list|)
condition|)
block|{
name|expandableProperties
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|options
operator|.
name|get
argument_list|(
name|EXPANDABLE_PROPERTIES
argument_list|)
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|SKIP_WHITESPACE
argument_list|)
condition|)
block|{
name|skipWhitespace
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|options
operator|.
name|get
argument_list|(
name|SKIP_WHITESPACE
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|TRIM_SPACES
argument_list|)
condition|)
block|{
name|trimSpaces
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|options
operator|.
name|get
argument_list|(
name|TRIM_SPACES
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|SKIP_NAMESPACES
argument_list|)
condition|)
block|{
name|skipNamespaces
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|options
operator|.
name|get
argument_list|(
name|SKIP_NAMESPACES
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|REMOVE_NAMESPACE_PREFIXES
argument_list|)
condition|)
block|{
name|removeNamespacePrefixes
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|options
operator|.
name|get
argument_list|(
name|REMOVE_NAMESPACE_PREFIXES
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|containsKey
argument_list|(
name|TYPE_HINTS
argument_list|)
condition|)
block|{
name|typeHints
operator|=
name|options
operator|.
name|get
argument_list|(
name|TYPE_HINTS
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|encoding
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
name|ENCODING
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|forceTopLevelObject
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
name|FORCE_TOP_LEVEL_OBJECT
argument_list|,
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
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
name|NAMESPACE_LENIENT
argument_list|,
name|namespaceLenient
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|rootName
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
name|ROOT_NAME
argument_list|,
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
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
name|ELEMENT_NAME
argument_list|,
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
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
name|ARRAY_NAME
argument_list|,
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
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
name|EXPANDABLE_PROPERTIES
argument_list|,
name|expandableProperties
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
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
name|SKIP_WHITESPACE
argument_list|,
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
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
name|TRIM_SPACES
argument_list|,
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
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
name|SKIP_NAMESPACES
argument_list|,
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
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
name|REMOVE_NAMESPACE_PREFIXES
argument_list|,
name|removeNamespacePrefixes
argument_list|)
expr_stmt|;
block|}
comment|// will end up calling the setTypeHints(String s) which does the parsing from the Enum String key to the Enum value
if|if
condition|(
name|typeHints
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
name|TYPE_HINTS
argument_list|,
name|typeHints
argument_list|)
expr_stmt|;
block|}
comment|//TODO: xmljson: element-namespace mapping is not implemented in the XML DSL
comment|// depending on adoption rate of this data format, we'll make this data format NamespaceAware so that it gets
comment|// the prefix-namespaceURI mappings from the context, and with a new attribute called "namespacedElements",
comment|// we'll associate named elements with prefixes following a format "element1:prefix1,element2:prefix2,..."
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
DECL|method|getTypeHints ()
specifier|public
name|String
name|getTypeHints
parameter_list|()
block|{
return|return
name|typeHints
return|;
block|}
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
name|typeHints
expr_stmt|;
block|}
block|}
end_class

end_unit

