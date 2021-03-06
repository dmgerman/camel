begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite.cache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ignite
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Set
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
name|InvalidPayloadException
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
name|ignite
operator|.
name|IgniteConstants
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
name|ignite
operator|.
name|IgniteHelper
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
name|Synchronization
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
name|DefaultAsyncProducer
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
name|MessageHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|IgniteCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|cache
operator|.
name|CachePeekMode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|cache
operator|.
name|query
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|cache
operator|.
name|query
operator|.
name|QueryCursor
import|;
end_import

begin_comment
comment|/**  * Ignite Cache producer.  */
end_comment

begin_class
DECL|class|IgniteCacheProducer
specifier|public
class|class
name|IgniteCacheProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|cache
specifier|private
name|IgniteCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
decl_stmt|;
DECL|field|endpoint
specifier|private
name|IgniteCacheEndpoint
name|endpoint
decl_stmt|;
DECL|method|IgniteCacheProducer (IgniteCacheEndpoint endpoint, IgniteCache<Object, Object> igniteCache)
specifier|public
name|IgniteCacheProducer
parameter_list|(
name|IgniteCacheEndpoint
name|endpoint
parameter_list|,
name|IgniteCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|igniteCache
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
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
name|cache
operator|=
name|igniteCache
expr_stmt|;
block|}
annotation|@
name|Override
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
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|MessageHelper
operator|.
name|copyHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|out
argument_list|,
literal|true
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|cacheOperationFor
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
case|case
name|GET
case|:
name|doGet
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
break|break;
case|case
name|PUT
case|:
name|doPut
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
break|break;
case|case
name|QUERY
case|:
name|doQuery
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|REMOVE
case|:
name|doRemove
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
break|break;
case|case
name|CLEAR
case|:
name|doClear
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
break|break;
case|case
name|SIZE
case|:
name|doSize
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
break|break;
case|case
name|REBALANCE
case|:
name|doRebalance
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doGet (Message in, Message out)
specifier|private
name|void
name|doGet
parameter_list|(
name|Message
name|in
parameter_list|,
name|Message
name|out
parameter_list|)
block|{
name|Object
name|cacheKey
init|=
name|cacheKey
argument_list|(
name|in
argument_list|)
decl_stmt|;
if|if
condition|(
name|cacheKey
operator|instanceof
name|Set
operator|&&
operator|!
name|endpoint
operator|.
name|isTreatCollectionsAsCacheObjects
argument_list|()
condition|)
block|{
name|out
operator|.
name|setBody
argument_list|(
name|cache
operator|.
name|getAll
argument_list|(
operator|(
name|Set
argument_list|<
name|Object
argument_list|>
operator|)
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|setBody
argument_list|(
name|cache
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doPut (Message in, Message out)
specifier|private
name|void
name|doPut
parameter_list|(
name|Message
name|in
parameter_list|,
name|Message
name|out
parameter_list|)
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|in
operator|.
name|getBody
argument_list|(
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
name|cache
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return;
block|}
name|Object
name|cacheKey
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_CACHE_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|cacheKey
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cache PUT operation requires the cache key in the CamelIgniteCacheKey header, "
operator|+
literal|"or a payload of type Map."
argument_list|)
throw|;
block|}
name|cache
operator|.
name|put
argument_list|(
name|cacheKey
argument_list|,
name|in
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|IgniteHelper
operator|.
name|maybePropagateIncomingBody
argument_list|(
name|endpoint
argument_list|,
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doQuery (Message in, Message out, Exchange exchange)
specifier|private
name|void
name|doQuery
parameter_list|(
name|Message
name|in
parameter_list|,
name|Message
name|out
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Query
argument_list|<
name|Object
argument_list|>
name|query
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_CACHE_QUERY
argument_list|,
name|Query
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|query
operator|=
name|in
operator|.
name|getMandatoryBody
argument_list|(
name|Query
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidPayloadException
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
return|return;
block|}
block|}
specifier|final
name|QueryCursor
argument_list|<
name|Object
argument_list|>
name|cursor
init|=
name|cache
operator|.
name|query
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|out
operator|.
name|setBody
argument_list|(
name|cursor
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|Synchronization
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|cursor
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|cursor
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doRemove (Message in, Message out)
specifier|private
name|void
name|doRemove
parameter_list|(
name|Message
name|in
parameter_list|,
name|Message
name|out
parameter_list|)
block|{
name|Object
name|cacheKey
init|=
name|cacheKey
argument_list|(
name|in
argument_list|)
decl_stmt|;
if|if
condition|(
name|cacheKey
operator|instanceof
name|Set
operator|&&
operator|!
name|endpoint
operator|.
name|isTreatCollectionsAsCacheObjects
argument_list|()
condition|)
block|{
name|cache
operator|.
name|removeAll
argument_list|(
operator|(
name|Set
argument_list|<
name|Object
argument_list|>
operator|)
name|cacheKey
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cache
operator|.
name|remove
argument_list|(
name|cacheKey
argument_list|)
expr_stmt|;
block|}
name|IgniteHelper
operator|.
name|maybePropagateIncomingBody
argument_list|(
name|endpoint
argument_list|,
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|doClear (Message in, Message out)
specifier|private
name|void
name|doClear
parameter_list|(
name|Message
name|in
parameter_list|,
name|Message
name|out
parameter_list|)
block|{
name|cache
operator|.
name|removeAll
argument_list|()
expr_stmt|;
name|IgniteHelper
operator|.
name|maybePropagateIncomingBody
argument_list|(
name|endpoint
argument_list|,
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|doRebalance (Message in, Message out)
specifier|private
name|void
name|doRebalance
parameter_list|(
name|Message
name|in
parameter_list|,
name|Message
name|out
parameter_list|)
block|{
name|cache
operator|.
name|rebalance
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|IgniteHelper
operator|.
name|maybePropagateIncomingBody
argument_list|(
name|endpoint
argument_list|,
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doSize (Message in, Message out)
specifier|private
name|void
name|doSize
parameter_list|(
name|Message
name|in
parameter_list|,
name|Message
name|out
parameter_list|)
block|{
name|Object
name|peekMode
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_CACHE_PEEK_MODE
argument_list|,
name|endpoint
operator|.
name|getCachePeekMode
argument_list|()
argument_list|)
decl_stmt|;
name|Integer
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|peekMode
operator|instanceof
name|Collection
condition|)
block|{
name|result
operator|=
name|cache
operator|.
name|size
argument_list|(
operator|(
operator|(
name|Collection
argument_list|<
name|Object
argument_list|>
operator|)
name|peekMode
operator|)
operator|.
name|toArray
argument_list|(
operator|new
name|CachePeekMode
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|peekMode
operator|instanceof
name|CachePeekMode
condition|)
block|{
name|result
operator|=
name|cache
operator|.
name|size
argument_list|(
operator|(
name|CachePeekMode
operator|)
name|peekMode
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|cacheKey (Message msg)
specifier|private
name|Object
name|cacheKey
parameter_list|(
name|Message
name|msg
parameter_list|)
block|{
name|Object
name|cacheKey
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_CACHE_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|cacheKey
operator|==
literal|null
condition|)
block|{
name|cacheKey
operator|=
name|msg
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
return|return
name|cacheKey
return|;
block|}
DECL|method|cacheOperationFor (Exchange exchange)
specifier|private
name|IgniteCacheOperation
name|cacheOperationFor
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_CACHE_OPERATION
argument_list|,
name|endpoint
operator|.
name|getOperation
argument_list|()
argument_list|,
name|IgniteCacheOperation
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

