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
name|Topic
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TopicSubscriber
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
comment|/**  * A JMS {@link javax.jms.TopicSubscriber} which consumes message exchanges from a  * Camel {@link Endpoint}  *  *   */
end_comment

begin_class
DECL|class|CamelTopicSubscriber
specifier|public
class|class
name|CamelTopicSubscriber
extends|extends
name|CamelMessageConsumer
implements|implements
name|TopicSubscriber
block|{
DECL|method|CamelTopicSubscriber (CamelTopic destination, Endpoint endpoint, ActiveMQSession session, String name, String messageSelector, boolean noLocal)
specifier|public
name|CamelTopicSubscriber
parameter_list|(
name|CamelTopic
name|destination
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|ActiveMQSession
name|session
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|messageSelector
parameter_list|,
name|boolean
name|noLocal
parameter_list|)
block|{
name|super
argument_list|(
name|destination
argument_list|,
name|endpoint
argument_list|,
name|session
argument_list|,
name|messageSelector
argument_list|,
name|noLocal
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets the<CODE>Topic</CODE> associated with this subscriber.      *      * @return this subscriber's<CODE>Topic</CODE>      * @throws javax.jms.JMSException if the JMS provider fails to get the topic for this topic      *                                subscriber due to some internal error.      */
DECL|method|getTopic ()
specifier|public
name|Topic
name|getTopic
parameter_list|()
throws|throws
name|JMSException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
operator|(
name|Topic
operator|)
name|super
operator|.
name|getDestination
argument_list|()
return|;
block|}
comment|/**      * Gets the<CODE>NoLocal</CODE> attribute for this subscriber. The      * default value for this attribute is false.      *      * @return true if locally published messages are being inhibited      * @throws JMSException if the JMS provider fails to get the<CODE>NoLocal      *</CODE> attribute for this topic subscriber due to some      *                      internal error.      */
DECL|method|getNoLocal ()
specifier|public
name|boolean
name|getNoLocal
parameter_list|()
throws|throws
name|JMSException
block|{
name|checkClosed
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|isNoLocal
argument_list|()
return|;
block|}
block|}
end_class

end_unit

