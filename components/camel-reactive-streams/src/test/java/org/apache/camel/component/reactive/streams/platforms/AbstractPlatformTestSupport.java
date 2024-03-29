begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.platforms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|platforms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|java
operator|.
name|util
operator|.
name|function
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
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreams
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreamsService
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

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Publisher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Subscriber
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|AbstractPlatformTestSupport
specifier|public
specifier|abstract
class|class
name|AbstractPlatformTestSupport
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testPublisher ()
specifier|public
name|void
name|testPublisher
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|num
init|=
literal|20
decl_stmt|;
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
literal|"direct:endpoint"
argument_list|)
operator|.
name|to
argument_list|(
literal|"reactive-streams:integers"
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|addRoutesToCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|CamelReactiveStreamsService
name|camel
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|elements
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|num
argument_list|)
decl_stmt|;
name|this
operator|.
name|changeSign
argument_list|(
name|camel
operator|.
name|fromStream
argument_list|(
literal|"integers"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|,
name|i
lambda|->
block|{
name|elements
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|num
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:endpoint"
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Integer
name|number
range|:
name|elements
control|)
block|{
name|assertTrue
argument_list|(
name|number
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSubscriber ()
specifier|public
name|void
name|testSubscriber
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|num
init|=
literal|20
decl_stmt|;
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
literal|"reactive-streams:integers"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endpoint"
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|addRoutesToCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|CamelReactiveStreamsService
name|camel
init|=
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|elements
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|num
condition|;
name|i
operator|++
control|)
block|{
name|elements
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|changeSign
argument_list|(
name|elements
argument_list|,
name|camel
operator|.
name|streamSubscriber
argument_list|(
literal|"integers"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:endpoint"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
name|num
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
for|for
control|(
name|Exchange
name|ex
range|:
name|mock
operator|.
name|getExchanges
argument_list|()
control|)
block|{
name|Integer
name|number
init|=
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|number
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|number
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
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
DECL|method|changeSign (Publisher<Integer> data, Consumer<Integer> consume)
specifier|protected
specifier|abstract
name|void
name|changeSign
parameter_list|(
name|Publisher
argument_list|<
name|Integer
argument_list|>
name|data
parameter_list|,
name|Consumer
argument_list|<
name|Integer
argument_list|>
name|consume
parameter_list|)
function_decl|;
DECL|method|changeSign (Iterable<Integer> data, Subscriber<Integer> camel)
specifier|protected
specifier|abstract
name|void
name|changeSign
parameter_list|(
name|Iterable
argument_list|<
name|Integer
argument_list|>
name|data
parameter_list|,
name|Subscriber
argument_list|<
name|Integer
argument_list|>
name|camel
parameter_list|)
function_decl|;
block|}
end_class

end_unit

