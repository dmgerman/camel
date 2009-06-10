begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
name|ContextTestSupport
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
name|PollingConsumer
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
name|springframework
operator|.
name|scheduling
operator|.
name|concurrent
operator|.
name|ThreadPoolTaskExecutor
import|;
end_import

begin_comment
comment|/**  * Unit test to verify that we can stop and start a seda consumer while a producer  * continues to send messages to it.  */
end_comment

begin_class
DECL|class|SedaConsumerStartStopTest
specifier|public
class|class
name|SedaConsumerStartStopTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|SEDA_QUEUE_CONSUMERS_5
specifier|private
specifier|static
specifier|final
name|String
name|SEDA_QUEUE_CONSUMERS_5
init|=
literal|"seda:queue?concurrentConsumers=5"
decl_stmt|;
DECL|field|consumer
specifier|private
name|PollingConsumer
name|consumer
decl_stmt|;
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
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
name|consumer
operator|!=
literal|null
condition|)
block|{
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|sendMessagesToQueue ()
specifier|private
name|void
name|sendMessagesToQueue
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolTaskExecutor
name|executor
init|=
operator|new
name|ThreadPoolTaskExecutor
argument_list|()
decl_stmt|;
name|executor
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
comment|// when this delay is removed, the seda endpoint has ordering issues
try|try
block|{
comment|// do some random sleep to simulate spread in user activity
comment|// range is 5-15
name|Thread
operator|.
name|sleep
argument_list|(
operator|new
name|Random
argument_list|()
operator|.
name|nextInt
argument_list|(
literal|10
argument_list|)
operator|+
literal|5
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:queue"
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|initRoute ()
specifier|public
name|void
name|initRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:queue"
argument_list|)
decl_stmt|;
name|consumer
operator|=
name|endpoint
operator|.
name|createPollingConsumer
argument_list|()
expr_stmt|;
block|}
DECL|method|testStartStopConsumer ()
specifier|public
name|void
name|testStartStopConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectsAscending
argument_list|(
name|body
argument_list|()
argument_list|)
expr_stmt|;
name|initRoute
argument_list|()
expr_stmt|;
comment|// will send messages to the queue in another thread simulation a producer
name|sendMessagesToQueue
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|==
literal|5
condition|)
block|{
comment|// stop while sending, and then start again to pickup what is left in the queue
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
comment|// use 1000 as timeout otherwise we might get null if the consumer hasn't been started again
name|Exchange
name|exchange
init|=
name|consumer
operator|.
name|receive
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"mock:result"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testConcurrentConsumers ()
specifier|public
name|void
name|testConcurrentConsumers
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|(
name|context
argument_list|)
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
name|SEDA_QUEUE_CONSUMERS_5
argument_list|)
operator|.
name|delay
argument_list|(
literal|500
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|10
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|sendBody
argument_list|(
name|SEDA_QUEUE_CONSUMERS_5
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|800
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|mock
operator|.
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|700
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|mock
operator|.
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

