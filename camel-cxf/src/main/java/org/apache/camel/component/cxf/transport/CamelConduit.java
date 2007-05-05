begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Processor
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
name|AbstractCachedOutputStream
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
name|Destination
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
name|AttributedURIType
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
name|ByteArrayOutputStream
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelConduit
specifier|public
class|class
name|CamelConduit
extends|extends
name|AbstractConduit
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
literal|".camel-conduit-base"
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
name|CamelConduit
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|base
specifier|private
specifier|final
name|CamelTransportBase
name|base
decl_stmt|;
DECL|field|targetCamelEndpointUri
specifier|private
name|String
name|targetCamelEndpointUri
decl_stmt|;
comment|/*     protected ClientConfig clientConfig;     protected ClientBehaviorPolicyType runtimePolicy;     protected AddressType address;     protected SessionPoolType sessionPool; */
DECL|method|CamelConduit (CamelContext camelContext, Bus bus, EndpointInfo endpointInfo, EndpointReferenceType targetReference)
specifier|public
name|CamelConduit
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|EndpointReferenceType
name|targetReference
parameter_list|)
block|{
name|super
argument_list|(
name|targetReference
argument_list|)
expr_stmt|;
name|AttributedURIType
name|address
init|=
name|targetReference
operator|.
name|getAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|address
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|targetCamelEndpointUri
operator|=
name|address
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
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
literal|false
argument_list|,
name|BASE_BEAN_NAME_SUFFIX
argument_list|)
expr_stmt|;
name|initConfig
argument_list|()
expr_stmt|;
block|}
comment|// prepare the message for send out , not actually send out the message
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
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"CamelConduit send message"
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
DECL|method|close ()
specifier|public
name|void
name|close
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
literal|"CamelConduit closed "
argument_list|)
expr_stmt|;
comment|// ensure resources held by session factory are released
comment|//
name|base
operator|.
name|close
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
DECL|method|getBeanName ()
specifier|public
name|String
name|getBeanName
parameter_list|()
block|{
name|EndpointInfo
name|info
init|=
name|base
operator|.
name|endpointInfo
decl_stmt|;
if|if
condition|(
name|info
operator|==
literal|null
condition|)
block|{
return|return
literal|"default.camel-conduit"
return|;
block|}
return|return
name|info
operator|.
name|getName
argument_list|()
operator|+
literal|".camel-conduit"
return|;
block|}
DECL|method|initConfig ()
specifier|private
name|void
name|initConfig
parameter_list|()
block|{
comment|/*         this.address = base.endpointInfo.getTraversedExtensor(new AddressType(),                                                               AddressType.class);         this.sessionPool = base.endpointInfo.getTraversedExtensor(new SessionPoolType(),                                                                   SessionPoolType.class);         this.clientConfig = base.endpointInfo.getTraversedExtensor(new ClientConfig(),                                                                    ClientConfig.class);         this.runtimePolicy = base.endpointInfo.getTraversedExtensor(new ClientBehaviorPolicyType(),                                                                     ClientBehaviorPolicyType.class); */
name|Configurer
name|configurer
init|=
name|base
operator|.
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
DECL|class|CamelOutputStream
specifier|private
class|class
name|CamelOutputStream
extends|extends
name|AbstractCachedOutputStream
block|{
DECL|field|outMessage
specifier|private
name|Message
name|outMessage
decl_stmt|;
DECL|field|isOneWay
specifier|private
name|boolean
name|isOneWay
decl_stmt|;
DECL|method|CamelOutputStream (Message m)
specifier|public
name|CamelOutputStream
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|outMessage
operator|=
name|m
expr_stmt|;
block|}
DECL|method|doFlush ()
specifier|protected
name|void
name|doFlush
parameter_list|()
throws|throws
name|IOException
block|{
comment|//do nothing here
block|}
DECL|method|doClose ()
specifier|protected
name|void
name|doClose
parameter_list|()
throws|throws
name|IOException
block|{
name|isOneWay
operator|=
name|outMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|isOneWay
argument_list|()
expr_stmt|;
name|commitOutputMessage
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|isOneWay
condition|)
block|{
name|handleResponse
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|onWrite ()
specifier|protected
name|void
name|onWrite
parameter_list|()
throws|throws
name|IOException
block|{          }
DECL|method|commitOutputMessage ()
specifier|private
name|void
name|commitOutputMessage
parameter_list|()
block|{
name|base
operator|.
name|template
operator|.
name|send
argument_list|(
name|targetCamelEndpointUri
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|reply
parameter_list|)
block|{
name|Object
name|request
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isTextPayload
argument_list|()
condition|)
block|{
name|request
operator|=
name|currentStream
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|request
operator|=
operator|(
operator|(
name|ByteArrayOutputStream
operator|)
name|currentStream
operator|)
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
block|}
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Conduit Request is :["
operator|+
name|request
operator|+
literal|"]"
argument_list|)
expr_stmt|;
name|String
name|replyTo
init|=
name|base
operator|.
name|getReplyDestination
argument_list|()
decl_stmt|;
comment|//TODO setting up the responseExpected
name|base
operator|.
name|marshal
argument_list|(
name|request
argument_list|,
name|replyTo
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|base
operator|.
name|setMessageProperties
argument_list|(
name|outMessage
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|String
name|correlationID
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|isOneWay
condition|)
block|{
comment|// TODO create a correlationID
name|String
name|id
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|correlationID
operator|!=
literal|null
condition|)
block|{
name|String
name|error
init|=
literal|"User cannot set CamelCorrelationID when "
operator|+
literal|"making a request/reply invocation using "
operator|+
literal|"a static replyTo Queue."
decl_stmt|;
block|}
name|correlationID
operator|=
name|id
expr_stmt|;
block|}
block|}
if|if
condition|(
name|correlationID
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
name|correlationID
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//No message correlation id is set. Whatever comeback will be accepted as responses.
comment|// We assume that it will only happen in case of the temp. reply queue.
block|}
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"template sending request: "
argument_list|,
name|reply
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|handleResponse ()
specifier|private
name|void
name|handleResponse
parameter_list|()
throws|throws
name|IOException
block|{
comment|// REVISIT distinguish decoupled case or oneway call
name|Object
name|response
init|=
literal|null
decl_stmt|;
comment|//TODO if outMessage need to get the response
name|Message
name|inMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|outMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|setInMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
comment|//set the message header back to the incomeMessage
comment|//inMessage.put(CamelConstants.CAMEL_CLIENT_RESPONSE_HEADERS,
comment|//              outMessage.get(CamelConstants.CAMEL_CLIENT_RESPONSE_HEADERS));
comment|/*             Object result1;              Object result = null;              javax.camel.Message camelMessage1 = pooledSession.consumer().receive(timeout);             getLogger().log(Level.FINE, "template received reply: " , camelMessage1);              if (camelMessage1 != null) {                  base.populateIncomingContext(camelMessage1, outMessage, CamelConstants.CAMEL_CLIENT_RESPONSE_HEADERS);                 String messageType = camelMessage1 instanceof TextMessage                             ? CamelConstants.TEXT_MESSAGE_TYPE : CamelConstants.BINARY_MESSAGE_TYPE;                 result = base.unmarshal((org.apache.camel.Exchange) outMessage);                 result1 = result;             } else {                 String error = "CamelClientTransport.receive() timed out. No message available.";                 getLogger().log(Level.SEVERE, error);                 //TODO: Review what exception should we throw.                 throw new CamelException(error);              }             response = result1;              //set the message header back to the incomeMessage             inMessage.put(CamelConstants.CAMEL_CLIENT_RESPONSE_HEADERS,                           outMessage.get(CamelConstants.CAMEL_CLIENT_RESPONSE_HEADERS));              */
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"The Response Message is : ["
operator|+
name|response
operator|+
literal|"]"
argument_list|)
expr_stmt|;
comment|// setup the inMessage response stream
name|byte
index|[]
name|bytes
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|response
operator|instanceof
name|String
condition|)
block|{
name|String
name|requestString
init|=
operator|(
name|String
operator|)
name|response
decl_stmt|;
name|bytes
operator|=
name|requestString
operator|.
name|getBytes
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|bytes
operator|=
operator|(
name|byte
index|[]
operator|)
name|response
expr_stmt|;
block|}
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
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"incoming observer is "
operator|+
name|incomingObserver
argument_list|)
expr_stmt|;
name|incomingObserver
operator|.
name|onMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isTextPayload ()
specifier|private
name|boolean
name|isTextPayload
parameter_list|()
block|{
comment|// TODO use runtime policy
return|return
literal|true
return|;
block|}
comment|/**      * Represented decoupled response endpoint.      */
DECL|class|DecoupledDestination
specifier|protected
class|class
name|DecoupledDestination
implements|implements
name|Destination
block|{
DECL|field|decoupledMessageObserver
specifier|protected
name|MessageObserver
name|decoupledMessageObserver
decl_stmt|;
DECL|field|address
specifier|private
name|EndpointReferenceType
name|address
decl_stmt|;
DECL|method|DecoupledDestination (EndpointReferenceType ref, MessageObserver incomingObserver)
name|DecoupledDestination
parameter_list|(
name|EndpointReferenceType
name|ref
parameter_list|,
name|MessageObserver
name|incomingObserver
parameter_list|)
block|{
name|address
operator|=
name|ref
expr_stmt|;
name|decoupledMessageObserver
operator|=
name|incomingObserver
expr_stmt|;
block|}
DECL|method|getAddress ()
specifier|public
name|EndpointReferenceType
name|getAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
DECL|method|getBackChannel (Message inMessage, Message partialResponse, EndpointReferenceType addr)
specifier|public
name|Conduit
name|getBackChannel
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|Message
name|partialResponse
parameter_list|,
name|EndpointReferenceType
name|addr
parameter_list|)
throws|throws
name|IOException
block|{
comment|// shouldn't be called on decoupled endpoint
return|return
literal|null
return|;
block|}
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
block|}
DECL|method|setMessageObserver (MessageObserver observer)
specifier|public
specifier|synchronized
name|void
name|setMessageObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{
name|decoupledMessageObserver
operator|=
name|observer
expr_stmt|;
block|}
DECL|method|getMessageObserver ()
specifier|public
specifier|synchronized
name|MessageObserver
name|getMessageObserver
parameter_list|()
block|{
return|return
name|decoupledMessageObserver
return|;
block|}
block|}
block|}
end_class

end_unit

