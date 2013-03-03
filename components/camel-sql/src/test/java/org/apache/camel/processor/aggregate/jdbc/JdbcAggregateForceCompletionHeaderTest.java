begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.jdbc
package|package
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
name|jdbc
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * To test CAMEL-4118 support for completing all aggregation groups with a signal message  */
end_comment

begin_class
DECL|class|JdbcAggregateForceCompletionHeaderTest
specifier|public
class|class
name|JdbcAggregateForceCompletionHeaderTest
extends|extends
name|AbstractJdbcAggregationTestSupport
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
literal|"forceCompletion"
argument_list|)
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
literal|"forceCompletion"
argument_list|)
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
comment|// here is the Camel route where we aggregate
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
comment|// use our created jdbc repo as aggregation repository
operator|.
name|completionSize
argument_list|(
literal|10
argument_list|)
operator|.
name|aggregationRepository
argument_list|(
name|repo
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
block|}
end_class

end_unit

