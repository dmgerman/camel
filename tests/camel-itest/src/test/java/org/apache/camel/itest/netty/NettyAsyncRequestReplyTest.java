begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.netty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|netty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
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
name|Future
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
name|AvailablePortFinder
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

begin_comment
comment|/**  * Doing request/reply over Netty with async processing.  */
end_comment

begin_class
DECL|class|NettyAsyncRequestReplyTest
specifier|public
class|class
name|NettyAsyncRequestReplyTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|Test
DECL|method|testNetty ()
specifier|public
name|void
name|testNetty
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty:tcp://localhost:"
operator|+
name|port
operator|+
literal|"?textline=true&sync=true"
argument_list|,
literal|"World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|String
name|out2
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty:tcp://localhost:"
operator|+
name|port
operator|+
literal|"?textline=true&sync=true"
argument_list|,
literal|"Camel"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye Camel"
argument_list|,
name|out2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConcurrent ()
specifier|public
name|void
name|testConcurrent
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|size
init|=
literal|1000
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|20
argument_list|)
decl_stmt|;
comment|// we access the responses Map below only inside the main thread,
comment|// so no need for a thread-safe Map implementation
name|Map
argument_list|<
name|Integer
argument_list|,
name|Future
argument_list|<
name|String
argument_list|>
argument_list|>
name|responses
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
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
name|size
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
name|Future
argument_list|<
name|String
argument_list|>
name|out
init|=
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty:tcp://localhost:"
operator|+
name|port
operator|+
literal|"?textline=true&sync=true"
argument_list|,
name|index
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Sent {} received {}"
argument_list|,
name|index
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye "
operator|+
name|index
argument_list|,
name|reply
argument_list|)
expr_stmt|;
return|return
name|reply
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|responses
operator|.
name|put
argument_list|(
name|index
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
comment|// get all responses
name|Set
argument_list|<
name|String
argument_list|>
name|unique
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Future
argument_list|<
name|String
argument_list|>
name|future
range|:
name|responses
operator|.
name|values
argument_list|()
control|)
block|{
name|String
name|reply
init|=
name|future
operator|.
name|get
argument_list|(
literal|120
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should get a reply"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|unique
operator|.
name|add
argument_list|(
name|reply
argument_list|)
expr_stmt|;
block|}
comment|// should be 1000 unique responses
name|assertEquals
argument_list|(
literal|"Should be "
operator|+
name|size
operator|+
literal|" unique responses"
argument_list|,
name|size
argument_list|,
name|unique
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|executor
operator|.
name|shutdownNow
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
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|8000
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty:tcp://localhost:"
operator|+
name|port
operator|+
literal|"?textline=true&sync=true&reuseAddress=true&synchronous=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"activemq:queue:foo"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Writing reply ${body}"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"activemq:queue:foo"
argument_list|)
operator|.
name|transform
argument_list|(
name|simple
argument_list|(
literal|"Bye ${body}"
argument_list|)
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
block|}
end_class

end_unit

