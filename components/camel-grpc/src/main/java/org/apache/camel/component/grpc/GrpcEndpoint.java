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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Producer
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|support
operator|.
name|DefaultEndpoint
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
name|support
operator|.
name|SynchronousDelegateProducer
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

begin_comment
comment|/**  * The gRPC component allows to call and expose remote procedures via HTTP/2 with protobuf dataformat  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.19.0"
argument_list|,
name|scheme
operator|=
literal|"grpc"
argument_list|,
name|title
operator|=
literal|"gRPC"
argument_list|,
name|syntax
operator|=
literal|"grpc:host:port/service"
argument_list|,
name|label
operator|=
literal|"rpc"
argument_list|)
DECL|class|GrpcEndpoint
specifier|public
class|class
name|GrpcEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|protected
specifier|final
name|GrpcConfiguration
name|configuration
decl_stmt|;
DECL|field|serviceName
specifier|private
name|String
name|serviceName
decl_stmt|;
DECL|field|servicePackage
specifier|private
name|String
name|servicePackage
decl_stmt|;
DECL|method|GrpcEndpoint (String uri, GrpcComponent component, GrpcConfiguration config)
specifier|public
name|GrpcEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|GrpcComponent
name|component
parameter_list|,
name|GrpcConfiguration
name|config
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|config
expr_stmt|;
comment|// Extract service and package names from the full service name
name|serviceName
operator|=
name|GrpcUtils
operator|.
name|extractServiceName
argument_list|(
name|configuration
operator|.
name|getService
argument_list|()
argument_list|)
expr_stmt|;
name|servicePackage
operator|=
name|GrpcUtils
operator|.
name|extractServicePackage
argument_list|(
name|configuration
operator|.
name|getService
argument_list|()
argument_list|)
expr_stmt|;
comment|// Convert method name to the camel case style
comment|// This requires if method name as described inside .proto file directly
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getMethod
argument_list|()
argument_list|)
condition|)
block|{
name|configuration
operator|.
name|setMethod
argument_list|(
name|GrpcUtils
operator|.
name|convertMethod2CamelCase
argument_list|(
name|configuration
operator|.
name|getMethod
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|GrpcProducer
name|producer
init|=
operator|new
name|GrpcProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
if|if
condition|(
name|isSynchronous
argument_list|()
condition|)
block|{
return|return
operator|new
name|SynchronousDelegateProducer
argument_list|(
name|producer
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|producer
return|;
block|}
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|GrpcConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|configuration
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getServiceName ()
specifier|public
name|String
name|getServiceName
parameter_list|()
block|{
return|return
name|serviceName
return|;
block|}
DECL|method|getServicePackage ()
specifier|public
name|String
name|getServicePackage
parameter_list|()
block|{
return|return
name|servicePackage
return|;
block|}
block|}
end_class

end_unit

