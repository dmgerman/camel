begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
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
name|impl
operator|.
name|DefaultClassResolver
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
DECL|class|NettySharedHttpServerTest
specifier|public
class|class
name|NettySharedHttpServerTest
extends|extends
name|BaseNettyTest
block|{
DECL|field|nettySharedHttpServer
specifier|private
name|NettySharedHttpServer
name|nettySharedHttpServer
decl_stmt|;
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|nettySharedHttpServer
operator|=
operator|new
name|DefaultNettySharedHttpServer
argument_list|()
expr_stmt|;
name|nettySharedHttpServer
operator|.
name|setClassResolver
argument_list|(
operator|new
name|DefaultClassResolver
argument_list|()
argument_list|)
expr_stmt|;
name|NettySharedHttpServerBootstrapConfiguration
name|configuration
init|=
operator|new
name|NettySharedHttpServerBootstrapConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setPort
argument_list|(
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setHost
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setBacklog
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setKeepAlive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setCompression
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|nettySharedHttpServer
operator|.
name|setNettyServerBootstrapConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|nettySharedHttpServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myNettyServer"
argument_list|,
name|nettySharedHttpServer
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|nettySharedHttpServer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
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
literal|"netty-http:http://localhost:{{port}}/foo"
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
literal|"netty-http:http://localhost:{{port}}/bar"
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
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|nettySharedHttpServer
operator|.
name|getConsumersSize
argument_list|()
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
comment|// we are using a shared netty http server, so the port number is not needed to be defined in the uri
name|from
argument_list|(
literal|"netty-http:http://localhost/foo?nettySharedHttpServer=#myNettyServer"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Foo route using thread ${threadName}"
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
comment|// we are using a shared netty http server, so the port number is not needed to be defined in the uri
name|from
argument_list|(
literal|"netty-http:http://localhost/bar?nettySharedHttpServer=#myNettyServer"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Bar route using thread ${threadName}"
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

