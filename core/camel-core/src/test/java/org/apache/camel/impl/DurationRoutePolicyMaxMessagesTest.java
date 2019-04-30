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
name|engine
operator|.
name|DurationRoutePolicy
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
name|awaitility
operator|.
name|Awaitility
operator|.
name|await
import|;
end_import

begin_class
DECL|class|DurationRoutePolicyMaxMessagesTest
specifier|public
class|class
name|DurationRoutePolicyMaxMessagesTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testDurationRoutePolicy ()
specifier|public
name|void
name|testDurationRoutePolicy
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
name|assertFalse
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
comment|// the policy should stop the route after 5 messages
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// need a little time to stop async
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|untilAsserted
argument_list|(
parameter_list|()
lambda|->
block|{
name|assertFalse
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
name|DurationRoutePolicy
name|policy
init|=
operator|new
name|DurationRoutePolicy
argument_list|()
decl_stmt|;
name|policy
operator|.
name|setMaxMessages
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer:foo?period=100"
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
literal|"mock:foo"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

