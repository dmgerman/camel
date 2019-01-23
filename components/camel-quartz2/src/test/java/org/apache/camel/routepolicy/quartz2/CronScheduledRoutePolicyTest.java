begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.routepolicy.quartz2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|routepolicy
operator|.
name|quartz2
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
name|CountDownLatch
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
name|Consumer
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
name|SuspendableService
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
name|direct
operator|.
name|DirectComponent
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
name|component
operator|.
name|quartz2
operator|.
name|QuartzComponent
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
name|service
operator|.
name|ServiceHelper
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
name|junit4
operator|.
name|CamelTestSupport
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

begin_class
DECL|class|CronScheduledRoutePolicyTest
specifier|public
class|class
name|CronScheduledRoutePolicyTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testScheduledStartRoutePolicyWithTwoRoutes ()
specifier|public
name|void
name|testScheduledStartRoutePolicyWithTwoRoutes
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|success1
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:success1"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|MockEndpoint
name|success2
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:success2"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|success1
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|success2
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"direct"
argument_list|,
name|DirectComponent
operator|.
name|class
argument_list|)
operator|.
name|setBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"quartz2"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
operator|.
name|setPropertiesFile
argument_list|(
literal|"org/apache/camel/routepolicy/quartz2/myquartz.properties"
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|CronScheduledRoutePolicy
name|policy
init|=
operator|new
name|CronScheduledRoutePolicy
argument_list|()
decl_stmt|;
name|policy
operator|.
name|setRouteStartTime
argument_list|(
literal|"*/3 * * * * ?"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start1"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"test1"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:success1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start2"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"test2"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:success2"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"test1"
argument_list|,
literal|1000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"test2"
argument_list|,
literal|1000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"test1"
argument_list|)
operator|==
name|ServiceStatus
operator|.
name|Started
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"test2"
argument_list|)
operator|==
name|ServiceStatus
operator|.
name|Started
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"Ready or not, Here, I come"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start2"
argument_list|,
literal|"Ready or not, Here, I come"
argument_list|)
expr_stmt|;
name|success1
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|success2
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScheduledStopRoutePolicyWithTwoRoutes ()
specifier|public
name|void
name|testScheduledStopRoutePolicyWithTwoRoutes
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getComponent
argument_list|(
literal|"direct"
argument_list|,
name|DirectComponent
operator|.
name|class
argument_list|)
operator|.
name|setBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"quartz2"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
operator|.
name|setPropertiesFile
argument_list|(
literal|"org/apache/camel/routepolicy/quartz2/myquartz.properties"
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|CronScheduledRoutePolicy
name|policy
init|=
operator|new
name|CronScheduledRoutePolicy
argument_list|()
decl_stmt|;
name|policy
operator|.
name|setRouteStopTime
argument_list|(
literal|"*/3 * * * * ?"
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setRouteStopGracePeriod
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setTimeUnit
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start1"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"test1"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unreachable"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start2"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"test2"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unreachable"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"test1"
argument_list|)
operator|==
name|ServiceStatus
operator|.
name|Stopped
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"test2"
argument_list|)
operator|==
name|ServiceStatus
operator|.
name|Stopped
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScheduledStartRoutePolicy ()
specifier|public
name|void
name|testScheduledStartRoutePolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|success
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:success"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|success
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"direct"
argument_list|,
name|DirectComponent
operator|.
name|class
argument_list|)
operator|.
name|setBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"quartz2"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
operator|.
name|setPropertiesFile
argument_list|(
literal|"org/apache/camel/routepolicy/quartz2/myquartz.properties"
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|CronScheduledRoutePolicy
name|policy
init|=
operator|new
name|CronScheduledRoutePolicy
argument_list|()
decl_stmt|;
name|policy
operator|.
name|setRouteStartTime
argument_list|(
literal|"*/3 * * * * ?"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"test"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:success"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"test"
argument_list|,
literal|1000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"test"
argument_list|)
operator|==
name|ServiceStatus
operator|.
name|Started
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Ready or not, Here, I come"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"quartz2"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
operator|.
name|stop
argument_list|()
expr_stmt|;
name|success
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScheduledStopRoutePolicy ()
specifier|public
name|void
name|testScheduledStopRoutePolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getComponent
argument_list|(
literal|"direct"
argument_list|,
name|DirectComponent
operator|.
name|class
argument_list|)
operator|.
name|setBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"quartz2"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
operator|.
name|setPropertiesFile
argument_list|(
literal|"org/apache/camel/routepolicy/quartz2/myquartz.properties"
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|CronScheduledRoutePolicy
name|policy
init|=
operator|new
name|CronScheduledRoutePolicy
argument_list|()
decl_stmt|;
name|policy
operator|.
name|setRouteStopTime
argument_list|(
literal|"*/3 * * * * ?"
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setRouteStopGracePeriod
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setTimeUnit
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"test"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unreachable"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"test"
argument_list|)
operator|==
name|ServiceStatus
operator|.
name|Stopped
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScheduledStartAndStopRoutePolicy ()
specifier|public
name|void
name|testScheduledStartAndStopRoutePolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|success
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:success"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|success
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
specifier|final
name|CountDownLatch
name|startedLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|stoppedLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"direct"
argument_list|,
name|DirectComponent
operator|.
name|class
argument_list|)
operator|.
name|setBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"quartz2"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
operator|.
name|setPropertiesFile
argument_list|(
literal|"org/apache/camel/routepolicy/quartz2/myquartz.properties"
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|CronScheduledRoutePolicy
name|policy
init|=
operator|new
name|CronScheduledRoutePolicy
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onStart
parameter_list|(
specifier|final
name|Route
name|route
parameter_list|)
block|{
name|super
operator|.
name|onStart
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|startedLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onStop
parameter_list|(
specifier|final
name|Route
name|route
parameter_list|)
block|{
name|super
operator|.
name|onStop
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|stoppedLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
name|policy
operator|.
name|setRouteStartTime
argument_list|(
literal|"*/3 * * * * ?"
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setRouteStopTime
argument_list|(
literal|"*/6 * * * * ?"
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setRouteStopGracePeriod
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"test"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:success"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|startedLatch
operator|.
name|await
argument_list|(
literal|5000
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|ServiceStatus
name|startedStatus
init|=
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|startedStatus
operator|==
name|ServiceStatus
operator|.
name|Started
operator|||
name|startedStatus
operator|==
name|ServiceStatus
operator|.
name|Starting
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Ready or not, Here, I come"
argument_list|)
expr_stmt|;
name|stoppedLatch
operator|.
name|await
argument_list|(
literal|5000
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|ServiceStatus
name|stoppedStatus
init|=
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|stoppedStatus
operator|==
name|ServiceStatus
operator|.
name|Stopped
operator|||
name|stoppedStatus
operator|==
name|ServiceStatus
operator|.
name|Stopping
argument_list|)
expr_stmt|;
name|success
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScheduledStopRoutePolicyWithExtraPolicy ()
specifier|public
name|void
name|testScheduledStopRoutePolicyWithExtraPolicy
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|MyRoutePolicy
name|myPolicy
init|=
operator|new
name|MyRoutePolicy
argument_list|()
decl_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"direct"
argument_list|,
name|DirectComponent
operator|.
name|class
argument_list|)
operator|.
name|setBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"quartz2"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
operator|.
name|setPropertiesFile
argument_list|(
literal|"org/apache/camel/routepolicy/quartz2/myquartz.properties"
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|CronScheduledRoutePolicy
name|policy
init|=
operator|new
name|CronScheduledRoutePolicy
argument_list|()
decl_stmt|;
name|policy
operator|.
name|setRouteStopTime
argument_list|(
literal|"*/3 * * * * ?"
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setRouteStopGracePeriod
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setTimeUnit
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"test"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|,
name|myPolicy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unreachable"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"test"
argument_list|)
operator|==
name|ServiceStatus
operator|.
name|Stopped
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should have called onStart"
argument_list|,
name|myPolicy
operator|.
name|isStart
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should have called onStop"
argument_list|,
name|myPolicy
operator|.
name|isStop
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScheduledSuspendRoutePolicy ()
specifier|public
name|void
name|testScheduledSuspendRoutePolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getComponent
argument_list|(
literal|"direct"
argument_list|,
name|DirectComponent
operator|.
name|class
argument_list|)
operator|.
name|setBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"quartz2"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
operator|.
name|setPropertiesFile
argument_list|(
literal|"org/apache/camel/routepolicy/quartz2/myquartz.properties"
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|CronScheduledRoutePolicy
name|policy
init|=
operator|new
name|CronScheduledRoutePolicy
argument_list|()
decl_stmt|;
name|policy
operator|.
name|setRouteSuspendTime
argument_list|(
literal|"*/3 * * * * ?"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"test"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unreachable"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
comment|// when suspending its only the consumer that suspends
comment|// there is a ticket to improve this
name|Consumer
name|consumer
init|=
name|context
operator|.
name|getRoute
argument_list|(
literal|"test"
argument_list|)
operator|.
name|getConsumer
argument_list|()
decl_stmt|;
name|SuspendableService
name|ss
init|=
operator|(
name|SuspendableService
operator|)
name|consumer
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Consumer should be suspended"
argument_list|,
name|ss
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testScheduledResumeRoutePolicy ()
specifier|public
name|void
name|testScheduledResumeRoutePolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|success
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:success"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|success
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"direct"
argument_list|,
name|DirectComponent
operator|.
name|class
argument_list|)
operator|.
name|setBlock
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|context
operator|.
name|getComponent
argument_list|(
literal|"quartz2"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
operator|.
name|setPropertiesFile
argument_list|(
literal|"org/apache/camel/routepolicy/quartz2/myquartz.properties"
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|CronScheduledRoutePolicy
name|policy
init|=
operator|new
name|CronScheduledRoutePolicy
argument_list|()
decl_stmt|;
name|policy
operator|.
name|setRouteResumeTime
argument_list|(
literal|"*/3 * * * * ?"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"test"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:success"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|suspendService
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"test"
argument_list|)
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"test"
argument_list|)
operator|==
name|ServiceStatus
operator|.
name|Started
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Ready or not, Here, I come"
argument_list|)
expr_stmt|;
name|success
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

