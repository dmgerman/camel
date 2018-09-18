begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
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
name|http
operator|.
name|handler
operator|.
name|SessionReflectionHandler
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
name|http
operator|.
name|common
operator|.
name|cookie
operator|.
name|ExchangeCookieHandler
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
name|http
operator|.
name|common
operator|.
name|cookie
operator|.
name|InstanceCookieHandler
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
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|AvailablePortFinder
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|handler
operator|.
name|ContextHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|session
operator|.
name|SessionHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
DECL|class|HttpProducerSessionTest
specifier|public
class|class
name|HttpProducerSessionTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port
specifier|private
specifier|static
specifier|volatile
name|int
name|port
decl_stmt|;
DECL|field|localServer
specifier|private
specifier|static
name|Server
name|localServer
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|initServer ()
specifier|public
specifier|static
name|void
name|initServer
parameter_list|()
throws|throws
name|Exception
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|24000
argument_list|)
expr_stmt|;
name|localServer
operator|=
operator|new
name|Server
argument_list|(
operator|new
name|InetSocketAddress
argument_list|(
literal|"127.0.0.1"
argument_list|,
name|port
argument_list|)
argument_list|)
expr_stmt|;
name|ContextHandler
name|contextHandler
init|=
operator|new
name|ContextHandler
argument_list|()
decl_stmt|;
name|contextHandler
operator|.
name|setContextPath
argument_list|(
literal|"/session"
argument_list|)
expr_stmt|;
name|SessionHandler
name|sessionHandler
init|=
operator|new
name|SessionHandler
argument_list|()
decl_stmt|;
name|sessionHandler
operator|.
name|setHandler
argument_list|(
operator|new
name|SessionReflectionHandler
argument_list|()
argument_list|)
expr_stmt|;
name|contextHandler
operator|.
name|setHandler
argument_list|(
name|sessionHandler
argument_list|)
expr_stmt|;
name|localServer
operator|.
name|setHandler
argument_list|(
name|contextHandler
argument_list|)
expr_stmt|;
name|localServer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|shutdownServer ()
specifier|public
specifier|static
name|void
name|shutdownServer
parameter_list|()
throws|throws
name|Exception
block|{
name|localServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoSession ()
specifier|public
name|void
name|testNoSession
parameter_list|()
throws|throws
name|Exception
block|{
comment|// the camel-http component will handle cookies at endpoint level
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"New New World"
argument_list|,
literal|"Old Old World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInstanceSession ()
specifier|public
name|void
name|testInstanceSession
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
literal|"Old New World"
argument_list|,
literal|"Old Old World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:instance"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:instance"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExchangeSession ()
specifier|public
name|void
name|testExchangeSession
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
literal|"Old New World"
argument_list|,
literal|"Old New World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:exchange"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:exchange"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
name|JndiRegistry
name|jndiRegistry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndiRegistry
operator|.
name|bind
argument_list|(
literal|"instanceCookieHandler"
argument_list|,
operator|new
name|InstanceCookieHandler
argument_list|()
argument_list|)
expr_stmt|;
name|jndiRegistry
operator|.
name|bind
argument_list|(
literal|"exchangeCookieHandler"
argument_list|,
operator|new
name|ExchangeCookieHandler
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndiRegistry
return|;
block|}
DECL|method|getTestServerEndpointSessionUrl ()
specifier|private
name|String
name|getTestServerEndpointSessionUrl
parameter_list|()
block|{
comment|// session handling will not work for localhost
return|return
literal|"http://127.0.0.1:"
operator|+
name|port
operator|+
literal|"/session/"
return|;
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
name|to
argument_list|(
name|getTestServerEndpointSessionUrl
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
name|getTestServerEndpointSessionUrl
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:instance"
argument_list|)
operator|.
name|to
argument_list|(
name|getTestServerEndpointSessionUrl
argument_list|()
operator|+
literal|"?cookieHandler=#instanceCookieHandler"
argument_list|)
operator|.
name|to
argument_list|(
name|getTestServerEndpointSessionUrl
argument_list|()
operator|+
literal|"?cookieHandler=#instanceCookieHandler"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:exchange"
argument_list|)
operator|.
name|to
argument_list|(
name|getTestServerEndpointSessionUrl
argument_list|()
operator|+
literal|"?cookieHandler=#exchangeCookieHandler"
argument_list|)
operator|.
name|to
argument_list|(
name|getTestServerEndpointSessionUrl
argument_list|()
operator|+
literal|"?cookieHandler=#exchangeCookieHandler"
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
block|}
end_class

end_unit

