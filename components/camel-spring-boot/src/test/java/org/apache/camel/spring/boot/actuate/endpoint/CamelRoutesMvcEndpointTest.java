begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.actuate.endpoint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|actuate
operator|.
name|endpoint
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
name|spring
operator|.
name|boot
operator|.
name|CamelAutoConfiguration
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
name|spring
operator|.
name|boot
operator|.
name|actuate
operator|.
name|endpoint
operator|.
name|CamelRoutesEndpoint
operator|.
name|RouteDetailsEndpointInfo
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
name|spring
operator|.
name|boot
operator|.
name|actuate
operator|.
name|endpoint
operator|.
name|CamelRoutesEndpoint
operator|.
name|RouteEndpointInfo
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|EnableAutoConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|SpringBootApplication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|context
operator|.
name|SpringBootTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringRunner
import|;
end_import

begin_comment
comment|/**  * Test for the {@link CamelRoutesEndpoint} actuator endpoint.  */
end_comment

begin_class
annotation|@
name|DirtiesContext
annotation|@
name|RunWith
argument_list|(
name|SpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|EnableAutoConfiguration
annotation|@
name|SpringBootApplication
annotation|@
name|SpringBootTest
argument_list|(
name|classes
operator|=
block|{
name|CamelAutoConfiguration
operator|.
name|class
block|,
name|CamelRoutesEndpointAutoConfiguration
operator|.
name|class
block|,
name|ActuatorTestRoute
operator|.
name|class
block|}
argument_list|)
DECL|class|CamelRoutesMvcEndpointTest
specifier|public
class|class
name|CamelRoutesMvcEndpointTest
extends|extends
name|Assert
block|{
annotation|@
name|Autowired
DECL|field|endpoint
name|CamelRoutesMvcEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|Autowired
DECL|field|camelContext
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Test
DECL|method|testRoutesEndpoint ()
specifier|public
name|void
name|testRoutesEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|RouteEndpointInfo
argument_list|>
name|routes
init|=
operator|(
name|List
argument_list|<
name|RouteEndpointInfo
argument_list|>
operator|)
name|endpoint
operator|.
name|invoke
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|routes
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|routes
operator|.
name|size
argument_list|()
argument_list|,
name|camelContext
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|routes
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|r
lambda|->
literal|"foo-route"
operator|.
name|equals
argument_list|(
name|r
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMvcRoutesEndpoint ()
specifier|public
name|void
name|testMvcRoutesEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|endpoint
operator|.
name|get
argument_list|(
literal|"foo-route"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|instanceof
name|RouteDetailsEndpointInfo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo-route"
argument_list|,
operator|(
operator|(
name|RouteDetailsEndpointInfo
operator|)
name|result
operator|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

