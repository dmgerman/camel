begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
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
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HttpsURLConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
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
name|CamelContext
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
name|Endpoint
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
name|Message
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
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
name|impl
operator|.
name|DefaultProducer
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
name|ExchangeHelper
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|WebServiceMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|client
operator|.
name|core
operator|.
name|WebServiceMessageCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|client
operator|.
name|core
operator|.
name|WebServiceTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|mime
operator|.
name|Attachment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|SoapHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|SoapHeaderElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|SoapMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|addressing
operator|.
name|client
operator|.
name|ActionCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|addressing
operator|.
name|core
operator|.
name|EndpointReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|soap
operator|.
name|client
operator|.
name|core
operator|.
name|SoapActionCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|transport
operator|.
name|WebServiceConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|transport
operator|.
name|WebServiceMessageSender
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|transport
operator|.
name|http
operator|.
name|AbstractHttpWebServiceMessageSender
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|transport
operator|.
name|http
operator|.
name|HttpComponentsMessageSender
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|transport
operator|.
name|http
operator|.
name|HttpUrlConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|transport
operator|.
name|http
operator|.
name|HttpUrlConnectionMessageSender
import|;
end_import

begin_class
DECL|class|SpringWebserviceProducer
specifier|public
class|class
name|SpringWebserviceProducer
extends|extends
name|DefaultProducer
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
name|SpringWebserviceProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|XML_CONVERTER
specifier|private
specifier|static
specifier|final
name|XmlConverter
name|XML_CONVERTER
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
DECL|method|SpringWebserviceProducer (Endpoint endpoint)
specifier|public
name|SpringWebserviceProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|prepareMessageSenders
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SpringWebserviceEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SpringWebserviceEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Let Camel TypeConverter hierarchy handle the conversion of XML messages to Source objects
name|Source
name|sourcePayload
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|Source
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Extract optional headers
name|String
name|endpointUriHeader
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SpringWebserviceConstants
operator|.
name|SPRING_WS_ENDPOINT_URI
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|soapActionHeader
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SpringWebserviceConstants
operator|.
name|SPRING_WS_SOAP_ACTION
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|URI
name|wsAddressingActionHeader
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SpringWebserviceConstants
operator|.
name|SPRING_WS_ADDRESSING_ACTION
argument_list|,
name|URI
operator|.
name|class
argument_list|)
decl_stmt|;
name|URI
name|wsReplyToHeader
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SpringWebserviceConstants
operator|.
name|SPRING_WS_ADDRESSING_PRODUCER_REPLY_TO
argument_list|,
name|URI
operator|.
name|class
argument_list|)
decl_stmt|;
name|URI
name|wsFaultToHeader
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SpringWebserviceConstants
operator|.
name|SPRING_WS_ADDRESSING_PRODUCER_FAULT_TO
argument_list|,
name|URI
operator|.
name|class
argument_list|)
decl_stmt|;
name|Source
name|soapHeaderSource
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SpringWebserviceConstants
operator|.
name|SPRING_WS_SOAP_HEADER
argument_list|,
name|Source
operator|.
name|class
argument_list|)
decl_stmt|;
name|WebServiceMessageCallback
name|callback
init|=
operator|new
name|DefaultWebserviceMessageCallback
argument_list|(
name|soapActionHeader
argument_list|,
name|wsAddressingActionHeader
argument_list|,
name|wsReplyToHeader
argument_list|,
name|wsFaultToHeader
argument_list|,
name|soapHeaderSource
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpointUriHeader
operator|==
literal|null
condition|)
block|{
name|endpointUriHeader
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getWebServiceTemplate
argument_list|()
operator|.
name|getDefaultUri
argument_list|()
expr_stmt|;
block|}
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getWebServiceTemplate
argument_list|()
operator|.
name|sendAndReceive
argument_list|(
name|endpointUriHeader
argument_list|,
operator|new
name|WebServiceMessageCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|doWithMessage
parameter_list|(
name|WebServiceMessage
name|requestMessage
parameter_list|)
throws|throws
name|IOException
throws|,
name|TransformerException
block|{
name|XML_CONVERTER
operator|.
name|toResult
argument_list|(
name|sourcePayload
argument_list|,
name|requestMessage
operator|.
name|getPayloadResult
argument_list|()
argument_list|)
expr_stmt|;
name|callback
operator|.
name|doWithMessage
argument_list|(
name|requestMessage
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
operator|new
name|WebServiceMessageCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|doWithMessage
parameter_list|(
name|WebServiceMessage
name|responseMessage
parameter_list|)
throws|throws
name|IOException
throws|,
name|TransformerException
block|{
name|SoapMessage
name|soapMessage
init|=
operator|(
name|SoapMessage
operator|)
name|responseMessage
decl_stmt|;
if|if
condition|(
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|copyFromWithNewBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|soapMessage
operator|.
name|getPayloadSource
argument_list|()
argument_list|)
expr_stmt|;
name|populateHeaderAndAttachmentsFromResponse
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
name|soapMessage
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|soapMessage
operator|.
name|getPayloadSource
argument_list|()
argument_list|)
expr_stmt|;
name|populateHeaderAndAttachmentsFromResponse
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|soapMessage
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Populates soap message headers and attachments from soap response      * @param inOrOut {@link Message}      * @param soapMessage {@link SoapMessage}      */
DECL|method|populateHeaderAndAttachmentsFromResponse (Message inOrOut, SoapMessage soapMessage)
specifier|private
name|void
name|populateHeaderAndAttachmentsFromResponse
parameter_list|(
name|Message
name|inOrOut
parameter_list|,
name|SoapMessage
name|soapMessage
parameter_list|)
block|{
if|if
condition|(
name|soapMessage
operator|.
name|getSoapHeader
argument_list|()
operator|!=
literal|null
operator|&&
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isAllowResponseHeaderOverride
argument_list|()
condition|)
block|{
name|populateMessageHeaderFromResponse
argument_list|(
name|inOrOut
argument_list|,
name|soapMessage
operator|.
name|getSoapHeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|soapMessage
operator|.
name|getAttachments
argument_list|()
operator|!=
literal|null
operator|&&
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isAllowResponseAttachmentOverride
argument_list|()
condition|)
block|{
name|populateMessageAttachmentsFromResponse
argument_list|(
name|inOrOut
argument_list|,
name|soapMessage
operator|.
name|getAttachments
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Populates message headers from soapHeader response      *       * @param message      *            Message      * @param soapHeader      *            SoapHeader      */
DECL|method|populateMessageHeaderFromResponse (Message message, SoapHeader soapHeader)
specifier|private
name|void
name|populateMessageHeaderFromResponse
parameter_list|(
name|Message
name|message
parameter_list|,
name|SoapHeader
name|soapHeader
parameter_list|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|SpringWebserviceConstants
operator|.
name|SPRING_WS_SOAP_HEADER
argument_list|,
name|soapHeader
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
comment|// Set header values for the soap header attributes
name|Iterator
argument_list|<
name|QName
argument_list|>
name|attIter
init|=
name|soapHeader
operator|.
name|getAllAttributes
argument_list|()
decl_stmt|;
while|while
condition|(
name|attIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|QName
name|name
init|=
name|attIter
operator|.
name|next
argument_list|()
decl_stmt|;
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|soapHeader
operator|.
name|getAttributeValue
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Set header values for the soap header elements
name|Iterator
argument_list|<
name|SoapHeaderElement
argument_list|>
name|elementIter
init|=
name|soapHeader
operator|.
name|examineAllHeaderElements
argument_list|()
decl_stmt|;
while|while
condition|(
name|elementIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|SoapHeaderElement
name|element
init|=
name|elementIter
operator|.
name|next
argument_list|()
decl_stmt|;
name|QName
name|name
init|=
name|element
operator|.
name|getName
argument_list|()
decl_stmt|;
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Populates message attachments from soap response attachments       * @param inOrOut {@link Message}      * @param soapMessage {@link SoapMessage}      */
DECL|method|populateMessageAttachmentsFromResponse (Message inOrOut, Iterator<Attachment> attachments)
specifier|private
name|void
name|populateMessageAttachmentsFromResponse
parameter_list|(
name|Message
name|inOrOut
parameter_list|,
name|Iterator
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|)
block|{
while|while
condition|(
name|attachments
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Attachment
name|attachment
init|=
name|attachments
operator|.
name|next
argument_list|()
decl_stmt|;
name|inOrOut
operator|.
name|getAttachments
argument_list|()
operator|.
name|put
argument_list|(
name|attachment
operator|.
name|getContentId
argument_list|()
argument_list|,
name|attachment
operator|.
name|getDataHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|prepareMessageSenders (SpringWebserviceConfiguration configuration)
specifier|private
name|void
name|prepareMessageSenders
parameter_list|(
name|SpringWebserviceConfiguration
name|configuration
parameter_list|)
block|{
comment|// Skip this whole thing if none of the relevant config options are set.
if|if
condition|(
operator|!
operator|(
name|configuration
operator|.
name|getTimeout
argument_list|()
operator|>
operator|-
literal|1
operator|)
operator|&&
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|WebServiceTemplate
name|webServiceTemplate
init|=
name|configuration
operator|.
name|getWebServiceTemplate
argument_list|()
decl_stmt|;
name|WebServiceMessageSender
index|[]
name|messageSenders
init|=
name|webServiceTemplate
operator|.
name|getMessageSenders
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|messageSenders
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|WebServiceMessageSender
name|messageSender
init|=
name|messageSenders
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|messageSender
operator|instanceof
name|HttpComponentsMessageSender
condition|)
block|{
if|if
condition|(
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Not applying SSLContextParameters based configuration to HttpComponentsMessageSender.  "
operator|+
literal|"If you are using this MessageSender, which you are not by default, you will need "
operator|+
literal|"to configure SSL using the Commons HTTP 3.x Protocol registry."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getTimeout
argument_list|()
operator|>
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|messageSender
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|HttpComponentsMessageSender
operator|.
name|class
argument_list|)
condition|)
block|{
operator|(
operator|(
name|HttpComponentsMessageSender
operator|)
name|messageSender
operator|)
operator|.
name|setReadTimeout
argument_list|(
name|configuration
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Not applying timeout configuration to HttpComponentsMessageSender based implementation.  "
operator|+
literal|"You are using what appears to be a custom MessageSender, which you are not doing by default. "
operator|+
literal|"You will need configure timeout on your own."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|messageSender
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|HttpUrlConnectionMessageSender
operator|.
name|class
argument_list|)
condition|)
block|{
comment|// Only if exact match denoting likely use of default configuration.  We don't want to get
comment|// sub-classes that might have been otherwise injected.
name|messageSenders
index|[
name|i
index|]
operator|=
operator|new
name|AbstractHttpWebServiceMessageSenderDecorator
argument_list|(
operator|(
name|HttpUrlConnectionMessageSender
operator|)
name|messageSender
argument_list|,
name|configuration
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// For example this will be the case during unit-testing with the net.javacrumbs.spring-ws-test API
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring the timeout and SSLContextParameters options for {}.  You will need to configure "
operator|+
literal|"these options directly on your custom configured WebServiceMessageSender"
argument_list|,
name|messageSender
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * A decorator of {@link HttpUrlConnectionMessageSender} instances that can apply configuration options      * from the Camel component/endpoint configuration without replacing the actual implementation which may      * actually be an end-user implementation and not one of the built-in implementations.      */
DECL|class|AbstractHttpWebServiceMessageSenderDecorator
specifier|protected
specifier|static
specifier|final
class|class
name|AbstractHttpWebServiceMessageSenderDecorator
extends|extends
name|AbstractHttpWebServiceMessageSender
block|{
DECL|field|delegate
specifier|private
specifier|final
name|AbstractHttpWebServiceMessageSender
name|delegate
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|SpringWebserviceConfiguration
name|configuration
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|sslContext
specifier|private
name|SSLContext
name|sslContext
decl_stmt|;
DECL|method|AbstractHttpWebServiceMessageSenderDecorator (AbstractHttpWebServiceMessageSender delegate, SpringWebserviceConfiguration configuration, CamelContext camelContext)
specifier|public
name|AbstractHttpWebServiceMessageSenderDecorator
parameter_list|(
name|AbstractHttpWebServiceMessageSender
name|delegate
parameter_list|,
name|SpringWebserviceConfiguration
name|configuration
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConnection (URI uri)
specifier|public
name|WebServiceConnection
name|createConnection
parameter_list|(
name|URI
name|uri
parameter_list|)
throws|throws
name|IOException
block|{
name|WebServiceConnection
name|wsc
init|=
name|delegate
operator|.
name|createConnection
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|wsc
operator|instanceof
name|HttpUrlConnection
condition|)
block|{
name|HttpURLConnection
name|connection
init|=
operator|(
operator|(
name|HttpUrlConnection
operator|)
name|wsc
operator|)
operator|.
name|getConnection
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getTimeout
argument_list|()
operator|>
operator|-
literal|1
condition|)
block|{
name|connection
operator|.
name|setReadTimeout
argument_list|(
name|configuration
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
operator|!=
literal|null
operator|&&
name|connection
operator|instanceof
name|HttpsURLConnection
condition|)
block|{
try|try
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|sslContext
operator|==
literal|null
condition|)
block|{
name|sslContext
operator|=
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
operator|.
name|createSSLContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Error creating SSLContext based on SSLContextParameters."
argument_list|,
name|e
argument_list|)
throw|;
block|}
operator|(
operator|(
name|HttpsURLConnection
operator|)
name|connection
operator|)
operator|.
name|setSSLSocketFactory
argument_list|(
name|sslContext
operator|.
name|getSocketFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unsupported delegate.  Delegate must return a org.springframework.ws.transport.http.HttpUrlConnection.  Found "
operator|+
name|wsc
operator|.
name|getClass
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|wsc
return|;
block|}
annotation|@
name|Override
DECL|method|isAcceptGzipEncoding ()
specifier|public
name|boolean
name|isAcceptGzipEncoding
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|isAcceptGzipEncoding
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setAcceptGzipEncoding (boolean acceptGzipEncoding)
specifier|public
name|void
name|setAcceptGzipEncoding
parameter_list|(
name|boolean
name|acceptGzipEncoding
parameter_list|)
block|{
name|delegate
operator|.
name|setAcceptGzipEncoding
argument_list|(
name|acceptGzipEncoding
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|supports (URI uri)
specifier|public
name|boolean
name|supports
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|supports
argument_list|(
name|uri
argument_list|)
return|;
block|}
block|}
DECL|class|DefaultWebserviceMessageCallback
specifier|protected
specifier|static
class|class
name|DefaultWebserviceMessageCallback
implements|implements
name|WebServiceMessageCallback
block|{
DECL|field|soapActionHeader
specifier|private
specifier|final
name|String
name|soapActionHeader
decl_stmt|;
DECL|field|wsAddressingActionHeader
specifier|private
specifier|final
name|URI
name|wsAddressingActionHeader
decl_stmt|;
DECL|field|wsReplyToHeader
specifier|private
specifier|final
name|URI
name|wsReplyToHeader
decl_stmt|;
DECL|field|wsFaultToHeader
specifier|private
specifier|final
name|URI
name|wsFaultToHeader
decl_stmt|;
DECL|field|soapHeaderSource
specifier|private
specifier|final
name|Source
name|soapHeaderSource
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|SpringWebserviceConfiguration
name|configuration
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|method|DefaultWebserviceMessageCallback (String soapAction, URI wsAddressingAction, URI wsReplyTo, URI wsFaultTo, Source soapHeaderSource, SpringWebserviceConfiguration configuration, Exchange exchange)
specifier|public
name|DefaultWebserviceMessageCallback
parameter_list|(
name|String
name|soapAction
parameter_list|,
name|URI
name|wsAddressingAction
parameter_list|,
name|URI
name|wsReplyTo
parameter_list|,
name|URI
name|wsFaultTo
parameter_list|,
name|Source
name|soapHeaderSource
parameter_list|,
name|SpringWebserviceConfiguration
name|configuration
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|soapActionHeader
operator|=
name|soapAction
expr_stmt|;
name|this
operator|.
name|wsAddressingActionHeader
operator|=
name|wsAddressingAction
expr_stmt|;
name|this
operator|.
name|wsReplyToHeader
operator|=
name|wsReplyTo
expr_stmt|;
name|this
operator|.
name|wsFaultToHeader
operator|=
name|wsFaultTo
expr_stmt|;
name|this
operator|.
name|soapHeaderSource
operator|=
name|soapHeaderSource
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
block|}
DECL|method|doWithMessage (WebServiceMessage message)
specifier|public
name|void
name|doWithMessage
parameter_list|(
name|WebServiceMessage
name|message
parameter_list|)
throws|throws
name|IOException
throws|,
name|TransformerException
block|{
comment|// Add SoapAction to webservice request. Note that exchange header
comment|// takes precedence over endpoint option
name|String
name|soapAction
init|=
name|soapActionHeader
operator|!=
literal|null
condition|?
name|soapActionHeader
else|:
name|configuration
operator|.
name|getSoapAction
argument_list|()
decl_stmt|;
if|if
condition|(
name|soapAction
operator|!=
literal|null
condition|)
block|{
operator|new
name|SoapActionCallback
argument_list|(
name|soapAction
argument_list|)
operator|.
name|doWithMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|// Add WS-Addressing Action to webservice request (the WS-Addressing
comment|// 'to' header will default to the URL of the connection).
comment|// Note that exchange header takes precedence over endpoint option
name|URI
name|wsAddressingAction
init|=
name|wsAddressingActionHeader
operator|!=
literal|null
condition|?
name|wsAddressingActionHeader
else|:
name|configuration
operator|.
name|getWsAddressingAction
argument_list|()
decl_stmt|;
name|URI
name|wsReplyTo
init|=
name|wsReplyToHeader
operator|!=
literal|null
condition|?
name|wsReplyToHeader
else|:
name|configuration
operator|.
name|getReplyTo
argument_list|()
decl_stmt|;
name|URI
name|wsFaultTo
init|=
name|wsFaultToHeader
operator|!=
literal|null
condition|?
name|wsFaultToHeader
else|:
name|configuration
operator|.
name|getFaultTo
argument_list|()
decl_stmt|;
comment|// Create the SOAP header
if|if
condition|(
name|soapHeaderSource
operator|!=
literal|null
condition|)
block|{
name|SoapHeader
name|header
init|=
operator|(
operator|(
name|SoapMessage
operator|)
name|message
operator|)
operator|.
name|getSoapHeader
argument_list|()
decl_stmt|;
name|XML_CONVERTER
operator|.
name|toResult
argument_list|(
name|soapHeaderSource
argument_list|,
name|header
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsAddressingAction
operator|!=
literal|null
condition|)
block|{
name|ActionCallback
name|actionCallback
init|=
operator|new
name|ActionCallback
argument_list|(
name|wsAddressingAction
argument_list|)
decl_stmt|;
if|if
condition|(
name|wsReplyTo
operator|!=
literal|null
condition|)
block|{
name|actionCallback
operator|.
name|setReplyTo
argument_list|(
operator|new
name|EndpointReference
argument_list|(
name|wsReplyTo
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wsFaultTo
operator|!=
literal|null
condition|)
block|{
name|actionCallback
operator|.
name|setFaultTo
argument_list|(
operator|new
name|EndpointReference
argument_list|(
name|wsFaultTo
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|actionCallback
operator|.
name|doWithMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|configuration
operator|.
name|getMessageFilter
argument_list|()
operator|.
name|filterProducer
argument_list|(
name|exchange
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

