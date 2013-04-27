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
name|util
operator|.
name|Iterator
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
name|ExchangePattern
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
name|DefaultConsumer
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
name|DefaultExchange
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
name|context
operator|.
name|MessageContext
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
name|server
operator|.
name|endpoint
operator|.
name|MessageEndpoint
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

begin_class
DECL|class|SpringWebserviceConsumer
specifier|public
class|class
name|SpringWebserviceConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|MessageEndpoint
block|{
DECL|field|endpoint
specifier|private
name|SpringWebserviceEndpoint
name|endpoint
decl_stmt|;
DECL|field|configuration
specifier|private
name|SpringWebserviceConfiguration
name|configuration
decl_stmt|;
DECL|method|SpringWebserviceConsumer (Endpoint endpoint, Processor processor)
specifier|public
name|SpringWebserviceConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
operator|(
name|SpringWebserviceEndpoint
operator|)
name|endpoint
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|this
operator|.
name|endpoint
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
block|}
comment|/**      * Invoked by Spring-WS when a {@link WebServiceMessage} is received      */
DECL|method|invoke (MessageContext messageContext)
specifier|public
name|void
name|invoke
parameter_list|(
name|MessageContext
name|messageContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|ExchangePattern
operator|.
name|InOptionalOut
argument_list|)
decl_stmt|;
name|populateExchangeFromMessageContext
argument_list|(
name|messageContext
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// start message processing
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|exchange
operator|.
name|getException
argument_list|()
throw|;
block|}
elseif|else
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|Message
name|responseMessage
init|=
name|exchange
operator|.
name|getOut
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseMessage
operator|!=
literal|null
condition|)
block|{
name|Source
name|responseBody
init|=
name|responseMessage
operator|.
name|getBody
argument_list|(
name|Source
operator|.
name|class
argument_list|)
decl_stmt|;
name|WebServiceMessage
name|response
init|=
name|messageContext
operator|.
name|getResponse
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|getMessageFilter
argument_list|()
operator|.
name|filterConsumer
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|XmlConverter
name|xmlConverter
init|=
name|configuration
operator|.
name|getXmlConverter
argument_list|()
decl_stmt|;
name|xmlConverter
operator|.
name|toResult
argument_list|(
name|responseBody
argument_list|,
name|response
operator|.
name|getPayloadResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|populateExchangeFromMessageContext (MessageContext messageContext, Exchange exchange)
specifier|private
name|void
name|populateExchangeFromMessageContext
parameter_list|(
name|MessageContext
name|messageContext
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|populateExchangeWithPropertiesFromMessageContext
argument_list|(
name|messageContext
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// create inbound message
name|WebServiceMessage
name|request
init|=
name|messageContext
operator|.
name|getRequest
argument_list|()
decl_stmt|;
name|SpringWebserviceMessage
name|inMessage
init|=
operator|new
name|SpringWebserviceMessage
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|extractSourceFromSoapHeader
argument_list|(
name|inMessage
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
block|}
DECL|method|populateExchangeWithPropertiesFromMessageContext (MessageContext messageContext, Exchange exchange)
specifier|private
name|void
name|populateExchangeWithPropertiesFromMessageContext
parameter_list|(
name|MessageContext
name|messageContext
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// convert WebserviceMessage properties (added through interceptors) to
comment|// Camel exchange properties
name|String
index|[]
name|propertyNames
init|=
name|messageContext
operator|.
name|getPropertyNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|propertyNames
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|propertyName
range|:
name|propertyNames
control|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|propertyName
argument_list|,
name|messageContext
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Extracts the SOAP headers and set them as headers in the Exchange. Also sets      * it as a header with the key SpringWebserviceConstants.SPRING_WS_SOAP_HEADER      * and a value of type Source.      *      * @param headers   the Exchange Headers      * @param request   the WebService Request      */
DECL|method|extractSourceFromSoapHeader (Map<String, Object> headers, WebServiceMessage request)
specifier|private
name|void
name|extractSourceFromSoapHeader
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|WebServiceMessage
name|request
parameter_list|)
block|{
if|if
condition|(
name|request
operator|instanceof
name|SoapMessage
condition|)
block|{
name|SoapMessage
name|soapMessage
init|=
operator|(
name|SoapMessage
operator|)
name|request
decl_stmt|;
name|SoapHeader
name|soapHeader
init|=
name|soapMessage
operator|.
name|getSoapHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|soapHeader
operator|!=
literal|null
condition|)
block|{
comment|//Set the raw soap header as a header in the exchange.
name|headers
operator|.
name|put
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
comment|//Set header values for the soap header attributes
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
name|headers
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
comment|//Set header values for the soap header elements
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
name|headers
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
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|configuration
operator|.
name|getEndpointMapping
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|getEndpointMapping
argument_list|()
operator|.
name|removeConsumer
argument_list|(
name|configuration
operator|.
name|getEndpointMappingKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|configuration
operator|.
name|getEndpointMapping
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|getEndpointMapping
argument_list|()
operator|.
name|addConsumer
argument_list|(
name|configuration
operator|.
name|getEndpointMappingKey
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

