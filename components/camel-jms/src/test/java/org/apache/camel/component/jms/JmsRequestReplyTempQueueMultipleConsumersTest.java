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
name|concurrent
operator|.
name|Callable
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
name|ConcurrentHashMap
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
name|ExecutorService
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
name|Executors
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|pool
operator|.
name|PooledConnectionFactory
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
name|Processor
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
name|mock
operator|.
name|MockEndpoint
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

begin_comment
comment|/**  * Reliability tests for JMS TempQueue Reply Manager with multiple consumers.  * @version   */
end_comment

begin_class
DECL|class|JmsRequestReplyTempQueueMultipleConsumersTest
specifier|public
class|class
name|JmsRequestReplyTempQueueMultipleConsumersTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|msgsPerThread
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|AtomicInteger
argument_list|>
name|msgsPerThread
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|AtomicInteger
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|connectionFactory
specifier|private
name|PooledConnectionFactory
name|connectionFactory
decl_stmt|;
annotation|@
name|Test
DECL|method|testMultipleConsumingThreads ()
specifier|public
name|void
name|testMultipleConsumingThreads
parameter_list|()
throws|throws
name|Exception
block|{
name|doSendMessages
argument_list|(
literal|1000
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expected multiple consuming threads, but only found: "
operator|+
name|msgsPerThread
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|msgsPerThread
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTempQueueRefreshed ()
specifier|public
name|void
name|testTempQueueRefreshed
parameter_list|()
throws|throws
name|Exception
block|{
name|doSendMessages
argument_list|(
literal|500
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|connectionFactory
operator|.
name|clear
argument_list|()
expr_stmt|;
name|doSendMessages
argument_list|(
literal|100
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|connectionFactory
operator|.
name|clear
argument_list|()
expr_stmt|;
name|doSendMessages
argument_list|(
literal|100
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|connectionFactory
operator|.
name|clear
argument_list|()
expr_stmt|;
name|doSendMessages
argument_list|(
literal|100
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
DECL|method|doSendMessages (int files, int poolSize)
specifier|private
name|void
name|doSendMessages
parameter_list|(
name|int
name|files
parameter_list|,
name|int
name|poolSize
parameter_list|)
throws|throws
name|Exception
block|{
name|resetMocks
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
name|files
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectsNoDuplicates
argument_list|(
name|body
argument_list|()
argument_list|)
expr_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|poolSize
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|files
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|index
init|=
name|i
decl_stmt|;
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|public
name|Object
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|"Message "
operator|+
name|index
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|(
literal|20
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|connectionFactory
operator|=
name|CamelJmsTestHelper
operator|.
name|createPooledConnectionFactory
argument_list|()
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"jms"
argument_list|,
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|camelContext
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"seda:start"
argument_list|)
operator|.
name|inOut
argument_list|(
literal|"jms:queue:foo?concurrentConsumers=10&maxConcurrentConsumers=20&recoveryInterval=10"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|threadName
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|msgsPerThread
init|)
block|{
name|AtomicInteger
name|count
init|=
name|msgsPerThread
operator|.
name|get
argument_list|(
name|threadName
argument_list|)
decl_stmt|;
if|if
condition|(
name|count
operator|==
literal|null
condition|)
block|{
name|count
operator|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|msgsPerThread
operator|.
name|put
argument_list|(
name|threadName
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
name|count
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jms:queue:foo?concurrentConsumers=20&recoveryInterval=10"
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
literal|"Reply>>> ${body}"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

