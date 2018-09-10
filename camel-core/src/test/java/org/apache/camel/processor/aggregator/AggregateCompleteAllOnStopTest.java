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
name|aggregate
operator|.
name|MemoryAggregationRepository
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
comment|/**  * @version  */
end_comment

begin_class
DECL|class|AggregateCompleteAllOnStopTest
specifier|public
class|class
name|AggregateCompleteAllOnStopTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testCompleteAllOnStop ()
specifier|public
name|void
name|testCompleteAllOnStop
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
name|expectedBodiesReceived
argument_list|(
literal|"A+B"
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
comment|// we only send 3, but we get 2 exchanges completed when stopping
comment|// as we tell it to complete all on stop
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"seda:start"
argument_list|,
literal|"A"
argument_list|,
literal|"id"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"seda:start"
argument_list|,
literal|"B"
argument_list|,
literal|"id"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"seda:start"
argument_list|,
literal|"C"
argument_list|,
literal|"id"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"foo"
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
literal|"seda:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
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
name|BodyInAggregatingStrategy
argument_list|()
argument_list|)
operator|.
name|aggregationRepository
argument_list|(
operator|new
name|MemoryAggregationRepository
argument_list|()
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|2
argument_list|)
operator|.
name|completionTimeout
argument_list|(
literal|100
argument_list|)
operator|.
name|completeAllOnStop
argument_list|()
operator|.
name|completionTimeoutCheckerInterval
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
block|}
end_class

end_unit

