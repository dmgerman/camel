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
operator|.
name|ApplicationEDIEntity
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
name|entity
operator|.
name|EntityParser
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
name|entity
operator|.
name|MultipartSignedEntity
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
name|util
operator|.
name|EntityUtils
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
name|util
operator|.
name|SigningUtils
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
name|HttpResponse
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
name|BasicHttpEntityEnclosingRequest
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
name|protocol
operator|.
name|HttpCoreContext
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

begin_comment
comment|/**  * AS2 Client Manager  *  *<p>  * Sends EDI Messages over HTTP  *  */
end_comment

begin_class
DECL|class|AS2ClientManager
specifier|public
class|class
name|AS2ClientManager
block|{
comment|//
comment|// AS2 HTTP Context Attribute Keys
comment|//
comment|/**      * Prefix for all AS2 HTTP Context Attributes used by the Http Client      * Manager.      */
DECL|field|CAMEL_AS2_CLIENT_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_AS2_CLIENT_PREFIX
init|=
literal|"camel-as2.client"
decl_stmt|;
comment|/**      * The HTTP Context Attribute indicating the AS2 message structure to be sent.      */
DECL|field|AS2_MESSAGE_STRUCTURE
specifier|public
specifier|static
specifier|final
name|String
name|AS2_MESSAGE_STRUCTURE
init|=
name|CAMEL_AS2_CLIENT_PREFIX
operator|+
literal|"as2-message-structure"
decl_stmt|;
comment|/**      * The HTTP Context Attribute indicating the EDI message content type to be sent.      */
DECL|field|EDI_MESSAGE_CONTENT_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|EDI_MESSAGE_CONTENT_TYPE
init|=
name|CAMEL_AS2_CLIENT_PREFIX
operator|+
literal|"edi-message-content-type"
decl_stmt|;
comment|/**      * The HTTP Context Attribute indicating the EDI message transfer encoding to be sent.      */
DECL|field|EDI_MESSAGE_TRANSFER_ENCODING
specifier|public
specifier|static
specifier|final
name|String
name|EDI_MESSAGE_TRANSFER_ENCODING
init|=
name|CAMEL_AS2_CLIENT_PREFIX
operator|+
literal|"edi-message-transfer-encoding"
decl_stmt|;
comment|/**      * The HTTP Context Attribute containing the HTTP request message      * transporting the EDI message      */
DECL|field|HTTP_REQUEST
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_REQUEST
init|=
name|HttpCoreContext
operator|.
name|HTTP_REQUEST
decl_stmt|;
comment|/**      * The HTTP Context Attribute containing the HTTP response message      * transporting the EDI message      */
DECL|field|HTTP_RESPONSE
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_RESPONSE
init|=
name|HttpCoreContext
operator|.
name|HTTP_RESPONSE
decl_stmt|;
comment|/**      * The HTTP Context Attribute containing the AS2 Connection used to send      * request message.      */
DECL|field|AS2_CONNECTION
specifier|public
specifier|static
specifier|final
name|String
name|AS2_CONNECTION
init|=
name|CAMEL_AS2_CLIENT_PREFIX
operator|+
literal|"as2-connection"
decl_stmt|;
comment|/**      * The HTTP Context Attribute containing the request URI identifying the      * process on the receiving system responsible for unpacking and handling of      * message data and generating a reply for the sending system that contains      * a Message Disposition Acknowledgement (MDN)      */
DECL|field|REQUEST_URI
specifier|public
specifier|static
specifier|final
name|String
name|REQUEST_URI
init|=
name|CAMEL_AS2_CLIENT_PREFIX
operator|+
literal|"request-uri"
decl_stmt|;
comment|/**      * The HTTP Context Attribute containing the subject header sent in an AS2      * message.      */
DECL|field|SUBJECT
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT
init|=
name|CAMEL_AS2_CLIENT_PREFIX
operator|+
literal|"subject"
decl_stmt|;
comment|/**      * The HTTP Context Attribute containing the internet e-mail address of      * sending system      */
DECL|field|FROM
specifier|public
specifier|static
specifier|final
name|String
name|FROM
init|=
name|CAMEL_AS2_CLIENT_PREFIX
operator|+
literal|"from"
decl_stmt|;
comment|/**      * The HTTP Context Attribute containing the AS2 System Identifier of the      * sending system      */
DECL|field|AS2_FROM
specifier|public
specifier|static
specifier|final
name|String
name|AS2_FROM
init|=
name|CAMEL_AS2_CLIENT_PREFIX
operator|+
literal|"as2-from"
decl_stmt|;
comment|/**      * The HTTP Context Attribute containing the AS2 System Identifier of the      * receiving system      */
DECL|field|AS2_TO
specifier|public
specifier|static
specifier|final
name|String
name|AS2_TO
init|=
name|CAMEL_AS2_CLIENT_PREFIX
operator|+
literal|"as2-to"
decl_stmt|;
comment|/**      * The HTTP Context Attribute containing the algorithm name used to sign EDI      * message      */
DECL|field|SIGNING_ALGORITHM_NAME
specifier|public
specifier|static
specifier|final
name|String
name|SIGNING_ALGORITHM_NAME
init|=
name|CAMEL_AS2_CLIENT_PREFIX
operator|+
literal|"signing-algorithm-name"
decl_stmt|;
comment|/**      * The HTTP Context Attribute containing the certificate chain used to sign      * EDI message      */
DECL|field|SIGNING_CERTIFICATE_CHAIN
specifier|public
specifier|static
specifier|final
name|String
name|SIGNING_CERTIFICATE_CHAIN
init|=
name|CAMEL_AS2_CLIENT_PREFIX
operator|+
literal|"signing-certificate-chain"
decl_stmt|;
comment|/**      * The HTTP Context Attribute containing the private key used to sign EDI      * message      */
DECL|field|SIGNING_PRIVATE_KEY
specifier|public
specifier|static
specifier|final
name|String
name|SIGNING_PRIVATE_KEY
init|=
name|CAMEL_AS2_CLIENT_PREFIX
operator|+
literal|"signing-private-key"
decl_stmt|;
comment|/**      * The HTTP Context Attribute containing the internet e-mail address of      * sending system requesting a message disposition notification.      */
DECL|field|DISPOSITION_NOTIFICATION_TO
specifier|public
specifier|static
specifier|final
name|String
name|DISPOSITION_NOTIFICATION_TO
init|=
name|CAMEL_AS2_CLIENT_PREFIX
operator|+
literal|"disposition-notification-to"
decl_stmt|;
comment|/**      * The HTTP Context Attribute containing the list of names of the requested MIC algorithms to be used      * by the receiving system to construct a message disposition notification.      */
DECL|field|SIGNED_RECEIPT_MIC_ALGORITHMS
specifier|public
specifier|static
specifier|final
name|String
name|SIGNED_RECEIPT_MIC_ALGORITHMS
init|=
name|CAMEL_AS2_CLIENT_PREFIX
operator|+
literal|"signed-receipt-mic-algorithms"
decl_stmt|;
comment|//
DECL|field|as2ClientConnection
specifier|private
name|AS2ClientConnection
name|as2ClientConnection
decl_stmt|;
DECL|method|AS2ClientManager (AS2ClientConnection as2ClientConnection)
specifier|public
name|AS2ClientManager
parameter_list|(
name|AS2ClientConnection
name|as2ClientConnection
parameter_list|)
block|{
name|this
operator|.
name|as2ClientConnection
operator|=
name|as2ClientConnection
expr_stmt|;
block|}
comment|/**      * Send<code>ediMessage</code> to trading partner.      *      * @param ediMessage      *            - EDI message to transport      * @param requestUri      *            - resource location to deliver message      * @param subject - message subject      * @param from - RFC2822 address of sender      * @param as2From - AS2 name of sender      * @param as2To - AS2 name of recipient      * @param as2MessageStructure - the structure of AS2 to send; see {@link AS2MessageStructure}      * @param ediMessageContentType - the content typw of EDI message      * @param ediMessageTransferEncoding - the transfer encoding used to transport EDI message      * @param signingCertificateChain - the chain of certificates used to sign the message or<code>null</code> if sending EDI message unsigned      * @param signingPrivateKey - the private key used to sign EDI message      * @param dispositionNotificationTo - an RFC2822 address to request a receipt or<code>null</code> if no receipt requested      * @param signedReceiptMicAlgorithms - the senders list of signing algorithms for signing receipt, in preferred order,  or<code>null</code> if requesting an unsigned receipt.      * @return {@link HttpCoreContext} containing request and response used to send EDI message      * @throws HttpException when things go wrong.      */
DECL|method|send (String ediMessage, String requestUri, String subject, String from, String as2From, String as2To, AS2MessageStructure as2MessageStructure, ContentType ediMessageContentType, String ediMessageTransferEncoding, Certificate[] signingCertificateChain, PrivateKey signingPrivateKey, String dispositionNotificationTo, String[] signedReceiptMicAlgorithms)
specifier|public
name|HttpCoreContext
name|send
parameter_list|(
name|String
name|ediMessage
parameter_list|,
name|String
name|requestUri
parameter_list|,
name|String
name|subject
parameter_list|,
name|String
name|from
parameter_list|,
name|String
name|as2From
parameter_list|,
name|String
name|as2To
parameter_list|,
name|AS2MessageStructure
name|as2MessageStructure
parameter_list|,
name|ContentType
name|ediMessageContentType
parameter_list|,
name|String
name|ediMessageTransferEncoding
parameter_list|,
name|Certificate
index|[]
name|signingCertificateChain
parameter_list|,
name|PrivateKey
name|signingPrivateKey
parameter_list|,
name|String
name|dispositionNotificationTo
parameter_list|,
name|String
index|[]
name|signedReceiptMicAlgorithms
parameter_list|)
throws|throws
name|HttpException
block|{
name|Args
operator|.
name|notNull
argument_list|(
name|ediMessage
argument_list|,
literal|"EDI Message"
argument_list|)
expr_stmt|;
name|Args
operator|.
name|notNull
argument_list|(
name|as2MessageStructure
argument_list|,
literal|"AS2 Message Structure"
argument_list|)
expr_stmt|;
name|Args
operator|.
name|notNull
argument_list|(
name|requestUri
argument_list|,
literal|"Request URI"
argument_list|)
expr_stmt|;
name|Args
operator|.
name|notNull
argument_list|(
name|ediMessageContentType
argument_list|,
literal|"EDI Message Content Type"
argument_list|)
expr_stmt|;
comment|// Add Context attributes
name|HttpCoreContext
name|httpContext
init|=
name|HttpCoreContext
operator|.
name|create
argument_list|()
decl_stmt|;
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|REQUEST_URI
argument_list|,
name|requestUri
argument_list|)
expr_stmt|;
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|SUBJECT
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|FROM
argument_list|,
name|from
argument_list|)
expr_stmt|;
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|AS2_FROM
argument_list|,
name|as2From
argument_list|)
expr_stmt|;
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|AS2_TO
argument_list|,
name|as2To
argument_list|)
expr_stmt|;
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|AS2_MESSAGE_STRUCTURE
argument_list|,
name|as2MessageStructure
argument_list|)
expr_stmt|;
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|EDI_MESSAGE_CONTENT_TYPE
argument_list|,
name|ediMessageContentType
argument_list|)
expr_stmt|;
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|EDI_MESSAGE_TRANSFER_ENCODING
argument_list|,
name|ediMessageTransferEncoding
argument_list|)
expr_stmt|;
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|SIGNING_CERTIFICATE_CHAIN
argument_list|,
name|signingCertificateChain
argument_list|)
expr_stmt|;
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|SIGNING_PRIVATE_KEY
argument_list|,
name|signingPrivateKey
argument_list|)
expr_stmt|;
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|DISPOSITION_NOTIFICATION_TO
argument_list|,
name|dispositionNotificationTo
argument_list|)
expr_stmt|;
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|AS2ClientManager
operator|.
name|SIGNED_RECEIPT_MIC_ALGORITHMS
argument_list|,
name|signedReceiptMicAlgorithms
argument_list|)
expr_stmt|;
name|BasicHttpEntityEnclosingRequest
name|request
init|=
operator|new
name|BasicHttpEntityEnclosingRequest
argument_list|(
literal|"POST"
argument_list|,
name|requestUri
argument_list|)
decl_stmt|;
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|HTTP_REQUEST
argument_list|,
name|request
argument_list|)
expr_stmt|;
comment|// Create Message Body
name|ApplicationEDIEntity
name|applicationEDIEntity
decl_stmt|;
try|try
block|{
name|applicationEDIEntity
operator|=
name|EntityUtils
operator|.
name|createEDIEntity
argument_list|(
name|ediMessage
argument_list|,
name|ediMessageContentType
argument_list|,
name|ediMessageTransferEncoding
argument_list|,
literal|false
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
literal|"Failed to create EDI message entity"
argument_list|,
name|e
argument_list|)
throw|;
block|}
switch|switch
condition|(
name|as2MessageStructure
condition|)
block|{
case|case
name|PLAIN
case|:
name|applicationEDIEntity
operator|.
name|setMainBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|EntityUtils
operator|.
name|setMessageEntity
argument_list|(
name|request
argument_list|,
name|applicationEDIEntity
argument_list|)
expr_stmt|;
break|break;
case|case
name|SIGNED
case|:
name|AS2SignedDataGenerator
name|gen
init|=
name|createSigningGenerator
argument_list|(
name|httpContext
argument_list|)
decl_stmt|;
comment|// Create Multipart Signed Entity
try|try
block|{
name|MultipartSignedEntity
name|multipartSignedEntity
init|=
operator|new
name|MultipartSignedEntity
argument_list|(
name|applicationEDIEntity
argument_list|,
name|gen
argument_list|,
name|AS2Charset
operator|.
name|US_ASCII
argument_list|,
name|AS2TransferEncoding
operator|.
name|BASE64
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|EntityUtils
operator|.
name|setMessageEntity
argument_list|(
name|request
argument_list|,
name|multipartSignedEntity
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
literal|"Failed to sign message"
argument_list|,
name|e
argument_list|)
throw|;
block|}
break|break;
case|case
name|ENCRYPTED
case|:
comment|// TODO : Add code here to add application/pkcs7-mime entity when encryption facility available.
break|break;
case|case
name|ENCRYPTED_SIGNED
case|:
comment|// TODO : Add code here to add application/pkcs7-mime entity when encryption facility available.
break|break;
default|default:
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"Unknown AS2 Message Structure"
argument_list|)
throw|;
block|}
name|HttpResponse
name|response
decl_stmt|;
try|try
block|{
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|AS2_CONNECTION
argument_list|,
name|as2ClientConnection
argument_list|)
expr_stmt|;
name|response
operator|=
name|as2ClientConnection
operator|.
name|send
argument_list|(
name|request
argument_list|,
name|httpContext
argument_list|)
expr_stmt|;
name|EntityParser
operator|.
name|parseAS2MessageEntity
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"Failed to send http request message"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|httpContext
operator|.
name|setAttribute
argument_list|(
name|HTTP_RESPONSE
argument_list|,
name|response
argument_list|)
expr_stmt|;
return|return
name|httpContext
return|;
block|}
DECL|method|createSigningGenerator (HttpCoreContext httpContext)
specifier|public
name|AS2SignedDataGenerator
name|createSigningGenerator
parameter_list|(
name|HttpCoreContext
name|httpContext
parameter_list|)
throws|throws
name|HttpException
block|{
name|Certificate
index|[]
name|certificateChain
init|=
name|httpContext
operator|.
name|getAttribute
argument_list|(
name|SIGNING_CERTIFICATE_CHAIN
argument_list|,
name|Certificate
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|certificateChain
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"Signing certificate chain missing"
argument_list|)
throw|;
block|}
name|PrivateKey
name|privateKey
init|=
name|httpContext
operator|.
name|getAttribute
argument_list|(
name|SIGNING_PRIVATE_KEY
argument_list|,
name|PrivateKey
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|privateKey
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"Signing private key missing"
argument_list|)
throw|;
block|}
return|return
name|SigningUtils
operator|.
name|createSigningGenerator
argument_list|(
name|certificateChain
argument_list|,
name|privateKey
argument_list|)
return|;
block|}
block|}
end_class

end_unit

