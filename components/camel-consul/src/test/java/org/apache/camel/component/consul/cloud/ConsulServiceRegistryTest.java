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
name|CatalogClient
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
name|HealthClient
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
name|catalog
operator|.
name|CatalogService
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
name|health
operator|.
name|ServiceHealth
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
name|impl
operator|.
name|cloud
operator|.
name|DefaultServiceDefinition
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
name|assertEquals
import|;
end_import

begin_class
DECL|class|ConsulServiceRegistryTest
specifier|public
class|class
name|ConsulServiceRegistryTest
extends|extends
name|ConsulTestSupport
block|{
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
name|Test
DECL|method|testSimpleServiceRegistration ()
specifier|public
name|void
name|testSimpleServiceRegistration
parameter_list|()
block|{
name|ConsulServiceRegistry
name|registry
init|=
operator|new
name|ConsulServiceRegistry
argument_list|()
decl_stmt|;
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
literal|"service-host"
argument_list|)
expr_stmt|;
name|registry
operator|.
name|setOverrideServiceHost
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|registry
operator|.
name|start
argument_list|()
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|DefaultServiceDefinition
operator|.
name|builder
argument_list|()
operator|.
name|withId
argument_list|(
literal|"my-id"
argument_list|)
operator|.
name|withName
argument_list|(
literal|"service-name"
argument_list|)
operator|.
name|withHost
argument_list|(
literal|"my-host"
argument_list|)
operator|.
name|withPort
argument_list|(
literal|9091
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|CatalogClient
name|catalog
init|=
name|getConsul
argument_list|()
operator|.
name|catalogClient
argument_list|()
decl_stmt|;
specifier|final
name|HealthClient
name|health
init|=
name|getConsul
argument_list|()
operator|.
name|healthClient
argument_list|()
decl_stmt|;
comment|// check that service has been registered
name|List
argument_list|<
name|CatalogService
argument_list|>
name|services
init|=
name|catalog
operator|.
name|getService
argument_list|(
literal|"service-name"
argument_list|)
operator|.
name|getResponse
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|services
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9091
argument_list|,
name|services
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getServicePort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"service-host"
argument_list|,
name|services
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getServiceAddress
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"my-id"
argument_list|,
name|services
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getServiceId
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ServiceHealth
argument_list|>
name|checks
init|=
name|health
operator|.
name|getHealthyServiceInstances
argument_list|(
literal|"service-name"
argument_list|)
operator|.
name|getResponse
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|checks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9091
argument_list|,
name|checks
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getService
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"service-host"
argument_list|,
name|checks
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getService
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"my-id"
argument_list|,
name|checks
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getService
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// check that service has been de registered on service registry
comment|// shutdown
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|catalog
operator|.
name|getService
argument_list|(
literal|"service-name"
argument_list|)
operator|.
name|getResponse
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|health
operator|.
name|getServiceChecks
argument_list|(
literal|"service-name"
argument_list|)
operator|.
name|getResponse
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

