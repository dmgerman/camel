begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jclouds
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jclouds
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|ByteSource
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|ByteStreams
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|Files
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
name|Converter
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
name|TypeConverter
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
name|WrappedFile
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
name|converter
operator|.
name|stream
operator|.
name|StreamSourceCache
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
name|TypeConverterRegistry
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
name|support
operator|.
name|ExchangeHelper
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
import|import
name|org
operator|.
name|jclouds
operator|.
name|io
operator|.
name|Payload
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|io
operator|.
name|payloads
operator|.
name|ByteSourcePayload
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jclouds
operator|.
name|io
operator|.
name|payloads
operator|.
name|InputStreamPayload
import|;
end_import

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|)
DECL|class|JcloudsPayloadConverter
specifier|public
specifier|final
class|class
name|JcloudsPayloadConverter
block|{
DECL|method|JcloudsPayloadConverter ()
specifier|private
name|JcloudsPayloadConverter
parameter_list|()
block|{
comment|//Utility Class
block|}
annotation|@
name|Converter
DECL|method|toPayload (byte[] bytes)
specifier|public
specifier|static
name|Payload
name|toPayload
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
operator|new
name|ByteSourcePayload
argument_list|(
name|ByteSource
operator|.
name|wrap
argument_list|(
name|bytes
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toPayload (String str, Exchange ex)
specifier|public
specifier|static
name|Payload
name|toPayload
parameter_list|(
name|String
name|str
parameter_list|,
name|Exchange
name|ex
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
return|return
name|toPayload
argument_list|(
name|str
operator|.
name|getBytes
argument_list|(
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|ex
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|toPayload (String str)
specifier|public
specifier|static
name|Payload
name|toPayload
parameter_list|(
name|String
name|str
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
return|return
name|toPayload
argument_list|(
name|str
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toPayload (File file)
specifier|public
specifier|static
name|Payload
name|toPayload
parameter_list|(
name|File
name|file
parameter_list|)
block|{
return|return
operator|new
name|ByteSourcePayload
argument_list|(
name|Files
operator|.
name|asByteSource
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
DECL|method|setContentMetadata (Payload payload, Exchange exchange)
specifier|protected
specifier|static
name|Payload
name|setContentMetadata
parameter_list|(
name|Payload
name|payload
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// Just add an NPE check on the payload
if|if
condition|(
name|exchange
operator|==
literal|null
condition|)
block|{
return|return
name|payload
return|;
block|}
name|String
name|contentType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|contentEncoding
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|contentDisposition
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|CONTENT_DISPOSITION
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|contentLanguage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|CONTENT_LANGUAGE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Date
name|payloadExpires
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JcloudsConstants
operator|.
name|PAYLOAD_EXPIRES
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|contentType
argument_list|)
condition|)
block|{
name|payload
operator|.
name|getContentMetadata
argument_list|()
operator|.
name|setContentType
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|contentEncoding
argument_list|)
condition|)
block|{
name|payload
operator|.
name|getContentMetadata
argument_list|()
operator|.
name|setContentEncoding
argument_list|(
name|contentEncoding
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|contentDisposition
argument_list|)
condition|)
block|{
name|payload
operator|.
name|getContentMetadata
argument_list|()
operator|.
name|setContentDisposition
argument_list|(
name|contentDisposition
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|contentLanguage
argument_list|)
condition|)
block|{
name|payload
operator|.
name|getContentMetadata
argument_list|()
operator|.
name|setContentLanguage
argument_list|(
name|contentLanguage
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|payloadExpires
argument_list|)
condition|)
block|{
name|payload
operator|.
name|getContentMetadata
argument_list|()
operator|.
name|setExpires
argument_list|(
name|payloadExpires
argument_list|)
expr_stmt|;
block|}
return|return
name|payload
return|;
block|}
annotation|@
name|Converter
DECL|method|toPayload (final InputStream is, Exchange exchange)
specifier|public
specifier|static
name|Payload
name|toPayload
parameter_list|(
specifier|final
name|InputStream
name|is
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStreamPayload
name|payload
init|=
operator|new
name|InputStreamPayload
argument_list|(
name|is
argument_list|)
decl_stmt|;
comment|// only set the contentlength if possible
if|if
condition|(
name|is
operator|.
name|markSupported
argument_list|()
condition|)
block|{
name|long
name|contentLength
init|=
name|ByteStreams
operator|.
name|toByteArray
argument_list|(
name|is
argument_list|)
operator|.
name|length
decl_stmt|;
name|is
operator|.
name|reset
argument_list|()
expr_stmt|;
name|payload
operator|.
name|getContentMetadata
argument_list|()
operator|.
name|setContentLength
argument_list|(
name|contentLength
argument_list|)
expr_stmt|;
block|}
return|return
name|payload
return|;
block|}
annotation|@
name|Converter
DECL|method|toPayload (StreamSource source, Exchange exchange)
specifier|public
specifier|static
name|Payload
name|toPayload
parameter_list|(
name|StreamSource
name|source
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toPayload
argument_list|(
operator|new
name|StreamSourceCache
argument_list|(
name|source
argument_list|,
name|exchange
argument_list|)
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toPayload (final StreamSourceCache cache, Exchange exchange)
specifier|public
specifier|static
name|Payload
name|toPayload
parameter_list|(
specifier|final
name|StreamSourceCache
name|cache
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|long
name|contentLength
init|=
name|ByteStreams
operator|.
name|toByteArray
argument_list|(
name|cache
operator|.
name|getInputStream
argument_list|()
argument_list|)
operator|.
name|length
decl_stmt|;
name|cache
operator|.
name|reset
argument_list|()
expr_stmt|;
name|InputStreamPayload
name|payload
init|=
operator|new
name|InputStreamPayload
argument_list|(
name|cache
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|payload
operator|.
name|getContentMetadata
argument_list|()
operator|.
name|setContentLength
argument_list|(
name|contentLength
argument_list|)
expr_stmt|;
name|setContentMetadata
argument_list|(
name|payload
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
return|return
name|payload
return|;
block|}
annotation|@
name|Converter
argument_list|(
name|fallback
operator|=
literal|true
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|convertTo (Class<T> type, Exchange exchange, Object value, TypeConverterRegistry registry)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|,
name|TypeConverterRegistry
name|registry
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|sourceType
init|=
name|value
operator|.
name|getClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|Payload
operator|.
name|class
operator|&&
name|WrappedFile
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|sourceType
argument_list|)
condition|)
block|{
comment|// attempt to convert to JClouds Payload from a file
name|WrappedFile
name|wf
init|=
operator|(
name|WrappedFile
operator|)
name|value
decl_stmt|;
if|if
condition|(
name|wf
operator|.
name|getFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|TypeConverter
name|converter
init|=
name|registry
operator|.
name|lookup
argument_list|(
name|Payload
operator|.
name|class
argument_list|,
name|wf
operator|.
name|getFile
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|converter
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|converter
operator|.
name|tryConvertTo
argument_list|(
name|Payload
operator|.
name|class
argument_list|,
name|wf
operator|.
name|getFile
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

