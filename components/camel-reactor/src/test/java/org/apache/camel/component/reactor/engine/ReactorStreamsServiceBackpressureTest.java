begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactor.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactor
operator|.
name|engine
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
name|ReactiveStreamsBackpressureStrategy
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
name|reactor
operator|.
name|engine
operator|.
name|suport
operator|.
name|TestSubscriber
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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
name|reactor
operator|.
name|core
operator|.
name|publisher
operator|.
name|Flux
import|;
end_import

begin_class
DECL|class|ReactorStreamsServiceBackpressureTest
specifier|public
class|class
name|ReactorStreamsServiceBackpressureTest
extends|extends
name|ReactorStreamsServiceTestSupport
block|{
annotation|@
name|Test
DECL|method|testBufferStrategy ()
specifier|public
name|void
name|testBufferStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|getReactiveStreamsComponent
argument_list|()
operator|.
name|setBackpressureStrategy
argument_list|(
name|ReactiveStreamsBackpressureStrategy
operator|.
name|BUFFER
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
argument_list|)
expr_stmt|;
name|Flux
argument_list|<
name|Integer
argument_list|>
name|integers
init|=
name|Flux
operator|.
name|from
argument_list|(
name|crs
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
name|Flux
operator|.
name|range
argument_list|(
literal|0
argument_list|,
literal|50
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
name|timeoutMillis
argument_list|(
literal|2000
argument_list|,
name|Flux
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
operator|.
name|start
argument_list|()
expr_stmt|;
name|Assert
operator|.
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
name|Assert
operator|.
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
name|Assert
operator|.
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
name|Ignore
annotation|@
name|Test
DECL|method|testDropStrategy ()
specifier|public
name|void
name|testDropStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|getReactiveStreamsComponent
argument_list|()
operator|.
name|setBackpressureStrategy
argument_list|(
name|ReactiveStreamsBackpressureStrategy
operator|.
name|OLDEST
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
argument_list|)
expr_stmt|;
specifier|final
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
name|crs
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
operator|.
name|start
argument_list|()
expr_stmt|;
name|Assert
operator|.
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
name|Assert
operator|.
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
name|Assert
operator|.
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
name|Assert
operator|.
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
name|Ignore
annotation|@
name|Test
DECL|method|testLatestStrategy ()
specifier|public
name|void
name|testLatestStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|getReactiveStreamsComponent
argument_list|()
operator|.
name|setBackpressureStrategy
argument_list|(
name|ReactiveStreamsBackpressureStrategy
operator|.
name|LATEST
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
argument_list|)
expr_stmt|;
specifier|final
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
name|latch1
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
name|latch1
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
name|crs
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
operator|.
name|start
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|latch1
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
name|Assert
operator|.
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
name|Assert
operator|.
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
name|Assert
operator|.
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
block|}
end_class

end_unit

