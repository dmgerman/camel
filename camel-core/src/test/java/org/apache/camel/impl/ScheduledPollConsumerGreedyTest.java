begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|camel
operator|.
name|Consumer
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
name|spi
operator|.
name|PollingConsumerPollStrategy
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
DECL|class|ScheduledPollConsumerGreedyTest
specifier|public
class|class
name|ScheduledPollConsumerGreedyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|polled
specifier|private
specifier|final
name|AtomicInteger
name|polled
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|test321Greedy ()
specifier|public
name|void
name|test321Greedy
parameter_list|()
throws|throws
name|Exception
block|{
name|polled
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|MockScheduledPollConsumer
name|consumer
init|=
operator|new
name|Mock321ScheduledPollConsumer
argument_list|(
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setGreedy
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setPollStrategy
argument_list|(
operator|new
name|PollingConsumerPollStrategy
argument_list|()
block|{
specifier|public
name|boolean
name|begin
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|commit
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|polledMessages
parameter_list|)
block|{
name|polled
operator|.
name|addAndGet
argument_list|(
name|polledMessages
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|rollback
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|retryCounter
parameter_list|,
name|Exception
name|e
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|false
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|polled
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|test321NotGreedy ()
specifier|public
name|void
name|test321NotGreedy
parameter_list|()
throws|throws
name|Exception
block|{
name|polled
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|MockScheduledPollConsumer
name|consumer
init|=
operator|new
name|Mock321ScheduledPollConsumer
argument_list|(
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setGreedy
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setPollStrategy
argument_list|(
operator|new
name|PollingConsumerPollStrategy
argument_list|()
block|{
specifier|public
name|boolean
name|begin
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|commit
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|polledMessages
parameter_list|)
block|{
name|polled
operator|.
name|addAndGet
argument_list|(
name|polledMessages
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|rollback
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|retryCounter
parameter_list|,
name|Exception
name|e
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|false
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|polled
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|polled
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|polled
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|polled
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

