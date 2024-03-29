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
name|io
operator|.
name|File
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
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLException
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
name|GrpcSslContexts
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
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|ssl
operator|.
name|SslProvider
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
name|grpc
operator|.
name|auth
operator|.
name|jwt
operator|.
name|JwtAlgorithm
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
name|grpc
operator|.
name|auth
operator|.
name|jwt
operator|.
name|JwtCallCredentials
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
name|grpc
operator|.
name|auth
operator|.
name|jwt
operator|.
name|JwtHelper
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
DECL|class|GrpcConsumerSecurityTest
specifier|public
class|class
name|GrpcConsumerSecurityTest
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
name|GrpcConsumerSecurityTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|GRPC_TLS_TEST_PORT
specifier|private
specifier|static
specifier|final
name|int
name|GRPC_TLS_TEST_PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|GRPC_JWT_CORRECT_TEST_PORT
specifier|private
specifier|static
specifier|final
name|int
name|GRPC_JWT_CORRECT_TEST_PORT
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
DECL|field|GRPC_JWT_INCORRECT_TEST_PORT
specifier|private
specifier|static
specifier|final
name|int
name|GRPC_JWT_INCORRECT_TEST_PORT
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
DECL|field|GRPC_JWT_CORRECT_SECRET
specifier|private
specifier|static
specifier|final
name|String
name|GRPC_JWT_CORRECT_SECRET
init|=
literal|"correctsecret"
decl_stmt|;
DECL|field|GRPC_JWT_INCORRECT_SECRET
specifier|private
specifier|static
specifier|final
name|String
name|GRPC_JWT_INCORRECT_SECRET
init|=
literal|"incorrectsecret"
decl_stmt|;
DECL|field|tlsChannel
specifier|private
name|ManagedChannel
name|tlsChannel
decl_stmt|;
DECL|field|jwtCorrectChannel
specifier|private
name|ManagedChannel
name|jwtCorrectChannel
decl_stmt|;
DECL|field|jwtIncorrectChannel
specifier|private
name|ManagedChannel
name|jwtIncorrectChannel
decl_stmt|;
DECL|field|tlsAsyncStub
specifier|private
name|PingPongGrpc
operator|.
name|PingPongStub
name|tlsAsyncStub
decl_stmt|;
DECL|field|jwtCorrectAsyncStub
specifier|private
name|PingPongGrpc
operator|.
name|PingPongStub
name|jwtCorrectAsyncStub
decl_stmt|;
DECL|field|jwtIncorrectAsyncStub
specifier|private
name|PingPongGrpc
operator|.
name|PingPongStub
name|jwtIncorrectAsyncStub
decl_stmt|;
annotation|@
name|Before
DECL|method|startGrpcChannels ()
specifier|public
name|void
name|startGrpcChannels
parameter_list|()
throws|throws
name|SSLException
block|{
name|String
name|correctJwtToken
init|=
name|JwtHelper
operator|.
name|createJwtToken
argument_list|(
name|JwtAlgorithm
operator|.
name|HMAC256
argument_list|,
name|GRPC_JWT_CORRECT_SECRET
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
name|incorrectJwtToken
init|=
name|JwtHelper
operator|.
name|createJwtToken
argument_list|(
name|JwtAlgorithm
operator|.
name|HMAC256
argument_list|,
name|GRPC_JWT_INCORRECT_SECRET
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|tlsChannel
operator|=
name|NettyChannelBuilder
operator|.
name|forAddress
argument_list|(
literal|"localhost"
argument_list|,
name|GRPC_TLS_TEST_PORT
argument_list|)
operator|.
name|sslContext
argument_list|(
name|GrpcSslContexts
operator|.
name|forClient
argument_list|()
operator|.
name|sslProvider
argument_list|(
name|SslProvider
operator|.
name|OPENSSL
argument_list|)
operator|.
name|keyManager
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/certs/client.pem"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/certs/client.key"
argument_list|)
argument_list|)
operator|.
name|trustManager
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/certs/ca.pem"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|jwtCorrectChannel
operator|=
name|NettyChannelBuilder
operator|.
name|forAddress
argument_list|(
literal|"localhost"
argument_list|,
name|GRPC_JWT_CORRECT_TEST_PORT
argument_list|)
operator|.
name|usePlaintext
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
name|jwtIncorrectChannel
operator|=
name|NettyChannelBuilder
operator|.
name|forAddress
argument_list|(
literal|"localhost"
argument_list|,
name|GRPC_JWT_INCORRECT_TEST_PORT
argument_list|)
operator|.
name|usePlaintext
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
name|tlsAsyncStub
operator|=
name|PingPongGrpc
operator|.
name|newStub
argument_list|(
name|tlsChannel
argument_list|)
expr_stmt|;
name|jwtCorrectAsyncStub
operator|=
name|PingPongGrpc
operator|.
name|newStub
argument_list|(
name|jwtCorrectChannel
argument_list|)
operator|.
name|withCallCredentials
argument_list|(
operator|new
name|JwtCallCredentials
argument_list|(
name|correctJwtToken
argument_list|)
argument_list|)
expr_stmt|;
name|jwtIncorrectAsyncStub
operator|=
name|PingPongGrpc
operator|.
name|newStub
argument_list|(
name|jwtIncorrectChannel
argument_list|)
operator|.
name|withCallCredentials
argument_list|(
operator|new
name|JwtCallCredentials
argument_list|(
name|incorrectJwtToken
argument_list|)
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
name|tlsChannel
operator|.
name|shutdown
argument_list|()
operator|.
name|awaitTermination
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|jwtCorrectChannel
operator|.
name|shutdown
argument_list|()
operator|.
name|awaitTermination
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|jwtIncorrectChannel
operator|.
name|shutdown
argument_list|()
operator|.
name|awaitTermination
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWithEnableTLS ()
specifier|public
name|void
name|testWithEnableTLS
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"gRPC pingAsyncSync method aync test with TLS enable start"
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
name|tlsAsyncStub
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
literal|"mock:tls-enable"
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
DECL|method|testWithCorrectJWT ()
specifier|public
name|void
name|testWithCorrectJWT
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"gRPC pingAsyncSync method aync test with correct JWT start"
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
name|jwtCorrectAsyncStub
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
literal|"mock:jwt-correct-secret"
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
DECL|method|testWithIncorrectJWT ()
specifier|public
name|void
name|testWithIncorrectJWT
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"gRPC pingAsyncSync method aync test with correct JWT start"
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
name|jwtIncorrectAsyncStub
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
literal|"mock:jwt-incorrect-secret"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|0
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
name|GRPC_TLS_TEST_PORT
operator|+
literal|"/org.apache.camel.component.grpc.PingPong?consumerStrategy=PROPAGATION&"
operator|+
literal|"negotiationType=TLS&keyCertChainResource=file:src/test/resources/certs/server.pem&"
operator|+
literal|"keyResource=file:src/test/resources/certs/server.key&trustCertCollectionResource=file:src/test/resources/certs/ca.pem"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:tls-enable"
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
name|GRPC_JWT_CORRECT_TEST_PORT
operator|+
literal|"/org.apache.camel.component.grpc.PingPong?consumerStrategy=PROPAGATION&"
operator|+
literal|"authenticationType=JWT&jwtSecret="
operator|+
name|GRPC_JWT_CORRECT_SECRET
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:jwt-correct-secret"
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
name|GRPC_JWT_INCORRECT_TEST_PORT
operator|+
literal|"/org.apache.camel.component.grpc.PingPong?consumerStrategy=PROPAGATION&"
operator|+
literal|"authenticationType=JWT&jwtSecret="
operator|+
name|GRPC_JWT_CORRECT_SECRET
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:jwt-incorrect-secret"
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

