begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|IOException
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
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|boxjavalibv2
operator|.
name|BoxClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|boxjavalibv2
operator|.
name|authorization
operator|.
name|IAuthFlowListener
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|boxjavalibv2
operator|.
name|authorization
operator|.
name|IAuthFlowUI
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|boxjavalibv2
operator|.
name|authorization
operator|.
name|OAuthDataMessage
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|boxjavalibv2
operator|.
name|authorization
operator|.
name|OAuthWebViewData
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|boxjavalibv2
operator|.
name|dao
operator|.
name|BoxOAuthToken
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|boxjavalibv2
operator|.
name|events
operator|.
name|OAuthEvent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|boxjavalibv2
operator|.
name|resourcemanagers
operator|.
name|IBoxOAuthManager
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|BrowserVersion
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|FailingHttpStatusCodeException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|Page
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|ProxyConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|WebClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|WebClientOptions
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|html
operator|.
name|HtmlButton
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|html
operator|.
name|HtmlForm
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|html
operator|.
name|HtmlPage
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|html
operator|.
name|HtmlPasswordInput
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|html
operator|.
name|HtmlSubmitInput
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|html
operator|.
name|HtmlTextInput
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
name|SSLContextParameters
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
name|apache
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
name|apache
operator|.
name|http
operator|.
name|conn
operator|.
name|params
operator|.
name|ConnRoutePNames
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
comment|/** * HtmlUnit based OAuth2 implementation of {@link IAuthFlowUI} */
end_comment

