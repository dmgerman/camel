begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.xmljson.springboot
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
operator|.
name|springboot
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
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|boot
operator|.
name|DataFormatConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Camel XML JSON Data Format  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.dataformat.xmljson"
argument_list|)
DECL|class|XmlJsonDataFormatConfiguration
specifier|public
class|class
name|XmlJsonDataFormatConfiguration
extends|extends
name|DataFormatConfigurationPropertiesCommon
block|{
comment|/**      * Sets the encoding. Used for unmarshalling (JSON to XML conversion).      */
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
comment|/**      * Specifies the name of the XML elements representing each array element.      * Used for unmarshalling (JSON to XML conversion).      */
DECL|field|elementName
specifier|private
name|String
name|elementName
decl_stmt|;
comment|/**      * Specifies the name of the top-level XML element. Used for unmarshalling      * (JSON to XML conversion). For example when converting 1 2 3 it will be      * output by default as 123. By setting this option or rootName you can      * alter the name of element 'a'.      */
DECL|field|arrayName
specifier|private
name|String
name|arrayName
decl_stmt|;
comment|/**      * Determines whether the resulting JSON will start off with a top-most      * element whose name matches the XML root element. Used for marshalling      * (XML to JSon conversion). If disabled XML string 12 turns into 'x: '1'      * 'y': '2' . Otherwise it turns into 'a': 'x: '1' 'y': '2' .      */
DECL|field|forceTopLevelObject
specifier|private
name|Boolean
name|forceTopLevelObject
init|=
literal|false
decl_stmt|;
comment|/**      * Flag to be tolerant to incomplete namespace prefixes. Used for      * unmarshalling (JSON to XML conversion). In most cases json-lib      * automatically changes this flag at runtime to match the processing.      */
DECL|field|namespaceLenient
specifier|private
name|Boolean
name|namespaceLenient
init|=
literal|false
decl_stmt|;
comment|/**      * Specifies the name of the top-level element. Used for unmarshalling (JSON      * to XML conversion). If not set json-lib will use arrayName or objectName      * (default value: 'o' at the current time it is not configurable in this      * data format). If set to 'root' the JSON string 'x': 'value1' 'y' :      * 'value2' would turn into value1value2 otherwise the 'root' element would      * be named 'o'.      */
DECL|field|rootName
specifier|private
name|String
name|rootName
decl_stmt|;
comment|/**      * Determines whether white spaces between XML elements will be regarded as      * text values or disregarded. Used for marshalling (XML to JSon      * conversion).      */
DECL|field|skipWhitespace
specifier|private
name|Boolean
name|skipWhitespace
init|=
literal|false
decl_stmt|;
comment|/**      * Determines whether leading and trailing white spaces will be omitted from      * String values. Used for marshalling (XML to JSon conversion).      */
DECL|field|trimSpaces
specifier|private
name|Boolean
name|trimSpaces
init|=
literal|false
decl_stmt|;
comment|/**      * Signals whether namespaces should be ignored. By default they will be      * added to the JSON output using xmlns elements. Used for marshalling (XML      * to JSon conversion).      */
DECL|field|skipNamespaces
specifier|private
name|Boolean
name|skipNamespaces
init|=
literal|false
decl_stmt|;
comment|/**      * Removes the namespace prefixes from XML qualified elements so that the      * resulting JSON string does not contain them. Used for marshalling (XML to      * JSon conversion).      */
DECL|field|removeNamespacePrefixes
specifier|private
name|Boolean
name|removeNamespacePrefixes
init|=
literal|false
decl_stmt|;
comment|/**      * With expandable properties JSON array elements are converted to XML as a      * sequence of repetitive XML elements with the local name equal to the JSON      * key for example: number: 123 normally converted to: 123 (where e can be      * modified by setting elementName) would instead translate to 123 if number      * is set as an expandable property Used for unmarshalling (JSON to XML      * conversion).      */
DECL|field|expandableProperties
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|expandableProperties
decl_stmt|;
comment|/**      * Adds type hints to the resulting XML to aid conversion back to JSON. Used      * for unmarshalling (JSON to XML conversion).      */
DECL|field|typeHints
specifier|private
name|String
name|typeHints
decl_stmt|;
comment|/**      * Whether the data format should set the Content-Type header with the type      * from the data format if the data format is capable of doing so. For      * example application/xml for data formats marshalling to XML or      * application/json for data formats marshalling to JSon etc.      */
DECL|field|contentTypeHeader
specifier|private
name|Boolean
name|contentTypeHeader
init|=
literal|false
decl_stmt|;
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
DECL|method|getContentTypeHeader ()
specifier|public
name|Boolean
name|getContentTypeHeader
parameter_list|()
block|{
return|return
name|contentTypeHeader
return|;
block|}
DECL|method|setContentTypeHeader (Boolean contentTypeHeader)
specifier|public
name|void
name|setContentTypeHeader
parameter_list|(
name|Boolean
name|contentTypeHeader
parameter_list|)
block|{
name|this
operator|.
name|contentTypeHeader
operator|=
name|contentTypeHeader
expr_stmt|;
block|}
block|}
end_class

end_unit

