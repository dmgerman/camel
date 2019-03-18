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

begin_comment
comment|/**  * Unit test for PredicateAggregatorCollection.  */
end_comment

begin_class
DECL|class|PredicateAggregatorCollectionTest
specifier|public
class|class
name|PredicateAggregatorCollectionTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testPredicateAggregateCollection ()
specifier|public
name|void
name|testPredicateAggregateCollection
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e2
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
comment|// we only expect two messages as they have reached the completed predicate
comment|// that we want 3 messages that has the same header id
name|result
operator|.
name|expectedMessageCount
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
literal|"Message 3c"
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
literal|"Message 1d"
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
comment|// we use the collection based aggregator we already have configured
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
name|completionSize
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
return|;
block|}
block|}
end_class

end_unit

