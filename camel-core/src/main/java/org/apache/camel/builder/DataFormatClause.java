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
name|ArtixDSContentType
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
name|ArtixDSDataFormat
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
comment|/**  * An expression for constructing the different possible {@link org.apache.camel.spi.DataFormat}  * options.  *  * @version $Revision$  */
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
comment|/**      * Uses the      *<a href="http://camel.apache.org/artix-data-services.html">Artix Data Services</a>      * data format for dealing with lots of different message formats such as SWIFT etc.      */
DECL|method|artixDS ()
specifier|public
name|T
name|artixDS
parameter_list|()
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|ArtixDSDataFormat
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Uses the      *<a href="http://camel.apache.org/artix-data-services.html">Artix Data Services</a>      * data format with the specified type of ComplexDataObject      * for marshalling and unmarshalling messages using the dataObject's default Source and Sink.      */
DECL|method|artixDS (Class<?> dataObjectType)
specifier|public
name|T
name|artixDS
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
name|ArtixDSDataFormat
argument_list|(
name|dataObjectType
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the      *<a href="http://camel.apache.org/artix-data-services.html">Artix Data Services</a>      * data format with the specified type of ComplexDataObject      * for marshalling and unmarshalling messages using the dataObject's default Source and Sink.      */
DECL|method|artixDS (Class<?> elementType, ArtixDSContentType contentType)
specifier|public
name|T
name|artixDS
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|elementType
parameter_list|,
name|ArtixDSContentType
name|contentType
parameter_list|)
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|ArtixDSDataFormat
argument_list|(
name|elementType
argument_list|,
name|contentType
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the      *<a href="http://camel.apache.org/artix-data-services.html">Artix Data Services</a>      * data format with the specified content type      * for marshalling and unmarshalling messages      */
DECL|method|artixDS (ArtixDSContentType contentType)
specifier|public
name|T
name|artixDS
parameter_list|(
name|ArtixDSContentType
name|contentType
parameter_list|)
block|{
return|return
name|dataFormat
argument_list|(
operator|new
name|ArtixDSDataFormat
argument_list|(
name|contentType
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Uses the Bindy data format      *      * @param type the type of bindy data format to use      * @param packages packages to scan for Bindy annotated POJO classes      */
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
comment|/**      * Uses the JAXB data format      */
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
comment|/**      * Return WellFormed HTML (an XML Document) either       * {@link java.lang.String} or {@link org.w3c.dom.Node}      */
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
comment|/**      * Return TidyMarkup in the default format       *  as {@link org.w3c.dom.Node}      */
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
comment|/**      * Uses the JSON data format      *      * @param type the json type to use      * @param unmarshalType unmarshal type for json jackson type      */
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

