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
name|KeyStoreException
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
name|KeyStoreParameters
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
literal|"coap"
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
DECL|field|keyStoreParameters
specifier|private
name|KeyStoreParameters
name|keyStoreParameters
decl_stmt|;
annotation|@
name|UriParam
DECL|field|keystore
specifier|private
name|KeyStore
name|keystore
decl_stmt|;
annotation|@
name|UriParam
DECL|field|trustStoreParameters
specifier|private
name|KeyStoreParameters
name|trustStoreParameters
decl_stmt|;
annotation|@
name|UriParam
DECL|field|truststore
specifier|private
name|KeyStore
name|truststore
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
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|javaType
operator|=
literal|"java.lang.String"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|password
specifier|private
name|char
index|[]
name|password
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
comment|/**      * The KeyStoreParameters object to use with TLS to configure the keystore. Alternatively, a "keystore"       * parameter can be directly configured instead. An alias and password should also be configured on the route definition.      */
DECL|method|getKeyStoreParameters ()
specifier|public
name|KeyStoreParameters
name|getKeyStoreParameters
parameter_list|()
block|{
return|return
name|keyStoreParameters
return|;
block|}
DECL|method|setKeyStoreParameters (KeyStoreParameters keyStoreParameters)
specifier|public
name|void
name|setKeyStoreParameters
parameter_list|(
name|KeyStoreParameters
name|keyStoreParameters
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|this
operator|.
name|keyStoreParameters
operator|=
name|keyStoreParameters
expr_stmt|;
if|if
condition|(
name|keyStoreParameters
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|keystore
operator|=
name|keyStoreParameters
operator|.
name|createKeyStore
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * The KeyStoreParameters object to use with TLS to configure the truststore. Alternatively, a "truststore"       * object can be directly configured instead. All certificates in the truststore are used to establish trust.      */
DECL|method|getTrustStoreParameters ()
specifier|public
name|KeyStoreParameters
name|getTrustStoreParameters
parameter_list|()
block|{
return|return
name|trustStoreParameters
return|;
block|}
DECL|method|setTrustStoreParameters (KeyStoreParameters trustStoreParameters)
specifier|public
name|void
name|setTrustStoreParameters
parameter_list|(
name|KeyStoreParameters
name|trustStoreParameters
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|this
operator|.
name|trustStoreParameters
operator|=
name|trustStoreParameters
expr_stmt|;
if|if
condition|(
name|trustStoreParameters
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|truststore
operator|=
name|trustStoreParameters
operator|.
name|createKeyStore
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Gets the TLS key store. Alternatively, a KeyStoreParameters object can be configured instead.      * An alias and password should also be configured on the route definition.      */
DECL|method|getKeystore ()
specifier|public
name|KeyStore
name|getKeystore
parameter_list|()
block|{
return|return
name|keystore
return|;
block|}
comment|/**      * Sets the TLS key store. Alternatively, a KeyStoreParameters object can be configured instead.      * An alias and password should also be configured on the route definition.      */
DECL|method|setKeystore (KeyStore keystore)
specifier|public
name|void
name|setKeystore
parameter_list|(
name|KeyStore
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
comment|/**      * Gets the TLS trust store. Alternatively, a "trustStoreParameters" object can be configured instead.      * All certificates in the truststore are used to establish trust.      */
DECL|method|getTruststore ()
specifier|public
name|KeyStore
name|getTruststore
parameter_list|()
block|{
return|return
name|truststore
return|;
block|}
comment|/**      * Sets the TLS trust store. Alternatively, a "trustStoreParameters" object can be configured instead.      * All certificates in the truststore are used to establish trust.      */
DECL|method|setTruststore (KeyStore truststore)
specifier|public
name|void
name|setTruststore
parameter_list|(
name|KeyStore
name|truststore
parameter_list|)
block|{
name|this
operator|.
name|truststore
operator|=
name|truststore
expr_stmt|;
block|}
comment|/**      * Gets the alias used to query the KeyStore for the private key and certificate.      */
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
comment|/**      * Sets the alias used to query the KeyStore for the private key and certificate.      */
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
comment|/**      * Gets the password used to access an aliased {@link PrivateKey} in the KeyStore.      */
DECL|method|getPassword ()
specifier|public
name|char
index|[]
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/**      * Sets the password used to access an aliased {@link PrivateKey} in the KeyStore.      */
DECL|method|setPassword (char[] password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|char
index|[]
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
comment|/**      * Gets the cipherSuites String. This is a comma separated String of ciphersuites to configure.      */
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
comment|/**      * Sets the cipherSuites String. This is a comma separated String of ciphersuites to configure.      */
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
specifier|public
name|String
index|[]
name|getConfiguredCipherSuites
parameter_list|()
block|{
return|return
name|configuredCipherSuites
return|;
block|}
DECL|method|getTrustedCerts ()
specifier|public
name|Certificate
index|[]
name|getTrustedCerts
parameter_list|()
throws|throws
name|KeyStoreException
block|{
name|Enumeration
argument_list|<
name|String
argument_list|>
name|aliases
init|=
name|truststore
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
name|truststore
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
block|}
end_class

end_unit

