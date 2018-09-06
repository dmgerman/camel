begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|ContextTestSupport
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
name|Exchange
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
name|spi
operator|.
name|RoutePolicy
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
DECL|class|RoutePolicyTest
specifier|public
class|class
name|RoutePolicyTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|routePolicy
specifier|private
name|MyRoutPolicy
name|routePolicy
init|=
operator|new
name|MyRoutPolicy
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testStartCalledWhenCamelStarts ()
specifier|public
name|void
name|testStartCalledWhenCamelStarts
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|routePolicy
operator|.
name|getStartCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStartCalledWhenRouteStarts ()
specifier|public
name|void
name|testStartCalledWhenRouteStarts
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|routePolicy
operator|.
name|getStartCount
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stopRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|context
operator|.
name|startRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|routePolicy
operator|.
name|getStartCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStopCalledWhenCamelStops ()
specifier|public
name|void
name|testStopCalledWhenCamelStops
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|routePolicy
operator|.
name|getStopCount
argument_list|()
argument_list|)
expr_stmt|;
name|stopCamelContext
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|routePolicy
operator|.
name|getStopCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStopCalledWhenRouteStops ()
specifier|public
name|void
name|testStopCalledWhenRouteStops
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|routePolicy
operator|.
name|getStopCount
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stopRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|routePolicy
operator|.
name|getStopCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSuspendCalledWhenRouteSuspends ()
specifier|public
name|void
name|testSuspendCalledWhenRouteSuspends
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|routePolicy
operator|.
name|getSuspendCount
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|suspendRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|routePolicy
operator|.
name|getSuspendCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testResumeCalledWhenRouteResumes ()
specifier|public
name|void
name|testResumeCalledWhenRouteResumes
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|routePolicy
operator|.
name|getResumeCount
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|suspendRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|context
operator|.
name|resumeRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|routePolicy
operator|.
name|getResumeCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveCalledWhenRouteIsRemovedById ()
specifier|public
name|void
name|testRemoveCalledWhenRouteIsRemovedById
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|routePolicy
operator|.
name|getRemoveCount
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stopRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|routePolicy
operator|.
name|getRemoveCount
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|routePolicy
operator|.
name|getRemoveCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveCalledWhenCamelIsStopped ()
specifier|public
name|void
name|testRemoveCalledWhenCamelIsStopped
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|routePolicy
operator|.
name|getRemoveCount
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|routePolicy
operator|.
name|getRemoveCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
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
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|routePolicy
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
DECL|class|MyRoutPolicy
specifier|private
class|class
name|MyRoutPolicy
implements|implements
name|RoutePolicy
block|{
DECL|field|removeCounter
specifier|private
name|AtomicInteger
name|removeCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|startCounter
specifier|private
name|AtomicInteger
name|startCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|stopCounter
specifier|private
name|AtomicInteger
name|stopCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|suspendCounter
specifier|private
name|AtomicInteger
name|suspendCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|resumeCounter
specifier|private
name|AtomicInteger
name|resumeCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|onRemove (Route route)
specifier|public
name|void
name|onRemove
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|removeCounter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onStart (Route route)
specifier|public
name|void
name|onStart
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|startCounter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onStop (Route route)
specifier|public
name|void
name|onStop
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|stopCounter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onSuspend (Route route)
specifier|public
name|void
name|onSuspend
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|suspendCounter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onResume (Route route)
specifier|public
name|void
name|onResume
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|resumeCounter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onInit (Route route)
specifier|public
name|void
name|onInit
parameter_list|(
name|Route
name|route
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|onExchangeBegin (Route route, Exchange exchange)
specifier|public
name|void
name|onExchangeBegin
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|onExchangeDone (Route route, Exchange exchange)
specifier|public
name|void
name|onExchangeDone
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{         }
DECL|method|getRemoveCount ()
specifier|private
name|int
name|getRemoveCount
parameter_list|()
block|{
return|return
name|removeCounter
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getStartCount ()
specifier|private
name|int
name|getStartCount
parameter_list|()
block|{
return|return
name|startCounter
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getStopCount ()
specifier|private
name|int
name|getStopCount
parameter_list|()
block|{
return|return
name|stopCounter
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getSuspendCount ()
specifier|private
name|int
name|getSuspendCount
parameter_list|()
block|{
return|return
name|suspendCounter
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getResumeCount ()
specifier|private
name|int
name|getResumeCount
parameter_list|()
block|{
return|return
name|resumeCounter
operator|.
name|get
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

