begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
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
name|ByteArrayInputStream
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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|GZIPInputStream
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
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|urlfetch
operator|.
name|HTTPHeader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|urlfetch
operator|.
name|HTTPMethod
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|urlfetch
operator|.
name|HTTPRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|appengine
operator|.
name|api
operator|.
name|urlfetch
operator|.
name|HTTPResponse
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
name|component
operator|.
name|gae
operator|.
name|bind
operator|.
name|InboundBinding
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
name|gae
operator|.
name|bind
operator|.
name|OutboundBinding
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
name|UnsafeUriCharactersEncoder
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
name|component
operator|.
name|gae
operator|.
name|http
operator|.
name|GHttpEndpoint
operator|.
name|getEndpointUrl
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
name|component
operator|.
name|http
operator|.
name|helper
operator|.
name|GZIPHelper
operator|.
name|isGzip
import|;
end_import

begin_comment
comment|/**  * Binds the {@link HTTPRequest}/{@link HTTPResponse} pair of the URL fetch  * service to a Camel {@link Exchange}.  */
end_comment

begin_class
DECL|class|GHttpBinding
specifier|public
class|class
name|GHttpBinding
implements|implements
name|OutboundBinding
argument_list|<
name|GHttpEndpoint
argument_list|,
name|HTTPRequest
argument_list|,
name|HTTPResponse
argument_list|>
implements|,
name|InboundBinding
argument_list|<
name|GHttpEndpoint
argument_list|,
name|HttpServletRequest
argument_list|,
name|HttpServletResponse
argument_list|>
block|{
comment|// ----------------------------------------------------------------
comment|//  Outbound binding
comment|// ----------------------------------------------------------------
comment|/**      * Reads data from<code>response</code> and writes it to the out-message of      * the<code>exchange</code>.      *       * @param endpoint      * @param exchange      * @param response      * @return the original<code>exchange</code> instance populated with      *         response data.      * @throws GHttpException      *             if the response code is>= 400 and      *             {@link GHttpEndpoint#isThrowExceptionOnFailure()} returns      *<code>true</code>.      */
DECL|method|readResponse (GHttpEndpoint endpoint, Exchange exchange, HTTPResponse response)
specifier|public
name|Exchange
name|readResponse
parameter_list|(
name|GHttpEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|HTTPResponse
name|response
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|responseCode
init|=
name|response
operator|.
name|getResponseCode
argument_list|()
decl_stmt|;
name|readResponseHeaders
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|readResponseBody
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
if|if
condition|(
name|responseCode
operator|>=
literal|400
operator|&&
name|endpoint
operator|.
name|isThrowExceptionOnFailure
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|GHttpException
argument_list|(
name|responseCode
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|exchange
return|;
block|}
comment|/**      * Reads data from<code>exchange</code> and writes it to a newly created      * {@link HTTPRequest} instance. The<code>request</code> parameter is      * ignored.      *       * @param endpoint      * @param exchange      * @param request      *            ignored.      * @return a newly created {@link HTTPRequest} instance containing data from      *<code>exchange</code>.      */
DECL|method|writeRequest (GHttpEndpoint endpoint, Exchange exchange, HTTPRequest request)
specifier|public
name|HTTPRequest
name|writeRequest
parameter_list|(
name|GHttpEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|HTTPRequest
name|request
parameter_list|)
throws|throws
name|Exception
block|{
name|HTTPRequest
name|answer
init|=
operator|new
name|HTTPRequest
argument_list|(
name|getRequestUrl
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
argument_list|,
name|getRequestMethod
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
name|writeRequestHeaders
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|writeRequestBody
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|// ----------------------------------------------------------------
comment|//  Inbound binding
comment|// ----------------------------------------------------------------
DECL|method|readRequest (GHttpEndpoint endpoint, Exchange exchange, HttpServletRequest request)
specifier|public
name|Exchange
name|readRequest
parameter_list|(
name|GHttpEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|readRequestHeaders
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|request
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
comment|/**      * @throws UnsupportedOperationException.      */
DECL|method|writeResponse (GHttpEndpoint endpoint, Exchange exchange, HttpServletResponse response)
specifier|public
name|HttpServletResponse
name|writeResponse
parameter_list|(
name|GHttpEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"gtask responses not supported"
argument_list|)
throw|;
block|}
comment|// ----------------------------------------------------------------
comment|//  Customization points
comment|// ----------------------------------------------------------------
DECL|method|readResponseHeaders (GHttpEndpoint endpoint, Exchange exchange, HTTPResponse response)
specifier|protected
name|void
name|readResponseHeaders
parameter_list|(
name|GHttpEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|HTTPResponse
name|response
parameter_list|)
block|{
name|HeaderFilterStrategy
name|strategy
init|=
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
decl_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|out
operator|.
name|setHeaders
argument_list|(
name|in
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|response
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|contentType
init|=
name|getResponseHeader
argument_list|(
literal|"Content-Type"
argument_list|,
name|response
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|HTTPHeader
name|header
range|:
name|response
operator|.
name|getHeaders
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|header
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|header
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|strategy
operator|!=
literal|null
operator|&&
operator|!
name|strategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|out
operator|.
name|setHeader
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|readRequestHeaders (GHttpEndpoint endpoint, Exchange exchange, HttpServletRequest request)
specifier|protected
name|void
name|readRequestHeaders
parameter_list|(
name|GHttpEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|)
block|{
comment|// EXPERIMENTAL // TODO: resolve gzip encoding issues
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
literal|"Accept-Encoding"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
literal|"Content-Encoding"
argument_list|)
expr_stmt|;
block|}
DECL|method|writeRequestHeaders (GHttpEndpoint endpoint, Exchange exchange, HTTPRequest request)
specifier|protected
name|void
name|writeRequestHeaders
parameter_list|(
name|GHttpEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|HTTPRequest
name|request
parameter_list|)
block|{
name|HeaderFilterStrategy
name|strategy
init|=
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|headerName
range|:
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|headerValue
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|headerName
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|strategy
operator|!=
literal|null
operator|&&
operator|!
name|strategy
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|headerName
argument_list|,
name|headerValue
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|request
operator|.
name|addHeader
argument_list|(
operator|new
name|HTTPHeader
argument_list|(
name|headerName
argument_list|,
name|headerValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|readResponseBody (GHttpEndpoint endpoint, Exchange exchange, HTTPResponse response)
specifier|protected
name|void
name|readResponseBody
parameter_list|(
name|GHttpEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|HTTPResponse
name|response
parameter_list|)
throws|throws
name|Exception
block|{
name|byte
index|[]
name|content
init|=
name|response
operator|.
name|getContent
argument_list|()
decl_stmt|;
if|if
condition|(
name|content
operator|!=
literal|null
condition|)
block|{
name|InputStream
name|stream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|content
argument_list|)
decl_stmt|;
if|if
condition|(
name|isGzip
argument_list|(
name|getResponseHeader
argument_list|(
literal|"Content-Encoding"
argument_list|,
name|response
argument_list|)
argument_list|)
condition|)
block|{
name|stream
operator|=
operator|new
name|GZIPInputStream
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|writeRequestBody (GHttpEndpoint endpoint, Exchange exchange, HTTPRequest request)
specifier|protected
name|void
name|writeRequestBody
parameter_list|(
name|GHttpEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|HTTPRequest
name|request
parameter_list|)
block|{
name|request
operator|.
name|setPayload
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getRequestUrl (GHttpEndpoint endpoint, Exchange exchange)
specifier|protected
name|URL
name|getRequestUrl
parameter_list|(
name|GHttpEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|uri
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
name|HTTP_URI
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|query
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
name|HTTP_QUERY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
operator|&&
operator|!
name|endpoint
operator|.
name|isBridgeEndpoint
argument_list|()
condition|)
block|{
return|return
name|getEndpointUrl
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|uri
argument_list|)
argument_list|,
name|query
argument_list|)
return|;
block|}
return|return
name|getEndpointUrl
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|query
argument_list|)
return|;
block|}
DECL|method|getRequestMethod (GHttpEndpoint endpoint, Exchange exchange)
specifier|protected
name|HTTPMethod
name|getRequestMethod
parameter_list|(
name|GHttpEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|method
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
return|return
name|HTTPMethod
operator|.
name|valueOf
argument_list|(
name|method
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|HTTPMethod
operator|.
name|POST
return|;
block|}
else|else
block|{
return|return
name|HTTPMethod
operator|.
name|GET
return|;
block|}
block|}
DECL|method|getResponseHeader (String name, HTTPResponse response)
specifier|protected
name|String
name|getResponseHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|HTTPResponse
name|response
parameter_list|)
block|{
for|for
control|(
name|HTTPHeader
name|header
range|:
name|response
operator|.
name|getHeaders
argument_list|()
control|)
block|{
if|if
condition|(
name|header
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|header
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

