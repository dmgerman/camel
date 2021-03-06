begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.support
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
name|support
package|;
end_package

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
name|NoSuchElementException
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
name|AtomicLong
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
name|reactivestreams
operator|.
name|Subscription
import|;
end_import

begin_comment
comment|/**  * A publisher for tests.  */
end_comment

begin_class
DECL|class|TestPublisher
specifier|public
class|class
name|TestPublisher
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Publisher
argument_list|<
name|T
argument_list|>
block|{
DECL|field|data
specifier|private
name|Iterable
argument_list|<
name|T
argument_list|>
name|data
decl_stmt|;
DECL|field|delay
specifier|private
name|long
name|delay
decl_stmt|;
DECL|method|TestPublisher (Iterable<T> data)
specifier|public
name|TestPublisher
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|data
parameter_list|)
block|{
name|this
argument_list|(
name|data
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
block|}
DECL|method|TestPublisher (Iterable<T> data, long delay)
specifier|public
name|TestPublisher
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|data
parameter_list|,
name|long
name|delay
parameter_list|)
block|{
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|subscribe (Subscriber<? super T> subscriber)
specifier|public
name|void
name|subscribe
parameter_list|(
name|Subscriber
argument_list|<
name|?
super|super
name|T
argument_list|>
name|subscriber
parameter_list|)
block|{
name|subscriber
operator|.
name|onSubscribe
argument_list|(
operator|new
name|Subscription
argument_list|()
block|{
specifier|private
name|Iterator
argument_list|<
name|T
argument_list|>
name|it
init|=
name|data
operator|.
name|iterator
argument_list|()
decl_stmt|;
specifier|private
name|AtomicLong
name|requested
init|=
operator|new
name|AtomicLong
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|private
name|Object
name|monitor
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|request
parameter_list|(
name|long
name|l
parameter_list|)
block|{
name|this
operator|.
name|requested
operator|.
name|addAndGet
argument_list|(
name|l
argument_list|)
expr_stmt|;
operator|new
name|Thread
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
synchronized|synchronized
init|(
name|monitor
init|)
block|{
name|boolean
name|wasNonEmpty
init|=
name|it
operator|.
name|hasNext
argument_list|()
decl_stmt|;
while|while
condition|(
name|requested
operator|.
name|longValue
argument_list|()
operator|>
literal|0
operator|&&
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|T
name|d
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|requested
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
if|if
condition|(
name|delay
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{                                     }
block|}
name|subscriber
operator|.
name|onNext
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wasNonEmpty
operator|&&
operator|!
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|// data cannot be added to this test publisher
name|subscriber
operator|.
name|onComplete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|cancel
parameter_list|()
block|{
synchronized|synchronized
init|(
name|monitor
init|)
block|{
name|this
operator|.
name|requested
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|this
operator|.
name|it
operator|=
operator|new
name|Iterator
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|T
name|next
parameter_list|()
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
block|}
expr_stmt|;
operator|new
name|Thread
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|subscriber
operator|.
name|onComplete
argument_list|()
expr_stmt|;
block|}
block|}
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

