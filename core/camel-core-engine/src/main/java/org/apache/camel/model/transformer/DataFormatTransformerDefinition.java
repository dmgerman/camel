begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.transformer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|transformer
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
name|XmlElement
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
name|XmlElements
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
name|XmlType
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
name|ASN1DataFormat
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
name|Any23DataFormat
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
name|AvroDataFormat
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
name|BarcodeDataFormat
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
name|Base64DataFormat
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
name|BeanioDataFormat
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
name|CBORDataFormat
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
name|CryptoDataFormat
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
name|CustomDataFormat
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
name|FhirJsonDataFormat
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
name|FhirXmlDataFormat
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
name|FlatpackDataFormat
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
name|GrokDataFormat
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
name|IcalDataFormat
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
name|JacksonXMLDataFormat
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
name|JsonApiDataFormat
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
name|LZFDataFormat
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
name|MimeMultipartDataFormat
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
name|PGPDataFormat
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
name|TarFileDataFormat
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
name|ThriftDataFormat
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
name|UniVocityCsvDataFormat
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
name|UniVocityFixedWidthDataFormat
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
name|UniVocityTsvDataFormat
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
name|XmlRpcDataFormat
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
name|YAMLDataFormat
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
name|ZipDeflaterDataFormat
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
name|ZipFileDataFormat
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
comment|/**  * Represents a {@link org.apache.camel.impl.transformer.DataFormatTransformer}  * which leverages {@link org.apache.camel.spi.DataFormat} to perform  * transformation. One of the DataFormat 'ref' or DataFormat 'type' needs to be  * specified.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"dataformat,transformation"
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"dataFormatTransformer"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|DataFormatTransformerDefinition
specifier|public
class|class
name|DataFormatTransformerDefinition
extends|extends
name|TransformerDefinition
block|{
annotation|@
name|XmlElements
argument_list|(
block|{
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"any23"
argument_list|,
name|type
operator|=
name|Any23DataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"asn1"
argument_list|,
name|type
operator|=
name|ASN1DataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"avro"
argument_list|,
name|type
operator|=
name|AvroDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"barcode"
argument_list|,
name|type
operator|=
name|BarcodeDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"base64"
argument_list|,
name|type
operator|=
name|Base64DataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"beanio"
argument_list|,
name|type
operator|=
name|BeanioDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"bindy"
argument_list|,
name|type
operator|=
name|BindyDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"cbor"
argument_list|,
name|type
operator|=
name|CBORDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"crypto"
argument_list|,
name|type
operator|=
name|CryptoDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"csv"
argument_list|,
name|type
operator|=
name|CsvDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"custom"
argument_list|,
name|type
operator|=
name|CustomDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"fhirJson"
argument_list|,
name|type
operator|=
name|FhirJsonDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"fhirXml"
argument_list|,
name|type
operator|=
name|FhirXmlDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"flatpack"
argument_list|,
name|type
operator|=
name|FlatpackDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"grok"
argument_list|,
name|type
operator|=
name|GrokDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"gzip"
argument_list|,
name|type
operator|=
name|GzipDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"hl7"
argument_list|,
name|type
operator|=
name|HL7DataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"ical"
argument_list|,
name|type
operator|=
name|IcalDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"jacksonxml"
argument_list|,
name|type
operator|=
name|JacksonXMLDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"jaxb"
argument_list|,
name|type
operator|=
name|JaxbDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"json"
argument_list|,
name|type
operator|=
name|JsonDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"jsonApi"
argument_list|,
name|type
operator|=
name|JsonApiDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"lzf"
argument_list|,
name|type
operator|=
name|LZFDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"mimeMultipart"
argument_list|,
name|type
operator|=
name|MimeMultipartDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"protobuf"
argument_list|,
name|type
operator|=
name|ProtobufDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"rss"
argument_list|,
name|type
operator|=
name|RssDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"secureXML"
argument_list|,
name|type
operator|=
name|XMLSecurityDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"soapjaxb"
argument_list|,
name|type
operator|=
name|SoapJaxbDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"syslog"
argument_list|,
name|type
operator|=
name|SyslogDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"tarfile"
argument_list|,
name|type
operator|=
name|TarFileDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"thrift"
argument_list|,
name|type
operator|=
name|ThriftDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"tidyMarkup"
argument_list|,
name|type
operator|=
name|TidyMarkupDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"univocity-csv"
argument_list|,
name|type
operator|=
name|UniVocityCsvDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"univocity-fixed"
argument_list|,
name|type
operator|=
name|UniVocityFixedWidthDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"univocity-tsv"
argument_list|,
name|type
operator|=
name|UniVocityTsvDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"xmlrpc"
argument_list|,
name|type
operator|=
name|XmlRpcDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"xstream"
argument_list|,
name|type
operator|=
name|XStreamDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"pgp"
argument_list|,
name|type
operator|=
name|PGPDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"yaml"
argument_list|,
name|type
operator|=
name|YAMLDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"zip"
argument_list|,
name|type
operator|=
name|ZipDeflaterDataFormat
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|name
operator|=
literal|"zipfile"
argument_list|,
name|type
operator|=
name|ZipFileDataFormat
operator|.
name|class
argument_list|)
block|}
argument_list|)
DECL|field|dataFormatType
specifier|private
name|DataFormatDefinition
name|dataFormatType
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ref
specifier|private
name|String
name|ref
decl_stmt|;
DECL|method|getRef ()
specifier|public
name|String
name|getRef
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
comment|/**      * Set the reference of the DataFormat.      *      * @param ref reference of the DataFormat      */
DECL|method|setRef (String ref)
specifier|public
name|void
name|setRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
DECL|method|getDataFormatType ()
specifier|public
name|DataFormatDefinition
name|getDataFormatType
parameter_list|()
block|{
return|return
name|dataFormatType
return|;
block|}
comment|/**      * The data format to be used      */
DECL|method|setDataFormatType (DataFormatDefinition dataFormatType)
specifier|public
name|void
name|setDataFormatType
parameter_list|(
name|DataFormatDefinition
name|dataFormatType
parameter_list|)
block|{
name|this
operator|.
name|dataFormatType
operator|=
name|dataFormatType
expr_stmt|;
block|}
block|}
end_class

end_unit

