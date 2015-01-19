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
name|XmlRootElement
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

begin_comment
comment|/**  * JAXB data format  *  * @version   */
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
literal|"jaxb"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|JaxbDataFormat
specifier|public
class|class
name|JaxbDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|contextPath
specifier|private
name|String
name|contextPath
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|schema
specifier|private
name|String
name|schema
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|prettyPrint
specifier|private
name|Boolean
name|prettyPrint
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ignoreJAXBElement
specifier|private
name|Boolean
name|ignoreJAXBElement
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|mustBeJAXBElement
specifier|private
name|Boolean
name|mustBeJAXBElement
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|filterNonXmlChars
specifier|private
name|Boolean
name|filterNonXmlChars
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
DECL|field|fragment
specifier|private
name|Boolean
name|fragment
decl_stmt|;
comment|// Partial encoding
annotation|@
name|XmlAttribute
DECL|field|partClass
specifier|private
name|String
name|partClass
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|partNamespace
specifier|private
name|String
name|partNamespace
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|namespacePrefixRef
specifier|private
name|String
name|namespacePrefixRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|xmlStreamWriterWrapper
specifier|private
name|String
name|xmlStreamWriterWrapper
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|schemaLocation
specifier|private
name|String
name|schemaLocation
decl_stmt|;
DECL|method|JaxbDataFormat ()
specifier|public
name|JaxbDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"jaxb"
argument_list|)
expr_stmt|;
block|}
DECL|method|JaxbDataFormat (boolean prettyPrint)
specifier|public
name|JaxbDataFormat
parameter_list|(
name|boolean
name|prettyPrint
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setPrettyPrint
argument_list|(
name|prettyPrint
argument_list|)
expr_stmt|;
block|}
DECL|method|getContextPath ()
specifier|public
name|String
name|getContextPath
parameter_list|()
block|{
return|return
name|contextPath
return|;
block|}
comment|/**      * Package name where your JAXB classes are located.      */
DECL|method|setContextPath (String contextPath)
specifier|public
name|void
name|setContextPath
parameter_list|(
name|String
name|contextPath
parameter_list|)
block|{
name|this
operator|.
name|contextPath
operator|=
name|contextPath
expr_stmt|;
block|}
DECL|method|getSchema ()
specifier|public
name|String
name|getSchema
parameter_list|()
block|{
return|return
name|schema
return|;
block|}
comment|/**      * To validate against an existing schema.      * Your can use the prefix classpath:, file:* or *http: to specify how the resource should by resolved.      * You can separate multiple schema files by using the ',' character.      */
DECL|method|setSchema (String schema)
specifier|public
name|void
name|setSchema
parameter_list|(
name|String
name|schema
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
DECL|method|getPrettyPrint ()
specifier|public
name|Boolean
name|getPrettyPrint
parameter_list|()
block|{
return|return
name|prettyPrint
return|;
block|}
comment|/**      * To enable pretty printing output nicely formatted.      *<p/>      * Is by default false.      */
DECL|method|setPrettyPrint (Boolean prettyPrint)
specifier|public
name|void
name|setPrettyPrint
parameter_list|(
name|Boolean
name|prettyPrint
parameter_list|)
block|{
name|this
operator|.
name|prettyPrint
operator|=
name|prettyPrint
expr_stmt|;
block|}
DECL|method|getIgnoreJAXBElement ()
specifier|public
name|Boolean
name|getIgnoreJAXBElement
parameter_list|()
block|{
return|return
name|ignoreJAXBElement
return|;
block|}
comment|/**      * Whether to ignore JAXBElement elements - only needed to be set to false in very special use-cases.      */
DECL|method|setIgnoreJAXBElement (Boolean ignoreJAXBElement)
specifier|public
name|void
name|setIgnoreJAXBElement
parameter_list|(
name|Boolean
name|ignoreJAXBElement
parameter_list|)
block|{
name|this
operator|.
name|ignoreJAXBElement
operator|=
name|ignoreJAXBElement
expr_stmt|;
block|}
DECL|method|getMustBeJAXBElement ()
specifier|public
name|Boolean
name|getMustBeJAXBElement
parameter_list|()
block|{
return|return
name|mustBeJAXBElement
return|;
block|}
comment|/**      * Whether marhsalling must be java objects with JAXB annotations. And if not then it fails.      * This option can be set to false to relax that, such as when the data is already in XML format.      */
DECL|method|setMustBeJAXBElement (Boolean mustBeJAXBElement)
specifier|public
name|void
name|setMustBeJAXBElement
parameter_list|(
name|Boolean
name|mustBeJAXBElement
parameter_list|)
block|{
name|this
operator|.
name|mustBeJAXBElement
operator|=
name|mustBeJAXBElement
expr_stmt|;
block|}
comment|/**      * To turn on marshalling XML fragment trees.      * By default JAXB looks for @XmlRootElement annotation on given class to operate on whole XML tree.      * This is useful but not always - sometimes generated code does not have @XmlRootElement annotation,      * sometimes you need unmarshall only part of tree.      * In that case you can use partial unmarshalling. To enable this behaviours you need set property partClass.      * Camel will pass this class to JAXB's unmarshaler.      */
DECL|method|setFragment (Boolean fragment)
specifier|public
name|void
name|setFragment
parameter_list|(
name|Boolean
name|fragment
parameter_list|)
block|{
name|this
operator|.
name|fragment
operator|=
name|fragment
expr_stmt|;
block|}
DECL|method|getFragment ()
specifier|public
name|Boolean
name|getFragment
parameter_list|()
block|{
return|return
name|fragment
return|;
block|}
DECL|method|getFilterNonXmlChars ()
specifier|public
name|Boolean
name|getFilterNonXmlChars
parameter_list|()
block|{
return|return
name|filterNonXmlChars
return|;
block|}
comment|/**      * To ignore non xml characheters and replace them with an empty space.      */
DECL|method|setFilterNonXmlChars (Boolean filterNonXmlChars)
specifier|public
name|void
name|setFilterNonXmlChars
parameter_list|(
name|Boolean
name|filterNonXmlChars
parameter_list|)
block|{
name|this
operator|.
name|filterNonXmlChars
operator|=
name|filterNonXmlChars
expr_stmt|;
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
comment|/**      * To overrule and use a specific encoding      */
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
DECL|method|getPartClass ()
specifier|public
name|String
name|getPartClass
parameter_list|()
block|{
return|return
name|partClass
return|;
block|}
comment|/**      * Name of class used for fragment parsing.      *<p/>      * See more details at the fragment option.      */
DECL|method|setPartClass (String partClass)
specifier|public
name|void
name|setPartClass
parameter_list|(
name|String
name|partClass
parameter_list|)
block|{
name|this
operator|.
name|partClass
operator|=
name|partClass
expr_stmt|;
block|}
DECL|method|getPartNamespace ()
specifier|public
name|String
name|getPartNamespace
parameter_list|()
block|{
return|return
name|partNamespace
return|;
block|}
comment|/**      * XML namespace to use for fragment parsing.      *<p/>      * See more details at the fragment option.      */
DECL|method|setPartNamespace (String partNamespace)
specifier|public
name|void
name|setPartNamespace
parameter_list|(
name|String
name|partNamespace
parameter_list|)
block|{
name|this
operator|.
name|partNamespace
operator|=
name|partNamespace
expr_stmt|;
block|}
DECL|method|getNamespacePrefixRef ()
specifier|public
name|String
name|getNamespacePrefixRef
parameter_list|()
block|{
return|return
name|namespacePrefixRef
return|;
block|}
comment|/**      * When marshalling using JAXB or SOAP then the JAXB implementation will automatic assign namespace prefixes,      * such as ns2, ns3, ns4 etc. To control this mapping, Camel allows you to refer to a map which contains the desired mapping.      */
DECL|method|setNamespacePrefixRef (String namespacePrefixRef)
specifier|public
name|void
name|setNamespacePrefixRef
parameter_list|(
name|String
name|namespacePrefixRef
parameter_list|)
block|{
name|this
operator|.
name|namespacePrefixRef
operator|=
name|namespacePrefixRef
expr_stmt|;
block|}
DECL|method|getXmlStreamWriterWrapper ()
specifier|public
name|String
name|getXmlStreamWriterWrapper
parameter_list|()
block|{
return|return
name|xmlStreamWriterWrapper
return|;
block|}
comment|/**      * To use a custom xml stream writer.      */
DECL|method|setXmlStreamWriterWrapper (String xmlStreamWriterWrapperRef)
specifier|public
name|void
name|setXmlStreamWriterWrapper
parameter_list|(
name|String
name|xmlStreamWriterWrapperRef
parameter_list|)
block|{
name|this
operator|.
name|xmlStreamWriterWrapper
operator|=
name|xmlStreamWriterWrapperRef
expr_stmt|;
block|}
DECL|method|getSchemaLocation ()
specifier|public
name|String
name|getSchemaLocation
parameter_list|()
block|{
return|return
name|schemaLocation
return|;
block|}
comment|/**      * To define the location of the schema      */
DECL|method|setSchemaLocation (String schemaLocation)
specifier|public
name|void
name|setSchemaLocation
parameter_list|(
name|String
name|schemaLocation
parameter_list|)
block|{
name|this
operator|.
name|schemaLocation
operator|=
name|schemaLocation
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
if|if
condition|(
name|partClass
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
name|partClass
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|partNamespace
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
name|partNamespace
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|"encoding"
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|namespacePrefixRef
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
name|namespacePrefixRef
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
name|contextPath
argument_list|)
expr_stmt|;
if|if
condition|(
name|schema
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
name|schema
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xmlStreamWriterWrapper
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
name|xmlStreamWriterWrapper
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|schemaLocation
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
name|schemaLocation
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

