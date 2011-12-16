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
name|java
operator|.
name|util
operator|.
name|List
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
name|Navigate
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
name|Processor
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
name|aggregate
operator|.
name|AggregationStrategy
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SplitWithEndTest
specifier|public
class|class
name|SplitWithEndTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testRouteIsCorrectAtRuntime ()
specifier|public
name|void
name|testRouteIsCorrectAtRuntime
parameter_list|()
throws|throws
name|Exception
block|{
comment|// use navigate to find that the end works as expected
name|Navigate
argument_list|<
name|Processor
argument_list|>
name|nav
init|=
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|navigate
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Processor
argument_list|>
name|node
init|=
name|nav
operator|.
name|next
argument_list|()
decl_stmt|;
name|node
operator|=
operator|(
operator|(
name|Navigate
operator|)
name|node
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|next
argument_list|()
expr_stmt|;
name|node
operator|=
operator|(
operator|(
name|Navigate
operator|)
name|node
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|next
argument_list|()
expr_stmt|;
comment|// there should be 4 outputs as the end in the otherwise should
comment|// ensure that the transform and last send is not within the choice
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|node
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// the navigate API is a bit simple at this time of writing so it does take a little
comment|// bit of ugly code to find the correct processor in the runtime route
name|assertIsInstanceOf
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|unwrapChannel
argument_list|(
name|node
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|getNextProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|Splitter
operator|.
name|class
argument_list|,
name|unwrapChannel
argument_list|(
name|node
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getNextProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|TransformProcessor
operator|.
name|class
argument_list|,
name|unwrapChannel
argument_list|(
name|node
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|getNextProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|,
name|unwrapChannel
argument_list|(
name|node
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
operator|.
name|getNextProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSplit ()
specifier|public
name|void
name|testSplit
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:start"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello,World,Moon"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:last"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"last hi Hello@hi World@hi Moon"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello,World,Moon"
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
name|context
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|MySplitBean
name|bean
init|=
operator|new
name|MySplitBean
argument_list|()
decl_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:start"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|","
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
name|body
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
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
operator|+
literal|"@"
operator|+
name|newBody
argument_list|)
expr_stmt|;
return|return
name|newExchange
return|;
block|}
block|}
argument_list|)
operator|.
name|bean
argument_list|(
name|bean
argument_list|,
literal|"hi"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:split"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"last "
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:last"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MySplitBean
specifier|public
class|class
name|MySplitBean
block|{
DECL|method|hi (String s)
specifier|public
name|String
name|hi
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
literal|"hi "
operator|+
name|s
return|;
block|}
block|}
block|}
end_class

end_unit

