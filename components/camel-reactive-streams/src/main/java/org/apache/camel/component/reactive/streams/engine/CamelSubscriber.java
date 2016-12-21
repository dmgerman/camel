begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactive
operator|.
name|streams
operator|.
name|engine
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|reactive
operator|.
name|streams
operator|.
name|ReactiveStreamsConsumer
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
name|reactive
operator|.
name|streams
operator|.
name|api
operator|.
name|CamelReactiveStreamsService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Subscriber
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Subscription
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * The Camel subscriber. It bridges messages from reactive streams to Camel routes.  */
end_comment

begin_class
DECL|class|CamelSubscriber
specifier|public
class|class
name|CamelSubscriber
implements|implements
name|Subscriber
argument_list|<
name|Exchange
argument_list|>
implements|,
name|Closeable
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelSubscriber
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Enough to be considered unbounded. Requests are refilled once completed.      */
DECL|field|MAX_INFLIGHT_UNBOUNDED
specifier|private
specifier|static
specifier|final
name|long
name|MAX_INFLIGHT_UNBOUNDED
init|=
name|Long
operator|.
name|MAX_VALUE
operator|/
literal|2
decl_stmt|;
DECL|field|consumer
specifier|private
name|ReactiveStreamsConsumer
name|consumer
decl_stmt|;
DECL|field|subscription
specifier|private
name|Subscription
name|subscription
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|requested
specifier|private
name|long
name|requested
decl_stmt|;
DECL|field|inflightCount
specifier|private
name|long
name|inflightCount
decl_stmt|;
DECL|method|CamelSubscriber (String name)
specifier|public
name|CamelSubscriber
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|attachConsumer (ReactiveStreamsConsumer consumer)
specifier|public
name|void
name|attachConsumer
parameter_list|(
name|ReactiveStreamsConsumer
name|consumer
parameter_list|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|this
operator|.
name|consumer
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"A consumer is already attached on stream '"
operator|+
name|name
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
name|refill
argument_list|()
expr_stmt|;
block|}
DECL|method|detachConsumer ()
specifier|public
name|void
name|detachConsumer
parameter_list|()
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|this
operator|.
name|consumer
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onSubscribe (Subscription subscription)
specifier|public
name|void
name|onSubscribe
parameter_list|(
name|Subscription
name|subscription
parameter_list|)
block|{
if|if
condition|(
name|subscription
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"subscription is null for stream '"
operator|+
name|name
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|boolean
name|allowed
init|=
literal|true
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|this
operator|.
name|subscription
operator|!=
literal|null
condition|)
block|{
name|allowed
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|subscription
operator|=
name|subscription
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|allowed
condition|)
block|{
name|subscription
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|refill
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onNext (Exchange exchange)
specifier|public
name|void
name|onNext
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"exchange is null"
argument_list|)
throw|;
block|}
name|ReactiveStreamsConsumer
name|target
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|requested
operator|--
expr_stmt|;
name|target
operator|=
name|this
operator|.
name|consumer
expr_stmt|;
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
name|inflightCount
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
name|target
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|doneSync
lambda|->
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|inflightCount
operator|--
expr_stmt|;
block|}
name|refill
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// This may happen when the consumer is stopped
name|LOG
operator|.
name|warn
argument_list|(
literal|"Message received in stream '{}', but no consumers were attached. Discarding {}."
argument_list|,
name|name
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|refill ()
specifier|protected
name|void
name|refill
parameter_list|()
block|{
name|Long
name|toBeRequested
init|=
literal|null
decl_stmt|;
name|Subscription
name|subs
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|consumer
operator|!=
literal|null
operator|&&
name|this
operator|.
name|subscription
operator|!=
literal|null
condition|)
block|{
name|Integer
name|consMax
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getMaxInflightExchanges
argument_list|()
decl_stmt|;
name|long
name|max
init|=
operator|(
name|consMax
operator|!=
literal|null
operator|&&
name|consMax
operator|>
literal|0
operator|)
condition|?
name|consMax
operator|.
name|longValue
argument_list|()
else|:
name|MAX_INFLIGHT_UNBOUNDED
decl_stmt|;
name|long
name|newRequest
init|=
name|max
operator|-
name|requested
operator|-
name|inflightCount
decl_stmt|;
if|if
condition|(
name|newRequest
operator|>
literal|0
condition|)
block|{
name|toBeRequested
operator|=
name|newRequest
expr_stmt|;
name|requested
operator|+=
name|toBeRequested
expr_stmt|;
name|subs
operator|=
name|this
operator|.
name|subscription
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|toBeRequested
operator|!=
literal|null
condition|)
block|{
name|subs
operator|.
name|request
argument_list|(
name|toBeRequested
argument_list|)
expr_stmt|;
block|}
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
name|throwable
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"throwable is null"
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|error
argument_list|(
literal|"Error in reactive stream '"
operator|+
name|name
operator|+
literal|"'"
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|this
operator|.
name|subscription
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onComplete ()
specifier|public
name|void
name|onComplete
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Reactive stream '{}' completed"
argument_list|,
name|name
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|this
operator|.
name|subscription
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|Subscription
name|subscription
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|subscription
operator|=
name|this
operator|.
name|subscription
expr_stmt|;
block|}
if|if
condition|(
name|subscription
operator|!=
literal|null
condition|)
block|{
name|subscription
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

