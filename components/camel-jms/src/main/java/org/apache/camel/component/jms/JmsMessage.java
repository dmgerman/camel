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
name|io
operator|.
name|File
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
name|impl
operator|.
name|DefaultMessage
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
name|ExchangeHelper
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

begin_comment
comment|/**  * Represents a {@link org.apache.camel.Message} for working with JMS  *  * @version   */
end_comment

begin_class
DECL|class|JmsMessage
specifier|public
class|class
name|JmsMessage
extends|extends
name|DefaultMessage
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
name|JmsMessage
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|jmsMessage
specifier|private
name|Message
name|jmsMessage
decl_stmt|;
DECL|field|binding
specifier|private
name|JmsBinding
name|binding
decl_stmt|;
DECL|method|JmsMessage (Message jmsMessage, JmsBinding binding)
specifier|public
name|JmsMessage
parameter_list|(
name|Message
name|jmsMessage
parameter_list|,
name|JmsBinding
name|binding
parameter_list|)
block|{
name|setJmsMessage
argument_list|(
name|jmsMessage
argument_list|)
expr_stmt|;
name|setBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
comment|// do not print jmsMessage as there could be sensitive details
if|if
condition|(
name|jmsMessage
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
literal|"JmsMessage[JmsMessageID: "
operator|+
name|jmsMessage
operator|.
name|getJMSMessageID
argument_list|()
operator|+
literal|"]"
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
literal|"JmsMessage@"
operator|+
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|copyFrom (org.apache.camel.Message that)
specifier|public
name|void
name|copyFrom
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|that
parameter_list|)
block|{
if|if
condition|(
name|that
operator|==
name|this
condition|)
block|{
comment|// the same instance so do not need to copy
return|return;
block|}
comment|// must initialize headers before we set the JmsMessage to avoid Camel
comment|// populating it before we do the copy
name|getHeaders
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|boolean
name|copyMessageId
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|that
operator|instanceof
name|JmsMessage
condition|)
block|{
name|JmsMessage
name|thatMessage
init|=
operator|(
name|JmsMessage
operator|)
name|that
decl_stmt|;
name|this
operator|.
name|jmsMessage
operator|=
name|thatMessage
operator|.
name|jmsMessage
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|jmsMessage
operator|!=
literal|null
condition|)
block|{
comment|// for performance lets not copy the messageID if we are a JMS message
name|copyMessageId
operator|=
literal|false
expr_stmt|;
block|}
block|}
if|if
condition|(
name|copyMessageId
condition|)
block|{
name|setMessageId
argument_list|(
name|that
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// copy body and fault flag
name|setBody
argument_list|(
name|that
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|setFault
argument_list|(
name|that
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
comment|// we have already cleared the headers
if|if
condition|(
name|that
operator|.
name|hasHeaders
argument_list|()
condition|)
block|{
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|that
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getAttachments
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|that
operator|.
name|hasAttachments
argument_list|()
condition|)
block|{
name|getAttachments
argument_list|()
operator|.
name|putAll
argument_list|(
name|that
operator|.
name|getAttachments
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the underlying JMS message      */
DECL|method|getJmsMessage ()
specifier|public
name|Message
name|getJmsMessage
parameter_list|()
block|{
return|return
name|jmsMessage
return|;
block|}
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
name|ExchangeHelper
operator|.
name|getBinding
argument_list|(
name|getExchange
argument_list|()
argument_list|,
name|JmsBinding
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
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
DECL|method|setJmsMessage (Message jmsMessage)
specifier|public
name|void
name|setJmsMessage
parameter_list|(
name|Message
name|jmsMessage
parameter_list|)
block|{
if|if
condition|(
name|jmsMessage
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|setMessageId
argument_list|(
name|jmsMessage
operator|.
name|getJMSMessageID
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"Unable to retrieve JMSMessageID from JMS Message"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|jmsMessage
operator|=
name|jmsMessage
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setBody (Object body)
specifier|public
name|void
name|setBody
parameter_list|(
name|Object
name|body
parameter_list|)
block|{
name|super
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
comment|// preserver headers even if we set body to null
name|ensureInitialHeaders
argument_list|()
expr_stmt|;
comment|// remove underlying jmsMessage since we mutated body to null
name|jmsMessage
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|getHeader (String name)
specifier|public
name|Object
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Object
name|answer
init|=
literal|null
decl_stmt|;
comment|// we will exclude using JMS-prefixed headers here to avoid strangeness with some JMS providers
comment|// e.g. ActiveMQ returns the String not the Destination type for "JMSReplyTo"!
comment|// only look in jms message directly if we have not populated headers
if|if
condition|(
name|jmsMessage
operator|!=
literal|null
operator|&&
operator|!
name|hasPopulatedHeaders
argument_list|()
operator|&&
operator|!
name|name
operator|.
name|startsWith
argument_list|(
literal|"JMS"
argument_list|)
condition|)
block|{
try|try
block|{
comment|// use binding to do the lookup as it has to consider using encoded keys
name|answer
operator|=
name|getBinding
argument_list|()
operator|.
name|getObjectProperty
argument_list|(
name|jmsMessage
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeExchangeException
argument_list|(
literal|"Unable to retrieve header from JMS Message: "
operator|+
name|name
argument_list|,
name|getExchange
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|// only look if we have populated headers otherwise there are no headers at all
comment|// if we do lookup a header starting with JMS then force a lookup
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
operator|(
name|hasPopulatedHeaders
argument_list|()
operator|||
name|name
operator|.
name|startsWith
argument_list|(
literal|"JMS"
argument_list|)
operator|)
condition|)
block|{
name|answer
operator|=
name|super
operator|.
name|getHeader
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|getHeaders ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHeaders
parameter_list|()
block|{
name|ensureInitialHeaders
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|getHeaders
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|removeHeader (String name)
specifier|public
name|Object
name|removeHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|ensureInitialHeaders
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|removeHeader
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setHeaders (Map<String, Object> headers)
specifier|public
name|void
name|setHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
block|{
name|ensureInitialHeaders
argument_list|()
expr_stmt|;
name|super
operator|.
name|setHeaders
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setHeader (String name, Object value)
specifier|public
name|void
name|setHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|ensureInitialHeaders
argument_list|()
expr_stmt|;
name|super
operator|.
name|setHeader
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|JmsMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|JmsMessage
argument_list|(
literal|null
argument_list|,
name|binding
argument_list|)
return|;
block|}
comment|/**      * Returns true if a new JMS message instance should be created to send to the next component      */
DECL|method|shouldCreateNewMessage ()
specifier|public
name|boolean
name|shouldCreateNewMessage
parameter_list|()
block|{
return|return
name|super
operator|.
name|hasPopulatedHeaders
argument_list|()
return|;
block|}
comment|/**      * Ensure that the headers have been populated from the underlying JMS message      * before we start mutating the headers      */
DECL|method|ensureInitialHeaders ()
specifier|protected
name|void
name|ensureInitialHeaders
parameter_list|()
block|{
if|if
condition|(
name|jmsMessage
operator|!=
literal|null
operator|&&
operator|!
name|hasPopulatedHeaders
argument_list|()
condition|)
block|{
comment|// we have not populated headers so force this by creating
comment|// new headers and set it on super
name|super
operator|.
name|setHeaders
argument_list|(
name|createHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createBody ()
specifier|protected
name|Object
name|createBody
parameter_list|()
block|{
if|if
condition|(
name|jmsMessage
operator|!=
literal|null
condition|)
block|{
return|return
name|getBinding
argument_list|()
operator|.
name|extractBodyFromJms
argument_list|(
name|getExchange
argument_list|()
argument_list|,
name|jmsMessage
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|populateInitialHeaders (Map<String, Object> map)
specifier|protected
name|void
name|populateInitialHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|jmsMessage
operator|!=
literal|null
operator|&&
name|map
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|putAll
argument_list|(
name|getBinding
argument_list|()
operator|.
name|extractHeadersFromJms
argument_list|(
name|jmsMessage
argument_list|,
name|getExchange
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createMessageId ()
specifier|protected
name|String
name|createMessageId
parameter_list|()
block|{
if|if
condition|(
name|jmsMessage
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"No javax.jms.Message set so generating a new message id"
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|createMessageId
argument_list|()
return|;
block|}
try|try
block|{
name|String
name|id
init|=
name|getDestinationAsString
argument_list|(
name|jmsMessage
operator|.
name|getJMSDestination
argument_list|()
argument_list|)
operator|+
name|jmsMessage
operator|.
name|getJMSMessageID
argument_list|()
decl_stmt|;
return|return
name|getSanitizedString
argument_list|(
name|id
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeExchangeException
argument_list|(
literal|"Unable to retrieve JMSMessageID from JMS Message"
argument_list|,
name|getExchange
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|isTransactedRedelivered ()
specifier|protected
name|Boolean
name|isTransactedRedelivered
parameter_list|()
block|{
if|if
condition|(
name|jmsMessage
operator|!=
literal|null
condition|)
block|{
return|return
name|JmsMessageHelper
operator|.
name|getJMSRedelivered
argument_list|(
name|jmsMessage
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|getDestinationAsString (Destination destination)
specifier|private
name|String
name|getDestinationAsString
parameter_list|(
name|Destination
name|destination
parameter_list|)
throws|throws
name|JMSException
block|{
name|String
name|result
decl_stmt|;
if|if
condition|(
name|destination
operator|==
literal|null
condition|)
block|{
name|result
operator|=
literal|"null destination!"
operator|+
name|File
operator|.
name|separator
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
name|result
operator|=
literal|"topic"
operator|+
name|File
operator|.
name|separator
operator|+
operator|(
operator|(
name|Topic
operator|)
name|destination
operator|)
operator|.
name|getTopicName
argument_list|()
operator|+
name|File
operator|.
name|separator
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
literal|"queue"
operator|+
name|File
operator|.
name|separator
operator|+
operator|(
operator|(
name|Queue
operator|)
name|destination
operator|)
operator|.
name|getQueueName
argument_list|()
operator|+
name|File
operator|.
name|separator
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|getSanitizedString (Object value)
specifier|private
name|String
name|getSanitizedString
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|toString
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"[^a-zA-Z0-9\\.\\_\\-]"
argument_list|,
literal|"_"
argument_list|)
else|:
literal|""
return|;
block|}
block|}
end_class

end_unit

