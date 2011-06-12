begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
package|;
end_package

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|AsyncHttpClientConfig
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|AhcComponentClientConfigTest
specifier|public
class|class
name|AhcComponentClientConfigTest
extends|extends
name|BaseAhcTest
block|{
DECL|method|configureComponent ()
specifier|public
name|void
name|configureComponent
parameter_list|()
block|{
comment|// START SNIPPET: e1
comment|// create a client config builder
name|AsyncHttpClientConfig
operator|.
name|Builder
name|builder
init|=
operator|new
name|AsyncHttpClientConfig
operator|.
name|Builder
argument_list|()
decl_stmt|;
comment|// use the builder to set the options we want, in this case we want to follow redirects and try
comment|// at most 3 retries to send a request to the host
name|AsyncHttpClientConfig
name|config
init|=
name|builder
operator|.
name|setFollowRedirects
argument_list|(
literal|true
argument_list|)
operator|.
name|setMaxRequestRetry
argument_list|(
literal|3
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
comment|// lookup AhcComponent
name|AhcComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"ahc"
argument_list|,
name|AhcComponent
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// and set our custom client config to be used
name|component
operator|.
name|setClientConfig
argument_list|(
name|config
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
annotation|@
name|Test
DECL|method|testAhcComponentClientConfig ()
specifier|public
name|void
name|testAhcComponentClientConfig
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
literal|"Bye World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
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
name|configureComponent
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ahc:http://localhost:{{port}}/foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:{{port}}/foo"
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
comment|// redirect to test the client config worked as we told it to follow redirects
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
literal|"301"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Location"
argument_list|,
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/bar"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:{{port}}/bar"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

