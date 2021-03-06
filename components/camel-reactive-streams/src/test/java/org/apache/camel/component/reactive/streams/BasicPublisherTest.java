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
name|io
operator|.
name|reactivex
operator|.
name|Observable
import|;
end_import

begin_import
import|import
name|io
operator|.
name|reactivex
operator|.
name|disposables
operator|.
name|Disposable
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
name|FailedToStartRouteException
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
DECL|class|BasicPublisherTest
specifier|public
class|class
name|BasicPublisherTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testWorking ()
specifier|public
name|void
name|testWorking
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
literal|"timer:tick?period=5&repeatCount=30"
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
literal|"reactive-streams:pub"
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
name|int
name|num
init|=
literal|30
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
name|List
argument_list|<
name|Integer
argument_list|>
name|recv
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|Observable
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
literal|"pub"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|recv
operator|::
name|add
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|n
lambda|->
name|latch
operator|.
name|countDown
argument_list|()
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
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
expr_stmt|;
name|assertEquals
argument_list|(
name|num
argument_list|,
name|recv
operator|.
name|size
argument_list|()
argument_list|)
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
name|assertEquals
argument_list|(
name|i
argument_list|,
name|recv
operator|.
name|get
argument_list|(
name|i
operator|-
literal|1
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testMultipleSubscriptions ()
specifier|public
name|void
name|testMultipleSubscriptions
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
literal|"timer:tick?period=50"
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
literal|"reactive-streams:unbounded"
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
name|CountDownLatch
name|latch1
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|Disposable
name|disp1
init|=
name|Observable
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
literal|"unbounded"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|subscribe
argument_list|(
name|n
lambda|->
name|latch1
operator|.
name|countDown
argument_list|()
argument_list|)
decl_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// Add another subscription
name|CountDownLatch
name|latch2
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|Disposable
name|disp2
init|=
name|Observable
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
literal|"unbounded"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|subscribe
argument_list|(
name|n
lambda|->
name|latch2
operator|.
name|countDown
argument_list|()
argument_list|)
decl_stmt|;
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
name|assertTrue
argument_list|(
name|latch2
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
comment|// Unsubscribe both
name|disp1
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|disp2
operator|.
name|dispose
argument_list|()
expr_stmt|;
comment|// No active subscriptions, warnings expected
name|Thread
operator|.
name|sleep
argument_list|(
literal|60
argument_list|)
expr_stmt|;
comment|// Add another subscription
name|CountDownLatch
name|latch3
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|Disposable
name|disp3
init|=
name|Observable
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
literal|"unbounded"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|subscribe
argument_list|(
name|n
lambda|->
name|latch3
operator|.
name|countDown
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|latch3
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
name|disp3
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FailedToStartRouteException
operator|.
name|class
argument_list|)
DECL|method|testOnlyOneCamelProducerPerPublisher ()
specifier|public
name|void
name|testOnlyOneCamelProducerPerPublisher
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
literal|"direct:one"
argument_list|)
operator|.
name|to
argument_list|(
literal|"reactive-streams:stream"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:two"
argument_list|)
operator|.
name|to
argument_list|(
literal|"reactive-streams:stream"
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
name|context
operator|.
name|start
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

