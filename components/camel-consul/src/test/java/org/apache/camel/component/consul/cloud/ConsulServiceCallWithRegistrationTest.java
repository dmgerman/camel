begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|UUID
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
name|CamelContext
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
name|CamelExecutionException
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
name|support
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
name|impl
operator|.
name|cloud
operator|.
name|ServiceRegistrationRoutePolicy
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|SocketUtils
import|;
end_import

begin_class
DECL|class|ConsulServiceCallWithRegistrationTest
specifier|public
class|class
name|ConsulServiceCallWithRegistrationTest
extends|extends
name|ConsulTestSupport
block|{
DECL|field|SERVICE_HOST
specifier|private
specifier|final
specifier|static
name|String
name|SERVICE_HOST
init|=
literal|"localhost"
decl_stmt|;
comment|// ******************************
comment|// Setup / tear down
comment|// ******************************
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|ConsulServiceRegistry
name|registry
init|=
operator|new
name|ConsulServiceRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|setId
argument_list|(
name|context
operator|.
name|getUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|setUrl
argument_list|(
name|consulUrl
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|setServiceHost
argument_list|(
name|SERVICE_HOST
argument_list|)
expr_stmt|;
name|registry
operator|.
name|setOverrideServiceHost
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|registry
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
comment|// ******************************
comment|// Test
comment|// ******************************
annotation|@
name|Test
DECL|method|testServiceCallSuccess ()
specifier|public
name|void
name|testServiceCallSuccess
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|int
name|port
init|=
name|SocketUtils
operator|.
name|findAvailableTcpPort
argument_list|()
decl_stmt|;
specifier|final
name|String
name|serviceId
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
specifier|final
name|String
name|serviceName
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
comment|// context path is derived from the the jetty endpoint.
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
name|serviceName
argument_list|)
operator|.
name|component
argument_list|(
literal|"jetty"
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
name|end
argument_list|()
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
literal|"${body}"
argument_list|)
expr_stmt|;
name|fromF
argument_list|(
literal|"undertow:http://%s:%d/service/path"
argument_list|,
name|SERVICE_HOST
argument_list|,
name|port
argument_list|)
operator|.
name|routeId
argument_list|(
name|serviceId
argument_list|)
operator|.
name|routeGroup
argument_list|(
name|serviceName
argument_list|)
operator|.
name|routePolicy
argument_list|(
operator|new
name|ServiceRegistrationRoutePolicy
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
name|port
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ping on "
operator|+
name|port
argument_list|,
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"ping"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CamelExecutionException
operator|.
name|class
argument_list|)
DECL|method|testServiceCallFailure ()
specifier|public
name|void
name|testServiceCallFailure
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|int
name|port
init|=
name|SocketUtils
operator|.
name|findAvailableTcpPort
argument_list|()
decl_stmt|;
specifier|final
name|String
name|serviceId
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
specifier|final
name|String
name|serviceName
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
comment|// context path is had coded so it should fail as it not exposed
comment|// by jetty
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
name|serviceName
operator|+
literal|"/bad/path"
argument_list|)
operator|.
name|component
argument_list|(
literal|"jetty"
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
name|end
argument_list|()
operator|.
name|end
argument_list|()
operator|.
name|log
argument_list|(
literal|"${body}"
argument_list|)
expr_stmt|;
name|fromF
argument_list|(
literal|"undertow:http://%s:%d/service/path"
argument_list|,
name|SERVICE_HOST
argument_list|,
name|port
argument_list|)
operator|.
name|routeId
argument_list|(
name|serviceId
argument_list|)
operator|.
name|routeGroup
argument_list|(
name|serviceName
argument_list|)
operator|.
name|routePolicy
argument_list|(
operator|new
name|ServiceRegistrationRoutePolicy
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
name|port
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"ping"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have failed"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

