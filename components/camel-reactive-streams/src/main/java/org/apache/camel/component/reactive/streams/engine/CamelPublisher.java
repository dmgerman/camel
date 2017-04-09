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
name|util
operator|.
name|LinkedList
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
name|Objects
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
name|CopyOnWriteArrayList
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
name|ExecutorService
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
name|atomic
operator|.
name|AtomicInteger
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
name|atomic
operator|.
name|AtomicReference
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
name|ReactiveStreamsBackpressureStrategy
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
name|ReactiveStreamsComponent
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
name|ReactiveStreamsEndpoint
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
name|ReactiveStreamsProducer
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
name|DispatchCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|reactivestreams
operator|.
name|Publisher
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
comment|/**  * The Camel publisher. It forwards Camel exchanges to external reactive-streams subscribers.  */
end_comment

begin_class
DECL|class|CamelPublisher
specifier|public
class|class
name|CamelPublisher
implements|implements
name|Publisher
argument_list|<
name|StreamPayload
argument_list|<
name|Exchange
argument_list|>
argument_list|>
implements|,
name|AutoCloseable
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
name|CamelPublisher
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|workerPool
specifier|private
name|ExecutorService
name|workerPool
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|backpressureStrategy
specifier|private
name|ReactiveStreamsBackpressureStrategy
name|backpressureStrategy
decl_stmt|;
DECL|field|subscriptions
specifier|private
name|List
argument_list|<
name|CamelSubscription
argument_list|>
name|subscriptions
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|producer
specifier|private
name|ReactiveStreamsProducer
name|producer
decl_stmt|;
DECL|method|CamelPublisher (ExecutorService workerPool, CamelContext context, String name)
specifier|public
name|CamelPublisher
parameter_list|(
name|ExecutorService
name|workerPool
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|workerPool
operator|=
name|workerPool
expr_stmt|;
name|this
operator|.
name|backpressureStrategy
operator|=
operator|(
operator|(
name|ReactiveStreamsComponent
operator|)
name|context
operator|.
name|getComponent
argument_list|(
literal|"reactive-streams"
argument_list|)
operator|)
operator|.
name|getBackpressureStrategy
argument_list|()
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|subscribe (Subscriber<? super StreamPayload<Exchange>> subscriber)
specifier|public
name|void
name|subscribe
parameter_list|(
name|Subscriber
argument_list|<
name|?
super|super
name|StreamPayload
argument_list|<
name|Exchange
argument_list|>
argument_list|>
name|subscriber
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|subscriber
argument_list|,
literal|"subscriber must not be null"
argument_list|)
expr_stmt|;
name|CamelSubscription
name|sub
init|=
operator|new
name|CamelSubscription
argument_list|(
name|workerPool
argument_list|,
name|this
argument_list|,
name|name
argument_list|,
name|this
operator|.
name|backpressureStrategy
argument_list|,
name|subscriber
argument_list|)
decl_stmt|;
name|this
operator|.
name|subscriptions
operator|.
name|add
argument_list|(
name|sub
argument_list|)
expr_stmt|;
name|subscriber
operator|.
name|onSubscribe
argument_list|(
name|sub
argument_list|)
expr_stmt|;
block|}
DECL|method|unsubscribe (CamelSubscription subscription)
specifier|public
name|void
name|unsubscribe
parameter_list|(
name|CamelSubscription
name|subscription
parameter_list|)
block|{
name|subscriptions
operator|.
name|remove
argument_list|(
name|subscription
argument_list|)
expr_stmt|;
block|}
DECL|method|publish (StreamPayload<Exchange> data)
specifier|public
name|void
name|publish
parameter_list|(
name|StreamPayload
argument_list|<
name|Exchange
argument_list|>
name|data
parameter_list|)
block|{
comment|// freeze the subscriptions
name|List
argument_list|<
name|CamelSubscription
argument_list|>
name|subs
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|(
name|subscriptions
argument_list|)
decl_stmt|;
name|DispatchCallback
argument_list|<
name|Exchange
argument_list|>
name|originalCallback
init|=
name|data
operator|.
name|getCallback
argument_list|()
decl_stmt|;
if|if
condition|(
name|originalCallback
operator|!=
literal|null
operator|&&
name|subs
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// When multiple subscribers have an active subscription,
comment|// we acknowledge the exchange once it has been delivered to every
comment|// subscriber (or their subscription is cancelled)
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|(
name|subs
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
comment|// Use just the first exception in the callback when multiple exceptions are thrown
name|AtomicReference
argument_list|<
name|Throwable
argument_list|>
name|thrown
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|data
operator|=
operator|new
name|StreamPayload
argument_list|<>
argument_list|(
name|data
operator|.
name|getItem
argument_list|()
argument_list|,
parameter_list|(
name|ex
parameter_list|,
name|error
parameter_list|)
lambda|->
block|{
name|thrown
operator|.
name|compareAndSet
argument_list|(
literal|null
argument_list|,
name|error
argument_list|)
expr_stmt|;
if|if
condition|(
name|counter
operator|.
name|decrementAndGet
argument_list|()
operator|==
literal|0
condition|)
block|{
name|originalCallback
operator|.
name|processed
argument_list|(
name|ex
argument_list|,
name|thrown
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|subs
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Exchange published to {} subscriptions for the stream {}: {}"
argument_list|,
name|subs
operator|.
name|size
argument_list|()
argument_list|,
name|name
argument_list|,
name|data
operator|.
name|getItem
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// at least one subscriber
for|for
control|(
name|CamelSubscription
name|sub
range|:
name|subs
control|)
block|{
name|sub
operator|.
name|publish
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|data
operator|.
name|getCallback
argument_list|()
operator|.
name|processed
argument_list|(
name|data
operator|.
name|getItem
argument_list|()
argument_list|,
operator|new
name|IllegalStateException
argument_list|(
literal|"The stream has no active subscriptions"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|attachProducer (ReactiveStreamsProducer producer)
specifier|public
name|void
name|attachProducer
parameter_list|(
name|ReactiveStreamsProducer
name|producer
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|producer
argument_list|,
literal|"producer cannot be null, use the detach method"
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|producer
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"A producer is already attached to the stream '"
operator|+
name|name
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|this
operator|.
name|producer
operator|=
name|producer
expr_stmt|;
comment|// Apply endpoint options if available
name|ReactiveStreamsEndpoint
name|endpoint
init|=
name|producer
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getBackpressureStrategy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|backpressureStrategy
operator|=
name|endpoint
operator|.
name|getBackpressureStrategy
argument_list|()
expr_stmt|;
for|for
control|(
name|CamelSubscription
name|sub
range|:
name|this
operator|.
name|subscriptions
control|)
block|{
name|sub
operator|.
name|setBackpressureStrategy
argument_list|(
name|endpoint
operator|.
name|getBackpressureStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|detachProducer ()
specifier|public
name|void
name|detachProducer
parameter_list|()
block|{
name|this
operator|.
name|producer
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|CamelSubscription
name|sub
range|:
name|subscriptions
control|)
block|{
name|sub
operator|.
name|signalCompletion
argument_list|()
expr_stmt|;
block|}
name|subscriptions
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|getSubscriptionSize ()
specifier|public
name|int
name|getSubscriptionSize
parameter_list|()
block|{
return|return
name|subscriptions
operator|.
name|size
argument_list|()
return|;
block|}
block|}
end_class

end_unit

