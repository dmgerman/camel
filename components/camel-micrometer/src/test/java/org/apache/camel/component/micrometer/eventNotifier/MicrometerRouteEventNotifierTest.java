begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *       http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  *  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer.eventNotifier
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
operator|.
name|eventNotifier
package|;
end_package

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
name|Gauge
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
name|micrometer
operator|.
name|eventnotifier
operator|.
name|AbstractMicrometerEventNotifier
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
name|micrometer
operator|.
name|eventnotifier
operator|.
name|MicrometerRouteEventNotifier
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
name|EventNotifier
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
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|DEFAULT_CAMEL_ROUTES_ADDED
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
name|DEFAULT_CAMEL_ROUTES_RUNNING
import|;
end_import

begin_comment
comment|/**  * @author Christian Ohr  */
end_comment

begin_class
DECL|class|MicrometerRouteEventNotifierTest
specifier|public
class|class
name|MicrometerRouteEventNotifierTest
extends|extends
name|AbstractMicrometerEventNotifierTest
block|{
DECL|field|ROUTE_ID
specifier|private
specifier|static
specifier|final
name|String
name|ROUTE_ID
init|=
literal|"test"
decl_stmt|;
annotation|@
name|Override
DECL|method|getEventNotifier ()
specifier|protected
name|AbstractMicrometerEventNotifier
argument_list|<
name|?
argument_list|>
name|getEventNotifier
parameter_list|()
block|{
return|return
operator|new
name|MicrometerRouteEventNotifier
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|testCamelRouteEvents ()
specifier|public
name|void
name|testCamelRouteEvents
parameter_list|()
throws|throws
name|Exception
block|{
name|Gauge
name|added
init|=
name|meterRegistry
operator|.
name|find
argument_list|(
name|DEFAULT_CAMEL_ROUTES_ADDED
argument_list|)
operator|.
name|gauge
argument_list|()
decl_stmt|;
name|Gauge
name|running
init|=
name|meterRegistry
operator|.
name|find
argument_list|(
name|DEFAULT_CAMEL_ROUTES_RUNNING
argument_list|)
operator|.
name|gauge
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0.0d
argument_list|,
name|added
operator|.
name|value
argument_list|()
argument_list|,
literal|0.0001d
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.0d
argument_list|,
name|running
operator|.
name|value
argument_list|()
argument_list|,
literal|0.0001d
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|TestRoute
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0d
argument_list|,
name|added
operator|.
name|value
argument_list|()
argument_list|,
literal|0.0001d
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0d
argument_list|,
name|running
operator|.
name|value
argument_list|()
argument_list|,
literal|0.0001d
argument_list|)
expr_stmt|;
name|context
operator|.
name|stopRoute
argument_list|(
name|ROUTE_ID
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1.0d
argument_list|,
name|added
operator|.
name|value
argument_list|()
argument_list|,
literal|0.0001d
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.0d
argument_list|,
name|running
operator|.
name|value
argument_list|()
argument_list|,
literal|0.0001d
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeRoute
argument_list|(
name|ROUTE_ID
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.0d
argument_list|,
name|added
operator|.
name|value
argument_list|()
argument_list|,
literal|0.0001d
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.0d
argument_list|,
name|running
operator|.
name|value
argument_list|()
argument_list|,
literal|0.0001d
argument_list|)
expr_stmt|;
block|}
DECL|class|TestRoute
specifier|private
class|class
name|TestRoute
extends|extends
name|RouteBuilder
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
literal|"direct:in"
argument_list|)
operator|.
name|routeId
argument_list|(
name|ROUTE_ID
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

