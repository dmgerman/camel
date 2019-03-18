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
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
DECL|class|AggregateDslTest
specifier|public
class|class
name|AggregateDslTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testAggregate ()
specifier|public
name|void
name|testAggregate
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"0,3"
argument_list|,
literal|"1,4"
argument_list|,
literal|"2,5"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:aggregated-supplier"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"0,3,6"
argument_list|,
literal|"1,4,7"
argument_list|,
literal|"2,5,8"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|9
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
name|i
argument_list|,
literal|"type"
argument_list|,
name|i
operator|%
literal|3
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start-supplier"
argument_list|,
name|i
argument_list|,
literal|"type"
argument_list|,
name|i
operator|%
literal|3
argument_list|)
expr_stmt|;
block|}
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
name|aggregate
argument_list|()
operator|.
name|message
argument_list|(
name|m
lambda|->
name|m
operator|.
name|getHeader
argument_list|(
literal|"type"
argument_list|)
argument_list|)
operator|.
name|strategy
argument_list|()
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|AggregateDslTest
operator|::
name|joinString
argument_list|)
operator|.
name|completion
argument_list|()
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|s
lambda|->
name|s
operator|.
name|split
argument_list|(
literal|","
argument_list|)
operator|.
name|length
operator|==
literal|2
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:aggregated"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start-supplier"
argument_list|)
operator|.
name|aggregate
argument_list|()
operator|.
name|header
argument_list|(
literal|"type"
argument_list|)
operator|.
name|strategy
argument_list|(
name|AggregateDslTest
operator|::
name|joinStringStrategy
argument_list|)
operator|.
name|completion
argument_list|()
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|s
lambda|->
name|s
operator|.
name|split
argument_list|(
literal|","
argument_list|)
operator|.
name|length
operator|==
literal|3
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:aggregated-supplier"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// *************************************************************************
comment|// Strategies
comment|// *************************************************************************
DECL|method|joinString (String o, String n)
specifier|private
specifier|static
name|String
name|joinString
parameter_list|(
name|String
name|o
parameter_list|,
name|String
name|n
parameter_list|)
block|{
return|return
name|Stream
operator|.
name|of
argument_list|(
name|o
argument_list|,
name|n
argument_list|)
operator|.
name|filter
argument_list|(
name|Objects
operator|::
name|nonNull
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|","
argument_list|)
argument_list|)
return|;
block|}
DECL|method|joinStringStrategy (Exchange oldExchange, Exchange newExchange)
specifier|private
specifier|static
name|Exchange
name|joinStringStrategy
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|joinString
argument_list|(
name|oldExchange
operator|!=
literal|null
condition|?
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
else|:
literal|null
argument_list|,
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|newExchange
return|;
block|}
block|}
end_class

end_unit

