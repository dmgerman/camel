begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ServiceHelper
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
DECL|class|DisruptorConsumerSuspendResumeTest
specifier|public
class|class
name|DisruptorConsumerSuspendResumeTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testSuspendResume ()
specifier|public
name|void
name|testSuspendResume
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
decl_stmt|;
name|mock
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
literal|"disruptor:foo"
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
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
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
comment|// suspend bar consumer (not the route)
specifier|final
name|DisruptorConsumer
name|consumer
init|=
operator|(
name|DisruptorConsumer
operator|)
name|context
operator|.
name|getRoute
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|getConsumer
argument_list|()
decl_stmt|;
name|ServiceHelper
operator|.
name|suspendService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Suspended"
argument_list|,
name|consumer
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
comment|// send a message to the route but the consumer is suspended
comment|// so it should not route it
name|resetMocks
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// wait a bit to ensure consumer is suspended, as it could be in a poll mode where
comment|// it would poll and route (there is a little slack (up till 1 sec) before suspension is empowered)
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"disruptor:foo"
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
comment|// wait 2 sec to ensure disruptor consumer thread would have tried to poll otherwise
name|mock
operator|.
name|assertIsSatisfied
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
comment|// resume consumer
name|resetMocks
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// resume bar consumer (not the route)
name|ServiceHelper
operator|.
name|resumeService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
name|consumer
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
comment|// the message should be routed now
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
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
literal|"disruptor:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"disruptor:bar"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"disruptor:bar"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

