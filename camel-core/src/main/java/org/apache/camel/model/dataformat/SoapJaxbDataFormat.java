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
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|spi
operator|.
name|Metadata
import|;
end_import

begin_comment
comment|/**  * SOAP data format  */
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
literal|"soapjaxb"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|SoapJaxbDataFormat
specifier|public
class|class
name|SoapJaxbDataFormat
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
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|elementNameStrategyRef
specifier|private
name|String
name|elementNameStrategyRef
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|elementNameStrategy
specifier|private
name|Object
name|elementNameStrategy
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"1.1"
argument_list|)
DECL|field|version
specifier|private
name|String
name|version
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
DECL|field|schema
specifier|private
name|String
name|schema
decl_stmt|;
DECL|method|SoapJaxbDataFormat ()
specifier|public
name|SoapJaxbDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"soapjaxb"
argument_list|)
expr_stmt|;
block|}
DECL|method|SoapJaxbDataFormat (String contextPath)
specifier|public
name|SoapJaxbDataFormat
parameter_list|(
name|String
name|contextPath
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setContextPath
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
block|}
DECL|method|SoapJaxbDataFormat (String contextPath, String elementNameStrategyRef)
specifier|public
name|SoapJaxbDataFormat
parameter_list|(
name|String
name|contextPath
parameter_list|,
name|String
name|elementNameStrategyRef
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setContextPath
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
name|setElementNameStrategyRef
argument_list|(
name|elementNameStrategyRef
argument_list|)
expr_stmt|;
block|}
DECL|method|SoapJaxbDataFormat (String contextPath, Object elementNameStrategy)
specifier|public
name|SoapJaxbDataFormat
parameter_list|(
name|String
name|contextPath
parameter_list|,
name|Object
name|elementNameStrategy
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setContextPath
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
name|setElementNameStrategy
argument_list|(
name|elementNameStrategy
argument_list|)
expr_stmt|;
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
comment|/**      * Refers to an element strategy to lookup from the registry.      *<p/>      * An element name strategy is used for two purposes. The first is to find a xml element name for a given object      * and soap action when marshaling the object into a SOAP message. The second is to find an Exception class for a given soap fault name.      *<p/>      * The following three element strategy class name is provided out of the box.      * QNameStrategy - Uses a fixed qName that is configured on instantiation. Exception lookup is not supported      * TypeNameStrategy - Uses the name and namespace from the @XMLType annotation of the given type. If no namespace is set then package-info is used. Exception lookup is not supported      * ServiceInterfaceStrategy - Uses information from a webservice interface to determine the type name and to find the exception class for a SOAP fault      *<p/>      * All three classes is located in the package name org.apache.camel.dataformat.soap.name      *<p/>      * If you have generated the web service stub code with cxf-codegen or a similar tool then you probably      * will want to use the ServiceInterfaceStrategy. In the case you have no annotated service interface you should use QNameStrategy or TypeNameStrategy.      */
DECL|method|setElementNameStrategyRef (String elementNameStrategyRef)
specifier|public
name|void
name|setElementNameStrategyRef
parameter_list|(
name|String
name|elementNameStrategyRef
parameter_list|)
block|{
name|this
operator|.
name|elementNameStrategyRef
operator|=
name|elementNameStrategyRef
expr_stmt|;
block|}
DECL|method|getElementNameStrategyRef ()
specifier|public
name|String
name|getElementNameStrategyRef
parameter_list|()
block|{
return|return
name|elementNameStrategyRef
return|;
block|}
DECL|method|getVersion ()
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
comment|/**      * SOAP version should either be 1.1 or 1.2.      *<p/>      * Is by default 1.1      */
DECL|method|setVersion (String version)
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
comment|/**      * Sets an element strategy instance to use.      *<p/>      * An element name strategy is used for two purposes. The first is to find a xml element name for a given object      * and soap action when marshaling the object into a SOAP message. The second is to find an Exception class for a given soap fault name.      *<p/>      * The following three element strategy class name is provided out of the box.      * QNameStrategy - Uses a fixed qName that is configured on instantiation. Exception lookup is not supported      * TypeNameStrategy - Uses the name and namespace from the @XMLType annotation of the given type. If no namespace is set then package-info is used. Exception lookup is not supported      * ServiceInterfaceStrategy - Uses information from a webservice interface to determine the type name and to find the exception class for a SOAP fault      *<p/>      * All three classes is located in the package name org.apache.camel.dataformat.soap.name      *<p/>      * If you have generated the web service stub code with cxf-codegen or a similar tool then you probably      * will want to use the ServiceInterfaceStrategy. In the case you have no annotated service interface you should use QNameStrategy or TypeNameStrategy.      */
DECL|method|setElementNameStrategy (Object elementNameStrategy)
specifier|public
name|void
name|setElementNameStrategy
parameter_list|(
name|Object
name|elementNameStrategy
parameter_list|)
block|{
name|this
operator|.
name|elementNameStrategy
operator|=
name|elementNameStrategy
expr_stmt|;
block|}
DECL|method|getElementNameStrategy ()
specifier|public
name|Object
name|getElementNameStrategy
parameter_list|()
block|{
return|return
name|elementNameStrategy
return|;
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
name|elementNameStrategy
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
literal|"elementNameStrategy"
argument_list|,
name|elementNameStrategy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|elementNameStrategyRef
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
literal|"elementNameStrategyRef"
argument_list|,
name|elementNameStrategyRef
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
name|version
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
literal|"version"
argument_list|,
name|version
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
block|}
block|}
end_class

end_unit

