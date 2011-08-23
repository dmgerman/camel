begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|CamelContextAware
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
name|Component
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
name|Endpoint
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
name|NoSuchEndpointException
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
name|ResolveEndpointFailedException
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
name|TestSupport
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
name|bean
operator|.
name|BeanComponent
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
name|direct
operator|.
name|DirectComponent
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
name|log
operator|.
name|LogComponent
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
name|spi
operator|.
name|UuidGenerator
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
name|support
operator|.
name|ServiceSupport
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
name|CamelContextHelper
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|DefaultCamelContextTest
specifier|public
class|class
name|DefaultCamelContextTest
extends|extends
name|TestSupport
block|{
DECL|method|testAutoCreateComponentsOn ()
specifier|public
name|void
name|testAutoCreateComponentsOn
parameter_list|()
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
name|Component
name|component
init|=
name|ctx
operator|.
name|getComponent
argument_list|(
literal|"bean"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|component
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|component
operator|.
name|getClass
argument_list|()
argument_list|,
name|BeanComponent
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testAutoCreateComponentsOff ()
specifier|public
name|void
name|testAutoCreateComponentsOff
parameter_list|()
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
name|ctx
operator|.
name|setAutoCreateComponents
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Component
name|component
init|=
name|ctx
operator|.
name|getComponent
argument_list|(
literal|"bean"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateDefaultUuidGenerator ()
specifier|public
name|void
name|testCreateDefaultUuidGenerator
parameter_list|()
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
name|UuidGenerator
name|uuidGenerator
init|=
name|ctx
operator|.
name|getUuidGenerator
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|uuidGenerator
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|uuidGenerator
operator|.
name|getClass
argument_list|()
argument_list|,
name|ActiveMQUuidGenerator
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetComponents ()
specifier|public
name|void
name|testGetComponents
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
name|Component
name|component
init|=
name|ctx
operator|.
name|getComponent
argument_list|(
literal|"bean"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|component
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|ctx
operator|.
name|getComponentNames
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bean"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetEndpoint ()
specifier|public
name|void
name|testGetEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|ctx
operator|.
name|getEndpoint
argument_list|(
literal|"log:foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
try|try
block|{
name|ctx
operator|.
name|getEndpoint
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
DECL|method|testGetEndPointByTypeUnknown ()
specifier|public
name|void
name|testGetEndPointByTypeUnknown
parameter_list|()
block|{
name|DefaultCamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
try|try
block|{
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"xxx"
argument_list|,
name|Endpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchEndpointException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"No endpoint could be found for: xxx, please check your classpath contains the needed Camel component jar."
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testRemoveEndpoint ()
specifier|public
name|void
name|testRemoveEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
name|ctx
operator|.
name|getEndpoint
argument_list|(
literal|"log:foo"
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|getEndpoint
argument_list|(
literal|"log:bar"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|list
init|=
name|ctx
operator|.
name|removeEndpoints
argument_list|(
literal|"log:foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"log://foo"
argument_list|,
name|list
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|getEndpoint
argument_list|(
literal|"log:baz"
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|getEndpoint
argument_list|(
literal|"seda:cool"
argument_list|)
expr_stmt|;
name|list
operator|=
name|ctx
operator|.
name|removeEndpoints
argument_list|(
literal|"log:*"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Endpoint
argument_list|>
name|it
init|=
name|list
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"log://bar"
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"log://baz"
argument_list|,
name|it
operator|.
name|next
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ctx
operator|.
name|getEndpoints
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetEndpointNotFound ()
specifier|public
name|void
name|testGetEndpointNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
try|try
block|{
name|ctx
operator|.
name|getEndpoint
argument_list|(
literal|"xxx:foo"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a ResolveEndpointFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResolveEndpointFailedException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"No component found with scheme: xxx"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testGetEndpointNoScheme ()
specifier|public
name|void
name|testGetEndpointNoScheme
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
try|try
block|{
name|CamelContextHelper
operator|.
name|getMandatoryEndpoint
argument_list|(
name|ctx
argument_list|,
literal|"log.foo"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a NoSuchEndpointException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchEndpointException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
DECL|method|testRestartCamelContext ()
specifier|public
name|void
name|testRestartCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
name|ctx
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
literal|"direct:endpointA"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpointB"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have one RouteService"
argument_list|,
literal|1
argument_list|,
name|ctx
operator|.
name|getRouteServices
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|routesString
init|=
name|ctx
operator|.
name|getRoutes
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The RouteService should NOT be removed even when we stop"
argument_list|,
literal|1
argument_list|,
name|ctx
operator|.
name|getRouteServices
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have one RouteService"
argument_list|,
literal|1
argument_list|,
name|ctx
operator|.
name|getRouteServices
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The Routes should be same"
argument_list|,
name|routesString
argument_list|,
name|ctx
operator|.
name|getRoutes
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The RouteService should NOT be removed even when we stop"
argument_list|,
literal|1
argument_list|,
name|ctx
operator|.
name|getRouteServices
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testName ()
specifier|public
name|void
name|testName
parameter_list|()
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have a default name"
argument_list|,
name|ctx
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|setName
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|ctx
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ctx
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ctx
operator|.
name|isAutoStartup
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testVersion ()
specifier|public
name|void
name|testVersion
parameter_list|()
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have a version"
argument_list|,
name|ctx
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testHasComponent ()
specifier|public
name|void
name|testHasComponent
parameter_list|()
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|ctx
operator|.
name|hasComponent
argument_list|(
literal|"log"
argument_list|)
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|addComponent
argument_list|(
literal|"log"
argument_list|,
operator|new
name|LogComponent
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ctx
operator|.
name|hasComponent
argument_list|(
literal|"log"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetComponent ()
specifier|public
name|void
name|testGetComponent
parameter_list|()
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
name|ctx
operator|.
name|addComponent
argument_list|(
literal|"log"
argument_list|,
operator|new
name|LogComponent
argument_list|()
argument_list|)
expr_stmt|;
name|LogComponent
name|log
init|=
name|ctx
operator|.
name|getComponent
argument_list|(
literal|"log"
argument_list|,
name|LogComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|log
argument_list|)
expr_stmt|;
try|try
block|{
name|ctx
operator|.
name|addComponent
argument_list|(
literal|"direct"
argument_list|,
operator|new
name|DirectComponent
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|getComponent
argument_list|(
literal|"log"
argument_list|,
name|DirectComponent
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
DECL|method|testGetEndpointMap ()
specifier|public
name|void
name|testGetEndpointMap
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
name|ctx
operator|.
name|addEndpoint
argument_list|(
literal|"mock://foo"
argument_list|,
operator|new
name|MockEndpoint
argument_list|(
literal|"mock://foo"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Endpoint
argument_list|>
name|map
init|=
name|ctx
operator|.
name|getEndpointMap
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testHasEndpoint ()
specifier|public
name|void
name|testHasEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
name|ctx
operator|.
name|addEndpoint
argument_list|(
literal|"mock://foo"
argument_list|,
operator|new
name|MockEndpoint
argument_list|(
literal|"mock://foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ctx
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|ctx
operator|.
name|hasEndpoint
argument_list|(
literal|"mock://bar"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|Endpoint
name|endpoint
init|=
name|ctx
operator|.
name|hasEndpoint
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Should not have endpoint"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResolveEndpointFailedException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
DECL|method|testGetRouteById ()
specifier|public
name|void
name|testGetRouteById
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
comment|// should not throw NPE (CAMEL-3198)
name|Route
name|route
init|=
name|ctx
operator|.
name|getRoute
argument_list|(
literal|"coolRoute"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|ctx
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
literal|"coolRoute"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|start
argument_list|()
expr_stmt|;
name|route
operator|=
name|ctx
operator|.
name|getRoute
argument_list|(
literal|"coolRoute"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"coolRoute"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct://start"
argument_list|,
name|route
operator|.
name|getConsumer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|ctx
operator|.
name|getRoute
argument_list|(
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testSuspend ()
specifier|public
name|void
name|testSuspend
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|ctx
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|suspend
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|ctx
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|suspend
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|ctx
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testResume ()
specifier|public
name|void
name|testResume
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|ctx
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|resume
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|ctx
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|resume
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|ctx
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSuspendResume ()
specifier|public
name|void
name|testSuspendResume
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|ctx
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|suspend
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|ctx
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|resume
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|ctx
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|ctx
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAddServiceInjectCamelContext ()
specifier|public
name|void
name|testAddServiceInjectCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|MyService
name|my
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|DefaultCamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|addService
argument_list|(
name|my
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ctx
argument_list|,
name|my
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
name|my
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Stopped"
argument_list|,
name|my
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyService
specifier|private
specifier|static
class|class
name|MyService
extends|extends
name|ServiceSupport
implements|implements
name|CamelContextAware
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
block|}
end_class

end_unit

