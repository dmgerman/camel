begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.activemq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|activemq
package|;
end_package

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
name|QueueSender
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQSession
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
name|Endpoint
import|;
end_import

begin_comment
comment|/**  * A JMS {@link javax.jms.QueueSender} which sends message exchanges to a Camel  * {@link org.apache.camel.Endpoint}  */
end_comment

begin_class
DECL|class|CamelQueueSender
specifier|public
class|class
name|CamelQueueSender
extends|extends
name|CamelMessageProducer
implements|implements
name|QueueSender
block|{
DECL|method|CamelQueueSender (CamelQueue destination, Endpoint endpoint, ActiveMQSession session)
specifier|public
name|CamelQueueSender
parameter_list|(
name|CamelQueue
name|destination
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|ActiveMQSession
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
name|super
argument_list|(
name|destination
argument_list|,
name|endpoint
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets the queue associated with this<CODE>QueueSender</CODE>.      *       * @return this sender's queue      * @throws JMSException if the JMS provider fails to get the queue for this      *<CODE>QueueSender</CODE> due to some internal error.      */
DECL|method|getQueue ()
specifier|public
name|Queue
name|getQueue
parameter_list|()
throws|throws
name|JMSException
block|{
return|return
operator|(
name|Queue
operator|)
name|super
operator|.
name|getDestination
argument_list|()
return|;
block|}
comment|/**      * Sends a message to a queue for an unidentified message producer. Uses the      *<CODE>QueueSender</CODE>'s default delivery mode, priority, and time to      * live.      *<p/>      *<p/>      * Typically, a message producer is assigned a queue at creation time;      * however, the JMS API also supports unidentified message producers, which      * require that the queue be supplied every time a message is sent.      *       * @param queue the queue to send this message to      * @param message the message to send      * @throws JMSException if the JMS provider fails to send the message due to      *             some internal error.      * @see javax.jms.MessageProducer#getDeliveryMode()      * @see javax.jms.MessageProducer#getTimeToLive()      * @see javax.jms.MessageProducer#getPriority()      */
DECL|method|send (Queue queue, Message message)
specifier|public
name|void
name|send
parameter_list|(
name|Queue
name|queue
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|JMSException
block|{
name|super
operator|.
name|send
argument_list|(
name|queue
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sends a message to a queue for an unidentified message producer,      * specifying delivery mode, priority and time to live.      *<p/>      *<p/>      * Typically, a message producer is assigned a queue at creation time;      * however, the JMS API also supports unidentified message producers, which      * require that the queue be supplied every time a message is sent.      *       * @param queue the queue to send this message to      * @param message the message to send      * @param deliveryMode the delivery mode to use      * @param priority the priority for this message      * @param timeToLive the message's lifetime (in milliseconds)      * @throws JMSException if the JMS provider fails to send the message due to      *             some internal error.      */
DECL|method|send (Queue queue, Message message, int deliveryMode, int priority, long timeToLive)
specifier|public
name|void
name|send
parameter_list|(
name|Queue
name|queue
parameter_list|,
name|Message
name|message
parameter_list|,
name|int
name|deliveryMode
parameter_list|,
name|int
name|priority
parameter_list|,
name|long
name|timeToLive
parameter_list|)
throws|throws
name|JMSException
block|{
name|super
operator|.
name|send
argument_list|(
name|queue
argument_list|,
name|message
argument_list|,
name|deliveryMode
argument_list|,
name|priority
argument_list|,
name|timeToLive
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

