begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|management
operator|.
name|event
operator|.
name|CamelContextStoppingEvent
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
name|event
operator|.
name|RouteStartedEvent
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
name|event
operator|.
name|RouteStoppedEvent
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
name|ManagementStrategy
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
name|EventNotifierSupport
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
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
DECL|class|EventHelperTest
specifier|public
class|class
name|EventHelperTest
block|{
annotation|@
name|Test
DECL|method|testStartStopEventsReceived ()
specifier|public
name|void
name|testStartStopEventsReceived
parameter_list|()
throws|throws
name|Exception
block|{
name|MyEventNotifier
name|en1
init|=
operator|new
name|MyEventNotifier
argument_list|()
decl_stmt|;
name|MyEventNotifier
name|en2
init|=
operator|new
name|MyEventNotifier
argument_list|()
decl_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
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
literal|"route-1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ManagementStrategy
name|managementStrategy
init|=
operator|new
name|DefaultManagementStrategy
argument_list|()
decl_stmt|;
name|managementStrategy
operator|.
name|addEventNotifier
argument_list|(
name|en1
argument_list|)
expr_stmt|;
name|managementStrategy
operator|.
name|addEventNotifier
argument_list|(
name|en2
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|setManagementStrategy
argument_list|(
name|managementStrategy
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|en1
operator|.
name|routeStartedEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|en1
operator|.
name|routeStoppedEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|en1
operator|.
name|camelContextStoppingEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|en2
operator|.
name|routeStartedEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|en2
operator|.
name|routeStoppedEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|en2
operator|.
name|camelContextStoppingEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStartStopEventsReceivedWhenTheFirstOneIgnoreTheseEvents ()
specifier|public
name|void
name|testStartStopEventsReceivedWhenTheFirstOneIgnoreTheseEvents
parameter_list|()
throws|throws
name|Exception
block|{
name|MyEventNotifier
name|en1
init|=
operator|new
name|MyEventNotifier
argument_list|()
decl_stmt|;
name|en1
operator|.
name|setIgnoreRouteEvents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|en1
operator|.
name|setIgnoreCamelContextEvents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|MyEventNotifier
name|en2
init|=
operator|new
name|MyEventNotifier
argument_list|()
decl_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
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
literal|"route-1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ManagementStrategy
name|managementStrategy
init|=
operator|new
name|DefaultManagementStrategy
argument_list|()
decl_stmt|;
name|managementStrategy
operator|.
name|addEventNotifier
argument_list|(
name|en1
argument_list|)
expr_stmt|;
name|managementStrategy
operator|.
name|addEventNotifier
argument_list|(
name|en2
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|setManagementStrategy
argument_list|(
name|managementStrategy
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|en1
operator|.
name|routeStartedEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|en1
operator|.
name|routeStoppedEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|en1
operator|.
name|camelContextStoppingEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|en2
operator|.
name|routeStartedEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|en2
operator|.
name|routeStoppedEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|en2
operator|.
name|camelContextStoppingEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStartStopEventsReceivedWhenTheSecondOneIgnoreTheseEvents ()
specifier|public
name|void
name|testStartStopEventsReceivedWhenTheSecondOneIgnoreTheseEvents
parameter_list|()
throws|throws
name|Exception
block|{
name|MyEventNotifier
name|en1
init|=
operator|new
name|MyEventNotifier
argument_list|()
decl_stmt|;
name|MyEventNotifier
name|en2
init|=
operator|new
name|MyEventNotifier
argument_list|()
decl_stmt|;
name|en2
operator|.
name|setIgnoreRouteEvents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|en2
operator|.
name|setIgnoreCamelContextEvents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
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
literal|"route-1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ManagementStrategy
name|managementStrategy
init|=
operator|new
name|DefaultManagementStrategy
argument_list|()
decl_stmt|;
name|managementStrategy
operator|.
name|addEventNotifier
argument_list|(
name|en1
argument_list|)
expr_stmt|;
name|managementStrategy
operator|.
name|addEventNotifier
argument_list|(
name|en2
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|setManagementStrategy
argument_list|(
name|managementStrategy
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|en1
operator|.
name|routeStartedEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|en1
operator|.
name|routeStoppedEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|en1
operator|.
name|camelContextStoppingEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|en2
operator|.
name|routeStartedEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|en2
operator|.
name|routeStoppedEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|en2
operator|.
name|camelContextStoppingEvent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyEventNotifier
specifier|static
class|class
name|MyEventNotifier
extends|extends
name|EventNotifierSupport
block|{
DECL|field|routeStartedEvent
name|AtomicInteger
name|routeStartedEvent
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|routeStoppedEvent
name|AtomicInteger
name|routeStoppedEvent
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|camelContextStoppingEvent
name|AtomicInteger
name|camelContextStoppingEvent
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|notify (EventObject event)
specifier|public
name|void
name|notify
parameter_list|(
name|EventObject
name|event
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|event
operator|instanceof
name|RouteStartedEvent
condition|)
block|{
name|routeStartedEvent
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|RouteStoppedEvent
condition|)
block|{
name|routeStoppedEvent
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|CamelContextStoppingEvent
condition|)
block|{
name|camelContextStoppingEvent
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|isEnabled (EventObject event)
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|EventObject
name|event
parameter_list|)
block|{
return|return
literal|true
return|;
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
block|{         }
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{         }
block|}
block|}
end_class

end_unit

