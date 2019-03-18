begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Collection
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
name|VetoCamelContextStartException
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
name|support
operator|.
name|LifecycleStrategySupport
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|DefaultCamelContextWithLifecycleStrategyRestartTest
specifier|public
class|class
name|DefaultCamelContextWithLifecycleStrategyRestartTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|strategy
specifier|private
name|MyStrategy
name|strategy
init|=
operator|new
name|MyStrategy
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testRestart ()
specifier|public
name|void
name|testRestart
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
name|assertFalse
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
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|strategy
operator|.
name|getContextStartCounter
argument_list|()
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// now stop
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertFalse
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
literal|0
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// now start
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
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
name|assertFalse
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
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|strategy
operator|.
name|getContextStartCounter
argument_list|()
argument_list|)
expr_stmt|;
comment|// must obtain a new template
name|template
operator|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
comment|// should still work
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRouteStopped ()
specifier|public
name|void
name|testRouteStopped
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|strategy
operator|.
name|getRemoveCounter
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
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|strategy
operator|.
name|getRemoveCounter
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
name|strategy
operator|.
name|getRemoveCounter
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
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|addLifecycleStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
return|return
name|context
return|;
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
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyStrategy
specifier|private
class|class
name|MyStrategy
extends|extends
name|LifecycleStrategySupport
block|{
DECL|field|contextStartCounter
specifier|private
name|AtomicInteger
name|contextStartCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|removeCounter
specifier|private
name|AtomicInteger
name|removeCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|onContextStart (CamelContext context)
specifier|public
name|void
name|onContextStart
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|VetoCamelContextStartException
block|{
name|contextStartCounter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onRoutesRemove (Collection<Route> routes)
specifier|public
name|void
name|onRoutesRemove
parameter_list|(
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
block|{
name|removeCounter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
DECL|method|getContextStartCounter ()
specifier|public
name|int
name|getContextStartCounter
parameter_list|()
block|{
return|return
name|contextStartCounter
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getRemoveCounter ()
specifier|public
name|int
name|getRemoveCounter
parameter_list|()
block|{
return|return
name|removeCounter
operator|.
name|get
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

