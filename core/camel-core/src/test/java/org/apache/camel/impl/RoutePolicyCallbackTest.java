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
name|support
operator|.
name|RoutePolicySupport
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
DECL|class|RoutePolicyCallbackTest
specifier|public
class|class
name|RoutePolicyCallbackTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|policy
specifier|protected
name|MyRoutePolicy
name|policy
init|=
operator|new
name|MyRoutePolicy
argument_list|()
decl_stmt|;
DECL|class|MyRoutePolicy
specifier|public
specifier|static
class|class
name|MyRoutePolicy
extends|extends
name|RoutePolicySupport
block|{
DECL|field|begin
name|boolean
name|begin
decl_stmt|;
DECL|field|done
name|boolean
name|done
decl_stmt|;
DECL|field|init
name|boolean
name|init
decl_stmt|;
DECL|field|remove
name|boolean
name|remove
decl_stmt|;
DECL|field|resume
name|boolean
name|resume
decl_stmt|;
DECL|field|start
name|boolean
name|start
decl_stmt|;
DECL|field|stop
name|boolean
name|stop
decl_stmt|;
DECL|field|suspend
name|boolean
name|suspend
decl_stmt|;
DECL|field|doStart
name|boolean
name|doStart
decl_stmt|;
DECL|field|doStop
name|boolean
name|doStop
decl_stmt|;
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
block|{
name|begin
operator|=
literal|true
expr_stmt|;
block|}
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
block|{
name|done
operator|=
literal|true
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
block|{
name|init
operator|=
literal|true
expr_stmt|;
block|}
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
name|remove
operator|=
literal|true
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
name|resume
operator|=
literal|true
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
name|start
operator|=
literal|true
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
name|stop
operator|=
literal|true
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
name|suspend
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|doStop
operator|=
literal|true
expr_stmt|;
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
block|{
name|doStart
operator|=
literal|true
expr_stmt|;
block|}
block|}
DECL|method|getAndInitMyRoutePolicy ()
specifier|protected
name|MyRoutePolicy
name|getAndInitMyRoutePolicy
parameter_list|()
block|{
return|return
name|policy
return|;
block|}
annotation|@
name|Test
DECL|method|testCallback ()
specifier|public
name|void
name|testCallback
parameter_list|()
throws|throws
name|Exception
block|{
name|policy
operator|=
name|getAndInitMyRoutePolicy
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|policy
operator|.
name|doStart
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|policy
operator|.
name|init
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|policy
operator|.
name|begin
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|policy
operator|.
name|done
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
name|assertTrue
argument_list|(
name|policy
operator|.
name|begin
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|policy
operator|.
name|done
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|policy
operator|.
name|suspend
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|suspendRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|policy
operator|.
name|suspend
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|policy
operator|.
name|resume
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|resumeRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|policy
operator|.
name|resume
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|policy
operator|.
name|stop
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
name|assertTrue
argument_list|(
name|policy
operator|.
name|stop
argument_list|)
expr_stmt|;
comment|// previously started, so force flag to be false
name|policy
operator|.
name|start
operator|=
literal|false
expr_stmt|;
name|assertFalse
argument_list|(
name|policy
operator|.
name|start
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|policy
operator|.
name|start
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|policy
operator|.
name|remove
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
name|context
operator|.
name|removeRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|policy
operator|.
name|remove
argument_list|)
expr_stmt|;
comment|// stop camel which should stop policy as well
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|policy
operator|.
name|doStop
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
name|policy
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
