begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * To configure data formats  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"dataformat,transformation"
argument_list|,
name|title
operator|=
literal|"Data formats"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"dataFormats"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|DataFormatsDefinition
specifier|public
class|class
name|DataFormatsDefinition
block|{
comment|// cannot use @XmlElementRef as it doesn't allow optional properties
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
literal|"boon"
argument_list|,
name|type
operator|=
name|BoonDataFormat
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
literal|"jibx"
argument_list|,
name|type
operator|=
name|JibxDataFormat
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
DECL|field|dataFormats
specifier|private
name|List
argument_list|<
name|DataFormatDefinition
argument_list|>
name|dataFormats
decl_stmt|;
comment|/**      * A list holding the configured data formats      */
DECL|method|setDataFormats (List<DataFormatDefinition> dataFormats)
specifier|public
name|void
name|setDataFormats
parameter_list|(
name|List
argument_list|<
name|DataFormatDefinition
argument_list|>
name|dataFormats
parameter_list|)
block|{
name|this
operator|.
name|dataFormats
operator|=
name|dataFormats
expr_stmt|;
block|}
DECL|method|getDataFormats ()
specifier|public
name|List
argument_list|<
name|DataFormatDefinition
argument_list|>
name|getDataFormats
parameter_list|()
block|{
return|return
name|dataFormats
return|;
block|}
comment|/***      * @return A Map of the contained DataFormatType's indexed by id.      */
DECL|method|asMap ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|DataFormatDefinition
argument_list|>
name|asMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|DataFormatDefinition
argument_list|>
name|dataFormatsAsMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|DataFormatDefinition
name|dataFormatType
range|:
name|getDataFormats
argument_list|()
control|)
block|{
name|dataFormatsAsMap
operator|.
name|put
argument_list|(
name|dataFormatType
operator|.
name|getId
argument_list|()
argument_list|,
name|dataFormatType
argument_list|)
expr_stmt|;
block|}
return|return
name|dataFormatsAsMap
return|;
block|}
block|}
end_class

end_unit

