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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|ProducerTemplate
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
name|NotifyBuilder
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
name|direct
operator|.
name|DirectEndpoint
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
DECL|class|SamplingThrottlerTest
specifier|public
class|class
name|SamplingThrottlerTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testSamplingFromExchangeStream ()
specifier|public
name|void
name|testSamplingFromExchangeStream
parameter_list|()
throws|throws
name|Exception
block|{
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|15
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
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
name|expectedMinimumMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|sentExchanges
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|sendExchangesThroughDroppingThrottler
argument_list|(
name|sentExchanges
argument_list|,
literal|15
argument_list|)
expr_stmt|;
name|notify
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|validateDroppedExchanges
argument_list|(
name|sentExchanges
argument_list|,
name|mock
operator|.
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBurstySampling ()
specifier|public
name|void
name|testBurstySampling
parameter_list|()
throws|throws
name|Exception
block|{
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|5
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
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
name|expectedMinimumMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|sentExchanges
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// send a burst of 5 exchanges, expecting only one to get through
name|sendExchangesThroughDroppingThrottler
argument_list|(
name|sentExchanges
argument_list|,
literal|5
argument_list|)
expr_stmt|;
comment|// sleep through a complete period
name|Thread
operator|.
name|sleep
argument_list|(
literal|1100
argument_list|)
expr_stmt|;
comment|// send another 5 now
name|sendExchangesThroughDroppingThrottler
argument_list|(
name|sentExchanges
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|notify
operator|.
name|matchesMockWaitTime
argument_list|()
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|validateDroppedExchanges
argument_list|(
name|sentExchanges
argument_list|,
name|mock
operator|.
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendLotsOfMessagesSimultaneouslyButOnly3GetThrough ()
specifier|public
name|void
name|testSendLotsOfMessagesSimultaneouslyButOnly3GetThrough
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
name|expectedMinimumMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|4000
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Exchange
argument_list|>
name|sentExchanges
init|=
name|Collections
operator|.
name|synchronizedList
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Exchange
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|5
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
literal|5
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
try|try
block|{
name|sendExchangesThroughDroppingThrottler
argument_list|(
name|sentExchanges
argument_list|,
literal|35
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSamplingUsingMessageFrequency ()
specifier|public
name|void
name|testSamplingUsingMessageFrequency
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|totalMessages
init|=
literal|100
decl_stmt|;
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
name|expectedMinimumMessageCount
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|100
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
name|totalMessages
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:sample-messageFrequency"
argument_list|,
literal|"<message>"
operator|+
name|i
operator|+
literal|"</message>"
argument_list|)
expr_stmt|;
block|}
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSamplingUsingMessageFrequencyViaDSL ()
specifier|public
name|void
name|testSamplingUsingMessageFrequencyViaDSL
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|totalMessages
init|=
literal|50
decl_stmt|;
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
name|expectedMinimumMessageCount
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|100
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
name|totalMessages
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:sample-messageFrequency-via-dsl"
argument_list|,
literal|"<message>"
operator|+
name|i
operator|+
literal|"</message>"
argument_list|)
expr_stmt|;
block|}
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|sendExchangesThroughDroppingThrottler (List<Exchange> sentExchanges, int messages)
specifier|private
name|void
name|sendExchangesThroughDroppingThrottler
parameter_list|(
name|List
argument_list|<
name|Exchange
argument_list|>
name|sentExchanges
parameter_list|,
name|int
name|messages
parameter_list|)
throws|throws
name|Exception
block|{
name|ProducerTemplate
name|myTemplate
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|DirectEndpoint
name|targetEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"direct:sample"
argument_list|,
name|DirectEndpoint
operator|.
name|class
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
name|Exchange
name|e
init|=
name|targetEndpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<message>"
operator|+
name|i
operator|+
literal|"</message>"
argument_list|)
expr_stmt|;
comment|// only send if we are still started
if|if
condition|(
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
name|myTemplate
operator|.
name|send
argument_list|(
name|targetEndpoint
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|sentExchanges
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
block|}
name|myTemplate
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|validateDroppedExchanges (List<Exchange> sentExchanges, int expectedNotDroppedCount)
specifier|private
name|void
name|validateDroppedExchanges
parameter_list|(
name|List
argument_list|<
name|Exchange
argument_list|>
name|sentExchanges
parameter_list|,
name|int
name|expectedNotDroppedCount
parameter_list|)
block|{
name|int
name|notDropped
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Exchange
name|e
range|:
name|sentExchanges
control|)
block|{
name|Boolean
name|stopped
init|=
name|e
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ROUTE_STOP
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|stopped
operator|==
literal|null
condition|)
block|{
name|notDropped
operator|++
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
name|expectedNotDroppedCount
argument_list|,
name|notDropped
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
comment|// START SNIPPET: e1
name|from
argument_list|(
literal|"direct:sample"
argument_list|)
operator|.
name|sample
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:sample-configured"
argument_list|)
operator|.
name|sample
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:sample-configured-via-dsl"
argument_list|)
operator|.
name|sample
argument_list|()
operator|.
name|samplePeriod
argument_list|(
literal|1
argument_list|)
operator|.
name|timeUnits
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:sample-messageFrequency"
argument_list|)
operator|.
name|sample
argument_list|(
literal|10
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:sample-messageFrequency-via-dsl"
argument_list|)
operator|.
name|sample
argument_list|()
operator|.
name|sampleMessageFrequency
argument_list|(
literal|5
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

