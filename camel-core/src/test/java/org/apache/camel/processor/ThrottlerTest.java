begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|impl
operator|.
name|DefaultExchange
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
name|processor
operator|.
name|Throttler
operator|.
name|TimeSlot
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
name|builder
operator|.
name|Builder
operator|.
name|constant
import|;
end_import

begin_comment
comment|/**  * @version  */
end_comment

begin_class
DECL|class|ThrottlerTest
specifier|public
class|class
name|ThrottlerTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|INTERVAL
specifier|private
specifier|static
specifier|final
name|int
name|INTERVAL
init|=
literal|500
decl_stmt|;
DECL|field|messageCount
specifier|protected
name|int
name|messageCount
init|=
literal|9
decl_stmt|;
DECL|method|testSendLotsOfMessagesButOnly3GetThrough ()
specifier|public
name|void
name|testSendLotsOfMessagesButOnly3GetThrough
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|setResultWaitTime
argument_list|(
literal|2000
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
name|messageCount
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:a"
argument_list|,
literal|"<message>"
operator|+
name|i
operator|+
literal|"</message>"
argument_list|)
expr_stmt|;
block|}
comment|// lets pause to give the requests time to be processed
comment|// to check that the throttle really does kick in
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testSendLotsOfMessagesWithRejctExecution ()
specifier|public
name|void
name|testSendLotsOfMessagesWithRejctExecution
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|setResultWaitTime
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|MockEndpoint
name|errorEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:error"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|errorEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|errorEndpoint
operator|.
name|setResultWaitTime
argument_list|(
literal|2000
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
literal|6
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<message>"
operator|+
name|i
operator|+
literal|"</message>"
argument_list|)
expr_stmt|;
block|}
comment|// lets pause to give the requests time to be processed
comment|// to check that the throttle really does kick in
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|errorEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testSendLotsOfMessagesSimultaneouslyButOnly3GetThrough ()
specifier|public
name|void
name|testSendLotsOfMessagesSimultaneouslyButOnly3GetThrough
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
name|messageCount
argument_list|)
expr_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|messageCount
argument_list|)
decl_stmt|;
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
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
name|messageCount
condition|;
name|i
operator|++
control|)
block|{
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"<message>payload</message>"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|// let's wait for the exchanges to arrive
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// now assert that they have actually been throttled
name|long
name|minimumTime
init|=
operator|(
name|messageCount
operator|-
literal|1
operator|)
operator|*
name|INTERVAL
decl_stmt|;
comment|// add a little slack
name|long
name|delta
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
operator|+
literal|200
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should take at least "
operator|+
name|minimumTime
operator|+
literal|"ms, was: "
operator|+
name|delta
argument_list|,
name|delta
operator|>=
name|minimumTime
argument_list|)
expr_stmt|;
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
DECL|method|testTimeSlotCalculus ()
specifier|public
name|void
name|testTimeSlotCalculus
parameter_list|()
throws|throws
name|Exception
block|{
name|Throttler
name|throttler
init|=
operator|new
name|Throttler
argument_list|(
name|context
argument_list|,
literal|null
argument_list|,
name|constant
argument_list|(
literal|3
argument_list|)
argument_list|,
literal|1000
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// calculate will assign a new slot
name|throttler
operator|.
name|calculateDelay
argument_list|(
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|TimeSlot
name|slot
init|=
name|throttler
operator|.
name|nextSlot
argument_list|()
decl_stmt|;
comment|// start a new time slot
name|assertNotNull
argument_list|(
name|slot
argument_list|)
expr_stmt|;
comment|// make sure the same slot is used (3 exchanges per slot)
name|assertSame
argument_list|(
name|slot
argument_list|,
name|throttler
operator|.
name|nextSlot
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|slot
operator|.
name|isFull
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|slot
operator|.
name|isActive
argument_list|()
argument_list|)
expr_stmt|;
name|TimeSlot
name|next
init|=
name|throttler
operator|.
name|nextSlot
argument_list|()
decl_stmt|;
comment|// now we should have a new slot that starts somewhere in the future
name|assertNotSame
argument_list|(
name|slot
argument_list|,
name|next
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|next
operator|.
name|isActive
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTimeSlotCalculusForPeriod ()
specifier|public
name|void
name|testTimeSlotCalculusForPeriod
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|Throttler
name|throttler
init|=
operator|new
name|Throttler
argument_list|(
name|context
argument_list|,
literal|null
argument_list|,
name|constant
argument_list|(
literal|3
argument_list|)
argument_list|,
literal|1000
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|throttler
operator|.
name|calculateDelay
argument_list|(
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|TimeSlot
name|slot
init|=
name|throttler
operator|.
name|getSlot
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|slot
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|slot
argument_list|,
name|throttler
operator|.
name|nextSlot
argument_list|()
argument_list|)
expr_stmt|;
comment|// we've only used up two of three slots, but now we introduce a time delay
comment|// so to make the slot not valid anymore
name|Thread
operator|.
name|sleep
argument_list|(
call|(
name|long
call|)
argument_list|(
literal|1.5
operator|*
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|slot
operator|.
name|isActive
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|slot
argument_list|,
name|throttler
operator|.
name|nextSlot
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testConfigurationWithConstantExpression ()
specifier|public
name|void
name|testConfigurationWithConstantExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
name|messageCount
argument_list|)
expr_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|messageCount
argument_list|)
decl_stmt|;
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
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
name|messageCount
condition|;
name|i
operator|++
control|)
block|{
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:expressionConstant"
argument_list|,
literal|"<message>payload</message>"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|// let's wait for the exchanges to arrive
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// now assert that they have actually been throttled
name|long
name|minimumTime
init|=
operator|(
name|messageCount
operator|-
literal|1
operator|)
operator|*
name|INTERVAL
decl_stmt|;
comment|// add a little slack
name|long
name|delta
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
operator|+
literal|200
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should take at least "
operator|+
name|minimumTime
operator|+
literal|"ms, was: "
operator|+
name|delta
argument_list|,
name|delta
operator|>=
name|minimumTime
argument_list|)
expr_stmt|;
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
DECL|method|testConfigurationWithHeaderExpression ()
specifier|public
name|void
name|testConfigurationWithHeaderExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
name|messageCount
argument_list|)
expr_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|messageCount
argument_list|)
decl_stmt|;
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
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
name|messageCount
condition|;
name|i
operator|++
control|)
block|{
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
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:expressionHeader"
argument_list|,
literal|"<message>payload</message>"
argument_list|,
literal|"throttleValue"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|// let's wait for the exchanges to arrive
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// now assert that they have actually been throttled
name|long
name|minimumTime
init|=
operator|(
name|messageCount
operator|-
literal|1
operator|)
operator|*
name|INTERVAL
decl_stmt|;
comment|// add a little slack
name|long
name|delta
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
operator|+
literal|200
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should take at least "
operator|+
name|minimumTime
operator|+
literal|"ms, was: "
operator|+
name|delta
argument_list|,
name|delta
operator|>=
name|minimumTime
argument_list|)
expr_stmt|;
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
DECL|method|testConfigurationWithChangingHeaderExpression ()
specifier|public
name|void
name|testConfigurationWithChangingHeaderExpression
parameter_list|()
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
name|messageCount
argument_list|)
decl_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|sendMessagesWithHeaderExpression
argument_list|(
name|executor
argument_list|,
name|resultEndpoint
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
name|sendMessagesWithHeaderExpression
argument_list|(
name|executor
argument_list|,
name|resultEndpoint
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
name|sendMessagesWithHeaderExpression
argument_list|(
name|executor
argument_list|,
name|resultEndpoint
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
DECL|method|sendMessagesWithHeaderExpression (ExecutorService executor, MockEndpoint resultEndpoint, final int throttle)
specifier|private
name|void
name|sendMessagesWithHeaderExpression
parameter_list|(
name|ExecutorService
name|executor
parameter_list|,
name|MockEndpoint
name|resultEndpoint
parameter_list|,
specifier|final
name|int
name|throttle
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
name|messageCount
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|messageCount
condition|;
name|i
operator|++
control|)
block|{
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
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:expressionHeader"
argument_list|,
literal|"<message>payload</message>"
argument_list|,
literal|"throttleValue"
argument_list|,
name|throttle
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|// let's wait for the exchanges to arrive
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// now assert that they have actually been throttled
name|long
name|minimumTime
init|=
operator|(
name|messageCount
operator|-
literal|1
operator|)
operator|*
name|INTERVAL
operator|/
name|throttle
decl_stmt|;
comment|// add a little slack
name|long
name|delta
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
operator|+
literal|200
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should take at least "
operator|+
name|minimumTime
operator|+
literal|"ms, was: "
operator|+
name|delta
argument_list|,
name|delta
operator|>=
name|minimumTime
argument_list|)
expr_stmt|;
name|long
name|maxTime
init|=
operator|(
name|messageCount
operator|-
literal|1
operator|)
operator|*
name|INTERVAL
operator|/
name|throttle
operator|*
literal|3
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should take at most "
operator|+
name|maxTime
operator|+
literal|"ms, was: "
operator|+
name|delta
argument_list|,
name|delta
operator|<=
name|maxTime
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
name|onException
argument_list|(
name|ThrottlerRejectedExecutionException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:error"
argument_list|)
expr_stmt|;
comment|// START SNIPPET: ex
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|throttle
argument_list|(
literal|3
argument_list|)
operator|.
name|timePeriodMillis
argument_list|(
literal|10000
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:result"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: ex
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|throttle
argument_list|(
literal|1
argument_list|)
operator|.
name|timePeriodMillis
argument_list|(
name|INTERVAL
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:result"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:expressionConstant"
argument_list|)
operator|.
name|throttle
argument_list|(
name|constant
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|timePeriodMillis
argument_list|(
name|INTERVAL
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:result"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:expressionHeader"
argument_list|)
operator|.
name|throttle
argument_list|(
name|header
argument_list|(
literal|"throttleValue"
argument_list|)
argument_list|)
operator|.
name|timePeriodMillis
argument_list|(
name|INTERVAL
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:result"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|throttle
argument_list|(
literal|2
argument_list|)
operator|.
name|timePeriodMillis
argument_list|(
literal|10000
argument_list|)
operator|.
name|rejectExecution
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:result"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

