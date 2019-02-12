begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregator
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
name|AggregationStrategy
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
name|AsyncProcessor
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
name|Expression
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
name|BodyInAggregatingStrategy
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
name|SendProcessor
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
name|aggregate
operator|.
name|AggregateProcessor
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
name|support
operator|.
name|DefaultExchange
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
name|Test
import|;
end_import

begin_comment
comment|/**  * To test CAMEL-4037 that a restart of aggregator can re-initialize the timeout map  */
end_comment

begin_class
DECL|class|AggregateProcessorTimeoutCompletionRestartTest
specifier|public
class|class
name|AggregateProcessorTimeoutCompletionRestartTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
annotation|@
name|Override
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
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|executorService
operator|=
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAggregateProcessorTimeoutRestart ()
specifier|public
name|void
name|testAggregateProcessorTimeoutRestart
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
name|expectedBodiesReceived
argument_list|(
literal|"A+B"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COMPLETED_BY
argument_list|,
literal|"timeout"
argument_list|)
expr_stmt|;
name|AsyncProcessor
name|done
init|=
operator|new
name|SendProcessor
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
argument_list|)
decl_stmt|;
name|Expression
name|corr
init|=
name|header
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
name|AggregationStrategy
name|as
init|=
operator|new
name|BodyInAggregatingStrategy
argument_list|()
decl_stmt|;
name|AggregateProcessor
name|ap
init|=
operator|new
name|AggregateProcessor
argument_list|(
name|context
argument_list|,
name|done
argument_list|,
name|corr
argument_list|,
name|as
argument_list|,
name|executorService
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// start with a high timeout so no completes before we stop
name|ap
operator|.
name|setCompletionTimeout
argument_list|(
literal|250
argument_list|)
expr_stmt|;
name|ap
operator|.
name|setCompletionTimeoutCheckerInterval
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|ap
operator|.
name|start
argument_list|()
expr_stmt|;
name|Exchange
name|e1
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|e1
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|e1
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|Exchange
name|e2
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|e2
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|e2
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|ap
operator|.
name|process
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|ap
operator|.
name|process
argument_list|(
name|e2
argument_list|)
expr_stmt|;
comment|// shutdown before the 1/4 sec timeout occurs
comment|// however we use stop instead of shutdown as shutdown will clear the in memory aggregation repository,
name|ap
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// should be no completed
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|mock
operator|.
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
comment|// start aggregator again
name|ap
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// the aggregator should restore the timeout condition and trigger timeout
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mock
operator|.
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
name|ap
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAggregateProcessorTimeoutExpressionRestart ()
specifier|public
name|void
name|testAggregateProcessorTimeoutExpressionRestart
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
name|expectedBodiesReceived
argument_list|(
literal|"A+B"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COMPLETED_BY
argument_list|,
literal|"timeout"
argument_list|)
expr_stmt|;
name|AsyncProcessor
name|done
init|=
operator|new
name|SendProcessor
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
argument_list|)
decl_stmt|;
name|Expression
name|corr
init|=
name|header
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
name|AggregationStrategy
name|as
init|=
operator|new
name|BodyInAggregatingStrategy
argument_list|()
decl_stmt|;
name|AggregateProcessor
name|ap
init|=
operator|new
name|AggregateProcessor
argument_list|(
name|context
argument_list|,
name|done
argument_list|,
name|corr
argument_list|,
name|as
argument_list|,
name|executorService
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// start with a high timeout so no completes before we stop
name|ap
operator|.
name|setCompletionTimeoutExpression
argument_list|(
name|header
argument_list|(
literal|"myTimeout"
argument_list|)
argument_list|)
expr_stmt|;
name|ap
operator|.
name|setCompletionTimeoutCheckerInterval
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|ap
operator|.
name|start
argument_list|()
expr_stmt|;
name|Exchange
name|e1
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|e1
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|e1
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|e1
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"myTimeout"
argument_list|,
literal|250
argument_list|)
expr_stmt|;
name|Exchange
name|e2
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|e2
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|e2
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|e2
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"myTimeout"
argument_list|,
literal|250
argument_list|)
expr_stmt|;
name|ap
operator|.
name|process
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|ap
operator|.
name|process
argument_list|(
name|e2
argument_list|)
expr_stmt|;
comment|// shutdown before the 1/4 sec timeout occurs
comment|// however we use stop instead of shutdown as shutdown will clear the in memory aggregation repository,
name|ap
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// should be no completed
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|mock
operator|.
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
comment|// start aggregator again
name|ap
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// the aggregator should restore the timeout condition and trigger timeout
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mock
operator|.
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
name|ap
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAggregateProcessorTwoTimeoutExpressionRestart ()
specifier|public
name|void
name|testAggregateProcessorTwoTimeoutExpressionRestart
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
name|expectedBodiesReceived
argument_list|(
literal|"C+D"
argument_list|,
literal|"A+B"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COMPLETED_BY
argument_list|,
literal|"timeout"
argument_list|)
expr_stmt|;
name|AsyncProcessor
name|done
init|=
operator|new
name|SendProcessor
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
argument_list|)
decl_stmt|;
name|Expression
name|corr
init|=
name|header
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
name|AggregationStrategy
name|as
init|=
operator|new
name|BodyInAggregatingStrategy
argument_list|()
decl_stmt|;
name|AggregateProcessor
name|ap
init|=
operator|new
name|AggregateProcessor
argument_list|(
name|context
argument_list|,
name|done
argument_list|,
name|corr
argument_list|,
name|as
argument_list|,
name|executorService
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// start with a high timeout so no completes before we stop
name|ap
operator|.
name|setCompletionTimeoutExpression
argument_list|(
name|header
argument_list|(
literal|"myTimeout"
argument_list|)
argument_list|)
expr_stmt|;
name|ap
operator|.
name|setCompletionTimeoutCheckerInterval
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|ap
operator|.
name|start
argument_list|()
expr_stmt|;
name|Exchange
name|e1
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|e1
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|e1
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|e1
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"myTimeout"
argument_list|,
literal|300
argument_list|)
expr_stmt|;
name|Exchange
name|e2
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|e2
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|e2
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|e2
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"myTimeout"
argument_list|,
literal|300
argument_list|)
expr_stmt|;
name|Exchange
name|e3
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|e3
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"C"
argument_list|)
expr_stmt|;
name|e3
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"id"
argument_list|,
literal|456
argument_list|)
expr_stmt|;
name|e3
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"myTimeout"
argument_list|,
literal|250
argument_list|)
expr_stmt|;
name|Exchange
name|e4
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|e4
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"D"
argument_list|)
expr_stmt|;
name|e4
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"id"
argument_list|,
literal|456
argument_list|)
expr_stmt|;
name|e4
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"myTimeout"
argument_list|,
literal|250
argument_list|)
expr_stmt|;
name|ap
operator|.
name|process
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|ap
operator|.
name|process
argument_list|(
name|e2
argument_list|)
expr_stmt|;
name|ap
operator|.
name|process
argument_list|(
name|e3
argument_list|)
expr_stmt|;
name|ap
operator|.
name|process
argument_list|(
name|e4
argument_list|)
expr_stmt|;
comment|// shutdown before the 1/4 sec timeout occurs
comment|// however we use stop instead of shutdown as shutdown will clear the in memory aggregation repository,
name|ap
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// should be no completed
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|mock
operator|.
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
comment|// start aggregator again
name|ap
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// the aggregator should restore the timeout condition and trigger timeout
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|mock
operator|.
name|getReceivedCounter
argument_list|()
argument_list|)
expr_stmt|;
name|ap
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit
