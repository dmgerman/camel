begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Connection
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
name|TextMessage
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
name|ActiveMQConnectionFactory
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
name|broker
operator|.
name|BrokerService
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
name|command
operator|.
name|ActiveMQQueue
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
name|test
operator|.
name|spring
operator|.
name|CamelSpringTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|JmsConsumeSendTransacted
specifier|public
class|class
name|JmsConsumeSendTransacted
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|broker
name|BrokerService
name|broker
decl_stmt|;
DECL|field|messageCount
name|int
name|messageCount
decl_stmt|;
annotation|@
name|Test
DECL|method|testTransactedRoute ()
specifier|public
name|void
name|testTransactedRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|sendJMSMessageToKickOffRoute
argument_list|()
expr_stmt|;
comment|// camel route will use a single transaction for send and and ack
name|consumeMessages
argument_list|()
expr_stmt|;
block|}
DECL|method|consumeMessages ()
specifier|private
name|void
name|consumeMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|ActiveMQConnectionFactory
name|factory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://testTran"
argument_list|)
decl_stmt|;
name|factory
operator|.
name|setWatchTopicAdvisories
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Connection
name|connection
init|=
name|factory
operator|.
name|createConnection
argument_list|()
decl_stmt|;
name|connection
operator|.
name|start
argument_list|()
expr_stmt|;
name|Session
name|session
init|=
name|connection
operator|.
name|createSession
argument_list|(
literal|false
argument_list|,
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
decl_stmt|;
name|MessageConsumer
name|consumer
init|=
name|session
operator|.
name|createConsumer
argument_list|(
operator|new
name|ActiveMQQueue
argument_list|(
literal|"to"
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|messagesToConsume
init|=
name|messageCount
decl_stmt|;
while|while
condition|(
name|messagesToConsume
operator|>
literal|0
condition|)
block|{
name|Message
name|message
init|=
name|consumer
operator|.
name|receive
argument_list|(
literal|5000
argument_list|)
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|messagesToConsume
operator|--
expr_stmt|;
block|}
block|}
block|}
DECL|method|sendJMSMessageToKickOffRoute ()
specifier|private
name|void
name|sendJMSMessageToKickOffRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|ActiveMQConnectionFactory
name|factory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://testTran"
argument_list|)
decl_stmt|;
name|factory
operator|.
name|setWatchTopicAdvisories
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Connection
name|connection
init|=
name|factory
operator|.
name|createConnection
argument_list|()
decl_stmt|;
name|connection
operator|.
name|start
argument_list|()
expr_stmt|;
name|Session
name|session
init|=
name|connection
operator|.
name|createSession
argument_list|(
literal|false
argument_list|,
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
decl_stmt|;
name|MessageProducer
name|producer
init|=
name|session
operator|.
name|createProducer
argument_list|(
operator|new
name|ActiveMQQueue
argument_list|(
literal|"from"
argument_list|)
argument_list|)
decl_stmt|;
name|TextMessage
name|message
init|=
name|session
operator|.
name|createTextMessage
argument_list|(
literal|"Some Text, messageCount:"
operator|+
name|messageCount
operator|++
argument_list|)
decl_stmt|;
name|message
operator|.
name|setIntProperty
argument_list|(
literal|"seq"
argument_list|,
name|messageCount
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|createBroker (boolean deleteAllMessages)
specifier|private
name|BrokerService
name|createBroker
parameter_list|(
name|boolean
name|deleteAllMessages
parameter_list|)
throws|throws
name|Exception
block|{
name|BrokerService
name|brokerService
init|=
operator|new
name|BrokerService
argument_list|()
decl_stmt|;
name|brokerService
operator|.
name|setDeleteAllMessagesOnStartup
argument_list|(
name|deleteAllMessages
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|setBrokerName
argument_list|(
literal|"testTran"
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|setAdvisorySupport
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|setUseJmx
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|setDataDirectory
argument_list|(
literal|"target/data"
argument_list|)
expr_stmt|;
name|brokerService
operator|.
name|addConnector
argument_list|(
literal|"tcp://0.0.0.0:61616"
argument_list|)
expr_stmt|;
return|return
name|brokerService
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
try|try
block|{
name|broker
operator|=
name|createBroker
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|broker
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Failed to start broker"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/activemq/camel/jmsConsumeSendTransacted.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

