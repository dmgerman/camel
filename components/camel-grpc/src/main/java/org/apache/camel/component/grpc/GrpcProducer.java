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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncCallback
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
name|AsyncProcessor
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
name|Message
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
name|client
operator|.
name|GrpcResponseAggregationStreamObserver
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
name|DefaultProducer
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
name|util
operator|.
name|ObjectHelper
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

begin_comment
comment|/**  * Represents asynchronous and synchronous gRPC producer implementations.  */
end_comment

begin_class
DECL|class|GrpcProducer
specifier|public
class|class
name|GrpcProducer
extends|extends
name|DefaultProducer
implements|implements
name|AsyncProcessor
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
name|GrpcProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|protected
specifier|final
name|GrpcConfiguration
name|configuration
decl_stmt|;
DECL|field|endpoint
specifier|protected
specifier|final
name|GrpcEndpoint
name|endpoint
decl_stmt|;
DECL|field|channel
specifier|private
name|ManagedChannel
name|channel
decl_stmt|;
DECL|field|grpcStub
specifier|private
name|Object
name|grpcStub
decl_stmt|;
DECL|method|GrpcProducer (GrpcEndpoint endpoint, GrpcConfiguration configuration)
specifier|public
name|GrpcProducer
parameter_list|(
name|GrpcEndpoint
name|endpoint
parameter_list|,
name|GrpcConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
try|try
block|{
name|GrpcUtils
operator|.
name|invokeAsyncMethod
argument_list|(
name|grpcStub
argument_list|,
name|configuration
operator|.
name|getMethod
argument_list|()
argument_list|,
name|message
operator|.
name|getBody
argument_list|()
argument_list|,
operator|new
name|GrpcResponseAggregationStreamObserver
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
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
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Object
name|outBody
init|=
name|GrpcUtils
operator|.
name|invokeSyncMethod
argument_list|(
name|grpcStub
argument_list|,
name|configuration
operator|.
name|getMethod
argument_list|()
argument_list|,
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|outBody
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|channel
operator|==
literal|null
condition|)
block|{
name|initializeChannel
argument_list|()
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isSynchronous
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Getting synchronous method stub from channel"
argument_list|)
expr_stmt|;
name|grpcStub
operator|=
name|GrpcUtils
operator|.
name|constructGrpcBlockingStub
argument_list|(
name|configuration
operator|.
name|getServicePackage
argument_list|()
argument_list|,
name|configuration
operator|.
name|getServiceName
argument_list|()
argument_list|,
name|channel
argument_list|,
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Getting asynchronous method stub from channel"
argument_list|)
expr_stmt|;
name|grpcStub
operator|=
name|GrpcUtils
operator|.
name|constructGrpcAsyncStub
argument_list|(
name|configuration
operator|.
name|getServicePackage
argument_list|()
argument_list|,
name|configuration
operator|.
name|getServiceName
argument_list|()
argument_list|,
name|channel
argument_list|,
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|channel
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Terminating channel to the remote gRPC server"
argument_list|)
expr_stmt|;
name|channel
operator|.
name|shutdown
argument_list|()
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
name|channel
operator|=
literal|null
expr_stmt|;
name|grpcStub
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|initializeChannel ()
specifier|protected
name|void
name|initializeChannel
parameter_list|()
block|{
name|NettyChannelBuilder
name|channelBuilder
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
operator|&&
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating channel to the remote gRPC server "
operator|+
name|configuration
operator|.
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|channelBuilder
operator|=
name|NettyChannelBuilder
operator|.
name|forAddress
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getTarget
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating channel to the remote gRPC server "
operator|+
name|configuration
operator|.
name|getTarget
argument_list|()
argument_list|)
expr_stmt|;
name|channelBuilder
operator|=
name|NettyChannelBuilder
operator|.
name|forTarget
argument_list|(
name|configuration
operator|.
name|getTarget
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No connection properties (host, port or target) specified"
argument_list|)
throw|;
block|}
name|channel
operator|=
name|channelBuilder
operator|.
name|usePlaintext
argument_list|(
name|configuration
operator|.
name|getUsePlainText
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

