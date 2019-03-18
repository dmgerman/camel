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

begin_comment
comment|/**  * A forwarder is responsible to forward exchanges to a remote gRPC server.  */
end_comment

begin_interface
DECL|interface|GrpcExchangeForwarder
specifier|public
interface|interface
name|GrpcExchangeForwarder
block|{
DECL|method|forward (Exchange exchange, StreamObserver<Object> responseObserver, AsyncCallback callback)
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
function_decl|;
DECL|method|forward (Exchange exchange)
name|void
name|forward
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
DECL|method|shutdown ()
name|void
name|shutdown
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

