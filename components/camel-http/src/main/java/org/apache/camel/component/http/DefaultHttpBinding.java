begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
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
name|PrintWriter
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
name|net
operator|.
name|URLDecoder
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
name|Map
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
name|servlet
operator|.
name|ServletOutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|Endpoint
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
name|InvalidPayloadException
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
name|RuntimeCamelException
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
name|StreamCache
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
name|http
operator|.
name|helper
operator|.
name|CamelFileDataSource
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
name|http
operator|.
name|helper
operator|.
name|GZIPHelper
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
name|http
operator|.
name|helper
operator|.
name|HttpHelper
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
name|HeaderFilterStrategy
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Binding between {@link HttpMessage} and {@link HttpServletResponse}.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultHttpBinding
specifier|public
class|class
name|DefaultHttpBinding
implements|implements
name|HttpBinding
block|{
DECL|field|useReaderForPayload
specifier|private
name|boolean
name|useReaderForPayload
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
operator|new
name|HttpHeaderFilterStrategy
argument_list|()
decl_stmt|;
DECL|field|endpoint
specifier|private
name|HttpEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|Deprecated
DECL|method|DefaultHttpBinding ()
specifier|public
name|DefaultHttpBinding
parameter_list|()
block|{     }
annotation|@
name|Deprecated
DECL|method|DefaultHttpBinding (HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|DefaultHttpBinding
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
DECL|method|DefaultHttpBinding (HttpEndpoint endpoint)
specifier|public
name|DefaultHttpBinding
parameter_list|(
name|HttpEndpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|headerFilterStrategy
operator|=
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
expr_stmt|;
block|}
DECL|method|readRequest (HttpServletRequest request, HttpMessage message)
specifier|public
name|void
name|readRequest
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpMessage
name|message
parameter_list|)
block|{
comment|// lets force a parse of the body and headers
name|message
operator|.
name|getBody
argument_list|()
expr_stmt|;
comment|// populate the headers from the request
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|message
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
comment|//apply the headerFilterStrategy
name|Enumeration
name|names
init|=
name|request
operator|.
name|getHeaderNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|names
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|names
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|request
operator|.
name|getHeader
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// mapping the content-type
if|if
condition|(
name|name
operator|.
name|toLowerCase
argument_list|()
operator|.
name|equals
argument_list|(
literal|"content-type"
argument_list|)
condition|)
block|{
name|name
operator|=
name|Exchange
operator|.
name|CONTENT_TYPE
expr_stmt|;
block|}
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
operator|&&
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|request
operator|.
name|getCharacterEncoding
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|HTTP_CHARACTER_ENCODING
argument_list|,
name|request
operator|.
name|getCharacterEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|request
operator|.
name|getCharacterEncoding
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|populateRequestParameters
argument_list|(
name|request
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot read request parameters due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|Object
name|body
init|=
name|message
operator|.
name|getBody
argument_list|()
decl_stmt|;
comment|// reset the stream cache if the body is the instance of StreamCache
if|if
condition|(
name|body
operator|instanceof
name|StreamCache
condition|)
block|{
operator|(
operator|(
name|StreamCache
operator|)
name|body
operator|)
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
comment|// store the method and query and other info in headers
name|headers
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|request
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
name|request
operator|.
name|getQueryString
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|HTTP_URL
argument_list|,
name|request
operator|.
name|getRequestURL
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|,
name|request
operator|.
name|getRequestURI
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
name|request
operator|.
name|getPathInfo
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|request
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|populateAttachments
argument_list|(
name|request
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|populateRequestParameters (HttpServletRequest request, HttpMessage message)
specifier|protected
name|void
name|populateRequestParameters
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpMessage
name|message
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
comment|//we populate the http request parameters without checking the request method
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|message
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|Enumeration
name|names
init|=
name|request
operator|.
name|getParameterNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|names
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|names
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|request
operator|.
name|getParameter
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
operator|&&
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|request
operator|.
name|getMethod
argument_list|()
operator|.
name|equals
argument_list|(
literal|"POST"
argument_list|)
operator|&&
name|request
operator|.
name|getContentType
argument_list|()
operator|!=
literal|null
operator|&&
name|request
operator|.
name|getContentType
argument_list|()
operator|.
name|startsWith
argument_list|(
name|HttpConstants
operator|.
name|CONTENT_TYPE_WWW_FORM_URLENCODED
argument_list|)
condition|)
block|{
name|String
name|charset
init|=
name|request
operator|.
name|getCharacterEncoding
argument_list|()
decl_stmt|;
if|if
condition|(
name|charset
operator|==
literal|null
condition|)
block|{
name|charset
operator|=
literal|"UTF-8"
expr_stmt|;
block|}
comment|// Push POST form params into the headers to retain compatibility with DefaultHttpBinding
name|String
name|body
init|=
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|param
range|:
name|body
operator|.
name|split
argument_list|(
literal|"&"
argument_list|)
control|)
block|{
name|String
index|[]
name|pair
init|=
name|param
operator|.
name|split
argument_list|(
literal|"="
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|pair
index|[
literal|0
index|]
argument_list|,
name|charset
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|pair
index|[
literal|1
index|]
argument_list|,
name|charset
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
operator|&&
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|populateAttachments (HttpServletRequest request, HttpMessage message)
specifier|protected
name|void
name|populateAttachments
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpMessage
name|message
parameter_list|)
block|{
comment|// check if there is multipart files, if so will put it into DataHandler
name|Enumeration
name|names
init|=
name|request
operator|.
name|getAttributeNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|names
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|names
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|Object
name|object
init|=
name|request
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|File
condition|)
block|{
name|String
name|fileName
init|=
name|request
operator|.
name|getParameter
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|message
operator|.
name|addAttachment
argument_list|(
name|fileName
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|CamelFileDataSource
argument_list|(
operator|(
name|File
operator|)
name|object
argument_list|,
name|fileName
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|writeResponse (Exchange exchange, HttpServletResponse response)
specifier|public
name|void
name|writeResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|exchange
operator|.
name|isFailed
argument_list|()
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|doWriteExceptionResponse
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// it must be a fault, no need to check for the fault flag on the message
name|doWriteFaultResponse
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
name|response
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// just copy the protocol relates header
name|copyProtocolHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
name|doWriteResponse
argument_list|(
name|out
argument_list|,
name|response
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|copyProtocolHeaders (Message request, Message response)
specifier|private
name|void
name|copyProtocolHeaders
parameter_list|(
name|Message
name|request
parameter_list|,
name|Message
name|response
parameter_list|)
block|{
if|if
condition|(
name|request
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|String
name|contentEncoding
init|=
name|request
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
name|response
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|,
name|contentEncoding
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|checkChunked
argument_list|(
name|response
argument_list|,
name|response
operator|.
name|getExchange
argument_list|()
argument_list|)
condition|)
block|{
name|response
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|TRANSFER_ENCODING
argument_list|,
literal|"chunked"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doWriteExceptionResponse (Throwable exception, HttpServletResponse response)
specifier|public
name|void
name|doWriteExceptionResponse
parameter_list|(
name|Throwable
name|exception
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
block|{
comment|// 500 for internal server error
name|response
operator|.
name|setStatus
argument_list|(
literal|500
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
operator|&&
name|endpoint
operator|.
name|isTransferException
argument_list|()
condition|)
block|{
comment|// transfer the exception as a serialized java object
name|HttpHelper
operator|.
name|writeObjectToServletResponse
argument_list|(
name|response
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// write stacktrace as plain text
name|response
operator|.
name|setContentType
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|PrintWriter
name|pw
init|=
name|response
operator|.
name|getWriter
argument_list|()
decl_stmt|;
name|exception
operator|.
name|printStackTrace
argument_list|(
name|pw
argument_list|)
expr_stmt|;
name|pw
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doWriteFaultResponse (Message message, HttpServletResponse response, Exchange exchange)
specifier|public
name|void
name|doWriteFaultResponse
parameter_list|(
name|Message
name|message
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|doWriteResponse
argument_list|(
name|message
argument_list|,
name|response
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|doWriteResponse (Message message, HttpServletResponse response, Exchange exchange)
specifier|public
name|void
name|doWriteResponse
parameter_list|(
name|Message
name|message
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
comment|// set the status code in the response. Default is 200.
if|if
condition|(
name|message
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|int
name|code
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|response
operator|.
name|setStatus
argument_list|(
name|code
argument_list|)
expr_stmt|;
block|}
comment|// set the content type in the response.
name|String
name|contentType
init|=
name|MessageHelper
operator|.
name|getContentType
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|MessageHelper
operator|.
name|getContentType
argument_list|(
name|message
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|setContentType
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
block|}
comment|// append headers
comment|// must use entrySet to ensure case of keys is preserved
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
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|headerFilterStrategy
operator|!=
literal|null
operator|&&
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|response
operator|.
name|setHeader
argument_list|(
name|key
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// write the body.
if|if
condition|(
name|message
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|GZIPHelper
operator|.
name|isGzip
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|doWriteGZIPResponse
argument_list|(
name|message
argument_list|,
name|response
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doWriteDirectResponse
argument_list|(
name|message
argument_list|,
name|response
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|doWriteDirectResponse (Message message, HttpServletResponse response, Exchange exchange)
specifier|protected
name|void
name|doWriteDirectResponse
parameter_list|(
name|Message
name|message
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|checkChunked
argument_list|(
name|message
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|is
operator|=
name|message
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|ServletOutputStream
name|os
init|=
name|response
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
comment|// copy directly from input stream to output stream
name|IOHelper
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|os
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// not convertable as a stream so try as a String
name|String
name|data
init|=
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|!=
literal|null
condition|)
block|{
comment|// set content length before we write data
name|response
operator|.
name|setContentLength
argument_list|(
name|data
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|print
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|checkChunked (Message message, Exchange exchange)
specifier|protected
name|boolean
name|checkChunked
parameter_list|(
name|Message
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|boolean
name|answer
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|message
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_CHUNKED
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|// check the endpoint option
name|Endpoint
name|endpoint
init|=
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|instanceof
name|HttpEndpoint
condition|)
block|{
name|answer
operator|=
operator|(
operator|(
name|HttpEndpoint
operator|)
name|endpoint
operator|)
operator|.
name|isChunked
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|answer
operator|=
name|message
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_CHUNKED
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|doWriteGZIPResponse (Message message, HttpServletResponse response, Exchange exchange)
specifier|protected
name|void
name|doWriteGZIPResponse
parameter_list|(
name|Message
name|message
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|bytes
decl_stmt|;
try|try
block|{
name|bytes
operator|=
name|message
operator|.
name|getMandatoryBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidPayloadException
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|byte
index|[]
name|data
init|=
name|GZIPHelper
operator|.
name|compressGZIP
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|ServletOutputStream
name|os
init|=
name|response
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|response
operator|.
name|setContentLength
argument_list|(
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|os
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|parseBody (HttpMessage httpMessage)
specifier|public
name|Object
name|parseBody
parameter_list|(
name|HttpMessage
name|httpMessage
parameter_list|)
throws|throws
name|IOException
block|{
comment|// lets assume the body is a reader
name|HttpServletRequest
name|request
init|=
name|httpMessage
operator|.
name|getRequest
argument_list|()
decl_stmt|;
comment|// Need to handle the GET Method which has no inputStream
if|if
condition|(
literal|"GET"
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getMethod
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|isUseReaderForPayload
argument_list|()
condition|)
block|{
comment|// use reader to read the response body
return|return
name|request
operator|.
name|getReader
argument_list|()
return|;
block|}
else|else
block|{
comment|// reade the response body from servlet request
return|return
name|HttpHelper
operator|.
name|readResponseBodyFromServletRequest
argument_list|(
name|request
argument_list|,
name|httpMessage
operator|.
name|getExchange
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|isUseReaderForPayload ()
specifier|public
name|boolean
name|isUseReaderForPayload
parameter_list|()
block|{
return|return
name|useReaderForPayload
return|;
block|}
DECL|method|setUseReaderForPayload (boolean useReaderForPayload)
specifier|public
name|void
name|setUseReaderForPayload
parameter_list|(
name|boolean
name|useReaderForPayload
parameter_list|)
block|{
name|this
operator|.
name|useReaderForPayload
operator|=
name|useReaderForPayload
expr_stmt|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
block|}
end_class

end_unit

