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
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|io
operator|.
name|grpc
operator|.
name|BindableService
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
name|ServerInterceptor
import|;
end_import

begin_import
import|import
name|io
operator|.
name|grpc
operator|.
name|ServerInterceptors
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
name|javassist
operator|.
name|util
operator|.
name|proxy
operator|.
name|MethodHandler
import|;
end_import

begin_import
import|import
name|javassist
operator|.
name|util
operator|.
name|proxy
operator|.
name|ProxyFactory
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
name|component
operator|.
name|grpc
operator|.
name|server
operator|.
name|GrpcHeaderInterceptor
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
name|server
operator|.
name|GrpcMethodHandler
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
name|DefaultConsumer
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
comment|/**  * Represents gRPC server consumer implementation  */
end_comment

begin_class
DECL|class|GrpcConsumer
specifier|public
class|class
name|GrpcConsumer
extends|extends
name|DefaultConsumer
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
name|GrpcConsumer
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
DECL|field|server
specifier|private
name|Server
name|server
decl_stmt|;
DECL|method|GrpcConsumer (GrpcEndpoint endpoint, Processor processor, GrpcConfiguration configuration)
specifier|public
name|GrpcConsumer
parameter_list|(
name|GrpcEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|GrpcConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
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
DECL|method|getConfiguration ()
specifier|public
name|GrpcConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
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
name|server
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting the gRPC server"
argument_list|)
expr_stmt|;
name|initializeServer
argument_list|()
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"gRPC server started and listening on port: {}"
argument_list|,
name|server
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
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
name|server
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Terminating gRPC server"
argument_list|)
expr_stmt|;
name|server
operator|.
name|shutdown
argument_list|()
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
name|server
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
DECL|method|initializeServer ()
specifier|protected
name|void
name|initializeServer
parameter_list|()
block|{
name|NettyServerBuilder
name|serverBuilder
init|=
literal|null
decl_stmt|;
name|BindableService
name|bindableService
init|=
literal|null
decl_stmt|;
name|ProxyFactory
name|serviceProxy
init|=
operator|new
name|ProxyFactory
argument_list|()
decl_stmt|;
name|ServerInterceptor
name|headerInterceptor
init|=
operator|new
name|GrpcHeaderInterceptor
argument_list|()
decl_stmt|;
name|MethodHandler
name|methodHandler
init|=
operator|new
name|GrpcMethodHandler
argument_list|(
name|endpoint
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|serviceProxy
operator|.
name|setSuperclass
argument_list|(
name|GrpcUtils
operator|.
name|constructGrpcImplBaseClass
argument_list|(
name|endpoint
operator|.
name|getServicePackage
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getServiceName
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|bindableService
operator|=
operator|(
name|BindableService
operator|)
name|serviceProxy
operator|.
name|create
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[
literal|0
index|]
operator|,
operator|new
name|Object
index|[
literal|0
index|]
operator|,
name|methodHandler
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
decl||
name|IllegalArgumentException
decl||
name|InstantiationException
decl||
name|IllegalAccessException
decl||
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to create bindable proxy service for "
operator|+
name|configuration
operator|.
name|getService
argument_list|()
argument_list|)
throw|;
block|}
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
name|debug
argument_list|(
literal|"Building gRPC server on {}:{}"
argument_list|,
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
name|serverBuilder
operator|=
name|NettyServerBuilder
operator|.
name|forAddress
argument_list|(
operator|new
name|InetSocketAddress
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
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No server start properties (host, port) specified"
argument_list|)
throw|;
block|}
name|server
operator|=
name|serverBuilder
operator|.
name|addService
argument_list|(
name|ServerInterceptors
operator|.
name|intercept
argument_list|(
name|bindableService
argument_list|,
name|headerInterceptor
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
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
return|return
name|doSend
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
DECL|method|onCompleted (Exchange exchange)
specifier|public
name|void
name|onCompleted
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|.
name|isForwardOnCompleted
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
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
name|doSend
argument_list|(
name|exchange
argument_list|,
name|done
lambda|->
block|{             }
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onError (Exchange exchange, Throwable error)
specifier|public
name|void
name|onError
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|error
parameter_list|)
block|{
if|if
condition|(
name|configuration
operator|.
name|isForwardOnError
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|GrpcConstants
operator|.
name|GRPC_EVENT_TYPE_HEADER
argument_list|,
name|GrpcConstants
operator|.
name|GRPC_EVENT_TYPE_ON_ERROR
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|error
argument_list|)
expr_stmt|;
name|doSend
argument_list|(
name|exchange
argument_list|,
name|done
lambda|->
block|{             }
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doSend (Exchange exchange, AsyncCallback callback)
specifier|private
name|boolean
name|doSend
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|isRunAllowed
argument_list|()
condition|)
block|{
name|this
operator|.
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|doneSync
lambda|->
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Consumer not ready to process exchanges. The exchange {} will be discarded"
argument_list|,
name|exchange
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
block|}
block|}
end_class

end_unit

