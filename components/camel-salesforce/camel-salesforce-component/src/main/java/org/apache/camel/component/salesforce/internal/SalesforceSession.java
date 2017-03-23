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
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Signature
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Clock
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
name|Base64
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
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
name|databind
operator|.
name|ObjectMapper
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
name|CamelContext
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
name|SalesforceLoginConfig
operator|.
name|Type
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
name|api
operator|.
name|utils
operator|.
name|JsonUtils
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|jsse
operator|.
name|KeyStoreParameters
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
name|HttpConversation
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
name|util
operator|.
name|FormContentProvider
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
name|eclipse
operator|.
name|jetty
operator|.
name|util
operator|.
name|Fields
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
DECL|field|JWT_SIGNATURE_ALGORITHM
specifier|private
specifier|static
specifier|final
name|String
name|JWT_SIGNATURE_ALGORITHM
init|=
literal|"SHA256withRSA"
decl_stmt|;
DECL|field|JWT_CLAIM_WINDOW
specifier|private
specifier|static
specifier|final
name|int
name|JWT_CLAIM_WINDOW
init|=
literal|270
decl_stmt|;
comment|// 4.5 min
DECL|field|JWT_HEADER
specifier|private
specifier|static
specifier|final
name|String
name|JWT_HEADER
init|=
name|Base64
operator|.
name|getUrlEncoder
argument_list|()
operator|.
name|encodeToString
argument_list|(
literal|"{\"alg\":\"RS256\"}"
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
decl_stmt|;
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
DECL|field|httpClient
specifier|private
specifier|final
name|SalesforceHttpClient
name|httpClient
decl_stmt|;
DECL|field|timeout
specifier|private
specifier|final
name|long
name|timeout
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
specifier|volatile
name|String
name|accessToken
decl_stmt|;
DECL|field|instanceUrl
specifier|private
specifier|volatile
name|String
name|instanceUrl
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|SalesforceSession (CamelContext camelContext, SalesforceHttpClient httpClient, long timeout, SalesforceLoginConfig config)
specifier|public
name|SalesforceSession
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|SalesforceHttpClient
name|httpClient
parameter_list|,
name|long
name|timeout
parameter_list|,
name|SalesforceLoginConfig
name|config
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
comment|// validate parameters
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|httpClient
argument_list|,
literal|"httpClient"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|config
argument_list|,
literal|"SalesforceLoginConfig"
argument_list|)
expr_stmt|;
name|config
operator|.
name|validate
argument_list|()
expr_stmt|;
name|this
operator|.
name|httpClient
operator|=
name|httpClient
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|timeout
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
name|JsonUtils
operator|.
name|createObjectMapper
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
name|Request
name|loginPost
init|=
name|getLoginRequest
argument_list|(
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
specifier|final
name|ContentResponse
name|loginResponse
init|=
name|loginPost
operator|.
name|send
argument_list|()
decl_stmt|;
name|parseLoginResponse
argument_list|(
name|loginResponse
argument_list|,
name|loginResponse
operator|.
name|getContentAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SalesforceException
argument_list|(
literal|"Login error: "
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
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SalesforceException
argument_list|(
literal|"Login request timeout: "
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
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SalesforceException
argument_list|(
literal|"Unexpected login error: "
operator|+
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|accessToken
return|;
block|}
comment|/**      * Creates login request, allows SalesforceSecurityHandler to create a login request for a failed authentication      * conversation      *       * @return login POST request.      */
DECL|method|getLoginRequest (HttpConversation conversation)
specifier|public
name|Request
name|getLoginRequest
parameter_list|(
name|HttpConversation
name|conversation
parameter_list|)
block|{
specifier|final
name|String
name|loginUrl
init|=
operator|(
name|instanceUrl
operator|==
literal|null
condition|?
name|config
operator|.
name|getLoginUrl
argument_list|()
else|:
name|instanceUrl
operator|)
operator|+
name|OAUTH2_TOKEN_PATH
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Login at Salesforce loginUrl: {}"
argument_list|,
name|loginUrl
argument_list|)
expr_stmt|;
specifier|final
name|Fields
name|fields
init|=
operator|new
name|Fields
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|fields
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
name|fields
operator|.
name|put
argument_list|(
literal|"format"
argument_list|,
literal|"json"
argument_list|)
expr_stmt|;
specifier|final
name|Type
name|type
init|=
name|config
operator|.
name|getType
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|USERNAME_PASSWORD
case|:
name|fields
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
name|fields
operator|.
name|put
argument_list|(
literal|"grant_type"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|fields
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
name|fields
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
break|break;
case|case
name|REFRESH_TOKEN
case|:
name|fields
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
name|fields
operator|.
name|put
argument_list|(
literal|"grant_type"
argument_list|,
literal|"refresh_token"
argument_list|)
expr_stmt|;
name|fields
operator|.
name|put
argument_list|(
literal|"refresh_token"
argument_list|,
name|config
operator|.
name|getRefreshToken
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|JWT
case|:
name|fields
operator|.
name|put
argument_list|(
literal|"grant_type"
argument_list|,
literal|"urn:ietf:params:oauth:grant-type:jwt-bearer"
argument_list|)
expr_stmt|;
name|fields
operator|.
name|put
argument_list|(
literal|"assertion"
argument_list|,
name|generateJwtAssertion
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported login configuration type: "
operator|+
name|type
argument_list|)
throw|;
block|}
specifier|final
name|Request
name|post
decl_stmt|;
if|if
condition|(
name|conversation
operator|==
literal|null
condition|)
block|{
name|post
operator|=
name|httpClient
operator|.
name|POST
argument_list|(
name|loginUrl
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|post
operator|=
name|httpClient
operator|.
name|newHttpRequest
argument_list|(
name|conversation
argument_list|,
name|URI
operator|.
name|create
argument_list|(
name|loginUrl
argument_list|)
argument_list|)
operator|.
name|method
argument_list|(
name|HttpMethod
operator|.
name|POST
argument_list|)
expr_stmt|;
block|}
return|return
name|post
operator|.
name|content
argument_list|(
operator|new
name|FormContentProvider
argument_list|(
name|fields
argument_list|)
argument_list|)
operator|.
name|timeout
argument_list|(
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
return|;
block|}
DECL|method|generateJwtAssertion ()
name|String
name|generateJwtAssertion
parameter_list|()
block|{
specifier|final
name|long
name|utcPlusWindow
init|=
name|Clock
operator|.
name|systemUTC
argument_list|()
operator|.
name|millis
argument_list|()
operator|/
literal|1000
operator|+
name|JWT_CLAIM_WINDOW
decl_stmt|;
specifier|final
name|StringBuilder
name|claim
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|"{\"iss\":\""
argument_list|)
operator|.
name|append
argument_list|(
name|config
operator|.
name|getClientId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\",\"sub\":\""
argument_list|)
operator|.
name|append
argument_list|(
name|config
operator|.
name|getUserName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\",\"aud\":\""
argument_list|)
operator|.
name|append
argument_list|(
name|config
operator|.
name|getLoginUrl
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\",\"exp\":\""
argument_list|)
operator|.
name|append
argument_list|(
name|utcPlusWindow
argument_list|)
operator|.
name|append
argument_list|(
literal|"\"}"
argument_list|)
decl_stmt|;
specifier|final
name|StringBuilder
name|token
init|=
operator|new
name|StringBuilder
argument_list|(
name|JWT_HEADER
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|Base64
operator|.
name|getUrlEncoder
argument_list|()
operator|.
name|encodeToString
argument_list|(
name|claim
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|KeyStoreParameters
name|keyStoreParameters
init|=
name|config
operator|.
name|getKeystore
argument_list|()
decl_stmt|;
name|keyStoreParameters
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|KeyStore
name|keystore
init|=
name|keyStoreParameters
operator|.
name|createKeyStore
argument_list|()
decl_stmt|;
specifier|final
name|Enumeration
argument_list|<
name|String
argument_list|>
name|aliases
init|=
name|keystore
operator|.
name|aliases
argument_list|()
decl_stmt|;
name|String
name|alias
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|aliases
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|tmp
init|=
name|aliases
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|keystore
operator|.
name|isKeyEntry
argument_list|(
name|tmp
argument_list|)
condition|)
block|{
if|if
condition|(
name|alias
operator|==
literal|null
condition|)
block|{
name|alias
operator|=
name|tmp
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The given keystore `"
operator|+
name|keyStoreParameters
operator|.
name|getResource
argument_list|()
operator|+
literal|"` contains more than one key entry, expecting only one"
argument_list|)
throw|;
block|}
block|}
block|}
name|PrivateKey
name|key
init|=
operator|(
name|PrivateKey
operator|)
name|keystore
operator|.
name|getKey
argument_list|(
name|alias
argument_list|,
name|keyStoreParameters
operator|.
name|getPassword
argument_list|()
operator|.
name|toCharArray
argument_list|()
argument_list|)
decl_stmt|;
name|Signature
name|signature
init|=
name|Signature
operator|.
name|getInstance
argument_list|(
name|JWT_SIGNATURE_ALGORITHM
argument_list|)
decl_stmt|;
name|signature
operator|.
name|initSign
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|signature
operator|.
name|update
argument_list|(
name|token
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|signed
init|=
name|signature
operator|.
name|sign
argument_list|()
decl_stmt|;
name|token
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|Base64
operator|.
name|getUrlEncoder
argument_list|()
operator|.
name|encodeToString
argument_list|(
name|signed
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|GeneralSecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|token
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Parses login response, allows SalesforceSecurityHandler to parse a login request for a failed authentication      * conversation.      */
DECL|method|parseLoginResponse (ContentResponse loginResponse, String responseContent)
specifier|public
specifier|synchronized
name|void
name|parseLoginResponse
parameter_list|(
name|ContentResponse
name|loginResponse
parameter_list|,
name|String
name|responseContent
parameter_list|)
throws|throws
name|SalesforceException
block|{
specifier|final
name|int
name|responseStatus
init|=
name|loginResponse
operator|.
name|getStatus
argument_list|()
decl_stmt|;
try|try
block|{
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
comment|// notify all session listeners
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
name|errorCode
init|=
name|error
operator|.
name|getError
argument_list|()
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
name|errorCode
argument_list|,
name|msg
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
name|loginResponse
operator|.
name|getReason
argument_list|()
argument_list|)
argument_list|,
name|responseStatus
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
literal|"Login error: response parse exception "
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
DECL|method|logout ()
specifier|public
specifier|synchronized
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
try|try
block|{
name|String
name|logoutUrl
init|=
operator|(
name|instanceUrl
operator|==
literal|null
condition|?
name|config
operator|.
name|getLoginUrl
argument_list|()
else|:
name|instanceUrl
operator|)
operator|+
name|OAUTH2_REVOKE_PATH
operator|+
name|accessToken
decl_stmt|;
specifier|final
name|Request
name|logoutGet
init|=
name|httpClient
operator|.
name|newRequest
argument_list|(
name|logoutUrl
argument_list|)
operator|.
name|timeout
argument_list|(
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
specifier|final
name|ContentResponse
name|logoutResponse
init|=
name|logoutGet
operator|.
name|send
argument_list|()
decl_stmt|;
specifier|final
name|int
name|statusCode
init|=
name|logoutResponse
operator|.
name|getStatus
argument_list|()
decl_stmt|;
specifier|final
name|String
name|reason
init|=
name|logoutResponse
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
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
specifier|final
name|Throwable
name|ex
init|=
name|e
operator|.
name|getCause
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
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
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
comment|// notify all session listeners about logout
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
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|interface|SalesforceSessionListener
specifier|public
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

