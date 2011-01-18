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
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|ExchangeTimedOutException
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
name|JmsEndpoint
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
name|JmsMessage
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
name|JmsMessageHelper
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
name|listener
operator|.
name|AbstractMessageListenerContainer
import|;
end_import

begin_comment
comment|/**  * Base class for {@link ReplyManager} implementations.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ReplyManagerSupport
specifier|public
specifier|abstract
class|class
name|ReplyManagerSupport
extends|extends
name|ServiceSupport
implements|implements
name|ReplyManager
block|{
DECL|field|log
specifier|protected
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|executorService
specifier|protected
name|ScheduledExecutorService
name|executorService
decl_stmt|;
DECL|field|endpoint
specifier|protected
name|JmsEndpoint
name|endpoint
decl_stmt|;
DECL|field|replyTo
specifier|protected
name|Destination
name|replyTo
decl_stmt|;
DECL|field|listenerContainer
specifier|protected
name|AbstractMessageListenerContainer
name|listenerContainer
decl_stmt|;
DECL|field|replyToLatch
specifier|protected
specifier|final
name|CountDownLatch
name|replyToLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|replyToTimeout
specifier|protected
specifier|final
name|long
name|replyToTimeout
init|=
literal|10000
decl_stmt|;
DECL|field|correlation
specifier|protected
name|CorrelationMap
name|correlation
decl_stmt|;
DECL|method|setScheduledExecutorService (ScheduledExecutorService executorService)
specifier|public
name|void
name|setScheduledExecutorService
parameter_list|(
name|ScheduledExecutorService
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
block|}
DECL|method|setEndpoint (JmsEndpoint endpoint)
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
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
literal|"ReplyTo destination: "
operator|+
name|replyTo
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|replyTo
operator|=
name|replyTo
expr_stmt|;
comment|// trigger latch as the reply to has been resolved and set
name|replyToLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
DECL|method|getReplyTo ()
specifier|public
name|Destination
name|getReplyTo
parameter_list|()
block|{
if|if
condition|(
name|replyTo
operator|!=
literal|null
condition|)
block|{
return|return
name|replyTo
return|;
block|}
try|try
block|{
comment|// the reply to destination has to be resolved using a DestinationResolver using
comment|// the MessageListenerContainer which occurs asynchronously so we have to wait
comment|// for that to happen before we can retrieve the reply to destination to be used
name|log
operator|.
name|trace
argument_list|(
literal|"Waiting for replyTo to be set"
argument_list|)
expr_stmt|;
name|boolean
name|done
init|=
name|replyToLatch
operator|.
name|await
argument_list|(
name|replyToTimeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|done
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"ReplyTo destination was not set and timeout occurred"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Waiting for replyTo to be set done"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
name|replyTo
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
name|String
name|correlationID
init|=
literal|null
decl_stmt|;
try|try
block|{
name|correlationID
operator|=
name|message
operator|.
name|getJMSCorrelationID
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
if|if
condition|(
name|correlationID
operator|==
literal|null
condition|)
block|{
name|log
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
literal|"Received reply message with correlationID: "
operator|+
name|correlationID
operator|+
literal|" -> "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
comment|// handle the reply message
name|handleReplyMessage
argument_list|(
name|correlationID
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|processReply (ReplyHolder holder)
specifier|public
name|void
name|processReply
parameter_list|(
name|ReplyHolder
name|holder
parameter_list|)
block|{
if|if
condition|(
name|holder
operator|!=
literal|null
operator|&&
name|isRunAllowed
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|holder
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|holder
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|boolean
name|timeout
init|=
name|holder
operator|.
name|isTimeout
argument_list|()
decl_stmt|;
if|if
condition|(
name|timeout
condition|)
block|{
comment|// no response, so lets set a timed out exception
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|ExchangeTimedOutException
argument_list|(
name|exchange
argument_list|,
name|holder
operator|.
name|getRequestTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|JmsMessage
name|response
init|=
operator|new
name|JmsMessage
argument_list|(
name|message
argument_list|,
name|endpoint
operator|.
name|getBinding
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|body
init|=
name|response
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isTransferException
argument_list|()
operator|&&
name|body
operator|instanceof
name|Exception
condition|)
block|{
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
literal|"Reply received. Setting reply as an Exception: "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
comment|// we got an exception back and endpoint was configured to transfer exception
comment|// therefore set response as exception
name|exchange
operator|.
name|setException
argument_list|(
operator|(
name|Exception
operator|)
name|body
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
literal|"Reply received. Setting reply as OUT message: "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
comment|// regular response
name|exchange
operator|.
name|setOut
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
comment|// restore correlation id in case the remote server messed with it
if|if
condition|(
name|holder
operator|.
name|getOriginalCorrelationId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|JmsMessageHelper
operator|.
name|setCorrelationId
argument_list|(
name|message
argument_list|,
name|holder
operator|.
name|getOriginalCorrelationId
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"JMSCorrelationID"
argument_list|,
name|holder
operator|.
name|getOriginalCorrelationId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// notify callback
name|AsyncCallback
name|callback
init|=
name|holder
operator|.
name|getCallback
argument_list|()
decl_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|handleReplyMessage (String correlationID, Message message)
specifier|protected
specifier|abstract
name|void
name|handleReplyMessage
parameter_list|(
name|String
name|correlationID
parameter_list|,
name|Message
name|message
parameter_list|)
function_decl|;
DECL|method|createListenerContainer ()
specifier|protected
specifier|abstract
name|AbstractMessageListenerContainer
name|createListenerContainer
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      *<b>IMPORTANT:</b> This logic is only being used due to high performance in-memory only      * testing using InOut over JMS. Its unlikely to happen in a real life situation with communication      * to a remote broker, which always will be slower to send back reply, before Camel had a chance      * to update it's internal correlation map.      */
DECL|method|waitForProvisionCorrelationToBeUpdated (String correlationID, Message message)
specifier|protected
name|ReplyHandler
name|waitForProvisionCorrelationToBeUpdated
parameter_list|(
name|String
name|correlationID
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
comment|// race condition, when using messageID as correlationID then we store a provisional correlation id
comment|// at first, which gets updated with the JMSMessageID after the message has been sent. And in the unlikely
comment|// event that the reply comes back really really fast, and the correlation map hasn't yet been updated
comment|// from the provisional id to the JMSMessageID. If so we have to wait a bit and lookup again.
if|if
condition|(
name|log
operator|.
name|isWarnEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Early reply received with correlationID ["
operator|+
name|correlationID
operator|+
literal|"] -> "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
name|ReplyHandler
name|answer
init|=
literal|null
decl_stmt|;
comment|// wait up till 5 seconds
name|boolean
name|done
init|=
literal|false
decl_stmt|;
name|int
name|counter
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|!
name|done
operator|&&
name|counter
operator|++
operator|<
literal|50
condition|)
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
literal|"Early reply not found handler at attempt "
operator|+
name|counter
operator|+
literal|". Waiting a bit longer."
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
comment|// try again
name|answer
operator|=
name|correlation
operator|.
name|get
argument_list|(
name|correlationID
argument_list|)
expr_stmt|;
name|done
operator|=
name|answer
operator|!=
literal|null
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
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
literal|"Early reply with correlationID ["
operator|+
name|correlationID
operator|+
literal|"] has been matched after "
operator|+
name|counter
operator|+
literal|" attempts and can be processed using handler: "
operator|+
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|executorService
argument_list|,
literal|"executorService"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|endpoint
argument_list|,
literal|"endpoint"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// purge for timeout every second
name|correlation
operator|=
operator|new
name|CorrelationMap
argument_list|(
name|executorService
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|correlation
argument_list|)
expr_stmt|;
comment|// create JMS listener and start it
name|listenerContainer
operator|=
name|createListenerContainer
argument_list|()
expr_stmt|;
name|listenerContainer
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Starting reply listener container on endpoint: "
operator|+
name|endpoint
argument_list|)
expr_stmt|;
name|listenerContainer
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
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|correlation
argument_list|)
expr_stmt|;
if|if
condition|(
name|listenerContainer
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Stopping reply listener container on endpoint: "
operator|+
name|endpoint
argument_list|)
expr_stmt|;
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
name|listenerContainer
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

