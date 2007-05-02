begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|beans
operator|.
name|DesignMode
import|;
end_import

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
name|Enumeration
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
name|TemporaryTopic
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

begin_comment
comment|/**  * Represents a {@link org.apache.camel.Message} for working with JMS  *  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|JmsMessage
specifier|public
class|class
name|JmsMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
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
DECL|method|JmsMessage ()
specifier|public
name|JmsMessage
parameter_list|()
block|{     }
DECL|method|JmsMessage (Message jmsMessage)
specifier|public
name|JmsMessage
parameter_list|(
name|Message
name|jmsMessage
parameter_list|)
block|{
name|setJmsMessage
argument_list|(
name|jmsMessage
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
if|if
condition|(
name|jmsMessage
operator|!=
literal|null
condition|)
block|{
return|return
literal|"JmsMessage: "
operator|+
name|jmsMessage
return|;
block|}
else|else
block|{
return|return
literal|"JmsMessage: "
operator|+
name|getBody
argument_list|()
return|;
block|}
block|}
comment|/**      * Returns the underlying JMS message      *      * @return the underlying JMS message      */
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
DECL|method|setJmsMessage (Message jmsMessage)
specifier|public
name|void
name|setJmsMessage
parameter_list|(
name|Message
name|jmsMessage
parameter_list|)
block|{
name|this
operator|.
name|jmsMessage
operator|=
name|jmsMessage
expr_stmt|;
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
decl_stmt|;
name|id
operator|+=
name|getSanitizedString
argument_list|(
name|jmsMessage
operator|.
name|getJMSMessageID
argument_list|()
argument_list|)
expr_stmt|;
name|setMessageId
argument_list|(
name|id
argument_list|)
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
name|error
argument_list|(
literal|"Failed to get message id from message "
operator|+
name|jmsMessage
argument_list|,
name|e
argument_list|)
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
if|if
condition|(
name|jmsMessage
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|answer
operator|=
name|jmsMessage
operator|.
name|getObjectProperty
argument_list|(
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
name|MessagePropertyAccessException
argument_list|(
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
DECL|method|newInstance ()
specifier|public
name|JmsMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|JmsMessage
argument_list|()
return|;
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
operator|&&
name|getExchange
argument_list|()
operator|instanceof
name|JmsExchange
condition|)
block|{
name|JmsExchange
name|exchange
init|=
operator|(
name|JmsExchange
operator|)
name|getExchange
argument_list|()
decl_stmt|;
return|return
operator|(
name|exchange
operator|.
name|getBinding
argument_list|()
operator|.
name|extractBodyFromJms
argument_list|(
name|exchange
argument_list|,
name|jmsMessage
argument_list|)
operator|)
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
condition|)
block|{
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
name|MessagePropertyNamesAccessException
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
name|map
operator|.
name|put
argument_list|(
name|name
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
name|MessagePropertyAccessException
argument_list|(
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
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
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|destination
operator|instanceof
name|Topic
condition|)
block|{
name|result
operator|+=
literal|"topic"
operator|+
name|File
operator|.
name|separator
operator|+
name|getSanitizedString
argument_list|(
operator|(
operator|(
name|Topic
operator|)
name|destination
operator|)
operator|.
name|getTopicName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|+=
literal|"queue"
operator|+
name|File
operator|.
name|separator
operator|+
name|getSanitizedString
argument_list|(
operator|(
operator|(
name|Queue
operator|)
name|destination
operator|)
operator|.
name|getQueueName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|result
operator|+=
name|File
operator|.
name|separator
expr_stmt|;
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

