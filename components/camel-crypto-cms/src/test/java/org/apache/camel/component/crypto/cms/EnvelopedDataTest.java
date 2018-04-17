begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.cms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|crypto
operator|.
name|cms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|io
operator|.
name|UnsupportedEncodingException
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
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
name|crypto
operator|.
name|cms
operator|.
name|crypt
operator|.
name|DefaultEnvelopedDataDecryptorConfiguration
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
name|crypto
operator|.
name|cms
operator|.
name|crypt
operator|.
name|DefaultKeyTransRecipientInfo
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
name|crypto
operator|.
name|cms
operator|.
name|crypt
operator|.
name|EnvelopedDataDecryptor
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
name|crypto
operator|.
name|cms
operator|.
name|crypt
operator|.
name|EnvelopedDataEncryptor
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
name|crypto
operator|.
name|cms
operator|.
name|crypt
operator|.
name|EnvelopedDataEncryptorConfiguration
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
name|crypto
operator|.
name|cms
operator|.
name|crypt
operator|.
name|RecipientInfo
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
name|crypto
operator|.
name|cms
operator|.
name|exception
operator|.
name|CryptoCmsException
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
name|crypto
operator|.
name|cms
operator|.
name|exception
operator|.
name|CryptoCmsFormatException
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
name|crypto
operator|.
name|cms
operator|.
name|exception
operator|.
name|CryptoCmsNoCertificateForRecipientsException
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
name|crypto
operator|.
name|cms
operator|.
name|exception
operator|.
name|CryptoCmsNoKeyOrCertificateForAliasException
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
name|crypto
operator|.
name|cms
operator|.
name|util
operator|.
name|ExchangeUtil
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
name|crypto
operator|.
name|cms
operator|.
name|util
operator|.
name|KeystoreUtil
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
name|KeyStoreParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|jce
operator|.
name|provider
operator|.
name|BouncyCastleProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|EnvelopedDataTest
specifier|public
class|class
name|EnvelopedDataTest
block|{
annotation|@
name|BeforeClass
DECL|method|setUpProvider ()
specifier|public
specifier|static
name|void
name|setUpProvider
parameter_list|()
block|{
name|Security
operator|.
name|addProvider
argument_list|(
operator|new
name|BouncyCastleProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeDESedeCBClength192 ()
specifier|public
name|void
name|executeDESedeCBClength192
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"DESede/CBC/PKCS5Padding"
argument_list|,
literal|192
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeDESedeCBClength128 ()
specifier|public
name|void
name|executeDESedeCBClength128
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"DESede/CBC/PKCS5Padding"
argument_list|,
literal|128
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeDESCBCkeyLength64 ()
specifier|public
name|void
name|executeDESCBCkeyLength64
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"DES/CBC/PKCS5Padding"
argument_list|,
literal|64
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeDESCBCkeyLength56 ()
specifier|public
name|void
name|executeDESCBCkeyLength56
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"DES/CBC/PKCS5Padding"
argument_list|,
literal|56
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeCAST5CBCkeyLength128 ()
specifier|public
name|void
name|executeCAST5CBCkeyLength128
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"CAST5/CBC/PKCS5Padding"
argument_list|,
literal|128
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeCAST5CBCkeyLength120 ()
specifier|public
name|void
name|executeCAST5CBCkeyLength120
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"CAST5/CBC/PKCS5Padding"
argument_list|,
literal|120
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeCAST5CBCkeyLength112 ()
specifier|public
name|void
name|executeCAST5CBCkeyLength112
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"CAST5/CBC/PKCS5Padding"
argument_list|,
literal|112
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeCAST5CBCkeyLength104 ()
specifier|public
name|void
name|executeCAST5CBCkeyLength104
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"CAST5/CBC/PKCS5Padding"
argument_list|,
literal|104
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeCAST5CBCkeyLength96 ()
specifier|public
name|void
name|executeCAST5CBCkeyLength96
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"CAST5/CBC/PKCS5Padding"
argument_list|,
literal|96
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeCAST5CBCkeyLength88 ()
specifier|public
name|void
name|executeCAST5CBCkeyLength88
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"CAST5/CBC/PKCS5Padding"
argument_list|,
literal|88
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeCAST5CBCkeyLength80 ()
specifier|public
name|void
name|executeCAST5CBCkeyLength80
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"CAST5/CBC/PKCS5Padding"
argument_list|,
literal|80
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeCAST5CBCkeyLength72 ()
specifier|public
name|void
name|executeCAST5CBCkeyLength72
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"CAST5/CBC/PKCS5Padding"
argument_list|,
literal|72
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeCAST5CBCkeyLength64 ()
specifier|public
name|void
name|executeCAST5CBCkeyLength64
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"CAST5/CBC/PKCS5Padding"
argument_list|,
literal|64
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeCAST5CBCkeyLength56 ()
specifier|public
name|void
name|executeCAST5CBCkeyLength56
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"CAST5/CBC/PKCS5Padding"
argument_list|,
literal|56
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeCAST5CBCkeyLength48 ()
specifier|public
name|void
name|executeCAST5CBCkeyLength48
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"CAST5/CBC/PKCS5Padding"
argument_list|,
literal|48
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeCAST5CBCkeyLength40 ()
specifier|public
name|void
name|executeCAST5CBCkeyLength40
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"CAST5/CBC/PKCS5Padding"
argument_list|,
literal|40
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeRC2CBCkeyLength128 ()
specifier|public
name|void
name|executeRC2CBCkeyLength128
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"RC2/CBC/PKCS5Padding"
argument_list|,
literal|128
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeRC2CBCkeyLength120 ()
specifier|public
name|void
name|executeRC2CBCkeyLength120
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"RC2/CBC/PKCS5Padding"
argument_list|,
literal|120
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeRC2CBCkeyLength112 ()
specifier|public
name|void
name|executeRC2CBCkeyLength112
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"RC2/CBC/PKCS5Padding"
argument_list|,
literal|112
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeRC2CBCkeyLength104 ()
specifier|public
name|void
name|executeRC2CBCkeyLength104
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"RC2/CBC/PKCS5Padding"
argument_list|,
literal|104
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeRC2CBCkeyLength96 ()
specifier|public
name|void
name|executeRC2CBCkeyLength96
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"RC2/CBC/PKCS5Padding"
argument_list|,
literal|96
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeRC2CBCkeyLength88 ()
specifier|public
name|void
name|executeRC2CBCkeyLength88
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"RC2/CBC/PKCS5Padding"
argument_list|,
literal|88
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeRC2CBCkeyLength80 ()
specifier|public
name|void
name|executeRC2CBCkeyLength80
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"RC2/CBC/PKCS5Padding"
argument_list|,
literal|80
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeRC2CBCkeyLength72 ()
specifier|public
name|void
name|executeRC2CBCkeyLength72
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"RC2/CBC/PKCS5Padding"
argument_list|,
literal|72
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeRC2CBCkeyLength64 ()
specifier|public
name|void
name|executeRC2CBCkeyLength64
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"RC2/CBC/PKCS5Padding"
argument_list|,
literal|64
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeRC2CBCkeyLength56 ()
specifier|public
name|void
name|executeRC2CBCkeyLength56
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"RC2/CBC/PKCS5Padding"
argument_list|,
literal|56
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeRC2CBCkeyLength48 ()
specifier|public
name|void
name|executeRC2CBCkeyLength48
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"RC2/CBC/PKCS5Padding"
argument_list|,
literal|48
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeRC2CBCkeyLength40 ()
specifier|public
name|void
name|executeRC2CBCkeyLength40
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"RC2/CBC/PKCS5Padding"
argument_list|,
literal|40
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeCamelliaCBCKeySize128 ()
specifier|public
name|void
name|executeCamelliaCBCKeySize128
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"Camellia/CBC/PKCS5Padding"
argument_list|,
literal|128
argument_list|)
expr_stmt|;
block|}
comment|/** Works if strong encryption policy jars are installed. */
annotation|@
name|Ignore
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executeCamelliaCBCKeySize256 ()
specifier|public
name|void
name|executeCamelliaCBCKeySize256
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"Camellia/CBC/PKCS5Padding"
argument_list|,
literal|256
argument_list|)
expr_stmt|;
block|}
comment|/** Works if strong encryption policy jars are installed. */
annotation|@
name|Ignore
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executeCamelliaCBCKeySize192 ()
specifier|public
name|void
name|executeCamelliaCBCKeySize192
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"Camellia/CBC/PKCS5Padding"
argument_list|,
literal|192
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executeNoInWhiteListCamellia256CBC ()
specifier|public
name|void
name|executeNoInWhiteListCamellia256CBC
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"Camellia256/CBC/PKCS5Padding"
argument_list|,
literal|256
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executeNotInWhiteListCamellia192CBC ()
specifier|public
name|void
name|executeNotInWhiteListCamellia192CBC
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"Camellia192/CBC/PKCS5Padding"
argument_list|,
literal|192
argument_list|)
expr_stmt|;
block|}
comment|/** Works if strong encryption policy jars are installed. */
annotation|@
name|Ignore
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executeAESCBCKeySize256 ()
specifier|public
name|void
name|executeAESCBCKeySize256
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"AES/CBC/PKCS5Padding"
argument_list|,
literal|256
argument_list|)
expr_stmt|;
block|}
comment|/** Works if strong encryption policy jars are installed. */
annotation|@
name|Ignore
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executeAESCBCKeySize192 ()
specifier|public
name|void
name|executeAESCBCKeySize192
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"AES/CBC/PKCS5Padding"
argument_list|,
literal|192
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeAESCBCKeySize128 ()
specifier|public
name|void
name|executeAESCBCKeySize128
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"AES/CBC/PKCS5Padding"
argument_list|,
literal|128
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executeNotInWhiteListAES256CBC ()
specifier|public
name|void
name|executeNotInWhiteListAES256CBC
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"AES256/CBC/PKCS5Padding"
argument_list|,
literal|256
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executeNotInWhiteListAES192CBC ()
specifier|public
name|void
name|executeNotInWhiteListAES192CBC
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"AES192/CBC/PKCS5Padding"
argument_list|,
literal|192
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executerNoImplRSAECB ()
specifier|public
name|void
name|executerNoImplRSAECB
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"RSA/ECB/OAEP"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executeNotInWhiteListAESGCM ()
specifier|public
name|void
name|executeNotInWhiteListAESGCM
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"AES/GCM/NoPadding"
argument_list|,
literal|128
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executeNotInWhiteListAES192GCM ()
specifier|public
name|void
name|executeNotInWhiteListAES192GCM
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"AES192/GCM/NoPadding"
argument_list|,
literal|192
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executeNotInWhiteListAES256GCM ()
specifier|public
name|void
name|executeNotInWhiteListAES256GCM
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"AES256/GCM/NoPadding"
argument_list|,
literal|256
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executeNotInWhiteListAES256CCM ()
specifier|public
name|void
name|executeNotInWhiteListAES256CCM
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"AES256/CCM/NoPadding"
argument_list|,
literal|256
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executeNotInWhiteListIDEACBC ()
specifier|public
name|void
name|executeNotInWhiteListIDEACBC
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"IDEA/CBC/PKCS5Padding"
argument_list|,
literal|128
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executeNotInWhiteListAESCCM ()
specifier|public
name|void
name|executeNotInWhiteListAESCCM
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"AES/CCM/NoPadding"
argument_list|,
literal|128
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executeNotInWhiteListAES192CCM ()
specifier|public
name|void
name|executeNotInWhiteListAES192CCM
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"AES192/CCM/NoPadding"
argument_list|,
literal|192
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|executeNotInWhiteListRC5CBC ()
specifier|public
name|void
name|executeNotInWhiteListRC5CBC
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"RC5/CBC/PKCS5Padding"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|wrongSecretKeyLength ()
specifier|public
name|void
name|wrongSecretKeyLength
parameter_list|()
throws|throws
name|Exception
block|{
name|encrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"DESede/CBC/PKCS5Padding"
argument_list|,
literal|200
argument_list|,
literal|"testMessage"
argument_list|,
literal|"rsa"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsException
operator|.
name|class
argument_list|)
DECL|method|wrongContentEncryptionAlgorithm ()
specifier|public
name|void
name|wrongContentEncryptionAlgorithm
parameter_list|()
throws|throws
name|Exception
block|{
name|encryptDecrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"rsa"
argument_list|,
literal|"WrongDESede/CBC/PKCS5Padding"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsNoKeyOrCertificateForAliasException
operator|.
name|class
argument_list|)
DECL|method|wrongEncryptAlias ()
specifier|public
name|void
name|wrongEncryptAlias
parameter_list|()
throws|throws
name|Exception
block|{
name|encrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"DESede/CBC/PKCS5Padding"
argument_list|,
literal|128
argument_list|,
literal|"testMessage"
argument_list|,
literal|"wrongAlias"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsNoKeyOrCertificateForAliasException
operator|.
name|class
argument_list|)
DECL|method|encryptWrongAliasAndCorrectAlias ()
specifier|public
name|void
name|encryptWrongAliasAndCorrectAlias
parameter_list|()
throws|throws
name|Exception
block|{
name|encrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"DESede/CBC/PKCS5Padding"
argument_list|,
literal|128
argument_list|,
literal|"testMessage"
argument_list|,
literal|"wrongAlias"
argument_list|,
literal|"rsa"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsNoKeyOrCertificateForAliasException
operator|.
name|class
argument_list|)
DECL|method|encryptTwoWrongAliases ()
specifier|public
name|void
name|encryptTwoWrongAliases
parameter_list|()
throws|throws
name|Exception
block|{
name|encrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"DESede/CBC/PKCS5Padding"
argument_list|,
literal|128
argument_list|,
literal|"testMessage"
argument_list|,
literal|"wrongAlias"
argument_list|,
literal|"wrongAlias2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|encryptTwoCorrectAliases ()
specifier|public
name|void
name|encryptTwoCorrectAliases
parameter_list|()
throws|throws
name|Exception
block|{
name|encrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"DESede/CBC/PKCS5Padding"
argument_list|,
literal|128
argument_list|,
literal|"testMessage"
argument_list|,
literal|"rsa2"
argument_list|,
literal|"rsa"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsFormatException
operator|.
name|class
argument_list|)
DECL|method|wrongEncryptedMessage ()
specifier|public
name|void
name|wrongEncryptedMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|decrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"TestMessage"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsFormatException
operator|.
name|class
argument_list|)
DECL|method|wrongEncryptedEmptyMessage ()
specifier|public
name|void
name|wrongEncryptedEmptyMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|decrypt
argument_list|(
literal|"system.jks"
argument_list|,
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|decryptionWithEmptyAlias ()
specifier|public
name|void
name|decryptionWithEmptyAlias
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|bytes
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bytes
operator|=
name|encrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"DESede/CBC/PKCS5Padding"
argument_list|,
literal|192
argument_list|,
literal|"Test Message"
argument_list|,
literal|"rsa"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Unexpected exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|decrypt
argument_list|(
literal|"system.jks"
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CryptoCmsNoCertificateForRecipientsException
operator|.
name|class
argument_list|)
DECL|method|decryptionWithNullAliasWrongKeystore ()
specifier|public
name|void
name|decryptionWithNullAliasWrongKeystore
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|bytes
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bytes
operator|=
name|encrypt
argument_list|(
literal|"system.jks"
argument_list|,
literal|"DESede/CBC/PKCS5Padding"
argument_list|,
literal|192
argument_list|,
literal|"Test Message"
argument_list|,
literal|"rsa"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Unexpected exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|decrypt
argument_list|(
literal|"test.jks"
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
block|}
comment|// @Test
comment|// public void invalidContentTypeEnvelopedData() throws Exception {
comment|// try {
comment|// encryptDecrypt("system.jks", "rsa", "DESede/CBC/PKCS5Padding", 192,
comment|// CmsEnvelopedDataDecryptorConfiguration.SIGNEDANDENVELOPEDDATA);
comment|// } catch (CmsException e) {
comment|// Assert.assertTrue(e.getMessage().contains("The PKCS#7/CMS decryptor step
comment|// does not accept PKCS#7/CMS messages of content type 'Enveloped Data'"));
comment|// return;
comment|// }
comment|// Assert.fail("Exception expected");
comment|// }
DECL|method|encryptDecrypt (String keystoreName, String alias, String contentEncryptionAlgorithm, int secretKeyLength)
specifier|private
name|void
name|encryptDecrypt
parameter_list|(
name|String
name|keystoreName
parameter_list|,
name|String
name|alias
parameter_list|,
name|String
name|contentEncryptionAlgorithm
parameter_list|,
name|int
name|secretKeyLength
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|message
init|=
literal|"Test Message"
decl_stmt|;
name|byte
index|[]
name|encrypted
init|=
name|encrypt
argument_list|(
name|keystoreName
argument_list|,
name|contentEncryptionAlgorithm
argument_list|,
name|secretKeyLength
argument_list|,
name|message
argument_list|,
name|alias
argument_list|)
decl_stmt|;
name|byte
index|[]
name|decrypted
init|=
name|decrypt
argument_list|(
name|keystoreName
argument_list|,
name|encrypted
argument_list|)
decl_stmt|;
name|String
name|actual
init|=
operator|new
name|String
argument_list|(
name|decrypted
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|message
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
DECL|method|encrypt (String keystoreName, String contentEncryptionAlgorithm, int secretKeyLength, String message, String... aliases)
specifier|private
name|byte
index|[]
name|encrypt
parameter_list|(
name|String
name|keystoreName
parameter_list|,
name|String
name|contentEncryptionAlgorithm
parameter_list|,
name|int
name|secretKeyLength
parameter_list|,
name|String
name|message
parameter_list|,
name|String
modifier|...
name|aliases
parameter_list|)
throws|throws
name|UnsupportedEncodingException
throws|,
name|Exception
block|{
name|KeyStoreParameters
name|keystorePas
init|=
name|KeystoreUtil
operator|.
name|getKeyStoreParameters
argument_list|(
name|keystoreName
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|RecipientInfo
argument_list|>
name|recipients
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|aliases
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|alias
range|:
name|aliases
control|)
block|{
name|DefaultKeyTransRecipientInfo
name|recipient
init|=
operator|new
name|DefaultKeyTransRecipientInfo
argument_list|()
decl_stmt|;
name|recipient
operator|.
name|setCertificateAlias
argument_list|(
name|alias
argument_list|)
expr_stmt|;
name|recipient
operator|.
name|setKeyStoreParameters
argument_list|(
name|keystorePas
argument_list|)
expr_stmt|;
name|recipients
operator|.
name|add
argument_list|(
name|recipient
argument_list|)
expr_stmt|;
block|}
name|EnvelopedDataEncryptorConfiguration
name|enConf
init|=
operator|new
name|EnvelopedDataEncryptorConfiguration
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|enConf
operator|.
name|setContentEncryptionAlgorithm
argument_list|(
name|contentEncryptionAlgorithm
argument_list|)
expr_stmt|;
for|for
control|(
name|RecipientInfo
name|recipient
range|:
name|recipients
control|)
block|{
name|enConf
operator|.
name|setRecipient
argument_list|(
name|recipient
argument_list|)
expr_stmt|;
block|}
name|enConf
operator|.
name|setSecretKeyLength
argument_list|(
name|secretKeyLength
argument_list|)
expr_stmt|;
comment|// optional
comment|// enConf.setBlockSize(2048); // optional
name|enConf
operator|.
name|init
argument_list|()
expr_stmt|;
name|EnvelopedDataEncryptor
name|encryptor
init|=
operator|new
name|EnvelopedDataEncryptor
argument_list|(
name|enConf
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|ExchangeUtil
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|message
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|encryptor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|byte
index|[]
name|encrypted
init|=
operator|(
name|byte
index|[]
operator|)
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
return|return
name|encrypted
return|;
block|}
DECL|method|decrypt (String keystoreName, byte[] encrypted)
specifier|private
name|byte
index|[]
name|decrypt
parameter_list|(
name|String
name|keystoreName
parameter_list|,
name|byte
index|[]
name|encrypted
parameter_list|)
throws|throws
name|UnsupportedEncodingException
throws|,
name|Exception
throws|,
name|IOException
block|{
name|KeyStoreParameters
name|keystore
init|=
name|KeystoreUtil
operator|.
name|getKeyStoreParameters
argument_list|(
name|keystoreName
argument_list|)
decl_stmt|;
name|Exchange
name|exchangeDecrypt
init|=
name|ExchangeUtil
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|exchangeDecrypt
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|encrypted
argument_list|)
argument_list|)
expr_stmt|;
name|DefaultEnvelopedDataDecryptorConfiguration
name|conf
init|=
operator|new
name|DefaultEnvelopedDataDecryptorConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setKeyStoreParameters
argument_list|(
name|keystore
argument_list|)
expr_stmt|;
name|EnvelopedDataDecryptor
name|decryptor
init|=
operator|new
name|EnvelopedDataDecryptor
argument_list|(
name|conf
argument_list|)
decl_stmt|;
name|decryptor
operator|.
name|process
argument_list|(
name|exchangeDecrypt
argument_list|)
expr_stmt|;
name|byte
index|[]
name|decrypted
init|=
operator|(
name|byte
index|[]
operator|)
name|exchangeDecrypt
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
return|return
name|decrypted
return|;
block|}
block|}
end_class

end_unit

