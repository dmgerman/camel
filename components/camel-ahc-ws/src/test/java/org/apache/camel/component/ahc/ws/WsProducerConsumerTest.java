begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc.ws
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
operator|.
name|ws
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
name|junit5
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
name|Connector
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
name|ServerConnector
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
name|servlet
operator|.
name|ServletContextHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|AfterEach
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|BeforeEach
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|WsProducerConsumerTest
specifier|public
class|class
name|WsProducerConsumerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEST_MESSAGE
specifier|protected
specifier|static
specifier|final
name|String
name|TEST_MESSAGE
init|=
literal|"Hello World!"
decl_stmt|;
DECL|field|PORT
specifier|protected
specifier|static
specifier|final
name|int
name|PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|WsProducerConsumerTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|server
specifier|protected
name|Server
name|server
decl_stmt|;
DECL|field|messages
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|messages
decl_stmt|;
DECL|method|startTestServer ()
specifier|public
name|void
name|startTestServer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// start a simple websocket echo service
name|server
operator|=
operator|new
name|Server
argument_list|(
name|PORT
argument_list|)
expr_stmt|;
name|Connector
name|connector
init|=
operator|new
name|ServerConnector
argument_list|(
name|server
argument_list|)
decl_stmt|;
name|server
operator|.
name|addConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
name|ServletContextHandler
name|ctx
init|=
operator|new
name|ServletContextHandler
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|setContextPath
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|addServlet
argument_list|(
name|TestServletFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"/*"
argument_list|)
expr_stmt|;
name|server
operator|.
name|setHandler
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|server
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|stopTestServer ()
specifier|public
name|void
name|stopTestServer
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|BeforeEach
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|startTestServer
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|AfterEach
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|stopTestServer
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
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:input"
argument_list|,
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTwoRoutesRestartConsumer ()
specifier|public
name|void
name|testTwoRoutesRestartConsumer
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
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:input"
argument_list|,
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|resetMocks
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Restarting bar route"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:input"
argument_list|,
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTwoRoutesRestartProducer ()
specifier|public
name|void
name|testTwoRoutesRestartProducer
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
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:input"
argument_list|,
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|resetMocks
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Restarting foo route"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:input"
argument_list|,
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilders ()
specifier|protected
name|RouteBuilder
index|[]
name|createRouteBuilders
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteBuilder
index|[]
name|rbs
init|=
operator|new
name|RouteBuilder
index|[
literal|2
index|]
decl_stmt|;
name|rbs
index|[
literal|0
index|]
operator|=
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
literal|"direct:input"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ahc-ws://localhost:"
operator|+
name|PORT
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|rbs
index|[
literal|1
index|]
operator|=
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
literal|"ahc-ws://localhost:"
operator|+
name|PORT
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
return|return
name|rbs
return|;
block|}
block|}
end_class

end_unit

