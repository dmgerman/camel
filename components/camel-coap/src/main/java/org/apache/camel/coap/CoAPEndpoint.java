begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.coap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|coap
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
name|InetSocketAddress
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
name|PublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
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
name|support
operator|.
name|jsse
operator|.
name|ClientAuthentication
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
name|support
operator|.
name|jsse
operator|.
name|KeyManagersParameters
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
name|support
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
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|CoapServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|scandium
operator|.
name|DTLSConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|scandium
operator|.
name|config
operator|.
name|DtlsConnectorConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|scandium
operator|.
name|dtls
operator|.
name|CertificateType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|scandium
operator|.
name|dtls
operator|.
name|pskstore
operator|.
name|PskStore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|scandium
operator|.
name|dtls
operator|.
name|rpkstore
operator|.
name|TrustedRpkStore
import|;
end_import

begin_comment
comment|/**  * The coap component is used for sending and receiving messages from COAP capable devices.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.16.0"
argument_list|,
name|scheme
operator|=
literal|"coap,coaps,coap+tcp,coaps+tcp"
argument_list|,
name|title
operator|=
literal|"CoAP"
argument_list|,
name|syntax
operator|=
literal|"coap:uri"
argument_list|,
name|label
operator|=
literal|"iot"
argument_list|)
DECL|class|CoAPEndpoint
specifier|public
class|class
name|CoAPEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
DECL|field|uri
specifier|private
name|URI
name|uri
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|coapMethodRestrict
specifier|private
name|String
name|coapMethodRestrict
decl_stmt|;
annotation|@
name|UriParam
DECL|field|privateKey
specifier|private
name|PrivateKey
name|privateKey
decl_stmt|;
annotation|@
name|UriParam
DECL|field|publicKey
specifier|private
name|PublicKey
name|publicKey
decl_stmt|;
annotation|@
name|UriParam
DECL|field|trustedRpkStore
specifier|private
name|TrustedRpkStore
name|trustedRpkStore
decl_stmt|;
annotation|@
name|UriParam
DECL|field|pskStore
specifier|private
name|PskStore
name|pskStore
decl_stmt|;
annotation|@
name|UriParam
DECL|field|cipherSuites
specifier|private
name|String
name|cipherSuites
decl_stmt|;
DECL|field|configuredCipherSuites
specifier|private
name|String
index|[]
name|configuredCipherSuites
decl_stmt|;
annotation|@
name|UriParam
DECL|field|clientAuthentication
specifier|private
name|String
name|clientAuthentication
decl_stmt|;
annotation|@
name|UriParam
DECL|field|alias
specifier|private
name|String
name|alias
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
DECL|field|component
specifier|private
name|CoAPComponent
name|component
decl_stmt|;
DECL|method|CoAPEndpoint (String uri, CoAPComponent component)
specifier|public
name|CoAPEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|CoAPComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
try|try
block|{
name|this
operator|.
name|uri
operator|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|net
operator|.
name|URISyntaxException
name|use
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
literal|null
expr_stmt|;
block|}
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
DECL|method|setCoapMethodRestrict (String coapMethodRestrict)
specifier|public
name|void
name|setCoapMethodRestrict
parameter_list|(
name|String
name|coapMethodRestrict
parameter_list|)
block|{
name|this
operator|.
name|coapMethodRestrict
operator|=
name|coapMethodRestrict
expr_stmt|;
block|}
comment|/**      * Comma separated list of methods that the CoAP consumer will bind to. The default is to bind to all methods (DELETE, GET, POST, PUT).      */
DECL|method|getCoapMethodRestrict ()
specifier|public
name|String
name|getCoapMethodRestrict
parameter_list|()
block|{
return|return
name|this
operator|.
name|coapMethodRestrict
return|;
block|}
annotation|@
name|Override
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
name|CoAPProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
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
return|return
operator|new
name|CoAPConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|setUri (URI u)
specifier|public
name|void
name|setUri
parameter_list|(
name|URI
name|u
parameter_list|)
block|{
name|uri
operator|=
name|u
expr_stmt|;
block|}
comment|/**      * The URI for the CoAP endpoint      */
DECL|method|getUri ()
specifier|public
name|URI
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|getCoapServer ()
specifier|public
name|CoapServer
name|getCoapServer
parameter_list|()
throws|throws
name|IOException
throws|,
name|GeneralSecurityException
block|{
return|return
name|component
operator|.
name|getServer
argument_list|(
name|getUri
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|,
name|this
argument_list|)
return|;
block|}
comment|/**      * Gets the alias used to query the KeyStore for the private key and certificate. This parameter is used      * when we are enabling TLS with certificates on the service side, and similarly on the client side when      * TLS is used with certificates and client authentication. If the parameter is not specified then the      * default behavior is to use the first alias in the keystore that contains a key entry. This configuration      * parameter does not apply to configuring TLS via a Raw Public Key or a Pre-Shared Key.      */
DECL|method|getAlias ()
specifier|public
name|String
name|getAlias
parameter_list|()
block|{
return|return
name|alias
return|;
block|}
comment|/**      * Sets the alias used to query the KeyStore for the private key and certificate. This parameter is used      * when we are enabling TLS with certificates on the service side, and similarly on the client side when      * TLS is used with certificates and client authentication. If the parameter is not specified then the      * default behavior is to use the first alias in the keystore that contains a key entry. This configuration      * parameter does not apply to configuring TLS via a Raw Public Key or a Pre-Shared Key.      */
DECL|method|setAlias (String alias)
specifier|public
name|void
name|setAlias
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
name|this
operator|.
name|alias
operator|=
name|alias
expr_stmt|;
block|}
comment|/**      * Get the SSLContextParameters object for setting up TLS. This is required for coaps+tcp, and for coaps when we are      * using certificates for TLS (as opposed to RPK or PKS).      */
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
comment|/**      * Set the SSLContextParameters object for setting up TLS. This is required for coaps+tcp, and for coaps when we are      * using certificates for TLS (as opposed to RPK or PKS).      */
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
comment|/**      * Get the TrustedRpkStore to use to determine trust in raw public keys.      */
DECL|method|getTrustedRpkStore ()
specifier|public
name|TrustedRpkStore
name|getTrustedRpkStore
parameter_list|()
block|{
return|return
name|trustedRpkStore
return|;
block|}
comment|/**      * Set the TrustedRpkStore to use to determine trust in raw public keys.      */
DECL|method|setTrustedRpkStore (TrustedRpkStore trustedRpkStore)
specifier|public
name|void
name|setTrustedRpkStore
parameter_list|(
name|TrustedRpkStore
name|trustedRpkStore
parameter_list|)
block|{
name|this
operator|.
name|trustedRpkStore
operator|=
name|trustedRpkStore
expr_stmt|;
block|}
comment|/**      * Get the PskStore to use for pre-shared key.      */
DECL|method|getPskStore ()
specifier|public
name|PskStore
name|getPskStore
parameter_list|()
block|{
return|return
name|pskStore
return|;
block|}
comment|/**      * Set the PskStore to use for pre-shared key.      */
DECL|method|setPskStore (PskStore pskStore)
specifier|public
name|void
name|setPskStore
parameter_list|(
name|PskStore
name|pskStore
parameter_list|)
block|{
name|this
operator|.
name|pskStore
operator|=
name|pskStore
expr_stmt|;
block|}
comment|/**      * Get the configured private key for use with Raw Public Key.      */
DECL|method|getPrivateKey ()
specifier|public
name|PrivateKey
name|getPrivateKey
parameter_list|()
block|{
return|return
name|privateKey
return|;
block|}
comment|/**      * Set the configured private key for use with Raw Public Key.      */
DECL|method|setPrivateKey (PrivateKey privateKey)
specifier|public
name|void
name|setPrivateKey
parameter_list|(
name|PrivateKey
name|privateKey
parameter_list|)
block|{
name|this
operator|.
name|privateKey
operator|=
name|privateKey
expr_stmt|;
block|}
comment|/**      * Get the configured public key for use with Raw Public Key.      */
DECL|method|getPublicKey ()
specifier|public
name|PublicKey
name|getPublicKey
parameter_list|()
block|{
return|return
name|publicKey
return|;
block|}
comment|/**      * Set the configured public key for use with Raw Public Key.      */
DECL|method|setPublicKey (PublicKey publicKey)
specifier|public
name|void
name|setPublicKey
parameter_list|(
name|PublicKey
name|publicKey
parameter_list|)
block|{
name|this
operator|.
name|publicKey
operator|=
name|publicKey
expr_stmt|;
block|}
comment|/**      * Gets the cipherSuites String. This is a comma separated String of ciphersuites to configure. If it is not      * specified, then it falls back to getting the ciphersuites from the sslContextParameters object.      */
DECL|method|getCipherSuites ()
specifier|public
name|String
name|getCipherSuites
parameter_list|()
block|{
return|return
name|cipherSuites
return|;
block|}
comment|/**      * Sets the cipherSuites String. This is a comma separated String of ciphersuites to configure. If it is not      * specified, then it falls back to getting the ciphersuites from the sslContextParameters object.      */
DECL|method|setCipherSuites (String cipherSuites)
specifier|public
name|void
name|setCipherSuites
parameter_list|(
name|String
name|cipherSuites
parameter_list|)
block|{
name|this
operator|.
name|cipherSuites
operator|=
name|cipherSuites
expr_stmt|;
if|if
condition|(
name|cipherSuites
operator|!=
literal|null
condition|)
block|{
name|configuredCipherSuites
operator|=
name|cipherSuites
operator|.
name|split
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getConfiguredCipherSuites ()
specifier|private
name|String
index|[]
name|getConfiguredCipherSuites
parameter_list|()
block|{
if|if
condition|(
name|configuredCipherSuites
operator|!=
literal|null
condition|)
block|{
return|return
name|configuredCipherSuites
return|;
block|}
elseif|else
if|if
condition|(
name|sslContextParameters
operator|!=
literal|null
operator|&&
name|sslContextParameters
operator|.
name|getCipherSuites
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|sslContextParameters
operator|.
name|getCipherSuites
argument_list|()
operator|.
name|getCipherSuite
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Gets the configuration options for server-side client-authentication requirements. The value is      * either null or one of NONE, WANT, REQUIRE. If this value is not specified, then it falls back      * to checking the sslContextParameters.getServerParameters().getClientAuthentication() value.      */
DECL|method|getClientAuthentication ()
specifier|public
name|String
name|getClientAuthentication
parameter_list|()
block|{
return|return
name|clientAuthentication
return|;
block|}
comment|/**      * Sets the configuration options for server-side client-authentication requirements.      * The value must be one of NONE, WANT, REQUIRE. If this value is not specified, then it falls back      * to checking the sslContextParameters.getServerParameters().getClientAuthentication() value.      */
DECL|method|setClientAuthentication (String clientAuthentication)
specifier|public
name|void
name|setClientAuthentication
parameter_list|(
name|String
name|clientAuthentication
parameter_list|)
block|{
name|this
operator|.
name|clientAuthentication
operator|=
name|clientAuthentication
expr_stmt|;
block|}
DECL|method|isClientAuthenticationRequired ()
specifier|public
name|boolean
name|isClientAuthenticationRequired
parameter_list|()
block|{
name|String
name|clientAuth
init|=
name|clientAuthentication
decl_stmt|;
if|if
condition|(
name|clientAuth
operator|==
literal|null
operator|&&
name|sslContextParameters
operator|!=
literal|null
operator|&&
name|sslContextParameters
operator|.
name|getServerParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|clientAuth
operator|=
name|sslContextParameters
operator|.
name|getServerParameters
argument_list|()
operator|.
name|getClientAuthentication
argument_list|()
expr_stmt|;
block|}
return|return
name|clientAuth
operator|!=
literal|null
operator|&&
name|ClientAuthentication
operator|.
name|valueOf
argument_list|(
name|clientAuth
argument_list|)
operator|==
name|ClientAuthentication
operator|.
name|REQUIRE
return|;
block|}
DECL|method|isClientAuthenticationWanted ()
specifier|public
name|boolean
name|isClientAuthenticationWanted
parameter_list|()
block|{
name|String
name|clientAuth
init|=
name|clientAuthentication
decl_stmt|;
if|if
condition|(
name|clientAuth
operator|==
literal|null
operator|&&
name|sslContextParameters
operator|!=
literal|null
operator|&&
name|sslContextParameters
operator|.
name|getServerParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|clientAuth
operator|=
name|sslContextParameters
operator|.
name|getServerParameters
argument_list|()
operator|.
name|getClientAuthentication
argument_list|()
expr_stmt|;
block|}
return|return
name|clientAuth
operator|!=
literal|null
operator|&&
name|ClientAuthentication
operator|.
name|valueOf
argument_list|(
name|clientAuth
argument_list|)
operator|==
name|ClientAuthentication
operator|.
name|WANT
return|;
block|}
comment|/**      * Get all the certificates contained in the sslContextParameters truststore      */
DECL|method|getTrustedCerts ()
specifier|private
name|Certificate
index|[]
name|getTrustedCerts
parameter_list|()
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
if|if
condition|(
name|sslContextParameters
operator|!=
literal|null
operator|&&
name|sslContextParameters
operator|.
name|getTrustManagers
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|KeyStore
name|trustStore
init|=
name|sslContextParameters
operator|.
name|getTrustManagers
argument_list|()
operator|.
name|getKeyStore
argument_list|()
operator|.
name|createKeyStore
argument_list|()
decl_stmt|;
name|Enumeration
argument_list|<
name|String
argument_list|>
name|aliases
init|=
name|trustStore
operator|.
name|aliases
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Certificate
argument_list|>
name|trustCerts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
name|alias
init|=
name|aliases
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|X509Certificate
name|cert
init|=
operator|(
name|X509Certificate
operator|)
name|trustStore
operator|.
name|getCertificate
argument_list|(
name|alias
argument_list|)
decl_stmt|;
if|if
condition|(
name|cert
operator|!=
literal|null
condition|)
block|{
name|trustCerts
operator|.
name|add
argument_list|(
name|cert
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|trustCerts
operator|.
name|toArray
argument_list|(
operator|new
name|Certificate
index|[
literal|0
index|]
argument_list|)
return|;
block|}
return|return
operator|new
name|Certificate
index|[
literal|0
index|]
return|;
block|}
DECL|method|enableTLS (URI uri)
specifier|public
specifier|static
name|boolean
name|enableTLS
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
return|return
literal|"coaps"
operator|.
name|equals
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
return|;
block|}
DECL|method|enableTCP (URI uri)
specifier|public
specifier|static
name|boolean
name|enableTCP
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
return|return
name|uri
operator|.
name|getScheme
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"+tcp"
argument_list|)
return|;
block|}
DECL|method|createDTLSConnector (InetSocketAddress address, boolean client)
specifier|public
name|DTLSConnector
name|createDTLSConnector
parameter_list|(
name|InetSocketAddress
name|address
parameter_list|,
name|boolean
name|client
parameter_list|)
throws|throws
name|IOException
block|{
name|DtlsConnectorConfig
operator|.
name|Builder
name|builder
init|=
operator|new
name|DtlsConnectorConfig
operator|.
name|Builder
argument_list|()
decl_stmt|;
if|if
condition|(
name|client
condition|)
block|{
if|if
condition|(
name|trustedRpkStore
operator|==
literal|null
operator|&&
name|sslContextParameters
operator|==
literal|null
operator|&&
name|pskStore
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Either a trustedRpkStore, sslContextParameters or pskStore object "
operator|+
literal|"must be configured for a TLS client"
argument_list|)
throw|;
block|}
name|builder
operator|.
name|setClientOnly
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|privateKey
operator|==
literal|null
operator|&&
name|sslContextParameters
operator|==
literal|null
operator|&&
name|pskStore
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Either a privateKey, sslContextParameters or pskStore object "
operator|+
literal|"must be configured for a TLS service"
argument_list|)
throw|;
block|}
if|if
condition|(
name|privateKey
operator|!=
literal|null
operator|&&
name|publicKey
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"A public key must be configured to use a Raw Public Key with TLS"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|(
name|isClientAuthenticationRequired
argument_list|()
operator|||
name|isClientAuthenticationWanted
argument_list|()
operator|)
operator|&&
operator|(
name|sslContextParameters
operator|==
literal|null
operator|||
name|sslContextParameters
operator|.
name|getTrustManagers
argument_list|()
operator|==
literal|null
operator|)
operator|&&
name|publicKey
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"A truststore must be configured to support TLS client authentication"
argument_list|)
throw|;
block|}
name|builder
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setClientAuthenticationRequired
argument_list|(
name|isClientAuthenticationRequired
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setClientAuthenticationWanted
argument_list|(
name|isClientAuthenticationWanted
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
comment|// Configure the identity if the sslContextParameters or privateKey parameter is specified
if|if
condition|(
name|sslContextParameters
operator|!=
literal|null
operator|&&
name|sslContextParameters
operator|.
name|getKeyManagers
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|KeyManagersParameters
name|keyManagers
init|=
name|sslContextParameters
operator|.
name|getKeyManagers
argument_list|()
decl_stmt|;
name|KeyStore
name|keyStore
init|=
name|keyManagers
operator|.
name|getKeyStore
argument_list|()
operator|.
name|createKeyStore
argument_list|()
decl_stmt|;
comment|// Use the configured alias or fall back to the first alias in the keystore that contains a key
name|String
name|alias
init|=
name|getAlias
argument_list|()
decl_stmt|;
if|if
condition|(
name|alias
operator|==
literal|null
condition|)
block|{
name|Enumeration
argument_list|<
name|String
argument_list|>
name|aliases
init|=
name|keyStore
operator|.
name|aliases
argument_list|()
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
name|ksAlias
init|=
name|aliases
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|keyStore
operator|.
name|isKeyEntry
argument_list|(
name|ksAlias
argument_list|)
condition|)
block|{
name|alias
operator|=
name|ksAlias
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|alias
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The sslContextParameters keystore must contain a key entry"
argument_list|)
throw|;
block|}
name|PrivateKey
name|privateKey
init|=
operator|(
name|PrivateKey
operator|)
name|keyStore
operator|.
name|getKey
argument_list|(
name|alias
argument_list|,
name|keyManagers
operator|.
name|getKeyPassword
argument_list|()
operator|.
name|toCharArray
argument_list|()
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setIdentity
argument_list|(
name|privateKey
argument_list|,
name|keyStore
operator|.
name|getCertificateChain
argument_list|(
name|alias
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|privateKey
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setIdentity
argument_list|(
name|privateKey
argument_list|,
name|publicKey
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pskStore
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setPskStore
argument_list|(
name|pskStore
argument_list|)
expr_stmt|;
block|}
comment|// Add all certificates from the truststore
name|Certificate
index|[]
name|certs
init|=
name|getTrustedCerts
argument_list|()
decl_stmt|;
if|if
condition|(
name|certs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|builder
operator|.
name|setTrustStore
argument_list|(
name|certs
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|trustedRpkStore
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setTrustCertificateTypes
argument_list|(
name|CertificateType
operator|.
name|RAW_PUBLIC_KEY
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setRpkTrustStore
argument_list|(
name|trustedRpkStore
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Error in configuring TLS"
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|getConfiguredCipherSuites
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setSupportedCipherSuites
argument_list|(
name|getConfiguredCipherSuites
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|DTLSConnector
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

