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
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|io
operator|.
name|grpc
operator|.
name|Channel
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
name|CamelContext
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ReflectionHelper
import|;
end_import

begin_comment
comment|/**  * GrpcUtils helpers are working with dynamic methods via Camel and   * Java reflection utilities  */
end_comment

begin_class
DECL|class|GrpcUtils
specifier|public
specifier|final
class|class
name|GrpcUtils
block|{
DECL|method|GrpcUtils ()
specifier|private
name|GrpcUtils
parameter_list|()
block|{     }
DECL|method|constructGrpcAsyncStub (String packageName, String serviceName, Channel channel, final CamelContext context)
specifier|public
specifier|static
name|Object
name|constructGrpcAsyncStub
parameter_list|(
name|String
name|packageName
parameter_list|,
name|String
name|serviceName
parameter_list|,
name|Channel
name|channel
parameter_list|,
specifier|final
name|CamelContext
name|context
parameter_list|)
block|{
return|return
name|constructGrpcStubClass
argument_list|(
name|packageName
argument_list|,
name|serviceName
argument_list|,
name|GrpcConstants
operator|.
name|GRPC_SERVICE_ASYNC_STUB_METHOD
argument_list|,
name|channel
argument_list|,
name|context
argument_list|)
return|;
block|}
DECL|method|constructGrpcBlockingStub (String packageName, String serviceName, Channel channel, final CamelContext context)
specifier|public
specifier|static
name|Object
name|constructGrpcBlockingStub
parameter_list|(
name|String
name|packageName
parameter_list|,
name|String
name|serviceName
parameter_list|,
name|Channel
name|channel
parameter_list|,
specifier|final
name|CamelContext
name|context
parameter_list|)
block|{
return|return
name|constructGrpcStubClass
argument_list|(
name|packageName
argument_list|,
name|serviceName
argument_list|,
name|GrpcConstants
operator|.
name|GRPC_SERVICE_SYNC_STUB_METHOD
argument_list|,
name|channel
argument_list|,
name|context
argument_list|)
return|;
block|}
comment|/**      * Get gRPC stub class instance depends on the invocation style      * newBlockingStub - for sync style      * newStub - for async style      * newFutureStub - for ListenableFuture-style (not implemented yet)      */
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|}
argument_list|)
DECL|method|constructGrpcStubClass (String packageName, String serviceName, String stubMethod, Channel channel, final CamelContext context)
specifier|private
specifier|static
name|Object
name|constructGrpcStubClass
parameter_list|(
name|String
name|packageName
parameter_list|,
name|String
name|serviceName
parameter_list|,
name|String
name|stubMethod
parameter_list|,
name|Channel
name|channel
parameter_list|,
specifier|final
name|CamelContext
name|context
parameter_list|)
block|{
name|Class
index|[]
name|paramChannel
init|=
operator|new
name|Class
index|[
literal|1
index|]
decl_stmt|;
name|paramChannel
index|[
literal|0
index|]
operator|=
name|Channel
operator|.
name|class
expr_stmt|;
name|Object
name|grpcBlockingStub
init|=
literal|null
decl_stmt|;
name|String
name|serviceClassName
init|=
name|packageName
operator|+
literal|"."
operator|+
name|serviceName
operator|+
name|GrpcConstants
operator|.
name|GRPC_SERVICE_CLASS_POSTFIX
decl_stmt|;
try|try
block|{
name|Class
name|grpcServiceClass
init|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|serviceClassName
argument_list|)
decl_stmt|;
name|Method
name|grpcBlockingMethod
init|=
name|ReflectionHelper
operator|.
name|findMethod
argument_list|(
name|grpcServiceClass
argument_list|,
name|stubMethod
argument_list|,
name|paramChannel
argument_list|)
decl_stmt|;
if|if
condition|(
name|grpcBlockingMethod
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"gRPC service method not found: "
operator|+
name|serviceClassName
operator|+
literal|"."
operator|+
name|stubMethod
argument_list|)
throw|;
block|}
name|grpcBlockingStub
operator|=
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|grpcBlockingMethod
argument_list|,
name|grpcServiceClass
argument_list|,
name|channel
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"gRPC service class not found: "
operator|+
name|serviceClassName
argument_list|)
throw|;
block|}
return|return
name|grpcBlockingStub
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|constructGrpcImplBaseClass (String packageName, String serviceName, final CamelContext context)
specifier|public
specifier|static
name|Class
name|constructGrpcImplBaseClass
parameter_list|(
name|String
name|packageName
parameter_list|,
name|String
name|serviceName
parameter_list|,
specifier|final
name|CamelContext
name|context
parameter_list|)
block|{
name|Class
name|grpcServerImpl
decl_stmt|;
name|String
name|serverBaseImpl
init|=
name|packageName
operator|+
literal|"."
operator|+
name|serviceName
operator|+
name|GrpcConstants
operator|.
name|GRPC_SERVICE_CLASS_POSTFIX
operator|+
literal|"$"
operator|+
name|serviceName
operator|+
name|GrpcConstants
operator|.
name|GRPC_SERVER_IMPL_POSTFIX
decl_stmt|;
try|try
block|{
name|grpcServerImpl
operator|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|serverBaseImpl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"gRPC server base class not found: "
operator|+
name|serverBaseImpl
argument_list|)
throw|;
block|}
return|return
name|grpcServerImpl
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|invokeAsyncMethod (Object asyncStubClass, String invokeMethod, Object request, StreamObserver responseObserver)
specifier|public
specifier|static
name|void
name|invokeAsyncMethod
parameter_list|(
name|Object
name|asyncStubClass
parameter_list|,
name|String
name|invokeMethod
parameter_list|,
name|Object
name|request
parameter_list|,
name|StreamObserver
name|responseObserver
parameter_list|)
block|{
name|Class
index|[]
name|paramMethod
init|=
literal|null
decl_stmt|;
name|Method
name|method
init|=
name|ReflectionHelper
operator|.
name|findMethod
argument_list|(
name|asyncStubClass
operator|.
name|getClass
argument_list|()
argument_list|,
name|invokeMethod
argument_list|,
name|paramMethod
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"gRPC service method not found: "
operator|+
name|asyncStubClass
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|invokeMethod
argument_list|)
throw|;
block|}
if|if
condition|(
name|method
operator|.
name|getReturnType
argument_list|()
operator|.
name|equals
argument_list|(
name|StreamObserver
operator|.
name|class
argument_list|)
condition|)
block|{
name|StreamObserver
argument_list|<
name|Object
argument_list|>
name|requestObserver
init|=
operator|(
name|StreamObserver
argument_list|<
name|Object
argument_list|>
operator|)
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|method
argument_list|,
name|asyncStubClass
argument_list|,
name|responseObserver
argument_list|)
decl_stmt|;
if|if
condition|(
name|request
operator|instanceof
name|List
condition|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|requestList
init|=
operator|(
name|List
argument_list|<
name|Object
argument_list|>
operator|)
name|request
decl_stmt|;
name|requestList
operator|.
name|forEach
argument_list|(
parameter_list|(
name|requestItem
parameter_list|)
lambda|->
block|{
name|requestObserver
operator|.
name|onNext
argument_list|(
name|requestItem
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|requestObserver
operator|.
name|onNext
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
name|requestObserver
operator|.
name|onCompleted
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|method
argument_list|,
name|asyncStubClass
argument_list|,
name|request
argument_list|,
name|responseObserver
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|invokeAsyncMethodStreaming (Object asyncStubClass, String invokeMethod, StreamObserver<?> responseObserver)
specifier|public
specifier|static
name|StreamObserver
argument_list|<
name|Object
argument_list|>
name|invokeAsyncMethodStreaming
parameter_list|(
name|Object
name|asyncStubClass
parameter_list|,
name|String
name|invokeMethod
parameter_list|,
name|StreamObserver
argument_list|<
name|?
argument_list|>
name|responseObserver
parameter_list|)
block|{
name|Class
index|[]
name|paramMethod
init|=
literal|null
decl_stmt|;
name|Method
name|method
init|=
name|ReflectionHelper
operator|.
name|findMethod
argument_list|(
name|asyncStubClass
operator|.
name|getClass
argument_list|()
argument_list|,
name|invokeMethod
argument_list|,
name|paramMethod
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"gRPC service method not found: "
operator|+
name|asyncStubClass
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|invokeMethod
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|StreamObserver
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|method
operator|.
name|getReturnType
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"gRPC service method does not declare an input of type stream (cannot be used in streaming mode): "
operator|+
name|asyncStubClass
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|invokeMethod
argument_list|)
throw|;
block|}
return|return
operator|(
name|StreamObserver
argument_list|<
name|Object
argument_list|>
operator|)
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|method
argument_list|,
name|asyncStubClass
argument_list|,
name|responseObserver
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|invokeSyncMethod (Object blockingStubClass, String invokeMethod, Object request)
specifier|public
specifier|static
name|Object
name|invokeSyncMethod
parameter_list|(
name|Object
name|blockingStubClass
parameter_list|,
name|String
name|invokeMethod
parameter_list|,
name|Object
name|request
parameter_list|)
block|{
name|Class
index|[]
name|paramMethod
init|=
literal|null
decl_stmt|;
name|Method
name|method
init|=
name|ReflectionHelper
operator|.
name|findMethod
argument_list|(
name|blockingStubClass
operator|.
name|getClass
argument_list|()
argument_list|,
name|invokeMethod
argument_list|,
name|paramMethod
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"gRPC service method not found: "
operator|+
name|blockingStubClass
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|invokeMethod
argument_list|)
throw|;
block|}
if|if
condition|(
name|method
operator|.
name|getReturnType
argument_list|()
operator|.
name|equals
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
condition|)
block|{
name|Iterator
argument_list|<
name|Object
argument_list|>
name|responseObjects
init|=
operator|(
name|Iterator
argument_list|<
name|Object
argument_list|>
operator|)
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|method
argument_list|,
name|blockingStubClass
argument_list|,
name|request
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|objectList
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|responseObjects
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|objectList
operator|.
name|add
argument_list|(
name|responseObjects
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|objectList
return|;
block|}
else|else
block|{
return|return
name|ObjectHelper
operator|.
name|invokeMethod
argument_list|(
name|method
argument_list|,
name|blockingStubClass
argument_list|,
name|request
argument_list|)
return|;
block|}
block|}
comment|/**      * Migrated MixedLower function from the gRPC converting plugin source code      * (https://github.com/grpc/grpc-java/blob/master/compiler/src/java_plugin/cpp/java_generator.cpp)      *      * - decapitalize the first letter      * - remove embedded underscores& capitalize the following letter      */
DECL|method|convertMethod2CamelCase (final String method)
specifier|public
specifier|static
name|String
name|convertMethod2CamelCase
parameter_list|(
specifier|final
name|String
name|method
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|method
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|method
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|Boolean
name|afterUnderscore
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|method
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|method
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|==
literal|'_'
condition|)
block|{
name|afterUnderscore
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|afterUnderscore
condition|?
name|Character
operator|.
name|toUpperCase
argument_list|(
name|method
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
else|:
name|method
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|afterUnderscore
operator|=
literal|false
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

