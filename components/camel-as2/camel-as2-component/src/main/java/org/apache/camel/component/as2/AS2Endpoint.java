begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|AS2ClientConnection
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
name|AS2ClientManager
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
name|AS2ServerConnection
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
name|AS2ServerManager
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
name|AS2ApiCollection
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
name|component
operator|.
name|as2
operator|.
name|internal
operator|.
name|AS2ConnectionHelper
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
name|AS2Constants
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
name|AS2PropertiesHelper
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
name|UriEndpoint
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
name|support
operator|.
name|component
operator|.
name|AbstractApiEndpoint
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
name|component
operator|.
name|ApiMethod
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
name|component
operator|.
name|ApiMethodPropertiesHelper
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
comment|/**  * Component used for transferring data secure and reliable over the internet using the AS2 protocol.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"as2"
argument_list|,
name|firstVersion
operator|=
literal|"2.22.0"
argument_list|,
name|title
operator|=
literal|"AS2"
argument_list|,
name|syntax
operator|=
literal|"as2:apiName/methodName"
argument_list|,
name|label
operator|=
literal|"AS2"
argument_list|)
DECL|class|AS2Endpoint
specifier|public
class|class
name|AS2Endpoint
extends|extends
name|AbstractApiEndpoint
argument_list|<
name|AS2ApiName
argument_list|,
name|AS2Configuration
argument_list|>
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|AS2Configuration
name|configuration
decl_stmt|;
DECL|field|apiProxy
specifier|private
name|Object
name|apiProxy
decl_stmt|;
DECL|field|as2ClientConnection
specifier|private
name|AS2ClientConnection
name|as2ClientConnection
decl_stmt|;
DECL|field|as2ServerConnection
specifier|private
name|AS2ServerConnection
name|as2ServerConnection
decl_stmt|;
DECL|method|AS2Endpoint (String uri, AS2Component component, AS2ApiName apiName, String methodName, AS2Configuration endpointConfiguration)
specifier|public
name|AS2Endpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|AS2Component
name|component
parameter_list|,
name|AS2ApiName
name|apiName
parameter_list|,
name|String
name|methodName
parameter_list|,
name|AS2Configuration
name|endpointConfiguration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|apiName
argument_list|,
name|methodName
argument_list|,
name|AS2ApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getHelper
argument_list|(
name|apiName
argument_list|)
argument_list|,
name|endpointConfiguration
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|endpointConfiguration
expr_stmt|;
block|}
DECL|method|getAs2Configuration ()
specifier|public
name|AS2Configuration
name|getAs2Configuration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getAS2ClientConnection ()
specifier|public
name|AS2ClientConnection
name|getAS2ClientConnection
parameter_list|()
block|{
return|return
name|as2ClientConnection
return|;
block|}
DECL|method|getAS2ServerConnection ()
specifier|public
name|AS2ServerConnection
name|getAS2ServerConnection
parameter_list|()
block|{
return|return
name|as2ServerConnection
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|AS2Producer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
comment|// make sure inBody is not set for consumers
if|if
condition|(
name|inBody
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Option inBody is not supported for consumer endpoint"
argument_list|)
throw|;
block|}
specifier|final
name|AS2Consumer
name|consumer
init|=
operator|new
name|AS2Consumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
comment|// also set consumer.* properties
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|getRequestUri ()
specifier|public
name|String
name|getRequestUri
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getRequestUri
argument_list|()
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
name|configuration
operator|.
name|setRequestUri
argument_list|(
name|requestUri
argument_list|)
expr_stmt|;
block|}
DECL|method|getSubject ()
specifier|public
name|String
name|getSubject
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getSubject
argument_list|()
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
name|configuration
operator|.
name|setSubject
argument_list|(
name|subject
argument_list|)
expr_stmt|;
block|}
DECL|method|getFrom ()
specifier|public
name|String
name|getFrom
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getFrom
argument_list|()
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
name|configuration
operator|.
name|setFrom
argument_list|(
name|from
argument_list|)
expr_stmt|;
block|}
DECL|method|getAs2From ()
specifier|public
name|String
name|getAs2From
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getAs2From
argument_list|()
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
name|configuration
operator|.
name|setAs2From
argument_list|(
name|as2From
argument_list|)
expr_stmt|;
block|}
DECL|method|getAs2To ()
specifier|public
name|String
name|getAs2To
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getAs2To
argument_list|()
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
name|configuration
operator|.
name|setAs2To
argument_list|(
name|as2To
argument_list|)
expr_stmt|;
block|}
DECL|method|getAs2MessageStructure ()
specifier|public
name|AS2MessageStructure
name|getAs2MessageStructure
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getAs2MessageStructure
argument_list|()
return|;
block|}
DECL|method|setAs2MessageStructure (AS2MessageStructure as2MessageStructure)
specifier|public
name|void
name|setAs2MessageStructure
parameter_list|(
name|AS2MessageStructure
name|as2MessageStructure
parameter_list|)
block|{
name|configuration
operator|.
name|setAs2MessageStructure
argument_list|(
name|as2MessageStructure
argument_list|)
expr_stmt|;
block|}
DECL|method|getEdiMessageType ()
specifier|public
name|ContentType
name|getEdiMessageType
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getEdiMessageType
argument_list|()
return|;
block|}
DECL|method|setEdiMessageContentType (ContentType ediMessageType)
specifier|public
name|void
name|setEdiMessageContentType
parameter_list|(
name|ContentType
name|ediMessageType
parameter_list|)
block|{
name|configuration
operator|.
name|setEdiMessageType
argument_list|(
name|ediMessageType
argument_list|)
expr_stmt|;
block|}
DECL|method|getEdiMessageTransferEncoding ()
specifier|public
name|String
name|getEdiMessageTransferEncoding
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getEdiMessageTransferEncoding
argument_list|()
return|;
block|}
DECL|method|setEdiMessageTransferEncoding (String ediMessageTransferEncoding)
specifier|public
name|void
name|setEdiMessageTransferEncoding
parameter_list|(
name|String
name|ediMessageTransferEncoding
parameter_list|)
block|{
name|configuration
operator|.
name|setEdiMessageTransferEncoding
argument_list|(
name|ediMessageTransferEncoding
argument_list|)
expr_stmt|;
block|}
DECL|method|getSigningAlgorithm ()
specifier|public
name|AS2SignatureAlgorithm
name|getSigningAlgorithm
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getSigningAlgorithm
argument_list|()
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
name|configuration
operator|.
name|setSigningAlgorithm
argument_list|(
name|signingAlgorithm
argument_list|)
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
name|configuration
operator|.
name|getSigningCertificateChain
argument_list|()
return|;
block|}
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
name|configuration
operator|.
name|setSigningCertificateChain
argument_list|(
name|signingCertificateChain
argument_list|)
expr_stmt|;
block|}
DECL|method|getSigningPrivateKey ()
specifier|public
name|PrivateKey
name|getSigningPrivateKey
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getSigningPrivateKey
argument_list|()
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
name|configuration
operator|.
name|setSigningPrivateKey
argument_list|(
name|signingPrivateKey
argument_list|)
expr_stmt|;
block|}
DECL|method|getCompressionAlgorithm ()
specifier|public
name|AS2CompressionAlgorithm
name|getCompressionAlgorithm
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getCompressionAlgorithm
argument_list|()
return|;
block|}
DECL|method|setCompressionAlgorithm (AS2CompressionAlgorithm compressionAlgorithm)
specifier|public
name|void
name|setCompressionAlgorithm
parameter_list|(
name|AS2CompressionAlgorithm
name|compressionAlgorithm
parameter_list|)
block|{
name|configuration
operator|.
name|setCompressionAlgorithm
argument_list|(
name|compressionAlgorithm
argument_list|)
expr_stmt|;
block|}
DECL|method|getDispositionNotificationTo ()
specifier|public
name|String
name|getDispositionNotificationTo
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getDispositionNotificationTo
argument_list|()
return|;
block|}
DECL|method|setDispositionNotificationTo (String dispositionNotificationTo)
specifier|public
name|void
name|setDispositionNotificationTo
parameter_list|(
name|String
name|dispositionNotificationTo
parameter_list|)
block|{
name|configuration
operator|.
name|setDispositionNotificationTo
argument_list|(
name|dispositionNotificationTo
argument_list|)
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
name|configuration
operator|.
name|getSignedReceiptMicAlgorithms
argument_list|()
return|;
block|}
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
name|configuration
operator|.
name|setSignedReceiptMicAlgorithms
argument_list|(
name|signedReceiptMicAlgorithms
argument_list|)
expr_stmt|;
block|}
DECL|method|getEncryptingAlgorithm ()
specifier|public
name|AS2EncryptionAlgorithm
name|getEncryptingAlgorithm
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getEncryptingAlgorithm
argument_list|()
return|;
block|}
DECL|method|setEncryptingAlgorithm (AS2EncryptionAlgorithm encryptingAlgorithm)
specifier|public
name|void
name|setEncryptingAlgorithm
parameter_list|(
name|AS2EncryptionAlgorithm
name|encryptingAlgorithm
parameter_list|)
block|{
name|configuration
operator|.
name|setEncryptingAlgorithm
argument_list|(
name|encryptingAlgorithm
argument_list|)
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
name|configuration
operator|.
name|getEncryptingCertificateChain
argument_list|()
return|;
block|}
DECL|method|setEncryptingCertificateChain (Certificate[] encryptingCertificateChain)
specifier|public
name|void
name|setEncryptingCertificateChain
parameter_list|(
name|Certificate
index|[]
name|encryptingCertificateChain
parameter_list|)
block|{
name|configuration
operator|.
name|setEncryptingCertificateChain
argument_list|(
name|encryptingCertificateChain
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getPropertiesHelper ()
specifier|protected
name|ApiMethodPropertiesHelper
argument_list|<
name|AS2Configuration
argument_list|>
name|getPropertiesHelper
parameter_list|()
block|{
return|return
name|AS2PropertiesHelper
operator|.
name|getHelper
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getThreadProfileName ()
specifier|protected
name|String
name|getThreadProfileName
parameter_list|()
block|{
return|return
name|AS2Constants
operator|.
name|THREAD_PROFILE_NAME
return|;
block|}
annotation|@
name|Override
DECL|method|afterConfigureProperties ()
specifier|protected
name|void
name|afterConfigureProperties
parameter_list|()
block|{
comment|// create HTTP connection eagerly, a good way to validate configuration
switch|switch
condition|(
name|apiName
condition|)
block|{
case|case
name|CLIENT
case|:
name|createAS2ClientConnection
argument_list|()
expr_stmt|;
break|break;
case|case
name|SERVER
case|:
name|createAS2ServerConnection
argument_list|()
expr_stmt|;
break|break;
default|default:
break|break;
block|}
block|}
annotation|@
name|Override
DECL|method|getApiProxy (ApiMethod method, Map<String, Object> args)
specifier|public
name|Object
name|getApiProxy
parameter_list|(
name|ApiMethod
name|method
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
parameter_list|)
block|{
if|if
condition|(
name|apiProxy
operator|==
literal|null
condition|)
block|{
name|createApiProxy
argument_list|(
name|method
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
return|return
name|apiProxy
return|;
block|}
DECL|method|createApiProxy (ApiMethod method, Map<String, Object> args)
specifier|private
name|void
name|createApiProxy
parameter_list|(
name|ApiMethod
name|method
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
parameter_list|)
block|{
switch|switch
condition|(
name|apiName
condition|)
block|{
case|case
name|CLIENT
case|:
name|apiProxy
operator|=
operator|new
name|AS2ClientManager
argument_list|(
name|getAS2ClientConnection
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|SERVER
case|:
name|apiProxy
operator|=
operator|new
name|AS2ServerManager
argument_list|(
name|getAS2ServerConnection
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid API name "
operator|+
name|apiName
argument_list|)
throw|;
block|}
block|}
DECL|method|createAS2ClientConnection ()
specifier|private
name|void
name|createAS2ClientConnection
parameter_list|()
block|{
try|try
block|{
name|as2ClientConnection
operator|=
name|AS2ConnectionHelper
operator|.
name|createAS2ClientConnection
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownHostException
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
literal|"Client HTTP connection failed: Unknown target host '%s'"
argument_list|,
name|configuration
operator|.
name|getTargetHostname
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Client HTTP connection failed"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|createAS2ServerConnection ()
specifier|private
name|void
name|createAS2ServerConnection
parameter_list|()
block|{
try|try
block|{
name|as2ServerConnection
operator|=
name|AS2ConnectionHelper
operator|.
name|createAS2ServerConnection
argument_list|(
name|configuration
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
name|RuntimeCamelException
argument_list|(
literal|"Server HTTP connection failed"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

