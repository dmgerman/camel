begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api.entity
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
name|entity
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
name|ByteArrayOutputStream
import|;
end_import

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
name|Collection
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
name|AS2Header
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
name|entity
operator|.
name|ContentType
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
name|message
operator|.
name|BasicHeader
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
name|X509CertificateHolder
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
name|cms
operator|.
name|CMSProcessable
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
name|CMSProcessableByteArray
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
name|CMSSignedData
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
name|SignerInformation
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
name|SignerInformationStore
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
name|JcaSimpleSignerInfoVerifierBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|util
operator|.
name|Store
import|;
end_import

begin_class
DECL|class|MultipartSignedEntity
specifier|public
class|class
name|MultipartSignedEntity
extends|extends
name|MultipartMimeEntity
block|{
DECL|method|MultipartSignedEntity (MimeEntity data, AS2SignedDataGenerator signer, String signatureCharSet, String signatureTransferEncoding, boolean isMainBody, String boundary)
specifier|public
name|MultipartSignedEntity
parameter_list|(
name|MimeEntity
name|data
parameter_list|,
name|AS2SignedDataGenerator
name|signer
parameter_list|,
name|String
name|signatureCharSet
parameter_list|,
name|String
name|signatureTransferEncoding
parameter_list|,
name|boolean
name|isMainBody
parameter_list|,
name|String
name|boundary
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
literal|null
argument_list|,
name|isMainBody
argument_list|,
name|boundary
argument_list|)
expr_stmt|;
name|ContentType
name|contentType
init|=
name|signer
operator|.
name|createMultipartSignedContentType
argument_list|(
name|this
operator|.
name|boundary
argument_list|)
decl_stmt|;
name|this
operator|.
name|contentType
operator|=
operator|new
name|BasicHeader
argument_list|(
name|AS2Header
operator|.
name|CONTENT_TYPE
argument_list|,
name|contentType
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|addPart
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|ApplicationPkcs7SignatureEntity
name|signature
init|=
operator|new
name|ApplicationPkcs7SignatureEntity
argument_list|(
name|data
argument_list|,
name|signer
argument_list|,
name|signatureCharSet
argument_list|,
name|signatureTransferEncoding
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|addPart
argument_list|(
name|signature
argument_list|)
expr_stmt|;
block|}
DECL|method|MultipartSignedEntity (String boundary, boolean isMainBody)
specifier|protected
name|MultipartSignedEntity
parameter_list|(
name|String
name|boundary
parameter_list|,
name|boolean
name|isMainBody
parameter_list|)
block|{
name|this
operator|.
name|boundary
operator|=
name|boundary
expr_stmt|;
name|this
operator|.
name|isMainBody
operator|=
name|isMainBody
expr_stmt|;
block|}
DECL|method|isValid ()
specifier|public
name|boolean
name|isValid
parameter_list|()
block|{
name|ApplicationEDIEntity
name|applicationEDIEntity
init|=
name|getSignedDataEntity
argument_list|()
decl_stmt|;
name|ApplicationPkcs7SignatureEntity
name|applicationPkcs7SignatureEntity
init|=
name|getSignatureEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|applicationEDIEntity
operator|==
literal|null
operator|||
name|applicationPkcs7SignatureEntity
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
try|try
block|{
name|ByteArrayOutputStream
name|outstream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|applicationEDIEntity
operator|.
name|writeTo
argument_list|(
name|outstream
argument_list|)
expr_stmt|;
name|CMSProcessable
name|signedContent
init|=
operator|new
name|CMSProcessableByteArray
argument_list|(
name|outstream
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|signature
init|=
name|applicationPkcs7SignatureEntity
operator|.
name|getSignature
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|signature
argument_list|)
decl_stmt|;
name|CMSSignedData
name|signedData
init|=
operator|new
name|CMSSignedData
argument_list|(
name|signedContent
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|Store
argument_list|<
name|X509CertificateHolder
argument_list|>
name|store
init|=
name|signedData
operator|.
name|getCertificates
argument_list|()
decl_stmt|;
name|SignerInformationStore
name|signers
init|=
name|signedData
operator|.
name|getSignerInfos
argument_list|()
decl_stmt|;
for|for
control|(
name|SignerInformation
name|signer
range|:
name|signers
operator|.
name|getSigners
argument_list|()
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Collection
argument_list|<
name|X509CertificateHolder
argument_list|>
name|certCollection
init|=
name|store
operator|.
name|getMatches
argument_list|(
name|signer
operator|.
name|getSID
argument_list|()
argument_list|)
decl_stmt|;
name|X509CertificateHolder
name|certHolder
init|=
name|certCollection
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|X509Certificate
name|cert
init|=
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
name|certHolder
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|signer
operator|.
name|verify
argument_list|(
operator|new
name|JcaSimpleSignerInfoVerifierBuilder
argument_list|()
operator|.
name|setProvider
argument_list|(
literal|"BC"
argument_list|)
operator|.
name|build
argument_list|(
name|cert
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|getSignedDataEntity ()
specifier|public
name|ApplicationEDIEntity
name|getSignedDataEntity
parameter_list|()
block|{
if|if
condition|(
name|getPartCount
argument_list|()
operator|>
literal|0
operator|&&
name|getPart
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|ApplicationEDIEntity
condition|)
block|{
return|return
operator|(
name|ApplicationEDIEntity
operator|)
name|getPart
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getSignatureEntity ()
specifier|public
name|ApplicationPkcs7SignatureEntity
name|getSignatureEntity
parameter_list|()
block|{
if|if
condition|(
name|getPartCount
argument_list|()
operator|>
literal|1
operator|&&
name|getPart
argument_list|(
literal|1
argument_list|)
operator|instanceof
name|ApplicationPkcs7SignatureEntity
condition|)
block|{
return|return
operator|(
name|ApplicationPkcs7SignatureEntity
operator|)
name|getPart
argument_list|(
literal|1
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

