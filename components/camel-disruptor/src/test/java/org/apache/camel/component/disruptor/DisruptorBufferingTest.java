begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * This test suite is testing different scenarios where a disruptor is forced to  * buffer exchanges locally until a consumer is registered.  */
end_comment

begin_class
DECL|class|DisruptorBufferingTest
specifier|public
class|class
name|DisruptorBufferingTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testDisruptorBufferingWhileWaitingOnFirstConsumer ()
specifier|public
name|void
name|testDisruptorBufferingWhileWaitingOnFirstConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"disruptor:foo"
argument_list|,
literal|"A"
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"disruptor:foo"
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
specifier|final
name|DisruptorEndpoint
name|disruptorEndpoint
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"disruptor:foo"
argument_list|,
name|DisruptorEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|disruptorEndpoint
operator|.
name|getDisruptor
argument_list|()
operator|.
name|getRemainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
comment|// Add a first consumer on the endpoint
name|context
operator|.
name|addRoutes
argument_list|(
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
argument_list|)
expr_stmt|;
comment|// Now that we have a consumer, the disruptor should send the buffered
comment|// events downstream. Expect to receive the 3 original exchanges.
specifier|final
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDisruptorBufferingWhileWaitingOnNextConsumer ()
specifier|public
name|void
name|testDisruptorBufferingWhileWaitingOnNextConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"disruptor:foo"
argument_list|,
literal|"A"
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"disruptor:foo"
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
specifier|final
name|DisruptorEndpoint
name|disruptorEndpoint
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"disruptor:foo"
argument_list|,
name|DisruptorEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|disruptorEndpoint
operator|.
name|getDisruptor
argument_list|()
operator|.
name|getRemainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
comment|// Add a first consumer on the endpoint
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"bar1"
argument_list|)
operator|.
name|delay
argument_list|(
literal|200
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// Now that we have a consumer, the disruptor should send the buffered
comment|// events downstream. Wait until we have processed at least one
comment|// exchange.
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|200
argument_list|)
expr_stmt|;
comment|// Stop route and make sure all exchanges have been flushed.
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"bar1"
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|resetMocks
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"disruptor:foo"
argument_list|,
literal|"D"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"disruptor:foo"
argument_list|,
literal|"E"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"disruptor:foo"
argument_list|,
literal|"F"
argument_list|)
expr_stmt|;
comment|// Add a new consumer on the endpoint
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"bar2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"disruptor:foo"
argument_list|,
literal|"G"
argument_list|)
expr_stmt|;
comment|// Make sure we have received the 3 buffered exchanges plus the one
comment|// added late.
name|mockEndpoint
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|100
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
name|to
argument_list|(
literal|"disruptor:foo?size=8"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

