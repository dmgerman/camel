begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
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
comment|/**  * Based on end user on forum how to get the 404 error code in his enrich aggregator  */
end_comment

begin_class
DECL|class|JettySimplifiedHandle404Test
specifier|public
class|class
name|JettySimplifiedHandle404Test
extends|extends
name|BaseJettyTest
block|{
annotation|@
name|Test
DECL|method|testSimulate404 ()
specifier|public
name|void
name|testSimulate404
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"Page not found"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
literal|404
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
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
comment|// disable error handling
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
comment|// START SNIPPET: e1
comment|// We set throwExceptionOnFailure to false to let Camel return any response from the remove HTTP server without thrown
comment|// HttpOperationFailedException in case of failures.
comment|// This allows us to handle all responses in the aggregation strategy where we can check the HTTP response code
comment|// and decide what to do. As this is based on an unit test we assert the code is 404
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|enrich
argument_list|(
literal|"http://localhost:{{port}}/myserver?throwExceptionOnFailure=false&user=Camel"
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
name|original
parameter_list|,
name|Exchange
name|resource
parameter_list|)
block|{
comment|// get the response code
name|Integer
name|code
init|=
name|resource
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|404
argument_list|,
name|code
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|resource
return|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// this is our jetty server where we simulate the 404
name|from
argument_list|(
literal|"jetty://http://localhost:{{port}}/myserver"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Page not found"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
literal|404
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

