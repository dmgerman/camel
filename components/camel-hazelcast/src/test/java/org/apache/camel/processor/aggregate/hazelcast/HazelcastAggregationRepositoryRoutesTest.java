begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.hazelcast
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
name|hazelcast
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
name|EndpointInject
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
name|Produce
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|HazelcastAggregationRepositoryRoutesTest
specifier|public
class|class
name|HazelcastAggregationRepositoryRoutesTest
extends|extends
name|HazelcastAggregationRepositoryCamelTestSupport
block|{
DECL|field|REPO_NAME
specifier|private
specifier|static
specifier|final
name|String
name|REPO_NAME
init|=
literal|"routeTestRepo"
decl_stmt|;
DECL|field|MOCK_GOTCHA
specifier|private
specifier|static
specifier|final
name|String
name|MOCK_GOTCHA
init|=
literal|"mock:gotcha"
decl_stmt|;
DECL|field|DIRECT_ONE
specifier|private
specifier|static
specifier|final
name|String
name|DIRECT_ONE
init|=
literal|"direct:one"
decl_stmt|;
DECL|field|DIRECT_TWO
specifier|private
specifier|static
specifier|final
name|String
name|DIRECT_TWO
init|=
literal|"direct:two"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|MOCK_GOTCHA
argument_list|)
DECL|field|mock
specifier|private
name|MockEndpoint
name|mock
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|DIRECT_ONE
argument_list|)
DECL|field|produceOne
specifier|private
name|ProducerTemplate
name|produceOne
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|DIRECT_TWO
argument_list|)
DECL|field|produceTwo
specifier|private
name|ProducerTemplate
name|produceTwo
decl_stmt|;
annotation|@
name|Test
DECL|method|checkAggregationFromTwoRoutes ()
specifier|public
name|void
name|checkAggregationFromTwoRoutes
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|HazelcastAggregationRepository
name|repoOne
init|=
operator|new
name|HazelcastAggregationRepository
argument_list|(
name|REPO_NAME
argument_list|,
literal|false
argument_list|,
name|getFirstInstance
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|HazelcastAggregationRepository
name|repoTwo
init|=
operator|new
name|HazelcastAggregationRepository
argument_list|(
name|REPO_NAME
argument_list|,
literal|false
argument_list|,
name|getSecondInstance
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|int
name|completionSize
init|=
literal|4
decl_stmt|;
specifier|final
name|String
name|correlator
init|=
literal|"CORRELATOR"
decl_stmt|;
name|RouteBuilder
name|rbOne
init|=
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
name|DIRECT_ONE
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"AggregatingRouteOne"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
name|correlator
argument_list|)
argument_list|)
operator|.
name|aggregationRepository
argument_list|(
name|repoOne
argument_list|)
operator|.
name|aggregationStrategy
argument_list|(
operator|new
name|SumOfIntsAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|completionSize
argument_list|(
name|completionSize
argument_list|)
operator|.
name|to
argument_list|(
name|MOCK_GOTCHA
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|RouteBuilder
name|rbTwo
init|=
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
name|DIRECT_TWO
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"AggregatingRouteTwo"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
name|correlator
argument_list|)
argument_list|)
operator|.
name|aggregationRepository
argument_list|(
name|repoTwo
argument_list|)
operator|.
name|aggregationStrategy
argument_list|(
operator|new
name|SumOfIntsAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|completionSize
argument_list|(
name|completionSize
argument_list|)
operator|.
name|to
argument_list|(
name|MOCK_GOTCHA
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|context
argument_list|()
operator|.
name|addRoutes
argument_list|(
name|rbOne
argument_list|)
expr_stmt|;
name|context
argument_list|()
operator|.
name|addRoutes
argument_list|(
name|rbTwo
argument_list|)
expr_stmt|;
name|context
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|1
operator|+
literal|2
operator|+
literal|3
operator|+
literal|4
argument_list|)
expr_stmt|;
name|produceOne
operator|.
name|sendBodyAndHeader
argument_list|(
literal|1
argument_list|,
name|correlator
argument_list|,
name|correlator
argument_list|)
expr_stmt|;
name|produceTwo
operator|.
name|sendBodyAndHeader
argument_list|(
literal|2
argument_list|,
name|correlator
argument_list|,
name|correlator
argument_list|)
expr_stmt|;
name|produceOne
operator|.
name|sendBodyAndHeader
argument_list|(
literal|3
argument_list|,
name|correlator
argument_list|,
name|correlator
argument_list|)
expr_stmt|;
name|produceOne
operator|.
name|sendBodyAndHeader
argument_list|(
literal|4
argument_list|,
name|correlator
argument_list|,
name|correlator
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|checkAggregationFromTwoRoutesNoRecovery ()
specifier|public
name|void
name|checkAggregationFromTwoRoutesNoRecovery
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|HazelcastAggregationRepository
name|repoOne
init|=
operator|new
name|HazelcastAggregationRepository
argument_list|(
name|REPO_NAME
argument_list|,
literal|false
argument_list|,
name|getFirstInstance
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|HazelcastAggregationRepository
name|repoTwo
init|=
operator|new
name|HazelcastAggregationRepository
argument_list|(
name|REPO_NAME
argument_list|,
literal|false
argument_list|,
name|getSecondInstance
argument_list|()
argument_list|)
decl_stmt|;
name|repoOne
operator|.
name|setUseRecovery
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|repoTwo
operator|.
name|setUseRecovery
argument_list|(
literal|false
argument_list|)
expr_stmt|;
specifier|final
name|int
name|completionSize
init|=
literal|4
decl_stmt|;
specifier|final
name|String
name|correlator
init|=
literal|"CORRELATOR"
decl_stmt|;
name|RouteBuilder
name|rbOne
init|=
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
name|DIRECT_ONE
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"AggregatingRouteOne"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
name|correlator
argument_list|)
argument_list|)
operator|.
name|aggregationRepository
argument_list|(
name|repoOne
argument_list|)
operator|.
name|aggregationStrategy
argument_list|(
operator|new
name|SumOfIntsAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|completionSize
argument_list|(
name|completionSize
argument_list|)
operator|.
name|to
argument_list|(
name|MOCK_GOTCHA
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|RouteBuilder
name|rbTwo
init|=
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
name|DIRECT_TWO
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"AggregatingRouteTwo"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
name|correlator
argument_list|)
argument_list|)
operator|.
name|aggregationRepository
argument_list|(
name|repoTwo
argument_list|)
operator|.
name|aggregationStrategy
argument_list|(
operator|new
name|SumOfIntsAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|completionSize
argument_list|(
name|completionSize
argument_list|)
operator|.
name|to
argument_list|(
name|MOCK_GOTCHA
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|context
argument_list|()
operator|.
name|addRoutes
argument_list|(
name|rbOne
argument_list|)
expr_stmt|;
name|context
argument_list|()
operator|.
name|addRoutes
argument_list|(
name|rbTwo
argument_list|)
expr_stmt|;
name|context
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|1
operator|+
literal|2
operator|+
literal|3
operator|+
literal|4
argument_list|)
expr_stmt|;
name|produceOne
operator|.
name|sendBodyAndHeader
argument_list|(
literal|1
argument_list|,
name|correlator
argument_list|,
name|correlator
argument_list|)
expr_stmt|;
name|produceTwo
operator|.
name|sendBodyAndHeader
argument_list|(
literal|2
argument_list|,
name|correlator
argument_list|,
name|correlator
argument_list|)
expr_stmt|;
name|produceOne
operator|.
name|sendBodyAndHeader
argument_list|(
literal|3
argument_list|,
name|correlator
argument_list|,
name|correlator
argument_list|)
expr_stmt|;
name|produceOne
operator|.
name|sendBodyAndHeader
argument_list|(
literal|4
argument_list|,
name|correlator
argument_list|,
name|correlator
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

