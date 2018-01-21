begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxb.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxb
operator|.
name|springboot
package|;
end_package

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
comment|/**  * JAXB data format uses the JAXB2 XML marshalling standard to unmarshal an XML  * payload into Java objects or to marshal Java objects into an XML payload.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.dataformat.jaxb"
argument_list|)
DECL|class|JaxbDataFormatConfiguration
specifier|public
class|class
name|JaxbDataFormatConfiguration
extends|extends
name|DataFormatConfigurationPropertiesCommon
block|{
comment|/**      * Package name where your JAXB classes are located.      */
DECL|field|contextPath
specifier|private
name|String
name|contextPath
decl_stmt|;
comment|/**      * To validate against an existing schema. Your can use the prefix      * classpath: file: or http: to specify how the resource should by resolved.      * You can separate multiple schema files by using the '' character.      */
DECL|field|schema
specifier|private
name|String
name|schema
decl_stmt|;
comment|/**      * Sets the schema severity level to use when validating against a schema.      * This level determines the minimum severity error that triggers JAXB to      * stop continue parsing. The default value of 0 (warning) means that any      * error (warning error or fatal error) will trigger JAXB to stop. There are      * the following three levels: 0=warning 1=error 2=fatal error.      */
DECL|field|schemaSeverityLevel
specifier|private
name|Integer
name|schemaSeverityLevel
init|=
literal|0
decl_stmt|;
comment|/**      * To enable pretty printing output nicely formatted. Is by default false.      */
DECL|field|prettyPrint
specifier|private
name|Boolean
name|prettyPrint
init|=
literal|false
decl_stmt|;
comment|/**      * Whether to allow using ObjectFactory classes to create the POJO classes      * during marshalling. This only applies to POJO classes that has not been      * annotated with JAXB and providing jaxb.index descriptor files.      */
DECL|field|objectFactory
specifier|private
name|Boolean
name|objectFactory
init|=
literal|false
decl_stmt|;
comment|/**      * Whether to ignore JAXBElement elements - only needed to be set to false      * in very special use-cases.      */
DECL|field|ignoreJAXBElement
specifier|private
name|Boolean
name|ignoreJAXBElement
init|=
literal|false
decl_stmt|;
comment|/**      * Whether marhsalling must be java objects with JAXB annotations. And if      * not then it fails. This option can be set to false to relax that such as      * when the data is already in XML format.      */
DECL|field|mustBeJAXBElement
specifier|private
name|Boolean
name|mustBeJAXBElement
init|=
literal|false
decl_stmt|;
comment|/**      * To ignore non xml characheters and replace them with an empty space.      */
DECL|field|filterNonXmlChars
specifier|private
name|Boolean
name|filterNonXmlChars
init|=
literal|false
decl_stmt|;
comment|/**      * To overrule and use a specific encoding      */
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
comment|/**      * To turn on marshalling XML fragment trees. By default JAXB looks for      * XmlRootElement annotation on given class to operate on whole XML tree.      * This is useful but not always - sometimes generated code does not have      * XmlRootElement annotation sometimes you need unmarshall only part of      * tree. In that case you can use partial unmarshalling. To enable this      * behaviours you need set property partClass. Camel will pass this class to      * JAXB's unmarshaler.      */
DECL|field|fragment
specifier|private
name|Boolean
name|fragment
init|=
literal|false
decl_stmt|;
comment|/**      * Name of class used for fragment parsing. See more details at the fragment      * option.      */
DECL|field|partClass
specifier|private
name|String
name|partClass
decl_stmt|;
comment|/**      * XML namespace to use for fragment parsing. See more details at the      * fragment option.      */
DECL|field|partNamespace
specifier|private
name|String
name|partNamespace
decl_stmt|;
comment|/**      * When marshalling using JAXB or SOAP then the JAXB implementation will      * automatic assign namespace prefixes such as ns2 ns3 ns4 etc. To control      * this mapping Camel allows you to refer to a map which contains the      * desired mapping.      */
DECL|field|namespacePrefixRef
specifier|private
name|String
name|namespacePrefixRef
decl_stmt|;
comment|/**      * To use a custom xml stream writer.      */
DECL|field|xmlStreamWriterWrapper
specifier|private
name|String
name|xmlStreamWriterWrapper
decl_stmt|;
comment|/**      * To define the location of the schema      */
DECL|field|schemaLocation
specifier|private
name|String
name|schemaLocation
decl_stmt|;
comment|/**      * To define the location of the namespaceless schema      */
DECL|field|noNamespaceSchemaLocation
specifier|private
name|String
name|noNamespaceSchemaLocation
decl_stmt|;
comment|/**      * Refers to a custom java.util.Map to lookup in the registry containing      * custom JAXB provider properties to be used with the JAXB marshaller.      */
DECL|field|jaxbProviderProperties
specifier|private
name|String
name|jaxbProviderProperties
decl_stmt|;
comment|/**      * Whether the data format should set the Content-Type header with the type      * from the data format if the data format is capable of doing so. For      * example application/xml for data formats marshalling to XML or      * application/json for data formats marshalling to JSon etc.      */
DECL|field|contentTypeHeader
specifier|private
name|Boolean
name|contentTypeHeader
init|=
literal|false
decl_stmt|;
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
DECL|method|getSchemaSeverityLevel ()
specifier|public
name|Integer
name|getSchemaSeverityLevel
parameter_list|()
block|{
return|return
name|schemaSeverityLevel
return|;
block|}
DECL|method|setSchemaSeverityLevel (Integer schemaSeverityLevel)
specifier|public
name|void
name|setSchemaSeverityLevel
parameter_list|(
name|Integer
name|schemaSeverityLevel
parameter_list|)
block|{
name|this
operator|.
name|schemaSeverityLevel
operator|=
name|schemaSeverityLevel
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
DECL|method|getObjectFactory ()
specifier|public
name|Boolean
name|getObjectFactory
parameter_list|()
block|{
return|return
name|objectFactory
return|;
block|}
DECL|method|setObjectFactory (Boolean objectFactory)
specifier|public
name|void
name|setObjectFactory
parameter_list|(
name|Boolean
name|objectFactory
parameter_list|)
block|{
name|this
operator|.
name|objectFactory
operator|=
name|objectFactory
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
DECL|method|setXmlStreamWriterWrapper (String xmlStreamWriterWrapper)
specifier|public
name|void
name|setXmlStreamWriterWrapper
parameter_list|(
name|String
name|xmlStreamWriterWrapper
parameter_list|)
block|{
name|this
operator|.
name|xmlStreamWriterWrapper
operator|=
name|xmlStreamWriterWrapper
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
DECL|method|getNoNamespaceSchemaLocation ()
specifier|public
name|String
name|getNoNamespaceSchemaLocation
parameter_list|()
block|{
return|return
name|noNamespaceSchemaLocation
return|;
block|}
DECL|method|setNoNamespaceSchemaLocation (String noNamespaceSchemaLocation)
specifier|public
name|void
name|setNoNamespaceSchemaLocation
parameter_list|(
name|String
name|noNamespaceSchemaLocation
parameter_list|)
block|{
name|this
operator|.
name|noNamespaceSchemaLocation
operator|=
name|noNamespaceSchemaLocation
expr_stmt|;
block|}
DECL|method|getJaxbProviderProperties ()
specifier|public
name|String
name|getJaxbProviderProperties
parameter_list|()
block|{
return|return
name|jaxbProviderProperties
return|;
block|}
DECL|method|setJaxbProviderProperties (String jaxbProviderProperties)
specifier|public
name|void
name|setJaxbProviderProperties
parameter_list|(
name|String
name|jaxbProviderProperties
parameter_list|)
block|{
name|this
operator|.
name|jaxbProviderProperties
operator|=
name|jaxbProviderProperties
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

