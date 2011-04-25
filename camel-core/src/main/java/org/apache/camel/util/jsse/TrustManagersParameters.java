begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.jsse
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|jsse
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
name|KeyStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Security
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
name|TrustManager
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
name|TrustManagerFactory
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

begin_class
DECL|class|TrustManagersParameters
specifier|public
class|class
name|TrustManagersParameters
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
name|TrustManagersParameters
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * The key store configuration used to create the {@link KeyStoreParameters} that the      * {@link TrustManager}s produced by this object's configuration expose.      */
DECL|field|keyStore
specifier|protected
name|KeyStoreParameters
name|keyStore
decl_stmt|;
comment|/**      * The optional provider identifier for the {@link TrustManagerFactory} used to create      * the {@link TrustManager}s represented by this object's configuration.      */
DECL|field|provider
specifier|protected
name|String
name|provider
decl_stmt|;
comment|/**      * The optional algorithm name for the {@link TrustManagerFactory} used to      * create the {@link TrustManager}s represented by this object's      * configuration. See the<a href=      * "http://download.oracle.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html"      *>Java Secure Socket Extension Reference Guide</a> for information about      * standard algorithm names.      */
DECL|field|algorithm
specifier|protected
name|String
name|algorithm
decl_stmt|;
comment|/**      * Creates {@link TrustManager}s based on this instance's configuration and the      * {@code KeyStore} produced by the configuration returned from      * {@link #getKeyStore()}. The {@code KeyManager}s are produced from a      * factory created by using the provider and algorithm identifiers returned      * by {@link #getProvider()} and {@link #getAlgorithm()}, respectively. If      * either of these methods returns null, the default JSSE value is used      * instead.      *       * @return the initialized {@code TrustManager}s      * @throws GeneralSecurityException if there is an error creating the      *             {@code TrustManagers}s or in creating the {@code KeyStore}      * @throws IOException if there is an error loading the {@code KeyStore}      *      * @see KeyStoreParameters#createKeyStore()      */
DECL|method|createTrustManagers ()
specifier|public
name|TrustManager
index|[]
name|createTrustManagers
parameter_list|()
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating TrustManager[] from TrustManagersParameters: {}"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|TrustManager
index|[]
name|trustManagers
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|getKeyStore
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|tmfAlgorithm
init|=
name|this
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
if|if
condition|(
name|tmfAlgorithm
operator|==
literal|null
condition|)
block|{
name|tmfAlgorithm
operator|=
name|TrustManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
expr_stmt|;
block|}
name|TrustManagerFactory
name|tmf
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|getProvider
argument_list|()
operator|==
literal|null
condition|)
block|{
name|tmf
operator|=
name|TrustManagerFactory
operator|.
name|getInstance
argument_list|(
name|tmfAlgorithm
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tmf
operator|=
name|TrustManagerFactory
operator|.
name|getInstance
argument_list|(
name|tmfAlgorithm
argument_list|,
name|this
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|KeyStore
name|ks
init|=
name|this
operator|.
name|getKeyStore
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|this
operator|.
name|getKeyStore
argument_list|()
operator|.
name|createKeyStore
argument_list|()
decl_stmt|;
name|tmf
operator|.
name|init
argument_list|(
name|ks
argument_list|)
expr_stmt|;
name|trustManagers
operator|=
name|tmf
operator|.
name|getTrustManagers
argument_list|()
expr_stmt|;
block|}
return|return
name|trustManagers
return|;
block|}
DECL|method|getKeyStore ()
specifier|public
name|KeyStoreParameters
name|getKeyStore
parameter_list|()
block|{
return|return
name|keyStore
return|;
block|}
comment|/**      * Sets the key store configuration used to create the {@link KeyStoreParameters} that the      * {@link TrustManager}s produced by this object's configuration expose.      *       * @param value the configuration to use      */
DECL|method|setKeyStore (KeyStoreParameters value)
specifier|public
name|void
name|setKeyStore
parameter_list|(
name|KeyStoreParameters
name|value
parameter_list|)
block|{
name|this
operator|.
name|keyStore
operator|=
name|value
expr_stmt|;
block|}
DECL|method|getProvider ()
specifier|public
name|String
name|getProvider
parameter_list|()
block|{
return|return
name|provider
return|;
block|}
comment|/**      * Sets the optional provider identifier for the {@link TrustManagerFactory}      * used to create the {@link TrustManager}s represented by this object's      * configuration.      *       * @param value the desired provider identifier or {@code null} to use the      *            highest priority provider implementing the algorithm      *                  * @see Security#getProviders()      */
DECL|method|setProvider (String value)
specifier|public
name|void
name|setProvider
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|provider
operator|=
name|value
expr_stmt|;
block|}
DECL|method|getAlgorithm ()
specifier|public
name|String
name|getAlgorithm
parameter_list|()
block|{
return|return
name|algorithm
return|;
block|}
comment|/**      * Sets optional algorithm name for the {@link TrustManagerFactory} used to create      * the {@link TrustManager}s represented by this object's configuration.  See the<a href=      * "http://download.oracle.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html"      *>Java Secure Socket Extension Reference Guide</a> for information about      * standard algorithm names.      *       * @param value the desired algorithm or {@code null} to use default      *       * @see TrustManagerFactory#getDefaultAlgorithm()      */
DECL|method|setAlgorithm (String value)
specifier|public
name|void
name|setAlgorithm
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|algorithm
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"TrustManagerType [keyStore="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|keyStore
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", provider="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|provider
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", algorithm="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|algorithm
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

