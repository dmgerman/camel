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
name|Arrays
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
name|LinkedHashMap
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
name|Map
operator|.
name|Entry
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
name|Phaser
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeoutException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonParseException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|JsonMappingException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|XStream
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
name|api
operator|.
name|TypeReferences
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
name|dto
operator|.
name|RestError
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
name|PayloadFormat
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
name|dto
operator|.
name|RestErrors
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
name|HttpClient
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
name|HttpField
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
name|HttpFields
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
implements|,
name|HttpClientHolder
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
DECL|field|DEFAULT_TERMINATION_TIMEOUT
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_TERMINATION_TIMEOUT
init|=
literal|10
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
DECL|field|inflightRequests
specifier|private
name|Phaser
name|inflightRequests
decl_stmt|;
DECL|field|terminationTimeout
specifier|private
name|long
name|terminationTimeout
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
argument_list|(
name|version
argument_list|,
name|session
argument_list|,
name|httpClient
argument_list|,
name|DEFAULT_TERMINATION_TIMEOUT
argument_list|)
expr_stmt|;
block|}
DECL|method|AbstractClientBase (String version, SalesforceSession session, SalesforceHttpClient httpClient, int terminationTimeout)
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
parameter_list|,
name|int
name|terminationTimeout
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
name|this
operator|.
name|terminationTimeout
operator|=
name|terminationTimeout
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
name|inflightRequests
operator|=
operator|new
name|Phaser
argument_list|(
literal|1
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
if|if
condition|(
name|inflightRequests
operator|!=
literal|null
condition|)
block|{
name|inflightRequests
operator|.
name|arrive
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|inflightRequests
operator|.
name|isTerminated
argument_list|()
condition|)
block|{
try|try
block|{
name|inflightRequests
operator|.
name|awaitAdvanceInterruptibly
argument_list|(
literal|0
argument_list|,
name|terminationTimeout
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
decl||
name|TimeoutException
name|ignored
parameter_list|)
block|{
comment|// exception is ignored
block|}
block|}
block|}
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
DECL|method|getRequest (HttpMethod method, String url, Map<String, List<String>> headers)
specifier|protected
name|Request
name|getRequest
parameter_list|(
name|HttpMethod
name|method
parameter_list|,
name|String
name|url
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
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
argument_list|,
name|headers
argument_list|)
return|;
block|}
DECL|method|getRequest (String method, String url, Map<String, List<String>> headers)
specifier|protected
name|Request
name|getRequest
parameter_list|(
name|String
name|method
parameter_list|,
name|String
name|url
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
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
name|addHeadersTo
argument_list|(
name|request
argument_list|,
name|headers
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
DECL|method|onResponse (InputStream response, Map<String, String> headers, SalesforceException ex)
name|void
name|onResponse
parameter_list|(
name|InputStream
name|response
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
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
name|inflightRequests
operator|.
name|register
argument_list|()
expr_stmt|;
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
try|try
block|{
name|Response
name|response
init|=
name|result
operator|.
name|getResponse
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|determineHeadersFrom
argument_list|(
name|response
argument_list|)
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
name|headers
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
name|headers
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
name|headers
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
name|headers
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
specifier|final
name|SalesforceException
name|exception
init|=
name|createRestException
argument_list|(
name|response
argument_list|,
name|getContentAsInputStream
argument_list|()
argument_list|)
decl_stmt|;
comment|// for APIs that return body on status 400, such as Composite API we need content as well
name|callback
operator|.
name|onResponse
argument_list|(
name|getContentAsInputStream
argument_list|()
argument_list|,
name|headers
argument_list|,
name|exception
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
name|headers
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|inflightRequests
operator|.
name|arriveAndDeregister
argument_list|()
expr_stmt|;
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
annotation|@
name|Override
DECL|method|getHttpClient ()
specifier|public
name|HttpClient
name|getHttpClient
parameter_list|()
block|{
return|return
name|httpClient
return|;
block|}
DECL|method|readErrorsFrom (final InputStream responseContent, final PayloadFormat format, final ObjectMapper objectMapper, final XStream xStream)
specifier|final
name|List
argument_list|<
name|RestError
argument_list|>
name|readErrorsFrom
parameter_list|(
specifier|final
name|InputStream
name|responseContent
parameter_list|,
specifier|final
name|PayloadFormat
name|format
parameter_list|,
specifier|final
name|ObjectMapper
name|objectMapper
parameter_list|,
specifier|final
name|XStream
name|xStream
parameter_list|)
throws|throws
name|IOException
throws|,
name|JsonParseException
throws|,
name|JsonMappingException
block|{
specifier|final
name|List
argument_list|<
name|RestError
argument_list|>
name|restErrors
decl_stmt|;
if|if
condition|(
name|PayloadFormat
operator|.
name|JSON
operator|.
name|equals
argument_list|(
name|format
argument_list|)
condition|)
block|{
name|restErrors
operator|=
name|objectMapper
operator|.
name|readValue
argument_list|(
name|responseContent
argument_list|,
name|TypeReferences
operator|.
name|REST_ERROR_LIST_TYPE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|RestErrors
name|errors
init|=
operator|new
name|RestErrors
argument_list|()
decl_stmt|;
name|xStream
operator|.
name|fromXML
argument_list|(
name|responseContent
argument_list|,
name|errors
argument_list|)
expr_stmt|;
name|restErrors
operator|=
name|errors
operator|.
name|getErrors
argument_list|()
expr_stmt|;
block|}
return|return
name|restErrors
return|;
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
DECL|method|determineHeadersFrom (final Response response)
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|determineHeadersFrom
parameter_list|(
specifier|final
name|Response
name|response
parameter_list|)
block|{
specifier|final
name|HttpFields
name|headers
init|=
name|response
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|HttpField
name|header
range|:
name|headers
control|)
block|{
specifier|final
name|String
name|headerName
init|=
name|header
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|headerName
operator|.
name|startsWith
argument_list|(
literal|"Sforce"
argument_list|)
condition|)
block|{
name|answer
operator|.
name|put
argument_list|(
name|headerName
argument_list|,
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|addHeadersTo (final Request request, final Map<String, List<String>> headers)
specifier|private
specifier|static
name|void
name|addHeadersTo
parameter_list|(
specifier|final
name|Request
name|request
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|)
block|{
if|if
condition|(
name|headers
operator|==
literal|null
operator|||
name|headers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|HttpFields
name|requestHeaders
init|=
name|request
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|header
range|:
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|requestHeaders
operator|.
name|put
argument_list|(
name|header
operator|.
name|getKey
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
DECL|method|determineHeaders (final Exchange exchange)
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|determineHeaders
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|Message
name|inboundMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|inboundMessage
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|headerName
range|:
name|headers
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|headerName
operator|.
name|startsWith
argument_list|(
literal|"Sforce"
argument_list|)
operator|||
name|headerName
operator|.
name|startsWith
argument_list|(
literal|"x-sfdc"
argument_list|)
condition|)
block|{
specifier|final
name|String
index|[]
name|values
init|=
name|inboundMessage
operator|.
name|getHeader
argument_list|(
name|headerName
argument_list|,
name|String
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|headerName
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|)
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

