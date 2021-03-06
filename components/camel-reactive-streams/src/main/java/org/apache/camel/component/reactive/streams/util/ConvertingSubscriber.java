begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.reactive.streams.util
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
name|util
package|;
end_package

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
name|support
operator|.
name|DefaultExchange
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
comment|/**  * A subscriber that converts items of the given type into Camel {@code Exchange}s.  */
end_comment

begin_class
DECL|class|ConvertingSubscriber
specifier|public
class|class
name|ConvertingSubscriber
parameter_list|<
name|R
parameter_list|>
implements|implements
name|Subscriber
argument_list|<
name|R
argument_list|>
block|{
DECL|field|type
specifier|private
name|Class
argument_list|<
name|R
argument_list|>
name|type
decl_stmt|;
DECL|field|delegate
specifier|private
name|Subscriber
argument_list|<
name|Exchange
argument_list|>
name|delegate
decl_stmt|;
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|method|ConvertingSubscriber (Subscriber<Exchange> delegate, CamelContext context, Class<R> type)
specifier|public
name|ConvertingSubscriber
parameter_list|(
name|Subscriber
argument_list|<
name|Exchange
argument_list|>
name|delegate
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|Class
argument_list|<
name|R
argument_list|>
name|type
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|delegate
argument_list|,
literal|"delegate subscriber cannot be null"
argument_list|)
expr_stmt|;
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|type
argument_list|,
literal|"type cannot be null"
argument_list|)
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
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
literal|"subscription is null"
argument_list|)
throw|;
block|}
name|delegate
operator|.
name|onSubscribe
argument_list|(
name|subscription
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onNext (R r)
specifier|public
name|void
name|onNext
parameter_list|(
name|R
name|r
parameter_list|)
block|{
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"element is null"
argument_list|)
throw|;
block|}
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|r
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|onNext
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
name|delegate
operator|.
name|onError
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onComplete ()
specifier|public
name|void
name|onComplete
parameter_list|()
block|{
name|delegate
operator|.
name|onComplete
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

