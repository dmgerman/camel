begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.microprofile.metrics.event.notifier.route
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|event
operator|.
name|notifier
operator|.
name|route
package|;
end_package

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
name|RoutesBuilder
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
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|Gauge
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|DEFAULT_CAMEL_ROUTES_ADDED_METRIC_NAME
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
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsConstants
operator|.
name|DEFAULT_CAMEL_ROUTES_RUNNING_METRIC_NAME
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
name|microprofile
operator|.
name|metrics
operator|.
name|MicroProfileMetricsHelper
operator|.
name|findMetric
import|;
end_import

begin_class
DECL|class|MicroProfileMetricsRouteEventNotifierTest
specifier|public
class|class
name|MicroProfileMetricsRouteEventNotifierTest
extends|extends
name|MicroProfileMetricsTestSupport
block|{
DECL|field|eventNotifier
specifier|private
name|MicroProfileMetricsRouteEventNotifier
name|eventNotifier
decl_stmt|;
annotation|@
name|Test
DECL|method|testMicroProfileMetricsRouteEventNotifier ()
specifier|public
name|void
name|testMicroProfileMetricsRouteEventNotifier
parameter_list|()
throws|throws
name|Exception
block|{
name|Gauge
name|routesAdded
init|=
name|findMetric
argument_list|(
name|metricRegistry
argument_list|,
name|DEFAULT_CAMEL_ROUTES_ADDED_METRIC_NAME
argument_list|,
name|Gauge
operator|.
name|class
argument_list|)
decl_stmt|;
name|Gauge
name|routesRunning
init|=
name|findMetric
argument_list|(
name|metricRegistry
argument_list|,
name|DEFAULT_CAMEL_ROUTES_RUNNING_METRIC_NAME
argument_list|,
name|Gauge
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|routesAdded
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|routesRunning
operator|.
name|getValue
argument_list|()
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
literal|"direct:foo"
argument_list|)
operator|.
name|id
argument_list|(
literal|"test"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|routesAdded
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|routesRunning
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|routesAdded
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|routesRunning
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeRoute
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|routesAdded
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|routesRunning
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMicroProfileMetricsRouteEventNotifierStop ()
specifier|public
name|void
name|testMicroProfileMetricsRouteEventNotifierStop
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|metricRegistry
operator|.
name|getMetricIDs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|eventNotifier
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|metricRegistry
operator|.
name|getMetricIDs
argument_list|()
operator|.
name|size
argument_list|()
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
throws|throws
name|Exception
block|{
name|eventNotifier
operator|=
operator|new
name|MicroProfileMetricsRouteEventNotifier
argument_list|()
expr_stmt|;
name|eventNotifier
operator|.
name|setMetricRegistry
argument_list|(
name|metricRegistry
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
name|eventNotifier
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

