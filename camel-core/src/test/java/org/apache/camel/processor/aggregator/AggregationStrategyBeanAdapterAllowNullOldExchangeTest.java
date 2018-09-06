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
name|aggregate
operator|.
name|AggregationStrategyBeanAdapter
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
DECL|class|AggregationStrategyBeanAdapterAllowNullOldExchangeTest
specifier|public
class|class
name|AggregationStrategyBeanAdapterAllowNullOldExchangeTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|appender
specifier|private
name|MyBodyAppender
name|appender
init|=
operator|new
name|MyBodyAppender
argument_list|()
decl_stmt|;
DECL|field|myStrategy
specifier|private
name|AggregationStrategyBeanAdapter
name|myStrategy
decl_stmt|;
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
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"OldWasNullABC"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"C"
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
name|myStrategy
operator|=
operator|new
name|AggregationStrategyBeanAdapter
argument_list|(
name|appender
argument_list|,
literal|"append"
argument_list|)
expr_stmt|;
name|myStrategy
operator|.
name|setAllowNullOldExchange
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|constant
argument_list|(
literal|true
argument_list|)
argument_list|,
name|myStrategy
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
block|}
block|}
return|;
block|}
DECL|class|MyBodyAppender
specifier|public
specifier|static
specifier|final
class|class
name|MyBodyAppender
block|{
DECL|method|append (String existing, String next)
specifier|public
name|String
name|append
parameter_list|(
name|String
name|existing
parameter_list|,
name|String
name|next
parameter_list|)
block|{
if|if
condition|(
name|existing
operator|==
literal|null
condition|)
block|{
return|return
literal|"OldWasNull"
operator|+
name|next
return|;
block|}
if|if
condition|(
name|next
operator|!=
literal|null
condition|)
block|{
return|return
name|existing
operator|+
name|next
return|;
block|}
else|else
block|{
return|return
name|existing
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

