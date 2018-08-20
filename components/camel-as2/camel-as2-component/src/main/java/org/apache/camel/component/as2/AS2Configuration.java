begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriParams
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
name|spi
operator|.
name|UriPath
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

begin_comment
comment|/**  * Component configuration for AS2 component.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|AS2Configuration
specifier|public
class|class
name|AS2Configuration
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|apiName
specifier|private
name|AS2ApiName
name|apiName
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|methodName
specifier|private
name|String
name|methodName
decl_stmt|;
annotation|@
name|UriPath
DECL|field|as2Version
specifier|private
name|String
name|as2Version
init|=
literal|"1.1"
decl_stmt|;
annotation|@
name|UriParam
DECL|field|userAgent
specifier|private
name|String
name|userAgent
init|=
literal|"Camel AS2 Client Endpoint"
decl_stmt|;
annotation|@
name|UriParam
DECL|field|server
specifier|private
name|String
name|server
init|=
literal|"Camel AS2 Server Endpoint"
decl_stmt|;
annotation|@
name|UriParam
DECL|field|serverFqdn
specifier|private
name|String
name|serverFqdn
init|=
literal|"camel.apache.org"
decl_stmt|;
annotation|@
name|UriParam
DECL|field|targetHostname
specifier|private
name|String
name|targetHostname
decl_stmt|;
annotation|@
name|UriParam
DECL|field|targetPortNumber
specifier|private
name|Integer
name|targetPortNumber
decl_stmt|;
annotation|@
name|UriParam
DECL|field|clientFqdn
specifier|private
name|String
name|clientFqdn
init|=
literal|"camel.apache.org"
decl_stmt|;
annotation|@
name|UriParam
DECL|field|serverPortNumber
specifier|private
name|Integer
name|serverPortNumber
decl_stmt|;
annotation|@
name|UriParam
DECL|field|requestUri
specifier|private
name|String
name|requestUri
init|=
literal|"/"
decl_stmt|;
annotation|@
name|UriParam
DECL|field|ediMessageType
specifier|private
name|ContentType
name|ediMessageType
decl_stmt|;
annotation|@
name|UriParam
DECL|field|ediMessageTransferEncoding
specifier|private
name|String
name|ediMessageTransferEncoding
decl_stmt|;
annotation|@
name|UriParam
DECL|field|as2MessageStructure
specifier|private
name|AS2MessageStructure
name|as2MessageStructure
decl_stmt|;
annotation|@
name|UriParam
DECL|field|subject
specifier|private
name|String
name|subject
decl_stmt|;
annotation|@
name|UriParam
DECL|field|from
specifier|private
name|String
name|from
decl_stmt|;
annotation|@
name|UriParam
DECL|field|as2From
specifier|private
name|String
name|as2From
decl_stmt|;
annotation|@
name|UriParam
DECL|field|as2To
specifier|private
name|String
name|as2To
decl_stmt|;
annotation|@
name|UriParam
DECL|field|signingAlgorithmName
specifier|private
name|String
name|signingAlgorithmName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|signingCertificateChain
specifier|private
name|Certificate
index|[]
name|signingCertificateChain
decl_stmt|;
annotation|@
name|UriParam
DECL|field|signingPrivateKey
specifier|private
name|PrivateKey
name|signingPrivateKey
decl_stmt|;
annotation|@
name|UriParam
DECL|field|dispositionNotificationTo
specifier|private
name|String
name|dispositionNotificationTo
decl_stmt|;
annotation|@
name|UriParam
DECL|field|signedReceiptMicAlgorithms
specifier|private
name|String
index|[]
name|signedReceiptMicAlgorithms
decl_stmt|;
comment|/**      * What kind of operation to perform      *      * @return the API Name      */
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
comment|/**      * What kind of operation to perform      *      * @param apiName      *            - the API Name to set      */
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
comment|/**      * What sub operation to use for the selected operation      *      * @return The methodName      */
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
comment|/**      * What sub operation to use for the selected operation      *      * @param methodName      *            - the methodName to set      */
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
comment|/**      * The version of the AS2 protocol.      *      * @return The version of the AS2 protocol.      */
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
comment|/**      * The version of the AS2 protocol.      *      * @param as2Version      *            - the version of the AS2 protocol.      */
DECL|method|setAs2Version (String as2Version)
specifier|public
name|void
name|setAs2Version
parameter_list|(
name|String
name|as2Version
parameter_list|)
block|{
if|if
condition|(
operator|!
name|as2Version
operator|.
name|equals
argument_list|(
literal|"1.0"
argument_list|)
operator|&&
operator|!
name|as2Version
operator|.
name|equals
argument_list|(
literal|"1.1"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Value '%s' of configuration parameter 'as2Version' must be either '1.0' or '1.1'"
argument_list|,
name|as2Version
argument_list|)
argument_list|)
throw|;
block|}
name|this
operator|.
name|as2Version
operator|=
name|as2Version
expr_stmt|;
block|}
comment|/**      * The value included in the<code>User-Agent</code> message header identifying      * the AS2 user agent.      *      * @return AS2 user agent identification string.      */
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
comment|/**      * The value included in the<code>User-Agent</code> message header identifying      * the AS2 user agent.      *      * @param userAgent      *            - AS2 user agent identification string.      */
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
comment|/**      * The value included in the<code>Server</code> message header identifying the      * AS2 Server.      *      * @return AS2 server identification string.      */
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
comment|/**      * The value included in the<code>Server</code> message header identifying the      * AS2 Server.      *      * @param server      *            - AS2 server identification string.      */
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
comment|/**      * The Server Fully Qualified Domain Name (FQDN).      *      *<p>      * Used in message ids sent by endpoint.      *      * @return The FQDN of client.      */
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
comment|/**      * The Server Fully Qualified Domain Name (FQDN).      *      *<p>      * Used in message ids sent by endpoint.      *      * @param clientFqdn      *            - the FQDN of server.      */
DECL|method|setServerFqdn (String serverFqdn)
specifier|public
name|void
name|setServerFqdn
parameter_list|(
name|String
name|serverFqdn
parameter_list|)
block|{
if|if
condition|(
name|clientFqdn
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Parameter 'serverFqdn' can not be null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|serverFqdn
operator|=
name|serverFqdn
expr_stmt|;
block|}
comment|/**      * The host name (IP or DNS) of target host.      *      * @return The target host name (IP or DNS name).      */
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
comment|/**      * The host name (IP or DNS name) of target host.      *      * @param targetHostname      *            - the target host name (IP or DNS name).      */
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
comment|/**      * The port number of target host.      *      * @return The target port number. -1 indicates the scheme default port.      */
DECL|method|getTargetPortNumber ()
specifier|public
name|int
name|getTargetPortNumber
parameter_list|()
block|{
return|return
name|targetPortNumber
return|;
block|}
comment|/**      * The port number of target host.      *      * @param targetPortNumber      *            - the target port number. -1 indicates the scheme default port.      */
DECL|method|setTargetPortNumber (String targetPortNumber)
specifier|public
name|void
name|setTargetPortNumber
parameter_list|(
name|String
name|targetPortNumber
parameter_list|)
block|{
try|try
block|{
name|this
operator|.
name|targetPortNumber
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|targetPortNumber
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Invalid target port number: %s"
argument_list|,
name|targetPortNumber
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**      * The port number of target host.      *       * @param targetPortNumber      *            - the target port number. -1 indicates the scheme default port.      */
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
comment|/**      * The Client Fully Qualified Domain Name (FQDN).      *      *<p>      * Used in message ids sent by endpoint.      *      * @return The FQDN of client.      */
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
comment|/**      * The Client Fully Qualified Domain Name (FQDN).      *      *<p>      * Used in message ids sent by endpoint.      *      * @param clientFqdn      *            - the FQDN of client.      */
DECL|method|setClientFqdn (String clientFqdn)
specifier|public
name|void
name|setClientFqdn
parameter_list|(
name|String
name|clientFqdn
parameter_list|)
block|{
if|if
condition|(
name|clientFqdn
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Parameter 'clientFqdn' can not be null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|clientFqdn
operator|=
name|clientFqdn
expr_stmt|;
block|}
comment|/**      * The port number of server.      *      * @return The server port number.      */
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
comment|/**      * The port number of server.      *      * @param serverPortNumber      *            - the server port number.      */
DECL|method|setServerPortNumber (String serverPortNumber)
specifier|public
name|void
name|setServerPortNumber
parameter_list|(
name|String
name|serverPortNumber
parameter_list|)
block|{
try|try
block|{
name|this
operator|.
name|serverPortNumber
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|serverPortNumber
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Invalid target port number: %s"
argument_list|,
name|targetPortNumber
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**      * The port number of server.      *      * @param serverPortNumber      *            - the server port number.      */
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
comment|/**      * The request URI of EDI message.      *       * @return The URI of request      */
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
comment|/**      * The request URI of EDI message.      *       * @param requestUri      *            - the URI of request      */
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
comment|/**      * The content type of EDI message. One of      *<ul>      *<li>application/edifact</li>      *<li>application/edi-x12</li>      *<li>application/edi-consent</li>      *</ul>      *       * @return The content type of EDI message.      */
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
comment|/**      * The content type of EDI message. One of      *<ul>      *<li>application/edifact</li>      *<li>application/edi-x12</li>      *<li>application/edi-consent</li>      *</ul>      *       * @param ediMessageType      *            - the content type of EDI message.      */
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
comment|/**      * The transfer encoding of EDI message.      *       * @return The transfer encoding of EDI message.      */
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
comment|/**      * The transfer encoding of EDI message.      *       * @param ediMessageTransferEncoding      *            - the transfer encoding of EDI message.      */
DECL|method|setEdiMessageTransferEncoding (String ediMessageTransferEncoding)
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
comment|/**      * The structure of AS2 Message. One of      *<ul>      *<li>PLAIN - No encryption, no signature</li>      *<li>SIGNED - No encryption, signature</li>      *<li>ENCRYPTED - Encryption, no signature</li>      *<li>ENCRYPTED_SIGNED - Encryption, signature</li>      *</ul>      *       * @return The structure of AS2 Message.      */
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
comment|/**      * The structure of AS2 Message. One of      *<ul>      *<li>PLAIN - No encryption, no signature</li>      *<li>SIGNED - No encryption, signature</li>      *<li>ENCRYPTED - Encryption, no signature</li>      *<li>ENCRYPTED_SIGNED - Encryption, signature</li>      *</ul>      *       * @param as2MessageStructure      *            - the structure of AS2 Message.      */
DECL|method|setAs2MessageStructure (AS2MessageStructure as2MessageStructure)
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
comment|/**      * The value of Subject header of AS2 message.      *       * @return The value of Subject header of AS2 message.      */
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
comment|/**      * The value of Subject header of AS2 message.      *       * @param subject      *            - the value of Subject header of AS2 message.      */
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
comment|/**      * The value of the From header of AS2 message.      *       * @return The value of the From header of AS2 message.      */
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
comment|/**      * The value of the From header of AS2 message.      *       * @param from      *            - the value of the From header of AS2 message.      */
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
comment|/**      * The value of the AS2From header of AS2 message.      *       * @return The value of the AS2From header of AS2 message.      */
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
comment|/**      * The value of the AS2From header of AS2 message.      *       * @param as2From      *            - the value of the AS2From header of AS2 message.      */
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
comment|/**      * The value of the AS2To header of AS2 message.      *       * @return The value of the AS2To header of AS2 message.      */
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
comment|/**      * The value of the AS2To header of AS2 message.      *       * @param as2From      *            - the value of the AS2To header of AS2 message.      */
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
comment|/**      * The name of algorithm used to sign EDI message.      *       * @return The name of algorithm used to sign EDI message.      */
DECL|method|getSigningAlgorithmName ()
specifier|public
name|String
name|getSigningAlgorithmName
parameter_list|()
block|{
return|return
name|signingAlgorithmName
return|;
block|}
comment|/**      * The name of algorithm used to sign EDI message.      *       * @param signingAlgorithmName      *            - the name of algorithm used to sign EDI message.      */
DECL|method|setSigningAlgorithmName (String signingAlgorithmName)
specifier|public
name|void
name|setSigningAlgorithmName
parameter_list|(
name|String
name|signingAlgorithmName
parameter_list|)
block|{
name|this
operator|.
name|signingAlgorithmName
operator|=
name|signingAlgorithmName
expr_stmt|;
block|}
comment|/**      * The chain of certificates used to sign EDI message.      *       * @return The chain of certificates used to sign EDI message.      */
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
comment|/**      * The chain of certificates used to sign EDI message.      *       * @param signingCertificateChain      *            - the chain of certificates used to sign EDI message.      */
DECL|method|setSigningCertificateChain (Certificate[] signingCertificateChain)
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
comment|/**      * The key used to sign the EDI message.      *       * @return The key used to sign the EDI message.      */
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
comment|/**      * The key used to sign the EDI message.      *       * @param signingPrivateKey      *            - the key used to sign the EDI message.      */
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
comment|/**      * The value of the Dispostion-Notification-To header.      *       * Assigning a value to this parameter requests a message disposition      * notification (MDN) for the AS2 message.      *       * @return The value of the Dispostion-Notification-To header.      */
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
comment|/**      * The value of the Dispostion-Notification-To header.      *       * Assigning a value to this parameter requests a message disposition      * notification (MDN) for the AS2 message.      *       * @param dispositionNotificationTo      *            - the value of the Dispostion-Notification-To header.      */
DECL|method|setDispositionNotificationTo (String dispositionNotificationTo)
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
comment|/**      * The list of algorithms, in order of preference, requested to generate a      * message integrity check (MIC) returned in message dispostion notification      * (MDN)      *       * @return The list of algorithms.      */
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
comment|/**      * The list of algorithms, in order of preference, requested to generate a      * message integrity check (MIC) returned in message dispostion notification      * (MDN)      *       * @param signedReceiptMicAlgorithms      *            - the list of algorithms.      */
DECL|method|setSignedReceiptMicAlgorithms (String[] signedReceiptMicAlgorithms)
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
block|}
end_class

end_unit

