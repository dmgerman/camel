begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rxjava2.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rxjava2
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
name|io
operator|.
name|reactivex
operator|.
name|BackpressureStrategy
import|;
end_import

begin_import
import|import
name|io
operator|.
name|reactivex
operator|.
name|Flowable
import|;
end_import

begin_import
import|import
name|io
operator|.
name|reactivex
operator|.
name|FlowableEmitter
import|;
end_import

begin_import
import|import
name|io
operator|.
name|reactivex
operator|.
name|processors
operator|.
name|FlowableProcessor
import|;
end_import

begin_import
import|import
name|io
operator|.
name|reactivex
operator|.
name|processors
operator|.
name|MulticastProcessor
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

begin_class
DECL|class|RxJavaCamelProcessor
specifier|final
class|class
name|RxJavaCamelProcessor
implements|implements
name|Closeable
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|service
specifier|private
specifier|final
name|RxJavaStreamsService
name|service
decl_stmt|;
DECL|field|camelEmitter
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|FlowableEmitter
argument_list|<
name|Exchange
argument_list|>
argument_list|>
name|camelEmitter
decl_stmt|;
DECL|field|publisher
specifier|private
name|FlowableProcessor
argument_list|<
name|Exchange
argument_list|>
name|publisher
decl_stmt|;
DECL|field|camelProducer
specifier|private
name|ReactiveStreamsProducer
name|camelProducer
decl_stmt|;
DECL|method|RxJavaCamelProcessor (RxJavaStreamsService service, String name)
name|RxJavaCamelProcessor
parameter_list|(
name|RxJavaStreamsService
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
name|camelEmitter
operator|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|publisher
operator|=
name|MulticastProcessor
operator|.
name|create
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// Buffered downstream if needed
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
name|detach
argument_list|()
expr_stmt|;
block|}
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
name|Flowable
argument_list|<
name|Exchange
argument_list|>
name|flow
init|=
name|Flowable
operator|.
name|create
argument_list|(
name|camelEmitter
operator|::
name|set
argument_list|,
name|BackpressureStrategy
operator|.
name|MISSING
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
name|flow
operator|.
name|onBackpressureDrop
argument_list|(
name|this
operator|::
name|onBackPressure
argument_list|)
operator|.
name|doAfterNext
argument_list|(
name|this
operator|::
name|onItemEmitted
argument_list|)
operator|.
name|subscribe
argument_list|(
name|this
operator|.
name|publisher
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
name|flow
operator|.
name|doAfterNext
argument_list|(
name|this
operator|::
name|onItemEmitted
argument_list|)
operator|.
name|onBackpressureLatest
argument_list|()
operator|.
name|subscribe
argument_list|(
name|this
operator|.
name|publisher
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|flow
operator|.
name|doAfterNext
argument_list|(
name|this
operator|::
name|onItemEmitted
argument_list|)
operator|.
name|onBackpressureBuffer
argument_list|()
operator|.
name|subscribe
argument_list|(
name|this
operator|.
name|publisher
argument_list|)
expr_stmt|;
block|}
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
name|camelEmitter
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
name|FlowableEmitter
argument_list|<
name|Exchange
argument_list|>
name|emitter
init|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelEmitter
operator|.
name|get
argument_list|()
argument_list|,
literal|"FlowableEmitter"
argument_list|)
decl_stmt|;
name|emitter
operator|.
name|onNext
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|// **************************************
comment|// Helpers
comment|// **************************************
DECL|method|onItemEmitted (Exchange exchange)
specifier|private
name|void
name|onItemEmitted
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

