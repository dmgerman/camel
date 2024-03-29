begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.grpc.server
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
operator|.
name|server
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
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Map
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
name|component
operator|.
name|grpc
operator|.
name|GrpcConstants
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
name|GrpcConsumer
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
name|GrpcConsumerStrategy
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
name|GrpcEndpoint
import|;
end_import

begin_comment
comment|/**  * gRPC server method invocation handler  */
end_comment

begin_class
DECL|class|GrpcMethodHandler
specifier|public
class|class
name|GrpcMethodHandler
implements|implements
name|MethodHandler
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|GrpcEndpoint
name|endpoint
decl_stmt|;
DECL|field|consumer
specifier|private
specifier|final
name|GrpcConsumer
name|consumer
decl_stmt|;
DECL|method|GrpcMethodHandler (GrpcEndpoint endpoint, GrpcConsumer consumer)
specifier|public
name|GrpcMethodHandler
parameter_list|(
name|GrpcEndpoint
name|endpoint
parameter_list|,
name|GrpcConsumer
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|invoke (Object self, Method thisMethod, Method proceed, Object[] args)
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|self
parameter_list|,
name|Method
name|thisMethod
parameter_list|,
name|Method
name|proceed
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|grcpHeaders
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|grcpHeaders
operator|.
name|put
argument_list|(
name|GrpcHeaderInterceptor
operator|.
name|USER_AGENT_CONTEXT_KEY
operator|.
name|toString
argument_list|()
argument_list|,
name|GrpcHeaderInterceptor
operator|.
name|USER_AGENT_CONTEXT_KEY
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|grcpHeaders
operator|.
name|put
argument_list|(
name|GrpcHeaderInterceptor
operator|.
name|CONTENT_TYPE_CONTEXT_KEY
operator|.
name|toString
argument_list|()
argument_list|,
name|GrpcHeaderInterceptor
operator|.
name|CONTENT_TYPE_CONTEXT_KEY
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|grcpHeaders
operator|.
name|put
argument_list|(
name|GrpcConstants
operator|.
name|GRPC_METHOD_NAME_HEADER
argument_list|,
name|thisMethod
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Determines that the incoming parameters are transmitted in synchronous mode
comment|// Two incoming parameters and second is instance of the io.grpc.stub.StreamObserver
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|2
operator|&&
name|args
index|[
literal|1
index|]
operator|instanceof
name|StreamObserver
condition|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|grcpHeaders
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isSynchronous
argument_list|()
condition|)
block|{
name|consumer
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|consumer
operator|.
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
name|StreamObserver
argument_list|<
name|Object
argument_list|>
name|responseObserver
init|=
operator|(
name|StreamObserver
argument_list|<
name|Object
argument_list|>
operator|)
name|args
index|[
literal|1
index|]
decl_stmt|;
name|Object
name|responseBody
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|responseBody
operator|instanceof
name|List
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|responseList
init|=
operator|(
name|List
argument_list|<
name|Object
argument_list|>
operator|)
name|responseBody
decl_stmt|;
name|responseList
operator|.
name|forEach
argument_list|(
name|responseObserver
operator|::
name|onNext
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|responseObserver
operator|.
name|onNext
argument_list|(
name|responseBody
argument_list|)
expr_stmt|;
block|}
name|responseObserver
operator|.
name|onCompleted
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|1
operator|&&
name|args
index|[
literal|0
index|]
operator|instanceof
name|StreamObserver
condition|)
block|{
comment|// Single incoming parameter is instance of the io.grpc.stub.StreamObserver
specifier|final
name|StreamObserver
argument_list|<
name|Object
argument_list|>
name|responseObserver
init|=
operator|(
name|StreamObserver
argument_list|<
name|Object
argument_list|>
operator|)
name|args
index|[
literal|0
index|]
decl_stmt|;
name|StreamObserver
argument_list|<
name|Object
argument_list|>
name|requestObserver
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getConsumerStrategy
argument_list|()
operator|==
name|GrpcConsumerStrategy
operator|.
name|AGGREGATION
condition|)
block|{
name|requestObserver
operator|=
operator|new
name|GrpcRequestAggregationStreamObserver
argument_list|(
name|endpoint
argument_list|,
name|consumer
argument_list|,
name|responseObserver
argument_list|,
name|grcpHeaders
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getConsumerStrategy
argument_list|()
operator|==
name|GrpcConsumerStrategy
operator|.
name|PROPAGATION
condition|)
block|{
name|requestObserver
operator|=
operator|new
name|GrpcRequestPropagationStreamObserver
argument_list|(
name|endpoint
argument_list|,
name|consumer
argument_list|,
name|responseObserver
argument_list|,
name|grcpHeaders
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"gRPC processing strategy not implemented "
operator|+
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getConsumerStrategy
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|requestObserver
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid to process gRPC method: "
operator|+
name|thisMethod
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

