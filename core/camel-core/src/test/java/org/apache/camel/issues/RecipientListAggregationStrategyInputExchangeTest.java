begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|issues
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
name|AggregationStrategy
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|RecipientListAggregationStrategyInputExchangeTest
specifier|public
class|class
name|RecipientListAggregationStrategyInputExchangeTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testInputExchange ()
specifier|public
name|void
name|testInputExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:start"
argument_list|,
name|p
lambda|->
name|p
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|out
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Forced"
argument_list|,
name|out
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"FailedDue"
argument_list|)
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
name|recipientList
argument_list|(
name|constant
argument_list|(
literal|"direct:a,direct:b"
argument_list|)
argument_list|)
operator|.
name|aggregationStrategy
argument_list|(
operator|new
name|MyAggregateBean
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
name|constant
argument_list|(
literal|"123"
argument_list|)
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"A"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
name|constant
argument_list|(
literal|"456"
argument_list|)
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"B"
argument_list|)
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Forced"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyAggregateBean
specifier|public
specifier|static
class|class
name|MyAggregateBean
implements|implements
name|AggregationStrategy
block|{
annotation|@
name|Override
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
comment|// NOT in use
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange, Exchange inputExchange)
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|,
name|Exchange
name|inputExchange
parameter_list|)
block|{
if|if
condition|(
name|newExchange
operator|.
name|isFailed
argument_list|()
condition|)
block|{
name|inputExchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"FailedDue"
argument_list|,
name|newExchange
operator|.
name|getException
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|inputExchange
return|;
block|}
comment|// dont care so much about merging in this unit test
return|return
name|newExchange
return|;
block|}
block|}
block|}
end_class

end_unit

