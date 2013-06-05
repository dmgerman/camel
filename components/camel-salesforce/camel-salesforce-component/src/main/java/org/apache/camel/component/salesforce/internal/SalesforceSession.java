begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal
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
name|Set
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
name|CopyOnWriteArraySet
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
name|SalesforceLoginConfig
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
name|dto
operator|.
name|LoginError
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
name|LoginToken
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|map
operator|.
name|ObjectMapper
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
name|HttpMethods
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
name|io
operator|.
name|ByteArrayBuffer
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
name|eclipse
operator|.
name|jetty
operator|.
name|util
operator|.
name|UrlEncoded
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
DECL|class|SalesforceSession
specifier|public
class|class
name|SalesforceSession
implements|implements
name|Service
block|{
DECL|field|OAUTH2_REVOKE_PATH
specifier|private
specifier|static
specifier|final
name|String
name|OAUTH2_REVOKE_PATH
init|=
literal|"/services/oauth2/revoke?token="
decl_stmt|;
DECL|field|OAUTH2_TOKEN_PATH
specifier|private
specifier|static
specifier|final
name|String
name|OAUTH2_TOKEN_PATH
init|=
literal|"/services/oauth2/token"
decl_stmt|;
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
name|SalesforceSession
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|FORM_CONTENT_TYPE
specifier|private
specifier|static
specifier|final
name|String
name|FORM_CONTENT_TYPE
init|=
literal|"application/x-www-form-urlencoded;charset=utf-8"
decl_stmt|;
DECL|field|httpClient
specifier|private
specifier|final
name|HttpClient
name|httpClient
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|SalesforceLoginConfig
name|config
decl_stmt|;
DECL|field|objectMapper
specifier|private
specifier|final
name|ObjectMapper
name|objectMapper
decl_stmt|;
DECL|field|listeners
specifier|private
specifier|final
name|Set
argument_list|<
name|SalesforceSessionListener
argument_list|>
name|listeners
decl_stmt|;
DECL|field|accessToken
specifier|private
name|String
name|accessToken
decl_stmt|;
DECL|field|instanceUrl
specifier|private
name|String
name|instanceUrl
decl_stmt|;
DECL|method|SalesforceSession (HttpClient httpClient, SalesforceLoginConfig config)
specifier|public
name|SalesforceSession
parameter_list|(
name|HttpClient
name|httpClient
parameter_list|,
name|SalesforceLoginConfig
name|config
parameter_list|)
block|{
comment|// validate parameters
name|assertNotNull
argument_list|(
literal|"Null httpClient"
argument_list|,
name|httpClient
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Null SalesforceLoginConfig"
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Null loginUrl"
argument_list|,
name|config
operator|.
name|getLoginUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Null clientId"
argument_list|,
name|config
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Null clientSecret"
argument_list|,
name|config
operator|.
name|getClientSecret
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Null userName"
argument_list|,
name|config
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Null password"
argument_list|,
name|config
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|httpClient
operator|=
name|httpClient
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
comment|// strip trailing '/'
name|String
name|loginUrl
init|=
name|config
operator|.
name|getLoginUrl
argument_list|()
decl_stmt|;
name|config
operator|.
name|setLoginUrl
argument_list|(
name|loginUrl
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|?
name|loginUrl
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|loginUrl
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
else|:
name|loginUrl
argument_list|)
expr_stmt|;
name|this
operator|.
name|objectMapper
operator|=
operator|new
name|ObjectMapper
argument_list|()
expr_stmt|;
name|this
operator|.
name|listeners
operator|=
operator|new
name|CopyOnWriteArraySet
argument_list|<
name|SalesforceSessionListener
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|assertNotNull (String s, Object o)
specifier|private
name|void
name|assertNotNull
parameter_list|(
name|String
name|s
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|s
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|login (String oldToken)
specifier|public
specifier|synchronized
name|String
name|login
parameter_list|(
name|String
name|oldToken
parameter_list|)
throws|throws
name|SalesforceException
block|{
comment|// check if we need a new session
comment|// this way there's always a single valid session
if|if
condition|(
operator|(
name|accessToken
operator|==
literal|null
operator|)
operator|||
name|accessToken
operator|.
name|equals
argument_list|(
name|oldToken
argument_list|)
condition|)
block|{
comment|// try revoking the old access token before creating a new one
name|accessToken
operator|=
name|oldToken
expr_stmt|;
if|if
condition|(
name|accessToken
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|logout
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SalesforceException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error revoking old access token: "
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
name|accessToken
operator|=
literal|null
expr_stmt|;
block|}
comment|// login to Salesforce and get session id
specifier|final
name|StatusExceptionExchange
name|loginPost
init|=
operator|new
name|StatusExceptionExchange
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|loginPost
operator|.
name|setURL
argument_list|(
name|config
operator|.
name|getLoginUrl
argument_list|()
operator|+
name|OAUTH2_TOKEN_PATH
argument_list|)
expr_stmt|;
name|loginPost
operator|.
name|setMethod
argument_list|(
name|HttpMethods
operator|.
name|POST
argument_list|)
expr_stmt|;
name|loginPost
operator|.
name|setRequestContentType
argument_list|(
name|FORM_CONTENT_TYPE
argument_list|)
expr_stmt|;
specifier|final
name|UrlEncoded
name|nvps
init|=
operator|new
name|UrlEncoded
argument_list|()
decl_stmt|;
name|nvps
operator|.
name|put
argument_list|(
literal|"grant_type"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|nvps
operator|.
name|put
argument_list|(
literal|"client_id"
argument_list|,
name|config
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|nvps
operator|.
name|put
argument_list|(
literal|"client_secret"
argument_list|,
name|config
operator|.
name|getClientSecret
argument_list|()
argument_list|)
expr_stmt|;
name|nvps
operator|.
name|put
argument_list|(
literal|"username"
argument_list|,
name|config
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|nvps
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
name|config
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|nvps
operator|.
name|put
argument_list|(
literal|"format"
argument_list|,
literal|"json"
argument_list|)
expr_stmt|;
try|try
block|{
comment|// set form content
name|loginPost
operator|.
name|setRequestContent
argument_list|(
operator|new
name|ByteArrayBuffer
argument_list|(
name|nvps
operator|.
name|encode
argument_list|(
name|StringUtil
operator|.
name|__UTF8
argument_list|,
literal|true
argument_list|)
operator|.
name|getBytes
argument_list|(
name|StringUtil
operator|.
name|__UTF8
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|httpClient
operator|.
name|send
argument_list|(
name|loginPost
argument_list|)
expr_stmt|;
comment|// wait for the login to finish
specifier|final
name|int
name|exchangeState
init|=
name|loginPost
operator|.
name|waitForDone
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|exchangeState
condition|)
block|{
case|case
name|HttpExchange
operator|.
name|STATUS_COMPLETED
case|:
specifier|final
name|byte
index|[]
name|responseContent
init|=
name|loginPost
operator|.
name|getResponseContentBytes
argument_list|()
decl_stmt|;
specifier|final
name|int
name|responseStatus
init|=
name|loginPost
operator|.
name|getResponseStatus
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|responseStatus
condition|)
block|{
case|case
name|HttpStatus
operator|.
name|OK_200
case|:
comment|// parse the response to get token
name|LoginToken
name|token
init|=
name|objectMapper
operator|.
name|readValue
argument_list|(
name|responseContent
argument_list|,
name|LoginToken
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// don't log token or instance URL for security reasons
name|LOG
operator|.
name|info
argument_list|(
literal|"Login successful"
argument_list|)
expr_stmt|;
name|accessToken
operator|=
name|token
operator|.
name|getAccessToken
argument_list|()
expr_stmt|;
name|instanceUrl
operator|=
name|token
operator|.
name|getInstanceUrl
argument_list|()
expr_stmt|;
comment|// notify all listeners
for|for
control|(
name|SalesforceSessionListener
name|listener
range|:
name|listeners
control|)
block|{
try|try
block|{
name|listener
operator|.
name|onLogin
argument_list|(
name|accessToken
argument_list|,
name|instanceUrl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unexpected error from listener {}: {}"
argument_list|,
name|listener
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
break|break;
case|case
name|HttpStatus
operator|.
name|BAD_REQUEST_400
case|:
comment|// parse the response to get error
specifier|final
name|LoginError
name|error
init|=
name|objectMapper
operator|.
name|readValue
argument_list|(
name|responseContent
argument_list|,
name|LoginError
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Login error code:[%s] description:[%s]"
argument_list|,
name|error
operator|.
name|getError
argument_list|()
argument_list|,
name|error
operator|.
name|getErrorDescription
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|RestError
argument_list|>
name|errors
init|=
operator|new
name|ArrayList
argument_list|<
name|RestError
argument_list|>
argument_list|()
decl_stmt|;
name|errors
operator|.
name|add
argument_list|(
operator|new
name|RestError
argument_list|(
name|msg
argument_list|,
name|error
operator|.
name|getErrorDescription
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|errors
argument_list|,
name|HttpStatus
operator|.
name|BAD_REQUEST_400
argument_list|)
throw|;
default|default:
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Login error status:[%s] reason:[%s]"
argument_list|,
name|responseStatus
argument_list|,
name|loginPost
operator|.
name|getReason
argument_list|()
argument_list|)
argument_list|,
name|responseStatus
argument_list|)
throw|;
block|}
break|break;
case|case
name|HttpExchange
operator|.
name|STATUS_EXCEPTED
case|:
specifier|final
name|Throwable
name|ex
init|=
name|loginPost
operator|.
name|getException
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unexpected login exception: %s"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|ex
argument_list|)
throw|;
case|case
name|HttpExchange
operator|.
name|STATUS_CANCELLED
case|:
throw|throw
operator|new
name|SalesforceException
argument_list|(
literal|"Login request CANCELLED!"
argument_list|,
literal|null
argument_list|)
throw|;
case|case
name|HttpExchange
operator|.
name|STATUS_EXPIRED
case|:
throw|throw
operator|new
name|SalesforceException
argument_list|(
literal|"Login request TIMEOUT!"
argument_list|,
literal|null
argument_list|)
throw|;
block|}
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
literal|"Login error: unexpected exception "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Login error: unexpected exception "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|accessToken
return|;
block|}
DECL|method|logout ()
specifier|public
name|void
name|logout
parameter_list|()
throws|throws
name|SalesforceException
block|{
if|if
condition|(
name|accessToken
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|StatusExceptionExchange
name|logoutGet
init|=
operator|new
name|StatusExceptionExchange
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|logoutGet
operator|.
name|setURL
argument_list|(
name|config
operator|.
name|getLoginUrl
argument_list|()
operator|+
name|OAUTH2_REVOKE_PATH
operator|+
name|accessToken
argument_list|)
expr_stmt|;
name|logoutGet
operator|.
name|setMethod
argument_list|(
name|HttpMethods
operator|.
name|GET
argument_list|)
expr_stmt|;
try|try
block|{
name|httpClient
operator|.
name|send
argument_list|(
name|logoutGet
argument_list|)
expr_stmt|;
specifier|final
name|int
name|done
init|=
name|logoutGet
operator|.
name|waitForDone
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|done
condition|)
block|{
case|case
name|HttpExchange
operator|.
name|STATUS_COMPLETED
case|:
specifier|final
name|int
name|statusCode
init|=
name|logoutGet
operator|.
name|getResponseStatus
argument_list|()
decl_stmt|;
specifier|final
name|String
name|reason
init|=
name|logoutGet
operator|.
name|getReason
argument_list|()
decl_stmt|;
if|if
condition|(
name|statusCode
operator|==
name|HttpStatus
operator|.
name|OK_200
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Logout successful"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Logout error, code: [%s] reason: [%s]"
argument_list|,
name|statusCode
argument_list|,
name|reason
argument_list|)
argument_list|,
name|statusCode
argument_list|)
throw|;
block|}
break|break;
case|case
name|HttpExchange
operator|.
name|STATUS_EXCEPTED
case|:
specifier|final
name|Throwable
name|ex
init|=
name|logoutGet
operator|.
name|getException
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|SalesforceException
argument_list|(
literal|"Unexpected logout exception: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
case|case
name|HttpExchange
operator|.
name|STATUS_CANCELLED
case|:
throw|throw
operator|new
name|SalesforceException
argument_list|(
literal|"Logout request CANCELLED!"
argument_list|,
literal|null
argument_list|)
throw|;
case|case
name|HttpExchange
operator|.
name|STATUS_EXPIRED
case|:
throw|throw
operator|new
name|SalesforceException
argument_list|(
literal|"Logout request TIMEOUT!"
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|SalesforceException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Logout error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
comment|// reset session
name|accessToken
operator|=
literal|null
expr_stmt|;
name|instanceUrl
operator|=
literal|null
expr_stmt|;
comment|// notify all session listeners of the new access token and instance url
for|for
control|(
name|SalesforceSessionListener
name|listener
range|:
name|listeners
control|)
block|{
try|try
block|{
name|listener
operator|.
name|onLogout
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unexpected error from listener {}: {}"
argument_list|,
name|listener
argument_list|,
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|getAccessToken ()
specifier|public
name|String
name|getAccessToken
parameter_list|()
block|{
return|return
name|accessToken
return|;
block|}
DECL|method|getInstanceUrl ()
specifier|public
name|String
name|getInstanceUrl
parameter_list|()
block|{
return|return
name|instanceUrl
return|;
block|}
DECL|method|addListener (SalesforceSessionListener listener)
specifier|public
name|boolean
name|addListener
parameter_list|(
name|SalesforceSessionListener
name|listener
parameter_list|)
block|{
return|return
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
return|;
block|}
DECL|method|removeListener (SalesforceSessionListener listener)
specifier|public
name|boolean
name|removeListener
parameter_list|(
name|SalesforceSessionListener
name|listener
parameter_list|)
block|{
return|return
name|listeners
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
comment|// auto-login at start if needed
name|login
argument_list|(
name|accessToken
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
comment|// logout
name|logout
argument_list|()
expr_stmt|;
block|}
comment|/**      * Records status line, and exception from exchange.      *      * @author dbokde      */
DECL|class|StatusExceptionExchange
specifier|private
specifier|static
class|class
name|StatusExceptionExchange
extends|extends
name|ContentExchange
block|{
DECL|field|reason
specifier|private
name|String
name|reason
decl_stmt|;
DECL|field|exception
specifier|private
name|Throwable
name|exception
decl_stmt|;
DECL|method|StatusExceptionExchange (boolean cacheFields)
specifier|public
name|StatusExceptionExchange
parameter_list|(
name|boolean
name|cacheFields
parameter_list|)
block|{
name|super
argument_list|(
name|cacheFields
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onResponseStatus (Buffer version, int status, Buffer reason)
specifier|protected
specifier|synchronized
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
comment|// remember reason
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
block|}
annotation|@
name|Override
DECL|method|onConnectionFailed (Throwable x)
specifier|protected
name|void
name|onConnectionFailed
parameter_list|(
name|Throwable
name|x
parameter_list|)
block|{
name|this
operator|.
name|exception
operator|=
name|x
expr_stmt|;
name|super
operator|.
name|onConnectionFailed
argument_list|(
name|x
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onException (Throwable x)
specifier|protected
name|void
name|onException
parameter_list|(
name|Throwable
name|x
parameter_list|)
block|{
name|this
operator|.
name|exception
operator|=
name|x
expr_stmt|;
name|super
operator|.
name|onException
argument_list|(
name|x
argument_list|)
expr_stmt|;
block|}
DECL|method|getReason ()
specifier|public
name|String
name|getReason
parameter_list|()
block|{
return|return
name|reason
return|;
block|}
DECL|method|getException ()
specifier|public
name|Throwable
name|getException
parameter_list|()
block|{
return|return
name|exception
return|;
block|}
block|}
DECL|interface|SalesforceSessionListener
specifier|public
specifier|static
interface|interface
name|SalesforceSessionListener
block|{
DECL|method|onLogin (String accessToken, String instanceUrl)
name|void
name|onLogin
parameter_list|(
name|String
name|accessToken
parameter_list|,
name|String
name|instanceUrl
parameter_list|)
function_decl|;
DECL|method|onLogout ()
name|void
name|onLogout
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

