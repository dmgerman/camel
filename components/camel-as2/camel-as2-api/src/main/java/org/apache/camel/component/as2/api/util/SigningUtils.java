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
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|AS2SignatureAlgorithm
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
name|AS2SignedDataGenerator
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
name|asn1
operator|.
name|ASN1EncodableVector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|cms
operator|.
name|AttributeTable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|cms
operator|.
name|IssuerAndSerialNumber
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|smime
operator|.
name|SMIMECapabilitiesAttribute
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|smime
operator|.
name|SMIMECapability
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|smime
operator|.
name|SMIMECapabilityVector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|smime
operator|.
name|SMIMEEncryptionKeyPreferenceAttribute
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|asn1
operator|.
name|x500
operator|.
name|X500Name
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cert
operator|.
name|jcajce
operator|.
name|JcaCertStore
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
name|SignerInfoGenerator
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
name|JcaSimpleSignerInfoGeneratorBuilder
import|;
end_import

begin_class
DECL|class|SigningUtils
specifier|public
specifier|final
class|class
name|SigningUtils
block|{
DECL|method|SigningUtils ()
specifier|private
name|SigningUtils
parameter_list|()
block|{     }
DECL|method|createSigningGenerator (AS2SignatureAlgorithm signingAlgorithm, Certificate[] certificateChain, PrivateKey privateKey)
specifier|public
specifier|static
name|AS2SignedDataGenerator
name|createSigningGenerator
parameter_list|(
name|AS2SignatureAlgorithm
name|signingAlgorithm
parameter_list|,
name|Certificate
index|[]
name|certificateChain
parameter_list|,
name|PrivateKey
name|privateKey
parameter_list|)
throws|throws
name|HttpException
block|{
name|Args
operator|.
name|notNull
argument_list|(
name|certificateChain
argument_list|,
literal|"certificateChain"
argument_list|)
expr_stmt|;
if|if
condition|(
name|certificateChain
operator|.
name|length
operator|==
literal|0
operator|||
operator|!
operator|(
name|certificateChain
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
name|Args
operator|.
name|notNull
argument_list|(
name|privateKey
argument_list|,
literal|"privateKey"
argument_list|)
expr_stmt|;
name|AS2SignedDataGenerator
name|gen
init|=
operator|new
name|AS2SignedDataGenerator
argument_list|()
decl_stmt|;
comment|// Get first certificate in chain for signing
name|X509Certificate
name|signingCert
init|=
operator|(
name|X509Certificate
operator|)
name|certificateChain
index|[
literal|0
index|]
decl_stmt|;
comment|// Create capabilities vector
name|SMIMECapabilityVector
name|capabilities
init|=
operator|new
name|SMIMECapabilityVector
argument_list|()
decl_stmt|;
name|capabilities
operator|.
name|addCapability
argument_list|(
name|SMIMECapability
operator|.
name|dES_EDE3_CBC
argument_list|)
expr_stmt|;
name|capabilities
operator|.
name|addCapability
argument_list|(
name|SMIMECapability
operator|.
name|rC2_CBC
argument_list|,
literal|128
argument_list|)
expr_stmt|;
name|capabilities
operator|.
name|addCapability
argument_list|(
name|SMIMECapability
operator|.
name|dES_CBC
argument_list|)
expr_stmt|;
comment|// Create signing attributes
name|ASN1EncodableVector
name|attributes
init|=
operator|new
name|ASN1EncodableVector
argument_list|()
decl_stmt|;
name|attributes
operator|.
name|add
argument_list|(
operator|new
name|SMIMEEncryptionKeyPreferenceAttribute
argument_list|(
operator|new
name|IssuerAndSerialNumber
argument_list|(
operator|new
name|X500Name
argument_list|(
name|signingCert
operator|.
name|getIssuerDN
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|signingCert
operator|.
name|getSerialNumber
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|add
argument_list|(
operator|new
name|SMIMECapabilitiesAttribute
argument_list|(
name|capabilities
argument_list|)
argument_list|)
expr_stmt|;
name|SignerInfoGenerator
name|signerInfoGenerator
init|=
literal|null
decl_stmt|;
try|try
block|{
name|signerInfoGenerator
operator|=
operator|new
name|JcaSimpleSignerInfoGeneratorBuilder
argument_list|()
operator|.
name|setProvider
argument_list|(
literal|"BC"
argument_list|)
operator|.
name|setSignedAttributeGenerator
argument_list|(
operator|new
name|AttributeTable
argument_list|(
name|attributes
argument_list|)
argument_list|)
operator|.
name|build
argument_list|(
name|signingAlgorithm
operator|.
name|getSignatureAlgorithmName
argument_list|()
argument_list|,
name|privateKey
argument_list|,
name|signingCert
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"Failed to create signer info"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|gen
operator|.
name|addSignerInfoGenerator
argument_list|(
name|signerInfoGenerator
argument_list|)
expr_stmt|;
comment|// Create and populate certificate store.
try|try
block|{
name|JcaCertStore
name|certs
init|=
operator|new
name|JcaCertStore
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|certificateChain
argument_list|)
argument_list|)
decl_stmt|;
name|gen
operator|.
name|addCertificates
argument_list|(
name|certs
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CertificateEncodingException
decl||
name|CMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"Failed to add certificate chain to signature"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|gen
return|;
block|}
block|}
end_class

end_unit

