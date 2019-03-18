begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rxjava2.engine.suport
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
operator|.
name|suport
package|;
end_package

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
comment|/**  * A subscriber for tests.  */
end_comment

begin_class
DECL|class|TestSubscriber
specifier|public
class|class
name|TestSubscriber
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Subscriber
argument_list|<
name|T
argument_list|>
block|{
DECL|field|subscription
specifier|protected
name|Subscription
name|subscription
decl_stmt|;
DECL|field|initiallyRequested
specifier|private
name|long
name|initiallyRequested
decl_stmt|;
DECL|method|TestSubscriber ()
specifier|public
name|TestSubscriber
parameter_list|()
block|{     }
DECL|method|getInitiallyRequested ()
specifier|public
name|long
name|getInitiallyRequested
parameter_list|()
block|{
return|return
name|initiallyRequested
return|;
block|}
DECL|method|setInitiallyRequested (long initiallyRequested)
specifier|public
name|void
name|setInitiallyRequested
parameter_list|(
name|long
name|initiallyRequested
parameter_list|)
block|{
name|this
operator|.
name|initiallyRequested
operator|=
name|initiallyRequested
expr_stmt|;
block|}
DECL|method|request (long exchanges)
specifier|public
name|void
name|request
parameter_list|(
name|long
name|exchanges
parameter_list|)
block|{
name|this
operator|.
name|subscription
operator|.
name|request
argument_list|(
name|exchanges
argument_list|)
expr_stmt|;
block|}
DECL|method|cancel ()
specifier|public
name|void
name|cancel
parameter_list|()
block|{
name|this
operator|.
name|subscription
operator|.
name|cancel
argument_list|()
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
name|this
operator|.
name|subscription
operator|=
name|subscription
expr_stmt|;
if|if
condition|(
name|initiallyRequested
operator|>
literal|0
condition|)
block|{
name|subscription
operator|.
name|request
argument_list|(
name|initiallyRequested
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onNext (T t)
specifier|public
name|void
name|onNext
parameter_list|(
name|T
name|t
parameter_list|)
block|{      }
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
block|{      }
annotation|@
name|Override
DECL|method|onComplete ()
specifier|public
name|void
name|onComplete
parameter_list|()
block|{      }
block|}
end_class

end_unit

