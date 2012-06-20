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
name|KeyManager
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
name|KeyManagerFactory
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
comment|/**  * A representation of configuration options for creating and loading  * {@link KeyManager} instance(s).  */
end_comment

begin_class
DECL|class|KeyManagersParameters
specifier|public
class|class
name|KeyManagersParameters
extends|extends
name|JsseParameters
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
name|KeyManagersParameters
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * The key store configuration used to create the {@link KeyStoreParameters} that the      * {@link KeyManager}s produced by this object's configuration expose.      */
DECL|field|keyStore
specifier|protected
name|KeyStoreParameters
name|keyStore
decl_stmt|;
comment|/**      * The optional password for recovering keys in the key store. Used by the      * {@link KeyManagerFactory} that creates the {@link KeyManager}s      * represented by this object's configuration.      */
DECL|field|keyPassword
specifier|protected
name|String
name|keyPassword
decl_stmt|;
comment|/**      * The optional provider identifier for the {@link KeyManagerFactory} used to create      * the {@link KeyManager}s represented by this object's configuration.      */
DECL|field|provider
specifier|protected
name|String
name|provider
decl_stmt|;
comment|/**      * The optional algorithm name for the {@link KeyManagerFactory} used to create      * the {@link KeyManager}s represented by this object's configuration.  See the<a href=      * "http://download.oracle.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html"      *>Java Secure Socket Extension Reference Guide</a> for information about      * standard algorithm names.      */
DECL|field|algorithm
specifier|protected
name|String
name|algorithm
decl_stmt|;
comment|/**      * Creates {@link KeyManager}s based on this instance's configuration and the      * {@code KeyStore} produced by the configuration returned from      * {@link #getKeyStore()}. The {@code KeyManager}s are produced from a      * factory created by using the provider and algorithm identifiers returned      * by {@link #getProvider()} and {@link #getAlgorithm()}, respectively. If      * either of these methods returns null, the default JSSE value is used      * instead.      *       * @return the initialized {@code KeyManager}s      * @throws GeneralSecurityException if there is an error creating the      *             {@code KeyManager}s or in creating the {@code KeyStore}      * @throws IOException if there is an error loading the {@code KeyStore}      *      * @see KeyStoreParameters#createKeyStore()      */
DECL|method|createKeyManagers ()
specifier|public
name|KeyManager
index|[]
name|createKeyManagers
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
literal|"Creating KeyManager[] from KeyManagersParameters [{}]."
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|KeyManager
index|[]
name|keyManagers
decl_stmt|;
name|String
name|kmfAlgorithm
init|=
name|this
operator|.
name|parsePropertyValue
argument_list|(
name|this
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|kmfAlgorithm
operator|==
literal|null
condition|)
block|{
name|kmfAlgorithm
operator|=
name|KeyManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
expr_stmt|;
block|}
name|KeyManagerFactory
name|kmf
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
name|kmf
operator|=
name|KeyManagerFactory
operator|.
name|getInstance
argument_list|(
name|kmfAlgorithm
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|kmf
operator|=
name|KeyManagerFactory
operator|.
name|getInstance
argument_list|(
name|kmfAlgorithm
argument_list|,
name|this
operator|.
name|parsePropertyValue
argument_list|(
name|this
operator|.
name|getProvider
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"KeyManagerFactory [{}], initialized from [{}], is using provider [{}] and algorithm [{}]."
argument_list|,
operator|new
name|Object
index|[]
block|{
name|kmf
block|,
name|this
block|,
name|kmf
operator|.
name|getProvider
argument_list|()
block|,
name|kmf
operator|.
name|getAlgorithm
argument_list|()
block|}
argument_list|)
expr_stmt|;
name|char
index|[]
name|kmfPassword
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|getKeyPassword
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|kmfPassword
operator|=
name|this
operator|.
name|parsePropertyValue
argument_list|(
name|this
operator|.
name|getKeyPassword
argument_list|()
argument_list|)
operator|.
name|toCharArray
argument_list|()
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
name|kmf
operator|.
name|init
argument_list|(
name|ks
argument_list|,
name|kmfPassword
argument_list|)
expr_stmt|;
name|keyManagers
operator|=
name|kmf
operator|.
name|getKeyManagers
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"KeyManager[] [{}], initialized from KeyManagerFactory [{}]."
argument_list|,
name|keyManagers
argument_list|,
name|kmf
argument_list|)
expr_stmt|;
return|return
name|keyManagers
return|;
block|}
comment|/**      * @see #setKeyStore(KeyStoreParameters)      */
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
comment|/**      * Sets the key store configuration used to create the {@link KeyStore} that the      * {@link KeyManager}s produced by this object's configuration expose.      *       * @param value the configuration to use      */
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
comment|/**      * @see #setKeyPassword(String)      */
DECL|method|getKeyPassword ()
specifier|public
name|String
name|getKeyPassword
parameter_list|()
block|{
return|return
name|keyPassword
return|;
block|}
comment|/**      * Sets the optional password for recovering keys in the key store. Used by the      * {@link KeyManagerFactory} that creates the {@link KeyManager}s      * represented by this object's configuration.      *      * @param value the value to use      */
DECL|method|setKeyPassword (String value)
specifier|public
name|void
name|setKeyPassword
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|keyPassword
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * @see #setProvider(String)      */
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
comment|/**      * Sets the optional provider identifier for the {@link KeyManagerFactory} used to create      * the {@link KeyManager}s represented by this object's configuration.      *       * @param value the desired provider identifier or {@code null} to use the      *            highest priority provider implementing the algorithm      *      * @see Security#getProviders()      */
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
comment|/**      * @see KeyManagerFactory#getDefaultAlgorithm()      */
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
comment|/**      * Sets optional algorithm name for the {@link KeyManagerFactory} used to create      * the {@link KeyManager}s represented by this object's configuration.  See the<a href=      * "http://download.oracle.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html"      *>Java Secure Socket Extension Reference Guide</a> for information about      * standard algorithm names.      *       * @param value the desired algorithm or {@code null} to use default      *      * @see KeyManagerFactory#getDefaultAlgorithm()      */
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
literal|"KeyManagersParameters [keyStore="
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
literal|", keyPassword="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"********"
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
literal|", getContext()="
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|getCamelContext
argument_list|()
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

