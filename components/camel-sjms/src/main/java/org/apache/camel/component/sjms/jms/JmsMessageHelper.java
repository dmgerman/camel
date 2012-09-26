begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.jms
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
name|jms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|BytesMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|DeliveryMode
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
name|MapMessage
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
name|ObjectMessage
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
name|TextMessage
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
name|DefaultJmsKeyFormatStrategy
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
name|IllegalHeaderException
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
name|KeyFormatStrategy
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
comment|/**  * Utility class for {@link javax.jms.Message}.  *   * @author sully6768  */
end_comment

begin_class
DECL|class|JmsMessageHelper
specifier|public
specifier|final
class|class
name|JmsMessageHelper
block|{
comment|/**      * Set by the publishing Client      */
DECL|field|JMS_CORRELATION_ID
specifier|public
specifier|static
specifier|final
name|String
name|JMS_CORRELATION_ID
init|=
literal|"JMSCorrelationID"
decl_stmt|;
comment|/**      * Set on the send or publish event      */
DECL|field|JMS_DELIVERY_MODE
specifier|public
specifier|static
specifier|final
name|String
name|JMS_DELIVERY_MODE
init|=
literal|"JMSDeliveryMode"
decl_stmt|;
comment|/**      * Set on the send or publish event      */
DECL|field|JMS_DESTINATION
specifier|public
specifier|static
specifier|final
name|String
name|JMS_DESTINATION
init|=
literal|"JMSDestination"
decl_stmt|;
comment|/**      * Set on the send or publish event      */
DECL|field|JMS_EXPIRATION
specifier|public
specifier|static
specifier|final
name|String
name|JMS_EXPIRATION
init|=
literal|"JMSExpiration"
decl_stmt|;
comment|/**      * Set on the send or publish event      */
DECL|field|JMS_MESSAGE_ID
specifier|public
specifier|static
specifier|final
name|String
name|JMS_MESSAGE_ID
init|=
literal|"JMSMessageID"
decl_stmt|;
comment|/**      * Set on the send or publish event      */
DECL|field|JMS_PRIORITY
specifier|public
specifier|static
specifier|final
name|String
name|JMS_PRIORITY
init|=
literal|"JMSPriority"
decl_stmt|;
comment|/**      * A redelivery flag set by the JMS provider      */
DECL|field|JMS_REDELIVERED
specifier|public
specifier|static
specifier|final
name|String
name|JMS_REDELIVERED
init|=
literal|"JMSRedelivered"
decl_stmt|;
comment|/**      * The JMS Reply To {@link Destination} set by the publishing Client      */
DECL|field|JMS_REPLY_TO
specifier|public
specifier|static
specifier|final
name|String
name|JMS_REPLY_TO
init|=
literal|"JMSReplyTo"
decl_stmt|;
comment|/**      * Set on the send or publish event      */
DECL|field|JMS_TIMESTAMP
specifier|public
specifier|static
specifier|final
name|String
name|JMS_TIMESTAMP
init|=
literal|"JMSTimestamp"
decl_stmt|;
comment|/**      * Set by the publishing Client      */
DECL|field|JMS_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|JMS_TYPE
init|=
literal|"JMSType"
decl_stmt|;
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JmsMessageHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|JmsMessageHelper ()
specifier|private
name|JmsMessageHelper
parameter_list|()
block|{     }
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createMessage (Session session, Object payload, Map<String, Object> messageHeaders, KeyFormatStrategy keyFormatStrategy)
specifier|public
specifier|static
name|Message
name|createMessage
parameter_list|(
name|Session
name|session
parameter_list|,
name|Object
name|payload
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|messageHeaders
parameter_list|,
name|KeyFormatStrategy
name|keyFormatStrategy
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|answer
init|=
literal|null
decl_stmt|;
name|JmsMessageType
name|messageType
init|=
name|JmsMessageHelper
operator|.
name|discoverPayloadType
argument_list|(
name|payload
argument_list|)
decl_stmt|;
try|try
block|{
switch|switch
condition|(
name|messageType
condition|)
block|{
case|case
name|Bytes
case|:
name|BytesMessage
name|bytesMessage
init|=
name|session
operator|.
name|createBytesMessage
argument_list|()
decl_stmt|;
name|bytesMessage
operator|.
name|writeBytes
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|payload
argument_list|)
expr_stmt|;
name|answer
operator|=
name|bytesMessage
expr_stmt|;
break|break;
case|case
name|Map
case|:
name|MapMessage
name|mapMessage
init|=
name|session
operator|.
name|createMapMessage
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|objMap
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|payload
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|objMap
operator|.
name|keySet
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
name|Object
name|value
init|=
name|objMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|mapMessage
operator|.
name|setObject
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
name|mapMessage
expr_stmt|;
break|break;
case|case
name|Object
case|:
name|ObjectMessage
name|objectMessage
init|=
name|session
operator|.
name|createObjectMessage
argument_list|()
decl_stmt|;
name|objectMessage
operator|.
name|setObject
argument_list|(
operator|(
name|Serializable
operator|)
name|payload
argument_list|)
expr_stmt|;
name|answer
operator|=
name|objectMessage
expr_stmt|;
break|break;
case|case
name|Text
case|:
name|TextMessage
name|textMessage
init|=
name|session
operator|.
name|createTextMessage
argument_list|()
decl_stmt|;
name|textMessage
operator|.
name|setText
argument_list|(
operator|(
name|String
operator|)
name|payload
argument_list|)
expr_stmt|;
name|answer
operator|=
name|textMessage
expr_stmt|;
break|break;
default|default:
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Error creating a message of type: "
operator|+
name|messageType
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
if|if
condition|(
name|messageHeaders
operator|!=
literal|null
operator|&&
operator|!
name|messageHeaders
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|answer
operator|=
name|JmsMessageHelper
operator|.
name|setJmsMessageHeaders
argument_list|(
name|answer
argument_list|,
name|messageHeaders
argument_list|,
name|keyFormatStrategy
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Adds or updates the {@link Message} headers. Header names and values are      * checked for JMS 1.1 compliance.      *       * @param jmsMessage the {@link Message} to add or update the headers on      * @param messageHeaders a {@link Map} of String/Object pairs      * @param keyFormatStrategy the a {@link KeyFormatStrategy} to used to      *            format keys in a JMS 1.1 compliant manner. If null the      *            {@link DefaultJmsKeyFormatStrategy} will be used.      * @return {@link Message}      * @throws Exception a      */
DECL|method|setJmsMessageHeaders (final Message jmsMessage, Map<String, Object> messageHeaders, KeyFormatStrategy keyFormatStrategy)
specifier|public
specifier|static
name|Message
name|setJmsMessageHeaders
parameter_list|(
specifier|final
name|Message
name|jmsMessage
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|messageHeaders
parameter_list|,
name|KeyFormatStrategy
name|keyFormatStrategy
parameter_list|)
throws|throws
name|IllegalHeaderException
block|{
comment|// Support for the null keyFormatStrategy
name|KeyFormatStrategy
name|localKeyFormatStrategy
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|keyFormatStrategy
operator|==
literal|null
condition|)
block|{
name|localKeyFormatStrategy
operator|=
operator|new
name|DefaultJmsKeyFormatStrategy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|localKeyFormatStrategy
operator|=
name|keyFormatStrategy
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|messageHeaders
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|headers
operator|.
name|keySet
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|headerName
range|:
name|keys
control|)
block|{
name|Object
name|headerValue
init|=
name|headers
operator|.
name|get
argument_list|(
name|headerName
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerName
operator|.
name|equalsIgnoreCase
argument_list|(
name|JMS_CORRELATION_ID
argument_list|)
condition|)
block|{
if|if
condition|(
name|headerValue
operator|==
literal|null
condition|)
block|{
comment|// Value can be null but we can't cast a null to a String
comment|// so pass null to the setter
name|setCorrelationId
argument_list|(
name|jmsMessage
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|String
condition|)
block|{
name|setCorrelationId
argument_list|(
name|jmsMessage
argument_list|,
operator|(
name|String
operator|)
name|headerValue
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalHeaderException
argument_list|(
literal|"The "
operator|+
name|JMS_CORRELATION_ID
operator|+
literal|" must either be a String or null.  Found: "
operator|+
name|headerValue
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|headerName
operator|.
name|equalsIgnoreCase
argument_list|(
name|JMS_REPLY_TO
argument_list|)
condition|)
block|{
if|if
condition|(
name|headerValue
operator|instanceof
name|String
condition|)
block|{
comment|// FIXME Setting the reply to appears broken. walk back
comment|// through it. If the value is a String we must normalize it
comment|// first
block|}
else|else
block|{
comment|// TODO write destination converter
comment|// Destination replyTo =
comment|// ExchangeHelper.convertToType(exchange,
comment|// Destination.class,
comment|// headerValue);
comment|// jmsMessage.setJMSReplyTo(replyTo);
block|}
block|}
elseif|else
if|if
condition|(
name|headerName
operator|.
name|equalsIgnoreCase
argument_list|(
name|JMS_TYPE
argument_list|)
condition|)
block|{
if|if
condition|(
name|headerValue
operator|==
literal|null
condition|)
block|{
comment|// Value can be null but we can't cast a null to a String
comment|// so pass null to the setter
name|setMessageType
argument_list|(
name|jmsMessage
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|String
condition|)
block|{
comment|// Not null but is a String
name|setMessageType
argument_list|(
name|jmsMessage
argument_list|,
operator|(
name|String
operator|)
name|headerValue
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalHeaderException
argument_list|(
literal|"The "
operator|+
name|JMS_TYPE
operator|+
literal|" must either be a String or null.  Found: "
operator|+
name|headerValue
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|headerName
operator|.
name|equalsIgnoreCase
argument_list|(
name|JMS_PRIORITY
argument_list|)
condition|)
block|{
if|if
condition|(
name|headerValue
operator|instanceof
name|Integer
condition|)
block|{
try|try
block|{
name|jmsMessage
operator|.
name|setJMSPriority
argument_list|(
operator|(
name|Integer
operator|)
name|headerValue
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
name|IllegalHeaderException
argument_list|(
literal|"Failed to set the "
operator|+
name|JMS_PRIORITY
operator|+
literal|" header. Cause: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalHeaderException
argument_list|(
literal|"The "
operator|+
name|JMS_PRIORITY
operator|+
literal|" must be a Integer.  Type found: "
operator|+
name|headerValue
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|headerName
operator|.
name|equalsIgnoreCase
argument_list|(
name|JMS_DELIVERY_MODE
argument_list|)
condition|)
block|{
try|try
block|{
name|JmsMessageHelper
operator|.
name|setJMSDeliveryMode
argument_list|(
name|jmsMessage
argument_list|,
name|headerValue
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
name|IllegalHeaderException
argument_list|(
literal|"Failed to set the "
operator|+
name|JMS_DELIVERY_MODE
operator|+
literal|" header. Cause: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|headerName
operator|.
name|equalsIgnoreCase
argument_list|(
name|JMS_EXPIRATION
argument_list|)
condition|)
block|{
if|if
condition|(
name|headerValue
operator|instanceof
name|Long
condition|)
block|{
try|try
block|{
name|jmsMessage
operator|.
name|setJMSExpiration
argument_list|(
operator|(
name|Long
operator|)
name|headerValue
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
name|IllegalHeaderException
argument_list|(
literal|"Failed to set the "
operator|+
name|JMS_EXPIRATION
operator|+
literal|" header. Cause: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalHeaderException
argument_list|(
literal|"The "
operator|+
name|JMS_EXPIRATION
operator|+
literal|" must be a Long.  Type found: "
operator|+
name|headerValue
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Ignoring JMS header: {} with value: {}"
argument_list|,
name|headerName
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
if|if
condition|(
name|headerName
operator|.
name|equalsIgnoreCase
argument_list|(
name|JMS_DESTINATION
argument_list|)
operator|||
name|headerName
operator|.
name|equalsIgnoreCase
argument_list|(
name|JMS_MESSAGE_ID
argument_list|)
operator|||
name|headerName
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"JMSTimestamp"
argument_list|)
operator|||
name|headerName
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"JMSRedelivered"
argument_list|)
condition|)
block|{
comment|// The following properties are set by the
comment|// MessageProducer:
comment|// JMSDestination
comment|// The following are set on the underlying JMS provider:
comment|// JMSMessageID, JMSTimestamp, JMSRedelivered
comment|// log at trace level to not spam log
name|LOGGER
operator|.
name|trace
argument_list|(
literal|"Ignoring JMS header: {} with value: {}"
argument_list|,
name|headerName
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
operator|(
name|headerValue
operator|instanceof
name|JmsMessageType
operator|)
condition|)
block|{
name|String
name|encodedName
init|=
name|localKeyFormatStrategy
operator|.
name|encodeKey
argument_list|(
name|headerName
argument_list|)
decl_stmt|;
try|try
block|{
name|JmsMessageHelper
operator|.
name|setProperty
argument_list|(
name|jmsMessage
argument_list|,
name|encodedName
argument_list|,
name|headerValue
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
name|IllegalHeaderException
argument_list|(
literal|"Failed to set the header "
operator|+
name|encodedName
operator|+
literal|" header. Cause: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|// }
block|}
block|}
return|return
name|jmsMessage
return|;
block|}
comment|/**      * Sets the JMSDeliveryMode on the message.      *       * @param exchange the exchange      * @param message the message      * @param deliveryMode the delivery mode, either as a String or integer      * @throws javax.jms.JMSException is thrown if error setting the delivery      *             mode      */
DECL|method|setJMSDeliveryMode (Message message, Object deliveryMode)
specifier|public
specifier|static
name|void
name|setJMSDeliveryMode
parameter_list|(
name|Message
name|message
parameter_list|,
name|Object
name|deliveryMode
parameter_list|)
throws|throws
name|JMSException
block|{
name|Integer
name|mode
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|deliveryMode
operator|instanceof
name|String
condition|)
block|{
name|String
name|s
init|=
operator|(
name|String
operator|)
name|deliveryMode
decl_stmt|;
if|if
condition|(
literal|"PERSISTENT"
operator|.
name|equalsIgnoreCase
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|mode
operator|=
name|DeliveryMode
operator|.
name|PERSISTENT
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"NON_PERSISTENT"
operator|.
name|equalsIgnoreCase
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|mode
operator|=
name|DeliveryMode
operator|.
name|NON_PERSISTENT
expr_stmt|;
block|}
else|else
block|{
comment|// it may be a number in the String so try that
name|Integer
name|value
init|=
literal|null
decl_stmt|;
try|try
block|{
name|value
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
comment|// Do nothing. The error handler below is sufficient
block|}
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|mode
operator|=
name|value
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown delivery mode with value: "
operator|+
name|deliveryMode
argument_list|)
throw|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|deliveryMode
operator|instanceof
name|Integer
condition|)
block|{
comment|// fallback and try to convert to a number
name|mode
operator|=
operator|(
name|Integer
operator|)
name|deliveryMode
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to convert the given delivery mode of type "
operator|+
name|deliveryMode
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" with value: "
operator|+
name|deliveryMode
argument_list|)
throw|;
block|}
if|if
condition|(
name|mode
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setJMSDeliveryMode
argument_list|(
name|mode
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Sets the correlation id on the JMS message.      *<p/>      * Will ignore exception thrown      *       * @param message the JMS message      * @param type the correlation id      */
DECL|method|setMessageType (Message message, String type)
specifier|public
specifier|static
name|void
name|setMessageType
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|type
parameter_list|)
block|{
try|try
block|{
name|message
operator|.
name|setJMSType
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
if|if
condition|(
name|LOGGER
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Error setting the message type: {}"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Sets the correlation id on the JMS message.      *<p/>      * Will ignore exception thrown      *       * @param message the JMS message      * @param correlationId the correlation id      */
DECL|method|setCorrelationId (Message message, String correlationId)
specifier|public
specifier|static
name|void
name|setCorrelationId
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|correlationId
parameter_list|)
block|{
try|try
block|{
name|message
operator|.
name|setJMSCorrelationID
argument_list|(
name|correlationId
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
if|if
condition|(
name|LOGGER
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Error setting the correlationId: {}"
argument_list|,
name|correlationId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Sets the property on the given JMS message.      *       * @param jmsMessage the JMS message      * @param name name of the property to set      * @param value the value      * @throws JMSException can be thrown      */
DECL|method|setProperty (Message jmsMessage, String name, Object value)
specifier|public
specifier|static
name|void
name|setProperty
parameter_list|(
name|Message
name|jmsMessage
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|value
operator|instanceof
name|Byte
condition|)
block|{
name|jmsMessage
operator|.
name|setByteProperty
argument_list|(
name|name
argument_list|,
operator|(
name|Byte
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Boolean
condition|)
block|{
name|jmsMessage
operator|.
name|setBooleanProperty
argument_list|(
name|name
argument_list|,
operator|(
name|Boolean
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Double
condition|)
block|{
name|jmsMessage
operator|.
name|setDoubleProperty
argument_list|(
name|name
argument_list|,
operator|(
name|Double
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Float
condition|)
block|{
name|jmsMessage
operator|.
name|setFloatProperty
argument_list|(
name|name
argument_list|,
operator|(
name|Float
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Integer
condition|)
block|{
name|jmsMessage
operator|.
name|setIntProperty
argument_list|(
name|name
argument_list|,
operator|(
name|Integer
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Long
condition|)
block|{
name|jmsMessage
operator|.
name|setLongProperty
argument_list|(
name|name
argument_list|,
operator|(
name|Long
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Short
condition|)
block|{
name|jmsMessage
operator|.
name|setShortProperty
argument_list|(
name|name
argument_list|,
operator|(
name|Short
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
name|jmsMessage
operator|.
name|setStringProperty
argument_list|(
name|name
argument_list|,
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// fallback to Object
name|jmsMessage
operator|.
name|setObjectProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|discoverPayloadType (Object payload)
specifier|public
specifier|static
name|JmsMessageType
name|discoverPayloadType
parameter_list|(
name|Object
name|payload
parameter_list|)
block|{
name|JmsMessageType
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|payload
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|Byte
index|[]
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|payload
argument_list|)
condition|)
block|{
name|answer
operator|=
name|JmsMessageType
operator|.
name|Bytes
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Collection
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|payload
argument_list|)
condition|)
block|{
name|answer
operator|=
name|JmsMessageType
operator|.
name|Map
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|String
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|payload
argument_list|)
condition|)
block|{
name|answer
operator|=
name|JmsMessageType
operator|.
name|Text
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Serializable
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|payload
argument_list|)
condition|)
block|{
name|answer
operator|=
name|JmsMessageType
operator|.
name|Object
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|JmsMessageType
operator|.
name|Message
expr_stmt|;
block|}
block|}
else|else
block|{
name|answer
operator|=
name|JmsMessageType
operator|.
name|Message
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

