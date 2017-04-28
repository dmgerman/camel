begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactor.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|reactor
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
name|ReactiveStreamsDiscardedException
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
name|ReactiveStreamsHelper
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
name|util
operator|.
name|ObjectHelper
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
name|reactor
operator|.
name|core
operator|.
name|publisher
operator|.
name|EmitterProcessor
import|;
end_import

begin_import
import|import
name|reactor
operator|.
name|core
operator|.
name|publisher
operator|.
name|Flux
import|;
end_import

begin_import
import|import
name|reactor
operator|.
name|core
operator|.
name|publisher
operator|.
name|FluxSink
import|;
end_import

begin_import
import|import
name|reactor
operator|.
name|core
operator|.
name|publisher
operator|.
name|SynchronousSink
import|;
end_import

begin_import
import|import
name|reactor
operator|.
name|util
operator|.
name|concurrent
operator|.
name|QueueSupplier
import|;
end_import

begin_class
DECL|class|ReactorCamelProcessor
specifier|final
class|class
name|ReactorCamelProcessor
implements|implements
name|Closeable
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|publisher
specifier|private
specifier|final
name|EmitterProcessor
argument_list|<
name|Exchange
argument_list|>
name|publisher
decl_stmt|;
DECL|field|camelSink
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|FluxSink
argument_list|<
name|Exchange
argument_list|>
argument_list|>
name|camelSink
decl_stmt|;
DECL|field|service
specifier|private
specifier|final
name|ReactorStreamsService
name|service
decl_stmt|;
DECL|field|camelProducer
specifier|private
name|ReactiveStreamsProducer
name|camelProducer
decl_stmt|;
DECL|method|ReactorCamelProcessor (ReactorStreamsService service, String name)
name|ReactorCamelProcessor
parameter_list|(
name|ReactorStreamsService
name|service
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|service
operator|=
name|service
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|camelProducer
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|camelSink
operator|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|()
expr_stmt|;
comment|// TODO: The perfect emitter processor would have no buffer (0 sized)
comment|// The chain caches one more item than expected.
comment|// This implementation has (almost) full control over backpressure, but it's too chatty.
comment|// There's a ticket to improve chattiness of the reactive-streams internal impl.
name|this
operator|.
name|publisher
operator|=
name|EmitterProcessor
operator|.
name|create
argument_list|(
literal|1
argument_list|,
literal|false
argument_list|)
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
name|IOException
block|{     }
DECL|method|getPublisher ()
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|getPublisher
parameter_list|()
block|{
return|return
name|publisher
return|;
block|}
DECL|method|attach (ReactiveStreamsProducer producer)
specifier|synchronized
name|void
name|attach
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
name|camelProducer
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
if|if
condition|(
name|this
operator|.
name|camelProducer
operator|!=
name|producer
condition|)
block|{
name|detach
argument_list|()
expr_stmt|;
name|ReactiveStreamsBackpressureStrategy
name|strategy
init|=
name|producer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBackpressureStrategy
argument_list|()
decl_stmt|;
name|Flux
argument_list|<
name|Exchange
argument_list|>
name|flux
init|=
name|Flux
operator|.
name|create
argument_list|(
name|camelSink
operator|::
name|set
argument_list|,
name|FluxSink
operator|.
name|OverflowStrategy
operator|.
name|IGNORE
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|strategy
argument_list|,
name|ReactiveStreamsBackpressureStrategy
operator|.
name|OLDEST
argument_list|)
condition|)
block|{
comment|// signal item emitted for non-dropped items only
name|flux
operator|=
name|flux
operator|.
name|onBackpressureDrop
argument_list|(
name|this
operator|::
name|onBackPressure
argument_list|)
operator|.
name|handle
argument_list|(
name|this
operator|::
name|onItemEmitted
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|strategy
argument_list|,
name|ReactiveStreamsBackpressureStrategy
operator|.
name|LATEST
argument_list|)
condition|)
block|{
comment|// Since there is no callback for dropped elements on backpressure "latest", item emission is signaled before dropping
comment|// No exception is reported back to the exchanges
name|flux
operator|=
name|flux
operator|.
name|handle
argument_list|(
name|this
operator|::
name|onItemEmitted
argument_list|)
operator|.
name|onBackpressureLatest
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// Default strategy is BUFFER
name|flux
operator|=
name|flux
operator|.
name|onBackpressureBuffer
argument_list|(
name|QueueSupplier
operator|.
name|SMALL_BUFFER_SIZE
argument_list|,
name|this
operator|::
name|onBackPressure
argument_list|)
operator|.
name|handle
argument_list|(
name|this
operator|::
name|onItemEmitted
argument_list|)
expr_stmt|;
block|}
name|flux
operator|.
name|subscribe
argument_list|(
name|this
operator|.
name|publisher
argument_list|)
expr_stmt|;
name|camelProducer
operator|=
name|producer
expr_stmt|;
block|}
block|}
DECL|method|detach ()
specifier|synchronized
name|void
name|detach
parameter_list|()
block|{
name|this
operator|.
name|camelProducer
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|camelSink
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|send (Exchange exchange)
name|void
name|send
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|service
operator|.
name|isRunAllowed
argument_list|()
condition|)
block|{
name|FluxSink
argument_list|<
name|Exchange
argument_list|>
name|sink
init|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelSink
operator|.
name|get
argument_list|()
argument_list|,
literal|"FluxSink"
argument_list|)
decl_stmt|;
name|sink
operator|.
name|next
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|// **************************************
comment|// Helpers
comment|// **************************************
DECL|method|onItemEmitted (Exchange exchange, SynchronousSink<Exchange> sink)
specifier|private
name|void
name|onItemEmitted
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|SynchronousSink
argument_list|<
name|Exchange
argument_list|>
name|sink
parameter_list|)
block|{
if|if
condition|(
name|service
operator|.
name|isRunAllowed
argument_list|()
condition|)
block|{
name|sink
operator|.
name|next
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|ReactiveStreamsHelper
operator|.
name|invokeDispatchCallback
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onBackPressure (Exchange exchange)
specifier|private
name|void
name|onBackPressure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|ReactiveStreamsHelper
operator|.
name|invokeDispatchCallback
argument_list|(
name|exchange
argument_list|,
operator|new
name|ReactiveStreamsDiscardedException
argument_list|(
literal|"Discarded by back pressure strategy"
argument_list|,
name|exchange
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

