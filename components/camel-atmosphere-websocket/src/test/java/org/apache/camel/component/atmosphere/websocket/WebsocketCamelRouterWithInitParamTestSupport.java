begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atmosphere.websocket
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atmosphere
operator|.
name|websocket
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
name|servlet
operator|.
name|ServletContextHandler
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
name|ServletHolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_class
DECL|class|WebsocketCamelRouterWithInitParamTestSupport
specifier|public
class|class
name|WebsocketCamelRouterWithInitParamTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|CONTEXT
specifier|public
specifier|static
specifier|final
name|String
name|CONTEXT
init|=
literal|"/mycontext"
decl_stmt|;
DECL|field|CONTEXT_URL
specifier|public
specifier|static
specifier|final
name|String
name|CONTEXT_URL
init|=
literal|"http://localhost/mycontext"
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
DECL|field|startCamelContext
specifier|protected
name|boolean
name|startCamelContext
init|=
literal|true
decl_stmt|;
DECL|field|server
specifier|protected
name|Server
name|server
decl_stmt|;
DECL|field|servletHolder
specifier|protected
name|ServletHolder
name|servletHolder
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|=
operator|new
name|Server
argument_list|(
name|PORT
argument_list|)
expr_stmt|;
name|ServletContextHandler
name|context
init|=
operator|new
name|ServletContextHandler
argument_list|(
name|ServletContextHandler
operator|.
name|SESSIONS
argument_list|)
decl_stmt|;
name|context
operator|.
name|setContextPath
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|server
operator|.
name|setHandler
argument_list|(
name|context
argument_list|)
expr_stmt|;
if|if
condition|(
name|startCamelContext
condition|)
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
name|servletHolder
operator|=
operator|new
name|ServletHolder
argument_list|(
operator|new
name|CamelWebSocketServlet
argument_list|()
argument_list|)
expr_stmt|;
name|servletHolder
operator|.
name|setName
argument_list|(
literal|"CamelWsServlet"
argument_list|)
expr_stmt|;
name|servletHolder
operator|.
name|setInitParameter
argument_list|(
literal|"events"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|context
operator|.
name|addServlet
argument_list|(
name|servletHolder
argument_list|,
literal|"/*"
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|startCamelContext
condition|)
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
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
block|}
end_class

end_unit

