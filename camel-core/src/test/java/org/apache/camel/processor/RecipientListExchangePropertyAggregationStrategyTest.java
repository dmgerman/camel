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
name|ArrayList
import|;
end_import

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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|RecipientListExchangePropertyAggregationStrategyTest
specifier|public
class|class
name|RecipientListExchangePropertyAggregationStrategyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|strategy
specifier|private
specifier|final
name|MyAggregationStrategy
name|strategy
init|=
operator|new
name|MyAggregationStrategy
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testRecipientExchangeProperty ()
specifier|public
name|void
name|testRecipientExchangeProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|RECIPIENT_LIST_ENDPOINT
argument_list|,
literal|"direct://a"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
literal|"mock://a"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|RECIPIENT_LIST_ENDPOINT
argument_list|,
literal|"direct://b"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
literal|"mock://b"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|RECIPIENT_LIST_ENDPOINT
argument_list|,
literal|"direct://c"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
literal|"mock://c"
argument_list|)
expr_stmt|;
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
name|expectedBodiesReceived
argument_list|(
literal|"Hello c"
argument_list|)
expr_stmt|;
comment|// would be the last one
name|mock
operator|.
name|expectedPropertyReceived
argument_list|(
name|Exchange
operator|.
name|RECIPIENT_LIST_ENDPOINT
argument_list|,
literal|"direct://c"
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"slip"
argument_list|,
literal|"direct:a,direct:b,direct:c"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello c"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|strategy
operator|.
name|getUris
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://a"
argument_list|,
name|strategy
operator|.
name|getUris
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://b"
argument_list|,
name|strategy
operator|.
name|getUris
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://c"
argument_list|,
name|strategy
operator|.
name|getUris
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|recipientList
argument_list|(
name|header
argument_list|(
literal|"slip"
argument_list|)
argument_list|)
operator|.
name|aggregationStrategy
argument_list|(
name|strategy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Hello a"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Hello b"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:c"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Hello c"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyAggregationStrategy
specifier|private
specifier|static
class|class
name|MyAggregationStrategy
implements|implements
name|AggregationStrategy
block|{
DECL|field|uris
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|uris
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
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
name|uris
operator|.
name|add
argument_list|(
name|newExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|RECIPIENT_LIST_ENDPOINT
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|newExchange
return|;
block|}
DECL|method|getUris ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getUris
parameter_list|()
block|{
return|return
name|uris
return|;
block|}
block|}
block|}
end_class

end_unit