begin_class
DECL|class|LoginAuthFlowUI
specifier|public
specifier|final
class|class
name|LoginAuthFlowUI
implements|implements
name|IAuthFlowUI
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
name|LoginAuthFlowUI
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
DECL|field|configuration
specifier|private
specifier|final
name|BoxConfiguration
name|configuration
decl_stmt|;
DECL|field|boxClient
specifier|private
specifier|final
name|BoxClient
name|boxClient
decl_stmt|;
DECL|field|listener
specifier|private
name|IAuthFlowListener
name|listener
decl_stmt|;
DECL|method|LoginAuthFlowUI (BoxConfiguration configuration, BoxClient boxClient)
specifier|public
name|LoginAuthFlowUI
parameter_list|(
name|BoxConfiguration
name|configuration
parameter_list|,
name|BoxClient
name|boxClient
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|boxClient
operator|=
name|boxClient
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
annotation|@
name|Override
DECL|method|authenticate (IAuthFlowListener listener)
specifier|public
name|void
name|authenticate
parameter_list|(
name|IAuthFlowListener
name|listener
parameter_list|)
block|{
comment|// TODO run this on an Executor to make it async
comment|// create HtmlUnit client
specifier|final
name|WebClient
name|webClient
init|=
operator|new
name|WebClient
argument_list|(
name|BrowserVersion
operator|.
name|FIREFOX_24
argument_list|)
decl_stmt|;
specifier|final
name|WebClientOptions
name|options
init|=
name|webClient
operator|.
name|getOptions
argument_list|()
decl_stmt|;
name|options
operator|.
name|setRedirectEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|options
operator|.
name|setJavaScriptEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|options
operator|.
name|setThrowExceptionOnFailingStatusCode
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|options
operator|.
name|setThrowExceptionOnScriptError
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|options
operator|.
name|setPrintContentOnFailingStatusCode
argument_list|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
comment|// use default SSP to create supported non-SSL protocols list
specifier|final
name|SSLContext
name|sslContext
init|=
operator|new
name|SSLContextParameters
argument_list|()
operator|.
name|createSSLContext
argument_list|()
decl_stmt|;
name|options
operator|.
name|setSSLClientProtocols
argument_list|(
name|sslContext
operator|.
name|createSSLEngine
argument_list|()
operator|.
name|getEnabledProtocols
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
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
catch|catch
parameter_list|(
name|IOException
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
comment|// add HTTP proxy if set
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
name|ConnRoutePNames
operator|.
name|DEFAULT_PROXY
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
name|ConnRoutePNames
operator|.
name|DEFAULT_PROXY
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
specifier|final
name|ProxyConfig
name|proxyConfig
init|=
operator|new
name|ProxyConfig
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
argument_list|,
name|socksProxy
operator|!=
literal|null
condition|?
name|socksProxy
else|:
literal|false
argument_list|)
decl_stmt|;
name|options
operator|.
name|setProxyConfig
argument_list|(
name|proxyConfig
argument_list|)
expr_stmt|;
block|}
comment|// authorize application on user's behalf
try|try
block|{
specifier|final
name|String
name|csrfId
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
name|OAuthWebViewData
name|viewData
init|=
operator|new
name|OAuthWebViewData
argument_list|(
name|boxClient
operator|.
name|getOAuthDataController
argument_list|()
argument_list|)
decl_stmt|;
name|viewData
operator|.
name|setOptionalState
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|csrfId
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|HtmlPage
name|authPage
init|=
name|webClient
operator|.
name|getPage
argument_list|(
name|viewData
operator|.
name|buildUrl
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
comment|// submit login credentials
specifier|final
name|HtmlForm
name|loginForm
init|=
name|authPage
operator|.
name|getFormByName
argument_list|(
literal|"login_form"
argument_list|)
decl_stmt|;
specifier|final
name|HtmlTextInput
name|login
init|=
name|loginForm
operator|.
name|getInputByName
argument_list|(
literal|"login"
argument_list|)
decl_stmt|;
name|login
operator|.
name|setText
argument_list|(
name|configuration
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|HtmlPasswordInput
name|password
init|=
name|loginForm
operator|.
name|getInputByName
argument_list|(
literal|"password"
argument_list|)
decl_stmt|;
name|password
operator|.
name|setText
argument_list|(
name|configuration
operator|.
name|getUserPassword
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|HtmlSubmitInput
name|submitInput
init|=
name|loginForm
operator|.
name|getInputByName
argument_list|(
literal|"login_submit"
argument_list|)
decl_stmt|;
comment|// submit consent
specifier|final
name|HtmlPage
name|consentPage
init|=
name|submitInput
operator|.
name|click
argument_list|()
decl_stmt|;
specifier|final
name|HtmlForm
name|consentForm
init|=
name|consentPage
operator|.
name|getFormByName
argument_list|(
literal|"consent_form"
argument_list|)
decl_stmt|;
specifier|final
name|HtmlButton
name|consentAccept
init|=
name|consentForm
operator|.
name|getButtonByName
argument_list|(
literal|"consent_accept"
argument_list|)
decl_stmt|;
comment|// disable redirect to avoid loading redirect URL
name|webClient
operator|.
name|getOptions
argument_list|()
operator|.
name|setRedirectEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// validate CSRF and get authorization code
name|String
name|redirectQuery
decl_stmt|;
try|try
block|{
specifier|final
name|Page
name|redirectPage
init|=
name|consentAccept
operator|.
name|click
argument_list|()
decl_stmt|;
name|redirectQuery
operator|=
name|redirectPage
operator|.
name|getUrl
argument_list|()
operator|.
name|getQuery
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FailingHttpStatusCodeException
name|e
parameter_list|)
block|{
comment|// escalate non redirect errors
if|if
condition|(
name|e
operator|.
name|getStatusCode
argument_list|()
operator|!=
name|HttpStatus
operator|.
name|SC_MOVED_TEMPORARILY
condition|)
block|{
throw|throw
name|e
throw|;
block|}
specifier|final
name|String
name|location
init|=
name|e
operator|.
name|getResponse
argument_list|()
operator|.
name|getResponseHeaderValue
argument_list|(
literal|"Location"
argument_list|)
decl_stmt|;
name|redirectQuery
operator|=
name|location
operator|.
name|substring
argument_list|(
name|location
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
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
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
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
name|redirectQuery
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
name|csrfId
operator|.
name|equals
argument_list|(
name|state
argument_list|)
condition|)
block|{
specifier|final
name|SecurityException
name|e
init|=
operator|new
name|SecurityException
argument_list|(
literal|"Invalid CSRF code!"
argument_list|)
decl_stmt|;
name|listener
operator|.
name|onAuthFlowException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|this
operator|.
name|listener
operator|.
name|onAuthFlowException
argument_list|(
name|e
argument_list|)
expr_stmt|;
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
comment|// get OAuth token
specifier|final
name|IBoxOAuthManager
name|oAuthManager
init|=
name|boxClient
operator|.
name|getOAuthManager
argument_list|()
decl_stmt|;
specifier|final
name|BoxOAuthToken
name|oAuthToken
init|=
name|oAuthManager
operator|.
name|createOAuth
argument_list|(
name|authorizationCode
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
literal|null
argument_list|)
decl_stmt|;
comment|// send initial token to BoxClient and this.listener
specifier|final
name|OAuthDataMessage
name|authDataMessage
init|=
operator|new
name|OAuthDataMessage
argument_list|(
name|oAuthToken
argument_list|,
name|boxClient
operator|.
name|getJSONParser
argument_list|()
argument_list|,
name|boxClient
operator|.
name|getResourceHub
argument_list|()
argument_list|)
decl_stmt|;
name|listener
operator|.
name|onAuthFlowEvent
argument_list|(
name|OAuthEvent
operator|.
name|OAUTH_CREATED
argument_list|,
name|authDataMessage
argument_list|)
expr_stmt|;
name|this
operator|.
name|listener
operator|.
name|onAuthFlowEvent
argument_list|(
name|OAuthEvent
operator|.
name|OAUTH_CREATED
argument_list|,
name|authDataMessage
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// forward login exceptions to listener
name|listener
operator|.
name|onAuthFlowException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|this
operator|.
name|listener
operator|.
name|onAuthFlowException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|addAuthFlowListener (IAuthFlowListener listener)
specifier|public
name|void
name|addAuthFlowListener
parameter_list|(
name|IAuthFlowListener
name|listener
parameter_list|)
block|{
name|this
operator|.
name|listener
operator|=
name|listener
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|initializeAuthFlow (Object applicationContext, String clientId, String clientSecret)
specifier|public
name|void
name|initializeAuthFlow
parameter_list|(
name|Object
name|applicationContext
parameter_list|,
name|String
name|clientId
parameter_list|,
name|String
name|clientSecret
parameter_list|)
block|{
comment|// unknown usage
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"initializeAuthFlow"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|initializeAuthFlow (Object applicationContext, String clientId, String clientSecret, String redirectUrl)
specifier|public
name|void
name|initializeAuthFlow
parameter_list|(
name|Object
name|applicationContext
parameter_list|,
name|String
name|clientId
parameter_list|,
name|String
name|clientSecret
parameter_list|,
name|String
name|redirectUrl
parameter_list|)
block|{
comment|// unknown usage
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"initializeAuthFlow"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|initializeAuthFlow (Object applicationContext, String clientId, String clientSecret, String redirectUrl, BoxClient boxClient)
specifier|public
name|void
name|initializeAuthFlow
parameter_list|(
name|Object
name|applicationContext
parameter_list|,
name|String
name|clientId
parameter_list|,
name|String
name|clientSecret
parameter_list|,
name|String
name|redirectUrl
parameter_list|,
name|BoxClient
name|boxClient
parameter_list|)
block|{
comment|// unknown usage
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"initializeAuthFlow"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

