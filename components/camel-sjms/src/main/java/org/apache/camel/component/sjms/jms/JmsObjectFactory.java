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
name|MessageConsumer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageProducer
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * TODO Add Class documentation for JmsObjectFactory  *  */
end_comment

begin_class
DECL|class|JmsObjectFactory
specifier|public
specifier|final
class|class
name|JmsObjectFactory
block|{
DECL|method|JmsObjectFactory ()
specifier|private
name|JmsObjectFactory
parameter_list|()
block|{
comment|//Helper class
block|}
DECL|method|createDestination (Session session, String destinationName, boolean topic)
specifier|public
specifier|static
name|Destination
name|createDestination
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|destinationName
parameter_list|,
name|boolean
name|topic
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|topic
condition|)
block|{
return|return
name|createTopic
argument_list|(
name|session
argument_list|,
name|destinationName
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|createQueue
argument_list|(
name|session
argument_list|,
name|destinationName
argument_list|)
return|;
block|}
block|}
DECL|method|createQueue (Session session, String destinationName)
specifier|public
specifier|static
name|Destination
name|createQueue
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|destinationName
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|session
operator|.
name|createQueue
argument_list|(
name|destinationName
argument_list|)
return|;
block|}
DECL|method|createTemporaryDestination (Session session, boolean topic)
specifier|public
specifier|static
name|Destination
name|createTemporaryDestination
parameter_list|(
name|Session
name|session
parameter_list|,
name|boolean
name|topic
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|topic
condition|)
block|{
return|return
name|session
operator|.
name|createTemporaryTopic
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|session
operator|.
name|createTemporaryQueue
argument_list|()
return|;
block|}
block|}
DECL|method|createTopic (Session session, String destinationName)
specifier|public
specifier|static
name|Destination
name|createTopic
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|destinationName
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|session
operator|.
name|createTopic
argument_list|(
name|destinationName
argument_list|)
return|;
block|}
DECL|method|createQueueConsumer (Session session, String destinationName)
specifier|public
specifier|static
name|MessageConsumer
name|createQueueConsumer
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|destinationName
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createMessageConsumer
argument_list|(
name|session
argument_list|,
name|destinationName
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|createQueueConsumer (Session session, String destinationName, String messageSelector)
specifier|public
specifier|static
name|MessageConsumer
name|createQueueConsumer
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|destinationName
parameter_list|,
name|String
name|messageSelector
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createMessageConsumer
argument_list|(
name|session
argument_list|,
name|destinationName
argument_list|,
name|messageSelector
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|createTopicConsumer (Session session, String destinationName, String messageSelector)
specifier|public
specifier|static
name|MessageConsumer
name|createTopicConsumer
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|destinationName
parameter_list|,
name|String
name|messageSelector
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createMessageConsumer
argument_list|(
name|session
argument_list|,
name|destinationName
argument_list|,
name|messageSelector
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|createTemporaryMessageConsumer ( Session session, String messageSelector, boolean topic, String durableSubscriptionId, boolean noLocal)
specifier|public
specifier|static
name|MessageConsumer
name|createTemporaryMessageConsumer
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|messageSelector
parameter_list|,
name|boolean
name|topic
parameter_list|,
name|String
name|durableSubscriptionId
parameter_list|,
name|boolean
name|noLocal
parameter_list|)
throws|throws
name|Exception
block|{
name|Destination
name|destination
init|=
name|createTemporaryDestination
argument_list|(
name|session
argument_list|,
name|topic
argument_list|)
decl_stmt|;
return|return
name|createMessageConsumer
argument_list|(
name|session
argument_list|,
name|destination
argument_list|,
name|messageSelector
argument_list|,
name|topic
argument_list|,
name|durableSubscriptionId
argument_list|,
name|noLocal
argument_list|)
return|;
block|}
DECL|method|createMessageConsumer ( Session session, String destinationName, String messageSelector, boolean topic, String durableSubscriptionId)
specifier|public
specifier|static
name|MessageConsumer
name|createMessageConsumer
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|destinationName
parameter_list|,
name|String
name|messageSelector
parameter_list|,
name|boolean
name|topic
parameter_list|,
name|String
name|durableSubscriptionId
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noLocal is default false accordingly to JMS spec
return|return
name|createMessageConsumer
argument_list|(
name|session
argument_list|,
name|destinationName
argument_list|,
name|messageSelector
argument_list|,
name|topic
argument_list|,
name|durableSubscriptionId
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|createMessageConsumer ( Session session, String destinationName, String messageSelector, boolean topic, String durableSubscriptionId, boolean noLocal)
specifier|public
specifier|static
name|MessageConsumer
name|createMessageConsumer
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|destinationName
parameter_list|,
name|String
name|messageSelector
parameter_list|,
name|boolean
name|topic
parameter_list|,
name|String
name|durableSubscriptionId
parameter_list|,
name|boolean
name|noLocal
parameter_list|)
throws|throws
name|Exception
block|{
name|Destination
name|destination
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|topic
condition|)
block|{
name|destination
operator|=
name|session
operator|.
name|createTopic
argument_list|(
name|destinationName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|destination
operator|=
name|session
operator|.
name|createQueue
argument_list|(
name|destinationName
argument_list|)
expr_stmt|;
block|}
return|return
name|createMessageConsumer
argument_list|(
name|session
argument_list|,
name|destination
argument_list|,
name|messageSelector
argument_list|,
name|topic
argument_list|,
name|durableSubscriptionId
argument_list|,
name|noLocal
argument_list|)
return|;
block|}
DECL|method|createMessageConsumer ( Session session, Destination destination, String messageSelector, boolean topic, String durableSubscriptionId, boolean noLocal)
specifier|public
specifier|static
name|MessageConsumer
name|createMessageConsumer
parameter_list|(
name|Session
name|session
parameter_list|,
name|Destination
name|destination
parameter_list|,
name|String
name|messageSelector
parameter_list|,
name|boolean
name|topic
parameter_list|,
name|String
name|durableSubscriptionId
parameter_list|,
name|boolean
name|noLocal
parameter_list|)
throws|throws
name|Exception
block|{
name|MessageConsumer
name|messageConsumer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|topic
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|durableSubscriptionId
argument_list|)
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|messageSelector
argument_list|)
condition|)
block|{
name|messageConsumer
operator|=
name|session
operator|.
name|createDurableSubscriber
argument_list|(
operator|(
name|Topic
operator|)
name|destination
argument_list|,
name|durableSubscriptionId
argument_list|,
name|messageSelector
argument_list|,
name|noLocal
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|messageConsumer
operator|=
name|session
operator|.
name|createDurableSubscriber
argument_list|(
operator|(
name|Topic
operator|)
name|destination
argument_list|,
name|durableSubscriptionId
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|messageSelector
argument_list|)
condition|)
block|{
name|messageConsumer
operator|=
name|session
operator|.
name|createConsumer
argument_list|(
name|destination
argument_list|,
name|messageSelector
argument_list|,
name|noLocal
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|messageConsumer
operator|=
name|session
operator|.
name|createConsumer
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|messageSelector
argument_list|)
condition|)
block|{
name|messageConsumer
operator|=
name|session
operator|.
name|createConsumer
argument_list|(
name|destination
argument_list|,
name|messageSelector
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|messageConsumer
operator|=
name|session
operator|.
name|createConsumer
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|messageConsumer
return|;
block|}
DECL|method|createQueueProducer ( Session session, String destinationName)
specifier|public
specifier|static
name|MessageProducer
name|createQueueProducer
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|destinationName
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createMessageProducer
argument_list|(
name|session
argument_list|,
name|destinationName
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
DECL|method|createTopicProducer ( Session session, String destinationName)
specifier|public
specifier|static
name|MessageProducer
name|createTopicProducer
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|destinationName
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createMessageProducer
argument_list|(
name|session
argument_list|,
name|destinationName
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
DECL|method|createMessageProducer ( Session session, String destinationName, boolean topic, boolean persitent, long ttl)
specifier|public
specifier|static
name|MessageProducer
name|createMessageProducer
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|destinationName
parameter_list|,
name|boolean
name|topic
parameter_list|,
name|boolean
name|persitent
parameter_list|,
name|long
name|ttl
parameter_list|)
throws|throws
name|Exception
block|{
name|MessageProducer
name|messageProducer
init|=
literal|null
decl_stmt|;
name|Destination
name|destination
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|topic
condition|)
block|{
if|if
condition|(
name|destinationName
operator|.
name|startsWith
argument_list|(
literal|"topic://"
argument_list|)
condition|)
block|{
name|destinationName
operator|=
name|destinationName
operator|.
name|substring
argument_list|(
literal|"topic://"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|destination
operator|=
name|session
operator|.
name|createTopic
argument_list|(
name|destinationName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|destinationName
operator|.
name|startsWith
argument_list|(
literal|"queue://"
argument_list|)
condition|)
block|{
name|destinationName
operator|=
name|destinationName
operator|.
name|substring
argument_list|(
literal|"queue://"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|destination
operator|=
name|session
operator|.
name|createQueue
argument_list|(
name|destinationName
argument_list|)
expr_stmt|;
block|}
name|messageProducer
operator|=
name|session
operator|.
name|createProducer
argument_list|(
name|destination
argument_list|)
expr_stmt|;
if|if
condition|(
name|persitent
condition|)
block|{
name|messageProducer
operator|.
name|setDeliveryMode
argument_list|(
name|DeliveryMode
operator|.
name|PERSISTENT
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|messageProducer
operator|.
name|setDeliveryMode
argument_list|(
name|DeliveryMode
operator|.
name|NON_PERSISTENT
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ttl
operator|>
literal|0
condition|)
block|{
name|messageProducer
operator|.
name|setTimeToLive
argument_list|(
name|ttl
argument_list|)
expr_stmt|;
block|}
return|return
name|messageProducer
return|;
block|}
block|}
end_class

end_unit

