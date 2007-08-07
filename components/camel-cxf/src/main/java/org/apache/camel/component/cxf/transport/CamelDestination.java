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
name|ByteArrayInputStream
import|;
end_import

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
name|Producer
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
literal|".camel-destination-base"
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
DECL|field|camelContext
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|camelUri
name|String
name|camelUri
decl_stmt|;
DECL|field|conduitInitiator
specifier|final
name|ConduitInitiator
name|conduitInitiator
decl_stmt|;
DECL|field|base
specifier|private
name|CamelTransportBase
name|base
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
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
name|base
operator|=
operator|new
name|CamelTransportBase
argument_list|(
name|camelContext
argument_list|,
name|bus
argument_list|,
name|endpointInfo
argument_list|,
literal|true
argument_list|,
name|BASE_BEAN_NAME_SUFFIX
argument_list|)
expr_stmt|;
name|conduitInitiator
operator|=
name|ci
expr_stmt|;
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
return|return
operator|new
name|BackChannelConduit
argument_list|(
name|EndpointReferenceUtils
operator|.
name|getAnonymousEndpointReference
argument_list|()
argument_list|,
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
name|INFO
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
name|endpoint
operator|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|camelUri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
name|SEVERE
argument_list|,
literal|"Camel connect failed with EException : "
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
name|base
operator|.
name|close
argument_list|()
expr_stmt|;
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
DECL|method|incoming (Exchange exchange)
specifier|protected
name|void
name|incoming
parameter_list|(
name|Exchange
name|exchange
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
name|exchange
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|base
operator|.
name|unmarshal
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// get the message to be interceptor
name|MessageImpl
name|inMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|inMessage
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
argument_list|)
expr_stmt|;
name|base
operator|.
name|populateIncomingContext
argument_list|(
name|exchange
argument_list|,
name|inMessage
argument_list|,
name|CamelConstants
operator|.
name|CAMEL_SERVER_REQUEST_HEADERS
argument_list|)
expr_stmt|;
comment|// inMessage.put(CamelConstants.CAMEL_SERVER_RESPONSE_HEADERS, new
comment|// CamelMessageHeadersType());
name|inMessage
operator|.
name|put
argument_list|(
name|CamelConstants
operator|.
name|CAMEL_REQUEST_MESSAGE
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setDestination
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// handle the incoming message
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
return|return
name|endpointInfo
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|".camel-destination"
return|;
block|}
DECL|method|initConfig ()
specifier|private
name|void
name|initConfig
parameter_list|()
block|{
comment|/*          * this.runtimePolicy = endpointInfo.getTraversedExtensor(new          * ServerBehaviorPolicyType(), ServerBehaviorPolicyType.class);          * this.serverConfig = endpointInfo.getTraversedExtensor(new          * ServerConfig(), ServerConfig.class); this.address =          * endpointInfo.getTraversedExtensor(new AddressType(),          * AddressType.class); this.sessionPool =          * endpointInfo.getTraversedExtensor(new SessionPoolType(),          * SessionPoolType.class);          */
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
DECL|method|BackChannelConduit (EndpointReferenceType ref, Message message)
name|BackChannelConduit
parameter_list|(
name|EndpointReferenceType
name|ref
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|ref
argument_list|)
expr_stmt|;
name|inMessage
operator|=
name|message
expr_stmt|;
block|}
comment|/**          * Register a message observer for incoming messages.          *           * @param observer the observer to notify on receipt of incoming          */
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
comment|/**          * Send an outbound message, assumed to contain all the name-value          * mappings of the corresponding input message (if any).          *           * @param message the message to be sent.          */
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
comment|// setup the message to be send back
name|message
operator|.
name|put
argument_list|(
name|CamelConstants
operator|.
name|CAMEL_REQUEST_MESSAGE
argument_list|,
name|inMessage
operator|.
name|get
argument_list|(
name|CamelConstants
operator|.
name|CAMEL_REQUEST_MESSAGE
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
name|inMessage
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
DECL|class|CamelOutputStream
specifier|private
class|class
name|CamelOutputStream
extends|extends
name|CachedOutputStream
block|{
DECL|field|inMessage
specifier|private
name|Message
name|inMessage
decl_stmt|;
DECL|field|replyTo
specifier|private
name|Producer
argument_list|<
name|Exchange
argument_list|>
name|replyTo
decl_stmt|;
DECL|field|sender
specifier|private
name|Producer
argument_list|<
name|Exchange
argument_list|>
name|sender
decl_stmt|;
comment|// setup the ByteArrayStream
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
name|inMessage
operator|=
name|m
expr_stmt|;
block|}
comment|// prepair the message and get the send out message
DECL|method|commitOutputMessage ()
specifier|private
name|void
name|commitOutputMessage
parameter_list|()
throws|throws
name|IOException
block|{
comment|// setup the reply message
specifier|final
name|String
name|replyToUri
init|=
name|getReplyToDestination
argument_list|(
name|inMessage
argument_list|)
decl_stmt|;
name|base
operator|.
name|template
operator|.
name|send
argument_list|(
name|replyToUri
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|reply
parameter_list|)
block|{
name|base
operator|.
name|marshal
argument_list|(
name|currentStream
operator|.
name|toString
argument_list|()
argument_list|,
name|replyToUri
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|setReplyCorrelationID
argument_list|(
name|inMessage
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|base
operator|.
name|setMessageProperties
argument_list|(
name|inMessage
argument_list|,
name|reply
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
literal|"just server sending reply: "
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
block|}
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
DECL|method|getReplyToDestination (Message inMessage)
specifier|protected
name|String
name|getReplyToDestination
parameter_list|(
name|Message
name|inMessage
parameter_list|)
block|{
if|if
condition|(
name|inMessage
operator|.
name|get
argument_list|(
name|CamelConstants
operator|.
name|CAMEL_REBASED_REPLY_TO
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|CamelConstants
operator|.
name|CAMEL_REBASED_REPLY_TO
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|base
operator|.
name|getReplyDestination
argument_list|()
return|;
block|}
block|}
DECL|method|setReplyCorrelationID (Message inMessage, Exchange reply)
specifier|protected
name|void
name|setReplyCorrelationID
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|Exchange
name|reply
parameter_list|)
block|{
name|Object
name|value
init|=
name|inMessage
operator|.
name|get
argument_list|(
name|CamelConstants
operator|.
name|CAMEL_CORRELATION_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|reply
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CamelConstants
operator|.
name|CAMEL_CORRELATION_ID
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

