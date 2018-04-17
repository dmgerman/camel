begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.examples.grpc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|examples
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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

begin_comment
comment|/**  * Server that manages startup/shutdown of a server.  */
end_comment

begin_class
DECL|class|HelloCamelServer
specifier|public
class|class
name|HelloCamelServer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|HelloCamelServer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|server
specifier|private
name|Server
name|server
decl_stmt|;
DECL|method|start ()
specifier|private
name|void
name|start
parameter_list|()
throws|throws
name|IOException
block|{
comment|/* The port on which the server should run */
name|int
name|port
init|=
literal|8080
decl_stmt|;
name|server
operator|=
name|ServerBuilder
operator|.
name|forPort
argument_list|(
name|port
argument_list|)
operator|.
name|addService
argument_list|(
operator|new
name|HelloCamelImpl
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
literal|"Server started. I'm listening on "
operator|+
name|port
argument_list|)
expr_stmt|;
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|addShutdownHook
argument_list|(
operator|new
name|Thread
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|HelloCamelServer
operator|.
name|this
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|stop ()
specifier|private
name|void
name|stop
parameter_list|()
block|{
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|blockUntilShutdown ()
specifier|private
name|void
name|blockUntilShutdown
parameter_list|()
throws|throws
name|InterruptedException
block|{
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|awaitTermination
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Main needed to launch server from command line      */
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|IOException
throws|,
name|InterruptedException
block|{
specifier|final
name|HelloCamelServer
name|server
init|=
operator|new
name|HelloCamelServer
argument_list|()
decl_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|server
operator|.
name|blockUntilShutdown
argument_list|()
expr_stmt|;
block|}
DECL|class|HelloCamelImpl
specifier|static
class|class
name|HelloCamelImpl
extends|extends
name|CamelHelloGrpc
operator|.
name|CamelHelloImplBase
block|{
annotation|@
name|Override
DECL|method|sayHelloToCamel (CamelHelloRequest req, StreamObserver<CamelHelloReply> responseObserver)
specifier|public
name|void
name|sayHelloToCamel
parameter_list|(
name|CamelHelloRequest
name|req
parameter_list|,
name|StreamObserver
argument_list|<
name|CamelHelloReply
argument_list|>
name|responseObserver
parameter_list|)
block|{
name|CamelHelloReply
name|reply
init|=
name|CamelHelloReply
operator|.
name|newBuilder
argument_list|()
operator|.
name|setMessage
argument_list|(
literal|"Hello "
operator|+
name|req
operator|.
name|getName
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
name|reply
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

