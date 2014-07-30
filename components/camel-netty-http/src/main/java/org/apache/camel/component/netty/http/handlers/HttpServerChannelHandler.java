begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http.handlers
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
operator|.
name|handlers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketAddress
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
name|nio
operator|.
name|channels
operator|.
name|ClosedChannelException
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
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|login
operator|.
name|LoginException
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
name|LoggingLevel
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
name|netty
operator|.
name|NettyConsumer
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
name|netty
operator|.
name|NettyHelper
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
name|netty
operator|.
name|handlers
operator|.
name|ServerChannelHandler
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
name|netty
operator|.
name|http
operator|.
name|HttpPrincipal
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
name|netty
operator|.
name|http
operator|.
name|NettyHttpConsumer
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
name|netty
operator|.
name|http
operator|.
name|NettyHttpSecurityConfiguration
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
name|netty
operator|.
name|http
operator|.
name|SecurityAuthenticator
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
name|CamelLogger
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
name|jboss
operator|.
name|netty
operator|.
name|buffer
operator|.
name|ChannelBuffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|buffer
operator|.
name|ChannelBuffers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelFutureListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelHandlerContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|ExceptionEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|MessageEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|base64
operator|.
name|Base64
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|DefaultHttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpHeaders
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
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

begin_import
import|import static
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpHeaders
operator|.
name|isKeepAlive
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpResponseStatus
operator|.
name|BAD_REQUEST
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpResponseStatus
operator|.
name|METHOD_NOT_ALLOWED
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpResponseStatus
operator|.
name|SERVICE_UNAVAILABLE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpResponseStatus
operator|.
name|UNAUTHORIZED
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpVersion
operator|.
name|HTTP_1_1
import|;
end_import

begin_comment
comment|/**  * Netty HTTP {@link ServerChannelHandler} that handles the incoming HTTP requests and routes  * the received message in Camel.  */
end_comment

