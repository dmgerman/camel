begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.box2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|box2
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
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|EncryptionAlgorithm
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
name|box2
operator|.
name|internal
operator|.
name|Box2ApiName
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
comment|/**  * Component configuration for Box2 component.  *   * @author<a href="mailto:punkhornsw@gmail.com">William Collins</a>  *   */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|Box2Configuration
specifier|public
class|class
name|Box2Configuration
block|{
comment|/**      * Authentication Types for Connection      */
DECL|field|APP_ENTERPRISE_AUTHENTICATION
specifier|public
specifier|static
specifier|final
name|String
name|APP_ENTERPRISE_AUTHENTICATION
init|=
literal|"APP_ENTERPRISE_AUTHENTICATION"
decl_stmt|;
DECL|field|APP_USER_AUTHENTICATION
specifier|public
specifier|static
specifier|final
name|String
name|APP_USER_AUTHENTICATION
init|=
literal|"APP_USER_AUTHENTICATION"
decl_stmt|;
DECL|field|STANDARD_AUTHENTICATION
specifier|public
specifier|static
specifier|final
name|String
name|STANDARD_AUTHENTICATION
init|=
literal|"STANDARD_AUTHENTICATION"
decl_stmt|;
comment|/**      * Encryption Algorithm Types for Server Authentication.      */
DECL|field|RSA_SHA_512
specifier|public
specifier|static
specifier|final
name|String
name|RSA_SHA_512
init|=
literal|"RSA_SHA_512"
decl_stmt|;
DECL|field|RSA_SHA_384
specifier|public
specifier|static
specifier|final
name|String
name|RSA_SHA_384
init|=
literal|"RSA_SHA_384"
decl_stmt|;
DECL|field|RSA_SHA_256
specifier|public
specifier|static
specifier|final
name|String
name|RSA_SHA_256
init|=
literal|"RSA_SHA_256"
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
DECL|field|apiName
specifier|private
name|Box2ApiName
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
DECL|field|enterpriseId
specifier|private
name|String
name|enterpriseId
decl_stmt|;
annotation|@
name|UriParam
DECL|field|userId
specifier|private
name|String
name|userId
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
DECL|field|publicKeyId
specifier|private
name|String
name|publicKeyId
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
DECL|field|privateKeyFile
specifier|private
name|String
name|privateKeyFile
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
DECL|field|privateKeyPassword
specifier|private
name|String
name|privateKeyPassword
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
DECL|field|accessTokenCache
specifier|private
name|IAccessTokenCache
name|accessTokenCache
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced,security"
argument_list|,
name|defaultValue
operator|=
literal|"100"
argument_list|)
DECL|field|maxCacheEntries
specifier|private
name|int
name|maxCacheEntries
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced,security"
argument_list|,
name|defaultValue
operator|=
name|RSA_SHA_256
argument_list|)
DECL|field|encryptionAlgorithm
specifier|private
name|EncryptionAlgorithm
name|encryptionAlgorithm
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"authentication"
argument_list|,
name|defaultValue
operator|=
name|APP_USER_AUTHENTICATION
argument_list|)
DECL|field|authenticationType
specifier|private
name|String
name|authenticationType
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
comment|/**      * What kind of operation to perform      *       * @return the API Name      */
DECL|method|getApiName ()
specifier|public
name|Box2ApiName
name|getApiName
parameter_list|()
block|{
return|return
name|apiName
return|;
block|}
comment|/**      * What kind of operation to perform      *       * @param apiName      *            the API Name to set      */
DECL|method|setApiName (Box2ApiName apiName)
specifier|public
name|void
name|setApiName
parameter_list|(
name|Box2ApiName
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
comment|/**      * What sub operation to use for the selected operation      *       * @return the methodName      */
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
comment|/**      * What sub operation to use for the selected operation      *       * @param methodName      *            the methodName to set      */
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
comment|/**      * The enterprise ID to use for an App Enterprise.      *       * @return the enterpriseId      */
DECL|method|getEnterpriseId ()
specifier|public
name|String
name|getEnterpriseId
parameter_list|()
block|{
return|return
name|enterpriseId
return|;
block|}
comment|/**      * The enterprise ID to use for an App Enterprise.      *       * @param enterpriseId      *            the enterpriseId to set      */
DECL|method|setEnterpriseId (String enterpriseId)
specifier|public
name|void
name|setEnterpriseId
parameter_list|(
name|String
name|enterpriseId
parameter_list|)
block|{
name|this
operator|.
name|enterpriseId
operator|=
name|enterpriseId
expr_stmt|;
block|}
comment|/**      * The user ID to use for an App User.      *       * @return the userId      */
DECL|method|getUserId ()
specifier|public
name|String
name|getUserId
parameter_list|()
block|{
return|return
name|userId
return|;
block|}
comment|/**      * The user ID to use for an App User.      *       * @param userId      *            the userId to set      */
DECL|method|setUserId (String userId)
specifier|public
name|void
name|setUserId
parameter_list|(
name|String
name|userId
parameter_list|)
block|{
name|this
operator|.
name|userId
operator|=
name|userId
expr_stmt|;
block|}
comment|/**      * The ID for public key for validating the JWT signature.      *       * @return the publicKeyId      */
DECL|method|getPublicKeyId ()
specifier|public
name|String
name|getPublicKeyId
parameter_list|()
block|{
return|return
name|publicKeyId
return|;
block|}
comment|/**      * The ID for public key for validating the JWT signature.      *       * @param publicKeyId      *            the publicKeyId to set      */
DECL|method|setPublicKeyId (String publicKeyId)
specifier|public
name|void
name|setPublicKeyId
parameter_list|(
name|String
name|publicKeyId
parameter_list|)
block|{
name|this
operator|.
name|publicKeyId
operator|=
name|publicKeyId
expr_stmt|;
block|}
comment|/**      * The private key for generating the JWT signature.      *       * @return the privateKey      */
DECL|method|getPrivateKeyFile ()
specifier|public
name|String
name|getPrivateKeyFile
parameter_list|()
block|{
return|return
name|privateKeyFile
return|;
block|}
comment|/**      * The private key for generating the JWT signature.      *       * @param privateKey      *            the privateKey to set      */
DECL|method|setPrivateKeyFile (String privateKey)
specifier|public
name|void
name|setPrivateKeyFile
parameter_list|(
name|String
name|privateKey
parameter_list|)
block|{
name|this
operator|.
name|privateKeyFile
operator|=
name|privateKey
expr_stmt|;
block|}
comment|/**      * The password for the private key.      *       * @return the privateKeyPassword      */
DECL|method|getPrivateKeyPassword ()
specifier|public
name|String
name|getPrivateKeyPassword
parameter_list|()
block|{
return|return
name|privateKeyPassword
return|;
block|}
comment|/**      * The password for the private key.      *       * @param privateKeyPassword      *            the privateKeyPassword to set      */
DECL|method|setPrivateKeyPassword (String privateKeyPassword)
specifier|public
name|void
name|setPrivateKeyPassword
parameter_list|(
name|String
name|privateKeyPassword
parameter_list|)
block|{
name|this
operator|.
name|privateKeyPassword
operator|=
name|privateKeyPassword
expr_stmt|;
block|}
comment|/**      * The maximum number of access tokens in cache.      *       * @return the maxCacheEntries      */
DECL|method|getMaxCacheEntries ()
specifier|public
name|int
name|getMaxCacheEntries
parameter_list|()
block|{
return|return
name|maxCacheEntries
return|;
block|}
comment|/**      * The maximum number of access tokens in cache.      *       * @param maxCacheEntries      *            the maxCacheEntries to set      */
DECL|method|setMaxCacheEntries (String maxCacheEntries)
specifier|public
name|void
name|setMaxCacheEntries
parameter_list|(
name|String
name|maxCacheEntries
parameter_list|)
block|{
try|try
block|{
name|this
operator|.
name|maxCacheEntries
operator|=
name|Integer
operator|.
name|decode
argument_list|(
name|maxCacheEntries
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
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
literal|"Invalid 'maxCacheEntries' value: %s"
argument_list|,
name|maxCacheEntries
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * The type of encryption algorithm for JWT.      *       * @return the encryptionAlgorithm      */
DECL|method|getEncryptionAlgorithm ()
specifier|public
name|EncryptionAlgorithm
name|getEncryptionAlgorithm
parameter_list|()
block|{
return|return
name|encryptionAlgorithm
return|;
block|}
comment|/**      * The type of encryption algorithm for JWT.      *       *<p>      * Supported Algorithms:      *<ul>      *<li>RSA_SHA_256</li>      *<li>RSA_SHA_384</li>      *<li>RSA_SHA_512</li>      *</ul>      *       * @param encryptionAlgorithm      *            the encryptionAlgorithm to set      */
DECL|method|setEncryptionAlgorithm (String encryptionAlgorithm)
specifier|public
name|void
name|setEncryptionAlgorithm
parameter_list|(
name|String
name|encryptionAlgorithm
parameter_list|)
block|{
switch|switch
condition|(
name|encryptionAlgorithm
condition|)
block|{
case|case
name|RSA_SHA_256
case|:
name|this
operator|.
name|encryptionAlgorithm
operator|=
name|EncryptionAlgorithm
operator|.
name|RSA_SHA_256
expr_stmt|;
return|return;
case|case
name|RSA_SHA_384
case|:
name|this
operator|.
name|encryptionAlgorithm
operator|=
name|EncryptionAlgorithm
operator|.
name|RSA_SHA_384
expr_stmt|;
return|return;
case|case
name|RSA_SHA_512
case|:
name|this
operator|.
name|encryptionAlgorithm
operator|=
name|EncryptionAlgorithm
operator|.
name|RSA_SHA_512
expr_stmt|;
return|return;
default|default:
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Invalid Encryption Algorithm: %s"
argument_list|,
name|encryptionAlgorithm
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**      * The type of authentication for connection.      *       *<p>      * Types of Authentication:      *<ul>      *<li>STANDARD_AUTHENTICATION - OAuth 2.0 (3-legged)</li>      *<li>SERVER_AUTHENTICATION - OAuth 2.0 with JSON Web Tokens</li>      *</ul>      *       * @return the authenticationType      */
DECL|method|getAuthenticationType ()
specifier|public
name|String
name|getAuthenticationType
parameter_list|()
block|{
return|return
name|authenticationType
return|;
block|}
comment|/**      * The type of authentication for connection.      *       *<p>      * Types of Authentication:      *<ul>      *<li>STANDARD_AUTHENTICATION - OAuth 2.0 (3-legged)</li>      *<li>SERVER_AUTHENTICATION - OAuth 2.0 with JSON Web Tokens</li>      *</ul>      *       * @param authenticationType      *            the authenticationType to set      */
DECL|method|setAuthenticationType (String authenticationType)
specifier|public
name|void
name|setAuthenticationType
parameter_list|(
name|String
name|authenticationType
parameter_list|)
block|{
switch|switch
condition|(
name|authenticationType
condition|)
block|{
case|case
name|STANDARD_AUTHENTICATION
case|:
case|case
name|APP_USER_AUTHENTICATION
case|:
case|case
name|APP_ENTERPRISE_AUTHENTICATION
case|:
name|this
operator|.
name|authenticationType
operator|=
name|authenticationType
expr_stmt|;
return|return;
default|default:
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Invalid Authentication Type: %s"
argument_list|,
name|authenticationType
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**      * Box application client ID      *       * @return the clientId      */
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
comment|/**      * Box application client ID      *       * @param clientId      *            the clientId to set      */
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
comment|/**      * Box application client secret      *       * @return the clientSecret      */
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
comment|/**      * Box application client secret      *       * @param clientSecret      *            the clientSecret to set      */
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
comment|/**      * Box user name, MUST be provided      *       * @return the userName      */
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
comment|/**      * Box user name, MUST be provided      *       * @param userName      *            the userName to set      */
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
comment|/**      * Box user password, MUST be provided if authSecureStorage is not set, or      * returns null on first call      *       * @return the userPassword      */
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
comment|/**      * Box user password, MUST be provided if authSecureStorage is not set, or      * returns null on first call      *       * @param userPassword      *            the userPassword to set      */
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
comment|/**      * Custom HTTP params for settings like proxy host      *       * @return the httpParams      */
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
comment|/**      * Custom HTTP params for settings like proxy host      *       * @param httpParams      *            the httpParams to set      */
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
comment|/**      * To configure security using SSLContextParameters.      *       * @return the sslContextParameters      */
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
comment|/**      * To configure security using SSLContextParameters.      *       * @param sslContextParameters      *            the sslContextParameters to set      */
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
comment|/**      * Custom Access Token Cache for storing and retrieving access tokens.      *       * @return Custom Access Token Cache      */
DECL|method|getAccessTokenCache ()
specifier|public
name|IAccessTokenCache
name|getAccessTokenCache
parameter_list|()
block|{
return|return
name|accessTokenCache
return|;
block|}
comment|/**      * Custom Access Token Cache for storing and retrieving access tokens.      *       * @param accessTokenCache      *            - the Custom Access Token Cache      */
DECL|method|setAccessTokenCache (IAccessTokenCache accessTokenCache)
specifier|public
name|void
name|setAccessTokenCache
parameter_list|(
name|IAccessTokenCache
name|accessTokenCache
parameter_list|)
block|{
name|this
operator|.
name|accessTokenCache
operator|=
name|accessTokenCache
expr_stmt|;
block|}
block|}
end_class

end_unit

