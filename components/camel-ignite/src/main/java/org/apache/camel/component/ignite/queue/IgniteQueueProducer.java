begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite.queue
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
name|queue
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|concurrent
operator|.
name|TimeUnit
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
name|IgniteQueue
import|;
end_import

begin_class
DECL|class|IgniteQueueProducer
specifier|public
class|class
name|IgniteQueueProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|endpoint
specifier|private
name|IgniteQueueEndpoint
name|endpoint
decl_stmt|;
DECL|field|queue
specifier|private
name|IgniteQueue
argument_list|<
name|Object
argument_list|>
name|queue
decl_stmt|;
DECL|method|IgniteQueueProducer (IgniteQueueEndpoint endpoint, IgniteQueue<Object> queue)
specifier|public
name|IgniteQueueProducer
parameter_list|(
name|IgniteQueueEndpoint
name|endpoint
parameter_list|,
name|IgniteQueue
argument_list|<
name|Object
argument_list|>
name|queue
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
name|queue
operator|=
name|queue
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|Object
name|body
init|=
name|in
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Long
name|millis
decl_stmt|;
switch|switch
condition|(
name|queueOperationFor
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
case|case
name|ADD
case|:
if|if
condition|(
name|Collection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|body
operator|.
name|getClass
argument_list|()
argument_list|)
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
name|queue
operator|.
name|addAll
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
operator|)
name|body
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
name|queue
operator|.
name|add
argument_list|(
name|body
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|CONTAINS
case|:
if|if
condition|(
name|Collection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|body
operator|.
name|getClass
argument_list|()
argument_list|)
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
name|queue
operator|.
name|containsAll
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
operator|)
name|body
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
name|queue
operator|.
name|contains
argument_list|(
name|body
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|SIZE
case|:
name|out
operator|.
name|setBody
argument_list|(
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|REMOVE
case|:
if|if
condition|(
name|Collection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|body
operator|.
name|getClass
argument_list|()
argument_list|)
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
name|queue
operator|.
name|removeAll
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
operator|)
name|body
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
name|queue
operator|.
name|remove
argument_list|(
name|body
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|CLEAR
case|:
if|if
condition|(
name|endpoint
operator|.
name|isPropagateIncomingBodyIfNoReturnValue
argument_list|()
condition|)
block|{
name|out
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
name|queue
operator|.
name|clear
argument_list|()
expr_stmt|;
break|break;
case|case
name|ITERATOR
case|:
name|Iterator
argument_list|<
name|?
argument_list|>
name|iterator
init|=
name|queue
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|out
operator|.
name|setBody
argument_list|(
name|iterator
argument_list|)
expr_stmt|;
break|break;
case|case
name|ARRAY
case|:
name|out
operator|.
name|setBody
argument_list|(
name|queue
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|RETAIN_ALL
case|:
if|if
condition|(
name|Collection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|body
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|out
operator|.
name|setBody
argument_list|(
name|queue
operator|.
name|retainAll
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
operator|)
name|body
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
name|queue
operator|.
name|retainAll
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|body
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|DRAIN
case|:
name|Integer
name|maxElements
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_QUEUE_MAX_ELEMENTS
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|Object
argument_list|>
name|col
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
operator|&&
name|Collection
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|body
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|col
operator|=
operator|(
name|Collection
argument_list|<
name|Object
argument_list|>
operator|)
name|body
expr_stmt|;
block|}
else|else
block|{
name|col
operator|=
name|maxElements
operator|!=
literal|null
condition|?
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|maxElements
argument_list|)
else|:
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|int
name|transferred
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|maxElements
operator|==
literal|null
condition|)
block|{
name|transferred
operator|=
name|queue
operator|.
name|drainTo
argument_list|(
name|col
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|transferred
operator|=
name|queue
operator|.
name|drainTo
argument_list|(
name|col
argument_list|,
name|maxElements
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|setBody
argument_list|(
name|col
argument_list|)
expr_stmt|;
name|out
operator|.
name|setHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_QUEUE_TRANSFERRED_COUNT
argument_list|,
name|transferred
argument_list|)
expr_stmt|;
break|break;
case|case
name|ELEMENT
case|:
name|out
operator|.
name|setBody
argument_list|(
name|queue
operator|.
name|element
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|OFFER
case|:
name|millis
operator|=
name|in
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_QUEUE_TIMEOUT_MILLIS
argument_list|,
name|endpoint
operator|.
name|getTimeoutMillis
argument_list|()
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|boolean
name|result
init|=
name|millis
operator|==
literal|null
condition|?
name|queue
operator|.
name|offer
argument_list|(
name|body
argument_list|)
else|:
name|queue
operator|.
name|offer
argument_list|(
name|body
argument_list|,
name|millis
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|out
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
break|break;
case|case
name|PEEK
case|:
name|out
operator|.
name|setBody
argument_list|(
name|queue
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|POLL
case|:
name|millis
operator|=
name|in
operator|.
name|getHeader
argument_list|(
name|IgniteConstants
operator|.
name|IGNITE_QUEUE_TIMEOUT_MILLIS
argument_list|,
name|endpoint
operator|.
name|getTimeoutMillis
argument_list|()
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|out
operator|.
name|setBody
argument_list|(
name|millis
operator|==
literal|null
condition|?
name|queue
operator|.
name|poll
argument_list|()
else|:
name|queue
operator|.
name|poll
argument_list|(
name|millis
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|PUT
case|:
if|if
condition|(
name|endpoint
operator|.
name|isPropagateIncomingBodyIfNoReturnValue
argument_list|()
condition|)
block|{
name|out
operator|.
name|setBody
argument_list|(
name|in
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|queue
operator|.
name|put
argument_list|(
name|body
argument_list|)
expr_stmt|;
break|break;
case|case
name|TAKE
case|:
name|out
operator|.
name|setBody
argument_list|(
name|queue
operator|.
name|take
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Operation not supported by Ignite Queue producer."
argument_list|)
argument_list|)
expr_stmt|;
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
DECL|method|queueOperationFor (Exchange exchange)
specifier|private
name|IgniteQueueOperation
name|queueOperationFor
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
name|IGNITE_QUEUE_OPERATION
argument_list|,
name|endpoint
operator|.
name|getOperation
argument_list|()
argument_list|,
name|IgniteQueueOperation
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

