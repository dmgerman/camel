begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams
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
name|ConcurrentLinkedQueue
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
name|io
operator|.
name|reactivex
operator|.
name|Flowable
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
name|support
operator|.
name|TestSubscriber
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

begin_class
DECL|class|BackpressureStrategyTest
specifier|public
class|class
name|BackpressureStrategyTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testBackpressureBufferStrategy ()
specifier|public
name|void
name|testBackpressureBufferStrategy
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"timer:gen?period=20&repeatCount=20"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|TIMER_COUNTER
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
name|Flowable
argument_list|<
name|Integer
argument_list|>
name|integers
init|=
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
operator|.
name|fromStream
argument_list|(
literal|"integers"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|ConcurrentLinkedQueue
argument_list|<
name|Integer
argument_list|>
name|queue
init|=
operator|new
name|ConcurrentLinkedQueue
argument_list|<>
argument_list|()
decl_stmt|;
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Flowable
operator|.
name|interval
argument_list|(
literal|0
argument_list|,
literal|50
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
operator|.
name|zipWith
argument_list|(
name|integers
argument_list|,
parameter_list|(
name|l
parameter_list|,
name|i
parameter_list|)
lambda|->
name|i
argument_list|)
operator|.
name|timeout
argument_list|(
literal|2000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|,
name|Flowable
operator|.
name|empty
argument_list|()
argument_list|)
operator|.
name|doOnComplete
argument_list|(
name|latch
operator|::
name|countDown
argument_list|)
operator|.
name|subscribe
argument_list|(
name|queue
operator|::
name|add
argument_list|)
expr_stmt|;
name|context
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
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
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|num
init|=
literal|1
decl_stmt|;
for|for
control|(
name|int
name|i
range|:
name|queue
control|)
block|{
name|assertEquals
argument_list|(
name|num
operator|++
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testBackpressureDropStrategy ()
specifier|public
name|void
name|testBackpressureDropStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|ReactiveStreamsComponent
name|comp
init|=
operator|(
name|ReactiveStreamsComponent
operator|)
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"reactive-streams"
argument_list|)
decl_stmt|;
name|comp
operator|.
name|setBackpressureStrategy
argument_list|(
name|ReactiveStreamsBackpressureStrategy
operator|.
name|OLDEST
argument_list|)
expr_stmt|;
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
literal|"timer:gen?period=20&repeatCount=20"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|TIMER_COUNTER
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
name|ConcurrentLinkedQueue
argument_list|<
name|Integer
argument_list|>
name|queue
init|=
operator|new
name|ConcurrentLinkedQueue
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|latch2
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|TestSubscriber
argument_list|<
name|Integer
argument_list|>
name|subscriber
init|=
operator|new
name|TestSubscriber
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onNext
parameter_list|(
name|Integer
name|o
parameter_list|)
block|{
name|queue
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|latch2
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
name|subscriber
operator|.
name|setInitiallyRequested
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
operator|.
name|fromStream
argument_list|(
literal|"integers"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
operator|.
name|subscribe
argument_list|(
name|subscriber
argument_list|)
expr_stmt|;
name|context
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
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
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// wait for all numbers to be generated
name|subscriber
operator|.
name|request
argument_list|(
literal|19
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|latch2
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
comment|// add other time to ensure no other items arrive
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|sum
init|=
name|queue
operator|.
name|stream
argument_list|()
operator|.
name|reduce
argument_list|(
parameter_list|(
name|i
parameter_list|,
name|j
parameter_list|)
lambda|->
name|i
operator|+
name|j
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|sum
argument_list|)
expr_stmt|;
comment|// 1 + 2 = 3
name|subscriber
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBackpressureLatestStrategy ()
specifier|public
name|void
name|testBackpressureLatestStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|ReactiveStreamsComponent
name|comp
init|=
operator|(
name|ReactiveStreamsComponent
operator|)
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"reactive-streams"
argument_list|)
decl_stmt|;
name|comp
operator|.
name|setBackpressureStrategy
argument_list|(
name|ReactiveStreamsBackpressureStrategy
operator|.
name|LATEST
argument_list|)
expr_stmt|;
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
literal|"timer:gen?period=20&repeatCount=20"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|TIMER_COUNTER
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
name|ConcurrentLinkedQueue
argument_list|<
name|Integer
argument_list|>
name|queue
init|=
operator|new
name|ConcurrentLinkedQueue
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|latch2
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|TestSubscriber
argument_list|<
name|Integer
argument_list|>
name|subscriber
init|=
operator|new
name|TestSubscriber
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onNext
parameter_list|(
name|Integer
name|o
parameter_list|)
block|{
name|queue
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|latch2
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
name|subscriber
operator|.
name|setInitiallyRequested
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
operator|.
name|fromStream
argument_list|(
literal|"integers"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
operator|.
name|subscribe
argument_list|(
name|subscriber
argument_list|)
expr_stmt|;
name|context
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
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
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// wait for all numbers to be generated
name|subscriber
operator|.
name|request
argument_list|(
literal|19
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|latch2
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
comment|// add other time to ensure no other items arrive
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|sum
init|=
name|queue
operator|.
name|stream
argument_list|()
operator|.
name|reduce
argument_list|(
parameter_list|(
name|i
parameter_list|,
name|j
parameter_list|)
lambda|->
name|i
operator|+
name|j
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|21
argument_list|,
name|sum
argument_list|)
expr_stmt|;
comment|// 1 + 20 = 21
name|subscriber
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBackpressureDropStrategyInEndpoint ()
specifier|public
name|void
name|testBackpressureDropStrategyInEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"timer:gen?period=20&repeatCount=20"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|TIMER_COUNTER
argument_list|)
operator|.
name|to
argument_list|(
literal|"reactive-streams:integers?backpressureStrategy=OLDEST"
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
name|ConcurrentLinkedQueue
argument_list|<
name|Integer
argument_list|>
name|queue
init|=
operator|new
name|ConcurrentLinkedQueue
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|latch2
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|TestSubscriber
argument_list|<
name|Integer
argument_list|>
name|subscriber
init|=
operator|new
name|TestSubscriber
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onNext
parameter_list|(
name|Integer
name|o
parameter_list|)
block|{
name|queue
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|latch2
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
name|subscriber
operator|.
name|setInitiallyRequested
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|CamelReactiveStreams
operator|.
name|get
argument_list|(
name|context
argument_list|)
operator|.
name|fromStream
argument_list|(
literal|"integers"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
operator|.
name|subscribe
argument_list|(
name|subscriber
argument_list|)
expr_stmt|;
name|context
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
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
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// wait for all numbers to be generated
name|subscriber
operator|.
name|request
argument_list|(
literal|19
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|latch2
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
comment|// add other time to ensure no other items arrive
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|sum
init|=
name|queue
operator|.
name|stream
argument_list|()
operator|.
name|reduce
argument_list|(
parameter_list|(
name|i
parameter_list|,
name|j
parameter_list|)
lambda|->
name|i
operator|+
name|j
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|sum
argument_list|)
expr_stmt|;
comment|// 1 + 2 = 3
name|subscriber
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

