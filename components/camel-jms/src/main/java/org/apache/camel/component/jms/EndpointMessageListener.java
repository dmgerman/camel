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
name|AsyncProcessor
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
name|ExchangePattern
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
name|RollbackExchangeException
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
name|util
operator|.
name|AsyncProcessorConverterHelper
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * A JMS {@link MessageListener} which can be used to delegate processing to a  * Camel endpoint.  *  * Note that instance of this object has to be thread safe (reentrant)  *  * @version   */
end_comment

begin_class
DECL|class|EndpointMessageListener
specifier|public
class|class
name|EndpointMessageListener
implements|implements
name|MessageListener
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|EndpointMessageListener
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
DECL|field|processor
specifier|private
specifier|final
name|AsyncProcessor
name|processor
decl_stmt|;
DECL|field|binding
specifier|private
name|JmsBinding
name|binding
decl_stmt|;
DECL|field|eagerLoadingOfProperties
specifier|private
name|boolean
name|eagerLoadingOfProperties
decl_stmt|;
DECL|field|replyToDestination
specifier|private
name|Object
name|replyToDestination
decl_stmt|;
DECL|field|template
specifier|private
name|JmsOperations
name|template
decl_stmt|;
DECL|field|disableReplyTo
specifier|private
name|boolean
name|disableReplyTo
decl_stmt|;
DECL|field|async
specifier|private
name|boolean
name|async
decl_stmt|;
DECL|method|EndpointMessageListener (JmsEndpoint endpoint, Processor processor)
specifier|public
name|EndpointMessageListener
parameter_list|(
name|JmsEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|onMessage (final Message message)
specifier|public
name|void
name|onMessage
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"onMessage START"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} consumer received JMS message: {}"
argument_list|,
name|endpoint
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|boolean
name|sendReply
decl_stmt|;
name|RuntimeCamelException
name|rce
decl_stmt|;
try|try
block|{
name|Object
name|replyDestination
init|=
name|getReplyToDestination
argument_list|(
name|message
argument_list|)
decl_stmt|;
comment|// we can only send back a reply if there was a reply destination configured
comment|// and disableReplyTo hasn't been explicit enabled
name|sendReply
operator|=
name|replyDestination
operator|!=
literal|null
operator|&&
operator|!
name|disableReplyTo
expr_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|message
argument_list|,
name|replyDestination
argument_list|)
decl_stmt|;
if|if
condition|(
name|eagerLoadingOfProperties
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
expr_stmt|;
block|}
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
name|correlationId
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received Message has JMSCorrelationID [{}]"
argument_list|,
name|correlationId
argument_list|)
expr_stmt|;
block|}
comment|// process the exchange either asynchronously or synchronous
name|LOG
operator|.
name|trace
argument_list|(
literal|"onMessage.process START"
argument_list|)
expr_stmt|;
name|AsyncCallback
name|callback
init|=
operator|new
name|EndpointMessageListenerAsyncCallback
argument_list|(
name|message
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|,
name|sendReply
argument_list|,
name|replyDestination
argument_list|)
decl_stmt|;
comment|// async is by default false, which mean we by default will process the exchange synchronously
comment|// to keep backwards compatible, as well ensure this consumer will pickup messages in order
comment|// (eg to not consume the next message before the previous has been fully processed)
comment|// but if end user explicit configure consumerAsync=true, then we can process the message
comment|// asynchronously (unless endpoint has been configured synchronous, or we use transaction)
name|boolean
name|forceSync
init|=
name|endpoint
operator|.
name|isSynchronous
argument_list|()
operator|||
name|endpoint
operator|.
name|isTransacted
argument_list|()
decl_stmt|;
if|if
condition|(
name|forceSync
operator|||
operator|!
name|isAsync
argument_list|()
condition|)
block|{
comment|// must process synchronous if transacted or configured to do so
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
literal|"Processing exchange {} synchronously"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|processor
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
literal|"Processing exchange {} asynchronously"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|boolean
name|sync
init|=
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|sync
condition|)
block|{
comment|// will be done async so return now
return|return;
block|}
block|}
comment|// if we failed processed the exchange from the async callback task, then grab the exception
name|rce
operator|=
name|exchange
operator|.
name|getException
argument_list|(
name|RuntimeCamelException
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|rce
operator|=
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// an exception occurred so rethrow to trigger rollback on JMS listener
comment|// the JMS listener will use the error handler to handle the uncaught exception
if|if
condition|(
name|rce
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"onMessage END throwing exception: {}"
argument_list|,
name|rce
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
comment|// Spring message listener container will handle uncaught exceptions
comment|// being thrown from this onMessage, and will us the ErrorHandler configured
comment|// on the JmsEndpoint to handle the exception
throw|throw
name|rce
throw|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"onMessage END"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Callback task that is performed when the exchange has been processed      */
DECL|class|EndpointMessageListenerAsyncCallback
specifier|private
specifier|final
class|class
name|EndpointMessageListenerAsyncCallback
implements|implements
name|AsyncCallback
block|{
DECL|field|message
specifier|private
specifier|final
name|Message
name|message
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|JmsEndpoint
name|endpoint
decl_stmt|;
DECL|field|sendReply
specifier|private
specifier|final
name|boolean
name|sendReply
decl_stmt|;
DECL|field|replyDestination
specifier|private
specifier|final
name|Object
name|replyDestination
decl_stmt|;
DECL|method|EndpointMessageListenerAsyncCallback (Message message, Exchange exchange, JmsEndpoint endpoint, boolean sendReply, Object replyDestination)
specifier|private
name|EndpointMessageListenerAsyncCallback
parameter_list|(
name|Message
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|JmsEndpoint
name|endpoint
parameter_list|,
name|boolean
name|sendReply
parameter_list|,
name|Object
name|replyDestination
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|sendReply
operator|=
name|sendReply
expr_stmt|;
name|this
operator|.
name|replyDestination
operator|=
name|replyDestination
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|done (boolean doneSync)
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"onMessage.process END"
argument_list|)
expr_stmt|;
comment|// now we evaluate the processing of the exchange and determine if it was a success or failure
comment|// we also grab information from the exchange to be used for sending back a reply (if we are to do so)
comment|// so the following logic seems a bit complicated at first glance
comment|// if we send back a reply it can either be the message body or transferring a caused exception
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|body
init|=
literal|null
decl_stmt|;
name|Exception
name|cause
init|=
literal|null
decl_stmt|;
name|RuntimeCamelException
name|rce
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|isFailed
argument_list|()
operator|||
name|exchange
operator|.
name|isRollbackOnly
argument_list|()
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|isRollbackOnly
argument_list|()
condition|)
block|{
comment|// rollback only so wrap an exception so we can rethrow the exception to cause rollback
name|rce
operator|=
name|wrapRuntimeCamelException
argument_list|(
operator|new
name|RollbackExchangeException
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// an exception occurred while processing
if|if
condition|(
name|endpoint
operator|.
name|isTransferException
argument_list|()
condition|)
block|{
comment|// send the exception as reply, so null body and set the exception as the cause
name|body
operator|=
literal|null
expr_stmt|;
name|cause
operator|=
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// only throw exception if endpoint is not configured to transfer exceptions back to caller
comment|// do not send a reply but wrap and rethrow the exception
name|rce
operator|=
name|wrapRuntimeCamelException
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
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
name|hasOut
argument_list|()
condition|?
name|exchange
operator|.
name|getOut
argument_list|()
else|:
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|.
name|isFault
argument_list|()
condition|)
block|{
comment|// a fault occurred while processing
name|body
operator|=
name|msg
expr_stmt|;
name|cause
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// process OK so get the reply body if we are InOut and has a body
comment|// If the ppl don't want to send the message back, he should use the InOnly
if|if
condition|(
name|sendReply
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
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|body
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|body
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
expr_stmt|;
block|}
name|cause
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|// send back reply if there was no error and we are supposed to send back a reply
if|if
condition|(
name|rce
operator|==
literal|null
operator|&&
name|sendReply
operator|&&
operator|(
name|body
operator|!=
literal|null
operator|||
name|cause
operator|!=
literal|null
operator|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"onMessage.sendReply START"
argument_list|)
expr_stmt|;
if|if
condition|(
name|replyDestination
operator|instanceof
name|Destination
condition|)
block|{
name|sendReply
argument_list|(
operator|(
name|Destination
operator|)
name|replyDestination
argument_list|,
name|message
argument_list|,
name|exchange
argument_list|,
name|body
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sendReply
argument_list|(
operator|(
name|String
operator|)
name|replyDestination
argument_list|,
name|message
argument_list|,
name|exchange
argument_list|,
name|body
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"onMessage.sendReply END"
argument_list|)
expr_stmt|;
block|}
comment|// if an exception occurred
if|if
condition|(
name|rce
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|doneSync
condition|)
block|{
comment|// we were done sync, so put exception on exchange, so we can grab it in the onMessage
comment|// method and rethrow it
name|exchange
operator|.
name|setException
argument_list|(
name|rce
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// we were done async, so use the endpoint error handler
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
name|endpoint
operator|.
name|getErrorHandler
argument_list|()
operator|.
name|handleError
argument_list|(
name|rce
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|createExchange (Message message, Object replyDestination)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Message
name|message
parameter_list|,
name|Object
name|replyDestination
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|JmsBinding
name|binding
init|=
name|getBinding
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BINDING
argument_list|,
name|binding
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|JmsMessage
argument_list|(
name|message
argument_list|,
name|binding
argument_list|)
argument_list|)
expr_stmt|;
comment|// lets set to an InOut if we have some kind of reply-to destination
if|if
condition|(
name|replyDestination
operator|!=
literal|null
operator|&&
operator|!
name|disableReplyTo
condition|)
block|{
comment|// only change pattern if not already out capable
if|if
condition|(
operator|!
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|exchange
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getBinding ()
specifier|public
name|JmsBinding
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
operator|new
name|JmsBinding
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
comment|/**      * Sets the binding used to convert from a Camel message to and from a JMS      * message      *      * @param binding the binding to use      */
DECL|method|setBinding (JmsBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|JmsBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|isEagerLoadingOfProperties ()
specifier|public
name|boolean
name|isEagerLoadingOfProperties
parameter_list|()
block|{
return|return
name|eagerLoadingOfProperties
return|;
block|}
DECL|method|setEagerLoadingOfProperties (boolean eagerLoadingOfProperties)
specifier|public
name|void
name|setEagerLoadingOfProperties
parameter_list|(
name|boolean
name|eagerLoadingOfProperties
parameter_list|)
block|{
name|this
operator|.
name|eagerLoadingOfProperties
operator|=
name|eagerLoadingOfProperties
expr_stmt|;
block|}
DECL|method|getTemplate ()
specifier|public
specifier|synchronized
name|JmsOperations
name|getTemplate
parameter_list|()
block|{
if|if
condition|(
name|template
operator|==
literal|null
condition|)
block|{
name|template
operator|=
name|endpoint
operator|.
name|createInOnlyTemplate
argument_list|()
expr_stmt|;
block|}
return|return
name|template
return|;
block|}
DECL|method|setTemplate (JmsOperations template)
specifier|public
name|void
name|setTemplate
parameter_list|(
name|JmsOperations
name|template
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
DECL|method|isDisableReplyTo ()
specifier|public
name|boolean
name|isDisableReplyTo
parameter_list|()
block|{
return|return
name|disableReplyTo
return|;
block|}
comment|/**      * Allows the reply-to behaviour to be disabled      */
DECL|method|setDisableReplyTo (boolean disableReplyTo)
specifier|public
name|void
name|setDisableReplyTo
parameter_list|(
name|boolean
name|disableReplyTo
parameter_list|)
block|{
name|this
operator|.
name|disableReplyTo
operator|=
name|disableReplyTo
expr_stmt|;
block|}
DECL|method|getReplyToDestination ()
specifier|public
name|Object
name|getReplyToDestination
parameter_list|()
block|{
return|return
name|replyToDestination
return|;
block|}
comment|/**      * Provides an explicit reply to destination which overrides      * any incoming value of {@link Message#getJMSReplyTo()}      *      * @param replyToDestination the destination that should be used to send replies to      * as either a String or {@link javax.jms.Destination} type.      */
DECL|method|setReplyToDestination (Object replyToDestination)
specifier|public
name|void
name|setReplyToDestination
parameter_list|(
name|Object
name|replyToDestination
parameter_list|)
block|{
name|this
operator|.
name|replyToDestination
operator|=
name|replyToDestination
expr_stmt|;
block|}
DECL|method|isAsync ()
specifier|public
name|boolean
name|isAsync
parameter_list|()
block|{
return|return
name|async
return|;
block|}
comment|/**      * Sets whether asynchronous routing is enabled.      *<p/>      * By default this is<tt>false</tt>. If configured as<tt>true</tt> then      * this listener will process the {@link org.apache.camel.Exchange} asynchronous.      */
DECL|method|setAsync (boolean async)
specifier|public
name|void
name|setAsync
parameter_list|(
name|boolean
name|async
parameter_list|)
block|{
name|this
operator|.
name|async
operator|=
name|async
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
comment|/**      * Strategy to determine which correlation id to use among<tt>JMSMessageID</tt> and<tt>JMSCorrelationID</tt>.      *      * @param message the JMS message      * @return the correlation id to use      * @throws JMSException can be thrown      */
DECL|method|determineCorrelationId (final Message message)
specifier|protected
name|String
name|determineCorrelationId
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|)
throws|throws
name|JMSException
block|{
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
DECL|method|sendReply (Destination replyDestination, final Message message, final Exchange exchange, final org.apache.camel.Message out, final Exception cause)
specifier|protected
name|void
name|sendReply
parameter_list|(
name|Destination
name|replyDestination
parameter_list|,
specifier|final
name|Message
name|message
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|out
parameter_list|,
specifier|final
name|Exception
name|cause
parameter_list|)
block|{
if|if
condition|(
name|replyDestination
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot send reply message as there is no replyDestination for: {}"
argument_list|,
name|out
argument_list|)
expr_stmt|;
return|return;
block|}
name|getTemplate
argument_list|()
operator|.
name|send
argument_list|(
name|replyDestination
argument_list|,
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
name|reply
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
name|out
argument_list|,
name|session
argument_list|,
name|cause
argument_list|)
decl_stmt|;
specifier|final
name|String
name|correlationID
init|=
name|determineCorrelationId
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|reply
operator|.
name|setJMSCorrelationID
argument_list|(
name|correlationID
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
literal|"{} sending reply JMS message [correlationId:{}]: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|endpoint
block|,
name|correlationID
block|,
name|reply
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|reply
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|sendReply (String replyDestination, final Message message, final Exchange exchange, final org.apache.camel.Message out, final Exception cause)
specifier|protected
name|void
name|sendReply
parameter_list|(
name|String
name|replyDestination
parameter_list|,
specifier|final
name|Message
name|message
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|out
parameter_list|,
specifier|final
name|Exception
name|cause
parameter_list|)
block|{
if|if
condition|(
name|replyDestination
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot send reply message as there is no replyDestination for: {}"
argument_list|,
name|out
argument_list|)
expr_stmt|;
return|return;
block|}
name|getTemplate
argument_list|()
operator|.
name|send
argument_list|(
name|replyDestination
argument_list|,
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
name|reply
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
name|out
argument_list|,
name|session
argument_list|,
name|cause
argument_list|)
decl_stmt|;
specifier|final
name|String
name|correlationID
init|=
name|determineCorrelationId
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|reply
operator|.
name|setJMSCorrelationID
argument_list|(
name|correlationID
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
literal|"{} sending reply JMS message [correlationId:{}]: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|endpoint
block|,
name|correlationID
block|,
name|reply
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|reply
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|getReplyToDestination (Message message)
specifier|protected
name|Object
name|getReplyToDestination
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|JMSException
block|{
comment|// lets send a response back if we can
name|Object
name|destination
init|=
name|getReplyToDestination
argument_list|()
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
name|JmsMessageHelper
operator|.
name|getJMSReplyTo
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
return|return
name|destination
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"EndpointMessageListener["
operator|+
name|endpoint
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

