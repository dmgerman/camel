begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|internal
operator|.
name|client
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
name|nio
operator|.
name|ByteBuffer
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
name|List
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
name|TimeUnit
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
name|Service
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
name|salesforce
operator|.
name|SalesforceHttpClient
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
name|salesforce
operator|.
name|api
operator|.
name|SalesforceException
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
name|salesforce
operator|.
name|internal
operator|.
name|SalesforceSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|HttpContentResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|api
operator|.
name|ContentProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|api
operator|.
name|ContentResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|api
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|api
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|api
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|util
operator|.
name|BufferingResponseListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|util
operator|.
name|ByteBufferContentProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|util
operator|.
name|InputStreamContentProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|http
operator|.
name|HttpMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|http
operator|.
name|HttpStatus
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
DECL|class|AbstractClientBase
specifier|public
specifier|abstract
class|class
name|AbstractClientBase
implements|implements
name|SalesforceSession
operator|.
name|SalesforceSessionListener
implements|,
name|Service
block|{
DECL|field|APPLICATION_JSON_UTF8
specifier|protected
specifier|static
specifier|final
name|String
name|APPLICATION_JSON_UTF8
init|=
literal|"application/json;charset=utf-8"
decl_stmt|;
DECL|field|APPLICATION_XML_UTF8
specifier|protected
specifier|static
specifier|final
name|String
name|APPLICATION_XML_UTF8
init|=
literal|"application/xml;charset=utf-8"
decl_stmt|;
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|httpClient
specifier|protected
specifier|final
name|SalesforceHttpClient
name|httpClient
decl_stmt|;
DECL|field|session
specifier|protected
specifier|final
name|SalesforceSession
name|session
decl_stmt|;
DECL|field|version
specifier|protected
specifier|final
name|String
name|version
decl_stmt|;
DECL|field|accessToken
specifier|protected
name|String
name|accessToken
decl_stmt|;
DECL|field|instanceUrl
specifier|protected
name|String
name|instanceUrl
decl_stmt|;
DECL|method|AbstractClientBase (String version, SalesforceSession session, SalesforceHttpClient httpClient)
specifier|public
name|AbstractClientBase
parameter_list|(
name|String
name|version
parameter_list|,
name|SalesforceSession
name|session
parameter_list|,
name|SalesforceHttpClient
name|httpClient
parameter_list|)
throws|throws
name|SalesforceException
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
name|this
operator|.
name|httpClient
operator|=
name|httpClient
expr_stmt|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
comment|// local cache
name|accessToken
operator|=
name|session
operator|.
name|getAccessToken
argument_list|()
expr_stmt|;
if|if
condition|(
name|accessToken
operator|==
literal|null
condition|)
block|{
comment|// lazy login here!
name|accessToken
operator|=
name|session
operator|.
name|login
argument_list|(
name|accessToken
argument_list|)
expr_stmt|;
block|}
name|instanceUrl
operator|=
name|session
operator|.
name|getInstanceUrl
argument_list|()
expr_stmt|;
comment|// also register this client as a session listener
name|session
operator|.
name|addListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// deregister listener
name|session
operator|.
name|removeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onLogin (String accessToken, String instanceUrl)
specifier|public
name|void
name|onLogin
parameter_list|(
name|String
name|accessToken
parameter_list|,
name|String
name|instanceUrl
parameter_list|)
block|{
if|if
condition|(
operator|!
name|accessToken
operator|.
name|equals
argument_list|(
name|this
operator|.
name|accessToken
argument_list|)
condition|)
block|{
name|this
operator|.
name|accessToken
operator|=
name|accessToken
expr_stmt|;
name|this
operator|.
name|instanceUrl
operator|=
name|instanceUrl
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onLogout ()
specifier|public
name|void
name|onLogout
parameter_list|()
block|{
comment|// ignore, if this client makes another request with stale token,
comment|// SalesforceSecurityListener will auto login!
block|}
DECL|method|getRequest (HttpMethod method, String url)
specifier|protected
name|Request
name|getRequest
parameter_list|(
name|HttpMethod
name|method
parameter_list|,
name|String
name|url
parameter_list|)
block|{
return|return
name|getRequest
argument_list|(
name|method
operator|.
name|asString
argument_list|()
argument_list|,
name|url
argument_list|)
return|;
block|}
DECL|method|getRequest (String method, String url)
specifier|protected
name|Request
name|getRequest
parameter_list|(
name|String
name|method
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|SalesforceHttpRequest
name|request
init|=
operator|(
name|SalesforceHttpRequest
operator|)
name|httpClient
operator|.
name|newRequest
argument_list|(
name|url
argument_list|)
operator|.
name|method
argument_list|(
name|method
argument_list|)
operator|.
name|timeout
argument_list|(
name|session
operator|.
name|getTimeout
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|request
operator|.
name|getConversation
argument_list|()
operator|.
name|setAttribute
argument_list|(
name|SalesforceSecurityHandler
operator|.
name|CLIENT_ATTRIBUTE
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|request
return|;
block|}
DECL|interface|ClientResponseCallback
specifier|protected
interface|interface
name|ClientResponseCallback
block|{
DECL|method|onResponse (InputStream response, SalesforceException ex)
name|void
name|onResponse
parameter_list|(
name|InputStream
name|response
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
function_decl|;
block|}
DECL|method|doHttpRequest (final Request request, final ClientResponseCallback callback)
specifier|protected
name|void
name|doHttpRequest
parameter_list|(
specifier|final
name|Request
name|request
parameter_list|,
specifier|final
name|ClientResponseCallback
name|callback
parameter_list|)
block|{
comment|// Highly memory inefficient,
comment|// but buffer the request content to allow it to be replayed for authentication retries
specifier|final
name|ContentProvider
name|content
init|=
name|request
operator|.
name|getContent
argument_list|()
decl_stmt|;
if|if
condition|(
name|content
operator|instanceof
name|InputStreamContentProvider
condition|)
block|{
specifier|final
name|List
argument_list|<
name|ByteBuffer
argument_list|>
name|buffers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ByteBuffer
name|buffer
range|:
name|content
control|)
block|{
name|buffers
operator|.
name|add
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
name|request
operator|.
name|content
argument_list|(
operator|new
name|ByteBufferContentProvider
argument_list|(
name|buffers
operator|.
name|toArray
argument_list|(
operator|new
name|ByteBuffer
index|[
name|buffers
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|buffers
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|// execute the request
name|request
operator|.
name|send
argument_list|(
operator|new
name|BufferingResponseListener
argument_list|(
name|httpClient
operator|.
name|getMaxContentLength
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Result
name|result
parameter_list|)
block|{
name|Response
name|response
init|=
name|result
operator|.
name|getResponse
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|isFailed
argument_list|()
condition|)
block|{
comment|// Failure!!!
comment|// including Salesforce errors reported as exception from SalesforceSecurityHandler
name|Throwable
name|failure
init|=
name|result
operator|.
name|getFailure
argument_list|()
decl_stmt|;
if|if
condition|(
name|failure
operator|instanceof
name|SalesforceException
condition|)
block|{
name|callback
operator|.
name|onResponse
argument_list|(
literal|null
argument_list|,
operator|(
name|SalesforceException
operator|)
name|failure
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Unexpected error {%s:%s} executing {%s:%s}"
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
name|response
operator|.
name|getReason
argument_list|()
argument_list|,
name|request
operator|.
name|getMethod
argument_list|()
argument_list|,
name|request
operator|.
name|getURI
argument_list|()
argument_list|)
decl_stmt|;
name|callback
operator|.
name|onResponse
argument_list|(
literal|null
argument_list|,
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
name|failure
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// HTTP error status
specifier|final
name|int
name|status
init|=
name|response
operator|.
name|getStatus
argument_list|()
decl_stmt|;
name|SalesforceHttpRequest
name|request
init|=
call|(
name|SalesforceHttpRequest
call|)
argument_list|(
operator|(
name|SalesforceHttpRequest
operator|)
name|result
operator|.
name|getRequest
argument_list|()
argument_list|)
operator|.
name|getConversation
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|SalesforceSecurityHandler
operator|.
name|AUTHENTICATION_REQUEST_ATTRIBUTE
argument_list|)
decl_stmt|;
if|if
condition|(
name|status
operator|==
name|HttpStatus
operator|.
name|BAD_REQUEST_400
operator|&&
name|request
operator|!=
literal|null
condition|)
block|{
comment|// parse login error
name|ContentResponse
name|contentResponse
init|=
operator|new
name|HttpContentResponse
argument_list|(
name|response
argument_list|,
name|getContent
argument_list|()
argument_list|,
name|getMediaType
argument_list|()
argument_list|,
name|getEncoding
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|session
operator|.
name|parseLoginResponse
argument_list|(
name|contentResponse
argument_list|,
name|getContentAsString
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Unexpected Error {%s:%s} executing {%s:%s}"
argument_list|,
name|status
argument_list|,
name|response
operator|.
name|getReason
argument_list|()
argument_list|,
name|request
operator|.
name|getMethod
argument_list|()
argument_list|,
name|request
operator|.
name|getURI
argument_list|()
argument_list|)
decl_stmt|;
name|callback
operator|.
name|onResponse
argument_list|(
literal|null
argument_list|,
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SalesforceException
name|e
parameter_list|)
block|{
specifier|final
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Error {%s:%s} executing {%s:%s}"
argument_list|,
name|status
argument_list|,
name|response
operator|.
name|getReason
argument_list|()
argument_list|,
name|request
operator|.
name|getMethod
argument_list|()
argument_list|,
name|request
operator|.
name|getURI
argument_list|()
argument_list|)
decl_stmt|;
name|callback
operator|.
name|onResponse
argument_list|(
literal|null
argument_list|,
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|status
operator|<
name|HttpStatus
operator|.
name|OK_200
operator|||
name|status
operator|>=
name|HttpStatus
operator|.
name|MULTIPLE_CHOICES_300
condition|)
block|{
comment|// Salesforce HTTP failure!
name|request
operator|=
operator|(
name|SalesforceHttpRequest
operator|)
name|result
operator|.
name|getRequest
argument_list|()
expr_stmt|;
specifier|final
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Error {%s:%s} executing {%s:%s}"
argument_list|,
name|status
argument_list|,
name|response
operator|.
name|getReason
argument_list|()
argument_list|,
name|request
operator|.
name|getMethod
argument_list|()
argument_list|,
name|request
operator|.
name|getURI
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|SalesforceException
name|cause
init|=
name|createRestException
argument_list|(
name|response
argument_list|,
name|getContentAsInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|callback
operator|.
name|onResponse
argument_list|(
literal|null
argument_list|,
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
name|cause
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Success!!!
name|callback
operator|.
name|onResponse
argument_list|(
name|getContentAsInputStream
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|getContentAsInputStream
parameter_list|()
block|{
if|if
condition|(
name|getContent
argument_list|()
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|super
operator|.
name|getContentAsInputStream
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|setAccessToken (String accessToken)
specifier|public
name|void
name|setAccessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
block|{
name|this
operator|.
name|accessToken
operator|=
name|accessToken
expr_stmt|;
block|}
DECL|method|setInstanceUrl (String instanceUrl)
specifier|public
name|void
name|setInstanceUrl
parameter_list|(
name|String
name|instanceUrl
parameter_list|)
block|{
name|this
operator|.
name|instanceUrl
operator|=
name|instanceUrl
expr_stmt|;
block|}
DECL|method|setAccessToken (Request request)
specifier|protected
specifier|abstract
name|void
name|setAccessToken
parameter_list|(
name|Request
name|request
parameter_list|)
function_decl|;
DECL|method|createRestException (Response response, InputStream responseContent)
specifier|protected
specifier|abstract
name|SalesforceException
name|createRestException
parameter_list|(
name|Response
name|response
parameter_list|,
name|InputStream
name|responseContent
parameter_list|)
function_decl|;
block|}
end_class

end_unit

