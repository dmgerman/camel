begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo2
package|;
end_package

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
name|olingo2
operator|.
name|api
operator|.
name|Olingo2ResponseHandler
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
name|olingo2
operator|.
name|internal
operator|.
name|Olingo2ApiName
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|ep
operator|.
name|entry
operator|.
name|ODataEntry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|ep
operator|.
name|feed
operator|.
name|ODataFeed
import|;
end_import

begin_comment
comment|/**  * The Olingo2 consumer.  */
end_comment

begin_class
DECL|class|Olingo2Consumer
specifier|public
class|class
name|Olingo2Consumer
extends|extends
name|AbstractApiConsumer
argument_list|<
name|Olingo2ApiName
argument_list|,
name|Olingo2Configuration
argument_list|>
block|{
DECL|field|resultIndex
specifier|private
name|Olingo2Index
name|resultIndex
decl_stmt|;
DECL|method|Olingo2Consumer (Olingo2Endpoint endpoint, Processor processor)
specifier|public
name|Olingo2Consumer
parameter_list|(
name|Olingo2Endpoint
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
name|Olingo2Endpoint
operator|.
name|RESPONSE_HANDLER_PROPERTY
argument_list|,
operator|new
name|Olingo2ResponseHandler
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
if|if
condition|(
name|resultIndex
operator|!=
literal|null
condition|)
block|{
name|response
operator|=
name|resultIndex
operator|.
name|filterResponse
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
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
comment|//
comment|// Allow consumer idle properties to properly handle an empty
comment|// polling response
comment|//
if|if
condition|(
operator|(
name|result
index|[
literal|0
index|]
operator|==
literal|null
operator|)
operator|||
operator|(
name|result
index|[
literal|0
index|]
operator|instanceof
name|ODataFeed
operator|&&
operator|(
operator|(
operator|(
name|ODataFeed
operator|)
name|result
index|[
literal|0
index|]
operator|)
operator|.
name|getEntries
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|)
operator|)
condition|)
block|{
return|return
literal|0
return|;
block|}
else|else
block|{
name|int
name|processed
init|=
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
decl_stmt|;
return|return
name|processed
return|;
block|}
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
annotation|@
name|Override
DECL|method|interceptProperties (Map<String, Object> properties)
specifier|public
name|void
name|interceptProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
comment|//
comment|// If we have a filterAlreadySeen property then initialise the filter
comment|// index
comment|//
name|Object
name|value
init|=
name|properties
operator|.
name|get
argument_list|(
name|Olingo2Endpoint
operator|.
name|FILTER_ALREADY_SEEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|//
comment|// Initialise the index if not already and if filterAlreadySeen has been
comment|// set
comment|//
if|if
condition|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
operator|&&
name|resultIndex
operator|==
literal|null
condition|)
block|{
name|resultIndex
operator|=
operator|new
name|Olingo2Index
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|interceptResult (Object result, Exchange resultExchange)
specifier|public
name|void
name|interceptResult
parameter_list|(
name|Object
name|result
parameter_list|,
name|Exchange
name|resultExchange
parameter_list|)
block|{
if|if
condition|(
name|resultIndex
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|resultIndex
operator|.
name|index
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|splitResult (Object result)
specifier|public
name|Object
name|splitResult
parameter_list|(
name|Object
name|result
parameter_list|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|splitResult
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|instanceof
name|ODataFeed
condition|)
block|{
name|ODataFeed
name|odataFeed
init|=
operator|(
name|ODataFeed
operator|)
name|result
decl_stmt|;
for|for
control|(
name|ODataEntry
name|entry
range|:
name|odataFeed
operator|.
name|getEntries
argument_list|()
control|)
block|{
name|splitResult
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|result
operator|instanceof
name|List
condition|)
block|{
return|return
name|result
return|;
block|}
else|else
block|{
name|splitResult
operator|.
name|add
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
return|return
name|splitResult
return|;
block|}
block|}
end_class

end_unit

