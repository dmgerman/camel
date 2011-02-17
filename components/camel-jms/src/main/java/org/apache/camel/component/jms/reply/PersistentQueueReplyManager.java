begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.reply
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
operator|.
name|reply
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
name|springframework
operator|.
name|jms
operator|.
name|listener
operator|.
name|AbstractMessageListenerContainer
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
name|listener
operator|.
name|DefaultMessageListenerContainer
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
name|destination
operator|.
name|DestinationResolver
import|;
end_import

begin_comment
comment|/**  * A {@link ReplyManager} when using persistent queues.  *  * @version   */
end_comment

begin_class
DECL|class|PersistentQueueReplyManager
specifier|public
class|class
name|PersistentQueueReplyManager
extends|extends
name|ReplyManagerSupport
block|{
DECL|field|replyToSelectorValue
specifier|private
name|String
name|replyToSelectorValue
decl_stmt|;
DECL|field|dynamicMessageSelector
specifier|private
name|MessageSelectorCreator
name|dynamicMessageSelector
decl_stmt|;
DECL|method|registerReply (ReplyManager replyManager, Exchange exchange, AsyncCallback callback, String originalCorrelationId, String correlationId, long requestTimeout)
specifier|public
name|String
name|registerReply
parameter_list|(
name|ReplyManager
name|replyManager
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|String
name|originalCorrelationId
parameter_list|,
name|String
name|correlationId
parameter_list|,
name|long
name|requestTimeout
parameter_list|)
block|{
comment|// add to correlation map
name|PersistentQueueReplyHandler
name|handler
init|=
operator|new
name|PersistentQueueReplyHandler
argument_list|(
name|replyManager
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|,
name|originalCorrelationId
argument_list|,
name|requestTimeout
argument_list|,
name|dynamicMessageSelector
argument_list|)
decl_stmt|;
name|correlation
operator|.
name|put
argument_list|(
name|correlationId
argument_list|,
name|handler
argument_list|,
name|requestTimeout
argument_list|)
expr_stmt|;
if|if
condition|(
name|dynamicMessageSelector
operator|!=
literal|null
condition|)
block|{
comment|// also remember to keep the dynamic selector updated with the new correlation id
name|dynamicMessageSelector
operator|.
name|addCorrelationID
argument_list|(
name|correlationId
argument_list|)
expr_stmt|;
block|}
return|return
name|correlationId
return|;
block|}
DECL|method|updateCorrelationId (String correlationId, String newCorrelationId, long requestTimeout)
specifier|public
name|void
name|updateCorrelationId
parameter_list|(
name|String
name|correlationId
parameter_list|,
name|String
name|newCorrelationId
parameter_list|,
name|long
name|requestTimeout
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Updated provisional correlationId ["
operator|+
name|correlationId
operator|+
literal|"] to expected correlationId ["
operator|+
name|newCorrelationId
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
name|ReplyHandler
name|handler
init|=
name|correlation
operator|.
name|remove
argument_list|(
name|correlationId
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|==
literal|null
condition|)
block|{
comment|// should not happen that we can't find the handler
return|return;
block|}
name|correlation
operator|.
name|put
argument_list|(
name|newCorrelationId
argument_list|,
name|handler
argument_list|,
name|requestTimeout
argument_list|)
expr_stmt|;
comment|// no not arrived early
if|if
condition|(
name|dynamicMessageSelector
operator|!=
literal|null
condition|)
block|{
comment|// also remember to keep the dynamic selector updated with the new correlation id
name|dynamicMessageSelector
operator|.
name|addCorrelationID
argument_list|(
name|newCorrelationId
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|handleReplyMessage (String correlationID, Message message)
specifier|protected
name|void
name|handleReplyMessage
parameter_list|(
name|String
name|correlationID
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|ReplyHandler
name|handler
init|=
name|correlation
operator|.
name|get
argument_list|(
name|correlationID
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|==
literal|null
operator|&&
name|endpoint
operator|.
name|isUseMessageIDAsCorrelationID
argument_list|()
condition|)
block|{
name|handler
operator|=
name|waitForProvisionCorrelationToBeUpdated
argument_list|(
name|correlationID
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|handler
operator|.
name|onReply
argument_list|(
name|correlationID
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|dynamicMessageSelector
operator|!=
literal|null
condition|)
block|{
comment|// also remember to keep the dynamic selector updated with the new correlation id
name|dynamicMessageSelector
operator|.
name|removeCorrelationID
argument_list|(
name|correlationID
argument_list|)
expr_stmt|;
block|}
name|correlation
operator|.
name|remove
argument_list|(
name|correlationID
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// we could not correlate the received reply message to a matching request and therefore
comment|// we cannot continue routing the unknown message
name|String
name|text
init|=
literal|"Reply received for unknown correlationID ["
operator|+
name|correlationID
operator|+
literal|"] -> "
operator|+
name|message
decl_stmt|;
name|log
operator|.
name|warn
argument_list|(
name|text
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|UnknownReplyMessageException
argument_list|(
name|text
argument_list|,
name|message
argument_list|,
name|correlationID
argument_list|)
throw|;
block|}
block|}
DECL|method|setReplyToSelectorHeader (org.apache.camel.Message camelMessage, Message jmsMessage)
specifier|public
name|void
name|setReplyToSelectorHeader
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|,
name|Message
name|jmsMessage
parameter_list|)
throws|throws
name|JMSException
block|{
name|String
name|replyToSelectorName
init|=
name|endpoint
operator|.
name|getReplyToDestinationSelectorName
argument_list|()
decl_stmt|;
if|if
condition|(
name|replyToSelectorName
operator|!=
literal|null
operator|&&
name|replyToSelectorValue
operator|!=
literal|null
condition|)
block|{
name|camelMessage
operator|.
name|setHeader
argument_list|(
name|replyToSelectorName
argument_list|,
name|replyToSelectorValue
argument_list|)
expr_stmt|;
name|jmsMessage
operator|.
name|setStringProperty
argument_list|(
name|replyToSelectorName
argument_list|,
name|replyToSelectorValue
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|DestinationResolverDelegate
specifier|private
specifier|final
class|class
name|DestinationResolverDelegate
implements|implements
name|DestinationResolver
block|{
DECL|field|delegate
specifier|private
name|DestinationResolver
name|delegate
decl_stmt|;
DECL|field|destination
specifier|private
name|Destination
name|destination
decl_stmt|;
DECL|method|DestinationResolverDelegate (DestinationResolver delegate)
specifier|public
name|DestinationResolverDelegate
parameter_list|(
name|DestinationResolver
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
DECL|method|resolveDestinationName (Session session, String destinationName, boolean pubSubDomain)
specifier|public
name|Destination
name|resolveDestinationName
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|destinationName
parameter_list|,
name|boolean
name|pubSubDomain
parameter_list|)
throws|throws
name|JMSException
block|{
synchronized|synchronized
init|(
name|PersistentQueueReplyManager
operator|.
name|this
init|)
block|{
comment|// resolve the reply to destination
if|if
condition|(
name|destination
operator|==
literal|null
condition|)
block|{
name|destination
operator|=
name|delegate
operator|.
name|resolveDestinationName
argument_list|(
name|session
argument_list|,
name|destinationName
argument_list|,
name|pubSubDomain
argument_list|)
expr_stmt|;
name|setReplyTo
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|destination
return|;
block|}
block|}
empty_stmt|;
DECL|class|PersistentQueueMessageListenerContainer
specifier|private
specifier|final
class|class
name|PersistentQueueMessageListenerContainer
extends|extends
name|DefaultMessageListenerContainer
block|{
DECL|field|fixedMessageSelector
specifier|private
name|String
name|fixedMessageSelector
decl_stmt|;
DECL|field|creator
specifier|private
name|MessageSelectorCreator
name|creator
decl_stmt|;
DECL|method|PersistentQueueMessageListenerContainer (String fixedMessageSelector)
specifier|private
name|PersistentQueueMessageListenerContainer
parameter_list|(
name|String
name|fixedMessageSelector
parameter_list|)
block|{
name|this
operator|.
name|fixedMessageSelector
operator|=
name|fixedMessageSelector
expr_stmt|;
block|}
DECL|method|PersistentQueueMessageListenerContainer (MessageSelectorCreator creator)
specifier|private
name|PersistentQueueMessageListenerContainer
parameter_list|(
name|MessageSelectorCreator
name|creator
parameter_list|)
block|{
name|this
operator|.
name|creator
operator|=
name|creator
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMessageSelector ()
specifier|public
name|String
name|getMessageSelector
parameter_list|()
block|{
name|String
name|id
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|fixedMessageSelector
operator|!=
literal|null
condition|)
block|{
name|id
operator|=
name|fixedMessageSelector
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|creator
operator|!=
literal|null
condition|)
block|{
name|id
operator|=
name|creator
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Using MessageSelector["
operator|+
name|id
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
return|return
name|id
return|;
block|}
block|}
DECL|method|createListenerContainer ()
specifier|protected
name|AbstractMessageListenerContainer
name|createListenerContainer
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultMessageListenerContainer
name|answer
decl_stmt|;
name|String
name|replyToSelectorName
init|=
name|endpoint
operator|.
name|getReplyToDestinationSelectorName
argument_list|()
decl_stmt|;
if|if
condition|(
name|replyToSelectorName
operator|!=
literal|null
condition|)
block|{
comment|// create a random selector value we will use for the persistent reply queue
name|replyToSelectorValue
operator|=
literal|"ID:"
operator|+
operator|new
name|BigInteger
argument_list|(
literal|24
operator|*
literal|8
argument_list|,
operator|new
name|Random
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|(
literal|16
argument_list|)
expr_stmt|;
name|String
name|fixedMessageSelector
init|=
name|replyToSelectorName
operator|+
literal|"='"
operator|+
name|replyToSelectorValue
operator|+
literal|"'"
decl_stmt|;
name|answer
operator|=
operator|new
name|PersistentQueueMessageListenerContainer
argument_list|(
name|fixedMessageSelector
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use a dynamic message selector which will select the message we want to receive as reply
name|dynamicMessageSelector
operator|=
operator|new
name|MessageSelectorCreator
argument_list|()
expr_stmt|;
name|answer
operator|=
operator|new
name|PersistentQueueMessageListenerContainer
argument_list|(
name|dynamicMessageSelector
argument_list|)
expr_stmt|;
block|}
name|DestinationResolver
name|resolver
init|=
name|endpoint
operator|.
name|getDestinationResolver
argument_list|()
decl_stmt|;
if|if
condition|(
name|resolver
operator|==
literal|null
condition|)
block|{
name|resolver
operator|=
name|answer
operator|.
name|getDestinationResolver
argument_list|()
expr_stmt|;
block|}
name|answer
operator|.
name|setDestinationResolver
argument_list|(
operator|new
name|DestinationResolverDelegate
argument_list|(
name|resolver
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setDestinationName
argument_list|(
name|endpoint
operator|.
name|getReplyTo
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setAutoStartup
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setMessageListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setPubSubDomain
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setSubscriptionDurable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setConcurrentConsumers
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setConnectionFactory
argument_list|(
name|endpoint
operator|.
name|getConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|clientId
init|=
name|endpoint
operator|.
name|getClientId
argument_list|()
decl_stmt|;
if|if
condition|(
name|clientId
operator|!=
literal|null
condition|)
block|{
name|clientId
operator|+=
literal|".CamelReplyManager"
expr_stmt|;
name|answer
operator|.
name|setClientId
argument_list|(
name|clientId
argument_list|)
expr_stmt|;
block|}
comment|// must use cache level session
name|answer
operator|.
name|setCacheLevel
argument_list|(
name|DefaultMessageListenerContainer
operator|.
name|CACHE_SESSION
argument_list|)
expr_stmt|;
comment|// we cannot do request-reply over JMS with transaction
name|answer
operator|.
name|setSessionTransacted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// other optional properties
if|if
condition|(
name|endpoint
operator|.
name|getExceptionListener
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setExceptionListener
argument_list|(
name|endpoint
operator|.
name|getExceptionListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getReceiveTimeout
argument_list|()
operator|>=
literal|0
condition|)
block|{
name|answer
operator|.
name|setReceiveTimeout
argument_list|(
name|endpoint
operator|.
name|getReceiveTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getRecoveryInterval
argument_list|()
operator|>=
literal|0
condition|)
block|{
name|answer
operator|.
name|setRecoveryInterval
argument_list|(
name|endpoint
operator|.
name|getRecoveryInterval
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// do not use a task executor for reply as we are are always a single threaded task
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

