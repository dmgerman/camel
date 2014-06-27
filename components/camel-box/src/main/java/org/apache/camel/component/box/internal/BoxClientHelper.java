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
name|concurrent
operator|.
name|CountDownLatch
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
name|BoxConnectionManagerBuilder
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
name|BoxRESTClient
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
name|IAuthSecureStorage
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
name|exceptions
operator|.
name|AuthFatalFailureException
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
name|exceptions
operator|.
name|BoxServerException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|restclientv2
operator|.
name|IBoxRESTClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|restclientv2
operator|.
name|exceptions
operator|.
name|BoxRestException
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
name|http
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
name|apache
operator|.
name|http
operator|.
name|conn
operator|.
name|ClientConnectionManager
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
name|params
operator|.
name|HttpParams
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
comment|/**  * Helper class to work with {@link BoxClient}.  */
end_comment

begin_class
DECL|class|BoxClientHelper
specifier|public
specifier|final
class|class
name|BoxClientHelper
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
name|BoxClientHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|BoxClientHelper ()
specifier|private
name|BoxClientHelper
parameter_list|()
block|{     }
comment|// create BoxClient using provided configuration
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|createBoxClient (final BoxConfiguration configuration)
specifier|public
specifier|static
name|CachedBoxClient
name|createBoxClient
parameter_list|(
specifier|final
name|BoxConfiguration
name|configuration
parameter_list|)
block|{
specifier|final
name|String
name|clientId
init|=
name|configuration
operator|.
name|getClientId
argument_list|()
decl_stmt|;
specifier|final
name|String
name|clientSecret
init|=
name|configuration
operator|.
name|getClientSecret
argument_list|()
decl_stmt|;
specifier|final
name|IAuthSecureStorage
name|authSecureStorage
init|=
name|configuration
operator|.
name|getAuthSecureStorage
argument_list|()
decl_stmt|;
specifier|final
name|String
name|userName
init|=
name|configuration
operator|.
name|getUserName
argument_list|()
decl_stmt|;
specifier|final
name|String
name|userPassword
init|=
name|configuration
operator|.
name|getUserPassword
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|authSecureStorage
operator|==
literal|null
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|userPassword
argument_list|)
operator|)
operator|||
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|userName
argument_list|)
operator|||
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|clientId
argument_list|)
operator|||
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|clientSecret
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Missing one or more required properties "
operator|+
literal|"clientId, clientSecret, userName and either authSecureStorage or userPassword"
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating BoxClient for login:{}, client_id:{} ..."
argument_list|,
name|userName
argument_list|,
name|clientId
argument_list|)
expr_stmt|;
comment|// if set, use configured connection manager builder
specifier|final
name|BoxConnectionManagerBuilder
name|configBuilder
init|=
name|configuration
operator|.
name|getConnectionManagerBuilder
argument_list|()
decl_stmt|;
specifier|final
name|BoxConnectionManagerBuilder
name|connectionManager
init|=
name|configBuilder
operator|!=
literal|null
condition|?
name|configBuilder
else|:
operator|new
name|BoxConnectionManagerBuilder
argument_list|()
decl_stmt|;
comment|// create REST client for BoxClient
specifier|final
name|ClientConnectionManager
index|[]
name|clientConnectionManager
init|=
operator|new
name|ClientConnectionManager
index|[
literal|1
index|]
decl_stmt|;
specifier|final
name|IBoxRESTClient
name|restClient
init|=
operator|new
name|BoxRESTClient
argument_list|(
name|connectionManager
operator|.
name|build
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|HttpClient
name|getRawHttpClient
parameter_list|()
block|{
specifier|final
name|HttpClient
name|httpClient
init|=
name|super
operator|.
name|getRawHttpClient
argument_list|()
decl_stmt|;
name|clientConnectionManager
index|[
literal|0
index|]
operator|=
name|httpClient
operator|.
name|getConnectionManager
argument_list|()
expr_stmt|;
comment|// set custom HTTP params
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|configParams
init|=
name|configuration
operator|.
name|getHttpParams
argument_list|()
decl_stmt|;
if|if
condition|(
name|configParams
operator|!=
literal|null
operator|&&
operator|!
name|configParams
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Setting {} HTTP Params"
argument_list|,
name|configParams
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|HttpParams
name|httpParams
init|=
name|httpClient
operator|.
name|getParams
argument_list|()
decl_stmt|;
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
name|param
range|:
name|configParams
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|httpParams
operator|.
name|setParameter
argument_list|(
name|param
operator|.
name|getKey
argument_list|()
argument_list|,
name|param
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|httpClient
return|;
block|}
block|}
decl_stmt|;
specifier|final
name|BoxClient
name|boxClient
init|=
operator|new
name|BoxClient
argument_list|(
name|clientId
argument_list|,
name|clientSecret
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|restClient
argument_list|,
name|configuration
operator|.
name|getBoxConfig
argument_list|()
argument_list|)
decl_stmt|;
comment|// enable OAuth auto-refresh
name|boxClient
operator|.
name|setAutoRefreshOAuth
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// wrap the configured storage in a caching storage
specifier|final
name|CachingSecureStorage
name|storage
init|=
operator|new
name|CachingSecureStorage
argument_list|(
name|authSecureStorage
argument_list|)
decl_stmt|;
comment|// set up a listener to notify secure storage and user provided listener, store it in configuration!
specifier|final
name|OAuthHelperListener
name|listener
init|=
operator|new
name|OAuthHelperListener
argument_list|(
name|storage
argument_list|,
name|configuration
operator|.
name|getRefreshListener
argument_list|()
argument_list|)
decl_stmt|;
name|boxClient
operator|.
name|addOAuthRefreshListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
specifier|final
name|CachedBoxClient
name|cachedBoxClient
init|=
operator|new
name|CachedBoxClient
argument_list|(
name|boxClient
argument_list|,
name|userName
argument_list|,
name|clientId
argument_list|,
name|storage
argument_list|,
name|listener
argument_list|,
name|clientConnectionManager
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"BoxClient created {}"
argument_list|,
name|cachedBoxClient
argument_list|)
expr_stmt|;
return|return
name|cachedBoxClient
return|;
block|}
DECL|method|getOAuthToken (BoxConfiguration configuration, CachedBoxClient cachedBoxClient)
specifier|public
specifier|static
name|void
name|getOAuthToken
parameter_list|(
name|BoxConfiguration
name|configuration
parameter_list|,
name|CachedBoxClient
name|cachedBoxClient
parameter_list|)
throws|throws
name|AuthFatalFailureException
throws|,
name|BoxRestException
throws|,
name|BoxServerException
throws|,
name|InterruptedException
block|{
specifier|final
name|BoxClient
name|boxClient
init|=
name|cachedBoxClient
operator|.
name|getBoxClient
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|boxClient
init|)
block|{
if|if
condition|(
name|boxClient
operator|.
name|isAuthenticated
argument_list|()
condition|)
block|{
return|return;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Getting OAuth token for {}..."
argument_list|,
name|cachedBoxClient
argument_list|)
expr_stmt|;
specifier|final
name|IAuthSecureStorage
name|authSecureStorage
init|=
name|cachedBoxClient
operator|.
name|getSecureStorage
argument_list|()
decl_stmt|;
if|if
condition|(
name|authSecureStorage
operator|!=
literal|null
operator|&&
name|authSecureStorage
operator|.
name|getAuth
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using secure storage for {}"
argument_list|,
name|cachedBoxClient
argument_list|)
expr_stmt|;
comment|// authenticate using stored refresh token
name|boxClient
operator|.
name|authenticateFromSecureStorage
argument_list|(
name|authSecureStorage
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using OAuth {}"
argument_list|,
name|cachedBoxClient
argument_list|)
expr_stmt|;
comment|// authorize App for user, and create OAuth token with refresh token
specifier|final
name|IAuthFlowUI
name|authFlowUI
init|=
operator|new
name|LoginAuthFlowUI
argument_list|(
name|configuration
argument_list|,
name|boxClient
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|LoginAuthFlowListener
name|listener
init|=
operator|new
name|LoginAuthFlowListener
argument_list|(
name|latch
argument_list|)
decl_stmt|;
name|boxClient
operator|.
name|authenticate
argument_list|(
name|authFlowUI
argument_list|,
literal|true
argument_list|,
name|listener
argument_list|)
expr_stmt|;
comment|// wait for login to finish or timeout
if|if
condition|(
operator|!
name|latch
operator|.
name|await
argument_list|(
name|configuration
operator|.
name|getLoginTimeout
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|boxClient
operator|.
name|isAuthenticated
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Login timeout for %s"
argument_list|,
name|cachedBoxClient
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|final
name|Exception
name|ex
init|=
name|listener
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Login error for %s: %s"
argument_list|,
name|cachedBoxClient
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
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"OAuth token created for {}"
argument_list|,
name|cachedBoxClient
argument_list|)
expr_stmt|;
comment|// notify the cached client listener for the first time, since BoxClient doesn't!!!
name|cachedBoxClient
operator|.
name|getListener
argument_list|()
operator|.
name|onRefresh
argument_list|(
name|boxClient
operator|.
name|getAuthData
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|closeIdleConnections (CachedBoxClient cachedBoxClient)
specifier|public
specifier|static
name|void
name|closeIdleConnections
parameter_list|(
name|CachedBoxClient
name|cachedBoxClient
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|final
name|ClientConnectionManager
name|connectionManager
init|=
name|cachedBoxClient
operator|.
name|getClientConnectionManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|connectionManager
operator|!=
literal|null
condition|)
block|{
comment|// close all idle connections
name|connectionManager
operator|.
name|closeIdleConnections
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|shutdownBoxClient (BoxConfiguration configuration, CachedBoxClient cachedBoxClient)
specifier|public
specifier|static
name|void
name|shutdownBoxClient
parameter_list|(
name|BoxConfiguration
name|configuration
parameter_list|,
name|CachedBoxClient
name|cachedBoxClient
parameter_list|)
throws|throws
name|BoxServerException
throws|,
name|BoxRestException
throws|,
name|AuthFatalFailureException
block|{
specifier|final
name|BoxClient
name|boxClient
init|=
name|cachedBoxClient
operator|.
name|getBoxClient
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|boxClient
init|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Shutting down {} ..."
argument_list|,
name|cachedBoxClient
argument_list|)
expr_stmt|;
try|try
block|{
comment|// revoke token if requested
if|if
condition|(
name|configuration
operator|.
name|isRevokeOnShutdown
argument_list|()
condition|)
block|{
name|revokeOAuthToken
argument_list|(
name|configuration
argument_list|,
name|cachedBoxClient
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|boxClient
operator|.
name|setConnectionOpen
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// close connections in the underlying HttpClient
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|final
name|ClientConnectionManager
name|connectionManager
init|=
name|cachedBoxClient
operator|.
name|getClientConnectionManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|connectionManager
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Closing connections for {}"
argument_list|,
name|cachedBoxClient
argument_list|)
expr_stmt|;
name|connectionManager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"ConnectionManager not created for {}"
argument_list|,
name|cachedBoxClient
argument_list|)
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Shutdown successful for {}"
argument_list|,
name|cachedBoxClient
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|revokeOAuthToken (BoxConfiguration configuration, CachedBoxClient cachedBoxClient)
specifier|private
specifier|static
name|void
name|revokeOAuthToken
parameter_list|(
name|BoxConfiguration
name|configuration
parameter_list|,
name|CachedBoxClient
name|cachedBoxClient
parameter_list|)
throws|throws
name|BoxServerException
throws|,
name|BoxRestException
throws|,
name|AuthFatalFailureException
block|{
specifier|final
name|BoxClient
name|boxClient
init|=
name|cachedBoxClient
operator|.
name|getBoxClient
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|boxClient
init|)
block|{
if|if
condition|(
name|boxClient
operator|.
name|isAuthenticated
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Revoking OAuth refresh token for {}"
argument_list|,
name|cachedBoxClient
argument_list|)
expr_stmt|;
comment|// notify the OAuthListener of revoked token
name|cachedBoxClient
operator|.
name|getListener
argument_list|()
operator|.
name|onRefresh
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// mark auth data revoked
name|boxClient
operator|.
name|getOAuthDataController
argument_list|()
operator|.
name|setOAuthData
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// revoke OAuth token
name|boxClient
operator|.
name|getOAuthManager
argument_list|()
operator|.
name|revokeOAuth
argument_list|(
name|boxClient
operator|.
name|getAuthData
argument_list|()
operator|.
name|getAccessToken
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
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

