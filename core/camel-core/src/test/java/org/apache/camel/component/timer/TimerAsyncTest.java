begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.timer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|timer
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
name|util
operator|.
name|concurrent
operator|.
name|ThreadPoolRejectedPolicy
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
DECL|class|TimerAsyncTest
specifier|public
class|class
name|TimerAsyncTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testSync ()
specifier|public
name|void
name|testSync
parameter_list|()
throws|throws
name|Exception
block|{
name|TimerEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"timer:foo?synchronous=true"
argument_list|,
name|TimerEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Timer endpoint must be synchronous, but it isn't"
argument_list|,
name|endpoint
operator|.
name|isSynchronous
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsync ()
specifier|public
name|void
name|testAsync
parameter_list|()
throws|throws
name|Exception
block|{
name|TimerEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"timer:foo"
argument_list|,
name|TimerEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Timer endpoint must be asynchronous, but it isn't"
argument_list|,
name|endpoint
operator|.
name|isSynchronous
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAsyncRouting ()
specifier|public
name|void
name|testAsyncRouting
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|int
name|threads
init|=
literal|5
decl_stmt|;
comment|// should trigger many tasks as we are async
name|getMockEndpoint
argument_list|(
literal|"mock:task"
argument_list|)
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|20
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
name|from
argument_list|(
literal|"timer://foo?fixedRate=true&delay=0&period=10"
argument_list|)
operator|.
name|id
argument_list|(
literal|"timer"
argument_list|)
operator|.
name|threads
argument_list|(
name|threads
argument_list|,
name|threads
argument_list|)
operator|.
name|maxQueueSize
argument_list|(
literal|1
argument_list|)
operator|.
name|rejectedPolicy
argument_list|(
name|ThreadPoolRejectedPolicy
operator|.
name|CallerRuns
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:task"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:task"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
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
comment|// simulate long task
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|sleep
argument_list|(
literal|50
argument_list|)
expr_stmt|;
block|}
block|}
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
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
block|}
end_class

end_unit

