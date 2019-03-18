begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.grpc.client
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
name|client
package|;
end_package

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
name|GrpcConfiguration
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
name|GrpcUtils
import|;
end_import

begin_comment
comment|/**  * An exchange forwarder that creates a RPC request for each camel Exchange.  */
end_comment

begin_class
DECL|class|GrpcSimpleExchangeForwarder
class|class
name|GrpcSimpleExchangeForwarder
implements|implements
name|GrpcExchangeForwarder
block|{
DECL|field|configuration
specifier|private
specifier|final
name|GrpcConfiguration
name|configuration
decl_stmt|;
DECL|field|grpcStub
specifier|private
specifier|final
name|Object
name|grpcStub
decl_stmt|;
DECL|method|GrpcSimpleExchangeForwarder (GrpcConfiguration configuration, Object grpcStub)
specifier|public
name|GrpcSimpleExchangeForwarder
parameter_list|(
name|GrpcConfiguration
name|configuration
parameter_list|,
name|Object
name|grpcStub
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|grpcStub
operator|=
name|grpcStub
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|forward (Exchange exchange, StreamObserver<Object> responseObserver, AsyncCallback callback)
specifier|public
name|boolean
name|forward
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|StreamObserver
argument_list|<
name|Object
argument_list|>
name|responseObserver
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
name|responseObserver
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
DECL|method|forward (Exchange exchange)
specifier|public
name|void
name|forward
parameter_list|(
name|Exchange
name|exchange
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
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
block|{     }
block|}
end_class

end_unit

