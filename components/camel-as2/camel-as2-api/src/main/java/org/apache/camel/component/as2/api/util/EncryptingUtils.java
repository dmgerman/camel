begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|util
package|;
end_package

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
name|CertificateEncodingException
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|AS2EncryptionAlgorithm
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|util
operator|.
name|Args
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSEnvelopedDataGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|jcajce
operator|.
name|JceCMSContentEncryptorBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|jcajce
operator|.
name|JceKeyTransRecipientInfoGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|operator
operator|.
name|OutputEncryptor
import|;
end_import

begin_class
DECL|class|EncryptingUtils
specifier|public
specifier|final
class|class
name|EncryptingUtils
block|{
DECL|method|EncryptingUtils ()
specifier|private
name|EncryptingUtils
parameter_list|()
block|{     }
DECL|method|createEnvelopDataGenerator (Certificate[] encryptionCertificateChain)
specifier|public
specifier|static
name|CMSEnvelopedDataGenerator
name|createEnvelopDataGenerator
parameter_list|(
name|Certificate
index|[]
name|encryptionCertificateChain
parameter_list|)
throws|throws
name|HttpException
block|{
name|Args
operator|.
name|notNull
argument_list|(
name|encryptionCertificateChain
argument_list|,
literal|"encryptionCertificateChain"
argument_list|)
expr_stmt|;
if|if
condition|(
name|encryptionCertificateChain
operator|.
name|length
operator|==
literal|0
operator|||
operator|!
operator|(
name|encryptionCertificateChain
index|[
literal|0
index|]
operator|instanceof
name|X509Certificate
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid certificate chain"
argument_list|)
throw|;
block|}
try|try
block|{
name|X509Certificate
name|encryptionCertificate
init|=
operator|(
name|X509Certificate
operator|)
name|encryptionCertificateChain
index|[
literal|0
index|]
decl_stmt|;
name|CMSEnvelopedDataGenerator
name|cmsEnvelopeDataGenerator
init|=
operator|new
name|CMSEnvelopedDataGenerator
argument_list|()
decl_stmt|;
name|JceKeyTransRecipientInfoGenerator
name|recipientInfoGenerator
init|=
operator|new
name|JceKeyTransRecipientInfoGenerator
argument_list|(
name|encryptionCertificate
argument_list|)
decl_stmt|;
name|cmsEnvelopeDataGenerator
operator|.
name|addRecipientInfoGenerator
argument_list|(
name|recipientInfoGenerator
argument_list|)
expr_stmt|;
return|return
name|cmsEnvelopeDataGenerator
return|;
block|}
catch|catch
parameter_list|(
name|CertificateEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"Failed to create envelope data generator"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|createEncryptor (AS2EncryptionAlgorithm encryptionAlgorithm)
specifier|public
specifier|static
name|OutputEncryptor
name|createEncryptor
parameter_list|(
name|AS2EncryptionAlgorithm
name|encryptionAlgorithm
parameter_list|)
throws|throws
name|HttpException
block|{
name|Args
operator|.
name|notNull
argument_list|(
name|encryptionAlgorithm
argument_list|,
literal|"encryptionAlgorithmName"
argument_list|)
expr_stmt|;
try|try
block|{
return|return
operator|new
name|JceCMSContentEncryptorBuilder
argument_list|(
name|encryptionAlgorithm
operator|.
name|getAlgorithmOID
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"Failed to create encryptor "
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

