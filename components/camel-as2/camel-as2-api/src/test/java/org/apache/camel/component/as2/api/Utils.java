begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api
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
name|math
operator|.
name|BigInteger
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
name|KeyPair
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
name|X509Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|asn1
operator|.
name|x509
operator|.
name|AuthorityKeyIdentifier
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
name|x509
operator|.
name|Extension
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
name|x509
operator|.
name|SubjectKeyIdentifier
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
name|x509
operator|.
name|SubjectPublicKeyInfo
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
name|X509v3CertificateBuilder
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
name|bc
operator|.
name|BcX509ExtensionUtils
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
name|JcaX509CertificateConverter
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
name|JcaX509v3CertificateBuilder
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
name|OperatorCreationException
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
name|jcajce
operator|.
name|JcaContentSignerBuilder
import|;
end_import

begin_class
DECL|class|Utils
specifier|public
specifier|final
class|class
name|Utils
block|{
comment|//
comment|// certificate serial number seed.
comment|//
DECL|field|serialNo
specifier|static
name|int
name|serialNo
init|=
literal|1
decl_stmt|;
DECL|method|Utils ()
specifier|private
name|Utils
parameter_list|()
block|{     }
DECL|method|createAuthorityKeyId (PublicKey pub)
specifier|public
specifier|static
name|AuthorityKeyIdentifier
name|createAuthorityKeyId
parameter_list|(
name|PublicKey
name|pub
parameter_list|)
throws|throws
name|IOException
block|{
name|SubjectPublicKeyInfo
name|info
init|=
name|SubjectPublicKeyInfo
operator|.
name|getInstance
argument_list|(
name|pub
operator|.
name|getEncoded
argument_list|()
argument_list|)
decl_stmt|;
name|BcX509ExtensionUtils
name|utils
init|=
operator|new
name|BcX509ExtensionUtils
argument_list|()
decl_stmt|;
return|return
name|utils
operator|.
name|createAuthorityKeyIdentifier
argument_list|(
name|info
argument_list|)
return|;
block|}
DECL|method|createSubjectKeyId (PublicKey pub)
specifier|static
name|SubjectKeyIdentifier
name|createSubjectKeyId
parameter_list|(
name|PublicKey
name|pub
parameter_list|)
throws|throws
name|IOException
block|{
name|SubjectPublicKeyInfo
name|info
init|=
name|SubjectPublicKeyInfo
operator|.
name|getInstance
argument_list|(
name|pub
operator|.
name|getEncoded
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|BcX509ExtensionUtils
argument_list|()
operator|.
name|createSubjectKeyIdentifier
argument_list|(
name|info
argument_list|)
return|;
block|}
comment|/**      * create a basic X509 certificate from the given keys      */
DECL|method|makeCertificate (KeyPair subKP, String subDN, KeyPair issKP, String issDN)
specifier|public
specifier|static
name|X509Certificate
name|makeCertificate
parameter_list|(
name|KeyPair
name|subKP
parameter_list|,
name|String
name|subDN
parameter_list|,
name|KeyPair
name|issKP
parameter_list|,
name|String
name|issDN
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
throws|,
name|OperatorCreationException
block|{
name|PublicKey
name|subPub
init|=
name|subKP
operator|.
name|getPublic
argument_list|()
decl_stmt|;
name|PrivateKey
name|issPriv
init|=
name|issKP
operator|.
name|getPrivate
argument_list|()
decl_stmt|;
name|PublicKey
name|issPub
init|=
name|issKP
operator|.
name|getPublic
argument_list|()
decl_stmt|;
name|X509v3CertificateBuilder
name|v3CertGen
init|=
operator|new
name|JcaX509v3CertificateBuilder
argument_list|(
operator|new
name|X500Name
argument_list|(
name|issDN
argument_list|)
argument_list|,
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|serialNo
operator|++
argument_list|)
argument_list|,
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|,
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
operator|(
literal|1000L
operator|*
literal|60
operator|*
literal|60
operator|*
literal|24
operator|*
literal|100
operator|)
argument_list|)
argument_list|,
operator|new
name|X500Name
argument_list|(
name|subDN
argument_list|)
argument_list|,
name|subPub
argument_list|)
decl_stmt|;
name|v3CertGen
operator|.
name|addExtension
argument_list|(
name|Extension
operator|.
name|subjectKeyIdentifier
argument_list|,
literal|false
argument_list|,
name|createSubjectKeyId
argument_list|(
name|subPub
argument_list|)
argument_list|)
expr_stmt|;
name|v3CertGen
operator|.
name|addExtension
argument_list|(
name|Extension
operator|.
name|authorityKeyIdentifier
argument_list|,
literal|false
argument_list|,
name|createAuthorityKeyId
argument_list|(
name|issPub
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|JcaX509CertificateConverter
argument_list|()
operator|.
name|setProvider
argument_list|(
literal|"BC"
argument_list|)
operator|.
name|getCertificate
argument_list|(
name|v3CertGen
operator|.
name|build
argument_list|(
operator|new
name|JcaContentSignerBuilder
argument_list|(
literal|"MD5withRSA"
argument_list|)
operator|.
name|setProvider
argument_list|(
literal|"BC"
argument_list|)
operator|.
name|build
argument_list|(
name|issPriv
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

