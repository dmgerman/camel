begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|HashMap
import|;
end_import

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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|RemoveHeadersTest
specifier|public
class|class
name|RemoveHeadersTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testRemoveHeadersWildcard ()
specifier|public
name|void
name|testRemoveHeadersWildcard
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"duck"
argument_list|,
literal|"Donald"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"dudeCool"
argument_list|,
literal|"cool"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"DudeWicket"
argument_list|,
literal|"wicket"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"DUDEbig"
argument_list|,
literal|"upper"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"duck"
argument_list|,
literal|"Donald"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveHeadersRegEx ()
specifier|public
name|void
name|testRemoveHeadersRegEx
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"duck"
argument_list|,
literal|"Donald"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"BeerHeineken"
argument_list|,
literal|"Good"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"dudeCool"
argument_list|,
literal|"cool"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"DudeWicket"
argument_list|,
literal|"wicket"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"duck"
argument_list|,
literal|"Donald"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"DUDEbig"
argument_list|,
literal|"upper"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"BeerCarlsberg"
argument_list|,
literal|"Great"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"BeerTuborg"
argument_list|,
literal|"Also Great"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"BeerHeineken"
argument_list|,
literal|"Good"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveHeadersCaseInsensitive ()
specifier|public
name|void
name|testRemoveHeadersCaseInsensitive
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"duck"
argument_list|,
literal|"Donald"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"BeerHeineken"
argument_list|,
literal|"Good"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"dudeCool"
argument_list|,
literal|"cool"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"DudeWicket"
argument_list|,
literal|"wicket"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"DUDEbig"
argument_list|,
literal|"upper"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"duck"
argument_list|,
literal|"Donald"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Beercarlsberg"
argument_list|,
literal|"Great"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"BeerTUBORG"
argument_list|,
literal|"Also Great"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"BeerHeineken"
argument_list|,
literal|"Good"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"FOO"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
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
name|removeHeaders
argument_list|(
literal|"dude*"
argument_list|)
operator|.
name|removeHeaders
argument_list|(
literal|"Beer(Carlsberg|Tuborg)"
argument_list|)
operator|.
name|removeHeaders
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

