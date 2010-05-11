begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.requestor
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
name|requestor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|concurrent
operator|.
name|FutureTask
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
name|ScheduledExecutorService
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
name|ExceptionListener
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
name|MessageListener
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
name|javax
operator|.
name|jms
operator|.
name|TemporaryQueue
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
name|JmsProducer
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
name|requestor
operator|.
name|DeferredRequestReplyMap
operator|.
name|DeferredMessageSentCallback
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
name|ServiceSupport
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
name|DefaultTimeoutMap
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
name|TimeoutMap
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
name|core
operator|.
name|task
operator|.
name|TaskExecutor
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
name|SimpleMessageListenerContainer
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
name|SimpleMessageListenerContainer102
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
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|Requestor
specifier|public
class|class
name|Requestor
extends|extends
name|ServiceSupport
implements|implements
name|MessageListener
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
name|Requestor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|uuidGenerator
specifier|private
specifier|static
name|UuidGenerator
name|uuidGenerator
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|JmsConfiguration
name|configuration
decl_stmt|;
DECL|field|executorService
specifier|private
name|ScheduledExecutorService
name|executorService
decl_stmt|;
DECL|field|listenerContainer
specifier|private
name|AbstractMessageListenerContainer
name|listenerContainer
decl_stmt|;
DECL|field|requestMap
specifier|private
name|TimeoutMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestMap
decl_stmt|;
DECL|field|producerDeferredRequestReplyMap
specifier|private
name|Map
argument_list|<
name|JmsProducer
argument_list|,
name|DeferredRequestReplyMap
argument_list|>
name|producerDeferredRequestReplyMap
decl_stmt|;
DECL|field|deferredRequestMap
specifier|private
name|TimeoutMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|deferredRequestMap
decl_stmt|;
DECL|field|deferredReplyMap
specifier|private
name|TimeoutMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|deferredReplyMap
decl_stmt|;
DECL|field|replyTo
specifier|private
name|Destination
name|replyTo
decl_stmt|;
DECL|field|maxRequestTimeout
specifier|private
name|long
name|maxRequestTimeout
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|replyToResolverTimeout
specifier|private
name|long
name|replyToResolverTimeout
init|=
literal|5000
decl_stmt|;
DECL|method|Requestor (JmsConfiguration configuration, ScheduledExecutorService executorService)
specifier|public
name|Requestor
parameter_list|(
name|JmsConfiguration
name|configuration
parameter_list|,
name|ScheduledExecutorService
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
name|requestMap
operator|=
operator|new
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|executorService
argument_list|,
name|configuration
operator|.
name|getRequestMapPurgePollTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|producerDeferredRequestReplyMap
operator|=
operator|new
name|HashMap
argument_list|<
name|JmsProducer
argument_list|,
name|DeferredRequestReplyMap
argument_list|>
argument_list|()
expr_stmt|;
name|deferredRequestMap
operator|=
operator|new
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|executorService
argument_list|,
name|configuration
operator|.
name|getRequestMapPurgePollTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|deferredReplyMap
operator|=
operator|new
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|executorService
argument_list|,
name|configuration
operator|.
name|getRequestMapPurgePollTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getDeferredRequestReplyMap (JmsProducer producer)
specifier|public
specifier|synchronized
name|DeferredRequestReplyMap
name|getDeferredRequestReplyMap
parameter_list|(
name|JmsProducer
name|producer
parameter_list|)
block|{
name|DeferredRequestReplyMap
name|map
init|=
name|producerDeferredRequestReplyMap
operator|.
name|get
argument_list|(
name|producer
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
name|map
operator|=
operator|new
name|DeferredRequestReplyMap
argument_list|(
name|this
argument_list|,
name|producer
argument_list|,
name|deferredRequestMap
argument_list|,
name|deferredReplyMap
argument_list|)
expr_stmt|;
name|producerDeferredRequestReplyMap
operator|.
name|put
argument_list|(
name|producer
argument_list|,
name|map
argument_list|)
expr_stmt|;
if|if
condition|(
name|maxRequestTimeout
operator|==
operator|-
literal|1
condition|)
block|{
name|maxRequestTimeout
operator|=
name|producer
operator|.
name|getRequestTimeout
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|maxRequestTimeout
operator|<
name|producer
operator|.
name|getRequestTimeout
argument_list|()
condition|)
block|{
name|maxRequestTimeout
operator|=
name|producer
operator|.
name|getRequestTimeout
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
block|}
DECL|method|removeDeferredRequestReplyMap (JmsProducer producer)
specifier|public
specifier|synchronized
name|void
name|removeDeferredRequestReplyMap
parameter_list|(
name|JmsProducer
name|producer
parameter_list|)
block|{
name|DeferredRequestReplyMap
name|map
init|=
name|producerDeferredRequestReplyMap
operator|.
name|remove
argument_list|(
name|producer
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
comment|// already removed;
return|return;
block|}
if|if
condition|(
name|maxRequestTimeout
operator|==
name|producer
operator|.
name|getRequestTimeout
argument_list|()
condition|)
block|{
name|long
name|max
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|JmsProducer
argument_list|,
name|DeferredRequestReplyMap
argument_list|>
name|entry
range|:
name|producerDeferredRequestReplyMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|max
operator|<
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|getRequestTimeout
argument_list|()
condition|)
block|{
name|max
operator|=
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|getRequestTimeout
argument_list|()
expr_stmt|;
block|}
block|}
name|maxRequestTimeout
operator|=
name|max
expr_stmt|;
block|}
block|}
DECL|method|getMaxRequestTimeout ()
specifier|public
specifier|synchronized
name|long
name|getMaxRequestTimeout
parameter_list|()
block|{
return|return
name|maxRequestTimeout
return|;
block|}
DECL|method|getRequestMap ()
specifier|public
name|TimeoutMap
name|getRequestMap
parameter_list|()
block|{
return|return
name|requestMap
return|;
block|}
DECL|method|getDeferredRequestMap ()
specifier|public
name|TimeoutMap
name|getDeferredRequestMap
parameter_list|()
block|{
return|return
name|deferredRequestMap
return|;
block|}
DECL|method|getDeferredReplyMap ()
specifier|public
name|TimeoutMap
name|getDeferredReplyMap
parameter_list|()
block|{
return|return
name|deferredReplyMap
return|;
block|}
DECL|method|getReceiveFuture (String correlationID, long requestTimeout)
specifier|public
name|FutureTask
name|getReceiveFuture
parameter_list|(
name|String
name|correlationID
parameter_list|,
name|long
name|requestTimeout
parameter_list|)
block|{
name|FutureHandler
name|future
init|=
name|createFutureHandler
argument_list|(
name|correlationID
argument_list|)
decl_stmt|;
name|requestMap
operator|.
name|put
argument_list|(
name|correlationID
argument_list|,
name|future
argument_list|,
name|requestTimeout
argument_list|)
expr_stmt|;
return|return
name|future
return|;
block|}
DECL|method|getReceiveFuture (DeferredMessageSentCallback callback)
specifier|public
name|FutureTask
name|getReceiveFuture
parameter_list|(
name|DeferredMessageSentCallback
name|callback
parameter_list|)
block|{
name|FutureHandler
name|future
init|=
name|createFutureHandler
argument_list|(
name|callback
argument_list|)
decl_stmt|;
name|DeferredRequestReplyMap
name|map
init|=
name|callback
operator|.
name|getDeferredRequestReplyMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|callback
argument_list|,
name|future
argument_list|)
expr_stmt|;
return|return
name|future
return|;
block|}
DECL|method|createFutureHandler (String correlationID)
specifier|protected
name|FutureHandler
name|createFutureHandler
parameter_list|(
name|String
name|correlationID
parameter_list|)
block|{
return|return
operator|new
name|FutureHandler
argument_list|()
return|;
block|}
DECL|method|createFutureHandler (DeferredMessageSentCallback callback)
specifier|protected
name|FutureHandler
name|createFutureHandler
parameter_list|(
name|DeferredMessageSentCallback
name|callback
parameter_list|)
block|{
return|return
operator|new
name|FutureHandler
argument_list|()
return|;
block|}
DECL|method|onMessage (Message message)
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|String
name|correlationID
init|=
name|message
operator|.
name|getJMSCorrelationID
argument_list|()
decl_stmt|;
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
literal|"Message correlationID: "
operator|+
name|correlationID
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|correlationID
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring message with no correlationID: "
operator|+
name|message
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// lets notify the monitor for this response
name|Object
name|handler
init|=
name|requestMap
operator|.
name|get
argument_list|(
name|correlationID
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|!=
literal|null
operator|&&
name|handler
operator|instanceof
name|ReplyHandler
condition|)
block|{
name|ReplyHandler
name|replyHandler
init|=
operator|(
name|ReplyHandler
operator|)
name|handler
decl_stmt|;
name|boolean
name|complete
init|=
name|replyHandler
operator|.
name|handle
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|complete
condition|)
block|{
name|requestMap
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
name|DeferredRequestReplyMap
operator|.
name|processDeferredRequests
argument_list|(
name|this
argument_list|,
name|deferredRequestMap
argument_list|,
name|deferredReplyMap
argument_list|,
name|correlationID
argument_list|,
name|getMaxRequestTimeout
argument_list|()
argument_list|,
name|message
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
throw|throw
operator|new
name|FailedToProcessResponse
argument_list|(
name|message
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getListenerContainer ()
specifier|public
name|AbstractMessageListenerContainer
name|getListenerContainer
parameter_list|()
block|{
if|if
condition|(
name|listenerContainer
operator|==
literal|null
condition|)
block|{
name|listenerContainer
operator|=
name|createListenerContainer
argument_list|()
expr_stmt|;
block|}
return|return
name|listenerContainer
return|;
block|}
DECL|method|setListenerContainer (AbstractMessageListenerContainer listenerContainer)
specifier|public
name|void
name|setListenerContainer
parameter_list|(
name|AbstractMessageListenerContainer
name|listenerContainer
parameter_list|)
block|{
name|this
operator|.
name|listenerContainer
operator|=
name|listenerContainer
expr_stmt|;
block|}
DECL|method|getReplyTo ()
specifier|public
name|Destination
name|getReplyTo
parameter_list|()
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
try|try
block|{
if|if
condition|(
name|replyTo
operator|==
literal|null
condition|)
block|{
name|wait
argument_list|(
name|replyToResolverTimeout
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// eat it
block|}
block|}
return|return
name|replyTo
return|;
block|}
DECL|method|setReplyTo (Destination replyTo)
specifier|public
name|void
name|setReplyTo
parameter_list|(
name|Destination
name|replyTo
parameter_list|)
block|{
name|this
operator|.
name|replyTo
operator|=
name|replyTo
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
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
name|AbstractMessageListenerContainer
name|container
init|=
name|getListenerContainer
argument_list|()
decl_stmt|;
name|container
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
comment|// Need to call the container start in Spring 3.x
name|container
operator|.
name|start
argument_list|()
expr_stmt|;
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
name|listenerContainer
operator|!=
literal|null
condition|)
block|{
name|listenerContainer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|listenerContainer
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getOutterInstance ()
specifier|protected
name|Requestor
name|getOutterInstance
parameter_list|()
block|{
return|return
name|this
return|;
block|}
DECL|method|createListenerContainer ()
specifier|protected
name|AbstractMessageListenerContainer
name|createListenerContainer
parameter_list|()
block|{
name|SimpleMessageListenerContainer
name|answer
init|=
name|configuration
operator|.
name|isUseVersion102
argument_list|()
condition|?
operator|new
name|SimpleMessageListenerContainer102
argument_list|()
else|:
operator|new
name|SimpleMessageListenerContainer
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setDestinationName
argument_list|(
literal|"temporary"
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setDestinationResolver
argument_list|(
operator|new
name|DestinationResolver
argument_list|()
block|{
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
name|TemporaryQueue
name|queue
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|getOutterInstance
argument_list|()
init|)
block|{
try|try
block|{
name|queue
operator|=
name|session
operator|.
name|createTemporaryQueue
argument_list|()
expr_stmt|;
name|setReplyTo
argument_list|(
name|queue
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|getOutterInstance
argument_list|()
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|queue
return|;
block|}
block|}
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
name|configuration
operator|.
name|getConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|clientId
init|=
name|configuration
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
literal|".Requestor"
expr_stmt|;
name|answer
operator|.
name|setClientId
argument_list|(
name|clientId
argument_list|)
expr_stmt|;
block|}
name|TaskExecutor
name|taskExecutor
init|=
name|configuration
operator|.
name|getTaskExecutor
argument_list|()
decl_stmt|;
if|if
condition|(
name|taskExecutor
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setTaskExecutor
argument_list|(
name|taskExecutor
argument_list|)
expr_stmt|;
block|}
name|ExceptionListener
name|exceptionListener
init|=
name|configuration
operator|.
name|getExceptionListener
argument_list|()
decl_stmt|;
if|if
condition|(
name|exceptionListener
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setExceptionListener
argument_list|(
name|exceptionListener
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getUuidGenerator ()
specifier|public
specifier|static
specifier|synchronized
name|UuidGenerator
name|getUuidGenerator
parameter_list|()
block|{
if|if
condition|(
name|uuidGenerator
operator|==
literal|null
condition|)
block|{
name|uuidGenerator
operator|=
name|UuidGenerator
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
return|return
name|uuidGenerator
return|;
block|}
DECL|method|getConfiguration ()
specifier|protected
name|JmsConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setReplyToSelectorHeader (org.apache.camel.Message in, Message jmsIn)
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
name|in
parameter_list|,
name|Message
name|jmsIn
parameter_list|)
throws|throws
name|JMSException
block|{
comment|// complete
block|}
block|}
end_class

end_unit

