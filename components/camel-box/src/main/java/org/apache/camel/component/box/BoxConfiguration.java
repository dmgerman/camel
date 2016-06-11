begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.box
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
name|Objects
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
name|IBoxConfig
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
name|authorization
operator|.
name|OAuthRefreshListener
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
name|internal
operator|.
name|BoxApiName
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
name|Metadata
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
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriPath
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

begin_comment
comment|/**  * Component configuration for Box component.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|BoxConfiguration
specifier|public
class|class
name|BoxConfiguration
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|apiName
specifier|private
name|BoxApiName
name|apiName
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|methodName
specifier|private
name|String
name|methodName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|clientId
specifier|private
name|String
name|clientId
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
DECL|field|clientSecret
specifier|private
name|String
name|clientSecret
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
DECL|field|userName
specifier|private
name|String
name|userName
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
DECL|field|userPassword
specifier|private
name|String
name|userPassword
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced,security"
argument_list|)
DECL|field|authSecureStorage
specifier|private
name|IAuthSecureStorage
name|authSecureStorage
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|refreshListener
specifier|private
name|OAuthRefreshListener
name|refreshListener
decl_stmt|;
annotation|@
name|UriParam
DECL|field|revokeOnShutdown
specifier|private
name|boolean
name|revokeOnShutdown
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sharedLink
specifier|private
name|String
name|sharedLink
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sharedPassword
specifier|private
name|String
name|sharedPassword
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|boxConfig
specifier|private
name|IBoxConfig
name|boxConfig
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|connectionManagerBuilder
specifier|private
name|BoxConnectionManagerBuilder
name|connectionManagerBuilder
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|httpParams
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpParams
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"30"
argument_list|)
DECL|field|loginTimeout
specifier|private
name|int
name|loginTimeout
init|=
literal|30
decl_stmt|;
DECL|method|getApiName ()
specifier|public
name|BoxApiName
name|getApiName
parameter_list|()
block|{
return|return
name|apiName
return|;
block|}
comment|/**      * What kind of operation to perform      */
DECL|method|setApiName (BoxApiName apiName)
specifier|public
name|void
name|setApiName
parameter_list|(
name|BoxApiName
name|apiName
parameter_list|)
block|{
name|this
operator|.
name|apiName
operator|=
name|apiName
expr_stmt|;
block|}
DECL|method|getMethodName ()
specifier|public
name|String
name|getMethodName
parameter_list|()
block|{
return|return
name|methodName
return|;
block|}
comment|/**      * What sub operation to use for the selected operation      */
DECL|method|setMethodName (String methodName)
specifier|public
name|void
name|setMethodName
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
block|}
DECL|method|getClientId ()
specifier|public
name|String
name|getClientId
parameter_list|()
block|{
return|return
name|clientId
return|;
block|}
comment|/**      * Box application client ID      */
DECL|method|setClientId (String clientId)
specifier|public
name|void
name|setClientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|this
operator|.
name|clientId
operator|=
name|clientId
expr_stmt|;
block|}
DECL|method|getClientSecret ()
specifier|public
name|String
name|getClientSecret
parameter_list|()
block|{
return|return
name|clientSecret
return|;
block|}
comment|/**      * Box application client secret      */
DECL|method|setClientSecret (String clientSecret)
specifier|public
name|void
name|setClientSecret
parameter_list|(
name|String
name|clientSecret
parameter_list|)
block|{
name|this
operator|.
name|clientSecret
operator|=
name|clientSecret
expr_stmt|;
block|}
DECL|method|getAuthSecureStorage ()
specifier|public
name|IAuthSecureStorage
name|getAuthSecureStorage
parameter_list|()
block|{
return|return
name|authSecureStorage
return|;
block|}
comment|/**      * OAuth Secure Storage callback, can be used to provide and or save OAuth tokens.      * The callback may return null on first call to allow the component to login and authorize application      * and obtain an OAuth token, which can then be saved in the secure storage.      * For the component to be able to create a token automatically a user password must be provided.      */
DECL|method|setAuthSecureStorage (IAuthSecureStorage authSecureStorage)
specifier|public
name|void
name|setAuthSecureStorage
parameter_list|(
name|IAuthSecureStorage
name|authSecureStorage
parameter_list|)
block|{
name|this
operator|.
name|authSecureStorage
operator|=
name|authSecureStorage
expr_stmt|;
block|}
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
comment|/**      * Box user name, MUST be provided      */
DECL|method|setUserName (String userName)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
block|}
DECL|method|getUserPassword ()
specifier|public
name|String
name|getUserPassword
parameter_list|()
block|{
return|return
name|userPassword
return|;
block|}
comment|/**      * Box user password, MUST be provided if authSecureStorage is not set, or returns null on first call      */
DECL|method|setUserPassword (String userPassword)
specifier|public
name|void
name|setUserPassword
parameter_list|(
name|String
name|userPassword
parameter_list|)
block|{
name|this
operator|.
name|userPassword
operator|=
name|userPassword
expr_stmt|;
block|}
DECL|method|getRefreshListener ()
specifier|public
name|OAuthRefreshListener
name|getRefreshListener
parameter_list|()
block|{
return|return
name|refreshListener
return|;
block|}
comment|/**      * OAuth listener for token updates, if the Camel application needs to use the access token outside the route      */
DECL|method|setRefreshListener (OAuthRefreshListener refreshListener)
specifier|public
name|void
name|setRefreshListener
parameter_list|(
name|OAuthRefreshListener
name|refreshListener
parameter_list|)
block|{
name|this
operator|.
name|refreshListener
operator|=
name|refreshListener
expr_stmt|;
block|}
DECL|method|isRevokeOnShutdown ()
specifier|public
name|boolean
name|isRevokeOnShutdown
parameter_list|()
block|{
return|return
name|revokeOnShutdown
return|;
block|}
comment|/**      * Flag to revoke OAuth refresh token on route shutdown, default false.      * Will require a fresh refresh token on restart using either a custom IAuthSecureStorage      * or automatic component login by providing a user password      */
DECL|method|setRevokeOnShutdown (boolean revokeOnShutdown)
specifier|public
name|void
name|setRevokeOnShutdown
parameter_list|(
name|boolean
name|revokeOnShutdown
parameter_list|)
block|{
name|this
operator|.
name|revokeOnShutdown
operator|=
name|revokeOnShutdown
expr_stmt|;
block|}
DECL|method|getSharedLink ()
specifier|public
name|String
name|getSharedLink
parameter_list|()
block|{
return|return
name|sharedLink
return|;
block|}
comment|/**      * Box shared link for shared endpoints, can be a link for a shared comment, file or folder      */
DECL|method|setSharedLink (String sharedLink)
specifier|public
name|void
name|setSharedLink
parameter_list|(
name|String
name|sharedLink
parameter_list|)
block|{
name|this
operator|.
name|sharedLink
operator|=
name|sharedLink
expr_stmt|;
block|}
DECL|method|getSharedPassword ()
specifier|public
name|String
name|getSharedPassword
parameter_list|()
block|{
return|return
name|sharedPassword
return|;
block|}
comment|/**      * Password associated with the shared link, MUST be provided with sharedLink      */
DECL|method|setSharedPassword (String sharedPassword)
specifier|public
name|void
name|setSharedPassword
parameter_list|(
name|String
name|sharedPassword
parameter_list|)
block|{
name|this
operator|.
name|sharedPassword
operator|=
name|sharedPassword
expr_stmt|;
block|}
DECL|method|getBoxConfig ()
specifier|public
name|IBoxConfig
name|getBoxConfig
parameter_list|()
block|{
return|return
name|boxConfig
return|;
block|}
comment|/**      * Custom Box SDK configuration, not required normally      */
DECL|method|setBoxConfig (IBoxConfig boxConfig)
specifier|public
name|void
name|setBoxConfig
parameter_list|(
name|IBoxConfig
name|boxConfig
parameter_list|)
block|{
name|this
operator|.
name|boxConfig
operator|=
name|boxConfig
expr_stmt|;
block|}
DECL|method|getConnectionManagerBuilder ()
specifier|public
name|BoxConnectionManagerBuilder
name|getConnectionManagerBuilder
parameter_list|()
block|{
return|return
name|connectionManagerBuilder
return|;
block|}
comment|/**      * Custom Box connection manager builder, used to override default settings like max connections for underlying HttpClient.      */
DECL|method|setConnectionManagerBuilder (BoxConnectionManagerBuilder connectionManagerBuilder)
specifier|public
name|void
name|setConnectionManagerBuilder
parameter_list|(
name|BoxConnectionManagerBuilder
name|connectionManagerBuilder
parameter_list|)
block|{
name|this
operator|.
name|connectionManagerBuilder
operator|=
name|connectionManagerBuilder
expr_stmt|;
block|}
DECL|method|getHttpParams ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHttpParams
parameter_list|()
block|{
return|return
name|httpParams
return|;
block|}
comment|/**      * Custom HTTP params for settings like proxy host      */
DECL|method|setHttpParams (Map<String, Object> httpParams)
specifier|public
name|void
name|setHttpParams
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpParams
parameter_list|)
block|{
name|this
operator|.
name|httpParams
operator|=
name|httpParams
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
comment|/**      * To configure security using SSLContextParameters.      */
DECL|method|setSslContextParameters (SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|getLoginTimeout ()
specifier|public
name|int
name|getLoginTimeout
parameter_list|()
block|{
return|return
name|loginTimeout
return|;
block|}
comment|/**      * Amount of time the component will wait for a response from Box.com, default is 30 seconds      */
DECL|method|setLoginTimeout (int loginTimeout)
specifier|public
name|void
name|setLoginTimeout
parameter_list|(
name|int
name|loginTimeout
parameter_list|)
block|{
name|this
operator|.
name|loginTimeout
operator|=
name|loginTimeout
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|super
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|BoxConfiguration
condition|)
block|{
specifier|final
name|BoxConfiguration
name|other
init|=
operator|(
name|BoxConfiguration
operator|)
name|obj
decl_stmt|;
comment|// configurations are equal if BoxClient creation parameters are equal
return|return
name|boxConfig
operator|==
name|other
operator|.
name|boxConfig
operator|&&
name|connectionManagerBuilder
operator|==
name|other
operator|.
name|connectionManagerBuilder
operator|&&
name|httpParams
operator|==
name|other
operator|.
name|httpParams
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|clientId
argument_list|,
name|other
operator|.
name|clientId
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|clientSecret
argument_list|,
name|other
operator|.
name|clientSecret
argument_list|)
operator|&&
name|authSecureStorage
operator|==
name|other
operator|.
name|authSecureStorage
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

