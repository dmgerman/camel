begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.grpc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|grpc
package|;
end_package

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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|junittoolbox
operator|.
name|MultithreadingTester
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|junittoolbox
operator|.
name|RunnableAssert
import|;
end_import

begin_import
import|import
name|io
operator|.
name|grpc
operator|.
name|ManagedChannel
import|;
end_import

begin_import
import|import
name|io
operator|.
name|grpc
operator|.
name|netty
operator|.
name|NettyChannelBuilder
import|;
end_import

begin_import
import|import
name|io
operator|.
name|grpc
operator|.
name|stub
operator|.
name|StreamObserver
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
name|test
operator|.
name|AvailablePortFinder
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|GrpcConsumerConcurrentTest
specifier|public
class|class
name|GrpcConsumerConcurrentTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|GrpcConsumerConcurrentTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|GRPC_ASYNC_REQUEST_TEST_PORT
specifier|private
specifier|static
specifier|final
name|int
name|GRPC_ASYNC_REQUEST_TEST_PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|GRPC_HEADERS_TEST_PORT
specifier|private
specifier|static
specifier|final
name|int
name|GRPC_HEADERS_TEST_PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|CONCURRENT_THREAD_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|CONCURRENT_THREAD_COUNT
init|=
literal|300
decl_stmt|;
DECL|field|ROUNDS_PER_THREAD_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|ROUNDS_PER_THREAD_COUNT
init|=
literal|10
decl_stmt|;
DECL|field|GRPC_TEST_PING_VALUE
specifier|private
specifier|static
specifier|final
name|String
name|GRPC_TEST_PING_VALUE
init|=
literal|"PING"
decl_stmt|;
DECL|field|GRPC_TEST_PONG_VALUE
specifier|private
specifier|static
specifier|final
name|String
name|GRPC_TEST_PONG_VALUE
init|=
literal|"PONG"
decl_stmt|;
DECL|field|GRPC_USER_AGENT_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|GRPC_USER_AGENT_PREFIX
init|=
literal|"user-agent-"
decl_stmt|;
DECL|field|idCounter
specifier|private
specifier|static
name|AtomicInteger
name|idCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|method|createId ()
specifier|public
specifier|static
name|Integer
name|createId
parameter_list|()
block|{
return|return
name|idCounter
operator|.
name|getAndIncrement
argument_list|()
return|;
block|}
DECL|method|getId ()
specifier|public
specifier|static
name|Integer
name|getId
parameter_list|()
block|{
return|return
name|idCounter
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|testAsyncWithConcurrentThreads ()
specifier|public
name|void
name|testAsyncWithConcurrentThreads
parameter_list|()
throws|throws
name|Exception
block|{
name|RunnableAssert
name|ra
init|=
operator|new
name|RunnableAssert
argument_list|(
literal|"foo"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
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
name|ManagedChannel
name|asyncRequestChannel
init|=
name|NettyChannelBuilder
operator|.
name|forAddress
argument_list|(
literal|"localhost"
argument_list|,
name|GRPC_ASYNC_REQUEST_TEST_PORT
argument_list|)
operator|.
name|usePlaintext
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|PingPongGrpc
operator|.
name|PingPongStub
name|asyncNonBlockingStub
init|=
name|PingPongGrpc
operator|.
name|newStub
argument_list|(
name|asyncRequestChannel
argument_list|)
decl_stmt|;
name|PongResponseStreamObserver
name|responseObserver
init|=
operator|new
name|PongResponseStreamObserver
argument_list|(
name|latch
argument_list|)
decl_stmt|;
name|int
name|instanceId
init|=
name|createId
argument_list|()
decl_stmt|;
specifier|final
name|PingRequest
name|pingRequest
init|=
name|PingRequest
operator|.
name|newBuilder
argument_list|()
operator|.
name|setPingName
argument_list|(
name|GRPC_TEST_PING_VALUE
argument_list|)
operator|.
name|setPingId
argument_list|(
name|instanceId
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|StreamObserver
argument_list|<
name|PingRequest
argument_list|>
name|requestObserver
init|=
name|asyncNonBlockingStub
operator|.
name|pingAsyncAsync
argument_list|(
name|responseObserver
argument_list|)
decl_stmt|;
name|requestObserver
operator|.
name|onNext
argument_list|(
name|pingRequest
argument_list|)
expr_stmt|;
name|requestObserver
operator|.
name|onNext
argument_list|(
name|pingRequest
argument_list|)
expr_stmt|;
name|requestObserver
operator|.
name|onCompleted
argument_list|()
expr_stmt|;
try|try
block|{
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
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|PongResponse
name|pongResponse
init|=
name|responseObserver
operator|.
name|getPongResponse
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"instanceId = "
operator|+
name|instanceId
argument_list|,
name|pongResponse
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|instanceId
argument_list|,
name|pongResponse
operator|.
name|getPongId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GRPC_TEST_PING_VALUE
operator|+
name|GRPC_TEST_PONG_VALUE
argument_list|,
name|pongResponse
operator|.
name|getPongName
argument_list|()
argument_list|)
expr_stmt|;
name|asyncRequestChannel
operator|.
name|shutdown
argument_list|()
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
operator|new
name|MultithreadingTester
argument_list|()
operator|.
name|add
argument_list|(
name|ra
argument_list|)
operator|.
name|numThreads
argument_list|(
name|CONCURRENT_THREAD_COUNT
argument_list|)
operator|.
name|numRoundsPerThread
argument_list|(
name|ROUNDS_PER_THREAD_COUNT
argument_list|)
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHeadersWithConcurrentThreads ()
specifier|public
name|void
name|testHeadersWithConcurrentThreads
parameter_list|()
throws|throws
name|Exception
block|{
name|RunnableAssert
name|ra
init|=
operator|new
name|RunnableAssert
argument_list|(
literal|"foo"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|int
name|instanceId
init|=
name|createId
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
name|ManagedChannel
name|asyncRequestChannel
init|=
name|NettyChannelBuilder
operator|.
name|forAddress
argument_list|(
literal|"localhost"
argument_list|,
name|GRPC_HEADERS_TEST_PORT
argument_list|)
operator|.
name|userAgent
argument_list|(
name|GRPC_USER_AGENT_PREFIX
operator|+
name|instanceId
argument_list|)
operator|.
name|usePlaintext
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|PingPongGrpc
operator|.
name|PingPongStub
name|asyncNonBlockingStub
init|=
name|PingPongGrpc
operator|.
name|newStub
argument_list|(
name|asyncRequestChannel
argument_list|)
decl_stmt|;
name|PongResponseStreamObserver
name|responseObserver
init|=
operator|new
name|PongResponseStreamObserver
argument_list|(
name|latch
argument_list|)
decl_stmt|;
specifier|final
name|PingRequest
name|pingRequest
init|=
name|PingRequest
operator|.
name|newBuilder
argument_list|()
operator|.
name|setPingName
argument_list|(
name|GRPC_TEST_PING_VALUE
argument_list|)
operator|.
name|setPingId
argument_list|(
name|instanceId
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|StreamObserver
argument_list|<
name|PingRequest
argument_list|>
name|requestObserver
init|=
name|asyncNonBlockingStub
operator|.
name|pingAsyncAsync
argument_list|(
name|responseObserver
argument_list|)
decl_stmt|;
name|requestObserver
operator|.
name|onNext
argument_list|(
name|pingRequest
argument_list|)
expr_stmt|;
name|requestObserver
operator|.
name|onNext
argument_list|(
name|pingRequest
argument_list|)
expr_stmt|;
name|requestObserver
operator|.
name|onCompleted
argument_list|()
expr_stmt|;
try|try
block|{
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
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|PongResponse
name|pongResponse
init|=
name|responseObserver
operator|.
name|getPongResponse
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"instanceId = "
operator|+
name|instanceId
argument_list|,
name|pongResponse
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|instanceId
argument_list|,
name|pongResponse
operator|.
name|getPongId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GRPC_USER_AGENT_PREFIX
operator|+
name|instanceId
argument_list|,
name|pongResponse
operator|.
name|getPongName
argument_list|()
argument_list|)
expr_stmt|;
name|asyncRequestChannel
operator|.
name|shutdown
argument_list|()
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
operator|new
name|MultithreadingTester
argument_list|()
operator|.
name|add
argument_list|(
name|ra
argument_list|)
operator|.
name|numThreads
argument_list|(
name|CONCURRENT_THREAD_COUNT
argument_list|)
operator|.
name|numRoundsPerThread
argument_list|(
name|ROUNDS_PER_THREAD_COUNT
argument_list|)
operator|.
name|run
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
block|{
name|from
argument_list|(
literal|"grpc://org.apache.camel.component.grpc.PingPong?synchronous=true&processingStrategy=AGGREGATION&host=localhost&port="
operator|+
name|GRPC_ASYNC_REQUEST_TEST_PORT
argument_list|)
operator|.
name|bean
argument_list|(
operator|new
name|GrpcMessageBuilder
argument_list|()
argument_list|,
literal|"buildAsyncPongResponse"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"grpc://org.apache.camel.component.grpc.PingPong?synchronous=true&processingStrategy=AGGREGATION&host=localhost&port="
operator|+
name|GRPC_HEADERS_TEST_PORT
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|HeaderExchangeProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|PongResponseStreamObserver
specifier|public
class|class
name|PongResponseStreamObserver
implements|implements
name|StreamObserver
argument_list|<
name|PongResponse
argument_list|>
block|{
DECL|field|pongResponse
specifier|private
name|PongResponse
name|pongResponse
decl_stmt|;
DECL|field|latch
specifier|private
specifier|final
name|CountDownLatch
name|latch
decl_stmt|;
DECL|method|PongResponseStreamObserver (CountDownLatch latch)
specifier|public
name|PongResponseStreamObserver
parameter_list|(
name|CountDownLatch
name|latch
parameter_list|)
block|{
name|this
operator|.
name|latch
operator|=
name|latch
expr_stmt|;
block|}
DECL|method|getPongResponse ()
specifier|public
name|PongResponse
name|getPongResponse
parameter_list|()
block|{
return|return
name|pongResponse
return|;
block|}
annotation|@
name|Override
DECL|method|onNext (PongResponse value)
specifier|public
name|void
name|onNext
parameter_list|(
name|PongResponse
name|value
parameter_list|)
block|{
name|pongResponse
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onError (Throwable t)
specifier|public
name|void
name|onError
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Exception"
argument_list|,
name|t
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onCompleted ()
specifier|public
name|void
name|onCompleted
parameter_list|()
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|GrpcMessageBuilder
specifier|public
class|class
name|GrpcMessageBuilder
block|{
DECL|method|buildAsyncPongResponse (List<PingRequest> pingRequests)
specifier|public
name|PongResponse
name|buildAsyncPongResponse
parameter_list|(
name|List
argument_list|<
name|PingRequest
argument_list|>
name|pingRequests
parameter_list|)
block|{
return|return
name|PongResponse
operator|.
name|newBuilder
argument_list|()
operator|.
name|setPongName
argument_list|(
name|pingRequests
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPingName
argument_list|()
operator|+
name|GRPC_TEST_PONG_VALUE
argument_list|)
operator|.
name|setPongId
argument_list|(
name|pingRequests
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPingId
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
DECL|class|HeaderExchangeProcessor
specifier|public
class|class
name|HeaderExchangeProcessor
implements|implements
name|Processor
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|process (Exchange exchange)
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
name|List
argument_list|<
name|PingRequest
argument_list|>
name|pingRequests
init|=
operator|(
name|List
argument_list|<
name|PingRequest
argument_list|>
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|String
name|userAgentName
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GrpcConstants
operator|.
name|GRPC_USER_AGENT_HEADER
argument_list|)
decl_stmt|;
comment|// As user agent name is prepended the library's user agent
comment|// information it's necessary to extract this value (before first
comment|// space)
name|PongResponse
name|pongResponse
init|=
name|PongResponse
operator|.
name|newBuilder
argument_list|()
operator|.
name|setPongName
argument_list|(
name|userAgentName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|userAgentName
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
argument_list|)
argument_list|)
operator|.
name|setPongId
argument_list|(
name|pingRequests
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPingId
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|pongResponse
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

