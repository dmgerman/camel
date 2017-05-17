begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.box.springboot
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
name|springboot
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
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * For uploading downloading and managing files folders groups collaborations  * etc on box DOT com.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.box"
argument_list|)
DECL|class|BoxComponentConfiguration
specifier|public
class|class
name|BoxComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * To use the shared configuration      */
DECL|field|configuration
specifier|private
name|BoxConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|BoxConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( BoxConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|BoxConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|class|BoxConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|BoxConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
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
operator|.
name|class
decl_stmt|;
comment|/**          * What kind of operation to perform          *           * @param apiNamethe          *            API Name to set          */
DECL|field|apiName
specifier|private
name|BoxApiName
name|apiName
decl_stmt|;
comment|/**          * What sub operation to use for the selected operation          *           * @param methodNamethe          *            methodName to set          */
DECL|field|methodName
specifier|private
name|String
name|methodName
decl_stmt|;
comment|/**          * The enterprise ID to use for an App Enterprise.          *           * @param enterpriseIdthe          *            enterpriseId to set          */
DECL|field|enterpriseId
specifier|private
name|String
name|enterpriseId
decl_stmt|;
comment|/**          * The user ID to use for an App User.          *           * @param userIdthe          *            userId to set          */
DECL|field|userId
specifier|private
name|String
name|userId
decl_stmt|;
comment|/**          * The ID for public key for validating the JWT signature.          *           * @param publicKeyIdthe          *            publicKeyId to set          */
DECL|field|publicKeyId
specifier|private
name|String
name|publicKeyId
decl_stmt|;
comment|/**          * The private key for generating the JWT signature.          *           * @param privateKeythe          *            privateKey to set          */
DECL|field|privateKeyFile
specifier|private
name|String
name|privateKeyFile
decl_stmt|;
comment|/**          * The password for the private key.          *           * @param privateKeyPasswordthe          *            privateKeyPassword to set          */
DECL|field|privateKeyPassword
specifier|private
name|String
name|privateKeyPassword
decl_stmt|;
comment|/**          * The type of authentication for connection.          *<p>          * Types of Authentication:          *<ul>          *<li>STANDARD_AUTHENTICATION - OAuth 2.0 (3-legged)</li>          *<li>SERVER_AUTHENTICATION - OAuth 2.0 with JSON Web Tokens</li>          *</ul>          *           * @param authenticationTypethe          *            authenticationType to set          */
DECL|field|authenticationType
specifier|private
name|String
name|authenticationType
init|=
literal|"APP_USER_AUTHENTICATION"
decl_stmt|;
comment|/**          * Box application client ID          *           * @param clientIdthe          *            clientId to set          */
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
comment|/**          * Box application client secret          *           * @param clientSecretthe          *            clientSecret to set          */
DECL|field|clientSecret
specifier|private
name|String
name|clientSecret
decl_stmt|;
comment|/**          * Box user name, MUST be provided          *           * @param userNamethe          *            userName to set          */
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
comment|/**          * Box user password, MUST be provided if authSecureStorage is not set,          * or returns null on first call          *           * @param userPasswordthe          *            userPassword to set          */
DECL|field|userPassword
specifier|private
name|String
name|userPassword
decl_stmt|;
comment|/**          * Custom HTTP params for settings like proxy host          *           * @param httpParamsthe          *            httpParams to set          */
DECL|field|httpParams
specifier|private
name|Map
name|httpParams
decl_stmt|;
comment|/**          * To configure security using SSLContextParameters.          *           * @param sslContextParametersthe          *            sslContextParameters to set          */
annotation|@
name|NestedConfigurationProperty
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
comment|/**          * Custom Access Token Cache for storing and retrieving access tokens.          *           * @param accessTokenCache          *            - the Custom Access Token Cache          */
DECL|field|accessTokenCache
specifier|private
name|IAccessTokenCache
name|accessTokenCache
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
DECL|method|setPrivateKeyFile (String privateKeyFile)
specifier|public
name|void
name|setPrivateKeyFile
parameter_list|(
name|String
name|privateKeyFile
parameter_list|)
block|{
name|this
operator|.
name|privateKeyFile
operator|=
name|privateKeyFile
expr_stmt|;
block|}
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
DECL|method|setAuthenticationType (String authenticationType)
specifier|public
name|void
name|setAuthenticationType
parameter_list|(
name|String
name|authenticationType
parameter_list|)
block|{
name|this
operator|.
name|authenticationType
operator|=
name|authenticationType
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
DECL|method|getHttpParams ()
specifier|public
name|Map
name|getHttpParams
parameter_list|()
block|{
return|return
name|httpParams
return|;
block|}
DECL|method|setHttpParams (Map httpParams)
specifier|public
name|void
name|setHttpParams
parameter_list|(
name|Map
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
DECL|method|setSslContextParameters ( SSLContextParameters sslContextParameters)
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
block|}
end_class

end_unit

