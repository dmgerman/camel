begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|List
import|;
end_import

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
name|Route
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
name|cloud
operator|.
name|ServiceDefinition
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
name|impl
operator|.
name|DefaultCamelContext
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
name|model
operator|.
name|cloud
operator|.
name|ServiceCallConfigurationDefinition
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
name|model
operator|.
name|cloud
operator|.
name|ServiceCallDefinitionConstants
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
name|model
operator|.
name|cloud
operator|.
name|ServiceCallExpressionConfiguration
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
name|model
operator|.
name|language
operator|.
name|SimpleExpression
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
name|processor
operator|.
name|channel
operator|.
name|DefaultChannel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
DECL|class|ServiceCallConfigurationTest
specifier|public
class|class
name|ServiceCallConfigurationTest
block|{
comment|// ****************************************
comment|// test default resolution
comment|// ****************************************
annotation|@
name|Test
DECL|method|testDynamicUri ()
specifier|public
name|void
name|testDynamicUri
parameter_list|()
throws|throws
name|Exception
block|{
name|StaticServiceDiscovery
name|sd
init|=
operator|new
name|StaticServiceDiscovery
argument_list|()
decl_stmt|;
name|sd
operator|.
name|addServer
argument_list|(
literal|"scall@127.0.0.1:8080"
argument_list|)
expr_stmt|;
name|sd
operator|.
name|addServer
argument_list|(
literal|"scall@127.0.0.1:8081"
argument_list|)
expr_stmt|;
name|ServiceCallConfigurationDefinition
name|conf
init|=
operator|new
name|ServiceCallConfigurationDefinition
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setServiceDiscovery
argument_list|(
name|sd
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setComponent
argument_list|(
literal|"mock"
argument_list|)
expr_stmt|;
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setServiceCallConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"default"
argument_list|)
operator|.
name|serviceCall
argument_list|(
literal|"scall"
argument_list|,
literal|"scall/api/${header.customerId}"
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
name|MockEndpoint
name|mock
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:127.0.0.1:8080/api/123"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|DefaultServiceCallProcessor
name|proc
init|=
name|findServiceCallProcessor
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"default"
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|proc
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|proc
operator|.
name|getLoadBalancer
argument_list|()
operator|instanceof
name|DefaultServiceLoadBalancer
argument_list|)
expr_stmt|;
name|DefaultServiceLoadBalancer
name|loadBalancer
init|=
operator|(
name|DefaultServiceLoadBalancer
operator|)
name|proc
operator|.
name|getLoadBalancer
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|sd
argument_list|,
name|loadBalancer
operator|.
name|getServiceDiscovery
argument_list|()
argument_list|)
expr_stmt|;
comment|// call the route
name|context
operator|.
name|createFluentProducerTemplate
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|withHeader
argument_list|(
literal|"customerId"
argument_list|,
literal|"123"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
comment|// the service should call the mock
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultConfigurationFromCamelContext ()
specifier|public
name|void
name|testDefaultConfigurationFromCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|StaticServiceDiscovery
name|sd
init|=
operator|new
name|StaticServiceDiscovery
argument_list|()
decl_stmt|;
name|sd
operator|.
name|addServer
argument_list|(
literal|"service@127.0.0.1:8080"
argument_list|)
expr_stmt|;
name|sd
operator|.
name|addServer
argument_list|(
literal|"service@127.0.0.1:8081"
argument_list|)
expr_stmt|;
name|BlacklistServiceFilter
name|sf
init|=
operator|new
name|BlacklistServiceFilter
argument_list|()
decl_stmt|;
name|sf
operator|.
name|addServer
argument_list|(
literal|"*@127.0.0.1:8080"
argument_list|)
expr_stmt|;
name|ServiceCallConfigurationDefinition
name|conf
init|=
operator|new
name|ServiceCallConfigurationDefinition
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setServiceDiscovery
argument_list|(
name|sd
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setServiceFilter
argument_list|(
name|sf
argument_list|)
expr_stmt|;
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setServiceCallConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"default"
argument_list|)
operator|.
name|serviceCall
argument_list|()
operator|.
name|name
argument_list|(
literal|"scall"
argument_list|)
operator|.
name|component
argument_list|(
literal|"file"
argument_list|)
operator|.
name|end
argument_list|()
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
name|DefaultServiceCallProcessor
name|proc
init|=
name|findServiceCallProcessor
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"default"
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|proc
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|proc
operator|.
name|getLoadBalancer
argument_list|()
operator|instanceof
name|DefaultServiceLoadBalancer
argument_list|)
expr_stmt|;
name|DefaultServiceLoadBalancer
name|loadBalancer
init|=
operator|(
name|DefaultServiceLoadBalancer
operator|)
name|proc
operator|.
name|getLoadBalancer
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|sd
argument_list|,
name|loadBalancer
operator|.
name|getServiceDiscovery
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|sf
argument_list|,
name|loadBalancer
operator|.
name|getServiceFilter
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultConfigurationFromRegistryWithDefaultName ()
specifier|public
name|void
name|testDefaultConfigurationFromRegistryWithDefaultName
parameter_list|()
throws|throws
name|Exception
block|{
name|StaticServiceDiscovery
name|sd
init|=
operator|new
name|StaticServiceDiscovery
argument_list|()
decl_stmt|;
name|sd
operator|.
name|addServer
argument_list|(
literal|"service@127.0.0.1:8080"
argument_list|)
expr_stmt|;
name|sd
operator|.
name|addServer
argument_list|(
literal|"service@127.0.0.1:8081"
argument_list|)
expr_stmt|;
name|BlacklistServiceFilter
name|sf
init|=
operator|new
name|BlacklistServiceFilter
argument_list|()
decl_stmt|;
name|sf
operator|.
name|addServer
argument_list|(
literal|"*@127.0.0.1:8080"
argument_list|)
expr_stmt|;
name|ServiceCallConfigurationDefinition
name|conf
init|=
operator|new
name|ServiceCallConfigurationDefinition
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setServiceDiscovery
argument_list|(
name|sd
argument_list|)
expr_stmt|;
name|conf
operator|.
name|serviceFilter
argument_list|(
name|sf
argument_list|)
expr_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"default"
argument_list|)
operator|.
name|serviceCall
argument_list|()
operator|.
name|name
argument_list|(
literal|"scall"
argument_list|)
operator|.
name|component
argument_list|(
literal|"file"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
name|ServiceCallDefinitionConstants
operator|.
name|DEFAULT_SERVICE_CALL_CONFIG_ID
argument_list|,
name|conf
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|DefaultServiceCallProcessor
name|proc
init|=
name|findServiceCallProcessor
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"default"
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|proc
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|proc
operator|.
name|getLoadBalancer
argument_list|()
operator|instanceof
name|DefaultServiceLoadBalancer
argument_list|)
expr_stmt|;
name|DefaultServiceLoadBalancer
name|loadBalancer
init|=
operator|(
name|DefaultServiceLoadBalancer
operator|)
name|proc
operator|.
name|getLoadBalancer
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|sd
argument_list|,
name|loadBalancer
operator|.
name|getServiceDiscovery
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|sf
argument_list|,
name|loadBalancer
operator|.
name|getServiceFilter
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultConfigurationFromRegistryWithNonDefaultName ()
specifier|public
name|void
name|testDefaultConfigurationFromRegistryWithNonDefaultName
parameter_list|()
throws|throws
name|Exception
block|{
name|StaticServiceDiscovery
name|sd
init|=
operator|new
name|StaticServiceDiscovery
argument_list|()
decl_stmt|;
name|sd
operator|.
name|addServer
argument_list|(
literal|"service@127.0.0.1:8080"
argument_list|)
expr_stmt|;
name|sd
operator|.
name|addServer
argument_list|(
literal|"service@127.0.0.1:8081"
argument_list|)
expr_stmt|;
name|BlacklistServiceFilter
name|sf
init|=
operator|new
name|BlacklistServiceFilter
argument_list|()
decl_stmt|;
name|sf
operator|.
name|addServer
argument_list|(
literal|"*@127.0.0.1:8080"
argument_list|)
expr_stmt|;
name|ServiceCallConfigurationDefinition
name|conf
init|=
operator|new
name|ServiceCallConfigurationDefinition
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setServiceDiscovery
argument_list|(
name|sd
argument_list|)
expr_stmt|;
name|conf
operator|.
name|serviceFilter
argument_list|(
name|sf
argument_list|)
expr_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"default"
argument_list|)
operator|.
name|serviceCall
argument_list|()
operator|.
name|name
argument_list|(
literal|"scall"
argument_list|)
operator|.
name|component
argument_list|(
literal|"file"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|conf
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|DefaultServiceCallProcessor
name|proc
init|=
name|findServiceCallProcessor
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"default"
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|proc
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|proc
operator|.
name|getLoadBalancer
argument_list|()
operator|instanceof
name|DefaultServiceLoadBalancer
argument_list|)
expr_stmt|;
name|DefaultServiceLoadBalancer
name|loadBalancer
init|=
operator|(
name|DefaultServiceLoadBalancer
operator|)
name|proc
operator|.
name|getLoadBalancer
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|sd
argument_list|,
name|loadBalancer
operator|.
name|getServiceDiscovery
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|sf
argument_list|,
name|loadBalancer
operator|.
name|getServiceFilter
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|// ****************************************
comment|// test mixed resolution
comment|// ****************************************
annotation|@
name|Test
DECL|method|testMixedConfiguration ()
specifier|public
name|void
name|testMixedConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Default
name|StaticServiceDiscovery
name|defaultServiceDiscovery
init|=
operator|new
name|StaticServiceDiscovery
argument_list|()
decl_stmt|;
name|defaultServiceDiscovery
operator|.
name|addServer
argument_list|(
literal|"service@127.0.0.1:8080"
argument_list|)
expr_stmt|;
name|defaultServiceDiscovery
operator|.
name|addServer
argument_list|(
literal|"service@127.0.0.1:8081"
argument_list|)
expr_stmt|;
name|defaultServiceDiscovery
operator|.
name|addServer
argument_list|(
literal|"service@127.0.0.1:8082"
argument_list|)
expr_stmt|;
name|BlacklistServiceFilter
name|defaultServiceFilter
init|=
operator|new
name|BlacklistServiceFilter
argument_list|()
decl_stmt|;
name|defaultServiceFilter
operator|.
name|addServer
argument_list|(
literal|"*@127.0.0.1:8080"
argument_list|)
expr_stmt|;
name|ServiceCallConfigurationDefinition
name|defaultConfiguration
init|=
operator|new
name|ServiceCallConfigurationDefinition
argument_list|()
decl_stmt|;
name|defaultConfiguration
operator|.
name|setServiceDiscovery
argument_list|(
name|defaultServiceDiscovery
argument_list|)
expr_stmt|;
name|defaultConfiguration
operator|.
name|serviceFilter
argument_list|(
name|defaultServiceFilter
argument_list|)
expr_stmt|;
comment|// Named
name|BlacklistServiceFilter
name|namedServiceFilter
init|=
operator|new
name|BlacklistServiceFilter
argument_list|()
decl_stmt|;
name|namedServiceFilter
operator|.
name|addServer
argument_list|(
literal|"*@127.0.0.1:8081"
argument_list|)
expr_stmt|;
name|ServiceCallConfigurationDefinition
name|namedConfiguration
init|=
operator|new
name|ServiceCallConfigurationDefinition
argument_list|()
decl_stmt|;
name|namedConfiguration
operator|.
name|serviceFilter
argument_list|(
name|namedServiceFilter
argument_list|)
expr_stmt|;
comment|// Local
name|StaticServiceDiscovery
name|localServiceDiscovery
init|=
operator|new
name|StaticServiceDiscovery
argument_list|()
decl_stmt|;
name|localServiceDiscovery
operator|.
name|addServer
argument_list|(
literal|"service@127.0.0.1:8080"
argument_list|)
expr_stmt|;
name|localServiceDiscovery
operator|.
name|addServer
argument_list|(
literal|"service@127.0.0.1:8081"
argument_list|)
expr_stmt|;
name|localServiceDiscovery
operator|.
name|addServer
argument_list|(
literal|"service@127.0.0.1:8082"
argument_list|)
expr_stmt|;
name|localServiceDiscovery
operator|.
name|addServer
argument_list|(
literal|"service@127.0.0.1:8084"
argument_list|)
expr_stmt|;
comment|// Camel context
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setServiceCallConfiguration
argument_list|(
name|defaultConfiguration
argument_list|)
expr_stmt|;
name|context
operator|.
name|addServiceCallConfiguration
argument_list|(
literal|"named"
argument_list|,
name|namedConfiguration
argument_list|)
expr_stmt|;
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
name|from
argument_list|(
literal|"direct:default"
argument_list|)
operator|.
name|id
argument_list|(
literal|"default"
argument_list|)
operator|.
name|serviceCall
argument_list|()
operator|.
name|name
argument_list|(
literal|"default-scall"
argument_list|)
operator|.
name|component
argument_list|(
literal|"file"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:named"
argument_list|)
operator|.
name|id
argument_list|(
literal|"named"
argument_list|)
operator|.
name|serviceCall
argument_list|()
operator|.
name|serviceCallConfiguration
argument_list|(
literal|"named"
argument_list|)
operator|.
name|name
argument_list|(
literal|"named-scall"
argument_list|)
operator|.
name|component
argument_list|(
literal|"file"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:local"
argument_list|)
operator|.
name|id
argument_list|(
literal|"local"
argument_list|)
operator|.
name|serviceCall
argument_list|()
operator|.
name|serviceCallConfiguration
argument_list|(
literal|"named"
argument_list|)
operator|.
name|name
argument_list|(
literal|"local-scall"
argument_list|)
operator|.
name|component
argument_list|(
literal|"file"
argument_list|)
operator|.
name|serviceDiscovery
argument_list|(
name|localServiceDiscovery
argument_list|)
operator|.
name|end
argument_list|()
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
block|{
comment|// Default
name|DefaultServiceCallProcessor
name|proc
init|=
name|findServiceCallProcessor
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"default"
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|proc
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|proc
operator|.
name|getLoadBalancer
argument_list|()
operator|instanceof
name|DefaultServiceLoadBalancer
argument_list|)
expr_stmt|;
name|DefaultServiceLoadBalancer
name|loadBalancer
init|=
operator|(
name|DefaultServiceLoadBalancer
operator|)
name|proc
operator|.
name|getLoadBalancer
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|defaultServiceDiscovery
argument_list|,
name|loadBalancer
operator|.
name|getServiceDiscovery
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|defaultServiceFilter
argument_list|,
name|loadBalancer
operator|.
name|getServiceFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|{
comment|// Named
name|DefaultServiceCallProcessor
name|proc
init|=
name|findServiceCallProcessor
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"named"
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|proc
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|proc
operator|.
name|getLoadBalancer
argument_list|()
operator|instanceof
name|DefaultServiceLoadBalancer
argument_list|)
expr_stmt|;
name|DefaultServiceLoadBalancer
name|loadBalancer
init|=
operator|(
name|DefaultServiceLoadBalancer
operator|)
name|proc
operator|.
name|getLoadBalancer
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|defaultServiceDiscovery
argument_list|,
name|loadBalancer
operator|.
name|getServiceDiscovery
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|namedServiceFilter
argument_list|,
name|loadBalancer
operator|.
name|getServiceFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|{
comment|// Local
name|DefaultServiceCallProcessor
name|proc
init|=
name|findServiceCallProcessor
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"local"
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|proc
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|proc
operator|.
name|getLoadBalancer
argument_list|()
operator|instanceof
name|DefaultServiceLoadBalancer
argument_list|)
expr_stmt|;
name|DefaultServiceLoadBalancer
name|loadBalancer
init|=
operator|(
name|DefaultServiceLoadBalancer
operator|)
name|proc
operator|.
name|getLoadBalancer
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|localServiceDiscovery
argument_list|,
name|loadBalancer
operator|.
name|getServiceDiscovery
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|namedServiceFilter
argument_list|,
name|loadBalancer
operator|.
name|getServiceFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|// **********************************************
comment|// test placeholders
comment|// **********************************************
annotation|@
name|Test
DECL|method|testPlaceholders ()
specifier|public
name|void
name|testPlaceholders
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"scall.name"
argument_list|,
literal|"service-name"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"scall.scheme"
argument_list|,
literal|"file"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"scall.servers1"
argument_list|,
literal|"hello-service@localhost:8081,hello-service@localhost:8082"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"scall.servers2"
argument_list|,
literal|"hello-svc@localhost:8083,hello-svc@localhost:8084"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"scall.filter"
argument_list|,
literal|"hello-svc@localhost:8083"
argument_list|)
expr_stmt|;
name|ServiceCallConfigurationDefinition
name|global
init|=
operator|new
name|ServiceCallConfigurationDefinition
argument_list|()
decl_stmt|;
name|global
operator|.
name|blacklistFilter
argument_list|()
operator|.
name|servers
argument_list|(
literal|"{{scall.filter}}"
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|setServiceCallConfiguration
argument_list|(
name|global
argument_list|)
expr_stmt|;
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"default"
argument_list|)
operator|.
name|serviceCall
argument_list|()
operator|.
name|name
argument_list|(
literal|"{{scall.name}}"
argument_list|)
operator|.
name|component
argument_list|(
literal|"{{scall.scheme}}"
argument_list|)
operator|.
name|uri
argument_list|(
literal|"direct:{{scall.name}}"
argument_list|)
operator|.
name|staticServiceDiscovery
argument_list|()
operator|.
name|servers
argument_list|(
literal|"{{scall.servers1}}"
argument_list|)
operator|.
name|servers
argument_list|(
literal|"{{scall.servers2}}"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|end
argument_list|()
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
name|DefaultServiceCallProcessor
name|proc
init|=
name|findServiceCallProcessor
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"default"
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|proc
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|proc
operator|.
name|getLoadBalancer
argument_list|()
operator|instanceof
name|DefaultServiceLoadBalancer
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"service-name"
argument_list|,
name|proc
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"file"
argument_list|,
name|proc
operator|.
name|getScheme
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"direct:service-name"
argument_list|,
name|proc
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|DefaultServiceLoadBalancer
name|lb
init|=
operator|(
name|DefaultServiceLoadBalancer
operator|)
name|proc
operator|.
name|getLoadBalancer
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|lb
operator|.
name|getServiceFilter
argument_list|()
operator|instanceof
name|BlacklistServiceFilter
argument_list|)
expr_stmt|;
name|BlacklistServiceFilter
name|filter
init|=
operator|(
name|BlacklistServiceFilter
operator|)
name|lb
operator|.
name|getServiceFilter
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|blacklist
init|=
name|filter
operator|.
name|getBlacklistedServices
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|blacklist
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|lb
operator|.
name|getServiceDiscovery
argument_list|()
operator|instanceof
name|StaticServiceDiscovery
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|services1
init|=
name|lb
operator|.
name|getServiceDiscovery
argument_list|()
operator|.
name|getServices
argument_list|(
literal|"hello-service"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|filter
operator|.
name|apply
argument_list|(
name|services1
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|services2
init|=
name|lb
operator|.
name|getServiceDiscovery
argument_list|()
operator|.
name|getServices
argument_list|(
literal|"hello-svc"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|filter
operator|.
name|apply
argument_list|(
name|services2
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|// Cleanup system properties
name|System
operator|.
name|clearProperty
argument_list|(
literal|"scall.name"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"scall.scheme"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"scall.servers1"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"scall.servers2"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"scall.filter"
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|// **********************************************
comment|// test placeholders
comment|// **********************************************
annotation|@
name|Test
DECL|method|testExpression ()
specifier|public
name|void
name|testExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|ServiceCallConfigurationDefinition
name|config
init|=
operator|new
name|ServiceCallConfigurationDefinition
argument_list|()
decl_stmt|;
name|config
operator|.
name|setServiceDiscovery
argument_list|(
operator|new
name|StaticServiceDiscovery
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setExpressionConfiguration
argument_list|(
operator|new
name|ServiceCallExpressionConfiguration
argument_list|()
operator|.
name|expression
argument_list|(
operator|new
name|SimpleExpression
argument_list|(
literal|"file:${header.CamelServiceCallServiceHost}:${header.CamelServiceCallServicePort}"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|setServiceCallConfiguration
argument_list|(
name|config
argument_list|)
expr_stmt|;
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"default"
argument_list|)
operator|.
name|serviceCall
argument_list|(
literal|"scall"
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
name|DefaultServiceCallProcessor
name|proc
init|=
name|findServiceCallProcessor
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"default"
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|proc
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|proc
operator|.
name|getExpression
argument_list|()
operator|instanceof
name|SimpleExpression
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|// **********************************************
comment|// Helper
comment|// **********************************************
DECL|method|findServiceCallProcessor (Route route)
specifier|private
name|DefaultServiceCallProcessor
name|findServiceCallProcessor
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
for|for
control|(
name|Processor
name|processor
range|:
name|route
operator|.
name|navigate
argument_list|()
operator|.
name|next
argument_list|()
control|)
block|{
if|if
condition|(
name|processor
operator|instanceof
name|DefaultChannel
condition|)
block|{
name|processor
operator|=
operator|(
operator|(
name|DefaultChannel
operator|)
name|processor
operator|)
operator|.
name|getNextProcessor
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|processor
operator|instanceof
name|DefaultServiceCallProcessor
condition|)
block|{
return|return
operator|(
name|DefaultServiceCallProcessor
operator|)
name|processor
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to find a ServiceCallProcessor"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

