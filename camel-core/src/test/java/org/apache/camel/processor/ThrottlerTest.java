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
name|processor
operator|.
name|Throttler
operator|.
name|TimeSlot
import|;
end_import

begin_comment
comment|/**  * @version   */
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
literal|5000
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
literal|null
argument_list|,
literal|2
argument_list|,
literal|1000
argument_list|,
literal|null
argument_list|)
decl_stmt|;
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
comment|// make sure the same slot is used (2 exchanges per slot)
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
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

