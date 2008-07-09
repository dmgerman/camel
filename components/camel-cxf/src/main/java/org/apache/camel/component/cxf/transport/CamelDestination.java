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
name|java
operator|.
name|util
operator|.
name|List
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|ProducerTemplate
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
name|CxfConstants
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
name|CxfSoapBinding
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
name|DefaultCamelContext
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
name|helpers
operator|.
name|CastUtils
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

begin_comment
comment|/**  * @version $Revision$  */
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
DECL|field|camelTemplate
specifier|private
name|ProducerTemplate
argument_list|<
name|Exchange
argument_list|>
name|camelTemplate
decl_stmt|;
DECL|field|distinationEndpoint
specifier|private
name|Endpoint
name|distinationEndpoint
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
name|CxfConstants
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
block|}
DECL|method|getLogger ()
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
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
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"CamelDestination activate().... "
argument_list|)
expr_stmt|;
try|try
block|{
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"establishing Camel connection"
argument_list|)
expr_stmt|;
name|distinationEndpoint
operator|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|camelDestinationUri
argument_list|)
expr_stmt|;
name|consumer
operator|=
name|distinationEndpoint
operator|.
name|createConsumer
argument_list|(
operator|new
name|ConsumerProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// TODO: Is it okay just to log severe errors such as this?
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"Camel connect failed with Exception : "
argument_list|,
name|ex
argument_list|)
expr_stmt|;
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
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// TODO: Is it okay just to log severe errors such as this?
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"Camel stop failed with Exception : "
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
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"CamelDestination shutdown()"
argument_list|)
expr_stmt|;
name|this
operator|.
name|deactivate
argument_list|()
expr_stmt|;
block|}
DECL|method|getCamelTemplate ()
specifier|public
name|ProducerTemplate
argument_list|<
name|Exchange
argument_list|>
name|getCamelTemplate
parameter_list|()
block|{
if|if
condition|(
name|camelTemplate
operator|==
literal|null
condition|)
block|{
name|CamelContext
name|ctx
init|=
name|camelContext
operator|!=
literal|null
condition|?
name|camelContext
else|:
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|camelTemplate
operator|=
name|ctx
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
block|}
return|return
name|camelTemplate
return|;
block|}
DECL|method|setCamelTemplate (ProducerTemplate<Exchange> template)
specifier|public
name|void
name|setCamelTemplate
parameter_list|(
name|ProducerTemplate
argument_list|<
name|Exchange
argument_list|>
name|template
parameter_list|)
block|{
name|camelTemplate
operator|=
name|template
expr_stmt|;
block|}
DECL|method|setCamelContext (CamelContext context)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|camelContext
operator|=
name|context
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
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"server received request: "
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
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
name|CxfSoapBinding
operator|.
name|getCxfInMessage
argument_list|(
name|camelExchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|CxfConstants
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
comment|// The response message will be send back by the outgoingchain
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
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Failed to process incoming message : "
argument_list|,
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
name|CxfConstants
operator|.
name|CAMEL_EXCHANGE
argument_list|,
name|inMessage
operator|.
name|get
argument_list|(
name|CxfConstants
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
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
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
name|CxfConstants
operator|.
name|CAMEL_EXCHANGE
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|protocolHeader
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|CxfSoapBinding
operator|.
name|setProtocolHeader
argument_list|(
name|camelExchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|protocolHeader
argument_list|)
expr_stmt|;
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
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"send the response message: "
operator|+
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

