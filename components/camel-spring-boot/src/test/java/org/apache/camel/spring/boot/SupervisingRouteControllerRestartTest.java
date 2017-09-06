begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|impl
operator|.
name|SupervisingRouteController
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
name|dummy
operator|.
name|DummyComponent
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
name|backoff
operator|.
name|BackOffTimer
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
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
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

begin_import
import|import static
name|org
operator|.
name|awaitility
operator|.
name|Awaitility
operator|.
name|await
import|;
end_import

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
name|SpringBootTest
argument_list|(
name|classes
operator|=
block|{
name|CamelAutoConfiguration
operator|.
name|class
block|,
name|SupervisingRouteControllerAutoConfiguration
operator|.
name|class
block|,
name|SupervisingRouteControllerRestartTest
operator|.
name|TestConfiguration
operator|.
name|class
block|}
argument_list|,
name|properties
operator|=
block|{
literal|"camel.springboot.xml-routes = false"
block|,
literal|"camel.springboot.main-run-controller = true"
block|,
literal|"camel.supervising.controller.enabled = true"
block|,
literal|"camel.supervising.controller.initial-delay = 2s"
block|,
literal|"camel.supervising.controller.default-back-off.delay = 1s"
block|,
literal|"camel.supervising.controller.default-back-off.max-attempts = 10"
block|,
literal|"camel.supervising.controller.routes.bar.back-off.delay = 10s"
block|,
literal|"camel.supervising.controller.routes.bar.back-off.max-attempts = 3"
block|,
literal|"camel.supervising.controller.routes.timer-unmanaged.supervise = false"
block|}
argument_list|)
DECL|class|SupervisingRouteControllerRestartTest
specifier|public
class|class
name|SupervisingRouteControllerRestartTest
block|{
annotation|@
name|Autowired
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Test
DECL|method|testRouteRestart ()
specifier|public
name|void
name|testRouteRestart
parameter_list|()
throws|throws
name|Exception
block|{
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|context
operator|.
name|getRouteController
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|context
operator|.
name|getRouteController
argument_list|()
operator|instanceof
name|SupervisingRouteController
argument_list|)
expr_stmt|;
name|SupervisingRouteController
name|controller
init|=
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|unwrap
argument_list|(
name|SupervisingRouteController
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Wait for the controller to start the routes
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|3
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|untilAsserted
argument_list|(
parameter_list|()
lambda|->
block|{
comment|// now its suspended by the policy
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"dummy"
argument_list|)
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
comment|// restart the dummy route which should fail on first attempt
name|controller
operator|.
name|stopRoute
argument_list|(
literal|"dummy"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"dummy"
argument_list|)
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRouteController
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|controller
operator|.
name|startRoute
argument_list|(
literal|"dummy"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Forced error on restart"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Assert
operator|.
name|assertTrue
argument_list|(
name|controller
operator|.
name|getBackOffContext
argument_list|(
literal|"dummy"
argument_list|)
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|BackOffTimer
operator|.
name|Task
operator|.
name|Status
operator|.
name|Active
argument_list|,
name|controller
operator|.
name|getBackOffContext
argument_list|(
literal|"dummy"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|controller
operator|.
name|getBackOffContext
argument_list|(
literal|"dummy"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|getCurrentAttempts
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// Wait for wile to give time to the controller to start the route
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|untilAsserted
argument_list|(
parameter_list|()
lambda|->
block|{
comment|// now its suspended by the policy
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"dummy"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"dummy"
argument_list|)
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRouteController
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|controller
operator|.
name|getBackOffContext
argument_list|(
literal|"dummy"
argument_list|)
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
comment|// *************************************
comment|// Config
comment|// *************************************
annotation|@
name|Configuration
DECL|class|TestConfiguration
specifier|public
specifier|static
class|class
name|TestConfiguration
block|{
annotation|@
name|Bean
DECL|method|routeBuilder ()
specifier|public
name|RouteBuilder
name|routeBuilder
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
name|getContext
argument_list|()
operator|.
name|addComponent
argument_list|(
literal|"dummy"
argument_list|,
operator|new
name|DummyComponent
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer:foo?period=5s"
argument_list|)
operator|.
name|id
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|startupOrder
argument_list|(
literal|2
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer:bar?period=5s"
argument_list|)
operator|.
name|id
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|startupOrder
argument_list|(
literal|1
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer:unmanaged?period=5s"
argument_list|)
operator|.
name|id
argument_list|(
literal|"timer-unmanaged"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:timer-unmanaged"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer:no-autostartup?period=5s"
argument_list|)
operator|.
name|id
argument_list|(
literal|"timer-no-autostartup"
argument_list|)
operator|.
name|autoStartup
argument_list|(
literal|false
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:timer-no-autostartup"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"dummy:foo?failOnRestart=true"
argument_list|)
operator|.
name|id
argument_list|(
literal|"dummy"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:dummy"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
block|}
end_class

end_unit

