begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|AggregationStrategy
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
DECL|class|SplitWithCustomAggregationStrategyTest
specifier|public
class|class
name|SplitWithCustomAggregationStrategyTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testSplitWithCustomAggregatorStrategy ()
specifier|public
name|void
name|testSplitWithCustomAggregatorStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|files
init|=
literal|10
decl_stmt|;
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
name|files
argument_list|)
expr_stmt|;
comment|// no duplicates should be received
name|mock
operator|.
name|expectsNoDuplicates
argument_list|(
name|body
argument_list|()
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
name|files
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|""
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
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"<search><key>foo-${id}</key><key>bar-${id}</key><key>baz-${id}</key></search>"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:splitInOut"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:splitInOut"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"com.example.id"
argument_list|)
operator|.
name|simple
argument_list|(
literal|"${id}"
argument_list|)
operator|.
name|split
argument_list|(
name|xpath
argument_list|(
literal|"/search/key"
argument_list|)
argument_list|,
operator|new
name|AggregationStrategy
argument_list|()
block|{
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
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
return|return
name|newExchange
return|;
block|}
name|String
name|oldBody
init|=
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
decl_stmt|;
name|String
name|newBody
init|=
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
decl_stmt|;
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|oldBody
operator|+
name|newBody
argument_list|)
expr_stmt|;
return|return
name|oldExchange
return|;
block|}
block|}
argument_list|)
operator|.
name|parallelProcessing
argument_list|()
operator|.
name|streaming
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:processLine"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|transform
argument_list|()
operator|.
name|simple
argument_list|(
literal|"<results>${in.body}</results>"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:processLine"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:line"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|simple
argument_list|(
literal|"<index>${in.header.CamelSplitIndex}</index>${in.body}"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

