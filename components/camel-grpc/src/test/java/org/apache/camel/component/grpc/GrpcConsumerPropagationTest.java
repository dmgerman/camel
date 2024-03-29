begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ManagedChannelBuilder
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
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
DECL|class|GrpcConsumerPropagationTest
specifier|public
class|class
name|GrpcConsumerPropagationTest
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
name|GrpcConsumerPropagationTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|GRPC_ASYNC_NEXT_REQUEST_TEST_PORT
specifier|private
specifier|static
specifier|final
name|int
name|GRPC_ASYNC_NEXT_REQUEST_TEST_PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|GRPC_ASYNC_COMPLETED_REQUEST_TEST_PORT
specifier|private
specifier|static
specifier|final
name|int
name|GRPC_ASYNC_COMPLETED_REQUEST_TEST_PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|GRPC_TEST_PING_ID
specifier|private
specifier|static
specifier|final
name|int
name|GRPC_TEST_PING_ID
init|=
literal|1
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
DECL|field|asyncOnNextChannel
specifier|private
name|ManagedChannel
name|asyncOnNextChannel
decl_stmt|;
DECL|field|asyncOnCompletedChannel
specifier|private
name|ManagedChannel
name|asyncOnCompletedChannel
decl_stmt|;
DECL|field|asyncOnNextStub
specifier|private
name|PingPongGrpc
operator|.
name|PingPongStub
name|asyncOnNextStub
decl_stmt|;
DECL|field|asyncOnCompletedStub
specifier|private
name|PingPongGrpc
operator|.
name|PingPongStub
name|asyncOnCompletedStub
decl_stmt|;
annotation|@
name|Before
DECL|method|startGrpcChannels ()
specifier|public
name|void
name|startGrpcChannels
parameter_list|()
block|{
name|asyncOnNextChannel
operator|=
name|ManagedChannelBuilder
operator|.
name|forAddress
argument_list|(
literal|"localhost"
argument_list|,
name|GRPC_ASYNC_NEXT_REQUEST_TEST_PORT
argument_list|)
operator|.
name|usePlaintext
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
name|asyncOnCompletedChannel
operator|=
name|ManagedChannelBuilder
operator|.
name|forAddress
argument_list|(
literal|"localhost"
argument_list|,
name|GRPC_ASYNC_COMPLETED_REQUEST_TEST_PORT
argument_list|)
operator|.
name|usePlaintext
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
name|asyncOnNextStub
operator|=
name|PingPongGrpc
operator|.
name|newStub
argument_list|(
name|asyncOnNextChannel
argument_list|)
expr_stmt|;
name|asyncOnCompletedStub
operator|=
name|PingPongGrpc
operator|.
name|newStub
argument_list|(
name|asyncOnCompletedChannel
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|stopGrpcChannels ()
specifier|public
name|void
name|stopGrpcChannels
parameter_list|()
throws|throws
name|Exception
block|{
name|asyncOnNextChannel
operator|.
name|shutdown
argument_list|()
operator|.
name|awaitTermination
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|asyncOnCompletedChannel
operator|.
name|shutdown
argument_list|()
operator|.
name|awaitTermination
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOnNextPropagation ()
specifier|public
name|void
name|testOnNextPropagation
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"gRPC pingAsyncSync method aync test start"
argument_list|)
expr_stmt|;
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
name|GRPC_TEST_PING_ID
argument_list|)
operator|.
name|build
argument_list|()
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
name|StreamObserver
argument_list|<
name|PingRequest
argument_list|>
name|requestObserver
init|=
name|asyncOnNextStub
operator|.
name|pingAsyncSync
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
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:async-on-next-propagation"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedHeaderValuesReceivedInAnyOrder
argument_list|(
name|GrpcConstants
operator|.
name|GRPC_EVENT_TYPE_HEADER
argument_list|,
name|GrpcConstants
operator|.
name|GRPC_EVENT_TYPE_ON_NEXT
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedHeaderValuesReceivedInAnyOrder
argument_list|(
name|GrpcConstants
operator|.
name|GRPC_METHOD_NAME_HEADER
argument_list|,
literal|"pingAsyncSync"
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
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
name|pongResponse
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GRPC_TEST_PING_ID
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
block|}
annotation|@
name|Test
DECL|method|testOnCompletedPropagation ()
specifier|public
name|void
name|testOnCompletedPropagation
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"gRPC pingAsyncAsync method aync test start"
argument_list|)
expr_stmt|;
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
name|GRPC_TEST_PING_ID
argument_list|)
operator|.
name|build
argument_list|()
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
name|StreamObserver
argument_list|<
name|PingRequest
argument_list|>
name|requestObserver
init|=
name|asyncOnCompletedStub
operator|.
name|pingAsyncAsync
argument_list|(
name|responseObserver
argument_list|)
decl_stmt|;
name|requestObserver
operator|.
name|onCompleted
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
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:async-on-completed-propagation"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedHeaderValuesReceivedInAnyOrder
argument_list|(
name|GrpcConstants
operator|.
name|GRPC_EVENT_TYPE_HEADER
argument_list|,
name|GrpcConstants
operator|.
name|GRPC_EVENT_TYPE_ON_COMPLETED
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|expectedHeaderValuesReceivedInAnyOrder
argument_list|(
name|GrpcConstants
operator|.
name|GRPC_METHOD_NAME_HEADER
argument_list|,
literal|"pingAsyncAsync"
argument_list|)
expr_stmt|;
name|mockEndpoint
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
block|{
name|from
argument_list|(
literal|"grpc://localhost:"
operator|+
name|GRPC_ASYNC_NEXT_REQUEST_TEST_PORT
operator|+
literal|"/org.apache.camel.component.grpc.PingPong?consumerStrategy=PROPAGATION"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:async-on-next-propagation"
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
literal|"grpc://localhost:"
operator|+
name|GRPC_ASYNC_COMPLETED_REQUEST_TEST_PORT
operator|+
literal|"/org.apache.camel.component.grpc.PingPong?consumerStrategy=PROPAGATION&forwardOnCompleted=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:async-on-completed-propagation"
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
name|latch
operator|.
name|countDown
argument_list|()
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
DECL|method|buildAsyncPongResponse (PingRequest pingRequests)
specifier|public
name|PongResponse
name|buildAsyncPongResponse
parameter_list|(
name|PingRequest
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
name|getPingId
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

