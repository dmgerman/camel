begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
package|;
end_package

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
name|LinkedList
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RoutesBuilder
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreams
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreamsService
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Publisher
import|;
end_import

begin_import
import|import
name|reactor
operator|.
name|core
operator|.
name|publisher
operator|.
name|Flux
import|;
end_import

begin_comment
comment|/**  * Test the number of refill requests that are sent to a published from a Camel consumer.  */
end_comment

begin_class
DECL|class|RequestRefillTest
specifier|public
class|class
name|RequestRefillTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testUnboundedRequests ()
specifier|public
name|void
name|testUnboundedRequests
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|numReqs
init|=
literal|100
decl_stmt|;
name|List
argument_list|<
name|Long
argument_list|>
name|requests
init|=
name|executeTest
argument_list|(
literal|"unbounded"
argument_list|,
name|numReqs
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|requests
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|requests
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnboundedRequestsWatermarkNoEffect ()
specifier|public
name|void
name|testUnboundedRequestsWatermarkNoEffect
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|numReqs
init|=
literal|100
decl_stmt|;
name|List
argument_list|<
name|Long
argument_list|>
name|requests
init|=
name|executeTest
argument_list|(
literal|"unbounded-100"
argument_list|,
name|numReqs
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|requests
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|requests
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBoundedRequests ()
specifier|public
name|void
name|testBoundedRequests
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|numReqs
init|=
literal|100
decl_stmt|;
name|List
argument_list|<
name|Long
argument_list|>
name|requests
init|=
name|executeTest
argument_list|(
literal|"bounded"
argument_list|,
name|numReqs
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|requests
operator|.
name|size
argument_list|()
operator|>=
name|numReqs
operator|/
literal|10
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBoundedRequestsPercentageRefill ()
specifier|public
name|void
name|testBoundedRequestsPercentageRefill
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|numReqs
init|=
literal|120
decl_stmt|;
name|List
argument_list|<
name|Long
argument_list|>
name|requests0
init|=
name|executeTest
argument_list|(
literal|"bounded-0"
argument_list|,
name|numReqs
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Long
argument_list|>
name|requests10
init|=
name|executeTest
argument_list|(
literal|"bounded-10"
argument_list|,
name|numReqs
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Long
argument_list|>
name|requests25
init|=
name|executeTest
argument_list|(
literal|"bounded"
argument_list|,
name|numReqs
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Long
argument_list|>
name|requests80
init|=
name|executeTest
argument_list|(
literal|"bounded-80"
argument_list|,
name|numReqs
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Long
argument_list|>
name|requests100
init|=
name|executeTest
argument_list|(
literal|"bounded-100"
argument_list|,
name|numReqs
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|requests0
operator|.
name|size
argument_list|()
operator|<=
name|requests10
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// too close
name|assertTrue
argument_list|(
name|requests10
operator|.
name|size
argument_list|()
operator|<
name|requests25
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|requests25
operator|.
name|size
argument_list|()
operator|<
name|requests80
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|requests80
operator|.
name|size
argument_list|()
operator|<
name|requests100
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|executeTest (String name, int numReqs)
specifier|private
name|List
argument_list|<
name|Long
argument_list|>
name|executeTest
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|numReqs
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|List
argument_list|<
name|Long
argument_list|>
name|requests
init|=
name|Collections
operator|.
name|synchronizedList
argument_list|(
operator|new
name|LinkedList
argument_list|<>
argument_list|()
argument_list|)
decl_stmt|;
name|Publisher
argument_list|<
name|Long
argument_list|>
name|nums
init|=
name|createPublisher
argument_list|(
name|numReqs
argument_list|,
name|requests
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:"
operator|+
name|name
operator|+
literal|"-endpoint"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
name|numReqs
argument_list|)
expr_stmt|;
name|CamelReactiveStreamsService
name|rxCamel
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|()
argument_list|)
decl_stmt|;
name|nums
operator|.
name|subscribe
argument_list|(
name|rxCamel
operator|.
name|streamSubscriber
argument_list|(
name|name
argument_list|,
name|Long
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Long
name|sum
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|x
lambda|->
name|x
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Long
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|reduce
argument_list|(
parameter_list|(
name|l
parameter_list|,
name|r
parameter_list|)
lambda|->
name|l
operator|+
name|r
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|numReqs
operator|*
operator|(
name|numReqs
operator|+
literal|1
operator|)
operator|/
literal|2
argument_list|,
name|sum
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|requests
return|;
block|}
DECL|method|createPublisher (final int numReqs, final List<Long> requests)
specifier|private
name|Publisher
argument_list|<
name|Long
argument_list|>
name|createPublisher
parameter_list|(
specifier|final
name|int
name|numReqs
parameter_list|,
specifier|final
name|List
argument_list|<
name|Long
argument_list|>
name|requests
parameter_list|)
block|{
return|return
name|Flux
operator|.
name|range
argument_list|(
literal|1
argument_list|,
name|numReqs
argument_list|)
operator|.
name|map
argument_list|(
name|Long
operator|::
name|valueOf
argument_list|)
operator|.
name|doOnRequest
argument_list|(
name|requests
operator|::
name|add
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
literal|"reactive-streams:unbounded?maxInflightExchanges=-1"
argument_list|)
operator|.
name|delayer
argument_list|(
literal|1
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unbounded-endpoint"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"reactive-streams:unbounded-100?maxInflightExchanges=-1&exchangesRefillLowWatermark=1"
argument_list|)
operator|.
name|delayer
argument_list|(
literal|1
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unbounded-100-endpoint"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"reactive-streams:bounded?maxInflightExchanges=10"
argument_list|)
operator|.
name|delayer
argument_list|(
literal|1
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bounded-endpoint"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"reactive-streams:bounded-0?maxInflightExchanges=10&exchangesRefillLowWatermark=0"
argument_list|)
operator|.
name|delayer
argument_list|(
literal|1
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bounded-0-endpoint"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"reactive-streams:bounded-10?maxInflightExchanges=10&exchangesRefillLowWatermark=0.1"
argument_list|)
operator|.
name|delayer
argument_list|(
literal|1
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bounded-10-endpoint"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"reactive-streams:bounded-80?maxInflightExchanges=10&exchangesRefillLowWatermark=0.8"
argument_list|)
operator|.
name|delayer
argument_list|(
literal|1
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bounded-80-endpoint"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"reactive-streams:bounded-100?maxInflightExchanges=10&exchangesRefillLowWatermark=1"
argument_list|)
operator|.
name|delayer
argument_list|(
literal|1
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bounded-100-endpoint"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

