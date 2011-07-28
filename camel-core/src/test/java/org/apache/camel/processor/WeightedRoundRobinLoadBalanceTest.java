begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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

begin_class
DECL|class|WeightedRoundRobinLoadBalanceTest
specifier|public
class|class
name|WeightedRoundRobinLoadBalanceTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|x
specifier|protected
name|MockEndpoint
name|x
decl_stmt|;
DECL|field|y
specifier|protected
name|MockEndpoint
name|y
decl_stmt|;
DECL|field|z
specifier|protected
name|MockEndpoint
name|z
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|x
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:x"
argument_list|)
expr_stmt|;
name|y
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:y"
argument_list|)
expr_stmt|;
name|z
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:z"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|testRoundRobin ()
specifier|public
name|void
name|testRoundRobin
parameter_list|()
throws|throws
name|Exception
block|{
name|x
operator|.
name|expectedMessageCount
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|y
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|z
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// START SNIPPET: example
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|loadBalance
argument_list|()
operator|.
name|weighted
argument_list|(
literal|true
argument_list|,
literal|"4,2,1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:x"
argument_list|,
literal|"mock:y"
argument_list|,
literal|"mock:z"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|sendMessages
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|x
operator|.
name|expectedBodiesReceived
argument_list|(
literal|1
argument_list|,
literal|4
argument_list|,
literal|6
argument_list|,
literal|7
argument_list|,
literal|8
argument_list|)
expr_stmt|;
name|y
operator|.
name|expectedBodiesReceived
argument_list|(
literal|2
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|z
operator|.
name|expectedBodiesReceived
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
DECL|method|testRoundRobin2 ()
specifier|public
name|void
name|testRoundRobin2
parameter_list|()
throws|throws
name|Exception
block|{
name|x
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|y
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|z
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// START SNIPPET: example
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|loadBalance
argument_list|()
operator|.
name|weighted
argument_list|(
literal|true
argument_list|,
literal|"2, 1, 3"
argument_list|,
literal|","
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:x"
argument_list|,
literal|"mock:y"
argument_list|,
literal|"mock:z"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|sendMessages
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|x
operator|.
name|expectedBodiesReceived
argument_list|(
literal|1
argument_list|,
literal|4
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|y
operator|.
name|expectedBodiesReceived
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|z
operator|.
name|expectedBodiesReceived
argument_list|(
literal|3
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|)
expr_stmt|;
block|}
DECL|method|testRoundRobinBulk ()
specifier|public
name|void
name|testRoundRobinBulk
parameter_list|()
throws|throws
name|Exception
block|{
name|x
operator|.
name|expectedMessageCount
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|y
operator|.
name|expectedMessageCount
argument_list|(
literal|15
argument_list|)
expr_stmt|;
name|z
operator|.
name|expectedMessageCount
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// START SNIPPET: example
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|loadBalance
argument_list|()
operator|.
name|weighted
argument_list|(
literal|true
argument_list|,
literal|"2-3-5"
argument_list|,
literal|"-"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:x"
argument_list|,
literal|"mock:y"
argument_list|,
literal|"mock:z"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|sendBulkMessages
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testUnmatchedRatiosToProcessors ()
specifier|public
name|void
name|testUnmatchedRatiosToProcessors
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|error
init|=
literal|false
decl_stmt|;
try|try
block|{
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// START SNIPPET: example
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|loadBalance
argument_list|()
operator|.
name|weighted
argument_list|(
literal|true
argument_list|,
literal|"2,3"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:x"
argument_list|,
literal|"mock:y"
argument_list|,
literal|"mock:z"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: example
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Loadbalacing with 3 should match number of distributions 2"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|error
operator|=
literal|true
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
DECL|method|sendBulkMessages (int number)
specifier|protected
name|void
name|sendBulkMessages
parameter_list|(
name|int
name|number
parameter_list|)
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
name|number
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|createTestMessage
argument_list|(
name|i
argument_list|)
argument_list|,
literal|"counter"
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|sendMessages (int... counters)
specifier|protected
name|void
name|sendMessages
parameter_list|(
name|int
modifier|...
name|counters
parameter_list|)
block|{
for|for
control|(
name|int
name|counter
range|:
name|counters
control|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|createTestMessage
argument_list|(
name|counter
argument_list|)
argument_list|,
literal|"counter"
argument_list|,
name|counter
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createTestMessage (int counter)
specifier|private
name|String
name|createTestMessage
parameter_list|(
name|int
name|counter
parameter_list|)
block|{
return|return
literal|"<message>"
operator|+
name|counter
operator|+
literal|"</message>"
return|;
block|}
block|}
end_class

end_unit

