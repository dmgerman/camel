begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.facebook.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|facebook
operator|.
name|config
package|;
end_package

begin_import
import|import
name|facebook4j
operator|.
name|Facebook
import|;
end_import

begin_import
import|import
name|facebook4j
operator|.
name|FacebookException
import|;
end_import

begin_import
import|import
name|facebook4j
operator|.
name|FacebookFactory
import|;
end_import

begin_import
import|import
name|facebook4j
operator|.
name|auth
operator|.
name|OAuthAuthorization
import|;
end_import

begin_import
import|import
name|facebook4j
operator|.
name|conf
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|facebook4j
operator|.
name|conf
operator|.
name|ConfigurationBuilder
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
name|UriParam
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
name|UriParams
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
comment|/**  * Facebook component configuration.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|FacebookConfiguration
specifier|public
class|class
name|FacebookConfiguration
implements|implements
name|Cloneable
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
name|FacebookConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|oAuthAppId
specifier|private
name|String
name|oAuthAppId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|oAuthAppSecret
specifier|private
name|String
name|oAuthAppSecret
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|oAuthAccessToken
specifier|private
name|String
name|oAuthAccessToken
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|defaultValue
operator|=
literal|"https://www.facebook.com/dialog/oauth"
argument_list|)
DECL|field|oAuthAuthorizationURL
specifier|private
name|String
name|oAuthAuthorizationURL
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|oAuthPermissions
specifier|private
name|String
name|oAuthPermissions
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|defaultValue
operator|=
literal|"https://graph.facebook.com/oauth/access_token"
argument_list|)
DECL|field|oAuthAccessTokenURL
specifier|private
name|String
name|oAuthAccessTokenURL
decl_stmt|;
annotation|@
name|UriParam
DECL|field|clientURL
specifier|private
name|String
name|clientURL
decl_stmt|;
annotation|@
name|UriParam
DECL|field|clientVersion
specifier|private
name|String
name|clientVersion
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|debugEnabled
specifier|private
name|Boolean
name|debugEnabled
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|gzipEnabled
specifier|private
name|Boolean
name|gzipEnabled
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"20000"
argument_list|)
DECL|field|httpConnectionTimeout
specifier|private
name|Integer
name|httpConnectionTimeout
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"2"
argument_list|)
DECL|field|httpDefaultMaxPerRoute
specifier|private
name|Integer
name|httpDefaultMaxPerRoute
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"20"
argument_list|)
DECL|field|httpMaxTotalConnections
specifier|private
name|Integer
name|httpMaxTotalConnections
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|httpProxyHost
specifier|private
name|String
name|httpProxyHost
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|httpProxyPassword
specifier|private
name|String
name|httpProxyPassword
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|httpProxyPort
specifier|private
name|Integer
name|httpProxyPort
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|httpProxyUser
specifier|private
name|String
name|httpProxyUser
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"120000"
argument_list|)
DECL|field|httpReadTimeout
specifier|private
name|Integer
name|httpReadTimeout
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"0"
argument_list|)
DECL|field|httpRetryCount
specifier|private
name|Integer
name|httpRetryCount
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"5"
argument_list|)
DECL|field|httpRetryIntervalSeconds
specifier|private
name|Integer
name|httpRetryIntervalSeconds
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"40000"
argument_list|)
DECL|field|httpStreamingReadTimeout
specifier|private
name|Integer
name|httpStreamingReadTimeout
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|jsonStoreEnabled
specifier|private
name|Boolean
name|jsonStoreEnabled
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|mbeanEnabled
specifier|private
name|Boolean
name|mbeanEnabled
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|prettyDebugEnabled
specifier|private
name|Boolean
name|prettyDebugEnabled
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"https://graph.facebook.com/"
argument_list|)
DECL|field|restBaseURL
specifier|private
name|String
name|restBaseURL
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|useSSL
specifier|private
name|Boolean
name|useSSL
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"https://graph-video.facebook.com/"
argument_list|)
DECL|field|videoBaseURL
specifier|private
name|String
name|videoBaseURL
decl_stmt|;
comment|// cached FaceBook instance, is created in getFacebook by endpoint producers and consumers
DECL|field|facebook
specifier|private
name|Facebook
name|facebook
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
specifier|final
name|ConfigurationBuilder
name|builder
init|=
operator|new
name|ConfigurationBuilder
argument_list|()
decl_stmt|;
comment|// apply builder settings
if|if
condition|(
name|oAuthAccessToken
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setOAuthAccessToken
argument_list|(
name|oAuthAccessToken
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|oAuthAccessTokenURL
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setOAuthAccessTokenURL
argument_list|(
name|oAuthAccessTokenURL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|oAuthAppId
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setOAuthAppId
argument_list|(
name|oAuthAppId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|oAuthAppSecret
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setOAuthAppSecret
argument_list|(
name|oAuthAppSecret
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|oAuthAuthorizationURL
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setOAuthAuthorizationURL
argument_list|(
name|oAuthAuthorizationURL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|oAuthPermissions
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setOAuthPermissions
argument_list|(
name|oAuthPermissions
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|clientURL
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setClientURL
argument_list|(
name|clientURL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|clientVersion
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setClientVersion
argument_list|(
name|clientVersion
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|debugEnabled
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setDebugEnabled
argument_list|(
name|debugEnabled
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|gzipEnabled
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setGZIPEnabled
argument_list|(
name|gzipEnabled
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpConnectionTimeout
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setHttpConnectionTimeout
argument_list|(
name|httpConnectionTimeout
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpDefaultMaxPerRoute
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setHttpDefaultMaxPerRoute
argument_list|(
name|httpDefaultMaxPerRoute
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpMaxTotalConnections
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setHttpMaxTotalConnections
argument_list|(
name|httpMaxTotalConnections
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpProxyHost
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setHttpProxyHost
argument_list|(
name|httpProxyHost
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpProxyPassword
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setHttpProxyPassword
argument_list|(
name|httpProxyPassword
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpProxyPort
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setHttpProxyPort
argument_list|(
name|httpProxyPort
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpProxyUser
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setHttpProxyUser
argument_list|(
name|httpProxyUser
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpReadTimeout
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setHttpReadTimeout
argument_list|(
name|httpReadTimeout
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpRetryCount
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setHttpRetryCount
argument_list|(
name|httpRetryCount
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpRetryIntervalSeconds
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setHttpRetryIntervalSeconds
argument_list|(
name|httpRetryIntervalSeconds
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpStreamingReadTimeout
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setHttpStreamingReadTimeout
argument_list|(
name|httpStreamingReadTimeout
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jsonStoreEnabled
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setJSONStoreEnabled
argument_list|(
name|jsonStoreEnabled
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mbeanEnabled
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setMBeanEnabled
argument_list|(
name|mbeanEnabled
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|prettyDebugEnabled
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setPrettyDebugEnabled
argument_list|(
name|prettyDebugEnabled
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|restBaseURL
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setRestBaseURL
argument_list|(
name|restBaseURL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|useSSL
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setUseSSL
argument_list|(
name|useSSL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|videoBaseURL
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setVideoBaseURL
argument_list|(
name|videoBaseURL
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Returns {@link Facebook} instance. If needed, creates one from configuration.      * @return {@link Facebook} instance      */
DECL|method|getFacebook ()
specifier|public
name|Facebook
name|getFacebook
parameter_list|()
throws|throws
name|FacebookException
block|{
if|if
condition|(
name|facebook
operator|==
literal|null
condition|)
block|{
specifier|final
name|Configuration
name|configuration
init|=
name|getConfiguration
argument_list|()
decl_stmt|;
name|FacebookFactory
name|factory
init|=
operator|new
name|FacebookFactory
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|oAuthAccessToken
operator|==
literal|null
condition|)
block|{
comment|// app login
name|facebook
operator|=
name|factory
operator|.
name|getInstance
argument_list|(
operator|new
name|OAuthAuthorization
argument_list|(
name|configuration
argument_list|)
argument_list|)
expr_stmt|;
comment|// also get the App access token
name|facebook
operator|.
name|getOAuthAppAccessToken
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"Login with app id and secret, access to some APIs is restricted!"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// user login with token
name|facebook
operator|=
name|factory
operator|.
name|getInstance
argument_list|()
expr_stmt|;
comment|// verify the access token
name|facebook
operator|.
name|getOAuthAccessToken
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Login with app id, secret and token, all APIs accessible"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|facebook
return|;
block|}
DECL|method|copy ()
specifier|public
name|FacebookConfiguration
name|copy
parameter_list|()
throws|throws
name|CloneNotSupportedException
block|{
specifier|final
name|FacebookConfiguration
name|copy
init|=
operator|(
name|FacebookConfiguration
operator|)
name|clone
argument_list|()
decl_stmt|;
comment|// do not copy facebook instance!!!
name|copy
operator|.
name|facebook
operator|=
literal|null
expr_stmt|;
return|return
name|copy
return|;
block|}
DECL|method|getOAuthAccessToken ()
specifier|public
name|String
name|getOAuthAccessToken
parameter_list|()
block|{
return|return
name|oAuthAccessToken
return|;
block|}
comment|/**      * The user access token      */
DECL|method|setOAuthAccessToken (String oAuthAccessToken)
specifier|public
name|void
name|setOAuthAccessToken
parameter_list|(
name|String
name|oAuthAccessToken
parameter_list|)
block|{
name|this
operator|.
name|oAuthAccessToken
operator|=
name|oAuthAccessToken
expr_stmt|;
block|}
DECL|method|getOAuthAccessTokenURL ()
specifier|public
name|String
name|getOAuthAccessTokenURL
parameter_list|()
block|{
return|return
name|oAuthAccessTokenURL
return|;
block|}
comment|/**      * OAuth access token URL      */
DECL|method|setOAuthAccessTokenURL (String oAuthAccessTokenURL)
specifier|public
name|void
name|setOAuthAccessTokenURL
parameter_list|(
name|String
name|oAuthAccessTokenURL
parameter_list|)
block|{
name|this
operator|.
name|oAuthAccessTokenURL
operator|=
name|oAuthAccessTokenURL
expr_stmt|;
block|}
DECL|method|getOAuthAppId ()
specifier|public
name|String
name|getOAuthAppId
parameter_list|()
block|{
return|return
name|oAuthAppId
return|;
block|}
comment|/**      * The application Id      */
DECL|method|setOAuthAppId (String oAuthAppId)
specifier|public
name|void
name|setOAuthAppId
parameter_list|(
name|String
name|oAuthAppId
parameter_list|)
block|{
name|this
operator|.
name|oAuthAppId
operator|=
name|oAuthAppId
expr_stmt|;
block|}
DECL|method|getOAuthAppSecret ()
specifier|public
name|String
name|getOAuthAppSecret
parameter_list|()
block|{
return|return
name|oAuthAppSecret
return|;
block|}
comment|/**      * The application Secret      */
DECL|method|setOAuthAppSecret (String oAuthAppSecret)
specifier|public
name|void
name|setOAuthAppSecret
parameter_list|(
name|String
name|oAuthAppSecret
parameter_list|)
block|{
name|this
operator|.
name|oAuthAppSecret
operator|=
name|oAuthAppSecret
expr_stmt|;
block|}
DECL|method|getOAuthAuthorizationURL ()
specifier|public
name|String
name|getOAuthAuthorizationURL
parameter_list|()
block|{
return|return
name|oAuthAuthorizationURL
return|;
block|}
comment|/**      * OAuth authorization URL      */
DECL|method|setOAuthAuthorizationURL (String oAuthAuthorizationURL)
specifier|public
name|void
name|setOAuthAuthorizationURL
parameter_list|(
name|String
name|oAuthAuthorizationURL
parameter_list|)
block|{
name|this
operator|.
name|oAuthAuthorizationURL
operator|=
name|oAuthAuthorizationURL
expr_stmt|;
block|}
DECL|method|getClientURL ()
specifier|public
name|String
name|getClientURL
parameter_list|()
block|{
return|return
name|clientURL
return|;
block|}
comment|/**      * Facebook4J API client URL      */
DECL|method|setClientURL (String clientURL)
specifier|public
name|void
name|setClientURL
parameter_list|(
name|String
name|clientURL
parameter_list|)
block|{
name|this
operator|.
name|clientURL
operator|=
name|clientURL
expr_stmt|;
block|}
DECL|method|getClientVersion ()
specifier|public
name|String
name|getClientVersion
parameter_list|()
block|{
return|return
name|clientVersion
return|;
block|}
comment|/**      * Facebook4J client API version      */
DECL|method|setClientVersion (String clientVersion)
specifier|public
name|void
name|setClientVersion
parameter_list|(
name|String
name|clientVersion
parameter_list|)
block|{
name|this
operator|.
name|clientVersion
operator|=
name|clientVersion
expr_stmt|;
block|}
DECL|method|getDebugEnabled ()
specifier|public
name|Boolean
name|getDebugEnabled
parameter_list|()
block|{
return|return
name|debugEnabled
return|;
block|}
comment|/**      * Enables deubg output. Effective only with the embedded logger      */
DECL|method|setDebugEnabled (Boolean debugEnabled)
specifier|public
name|void
name|setDebugEnabled
parameter_list|(
name|Boolean
name|debugEnabled
parameter_list|)
block|{
name|this
operator|.
name|debugEnabled
operator|=
name|debugEnabled
expr_stmt|;
block|}
DECL|method|getGzipEnabled ()
specifier|public
name|Boolean
name|getGzipEnabled
parameter_list|()
block|{
return|return
name|gzipEnabled
return|;
block|}
comment|/**      * Use Facebook GZIP encoding      */
DECL|method|setGzipEnabled (Boolean gzipEnabled)
specifier|public
name|void
name|setGzipEnabled
parameter_list|(
name|Boolean
name|gzipEnabled
parameter_list|)
block|{
name|this
operator|.
name|gzipEnabled
operator|=
name|gzipEnabled
expr_stmt|;
block|}
DECL|method|getHttpConnectionTimeout ()
specifier|public
name|Integer
name|getHttpConnectionTimeout
parameter_list|()
block|{
return|return
name|httpConnectionTimeout
return|;
block|}
comment|/**      * Http connection timeout in milliseconds      */
DECL|method|setHttpConnectionTimeout (Integer httpConnectionTimeout)
specifier|public
name|void
name|setHttpConnectionTimeout
parameter_list|(
name|Integer
name|httpConnectionTimeout
parameter_list|)
block|{
name|this
operator|.
name|httpConnectionTimeout
operator|=
name|httpConnectionTimeout
expr_stmt|;
block|}
DECL|method|getHttpDefaultMaxPerRoute ()
specifier|public
name|Integer
name|getHttpDefaultMaxPerRoute
parameter_list|()
block|{
return|return
name|httpDefaultMaxPerRoute
return|;
block|}
comment|/**      * HTTP maximum connections per route      */
DECL|method|setHttpDefaultMaxPerRoute (Integer httpDefaultMaxPerRoute)
specifier|public
name|void
name|setHttpDefaultMaxPerRoute
parameter_list|(
name|Integer
name|httpDefaultMaxPerRoute
parameter_list|)
block|{
name|this
operator|.
name|httpDefaultMaxPerRoute
operator|=
name|httpDefaultMaxPerRoute
expr_stmt|;
block|}
DECL|method|getHttpMaxTotalConnections ()
specifier|public
name|Integer
name|getHttpMaxTotalConnections
parameter_list|()
block|{
return|return
name|httpMaxTotalConnections
return|;
block|}
comment|/**      * HTTP maximum total connections      */
DECL|method|setHttpMaxTotalConnections (Integer httpMaxTotalConnections)
specifier|public
name|void
name|setHttpMaxTotalConnections
parameter_list|(
name|Integer
name|httpMaxTotalConnections
parameter_list|)
block|{
name|this
operator|.
name|httpMaxTotalConnections
operator|=
name|httpMaxTotalConnections
expr_stmt|;
block|}
DECL|method|getHttpProxyHost ()
specifier|public
name|String
name|getHttpProxyHost
parameter_list|()
block|{
return|return
name|httpProxyHost
return|;
block|}
comment|/**      * HTTP proxy server host name      */
DECL|method|setHttpProxyHost (String httpProxyHost)
specifier|public
name|void
name|setHttpProxyHost
parameter_list|(
name|String
name|httpProxyHost
parameter_list|)
block|{
name|this
operator|.
name|httpProxyHost
operator|=
name|httpProxyHost
expr_stmt|;
block|}
DECL|method|getHttpProxyPassword ()
specifier|public
name|String
name|getHttpProxyPassword
parameter_list|()
block|{
return|return
name|httpProxyPassword
return|;
block|}
comment|/**      * HTTP proxy server password      */
DECL|method|setHttpProxyPassword (String httpProxyPassword)
specifier|public
name|void
name|setHttpProxyPassword
parameter_list|(
name|String
name|httpProxyPassword
parameter_list|)
block|{
name|this
operator|.
name|httpProxyPassword
operator|=
name|httpProxyPassword
expr_stmt|;
block|}
DECL|method|getHttpProxyPort ()
specifier|public
name|Integer
name|getHttpProxyPort
parameter_list|()
block|{
return|return
name|httpProxyPort
return|;
block|}
comment|/**      * HTTP proxy server port      */
DECL|method|setHttpProxyPort (Integer httpProxyPort)
specifier|public
name|void
name|setHttpProxyPort
parameter_list|(
name|Integer
name|httpProxyPort
parameter_list|)
block|{
name|this
operator|.
name|httpProxyPort
operator|=
name|httpProxyPort
expr_stmt|;
block|}
DECL|method|getHttpProxyUser ()
specifier|public
name|String
name|getHttpProxyUser
parameter_list|()
block|{
return|return
name|httpProxyUser
return|;
block|}
comment|/**      * HTTP proxy server user name      */
DECL|method|setHttpProxyUser (String httpProxyUser)
specifier|public
name|void
name|setHttpProxyUser
parameter_list|(
name|String
name|httpProxyUser
parameter_list|)
block|{
name|this
operator|.
name|httpProxyUser
operator|=
name|httpProxyUser
expr_stmt|;
block|}
DECL|method|getHttpReadTimeout ()
specifier|public
name|Integer
name|getHttpReadTimeout
parameter_list|()
block|{
return|return
name|httpReadTimeout
return|;
block|}
comment|/**      * Http read timeout in milliseconds      */
DECL|method|setHttpReadTimeout (Integer httpReadTimeout)
specifier|public
name|void
name|setHttpReadTimeout
parameter_list|(
name|Integer
name|httpReadTimeout
parameter_list|)
block|{
name|this
operator|.
name|httpReadTimeout
operator|=
name|httpReadTimeout
expr_stmt|;
block|}
DECL|method|getHttpRetryCount ()
specifier|public
name|Integer
name|getHttpRetryCount
parameter_list|()
block|{
return|return
name|httpRetryCount
return|;
block|}
comment|/**      * Number of HTTP retries      */
DECL|method|setHttpRetryCount (Integer httpRetryCount)
specifier|public
name|void
name|setHttpRetryCount
parameter_list|(
name|Integer
name|httpRetryCount
parameter_list|)
block|{
name|this
operator|.
name|httpRetryCount
operator|=
name|httpRetryCount
expr_stmt|;
block|}
DECL|method|getHttpRetryIntervalSeconds ()
specifier|public
name|Integer
name|getHttpRetryIntervalSeconds
parameter_list|()
block|{
return|return
name|httpRetryIntervalSeconds
return|;
block|}
comment|/**      * HTTP retry interval in seconds      */
DECL|method|setHttpRetryIntervalSeconds (Integer httpRetryIntervalSeconds)
specifier|public
name|void
name|setHttpRetryIntervalSeconds
parameter_list|(
name|Integer
name|httpRetryIntervalSeconds
parameter_list|)
block|{
name|this
operator|.
name|httpRetryIntervalSeconds
operator|=
name|httpRetryIntervalSeconds
expr_stmt|;
block|}
DECL|method|getHttpStreamingReadTimeout ()
specifier|public
name|Integer
name|getHttpStreamingReadTimeout
parameter_list|()
block|{
return|return
name|httpStreamingReadTimeout
return|;
block|}
comment|/**      * HTTP streaming read timeout in milliseconds      */
DECL|method|setHttpStreamingReadTimeout (Integer httpStreamingReadTimeout)
specifier|public
name|void
name|setHttpStreamingReadTimeout
parameter_list|(
name|Integer
name|httpStreamingReadTimeout
parameter_list|)
block|{
name|this
operator|.
name|httpStreamingReadTimeout
operator|=
name|httpStreamingReadTimeout
expr_stmt|;
block|}
DECL|method|getJsonStoreEnabled ()
specifier|public
name|Boolean
name|getJsonStoreEnabled
parameter_list|()
block|{
return|return
name|jsonStoreEnabled
return|;
block|}
comment|/**      * If set to true, raw JSON forms will be stored in DataObjectFactory      */
DECL|method|setJsonStoreEnabled (Boolean jsonStoreEnabled)
specifier|public
name|void
name|setJsonStoreEnabled
parameter_list|(
name|Boolean
name|jsonStoreEnabled
parameter_list|)
block|{
name|this
operator|.
name|jsonStoreEnabled
operator|=
name|jsonStoreEnabled
expr_stmt|;
block|}
DECL|method|getMbeanEnabled ()
specifier|public
name|Boolean
name|getMbeanEnabled
parameter_list|()
block|{
return|return
name|mbeanEnabled
return|;
block|}
comment|/**      * If set to true, Facebook4J mbean will be registerd      */
DECL|method|setMbeanEnabled (Boolean mbeanEnabled)
specifier|public
name|void
name|setMbeanEnabled
parameter_list|(
name|Boolean
name|mbeanEnabled
parameter_list|)
block|{
name|this
operator|.
name|mbeanEnabled
operator|=
name|mbeanEnabled
expr_stmt|;
block|}
DECL|method|getOAuthPermissions ()
specifier|public
name|String
name|getOAuthPermissions
parameter_list|()
block|{
return|return
name|oAuthPermissions
return|;
block|}
comment|/**      * Default OAuth permissions. Comma separated permission names.      * See https://developers.facebook.com/docs/reference/login/#permissions for the detail      */
DECL|method|setOAuthPermissions (String oAuthPermissions)
specifier|public
name|void
name|setOAuthPermissions
parameter_list|(
name|String
name|oAuthPermissions
parameter_list|)
block|{
name|this
operator|.
name|oAuthPermissions
operator|=
name|oAuthPermissions
expr_stmt|;
block|}
DECL|method|getPrettyDebugEnabled ()
specifier|public
name|Boolean
name|getPrettyDebugEnabled
parameter_list|()
block|{
return|return
name|prettyDebugEnabled
return|;
block|}
comment|/**      * Prettify JSON debug output if set to true      */
DECL|method|setPrettyDebugEnabled (Boolean prettyDebugEnabled)
specifier|public
name|void
name|setPrettyDebugEnabled
parameter_list|(
name|Boolean
name|prettyDebugEnabled
parameter_list|)
block|{
name|this
operator|.
name|prettyDebugEnabled
operator|=
name|prettyDebugEnabled
expr_stmt|;
block|}
DECL|method|getRestBaseURL ()
specifier|public
name|String
name|getRestBaseURL
parameter_list|()
block|{
return|return
name|restBaseURL
return|;
block|}
comment|/**      * API base URL      */
DECL|method|setRestBaseURL (String restBaseURL)
specifier|public
name|void
name|setRestBaseURL
parameter_list|(
name|String
name|restBaseURL
parameter_list|)
block|{
name|this
operator|.
name|restBaseURL
operator|=
name|restBaseURL
expr_stmt|;
block|}
DECL|method|getUseSSL ()
specifier|public
name|Boolean
name|getUseSSL
parameter_list|()
block|{
return|return
name|useSSL
return|;
block|}
comment|/**      * Use SSL      */
DECL|method|setUseSSL (Boolean useSSL)
specifier|public
name|void
name|setUseSSL
parameter_list|(
name|Boolean
name|useSSL
parameter_list|)
block|{
name|this
operator|.
name|useSSL
operator|=
name|useSSL
expr_stmt|;
block|}
DECL|method|getVideoBaseURL ()
specifier|public
name|String
name|getVideoBaseURL
parameter_list|()
block|{
return|return
name|videoBaseURL
return|;
block|}
comment|/**      * Video API base URL      */
DECL|method|setVideoBaseURL (String videoBaseURL)
specifier|public
name|void
name|setVideoBaseURL
parameter_list|(
name|String
name|videoBaseURL
parameter_list|)
block|{
name|this
operator|.
name|videoBaseURL
operator|=
name|videoBaseURL
expr_stmt|;
block|}
DECL|method|validate ()
specifier|public
name|void
name|validate
parameter_list|()
block|{
if|if
condition|(
operator|(
name|oAuthAppId
operator|==
literal|null
operator|||
name|oAuthAppId
operator|.
name|isEmpty
argument_list|()
operator|)
operator|||
operator|(
name|oAuthAppSecret
operator|==
literal|null
operator|||
name|oAuthAppSecret
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Missing required properties oAuthAppId, oAuthAppSecret"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

