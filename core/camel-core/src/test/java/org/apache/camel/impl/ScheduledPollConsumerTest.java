begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
DECL|class|ScheduledPollConsumerTest
specifier|public
class|class
name|ScheduledPollConsumerTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|rollback
specifier|private
specifier|static
name|boolean
name|rollback
decl_stmt|;
DECL|field|counter
specifier|private
specifier|static
name|int
name|counter
decl_stmt|;
DECL|field|event
specifier|private
specifier|static
name|String
name|event
init|=
literal|""
decl_stmt|;
annotation|@
name|Test
DECL|method|testExceptionOnPollAndCanStartAgain ()
specifier|public
name|void
name|testExceptionOnPollAndCanStartAgain
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Exception
name|expectedException
init|=
operator|new
name|Exception
argument_list|(
literal|"Hello, I should be thrown on shutdown only!"
argument_list|)
decl_stmt|;
specifier|final
name|Endpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|MockScheduledPollConsumer
name|consumer
init|=
operator|new
name|MockScheduledPollConsumer
argument_list|(
name|endpoint
argument_list|,
name|expectedException
argument_list|)
decl_stmt|;
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
block|{             }
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
if|if
condition|(
name|e
operator|==
name|expectedException
condition|)
block|{
name|rollback
operator|=
literal|true
expr_stmt|;
block|}
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
comment|// poll that throws an exception
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have rollback"
argument_list|,
literal|true
argument_list|,
name|rollback
argument_list|)
expr_stmt|;
comment|// prepare for 2nd run but this time it should not thrown an exception
comment|// on poll
name|rollback
operator|=
literal|false
expr_stmt|;
name|consumer
operator|.
name|setExceptionToThrowOnPoll
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// start it again and we should be able to run
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
comment|// should be able to stop with no problem
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should not have rollback"
argument_list|,
literal|false
argument_list|,
name|rollback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRetryAtMostThreeTimes ()
specifier|public
name|void
name|testRetryAtMostThreeTimes
parameter_list|()
throws|throws
name|Exception
block|{
name|counter
operator|=
literal|0
expr_stmt|;
name|event
operator|=
literal|""
expr_stmt|;
specifier|final
name|Exception
name|expectedException
init|=
operator|new
name|Exception
argument_list|(
literal|"Hello, I should be thrown on shutdown only!"
argument_list|)
decl_stmt|;
specifier|final
name|Endpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|MockScheduledPollConsumer
name|consumer
init|=
operator|new
name|MockScheduledPollConsumer
argument_list|(
name|endpoint
argument_list|,
name|expectedException
argument_list|)
decl_stmt|;
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
name|event
operator|+=
literal|"commit"
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
name|event
operator|+=
literal|"rollback"
expr_stmt|;
name|counter
operator|++
expr_stmt|;
if|if
condition|(
name|retryCounter
operator|<
literal|3
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setUseFixedDelay
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setDelay
argument_list|(
literal|60000
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// poll that throws an exception
name|consumer
operator|.
name|run
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// 3 retries + 1 last failed attempt when we give up
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|counter
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"rollbackrollbackrollbackrollback"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoExceptionOnPoll ()
specifier|public
name|void
name|testNoExceptionOnPoll
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Endpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|MockScheduledPollConsumer
name|consumer
init|=
operator|new
name|MockScheduledPollConsumer
argument_list|(
name|endpoint
argument_list|,
literal|null
argument_list|)
decl_stmt|;
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
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

