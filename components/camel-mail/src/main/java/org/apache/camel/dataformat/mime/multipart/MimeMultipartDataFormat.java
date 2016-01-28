begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.mime.multipart
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|mime
operator|.
name|multipart
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|BodyPart
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Header
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|MessagingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Part
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|ContentType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|InternetHeaders
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeBodyPart
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeMultipart
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeUtility
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|util
operator|.
name|ByteArrayDataSource
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
name|Message
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
name|NoTypeConversionAvailableException
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
name|IOHelper
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
name|MessageHelper
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
DECL|class|MimeMultipartDataFormat
specifier|public
class|class
name|MimeMultipartDataFormat
implements|implements
name|DataFormat
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
name|MimeMultipartDataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|MIME_VERSION
specifier|private
specifier|static
specifier|final
name|String
name|MIME_VERSION
init|=
literal|"MIME-Version"
decl_stmt|;
DECL|field|CONTENT_TYPE
specifier|private
specifier|static
specifier|final
name|String
name|CONTENT_TYPE
init|=
literal|"Content-Type"
decl_stmt|;
DECL|field|CONTENT_TRANSFER_ENCODING
specifier|private
specifier|static
specifier|final
name|String
name|CONTENT_TRANSFER_ENCODING
init|=
literal|"Content-Transfer-Encoding"
decl_stmt|;
DECL|field|DEFAULT_CONTENT_TYPE
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_CONTENT_TYPE
init|=
literal|"application/octet-stream"
decl_stmt|;
DECL|field|STANDARD_HEADERS
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|STANDARD_HEADERS
init|=
block|{
literal|"Message-ID"
block|,
literal|"MIME-Version"
block|,
literal|"Content-Type"
block|}
decl_stmt|;
DECL|field|multipartSubType
specifier|private
name|String
name|multipartSubType
init|=
literal|"mixed"
decl_stmt|;
DECL|field|multipartWithoutAttachment
specifier|private
name|boolean
name|multipartWithoutAttachment
decl_stmt|;
DECL|field|headersInline
specifier|private
name|boolean
name|headersInline
decl_stmt|;
DECL|field|includeHeaders
specifier|private
name|Pattern
name|includeHeaders
decl_stmt|;
DECL|field|binaryContent
specifier|private
name|boolean
name|binaryContent
decl_stmt|;
DECL|method|setBinaryContent (boolean binaryContent)
specifier|public
name|void
name|setBinaryContent
parameter_list|(
name|boolean
name|binaryContent
parameter_list|)
block|{
name|this
operator|.
name|binaryContent
operator|=
name|binaryContent
expr_stmt|;
block|}
DECL|method|setHeadersInline (boolean headersInline)
specifier|public
name|void
name|setHeadersInline
parameter_list|(
name|boolean
name|headersInline
parameter_list|)
block|{
name|this
operator|.
name|headersInline
operator|=
name|headersInline
expr_stmt|;
block|}
DECL|method|setIncludeHeaders (String includeHeaders)
specifier|public
name|void
name|setIncludeHeaders
parameter_list|(
name|String
name|includeHeaders
parameter_list|)
block|{
name|this
operator|.
name|includeHeaders
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|includeHeaders
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
expr_stmt|;
block|}
DECL|method|setMultipartWithoutAttachment (boolean multipartWithoutAttachment)
specifier|public
name|void
name|setMultipartWithoutAttachment
parameter_list|(
name|boolean
name|multipartWithoutAttachment
parameter_list|)
block|{
name|this
operator|.
name|multipartWithoutAttachment
operator|=
name|multipartWithoutAttachment
expr_stmt|;
block|}
DECL|method|setMultipartSubType (String multipartSubType)
specifier|public
name|void
name|setMultipartSubType
parameter_list|(
name|String
name|multipartSubType
parameter_list|)
block|{
name|this
operator|.
name|multipartSubType
operator|=
name|multipartSubType
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|marshal (Exchange exchange, Object graph, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
throws|,
name|MessagingException
throws|,
name|IOException
block|{
if|if
condition|(
name|multipartWithoutAttachment
operator|||
name|headersInline
operator|||
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|hasAttachments
argument_list|()
condition|)
block|{
name|ContentType
name|contentType
init|=
name|getContentType
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// remove the Content-Type header. This will be wrong afterwards...
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bodyContent
init|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|graph
argument_list|)
decl_stmt|;
name|Session
name|session
init|=
name|Session
operator|.
name|getInstance
argument_list|(
name|System
operator|.
name|getProperties
argument_list|()
argument_list|)
decl_stmt|;
name|MimeMessage
name|mm
init|=
operator|new
name|MimeMessage
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|MimeMultipart
name|mp
init|=
operator|new
name|MimeMultipart
argument_list|(
name|multipartSubType
argument_list|)
decl_stmt|;
name|BodyPart
name|part
init|=
operator|new
name|MimeBodyPart
argument_list|()
decl_stmt|;
name|writeBodyPart
argument_list|(
name|bodyContent
argument_list|,
name|part
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
name|mp
operator|.
name|addBodyPart
argument_list|(
name|part
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|entry
range|:
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getAttachments
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|attachmentFilename
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|DataHandler
name|handler
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|part
operator|=
operator|new
name|MimeBodyPart
argument_list|()
expr_stmt|;
name|part
operator|.
name|setDataHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|part
operator|.
name|setFileName
argument_list|(
name|MimeUtility
operator|.
name|encodeText
argument_list|(
name|attachmentFilename
argument_list|,
literal|"UTF-8"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|ct
init|=
name|handler
operator|.
name|getContentType
argument_list|()
decl_stmt|;
name|contentType
operator|=
operator|new
name|ContentType
argument_list|(
name|ct
argument_list|)
expr_stmt|;
name|part
operator|.
name|setHeader
argument_list|(
name|CONTENT_TYPE
argument_list|,
name|ct
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|contentType
operator|.
name|match
argument_list|(
literal|"text/*"
argument_list|)
operator|&&
name|binaryContent
condition|)
block|{
name|part
operator|.
name|setHeader
argument_list|(
name|CONTENT_TRANSFER_ENCODING
argument_list|,
literal|"binary"
argument_list|)
expr_stmt|;
block|}
name|mp
operator|.
name|addBodyPart
argument_list|(
name|part
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|removeAttachment
argument_list|(
name|attachmentFilename
argument_list|)
expr_stmt|;
block|}
name|mm
operator|.
name|setContent
argument_list|(
name|mp
argument_list|)
expr_stmt|;
comment|// copy headers if required and if the content can be converted into
comment|// a String
if|if
condition|(
name|headersInline
operator|&&
name|includeHeaders
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|includeHeaders
operator|.
name|matcher
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
name|String
name|headerStr
init|=
name|ExchangeHelper
operator|.
name|convertToType
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerStr
operator|!=
literal|null
condition|)
block|{
name|mm
operator|.
name|setHeader
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|headerStr
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|mm
operator|.
name|saveChanges
argument_list|()
expr_stmt|;
name|Enumeration
argument_list|<
name|?
argument_list|>
name|hl
init|=
name|mm
operator|.
name|getAllHeaders
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|headers
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|headersInline
condition|)
block|{
while|while
condition|(
name|hl
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|Object
name|ho
init|=
name|hl
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|ho
operator|instanceof
name|Header
condition|)
block|{
name|Header
name|h
init|=
operator|(
name|Header
operator|)
name|ho
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|h
operator|.
name|getName
argument_list|()
argument_list|,
name|h
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|add
argument_list|(
name|h
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|mm
operator|.
name|saveChanges
argument_list|()
expr_stmt|;
block|}
name|mm
operator|.
name|writeTo
argument_list|(
name|stream
argument_list|,
name|headers
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// keep the original data
name|InputStream
name|is
init|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|InputStream
operator|.
name|class
argument_list|,
name|graph
argument_list|)
decl_stmt|;
name|IOHelper
operator|.
name|copyAndCloseInput
argument_list|(
name|is
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getContentType (Exchange exchange)
specifier|private
name|ContentType
name|getContentType
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|ParseException
block|{
name|String
name|contentTypeStr
init|=
name|ExchangeHelper
operator|.
name|getContentType
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentTypeStr
operator|==
literal|null
condition|)
block|{
name|contentTypeStr
operator|=
name|DEFAULT_CONTENT_TYPE
expr_stmt|;
block|}
name|ContentType
name|contentType
init|=
operator|new
name|ContentType
argument_list|(
name|contentTypeStr
argument_list|)
decl_stmt|;
name|String
name|contentEncoding
init|=
name|ExchangeHelper
operator|.
name|getContentEncoding
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// add a charset parameter for text subtypes
if|if
condition|(
name|contentEncoding
operator|!=
literal|null
operator|&&
name|contentType
operator|.
name|match
argument_list|(
literal|"text/*"
argument_list|)
condition|)
block|{
name|contentType
operator|.
name|setParameter
argument_list|(
literal|"charset"
argument_list|,
name|MimeUtility
operator|.
name|mimeCharset
argument_list|(
name|contentEncoding
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|contentType
return|;
block|}
DECL|method|writeBodyPart (byte[] bodyContent, Part part, ContentType contentType)
specifier|private
name|void
name|writeBodyPart
parameter_list|(
name|byte
index|[]
name|bodyContent
parameter_list|,
name|Part
name|part
parameter_list|,
name|ContentType
name|contentType
parameter_list|)
throws|throws
name|MessagingException
block|{
name|DataSource
name|ds
init|=
operator|new
name|ByteArrayDataSource
argument_list|(
name|bodyContent
argument_list|,
name|contentType
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|part
operator|.
name|setDataHandler
argument_list|(
operator|new
name|DataHandler
argument_list|(
name|ds
argument_list|)
argument_list|)
expr_stmt|;
name|part
operator|.
name|setHeader
argument_list|(
name|CONTENT_TYPE
argument_list|,
name|contentType
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|contentType
operator|.
name|match
argument_list|(
literal|"text/*"
argument_list|)
condition|)
block|{
name|part
operator|.
name|setHeader
argument_list|(
name|CONTENT_TRANSFER_ENCODING
argument_list|,
literal|"8bit"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|binaryContent
condition|)
block|{
name|part
operator|.
name|setHeader
argument_list|(
name|CONTENT_TRANSFER_ENCODING
argument_list|,
literal|"binary"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|part
operator|.
name|setHeader
argument_list|(
name|CONTENT_TRANSFER_ENCODING
argument_list|,
literal|"base64"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|unmarshal (Exchange exchange, InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|IOException
throws|,
name|MessagingException
block|{
name|MimeBodyPart
name|mimeMessage
decl_stmt|;
name|String
name|contentType
decl_stmt|;
name|Message
name|camelMessage
decl_stmt|;
name|Object
name|content
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|headersInline
condition|)
block|{
name|mimeMessage
operator|=
operator|new
name|MimeBodyPart
argument_list|(
name|stream
argument_list|)
expr_stmt|;
name|camelMessage
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
expr_stmt|;
name|MessageHelper
operator|.
name|copyHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|camelMessage
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|contentType
operator|=
name|mimeMessage
operator|.
name|getHeader
argument_list|(
name|CONTENT_TYPE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// write the MIME headers not generated by javamail as Camel headers
name|Enumeration
argument_list|<
name|?
argument_list|>
name|headersEnum
init|=
name|mimeMessage
operator|.
name|getNonMatchingHeaders
argument_list|(
name|STANDARD_HEADERS
argument_list|)
decl_stmt|;
while|while
condition|(
name|headersEnum
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|Object
name|ho
init|=
name|headersEnum
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|ho
operator|instanceof
name|Header
condition|)
block|{
name|Header
name|header
init|=
operator|(
name|Header
operator|)
name|ho
decl_stmt|;
name|camelMessage
operator|.
name|setHeader
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|,
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// check if this a multipart at all. Otherwise do nothing
name|contentType
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|contentType
operator|==
literal|null
condition|)
block|{
return|return
name|stream
return|;
block|}
try|try
block|{
name|ContentType
name|ct
init|=
operator|new
name|ContentType
argument_list|(
name|contentType
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ct
operator|.
name|match
argument_list|(
literal|"multipart/*"
argument_list|)
condition|)
block|{
return|return
name|stream
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Invalid Content-Type "
operator|+
name|contentType
operator|+
literal|" ignored"
argument_list|)
expr_stmt|;
return|return
name|stream
return|;
block|}
name|camelMessage
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
expr_stmt|;
name|MessageHelper
operator|.
name|copyHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|camelMessage
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOHelper
operator|.
name|copyAndCloseInput
argument_list|(
name|stream
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|InternetHeaders
name|headers
init|=
operator|new
name|InternetHeaders
argument_list|()
decl_stmt|;
name|extractHeader
argument_list|(
name|CONTENT_TYPE
argument_list|,
name|camelMessage
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|extractHeader
argument_list|(
name|MIME_VERSION
argument_list|,
name|camelMessage
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|mimeMessage
operator|=
operator|new
name|MimeBodyPart
argument_list|(
name|headers
argument_list|,
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|DataHandler
name|dh
decl_stmt|;
try|try
block|{
name|dh
operator|=
name|mimeMessage
operator|.
name|getDataHandler
argument_list|()
expr_stmt|;
if|if
condition|(
name|dh
operator|!=
literal|null
condition|)
block|{
name|content
operator|=
name|dh
operator|.
name|getContent
argument_list|()
expr_stmt|;
name|contentType
operator|=
name|dh
operator|.
name|getContentType
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MessagingException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"cannot parse message, no unmarshalling done"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|content
operator|instanceof
name|MimeMultipart
condition|)
block|{
name|MimeMultipart
name|mp
init|=
operator|(
name|MimeMultipart
operator|)
name|content
decl_stmt|;
name|content
operator|=
name|mp
operator|.
name|getBodyPart
argument_list|(
literal|0
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|mp
operator|.
name|getCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|BodyPart
name|bp
init|=
name|mp
operator|.
name|getBodyPart
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|camelMessage
operator|.
name|addAttachment
argument_list|(
name|MimeUtility
operator|.
name|decodeText
argument_list|(
name|bp
operator|.
name|getFileName
argument_list|()
argument_list|)
argument_list|,
name|bp
operator|.
name|getDataHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|content
operator|instanceof
name|BodyPart
condition|)
block|{
name|BodyPart
name|bp
init|=
operator|(
name|BodyPart
operator|)
name|content
decl_stmt|;
name|camelMessage
operator|.
name|setBody
argument_list|(
name|bp
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|contentType
operator|=
name|bp
operator|.
name|getContentType
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// Last fallback: I don't see how this can happen, but we do this
comment|// just to be safe
name|camelMessage
operator|.
name|setBody
argument_list|(
name|content
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|contentType
operator|!=
literal|null
operator|&&
operator|!
name|DEFAULT_CONTENT_TYPE
operator|.
name|equals
argument_list|(
name|contentType
argument_list|)
condition|)
block|{
name|camelMessage
operator|.
name|setHeader
argument_list|(
name|CONTENT_TYPE
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
name|ContentType
name|ct
init|=
operator|new
name|ContentType
argument_list|(
name|contentType
argument_list|)
decl_stmt|;
name|String
name|charset
init|=
name|ct
operator|.
name|getParameter
argument_list|(
literal|"charset"
argument_list|)
decl_stmt|;
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
name|camelMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|,
name|MimeUtility
operator|.
name|javaCharset
argument_list|(
name|charset
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|camelMessage
return|;
block|}
DECL|method|extractHeader (String headerMame, Message camelMessage, InternetHeaders headers)
specifier|private
name|void
name|extractHeader
parameter_list|(
name|String
name|headerMame
parameter_list|,
name|Message
name|camelMessage
parameter_list|,
name|InternetHeaders
name|headers
parameter_list|)
block|{
name|String
name|h
init|=
name|camelMessage
operator|.
name|getHeader
argument_list|(
name|headerMame
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|h
operator|!=
literal|null
condition|)
block|{
name|headers
operator|.
name|addHeader
argument_list|(
name|headerMame
argument_list|,
name|h
argument_list|)
expr_stmt|;
name|camelMessage
operator|.
name|removeHeader
argument_list|(
name|headerMame
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

