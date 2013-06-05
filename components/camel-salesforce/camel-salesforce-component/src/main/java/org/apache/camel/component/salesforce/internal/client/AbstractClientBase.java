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
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|ContentExchange
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
name|HttpEventListenerWrapper
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
name|HttpExchange
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
name|HttpSchemes
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
name|eclipse
operator|.
name|jetty
operator|.
name|io
operator|.
name|Buffer
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
name|util
operator|.
name|StringUtil
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
DECL|field|LOG
specifier|protected
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
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
DECL|field|httpClient
specifier|protected
specifier|final
name|HttpClient
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
DECL|method|AbstractClientBase (String version, SalesforceSession session, HttpClient httpClient)
specifier|public
name|AbstractClientBase
parameter_list|(
name|String
name|version
parameter_list|,
name|SalesforceSession
name|session
parameter_list|,
name|HttpClient
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
DECL|method|getContentExchange (String method, String url)
specifier|protected
name|SalesforceExchange
name|getContentExchange
parameter_list|(
name|String
name|method
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|SalesforceExchange
name|get
init|=
operator|new
name|SalesforceExchange
argument_list|()
decl_stmt|;
name|get
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|get
operator|.
name|setURL
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|get
operator|.
name|setClient
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|get
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
DECL|method|doHttpRequest (final ContentExchange request, final ClientResponseCallback callback)
specifier|protected
name|void
name|doHttpRequest
parameter_list|(
specifier|final
name|ContentExchange
name|request
parameter_list|,
specifier|final
name|ClientResponseCallback
name|callback
parameter_list|)
block|{
comment|// use SalesforceSecurityListener for security login retries
try|try
block|{
specifier|final
name|boolean
name|isHttps
init|=
name|HttpSchemes
operator|.
name|HTTPS
operator|.
name|equals
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|request
operator|.
name|getScheme
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|request
operator|.
name|setEventListener
argument_list|(
operator|new
name|SalesforceSecurityListener
argument_list|(
name|httpClient
operator|.
name|getDestination
argument_list|(
name|request
operator|.
name|getAddress
argument_list|()
argument_list|,
name|isHttps
argument_list|)
argument_list|,
name|request
argument_list|,
name|session
argument_list|,
name|accessToken
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// propagate exception
name|callback
operator|.
name|onResponse
argument_list|(
literal|null
argument_list|,
operator|new
name|SalesforceException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error registering security listener: %s"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// use HttpEventListener for lifecycle events
name|request
operator|.
name|setEventListener
argument_list|(
operator|new
name|HttpEventListenerWrapper
argument_list|(
name|request
operator|.
name|getEventListener
argument_list|()
argument_list|,
literal|true
argument_list|)
block|{
specifier|public
name|String
name|reason
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|onConnectionFailed
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|super
operator|.
name|onConnectionFailed
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|callback
operator|.
name|onResponse
argument_list|(
literal|null
argument_list|,
operator|new
name|SalesforceException
argument_list|(
literal|"Connection error: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onException
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|super
operator|.
name|onException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|callback
operator|.
name|onResponse
argument_list|(
literal|null
argument_list|,
operator|new
name|SalesforceException
argument_list|(
literal|"Unexpected exception: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onExpire
parameter_list|()
block|{
name|super
operator|.
name|onExpire
argument_list|()
expr_stmt|;
name|callback
operator|.
name|onResponse
argument_list|(
literal|null
argument_list|,
operator|new
name|SalesforceException
argument_list|(
literal|"Request expired"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onResponseComplete
parameter_list|()
throws|throws
name|IOException
block|{
name|super
operator|.
name|onResponseComplete
argument_list|()
expr_stmt|;
specifier|final
name|int
name|responseStatus
init|=
name|request
operator|.
name|getResponseStatus
argument_list|()
decl_stmt|;
if|if
condition|(
name|responseStatus
operator|<
name|HttpStatus
operator|.
name|OK_200
operator|||
name|responseStatus
operator|>=
name|HttpStatus
operator|.
name|MULTIPLE_CHOICES_300
condition|)
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
name|responseStatus
argument_list|,
name|reason
argument_list|,
name|request
operator|.
name|getMethod
argument_list|()
argument_list|,
name|request
operator|.
name|getRequestURI
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|SalesforceException
name|exception
init|=
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|createRestException
argument_list|(
name|request
argument_list|)
argument_list|)
decl_stmt|;
name|exception
operator|.
name|setStatusCode
argument_list|(
name|responseStatus
argument_list|)
expr_stmt|;
name|callback
operator|.
name|onResponse
argument_list|(
literal|null
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// TODO not memory efficient for large response messages,
comment|// doesn't seem to be possible in Jetty 7 to directly stream to response parsers
specifier|final
name|byte
index|[]
name|bytes
init|=
name|request
operator|.
name|getResponseContentBytes
argument_list|()
decl_stmt|;
name|callback
operator|.
name|onResponse
argument_list|(
name|bytes
operator|!=
literal|null
condition|?
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
else|:
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onResponseStatus
parameter_list|(
name|Buffer
name|version
parameter_list|,
name|int
name|status
parameter_list|,
name|Buffer
name|reason
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|onResponseStatus
argument_list|(
name|version
argument_list|,
name|status
argument_list|,
name|reason
argument_list|)
expr_stmt|;
comment|// remember status reason
name|this
operator|.
name|reason
operator|=
name|reason
operator|.
name|toString
argument_list|(
name|StringUtil
operator|.
name|__ISO_8859_1
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// execute the request
try|try
block|{
name|httpClient
operator|.
name|send
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Unexpected Error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
comment|// send error through callback
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
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
DECL|method|setAccessToken (HttpExchange httpExchange)
specifier|protected
specifier|abstract
name|void
name|setAccessToken
parameter_list|(
name|HttpExchange
name|httpExchange
parameter_list|)
function_decl|;
DECL|method|createRestException (ContentExchange httpExchange)
specifier|protected
specifier|abstract
name|SalesforceException
name|createRestException
parameter_list|(
name|ContentExchange
name|httpExchange
parameter_list|)
function_decl|;
block|}
end_class

end_unit

