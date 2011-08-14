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
name|RejectedExecutionException
import|;
end_import

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
name|Connection
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
name|spi
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
name|ValueHolder
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|support
operator|.
name|JmsUtils
import|;
end_import

begin_import
import|import static
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
name|JmsMessageHelper
operator|.
name|normalizeDestinationName
import|;
end_import

begin_comment
comment|/**  * @version   */
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
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
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
comment|// validate that replyToType and replyTo is configured accordingly
if|if
condition|(
name|endpoint
operator|.
name|getReplyToType
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// setting temporary with a fixed replyTo is not supported
if|if
condition|(
name|endpoint
operator|.
name|getReplyTo
argument_list|()
operator|!=
literal|null
operator|&&
name|endpoint
operator|.
name|getReplyToType
argument_list|()
operator|==
name|ReplyToType
operator|.
name|Temporary
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ReplyToType "
operator|+
name|ReplyToType
operator|.
name|Temporary
operator|+
literal|" is not supported when replyTo "
operator|+
name|endpoint
operator|.
name|getReplyTo
argument_list|()
operator|+
literal|" is also configured."
argument_list|)
throw|;
block|}
block|}
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
argument_list|)
expr_stmt|;
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Using JmsReplyManager: "
operator|+
name|replyManager
operator|+
literal|" to process replies from temporary queue"
argument_list|)
expr_stmt|;
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
comment|// deny processing if we are not started
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RejectedExecutionException
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// we cannot process so invoke callback
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
comment|// get the reply to destination to be used from the reply manager
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
name|JmsMessageHelper
operator|.
name|setJMSReplyTo
argument_list|(
name|message
argument_list|,
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
specifier|final
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
name|answer
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
comment|// when in InOnly mode the JMSReplyTo is a bit complicated
comment|// we only want to set the JMSReplyTo on the answer if
comment|// there is a JMSReplyTo from the header/endpoint and
comment|// we have been told to preserveMessageQos
name|Object
name|jmsReplyTo
init|=
name|JmsMessageHelper
operator|.
name|getJMSReplyTo
argument_list|(
name|answer
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isDisableReplyTo
argument_list|()
condition|)
block|{
comment|// honor disable reply to configuration
name|LOG
operator|.
name|debug
argument_list|(
literal|"ReplyTo is disabled on endpoint: {}"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|JmsMessageHelper
operator|.
name|setJMSReplyTo
argument_list|(
name|answer
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if the binding did not create the reply to then we have to try to create it here
if|if
condition|(
name|jmsReplyTo
operator|==
literal|null
condition|)
block|{
comment|// prefer reply to from header over endpoint configured
name|jmsReplyTo
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"JMSReplyTo"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|jmsReplyTo
operator|==
literal|null
condition|)
block|{
name|jmsReplyTo
operator|=
name|endpoint
operator|.
name|getReplyTo
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|// we must honor these special flags to preserve QoS
comment|// as we are not OUT capable and thus do not expect a reply, and therefore
comment|// the consumer of this message should not return a reply so we remove it
comment|// unless we use preserveMessageQos=true to tell that we still want to use JMSReplyTo
if|if
condition|(
name|jmsReplyTo
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|endpoint
operator|.
name|isPreserveMessageQos
argument_list|()
operator|||
name|endpoint
operator|.
name|isExplicitQosEnabled
argument_list|()
operator|)
condition|)
block|{
comment|// log at debug what we are doing, as higher level may cause noise in production logs
comment|// this behavior is also documented at the camel website
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Disabling JMSReplyTo: {} for destination: {}. Use preserveMessageQos=true to force Camel to keep the JMSReplyTo on endpoint: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|jmsReplyTo
block|,
name|to
block|,
name|endpoint
block|}
argument_list|)
expr_stmt|;
block|}
name|jmsReplyTo
operator|=
literal|null
expr_stmt|;
block|}
comment|// the reply to is a String, so we need to look up its Destination instance
comment|// and if needed create the destination using the session if needed to
if|if
condition|(
name|jmsReplyTo
operator|!=
literal|null
operator|&&
name|jmsReplyTo
operator|instanceof
name|String
condition|)
block|{
comment|// must normalize the destination name
name|String
name|replyTo
init|=
name|normalizeDestinationName
argument_list|(
operator|(
name|String
operator|)
name|jmsReplyTo
argument_list|)
decl_stmt|;
comment|// we need to null it as we use the String to resolve it as a Destination instance
name|jmsReplyTo
operator|=
literal|null
expr_stmt|;
comment|// try using destination resolver to lookup the destination
if|if
condition|(
name|endpoint
operator|.
name|getDestinationResolver
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|jmsReplyTo
operator|=
name|endpoint
operator|.
name|getDestinationResolver
argument_list|()
operator|.
name|resolveDestinationName
argument_list|(
name|session
argument_list|,
name|replyTo
argument_list|,
name|endpoint
operator|.
name|isPubSubDomain
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jmsReplyTo
operator|==
literal|null
condition|)
block|{
comment|// okay then fallback and create the queue
if|if
condition|(
name|endpoint
operator|.
name|isPubSubDomain
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating JMSReplyTo topic: {}"
argument_list|,
name|replyTo
argument_list|)
expr_stmt|;
name|jmsReplyTo
operator|=
name|session
operator|.
name|createTopic
argument_list|(
name|replyTo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating JMSReplyTo queue: {}"
argument_list|,
name|replyTo
argument_list|)
expr_stmt|;
name|jmsReplyTo
operator|=
name|session
operator|.
name|createQueue
argument_list|(
name|replyTo
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// set the JMSReplyTo on the answer if we are to use it
name|Destination
name|replyTo
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|jmsReplyTo
operator|instanceof
name|Destination
condition|)
block|{
name|replyTo
operator|=
operator|(
name|Destination
operator|)
name|jmsReplyTo
expr_stmt|;
block|}
if|if
condition|(
name|replyTo
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using JMSReplyTo destination: {}"
argument_list|,
name|replyTo
argument_list|)
expr_stmt|;
name|JmsMessageHelper
operator|.
name|setJMSReplyTo
argument_list|(
name|answer
argument_list|,
name|replyTo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// do not use JMSReplyTo
name|JmsMessageHelper
operator|.
name|setJMSReplyTo
argument_list|(
name|answer
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
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
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using {} jms template"
argument_list|,
name|inOut
condition|?
literal|"inOut"
else|:
literal|"inOnly"
argument_list|)
expr_stmt|;
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
name|exchange
operator|.
name|getOut
argument_list|(
name|JmsMessage
operator|.
name|class
argument_list|)
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
comment|/**      * Pre tests the connection before starting the listening.      *<p/>      * In case of connection failure the exception is thrown which prevents Camel from starting.      *      * @throws FailedToCreateProducerException is thrown if testing the connection failed      */
DECL|method|testConnectionOnStartup ()
specifier|protected
name|void
name|testConnectionOnStartup
parameter_list|()
throws|throws
name|FailedToCreateProducerException
block|{
try|try
block|{
name|CamelJmsTemplate
name|template
init|=
operator|(
name|CamelJmsTemplate
operator|)
name|getInOnlyTemplate
argument_list|()
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Testing JMS Connection on startup for destination: "
operator|+
name|template
operator|.
name|getDefaultDestinationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Connection
name|conn
init|=
name|template
operator|.
name|getConnectionFactory
argument_list|()
operator|.
name|createConnection
argument_list|()
decl_stmt|;
name|JmsUtils
operator|.
name|closeConnection
argument_list|(
name|conn
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Successfully tested JMS Connection on startup for destination: "
operator|+
name|template
operator|.
name|getDefaultDestinationName
argument_list|()
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
name|FailedToCreateProducerException
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
comment|// use the generator configured on the camel context
name|uuidGenerator
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getUuidGenerator
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|isTestConnectionOnStartup
argument_list|()
condition|)
block|{
name|testConnectionOnStartup
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

