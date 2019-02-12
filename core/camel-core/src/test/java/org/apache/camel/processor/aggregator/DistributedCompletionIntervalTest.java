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
name|MemoryAggregationRepository
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
name|UseLatestAggregationStrategy
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
comment|/**  * Unit test to verify that aggregate by interval only also works.  */
end_comment

begin_class
DECL|class|DistributedCompletionIntervalTest
specifier|public
class|class
name|DistributedCompletionIntervalTest
extends|extends
name|AbstractDistributedTest
block|{
DECL|field|sharedAggregationRepository
specifier|private
name|MemoryAggregationRepository
name|sharedAggregationRepository
init|=
operator|new
name|MemoryAggregationRepository
argument_list|(
literal|true
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testCamelContext1Wins ()
specifier|public
name|void
name|testCamelContext1Wins
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
literal|"Message 19"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock2
init|=
name|getMockEndpoint2
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock2
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// ensure messages are send after the 1s
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|sendMessages
argument_list|()
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mock2
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCamelContext2Wins ()
specifier|public
name|void
name|testCamelContext2Wins
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
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock2
init|=
name|getMockEndpoint2
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock2
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Message 19"
argument_list|)
expr_stmt|;
comment|// ensure messages are send after the 1s
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|sendMessages
argument_list|()
expr_stmt|;
name|mock2
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|sendMessages ()
specifier|private
name|void
name|sendMessages
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|20
condition|;
name|i
operator|++
control|)
block|{
name|int
name|choice
init|=
name|i
operator|%
literal|2
decl_stmt|;
if|if
condition|(
name|choice
operator|==
literal|0
condition|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template2
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|// START SNIPPET: e1
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
name|UseLatestAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|aggregationRepository
argument_list|(
name|sharedAggregationRepository
argument_list|)
operator|.
name|optimisticLocking
argument_list|()
comment|// trigger completion every 5th second
operator|.
name|completionInterval
argument_list|(
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"testCamelContext1Wins"
argument_list|)
condition|?
literal|5000
else|:
literal|10000
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
annotation|@
name|Override
DECL|method|createRouteBuilder2 ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder2
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
comment|// START SNIPPET: e1
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
name|UseLatestAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|aggregationRepository
argument_list|(
name|sharedAggregationRepository
argument_list|)
operator|.
name|optimisticLocking
argument_list|()
comment|// trigger completion every 5th second
operator|.
name|completionInterval
argument_list|(
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"testCamelContext1Wins"
argument_list|)
condition|?
literal|10000
else|:
literal|5000
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
