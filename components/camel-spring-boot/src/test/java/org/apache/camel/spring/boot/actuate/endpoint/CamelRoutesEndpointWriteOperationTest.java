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
name|ReadAction
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
name|TimeInfo
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
name|WriteAction
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
argument_list|,
name|properties
operator|=
block|{
literal|"management.endpoint.camelroutes.read-only = false"
block|}
argument_list|)
DECL|class|CamelRoutesEndpointWriteOperationTest
specifier|public
class|class
name|CamelRoutesEndpointWriteOperationTest
extends|extends
name|Assert
block|{
annotation|@
name|Autowired
DECL|field|endpoint
name|CamelRoutesEndpoint
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
DECL|method|testWriteOperation ()
specifier|public
name|void
name|testWriteOperation
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceStatus
name|status
init|=
name|camelContext
operator|.
name|getRouteStatus
argument_list|(
literal|"foo-route"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|status
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|TimeInfo
name|timeInfo
init|=
operator|new
name|TimeInfo
argument_list|()
decl_stmt|;
name|timeInfo
operator|.
name|setAbortAfterTimeout
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|timeInfo
operator|.
name|setTimeout
argument_list|(
literal|5L
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|doWriteAction
argument_list|(
literal|"foo-route"
argument_list|,
name|WriteAction
operator|.
name|STOP
argument_list|,
name|timeInfo
argument_list|)
expr_stmt|;
name|status
operator|=
name|camelContext
operator|.
name|getRouteStatus
argument_list|(
literal|"foo-route"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|status
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|doWriteAction
argument_list|(
literal|"foo-route"
argument_list|,
name|WriteAction
operator|.
name|START
argument_list|,
name|timeInfo
argument_list|)
expr_stmt|;
name|status
operator|=
name|camelContext
operator|.
name|getRouteStatus
argument_list|(
literal|"foo-route"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|status
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRouteDump ()
specifier|public
name|void
name|testRouteDump
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dump
init|=
name|endpoint
operator|.
name|getRouteDump
argument_list|(
literal|"foo-route"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dump
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dump
argument_list|,
name|dump
operator|.
name|contains
argument_list|(
literal|"<route "
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dump
argument_list|,
name|dump
operator|.
name|contains
argument_list|(
literal|"<from "
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dump
argument_list|,
name|dump
operator|.
name|contains
argument_list|(
literal|"uri=\"timer:foo\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dump
argument_list|,
name|dump
operator|.
name|contains
argument_list|(
literal|"<to "
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dump
argument_list|,
name|dump
operator|.
name|contains
argument_list|(
literal|"uri=\"log:foo\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dump
argument_list|,
name|dump
operator|.
name|contains
argument_list|(
literal|"</route>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

