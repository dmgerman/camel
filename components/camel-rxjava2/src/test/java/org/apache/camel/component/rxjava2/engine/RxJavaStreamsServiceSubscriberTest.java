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
name|atomic
operator|.
name|AtomicLong
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
name|mock
operator|.
name|MockEndpoint
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

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Subscriber
import|;
end_import

begin_class
DECL|class|RxJavaStreamsServiceSubscriberTest
specifier|public
class|class
name|RxJavaStreamsServiceSubscriberTest
extends|extends
name|RxJavaStreamsServiceTestSupport
block|{
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
literal|"reactive-streams:sub1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:sub1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"reactive-streams:sub2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:sub2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer:tick?period=50"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"random(500)"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:sub3"
argument_list|)
operator|.
name|to
argument_list|(
literal|"reactive-streams:pub"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Subscriber
argument_list|<
name|Integer
argument_list|>
name|sub1
init|=
name|crs
operator|.
name|streamSubscriber
argument_list|(
literal|"sub1"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Subscriber
argument_list|<
name|Integer
argument_list|>
name|sub2
init|=
name|crs
operator|.
name|streamSubscriber
argument_list|(
literal|"sub2"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Publisher
argument_list|<
name|Integer
argument_list|>
name|pub
init|=
name|crs
operator|.
name|fromStream
argument_list|(
literal|"pub"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|pub
operator|.
name|subscribe
argument_list|(
name|sub1
argument_list|)
expr_stmt|;
name|pub
operator|.
name|subscribe
argument_list|(
name|sub2
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|int
name|count
init|=
literal|2
decl_stmt|;
name|MockEndpoint
name|e1
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:sub1"
argument_list|)
decl_stmt|;
name|e1
operator|.
name|expectedMinimumMessageCount
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|e1
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|MockEndpoint
name|e2
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:sub2"
argument_list|)
decl_stmt|;
name|e2
operator|.
name|expectedMinimumMessageCount
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|e2
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|MockEndpoint
name|e3
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:sub3"
argument_list|)
decl_stmt|;
name|e3
operator|.
name|expectedMinimumMessageCount
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|e3
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
name|Exchange
name|ex1
init|=
name|e1
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Exchange
name|ex2
init|=
name|e2
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Exchange
name|ex3
init|=
name|e3
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ex1
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|ex2
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ex1
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
name|ex3
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testSingleConsumer ()
specifier|public
name|void
name|testSingleConsumer
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
literal|"reactive-streams:singleConsumer"
argument_list|)
operator|.
name|process
argument_list|()
operator|.
name|message
argument_list|(
name|m
lambda|->
name|m
operator|.
name|setHeader
argument_list|(
literal|"thread"
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:singleBucket"
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
name|Flowable
operator|.
name|range
argument_list|(
literal|0
argument_list|,
literal|1000
argument_list|)
operator|.
name|subscribe
argument_list|(
name|crs
operator|.
name|streamSubscriber
argument_list|(
literal|"singleConsumer"
argument_list|,
name|Number
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:singleBucket"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|endpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|x
lambda|->
name|x
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"thread"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|distinct
argument_list|()
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
comment|// Ensure order is preserved when using a single consumer
name|AtomicLong
name|num
init|=
operator|new
name|AtomicLong
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|x
lambda|->
name|x
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Long
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|forEach
argument_list|(
name|n
lambda|->
name|Assert
operator|.
name|assertEquals
argument_list|(
name|num
operator|.
name|getAndIncrement
argument_list|()
argument_list|,
name|n
operator|.
name|longValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMultipleConsumers ()
specifier|public
name|void
name|testMultipleConsumers
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
literal|"reactive-streams:multipleConsumers?concurrentConsumers=3"
argument_list|)
operator|.
name|process
argument_list|()
operator|.
name|message
argument_list|(
name|m
lambda|->
name|m
operator|.
name|setHeader
argument_list|(
literal|"thread"
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:multipleBucket"
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
name|Flowable
operator|.
name|range
argument_list|(
literal|0
argument_list|,
literal|1000
argument_list|)
operator|.
name|subscribe
argument_list|(
name|crs
operator|.
name|streamSubscriber
argument_list|(
literal|"multipleConsumers"
argument_list|,
name|Number
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:multipleBucket"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|endpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|x
lambda|->
name|x
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"thread"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|distinct
argument_list|()
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
comment|// Order cannot be preserved when using multiple consumers
block|}
block|}
end_class

end_unit

