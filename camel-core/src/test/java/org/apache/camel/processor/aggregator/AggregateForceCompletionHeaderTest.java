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
name|processor
operator|.
name|aggregate
operator|.
name|AggregationStrategy
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
comment|/**  * To test CAMEL-4118 support for completing all aggregation groups with a signal message  */
end_comment

begin_class
DECL|class|AggregateForceCompletionHeaderTest
specifier|public
class|class
name|AggregateForceCompletionHeaderTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testForceCompletionTrueExclusive ()
specifier|public
name|void
name|testForceCompletionTrueExclusive
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
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
literal|"test1"
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
literal|"test2"
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
literal|"test3"
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
literal|"test4"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"test1test3"
argument_list|,
literal|"test2test4"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COMPLETED_BY
argument_list|,
literal|"force"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|allMessages
argument_list|()
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|AGGREGATION_COMPLETE_ALL_GROUPS
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
comment|//now send the signal message to trigger completion of all groups, message should NOT be aggregated
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"test5"
argument_list|,
name|Exchange
operator|.
name|AGGREGATION_COMPLETE_ALL_GROUPS
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testForceCompletionTrueInclusive ()
specifier|public
name|void
name|testForceCompletionTrueInclusive
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
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
literal|"test1"
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
literal|"test2"
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
literal|"test3"
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
literal|"test4"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"test1test3"
argument_list|,
literal|"test2test4"
argument_list|,
literal|"test5"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COMPLETED_BY
argument_list|,
literal|"force"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|allMessages
argument_list|()
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|AGGREGATION_COMPLETE_ALL_GROUPS_INCLUSIVE
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
comment|//now send a message to trigger completion of all groups, message should be aggregated
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
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"id"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|AGGREGATION_COMPLETE_ALL_GROUPS_INCLUSIVE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"test5"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
name|completionSize
argument_list|(
literal|10
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
specifier|public
specifier|static
class|class
name|MyAggregationStrategy
implements|implements
name|AggregationStrategy
block|{
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
name|body1
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
name|String
name|body2
init|=
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
decl_stmt|;
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body1
operator|+
name|body2
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

