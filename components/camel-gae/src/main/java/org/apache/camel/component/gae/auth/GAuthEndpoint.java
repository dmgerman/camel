begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.auth
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|auth
package|;
end_package

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
name|com
operator|.
name|google
operator|.
name|gdata
operator|.
name|client
operator|.
name|authn
operator|.
name|oauth
operator|.
name|GoogleOAuthHelper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gdata
operator|.
name|client
operator|.
name|authn
operator|.
name|oauth
operator|.
name|GoogleOAuthParameters
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gdata
operator|.
name|client
operator|.
name|authn
operator|.
name|oauth
operator|.
name|OAuthHelper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gdata
operator|.
name|client
operator|.
name|authn
operator|.
name|oauth
operator|.
name|OAuthHmacSha1Signer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gdata
operator|.
name|client
operator|.
name|authn
operator|.
name|oauth
operator|.
name|OAuthRsaSha1Signer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gdata
operator|.
name|client
operator|.
name|authn
operator|.
name|oauth
operator|.
name|OAuthSigner
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
name|Component
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|gae
operator|.
name|bind
operator|.
name|OutboundBinding
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
name|impl
operator|.
name|DefaultEndpoint
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
name|UriEndpoint
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
name|UriPath
import|;
end_import

begin_comment
comment|/**  * Represents a<a href="http://camel.apache.org/gauth.html">GAuth Endpoint</a>.  * Instances can have one of two names, either<code>authorize</code> for  * requesting an unauthorized request token or<code>upgrade</code> for  * upgrading an authorized request token to an access token. The corresponding  * endpoint URIs are<code>gauth:authorize</code> and<code>gauth:upgrade</code>  * , respectively.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"gauth"
argument_list|,
name|label
operator|=
literal|"cloud"
argument_list|)
DECL|class|GAuthEndpoint
specifier|public
class|class
name|GAuthEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|enum|Name
specifier|public
specifier|static
enum|enum
name|Name
block|{
comment|/**          * Name of the endpoint for requesting an unauthorized request token.           */
DECL|enumConstant|AUTHORIZE
name|AUTHORIZE
block|,
comment|/**          * Name of the endpoint for upgrading an authorized request token to an          * access token.          */
DECL|enumConstant|UPGRADE
name|UPGRADE
block|}
DECL|field|authorizeBinding
specifier|private
name|OutboundBinding
argument_list|<
name|GAuthEndpoint
argument_list|,
name|GoogleOAuthParameters
argument_list|,
name|GoogleOAuthParameters
argument_list|>
name|authorizeBinding
decl_stmt|;
DECL|field|upgradeBinding
specifier|private
name|OutboundBinding
argument_list|<
name|GAuthEndpoint
argument_list|,
name|GoogleOAuthParameters
argument_list|,
name|GoogleOAuthParameters
argument_list|>
name|upgradeBinding
decl_stmt|;
annotation|@
name|UriPath
DECL|field|name
specifier|private
name|Name
name|name
decl_stmt|;
annotation|@
name|UriParam
DECL|field|callback
specifier|private
name|String
name|callback
decl_stmt|;
annotation|@
name|UriParam
DECL|field|scope
specifier|private
name|String
name|scope
decl_stmt|;
annotation|@
name|UriParam
DECL|field|consumerKey
specifier|private
name|String
name|consumerKey
decl_stmt|;
annotation|@
name|UriParam
DECL|field|consumerSecret
specifier|private
name|String
name|consumerSecret
decl_stmt|;
DECL|field|keyLoader
specifier|private
name|GAuthKeyLoader
name|keyLoader
decl_stmt|;
DECL|field|service
specifier|private
name|GAuthService
name|service
decl_stmt|;
DECL|field|cachedKey
specifier|private
name|PrivateKey
name|cachedKey
decl_stmt|;
DECL|method|GAuthEndpoint (String endpointUri, Component component, String name)
specifier|public
name|GAuthEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|Name
operator|.
name|valueOf
argument_list|(
name|name
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|service
operator|=
operator|new
name|GAuthServiceImpl
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|getAuthorizeBinding ()
specifier|public
name|OutboundBinding
argument_list|<
name|GAuthEndpoint
argument_list|,
name|GoogleOAuthParameters
argument_list|,
name|GoogleOAuthParameters
argument_list|>
name|getAuthorizeBinding
parameter_list|()
block|{
return|return
name|authorizeBinding
return|;
block|}
comment|/**      * Sets the binding for<code>gauth:authorize</code> endpoints.        */
DECL|method|setAuthorizeBinding (OutboundBinding<GAuthEndpoint, GoogleOAuthParameters, GoogleOAuthParameters> authorizeBinding)
specifier|public
name|void
name|setAuthorizeBinding
parameter_list|(
name|OutboundBinding
argument_list|<
name|GAuthEndpoint
argument_list|,
name|GoogleOAuthParameters
argument_list|,
name|GoogleOAuthParameters
argument_list|>
name|authorizeBinding
parameter_list|)
block|{
name|this
operator|.
name|authorizeBinding
operator|=
name|authorizeBinding
expr_stmt|;
block|}
DECL|method|getUpgradeBinding ()
specifier|public
name|OutboundBinding
argument_list|<
name|GAuthEndpoint
argument_list|,
name|GoogleOAuthParameters
argument_list|,
name|GoogleOAuthParameters
argument_list|>
name|getUpgradeBinding
parameter_list|()
block|{
return|return
name|upgradeBinding
return|;
block|}
comment|/**      * Sets the binding for<code>gauth:upgrade</code> endpoints.       */
DECL|method|setUpgradeBinding (OutboundBinding<GAuthEndpoint, GoogleOAuthParameters, GoogleOAuthParameters> upgradeBinding)
specifier|public
name|void
name|setUpgradeBinding
parameter_list|(
name|OutboundBinding
argument_list|<
name|GAuthEndpoint
argument_list|,
name|GoogleOAuthParameters
argument_list|,
name|GoogleOAuthParameters
argument_list|>
name|upgradeBinding
parameter_list|)
block|{
name|this
operator|.
name|upgradeBinding
operator|=
name|upgradeBinding
expr_stmt|;
block|}
comment|/**      * Returns the component instance that created this endpoint.      */
DECL|method|getComponent ()
specifier|public
name|GAuthComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|GAuthComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
comment|/**      * Returns the endpoint name.      */
DECL|method|getName ()
specifier|public
name|Name
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * Returns the value of callback query parameter in the      *<code>gauth:authorize</code> endpoint URI.      */
DECL|method|getCallback ()
specifier|public
name|String
name|getCallback
parameter_list|()
block|{
return|return
name|callback
return|;
block|}
DECL|method|setCallback (String callback)
specifier|public
name|void
name|setCallback
parameter_list|(
name|String
name|callback
parameter_list|)
block|{
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
block|}
comment|/**      * Returns the value of the scope query parameter in      *<code>gauth:authorize</code> endpoint URI. This can be a single scope or      * a comma-separated list of scopes.      */
DECL|method|getScope ()
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
DECL|method|setScope (String services)
specifier|public
name|void
name|setScope
parameter_list|(
name|String
name|services
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|services
expr_stmt|;
block|}
comment|/**      * Returns the value of the scope query parameter as array.      * @see #getScope()      */
DECL|method|getScopeArray ()
specifier|public
name|String
index|[]
name|getScopeArray
parameter_list|()
block|{
return|return
name|getScope
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
return|;
block|}
comment|/**      * Returns the consumer key. If this endpoint's consumer key is      *<code>null</code> then {@link GAuthComponent#getConsumerKey()} is      * returned.      */
DECL|method|getConsumerKey ()
specifier|public
name|String
name|getConsumerKey
parameter_list|()
block|{
if|if
condition|(
name|consumerKey
operator|==
literal|null
condition|)
block|{
return|return
name|getComponent
argument_list|()
operator|.
name|getConsumerKey
argument_list|()
return|;
block|}
return|return
name|consumerKey
return|;
block|}
comment|/**      * Sets the consumer key. This key is generated when a web application is      * registered at Google.        *       * @param consumerKey      *            consumer key to set.      */
DECL|method|setConsumerKey (String consumerKey)
specifier|public
name|void
name|setConsumerKey
parameter_list|(
name|String
name|consumerKey
parameter_list|)
block|{
name|this
operator|.
name|consumerKey
operator|=
name|consumerKey
expr_stmt|;
block|}
comment|/**      * Returns the consumer secret. If this endpoint's consumer secret is      *<code>null</code> then {@link GAuthComponent#getConsumerSecret()} is      * returned.      */
DECL|method|getConsumerSecret ()
specifier|public
name|String
name|getConsumerSecret
parameter_list|()
block|{
if|if
condition|(
name|consumerSecret
operator|==
literal|null
condition|)
block|{
return|return
name|getComponent
argument_list|()
operator|.
name|getConsumerSecret
argument_list|()
return|;
block|}
return|return
name|consumerSecret
return|;
block|}
comment|/**      * Sets the consumer secret. This secret is generated when a web application      * is registered at Google. Only set the consumer secret if the HMAC-SHA1       * signature method shall be used.      */
DECL|method|setConsumerSecret (String consumerSecret)
specifier|public
name|void
name|setConsumerSecret
parameter_list|(
name|String
name|consumerSecret
parameter_list|)
block|{
name|this
operator|.
name|consumerSecret
operator|=
name|consumerSecret
expr_stmt|;
block|}
comment|/**      * Returns the key loader. If this endpoint's key loader is      *<code>null</code> then {@link GAuthComponent#getKeyLoader()} is      * returned.      */
DECL|method|getKeyLoader ()
specifier|public
name|GAuthKeyLoader
name|getKeyLoader
parameter_list|()
block|{
if|if
condition|(
name|keyLoader
operator|==
literal|null
condition|)
block|{
return|return
name|getComponent
argument_list|()
operator|.
name|getKeyLoader
argument_list|()
return|;
block|}
return|return
name|keyLoader
return|;
block|}
comment|/**      * Sets a key loader for loading a private key. A private key is required      * when the RSA-SHA1 signature method shall be used.          */
DECL|method|setKeyLoader (GAuthKeyLoader keyLoader)
specifier|public
name|void
name|setKeyLoader
parameter_list|(
name|GAuthKeyLoader
name|keyLoader
parameter_list|)
block|{
name|this
operator|.
name|keyLoader
operator|=
name|keyLoader
expr_stmt|;
block|}
DECL|method|getService ()
specifier|public
name|GAuthService
name|getService
parameter_list|()
block|{
return|return
name|service
return|;
block|}
comment|/**      * Sets the service that makes the remote calls to Google services. Testing      * code should inject a mock service here (using serviceRef in endpoint      * URI).      */
DECL|method|setService (GAuthService service)
specifier|public
name|void
name|setService
parameter_list|(
name|GAuthService
name|service
parameter_list|)
block|{
name|this
operator|.
name|service
operator|=
name|service
expr_stmt|;
block|}
comment|/**      * @throws UnsupportedOperationException      */
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"consumption from gauth endpoint not supported"
argument_list|)
throw|;
block|}
comment|/**      * Returns a {@link GAuthProducer}      */
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|GAuthProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Returns<code>true</code>.      */
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Creates an {@link OAuthHelper} configured with either an      * {@link OAuthHmacSha1Signer} or an {@link OAuthRsaSha1Signer}, depending      * on this endpoint's properties.      */
DECL|method|newOAuthHelper ()
name|OAuthHelper
name|newOAuthHelper
parameter_list|()
throws|throws
name|Exception
block|{
name|OAuthSigner
name|signer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getKeyLoader
argument_list|()
operator|==
literal|null
condition|)
block|{
name|signer
operator|=
operator|new
name|OAuthHmacSha1Signer
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|signer
operator|=
operator|new
name|OAuthRsaSha1Signer
argument_list|(
name|getPrivateKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|GoogleOAuthHelper
argument_list|(
name|signer
argument_list|)
return|;
block|}
DECL|method|getPrivateKey ()
specifier|private
specifier|synchronized
name|PrivateKey
name|getPrivateKey
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|cachedKey
operator|==
literal|null
condition|)
block|{
name|cachedKey
operator|=
name|getKeyLoader
argument_list|()
operator|.
name|loadPrivateKey
argument_list|()
expr_stmt|;
block|}
return|return
name|cachedKey
return|;
block|}
block|}
end_class

end_unit

