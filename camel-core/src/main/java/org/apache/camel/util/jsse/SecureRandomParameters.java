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
name|SecureRandom
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
DECL|class|SecureRandomParameters
specifier|public
class|class
name|SecureRandomParameters
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
name|SecureRandomParameters
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * The Random Number Generator algorithm identifier for the      * {@link SecureRandom} factory method used to create the      * {@link SecureRandom} represented by this object's configuration. See      * Appendix A in the<a href=      * "http://download.oracle.com/javase/6/docs/technotes/guides/security/crypto/CryptoSpec.html#AppA"      *> Java Cryptography Architecture API Specification&amp; Reference</a>      * for information about standard RNG algorithm names.      */
DECL|field|algorithm
specifier|protected
name|String
name|algorithm
decl_stmt|;
comment|/**      * The optional provider identifier for the {@link SecureRandom} factory      * method used to create the {@link SecureRandom} represented by this      * object's configuration.      */
DECL|field|provider
specifier|protected
name|String
name|provider
decl_stmt|;
comment|/**      * Returns a {@code SecureRandom} instance initialized using the configured      * algorithm and provider, if specified.      *       * @return the configured instance      *      * @throws GeneralSecurityException if the algorithm is not implemented by      *             any registered provider or if the identified provider does      *             not exist.      */
DECL|method|createSecureRandom ()
specifier|public
name|SecureRandom
name|createSecureRandom
parameter_list|()
throws|throws
name|GeneralSecurityException
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating SecureRandom from SecureRandomParameters: "
operator|+
name|this
argument_list|)
expr_stmt|;
block|}
name|SecureRandom
name|secureRandom
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|getProvider
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|secureRandom
operator|=
name|SecureRandom
operator|.
name|getInstance
argument_list|(
name|this
operator|.
name|getAlgorithm
argument_list|()
argument_list|,
name|this
operator|.
name|getProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|secureRandom
operator|=
name|SecureRandom
operator|.
name|getInstance
argument_list|(
name|this
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|secureRandom
return|;
block|}
comment|/**      * @see #setAlgorithm(String)      */
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
comment|/**      * Sets the Random Number Generator (RNG) algorithm identifier for the      * {@link SecureRandom} factory method used to create the      * {@link SecureRandom} represented by this object's configuration.      * See Appendix A in the<a href=      * "http://download.oracle.com/javase/6/docs/technotes/guides/security/crypto/CryptoSpec.html#AppA"      *> Java Cryptography Architecture Reference Guide</a>      * for information about standard RNG algorithm names.      *      * @param value the algorithm identifier      */
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
comment|/**      * Sets the optional provider identifier for the {@link SecureRandom}      * factory method used to create the {@link SecureRandom} represented by      * this object's configuration.      *       * @param value the provider identifier or {@code null} to use the highest      *            priority provider implementing the desired algorithm      *                  * @see Security#getProviders()      */
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
literal|"SecureRandomParameters [algorithm="
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

