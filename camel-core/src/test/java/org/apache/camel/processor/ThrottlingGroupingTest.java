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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ThrottlingGroupingTest
specifier|public
class|class
name|ThrottlingGroupingTest
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
DECL|field|MESSAGE_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|MESSAGE_COUNT
init|=
literal|9
decl_stmt|;
DECL|field|TOLERANCE
specifier|private
specifier|static
specifier|final
name|int
name|TOLERANCE
init|=
literal|50
decl_stmt|;
annotation|@
name|Test
DECL|method|testGroupingWithSingleConstant ()
specifier|public
name|void
name|testGroupingWithSingleConstant
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Kaboom"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"seda:a"
argument_list|,
literal|"Kaboom"
argument_list|,
literal|"max"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"seda:a"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"max"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"seda:a"
argument_list|,
literal|"Bye World"
argument_list|,
literal|"max"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGroupingWithDynamicHeaderExpression ()
specifier|public
name|void
name|testGroupingWithDynamicHeaderExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result2"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:dead"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Kaboom"
argument_list|,
literal|"Saloon"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:resultdynamic"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Dynamic World"
argument_list|,
literal|"Bye Dynamic World"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"seda:a"
argument_list|,
literal|"Kaboom"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"seda:a"
argument_list|,
literal|"Saloon"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"max"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"seda:a"
argument_list|,
literal|"Hello World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"seda:b"
argument_list|,
literal|"Bye World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"max"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"seda:c"
argument_list|,
literal|"Hello Dynamic World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"seda:c"
argument_list|,
literal|"Bye Dynamic World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendLotsOfMessagesButOnly3GetThroughWithin2Seconds ()
specifier|public
name|void
name|testSendLotsOfMessagesButOnly3GetThroughWithin2Seconds
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:gresult"
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
literal|9
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|%
literal|2
operator|==
literal|0
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
block|}
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"seda:ga"
argument_list|,
literal|"<message>"
operator|+
name|i
operator|+
literal|"</message>"
argument_list|,
name|headers
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
DECL|method|assertThrottlerTiming (final long elapsedTimeMs, final int throttle, final int intervalMs, final int messageCount)
specifier|private
name|void
name|assertThrottlerTiming
parameter_list|(
specifier|final
name|long
name|elapsedTimeMs
parameter_list|,
specifier|final
name|int
name|throttle
parameter_list|,
specifier|final
name|int
name|intervalMs
parameter_list|,
specifier|final
name|int
name|messageCount
parameter_list|)
block|{
comment|// now assert that they have actually been throttled (use +/- 50 as slack)
name|long
name|minimum
init|=
name|calculateMinimum
argument_list|(
name|intervalMs
argument_list|,
name|throttle
argument_list|,
name|messageCount
argument_list|)
operator|-
literal|50
decl_stmt|;
name|long
name|maximum
init|=
name|calculateMaximum
argument_list|(
name|intervalMs
argument_list|,
name|throttle
argument_list|,
name|messageCount
argument_list|)
operator|+
literal|50
decl_stmt|;
comment|// add 500 in case running on slow CI boxes
name|maximum
operator|+=
literal|500
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Sent {} exchanges in {}ms, with throttle rate of {} per {}ms. Calculated min {}ms and max {}ms"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|messageCount
block|,
name|elapsedTimeMs
block|,
name|throttle
block|,
name|intervalMs
block|,
name|minimum
block|,
name|maximum
block|}
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should take at least "
operator|+
name|minimum
operator|+
literal|"ms, was: "
operator|+
name|elapsedTimeMs
argument_list|,
name|elapsedTimeMs
operator|>=
name|minimum
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should take at most "
operator|+
name|maximum
operator|+
literal|"ms, was: "
operator|+
name|elapsedTimeMs
argument_list|,
name|elapsedTimeMs
operator|<=
name|maximum
operator|+
name|TOLERANCE
argument_list|)
expr_stmt|;
block|}
DECL|method|sendMessagesAndAwaitDelivery (final int messageCount, final String endpointUri, final int threadPoolSize, final MockEndpoint receivingEndpoint)
specifier|private
name|long
name|sendMessagesAndAwaitDelivery
parameter_list|(
specifier|final
name|int
name|messageCount
parameter_list|,
specifier|final
name|String
name|endpointUri
parameter_list|,
specifier|final
name|int
name|threadPoolSize
parameter_list|,
specifier|final
name|MockEndpoint
name|receivingEndpoint
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|threadPoolSize
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|receivingEndpoint
operator|!=
literal|null
condition|)
block|{
name|receivingEndpoint
operator|.
name|expectedMessageCount
argument_list|(
name|messageCount
argument_list|)
expr_stmt|;
block|}
name|long
name|start
init|=
name|System
operator|.
name|nanoTime
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageCount
operator|%
literal|2
operator|==
literal|0
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
block|}
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
name|endpointUri
argument_list|,
literal|"<message>payload</message>"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|// let's wait for the exchanges to arrive
if|if
condition|(
name|receivingEndpoint
operator|!=
literal|null
condition|)
block|{
name|receivingEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
return|return
name|TimeUnit
operator|.
name|NANOSECONDS
operator|.
name|toMillis
argument_list|(
name|System
operator|.
name|nanoTime
argument_list|()
operator|-
name|start
argument_list|)
return|;
block|}
finally|finally
block|{
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSendLotsOfMessagesSimultaneouslyButOnlyGetThroughAsConstantThrottleValue ()
specifier|public
name|void
name|testSendLotsOfMessagesSimultaneouslyButOnlyGetThroughAsConstantThrottleValue
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:gresult"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|long
name|elapsed
init|=
name|sendMessagesAndAwaitDelivery
argument_list|(
name|MESSAGE_COUNT
argument_list|,
literal|"direct:ga"
argument_list|,
name|MESSAGE_COUNT
argument_list|,
name|resultEndpoint
argument_list|)
decl_stmt|;
name|assertThrottlerTiming
argument_list|(
name|elapsed
argument_list|,
literal|5
argument_list|,
name|INTERVAL
argument_list|,
name|MESSAGE_COUNT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
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
literal|"mock:gresult"
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
name|MESSAGE_COUNT
argument_list|)
expr_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|MESSAGE_COUNT
argument_list|)
decl_stmt|;
try|try
block|{
name|sendMessagesWithHeaderExpression
argument_list|(
name|executor
argument_list|,
name|resultEndpoint
argument_list|,
literal|5
argument_list|,
name|INTERVAL
argument_list|,
name|MESSAGE_COUNT
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|calculateMinimum (final long periodMs, final long throttleRate, final long messageCount)
specifier|private
name|long
name|calculateMinimum
parameter_list|(
specifier|final
name|long
name|periodMs
parameter_list|,
specifier|final
name|long
name|throttleRate
parameter_list|,
specifier|final
name|long
name|messageCount
parameter_list|)
block|{
if|if
condition|(
name|messageCount
operator|%
name|throttleRate
operator|>
literal|0
condition|)
block|{
return|return
operator|(
name|long
operator|)
name|Math
operator|.
name|floor
argument_list|(
operator|(
name|double
operator|)
name|messageCount
operator|/
operator|(
name|double
operator|)
name|throttleRate
argument_list|)
operator|*
name|periodMs
return|;
block|}
else|else
block|{
return|return
call|(
name|long
call|)
argument_list|(
name|Math
operator|.
name|floor
argument_list|(
operator|(
name|double
operator|)
name|messageCount
operator|/
operator|(
name|double
operator|)
name|throttleRate
argument_list|)
operator|*
name|periodMs
argument_list|)
operator|-
name|periodMs
return|;
block|}
block|}
DECL|method|calculateMaximum (final long periodMs, final long throttleRate, final long messageCount)
specifier|private
name|long
name|calculateMaximum
parameter_list|(
specifier|final
name|long
name|periodMs
parameter_list|,
specifier|final
name|long
name|throttleRate
parameter_list|,
specifier|final
name|long
name|messageCount
parameter_list|)
block|{
return|return
operator|(
operator|(
name|long
operator|)
name|Math
operator|.
name|ceil
argument_list|(
operator|(
name|double
operator|)
name|messageCount
operator|/
operator|(
name|double
operator|)
name|throttleRate
argument_list|)
operator|)
operator|*
name|periodMs
return|;
block|}
DECL|method|sendMessagesWithHeaderExpression (final ExecutorService executor, final MockEndpoint resultEndpoint, final int throttle, final int intervalMs, final int messageCount)
specifier|private
name|void
name|sendMessagesWithHeaderExpression
parameter_list|(
specifier|final
name|ExecutorService
name|executor
parameter_list|,
specifier|final
name|MockEndpoint
name|resultEndpoint
parameter_list|,
specifier|final
name|int
name|throttle
parameter_list|,
specifier|final
name|int
name|intervalMs
parameter_list|,
specifier|final
name|int
name|messageCount
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
name|nanoTime
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"throttleValue"
argument_list|,
name|throttle
argument_list|)
expr_stmt|;
if|if
condition|(
name|messageCount
operator|%
literal|2
operator|==
literal|0
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
block|}
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:gexpressionHeader"
argument_list|,
literal|"<message>payload</message>"
argument_list|,
name|headers
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
name|long
name|elapsed
init|=
name|TimeUnit
operator|.
name|NANOSECONDS
operator|.
name|toMillis
argument_list|(
name|System
operator|.
name|nanoTime
argument_list|()
operator|-
name|start
argument_list|)
decl_stmt|;
name|assertThrottlerTiming
argument_list|(
name|elapsed
argument_list|,
name|throttle
argument_list|,
name|intervalMs
argument_list|,
name|messageCount
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:dead"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:a"
argument_list|)
operator|.
name|throttle
argument_list|(
name|header
argument_list|(
literal|"max"
argument_list|)
argument_list|,
literal|1
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:b"
argument_list|)
operator|.
name|throttle
argument_list|(
name|header
argument_list|(
literal|"max"
argument_list|)
argument_list|,
literal|2
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:c"
argument_list|)
operator|.
name|throttle
argument_list|(
name|header
argument_list|(
literal|"max"
argument_list|)
argument_list|,
name|header
argument_list|(
literal|"key"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultdynamic"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:ga"
argument_list|)
operator|.
name|throttle
argument_list|(
name|constant
argument_list|(
literal|3
argument_list|)
argument_list|,
name|header
argument_list|(
literal|"key"
argument_list|)
argument_list|)
operator|.
name|timePeriodMillis
argument_list|(
literal|1000
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:gresult"
argument_list|,
literal|"mock:gresult"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:ga"
argument_list|)
operator|.
name|throttle
argument_list|(
name|constant
argument_list|(
literal|5
argument_list|)
argument_list|,
name|header
argument_list|(
literal|"key"
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
literal|"log:gresult"
argument_list|,
literal|"mock:gresult"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:gexpressionHeader"
argument_list|)
operator|.
name|throttle
argument_list|(
name|header
argument_list|(
literal|"throttleValue"
argument_list|)
argument_list|,
name|header
argument_list|(
literal|"key"
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
literal|"log:gresult"
argument_list|,
literal|"mock:gresult"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

