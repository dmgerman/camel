begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.box.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|box
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Proxy
import|;
end_import

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
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SecureRandom
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
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxAPIConnection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxAPIException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxDeveloperEditionAPIConnection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|IAccessTokenCache
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|InMemoryLRUAccessTokenCache
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|JWTEncryptionPreferences
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
name|component
operator|.
name|box
operator|.
name|BoxConfiguration
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
name|HttpHost
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsoup
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsoup
operator|.
name|Jsoup
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsoup
operator|.
name|nodes
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsoup
operator|.
name|nodes
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsoup
operator|.
name|nodes
operator|.
name|FormElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsoup
operator|.
name|select
operator|.
name|Elements
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
comment|/**  * BoxConnectionHelper  *  *<p>  * Utility class for creating Box API Connections  */
end_comment

begin_class
DECL|class|BoxConnectionHelper
specifier|public
specifier|final
class|class
name|BoxConnectionHelper
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
name|BoxConnectionHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|QUERY_PARAM_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|QUERY_PARAM_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"&?([^=]+)=([^&]+)"
argument_list|)
decl_stmt|;
DECL|method|BoxConnectionHelper ()
specifier|private
name|BoxConnectionHelper
parameter_list|()
block|{
comment|// hide utility class constructor
block|}
DECL|method|createConnection (final BoxConfiguration configuration)
specifier|public
specifier|static
name|BoxAPIConnection
name|createConnection
parameter_list|(
specifier|final
name|BoxConfiguration
name|configuration
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|.
name|getAuthenticationType
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Box API connection failed: Authentication type not specified in configuration"
argument_list|)
throw|;
block|}
switch|switch
condition|(
name|configuration
operator|.
name|getAuthenticationType
argument_list|()
condition|)
block|{
case|case
name|BoxConfiguration
operator|.
name|APP_ENTERPRISE_AUTHENTICATION
case|:
return|return
name|createAppEnterpriseAuthenticatedConnection
argument_list|(
name|configuration
argument_list|)
return|;
case|case
name|BoxConfiguration
operator|.
name|APP_USER_AUTHENTICATION
case|:
return|return
name|createAppUserAuthenticatedConnection
argument_list|(
name|configuration
argument_list|)
return|;
case|case
name|BoxConfiguration
operator|.
name|STANDARD_AUTHENTICATION
case|:
return|return
name|createStandardAuthenticatedConnection
argument_list|(
name|configuration
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Box API connection failed: Invalid authentication type '%s'"
argument_list|,
name|configuration
operator|.
name|getAuthenticationType
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|createStandardAuthenticatedConnection (BoxConfiguration configuration)
specifier|public
specifier|static
name|BoxAPIConnection
name|createStandardAuthenticatedConnection
parameter_list|(
name|BoxConfiguration
name|configuration
parameter_list|)
block|{
comment|// authorize application on user's behalf
try|try
block|{
comment|//prepare proxy parameter
specifier|final
name|Proxy
name|proxy
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpParams
init|=
name|configuration
operator|.
name|getHttpParams
argument_list|()
decl_stmt|;
if|if
condition|(
name|httpParams
operator|!=
literal|null
operator|&&
name|httpParams
operator|.
name|get
argument_list|(
literal|"http.route.default-proxy"
argument_list|)
operator|!=
literal|null
condition|)
block|{
specifier|final
name|HttpHost
name|proxyHost
init|=
operator|(
name|HttpHost
operator|)
name|httpParams
operator|.
name|get
argument_list|(
literal|"http.route.default-proxy"
argument_list|)
decl_stmt|;
specifier|final
name|Boolean
name|socksProxy
init|=
operator|(
name|Boolean
operator|)
name|httpParams
operator|.
name|get
argument_list|(
literal|"http.route.socks-proxy"
argument_list|)
decl_stmt|;
name|SocketAddress
name|proxyAddr
init|=
operator|new
name|InetSocketAddress
argument_list|(
name|proxyHost
operator|.
name|getHostName
argument_list|()
argument_list|,
name|proxyHost
operator|.
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|socksProxy
operator|!=
literal|null
operator|&&
name|socksProxy
condition|)
block|{
name|proxy
operator|=
operator|new
name|Proxy
argument_list|(
name|Proxy
operator|.
name|Type
operator|.
name|SOCKS
argument_list|,
name|proxyAddr
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|proxy
operator|=
operator|new
name|Proxy
argument_list|(
name|Proxy
operator|.
name|Type
operator|.
name|HTTP
argument_list|,
name|proxyAddr
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|proxy
operator|=
literal|null
expr_stmt|;
block|}
comment|// generate anti-forgery token to prevent/detect CSRF attack
specifier|final
name|String
name|csrfToken
init|=
name|String
operator|.
name|valueOf
argument_list|(
operator|new
name|SecureRandom
argument_list|()
operator|.
name|nextLong
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|String
name|authorizeUrl
init|=
name|authorizationUrl
argument_list|(
name|configuration
operator|.
name|getClientId
argument_list|()
argument_list|,
name|csrfToken
argument_list|)
decl_stmt|;
comment|//load loginPage
specifier|final
name|Connection
operator|.
name|Response
name|loginPageResponse
init|=
name|addProxy
argument_list|(
name|Jsoup
operator|.
name|connect
argument_list|(
name|authorizeUrl
argument_list|)
argument_list|,
name|proxy
argument_list|)
operator|.
name|method
argument_list|(
name|Connection
operator|.
name|Method
operator|.
name|GET
argument_list|)
operator|.
name|execute
argument_list|()
decl_stmt|;
specifier|final
name|Document
name|loginPage
init|=
name|loginPageResponse
operator|.
name|parse
argument_list|()
decl_stmt|;
name|validatePage
argument_list|(
name|loginPage
argument_list|)
expr_stmt|;
comment|//fill login form
specifier|final
name|FormElement
name|loginForm
init|=
operator|(
name|FormElement
operator|)
name|loginPage
operator|.
name|select
argument_list|(
literal|"form[name=login_form]"
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
specifier|final
name|Element
name|loginField
init|=
name|loginForm
operator|.
name|select
argument_list|(
literal|"input[name=login]"
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
name|loginField
operator|.
name|val
argument_list|(
name|configuration
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Element
name|passwordField
init|=
name|loginForm
operator|.
name|select
argument_list|(
literal|"input[name=password]"
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
name|passwordField
operator|.
name|val
argument_list|(
name|configuration
operator|.
name|getUserPassword
argument_list|()
argument_list|)
expr_stmt|;
comment|//submit loginPage
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cookies
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|cookies
operator|.
name|putAll
argument_list|(
name|loginPageResponse
operator|.
name|cookies
argument_list|()
argument_list|)
expr_stmt|;
name|Connection
operator|.
name|Response
name|response
init|=
name|addProxy
argument_list|(
name|loginForm
operator|.
name|submit
argument_list|()
argument_list|,
name|proxy
argument_list|)
operator|.
name|cookies
argument_list|(
name|cookies
argument_list|)
operator|.
name|execute
argument_list|()
decl_stmt|;
name|cookies
operator|.
name|putAll
argument_list|(
name|response
operator|.
name|cookies
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Document
name|consentPage
init|=
name|response
operator|.
name|parse
argument_list|()
decl_stmt|;
comment|//possible invalid credentials error
name|validatePage
argument_list|(
name|consentPage
argument_list|)
expr_stmt|;
specifier|final
name|FormElement
name|consentForm
init|=
operator|(
name|FormElement
operator|)
name|consentPage
operator|.
name|select
argument_list|(
literal|"form[name=consent_form]"
argument_list|)
operator|.
name|first
argument_list|()
decl_stmt|;
comment|//remove reject input
name|consentForm
operator|.
name|elements
argument_list|()
operator|.
name|removeIf
argument_list|(
name|e
lambda|->
name|e
operator|.
name|attr
argument_list|(
literal|"name"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"consent_reject"
argument_list|)
argument_list|)
expr_stmt|;
comment|//parse request_token from javascript from head, it is the first script in the header
specifier|final
name|String
name|requestTokenScript
init|=
name|consentPage
operator|.
name|select
argument_list|(
literal|"script"
argument_list|)
operator|.
name|first
argument_list|()
operator|.
name|html
argument_list|()
decl_stmt|;
specifier|final
name|Matcher
name|m
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"var\\s+request_token\\s+=\\s+'([^'].+)'.*"
argument_list|)
operator|.
name|matcher
argument_list|(
name|requestTokenScript
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|find
argument_list|()
condition|)
block|{
specifier|final
name|String
name|requestToken
init|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|response
operator|=
name|addProxy
argument_list|(
name|consentForm
operator|.
name|submit
argument_list|()
argument_list|,
name|proxy
argument_list|)
operator|.
name|data
argument_list|(
literal|"request_token"
argument_list|,
name|requestToken
argument_list|)
operator|.
name|followRedirects
argument_list|(
literal|false
argument_list|)
operator|.
name|cookies
argument_list|(
name|cookies
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error authorizing application: Can not parse request token."
argument_list|)
throw|;
block|}
specifier|final
name|String
name|location
init|=
name|response
operator|.
name|header
argument_list|(
literal|"Location"
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Matcher
name|matcher
init|=
name|QUERY_PARAM_PATTERN
operator|.
name|matcher
argument_list|(
operator|new
name|URL
argument_list|(
name|location
argument_list|)
operator|.
name|getQuery
argument_list|()
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
name|params
operator|.
name|put
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|,
name|matcher
operator|.
name|group
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|state
init|=
name|params
operator|.
name|get
argument_list|(
literal|"state"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|csrfToken
operator|.
name|equals
argument_list|(
name|state
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Invalid CSRF code!"
argument_list|)
throw|;
block|}
else|else
block|{
comment|// get authorization code
specifier|final
name|String
name|authorizationCode
init|=
name|params
operator|.
name|get
argument_list|(
literal|"code"
argument_list|)
decl_stmt|;
return|return
operator|new
name|BoxAPIConnection
argument_list|(
name|configuration
operator|.
name|getClientId
argument_list|()
argument_list|,
name|configuration
operator|.
name|getClientSecret
argument_list|()
argument_list|,
name|authorizationCode
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|BoxAPIException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Box API connection failed: API returned the error code %d\n\n%s"
argument_list|,
name|e
operator|.
name|getResponseCode
argument_list|()
argument_list|,
name|e
operator|.
name|getResponse
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Box API connection failed: %s"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Validation of page:      * - detects invalid credentials error      * - detects wrong clientId error      */
DECL|method|validatePage (Document page)
specifier|private
specifier|static
name|void
name|validatePage
parameter_list|(
name|Document
name|page
parameter_list|)
block|{
name|Elements
name|errorDivs
init|=
name|page
operator|.
name|select
argument_list|(
literal|"div[class*=error_message]"
argument_list|)
decl_stmt|;
name|String
name|errorMessage
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|errorDivs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|errorMessage
operator|=
name|errorDivs
operator|.
name|first
argument_list|()
operator|.
name|text
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"\\s+"
argument_list|,
literal|" "
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|" Show Error Details"
argument_list|,
literal|":"
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|errorDivs
operator|=
name|page
operator|.
name|select
argument_list|(
literal|"div[class*=message]"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|errorDivs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|errorMessage
operator|=
name|errorDivs
operator|.
name|first
argument_list|()
operator|.
name|text
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|errorDivs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Error authorizing application: "
operator|+
name|errorMessage
argument_list|)
throw|;
block|}
block|}
comment|/**      * Helper method to add proxy into JSoup connection      */
DECL|method|addProxy (Connection connection, Proxy proxy)
specifier|private
specifier|static
name|Connection
name|addProxy
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|Proxy
name|proxy
parameter_list|)
block|{
if|if
condition|(
name|proxy
operator|!=
literal|null
condition|)
block|{
return|return
name|connection
operator|.
name|proxy
argument_list|(
name|proxy
argument_list|)
return|;
block|}
return|return
name|connection
return|;
block|}
DECL|method|createAppUserAuthenticatedConnection (BoxConfiguration configuration)
specifier|public
specifier|static
name|BoxAPIConnection
name|createAppUserAuthenticatedConnection
parameter_list|(
name|BoxConfiguration
name|configuration
parameter_list|)
block|{
comment|// Create Encryption Preferences
name|JWTEncryptionPreferences
name|encryptionPref
init|=
operator|new
name|JWTEncryptionPreferences
argument_list|()
decl_stmt|;
name|encryptionPref
operator|.
name|setPublicKeyID
argument_list|(
name|configuration
operator|.
name|getPublicKeyId
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|encryptionPref
operator|.
name|setPrivateKey
argument_list|(
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|configuration
operator|.
name|getPrivateKeyFile
argument_list|()
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Box API connection failed: could not read privateKeyFile"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|encryptionPref
operator|.
name|setPrivateKeyPassword
argument_list|(
name|configuration
operator|.
name|getPrivateKeyPassword
argument_list|()
argument_list|)
expr_stmt|;
name|encryptionPref
operator|.
name|setEncryptionAlgorithm
argument_list|(
name|configuration
operator|.
name|getEncryptionAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|IAccessTokenCache
name|accessTokenCache
init|=
name|configuration
operator|.
name|getAccessTokenCache
argument_list|()
decl_stmt|;
if|if
condition|(
name|accessTokenCache
operator|==
literal|null
condition|)
block|{
name|accessTokenCache
operator|=
operator|new
name|InMemoryLRUAccessTokenCache
argument_list|(
name|configuration
operator|.
name|getMaxCacheEntries
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
return|return
name|BoxDeveloperEditionAPIConnection
operator|.
name|getAppUserConnection
argument_list|(
name|configuration
operator|.
name|getUserId
argument_list|()
argument_list|,
name|configuration
operator|.
name|getClientId
argument_list|()
argument_list|,
name|configuration
operator|.
name|getClientSecret
argument_list|()
argument_list|,
name|encryptionPref
argument_list|,
name|accessTokenCache
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|BoxAPIException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Box API connection failed: API returned the error code %d\n\n%s"
argument_list|,
name|e
operator|.
name|getResponseCode
argument_list|()
argument_list|,
name|e
operator|.
name|getResponse
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|createAppEnterpriseAuthenticatedConnection (BoxConfiguration configuration)
specifier|public
specifier|static
name|BoxAPIConnection
name|createAppEnterpriseAuthenticatedConnection
parameter_list|(
name|BoxConfiguration
name|configuration
parameter_list|)
block|{
comment|// Create Encryption Preferences
name|JWTEncryptionPreferences
name|encryptionPref
init|=
operator|new
name|JWTEncryptionPreferences
argument_list|()
decl_stmt|;
name|encryptionPref
operator|.
name|setPublicKeyID
argument_list|(
name|configuration
operator|.
name|getPublicKeyId
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|encryptionPref
operator|.
name|setPrivateKey
argument_list|(
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|configuration
operator|.
name|getPrivateKeyFile
argument_list|()
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Box API connection failed: could not read privateKeyFile"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|encryptionPref
operator|.
name|setPrivateKeyPassword
argument_list|(
name|configuration
operator|.
name|getPrivateKeyPassword
argument_list|()
argument_list|)
expr_stmt|;
name|encryptionPref
operator|.
name|setEncryptionAlgorithm
argument_list|(
name|configuration
operator|.
name|getEncryptionAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|IAccessTokenCache
name|accessTokenCache
init|=
name|configuration
operator|.
name|getAccessTokenCache
argument_list|()
decl_stmt|;
if|if
condition|(
name|accessTokenCache
operator|==
literal|null
condition|)
block|{
name|accessTokenCache
operator|=
operator|new
name|InMemoryLRUAccessTokenCache
argument_list|(
name|configuration
operator|.
name|getMaxCacheEntries
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
return|return
name|BoxDeveloperEditionAPIConnection
operator|.
name|getAppEnterpriseConnection
argument_list|(
name|configuration
operator|.
name|getEnterpriseId
argument_list|()
argument_list|,
name|configuration
operator|.
name|getClientId
argument_list|()
argument_list|,
name|configuration
operator|.
name|getClientSecret
argument_list|()
argument_list|,
name|encryptionPref
argument_list|,
name|accessTokenCache
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|BoxAPIException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Box API connection failed: API returned the error code %d\n\n%s"
argument_list|,
name|e
operator|.
name|getResponseCode
argument_list|()
argument_list|,
name|e
operator|.
name|getResponse
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|authorizationUrl (String clientId, String stateToken)
specifier|public
specifier|static
name|String
name|authorizationUrl
parameter_list|(
name|String
name|clientId
parameter_list|,
name|String
name|stateToken
parameter_list|)
block|{
return|return
literal|"https://account.box.com/api/oauth2/authorize?response_type=code&redirect_url=https%3A%2F%2Flocalhost%2F&client_id="
operator|+
name|clientId
operator|+
literal|"&state="
operator|+
name|stateToken
return|;
block|}
block|}
end_class

end_unit

