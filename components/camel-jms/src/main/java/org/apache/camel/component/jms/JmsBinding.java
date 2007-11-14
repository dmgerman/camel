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
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|HashSet
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
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

begin_comment
comment|/**  * A Strategy used to convert between a Camel {@JmsExchange} and {@JmsMessage}  * to and from a JMS {@link Message}  *  * @version $Revision$  */
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
DECL|field|ignoreJmsHeaders
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|ignoreJmsHeaders
decl_stmt|;
DECL|field|xmlConverter
specifier|private
name|XmlConverter
name|xmlConverter
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
comment|/**      * Extracts the body from the JMS message      *      * @param exchange      * @param message      */
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
return|return
name|objectMessage
operator|.
name|getObject
argument_list|()
return|;
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
operator|||
name|message
operator|instanceof
name|StreamMessage
condition|)
block|{
comment|// TODO we need a decoder to be able to process the message
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
name|RuntimeJmsException
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
comment|/**      * Creates a JMS message from the Camel exchange and message      *      * @param session the JMS session used to create the message      * @return a newly created JMS Message instance containing the      * @throws JMSException if the message could not be created      */
DECL|method|makeJmsMessage (Exchange exchange, org.apache.camel.Message camelMessage, Session session)
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
parameter_list|)
throws|throws
name|JMSException
block|{
name|Message
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
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
name|answer
operator|=
name|jmsMessage
operator|.
name|getJmsMessage
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|createJmsMessage
argument_list|(
name|camelMessage
operator|.
name|getBody
argument_list|()
argument_list|,
name|session
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
return|return
name|answer
return|;
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
comment|// The following properties are set by the MessageProducer
comment|//   JMSDeliveryMode, JMSDestination, JMSExpiration, JMSPriority,
comment|// The following are set on the underlying JMS provider
comment|//   JMSMessageID, JMSTimestamp, JMSRedelivered
name|LOG
operator|.
name|debug
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
argument_list|)
condition|)
block|{
name|jmsMessage
operator|.
name|setObjectProperty
argument_list|(
name|headerName
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|createJmsMessage (Object body, Session session)
specifier|protected
name|Message
name|createJmsMessage
parameter_list|(
name|Object
name|body
parameter_list|,
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|body
operator|instanceof
name|Node
condition|)
block|{
comment|// lets convert the document to a String format
try|try
block|{
name|body
operator|=
name|xmlConverter
operator|.
name|toString
argument_list|(
operator|(
name|Node
operator|)
name|body
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TransformerException
name|e
parameter_list|)
block|{
name|JMSException
name|jmsException
init|=
operator|new
name|JMSException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|jmsException
operator|.
name|setLinkedException
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|jmsException
throw|;
block|}
block|}
if|if
condition|(
name|body
operator|instanceof
name|String
condition|)
block|{
return|return
name|session
operator|.
name|createTextMessage
argument_list|(
operator|(
name|String
operator|)
name|body
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|Serializable
condition|)
block|{
return|return
name|session
operator|.
name|createObjectMessage
argument_list|(
operator|(
name|Serializable
operator|)
name|body
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|session
operator|.
name|createMessage
argument_list|()
return|;
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
DECL|method|getIgnoreJmsHeaders ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getIgnoreJmsHeaders
parameter_list|()
block|{
if|if
condition|(
name|ignoreJmsHeaders
operator|==
literal|null
condition|)
block|{
name|ignoreJmsHeaders
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|populateIgnoreJmsHeaders
argument_list|(
name|ignoreJmsHeaders
argument_list|)
expr_stmt|;
block|}
return|return
name|ignoreJmsHeaders
return|;
block|}
DECL|method|setIgnoreJmsHeaders (Set<String> ignoreJmsHeaders)
specifier|public
name|void
name|setIgnoreJmsHeaders
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|ignoreJmsHeaders
parameter_list|)
block|{
name|this
operator|.
name|ignoreJmsHeaders
operator|=
name|ignoreJmsHeaders
expr_stmt|;
block|}
comment|/**      * Strategy to allow filtering of headers which are put on the JMS message      */
DECL|method|shouldOutputHeader (org.apache.camel.Message camelMessage, String headerName, Object headerValue)
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
parameter_list|)
block|{
return|return
name|headerValue
operator|!=
literal|null
operator|&&
operator|!
name|getIgnoreJmsHeaders
argument_list|()
operator|.
name|contains
argument_list|(
name|headerName
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isJavaIdentifier
argument_list|(
name|headerName
argument_list|)
return|;
block|}
comment|/**      * Populate any JMS headers that should be excluded from being copied from an input message      * onto an outgoing message      */
DECL|method|populateIgnoreJmsHeaders (Set<String> set)
specifier|protected
name|void
name|populateIgnoreJmsHeaders
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|set
parameter_list|)
block|{
comment|// ignore provider specified JMS extension headers
comment|// see page 39 of JMS 1.1 specification
name|String
index|[]
name|ignore
init|=
block|{
literal|"JMSXUserID"
block|,
literal|"JMSXAppID"
block|,
literal|"JMSXDeliveryCount"
block|,
literal|"JMSXProducerTXID"
block|,
literal|"JMSXConsumerTXID"
block|,
literal|"JMSXRcvTimestamp"
block|,
literal|"JMSXState"
block|}
decl_stmt|;
name|set
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|ignore
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

