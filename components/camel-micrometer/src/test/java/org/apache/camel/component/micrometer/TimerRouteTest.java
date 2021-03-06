begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
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
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|MeterRegistry
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|Timer
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|simple
operator|.
name|SimpleMeterRegistry
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
name|spring
operator|.
name|javaconfig
operator|.
name|SingleRouteCamelConfiguration
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
name|spring
operator|.
name|CamelSpringDelegatingTestContextLoader
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
name|spring
operator|.
name|CamelSpringRunner
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
name|spring
operator|.
name|MockEndpoints
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
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|HEADER_METRIC_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|HEADER_TIMER_ACTION
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|METRICS_REGISTRY_NAME
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
name|assertTrue
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|CamelSpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|classes
operator|=
block|{
name|TimerRouteTest
operator|.
name|TestConfig
operator|.
name|class
block|}
argument_list|,
name|loader
operator|=
name|CamelSpringDelegatingTestContextLoader
operator|.
name|class
argument_list|)
annotation|@
name|MockEndpoints
DECL|class|TimerRouteTest
specifier|public
class|class
name|TimerRouteTest
block|{
DECL|field|DELAY
specifier|private
specifier|static
specifier|final
name|long
name|DELAY
init|=
literal|20L
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:out"
argument_list|)
DECL|field|endpoint
specifier|private
name|MockEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|Produce
argument_list|(
literal|"direct:in-1"
argument_list|)
DECL|field|producer1
specifier|private
name|ProducerTemplate
name|producer1
decl_stmt|;
annotation|@
name|Produce
argument_list|(
literal|"direct:in-2"
argument_list|)
DECL|field|producer2
specifier|private
name|ProducerTemplate
name|producer2
decl_stmt|;
annotation|@
name|Produce
argument_list|(
literal|"direct:in-3"
argument_list|)
DECL|field|producer3
specifier|private
name|ProducerTemplate
name|producer3
decl_stmt|;
DECL|field|registry
specifier|private
name|MeterRegistry
name|registry
decl_stmt|;
annotation|@
name|Configuration
DECL|class|TestConfig
specifier|public
specifier|static
class|class
name|TestConfig
extends|extends
name|SingleRouteCamelConfiguration
block|{
annotation|@
name|Bean
annotation|@
name|Override
DECL|method|route ()
specifier|public
name|RouteBuilder
name|route
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
block|{
name|from
argument_list|(
literal|"direct:in-1"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HEADER_METRIC_NAME
argument_list|,
name|constant
argument_list|(
literal|"B"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"micrometer:timer:A?action=start"
argument_list|)
operator|.
name|delay
argument_list|(
name|DELAY
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HEADER_METRIC_NAME
argument_list|,
name|constant
argument_list|(
literal|"B"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"micrometer:timer:A?action=stop"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:in-2"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HEADER_TIMER_ACTION
argument_list|,
name|constant
argument_list|(
name|MicrometerTimerAction
operator|.
name|start
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"micrometer:timer:A"
argument_list|)
operator|.
name|delay
argument_list|(
name|DELAY
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HEADER_TIMER_ACTION
argument_list|,
name|constant
argument_list|(
name|MicrometerTimerAction
operator|.
name|stop
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"micrometer:timer:A"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:in-3"
argument_list|)
operator|.
name|to
argument_list|(
literal|"micrometer:timer:C?action=start"
argument_list|)
operator|.
name|delay
argument_list|(
name|DELAY
argument_list|)
operator|.
name|to
argument_list|(
literal|"micrometer:timer:C?action=stop&tags=a=${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Bean
argument_list|(
name|name
operator|=
name|METRICS_REGISTRY_NAME
argument_list|)
DECL|method|getMetricRegistry ()
specifier|public
name|MeterRegistry
name|getMetricRegistry
parameter_list|()
block|{
return|return
operator|new
name|SimpleMeterRegistry
argument_list|()
return|;
block|}
block|}
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
block|{
name|registry
operator|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|METRICS_REGISTRY_NAME
argument_list|,
name|MeterRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|endpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOverrideMetricsName ()
specifier|public
name|void
name|testOverrideMetricsName
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|producer1
operator|.
name|sendBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|Timer
name|timer
init|=
name|registry
operator|.
name|find
argument_list|(
literal|"B"
argument_list|)
operator|.
name|timer
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1L
argument_list|,
name|timer
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|timer
operator|.
name|max
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
operator|>
literal|0.0D
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOverrideNoAction ()
specifier|public
name|void
name|testOverrideNoAction
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|producer2
operator|.
name|sendBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|Timer
name|timer
init|=
name|registry
operator|.
name|find
argument_list|(
literal|"A"
argument_list|)
operator|.
name|timer
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1L
argument_list|,
name|timer
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|timer
operator|.
name|max
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
operator|>
literal|0.0D
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNormal ()
specifier|public
name|void
name|testNormal
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|count
init|=
literal|10
decl_stmt|;
name|String
name|body
init|=
literal|"Hello"
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
name|count
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
name|producer3
operator|.
name|sendBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
name|Timer
name|timer
init|=
name|registry
operator|.
name|find
argument_list|(
literal|"C"
argument_list|)
operator|.
name|timer
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|count
argument_list|,
name|timer
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|timer
operator|.
name|max
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
operator|>
name|DELAY
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|timer
operator|.
name|mean
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
operator|>
name|DELAY
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|timer
operator|.
name|totalTime
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
operator|>
name|DELAY
operator|*
name|count
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|body
argument_list|,
name|timer
operator|.
name|getId
argument_list|()
operator|.
name|getTag
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

