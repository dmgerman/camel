begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo4
package|;
end_package

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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
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
name|RuntimeCamelException
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
name|olingo4
operator|.
name|api
operator|.
name|Olingo4ResponseHandler
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
name|olingo4
operator|.
name|internal
operator|.
name|Olingo4ApiName
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
name|component
operator|.
name|AbstractApiConsumer
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
name|component
operator|.
name|ApiConsumerHelper
import|;
end_import

begin_comment
comment|/**  * The Olingo4 consumer.  */
end_comment

begin_class
DECL|class|Olingo4Consumer
specifier|public
class|class
name|Olingo4Consumer
extends|extends
name|AbstractApiConsumer
argument_list|<
name|Olingo4ApiName
argument_list|,
name|Olingo4Configuration
argument_list|>
block|{
DECL|method|Olingo4Consumer (Olingo4Endpoint endpoint, Processor processor)
specifier|public
name|Olingo4Consumer
parameter_list|(
name|Olingo4Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
comment|// invoke the consumer method
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|args
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|args
operator|.
name|putAll
argument_list|(
name|endpoint
operator|.
name|getEndpointProperties
argument_list|()
argument_list|)
expr_stmt|;
comment|// let the endpoint and the Consumer intercept properties
name|endpoint
operator|.
name|interceptProperties
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|interceptProperties
argument_list|(
name|args
argument_list|)
expr_stmt|;
try|try
block|{
comment|// create responseHandler
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|Object
index|[]
name|result
init|=
operator|new
name|Object
index|[
literal|1
index|]
decl_stmt|;
specifier|final
name|Exception
index|[]
name|error
init|=
operator|new
name|Exception
index|[
literal|1
index|]
decl_stmt|;
name|args
operator|.
name|put
argument_list|(
name|Olingo4Endpoint
operator|.
name|RESPONSE_HANDLER_PROPERTY
argument_list|,
operator|new
name|Olingo4ResponseHandler
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|Object
name|response
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|responseHeaders
parameter_list|)
block|{
name|result
index|[
literal|0
index|]
operator|=
name|response
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onException
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|error
index|[
literal|0
index|]
operator|=
name|ex
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onCanceled
parameter_list|()
block|{
name|error
index|[
literal|0
index|]
operator|=
operator|new
name|RuntimeCamelException
argument_list|(
literal|"OData HTTP Request cancelled"
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|doInvokeMethod
argument_list|(
name|args
argument_list|)
expr_stmt|;
comment|// guaranteed to return, since an exception on timeout is
comment|// expected!!!
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
if|if
condition|(
name|error
index|[
literal|0
index|]
operator|!=
literal|null
condition|)
block|{
throw|throw
name|error
index|[
literal|0
index|]
throw|;
block|}
return|return
name|ApiConsumerHelper
operator|.
name|getResultsProcessed
argument_list|(
name|this
argument_list|,
name|result
index|[
literal|0
index|]
argument_list|,
name|isSplitResult
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|t
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

