begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Destination
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
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
name|AsyncCallback
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
name|FailedToCreateProducerException
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
name|RuntimeExchangeException
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
name|jms
operator|.
name|JmsConfiguration
operator|.
name|CamelJmsTemplate
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
name|jms
operator|.
name|JmsConfiguration
operator|.
name|CamelJmsTemplate102
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
name|jms
operator|.
name|reply
operator|.
name|ReplyManager
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
name|jms
operator|.
name|reply
operator|.
name|UseMessageIdAsCorrelationIdMessageSentCallback
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
name|DefaultAsyncProducer
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
name|UuidGenerator
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
name|ValueHolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|JmsOperations
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|MessageCreator
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsProducer
specifier|public
class|class
name|JmsProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|JmsProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|JmsEndpoint
name|endpoint
decl_stmt|;
DECL|field|started
specifier|private
specifier|final
name|AtomicBoolean
name|started
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|inOnlyTemplate
specifier|private
name|JmsOperations
name|inOnlyTemplate
decl_stmt|;
DECL|field|inOutTemplate
specifier|private
name|JmsOperations
name|inOutTemplate
decl_stmt|;
DECL|field|uuidGenerator
specifier|private
name|UuidGenerator
name|uuidGenerator
decl_stmt|;
DECL|field|replyManager
specifier|private
name|ReplyManager
name|replyManager
decl_stmt|;
DECL|method|JmsProducer (JmsEndpoint endpoint)
specifier|public
name|JmsProducer
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|initReplyManager ()
specifier|protected
name|void
name|initReplyManager
parameter_list|()
block|{
if|if
condition|(
operator|!
name|started
operator|.
name|get
argument_list|()
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|started
operator|.
name|get
argument_list|()
condition|)
block|{
return|return;
block|}
try|try
block|{
if|if
condition|(
name|endpoint
operator|.
name|getReplyTo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|replyManager
operator|=
name|endpoint
operator|.
name|getReplyManager
argument_list|(
name|endpoint
operator|.
name|getReplyTo
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Using JmsReplyManager: "
operator|+
name|replyManager
operator|+
literal|" to process replies from: "
operator|+
name|endpoint
operator|.
name|getReplyTo
argument_list|()
operator|+
literal|" queue with "
operator|+
name|endpoint
operator|.
name|getConcurrentConsumers
argument_list|()
operator|+
literal|" concurrent consumers."
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|replyManager
operator|=
name|endpoint
operator|.
name|getReplyManager
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Using JmsReplyManager: "
operator|+
name|replyManager
operator|+
literal|" to process replies from temporary queue with "
operator|+
name|endpoint
operator|.
name|getConcurrentConsumers
argument_list|()
operator|+
literal|" concurrent consumers."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FailedToCreateProducerException
argument_list|(
name|endpoint
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|started
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isDisableReplyTo
argument_list|()
operator|&&
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
comment|// in out requires a bit more work than in only
return|return
name|processInOut
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
else|else
block|{
comment|// in only
return|return
name|processInOnly
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
block|}
DECL|method|processInOut (final Exchange exchange, final AsyncCallback callback)
specifier|protected
name|boolean
name|processInOut
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
specifier|final
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|destinationName
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|JmsConstants
operator|.
name|JMS_DESTINATION_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// remove the header so it wont be propagated
name|in
operator|.
name|removeHeader
argument_list|(
name|JmsConstants
operator|.
name|JMS_DESTINATION_NAME
argument_list|)
expr_stmt|;
if|if
condition|(
name|destinationName
operator|==
literal|null
condition|)
block|{
name|destinationName
operator|=
name|endpoint
operator|.
name|getDestinationName
argument_list|()
expr_stmt|;
block|}
name|Destination
name|destination
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|JmsConstants
operator|.
name|JMS_DESTINATION
argument_list|,
name|Destination
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// remove the header so it wont be propagated
name|in
operator|.
name|removeHeader
argument_list|(
name|JmsConstants
operator|.
name|JMS_DESTINATION
argument_list|)
expr_stmt|;
if|if
condition|(
name|destination
operator|==
literal|null
condition|)
block|{
name|destination
operator|=
name|endpoint
operator|.
name|getDestination
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|destination
operator|!=
literal|null
condition|)
block|{
comment|// prefer to use destination over destination name
name|destinationName
operator|=
literal|null
expr_stmt|;
block|}
name|initReplyManager
argument_list|()
expr_stmt|;
comment|// note due to JMS transaction semantics we cannot use a single transaction
comment|// for sending the request and receiving the response
specifier|final
name|Destination
name|replyTo
init|=
name|replyManager
operator|.
name|getReplyTo
argument_list|()
decl_stmt|;
if|if
condition|(
name|replyTo
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeExchangeException
argument_list|(
literal|"Failed to resolve replyTo destination"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
comment|// when using message id as correlation id, we need at first to use a provisional correlation id
comment|// which we then update to the real JMSMessageID when the message has been sent
comment|// this is done with the help of the MessageSentCallback
specifier|final
name|boolean
name|msgIdAsCorrId
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isUseMessageIDAsCorrelationID
argument_list|()
decl_stmt|;
specifier|final
name|String
name|provisionalCorrelationId
init|=
name|msgIdAsCorrId
condition|?
name|getUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
else|:
literal|null
decl_stmt|;
name|MessageSentCallback
name|messageSentCallback
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|msgIdAsCorrId
condition|)
block|{
name|messageSentCallback
operator|=
operator|new
name|UseMessageIdAsCorrelationIdMessageSentCallback
argument_list|(
name|replyManager
argument_list|,
name|provisionalCorrelationId
argument_list|,
name|endpoint
operator|.
name|getRequestTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|ValueHolder
argument_list|<
name|MessageSentCallback
argument_list|>
name|sentCallback
init|=
operator|new
name|ValueHolder
argument_list|<
name|MessageSentCallback
argument_list|>
argument_list|(
name|messageSentCallback
argument_list|)
decl_stmt|;
specifier|final
name|String
name|originalCorrelationId
init|=
name|in
operator|.
name|getHeader
argument_list|(
literal|"JMSCorrelationID"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|originalCorrelationId
operator|==
literal|null
operator|&&
operator|!
name|msgIdAsCorrId
condition|)
block|{
name|in
operator|.
name|setHeader
argument_list|(
literal|"JMSCorrelationID"
argument_list|,
name|getUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|MessageCreator
name|messageCreator
init|=
operator|new
name|MessageCreator
argument_list|()
block|{
specifier|public
name|Message
name|createMessage
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
name|Message
name|message
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|makeJmsMessage
argument_list|(
name|exchange
argument_list|,
name|in
argument_list|,
name|session
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|message
operator|.
name|setJMSReplyTo
argument_list|(
name|replyTo
argument_list|)
expr_stmt|;
name|replyManager
operator|.
name|setReplyToSelectorHeader
argument_list|(
name|in
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|String
name|correlationId
init|=
name|determineCorrelationId
argument_list|(
name|message
argument_list|,
name|provisionalCorrelationId
argument_list|)
decl_stmt|;
name|replyManager
operator|.
name|registerReply
argument_list|(
name|replyManager
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|,
name|originalCorrelationId
argument_list|,
name|correlationId
argument_list|,
name|endpoint
operator|.
name|getRequestTimeout
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
block|}
decl_stmt|;
name|doSend
argument_list|(
literal|true
argument_list|,
name|destinationName
argument_list|,
name|destination
argument_list|,
name|messageCreator
argument_list|,
name|sentCallback
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// after sending then set the OUT message id to the JMSMessageID so its identical
name|setMessageId
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// continue routing asynchronously (reply will be processed async when its received)
return|return
literal|false
return|;
block|}
comment|/**      * Strategy to determine which correlation id to use among<tt>JMSMessageID</tt> and<tt>JMSCorrelationID</tt>.      *      * @param message   the JMS message      * @param provisionalCorrelationId an optional provisional correlation id, which is preferred to be used      * @return the correlation id to use      * @throws JMSException can be thrown      */
DECL|method|determineCorrelationId (Message message, String provisionalCorrelationId)
specifier|protected
name|String
name|determineCorrelationId
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|provisionalCorrelationId
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|provisionalCorrelationId
operator|!=
literal|null
condition|)
block|{
return|return
name|provisionalCorrelationId
return|;
block|}
specifier|final
name|String
name|messageId
init|=
name|message
operator|.
name|getJMSMessageID
argument_list|()
decl_stmt|;
specifier|final
name|String
name|correlationId
init|=
name|message
operator|.
name|getJMSCorrelationID
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isUseMessageIDAsCorrelationID
argument_list|()
condition|)
block|{
return|return
name|messageId
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|correlationId
argument_list|)
condition|)
block|{
comment|// correlation id is empty so fallback to message id
return|return
name|messageId
return|;
block|}
else|else
block|{
return|return
name|correlationId
return|;
block|}
block|}
DECL|method|processInOnly (final Exchange exchange, final AsyncCallback callback)
specifier|protected
name|boolean
name|processInOnly
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
specifier|final
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|destinationName
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|JmsConstants
operator|.
name|JMS_DESTINATION_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|destinationName
operator|!=
literal|null
condition|)
block|{
comment|// remove the header so it wont be propagated
name|in
operator|.
name|removeHeader
argument_list|(
name|JmsConstants
operator|.
name|JMS_DESTINATION_NAME
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|destinationName
operator|==
literal|null
condition|)
block|{
name|destinationName
operator|=
name|endpoint
operator|.
name|getDestinationName
argument_list|()
expr_stmt|;
block|}
name|Destination
name|destination
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|JmsConstants
operator|.
name|JMS_DESTINATION
argument_list|,
name|Destination
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|destination
operator|!=
literal|null
condition|)
block|{
comment|// remove the header so it wont be propagated
name|in
operator|.
name|removeHeader
argument_list|(
name|JmsConstants
operator|.
name|JMS_DESTINATION
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|destination
operator|==
literal|null
condition|)
block|{
name|destination
operator|=
name|endpoint
operator|.
name|getDestination
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|destination
operator|!=
literal|null
condition|)
block|{
comment|// prefer to use destination over destination name
name|destinationName
operator|=
literal|null
expr_stmt|;
block|}
comment|// we must honor these special flags to preserve QoS
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isPreserveMessageQos
argument_list|()
operator|&&
operator|!
name|endpoint
operator|.
name|isExplicitQosEnabled
argument_list|()
condition|)
block|{
name|Object
name|replyTo
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"JMSReplyTo"
argument_list|)
decl_stmt|;
if|if
condition|(
name|replyTo
operator|!=
literal|null
condition|)
block|{
comment|// we are routing an existing JmsMessage, origin from another JMS endpoint
comment|// then we need to remove the existing JMSReplyTo
comment|// as we are not out capable and thus do not expect a reply, and therefore
comment|// the consumer of this message we send should not return a reply
name|String
name|to
init|=
name|destinationName
operator|!=
literal|null
condition|?
name|destinationName
else|:
literal|""
operator|+
name|destination
decl_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"Disabling JMSReplyTo as this Exchange is not OUT capable with JMSReplyTo: "
operator|+
name|replyTo
operator|+
literal|" for destination: "
operator|+
name|to
operator|+
literal|". Use preserveMessageQos=true to force Camel to keep the JMSReplyTo."
operator|+
literal|" Exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"JMSReplyTo"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
name|MessageCreator
name|messageCreator
init|=
operator|new
name|MessageCreator
argument_list|()
block|{
specifier|public
name|Message
name|createMessage
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|makeJmsMessage
argument_list|(
name|exchange
argument_list|,
name|in
argument_list|,
name|session
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|doSend
argument_list|(
literal|false
argument_list|,
name|destinationName
argument_list|,
name|destination
argument_list|,
name|messageCreator
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// after sending then set the OUT message id to the JMSMessageID so its identical
name|setMessageId
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// we are synchronous so return true
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**      * Sends the message using the JmsTemplate.      *      * @param inOut           use inOut or inOnly template      * @param destinationName the destination name      * @param destination     the destination (if no name provided)      * @param messageCreator  the creator to create the {@link Message} to send      * @param callback        optional callback for inOut messages      */
DECL|method|doSend (boolean inOut, String destinationName, Destination destination, MessageCreator messageCreator, MessageSentCallback callback)
specifier|protected
name|void
name|doSend
parameter_list|(
name|boolean
name|inOut
parameter_list|,
name|String
name|destinationName
parameter_list|,
name|Destination
name|destination
parameter_list|,
name|MessageCreator
name|messageCreator
parameter_list|,
name|MessageSentCallback
name|callback
parameter_list|)
block|{
name|CamelJmsTemplate
name|template
init|=
literal|null
decl_stmt|;
name|CamelJmsTemplate102
name|template102
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isUseVersion102
argument_list|()
condition|)
block|{
name|template102
operator|=
call|(
name|JmsConfiguration
operator|.
name|CamelJmsTemplate102
call|)
argument_list|(
name|inOut
condition|?
name|getInOutTemplate
argument_list|()
else|:
name|getInOnlyTemplate
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template
operator|=
call|(
name|CamelJmsTemplate
call|)
argument_list|(
name|inOut
condition|?
name|getInOutTemplate
argument_list|()
else|:
name|getInOnlyTemplate
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using "
operator|+
operator|(
name|inOut
condition|?
literal|"inOut"
else|:
literal|"inOnly"
operator|)
operator|+
literal|" jms template to send with API "
operator|+
operator|(
name|endpoint
operator|.
name|isUseVersion102
argument_list|()
condition|?
literal|"v1.0.2"
else|:
literal|"v1.1"
operator|)
argument_list|)
expr_stmt|;
block|}
comment|// destination should be preferred
if|if
condition|(
name|destination
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|inOut
condition|)
block|{
if|if
condition|(
name|template
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|send
argument_list|(
name|destination
argument_list|,
name|messageCreator
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|template102
operator|!=
literal|null
condition|)
block|{
name|template102
operator|.
name|send
argument_list|(
name|destination
argument_list|,
name|messageCreator
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|template
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|send
argument_list|(
name|destination
argument_list|,
name|messageCreator
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|template102
operator|!=
literal|null
condition|)
block|{
name|template102
operator|.
name|send
argument_list|(
name|destination
argument_list|,
name|messageCreator
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|destinationName
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|inOut
condition|)
block|{
if|if
condition|(
name|template
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|send
argument_list|(
name|destinationName
argument_list|,
name|messageCreator
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|template102
operator|!=
literal|null
condition|)
block|{
name|template102
operator|.
name|send
argument_list|(
name|destinationName
argument_list|,
name|messageCreator
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|template
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|send
argument_list|(
name|destinationName
argument_list|,
name|messageCreator
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|template102
operator|!=
literal|null
condition|)
block|{
name|template102
operator|.
name|send
argument_list|(
name|destinationName
argument_list|,
name|messageCreator
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Neither destination nor destinationName is specified on this endpoint: "
operator|+
name|endpoint
argument_list|)
throw|;
block|}
block|}
DECL|method|setMessageId (Exchange exchange)
specifier|protected
name|void
name|setMessageId
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|JmsMessage
name|out
init|=
operator|(
name|JmsMessage
operator|)
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|out
operator|!=
literal|null
operator|&&
name|out
operator|.
name|getJmsMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|setMessageId
argument_list|(
name|out
operator|.
name|getJmsMessage
argument_list|()
operator|.
name|getJMSMessageID
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unable to retrieve JMSMessageID from outgoing "
operator|+
literal|"JMS Message and set it into Camel's MessageId"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getInOnlyTemplate ()
specifier|public
name|JmsOperations
name|getInOnlyTemplate
parameter_list|()
block|{
if|if
condition|(
name|inOnlyTemplate
operator|==
literal|null
condition|)
block|{
name|inOnlyTemplate
operator|=
name|endpoint
operator|.
name|createInOnlyTemplate
argument_list|()
expr_stmt|;
block|}
return|return
name|inOnlyTemplate
return|;
block|}
DECL|method|setInOnlyTemplate (JmsOperations inOnlyTemplate)
specifier|public
name|void
name|setInOnlyTemplate
parameter_list|(
name|JmsOperations
name|inOnlyTemplate
parameter_list|)
block|{
name|this
operator|.
name|inOnlyTemplate
operator|=
name|inOnlyTemplate
expr_stmt|;
block|}
DECL|method|getInOutTemplate ()
specifier|public
name|JmsOperations
name|getInOutTemplate
parameter_list|()
block|{
if|if
condition|(
name|inOutTemplate
operator|==
literal|null
condition|)
block|{
name|inOutTemplate
operator|=
name|endpoint
operator|.
name|createInOutTemplate
argument_list|()
expr_stmt|;
block|}
return|return
name|inOutTemplate
return|;
block|}
DECL|method|setInOutTemplate (JmsOperations inOutTemplate)
specifier|public
name|void
name|setInOutTemplate
parameter_list|(
name|JmsOperations
name|inOutTemplate
parameter_list|)
block|{
name|this
operator|.
name|inOutTemplate
operator|=
name|inOutTemplate
expr_stmt|;
block|}
DECL|method|getUuidGenerator ()
specifier|public
name|UuidGenerator
name|getUuidGenerator
parameter_list|()
block|{
return|return
name|uuidGenerator
return|;
block|}
DECL|method|setUuidGenerator (UuidGenerator uuidGenerator)
specifier|public
name|void
name|setUuidGenerator
parameter_list|(
name|UuidGenerator
name|uuidGenerator
parameter_list|)
block|{
name|this
operator|.
name|uuidGenerator
operator|=
name|uuidGenerator
expr_stmt|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|uuidGenerator
operator|==
literal|null
condition|)
block|{
comment|// use the default generator
name|uuidGenerator
operator|=
name|UuidGenerator
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

