begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
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
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|AS2Charset
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
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|AS2Header
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
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|AS2MediaType
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
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|entity
operator|.
name|ApplicationEDIConsentEntity
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
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|entity
operator|.
name|ApplicationEDIEntity
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
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|entity
operator|.
name|ApplicationEDIFACTEntity
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
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|entity
operator|.
name|ApplicationEDIX12Entity
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
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|entity
operator|.
name|MimeEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|codec
operator|.
name|binary
operator|.
name|Base64InputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|codec
operator|.
name|binary
operator|.
name|Base64OutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|codec
operator|.
name|net
operator|.
name|QuotedPrintableCodec
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|Header
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpEntityEnclosingRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|ContentType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|util
operator|.
name|Args
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|util
operator|.
name|encoders
operator|.
name|Base64
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|EntityUtils
specifier|public
specifier|final
class|class
name|EntityUtils
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|EntityUtils
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|partNumber
specifier|private
specifier|static
name|AtomicLong
name|partNumber
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|method|EntityUtils ()
specifier|private
name|EntityUtils
parameter_list|()
block|{     }
comment|/**      * Generated a unique value for a Multipart boundary string.      *<p>      * The boundary string is composed of the components:      * "----=_Part_&lt;global_part_number&gt;_&lt;newly_created_object's_hashcode&gt;.&lt;current_time&gt;"      *<p>      * The generated string contains only US-ASCII characters and hence is safe      * for use in RFC822 headers.      *      * @return The generated boundary string.      */
DECL|method|createBoundaryValue ()
specifier|public
specifier|static
name|String
name|createBoundaryValue
parameter_list|()
block|{
comment|// TODO: ensure boundary string is limited to 70 characters or less.
name|StringBuffer
name|s
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|s
operator|.
name|append
argument_list|(
literal|"----=_Part_"
argument_list|)
operator|.
name|append
argument_list|(
name|partNumber
operator|.
name|incrementAndGet
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"_"
argument_list|)
operator|.
name|append
argument_list|(
name|s
operator|.
name|hashCode
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|s
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|validateBoundaryValue (String boundaryValue)
specifier|public
specifier|static
name|boolean
name|validateBoundaryValue
parameter_list|(
name|String
name|boundaryValue
parameter_list|)
block|{
return|return
literal|true
return|;
comment|// TODO: add validation logic.
block|}
DECL|method|appendParameter (String headerString, String parameterName, String parameterValue)
specifier|public
specifier|static
name|String
name|appendParameter
parameter_list|(
name|String
name|headerString
parameter_list|,
name|String
name|parameterName
parameter_list|,
name|String
name|parameterValue
parameter_list|)
block|{
return|return
name|headerString
operator|+
literal|"; "
operator|+
name|parameterName
operator|+
literal|"="
operator|+
name|parameterValue
return|;
block|}
DECL|method|encode (String data, Charset charset, String encoding)
specifier|public
specifier|static
name|String
name|encode
parameter_list|(
name|String
name|data
parameter_list|,
name|Charset
name|charset
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|Exception
block|{
name|byte
index|[]
name|encoded
init|=
name|encode
argument_list|(
name|data
operator|.
name|getBytes
argument_list|(
name|charset
argument_list|)
argument_list|,
name|encoding
argument_list|)
decl_stmt|;
return|return
operator|new
name|String
argument_list|(
name|encoded
argument_list|,
name|charset
argument_list|)
return|;
block|}
DECL|method|encode (byte[] data, String encoding)
specifier|public
specifier|static
name|byte
index|[]
name|encode
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|Exception
block|{
name|Args
operator|.
name|notNull
argument_list|(
name|data
argument_list|,
literal|"Data"
argument_list|)
expr_stmt|;
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
comment|// Identity encoding
return|return
name|data
return|;
block|}
switch|switch
condition|(
name|encoding
operator|.
name|toLowerCase
argument_list|()
condition|)
block|{
case|case
literal|"base64"
case|:
return|return
name|Base64
operator|.
name|encode
argument_list|(
name|data
argument_list|)
return|;
case|case
literal|"quoted-printable"
case|:
comment|// TODO: implement QuotedPrintableOutputStream
return|return
name|QuotedPrintableCodec
operator|.
name|encodeQuotedPrintable
argument_list|(
literal|null
argument_list|,
name|data
argument_list|)
return|;
case|case
literal|"binary"
case|:
case|case
literal|"7bit"
case|:
case|case
literal|"8bit"
case|:
comment|// Identity encoding
return|return
name|data
return|;
default|default:
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Unknown encoding: "
operator|+
name|encoding
argument_list|)
throw|;
block|}
block|}
DECL|method|encode (OutputStream os, String encoding)
specifier|public
specifier|static
name|OutputStream
name|encode
parameter_list|(
name|OutputStream
name|os
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|Exception
block|{
name|Args
operator|.
name|notNull
argument_list|(
name|os
argument_list|,
literal|"Output Stream"
argument_list|)
expr_stmt|;
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
comment|// Identity encoding
return|return
name|os
return|;
block|}
switch|switch
condition|(
name|encoding
operator|.
name|toLowerCase
argument_list|()
condition|)
block|{
case|case
literal|"base64"
case|:
return|return
operator|new
name|Base64OutputStream
argument_list|(
name|os
argument_list|,
literal|true
argument_list|)
return|;
case|case
literal|"quoted-printable"
case|:
comment|// TODO: implement QuotedPrintableOutputStream
return|return
operator|new
name|Base64OutputStream
argument_list|(
name|os
argument_list|,
literal|true
argument_list|)
return|;
case|case
literal|"binary"
case|:
case|case
literal|"7bit"
case|:
case|case
literal|"8bit"
case|:
comment|// Identity encoding
return|return
name|os
return|;
default|default:
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Unknown encoding: "
operator|+
name|encoding
argument_list|)
throw|;
block|}
block|}
DECL|method|decode (String data, Charset charset, String encoding)
specifier|public
specifier|static
name|String
name|decode
parameter_list|(
name|String
name|data
parameter_list|,
name|Charset
name|charset
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|Exception
block|{
name|byte
index|[]
name|decoded
init|=
name|decode
argument_list|(
name|data
operator|.
name|getBytes
argument_list|(
name|charset
argument_list|)
argument_list|,
name|encoding
argument_list|)
decl_stmt|;
return|return
operator|new
name|String
argument_list|(
name|decoded
argument_list|,
name|charset
argument_list|)
return|;
block|}
DECL|method|decode (byte[] data, String encoding)
specifier|public
specifier|static
name|byte
index|[]
name|decode
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|Exception
block|{
name|Args
operator|.
name|notNull
argument_list|(
name|data
argument_list|,
literal|"Data"
argument_list|)
expr_stmt|;
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
comment|// Identity encoding
return|return
name|data
return|;
block|}
switch|switch
condition|(
name|encoding
operator|.
name|toLowerCase
argument_list|()
condition|)
block|{
case|case
literal|"base64"
case|:
return|return
name|Base64
operator|.
name|decode
argument_list|(
name|data
argument_list|)
return|;
case|case
literal|"quoted-printable"
case|:
return|return
name|QuotedPrintableCodec
operator|.
name|decodeQuotedPrintable
argument_list|(
name|data
argument_list|)
return|;
case|case
literal|"binary"
case|:
case|case
literal|"7bit"
case|:
case|case
literal|"8bit"
case|:
comment|// Identity encoding
return|return
name|data
return|;
default|default:
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Unknown encoding: "
operator|+
name|encoding
argument_list|)
throw|;
block|}
block|}
DECL|method|decode (InputStream is, String encoding)
specifier|public
specifier|static
name|InputStream
name|decode
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|Exception
block|{
name|Args
operator|.
name|notNull
argument_list|(
name|is
argument_list|,
literal|"Input Stream"
argument_list|)
expr_stmt|;
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
comment|// Identity encoding
return|return
name|is
return|;
block|}
switch|switch
condition|(
name|encoding
operator|.
name|toLowerCase
argument_list|()
condition|)
block|{
case|case
literal|"base64"
case|:
return|return
operator|new
name|Base64InputStream
argument_list|(
name|is
argument_list|,
literal|false
argument_list|)
return|;
case|case
literal|"quoted-printable"
case|:
comment|// TODO: implement QuotedPrintableInputStream
return|return
operator|new
name|Base64InputStream
argument_list|(
name|is
argument_list|,
literal|false
argument_list|)
return|;
case|case
literal|"binary"
case|:
case|case
literal|"7bit"
case|:
case|case
literal|"8bit"
case|:
comment|// Identity encoding
return|return
name|is
return|;
default|default:
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Unknown encoding: "
operator|+
name|encoding
argument_list|)
throw|;
block|}
block|}
DECL|method|createEDIEntity (String ediMessage, ContentType ediMessageContentType, String contentTransferEncoding, boolean isMainBody)
specifier|public
specifier|static
name|ApplicationEDIEntity
name|createEDIEntity
parameter_list|(
name|String
name|ediMessage
parameter_list|,
name|ContentType
name|ediMessageContentType
parameter_list|,
name|String
name|contentTransferEncoding
parameter_list|,
name|boolean
name|isMainBody
parameter_list|)
throws|throws
name|Exception
block|{
name|Args
operator|.
name|notNull
argument_list|(
name|ediMessage
argument_list|,
literal|"EDI Message"
argument_list|)
expr_stmt|;
name|Args
operator|.
name|notNull
argument_list|(
name|ediMessageContentType
argument_list|,
literal|"EDI Message Content Type"
argument_list|)
expr_stmt|;
name|String
name|charset
init|=
name|ediMessageContentType
operator|.
name|getCharset
argument_list|()
operator|==
literal|null
condition|?
name|AS2Charset
operator|.
name|US_ASCII
else|:
name|ediMessageContentType
operator|.
name|getCharset
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|ediMessageContentType
operator|.
name|getMimeType
argument_list|()
operator|.
name|toLowerCase
argument_list|()
condition|)
block|{
case|case
name|AS2MediaType
operator|.
name|APPLICATION_EDIFACT
case|:
return|return
operator|new
name|ApplicationEDIFACTEntity
argument_list|(
name|ediMessage
argument_list|,
name|charset
argument_list|,
name|contentTransferEncoding
argument_list|,
name|isMainBody
argument_list|)
return|;
case|case
name|AS2MediaType
operator|.
name|APPLICATION_EDI_X12
case|:
return|return
operator|new
name|ApplicationEDIX12Entity
argument_list|(
name|ediMessage
argument_list|,
name|charset
argument_list|,
name|contentTransferEncoding
argument_list|,
name|isMainBody
argument_list|)
return|;
case|case
name|AS2MediaType
operator|.
name|APPLICATION_EDI_CONSENT
case|:
return|return
operator|new
name|ApplicationEDIConsentEntity
argument_list|(
name|ediMessage
argument_list|,
name|charset
argument_list|,
name|contentTransferEncoding
argument_list|,
name|isMainBody
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Invalid EDI entity mime type: "
operator|+
name|ediMessageContentType
operator|.
name|getMimeType
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|getContent (HttpEntity entity)
specifier|public
specifier|static
name|byte
index|[]
name|getContent
parameter_list|(
name|HttpEntity
name|entity
parameter_list|)
block|{
try|try
block|{
specifier|final
name|ByteArrayOutputStream
name|outstream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|entity
operator|.
name|writeTo
argument_list|(
name|outstream
argument_list|)
expr_stmt|;
name|outstream
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|outstream
operator|.
name|toByteArray
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"failed to get content"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
DECL|method|hasEntity (HttpMessage message)
specifier|public
specifier|static
name|boolean
name|hasEntity
parameter_list|(
name|HttpMessage
name|message
parameter_list|)
block|{
name|boolean
name|hasEntity
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|message
operator|instanceof
name|HttpEntityEnclosingRequest
condition|)
block|{
name|hasEntity
operator|=
operator|(
operator|(
name|HttpEntityEnclosingRequest
operator|)
name|message
operator|)
operator|.
name|getEntity
argument_list|()
operator|!=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|HttpResponse
condition|)
block|{
name|hasEntity
operator|=
operator|(
operator|(
name|HttpResponse
operator|)
name|message
operator|)
operator|.
name|getEntity
argument_list|()
operator|!=
literal|null
expr_stmt|;
block|}
return|return
name|hasEntity
return|;
block|}
DECL|method|getMessageEntity (HttpMessage message)
specifier|public
specifier|static
name|HttpEntity
name|getMessageEntity
parameter_list|(
name|HttpMessage
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|instanceof
name|HttpEntityEnclosingRequest
condition|)
block|{
return|return
operator|(
operator|(
name|HttpEntityEnclosingRequest
operator|)
name|message
operator|)
operator|.
name|getEntity
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|HttpResponse
condition|)
block|{
return|return
operator|(
operator|(
name|HttpResponse
operator|)
name|message
operator|)
operator|.
name|getEntity
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|setMessageEntity (HttpMessage message, HttpEntity entity)
specifier|public
specifier|static
name|void
name|setMessageEntity
parameter_list|(
name|HttpMessage
name|message
parameter_list|,
name|HttpEntity
name|entity
parameter_list|)
block|{
if|if
condition|(
name|message
operator|instanceof
name|HttpEntityEnclosingRequest
condition|)
block|{
operator|(
operator|(
name|HttpEntityEnclosingRequest
operator|)
name|message
operator|)
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|HttpResponse
condition|)
block|{
operator|(
operator|(
name|HttpResponse
operator|)
name|message
operator|)
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
name|Header
name|contentTypeHeader
init|=
name|entity
operator|.
name|getContentType
argument_list|()
decl_stmt|;
if|if
condition|(
name|contentTypeHeader
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|contentTypeHeader
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|entity
operator|instanceof
name|MimeEntity
condition|)
block|{
name|Header
name|contentTransferEncodingHeader
init|=
operator|(
operator|(
name|MimeEntity
operator|)
name|entity
operator|)
operator|.
name|getContentTransferEncoding
argument_list|()
decl_stmt|;
if|if
condition|(
name|contentTransferEncodingHeader
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|contentTransferEncodingHeader
argument_list|)
expr_stmt|;
block|}
block|}
name|long
name|contentLength
init|=
name|entity
operator|.
name|getContentLength
argument_list|()
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|AS2Header
operator|.
name|CONTENT_LENGTH
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|contentLength
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|decodeTransferEncodingOfBodyPartContent (String bodyPartContent, ContentType contentType, String bodyPartTransferEncoding)
specifier|public
specifier|static
name|byte
index|[]
name|decodeTransferEncodingOfBodyPartContent
parameter_list|(
name|String
name|bodyPartContent
parameter_list|,
name|ContentType
name|contentType
parameter_list|,
name|String
name|bodyPartTransferEncoding
parameter_list|)
throws|throws
name|Exception
block|{
name|Args
operator|.
name|notNull
argument_list|(
name|bodyPartContent
argument_list|,
literal|"bodyPartContent"
argument_list|)
expr_stmt|;
name|Charset
name|contentCharset
init|=
name|contentType
operator|.
name|getCharset
argument_list|()
decl_stmt|;
if|if
condition|(
name|contentCharset
operator|==
literal|null
condition|)
block|{
name|contentCharset
operator|=
name|StandardCharsets
operator|.
name|US_ASCII
expr_stmt|;
block|}
return|return
name|decode
argument_list|(
name|bodyPartContent
operator|.
name|getBytes
argument_list|(
name|contentCharset
argument_list|)
argument_list|,
name|bodyPartTransferEncoding
argument_list|)
return|;
block|}
DECL|method|printEntity (PrintStream out, HttpEntity entity)
specifier|public
specifier|static
name|void
name|printEntity
parameter_list|(
name|PrintStream
name|out
parameter_list|,
name|HttpEntity
name|entity
parameter_list|)
throws|throws
name|IOException
block|{
name|entity
operator|.
name|writeTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|printEntity (HttpEntity entity)
specifier|public
specifier|static
name|String
name|printEntity
parameter_list|(
name|HttpEntity
name|entity
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|;
name|PrintStream
name|ps
operator|=
operator|new
name|PrintStream
argument_list|(
name|baos
argument_list|,
literal|true
argument_list|,
literal|"utf-8"
argument_list|)
init|)
block|{
name|printEntity
argument_list|(
name|ps
argument_list|,
name|entity
argument_list|)
expr_stmt|;
name|String
name|content
init|=
operator|new
name|String
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
return|return
name|content
return|;
block|}
block|}
block|}
end_class

end_unit

