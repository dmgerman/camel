begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ironmq.integrationtest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ironmq
operator|.
name|integrationtest
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
name|concurrent
operator|.
name|TimeUnit
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
name|ironmq
operator|.
name|IronMQConstants
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
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"Must be manually tested. Provide your own projectId and token!"
argument_list|)
DECL|class|ConcurrentConsumerLoadTest
specifier|public
class|class
name|ConcurrentConsumerLoadTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|IRONMQCLOUD
specifier|private
specifier|static
specifier|final
name|String
name|IRONMQCLOUD
init|=
literal|"https://mq-aws-eu-west-1-1.iron.io"
decl_stmt|;
DECL|field|NO_OF_MESSAGES
specifier|private
specifier|static
specifier|final
name|int
name|NO_OF_MESSAGES
init|=
literal|50000
decl_stmt|;
DECL|field|BATCH_DELETE
specifier|private
specifier|static
specifier|final
name|String
name|BATCH_DELETE
init|=
literal|"true"
decl_stmt|;
DECL|field|CONCURRENT_CONSUMERS
specifier|private
specifier|static
specifier|final
name|int
name|CONCURRENT_CONSUMERS
init|=
literal|20
decl_stmt|;
DECL|field|PAYLOAD
specifier|private
specifier|static
specifier|final
name|String
name|PAYLOAD
init|=
literal|"{some:text, number:#}"
decl_stmt|;
comment|// replace with your project id
DECL|field|projectId
specifier|private
specifier|final
name|String
name|projectId
init|=
literal|"myIronMQproject"
decl_stmt|;
comment|// replace with your token
DECL|field|token
specifier|private
specifier|final
name|String
name|token
init|=
literal|"myIronMQToken"
decl_stmt|;
comment|// replace with your test queue name
DECL|field|ironmqQueue
specifier|private
specifier|final
name|String
name|ironmqQueue
init|=
literal|"testqueue"
decl_stmt|;
DECL|field|ironMQEndpoint
specifier|private
specifier|final
name|String
name|ironMQEndpoint
init|=
literal|"ironmq:"
operator|+
name|ironmqQueue
operator|+
literal|"?projectId="
operator|+
name|projectId
operator|+
literal|"&token="
operator|+
name|token
operator|+
literal|"&maxMessagesPerPoll=100&wait=30&ironMQCloud="
operator|+
name|IRONMQCLOUD
operator|+
literal|"&concurrentConsumers="
operator|+
name|CONCURRENT_CONSUMERS
operator|+
literal|"&batchDelete="
operator|+
name|BATCH_DELETE
decl_stmt|;
DECL|field|sedaEndpoint
specifier|private
specifier|final
name|String
name|sedaEndpoint
init|=
literal|"seda:push?concurrentConsumers="
operator|+
name|CONCURRENT_CONSUMERS
decl_stmt|;
annotation|@
name|Before
DECL|method|prepareQueue ()
specifier|public
name|void
name|prepareQueue
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// make sure the queue is empty before test
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|ironMQEndpoint
argument_list|,
literal|null
argument_list|,
name|IronMQConstants
operator|.
name|OPERATION
argument_list|,
name|IronMQConstants
operator|.
name|CLEARQUEUE
argument_list|)
expr_stmt|;
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|int
name|noOfBlocks
init|=
literal|0
decl_stmt|;
name|ArrayList
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
name|NO_OF_MESSAGES
condition|;
name|i
operator|++
control|)
block|{
name|String
name|payloadToSend
init|=
name|PAYLOAD
operator|.
name|replace
argument_list|(
literal|"#"
argument_list|,
literal|""
operator|+
name|i
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|payloadToSend
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|%
literal|100
operator|==
literal|0
condition|)
block|{
name|noOfBlocks
operator|++
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"sending blok "
operator|+
name|noOfBlocks
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|sedaEndpoint
argument_list|,
name|list
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|list
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:iron"
argument_list|)
decl_stmt|;
while|while
condition|(
name|mockEndpoint
operator|.
name|getReceivedCounter
argument_list|()
operator|!=
name|noOfBlocks
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Waiting for queue to fill up. Current size is "
operator|+
name|mockEndpoint
operator|.
name|getReceivedCounter
argument_list|()
operator|*
literal|100
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
name|long
name|delta
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
decl_stmt|;
name|int
name|seconds
init|=
operator|(
name|int
operator|)
name|delta
operator|/
literal|1000
decl_stmt|;
name|int
name|msgPrSec
init|=
name|NO_OF_MESSAGES
operator|/
name|seconds
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"IronMQPerformanceTest: Took: "
operator|+
name|seconds
operator|+
literal|" seconds to produce "
operator|+
name|NO_OF_MESSAGES
operator|+
literal|" messages. Which is "
operator|+
name|msgPrSec
operator|+
literal|" messages pr. second"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConcurrentConsumers ()
specifier|public
name|void
name|testConcurrentConsumers
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|context
operator|.
name|startRoute
argument_list|(
literal|"iron"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
name|NO_OF_MESSAGES
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|4
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
expr_stmt|;
name|long
name|delta
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
decl_stmt|;
name|int
name|seconds
init|=
operator|(
name|int
operator|)
name|delta
operator|/
literal|1000
decl_stmt|;
name|int
name|msgPrSec
init|=
name|NO_OF_MESSAGES
operator|/
name|seconds
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"IronmqPerformanceTest: Took: "
operator|+
name|seconds
operator|+
literal|" seconds to consume "
operator|+
name|NO_OF_MESSAGES
operator|+
literal|" messages. Which is "
operator|+
name|msgPrSec
operator|+
literal|" messages pr. second"
argument_list|)
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|ironMQEndpoint
argument_list|)
operator|.
name|id
argument_list|(
literal|"iron"
argument_list|)
operator|.
name|autoStartup
argument_list|(
literal|false
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|sedaEndpoint
argument_list|)
operator|.
name|to
argument_list|(
name|ironMQEndpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:iron"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

