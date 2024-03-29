begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_class
DECL|class|AggregatorTest
specifier|public
class|class
name|AggregatorTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|messageCount
specifier|protected
name|int
name|messageCount
init|=
literal|100
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendingLotsOfMessagesGetAggregatedToTheLatestMessage ()
specifier|public
name|void
name|testSendingLotsOfMessagesGetAggregatedToTheLatestMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"message:"
operator|+
name|messageCount
argument_list|)
expr_stmt|;
comment|// lets send a large batch of messages
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|messageCount
condition|;
name|i
operator|++
control|)
block|{
name|String
name|body
init|=
literal|"message:"
operator|+
name|i
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|body
argument_list|,
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
block|}
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOneMessage ()
specifier|public
name|void
name|testOneMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
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
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|"viper bar"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:predicate"
argument_list|,
literal|"test"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBatchTimeoutExpiry ()
specifier|public
name|void
name|testBatchTimeoutExpiry
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"message:1"
argument_list|,
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAggregatorNotAtStart ()
specifier|public
name|void
name|testAggregatorNotAtStart
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"visited"
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"seda:header"
argument_list|,
literal|"message:1"
argument_list|,
literal|"cheese"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
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
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// START SNIPPET: ex
comment|// in this route we aggregate all from direct:state based on the
comment|// header id cheese
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
literal|"cheese"
argument_list|)
argument_list|,
operator|new
name|UseLatestAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|completionTimeout
argument_list|(
literal|100
argument_list|)
operator|.
name|completionTimeoutCheckerInterval
argument_list|(
literal|10
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:header"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"visited"
argument_list|,
name|constant
argument_list|(
literal|true
argument_list|)
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
literal|"cheese"
argument_list|)
argument_list|,
operator|new
name|UseLatestAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|completionTimeout
argument_list|(
literal|100
argument_list|)
operator|.
name|completionTimeoutCheckerInterval
argument_list|(
literal|10
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// in this sample we aggregate with a completion predicate
name|from
argument_list|(
literal|"direct:predicate"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
literal|"cheese"
argument_list|)
argument_list|,
operator|new
name|UseLatestAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|completionTimeout
argument_list|(
literal|100
argument_list|)
operator|.
name|completionTimeoutCheckerInterval
argument_list|(
literal|10
argument_list|)
operator|.
name|completionPredicate
argument_list|(
name|header
argument_list|(
literal|"cheese"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|123
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: ex
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

