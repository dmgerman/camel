begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ribbon.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ribbon
operator|.
name|cloud
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
name|ribbon
operator|.
name|RibbonConfiguration
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
name|StaticServiceDiscovery
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
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
DECL|class|RibbonServiceCallUpdateRouteTest
specifier|public
class|class
name|RibbonServiceCallUpdateRouteTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|servers
specifier|private
specifier|final
name|StaticServiceDiscovery
name|servers
init|=
operator|new
name|StaticServiceDiscovery
argument_list|()
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
comment|// setup a static ribbon server list with these 2 servers to start with
name|servers
operator|.
name|addServer
argument_list|(
literal|"myService@localhost:9090"
argument_list|)
expr_stmt|;
name|servers
operator|.
name|addServer
argument_list|(
literal|"myService@localhost:9091"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
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
literal|"mock:9090"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:9091"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|out2
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"9091"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"9090"
argument_list|,
name|out2
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// stop the first server and remove it from the known list of servers
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"9090"
argument_list|)
expr_stmt|;
name|servers
operator|.
name|removeServer
argument_list|(
name|s
lambda|->
name|ObjectHelper
operator|.
name|equal
argument_list|(
literal|"localhost"
argument_list|,
name|s
operator|.
name|getHost
argument_list|()
argument_list|)
operator|&&
literal|9090
operator|==
name|s
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
comment|// call the other active server
name|String
name|out3
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"9091"
argument_list|,
name|out3
argument_list|)
expr_stmt|;
comment|// sleep a bit to make the server updated run and detect that a server is no longer in the list
name|log
operator|.
name|debug
argument_list|(
literal|"Sleeping to all the server list updated to run"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Calling the service now"
argument_list|)
expr_stmt|;
comment|// call again and it should call 9091 as its the only active server
name|String
name|out4
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"9091"
argument_list|,
name|out4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
name|RibbonConfiguration
name|configuration
init|=
operator|new
name|RibbonConfiguration
argument_list|()
decl_stmt|;
comment|// lets update quick so we do not have to sleep so much in the tests
name|configuration
operator|.
name|addProperty
argument_list|(
literal|"ServerListRefreshInterval"
argument_list|,
literal|"250"
argument_list|)
expr_stmt|;
name|RibbonServiceLoadBalancer
name|loadBalancer
init|=
operator|new
name|RibbonServiceLoadBalancer
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
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
literal|"myService"
argument_list|)
operator|.
name|component
argument_list|(
literal|"http"
argument_list|)
operator|.
name|loadBalancer
argument_list|(
name|loadBalancer
argument_list|)
operator|.
name|serviceDiscovery
argument_list|(
name|servers
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:9090"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"9090"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:9090"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"9090"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:http://localhost:9091"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"9091"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:9091"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"9091"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

