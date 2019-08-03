begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api.entity
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
name|entity
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|FilterOutputStream
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
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|HeaderIterator
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
name|AbstractHttpEntity
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
name|message
operator|.
name|BasicHeader
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
name|message
operator|.
name|HeaderGroup
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
name|protocol
operator|.
name|HTTP
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

begin_class
DECL|class|MimeEntity
specifier|public
specifier|abstract
class|class
name|MimeEntity
extends|extends
name|AbstractHttpEntity
block|{
comment|/**      * An OuputStream wrapper that doesn't close its underlying output stream.      *<p>      * Instances of this stream are used by entities to attach encoding streams      * to underlying output stream in order to write out their encoded content      * and then flush and close these encoding streams without closing the      * underlying output stream.      */
DECL|class|NoCloseOutputStream
specifier|protected
specifier|static
class|class
name|NoCloseOutputStream
extends|extends
name|FilterOutputStream
block|{
DECL|method|NoCloseOutputStream (OutputStream os)
specifier|public
name|NoCloseOutputStream
parameter_list|(
name|OutputStream
name|os
parameter_list|)
block|{
name|super
argument_list|(
name|os
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
comment|// do nothing
block|}
block|}
DECL|field|UNKNOWN_CONTENT_LENGTH
specifier|protected
specifier|static
specifier|final
name|long
name|UNKNOWN_CONTENT_LENGTH
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|RECALCULATE_CONTENT_LENGTH
specifier|protected
specifier|static
specifier|final
name|long
name|RECALCULATE_CONTENT_LENGTH
init|=
operator|-
literal|2
decl_stmt|;
DECL|field|isMainBody
specifier|protected
name|boolean
name|isMainBody
decl_stmt|;
DECL|field|contentTransferEncoding
specifier|protected
name|Header
name|contentTransferEncoding
decl_stmt|;
DECL|field|contentLength
specifier|protected
name|long
name|contentLength
init|=
name|RECALCULATE_CONTENT_LENGTH
decl_stmt|;
DECL|field|headergroup
specifier|private
specifier|final
name|HeaderGroup
name|headergroup
init|=
operator|new
name|HeaderGroup
argument_list|()
decl_stmt|;
DECL|method|MimeEntity ()
specifier|protected
name|MimeEntity
parameter_list|()
block|{     }
DECL|method|isMainBody ()
specifier|public
name|boolean
name|isMainBody
parameter_list|()
block|{
return|return
name|isMainBody
return|;
block|}
DECL|method|setMainBody (boolean isMainBody)
specifier|public
name|void
name|setMainBody
parameter_list|(
name|boolean
name|isMainBody
parameter_list|)
block|{
name|this
operator|.
name|isMainBody
operator|=
name|isMainBody
expr_stmt|;
block|}
DECL|method|getContentTypeValue ()
specifier|public
name|String
name|getContentTypeValue
parameter_list|()
block|{
name|Header
name|contentTypeHeader
init|=
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
return|return
name|contentTypeHeader
operator|.
name|getValue
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|setContentType (ContentType contentType)
specifier|public
name|void
name|setContentType
parameter_list|(
name|ContentType
name|contentType
parameter_list|)
block|{
name|super
operator|.
name|setContentType
argument_list|(
name|contentType
operator|==
literal|null
condition|?
literal|null
else|:
name|contentType
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setContentType (Header contentType)
specifier|public
name|void
name|setContentType
parameter_list|(
name|Header
name|contentType
parameter_list|)
block|{
name|super
operator|.
name|setContentType
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
name|addHeader
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|removeHeaders
argument_list|(
name|AS2Header
operator|.
name|CONTENT_TYPE
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getContentEncodingValue ()
specifier|public
name|String
name|getContentEncodingValue
parameter_list|()
block|{
name|Header
name|contentEncodingHeader
init|=
name|getContentEncoding
argument_list|()
decl_stmt|;
if|if
condition|(
name|contentEncodingHeader
operator|!=
literal|null
condition|)
block|{
return|return
name|contentEncodingHeader
operator|.
name|getValue
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setContentEncoding (Header contentEncoding)
specifier|public
name|void
name|setContentEncoding
parameter_list|(
name|Header
name|contentEncoding
parameter_list|)
block|{
name|super
operator|.
name|setContentEncoding
argument_list|(
name|contentEncoding
argument_list|)
expr_stmt|;
if|if
condition|(
name|contentEncoding
operator|!=
literal|null
condition|)
block|{
name|addHeader
argument_list|(
name|contentEncoding
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|removeHeaders
argument_list|(
name|HTTP
operator|.
name|CONTENT_ENCODING
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getContentTransferEncodingValue ()
specifier|public
name|String
name|getContentTransferEncodingValue
parameter_list|()
block|{
name|Header
name|contentTransferEncodingHeader
init|=
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
return|return
name|contentTransferEncodingHeader
operator|.
name|getValue
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Obtains the Content-Transfer-Encoding header.      * The default implementation returns the value of the      * {@link #contentEncoding contentEncoding} attribute.      *      * @return  the Content-Transfer-Encoding header, or {@code null}      */
DECL|method|getContentTransferEncoding ()
specifier|public
name|Header
name|getContentTransferEncoding
parameter_list|()
block|{
return|return
name|this
operator|.
name|contentTransferEncoding
return|;
block|}
comment|/**      * Specifies the Content-Transfer-Encoding header.      * The default implementation sets the value of the      * {@link #contentTransferEncoding contentTransferEncoding} attribute.      *      * @param contentTransferEncoding   the new Content-Transfer-Encoding header, or      *                          {@code null} to unset      */
DECL|method|setContentTransferEncoding (final Header contentTransferEncoding)
specifier|public
name|void
name|setContentTransferEncoding
parameter_list|(
specifier|final
name|Header
name|contentTransferEncoding
parameter_list|)
block|{
name|this
operator|.
name|contentTransferEncoding
operator|=
name|contentTransferEncoding
expr_stmt|;
if|if
condition|(
name|contentTransferEncoding
operator|!=
literal|null
condition|)
block|{
name|addHeader
argument_list|(
name|contentTransferEncoding
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|removeHeaders
argument_list|(
name|AS2Header
operator|.
name|CONTENT_TRANSFER_ENCODING
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Specifies the Content-Transfer-Encoding header, as a string.      * The default implementation calls      * {@link #setContentTransferEncoding(Header) setContentTransferEncoding(Header)}.      *      * @param contentTranserEncoding - the new Content-Transfer-Encoding header, or      *                     {@code null} to unset      */
DECL|method|setContentTransferEncoding (final String contentTranserEncoding)
specifier|public
name|void
name|setContentTransferEncoding
parameter_list|(
specifier|final
name|String
name|contentTranserEncoding
parameter_list|)
block|{
name|Header
name|h
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|contentTranserEncoding
operator|!=
literal|null
condition|)
block|{
name|h
operator|=
operator|new
name|BasicHeader
argument_list|(
name|AS2Header
operator|.
name|CONTENT_TRANSFER_ENCODING
argument_list|,
name|contentTranserEncoding
argument_list|)
expr_stmt|;
block|}
name|setContentTransferEncoding
argument_list|(
name|h
argument_list|)
expr_stmt|;
block|}
DECL|method|containsHeader (final String name)
specifier|public
name|boolean
name|containsHeader
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|this
operator|.
name|headergroup
operator|.
name|containsHeader
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|getHeaders (final String name)
specifier|public
name|Header
index|[]
name|getHeaders
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|this
operator|.
name|headergroup
operator|.
name|getHeaders
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|getFirstHeader (final String name)
specifier|public
name|Header
name|getFirstHeader
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|this
operator|.
name|headergroup
operator|.
name|getFirstHeader
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|getLastHeader (final String name)
specifier|public
name|Header
name|getLastHeader
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|this
operator|.
name|headergroup
operator|.
name|getLastHeader
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|getAllHeaders ()
specifier|public
name|Header
index|[]
name|getAllHeaders
parameter_list|()
block|{
return|return
name|this
operator|.
name|headergroup
operator|.
name|getAllHeaders
argument_list|()
return|;
block|}
DECL|method|addHeader (final Header header)
specifier|public
name|void
name|addHeader
parameter_list|(
specifier|final
name|Header
name|header
parameter_list|)
block|{
name|this
operator|.
name|headergroup
operator|.
name|addHeader
argument_list|(
name|header
argument_list|)
expr_stmt|;
block|}
DECL|method|addHeader (final String name, final String value)
specifier|public
name|void
name|addHeader
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|String
name|value
parameter_list|)
block|{
name|Args
operator|.
name|notNull
argument_list|(
name|name
argument_list|,
literal|"Header name"
argument_list|)
expr_stmt|;
name|this
operator|.
name|headergroup
operator|.
name|addHeader
argument_list|(
operator|new
name|BasicHeader
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|setHeader (final Header header)
specifier|public
name|void
name|setHeader
parameter_list|(
specifier|final
name|Header
name|header
parameter_list|)
block|{
name|this
operator|.
name|headergroup
operator|.
name|updateHeader
argument_list|(
name|header
argument_list|)
expr_stmt|;
block|}
DECL|method|setHeader (final String name, final String value)
specifier|public
name|void
name|setHeader
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|String
name|value
parameter_list|)
block|{
name|Args
operator|.
name|notNull
argument_list|(
name|name
argument_list|,
literal|"Header name"
argument_list|)
expr_stmt|;
name|this
operator|.
name|headergroup
operator|.
name|updateHeader
argument_list|(
operator|new
name|BasicHeader
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|setHeaders (final Header[] headers)
specifier|public
name|void
name|setHeaders
parameter_list|(
specifier|final
name|Header
index|[]
name|headers
parameter_list|)
block|{
name|this
operator|.
name|headergroup
operator|.
name|setHeaders
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
DECL|method|removeHeader (final Header header)
specifier|public
name|void
name|removeHeader
parameter_list|(
specifier|final
name|Header
name|header
parameter_list|)
block|{
name|this
operator|.
name|headergroup
operator|.
name|removeHeader
argument_list|(
name|header
argument_list|)
expr_stmt|;
block|}
DECL|method|removeHeaders (final String name)
specifier|public
name|void
name|removeHeaders
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
specifier|final
name|HeaderIterator
name|i
init|=
name|this
operator|.
name|headergroup
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
specifier|final
name|Header
name|header
init|=
name|i
operator|.
name|nextHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equalsIgnoreCase
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|i
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|removeAllHeaders ()
specifier|public
name|void
name|removeAllHeaders
parameter_list|()
block|{
name|this
operator|.
name|headergroup
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|headerIterator ()
specifier|public
name|HeaderIterator
name|headerIterator
parameter_list|()
block|{
return|return
name|this
operator|.
name|headergroup
operator|.
name|iterator
argument_list|()
return|;
block|}
DECL|method|headerIterator (final String name)
specifier|public
name|HeaderIterator
name|headerIterator
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|this
operator|.
name|headergroup
operator|.
name|iterator
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isRepeatable ()
specifier|public
name|boolean
name|isRepeatable
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isStreaming ()
specifier|public
name|boolean
name|isStreaming
parameter_list|()
block|{
return|return
operator|!
name|isRepeatable
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getContentLength ()
specifier|public
name|long
name|getContentLength
parameter_list|()
block|{
if|if
condition|(
name|contentLength
operator|==
name|RECALCULATE_CONTENT_LENGTH
condition|)
block|{
comment|// Calculate content length
specifier|final
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|writeTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|contentLength
operator|=
name|out
operator|.
name|toByteArray
argument_list|()
operator|.
name|length
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|contentLength
operator|=
name|MimeEntity
operator|.
name|UNKNOWN_CONTENT_LENGTH
expr_stmt|;
block|}
block|}
return|return
name|contentLength
return|;
block|}
annotation|@
name|Override
DECL|method|getContent ()
specifier|public
name|InputStream
name|getContent
parameter_list|()
throws|throws
name|IOException
throws|,
name|UnsupportedOperationException
block|{
specifier|final
name|ByteArrayOutputStream
name|outstream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
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
operator|new
name|ByteArrayInputStream
argument_list|(
name|outstream
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getCharset ()
specifier|public
name|String
name|getCharset
parameter_list|()
block|{
if|if
condition|(
name|getContentType
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|AS2Charset
operator|.
name|US_ASCII
return|;
block|}
name|ContentType
name|contentType
init|=
name|ContentType
operator|.
name|parse
argument_list|(
name|getContentType
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|Charset
name|charset
init|=
name|contentType
operator|.
name|getCharset
argument_list|()
decl_stmt|;
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
return|return
name|charset
operator|.
name|name
argument_list|()
return|;
block|}
return|return
name|AS2Charset
operator|.
name|US_ASCII
return|;
block|}
block|}
end_class

end_unit

