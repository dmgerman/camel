begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|Header
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
name|builder
operator|.
name|RouteBuilder
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
name|jms
operator|.
name|JmsComponent
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
name|itest
operator|.
name|CamelJmsTestHelper
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
name|junit4
operator|.
name|CamelTestSupport
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
name|jndi
operator|.
name|JndiContext
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
name|JmsComponent
operator|.
name|jmsComponentAutoAcknowledge
import|;
end_import

begin_class
DECL|class|JmsPerformanceTest
specifier|public
class|class
name|JmsPerformanceTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|receivedHeaders
specifier|private
name|List
argument_list|<
name|Integer
argument_list|>
name|receivedHeaders
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getMessageCount
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|receivedMessages
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|receivedMessages
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getMessageCount
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendingAndReceivingMessages ()
specifier|public
name|void
name|testSendingAndReceivingMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Sending {} messages"
argument_list|,
name|getMessageCount
argument_list|()
argument_list|)
expr_stmt|;
name|sendLoop
argument_list|(
name|getMessageCount
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Sending {} messages completed, now will assert on their content as well as the order of their receipt"
argument_list|,
name|getMessageCount
argument_list|()
argument_list|)
expr_stmt|;
comment|// should wait a bit to make sure all messages have been received by the MyBean#onMessage() method
comment|// as this happens asynchronously, that's not inside the 'main' thread
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|assertExpectedMessagesReceived
argument_list|()
expr_stmt|;
block|}
DECL|method|getMessageCount ()
specifier|protected
name|int
name|getMessageCount
parameter_list|()
block|{
return|return
literal|100
return|;
block|}
DECL|method|sendLoop (int messageCount)
specifier|protected
name|void
name|sendLoop
parameter_list|(
name|int
name|messageCount
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|messageCount
condition|;
name|i
operator|++
control|)
block|{
name|sendMessage
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|sendMessage (int messageCount)
specifier|protected
name|void
name|sendMessage
parameter_list|(
name|int
name|messageCount
parameter_list|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"activemq:"
operator|+
name|getQueueName
argument_list|()
argument_list|,
literal|"Hello:"
operator|+
name|messageCount
argument_list|,
literal|"counter"
argument_list|,
name|messageCount
argument_list|)
expr_stmt|;
block|}
DECL|method|getQueueName ()
specifier|protected
name|String
name|getQueueName
parameter_list|()
block|{
return|return
literal|"testSendingAndReceivingMessages"
return|;
block|}
DECL|method|assertExpectedMessagesReceived ()
specifier|protected
name|void
name|assertExpectedMessagesReceived
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// assert on the expected message count
name|assertEquals
argument_list|(
literal|"The expected message count does not match!"
argument_list|,
name|getMessageCount
argument_list|()
argument_list|,
name|receivedMessages
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// assert on the expected message order
name|List
argument_list|<
name|Integer
argument_list|>
name|expectedHeaders
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getMessageCount
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|getMessageCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|expectedHeaders
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Object
argument_list|>
name|expectedMessages
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getMessageCount
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|getMessageCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|expectedMessages
operator|.
name|add
argument_list|(
literal|"Hello:"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"The expected header order does not match!"
argument_list|,
name|expectedHeaders
argument_list|,
name|receivedHeaders
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The expected message order does not match!"
argument_list|,
name|expectedMessages
argument_list|,
name|receivedMessages
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiContext
name|answer
init|=
operator|new
name|JndiContext
argument_list|()
decl_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"myBean"
argument_list|,
operator|new
name|MyBean
argument_list|()
argument_list|)
expr_stmt|;
comment|// add AMQ client and make use of connection pooling we depend on because of the (large) number
comment|// of the JMS messages we do produce
comment|// add ActiveMQ with embedded broker
name|ConnectionFactory
name|connectionFactory
init|=
name|CamelJmsTestHelper
operator|.
name|createConnectionFactory
argument_list|()
decl_stmt|;
name|JmsComponent
name|amq
init|=
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
decl_stmt|;
name|amq
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|answer
operator|.
name|bind
argument_list|(
literal|"activemq"
argument_list|,
name|amq
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"activemq:"
operator|+
name|getQueueName
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:myBean"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyBean
specifier|protected
class|class
name|MyBean
block|{
DECL|method|onMessage (@eaderR) int counter, Object body)
specifier|public
name|void
name|onMessage
parameter_list|(
annotation|@
name|Header
argument_list|(
literal|"counter"
argument_list|)
name|int
name|counter
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
comment|// the invocation of this method happens inside the same thread so no need for a thread-safe list here
name|receivedHeaders
operator|.
name|add
argument_list|(
name|counter
argument_list|)
expr_stmt|;
name|receivedMessages
operator|.
name|add
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

