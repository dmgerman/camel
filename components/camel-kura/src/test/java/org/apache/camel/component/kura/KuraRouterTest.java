begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kura
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kura
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
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
name|ServiceStatus
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
name|ModelCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
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
name|Assert
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

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationAdmin
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|anyString
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|BDDMockito
operator|.
name|given
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|RETURNS_DEEP_STUBS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_class
DECL|class|KuraRouterTest
specifier|public
class|class
name|KuraRouterTest
extends|extends
name|Assert
block|{
DECL|field|router
name|TestKuraRouter
name|router
init|=
operator|new
name|TestKuraRouter
argument_list|()
decl_stmt|;
DECL|field|bundleContext
name|BundleContext
name|bundleContext
init|=
name|mock
argument_list|(
name|BundleContext
operator|.
name|class
argument_list|,
name|RETURNS_DEEP_STUBS
argument_list|)
decl_stmt|;
DECL|field|configurationAdmin
name|ConfigurationAdmin
name|configurationAdmin
init|=
name|mock
argument_list|(
name|ConfigurationAdmin
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
name|Configuration
name|configuration
init|=
name|mock
argument_list|(
name|Configuration
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Before
DECL|method|before ()
specifier|public
name|void
name|before
parameter_list|()
throws|throws
name|Exception
block|{
name|given
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getVersion
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|willReturn
argument_list|(
literal|"version"
argument_list|)
expr_stmt|;
name|given
argument_list|(
name|bundleContext
operator|.
name|getBundle
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
operator|.
name|willReturn
argument_list|(
literal|"symbolic_name"
argument_list|)
expr_stmt|;
name|given
argument_list|(
name|bundleContext
operator|.
name|getService
argument_list|(
name|any
argument_list|(
name|ServiceReference
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
name|configurationAdmin
argument_list|)
expr_stmt|;
name|router
operator|.
name|start
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|after ()
specifier|public
name|void
name|after
parameter_list|()
throws|throws
name|Exception
block|{
name|router
operator|.
name|stop
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldCloseCamelContext ()
specifier|public
name|void
name|shouldCloseCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
comment|// When
name|router
operator|.
name|stop
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
comment|// Then
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
name|router
operator|.
name|camelContext
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldStartCamelContext ()
specifier|public
name|void
name|shouldStartCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given
name|String
name|message
init|=
literal|"foo"
decl_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|router
operator|.
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"mock:test"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// When
name|router
operator|.
name|producerTemplate
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|message
argument_list|)
expr_stmt|;
comment|// Then
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldCreateConsumerTemplate ()
specifier|public
name|void
name|shouldCreateConsumerTemplate
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|router
operator|.
name|consumerTemplate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldReturnNoService ()
specifier|public
name|void
name|shouldReturnNoService
parameter_list|()
block|{
name|given
argument_list|(
name|bundleContext
operator|.
name|getServiceReference
argument_list|(
name|any
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|router
operator|.
name|service
argument_list|(
name|ConfigurationAdmin
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldReturnService ()
specifier|public
name|void
name|shouldReturnService
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|router
operator|.
name|service
argument_list|(
name|ConfigurationAdmin
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
name|IllegalStateException
operator|.
name|class
argument_list|)
DECL|method|shouldValidateLackOfService ()
specifier|public
name|void
name|shouldValidateLackOfService
parameter_list|()
block|{
name|given
argument_list|(
name|bundleContext
operator|.
name|getServiceReference
argument_list|(
name|any
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|router
operator|.
name|requiredService
argument_list|(
name|ConfigurationAdmin
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldLoadXmlRoutes ()
specifier|public
name|void
name|shouldLoadXmlRoutes
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given
name|given
argument_list|(
name|configurationAdmin
operator|.
name|getConfiguration
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|Hashtable
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|routeDefinition
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/route.xml"
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"kura.camel.symbolic_name.route"
argument_list|,
name|routeDefinition
argument_list|)
expr_stmt|;
name|given
argument_list|(
name|configuration
operator|.
name|getProperties
argument_list|()
argument_list|)
operator|.
name|willReturn
argument_list|(
name|properties
argument_list|)
expr_stmt|;
comment|// When
name|router
operator|.
name|start
argument_list|(
name|router
operator|.
name|bundleContext
argument_list|)
expr_stmt|;
comment|// Then
name|assertNotNull
argument_list|(
name|router
operator|.
name|camelContext
operator|.
name|adapt
argument_list|(
name|ModelCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getRouteDefinition
argument_list|(
literal|"loaded"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|TestKuraRouter
specifier|static
class|class
name|TestKuraRouter
extends|extends
name|KuraRouter
block|{
annotation|@
name|Override
DECL|method|configure ()
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
literal|"mock:test"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
block|{
return|return
operator|new
name|DefaultCamelContext
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

