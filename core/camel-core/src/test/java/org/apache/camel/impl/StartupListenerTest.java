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
name|StartupListener
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|StartupListenerTest
specifier|public
class|class
name|StartupListenerTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|my
specifier|private
name|MyStartupListener
name|my
init|=
operator|new
name|MyStartupListener
argument_list|()
decl_stmt|;
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
name|addStartupListener
argument_list|(
name|my
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|class|MyStartupListener
specifier|private
specifier|static
class|class
name|MyStartupListener
implements|implements
name|StartupListener
block|{
DECL|field|invoked
specifier|private
name|int
name|invoked
decl_stmt|;
DECL|field|alreadyStarted
specifier|private
name|boolean
name|alreadyStarted
decl_stmt|;
DECL|method|onCamelContextStarted (CamelContext context, boolean alreadyStarted)
specifier|public
name|void
name|onCamelContextStarted
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|boolean
name|alreadyStarted
parameter_list|)
throws|throws
name|Exception
block|{
name|invoked
operator|++
expr_stmt|;
name|this
operator|.
name|alreadyStarted
operator|=
name|alreadyStarted
expr_stmt|;
if|if
condition|(
name|alreadyStarted
condition|)
block|{
comment|// the routes should already been started as we add the listener afterwards
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
block|}
else|else
block|{
comment|// the routes should not have been started as they start afterwards
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
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getInvoked ()
specifier|public
name|int
name|getInvoked
parameter_list|()
block|{
return|return
name|invoked
return|;
block|}
DECL|method|isAlreadyStarted ()
specifier|public
name|boolean
name|isAlreadyStarted
parameter_list|()
block|{
return|return
name|alreadyStarted
return|;
block|}
block|}
annotation|@
name|Test
DECL|method|testStartupListenerComponent ()
specifier|public
name|void
name|testStartupListenerComponent
parameter_list|()
throws|throws
name|Exception
block|{
comment|// and now the routes are started
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
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|my
operator|.
name|getInvoked
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|my
operator|.
name|isAlreadyStarted
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStartupListenerComponentAlreadyStarted ()
specifier|public
name|void
name|testStartupListenerComponentAlreadyStarted
parameter_list|()
throws|throws
name|Exception
block|{
comment|// and now the routes are started
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
name|MyStartupListener
name|other
init|=
operator|new
name|MyStartupListener
argument_list|()
decl_stmt|;
name|context
operator|.
name|addStartupListener
argument_list|(
name|other
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
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|other
operator|.
name|getInvoked
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|other
operator|.
name|isAlreadyStarted
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
literal|"direct:foo"
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
block|}
end_class

end_unit
