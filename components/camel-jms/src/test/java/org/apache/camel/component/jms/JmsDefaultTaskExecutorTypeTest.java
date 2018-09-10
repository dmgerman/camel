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
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
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
name|concurrent
operator|.
name|ThreadHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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
name|junit
operator|.
name|rules
operator|.
name|TestName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
comment|/**  *  */
end_comment

begin_class
DECL|class|JmsDefaultTaskExecutorTypeTest
specifier|public
class|class
name|JmsDefaultTaskExecutorTypeTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JmsDefaultTaskExecutorTypeTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|name
annotation|@
name|Rule
specifier|public
name|TestName
name|name
init|=
operator|new
name|TestName
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testThreadPoolTaskExecutor ()
specifier|public
name|void
name|testThreadPoolTaskExecutor
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"threadPool"
argument_list|)
expr_stmt|;
name|Long
name|beforeThreadCount
init|=
name|currentThreadCount
argument_list|()
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result.threadPool"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|doSendMessages
argument_list|(
literal|"foo.threadPool"
argument_list|,
literal|500
argument_list|,
literal|5
argument_list|,
name|DefaultTaskExecutorType
operator|.
name|ThreadPool
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|doSendMessages
argument_list|(
literal|"foo.threadPool"
argument_list|,
literal|500
argument_list|,
literal|5
argument_list|,
name|DefaultTaskExecutorType
operator|.
name|ThreadPool
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Long
name|numberThreadsCreated
init|=
name|currentThreadCount
argument_list|()
operator|-
name|beforeThreadCount
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Number of threads created, testThreadPoolTaskExecutor: "
operator|+
name|numberThreadsCreated
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Number of threads created should be equal or lower than "
operator|+
literal|"100 with ThreadPoolTaskExecutor"
argument_list|,
name|numberThreadsCreated
operator|<=
literal|100
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSimpleAsyncTaskExecutor ()
specifier|public
name|void
name|testSimpleAsyncTaskExecutor
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"simpleAsync"
argument_list|)
expr_stmt|;
name|Long
name|beforeThreadCount
init|=
name|currentThreadCount
argument_list|()
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result.simpleAsync"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|doSendMessages
argument_list|(
literal|"foo.simpleAsync"
argument_list|,
literal|500
argument_list|,
literal|5
argument_list|,
name|DefaultTaskExecutorType
operator|.
name|SimpleAsync
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|doSendMessages
argument_list|(
literal|"foo.simpleAsync"
argument_list|,
literal|500
argument_list|,
literal|5
argument_list|,
name|DefaultTaskExecutorType
operator|.
name|SimpleAsync
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Long
name|numberThreadsCreated
init|=
name|currentThreadCount
argument_list|()
operator|-
name|beforeThreadCount
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Number of threads created, testSimpleAsyncTaskExecutor: "
operator|+
name|numberThreadsCreated
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Number of threads created should be equal or higher than "
operator|+
literal|"800 with SimpleAsyncTaskExecutor"
argument_list|,
name|numberThreadsCreated
operator|>=
literal|800
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultTaskExecutor ()
specifier|public
name|void
name|testDefaultTaskExecutor
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|Long
name|beforeThreadCount
init|=
name|currentThreadCount
argument_list|()
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result.default"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|doSendMessages
argument_list|(
literal|"foo.default"
argument_list|,
literal|500
argument_list|,
literal|5
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|doSendMessages
argument_list|(
literal|"foo.default"
argument_list|,
literal|500
argument_list|,
literal|5
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Long
name|numberThreadsCreated
init|=
name|currentThreadCount
argument_list|()
operator|-
name|beforeThreadCount
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Number of threads created, testDefaultTaskExecutor: "
operator|+
name|numberThreadsCreated
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Number of threads created should be equal or higher than "
operator|+
literal|"800 with default behaviour"
argument_list|,
name|numberThreadsCreated
operator|>=
literal|800
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultTaskExecutorThreadPoolAtComponentConfig ()
specifier|public
name|void
name|testDefaultTaskExecutorThreadPoolAtComponentConfig
parameter_list|()
throws|throws
name|Exception
block|{
comment|// the default behaviour changes in this test, see createCamelContext method below
comment|// the behaviour is the same as with testThreadPoolTaskExecutor test method above
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|Long
name|beforeThreadCount
init|=
name|currentThreadCount
argument_list|()
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result.default"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|doSendMessages
argument_list|(
literal|"foo.default"
argument_list|,
literal|500
argument_list|,
literal|5
argument_list|,
name|DefaultTaskExecutorType
operator|.
name|ThreadPool
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|doSendMessages
argument_list|(
literal|"foo.default"
argument_list|,
literal|500
argument_list|,
literal|5
argument_list|,
name|DefaultTaskExecutorType
operator|.
name|ThreadPool
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Long
name|numberThreadsCreated
init|=
name|currentThreadCount
argument_list|()
operator|-
name|beforeThreadCount
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Number of threads created, testDefaultTaskExecutorThreadPoolAtComponentConfig: "
operator|+
name|numberThreadsCreated
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Number of threads created should be equal or lower than "
operator|+
literal|"100 with ThreadPoolTaskExecutor as a component default"
argument_list|,
name|numberThreadsCreated
operator|<=
literal|100
argument_list|)
expr_stmt|;
block|}
DECL|method|currentThreadCount ()
specifier|private
name|Long
name|currentThreadCount
parameter_list|()
throws|throws
name|NoSuchMethodException
throws|,
name|IllegalAccessException
throws|,
name|InvocationTargetException
block|{
name|Method
name|m
init|=
name|ThreadHelper
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"nextThreadCounter"
argument_list|,
operator|(
name|Class
argument_list|<
name|?
argument_list|>
index|[]
operator|)
literal|null
argument_list|)
decl_stmt|;
name|m
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Long
name|nextThreadCount
init|=
operator|(
name|Long
operator|)
name|m
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
decl_stmt|;
return|return
name|nextThreadCount
return|;
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
name|ConnectionFactory
name|connectionFactory
init|=
name|CamelJmsTestHelper
operator|.
name|createConnectionFactory
argument_list|()
decl_stmt|;
name|JmsComponent
name|jmsComponent
init|=
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
decl_stmt|;
name|jmsComponent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setMaxMessagesPerTask
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|jmsComponent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setIdleTaskExecutionLimit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|jmsComponent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setConcurrentConsumers
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|jmsComponent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setMaxConcurrentConsumers
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|jmsComponent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|50
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"testDefaultTaskExecutorThreadPoolAtComponentConfig"
operator|.
name|equals
argument_list|(
name|getTestMethodName
argument_list|()
argument_list|)
condition|)
block|{
name|jmsComponent
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setDefaultTaskExecutorType
argument_list|(
name|DefaultTaskExecutorType
operator|.
name|ThreadPool
argument_list|)
expr_stmt|;
block|}
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"activemq"
argument_list|,
name|jmsComponent
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
DECL|method|doSendMessages (final String queueName, int messages, int poolSize, final DefaultTaskExecutorType defaultTaskExecutorType)
specifier|private
name|void
name|doSendMessages
parameter_list|(
specifier|final
name|String
name|queueName
parameter_list|,
name|int
name|messages
parameter_list|,
name|int
name|poolSize
parameter_list|,
specifier|final
name|DefaultTaskExecutorType
name|defaultTaskExecutorType
parameter_list|)
throws|throws
name|Exception
block|{
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
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|messages
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
name|messages
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
name|String
name|options
init|=
name|defaultTaskExecutorType
operator|==
literal|null
condition|?
literal|""
else|:
literal|"?defaultTaskExecutorType="
operator|+
name|defaultTaskExecutorType
operator|.
name|toString
argument_list|()
decl_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"activemq:queue:"
operator|+
name|queueName
operator|+
name|options
argument_list|,
literal|"Message "
operator|+
name|index
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
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
literal|"activemq:queue:foo.simpleAsync?defaultTaskExecutorType=SimpleAsync"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"simpleAsync"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result.simpleAsync"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Reply"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:foo.threadPool?defaultTaskExecutorType=ThreadPool"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"threadPool"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result.threadPool"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Reply"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:foo.default"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"default"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result.default"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Reply"
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

