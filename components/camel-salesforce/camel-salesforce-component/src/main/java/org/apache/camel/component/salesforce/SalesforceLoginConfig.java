begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce
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
name|support
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Configuration object for Salesforce login properties  */
end_comment

begin_class
DECL|class|SalesforceLoginConfig
specifier|public
class|class
name|SalesforceLoginConfig
block|{
DECL|field|DEFAULT_LOGIN_URL
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_LOGIN_URL
init|=
literal|"https://login.salesforce.com"
decl_stmt|;
DECL|field|type
specifier|private
name|AuthenticationType
name|type
decl_stmt|;
DECL|field|instanceUrl
specifier|private
name|String
name|instanceUrl
decl_stmt|;
DECL|field|loginUrl
specifier|private
name|String
name|loginUrl
decl_stmt|;
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
DECL|field|clientSecret
specifier|private
name|String
name|clientSecret
decl_stmt|;
DECL|field|refreshToken
specifier|private
name|String
name|refreshToken
decl_stmt|;
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|// allow lazy login into Salesforce
comment|// note that login issues may not surface until a message needs to be
comment|// processed
DECL|field|lazyLogin
specifier|private
name|boolean
name|lazyLogin
decl_stmt|;
DECL|field|keystore
specifier|private
name|KeyStoreParameters
name|keystore
decl_stmt|;
DECL|method|SalesforceLoginConfig ()
specifier|public
name|SalesforceLoginConfig
parameter_list|()
block|{
name|loginUrl
operator|=
name|DEFAULT_LOGIN_URL
expr_stmt|;
name|lazyLogin
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|SalesforceLoginConfig (AuthenticationType type, String loginUrl, String clientId, String clientSecret, String refreshToken, String userName, String password, boolean lazyLogin, KeyStoreParameters keystore)
specifier|private
name|SalesforceLoginConfig
parameter_list|(
name|AuthenticationType
name|type
parameter_list|,
name|String
name|loginUrl
parameter_list|,
name|String
name|clientId
parameter_list|,
name|String
name|clientSecret
parameter_list|,
name|String
name|refreshToken
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|,
name|boolean
name|lazyLogin
parameter_list|,
name|KeyStoreParameters
name|keystore
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|loginUrl
operator|=
name|loginUrl
expr_stmt|;
name|this
operator|.
name|clientId
operator|=
name|clientId
expr_stmt|;
name|this
operator|.
name|clientSecret
operator|=
name|clientSecret
expr_stmt|;
name|this
operator|.
name|refreshToken
operator|=
name|refreshToken
expr_stmt|;
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
name|this
operator|.
name|lazyLogin
operator|=
name|lazyLogin
expr_stmt|;
name|this
operator|.
name|keystore
operator|=
name|keystore
expr_stmt|;
block|}
DECL|method|SalesforceLoginConfig (String loginUrl, String clientId, String clientSecret, String userName, String password, boolean lazyLogin)
specifier|public
name|SalesforceLoginConfig
parameter_list|(
name|String
name|loginUrl
parameter_list|,
name|String
name|clientId
parameter_list|,
name|String
name|clientSecret
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|,
name|boolean
name|lazyLogin
parameter_list|)
block|{
name|this
argument_list|(
name|AuthenticationType
operator|.
name|USERNAME_PASSWORD
argument_list|,
name|loginUrl
argument_list|,
name|clientId
argument_list|,
name|clientSecret
argument_list|,
literal|null
argument_list|,
name|userName
argument_list|,
name|password
argument_list|,
name|lazyLogin
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|SalesforceLoginConfig (String loginUrl, String clientId, String clientSecret, String refreshToken, boolean lazyLogin)
specifier|public
name|SalesforceLoginConfig
parameter_list|(
name|String
name|loginUrl
parameter_list|,
name|String
name|clientId
parameter_list|,
name|String
name|clientSecret
parameter_list|,
name|String
name|refreshToken
parameter_list|,
name|boolean
name|lazyLogin
parameter_list|)
block|{
name|this
argument_list|(
name|AuthenticationType
operator|.
name|REFRESH_TOKEN
argument_list|,
name|loginUrl
argument_list|,
name|clientId
argument_list|,
name|clientSecret
argument_list|,
name|refreshToken
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|lazyLogin
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|SalesforceLoginConfig (String loginUrl, String clientId, String userName, KeyStoreParameters keystore, boolean lazyLogin)
specifier|public
name|SalesforceLoginConfig
parameter_list|(
name|String
name|loginUrl
parameter_list|,
name|String
name|clientId
parameter_list|,
name|String
name|userName
parameter_list|,
name|KeyStoreParameters
name|keystore
parameter_list|,
name|boolean
name|lazyLogin
parameter_list|)
block|{
name|this
argument_list|(
name|AuthenticationType
operator|.
name|JWT
argument_list|,
name|loginUrl
argument_list|,
name|clientId
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|userName
argument_list|,
literal|null
argument_list|,
name|lazyLogin
argument_list|,
name|keystore
argument_list|)
expr_stmt|;
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
DECL|method|setInstanceUrl (final String instanceUrl)
specifier|public
name|void
name|setInstanceUrl
parameter_list|(
specifier|final
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
DECL|method|getLoginUrl ()
specifier|public
name|String
name|getLoginUrl
parameter_list|()
block|{
return|return
name|loginUrl
return|;
block|}
comment|/**      * Salesforce login URL, defaults to https://login.salesforce.com      */
DECL|method|setLoginUrl (String loginUrl)
specifier|public
name|void
name|setLoginUrl
parameter_list|(
name|String
name|loginUrl
parameter_list|)
block|{
name|this
operator|.
name|loginUrl
operator|=
name|loginUrl
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
comment|/**      * Salesforce connected application Consumer Key      */
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
comment|/**      * Salesforce connected application Consumer Secret      */
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
comment|/**      * Keystore parameters for keystore containing certificate and private key      * needed for OAuth 2.0 JWT Bearer Token Flow.      */
DECL|method|setKeystore (final KeyStoreParameters keystore)
specifier|public
name|void
name|setKeystore
parameter_list|(
specifier|final
name|KeyStoreParameters
name|keystore
parameter_list|)
block|{
name|this
operator|.
name|keystore
operator|=
name|keystore
expr_stmt|;
block|}
DECL|method|getKeystore ()
specifier|public
name|KeyStoreParameters
name|getKeystore
parameter_list|()
block|{
return|return
name|keystore
return|;
block|}
DECL|method|getRefreshToken ()
specifier|public
name|String
name|getRefreshToken
parameter_list|()
block|{
return|return
name|refreshToken
return|;
block|}
comment|/**      * Salesforce connected application Consumer token      */
DECL|method|setRefreshToken (String refreshToken)
specifier|public
name|void
name|setRefreshToken
parameter_list|(
name|String
name|refreshToken
parameter_list|)
block|{
name|this
operator|.
name|refreshToken
operator|=
name|refreshToken
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|AuthenticationType
name|getType
parameter_list|()
block|{
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
comment|// use the user provided type
return|return
name|type
return|;
block|}
specifier|final
name|boolean
name|hasPassword
init|=
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|password
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|hasRefreshToken
init|=
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|refreshToken
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|hasKeystore
init|=
name|keystore
operator|!=
literal|null
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|keystore
operator|.
name|getResource
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|hasPassword
operator|&&
operator|!
name|hasRefreshToken
operator|&&
operator|!
name|hasKeystore
condition|)
block|{
return|return
name|AuthenticationType
operator|.
name|USERNAME_PASSWORD
return|;
block|}
if|if
condition|(
operator|!
name|hasPassword
operator|&&
name|hasRefreshToken
operator|&&
operator|!
name|hasKeystore
condition|)
block|{
return|return
name|AuthenticationType
operator|.
name|REFRESH_TOKEN
return|;
block|}
if|if
condition|(
operator|!
name|hasPassword
operator|&&
operator|!
name|hasRefreshToken
operator|&&
name|hasKeystore
condition|)
block|{
return|return
name|AuthenticationType
operator|.
name|JWT
return|;
block|}
if|if
condition|(
name|hasPassword
operator|&&
name|hasRefreshToken
operator|||
name|hasPassword
operator|&&
name|hasKeystore
operator|||
name|hasRefreshToken
operator|&&
name|hasKeystore
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The provided authentication configuration can be used in multiple ways"
operator|+
literal|" for instance both with username/password and refresh_token. Either remove some of the configuration"
operator|+
literal|" options, so that authentication method can be auto-determined or explicitly set the authentication"
operator|+
literal|" type."
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You must specify parameters aligned with one of the supported authentication methods:"
operator|+
literal|" for username and password authentication: userName, password, clientSecret;"
operator|+
literal|" for refresh token authentication: refreshToken, clientSecret;"
operator|+
literal|" for JWT: userName, keystore. And for every one of those loginUrl and clientId must be specified also."
argument_list|)
throw|;
block|}
DECL|method|setType (AuthenticationType type)
specifier|public
name|void
name|setType
parameter_list|(
name|AuthenticationType
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
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
comment|/**      * Salesforce account user name      */
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
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/**      * Salesforce account password      */
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|isLazyLogin ()
specifier|public
name|boolean
name|isLazyLogin
parameter_list|()
block|{
return|return
name|lazyLogin
return|;
block|}
comment|/**      * Flag to enable/disable lazy OAuth, default is false. When enabled, OAuth      * token retrieval or generation is not done until the first API call      */
DECL|method|setLazyLogin (boolean lazyLogin)
specifier|public
name|void
name|setLazyLogin
parameter_list|(
name|boolean
name|lazyLogin
parameter_list|)
block|{
name|this
operator|.
name|lazyLogin
operator|=
name|lazyLogin
expr_stmt|;
block|}
DECL|method|validate ()
specifier|public
name|void
name|validate
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|loginUrl
argument_list|,
literal|"loginUrl"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|clientId
argument_list|,
literal|"clientId"
argument_list|)
expr_stmt|;
specifier|final
name|AuthenticationType
name|type
init|=
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|userName
argument_list|,
literal|"userName (username/password authentication)"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|password
argument_list|,
literal|"password (username/password authentication)"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|clientSecret
argument_list|,
literal|"clientSecret (username/password authentication)"
argument_list|)
expr_stmt|;
break|break;
case|case
name|REFRESH_TOKEN
case|:
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|refreshToken
argument_list|,
literal|"refreshToken (authentication with refresh token)"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|clientSecret
argument_list|,
literal|"clientSecret (authentication with refresh token)"
argument_list|)
expr_stmt|;
break|break;
case|case
name|JWT
case|:
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|userName
argument_list|,
literal|"userName (JWT authentication)"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|keystore
argument_list|,
literal|"keystore (JWT authentication)"
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown authentication type: "
operator|+
name|type
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"SalesforceLoginConfig["
operator|+
literal|"instanceUrl= '"
operator|+
name|instanceUrl
operator|+
literal|"', loginUrl='"
operator|+
name|loginUrl
operator|+
literal|'\''
operator|+
literal|","
operator|+
literal|"clientId='"
operator|+
name|clientId
operator|+
literal|'\''
operator|+
literal|", clientSecret='********'"
operator|+
literal|", refreshToken='"
operator|+
name|refreshToken
operator|+
literal|'\''
operator|+
literal|", userName='"
operator|+
name|userName
operator|+
literal|'\''
operator|+
literal|", password=********'"
operator|+
name|password
operator|+
literal|'\''
operator|+
literal|", keystore=********'"
operator|+
name|keystore
operator|+
literal|'\''
operator|+
literal|", lazyLogin="
operator|+
name|lazyLogin
operator|+
literal|']'
return|;
block|}
block|}
end_class

end_unit

