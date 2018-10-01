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
name|CamelExecutionException
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
name|ClosedCorrelationKeyException
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
DECL|class|AggregateClosedCorrelationKeyTest
specifier|public
class|class
name|AggregateClosedCorrelationKeyTest
extends|extends
name|ContextTestSupport
block|{
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
annotation|@
name|Test
DECL|method|testAggregateClosedCorrelationKey ()
specifier|public
name|void
name|testAggregateClosedCorrelationKey
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|BodyInAggregatingStrategy
argument_list|()
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|2
argument_list|)
operator|.
name|closeCorrelationKeyOnCompletion
argument_list|(
literal|1000
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A+B"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A"
argument_list|,
literal|"id"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"B"
argument_list|,
literal|"id"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
comment|// should be closed
try|try
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"C"
argument_list|,
literal|"id"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|ClosedCorrelationKeyException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|ClosedCorrelationKeyException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|cause
operator|.
name|getCorrelationKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"The correlation key [1] has been closed."
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAggregateClosedCorrelationKeyCache ()
specifier|public
name|void
name|testAggregateClosedCorrelationKeyCache
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
name|BodyInAggregatingStrategy
argument_list|()
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|2
argument_list|)
operator|.
name|closeCorrelationKeyOnCompletion
argument_list|(
literal|2
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A+B"
argument_list|,
literal|"C+D"
argument_list|,
literal|"E+F"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A"
argument_list|,
literal|"id"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"B"
argument_list|,
literal|"id"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"C"
argument_list|,
literal|"id"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"D"
argument_list|,
literal|"id"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"E"
argument_list|,
literal|"id"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"F"
argument_list|,
literal|"id"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
comment|// 2 of them should now be closed
name|int
name|closed
init|=
literal|0
decl_stmt|;
comment|// should NOT be closed because only 2 and 3 is remembered as they are the two last used
try|try
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"G"
argument_list|,
literal|"id"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|closed
operator|++
expr_stmt|;
name|ClosedCorrelationKeyException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|ClosedCorrelationKeyException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|cause
operator|.
name|getCorrelationKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"The correlation key [1] has been closed."
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// should be closed
try|try
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"H"
argument_list|,
literal|"id"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|closed
operator|++
expr_stmt|;
name|ClosedCorrelationKeyException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|ClosedCorrelationKeyException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"2"
argument_list|,
name|cause
operator|.
name|getCorrelationKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"The correlation key [2] has been closed."
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// should be closed
try|try
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"I"
argument_list|,
literal|"id"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|closed
operator|++
expr_stmt|;
name|ClosedCorrelationKeyException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|ClosedCorrelationKeyException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"3"
argument_list|,
name|cause
operator|.
name|getCorrelationKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"The correlation key [3] has been closed."
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"There should be 2 closed"
argument_list|,
literal|2
argument_list|,
name|closed
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

