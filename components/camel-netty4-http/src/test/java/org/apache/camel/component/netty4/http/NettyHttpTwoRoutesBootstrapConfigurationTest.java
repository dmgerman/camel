begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
operator|.
name|http
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
name|BindToRegistry
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
name|netty4
operator|.
name|NettyServerBootstrapConfiguration
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
name|impl
operator|.
name|JndiRegistry
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
DECL|class|NettyHttpTwoRoutesBootstrapConfigurationTest
specifier|public
class|class
name|NettyHttpTwoRoutesBootstrapConfigurationTest
extends|extends
name|BaseNettyTest
block|{
DECL|field|bootstrapConfiguration
specifier|private
name|NettyServerBootstrapConfiguration
name|bootstrapConfiguration
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"myBootstrapOptions"
argument_list|)
DECL|method|loadNettyBootstrapConf ()
specifier|public
name|NettyServerBootstrapConfiguration
name|loadNettyBootstrapConf
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create NettyServerBootstrapConfiguration instance where we can configure the bootstrap
comment|// option we want to use in our Camel routes. This allows us to configure this once,
comment|// and also explicit
name|bootstrapConfiguration
operator|=
operator|new
name|NettyServerBootstrapConfiguration
argument_list|()
expr_stmt|;
name|bootstrapConfiguration
operator|.
name|setBacklog
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|bootstrapConfiguration
operator|.
name|setConnectTimeout
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|bootstrapConfiguration
operator|.
name|setKeepAlive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bootstrapConfiguration
operator|.
name|setWorkerCount
argument_list|(
literal|4
argument_list|)
expr_stmt|;
return|return
name|bootstrapConfiguration
return|;
block|}
annotation|@
name|Test
DECL|method|testTwoRoutes ()
specifier|public
name|void
name|testTwoRoutes
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Camel"
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4-http:http://localhost:{{port}}/foo"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty4-http:http://localhost:{{port}}/bar"
argument_list|,
literal|"Hello Camel"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye Camel"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// validate the options
name|NettyHttpConsumer
name|consumer
init|=
operator|(
name|NettyHttpConsumer
operator|)
name|context
operator|.
name|getRoute
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|getConsumer
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getBacklog
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getWorkerCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isKeepAlive
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5000
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getConnectTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|=
operator|(
name|NettyHttpConsumer
operator|)
name|context
operator|.
name|getRoute
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|getConsumer
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getBacklog
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getWorkerCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isKeepAlive
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5000
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getConnectTimeout
argument_list|()
argument_list|)
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
comment|// we want to use the same bootstrap options and want to configure this explicit, so we
comment|// have a NettyServerBootstrapConfiguration instance in the registry, with the key = myBootstrapOptions
comment|// which we then tell netty4-http to lookup and use
name|from
argument_list|(
literal|"netty4-http:http://0.0.0.0:{{port}}/foo?bootstrapConfiguration=#myBootstrapOptions"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty4-http:http://0.0.0.0:{{port}}/bar?bootstrapConfiguration=#myBootstrapOptions"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye Camel"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

