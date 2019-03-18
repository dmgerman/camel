begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|Map
operator|.
name|Entry
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
name|support
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
name|JmsConfiguration
operator|.
name|QUEUE_PREFIX
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
name|JmsConfiguration
operator|.
name|TEMP_QUEUE_PREFIX
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
name|JmsConfiguration
operator|.
name|TEMP_TOPIC_PREFIX
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
name|JmsConfiguration
operator|.
name|TOPIC_PREFIX
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
name|StringHelper
operator|.
name|removeStartingCharacters
import|;
end_import

begin_comment
comment|/**  * Utility class for {@link javax.jms.Message}.  */
end_comment

begin_class
DECL|class|JmsMessageHelper
specifier|public
specifier|final
class|class
name|JmsMessageHelper
block|{
DECL|method|JmsMessageHelper ()
specifier|private
name|JmsMessageHelper
parameter_list|()
block|{     }
comment|/**      * Removes the property from the JMS message.      *      * @param jmsMessage the JMS message      * @param name       name of the property to remove      * @return the old value of the property or<tt>null</tt> if not exists      * @throws JMSException can be thrown      */
DECL|method|removeJmsProperty (Message jmsMessage, String name)
specifier|public
specifier|static
name|Object
name|removeJmsProperty
parameter_list|(
name|Message
name|jmsMessage
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|JMSException
block|{
comment|// check if the property exists
if|if
condition|(
operator|!
name|jmsMessage
operator|.
name|propertyExists
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Object
name|answer
init|=
literal|null
decl_stmt|;
comment|// store the properties we want to keep in a temporary map
comment|// as the JMS API is a bit strict as we are not allowed to
comment|// clear a single property, but must clear them all and redo
comment|// the properties
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Enumeration
argument_list|<
name|?
argument_list|>
name|en
init|=
name|jmsMessage
operator|.
name|getPropertyNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|en
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|key
init|=
operator|(
name|String
operator|)
name|en
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|answer
operator|=
name|key
expr_stmt|;
block|}
else|else
block|{
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|getProperty
argument_list|(
name|jmsMessage
argument_list|,
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// redo the properties to keep
name|jmsMessage
operator|.
name|clearProperties
argument_list|()
expr_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|jmsMessage
operator|.
name|setObjectProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Tests whether a given property with the name exists      *      * @param jmsMessage the JMS message      * @param name       name of the property to test if exists      * @return<tt>true</tt> if the property exists,<tt>false</tt> if not.      * @throws JMSException can be thrown      */
DECL|method|hasProperty (Message jmsMessage, String name)
specifier|public
specifier|static
name|boolean
name|hasProperty
parameter_list|(
name|Message
name|jmsMessage
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|JMSException
block|{
name|Enumeration
argument_list|<
name|?
argument_list|>
name|en
init|=
name|jmsMessage
operator|.
name|getPropertyNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|en
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|key
init|=
operator|(
name|String
operator|)
name|en
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Gets a JMS property      *      * @param jmsMessage the JMS message      * @param name       name of the property to get      * @return the property value, or<tt>null</tt> if does not exists      * @throws JMSException can be thrown      */
DECL|method|getProperty (Message jmsMessage, String name)
specifier|public
specifier|static
name|Object
name|getProperty
parameter_list|(
name|Message
name|jmsMessage
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|JMSException
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
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|jmsMessage
operator|.
name|getStringProperty
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
comment|/**      * Gets a JMS property in a safe way      *      * @param jmsMessage the JMS message      * @param name       name of the property to get      * @return the property value, or<tt>null</tt> if does not exists or failure to get the value      */
DECL|method|getSafeLongProperty (Message jmsMessage, String name)
specifier|public
specifier|static
name|Long
name|getSafeLongProperty
parameter_list|(
name|Message
name|jmsMessage
parameter_list|,
name|String
name|name
parameter_list|)
block|{
try|try
block|{
return|return
name|jmsMessage
operator|.
name|getLongProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Is the JMS session from a given vendor      *      * @param session the JMS session      * @param vendor the vendor, such as<tt>ActiveMQ</tt>, or<tt>Artemis</tt>      * @return<tt>true</tt> if from the vendor,<tt>false</tt> if not or not possible to determine      */
DECL|method|isVendor (Session session, String vendor)
specifier|public
specifier|static
name|boolean
name|isVendor
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|vendor
parameter_list|)
block|{
if|if
condition|(
literal|"Artemis"
operator|.
name|equals
argument_list|(
name|vendor
argument_list|)
condition|)
block|{
return|return
name|session
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"org.apache.activemq.artemis"
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"ActiveMQ"
operator|.
name|equals
argument_list|(
name|vendor
argument_list|)
condition|)
block|{
return|return
operator|!
name|isVendor
argument_list|(
name|session
argument_list|,
literal|"Artemis"
argument_list|)
operator|&&
name|session
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"org.apache.activemq"
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Sets the property on the given JMS message.      *      * @param jmsMessage  the JMS message      * @param name        name of the property to set      * @param value       the value      * @throws JMSException can be thrown      */
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
comment|/**      * Sets the correlation id on the JMS message.      *<p/>      * Will ignore exception thrown      *      * @param message  the JMS message      * @param correlationId the correlation id      */
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
comment|// ignore
block|}
block|}
comment|/**      * Whether the destination name has either queue or temp queue prefix.      *      * @param destination the destination      * @return<tt>true</tt> if queue or temp-queue prefix,<tt>false</tt> otherwise      */
DECL|method|isQueuePrefix (String destination)
specifier|public
specifier|static
name|boolean
name|isQueuePrefix
parameter_list|(
name|String
name|destination
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|destination
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|destination
operator|.
name|startsWith
argument_list|(
name|QUEUE_PREFIX
argument_list|)
operator|||
name|destination
operator|.
name|startsWith
argument_list|(
name|TEMP_QUEUE_PREFIX
argument_list|)
return|;
block|}
comment|/**      * Whether the destination name has either topic or temp topic prefix.      *      * @param destination the destination      * @return<tt>true</tt> if topic or temp-topic prefix,<tt>false</tt> otherwise      */
DECL|method|isTopicPrefix (String destination)
specifier|public
specifier|static
name|boolean
name|isTopicPrefix
parameter_list|(
name|String
name|destination
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|destination
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|destination
operator|.
name|startsWith
argument_list|(
name|TOPIC_PREFIX
argument_list|)
operator|||
name|destination
operator|.
name|startsWith
argument_list|(
name|TEMP_TOPIC_PREFIX
argument_list|)
return|;
block|}
comment|/**      * Normalizes the destination name.      *<p/>      * This ensures the destination name is correct, and we do not create queues as<tt>queue://queue:foo</tt>, which      * was intended as<tt>queue://foo</tt>.      *      * @param destination the destination      * @return the normalized destination      */
DECL|method|normalizeDestinationName (String destination)
specifier|public
specifier|static
name|String
name|normalizeDestinationName
parameter_list|(
name|String
name|destination
parameter_list|)
block|{
comment|// do not include prefix which is the current behavior when using this method.
return|return
name|normalizeDestinationName
argument_list|(
name|destination
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Normalizes the destination name.      *<p/>      * This ensures the destination name is correct, and we do not create queues as<tt>queue://queue:foo</tt>, which      * was intended as<tt>queue://foo</tt>.      *      * @param destination the destination      * @param includePrefix whether to include<tt>queue://</tt>, or<tt>topic://</tt> prefix in the normalized destination name      * @return the normalized destination      */
DECL|method|normalizeDestinationName (String destination, boolean includePrefix)
specifier|public
specifier|static
name|String
name|normalizeDestinationName
parameter_list|(
name|String
name|destination
parameter_list|,
name|boolean
name|includePrefix
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|destination
argument_list|)
condition|)
block|{
return|return
name|destination
return|;
block|}
if|if
condition|(
name|destination
operator|.
name|startsWith
argument_list|(
name|QUEUE_PREFIX
argument_list|)
condition|)
block|{
name|String
name|s
init|=
name|removeStartingCharacters
argument_list|(
name|destination
operator|.
name|substring
argument_list|(
name|QUEUE_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|includePrefix
condition|)
block|{
name|s
operator|=
name|QUEUE_PREFIX
operator|+
literal|"//"
operator|+
name|s
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
elseif|else
if|if
condition|(
name|destination
operator|.
name|startsWith
argument_list|(
name|TEMP_QUEUE_PREFIX
argument_list|)
condition|)
block|{
name|String
name|s
init|=
name|removeStartingCharacters
argument_list|(
name|destination
operator|.
name|substring
argument_list|(
name|TEMP_QUEUE_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|includePrefix
condition|)
block|{
name|s
operator|=
name|TEMP_QUEUE_PREFIX
operator|+
literal|"//"
operator|+
name|s
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
elseif|else
if|if
condition|(
name|destination
operator|.
name|startsWith
argument_list|(
name|TOPIC_PREFIX
argument_list|)
condition|)
block|{
name|String
name|s
init|=
name|removeStartingCharacters
argument_list|(
name|destination
operator|.
name|substring
argument_list|(
name|TOPIC_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|includePrefix
condition|)
block|{
name|s
operator|=
name|TOPIC_PREFIX
operator|+
literal|"//"
operator|+
name|s
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
elseif|else
if|if
condition|(
name|destination
operator|.
name|startsWith
argument_list|(
name|TEMP_TOPIC_PREFIX
argument_list|)
condition|)
block|{
name|String
name|s
init|=
name|removeStartingCharacters
argument_list|(
name|destination
operator|.
name|substring
argument_list|(
name|TEMP_TOPIC_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|includePrefix
condition|)
block|{
name|s
operator|=
name|TEMP_TOPIC_PREFIX
operator|+
literal|"//"
operator|+
name|s
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
else|else
block|{
return|return
name|destination
return|;
block|}
block|}
comment|/**      * Sets the JMSReplyTo on the message.      *      * @param message  the message      * @param replyTo  the reply to destination      */
DECL|method|setJMSReplyTo (Message message, Destination replyTo)
specifier|public
specifier|static
name|void
name|setJMSReplyTo
parameter_list|(
name|Message
name|message
parameter_list|,
name|Destination
name|replyTo
parameter_list|)
block|{
try|try
block|{
name|message
operator|.
name|setJMSReplyTo
argument_list|(
name|replyTo
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore due OracleAQ does not support accessing JMSReplyTo
block|}
block|}
comment|/**      * Gets the JMSReplyTo from the message.      *      * @param message  the message      * @return the reply to, can be<tt>null</tt>      */
DECL|method|getJMSReplyTo (Message message)
specifier|public
specifier|static
name|Destination
name|getJMSReplyTo
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
return|return
name|message
operator|.
name|getJMSReplyTo
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore due OracleAQ does not support accessing JMSReplyTo
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Gets the JMSType from the message.      *      * @param message  the message      * @return the type, can be<tt>null</tt>      */
DECL|method|getJMSType (Message message)
specifier|public
specifier|static
name|String
name|getJMSType
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
return|return
name|message
operator|.
name|getJMSType
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore due OracleAQ does not support accessing JMSType
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Gets the String Properties from the message.      *      * @param message  the message      * @return the type, can be<tt>null</tt>      */
DECL|method|getStringProperty (Message message, String propertyName)
specifier|public
specifier|static
name|String
name|getStringProperty
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|propertyName
parameter_list|)
block|{
try|try
block|{
return|return
name|message
operator|.
name|getStringProperty
argument_list|(
name|propertyName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore due some broker client does not support accessing StringProperty
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Gets the JMSRedelivered from the message.      *      * @param message  the message      * @return<tt>true</tt> if redelivered,<tt>false</tt> if not,<tt>null</tt> if not able to determine      */
DECL|method|getJMSRedelivered (Message message)
specifier|public
specifier|static
name|Boolean
name|getJMSRedelivered
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
return|return
name|message
operator|.
name|getJMSRedelivered
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore if JMS broker do not support this
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Gets the JMSMessageID from the message.      *      * @param message  the message      * @return the JMSMessageID, or<tt>null</tt> if not able to get      */
DECL|method|getJMSMessageID (Message message)
specifier|public
specifier|static
name|String
name|getJMSMessageID
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
return|return
name|message
operator|.
name|getJMSMessageID
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore if JMS broker do not support this
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Gets the JMSDestination from the message.      *      * @param message  the message      * @return the JMSDestination, or<tt>null</tt> if not able to get      */
DECL|method|getJMSDestination (Message message)
specifier|public
specifier|static
name|Destination
name|getJMSDestination
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
return|return
name|message
operator|.
name|getJMSDestination
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore if JMS broker do not support this
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Sets the JMSDeliveryMode on the message.      *      * @param exchange the exchange      * @param message  the message      * @param deliveryMode  the delivery mode, either as a String or integer      * @throws javax.jms.JMSException is thrown if error setting the delivery mode      */
DECL|method|setJMSDeliveryMode (Exchange exchange, Message message, Object deliveryMode)
specifier|public
specifier|static
name|void
name|setJMSDeliveryMode
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
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
name|ExchangeHelper
operator|.
name|convertToType
argument_list|(
name|exchange
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|deliveryMode
argument_list|)
decl_stmt|;
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
else|else
block|{
comment|// fallback and try to convert to a number
name|Integer
name|value
init|=
name|ExchangeHelper
operator|.
name|convertToType
argument_list|(
name|exchange
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|deliveryMode
argument_list|)
decl_stmt|;
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
name|message
operator|.
name|setIntProperty
argument_list|(
name|JmsConstants
operator|.
name|JMS_DELIVERY_MODE
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Gets the JMSCorrelationIDAsBytes from the message.      *      * @param message the message      * @return the JMSCorrelationIDAsBytes, or<tt>null</tt> if not able to get      */
DECL|method|getJMSCorrelationIDAsBytes (Message message)
specifier|public
specifier|static
name|String
name|getJMSCorrelationIDAsBytes
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|byte
index|[]
name|bytes
init|=
name|message
operator|.
name|getJMSCorrelationIDAsBytes
argument_list|()
decl_stmt|;
name|boolean
name|isNull
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|bytes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|byte
name|b
range|:
name|bytes
control|)
block|{
if|if
condition|(
name|b
operator|!=
literal|0
condition|)
block|{
name|isNull
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
return|return
name|isNull
condition|?
literal|null
else|:
operator|new
name|String
argument_list|(
name|bytes
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore if JMS broker do not support this
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

