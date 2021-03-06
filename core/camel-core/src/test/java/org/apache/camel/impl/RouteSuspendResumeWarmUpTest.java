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
name|Processor
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
name|service
operator|.
name|ServiceSupport
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
DECL|class|RouteSuspendResumeWarmUpTest
specifier|public
class|class
name|RouteSuspendResumeWarmUpTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|service
specifier|private
name|MyService
name|service
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testRouteSuspendResumeWarmUpTest ()
specifier|public
name|void
name|testRouteSuspendResumeWarmUpTest
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"start"
argument_list|,
name|service
operator|.
name|getState
argument_list|()
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
comment|// should keep this state as we are only suspending the consumer
name|assertEquals
argument_list|(
literal|"start"
argument_list|,
name|service
operator|.
name|getState
argument_list|()
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
comment|// should keep this state as we are only suspending the consumer
name|assertEquals
argument_list|(
literal|"start"
argument_list|,
name|service
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// now its stopped
name|assertEquals
argument_list|(
literal|"stop"
argument_list|,
name|service
operator|.
name|getState
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
name|process
argument_list|(
name|service
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
DECL|class|MyService
specifier|private
specifier|static
class|class
name|MyService
extends|extends
name|ServiceSupport
implements|implements
name|Processor
block|{
DECL|field|state
specifier|private
specifier|volatile
name|String
name|state
decl_stmt|;
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
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
name|state
operator|=
literal|"start"
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
name|state
operator|=
literal|"stop"
expr_stmt|;
block|}
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
block|}
block|}
end_class

end_unit

