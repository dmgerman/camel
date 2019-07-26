begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.consumer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|consumer
package|;
end_package

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
name|TreeMap
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
name|ExecutorService
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
name|locks
operator|.
name|ReadWriteLock
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
name|locks
operator|.
name|ReentrantReadWriteLock
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
name|MessageProducer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Queue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Topic
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
name|component
operator|.
name|sjms
operator|.
name|SjmsEndpoint
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
name|sjms
operator|.
name|jms
operator|.
name|JmsConstants
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
name|Synchronization
import|;
end_import

begin_comment
comment|/**  * cache manager to store and purge unused cashed producers or we will have a  * memory leak  */
end_comment

begin_class
DECL|class|InOutMessageHandler
specifier|public
class|class
name|InOutMessageHandler
extends|extends
name|AbstractMessageHandler
block|{
DECL|field|producerCache
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|MessageProducer
argument_list|>
name|producerCache
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|lock
specifier|private
name|ReadWriteLock
name|lock
init|=
operator|new
name|ReentrantReadWriteLock
argument_list|()
decl_stmt|;
DECL|method|InOutMessageHandler (SjmsEndpoint endpoint, ExecutorService executor)
specifier|public
name|InOutMessageHandler
parameter_list|(
name|SjmsEndpoint
name|endpoint
parameter_list|,
name|ExecutorService
name|executor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|executor
argument_list|)
expr_stmt|;
block|}
DECL|method|InOutMessageHandler (SjmsEndpoint endpoint, ExecutorService executor, Synchronization synchronization)
specifier|public
name|InOutMessageHandler
parameter_list|(
name|SjmsEndpoint
name|endpoint
parameter_list|,
name|ExecutorService
name|executor
parameter_list|,
name|Synchronization
name|synchronization
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|executor
argument_list|,
name|synchronization
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handleMessage (final Exchange exchange)
specifier|public
name|void
name|handleMessage
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|MessageProducer
name|messageProducer
init|=
literal|null
decl_stmt|;
name|Object
name|obj
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JmsConstants
operator|.
name|JMS_REPLY_TO
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
name|Destination
name|replyTo
decl_stmt|;
if|if
condition|(
name|isDestination
argument_list|(
name|obj
argument_list|)
condition|)
block|{
name|replyTo
operator|=
operator|(
name|Destination
operator|)
name|obj
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|String
condition|)
block|{
name|replyTo
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getDestinationCreationStrategy
argument_list|()
operator|.
name|createDestination
argument_list|(
name|getSession
argument_list|()
argument_list|,
operator|(
name|String
operator|)
name|obj
argument_list|,
name|isTopic
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"The value of JMSReplyTo must be a valid Destination or String.  Value provided: "
operator|+
name|obj
argument_list|)
throw|;
block|}
name|String
name|destinationName
init|=
name|getDestinationName
argument_list|(
name|replyTo
argument_list|)
decl_stmt|;
try|try
block|{
name|lock
operator|.
name|readLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
if|if
condition|(
name|producerCache
operator|.
name|containsKey
argument_list|(
name|destinationName
argument_list|)
condition|)
block|{
name|messageProducer
operator|=
name|producerCache
operator|.
name|get
argument_list|(
name|destinationName
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|lock
operator|.
name|readLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|messageProducer
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|lock
operator|.
name|writeLock
argument_list|()
operator|.
name|lock
argument_list|()
expr_stmt|;
name|messageProducer
operator|=
name|getSession
argument_list|()
operator|.
name|createProducer
argument_list|(
name|replyTo
argument_list|)
expr_stmt|;
name|producerCache
operator|.
name|put
argument_list|(
name|destinationName
argument_list|,
name|messageProducer
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|writeLock
argument_list|()
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|MessageHandlerAsyncCallback
name|callback
init|=
operator|new
name|MessageHandlerAsyncCallback
argument_list|(
name|exchange
argument_list|,
name|messageProducer
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|isFailed
argument_list|()
condition|)
block|{
return|return;
block|}
else|else
block|{
if|if
condition|(
name|isTransacted
argument_list|()
operator|||
name|isSynchronous
argument_list|()
condition|)
block|{
comment|// must process synchronous if transacted or configured to do so
name|log
operator|.
name|debug
argument_list|(
literal|"Synchronous processing: Message[{}], Destination[{}] "
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// process asynchronous using the async routing engine
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
literal|"Asynchronous processing: Message[{}], Destination[{}] "
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
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
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
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
literal|"SjmsMessageConsumer invoked for Exchange id:{}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|MessageProducer
argument_list|>
name|entry
range|:
name|producerCache
operator|.
name|entrySet
argument_list|()
control|)
block|{
try|try
block|{
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Cached MessageProducer with key: "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" threw an unexpected exception. This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|producerCache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|isDestination (Object object)
specifier|private
name|boolean
name|isDestination
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|object
operator|instanceof
name|Destination
return|;
block|}
DECL|method|getDestinationName (Destination destination)
specifier|private
name|String
name|getDestinationName
parameter_list|(
name|Destination
name|destination
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|destination
operator|instanceof
name|Queue
condition|)
block|{
name|answer
operator|=
operator|(
operator|(
name|Queue
operator|)
name|destination
operator|)
operator|.
name|getQueueName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|destination
operator|instanceof
name|Topic
condition|)
block|{
name|answer
operator|=
operator|(
operator|(
name|Topic
operator|)
name|destination
operator|)
operator|.
name|getTopicName
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|class|MessageHandlerAsyncCallback
specifier|protected
class|class
name|MessageHandlerAsyncCallback
implements|implements
name|AsyncCallback
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|localProducer
specifier|private
specifier|final
name|MessageProducer
name|localProducer
decl_stmt|;
DECL|method|MessageHandlerAsyncCallback (Exchange exchange, MessageProducer localProducer)
specifier|public
name|MessageHandlerAsyncCallback
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|MessageProducer
name|localProducer
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|localProducer
operator|=
name|localProducer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|done (boolean sync)
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{
try|try
block|{
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|msg
init|=
name|exchange
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|Message
name|response
init|=
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|makeJmsMessage
argument_list|(
name|exchange
argument_list|,
name|msg
operator|.
name|getBody
argument_list|()
argument_list|,
name|msg
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|getSession
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|response
operator|.
name|setJMSCorrelationID
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JmsConstants
operator|.
name|JMS_CORRELATION_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|localProducer
operator|.
name|send
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

