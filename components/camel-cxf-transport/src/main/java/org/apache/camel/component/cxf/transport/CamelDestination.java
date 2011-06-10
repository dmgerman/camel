begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.transport
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|transport
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
name|OutputStream
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
name|FailedToCreateConsumerException
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
name|NoSuchEndpointException
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
name|component
operator|.
name|cxf
operator|.
name|common
operator|.
name|header
operator|.
name|CxfHeaderHelper
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
name|cxf
operator|.
name|common
operator|.
name|message
operator|.
name|DefaultCxfMesssageMapper
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
name|HeaderFilterStrategy
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
name|ObjectHelper
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
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|configuration
operator|.
name|Configurable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|configuration
operator|.
name|Configurer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|io
operator|.
name|CachedOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
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
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|EndpointInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|AbstractConduit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|AbstractDestination
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|Conduit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|ConduitInitiator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|MessageObserver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|wsdl
operator|.
name|EndpointReferenceUtils
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
comment|/**  * @version   *  * Forwards messages from Camel to CXF and the CXF response back to Camel  */
end_comment

begin_class
DECL|class|CamelDestination
specifier|public
class|class
name|CamelDestination
extends|extends
name|AbstractDestination
implements|implements
name|Configurable
block|{
DECL|field|BASE_BEAN_NAME_SUFFIX
specifier|protected
specifier|static
specifier|final
name|String
name|BASE_BEAN_NAME_SUFFIX
init|=
literal|".camel-destination"
decl_stmt|;
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
name|CamelDestination
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// used for places where CXF requires JUL
DECL|field|JUL_LOG
specifier|private
specifier|static
specifier|final
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
name|JUL_LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|CamelDestination
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|conduitInitiator
specifier|final
name|ConduitInitiator
name|conduitInitiator
decl_stmt|;
DECL|field|camelContext
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|consumer
name|Consumer
name|consumer
decl_stmt|;
DECL|field|camelDestinationUri
name|String
name|camelDestinationUri
decl_stmt|;
DECL|field|destinationEndpoint
specifier|private
name|Endpoint
name|destinationEndpoint
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|field|checkException
specifier|private
name|boolean
name|checkException
decl_stmt|;
DECL|method|CamelDestination (CamelContext camelContext, Bus bus, ConduitInitiator ci, EndpointInfo info)
specifier|public
name|CamelDestination
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|ConduitInitiator
name|ci
parameter_list|,
name|EndpointInfo
name|info
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|camelContext
argument_list|,
name|bus
argument_list|,
name|ci
argument_list|,
name|info
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|CamelDestination (CamelContext camelContext, Bus bus, ConduitInitiator ci, EndpointInfo info, HeaderFilterStrategy headerFilterStrategy, boolean checkException)
specifier|public
name|CamelDestination
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|ConduitInitiator
name|ci
parameter_list|,
name|EndpointInfo
name|info
parameter_list|,
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|,
name|boolean
name|checkException
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|bus
argument_list|,
name|getTargetReference
argument_list|(
name|info
argument_list|,
name|bus
argument_list|)
argument_list|,
name|info
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|conduitInitiator
operator|=
name|ci
expr_stmt|;
name|camelDestinationUri
operator|=
name|endpointInfo
operator|.
name|getAddress
argument_list|()
operator|.
name|substring
argument_list|(
name|CamelTransportConstants
operator|.
name|CAMEL_TRANSPORT_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|camelDestinationUri
operator|.
name|startsWith
argument_list|(
literal|"//"
argument_list|)
condition|)
block|{
name|camelDestinationUri
operator|=
name|camelDestinationUri
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|initConfig
argument_list|()
expr_stmt|;
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
name|this
operator|.
name|checkException
operator|=
name|checkException
expr_stmt|;
block|}
DECL|method|getLogger ()
specifier|protected
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|JUL_LOG
return|;
block|}
DECL|method|setCheckException (boolean exception)
specifier|public
name|void
name|setCheckException
parameter_list|(
name|boolean
name|exception
parameter_list|)
block|{
name|checkException
operator|=
name|exception
expr_stmt|;
block|}
DECL|method|isCheckException ()
specifier|public
name|boolean
name|isCheckException
parameter_list|()
block|{
return|return
name|checkException
return|;
block|}
comment|/**      * @param inMessage the incoming message      * @return the inbuilt backchannel      */
DECL|method|getInbuiltBackChannel (Message inMessage)
specifier|protected
name|Conduit
name|getInbuiltBackChannel
parameter_list|(
name|Message
name|inMessage
parameter_list|)
block|{
comment|//we can pass the message back by looking up the camelExchange from inMessage
return|return
operator|new
name|BackChannelConduit
argument_list|(
name|inMessage
argument_list|)
return|;
block|}
DECL|method|activate ()
specifier|public
name|void
name|activate
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"CamelDestination activate().... "
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"CamelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"establishing Camel connection"
argument_list|)
expr_stmt|;
name|destinationEndpoint
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|camelDestinationUri
argument_list|)
expr_stmt|;
if|if
condition|(
name|destinationEndpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchEndpointException
argument_list|(
name|camelDestinationUri
argument_list|)
throw|;
block|}
name|consumer
operator|=
name|destinationEndpoint
operator|.
name|createConsumer
argument_list|(
operator|new
name|ConsumerProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchEndpointException
name|nex
parameter_list|)
block|{
throw|throw
name|nex
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
if|if
condition|(
name|destinationEndpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|FailedToCreateConsumerException
argument_list|(
name|camelDestinationUri
argument_list|,
name|ex
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|FailedToCreateConsumerException
argument_list|(
name|destinationEndpoint
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
DECL|method|deactivate ()
specifier|public
name|void
name|deactivate
parameter_list|()
block|{
try|try
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error stopping consumer"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"CamelDestination shutdown()"
argument_list|)
expr_stmt|;
name|this
operator|.
name|deactivate
argument_list|()
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|incoming (org.apache.camel.Exchange camelExchange)
specifier|protected
name|void
name|incoming
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"server received request: "
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
name|DefaultCxfMesssageMapper
name|beanBinding
init|=
operator|new
name|DefaultCxfMesssageMapper
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|inMessage
init|=
name|beanBinding
operator|.
name|createCxfMessageFromCamelExchange
argument_list|(
name|camelExchange
argument_list|,
name|headerFilterStrategy
argument_list|)
decl_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|CamelTransportConstants
operator|.
name|CAMEL_EXCHANGE
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
operator|(
operator|(
name|MessageImpl
operator|)
name|inMessage
operator|)
operator|.
name|setDestination
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// Handling the incoming message
comment|// The response message will be send back by the outgoing chain
name|incomingObserver
operator|.
name|onMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
block|}
DECL|method|getBeanName ()
specifier|public
name|String
name|getBeanName
parameter_list|()
block|{
if|if
condition|(
name|endpointInfo
operator|==
literal|null
operator|||
name|endpointInfo
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|"default"
operator|+
name|BASE_BEAN_NAME_SUFFIX
return|;
block|}
return|return
name|endpointInfo
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
name|BASE_BEAN_NAME_SUFFIX
return|;
block|}
DECL|method|getCamelDestinationUri ()
specifier|public
name|String
name|getCamelDestinationUri
parameter_list|()
block|{
return|return
name|camelDestinationUri
return|;
block|}
DECL|method|initConfig ()
specifier|private
name|void
name|initConfig
parameter_list|()
block|{
comment|//we could configure the camel context here
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|Configurer
name|configurer
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|Configurer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|configurer
condition|)
block|{
name|configurer
operator|.
name|configureBean
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|ConsumerProcessor
specifier|protected
class|class
name|ConsumerProcessor
implements|implements
name|Processor
block|{
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|incoming
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// this should deal with the cxf message
DECL|class|BackChannelConduit
specifier|protected
class|class
name|BackChannelConduit
extends|extends
name|AbstractConduit
block|{
DECL|field|inMessage
specifier|protected
name|Message
name|inMessage
decl_stmt|;
DECL|field|camelExchange
name|Exchange
name|camelExchange
decl_stmt|;
DECL|field|cxfExchange
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
name|cxfExchange
decl_stmt|;
DECL|method|BackChannelConduit (Message message)
name|BackChannelConduit
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|EndpointReferenceUtils
operator|.
name|getAnonymousEndpointReference
argument_list|()
argument_list|)
expr_stmt|;
name|inMessage
operator|=
name|message
expr_stmt|;
name|cxfExchange
operator|=
name|inMessage
operator|.
name|getExchange
argument_list|()
expr_stmt|;
name|camelExchange
operator|=
name|cxfExchange
operator|.
name|get
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**          * Register a message observer for incoming messages.          *          * @param observer the observer to notify on receipt of incoming          */
DECL|method|setMessageObserver (MessageObserver observer)
specifier|public
name|void
name|setMessageObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{
comment|// shouldn't be called for a back channel conduit
block|}
comment|/**          * Send an outbound message, assumed to contain all the name-value          * mappings of the corresponding input message (if any).          *          * @param message the message to be sent.          */
DECL|method|prepare (Message message)
specifier|public
name|void
name|prepare
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
name|message
operator|.
name|put
argument_list|(
name|CamelTransportConstants
operator|.
name|CAMEL_EXCHANGE
argument_list|,
name|inMessage
operator|.
name|get
argument_list|(
name|CamelTransportConstants
operator|.
name|CAMEL_EXCHANGE
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
operator|new
name|CamelOutputStream
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getLogger ()
specifier|protected
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|JUL_LOG
return|;
block|}
block|}
comment|/**      * Mark message as a partial message.      *      * @param partialResponse the partial response message      * @param decoupledTarget the decoupled target      * @return<tt>true</tt> if partial responses is supported      */
DECL|method|markPartialResponse (Message partialResponse, EndpointReferenceType decoupledTarget)
specifier|protected
name|boolean
name|markPartialResponse
parameter_list|(
name|Message
name|partialResponse
parameter_list|,
name|EndpointReferenceType
name|decoupledTarget
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
comment|/**      * @return the associated conduit initiator      */
DECL|method|getConduitInitiator ()
specifier|protected
name|ConduitInitiator
name|getConduitInitiator
parameter_list|()
block|{
return|return
name|conduitInitiator
return|;
block|}
DECL|method|propagateResponseHeadersToCamel (Message outMessage, Exchange camelExchange)
specifier|protected
name|void
name|propagateResponseHeadersToCamel
parameter_list|(
name|Message
name|outMessage
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
block|{
name|CxfHeaderHelper
operator|.
name|propagateCxfToCamel
argument_list|(
name|headerFilterStrategy
argument_list|,
name|outMessage
argument_list|,
name|camelExchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * Receives a response from CXF and forwards it to the camel route the request came in from      */
DECL|class|CamelOutputStream
specifier|private
class|class
name|CamelOutputStream
extends|extends
name|CachedOutputStream
block|{
DECL|field|outMessage
specifier|private
name|Message
name|outMessage
decl_stmt|;
DECL|method|CamelOutputStream (Message m)
specifier|public
name|CamelOutputStream
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|outMessage
operator|=
name|m
expr_stmt|;
block|}
comment|// Prepare the message and get the send out message
DECL|method|commitOutputMessage ()
specifier|private
name|void
name|commitOutputMessage
parameter_list|()
throws|throws
name|IOException
block|{
name|Exchange
name|camelExchange
init|=
operator|(
name|Exchange
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|CamelTransportConstants
operator|.
name|CAMEL_EXCHANGE
argument_list|)
decl_stmt|;
name|propagateResponseHeadersToCamel
argument_list|(
name|outMessage
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
comment|// check if the outMessage has the exception
name|Exception
name|exception
init|=
name|outMessage
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|checkException
operator|&&
name|exception
operator|!=
literal|null
condition|)
block|{
name|camelExchange
operator|.
name|setException
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
name|CachedOutputStream
name|outputStream
init|=
operator|(
name|CachedOutputStream
operator|)
name|outMessage
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|camelExchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|outputStream
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"send the response message: {}"
argument_list|,
name|outputStream
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doFlush ()
specifier|protected
name|void
name|doFlush
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Do nothing here
block|}
annotation|@
name|Override
DECL|method|doClose ()
specifier|protected
name|void
name|doClose
parameter_list|()
throws|throws
name|IOException
block|{
name|commitOutputMessage
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onWrite ()
specifier|protected
name|void
name|onWrite
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Do nothing here
block|}
block|}
block|}
end_class

end_unit