begin_class
DECL|class|HttpServerChannelHandler
specifier|public
class|class
name|HttpServerChannelHandler
extends|extends
name|ServerChannelHandler
block|{
comment|// use NettyHttpConsumer as logger to make it easier to read the logs as this is part of the consumer
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
name|NettyHttpConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|consumer
specifier|private
specifier|final
name|NettyHttpConsumer
name|consumer
decl_stmt|;
DECL|field|request
specifier|private
name|HttpRequest
name|request
decl_stmt|;
DECL|method|HttpServerChannelHandler (NettyHttpConsumer consumer)
specifier|public
name|HttpServerChannelHandler
parameter_list|(
name|NettyHttpConsumer
name|consumer
parameter_list|)
block|{
name|super
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|messageReceived (ChannelHandlerContext ctx, MessageEvent messageEvent)
specifier|public
name|void
name|messageReceived
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|MessageEvent
name|messageEvent
parameter_list|)
throws|throws
name|Exception
block|{
comment|// store request, as this channel handler is created per pipeline
name|request
operator|=
operator|(
name|HttpRequest
operator|)
name|messageEvent
operator|.
name|getMessage
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Message received: {}"
argument_list|,
name|request
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumer
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
comment|// are we suspended?
name|LOG
operator|.
name|debug
argument_list|(
literal|"Consumer suspended, cannot service request {}"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
operator|new
name|DefaultHttpResponse
argument_list|(
name|HTTP_1_1
argument_list|,
name|SERVICE_UNAVAILABLE
argument_list|)
decl_stmt|;
name|response
operator|.
name|setChunked
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|response
operator|.
name|headers
argument_list|()
operator|.
name|set
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|response
operator|.
name|headers
argument_list|()
operator|.
name|set
argument_list|(
name|Exchange
operator|.
name|CONTENT_LENGTH
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|response
operator|.
name|setContent
argument_list|(
name|ChannelBuffers
operator|.
name|copiedBuffer
argument_list|(
operator|new
name|byte
index|[]
block|{}
argument_list|)
argument_list|)
expr_stmt|;
name|messageEvent
operator|.
name|getChannel
argument_list|()
operator|.
name|write
argument_list|(
name|response
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
argument_list|()
operator|.
name|contains
argument_list|(
name|request
operator|.
name|getMethod
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|HttpResponse
name|response
init|=
operator|new
name|DefaultHttpResponse
argument_list|(
name|HTTP_1_1
argument_list|,
name|METHOD_NOT_ALLOWED
argument_list|)
decl_stmt|;
name|response
operator|.
name|setChunked
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|response
operator|.
name|headers
argument_list|()
operator|.
name|set
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|response
operator|.
name|headers
argument_list|()
operator|.
name|set
argument_list|(
name|Exchange
operator|.
name|CONTENT_LENGTH
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|response
operator|.
name|setContent
argument_list|(
name|ChannelBuffers
operator|.
name|copiedBuffer
argument_list|(
operator|new
name|byte
index|[]
block|{}
argument_list|)
argument_list|)
expr_stmt|;
name|messageEvent
operator|.
name|getChannel
argument_list|()
operator|.
name|write
argument_list|(
name|response
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
literal|"TRACE"
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getMethod
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
operator|!
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|HttpResponse
name|response
init|=
operator|new
name|DefaultHttpResponse
argument_list|(
name|HTTP_1_1
argument_list|,
name|METHOD_NOT_ALLOWED
argument_list|)
decl_stmt|;
name|response
operator|.
name|setChunked
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|response
operator|.
name|headers
argument_list|()
operator|.
name|set
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|response
operator|.
name|headers
argument_list|()
operator|.
name|set
argument_list|(
name|Exchange
operator|.
name|CONTENT_LENGTH
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|response
operator|.
name|setContent
argument_list|(
name|ChannelBuffers
operator|.
name|copiedBuffer
argument_list|(
operator|new
name|byte
index|[]
block|{}
argument_list|)
argument_list|)
expr_stmt|;
name|messageEvent
operator|.
name|getChannel
argument_list|()
operator|.
name|write
argument_list|(
name|response
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// must include HOST header as required by HTTP 1.1
if|if
condition|(
operator|!
name|request
operator|.
name|headers
argument_list|()
operator|.
name|names
argument_list|()
operator|.
name|contains
argument_list|(
name|HttpHeaders
operator|.
name|Names
operator|.
name|HOST
argument_list|)
condition|)
block|{
name|HttpResponse
name|response
init|=
operator|new
name|DefaultHttpResponse
argument_list|(
name|HTTP_1_1
argument_list|,
name|BAD_REQUEST
argument_list|)
decl_stmt|;
name|response
operator|.
name|setChunked
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|response
operator|.
name|headers
argument_list|()
operator|.
name|set
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|response
operator|.
name|headers
argument_list|()
operator|.
name|set
argument_list|(
name|Exchange
operator|.
name|CONTENT_LENGTH
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|response
operator|.
name|setContent
argument_list|(
name|ChannelBuffers
operator|.
name|copiedBuffer
argument_list|(
operator|new
name|byte
index|[]
block|{}
argument_list|)
argument_list|)
expr_stmt|;
name|messageEvent
operator|.
name|getChannel
argument_list|()
operator|.
name|write
argument_list|(
name|response
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// is basic auth configured
name|NettyHttpSecurityConfiguration
name|security
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getSecurityConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|security
operator|!=
literal|null
operator|&&
name|security
operator|.
name|isAuthenticate
argument_list|()
operator|&&
literal|"Basic"
operator|.
name|equalsIgnoreCase
argument_list|(
name|security
operator|.
name|getConstraint
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|url
init|=
name|request
operator|.
name|getUri
argument_list|()
decl_stmt|;
comment|// drop parameters from url
if|if
condition|(
name|url
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|)
block|{
name|url
operator|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|url
argument_list|,
literal|"?"
argument_list|)
expr_stmt|;
block|}
comment|// we need the relative path without the hostname and port
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|request
operator|.
name|getUri
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|target
init|=
name|uri
operator|.
name|getPath
argument_list|()
decl_stmt|;
comment|// strip the starting endpoint path so the target is relative to the endpoint uri
name|String
name|path
init|=
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
name|target
operator|.
name|startsWith
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|target
operator|=
name|target
operator|.
name|substring
argument_list|(
name|path
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// is it a restricted resource?
name|String
name|roles
decl_stmt|;
if|if
condition|(
name|security
operator|.
name|getSecurityConstraint
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// if restricted returns null, then the resource is not restricted and we should not authenticate the user
name|roles
operator|=
name|security
operator|.
name|getSecurityConstraint
argument_list|()
operator|.
name|restricted
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// assume any roles is valid if no security constraint has been configured
name|roles
operator|=
literal|"*"
expr_stmt|;
block|}
if|if
condition|(
name|roles
operator|!=
literal|null
condition|)
block|{
comment|// basic auth subject
name|HttpPrincipal
name|principal
init|=
name|extractBasicAuthSubject
argument_list|(
name|request
argument_list|)
decl_stmt|;
comment|// authenticate principal and check if the user is in role
name|Subject
name|subject
init|=
literal|null
decl_stmt|;
name|boolean
name|inRole
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|principal
operator|!=
literal|null
condition|)
block|{
name|subject
operator|=
name|authenticate
argument_list|(
name|security
operator|.
name|getSecurityAuthenticator
argument_list|()
argument_list|,
name|security
operator|.
name|getLoginDeniedLoggingLevel
argument_list|()
argument_list|,
name|principal
argument_list|)
expr_stmt|;
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
name|String
name|userRoles
init|=
name|security
operator|.
name|getSecurityAuthenticator
argument_list|()
operator|.
name|getUserRoles
argument_list|(
name|subject
argument_list|)
decl_stmt|;
name|inRole
operator|=
name|matchesRoles
argument_list|(
name|roles
argument_list|,
name|userRoles
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|principal
operator|==
literal|null
operator|||
name|subject
operator|==
literal|null
operator|||
operator|!
name|inRole
condition|)
block|{
if|if
condition|(
name|principal
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Http Basic Auth required for resource: {}"
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|subject
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Http Basic Auth not authorized for username: {}"
argument_list|,
name|principal
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Http Basic Auth not in role for username: {}"
argument_list|,
name|principal
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// restricted resource, so send back 401 to require valid username/password
name|HttpResponse
name|response
init|=
operator|new
name|DefaultHttpResponse
argument_list|(
name|HTTP_1_1
argument_list|,
name|UNAUTHORIZED
argument_list|)
decl_stmt|;
name|response
operator|.
name|headers
argument_list|()
operator|.
name|set
argument_list|(
literal|"WWW-Authenticate"
argument_list|,
literal|"Basic realm=\""
operator|+
name|security
operator|.
name|getRealm
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|response
operator|.
name|headers
argument_list|()
operator|.
name|set
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|response
operator|.
name|headers
argument_list|()
operator|.
name|set
argument_list|(
name|Exchange
operator|.
name|CONTENT_LENGTH
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|response
operator|.
name|setContent
argument_list|(
name|ChannelBuffers
operator|.
name|copiedBuffer
argument_list|(
operator|new
name|byte
index|[]
block|{}
argument_list|)
argument_list|)
expr_stmt|;
name|messageEvent
operator|.
name|getChannel
argument_list|()
operator|.
name|write
argument_list|(
name|response
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Http Basic Auth authorized for username: {}"
argument_list|,
name|principal
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// let Camel process this message
name|super
operator|.
name|messageReceived
argument_list|(
name|ctx
argument_list|,
name|messageEvent
argument_list|)
expr_stmt|;
block|}
DECL|method|matchesRoles (String roles, String userRoles)
specifier|protected
name|boolean
name|matchesRoles
parameter_list|(
name|String
name|roles
parameter_list|,
name|String
name|userRoles
parameter_list|)
block|{
comment|// matches if no role restrictions or any role is accepted
if|if
condition|(
name|roles
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// see if any of the user roles is contained in the roles list
name|Iterator
argument_list|<
name|Object
argument_list|>
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|userRoles
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|userRole
init|=
name|it
operator|.
name|next
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|roles
operator|.
name|contains
argument_list|(
name|userRole
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Extracts the username and password details from the HTTP basic header Authorization.      *<p/>      * This requires that the<tt>Authorization</tt> HTTP header is provided, and its using Basic.      * Currently Digest is<b>not</b> supported.      *      * @return {@link HttpPrincipal} with username and password details, or<tt>null</tt> if not possible to extract      */
DECL|method|extractBasicAuthSubject (HttpRequest request)
specifier|protected
specifier|static
name|HttpPrincipal
name|extractBasicAuthSubject
parameter_list|(
name|HttpRequest
name|request
parameter_list|)
block|{
name|String
name|auth
init|=
name|request
operator|.
name|headers
argument_list|()
operator|.
name|get
argument_list|(
literal|"Authorization"
argument_list|)
decl_stmt|;
if|if
condition|(
name|auth
operator|!=
literal|null
condition|)
block|{
name|String
name|constraint
init|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|auth
argument_list|,
literal|" "
argument_list|)
decl_stmt|;
if|if
condition|(
name|constraint
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"Basic"
operator|.
name|equalsIgnoreCase
argument_list|(
name|constraint
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|decoded
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|auth
argument_list|,
literal|" "
argument_list|)
decl_stmt|;
comment|// the decoded part is base64 encoded, so we need to decode that
name|ChannelBuffer
name|buf
init|=
name|ChannelBuffers
operator|.
name|copiedBuffer
argument_list|(
name|decoded
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|ChannelBuffer
name|out
init|=
name|Base64
operator|.
name|decode
argument_list|(
name|buf
argument_list|)
decl_stmt|;
name|String
name|userAndPw
init|=
name|out
operator|.
name|toString
argument_list|(
name|Charset
operator|.
name|defaultCharset
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|username
init|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|userAndPw
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|String
name|password
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|userAndPw
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|HttpPrincipal
name|principal
init|=
operator|new
name|HttpPrincipal
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Extracted Basic Auth principal from HTTP header: {}"
argument_list|,
name|principal
argument_list|)
expr_stmt|;
return|return
name|principal
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Authenticates the http basic auth subject.      *      * @param authenticator      the authenticator      * @param principal          the principal      * @return<tt>true</tt> if username and password is valid,<tt>false</tt> if not      */
DECL|method|authenticate (SecurityAuthenticator authenticator, LoggingLevel deniedLoggingLevel, HttpPrincipal principal)
specifier|protected
name|Subject
name|authenticate
parameter_list|(
name|SecurityAuthenticator
name|authenticator
parameter_list|,
name|LoggingLevel
name|deniedLoggingLevel
parameter_list|,
name|HttpPrincipal
name|principal
parameter_list|)
block|{
try|try
block|{
return|return
name|authenticator
operator|.
name|login
argument_list|(
name|principal
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|LoginException
name|e
parameter_list|)
block|{
name|CamelLogger
name|logger
init|=
operator|new
name|CamelLogger
argument_list|(
name|LOG
argument_list|,
name|deniedLoggingLevel
argument_list|)
decl_stmt|;
name|logger
operator|.
name|log
argument_list|(
literal|"Cannot login "
operator|+
name|principal
operator|.
name|getName
argument_list|()
operator|+
literal|" due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|beforeProcess (Exchange exchange, MessageEvent messageEvent)
specifier|protected
name|void
name|beforeProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|MessageEvent
name|messageEvent
parameter_list|)
block|{
if|if
condition|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isBridgeEndpoint
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SKIP_GZIP_ENCODING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SKIP_WWW_FORM_URLENCODED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|exceptionCaught (ChannelHandlerContext ctx, ExceptionEvent exceptionEvent)
specifier|public
name|void
name|exceptionCaught
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|ExceptionEvent
name|exceptionEvent
parameter_list|)
throws|throws
name|Exception
block|{
comment|// only close if we are still allowed to run
if|if
condition|(
name|consumer
operator|.
name|isRunAllowed
argument_list|()
condition|)
block|{
if|if
condition|(
name|exceptionEvent
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ClosedChannelException
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Channel already closed. Ignoring this exception."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Closing channel as an exception was thrown from Netty"
argument_list|,
name|exceptionEvent
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
comment|// close channel in case an exception was thrown
name|NettyHelper
operator|.
name|close
argument_list|(
name|exceptionEvent
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|createResponseFutureListener (NettyConsumer consumer, Exchange exchange, SocketAddress remoteAddress)
specifier|protected
name|ChannelFutureListener
name|createResponseFutureListener
parameter_list|(
name|NettyConsumer
name|consumer
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|SocketAddress
name|remoteAddress
parameter_list|)
block|{
comment|// make sure to close channel if not keep-alive
if|if
condition|(
name|request
operator|!=
literal|null
operator|&&
name|isKeepAlive
argument_list|(
name|request
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Request has Connection: keep-alive so Channel is not being closed"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Request is not Connection: close so Channel is being closed"
argument_list|)
expr_stmt|;
return|return
name|ChannelFutureListener
operator|.
name|CLOSE
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getResponseBody (Exchange exchange)
specifier|protected
name|Object
name|getResponseBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// use the binding
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
return|return
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getNettyHttpBinding
argument_list|()
operator|.
name|toNettyResponse
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getNettyHttpBinding
argument_list|()
operator|.
name|toNettyResponse
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

