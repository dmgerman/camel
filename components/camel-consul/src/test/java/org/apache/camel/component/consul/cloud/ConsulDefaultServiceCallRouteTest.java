begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
operator|.
name|cloud
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
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|AgentClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|agent
operator|.
name|ImmutableRegistration
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|agent
operator|.
name|Registration
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
name|RoutesBuilder
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
name|consul
operator|.
name|ConsulTestSupport
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
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|ConsulDefaultServiceCallRouteTest
specifier|public
class|class
name|ConsulDefaultServiceCallRouteTest
extends|extends
name|ConsulTestSupport
block|{
DECL|field|SERVICE_NAME
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_NAME
init|=
literal|"http-service"
decl_stmt|;
DECL|field|SERVICE_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|SERVICE_COUNT
init|=
literal|5
decl_stmt|;
DECL|field|client
specifier|private
name|AgentClient
name|client
decl_stmt|;
DECL|field|registrations
specifier|private
name|List
argument_list|<
name|Registration
argument_list|>
name|registrations
decl_stmt|;
DECL|field|expectedBodies
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|expectedBodies
decl_stmt|;
comment|// *************************************************************************
comment|// Setup / tear down
comment|// *************************************************************************
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|protected
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doPreSetup
argument_list|()
expr_stmt|;
name|client
operator|=
name|getConsul
argument_list|()
operator|.
name|agentClient
argument_list|()
expr_stmt|;
name|registrations
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|SERVICE_COUNT
argument_list|)
expr_stmt|;
name|expectedBodies
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|SERVICE_COUNT
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|SERVICE_COUNT
condition|;
name|i
operator|++
control|)
block|{
name|Registration
name|r
init|=
name|ImmutableRegistration
operator|.
name|builder
argument_list|()
operator|.
name|id
argument_list|(
literal|"service-"
operator|+
name|i
argument_list|)
operator|.
name|name
argument_list|(
name|SERVICE_NAME
argument_list|)
operator|.
name|address
argument_list|(
literal|"127.0.0.1"
argument_list|)
operator|.
name|port
argument_list|(
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|client
operator|.
name|register
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|registrations
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|expectedBodies
operator|.
name|add
argument_list|(
literal|"ping on "
operator|+
name|r
operator|.
name|getPort
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doPostTearDown ()
specifier|public
name|void
name|doPostTearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doPostTearDown
argument_list|()
expr_stmt|;
name|registrations
operator|.
name|forEach
argument_list|(
name|r
lambda|->
name|client
operator|.
name|deregister
argument_list|(
name|r
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Test
comment|// *************************************************************************
annotation|@
name|Test
DECL|method|testServiceCall ()
specifier|public
name|void
name|testServiceCall
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|SERVICE_COUNT
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
name|expectedBodies
argument_list|)
expr_stmt|;
name|registrations
operator|.
name|forEach
argument_list|(
name|r
lambda|->
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"ping"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Route
comment|// *************************************************************************
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
name|createRouteBuilder
parameter_list|()
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
name|serviceCall
argument_list|()
operator|.
name|name
argument_list|(
name|SERVICE_NAME
argument_list|)
operator|.
name|component
argument_list|(
literal|"http"
argument_list|)
operator|.
name|defaultLoadBalancer
argument_list|()
operator|.
name|consulServiceDiscovery
argument_list|()
operator|.
name|url
argument_list|(
name|consulUrl
argument_list|()
argument_list|)
operator|.
name|endParent
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:org.apache.camel.component.consul.cloud?level=INFO&showAll=true&multiline=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|registrations
operator|.
name|forEach
argument_list|(
name|r
lambda|->
name|fromF
argument_list|(
literal|"jetty:http://%s:%d"
argument_list|,
name|r
operator|.
name|getAddress
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|r
operator|.
name|getPort
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|simple
argument_list|(
literal|"${in.body} on "
operator|+
name|r
operator|.
name|getPort
argument_list|()
operator|.
name|get
argument_list|()
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

