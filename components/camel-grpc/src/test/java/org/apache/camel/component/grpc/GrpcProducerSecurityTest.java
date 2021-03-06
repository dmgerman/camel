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
name|io
operator|.
name|IOException
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
name|StatusRuntimeException
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
name|NettyServerBuilder
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
name|ClientAuth
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
name|JwtServerInterceptor
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
DECL|class|GrpcProducerSecurityTest
specifier|public
class|class
name|GrpcProducerSecurityTest
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
name|GrpcProducerSecurityTest
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
DECL|field|GRPC_JWT_TEST_PORT
specifier|private
specifier|static
specifier|final
name|int
name|GRPC_JWT_TEST_PORT
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
DECL|field|grpcServerWithTLS
specifier|private
specifier|static
name|Server
name|grpcServerWithTLS
decl_stmt|;
DECL|field|grpcServerWithJWT
specifier|private
specifier|static
name|Server
name|grpcServerWithJWT
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
name|grpcServerWithTLS
operator|=
name|NettyServerBuilder
operator|.
name|forPort
argument_list|(
name|GRPC_TLS_TEST_PORT
argument_list|)
operator|.
name|sslContext
argument_list|(
name|GrpcSslContexts
operator|.
name|forServer
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/certs/server.pem"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/certs/server.key"
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
name|clientAuth
argument_list|(
name|ClientAuth
operator|.
name|REQUIRE
argument_list|)
operator|.
name|sslProvider
argument_list|(
name|SslProvider
operator|.
name|OPENSSL
argument_list|)
operator|.
name|build
argument_list|()
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
name|grpcServerWithJWT
operator|=
name|NettyServerBuilder
operator|.
name|forPort
argument_list|(
name|GRPC_JWT_TEST_PORT
argument_list|)
operator|.
name|addService
argument_list|(
operator|new
name|PingPongImpl
argument_list|()
argument_list|)
operator|.
name|intercept
argument_list|(
operator|new
name|JwtServerInterceptor
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
literal|"gRPC server with TLS started on port {}"
argument_list|,
name|GRPC_TLS_TEST_PORT
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"gRPC server with the JWT auth started on port {}"
argument_list|,
name|GRPC_JWT_TEST_PORT
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
name|grpcServerWithTLS
operator|!=
literal|null
condition|)
block|{
name|grpcServerWithTLS
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"gRPC server with TLS stoped"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|grpcServerWithJWT
operator|!=
literal|null
condition|)
block|{
name|grpcServerWithJWT
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"gRPC server with JWT stoped"
argument_list|)
expr_stmt|;
block|}
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
literal|"gRPC PingSyncSync method test start with TLS enable"
argument_list|)
expr_stmt|;
comment|// Testing simple sync method invoke using TLS negotiation
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
literal|"direct:grpc-tls"
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
literal|"gRPC PingSyncSync method test start with correct JWT authentication"
argument_list|)
expr_stmt|;
comment|// Testing simple sync method invoke using correct JWT authentication
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
literal|"direct:grpc-correct-jwt"
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
literal|"gRPC PingSyncSync method test start with incorrect JWT authentication"
argument_list|)
expr_stmt|;
comment|// Testing simple sync method invoke using incorrect JWT authentication
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
try|try
block|{
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:grpc-incorrect-jwt"
argument_list|,
name|pingRequest
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
operator|instanceof
name|StatusRuntimeException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"UNAUTHENTICATED: The Token's Signature resulted invalid when verified using the Algorithm: HmacSHA256"
argument_list|)
expr_stmt|;
block|}
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
literal|"direct:grpc-tls"
argument_list|)
operator|.
name|to
argument_list|(
literal|"grpc://localhost:"
operator|+
name|GRPC_TLS_TEST_PORT
operator|+
literal|"/org.apache.camel.component.grpc.PingPong?method=pingSyncSync&synchronous=true&"
operator|+
literal|"negotiationType=TLS&keyCertChainResource=file:src/test/resources/certs/client.pem&"
operator|+
literal|"keyResource=file:src/test/resources/certs/client.key&trustCertCollectionResource=file:src/test/resources/certs/ca.pem"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:grpc-correct-jwt"
argument_list|)
operator|.
name|to
argument_list|(
literal|"grpc://localhost:"
operator|+
name|GRPC_JWT_TEST_PORT
operator|+
literal|"/org.apache.camel.component.grpc.PingPong?method=pingSyncSync&synchronous=true&"
operator|+
literal|"authenticationType=JWT&jwtSecret="
operator|+
name|GRPC_JWT_CORRECT_SECRET
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:grpc-incorrect-jwt"
argument_list|)
operator|.
name|to
argument_list|(
literal|"grpc://localhost:"
operator|+
name|GRPC_JWT_TEST_PORT
operator|+
literal|"/org.apache.camel.component.grpc.PingPong?method=pingSyncSync&synchronous=true&"
operator|+
literal|"authenticationType=JWT&jwtSecret="
operator|+
name|GRPC_JWT_INCORRECT_SECRET
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

