begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rxjava2.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rxjava2
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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|Function
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
name|ProducerTemplate
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
name|support
operator|.
name|DefaultExchange
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
name|JndiRegistry
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
name|ExchangeHelper
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

begin_class
DECL|class|RxJavaStreamsServiceTest
specifier|public
class|class
name|RxJavaStreamsServiceTest
extends|extends
name|RxJavaStreamsServiceTestSupport
block|{
comment|// ************************************************
comment|// Setup
comment|// ************************************************
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"hello"
argument_list|,
operator|new
name|SampleBean
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
DECL|class|SampleBean
specifier|public
specifier|static
class|class
name|SampleBean
block|{
DECL|method|hello (String name)
specifier|public
name|String
name|hello
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|name
return|;
block|}
block|}
comment|// ************************************************
comment|// fromStream/from
comment|// ************************************************
annotation|@
name|Test
DECL|method|testFromStreamDirect ()
specifier|public
name|void
name|testFromStreamDirect
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"direct:reactive"
argument_list|)
operator|.
name|to
argument_list|(
literal|"reactive-streams:numbers"
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
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|AtomicInteger
name|value
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|crs
operator|.
name|fromStream
argument_list|(
literal|"numbers"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|res
lambda|->
name|Assert
operator|.
name|assertEquals
argument_list|(
name|value
operator|.
name|incrementAndGet
argument_list|()
argument_list|,
name|res
operator|.
name|intValue
argument_list|()
argument_list|)
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:reactive"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:reactive"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:reactive"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFromStreamTimer ()
specifier|public
name|void
name|testFromStreamTimer
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"reactive-streams:tick"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
specifier|final
name|int
name|num
init|=
literal|30
decl_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|num
argument_list|)
decl_stmt|;
specifier|final
name|AtomicInteger
name|value
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|crs
operator|.
name|fromStream
argument_list|(
literal|"tick"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|res
lambda|->
name|Assert
operator|.
name|assertEquals
argument_list|(
name|value
operator|.
name|incrementAndGet
argument_list|()
argument_list|,
name|res
operator|.
name|intValue
argument_list|()
argument_list|)
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
name|Assert
operator|.
name|assertEquals
argument_list|(
name|num
argument_list|,
name|value
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFromStreamMultipleSubscriptionsWithDirect ()
specifier|public
name|void
name|testFromStreamMultipleSubscriptionsWithDirect
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
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
literal|"direct:reactive"
argument_list|)
operator|.
name|to
argument_list|(
literal|"reactive-streams:direct"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|CountDownLatch
name|latch1
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|crs
operator|.
name|fromStream
argument_list|(
literal|"direct"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|res
lambda|->
name|latch1
operator|.
name|countDown
argument_list|()
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
name|CountDownLatch
name|latch2
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|crs
operator|.
name|fromStream
argument_list|(
literal|"direct"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|res
lambda|->
name|latch2
operator|.
name|countDown
argument_list|()
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:reactive"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:reactive"
argument_list|,
literal|2
argument_list|)
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
name|Assert
operator|.
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
block|}
annotation|@
name|Test
DECL|method|testMultipleSubscriptionsWithTimer ()
specifier|public
name|void
name|testMultipleSubscriptionsWithTimer
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"reactive-streams:tick"
argument_list|)
expr_stmt|;
block|}
block|}
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
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|crs
operator|.
name|fromStream
argument_list|(
literal|"tick"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|subscribe
argument_list|(
name|res
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
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|crs
operator|.
name|fromStream
argument_list|(
literal|"tick"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|subscribe
argument_list|(
name|res
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
comment|// Un subscribe both
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
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|crs
operator|.
name|fromStream
argument_list|(
literal|"tick"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|subscribe
argument_list|(
name|res
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
DECL|method|testFrom ()
specifier|public
name|void
name|testFrom
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|timer
init|=
name|crs
operator|.
name|from
argument_list|(
literal|"timer:reactive?period=250&repeatCount=3"
argument_list|)
decl_stmt|;
name|AtomicInteger
name|value
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|timer
argument_list|)
operator|.
name|map
argument_list|(
name|exchange
lambda|->
name|ExchangeHelper
operator|.
name|getHeaderOrProperty
argument_list|(
name|exchange
argument_list|,
name|Exchange
operator|.
name|TIMER_COUNTER
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|res
lambda|->
name|Assert
operator|.
name|assertEquals
argument_list|(
name|value
operator|.
name|incrementAndGet
argument_list|()
argument_list|,
name|res
operator|.
name|intValue
argument_list|()
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|res
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
name|Assert
operator|.
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// ************************************************
comment|// fromPublisher
comment|// ************************************************
annotation|@
name|Test
DECL|method|testFromPublisher ()
specifier|public
name|void
name|testFromPublisher
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
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
literal|"direct:source"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:stream"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"after stream: ${body}"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|crs
operator|.
name|process
argument_list|(
literal|"direct:stream"
argument_list|,
name|publisher
lambda|->
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|publisher
argument_list|)
operator|.
name|map
argument_list|(
name|e
lambda|->
block|{
name|int
name|i
operator|=
name|e
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
argument_list|;
name|e
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
operator|-
name|i
argument_list|)
argument_list|;                          return
name|e
argument_list|;
block|}
block|)
end_class

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_for
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"after stream: "
operator|+
operator|(
operator|-
name|i
operator|)
argument_list|,
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:source"
argument_list|,
name|i
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_for

begin_function
unit|}      @
name|Test
DECL|method|testFromPublisherWithConversion ()
specifier|public
name|void
name|testFromPublisherWithConversion
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
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
literal|"direct:source"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:stream"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"after stream: ${body}"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|crs
operator|.
name|process
argument_list|(
literal|"direct:stream"
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|publisher
lambda|->
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|publisher
argument_list|)
operator|.
name|map
argument_list|(
name|Math
operator|::
name|negateExact
argument_list|)
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
literal|3
condition|;
name|i
operator|++
control|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"after stream: "
operator|+
operator|(
operator|-
name|i
operator|)
argument_list|,
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:source"
argument_list|,
name|i
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_comment
comment|// ************************************************
end_comment

begin_comment
comment|// toStream/to
end_comment

begin_comment
comment|// ************************************************
end_comment

begin_function
annotation|@
name|Test
DECL|method|testToStream ()
specifier|public
name|void
name|testToStream
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"reactive-streams:reactive"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|"123"
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
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|publisher
init|=
name|crs
operator|.
name|toStream
argument_list|(
literal|"reactive"
argument_list|,
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
argument_list|)
decl_stmt|;
name|Exchange
name|res
init|=
name|Flowable
operator|.
name|fromPublisher
argument_list|(
name|publisher
argument_list|)
operator|.
name|blockingFirst
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|res
argument_list|)
expr_stmt|;
name|String
name|content
init|=
name|res
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testTo ()
specifier|public
name|void
name|testTo
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|AtomicInteger
name|value
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
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
name|just
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|.
name|flatMap
argument_list|(
name|e
lambda|->
name|crs
operator|.
name|to
argument_list|(
literal|"bean:hello"
argument_list|,
name|e
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|res
lambda|->
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Hello "
operator|+
name|value
operator|.
name|incrementAndGet
argument_list|()
argument_list|,
name|res
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|res
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
name|Assert
operator|.
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testToWithExchange ()
specifier|public
name|void
name|testToWithExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|AtomicInteger
name|value
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
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
name|just
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|.
name|flatMap
argument_list|(
name|e
lambda|->
name|crs
operator|.
name|to
argument_list|(
literal|"bean:hello"
argument_list|,
name|e
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|res
lambda|->
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Hello "
operator|+
name|value
operator|.
name|incrementAndGet
argument_list|()
argument_list|,
name|res
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|res
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
name|Assert
operator|.
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testToFunction ()
specifier|public
name|void
name|testToFunction
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|AtomicInteger
name|value
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
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
name|Function
argument_list|<
name|Object
argument_list|,
name|Publisher
argument_list|<
name|String
argument_list|>
argument_list|>
name|fun
init|=
name|crs
operator|.
name|to
argument_list|(
literal|"bean:hello"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Flowable
operator|.
name|just
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|.
name|flatMap
argument_list|(
name|fun
operator|::
name|apply
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|res
lambda|->
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Hello "
operator|+
name|value
operator|.
name|incrementAndGet
argument_list|()
argument_list|,
name|res
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|res
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
name|Assert
operator|.
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testToFunctionWithExchange ()
specifier|public
name|void
name|testToFunctionWithExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|AtomicInteger
name|value
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
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
name|Function
argument_list|<
name|Object
argument_list|,
name|Publisher
argument_list|<
name|Exchange
argument_list|>
argument_list|>
name|fun
init|=
name|crs
operator|.
name|to
argument_list|(
literal|"bean:hello"
argument_list|)
decl_stmt|;
name|Flowable
operator|.
name|just
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|.
name|flatMap
argument_list|(
name|fun
operator|::
name|apply
argument_list|)
operator|.
name|map
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|res
lambda|->
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Hello "
operator|+
name|value
operator|.
name|incrementAndGet
argument_list|()
argument_list|,
name|res
argument_list|)
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|res
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
name|Assert
operator|.
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_comment
comment|// ************************************************
end_comment

begin_comment
comment|// subscriber
end_comment

begin_comment
comment|// ************************************************
end_comment

begin_function
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
name|context
operator|.
name|start
argument_list|()
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
literal|"direct:reactor"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Flowable
operator|.
name|just
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|.
name|subscribe
argument_list|(
name|crs
operator|.
name|subscriber
argument_list|(
literal|"direct:reactor"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|int
name|idx
init|=
literal|1
decl_stmt|;
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
name|Assert
operator|.
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
name|idx
operator|++
argument_list|)
argument_list|,
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
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_comment
comment|// ************************************************
end_comment

begin_comment
comment|// misc
end_comment

begin_comment
comment|// ************************************************
end_comment

begin_function
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalStateException
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
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
end_function

unit|}
end_unit

