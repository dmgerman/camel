begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
package|;
end_package

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
name|net
operator|.
name|CookieStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpCookie
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncCallback
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
name|CamelExchangeException
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
name|impl
operator|.
name|DefaultAsyncProducer
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
name|URISupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Uniform
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Cookie
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|CookieSetting
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|util
operator|.
name|Series
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

begin_comment
comment|/**  * A Camel producer that acts as a client to Restlet server.  *  * @version   */
end_comment

begin_class
DECL|class|RestletProducer
specifier|public
class|class
name|RestletProducer
extends|extends
name|DefaultAsyncProducer
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
name|RestletProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\{([\\w\\.]*)\\}"
argument_list|)
decl_stmt|;
DECL|field|client
specifier|private
name|Client
name|client
decl_stmt|;
DECL|field|throwException
specifier|private
name|boolean
name|throwException
decl_stmt|;
DECL|method|RestletProducer (RestletEndpoint endpoint)
specifier|public
name|RestletProducer
parameter_list|(
name|RestletEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|throwException
operator|=
name|endpoint
operator|.
name|isThrowExceptionOnFailure
argument_list|()
expr_stmt|;
name|client
operator|=
operator|new
name|Client
argument_list|(
name|endpoint
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|setContext
argument_list|(
operator|new
name|Context
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|getContext
argument_list|()
operator|.
name|getParameters
argument_list|()
operator|.
name|add
argument_list|(
literal|"socketTimeout"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|endpoint
operator|.
name|getSocketTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|.
name|getContext
argument_list|()
operator|.
name|getParameters
argument_list|()
operator|.
name|add
argument_list|(
literal|"socketConnectTimeoutMs"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|endpoint
operator|.
name|getSocketTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|RestletComponent
name|component
init|=
operator|(
name|RestletComponent
operator|)
name|endpoint
operator|.
name|getComponent
argument_list|()
decl_stmt|;
if|if
condition|(
name|component
operator|.
name|getMaxConnectionsPerHost
argument_list|()
operator|!=
literal|null
operator|&&
name|component
operator|.
name|getMaxConnectionsPerHost
argument_list|()
operator|>
literal|0
condition|)
block|{
name|client
operator|.
name|getContext
argument_list|()
operator|.
name|getParameters
argument_list|()
operator|.
name|add
argument_list|(
literal|"maxConnectionsPerHost"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|component
operator|.
name|getMaxConnectionsPerHost
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|component
operator|.
name|getMaxTotalConnections
argument_list|()
operator|!=
literal|null
operator|&&
name|component
operator|.
name|getMaxTotalConnections
argument_list|()
operator|>
literal|0
condition|)
block|{
name|client
operator|.
name|getContext
argument_list|()
operator|.
name|getParameters
argument_list|()
operator|.
name|add
argument_list|(
literal|"maxTotalConnections"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|component
operator|.
name|getMaxTotalConnections
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|public
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|client
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|client
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|RestletEndpoint
name|endpoint
init|=
operator|(
name|RestletEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
specifier|final
name|RestletBinding
name|binding
init|=
name|endpoint
operator|.
name|getRestletBinding
argument_list|()
decl_stmt|;
name|Request
name|request
decl_stmt|;
name|String
name|resourceUri
init|=
name|buildUri
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|resourceUri
argument_list|)
decl_stmt|;
name|request
operator|=
operator|new
name|Request
argument_list|(
name|endpoint
operator|.
name|getRestletMethod
argument_list|()
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
name|binding
operator|.
name|populateRestletRequestFromExchange
argument_list|(
name|request
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|loadCookies
argument_list|(
name|exchange
argument_list|,
name|uri
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sending request synchronously: {} for exchangeId: {}"
argument_list|,
name|request
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|handle
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received response synchronously: {} for exchangeId: {}"
argument_list|,
name|response
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
name|Integer
name|respCode
init|=
name|response
operator|.
name|getStatus
argument_list|()
operator|.
name|getCode
argument_list|()
decl_stmt|;
name|storeCookies
argument_list|(
name|exchange
argument_list|,
name|uri
argument_list|,
name|response
argument_list|)
expr_stmt|;
if|if
condition|(
name|respCode
operator|>
literal|207
operator|&&
name|throwException
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|populateRestletProducerException
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|,
name|respCode
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|binding
operator|.
name|populateExchangeFromRestletResponse
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|storeCookies (Exchange exchange, URI uri, Response response)
specifier|private
name|void
name|storeCookies
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|URI
name|uri
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
name|RestletEndpoint
name|endpoint
init|=
operator|(
name|RestletEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getCookieHandler
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Series
argument_list|<
name|CookieSetting
argument_list|>
name|cookieSettings
init|=
name|response
operator|.
name|getCookieSettings
argument_list|()
decl_stmt|;
name|CookieStore
name|cookieJar
init|=
name|endpoint
operator|.
name|getCookieHandler
argument_list|()
operator|.
name|getCookieStore
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
for|for
control|(
name|CookieSetting
name|s
range|:
name|cookieSettings
control|)
block|{
name|HttpCookie
name|cookie
init|=
operator|new
name|HttpCookie
argument_list|(
name|s
operator|.
name|getName
argument_list|()
argument_list|,
name|s
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|cookie
operator|.
name|setComment
argument_list|(
name|s
operator|.
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
name|cookie
operator|.
name|setDomain
argument_list|(
name|s
operator|.
name|getDomain
argument_list|()
argument_list|)
expr_stmt|;
name|cookie
operator|.
name|setMaxAge
argument_list|(
name|s
operator|.
name|getMaxAge
argument_list|()
argument_list|)
expr_stmt|;
name|cookie
operator|.
name|setPath
argument_list|(
name|s
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|cookie
operator|.
name|setSecure
argument_list|(
name|s
operator|.
name|isSecure
argument_list|()
argument_list|)
expr_stmt|;
name|cookie
operator|.
name|setVersion
argument_list|(
name|s
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|cookieJar
operator|.
name|add
argument_list|(
name|uri
argument_list|,
name|cookie
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|loadCookies (Exchange exchange, URI uri, Request request)
specifier|private
name|void
name|loadCookies
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|URI
name|uri
parameter_list|,
name|Request
name|request
parameter_list|)
throws|throws
name|IOException
block|{
name|RestletEndpoint
name|endpoint
init|=
operator|(
name|RestletEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getCookieHandler
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Series
argument_list|<
name|Cookie
argument_list|>
name|cookies
init|=
name|request
operator|.
name|getCookies
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|cookieHeaders
init|=
name|endpoint
operator|.
name|getCookieHandler
argument_list|()
operator|.
name|loadCookies
argument_list|(
name|exchange
argument_list|,
name|uri
argument_list|)
decl_stmt|;
comment|// parse the cookies
for|for
control|(
name|String
name|cookieHeader
range|:
name|cookieHeaders
operator|.
name|keySet
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|cookieStr
range|:
name|cookieHeaders
operator|.
name|get
argument_list|(
name|cookieHeader
argument_list|)
control|)
block|{
for|for
control|(
name|HttpCookie
name|cookie
range|:
name|HttpCookie
operator|.
name|parse
argument_list|(
name|cookieStr
argument_list|)
control|)
block|{
name|cookies
operator|.
name|add
argument_list|(
operator|new
name|Cookie
argument_list|(
name|cookie
operator|.
name|getVersion
argument_list|()
argument_list|,
name|cookie
operator|.
name|getName
argument_list|()
argument_list|,
name|cookie
operator|.
name|getValue
argument_list|()
argument_list|,
name|cookie
operator|.
name|getPath
argument_list|()
argument_list|,
name|cookie
operator|.
name|getDomain
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|RestletEndpoint
name|endpoint
init|=
operator|(
name|RestletEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
comment|// force processing synchronously using different api
if|if
condition|(
name|endpoint
operator|.
name|isSynchronous
argument_list|()
condition|)
block|{
try|try
block|{
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing asynchronously"
argument_list|)
expr_stmt|;
specifier|final
name|RestletBinding
name|binding
init|=
name|endpoint
operator|.
name|getRestletBinding
argument_list|()
decl_stmt|;
name|Request
name|request
decl_stmt|;
try|try
block|{
name|String
name|resourceUri
init|=
name|buildUri
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|resourceUri
argument_list|)
decl_stmt|;
name|request
operator|=
operator|new
name|Request
argument_list|(
name|endpoint
operator|.
name|getRestletMethod
argument_list|()
argument_list|,
name|resourceUri
argument_list|)
expr_stmt|;
name|binding
operator|.
name|populateRestletRequestFromExchange
argument_list|(
name|request
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|loadCookies
argument_list|(
name|exchange
argument_list|,
name|uri
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// break out in case of exception
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// process the request asynchronously
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sending request asynchronously: {} for exchangeId: {}"
argument_list|,
name|request
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|handle
argument_list|(
name|request
argument_list|,
operator|new
name|Uniform
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|Request
name|request
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received response asynchronously: {} for exchangeId: {}"
argument_list|,
name|response
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
name|String
name|resourceUri
init|=
name|buildUri
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|resourceUri
argument_list|)
decl_stmt|;
name|Integer
name|respCode
init|=
name|response
operator|.
name|getStatus
argument_list|()
operator|.
name|getCode
argument_list|()
decl_stmt|;
name|storeCookies
argument_list|(
name|exchange
argument_list|,
name|uri
argument_list|,
name|response
argument_list|)
expr_stmt|;
if|if
condition|(
name|respCode
operator|>
literal|207
operator|&&
name|throwException
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|populateRestletProducerException
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|,
name|respCode
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|binding
operator|.
name|populateExchangeFromRestletResponse
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
comment|// we continue routing async
return|return
literal|false
return|;
block|}
DECL|method|buildUri (RestletEndpoint endpoint, Exchange exchange)
specifier|private
specifier|static
name|String
name|buildUri
parameter_list|(
name|RestletEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// rest producer may provide an override url to be used which we should discard if using (hence the remove)
name|String
name|uri
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|REST_HTTP_URI
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
name|uri
operator|=
name|endpoint
operator|.
name|getProtocol
argument_list|()
operator|+
literal|"://"
operator|+
name|endpoint
operator|.
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|endpoint
operator|.
name|getPort
argument_list|()
operator|+
name|endpoint
operator|.
name|getUriPattern
argument_list|()
expr_stmt|;
block|}
comment|// include any query parameters if needed
if|if
condition|(
name|endpoint
operator|.
name|getQueryParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|uri
operator|=
name|URISupport
operator|.
name|appendParametersToURI
argument_list|(
name|uri
argument_list|,
name|endpoint
operator|.
name|getQueryParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// substitute { } placeholders in uri and use mandatory headers
name|LOG
operator|.
name|trace
argument_list|(
literal|"Substituting '{value}' placeholders in uri: {}"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|Matcher
name|matcher
init|=
name|PATTERN
operator|.
name|matcher
argument_list|(
name|uri
argument_list|)
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|key
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|header
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|key
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// header should be mandatory
if|if
condition|(
name|header
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Header with key: "
operator|+
name|key
operator|+
literal|" not found in Exchange"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Replacing: {} with header value: {}"
argument_list|,
name|matcher
operator|.
name|group
argument_list|(
literal|0
argument_list|)
argument_list|,
name|header
argument_list|)
expr_stmt|;
block|}
name|uri
operator|=
name|matcher
operator|.
name|replaceFirst
argument_list|(
name|header
argument_list|)
expr_stmt|;
comment|// we replaced uri so reset and go again
name|matcher
operator|.
name|reset
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
comment|// rest producer may provide an override query string to be used which we should discard if using (hence the remove)
name|String
name|query
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|REST_HTTP_QUERY
argument_list|)
decl_stmt|;
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
name|query
operator|=
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
expr_stmt|;
block|}
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Adding query: {} to uri: {}"
argument_list|,
name|query
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|uri
operator|=
name|addQueryToUri
argument_list|(
name|uri
argument_list|,
name|query
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using uri: {}"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
return|return
name|uri
return|;
block|}
DECL|method|addQueryToUri (String uri, String query)
specifier|protected
specifier|static
name|String
name|addQueryToUri
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|query
parameter_list|)
block|{
if|if
condition|(
name|uri
operator|==
literal|null
operator|||
name|uri
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|uri
return|;
block|}
name|StringBuilder
name|answer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|index
init|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|<
literal|0
condition|)
block|{
name|answer
operator|.
name|append
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|answer
operator|.
name|append
argument_list|(
literal|"?"
argument_list|)
expr_stmt|;
name|answer
operator|.
name|append
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|.
name|append
argument_list|(
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|append
argument_list|(
literal|"?"
argument_list|)
expr_stmt|;
name|answer
operator|.
name|append
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|String
name|remaining
init|=
name|uri
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|remaining
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|answer
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
expr_stmt|;
name|answer
operator|.
name|append
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|populateRestletProducerException (Exchange exchange, Response response, int responseCode)
specifier|protected
name|RestletOperationException
name|populateRestletProducerException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Response
name|response
parameter_list|,
name|int
name|responseCode
parameter_list|)
block|{
name|RestletOperationException
name|exception
decl_stmt|;
name|String
name|uri
init|=
name|response
operator|.
name|getRequest
argument_list|()
operator|.
name|getResourceRef
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|statusText
init|=
name|response
operator|.
name|getStatus
argument_list|()
operator|.
name|getDescription
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|parseResponseHeaders
argument_list|(
name|response
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|String
name|copy
decl_stmt|;
if|if
condition|(
name|response
operator|.
name|getEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|copy
operator|=
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getText
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|copy
operator|=
name|ex
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|copy
operator|=
name|response
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|responseCode
operator|>=
literal|300
operator|&&
name|responseCode
operator|<
literal|400
condition|)
block|{
name|String
name|redirectLocation
decl_stmt|;
if|if
condition|(
name|response
operator|.
name|getStatus
argument_list|()
operator|.
name|isRedirection
argument_list|()
condition|)
block|{
name|redirectLocation
operator|=
name|response
operator|.
name|getLocationRef
argument_list|()
operator|.
name|getHostIdentifier
argument_list|()
expr_stmt|;
name|exception
operator|=
operator|new
name|RestletOperationException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|statusText
argument_list|,
name|redirectLocation
argument_list|,
name|headers
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//no redirect location
name|exception
operator|=
operator|new
name|RestletOperationException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|statusText
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|//internal server error(error code 500)
name|exception
operator|=
operator|new
name|RestletOperationException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|statusText
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
return|return
name|exception
return|;
block|}
DECL|method|parseResponseHeaders (Object response, Exchange camelExchange)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parseResponseHeaders
parameter_list|(
name|Object
name|response
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|response
operator|instanceof
name|Response
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
operator|(
operator|(
name|Response
operator|)
name|response
operator|)
operator|.
name|getAttributes
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"Parse external header {}={}"
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|answer
operator|.
name|put
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
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

