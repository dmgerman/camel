begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Endpoint
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
name|impl
operator|.
name|ProducerCache
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
name|CamelContextHelper
import|;
end_import

begin_comment
comment|/**  * A stream observer that routes all responses to another endpoint.  */
end_comment

begin_class
DECL|class|GrpcResponseRouterStreamObserver
specifier|public
class|class
name|GrpcResponseRouterStreamObserver
implements|implements
name|StreamObserver
argument_list|<
name|Object
argument_list|>
block|{
DECL|field|sourceEndpoint
specifier|private
specifier|final
name|Endpoint
name|sourceEndpoint
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|GrpcConfiguration
name|configuration
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|producerCache
specifier|private
specifier|final
name|ProducerCache
name|producerCache
decl_stmt|;
DECL|method|GrpcResponseRouterStreamObserver (GrpcConfiguration configuration, Endpoint sourceEndpoint)
specifier|public
name|GrpcResponseRouterStreamObserver
parameter_list|(
name|GrpcConfiguration
name|configuration
parameter_list|,
name|Endpoint
name|sourceEndpoint
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
name|sourceEndpoint
operator|=
name|sourceEndpoint
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|CamelContextHelper
operator|.
name|getMandatoryEndpoint
argument_list|(
name|sourceEndpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|configuration
operator|.
name|getStreamRepliesTo
argument_list|()
argument_list|)
expr_stmt|;
name|sourceEndpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|createProducerTemplate
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|this
operator|.
name|producerCache
operator|=
operator|new
name|ProducerCache
argument_list|(
name|this
argument_list|,
name|sourceEndpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onNext (Object o)
specifier|public
name|void
name|onNext
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|sourceEndpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|doSend
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onError (Throwable throwable)
specifier|public
name|void
name|onError
parameter_list|(
name|Throwable
name|throwable
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
name|Exchange
name|exchange
init|=
name|sourceEndpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
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
name|throwable
argument_list|)
expr_stmt|;
name|doSend
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onCompleted ()
specifier|public
name|void
name|onCompleted
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|.
name|isForwardOnCompleted
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|sourceEndpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
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
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doSend (Exchange exchange)
specifier|private
name|void
name|doSend
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|producerCache
operator|.
name|doInAsyncProducer
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|doneSync
lambda|->
block|{ }
argument_list|,
name|AsyncProcessor
operator|::
name|process
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

