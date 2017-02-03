begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|TypeConversionException
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
comment|/**  * A publisher that converts Camel {@code Exchange}s into the given type.  */
end_comment

begin_class
DECL|class|ConvertingPublisher
specifier|public
class|class
name|ConvertingPublisher
parameter_list|<
name|R
parameter_list|>
implements|implements
name|Publisher
argument_list|<
name|R
argument_list|>
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
name|ConvertingPublisher
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|delegate
specifier|private
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|delegate
decl_stmt|;
DECL|field|type
specifier|private
name|Class
argument_list|<
name|R
argument_list|>
name|type
decl_stmt|;
DECL|method|ConvertingPublisher (Publisher<Exchange> delegate, Class<R> type)
specifier|public
name|ConvertingPublisher
parameter_list|(
name|Publisher
argument_list|<
name|Exchange
argument_list|>
name|delegate
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
literal|"delegate publisher cannot be null"
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
name|type
operator|=
name|type
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|subscribe (Subscriber<? super R> subscriber)
specifier|public
name|void
name|subscribe
parameter_list|(
name|Subscriber
argument_list|<
name|?
super|super
name|R
argument_list|>
name|subscriber
parameter_list|)
block|{
name|delegate
operator|.
name|subscribe
argument_list|(
operator|new
name|Subscriber
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|private
name|AtomicBoolean
name|active
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|true
argument_list|)
decl_stmt|;
specifier|private
name|Subscription
name|subscription
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|onSubscribe
parameter_list|(
name|Subscription
name|newSubscription
parameter_list|)
block|{
if|if
condition|(
name|newSubscription
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
elseif|else
if|if
condition|(
name|newSubscription
operator|==
name|this
operator|.
name|subscription
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"already subscribed to the subscription: "
operator|+
name|newSubscription
argument_list|)
throw|;
block|}
if|if
condition|(
name|this
operator|.
name|subscription
operator|!=
literal|null
condition|)
block|{
name|newSubscription
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|subscription
operator|=
name|newSubscription
expr_stmt|;
name|subscriber
operator|.
name|onSubscribe
argument_list|(
name|newSubscription
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onNext
parameter_list|(
name|Exchange
name|ex
parameter_list|)
block|{
if|if
condition|(
operator|!
name|active
operator|.
name|get
argument_list|()
condition|)
block|{
return|return;
block|}
name|R
name|r
decl_stmt|;
try|try
block|{
if|if
condition|(
name|ex
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|r
operator|=
name|ex
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|r
operator|=
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|TypeConversionException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unable to convert body to the specified type: "
operator|+
name|type
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|r
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|r
operator|==
literal|null
operator|&&
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|onError
argument_list|(
operator|new
name|ClassCastException
argument_list|(
literal|"Unable to convert body to the specified type: "
operator|+
name|type
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|active
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|subscription
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|subscriber
operator|.
name|onNext
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
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
operator|!
name|active
operator|.
name|get
argument_list|()
condition|)
block|{
return|return;
block|}
name|subscriber
operator|.
name|onError
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|()
block|{
if|if
condition|(
operator|!
name|active
operator|.
name|get
argument_list|()
condition|)
block|{
return|return;
block|}
name|subscriber
operator|.
name|onComplete
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

