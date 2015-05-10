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
name|aggregate
operator|.
name|TimeoutAwareAggregationStrategy
import|;
end_import

begin_comment
comment|/**  * @version  */
end_comment

begin_class
DECL|class|AggregateTimeoutTest
specifier|public
class|class
name|AggregateTimeoutTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|invoked
specifier|private
specifier|final
name|AtomicInteger
name|invoked
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|receivedExchange
specifier|private
specifier|volatile
name|Exchange
name|receivedExchange
decl_stmt|;
DECL|field|receivedIndex
specifier|private
specifier|volatile
name|int
name|receivedIndex
decl_stmt|;
DECL|field|receivedTotal
specifier|private
specifier|volatile
name|int
name|receivedTotal
decl_stmt|;
DECL|field|receivedTimeout
specifier|private
specifier|volatile
name|long
name|receivedTimeout
decl_stmt|;
DECL|method|testAggregateTimeout ()
specifier|public
name|void
name|testAggregateTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A"
argument_list|,
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"B"
argument_list|,
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
comment|// wait about 4 seconds so that the timeout kicks in but it was discarded
name|mock
operator|.
name|assertIsSatisfied
argument_list|(
literal|4000
argument_list|)
expr_stmt|;
comment|// should invoke the timeout method
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|invoked
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|receivedExchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AB"
argument_list|,
name|receivedExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|receivedIndex
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|receivedTotal
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2000
argument_list|,
name|receivedTimeout
argument_list|)
expr_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"ABC"
argument_list|)
expr_stmt|;
comment|// now send 3 exchanges which shouldn't trigger the timeout anymore
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A"
argument_list|,
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"B"
argument_list|,
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"C"
argument_list|,
literal|"id"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
comment|// should complete before timeout
name|mock
operator|.
name|assertIsSatisfied
argument_list|(
literal|1500
argument_list|)
expr_stmt|;
comment|// should have not invoked the timeout method anymore
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|invoked
operator|.
name|get
argument_list|()
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
literal|"id"
argument_list|)
argument_list|,
operator|new
name|MyAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|discardOnCompletionTimeout
argument_list|()
operator|.
name|completionSize
argument_list|(
literal|3
argument_list|)
comment|// use a 2 second timeout
operator|.
name|completionTimeout
argument_list|(
literal|2000
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:aggregated"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyAggregationStrategy
specifier|private
class|class
name|MyAggregationStrategy
implements|implements
name|TimeoutAwareAggregationStrategy
block|{
DECL|method|timeout (Exchange oldExchange, int index, int total, long timeout)
specifier|public
name|void
name|timeout
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|total
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|invoked
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
comment|// we can't assert on the expected values here as the contract of this method doesn't
comment|// allow to throw any Throwable (including AssertionFailedError) so that we assert
comment|// about the expected values directly inside the test method itself. other than that
comment|// asserting inside a thread other than the main thread dosen't make much sense as
comment|// junit would not realize the failed assertion!
name|receivedExchange
operator|=
name|oldExchange
expr_stmt|;
name|receivedIndex
operator|=
name|index
expr_stmt|;
name|receivedTotal
operator|=
name|total
expr_stmt|;
name|receivedTimeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange)
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
return|return
name|newExchange
return|;
block|}
name|String
name|body
init|=
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
operator|+
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|oldExchange
return|;
block|}
block|}
block|}
end_class

end_unit

