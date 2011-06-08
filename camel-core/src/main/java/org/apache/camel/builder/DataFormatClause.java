begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|Deflater
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
name|Node
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
name|ProcessorDefinition
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
name|BindyDataFormat
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
name|BindyType
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
name|CastorDataFormat
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
name|CsvDataFormat
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
name|GzipDataFormat
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
name|HL7DataFormat
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
name|model
operator|.
name|dataformat
operator|.
name|JibxDataFormat
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
name|JsonDataFormat
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
name|JsonLibrary
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
name|ProtobufDataFormat
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
name|RefDataFormat
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
name|RssDataFormat
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
name|SerializationDataFormat
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
name|SoapJaxbDataFormat
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
name|StringDataFormat
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
name|SyslogDataFormat
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
name|TidyMarkupDataFormat
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
name|XMLBeansDataFormat
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
name|XMLSecurityDataFormat
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
name|XStreamDataFormat
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
name|ZipDataFormat
import|;
end_import

begin_comment
comment|/**  * An expression for constructing the different possible {@link org.apache.camel.spi.DataFormat}  * options.  *  * @version   */
end_comment

begin_class
DECL|class|DataFormatClause
specifier|public
class|class
name|DataFormatClause
parameter_list|<
name|T
extends|extends
name|ProcessorDefinition
parameter_list|<
name|?
parameter_list|>
parameter_list|>
block|{
DECL|field|processorType
specifier|private
specifier|final
name|T
name|processorType
decl_stmt|;
DECL|field|operation
specifier|private
specifier|final
name|Operation
name|operation
decl_stmt|;
comment|/**      * {@link org.apache.camel.spi.DataFormat} operations.      */
DECL|enum|Operation
specifier|public
enum|enum
name|Operation
block|{
DECL|enumConstant|Marshal
DECL|enumConstant|Unmarshal
name|Marshal
block|,
name|Unmarshal
block|}
DECL|method|DataFormatClause (T processorType, Operation operation)
specifier|public
name|DataFormatClause
parameter_list|(
name|T
name|processorType
parameter_list|,
name|Operation
name|operation
parameter_list|)
block|{
name|this
operator|.
name|processorType
operator|=
name|processorType
expr_stmt|;
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
comment|/**      * Uses the Bindy data format      *      * @param type     the type of bindy data format to use      * @param packages packages to scan for Bindy annotated POJO classes      */
DECL|method|bindy (BindyType type, String... packages)
specifier|public
name|T
name|bindy
parameter_list|(
name|BindyType
name|type
parameter_list|,
name|String
modifier|...
name|packages
parameter_list|)
block|{
name|BindyDataFormat
name|bindy
init|=
operator|new
name|BindyDataFormat
argument_list|()
decl_stmt|;
name|bindy
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|bindy
operator|.
name|setPackages
argument_list|(
name|packages
argument_list|)
expr_stmt|;
return|return
name|dataFormat
argument_list|(
name|bindy
argument_list|)
return|;
block|}
comment|/**      * Uses the CSV data format      */
DECL|method|csv ()
specifier|public
name|T
name|csv
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|CsvDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the Castor data format      */
DECL|method|castor ()
specifier|public
name|T
name|castor
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|CastorDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the Castor data format      *      * @param mappingFile name of mapping file to locate in classpath      */
DECL|method|castor (String mappingFile)
specifier|public
name|T
name|castor
parameter_list|(
name|String
name|mappingFile
parameter_list|)
block|{
name|CastorDataFormat
name|castor
init|=
operator|new
name|CastorDataFormat
argument_list|()
decl_stmt|;
name|castor
operator|.
name|setMappingFile
argument_list|(
name|mappingFile
argument_list|)
expr_stmt|;
return|return
name|dataFormat
argument_list|(
name|castor
argument_list|)
return|;
block|}
comment|/**      * Uses the Castor data format      *      * @param mappingFile name of mapping file to locate in classpath      * @param validation  whether validation is enabled or not      */
DECL|method|castor (String mappingFile, boolean validation)
specifier|public
name|T
name|castor
parameter_list|(
name|String
name|mappingFile
parameter_list|,
name|boolean
name|validation
parameter_list|)
block|{
name|CastorDataFormat
name|castor
init|=
operator|new
name|CastorDataFormat
argument_list|()
decl_stmt|;
name|castor
operator|.
name|setMappingFile
argument_list|(
name|mappingFile
argument_list|)
expr_stmt|;
name|castor
operator|.
name|setValidation
argument_list|(
name|validation
argument_list|)
expr_stmt|;
return|return
name|dataFormat
argument_list|(
name|castor
argument_list|)
return|;
block|}
comment|/**      * Uses the GZIP deflater data format      */
DECL|method|gzip ()
specifier|public
name|T
name|gzip
parameter_list|()
block|{
name|GzipDataFormat
name|gzdf
init|=
operator|new
name|GzipDataFormat
argument_list|()
decl_stmt|;
return|return
name|dataFormat
argument_list|(
name|gzdf
argument_list|)
return|;
block|}
comment|/**      * Uses the HL7 data format      */
DECL|method|hl7 ()
specifier|public
name|T
name|hl7
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|HL7DataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the HL7 data format      */
DECL|method|hl7 (boolean validate)
specifier|public
name|T
name|hl7
parameter_list|(
name|boolean
name|validate
parameter_list|)
block|{
name|HL7DataFormat
name|hl7
init|=
operator|new
name|HL7DataFormat
argument_list|()
decl_stmt|;
name|hl7
operator|.
name|setValidate
argument_list|(
name|validate
argument_list|)
expr_stmt|;
return|return
name|dataFormat
argument_list|(
name|hl7
argument_list|)
return|;
block|}
comment|/**      * Uses the JAXB data format      */
DECL|method|jaxb ()
specifier|public
name|T
name|jaxb
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|JaxbDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the JAXB data format with context path      */
DECL|method|jaxb (String contextPath)
specifier|public
name|T
name|jaxb
parameter_list|(
name|String
name|contextPath
parameter_list|)
block|{
name|JaxbDataFormat
name|dataFormat
init|=
operator|new
name|JaxbDataFormat
argument_list|()
decl_stmt|;
name|dataFormat
operator|.
name|setContextPath
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
return|return
name|dataFormat
argument_list|(
name|dataFormat
argument_list|)
return|;
block|}
comment|/**      * Uses the JAXB data format turning pretty printing on or off      */
DECL|method|jaxb (boolean prettyPrint)
specifier|public
name|T
name|jaxb
parameter_list|(
name|boolean
name|prettyPrint
parameter_list|)
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|JaxbDataFormat
argument_list|(
name|prettyPrint
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the JiBX data format.      */
DECL|method|jibx ()
specifier|public
name|T
name|jibx
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|JibxDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the JiBX data format with unmarshall class.      */
DECL|method|jibx (Class unmarshallClass)
specifier|public
name|T
name|jibx
parameter_list|(
name|Class
name|unmarshallClass
parameter_list|)
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|JibxDataFormat
argument_list|(
name|unmarshallClass
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the JSON data format using the XStream json library      */
DECL|method|json ()
specifier|public
name|T
name|json
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|JsonDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the JSON data format      *      * @param library the json library to use      */
DECL|method|json (JsonLibrary library)
specifier|public
name|T
name|json
parameter_list|(
name|JsonLibrary
name|library
parameter_list|)
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|JsonDataFormat
argument_list|(
name|library
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the JSON data format      *      * @param type          the json type to use      * @param unmarshalType unmarshal type for json jackson type      */
DECL|method|json (JsonLibrary type, Class<?> unmarshalType)
specifier|public
name|T
name|json
parameter_list|(
name|JsonLibrary
name|type
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|)
block|{
name|JsonDataFormat
name|json
init|=
operator|new
name|JsonDataFormat
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|json
operator|.
name|setUnmarshalType
argument_list|(
name|unmarshalType
argument_list|)
expr_stmt|;
return|return
name|dataFormat
argument_list|(
name|json
argument_list|)
return|;
block|}
comment|/**      * Uses the protobuf data format      */
DECL|method|protobuf ()
specifier|public
name|T
name|protobuf
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|ProtobufDataFormat
argument_list|()
argument_list|)
return|;
block|}
DECL|method|protobuf (Object defaultInstance)
specifier|public
name|T
name|protobuf
parameter_list|(
name|Object
name|defaultInstance
parameter_list|)
block|{
name|ProtobufDataFormat
name|dataFormat
init|=
operator|new
name|ProtobufDataFormat
argument_list|()
decl_stmt|;
name|dataFormat
operator|.
name|setDefaultInstance
argument_list|(
name|defaultInstance
argument_list|)
expr_stmt|;
return|return
name|dataFormat
argument_list|(
name|dataFormat
argument_list|)
return|;
block|}
DECL|method|protobuf (String instanceClassName)
specifier|public
name|T
name|protobuf
parameter_list|(
name|String
name|instanceClassName
parameter_list|)
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|ProtobufDataFormat
argument_list|(
name|instanceClassName
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the RSS data format      */
DECL|method|rss ()
specifier|public
name|T
name|rss
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|RssDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the ref data format      */
DECL|method|ref (String ref)
specifier|public
name|T
name|ref
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|RefDataFormat
argument_list|(
name|ref
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the Java Serialization data format      */
DECL|method|serialization ()
specifier|public
name|T
name|serialization
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|SerializationDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the Soap JAXB data format      */
DECL|method|soapjaxb ()
specifier|public
name|T
name|soapjaxb
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|SoapJaxbDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the Soap JAXB data format      */
DECL|method|soapjaxb (String contextPath)
specifier|public
name|T
name|soapjaxb
parameter_list|(
name|String
name|contextPath
parameter_list|)
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|SoapJaxbDataFormat
argument_list|(
name|contextPath
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the Soap JAXB data format      */
DECL|method|soapjaxb (String contextPath, String elementNameStrategyRef)
specifier|public
name|T
name|soapjaxb
parameter_list|(
name|String
name|contextPath
parameter_list|,
name|String
name|elementNameStrategyRef
parameter_list|)
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|SoapJaxbDataFormat
argument_list|(
name|contextPath
argument_list|,
name|elementNameStrategyRef
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the Soap JAXB data format      */
DECL|method|soapjaxb (String contextPath, Object elementNameStrategy)
specifier|public
name|T
name|soapjaxb
parameter_list|(
name|String
name|contextPath
parameter_list|,
name|Object
name|elementNameStrategy
parameter_list|)
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|SoapJaxbDataFormat
argument_list|(
name|contextPath
argument_list|,
name|elementNameStrategy
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the String data format      */
DECL|method|string ()
specifier|public
name|T
name|string
parameter_list|()
block|{
return|return
name|string
argument_list|(
literal|null
argument_list|)
return|;
block|}
comment|/**      * Uses the String data format supporting encoding using given charset      */
DECL|method|string (String charset)
specifier|public
name|T
name|string
parameter_list|(
name|String
name|charset
parameter_list|)
block|{
name|StringDataFormat
name|sdf
init|=
operator|new
name|StringDataFormat
argument_list|()
decl_stmt|;
name|sdf
operator|.
name|setCharset
argument_list|(
name|charset
argument_list|)
expr_stmt|;
return|return
name|dataFormat
argument_list|(
name|sdf
argument_list|)
return|;
block|}
comment|/**      * Uses the Syslog data format      */
DECL|method|syslog ()
specifier|public
name|T
name|syslog
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|SyslogDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Return WellFormed HTML (an XML Document) either      * {@link java.lang.String} or {@link org.w3c.dom.Node}      */
DECL|method|tidyMarkup (Class<?> dataObjectType)
specifier|public
name|T
name|tidyMarkup
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|dataObjectType
parameter_list|)
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|TidyMarkupDataFormat
argument_list|(
name|dataObjectType
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Return TidyMarkup in the default format      * as {@link org.w3c.dom.Node}      */
DECL|method|tidyMarkup ()
specifier|public
name|T
name|tidyMarkup
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|TidyMarkupDataFormat
argument_list|(
name|Node
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the XStream data format      */
DECL|method|xstream ()
specifier|public
name|T
name|xstream
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|XStreamDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the xstream by setting the encoding      */
DECL|method|xstream (String encoding)
specifier|public
name|T
name|xstream
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|XStreamDataFormat
argument_list|(
name|encoding
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the XML Security data format      */
DECL|method|secureXML ()
specifier|public
name|T
name|secureXML
parameter_list|()
block|{
name|XMLSecurityDataFormat
name|xsdf
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
return|return
name|dataFormat
argument_list|(
name|xsdf
argument_list|)
return|;
block|}
comment|/**      * Uses the XML Security data format      */
DECL|method|secureXML (String secureTag, boolean secureTagContents)
specifier|public
name|T
name|secureXML
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|)
block|{
name|XMLSecurityDataFormat
name|xsdf
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|)
decl_stmt|;
return|return
name|dataFormat
argument_list|(
name|xsdf
argument_list|)
return|;
block|}
comment|/**      * Uses the XML Security data format      */
DECL|method|secureXML (String secureTag, boolean secureTagContents, String passPhrase)
specifier|public
name|T
name|secureXML
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|,
name|String
name|passPhrase
parameter_list|)
block|{
name|XMLSecurityDataFormat
name|xsdf
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|,
name|passPhrase
argument_list|)
decl_stmt|;
return|return
name|dataFormat
argument_list|(
name|xsdf
argument_list|)
return|;
block|}
comment|/**      * Uses the XML Security data format      */
DECL|method|secureXML (String secureTag, boolean secureTagContents, String passPhrase, String xmlCipherAlgorithm)
specifier|public
name|T
name|secureXML
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|,
name|String
name|passPhrase
parameter_list|,
name|String
name|xmlCipherAlgorithm
parameter_list|)
block|{
name|XMLSecurityDataFormat
name|xsdf
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|,
name|passPhrase
argument_list|,
name|xmlCipherAlgorithm
argument_list|)
decl_stmt|;
return|return
name|dataFormat
argument_list|(
name|xsdf
argument_list|)
return|;
block|}
comment|/**      * Uses the XML Security data format      */
DECL|method|secureXML (String secureTag, boolean secureTagContents, String recipientKeyAlias, String xmlCipherAlgorithm, String keyCipherAlgorithm)
specifier|public
name|T
name|secureXML
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|,
name|String
name|recipientKeyAlias
parameter_list|,
name|String
name|xmlCipherAlgorithm
parameter_list|,
name|String
name|keyCipherAlgorithm
parameter_list|)
block|{
name|XMLSecurityDataFormat
name|xsdf
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|(
name|secureTag
argument_list|,
name|secureTagContents
argument_list|,
name|recipientKeyAlias
argument_list|,
name|xmlCipherAlgorithm
argument_list|,
name|keyCipherAlgorithm
argument_list|)
decl_stmt|;
return|return
name|dataFormat
argument_list|(
name|xsdf
argument_list|)
return|;
block|}
comment|/**      * Uses the xmlBeans data format      */
DECL|method|xmlBeans ()
specifier|public
name|T
name|xmlBeans
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|XMLBeansDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the ZIP deflater data format      */
DECL|method|zip ()
specifier|public
name|T
name|zip
parameter_list|()
block|{
name|ZipDataFormat
name|zdf
init|=
operator|new
name|ZipDataFormat
argument_list|(
name|Deflater
operator|.
name|DEFAULT_COMPRESSION
argument_list|)
decl_stmt|;
return|return
name|dataFormat
argument_list|(
name|zdf
argument_list|)
return|;
block|}
comment|/**      * Uses the ZIP deflater data format      */
DECL|method|zip (int compressionLevel)
specifier|public
name|T
name|zip
parameter_list|(
name|int
name|compressionLevel
parameter_list|)
block|{
name|ZipDataFormat
name|zdf
init|=
operator|new
name|ZipDataFormat
argument_list|(
name|compressionLevel
argument_list|)
decl_stmt|;
return|return
name|dataFormat
argument_list|(
name|zdf
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|dataFormat (DataFormatDefinition dataFormatType)
specifier|private
name|T
name|dataFormat
parameter_list|(
name|DataFormatDefinition
name|dataFormatType
parameter_list|)
block|{
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|Unmarshal
case|:
return|return
operator|(
name|T
operator|)
name|processorType
operator|.
name|unmarshal
argument_list|(
name|dataFormatType
argument_list|)
return|;
case|case
name|Marshal
case|:
return|return
operator|(
name|T
operator|)
name|processorType
operator|.
name|marshal
argument_list|(
name|dataFormatType
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown DataFormat operation: "
operator|+
name|operation
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

