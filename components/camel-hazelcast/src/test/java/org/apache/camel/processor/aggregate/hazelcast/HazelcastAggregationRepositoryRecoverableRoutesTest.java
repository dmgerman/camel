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
DECL|class|HazelcastAggregationRepositoryRecoverableRoutesTest
specifier|public
class|class
name|HazelcastAggregationRepositoryRecoverableRoutesTest
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
DECL|field|MOCK_FAILURE
specifier|private
specifier|static
specifier|final
name|String
name|MOCK_FAILURE
init|=
literal|"mock:failure"
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
name|uri
operator|=
name|MOCK_GOTCHA
argument_list|)
DECL|field|mockGotcha
specifier|private
name|MockEndpoint
name|mockGotcha
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
name|MOCK_FAILURE
argument_list|)
DECL|field|mockFailure
specifier|private
name|MockEndpoint
name|mockFailure
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
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
name|uri
operator|=
name|DIRECT_TWO
argument_list|)
DECL|field|produceTwo
specifier|private
name|ProducerTemplate
name|produceTwo
decl_stmt|;
annotation|@
name|Test
DECL|method|checkAggregationFromTwoRoutesWithRecovery ()
specifier|public
name|void
name|checkAggregationFromTwoRoutesWithRecovery
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
name|onException
argument_list|(
name|EverythingIsLostException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|useOriginalMessage
argument_list|()
operator|.
name|to
argument_list|(
name|MOCK_GOTCHA
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|interceptSendToEndpoint
argument_list|(
name|MOCK_FAILURE
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|EverythingIsLostException
argument_list|(
literal|"The field is lost... everything is lost"
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
name|DIRECT_ONE
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
name|MOCK_FAILURE
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
name|onException
argument_list|(
name|EverythingIsLostException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|useOriginalMessage
argument_list|()
operator|.
name|to
argument_list|(
name|MOCK_GOTCHA
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|interceptSendToEndpoint
argument_list|(
name|MOCK_FAILURE
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|EverythingIsLostException
argument_list|(
literal|"The field is lost... everything is lost"
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
name|DIRECT_TWO
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
name|MOCK_FAILURE
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
name|mockFailure
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mockGotcha
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockGotcha
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
literal|4
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
name|produceTwo
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
name|mockFailure
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mockFailure
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|class|EverythingIsLostException
specifier|private
specifier|static
specifier|final
class|class
name|EverythingIsLostException
extends|extends
name|Exception
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|EverythingIsLostException ()
specifier|private
name|EverythingIsLostException
parameter_list|()
block|{         }
DECL|method|EverythingIsLostException (String message)
specifier|private
name|EverythingIsLostException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|EverythingIsLostException (String message, Throwable cause)
specifier|private
name|EverythingIsLostException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|method|EverythingIsLostException (Throwable cause)
specifier|private
name|EverythingIsLostException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
comment|// not in jdk6
comment|//        private EverythingIsLostException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
comment|//            super(message, cause, enableSuppression, writableStackTrace);
comment|//        }
block|}
block|}
end_class

end_unit

