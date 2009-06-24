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
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|JmsPerformanceTest
specifier|public
class|class
name|JmsPerformanceTest
extends|extends
name|CamelTestSupport
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
name|JmsPerformanceTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|myBean
specifier|protected
name|MyBean
name|myBean
init|=
operator|new
name|MyBean
argument_list|()
decl_stmt|;
DECL|field|messageCount
specifier|protected
name|int
name|messageCount
init|=
literal|1000
decl_stmt|;
DECL|field|receivedCountDown
specifier|protected
name|CountDownLatch
name|receivedCountDown
init|=
operator|new
name|CountDownLatch
argument_list|(
name|messageCount
argument_list|)
decl_stmt|;
DECL|field|consumerSleep
specifier|protected
name|long
name|consumerSleep
decl_stmt|;
DECL|field|expectedMessageCount
specifier|protected
name|int
name|expectedMessageCount
decl_stmt|;
DECL|field|applicationContext
specifier|protected
name|ClassPathXmlApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|useLocalBroker
specifier|protected
name|boolean
name|useLocalBroker
init|=
literal|true
decl_stmt|;
DECL|field|consumedMessageCount
specifier|private
name|int
name|consumedMessageCount
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
name|setExpectedMessageCount
argument_list|(
name|messageCount
argument_list|)
expr_stmt|;
name|timedSendLoop
argument_list|(
literal|0
argument_list|,
name|messageCount
argument_list|)
expr_stmt|;
name|assertExpectedMessagesReceived
argument_list|()
expr_stmt|;
block|}
DECL|method|sendLoop (int startIndex, int endIndex)
specifier|protected
name|void
name|sendLoop
parameter_list|(
name|int
name|startIndex
parameter_list|,
name|int
name|endIndex
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
name|startIndex
init|;
name|i
operator|<
name|endIndex
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
DECL|method|timedSendLoop (int startIndex, int endIndex)
specifier|protected
name|void
name|timedSendLoop
parameter_list|(
name|int
name|startIndex
parameter_list|,
name|int
name|endIndex
parameter_list|)
block|{
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|(
literal|"Sending"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|startIndex
init|;
name|i
operator|<
name|endIndex
condition|;
name|i
operator|++
control|)
block|{
name|watch
operator|.
name|start
argument_list|()
expr_stmt|;
name|sendMessage
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|watch
operator|.
name|stop
argument_list|()
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
specifier|public
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
name|receivedCountDown
operator|.
name|await
argument_list|(
literal|50000
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Received message count"
argument_list|,
name|expectedMessageCount
argument_list|,
name|consumedMessageCount
argument_list|)
expr_stmt|;
comment|// TODO assert that messages are received in order
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|useLocalBroker
condition|)
block|{
name|applicationContext
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"activemq.xml"
argument_list|)
expr_stmt|;
name|applicationContext
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|applicationContext
operator|!=
literal|null
condition|)
block|{
name|applicationContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
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
name|myBean
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getExpectedMessageCount ()
specifier|public
name|int
name|getExpectedMessageCount
parameter_list|()
block|{
return|return
name|expectedMessageCount
return|;
block|}
DECL|method|setExpectedMessageCount (int expectedMessageCount)
specifier|public
name|void
name|setExpectedMessageCount
parameter_list|(
name|int
name|expectedMessageCount
parameter_list|)
block|{
name|this
operator|.
name|expectedMessageCount
operator|=
name|expectedMessageCount
expr_stmt|;
name|receivedCountDown
operator|=
operator|new
name|CountDownLatch
argument_list|(
name|expectedMessageCount
argument_list|)
expr_stmt|;
block|}
DECL|class|MyBean
specifier|protected
class|class
name|MyBean
block|{
DECL|method|onMessage (String body)
specifier|public
name|void
name|onMessage
parameter_list|(
name|String
name|body
parameter_list|)
block|{
if|if
condition|(
name|consumerSleep
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|consumerSleep
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Caught: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|consumedMessageCount
operator|++
expr_stmt|;
name|receivedCountDown
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

