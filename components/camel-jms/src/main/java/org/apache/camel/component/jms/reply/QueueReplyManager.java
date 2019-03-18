begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|component
operator|.
name|jms
operator|.
name|DefaultSpringErrorHandler
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
name|ReplyToType
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
comment|/**  * A {@link ReplyManager} when using regular queues.  */
end_comment

begin_class
DECL|class|QueueReplyManager
specifier|public
class|class
name|QueueReplyManager
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
DECL|method|QueueReplyManager (CamelContext camelContext)
specifier|public
name|QueueReplyManager
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|createReplyHandler (ReplyManager replyManager, Exchange exchange, AsyncCallback callback, String originalCorrelationId, String correlationId, long requestTimeout)
specifier|protected
name|ReplyHandler
name|createReplyHandler
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
return|return
operator|new
name|QueueReplyHandler
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
name|requestTimeout
argument_list|)
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
name|log
operator|.
name|trace
argument_list|(
literal|"Updated provisional correlationId [{}] to expected correlationId [{}]"
argument_list|,
name|correlationId
argument_list|,
name|newCorrelationId
argument_list|)
expr_stmt|;
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
block|}
DECL|method|handleReplyMessage (String correlationID, Message message, Session session)
specifier|protected
name|void
name|handleReplyMessage
parameter_list|(
name|String
name|correlationID
parameter_list|,
name|Message
name|message
parameter_list|,
name|Session
name|session
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
name|correlation
operator|.
name|remove
argument_list|(
name|correlationID
argument_list|)
expr_stmt|;
name|handler
operator|.
name|onReply
argument_list|(
name|correlationID
argument_list|,
name|message
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// we could not correlate the received reply message to a matching request and therefore
comment|// we cannot continue routing the unknown message
comment|// log a warn and then ignore the message
name|log
operator|.
name|warn
argument_list|(
literal|"Reply received for unknown correlationID [{}] on reply destination [{}]. Current correlation map size: {}. The message will be ignored: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|correlationID
block|,
name|replyTo
block|,
name|correlation
operator|.
name|size
argument_list|()
block|,
name|message
block|}
argument_list|)
expr_stmt|;
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
name|QueueReplyManager
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
name|ReplyToType
name|type
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getReplyToType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
comment|// use shared by default for reply queues
name|type
operator|=
name|ReplyToType
operator|.
name|Shared
expr_stmt|;
block|}
if|if
condition|(
name|ReplyToType
operator|.
name|Shared
operator|==
name|type
condition|)
block|{
comment|// shared reply to queues support either a fixed or dynamic JMS message selector
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
comment|// create a random selector value we will use for the reply queue
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
name|SharedQueueMessageListenerContainer
argument_list|(
name|endpoint
argument_list|,
name|fixedMessageSelector
argument_list|)
expr_stmt|;
comment|// must use cache level consumer for fixed message selector
name|answer
operator|.
name|setCacheLevel
argument_list|(
name|DefaultMessageListenerContainer
operator|.
name|CACHE_CONSUMER
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Using shared queue: {} with fixed message selector [{}] as reply listener: {}"
argument_list|,
name|endpoint
operator|.
name|getReplyTo
argument_list|()
argument_list|,
name|fixedMessageSelector
argument_list|,
name|answer
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
argument_list|(
name|correlation
argument_list|)
expr_stmt|;
name|answer
operator|=
operator|new
name|SharedQueueMessageListenerContainer
argument_list|(
name|endpoint
argument_list|,
name|dynamicMessageSelector
argument_list|)
expr_stmt|;
comment|// must use cache level session for dynamic message selector,
comment|// as otherwise the dynamic message selector will not be updated on-the-fly
name|answer
operator|.
name|setCacheLevel
argument_list|(
name|DefaultMessageListenerContainer
operator|.
name|CACHE_SESSION
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Using shared queue: {} with dynamic message selector as reply listener: {}"
argument_list|,
name|endpoint
operator|.
name|getReplyTo
argument_list|()
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
comment|// shared is not as fast as temporary or exclusive, so log this so the end user may be aware of this
name|log
operator|.
name|warn
argument_list|(
literal|"{} is using a shared reply queue, which is not as fast as alternatives."
operator|+
literal|" See more detail at the section 'Request-reply over JMS' at http://camel.apache.org/jms"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ReplyToType
operator|.
name|Exclusive
operator|==
name|type
condition|)
block|{
name|answer
operator|=
operator|new
name|ExclusiveQueueMessageListenerContainer
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
comment|// must use cache level consumer for exclusive as there is no message selector
name|answer
operator|.
name|setCacheLevel
argument_list|(
name|DefaultMessageListenerContainer
operator|.
name|CACHE_CONSUMER
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Using exclusive queue: {} as reply listener: {}"
argument_list|,
name|endpoint
operator|.
name|getReplyTo
argument_list|()
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ReplyToType "
operator|+
name|type
operator|+
literal|" is not supported for reply queues"
argument_list|)
throw|;
block|}
name|String
name|replyToCacheLevelName
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getReplyToCacheLevelName
argument_list|()
decl_stmt|;
if|if
condition|(
name|replyToCacheLevelName
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setCacheLevelName
argument_list|(
name|replyToCacheLevelName
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Setting the replyCacheLevel to be {}"
argument_list|,
name|replyToCacheLevelName
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
name|setIdleConsumerLimit
argument_list|(
name|endpoint
operator|.
name|getIdleConsumerLimit
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setIdleTaskExecutionLimit
argument_list|(
name|endpoint
operator|.
name|getIdleTaskExecutionLimit
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getMaxMessagesPerTask
argument_list|()
operator|>=
literal|0
condition|)
block|{
name|answer
operator|.
name|setMaxMessagesPerTask
argument_list|(
name|endpoint
operator|.
name|getMaxMessagesPerTask
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|endpoint
operator|.
name|getReplyToConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getReplyToMaxConcurrentConsumers
argument_list|()
operator|>
literal|0
condition|)
block|{
name|answer
operator|.
name|setMaxConcurrentConsumers
argument_list|(
name|endpoint
operator|.
name|getReplyToMaxConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|getErrorHandler
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setErrorHandler
argument_list|(
name|endpoint
operator|.
name|getErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|.
name|setErrorHandler
argument_list|(
operator|new
name|DefaultSpringErrorHandler
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|QueueReplyManager
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|getErrorHandlerLoggingLevel
argument_list|()
argument_list|,
name|endpoint
operator|.
name|isErrorHandlerLogStackTrace
argument_list|()
argument_list|)
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
comment|// set task executor
if|if
condition|(
name|endpoint
operator|.
name|getTaskExecutor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using custom TaskExecutor: {} on listener container: {}"
argument_list|,
name|endpoint
operator|.
name|getTaskExecutor
argument_list|()
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setTaskExecutor
argument_list|(
name|endpoint
operator|.
name|getTaskExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// setup a bean name which is used by Spring JMS as the thread name
name|String
name|name
init|=
literal|"QueueReplyManager["
operator|+
name|answer
operator|.
name|getDestinationName
argument_list|()
operator|+
literal|"]"
decl_stmt|;
name|answer
operator|.
name|setBeanName
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|.
name|getConcurrentConsumers
argument_list|()
operator|>
literal|1
condition|)
block|{
if|if
condition|(
name|ReplyToType
operator|.
name|Shared
operator|==
name|type
condition|)
block|{
comment|// warn if using concurrent consumer with shared reply queue as that may not work properly
name|log
operator|.
name|warn
argument_list|(
literal|"Using {}-{} concurrent consumer on {} with shared queue {} may not work properly with all message brokers."
argument_list|,
operator|new
name|Object
index|[]
block|{
name|answer
operator|.
name|getConcurrentConsumers
argument_list|()
block|,
name|answer
operator|.
name|getMaxConcurrentConsumers
argument_list|()
block|,
name|name
block|,
name|endpoint
operator|.
name|getReplyTo
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// log that we are using concurrent consumers
name|log
operator|.
name|info
argument_list|(
literal|"Using {}-{} concurrent consumers on {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|answer
operator|.
name|getConcurrentConsumers
argument_list|()
block|,
name|answer
operator|.
name|getMaxConcurrentConsumers
argument_list|()
block|,
name|name
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

