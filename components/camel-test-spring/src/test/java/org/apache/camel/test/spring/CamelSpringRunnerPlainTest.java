begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|spring
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
name|EndpointInject
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
name|Produce
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
name|ProducerTemplate
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
name|management
operator|.
name|DefaultManagementStrategy
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
name|StopWatch
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
name|annotation
operator|.
name|DirtiesContext
operator|.
name|ClassMode
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
name|BootstrapWith
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
name|ContextConfiguration
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
name|assertEquals
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
name|assertNull
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

begin_comment
comment|// START SNIPPET: e1
end_comment

begin_comment
comment|// tag::example[]
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|CamelSpringRunner
operator|.
name|class
argument_list|)
comment|// must tell Spring to bootstrap with Camel
annotation|@
name|BootstrapWith
argument_list|(
name|CamelTestContextBootstrapper
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|()
comment|// Put here to prevent Spring context caching across tests and test methods since some tests inherit
comment|// from this test and therefore use the same Spring context.  Also because we want to reset the
comment|// Camel context and mock endpoints between test methods automatically.
annotation|@
name|DirtiesContext
argument_list|(
name|classMode
operator|=
name|ClassMode
operator|.
name|AFTER_EACH_TEST_METHOD
argument_list|)
DECL|class|CamelSpringRunnerPlainTest
specifier|public
class|class
name|CamelSpringRunnerPlainTest
block|{
annotation|@
name|Autowired
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Autowired
DECL|field|camelContext2
specifier|protected
name|CamelContext
name|camelContext2
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:a"
argument_list|,
name|context
operator|=
literal|"camelContext"
argument_list|)
DECL|field|mockA
specifier|protected
name|MockEndpoint
name|mockA
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:b"
argument_list|,
name|context
operator|=
literal|"camelContext"
argument_list|)
DECL|field|mockB
specifier|protected
name|MockEndpoint
name|mockB
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:c"
argument_list|,
name|context
operator|=
literal|"camelContext2"
argument_list|)
DECL|field|mockC
specifier|protected
name|MockEndpoint
name|mockC
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|,
name|context
operator|=
literal|"camelContext"
argument_list|)
DECL|field|start
specifier|protected
name|ProducerTemplate
name|start
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start2"
argument_list|,
name|context
operator|=
literal|"camelContext2"
argument_list|)
DECL|field|start2
specifier|protected
name|ProducerTemplate
name|start2
decl_stmt|;
annotation|@
name|Test
DECL|method|testPositive ()
specifier|public
name|void
name|testPositive
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|camelContext
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|camelContext2
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|mockA
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"David"
argument_list|)
expr_stmt|;
name|mockB
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello David"
argument_list|)
expr_stmt|;
name|mockC
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"David"
argument_list|)
expr_stmt|;
name|start
operator|.
name|sendBody
argument_list|(
literal|"David"
argument_list|)
expr_stmt|;
name|start2
operator|.
name|sendBody
argument_list|(
literal|"David"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
name|camelContext2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testJmx ()
specifier|public
name|void
name|testJmx
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|DefaultManagementStrategy
operator|.
name|class
argument_list|,
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testShutdownTimeout ()
specifier|public
name|void
name|testShutdownTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|camelContext
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
name|camelContext
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|getTimeUnit
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStopwatch ()
specifier|public
name|void
name|testStopwatch
parameter_list|()
block|{
name|StopWatch
name|stopWatch
init|=
name|StopWatchTestExecutionListener
operator|.
name|getStopWatch
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|stopWatch
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|stopWatch
operator|.
name|taken
argument_list|()
operator|<
literal|100
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExcludedRoute ()
specifier|public
name|void
name|testExcludedRoute
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|camelContext
operator|.
name|getRoute
argument_list|(
literal|"excludedRoute"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProvidesBreakpoint ()
specifier|public
name|void
name|testProvidesBreakpoint
parameter_list|()
block|{
name|assertNull
argument_list|(
name|camelContext
operator|.
name|getDebugger
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|camelContext2
operator|.
name|getDebugger
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRouteCoverage ()
specifier|public
name|void
name|testRouteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

begin_comment
comment|// end::example[]
end_comment

begin_comment
comment|// END SNIPPET: e1
end_comment

end_unit

