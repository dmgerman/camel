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
name|After
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|TimeUnit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Stopwatch
import|;
end_import

begin_import
import|import
name|io
operator|.
name|grpc
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|io
operator|.
name|grpc
operator|.
name|ServerBuilder
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
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
DECL|class|GrpcProducerSyncTest
specifier|public
class|class
name|GrpcProducerSyncTest
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
name|GrpcProducerSyncTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|GRPC_TEST_PORT
specifier|private
specifier|static
specifier|final
name|int
name|GRPC_TEST_PORT
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
DECL|field|GRPC_TEST_PONG_ID01
specifier|private
specifier|static
specifier|final
name|int
name|GRPC_TEST_PONG_ID01
init|=
literal|1
decl_stmt|;
DECL|field|GRPC_TEST_PONG_ID02
specifier|private
specifier|static
specifier|final
name|int
name|GRPC_TEST_PONG_ID02
init|=
literal|2
decl_stmt|;
DECL|field|MULTIPLE_RUN_TEST_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|MULTIPLE_RUN_TEST_COUNT
init|=
literal|100
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
DECL|field|grpcServer
specifier|private
specifier|static
name|Server
name|grpcServer
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|startGrpcServer ()
specifier|public
specifier|static
name|void
name|startGrpcServer
parameter_list|()
throws|throws
name|Exception
block|{
name|grpcServer
operator|=
name|ServerBuilder
operator|.
name|forPort
argument_list|(
name|GRPC_TEST_PORT
argument_list|)
operator|.
name|addService
argument_list|(
operator|new
name|PingPongImpl
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"gRPC server started on port {}"
argument_list|,
name|GRPC_TEST_PORT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|stopGrpcServer ()
specifier|public
specifier|static
name|void
name|stopGrpcServer
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|grpcServer
operator|!=
literal|null
condition|)
block|{
name|grpcServer
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"gRPC server stoped"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testPingSyncSyncMethodInvocation ()
specifier|public
name|void
name|testPingSyncSyncMethodInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"gRPC PingSyncSync method test start"
argument_list|)
expr_stmt|;
comment|// Testing simple sync method invoke with host and port parameters
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
name|Object
name|pongResponse
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:grpc-sync-sync"
argument_list|,
name|pingRequest
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pongResponse
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pongResponse
operator|instanceof
name|PongResponse
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
operator|(
name|PongResponse
operator|)
name|pongResponse
operator|)
operator|.
name|getPongId
argument_list|()
argument_list|,
name|GRPC_TEST_PING_ID
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
operator|(
name|PongResponse
operator|)
name|pongResponse
operator|)
operator|.
name|getPongName
argument_list|()
argument_list|,
name|GRPC_TEST_PING_VALUE
operator|+
name|GRPC_TEST_PONG_VALUE
argument_list|)
expr_stmt|;
comment|// Testing simple sync method with name described in .proto file instead
comment|// of generated class
name|pongResponse
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:grpc-sync-proto-method-name"
argument_list|,
name|pingRequest
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|pongResponse
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pongResponse
operator|instanceof
name|PongResponse
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
operator|(
name|PongResponse
operator|)
name|pongResponse
operator|)
operator|.
name|getPongId
argument_list|()
argument_list|,
name|GRPC_TEST_PING_ID
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPingSyncSyncMultipleInvocation ()
specifier|public
name|void
name|testPingSyncSyncMultipleInvocation
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Stopwatch
name|stopwatch
init|=
name|Stopwatch
operator|.
name|createStarted
argument_list|()
decl_stmt|;
comment|// Multiple sync methods call for average performance estimation
for|for
control|(
name|int
name|id
init|=
literal|0
init|;
name|id
operator|<
name|MULTIPLE_RUN_TEST_COUNT
condition|;
name|id
operator|++
control|)
block|{
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
operator|+
name|id
argument_list|)
operator|.
name|setPingId
argument_list|(
name|id
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Object
name|pongResponse
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:grpc-sync-sync"
argument_list|,
name|pingRequest
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|(
operator|(
name|PongResponse
operator|)
name|pongResponse
operator|)
operator|.
name|getPongId
argument_list|()
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Multiple sync invocation time {} milliseconds, everage operations/sec {}"
argument_list|,
name|stopwatch
operator|.
name|stop
argument_list|()
operator|.
name|elapsed
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|,
name|Math
operator|.
name|round
argument_list|(
literal|1000
operator|*
name|MULTIPLE_RUN_TEST_COUNT
operator|/
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testPingSyncAsyncMethodInvocation ()
specifier|public
name|void
name|testPingSyncAsyncMethodInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"gRPC PingSyncAsync method test start"
argument_list|)
expr_stmt|;
comment|// Testing simple method with sync request and asyc response in synchronous invocation style
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
name|Object
name|pongResponse
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:grpc-sync-async"
argument_list|,
name|pingRequest
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pongResponse
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pongResponse
operator|instanceof
name|List
argument_list|<
name|?
argument_list|>
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
operator|(
name|List
argument_list|<
name|PongResponse
argument_list|>
operator|)
name|pongResponse
operator|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPongId
argument_list|()
argument_list|,
name|GRPC_TEST_PONG_ID01
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
operator|(
name|List
argument_list|<
name|PongResponse
argument_list|>
operator|)
name|pongResponse
operator|)
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getPongId
argument_list|()
argument_list|,
name|GRPC_TEST_PONG_ID02
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
operator|(
name|List
argument_list|<
name|PongResponse
argument_list|>
operator|)
name|pongResponse
operator|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPongName
argument_list|()
argument_list|,
name|GRPC_TEST_PING_VALUE
operator|+
name|GRPC_TEST_PONG_VALUE
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
block|{
name|from
argument_list|(
literal|"direct:grpc-sync-sync"
argument_list|)
operator|.
name|to
argument_list|(
literal|"grpc://localhost:"
operator|+
name|GRPC_TEST_PORT
operator|+
literal|"/org.apache.camel.component.grpc.PingPong?method=pingSyncSync&synchronous=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:grpc-sync-proto-method-name"
argument_list|)
operator|.
name|to
argument_list|(
literal|"grpc://localhost:"
operator|+
name|GRPC_TEST_PORT
operator|+
literal|"/org.apache.camel.component.grpc.PingPong?method=PingSyncSync&synchronous=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:grpc-sync-async"
argument_list|)
operator|.
name|to
argument_list|(
literal|"grpc://localhost:"
operator|+
name|GRPC_TEST_PORT
operator|+
literal|"/org.apache.camel.component.grpc.PingPong?method=pingSyncAsync&synchronous=true"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**      * Test gRPC PingPong server implementation      */
DECL|class|PingPongImpl
specifier|static
class|class
name|PingPongImpl
extends|extends
name|PingPongGrpc
operator|.
name|PingPongImplBase
block|{
annotation|@
name|Override
DECL|method|pingSyncSync (PingRequest request, StreamObserver<PongResponse> responseObserver)
specifier|public
name|void
name|pingSyncSync
parameter_list|(
name|PingRequest
name|request
parameter_list|,
name|StreamObserver
argument_list|<
name|PongResponse
argument_list|>
name|responseObserver
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"gRPC server received data from PingPong service PingId={} PingName={}"
argument_list|,
name|request
operator|.
name|getPingId
argument_list|()
argument_list|,
name|request
operator|.
name|getPingName
argument_list|()
argument_list|)
expr_stmt|;
name|PongResponse
name|response
init|=
name|PongResponse
operator|.
name|newBuilder
argument_list|()
operator|.
name|setPongName
argument_list|(
name|request
operator|.
name|getPingName
argument_list|()
operator|+
name|GRPC_TEST_PONG_VALUE
argument_list|)
operator|.
name|setPongId
argument_list|(
name|request
operator|.
name|getPingId
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|responseObserver
operator|.
name|onNext
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|responseObserver
operator|.
name|onCompleted
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|pingSyncAsync (PingRequest request, StreamObserver<PongResponse> responseObserver)
specifier|public
name|void
name|pingSyncAsync
parameter_list|(
name|PingRequest
name|request
parameter_list|,
name|StreamObserver
argument_list|<
name|PongResponse
argument_list|>
name|responseObserver
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"gRPC server received data from PingAsyncResponse service PingId={} PingName={}"
argument_list|,
name|request
operator|.
name|getPingId
argument_list|()
argument_list|,
name|request
operator|.
name|getPingName
argument_list|()
argument_list|)
expr_stmt|;
name|PongResponse
name|response01
init|=
name|PongResponse
operator|.
name|newBuilder
argument_list|()
operator|.
name|setPongName
argument_list|(
name|request
operator|.
name|getPingName
argument_list|()
operator|+
name|GRPC_TEST_PONG_VALUE
argument_list|)
operator|.
name|setPongId
argument_list|(
name|GRPC_TEST_PONG_ID01
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|PongResponse
name|response02
init|=
name|PongResponse
operator|.
name|newBuilder
argument_list|()
operator|.
name|setPongName
argument_list|(
name|request
operator|.
name|getPingName
argument_list|()
operator|+
name|GRPC_TEST_PONG_VALUE
argument_list|)
operator|.
name|setPongId
argument_list|(
name|GRPC_TEST_PONG_ID02
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|responseObserver
operator|.
name|onNext
argument_list|(
name|response01
argument_list|)
expr_stmt|;
name|responseObserver
operator|.
name|onNext
argument_list|(
name|response02
argument_list|)
expr_stmt|;
name|responseObserver
operator|.
name|onCompleted
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

