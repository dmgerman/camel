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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

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
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|StreamMessage
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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|StreamCache
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
name|file
operator|.
name|GenericFile
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
name|DefaultExchangeHolder
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
name|HeaderFilterStrategy
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
name|CamelContextHelper
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
import|import static
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
name|JmsMessageType
operator|.
name|Bytes
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
name|component
operator|.
name|jms
operator|.
name|JmsMessageType
operator|.
name|Map
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
name|component
operator|.
name|jms
operator|.
name|JmsMessageType
operator|.
name|Object
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
name|component
operator|.
name|jms
operator|.
name|JmsMessageType
operator|.
name|Text
import|;
end_import

begin_comment
comment|/**  * A Strategy used to convert between a Camel {@link JmsExchange} and {@link JmsMessage}  * to and from a JMS {@link Message}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JmsBinding
specifier|public
class|class
name|JmsBinding
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
name|JmsBinding
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
DECL|field|headerFilterStrategy
specifier|private
specifier|final
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|field|jmsKeyFormatStrategy
specifier|private
specifier|final
name|JmsKeyFormatStrategy
name|jmsKeyFormatStrategy
decl_stmt|;
DECL|method|JmsBinding ()
specifier|public
name|JmsBinding
parameter_list|()
block|{
name|this
operator|.
name|endpoint
operator|=
literal|null
expr_stmt|;
name|headerFilterStrategy
operator|=
operator|new
name|JmsHeaderFilterStrategy
argument_list|()
expr_stmt|;
name|jmsKeyFormatStrategy
operator|=
operator|new
name|DefaultJmsKeyFormatStrategy
argument_list|()
expr_stmt|;
block|}
DECL|method|JmsBinding (JmsEndpoint endpoint)
specifier|public
name|JmsBinding
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
if|if
condition|(
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|headerFilterStrategy
operator|=
name|endpoint
operator|.
name|getHeaderFilterStrategy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|headerFilterStrategy
operator|=
operator|new
name|JmsHeaderFilterStrategy
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getJmsKeyFormatStrategy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|jmsKeyFormatStrategy
operator|=
name|endpoint
operator|.
name|getJmsKeyFormatStrategy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|jmsKeyFormatStrategy
operator|=
operator|new
name|DefaultJmsKeyFormatStrategy
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Extracts the body from the JMS message      *      * @param exchange the exchange      * @param message  the message to extract its body      * @return the body, can be<tt>null</tt>      */
DECL|method|extractBodyFromJms (Exchange exchange, Message message)
specifier|public
name|Object
name|extractBodyFromJms
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|message
operator|instanceof
name|ObjectMessage
condition|)
block|{
name|ObjectMessage
name|objectMessage
init|=
operator|(
name|ObjectMessage
operator|)
name|message
decl_stmt|;
name|Object
name|payload
init|=
name|objectMessage
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|payload
operator|instanceof
name|DefaultExchangeHolder
condition|)
block|{
name|DefaultExchangeHolder
name|holder
init|=
operator|(
name|DefaultExchangeHolder
operator|)
name|payload
decl_stmt|;
name|DefaultExchangeHolder
operator|.
name|unmarshal
argument_list|(
name|exchange
argument_list|,
name|holder
argument_list|)
expr_stmt|;
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|objectMessage
operator|.
name|getObject
argument_list|()
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|TextMessage
condition|)
block|{
name|TextMessage
name|textMessage
init|=
operator|(
name|TextMessage
operator|)
name|message
decl_stmt|;
return|return
name|textMessage
operator|.
name|getText
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|MapMessage
condition|)
block|{
return|return
name|createMapFromMapMessage
argument_list|(
operator|(
name|MapMessage
operator|)
name|message
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|BytesMessage
condition|)
block|{
return|return
name|createByteArrayFromBytesMessage
argument_list|(
operator|(
name|BytesMessage
operator|)
name|message
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|StreamMessage
condition|)
block|{
return|return
name|message
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
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
name|RuntimeCamelException
argument_list|(
literal|"Failed to extract body due to: "
operator|+
name|e
operator|+
literal|". Message: "
operator|+
name|message
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|extractHeadersFromJms (Message jmsMessage, Exchange exchange)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|extractHeadersFromJms
parameter_list|(
name|Message
name|jmsMessage
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|jmsMessage
operator|!=
literal|null
condition|)
block|{
comment|// lets populate the standard JMS message headers
try|try
block|{
name|map
operator|.
name|put
argument_list|(
literal|"JMSCorrelationID"
argument_list|,
name|jmsMessage
operator|.
name|getJMSCorrelationID
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"JMSDeliveryMode"
argument_list|,
name|jmsMessage
operator|.
name|getJMSDeliveryMode
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"JMSDestination"
argument_list|,
name|jmsMessage
operator|.
name|getJMSDestination
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"JMSExpiration"
argument_list|,
name|jmsMessage
operator|.
name|getJMSExpiration
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"JMSMessageID"
argument_list|,
name|jmsMessage
operator|.
name|getJMSMessageID
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"JMSPriority"
argument_list|,
name|jmsMessage
operator|.
name|getJMSPriority
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"JMSRedelivered"
argument_list|,
name|jmsMessage
operator|.
name|getJMSRedelivered
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"JMSReplyTo"
argument_list|,
name|jmsMessage
operator|.
name|getJMSReplyTo
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"JMSTimestamp"
argument_list|,
name|jmsMessage
operator|.
name|getJMSTimestamp
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"JMSType"
argument_list|,
name|jmsMessage
operator|.
name|getJMSType
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO this works around a bug in the ActiveMQ property handling
name|map
operator|.
name|put
argument_list|(
literal|"JMSXGroupID"
argument_list|,
name|jmsMessage
operator|.
name|getStringProperty
argument_list|(
literal|"JMSXGroupID"
argument_list|)
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
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|Enumeration
name|names
decl_stmt|;
try|try
block|{
name|names
operator|=
name|jmsMessage
operator|.
name|getPropertyNames
argument_list|()
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
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
while|while
condition|(
name|names
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|names
operator|.
name|nextElement
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
try|try
block|{
name|Object
name|value
init|=
name|jmsMessage
operator|.
name|getObjectProperty
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
operator|&&
name|headerFilterStrategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
continue|continue;
block|}
comment|// must decode back from safe JMS header name to original header name
comment|// when storing on this Camel JmsMessage object.
name|String
name|key
init|=
name|jmsKeyFormatStrategy
operator|.
name|decodeKey
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
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
name|RuntimeCamelException
argument_list|(
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|map
return|;
block|}
DECL|method|createByteArrayFromBytesMessage (BytesMessage message)
specifier|protected
name|byte
index|[]
name|createByteArrayFromBytesMessage
parameter_list|(
name|BytesMessage
name|message
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|message
operator|.
name|getBodyLength
argument_list|()
operator|>
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Length of BytesMessage is too long: "
operator|+
name|message
operator|.
name|getBodyLength
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|byte
index|[]
name|result
init|=
operator|new
name|byte
index|[
operator|(
name|int
operator|)
name|message
operator|.
name|getBodyLength
argument_list|()
index|]
decl_stmt|;
name|message
operator|.
name|readBytes
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**      * Creates a JMS message from the Camel exchange and message      *      * @param exchange the current exchange      * @param session the JMS session used to create the message      * @return a newly created JMS Message instance containing the      * @throws JMSException if the message could not be created      */
DECL|method|makeJmsMessage (Exchange exchange, Session session)
specifier|public
name|Message
name|makeJmsMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
name|makeJmsMessage
argument_list|(
name|exchange
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|session
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Creates a JMS message from the Camel exchange and message      *      * @param exchange the current exchange      * @param camelMessage the body to make a javax.jms.Message as      * @param session the JMS session used to create the message      * @param cause optional exception occured that should be sent as reply instead of a regular body      * @return a newly created JMS Message instance containing the      * @throws JMSException if the message could not be created      */
DECL|method|makeJmsMessage (Exchange exchange, org.apache.camel.Message camelMessage, Session session, Exception cause)
specifier|public
name|Message
name|makeJmsMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|,
name|Session
name|session
parameter_list|,
name|Exception
name|cause
parameter_list|)
throws|throws
name|JMSException
block|{
name|Message
name|answer
init|=
literal|null
decl_stmt|;
name|boolean
name|alwaysCopy
init|=
name|endpoint
operator|!=
literal|null
operator|&&
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isAlwaysCopyMessage
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|alwaysCopy
operator|&&
name|camelMessage
operator|instanceof
name|JmsMessage
condition|)
block|{
name|JmsMessage
name|jmsMessage
init|=
operator|(
name|JmsMessage
operator|)
name|camelMessage
decl_stmt|;
if|if
condition|(
operator|!
name|jmsMessage
operator|.
name|shouldCreateNewMessage
argument_list|()
condition|)
block|{
name|answer
operator|=
name|jmsMessage
operator|.
name|getJmsMessage
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|cause
operator|!=
literal|null
condition|)
block|{
comment|// an exception occured so send it as response
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
literal|"Will create JmsMessage with caused exception: "
operator|+
name|cause
argument_list|)
expr_stmt|;
block|}
comment|// create jms message containg the caused exception
name|answer
operator|=
name|createJmsMessage
argument_list|(
name|cause
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelMessage
argument_list|,
literal|"message"
argument_list|)
expr_stmt|;
comment|// create regular jms message using the camel message body
name|answer
operator|=
name|createJmsMessage
argument_list|(
name|exchange
argument_list|,
name|camelMessage
operator|.
name|getBody
argument_list|()
argument_list|,
name|camelMessage
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|session
argument_list|,
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|appendJmsProperties
argument_list|(
name|answer
argument_list|,
name|exchange
argument_list|,
name|camelMessage
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Appends the JMS headers from the Camel {@link JmsMessage}      */
DECL|method|appendJmsProperties (Message jmsMessage, Exchange exchange)
specifier|public
name|void
name|appendJmsProperties
parameter_list|(
name|Message
name|jmsMessage
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|JMSException
block|{
name|appendJmsProperties
argument_list|(
name|jmsMessage
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Appends the JMS headers from the Camel {@link JmsMessage}      */
DECL|method|appendJmsProperties (Message jmsMessage, Exchange exchange, org.apache.camel.Message in)
specifier|public
name|void
name|appendJmsProperties
parameter_list|(
name|Message
name|jmsMessage
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|in
parameter_list|)
throws|throws
name|JMSException
block|{
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entries
init|=
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|String
name|headerName
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|headerValue
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|appendJmsProperty
argument_list|(
name|jmsMessage
argument_list|,
name|exchange
argument_list|,
name|in
argument_list|,
name|headerName
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|appendJmsProperty (Message jmsMessage, Exchange exchange, org.apache.camel.Message in, String headerName, Object headerValue)
specifier|public
name|void
name|appendJmsProperty
parameter_list|(
name|Message
name|jmsMessage
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|in
parameter_list|,
name|String
name|headerName
parameter_list|,
name|Object
name|headerValue
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|headerName
operator|.
name|startsWith
argument_list|(
literal|"JMS"
argument_list|)
operator|&&
operator|!
name|headerName
operator|.
name|startsWith
argument_list|(
literal|"JMSX"
argument_list|)
condition|)
block|{
if|if
condition|(
name|headerName
operator|.
name|equals
argument_list|(
literal|"JMSCorrelationID"
argument_list|)
condition|)
block|{
name|jmsMessage
operator|.
name|setJMSCorrelationID
argument_list|(
name|ExchangeHelper
operator|.
name|convertToType
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|headerValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|headerName
operator|.
name|equals
argument_list|(
literal|"JMSReplyTo"
argument_list|)
operator|&&
name|headerValue
operator|!=
literal|null
condition|)
block|{
name|jmsMessage
operator|.
name|setJMSReplyTo
argument_list|(
name|ExchangeHelper
operator|.
name|convertToType
argument_list|(
name|exchange
argument_list|,
name|Destination
operator|.
name|class
argument_list|,
name|headerValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|headerName
operator|.
name|equals
argument_list|(
literal|"JMSType"
argument_list|)
condition|)
block|{
name|jmsMessage
operator|.
name|setJMSType
argument_list|(
name|ExchangeHelper
operator|.
name|convertToType
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|headerValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
comment|// The following properties are set by the MessageProducer:
comment|// JMSDeliveryMode, JMSDestination, JMSExpiration, JMSPriorit
comment|// The following are set on the underlying JMS provider:
comment|// JMSMessageID, JMSTimestamp, JMSRedelivered
comment|// log at trace level to not spam log
name|LOG
operator|.
name|trace
argument_list|(
literal|"Ignoring JMS header: "
operator|+
name|headerName
operator|+
literal|" with value: "
operator|+
name|headerValue
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|shouldOutputHeader
argument_list|(
name|in
argument_list|,
name|headerName
argument_list|,
name|headerValue
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
comment|// only primitive headers and strings is allowed as properties
comment|// see message properties: http://java.sun.com/j2ee/1.4/docs/api/javax/jms/Message.html
name|Object
name|value
init|=
name|getValidJMSHeaderValue
argument_list|(
name|headerName
argument_list|,
name|headerValue
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
comment|// must encode to safe JMS header name before setting property on jmsMessage
name|String
name|key
init|=
name|jmsKeyFormatStrategy
operator|.
name|encodeKey
argument_list|(
name|headerName
argument_list|)
decl_stmt|;
name|jmsMessage
operator|.
name|setObjectProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
comment|// okay the value is not a primitive or string so we cannot sent it over the wire
name|LOG
operator|.
name|debug
argument_list|(
literal|"Ignoring non primitive header: "
operator|+
name|headerName
operator|+
literal|" of class: "
operator|+
name|headerValue
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" with value: "
operator|+
name|headerValue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Strategy to test if the given header is valid according to the JMS spec to be set as a property      * on the JMS message.      *<p/>      * This default implementation will allow:      *<ul>      *<li>any primitives and their counter Objects (Integer, Double etc.)</li>      *<li>String and any other literals, Character, CharSequence</li>      *<li>Boolean</li>      *<li>Number</li>      *<li>java.util.Date</li>      *</ul>      *      * @param headerName   the header name      * @param headerValue  the header value      * @return  the value to use,<tt>null</tt> to ignore this header      */
DECL|method|getValidJMSHeaderValue (String headerName, Object headerValue)
specifier|protected
name|Object
name|getValidJMSHeaderValue
parameter_list|(
name|String
name|headerName
parameter_list|,
name|Object
name|headerValue
parameter_list|)
block|{
if|if
condition|(
name|headerValue
operator|.
name|getClass
argument_list|()
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
return|return
name|headerValue
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|String
condition|)
block|{
return|return
name|headerValue
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|Number
condition|)
block|{
return|return
name|headerValue
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|Character
condition|)
block|{
return|return
name|headerValue
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|CharSequence
condition|)
block|{
return|return
name|headerValue
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|Boolean
condition|)
block|{
return|return
name|headerValue
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|Date
condition|)
block|{
return|return
name|headerValue
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|createJmsMessage (Exception cause, Session session)
specifier|protected
name|Message
name|createJmsMessage
parameter_list|(
name|Exception
name|cause
parameter_list|,
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
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
literal|"Using JmsMessageType: "
operator|+
name|Object
argument_list|)
expr_stmt|;
block|}
return|return
name|session
operator|.
name|createObjectMessage
argument_list|(
name|cause
argument_list|)
return|;
block|}
DECL|method|createJmsMessage (Exchange exchange, Object body, Map<String, Object> headers, Session session, CamelContext context)
specifier|protected
name|Message
name|createJmsMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|Session
name|session
parameter_list|,
name|CamelContext
name|context
parameter_list|)
throws|throws
name|JMSException
block|{
name|JmsMessageType
name|type
init|=
literal|null
decl_stmt|;
comment|// special for transferExchange
if|if
condition|(
name|endpoint
operator|!=
literal|null
operator|&&
name|endpoint
operator|.
name|isTransferExchange
argument_list|()
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
literal|"Option transferExchange=true so we use JmsMessageType: Object"
argument_list|)
expr_stmt|;
block|}
name|Serializable
name|holder
init|=
name|DefaultExchangeHolder
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
name|session
operator|.
name|createObjectMessage
argument_list|(
name|holder
argument_list|)
return|;
block|}
comment|// check if header have a type set, if so we force to use it
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|JmsConstants
operator|.
name|JMS_MESSAGE_TYPE
argument_list|)
condition|)
block|{
name|type
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|JmsMessageType
operator|.
name|class
argument_list|,
name|headers
operator|.
name|get
argument_list|(
name|JmsConstants
operator|.
name|JMS_MESSAGE_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|!=
literal|null
operator|&&
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getJmsMessageType
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// force a specific type from the endpoint configuration
name|type
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getJmsMessageType
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// let body deterime the type
if|if
condition|(
name|body
operator|instanceof
name|Node
operator|||
name|body
operator|instanceof
name|String
condition|)
block|{
name|type
operator|=
name|Text
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|byte
index|[]
operator|||
name|body
operator|instanceof
name|GenericFile
operator|||
name|body
operator|instanceof
name|File
operator|||
name|body
operator|instanceof
name|Reader
operator|||
name|body
operator|instanceof
name|InputStream
operator|||
name|body
operator|instanceof
name|ByteBuffer
operator|||
name|body
operator|instanceof
name|StreamCache
condition|)
block|{
name|type
operator|=
name|Bytes
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|Map
condition|)
block|{
name|type
operator|=
name|Map
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|Serializable
condition|)
block|{
name|type
operator|=
name|Object
expr_stmt|;
block|}
block|}
comment|// create the JmsMessage based on the type
if|if
condition|(
name|type
operator|!=
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
literal|"Using JmsMessageType: "
operator|+
name|type
argument_list|)
expr_stmt|;
block|}
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|Text
case|:
block|{
name|TextMessage
name|message
init|=
name|session
operator|.
name|createTextMessage
argument_list|()
decl_stmt|;
name|String
name|payload
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|message
operator|.
name|setText
argument_list|(
name|payload
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
case|case
name|Bytes
case|:
block|{
name|BytesMessage
name|message
init|=
name|session
operator|.
name|createBytesMessage
argument_list|()
decl_stmt|;
name|byte
index|[]
name|payload
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|exchange
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|message
operator|.
name|writeBytes
argument_list|(
name|payload
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
case|case
name|Map
case|:
block|{
name|MapMessage
name|message
init|=
name|session
operator|.
name|createMapMessage
argument_list|()
decl_stmt|;
name|Map
name|payload
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|populateMapMessage
argument_list|(
name|message
argument_list|,
name|payload
argument_list|,
name|context
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
case|case
name|Object
case|:
name|Serializable
name|payload
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Serializable
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|body
argument_list|)
decl_stmt|;
return|return
name|session
operator|.
name|createObjectMessage
argument_list|(
name|payload
argument_list|)
return|;
default|default:
break|break;
block|}
block|}
comment|// TODO: should we throw an exception instead?
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
literal|"Could not determine specific JmsMessage type to use from body."
operator|+
literal|" Will use generic JmsMessage."
operator|+
operator|(
name|body
operator|!=
literal|null
condition|?
operator|(
literal|" Body class: "
operator|+
name|body
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
operator|)
else|:
literal|""
operator|)
argument_list|)
expr_stmt|;
block|}
comment|// return a default message
return|return
name|session
operator|.
name|createMessage
argument_list|()
return|;
block|}
comment|/**      * Populates a {@link MapMessage} from a {@link Map} instance.      */
DECL|method|populateMapMessage (MapMessage message, Map<?, ?> map, CamelContext context)
specifier|protected
name|void
name|populateMapMessage
parameter_list|(
name|MapMessage
name|message
parameter_list|,
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|,
name|CamelContext
name|context
parameter_list|)
throws|throws
name|JMSException
block|{
for|for
control|(
name|Object
name|key
range|:
name|map
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|keyString
init|=
name|CamelContextHelper
operator|.
name|convertTo
argument_list|(
name|context
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyString
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setObject
argument_list|(
name|keyString
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Extracts a {@link Map} from a {@link MapMessage}      */
DECL|method|createMapFromMapMessage (MapMessage message)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|createMapFromMapMessage
parameter_list|(
name|MapMessage
name|message
parameter_list|)
throws|throws
name|JMSException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Enumeration
name|names
init|=
name|message
operator|.
name|getMapNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|names
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|names
operator|.
name|nextElement
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|message
operator|.
name|getObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Strategy to allow filtering of headers which are put on the JMS message      *<p/>      *<b>Note</b>: Currently only supports sending java identifiers as keys      */
DECL|method|shouldOutputHeader (org.apache.camel.Message camelMessage, String headerName, Object headerValue, Exchange exchange)
specifier|protected
name|boolean
name|shouldOutputHeader
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
name|String
name|headerName
parameter_list|,
name|Object
name|headerValue
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|headerFilterStrategy
operator|==
literal|null
operator|||
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|headerName
argument_list|,
name|headerValue
argument_list|,
name|exchange
argument_list|)
return|;
block|}
block|}
end_class

end_unit

