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
comment|/**  * Unit test for the batch size options on aggregator.  */
end_comment

begin_class
DECL|class|AggregatorBatchOptionsTest
specifier|public
class|class
name|AggregatorBatchOptionsTest
extends|extends
name|ContextTestSupport
block|{
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
DECL|method|testAggregateOutBatchSize ()
specifier|public
name|void
name|testAggregateOutBatchSize
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
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e1
comment|// our route is aggregating from the direct queue and sending the response to the mock
name|from
argument_list|(
literal|"direct:start"
argument_list|)
comment|// aggregated by header id
comment|// as we have not configured more on the aggregator it will default to aggregate the
comment|// latest exchange only
operator|.
name|aggregator
argument_list|()
operator|.
name|header
argument_list|(
literal|"id"
argument_list|)
comment|// wait for 0.5 seconds to aggregate
operator|.
name|batchTimeout
argument_list|(
literal|500L
argument_list|)
comment|// batch size in is the limit of number of exchanges received, so when we have received 100
comment|// exchanges then whatever we have in the collection will be sent
operator|.
name|batchSize
argument_list|(
literal|100
argument_list|)
comment|// limit the out batch size to 3 so when we have aggregated 3 exchanges
comment|// and we reach this limit then the exchanges is send
operator|.
name|outBatchSize
argument_list|(
literal|3
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
argument_list|)
expr_stmt|;
name|startCamelContext
argument_list|()
expr_stmt|;
comment|// START SNIPPET: e2
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
comment|// we expect 3 messages grouped by the latest message only
name|result
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Message 1c"
argument_list|,
literal|"Message 2b"
argument_list|,
literal|"Message 3a"
argument_list|)
expr_stmt|;
comment|// then we sent all the message at once
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 1a"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 2a"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 1b"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 2b"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 1c"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 3a"
argument_list|,
literal|"id"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
comment|// when we send message 4 then we will reach the collection batch size limit and the
comment|// exchanges above is the ones we have aggregated in the first batch
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 4"
argument_list|,
literal|"id"
argument_list|,
literal|"4"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// END SNIPPET: e2
block|}
DECL|method|testAggregateBatchSize ()
specifier|public
name|void
name|testAggregateBatchSize
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
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e3
comment|// our route is aggregating from the direct queue and sending the response to the mock
name|from
argument_list|(
literal|"direct:start"
argument_list|)
comment|// aggregated by header id
comment|// as we have not configured more on the aggregator it will default to aggregate the
comment|// latest exchange only
operator|.
name|aggregator
argument_list|()
operator|.
name|header
argument_list|(
literal|"id"
argument_list|)
comment|// wait for 0.5 seconds to aggregate
operator|.
name|batchTimeout
argument_list|(
literal|500L
argument_list|)
comment|// batch size in is the limit of number of exchanges received, so when we have received 100
comment|// exchanges then whatever we have in the collection will be sent
operator|.
name|batchSize
argument_list|(
literal|5
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e3
block|}
block|}
argument_list|)
expr_stmt|;
name|startCamelContext
argument_list|()
expr_stmt|;
comment|// START SNIPPET: e4
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
comment|// we expect 3 messages grouped by the latest message only
name|result
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Message 1c"
argument_list|,
literal|"Message 2b"
argument_list|)
expr_stmt|;
comment|// then we sent all the message at once
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 1a"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 2a"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 1b"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 2b"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 1c"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
comment|// when we sent the next message we have reached the in batch size limit and the current
comment|// aggregated exchanges will be sent
comment|// wait a while for aggregating in a slower box
name|Thread
operator|.
name|sleep
argument_list|(
literal|300L
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 3a"
argument_list|,
literal|"id"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 4"
argument_list|,
literal|"id"
argument_list|,
literal|"4"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 3b"
argument_list|,
literal|"id"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 3c"
argument_list|,
literal|"id"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 1d"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// END SNIPPET: e4
block|}
DECL|method|testAggregateBatchTimeout ()
specifier|public
name|void
name|testAggregateBatchTimeout
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
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e5
comment|// our route is aggregating from the direct queue and sending the response to the mock
name|from
argument_list|(
literal|"direct:start"
argument_list|)
comment|// aggregated by header id
comment|// as we have not configured more on the aggregator it will default to aggregate the
comment|// latest exchange only
operator|.
name|aggregator
argument_list|()
operator|.
name|header
argument_list|(
literal|"id"
argument_list|)
comment|// wait for 0.5 seconds to aggregate
operator|.
name|batchTimeout
argument_list|(
literal|500L
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e5
block|}
block|}
argument_list|)
expr_stmt|;
name|startCamelContext
argument_list|()
expr_stmt|;
comment|// START SNIPPET: e6
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
comment|// we expect 3 messages grouped by the latest message only
name|result
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Message 1c"
argument_list|,
literal|"Message 2b"
argument_list|,
literal|"Message 3a"
argument_list|)
expr_stmt|;
comment|// then we sent all the message at once
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 1a"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 2a"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 1b"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 2b"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 1c"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 3a"
argument_list|,
literal|"id"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|600L
argument_list|)
expr_stmt|;
comment|// these messages are not aggregated as the timeout should have accoured
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 4"
argument_list|,
literal|"id"
argument_list|,
literal|"4"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 3b"
argument_list|,
literal|"id"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 3c"
argument_list|,
literal|"id"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message 1d"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// END SNIPPET: e6
block|}
block|}
end_class

end_unit

