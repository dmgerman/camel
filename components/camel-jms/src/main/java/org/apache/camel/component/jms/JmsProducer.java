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
name|TimeUnit
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
name|TimeoutException
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
name|RuntimeCamelException
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
name|CamelJmsTeemplate102
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
name|requestor
operator|.
name|DeferredRequestReplyMap
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
name|component
operator|.
name|jms
operator|.
name|requestor
operator|.
name|PersistentReplyToRequestor
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
name|Requestor
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
name|DefaultProducer
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
name|DefaultProducer
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
DECL|field|affinity
specifier|private
name|RequestorAffinity
name|affinity
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|JmsEndpoint
name|endpoint
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
DECL|field|deferredRequestReplyMap
specifier|private
name|DeferredRequestReplyMap
name|deferredRequestReplyMap
decl_stmt|;
DECL|field|requestor
specifier|private
name|Requestor
name|requestor
decl_stmt|;
DECL|field|started
specifier|private
name|AtomicBoolean
name|started
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|enum|RequestorAffinity
specifier|private
enum|enum
name|RequestorAffinity
block|{
DECL|enumConstant|PER_COMPONENT
name|PER_COMPONENT
argument_list|(
literal|0
argument_list|)
block|,
DECL|enumConstant|PER_ENDPOINT
name|PER_ENDPOINT
argument_list|(
literal|1
argument_list|)
block|,
DECL|enumConstant|PER_PRODUCER
name|PER_PRODUCER
argument_list|(
literal|2
argument_list|)
block|;
DECL|field|value
specifier|private
name|int
name|value
decl_stmt|;
DECL|method|RequestorAffinity (int value)
specifier|private
name|RequestorAffinity
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
block|}
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
name|JmsConfiguration
name|c
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|affinity
operator|=
name|RequestorAffinity
operator|.
name|PER_PRODUCER
expr_stmt|;
if|if
condition|(
name|c
operator|.
name|getReplyTo
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|c
operator|.
name|getReplyToTempDestinationAffinity
argument_list|()
operator|.
name|equals
argument_list|(
name|JmsConfiguration
operator|.
name|REPLYTO_TEMP_DEST_AFFINITY_PER_ENDPOINT
argument_list|)
condition|)
block|{
name|affinity
operator|=
name|RequestorAffinity
operator|.
name|PER_ENDPOINT
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|.
name|getReplyToTempDestinationAffinity
argument_list|()
operator|.
name|equals
argument_list|(
name|JmsConfiguration
operator|.
name|REPLYTO_TEMP_DEST_AFFINITY_PER_COMPONENT
argument_list|)
condition|)
block|{
name|affinity
operator|=
name|RequestorAffinity
operator|.
name|PER_COMPONENT
expr_stmt|;
block|}
block|}
block|}
DECL|method|getRequestTimeout ()
specifier|public
name|long
name|getRequestTimeout
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRequestTimeout
argument_list|()
return|;
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
block|}
DECL|method|testAndSetRequestor ()
specifier|protected
name|void
name|testAndSetRequestor
parameter_list|()
throws|throws
name|RuntimeCamelException
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
name|JmsConfiguration
name|c
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|.
name|getReplyTo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|requestor
operator|=
operator|new
name|PersistentReplyToRequestor
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getScheduledExecutorService
argument_list|()
argument_list|)
expr_stmt|;
name|requestor
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|affinity
operator|==
name|RequestorAffinity
operator|.
name|PER_PRODUCER
condition|)
block|{
name|requestor
operator|=
operator|new
name|Requestor
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getScheduledExecutorService
argument_list|()
argument_list|)
expr_stmt|;
name|requestor
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|affinity
operator|==
name|RequestorAffinity
operator|.
name|PER_ENDPOINT
condition|)
block|{
name|requestor
operator|=
name|endpoint
operator|.
name|getRequestor
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|affinity
operator|==
name|RequestorAffinity
operator|.
name|PER_COMPONENT
condition|)
block|{
name|requestor
operator|=
operator|(
operator|(
name|JmsComponent
operator|)
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|)
operator|.
name|getRequestor
argument_list|()
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
name|deferredRequestReplyMap
operator|=
name|requestor
operator|.
name|getDeferredRequestReplyMap
argument_list|(
name|this
argument_list|)
expr_stmt|;
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
DECL|method|testAndUnsetRequestor ()
specifier|protected
name|void
name|testAndUnsetRequestor
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
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
operator|!
name|started
operator|.
name|get
argument_list|()
condition|)
block|{
return|return;
block|}
name|requestor
operator|.
name|removeDeferredRequestReplyMap
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|affinity
operator|==
name|RequestorAffinity
operator|.
name|PER_PRODUCER
condition|)
block|{
name|requestor
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|started
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
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
name|testAndUnsetRequestor
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
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
name|processInOut
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// in only
name|processInOnly
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processInOut (final Exchange exchange)
specifier|protected
name|void
name|processInOut
parameter_list|(
specifier|final
name|Exchange
name|exchange
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
name|endpoint
operator|.
name|getDestinationName
argument_list|()
decl_stmt|;
name|Destination
name|destination
init|=
name|exchange
operator|.
name|getProperty
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
name|testAndSetRequestor
argument_list|()
expr_stmt|;
comment|// note due to JMS transaction semantics we cannot use a single transaction
comment|// for sending the request and receiving the response
specifier|final
name|Destination
name|replyTo
init|=
name|requestor
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
name|String
name|correlationId
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
name|correlationId
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
name|generateId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|ValueHolder
argument_list|<
name|FutureTask
argument_list|>
name|futureHolder
init|=
operator|new
name|ValueHolder
argument_list|<
name|FutureTask
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|DeferredMessageSentCallback
name|callback
init|=
name|msgIdAsCorrId
condition|?
name|deferredRequestReplyMap
operator|.
name|createDeferredMessageSentCallback
argument_list|()
else|:
literal|null
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
name|requestor
operator|.
name|setReplyToSelectorHeader
argument_list|(
name|in
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|FutureTask
name|future
decl_stmt|;
name|future
operator|=
operator|(
operator|!
name|msgIdAsCorrId
operator|)
condition|?
name|requestor
operator|.
name|getReceiveFuture
argument_list|(
name|message
operator|.
name|getJMSCorrelationID
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRequestTimeout
argument_list|()
argument_list|)
else|:
name|requestor
operator|.
name|getReceiveFuture
argument_list|(
name|callback
argument_list|)
expr_stmt|;
name|futureHolder
operator|.
name|set
argument_list|(
name|future
argument_list|)
expr_stmt|;
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
name|endpoint
operator|+
literal|" sending JMS message: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
return|return
name|message
return|;
block|}
block|}
decl_stmt|;
name|CamelJmsTemplate
name|template
init|=
literal|null
decl_stmt|;
name|CamelJmsTeemplate102
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
operator|(
name|CamelJmsTeemplate102
operator|)
name|getInOutTemplate
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|template
operator|=
operator|(
name|CamelJmsTemplate
operator|)
name|getInOutTemplate
argument_list|()
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
literal|"Using JMS API "
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
if|if
condition|(
name|destinationName
operator|!=
literal|null
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
else|else
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
elseif|else
if|if
condition|(
name|destination
operator|!=
literal|null
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
else|else
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
name|setMessageId
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// lets wait and return the response
name|long
name|requestTimeout
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRequestTimeout
argument_list|()
decl_stmt|;
try|try
block|{
name|Message
name|message
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|requestTimeout
operator|<
literal|0
condition|)
block|{
name|message
operator|=
operator|(
name|Message
operator|)
name|futureHolder
operator|.
name|get
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|message
operator|=
operator|(
name|Message
operator|)
name|futureHolder
operator|.
name|get
argument_list|()
operator|.
name|get
argument_list|(
name|requestTimeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
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
literal|"Future interrupted: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
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
literal|"Future timed out: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
comment|// the response can be an exception
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
literal|"Reply recieved. Setting reply as Exception: "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
comment|// we got an exception back and endpoint was configued to transfer exception
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
literal|"Reply recieved. Setting reply as OUT message: "
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
comment|// correlation
if|if
condition|(
name|correlationId
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setJMSCorrelationID
argument_list|(
name|correlationId
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
name|correlationId
argument_list|)
expr_stmt|;
block|}
block|}
else|else
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
name|requestTimeout
argument_list|)
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
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processInOnly (final Exchange exchange)
specifier|protected
name|void
name|processInOnly
parameter_list|(
specifier|final
name|Exchange
name|exchange
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
name|endpoint
operator|.
name|getDestinationName
argument_list|()
decl_stmt|;
name|Destination
name|destination
init|=
name|exchange
operator|.
name|getProperty
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
comment|// we must honor these special flags to preverse QoS
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
literal|" to destination: "
operator|+
name|to
operator|+
literal|" for Exchange: "
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
name|endpoint
operator|+
literal|" sending JMS message: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
return|return
name|message
return|;
block|}
block|}
decl_stmt|;
if|if
condition|(
name|destination
operator|!=
literal|null
condition|)
block|{
name|getInOnlyTemplate
argument_list|()
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
name|destinationName
operator|!=
literal|null
condition|)
block|{
name|getInOnlyTemplate
argument_list|()
operator|.
name|send
argument_list|(
name|destinationName
argument_list|,
name|messageCreator
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Neither destination nor "
operator|+
literal|"destinationName are specified on this endpoint: "
operator|+
name|endpoint
argument_list|)
throw|;
block|}
name|setMessageId
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|uuidGenerator
operator|==
literal|null
condition|)
block|{
name|uuidGenerator
operator|=
operator|new
name|UuidGenerator
argument_list|()
expr_stmt|;
block|}
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
block|}
end_class

end_unit

