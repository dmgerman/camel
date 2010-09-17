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
name|util
operator|.
name|ServiceHelper
import|;
end_import

begin_class
DECL|class|LimitedPollingConsumerPollStrategyTest
specifier|public
class|class
name|LimitedPollingConsumerPollStrategyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|strategy
specifier|private
name|LimitedPollingConsumerPollStrategy
name|strategy
decl_stmt|;
DECL|method|testLimitedPollingConsumerPollStrategy ()
specifier|public
name|void
name|testLimitedPollingConsumerPollStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|Exception
name|expectedException
init|=
operator|new
name|Exception
argument_list|(
literal|"Hello"
argument_list|)
decl_stmt|;
name|strategy
operator|=
operator|new
name|LimitedPollingConsumerPollStrategy
argument_list|()
expr_stmt|;
name|strategy
operator|.
name|setLimit
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|MockScheduledPollConsumer
name|consumer
init|=
operator|new
name|MockScheduledPollConsumer
argument_list|(
name|expectedException
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setPollStrategy
argument_list|(
name|strategy
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
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be suspended"
argument_list|,
name|consumer
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLimitAtTwoLimitedPollingConsumerPollStrategy ()
specifier|public
name|void
name|testLimitAtTwoLimitedPollingConsumerPollStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|Exception
name|expectedException
init|=
operator|new
name|Exception
argument_list|(
literal|"Hello"
argument_list|)
decl_stmt|;
name|strategy
operator|=
operator|new
name|LimitedPollingConsumerPollStrategy
argument_list|()
expr_stmt|;
name|strategy
operator|.
name|setLimit
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|MockScheduledPollConsumer
name|consumer
init|=
operator|new
name|MockScheduledPollConsumer
argument_list|(
name|expectedException
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setPollStrategy
argument_list|(
name|strategy
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
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be suspended"
argument_list|,
name|consumer
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLimitedPollingConsumerPollStrategySuccess ()
specifier|public
name|void
name|testLimitedPollingConsumerPollStrategySuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|Exception
name|expectedException
init|=
operator|new
name|Exception
argument_list|(
literal|"Hello"
argument_list|)
decl_stmt|;
name|strategy
operator|=
operator|new
name|LimitedPollingConsumerPollStrategy
argument_list|()
expr_stmt|;
name|strategy
operator|.
name|setLimit
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|MockScheduledPollConsumer
name|consumer
init|=
operator|new
name|MockScheduledPollConsumer
argument_list|(
name|expectedException
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setPollStrategy
argument_list|(
name|strategy
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
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
comment|// now force success
name|consumer
operator|.
name|setExceptionToThrowOnPoll
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testLimitedPollingConsumerPollStrategySuccessThenFail ()
specifier|public
name|void
name|testLimitedPollingConsumerPollStrategySuccessThenFail
parameter_list|()
throws|throws
name|Exception
block|{
name|Exception
name|expectedException
init|=
operator|new
name|Exception
argument_list|(
literal|"Hello"
argument_list|)
decl_stmt|;
name|strategy
operator|=
operator|new
name|LimitedPollingConsumerPollStrategy
argument_list|()
expr_stmt|;
name|strategy
operator|.
name|setLimit
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|MockScheduledPollConsumer
name|consumer
init|=
operator|new
name|MockScheduledPollConsumer
argument_list|(
name|expectedException
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setPollStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// fail 2 times
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
comment|// now force success 2 times
name|consumer
operator|.
name|setExceptionToThrowOnPoll
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
comment|// now fail again, after hitting limit at 3
name|consumer
operator|.
name|setExceptionToThrowOnPoll
argument_list|(
name|expectedException
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be suspended"
argument_list|,
name|consumer
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testTwoConsumersLimitedPollingConsumerPollStrategy ()
specifier|public
name|void
name|testTwoConsumersLimitedPollingConsumerPollStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|Exception
name|expectedException
init|=
operator|new
name|Exception
argument_list|(
literal|"Hello"
argument_list|)
decl_stmt|;
name|strategy
operator|=
operator|new
name|LimitedPollingConsumerPollStrategy
argument_list|()
expr_stmt|;
name|strategy
operator|.
name|setLimit
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|MockScheduledPollConsumer
name|consumer
init|=
operator|new
name|MockScheduledPollConsumer
argument_list|(
name|expectedException
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setPollStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
name|MockScheduledPollConsumer
name|consumer2
init|=
operator|new
name|MockScheduledPollConsumer
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|consumer2
operator|.
name|setPollStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
name|consumer2
operator|.
name|start
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|consumer2
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer2
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|consumer2
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer2
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|consumer2
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be suspended"
argument_list|,
name|consumer
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer2
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|consumer2
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testRestartManuallyLimitedPollingConsumerPollStrategy ()
specifier|public
name|void
name|testRestartManuallyLimitedPollingConsumerPollStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|Exception
name|expectedException
init|=
operator|new
name|Exception
argument_list|(
literal|"Hello"
argument_list|)
decl_stmt|;
name|strategy
operator|=
operator|new
name|LimitedPollingConsumerPollStrategy
argument_list|()
expr_stmt|;
name|strategy
operator|.
name|setLimit
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|MockScheduledPollConsumer
name|consumer
init|=
operator|new
name|MockScheduledPollConsumer
argument_list|(
name|expectedException
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setPollStrategy
argument_list|(
name|strategy
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
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be suspended"
argument_list|,
name|consumer
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
comment|// now start the consumer again
name|ServiceHelper
operator|.
name|resumeService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be suspended"
argument_list|,
name|consumer
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
comment|// now start the consumer again
name|ServiceHelper
operator|.
name|resumeService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
comment|// and let it succeed
name|consumer
operator|.
name|setExceptionToThrowOnPoll
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should still be started"
argument_list|,
name|consumer
operator|.
name|isStarted
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

