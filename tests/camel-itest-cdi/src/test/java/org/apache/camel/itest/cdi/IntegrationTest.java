begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|cdi
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|component
operator|.
name|cdi
operator|.
name|internal
operator|.
name|CamelContextMap
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
name|cdi
operator|.
name|internal
operator|.
name|CamelExtension
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
name|jboss
operator|.
name|arquillian
operator|.
name|container
operator|.
name|test
operator|.
name|api
operator|.
name|Deployment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|arquillian
operator|.
name|junit
operator|.
name|Arquillian
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|ShrinkWrap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|asset
operator|.
name|EmptyAsset
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|shrinkwrap
operator|.
name|api
operator|.
name|spec
operator|.
name|JavaArchive
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
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
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Arquillian
operator|.
name|class
argument_list|)
DECL|class|IntegrationTest
specifier|public
class|class
name|IntegrationTest
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|IntegrationTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Inject
DECL|field|camelContextMap
name|CamelContextMap
name|camelContextMap
decl_stmt|;
annotation|@
name|Inject
DECL|field|routesA
name|RoutesContextA
name|routesA
decl_stmt|;
annotation|@
name|Inject
DECL|field|routesB
name|RoutesContextB
name|routesB
decl_stmt|;
annotation|@
name|Inject
DECL|field|routesC
name|RoutesContextC
name|routesC
decl_stmt|;
annotation|@
name|Inject
DECL|field|routesD
name|RoutesContextD
name|routesD
decl_stmt|;
annotation|@
name|Test
DECL|method|checkContextsHaveCorrectEndpointsAndRoutes ()
specifier|public
name|void
name|checkContextsHaveCorrectEndpointsAndRoutes
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|CamelContext
argument_list|>
argument_list|>
name|entries
init|=
name|camelContextMap
operator|.
name|getCamelContextMap
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|CamelContext
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"CamelContext "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" has endpoints: "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getEndpointMap
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|CamelContext
name|contextA
init|=
name|assertCamelContext
argument_list|(
literal|"contextA"
argument_list|)
decl_stmt|;
name|assertHasEndpoints
argument_list|(
name|contextA
argument_list|,
literal|"seda://A.a"
argument_list|,
literal|"mock://A.b"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockEndpoint
init|=
name|routesA
operator|.
name|b
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|Constants
operator|.
name|EXPECTED_BODIES_A
argument_list|)
expr_stmt|;
name|routesA
operator|.
name|sendMessages
argument_list|()
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|CamelContext
name|contextB
init|=
name|assertCamelContext
argument_list|(
literal|"contextB"
argument_list|)
decl_stmt|;
name|assertHasEndpoints
argument_list|(
name|contextB
argument_list|,
literal|"seda://B.a"
argument_list|,
literal|"mock://B.b"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockEndpointB
init|=
name|routesB
operator|.
name|b
decl_stmt|;
name|mockEndpointB
operator|.
name|expectedBodiesReceived
argument_list|(
name|Constants
operator|.
name|EXPECTED_BODIES_B
argument_list|)
expr_stmt|;
name|routesB
operator|.
name|sendMessages
argument_list|()
expr_stmt|;
name|mockEndpointB
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|CamelContext
name|contextC
init|=
name|assertCamelContext
argument_list|(
literal|"contextC"
argument_list|)
decl_stmt|;
name|assertHasEndpoints
argument_list|(
name|contextC
argument_list|,
literal|"seda://C.a"
argument_list|,
literal|"mock://C.b"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockEndpointC
init|=
name|routesC
operator|.
name|b
decl_stmt|;
name|mockEndpointC
operator|.
name|expectedBodiesReceived
argument_list|(
name|Constants
operator|.
name|EXPECTED_BODIES_C
argument_list|)
expr_stmt|;
name|routesC
operator|.
name|sendMessages
argument_list|()
expr_stmt|;
name|mockEndpointC
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|CamelContext
name|contextD
init|=
name|assertCamelContext
argument_list|(
literal|"contextD"
argument_list|)
decl_stmt|;
name|assertHasEndpoints
argument_list|(
name|contextD
argument_list|,
literal|"seda://D.a"
argument_list|,
literal|"mock://D.b"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockEndpointD
init|=
name|routesD
operator|.
name|b
decl_stmt|;
name|mockEndpointD
operator|.
name|expectedBodiesReceived
argument_list|(
name|Constants
operator|.
name|EXPECTED_BODIES_D
argument_list|)
expr_stmt|;
name|routesD
operator|.
name|sendMessages
argument_list|()
expr_stmt|;
name|mockEndpointD
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|assertHasEndpoints (CamelContext context, String... uris)
specifier|public
specifier|static
name|void
name|assertHasEndpoints
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
modifier|...
name|uris
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Endpoint
argument_list|>
name|endpointMap
init|=
name|context
operator|.
name|getEndpointMap
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|uri
range|:
name|uris
control|)
block|{
name|Endpoint
name|endpoint
init|=
name|endpointMap
operator|.
name|get
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"CamelContext "
operator|+
name|context
operator|+
literal|" does not have an Endpoint with URI "
operator|+
name|uri
operator|+
literal|" but has "
operator|+
name|endpointMap
operator|.
name|keySet
argument_list|()
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertCamelContext (String contextName)
specifier|protected
name|CamelContext
name|assertCamelContext
parameter_list|(
name|String
name|contextName
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"camelContextMap not injected!"
argument_list|,
name|camelContextMap
argument_list|)
expr_stmt|;
name|CamelContext
name|answer
init|=
name|camelContextMap
operator|.
name|getMandatoryCamelContext
argument_list|(
name|contextName
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"CamelContext '"
operator|+
name|contextName
operator|+
literal|"' is not started"
argument_list|,
name|answer
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Deployment
DECL|method|createDeployment ()
specifier|public
specifier|static
name|JavaArchive
name|createDeployment
parameter_list|()
block|{
return|return
name|ShrinkWrap
operator|.
name|create
argument_list|(
name|JavaArchive
operator|.
name|class
argument_list|)
operator|.
name|addPackage
argument_list|(
name|CamelExtension
operator|.
name|class
operator|.
name|getPackage
argument_list|()
argument_list|)
operator|.
name|addPackage
argument_list|(
name|RoutesContextA
operator|.
name|class
operator|.
name|getPackage
argument_list|()
argument_list|)
operator|.
name|addAsManifestResource
argument_list|(
name|EmptyAsset
operator|.
name|INSTANCE
argument_list|,
literal|"beans.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

