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

begin_comment
comment|/**  * gRPC component constants  */
end_comment

begin_interface
DECL|interface|GrpcConstants
specifier|public
interface|interface
name|GrpcConstants
block|{
DECL|field|GRPC_SERVICE_CLASS_POSTFIX
name|String
name|GRPC_SERVICE_CLASS_POSTFIX
init|=
literal|"Grpc"
decl_stmt|;
DECL|field|GRPC_SERVER_IMPL_POSTFIX
name|String
name|GRPC_SERVER_IMPL_POSTFIX
init|=
literal|"ImplBase"
decl_stmt|;
DECL|field|GRPC_SERVICE_SYNC_STUB_METHOD
name|String
name|GRPC_SERVICE_SYNC_STUB_METHOD
init|=
literal|"newBlockingStub"
decl_stmt|;
DECL|field|GRPC_SERVICE_ASYNC_STUB_METHOD
name|String
name|GRPC_SERVICE_ASYNC_STUB_METHOD
init|=
literal|"newStub"
decl_stmt|;
DECL|field|GRPC_SERVICE_FUTURE_STUB_METHOD
name|String
name|GRPC_SERVICE_FUTURE_STUB_METHOD
init|=
literal|"newFutureStub"
decl_stmt|;
comment|/*      * This headers will be set after gRPC consumer method is invoked      */
DECL|field|GRPC_METHOD_NAME_HEADER
name|String
name|GRPC_METHOD_NAME_HEADER
init|=
literal|"CamelGrpcMethodName"
decl_stmt|;
DECL|field|GRPC_USER_AGENT_HEADER
name|String
name|GRPC_USER_AGENT_HEADER
init|=
literal|"CamelGrpcUserAgent"
decl_stmt|;
DECL|field|GRPC_EVENT_TYPE_HEADER
name|String
name|GRPC_EVENT_TYPE_HEADER
init|=
literal|"CamelGrpcEventType"
decl_stmt|;
DECL|field|GRPC_EVENT_TYPE_ON_NEXT
name|String
name|GRPC_EVENT_TYPE_ON_NEXT
init|=
literal|"onNext"
decl_stmt|;
DECL|field|GRPC_EVENT_TYPE_ON_ERROR
name|String
name|GRPC_EVENT_TYPE_ON_ERROR
init|=
literal|"onError"
decl_stmt|;
DECL|field|GRPC_EVENT_TYPE_ON_COMPLETED
name|String
name|GRPC_EVENT_TYPE_ON_COMPLETED
init|=
literal|"onCompleted"
decl_stmt|;
block|}
end_interface

end_unit

