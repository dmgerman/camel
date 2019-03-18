begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ssh
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ssh
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPair
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
name|CamelContext
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
name|ResourceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|keyprovider
operator|.
name|AbstractKeyPairProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|util
operator|.
name|io
operator|.
name|IoUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|util
operator|.
name|security
operator|.
name|SecurityUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openssl
operator|.
name|PEMDecryptorProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openssl
operator|.
name|PEMEncryptedKeyPair
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openssl
operator|.
name|PEMKeyPair
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openssl
operator|.
name|PEMParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openssl
operator|.
name|jcajce
operator|.
name|JcaPEMKeyConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openssl
operator|.
name|jcajce
operator|.
name|JcePEMDecryptorProviderBuilder
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
comment|/**  * This host key provider loads private keys from the specified resources using  * {@link ResourceHelper}, and Camel's resource syntax for file:, classpath:, and http:.  * {@link ResourceHelper}, and Camel's resource syntax for file:, classpath:, and http:.  *  * Note that this class has a direct dependency on BouncyCastle and won't work  * unless it has been correctly registered as a security provider.  */
end_comment

begin_class
DECL|class|ResourceHelperKeyPairProvider
specifier|public
class|class
name|ResourceHelperKeyPairProvider
extends|extends
name|AbstractKeyPairProvider
block|{
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|resources
specifier|private
name|String
index|[]
name|resources
decl_stmt|;
DECL|field|passwordFinder
specifier|private
name|Supplier
argument_list|<
name|char
index|[]
argument_list|>
name|passwordFinder
decl_stmt|;
DECL|method|ResourceHelperKeyPairProvider ()
specifier|public
name|ResourceHelperKeyPairProvider
parameter_list|()
block|{     }
DECL|method|ResourceHelperKeyPairProvider (String[] resources, CamelContext camelContext)
specifier|public
name|ResourceHelperKeyPairProvider
parameter_list|(
name|String
index|[]
name|resources
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|resources
operator|=
name|resources
expr_stmt|;
block|}
DECL|method|ResourceHelperKeyPairProvider (String[] resources, Supplier<char[]> passwordFinder, CamelContext camelContext)
specifier|public
name|ResourceHelperKeyPairProvider
parameter_list|(
name|String
index|[]
name|resources
parameter_list|,
name|Supplier
argument_list|<
name|char
index|[]
argument_list|>
name|passwordFinder
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|resources
operator|=
name|resources
expr_stmt|;
name|this
operator|.
name|passwordFinder
operator|=
name|passwordFinder
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getResources ()
specifier|public
name|String
index|[]
name|getResources
parameter_list|()
block|{
return|return
name|resources
return|;
block|}
DECL|method|setResources (String[] resources)
specifier|public
name|void
name|setResources
parameter_list|(
name|String
index|[]
name|resources
parameter_list|)
block|{
name|this
operator|.
name|resources
operator|=
name|resources
expr_stmt|;
block|}
DECL|method|getPasswordFinder ()
specifier|public
name|Supplier
argument_list|<
name|char
index|[]
argument_list|>
name|getPasswordFinder
parameter_list|()
block|{
return|return
name|passwordFinder
return|;
block|}
DECL|method|setPasswordFinder (Supplier<char[]> passwordFinder)
specifier|public
name|void
name|setPasswordFinder
parameter_list|(
name|Supplier
argument_list|<
name|char
index|[]
argument_list|>
name|passwordFinder
parameter_list|)
block|{
name|this
operator|.
name|passwordFinder
operator|=
name|passwordFinder
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|loadKeys ()
specifier|public
name|Iterable
argument_list|<
name|KeyPair
argument_list|>
name|loadKeys
parameter_list|()
block|{
if|if
condition|(
operator|!
name|SecurityUtils
operator|.
name|isBouncyCastleRegistered
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"BouncyCastle must be registered as a JCE provider"
argument_list|)
throw|;
block|}
specifier|final
name|List
argument_list|<
name|KeyPair
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|this
operator|.
name|resources
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|resource
range|:
name|resources
control|)
block|{
name|PEMParser
name|r
init|=
literal|null
decl_stmt|;
name|InputStreamReader
name|isr
init|=
literal|null
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|camelContext
argument_list|,
name|resource
argument_list|)
expr_stmt|;
name|isr
operator|=
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|r
operator|=
operator|new
name|PEMParser
argument_list|(
name|isr
argument_list|)
expr_stmt|;
name|Object
name|o
init|=
name|r
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|JcaPEMKeyConverter
name|pemConverter
init|=
operator|new
name|JcaPEMKeyConverter
argument_list|()
decl_stmt|;
name|pemConverter
operator|.
name|setProvider
argument_list|(
literal|"BC"
argument_list|)
expr_stmt|;
if|if
condition|(
name|passwordFinder
operator|!=
literal|null
operator|&&
name|o
operator|instanceof
name|PEMEncryptedKeyPair
condition|)
block|{
name|JcePEMDecryptorProviderBuilder
name|decryptorBuilder
init|=
operator|new
name|JcePEMDecryptorProviderBuilder
argument_list|()
decl_stmt|;
name|PEMDecryptorProvider
name|pemDecryptor
init|=
name|decryptorBuilder
operator|.
name|build
argument_list|(
name|passwordFinder
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|o
operator|=
name|pemConverter
operator|.
name|getKeyPair
argument_list|(
operator|(
operator|(
name|PEMEncryptedKeyPair
operator|)
name|o
operator|)
operator|.
name|decryptKeyPair
argument_list|(
name|pemDecryptor
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|o
operator|instanceof
name|PEMKeyPair
condition|)
block|{
name|o
operator|=
name|pemConverter
operator|.
name|getKeyPair
argument_list|(
operator|(
name|PEMKeyPair
operator|)
name|o
argument_list|)
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
operator|(
name|KeyPair
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|KeyPair
condition|)
block|{
name|keys
operator|.
name|add
argument_list|(
operator|(
name|KeyPair
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to read key"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IoUtils
operator|.
name|closeQuietly
argument_list|(
name|r
argument_list|,
name|is
argument_list|,
name|isr
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|keys
return|;
block|}
block|}
end_class

end_unit

