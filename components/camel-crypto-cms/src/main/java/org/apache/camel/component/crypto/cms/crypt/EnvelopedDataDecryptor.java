begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.cms.crypt
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
operator|.
name|crypt
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
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
name|common
operator|.
name|CryptoCmsUnmarshaller
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
name|support
operator|.
name|builder
operator|.
name|OutputStreamBuilder
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
name|IOHelper
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
name|Attribute
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
name|cert
operator|.
name|X509CRLEntryHolder
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
name|X509CRLHolder
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
name|cms
operator|.
name|CMSEnvelopedDataParser
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
name|KeyTransRecipientId
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
name|KeyTransRecipientInformation
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
name|OriginatorInformation
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
name|Recipient
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
name|RecipientInformation
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
name|RecipientInformationStore
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
name|JceKeyTransEnvelopedRecipient
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
name|JceKeyTransRecipientId
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
comment|/**  * Processor for decrypting CMS EnvelopedData content.  */
end_comment

begin_class
DECL|class|EnvelopedDataDecryptor
specifier|public
class|class
name|EnvelopedDataDecryptor
extends|extends
name|CryptoCmsUnmarshaller
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
name|EnvelopedDataDecryptor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|conf
specifier|private
specifier|final
name|EnvelopedDataDecryptorConfiguration
name|conf
decl_stmt|;
comment|// private final PublicKeyFinder finder;
DECL|method|EnvelopedDataDecryptor (EnvelopedDataDecryptorConfiguration conf)
specifier|public
name|EnvelopedDataDecryptor
parameter_list|(
name|EnvelopedDataDecryptorConfiguration
name|conf
parameter_list|)
block|{
name|super
argument_list|(
name|conf
argument_list|)
expr_stmt|;
name|this
operator|.
name|conf
operator|=
name|conf
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unmarshalInternal (InputStream is, Exchange exchange)
specifier|protected
name|Object
name|unmarshalInternal
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|CMSEnvelopedDataParser
name|parser
decl_stmt|;
try|try
block|{
name|parser
operator|=
operator|new
name|CMSEnvelopedDataParser
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CryptoCmsFormatException
argument_list|(
name|getFormatErrorMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
comment|// occurs with an empty payloud
throw|throw
operator|new
name|CryptoCmsFormatException
argument_list|(
name|getFormatErrorMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|unmarshal
argument_list|(
name|parser
argument_list|,
name|exchange
argument_list|)
return|;
block|}
DECL|method|getFormatErrorMessage ()
name|String
name|getFormatErrorMessage
parameter_list|()
block|{
return|return
literal|"Message has invalid format. It was not possible to parse the message into a PKCS7/CMS enveloped data object."
return|;
block|}
DECL|method|unmarshal (CMSEnvelopedDataParser parser, Exchange exchange)
specifier|private
name|Object
name|unmarshal
parameter_list|(
name|CMSEnvelopedDataParser
name|parser
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Decrypting CMS Enveloped Data started"
argument_list|)
expr_stmt|;
name|debugLog
argument_list|(
name|parser
argument_list|)
expr_stmt|;
name|RecipientInformationStore
name|recipientsStore
init|=
name|parser
operator|.
name|getRecipientInfos
argument_list|()
decl_stmt|;
if|if
condition|(
name|recipientsStore
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|CryptoCmsException
argument_list|(
literal|"PKCS7/CMS enveloped data message is incorrect. No recipient information found in enveloped data."
argument_list|)
throw|;
block|}
comment|// we loop over the key-pairs in the keystore and use the first entry
comment|// which fits to a recipient info
name|RecipientInformation
name|recipientInformation
init|=
literal|null
decl_stmt|;
name|Collection
argument_list|<
name|PrivateKeyWithCertificate
argument_list|>
name|privateKeyCerts
init|=
name|conf
operator|.
name|getPrivateKeyCertificateCollection
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|privateKeyCerts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CryptoCmsNoCertificateForRecipientsException
argument_list|(
literal|"Cannot decrypt PKCS7/CMS enveloped data object. No private key for the decryption configured."
argument_list|)
throw|;
block|}
name|PrivateKey
name|foundPrivateKey
init|=
literal|null
decl_stmt|;
for|for
control|(
name|PrivateKeyWithCertificate
name|privateKeyCert
range|:
name|privateKeyCerts
control|)
block|{
name|X509Certificate
name|cert
init|=
name|privateKeyCert
operator|.
name|getCertificate
argument_list|()
decl_stmt|;
name|JceKeyTransRecipientId
name|recipientId
init|=
operator|new
name|JceKeyTransRecipientId
argument_list|(
name|cert
argument_list|)
decl_stmt|;
name|recipientInformation
operator|=
name|recipientsStore
operator|.
name|get
argument_list|(
name|recipientId
argument_list|)
expr_stmt|;
if|if
condition|(
name|recipientInformation
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Recipient found for certificate with subjectDN={}, issuerDN={}, and serial number={}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|cert
operator|.
name|getSubjectDN
argument_list|()
block|,
name|cert
operator|.
name|getIssuerDN
argument_list|()
block|,
name|cert
operator|.
name|getSerialNumber
argument_list|()
block|}
argument_list|)
expr_stmt|;
name|foundPrivateKey
operator|=
name|privateKeyCert
operator|.
name|getPrivateKey
argument_list|()
expr_stmt|;
break|break;
comment|// use the first found private key
block|}
block|}
if|if
condition|(
name|recipientInformation
operator|==
literal|null
condition|)
block|{
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|certs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|privateKeyCerts
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|PrivateKeyWithCertificate
name|pc
range|:
name|privateKeyCerts
control|)
block|{
name|certs
operator|.
name|add
argument_list|(
name|pc
operator|.
name|getCertificate
argument_list|()
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|CryptoCmsNoCertificateForRecipientsException
argument_list|(
literal|"Cannot decrypt PKCS7/CMS enveloped data object. No certificate found among the configured certificates "
operator|+
literal|"which fit to one of the recipients in the enveloped data object. The recipients in the enveloped data object are: "
operator|+
name|recipientsToString
argument_list|(
name|recipientsStore
operator|.
name|getRecipients
argument_list|()
argument_list|)
operator|+
literal|"The configured certificates are: "
operator|+
name|certsToString
argument_list|(
name|certs
argument_list|)
operator|+
literal|". Specify a certificate with private key which fits to one of the recipients in the configruation or"
operator|+
literal|" check whether the encrypted message is encrypted with the correct key."
argument_list|)
throw|;
block|}
name|Recipient
name|recipient
init|=
operator|new
name|JceKeyTransEnvelopedRecipient
argument_list|(
name|foundPrivateKey
argument_list|)
decl_stmt|;
comment|// get the InputStream with the decrypted data, here the decryption
comment|// takes place
name|InputStream
name|is
decl_stmt|;
try|try
block|{
name|is
operator|=
name|recipientInformation
operator|.
name|getContentStream
argument_list|(
name|recipient
argument_list|)
operator|.
name|getContentStream
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CMSException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CryptoCmsException
argument_list|(
literal|"Error during decrypting an enveloped data object"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|Object
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|transformToStreamCacheOrByteArray
argument_list|(
name|exchange
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
comment|// unprotected attributes can only be read after the parsing is
comment|// finished.
name|AttributeTable
name|unprotectedAttsTable
init|=
name|parser
operator|.
name|getUnprotectedAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|unprotectedAttsTable
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Unprotected attributes size {}"
argument_list|,
name|unprotectedAttsTable
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Attribute
argument_list|>
name|unprotectedAtts
init|=
name|unprotectedAttsTable
operator|.
name|toHashtable
argument_list|()
decl_stmt|;
if|if
condition|(
name|unprotectedAtts
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Unprotected attributes: {}"
argument_list|,
name|attributesToString
argument_list|(
name|unprotectedAtts
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|debugLog (CMSEnvelopedDataParser parser)
specifier|protected
name|void
name|debugLog
parameter_list|(
name|CMSEnvelopedDataParser
name|parser
parameter_list|)
block|{
if|if
condition|(
operator|!
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
return|return;
block|}
name|OriginatorInformation
name|originatorInfo
init|=
name|parser
operator|.
name|getOriginatorInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|originatorInfo
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Enveloped Data has originator information"
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Store
argument_list|<
name|X509CertificateHolder
argument_list|>
name|certStore
init|=
name|originatorInfo
operator|.
name|getCertificates
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|X509CertificateHolder
argument_list|>
name|certs
init|=
name|certStore
operator|.
name|getMatches
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|certs
operator|!=
literal|null
operator|&&
name|certs
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Certificates in the originator information:"
argument_list|)
expr_stmt|;
for|for
control|(
name|X509CertificateHolder
name|cert
range|:
name|certs
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"    subject="
operator|+
name|cert
operator|.
name|getSubject
argument_list|()
operator|+
literal|", issuer="
operator|+
name|cert
operator|.
name|getIssuer
argument_list|()
operator|+
literal|", serial number="
operator|+
name|cert
operator|.
name|getSerialNumber
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Store
argument_list|<
name|X509CRLHolder
argument_list|>
name|crlsStore
init|=
name|originatorInfo
operator|.
name|getCRLs
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|X509CRLHolder
argument_list|>
name|crls
init|=
name|crlsStore
operator|.
name|getMatches
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|crls
operator|!=
literal|null
operator|&&
name|crls
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"CRLs in the originator information:"
argument_list|)
expr_stmt|;
for|for
control|(
name|X509CRLHolder
name|crl
range|:
name|crls
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"    CRL issuer={}"
argument_list|,
name|crl
operator|.
name|getIssuer
argument_list|()
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Collection
argument_list|<
name|X509CRLEntryHolder
argument_list|>
name|revokedCerts
init|=
name|crl
operator|.
name|getRevokedCertificates
argument_list|()
decl_stmt|;
for|for
control|(
name|X509CRLEntryHolder
name|revokedCert
range|:
name|revokedCerts
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"        Revoked Certificate: issuer="
operator|+
name|revokedCert
operator|.
name|getCertificateIssuer
argument_list|()
operator|+
literal|", serial number="
operator|+
name|revokedCert
operator|.
name|getSerialNumber
argument_list|()
operator|+
literal|", date="
operator|+
name|revokedCert
operator|.
name|getRevocationDate
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Content encryption algorithm OID: {}"
argument_list|,
name|parser
operator|.
name|getEncryptionAlgOID
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Recipient Infos:"
argument_list|)
expr_stmt|;
name|RecipientInformationStore
name|recipientStore
init|=
name|parser
operator|.
name|getRecipientInfos
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|RecipientInformation
argument_list|>
name|recipients
init|=
name|recipientStore
operator|.
name|getRecipients
argument_list|()
decl_stmt|;
name|int
name|counter
init|=
literal|0
decl_stmt|;
for|for
control|(
name|RecipientInformation
name|recipient
range|:
name|recipients
control|)
block|{
name|counter
operator|++
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"   Recipient Info {}: {}"
argument_list|,
name|counter
argument_list|,
name|recipientToString
argument_list|(
name|recipient
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|recipientsToString (Collection<RecipientInformation> recipients)
specifier|protected
name|String
name|recipientsToString
parameter_list|(
name|Collection
argument_list|<
name|RecipientInformation
argument_list|>
name|recipients
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|counter
init|=
literal|0
decl_stmt|;
name|int
name|size
init|=
name|recipients
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|RecipientInformation
name|recipient
range|:
name|recipients
control|)
block|{
name|counter
operator|++
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|recipientToString
argument_list|(
name|recipient
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
if|if
condition|(
name|counter
operator|<
name|size
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|';'
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|recipientToString (RecipientInformation recipient)
specifier|protected
name|String
name|recipientToString
parameter_list|(
name|RecipientInformation
name|recipient
parameter_list|)
block|{
if|if
condition|(
name|recipient
operator|instanceof
name|KeyTransRecipientInformation
condition|)
block|{
name|KeyTransRecipientId
name|rid
init|=
operator|(
name|KeyTransRecipientId
operator|)
name|recipient
operator|.
name|getRID
argument_list|()
decl_stmt|;
return|return
literal|"Issuer="
operator|+
name|rid
operator|.
name|getIssuer
argument_list|()
operator|+
literal|", serial number="
operator|+
name|rid
operator|.
name|getSerialNumber
argument_list|()
operator|+
literal|", key encryption algorithm OID="
operator|+
name|recipient
operator|.
name|getKeyEncryptionAlgOID
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|"not a KeyTransRecipientInformation: "
operator|+
name|recipient
operator|.
name|getRID
argument_list|()
operator|.
name|getType
argument_list|()
return|;
block|}
block|}
DECL|method|attributesToString (Hashtable<String, Attribute> attributes)
specifier|protected
name|String
name|attributesToString
parameter_list|(
name|Hashtable
argument_list|<
name|String
argument_list|,
name|Attribute
argument_list|>
name|attributes
parameter_list|)
block|{
if|if
condition|(
name|attributes
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|size
init|=
name|attributes
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|counter
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Attribute
name|attr
range|:
name|attributes
operator|.
name|values
argument_list|()
control|)
block|{
name|counter
operator|++
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|attr
operator|.
name|getAttrType
argument_list|()
argument_list|)
expr_stmt|;
comment|// we do not print out the attribute value because the value may
comment|// contain sensitive information
if|if
condition|(
name|counter
operator|<
name|size
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|transformToStreamCacheOrByteArray (Exchange exchange, InputStream is)
specifier|private
name|Object
name|transformToStreamCacheOrByteArray
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|CryptoCmsException
block|{
comment|// the input stream must be completely read, outherwise you will get
comment|// errors when your use as next component the file adapter.
name|OutputStreamBuilder
name|output
init|=
name|OutputStreamBuilder
operator|.
name|withExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
try|try
block|{
comment|// data can be null in the case of explicit Signed Data
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|IOHelper
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"CMS Enveloped Data decryption successful"
argument_list|)
expr_stmt|;
return|return
name|output
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CryptoCmsException
argument_list|(
literal|"Error during reading the unencrypted content of the enveloped data object"
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

