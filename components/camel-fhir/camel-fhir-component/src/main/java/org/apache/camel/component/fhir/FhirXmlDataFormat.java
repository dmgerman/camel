begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.fhir
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|fhir
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|ca
operator|.
name|uhn
operator|.
name|fhir
operator|.
name|parser
operator|.
name|IParser
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
name|Exchange
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
name|annotations
operator|.
name|Dataformat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hl7
operator|.
name|fhir
operator|.
name|instance
operator|.
name|model
operator|.
name|api
operator|.
name|IBaseResource
import|;
end_import

begin_class
annotation|@
name|Dataformat
argument_list|(
literal|"fhirXml"
argument_list|)
DECL|class|FhirXmlDataFormat
specifier|public
class|class
name|FhirXmlDataFormat
extends|extends
name|FhirDataFormat
block|{
annotation|@
name|Override
DECL|method|marshal (Exchange exchange, Object o, OutputStream outputStream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|o
parameter_list|,
name|OutputStream
name|outputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|IBaseResource
name|iBaseResource
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|IBaseResource
operator|)
condition|)
block|{
name|iBaseResource
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|IBaseResource
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|iBaseResource
operator|=
operator|(
name|IBaseResource
operator|)
name|o
expr_stmt|;
block|}
name|IParser
name|parser
init|=
name|getFhirContext
argument_list|()
operator|.
name|newXmlParser
argument_list|()
decl_stmt|;
name|configureParser
argument_list|(
name|parser
argument_list|)
expr_stmt|;
name|parser
operator|.
name|encodeResourceToWriter
argument_list|(
name|iBaseResource
argument_list|,
operator|new
name|OutputStreamWriter
argument_list|(
name|outputStream
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|isContentTypeHeader
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|parser
operator|.
name|getEncoding
argument_list|()
operator|.
name|getResourceContentTypeNonLegacy
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|unmarshal (Exchange exchange, InputStream inputStream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|IParser
name|parser
init|=
name|getFhirContext
argument_list|()
operator|.
name|newXmlParser
argument_list|()
decl_stmt|;
name|configureParser
argument_list|(
name|parser
argument_list|)
expr_stmt|;
return|return
name|parser
operator|.
name|parseResource
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|inputStream
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
literal|"fhirXml"
return|;
block|}
block|}
end_class

end_unit

