begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
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
name|ExtendedCamelContext
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
name|Model
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
name|ProcessorDefinitionHelper
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
name|FhirDataformat
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
name|reifier
operator|.
name|AbstractReifier
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|EndpointHelper
operator|.
name|isReferenceParameter
import|;
end_import

begin_class
DECL|class|DataFormatReifier
specifier|public
specifier|abstract
class|class
name|DataFormatReifier
parameter_list|<
name|T
extends|extends
name|DataFormatDefinition
parameter_list|>
extends|extends
name|AbstractReifier
block|{
DECL|field|DATAFORMATS
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|DataFormatDefinition
argument_list|>
argument_list|,
name|Function
argument_list|<
name|DataFormatDefinition
argument_list|,
name|DataFormatReifier
argument_list|<
name|?
extends|extends
name|DataFormatDefinition
argument_list|>
argument_list|>
argument_list|>
name|DATAFORMATS
decl_stmt|;
static|static
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|DataFormatDefinition
argument_list|>
argument_list|,
name|Function
argument_list|<
name|DataFormatDefinition
argument_list|,
name|DataFormatReifier
argument_list|<
name|?
extends|extends
name|DataFormatDefinition
argument_list|>
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Any23DataFormat
operator|.
name|class
argument_list|,
name|Any23DataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|ASN1DataFormat
operator|.
name|class
argument_list|,
name|ASN1DataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|AvroDataFormat
operator|.
name|class
argument_list|,
name|AvroDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|BarcodeDataFormat
operator|.
name|class
argument_list|,
name|BarcodeDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|Base64DataFormat
operator|.
name|class
argument_list|,
name|Base64DataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|BeanioDataFormat
operator|.
name|class
argument_list|,
name|BeanioDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|BindyDataFormat
operator|.
name|class
argument_list|,
name|BindyDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|CBORDataFormat
operator|.
name|class
argument_list|,
name|CBORDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|CryptoDataFormat
operator|.
name|class
argument_list|,
name|CryptoDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|CsvDataFormat
operator|.
name|class
argument_list|,
name|CsvDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|CustomDataFormat
operator|.
name|class
argument_list|,
name|CustomDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|FhirDataformat
operator|.
name|class
argument_list|,
name|FhirDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|FhirJsonDataFormat
operator|.
name|class
argument_list|,
name|FhirJsonDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|FhirXmlDataFormat
operator|.
name|class
argument_list|,
name|FhirXmlDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|FlatpackDataFormat
operator|.
name|class
argument_list|,
name|FlatpackDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|GrokDataFormat
operator|.
name|class
argument_list|,
name|GrokDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|GzipDataFormat
operator|.
name|class
argument_list|,
name|GzipDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|HL7DataFormat
operator|.
name|class
argument_list|,
name|HL7DataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|IcalDataFormat
operator|.
name|class
argument_list|,
name|IcalDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|JacksonXMLDataFormat
operator|.
name|class
argument_list|,
name|JacksonXMLDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|JaxbDataFormat
operator|.
name|class
argument_list|,
name|JaxbDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|JsonApiDataFormat
operator|.
name|class
argument_list|,
name|JsonApiDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|JsonDataFormat
operator|.
name|class
argument_list|,
name|JsonDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|LZFDataFormat
operator|.
name|class
argument_list|,
name|LZFDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|MimeMultipartDataFormat
operator|.
name|class
argument_list|,
name|MimeMultipartDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|PGPDataFormat
operator|.
name|class
argument_list|,
name|PGPDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|ProtobufDataFormat
operator|.
name|class
argument_list|,
name|ProtobufDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|RssDataFormat
operator|.
name|class
argument_list|,
name|RssDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|SoapJaxbDataFormat
operator|.
name|class
argument_list|,
name|SoapJaxbDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|SyslogDataFormat
operator|.
name|class
argument_list|,
name|SyslogDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|TarFileDataFormat
operator|.
name|class
argument_list|,
name|TarFileDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|ThriftDataFormat
operator|.
name|class
argument_list|,
name|ThriftDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|TidyMarkupDataFormat
operator|.
name|class
argument_list|,
name|TidyMarkupDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|UniVocityCsvDataFormat
operator|.
name|class
argument_list|,
name|UniVocityCsvDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|UniVocityFixedWidthDataFormat
operator|.
name|class
argument_list|,
name|UniVocityFixedWidthDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|UniVocityTsvDataFormat
operator|.
name|class
argument_list|,
name|UniVocityTsvDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|XmlRpcDataFormat
operator|.
name|class
argument_list|,
name|XmlRpcDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|XMLSecurityDataFormat
operator|.
name|class
argument_list|,
name|XMLSecurityDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|XStreamDataFormat
operator|.
name|class
argument_list|,
name|XStreamDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|YAMLDataFormat
operator|.
name|class
argument_list|,
name|YAMLDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|ZipDeflaterDataFormat
operator|.
name|class
argument_list|,
name|ZipDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|ZipFileDataFormat
operator|.
name|class
argument_list|,
name|ZipFileDataFormatReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|DATAFORMATS
operator|=
name|map
expr_stmt|;
block|}
DECL|field|definition
specifier|protected
specifier|final
name|T
name|definition
decl_stmt|;
DECL|method|DataFormatReifier (T definition)
specifier|public
name|DataFormatReifier
parameter_list|(
name|T
name|definition
parameter_list|)
block|{
name|this
operator|.
name|definition
operator|=
name|definition
expr_stmt|;
block|}
DECL|method|registerReifier (Class<? extends DataFormatDefinition> dataFormatClass, Function<DataFormatDefinition, DataFormatReifier<? extends DataFormatDefinition>> creator)
specifier|public
specifier|static
name|void
name|registerReifier
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|DataFormatDefinition
argument_list|>
name|dataFormatClass
parameter_list|,
name|Function
argument_list|<
name|DataFormatDefinition
argument_list|,
name|DataFormatReifier
argument_list|<
name|?
extends|extends
name|DataFormatDefinition
argument_list|>
argument_list|>
name|creator
parameter_list|)
block|{
name|DATAFORMATS
operator|.
name|put
argument_list|(
name|dataFormatClass
argument_list|,
name|creator
argument_list|)
expr_stmt|;
block|}
comment|/**      * Factory method to create the data format      *      * @param camelContext the camel context      * @param type the data format type      * @param ref reference to lookup for a data format      * @return the data format or null if not possible to create      */
DECL|method|getDataFormat (CamelContext camelContext, DataFormatDefinition type, String ref)
specifier|public
specifier|static
name|DataFormat
name|getDataFormat
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|DataFormatDefinition
name|type
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|ref
argument_list|,
literal|"ref or type"
argument_list|)
expr_stmt|;
name|DataFormat
name|dataFormat
init|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|ref
argument_list|,
name|DataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataFormat
operator|!=
literal|null
condition|)
block|{
return|return
name|dataFormat
return|;
block|}
comment|// try to let resolver see if it can resolve it, its not always
comment|// possible
name|type
operator|=
name|camelContext
operator|.
name|getExtension
argument_list|(
name|Model
operator|.
name|class
argument_list|)
operator|.
name|resolveDataFormatDefinition
argument_list|(
name|ref
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|dataFormat
operator|=
name|camelContext
operator|.
name|resolveDataFormat
argument_list|(
name|ref
argument_list|)
expr_stmt|;
if|if
condition|(
name|dataFormat
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find data format in registry with ref: "
operator|+
name|ref
argument_list|)
throw|;
block|}
return|return
name|dataFormat
return|;
block|}
block|}
if|if
condition|(
name|type
operator|.
name|getDataFormat
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|type
operator|.
name|getDataFormat
argument_list|()
return|;
block|}
return|return
name|reifier
argument_list|(
name|type
argument_list|)
operator|.
name|createDataFormat
argument_list|(
name|camelContext
argument_list|)
return|;
block|}
DECL|method|reifier (DataFormatDefinition definition)
specifier|public
specifier|static
name|DataFormatReifier
argument_list|<
name|?
extends|extends
name|DataFormatDefinition
argument_list|>
name|reifier
parameter_list|(
name|DataFormatDefinition
name|definition
parameter_list|)
block|{
name|Function
argument_list|<
name|DataFormatDefinition
argument_list|,
name|DataFormatReifier
argument_list|<
name|?
extends|extends
name|DataFormatDefinition
argument_list|>
argument_list|>
name|reifier
init|=
name|DATAFORMATS
operator|.
name|get
argument_list|(
name|definition
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|reifier
operator|!=
literal|null
condition|)
block|{
return|return
name|reifier
operator|.
name|apply
argument_list|(
name|definition
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unsupported definition: "
operator|+
name|definition
argument_list|)
throw|;
block|}
DECL|method|createDataFormat (CamelContext camelContext)
specifier|public
name|DataFormat
name|createDataFormat
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|DataFormat
name|dataFormat
init|=
name|definition
operator|.
name|getDataFormat
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataFormat
operator|==
literal|null
condition|)
block|{
name|Runnable
name|propertyPlaceholdersChangeReverter
init|=
name|ProcessorDefinitionHelper
operator|.
name|createPropertyPlaceholdersChangeReverter
argument_list|()
decl_stmt|;
comment|// resolve properties before we create the data format
try|try
block|{
name|ProcessorDefinitionHelper
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|camelContext
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error resolving property placeholders on data format: "
operator|+
name|definition
argument_list|,
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|dataFormat
operator|=
name|doCreateDataFormat
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
if|if
condition|(
name|dataFormat
operator|!=
literal|null
condition|)
block|{
comment|// is enabled by default so assume true if null
specifier|final
name|boolean
name|contentTypeHeader
init|=
name|definition
operator|.
name|getContentTypeHeader
argument_list|()
operator|==
literal|null
operator|||
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|definition
operator|.
name|getContentTypeHeader
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"contentTypeHeader"
argument_list|,
name|contentTypeHeader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore as this option is optional and not all data
comment|// formats support this
block|}
comment|// configure the rest of the options
name|configureDataFormat
argument_list|(
name|dataFormat
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Data format '"
operator|+
operator|(
name|definition
operator|.
name|getDataFormatName
argument_list|()
operator|!=
literal|null
condition|?
name|definition
operator|.
name|getDataFormatName
argument_list|()
else|:
literal|"<null>"
operator|)
operator|+
literal|"' could not be created. "
operator|+
literal|"Ensure that the data format is valid and the associated Camel component is present on the classpath"
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|propertyPlaceholdersChangeReverter
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|dataFormat
return|;
block|}
comment|/**      * Factory method to create the data format instance      */
DECL|method|doCreateDataFormat (CamelContext camelContext)
specifier|protected
name|DataFormat
name|doCreateDataFormat
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
comment|// must use getDataFormatName() as we need special logic in json
comment|// dataformat
if|if
condition|(
name|definition
operator|.
name|getDataFormatName
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|camelContext
operator|.
name|createDataFormat
argument_list|(
name|definition
operator|.
name|getDataFormatName
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Allows derived classes to customize the data format      */
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
block|{     }
comment|/**      * Sets a named property on the data format instance using introspection      */
DECL|method|setProperty (CamelContext camelContext, Object bean, String name, Object value)
specifier|protected
name|void
name|setProperty
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Object
name|bean
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
try|try
block|{
name|String
name|ref
init|=
name|value
operator|instanceof
name|String
condition|?
name|value
operator|.
name|toString
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|isReferenceParameter
argument_list|(
name|ref
argument_list|)
operator|&&
name|camelContext
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getBeanIntrospection
argument_list|()
operator|.
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|bean
argument_list|,
name|name
argument_list|,
literal|null
argument_list|,
name|ref
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|camelContext
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getBeanIntrospection
argument_list|()
operator|.
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|bean
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to set property: "
operator|+
name|name
operator|+
literal|" on: "
operator|+
name|bean
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

