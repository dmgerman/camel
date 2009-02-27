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
name|RuntimeCamelException
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
comment|/**  * A JMS {@link MessageListener} which can be used to delegate processing to a  * Camel endpoint.  *  * Note that instance of this object has to be thread safe (reentrant)  *  * @version $Revision$  */
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
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|EndpointMessageListener
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|JmsEndpoint
name|endpoint
decl_stmt|;
DECL|field|processor
specifier|private
name|Processor
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
name|Destination
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
name|processor
expr_stmt|;
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|configure
argument_list|(
name|this
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
name|RuntimeCamelException
name|rce
init|=
literal|null
decl_stmt|;
try|try
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
name|endpoint
operator|+
literal|" consumer receiving JMS message: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
name|Destination
name|replyDestination
init|=
name|getReplyToDestination
argument_list|(
name|message
argument_list|)
decl_stmt|;
specifier|final
name|JmsExchange
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
comment|// process the exchange
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// get the correct jms message to send as reply
name|JmsMessage
name|body
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|isFailed
argument_list|()
condition|)
block|{
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
comment|// TODO: Camel-585 somekind of flag to determine if we should send the exchange back to the client
comment|// or do as now where we wrap as runtime exception to be thrown back to spring so it can do rollback
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
elseif|else
if|if
condition|(
name|exchange
operator|.
name|getFault
argument_list|()
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// a fault occurred while processing
name|body
operator|=
name|exchange
operator|.
name|getFault
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// process OK so get the reply
name|body
operator|=
name|exchange
operator|.
name|getOut
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|// send the reply if we got a response and the exchange is out capable
if|if
condition|(
name|rce
operator|==
literal|null
operator|&&
name|body
operator|!=
literal|null
operator|&&
operator|!
name|disableReplyTo
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
name|sendReply
argument_list|(
name|replyDestination
argument_list|,
name|message
argument_list|,
name|exchange
argument_list|,
name|body
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
name|rce
operator|=
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|rce
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|endpoint
operator|+
literal|" consumer caught an exception while processing JMS message: "
operator|+
name|message
argument_list|,
name|rce
argument_list|)
expr_stmt|;
throw|throw
name|rce
throw|;
block|}
block|}
DECL|method|createExchange (Message message, Destination replyDestination)
specifier|public
name|JmsExchange
name|createExchange
parameter_list|(
name|Message
name|message
parameter_list|,
name|Destination
name|replyDestination
parameter_list|)
block|{
name|JmsExchange
name|exchange
init|=
operator|new
name|JmsExchange
argument_list|(
name|endpoint
argument_list|,
name|endpoint
operator|.
name|getExchangePattern
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|,
name|message
argument_list|)
decl_stmt|;
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
name|exchange
operator|.
name|setProperty
argument_list|(
name|JmsConstants
operator|.
name|JMS_REPLY_DESTINATION
argument_list|,
name|replyDestination
argument_list|)
expr_stmt|;
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
name|Destination
name|getReplyToDestination
parameter_list|()
block|{
return|return
name|replyToDestination
return|;
block|}
comment|/**      * Provides an explicit reply to destination which overrides      * any incoming value of {@link Message#getJMSReplyTo()}      *      * @param replyToDestination the destination that should be used to send replies to      */
DECL|method|setReplyToDestination (Destination replyToDestination)
specifier|public
name|void
name|setReplyToDestination
parameter_list|(
name|Destination
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
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|sendReply (Destination replyDestination, final Message message, final JmsExchange exchange, final JmsMessage out)
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
name|JmsExchange
name|exchange
parameter_list|,
specifier|final
name|JmsMessage
name|out
parameter_list|)
block|{
if|if
condition|(
name|replyDestination
operator|==
literal|null
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
literal|"Cannot send reply message as there is no replyDestination for: "
operator|+
name|out
argument_list|)
expr_stmt|;
block|}
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
argument_list|)
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
name|String
name|messageID
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"JMSMessageID"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|reply
operator|.
name|setJMSCorrelationID
argument_list|(
name|messageID
argument_list|)
expr_stmt|;
block|}
else|else
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
name|correlationID
operator|!=
literal|null
condition|)
block|{
name|reply
operator|.
name|setJMSCorrelationID
argument_list|(
name|correlationID
argument_list|)
expr_stmt|;
block|}
block|}
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
literal|" sending reply JMS message: "
operator|+
name|reply
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
name|Destination
name|getReplyToDestination
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|JMSException
block|{
comment|// lets send a response back if we can
name|Destination
name|destination
init|=
name|replyToDestination
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
name|message
operator|.
name|getJMSReplyTo
argument_list|()
expr_stmt|;
block|}
return|return
name|destination
return|;
block|}
block|}
end_class

end_unit

