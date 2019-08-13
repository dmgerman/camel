begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.springboot
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
name|springboot
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
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|AS2CompressionAlgorithm
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
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|AS2MessageStructure
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
name|internal
operator|.
name|AS2ApiName
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Component used for transferring data secure and reliable over the internet  * using the AS2 protocol.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.as2"
argument_list|)
DECL|class|AS2ComponentConfiguration
specifier|public
class|class
name|AS2ComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the as2 component. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * To use the shared configuration      */
DECL|field|configuration
specifier|private
name|AS2ConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Whether the component should use basic property binding (Camel 2.x) or      * the newer property binding with additional capabilities      */
DECL|field|basicPropertyBinding
specifier|private
name|Boolean
name|basicPropertyBinding
init|=
literal|false
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|AS2ConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( AS2ConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|AS2ConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getBasicPropertyBinding ()
specifier|public
name|Boolean
name|getBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
DECL|method|setBasicPropertyBinding (Boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|Boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|class|AS2ConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|AS2ConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
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
name|AS2Configuration
operator|.
name|class
decl_stmt|;
comment|/**          * What kind of operation to perform          */
DECL|field|apiName
specifier|private
name|AS2ApiName
name|apiName
decl_stmt|;
comment|/**          * What sub operation to use for the selected operation          */
DECL|field|methodName
specifier|private
name|String
name|methodName
decl_stmt|;
comment|/**          * The version of the AS2 protocol.          */
DECL|field|as2Version
specifier|private
name|String
name|as2Version
init|=
literal|"1.1"
decl_stmt|;
comment|/**          * The value included in the User-Agent message header identifying the          * AS2 user agent.          */
DECL|field|userAgent
specifier|private
name|String
name|userAgent
init|=
literal|"Camel AS2 Client Endpoint"
decl_stmt|;
comment|/**          * The value included in the Server message header identifying the AS2          * Server.          */
DECL|field|server
specifier|private
name|String
name|server
init|=
literal|"Camel AS2 Server Endpoint"
decl_stmt|;
comment|/**          * The Server Fully Qualified Domain Name (FQDN). Used in message ids          * sent by endpoint.          */
DECL|field|serverFqdn
specifier|private
name|String
name|serverFqdn
init|=
literal|"camel.apache.org"
decl_stmt|;
comment|/**          * The host name (IP or DNS name) of target host.          */
DECL|field|targetHostname
specifier|private
name|String
name|targetHostname
decl_stmt|;
comment|/**          * The port number of target host. -1 indicates the scheme default port.          */
DECL|field|targetPortNumber
specifier|private
name|Integer
name|targetPortNumber
decl_stmt|;
comment|/**          * The Client Fully Qualified Domain Name (FQDN). Used in message ids          * sent by endpoint.          */
DECL|field|clientFqdn
specifier|private
name|String
name|clientFqdn
init|=
literal|"camel.apache.org"
decl_stmt|;
comment|/**          * The port number of server.          */
DECL|field|serverPortNumber
specifier|private
name|Integer
name|serverPortNumber
decl_stmt|;
comment|/**          * The request URI of EDI message.          */
DECL|field|requestUri
specifier|private
name|String
name|requestUri
init|=
literal|"/"
decl_stmt|;
comment|/**          * The content type of EDI message. One of application/edifact,          * application/edi-x12, application/edi-consent          */
DECL|field|ediMessageType
specifier|private
name|ContentType
name|ediMessageType
decl_stmt|;
comment|/**          * The transfer encoding of EDI message.          */
DECL|field|ediMessageTransferEncoding
specifier|private
name|String
name|ediMessageTransferEncoding
decl_stmt|;
comment|/**          * The structure of AS2 Message. One of: PLAIN - No encryption, no          * signature, SIGNED - No encryption, signature, ENCRYPTED - Encryption,          * no signature, ENCRYPTED_SIGNED - Encryption, signature          */
DECL|field|as2MessageStructure
specifier|private
name|AS2MessageStructure
name|as2MessageStructure
decl_stmt|;
comment|/**          * The value of Subject header of AS2 message.          */
DECL|field|subject
specifier|private
name|String
name|subject
decl_stmt|;
comment|/**          * The value of the From header of AS2 message.          */
DECL|field|from
specifier|private
name|String
name|from
decl_stmt|;
comment|/**          * The value of the AS2From header of AS2 message.          */
DECL|field|as2From
specifier|private
name|String
name|as2From
decl_stmt|;
comment|/**          * The value of the AS2To header of AS2 message.          */
DECL|field|as2To
specifier|private
name|String
name|as2To
decl_stmt|;
comment|/**          * The algorithm used to sign EDI message.          */
DECL|field|signingAlgorithm
specifier|private
name|AS2SignatureAlgorithm
name|signingAlgorithm
decl_stmt|;
comment|/**          * The chain of certificates used to sign EDI message.          */
DECL|field|signingCertificateChain
specifier|private
name|Certificate
index|[]
name|signingCertificateChain
decl_stmt|;
comment|/**          * The key used to sign the EDI message.          */
DECL|field|signingPrivateKey
specifier|private
name|PrivateKey
name|signingPrivateKey
decl_stmt|;
comment|/**          * The algorithm used to compress EDI message.          */
DECL|field|compressionAlgorithm
specifier|private
name|AS2CompressionAlgorithm
name|compressionAlgorithm
decl_stmt|;
comment|/**          * The value of the Disposition-Notification-To header. Assigning a          * value to this parameter requests a message disposition notification          * (MDN) for the AS2 message.          */
DECL|field|dispositionNotificationTo
specifier|private
name|String
name|dispositionNotificationTo
decl_stmt|;
comment|/**          * The list of algorithms, in order of preference, requested to generate          * a message integrity check (MIC) returned in message dispostion          * notification (MDN)          */
DECL|field|signedReceiptMicAlgorithms
specifier|private
name|String
index|[]
name|signedReceiptMicAlgorithms
decl_stmt|;
comment|/**          * The algorithm used to encrypt EDI message.          */
DECL|field|encryptingAlgorithm
specifier|private
name|AS2EncryptionAlgorithm
name|encryptingAlgorithm
decl_stmt|;
comment|/**          * The chain of certificates used to encrypt EDI message.          */
DECL|field|encryptingCertificateChain
specifier|private
name|Certificate
index|[]
name|encryptingCertificateChain
decl_stmt|;
comment|/**          * The key used to encrypt the EDI message.          */
DECL|field|decryptingPrivateKey
specifier|private
name|PrivateKey
name|decryptingPrivateKey
decl_stmt|;
comment|/**          * The template used to format MDN message          */
DECL|field|mdnMessageTemplate
specifier|private
name|String
name|mdnMessageTemplate
decl_stmt|;
DECL|method|getApiName ()
specifier|public
name|AS2ApiName
name|getApiName
parameter_list|()
block|{
return|return
name|apiName
return|;
block|}
DECL|method|setApiName (AS2ApiName apiName)
specifier|public
name|void
name|setApiName
parameter_list|(
name|AS2ApiName
name|apiName
parameter_list|)
block|{
name|this
operator|.
name|apiName
operator|=
name|apiName
expr_stmt|;
block|}
DECL|method|getMethodName ()
specifier|public
name|String
name|getMethodName
parameter_list|()
block|{
return|return
name|methodName
return|;
block|}
DECL|method|setMethodName (String methodName)
specifier|public
name|void
name|setMethodName
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
block|}
DECL|method|getAs2Version ()
specifier|public
name|String
name|getAs2Version
parameter_list|()
block|{
return|return
name|as2Version
return|;
block|}
DECL|method|setAs2Version (String as2Version)
specifier|public
name|void
name|setAs2Version
parameter_list|(
name|String
name|as2Version
parameter_list|)
block|{
name|this
operator|.
name|as2Version
operator|=
name|as2Version
expr_stmt|;
block|}
DECL|method|getUserAgent ()
specifier|public
name|String
name|getUserAgent
parameter_list|()
block|{
return|return
name|userAgent
return|;
block|}
DECL|method|setUserAgent (String userAgent)
specifier|public
name|void
name|setUserAgent
parameter_list|(
name|String
name|userAgent
parameter_list|)
block|{
name|this
operator|.
name|userAgent
operator|=
name|userAgent
expr_stmt|;
block|}
DECL|method|getServer ()
specifier|public
name|String
name|getServer
parameter_list|()
block|{
return|return
name|server
return|;
block|}
DECL|method|setServer (String server)
specifier|public
name|void
name|setServer
parameter_list|(
name|String
name|server
parameter_list|)
block|{
name|this
operator|.
name|server
operator|=
name|server
expr_stmt|;
block|}
DECL|method|getServerFqdn ()
specifier|public
name|String
name|getServerFqdn
parameter_list|()
block|{
return|return
name|serverFqdn
return|;
block|}
DECL|method|setServerFqdn (String serverFqdn)
specifier|public
name|void
name|setServerFqdn
parameter_list|(
name|String
name|serverFqdn
parameter_list|)
block|{
name|this
operator|.
name|serverFqdn
operator|=
name|serverFqdn
expr_stmt|;
block|}
DECL|method|getTargetHostname ()
specifier|public
name|String
name|getTargetHostname
parameter_list|()
block|{
return|return
name|targetHostname
return|;
block|}
DECL|method|setTargetHostname (String targetHostname)
specifier|public
name|void
name|setTargetHostname
parameter_list|(
name|String
name|targetHostname
parameter_list|)
block|{
name|this
operator|.
name|targetHostname
operator|=
name|targetHostname
expr_stmt|;
block|}
DECL|method|getTargetPortNumber ()
specifier|public
name|Integer
name|getTargetPortNumber
parameter_list|()
block|{
return|return
name|targetPortNumber
return|;
block|}
DECL|method|setTargetPortNumber (Integer targetPortNumber)
specifier|public
name|void
name|setTargetPortNumber
parameter_list|(
name|Integer
name|targetPortNumber
parameter_list|)
block|{
name|this
operator|.
name|targetPortNumber
operator|=
name|targetPortNumber
expr_stmt|;
block|}
DECL|method|getClientFqdn ()
specifier|public
name|String
name|getClientFqdn
parameter_list|()
block|{
return|return
name|clientFqdn
return|;
block|}
DECL|method|setClientFqdn (String clientFqdn)
specifier|public
name|void
name|setClientFqdn
parameter_list|(
name|String
name|clientFqdn
parameter_list|)
block|{
name|this
operator|.
name|clientFqdn
operator|=
name|clientFqdn
expr_stmt|;
block|}
DECL|method|getServerPortNumber ()
specifier|public
name|Integer
name|getServerPortNumber
parameter_list|()
block|{
return|return
name|serverPortNumber
return|;
block|}
DECL|method|setServerPortNumber (Integer serverPortNumber)
specifier|public
name|void
name|setServerPortNumber
parameter_list|(
name|Integer
name|serverPortNumber
parameter_list|)
block|{
name|this
operator|.
name|serverPortNumber
operator|=
name|serverPortNumber
expr_stmt|;
block|}
DECL|method|getRequestUri ()
specifier|public
name|String
name|getRequestUri
parameter_list|()
block|{
return|return
name|requestUri
return|;
block|}
DECL|method|setRequestUri (String requestUri)
specifier|public
name|void
name|setRequestUri
parameter_list|(
name|String
name|requestUri
parameter_list|)
block|{
name|this
operator|.
name|requestUri
operator|=
name|requestUri
expr_stmt|;
block|}
DECL|method|getEdiMessageType ()
specifier|public
name|ContentType
name|getEdiMessageType
parameter_list|()
block|{
return|return
name|ediMessageType
return|;
block|}
DECL|method|setEdiMessageType (ContentType ediMessageType)
specifier|public
name|void
name|setEdiMessageType
parameter_list|(
name|ContentType
name|ediMessageType
parameter_list|)
block|{
name|this
operator|.
name|ediMessageType
operator|=
name|ediMessageType
expr_stmt|;
block|}
DECL|method|getEdiMessageTransferEncoding ()
specifier|public
name|String
name|getEdiMessageTransferEncoding
parameter_list|()
block|{
return|return
name|ediMessageTransferEncoding
return|;
block|}
DECL|method|setEdiMessageTransferEncoding ( String ediMessageTransferEncoding)
specifier|public
name|void
name|setEdiMessageTransferEncoding
parameter_list|(
name|String
name|ediMessageTransferEncoding
parameter_list|)
block|{
name|this
operator|.
name|ediMessageTransferEncoding
operator|=
name|ediMessageTransferEncoding
expr_stmt|;
block|}
DECL|method|getAs2MessageStructure ()
specifier|public
name|AS2MessageStructure
name|getAs2MessageStructure
parameter_list|()
block|{
return|return
name|as2MessageStructure
return|;
block|}
DECL|method|setAs2MessageStructure ( AS2MessageStructure as2MessageStructure)
specifier|public
name|void
name|setAs2MessageStructure
parameter_list|(
name|AS2MessageStructure
name|as2MessageStructure
parameter_list|)
block|{
name|this
operator|.
name|as2MessageStructure
operator|=
name|as2MessageStructure
expr_stmt|;
block|}
DECL|method|getSubject ()
specifier|public
name|String
name|getSubject
parameter_list|()
block|{
return|return
name|subject
return|;
block|}
DECL|method|setSubject (String subject)
specifier|public
name|void
name|setSubject
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
block|}
DECL|method|getFrom ()
specifier|public
name|String
name|getFrom
parameter_list|()
block|{
return|return
name|from
return|;
block|}
DECL|method|setFrom (String from)
specifier|public
name|void
name|setFrom
parameter_list|(
name|String
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
block|}
DECL|method|getAs2From ()
specifier|public
name|String
name|getAs2From
parameter_list|()
block|{
return|return
name|as2From
return|;
block|}
DECL|method|setAs2From (String as2From)
specifier|public
name|void
name|setAs2From
parameter_list|(
name|String
name|as2From
parameter_list|)
block|{
name|this
operator|.
name|as2From
operator|=
name|as2From
expr_stmt|;
block|}
DECL|method|getAs2To ()
specifier|public
name|String
name|getAs2To
parameter_list|()
block|{
return|return
name|as2To
return|;
block|}
DECL|method|setAs2To (String as2To)
specifier|public
name|void
name|setAs2To
parameter_list|(
name|String
name|as2To
parameter_list|)
block|{
name|this
operator|.
name|as2To
operator|=
name|as2To
expr_stmt|;
block|}
DECL|method|getSigningAlgorithm ()
specifier|public
name|AS2SignatureAlgorithm
name|getSigningAlgorithm
parameter_list|()
block|{
return|return
name|signingAlgorithm
return|;
block|}
DECL|method|setSigningAlgorithm (AS2SignatureAlgorithm signingAlgorithm)
specifier|public
name|void
name|setSigningAlgorithm
parameter_list|(
name|AS2SignatureAlgorithm
name|signingAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|signingAlgorithm
operator|=
name|signingAlgorithm
expr_stmt|;
block|}
DECL|method|getSigningCertificateChain ()
specifier|public
name|Certificate
index|[]
name|getSigningCertificateChain
parameter_list|()
block|{
return|return
name|signingCertificateChain
return|;
block|}
DECL|method|setSigningCertificateChain ( Certificate[] signingCertificateChain)
specifier|public
name|void
name|setSigningCertificateChain
parameter_list|(
name|Certificate
index|[]
name|signingCertificateChain
parameter_list|)
block|{
name|this
operator|.
name|signingCertificateChain
operator|=
name|signingCertificateChain
expr_stmt|;
block|}
DECL|method|getSigningPrivateKey ()
specifier|public
name|PrivateKey
name|getSigningPrivateKey
parameter_list|()
block|{
return|return
name|signingPrivateKey
return|;
block|}
DECL|method|setSigningPrivateKey (PrivateKey signingPrivateKey)
specifier|public
name|void
name|setSigningPrivateKey
parameter_list|(
name|PrivateKey
name|signingPrivateKey
parameter_list|)
block|{
name|this
operator|.
name|signingPrivateKey
operator|=
name|signingPrivateKey
expr_stmt|;
block|}
DECL|method|getCompressionAlgorithm ()
specifier|public
name|AS2CompressionAlgorithm
name|getCompressionAlgorithm
parameter_list|()
block|{
return|return
name|compressionAlgorithm
return|;
block|}
DECL|method|setCompressionAlgorithm ( AS2CompressionAlgorithm compressionAlgorithm)
specifier|public
name|void
name|setCompressionAlgorithm
parameter_list|(
name|AS2CompressionAlgorithm
name|compressionAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|compressionAlgorithm
operator|=
name|compressionAlgorithm
expr_stmt|;
block|}
DECL|method|getDispositionNotificationTo ()
specifier|public
name|String
name|getDispositionNotificationTo
parameter_list|()
block|{
return|return
name|dispositionNotificationTo
return|;
block|}
DECL|method|setDispositionNotificationTo ( String dispositionNotificationTo)
specifier|public
name|void
name|setDispositionNotificationTo
parameter_list|(
name|String
name|dispositionNotificationTo
parameter_list|)
block|{
name|this
operator|.
name|dispositionNotificationTo
operator|=
name|dispositionNotificationTo
expr_stmt|;
block|}
DECL|method|getSignedReceiptMicAlgorithms ()
specifier|public
name|String
index|[]
name|getSignedReceiptMicAlgorithms
parameter_list|()
block|{
return|return
name|signedReceiptMicAlgorithms
return|;
block|}
DECL|method|setSignedReceiptMicAlgorithms ( String[] signedReceiptMicAlgorithms)
specifier|public
name|void
name|setSignedReceiptMicAlgorithms
parameter_list|(
name|String
index|[]
name|signedReceiptMicAlgorithms
parameter_list|)
block|{
name|this
operator|.
name|signedReceiptMicAlgorithms
operator|=
name|signedReceiptMicAlgorithms
expr_stmt|;
block|}
DECL|method|getEncryptingAlgorithm ()
specifier|public
name|AS2EncryptionAlgorithm
name|getEncryptingAlgorithm
parameter_list|()
block|{
return|return
name|encryptingAlgorithm
return|;
block|}
DECL|method|setEncryptingAlgorithm ( AS2EncryptionAlgorithm encryptingAlgorithm)
specifier|public
name|void
name|setEncryptingAlgorithm
parameter_list|(
name|AS2EncryptionAlgorithm
name|encryptingAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|encryptingAlgorithm
operator|=
name|encryptingAlgorithm
expr_stmt|;
block|}
DECL|method|getEncryptingCertificateChain ()
specifier|public
name|Certificate
index|[]
name|getEncryptingCertificateChain
parameter_list|()
block|{
return|return
name|encryptingCertificateChain
return|;
block|}
DECL|method|setEncryptingCertificateChain ( Certificate[] encryptingCertificateChain)
specifier|public
name|void
name|setEncryptingCertificateChain
parameter_list|(
name|Certificate
index|[]
name|encryptingCertificateChain
parameter_list|)
block|{
name|this
operator|.
name|encryptingCertificateChain
operator|=
name|encryptingCertificateChain
expr_stmt|;
block|}
DECL|method|getDecryptingPrivateKey ()
specifier|public
name|PrivateKey
name|getDecryptingPrivateKey
parameter_list|()
block|{
return|return
name|decryptingPrivateKey
return|;
block|}
DECL|method|setDecryptingPrivateKey (PrivateKey decryptingPrivateKey)
specifier|public
name|void
name|setDecryptingPrivateKey
parameter_list|(
name|PrivateKey
name|decryptingPrivateKey
parameter_list|)
block|{
name|this
operator|.
name|decryptingPrivateKey
operator|=
name|decryptingPrivateKey
expr_stmt|;
block|}
DECL|method|getMdnMessageTemplate ()
specifier|public
name|String
name|getMdnMessageTemplate
parameter_list|()
block|{
return|return
name|mdnMessageTemplate
return|;
block|}
DECL|method|setMdnMessageTemplate (String mdnMessageTemplate)
specifier|public
name|void
name|setMdnMessageTemplate
parameter_list|(
name|String
name|mdnMessageTemplate
parameter_list|)
block|{
name|this
operator|.
name|mdnMessageTemplate
operator|=
name|mdnMessageTemplate
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

