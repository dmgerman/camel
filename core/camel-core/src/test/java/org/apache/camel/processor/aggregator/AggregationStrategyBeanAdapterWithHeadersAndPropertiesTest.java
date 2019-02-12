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
name|AggregationStrategies
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
DECL|class|AggregationStrategyBeanAdapterWithHeadersAndPropertiesTest
specifier|public
class|class
name|AggregationStrategyBeanAdapterWithHeadersAndPropertiesTest
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
literal|"ABC"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"foo"
argument_list|,
literal|"yesyesyes"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedPropertyReceived
argument_list|(
literal|"count"
argument_list|,
literal|6
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndProperty
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A"
argument_list|,
literal|"count"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndProperty
argument_list|(
literal|"direct:start"
argument_list|,
literal|"B"
argument_list|,
literal|"count"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndProperty
argument_list|(
literal|"direct:start"
argument_list|,
literal|"C"
argument_list|,
literal|"count"
argument_list|,
literal|3
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
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
name|constant
argument_list|(
literal|"yes"
argument_list|)
argument_list|)
operator|.
name|aggregate
argument_list|(
name|constant
argument_list|(
literal|true
argument_list|)
argument_list|,
name|AggregationStrategies
operator|.
name|bean
argument_list|(
name|appender
argument_list|,
literal|"appendWithHeadersAndProperties"
argument_list|)
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
DECL|method|appendWithHeadersAndProperties (String existing, Map<String, String> oldHeaders, Map<String, Integer> oldProperties, String next, Map<String, String> newHeaders, Map<String, Integer> newProperties)
specifier|public
name|String
name|appendWithHeadersAndProperties
parameter_list|(
name|String
name|existing
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|oldHeaders
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|oldProperties
parameter_list|,
name|String
name|next
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|newHeaders
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|newProperties
parameter_list|)
block|{
if|if
condition|(
name|next
operator|!=
literal|null
condition|)
block|{
name|Integer
name|count
init|=
name|oldProperties
operator|.
name|get
argument_list|(
literal|"count"
argument_list|)
operator|+
name|newProperties
operator|.
name|get
argument_list|(
literal|"count"
argument_list|)
decl_stmt|;
name|oldProperties
operator|.
name|put
argument_list|(
literal|"count"
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|String
name|foo
init|=
name|oldHeaders
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
operator|+
name|newHeaders
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|oldHeaders
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
name|foo
argument_list|)
expr_stmt|;
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
