begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.rx.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|rx
operator|.
name|support
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|rx
operator|.
name|Observable
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|Subscriber
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|functions
operator|.
name|Func1
import|;
end_import

begin_class
DECL|class|EndpointSubscribeFunc
specifier|public
class|class
name|EndpointSubscribeFunc
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Observable
operator|.
name|OnSubscribe
argument_list|<
name|T
argument_list|>
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|converter
specifier|private
specifier|final
name|Func1
argument_list|<
name|Exchange
argument_list|,
name|T
argument_list|>
name|converter
decl_stmt|;
DECL|method|EndpointSubscribeFunc (Endpoint endpoint, Func1<Exchange, T> converter)
specifier|public
name|EndpointSubscribeFunc
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Func1
argument_list|<
name|Exchange
argument_list|,
name|T
argument_list|>
name|converter
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|converter
operator|=
name|converter
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call (Subscriber<? super T> subscriber)
specifier|public
name|void
name|call
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
name|add
argument_list|(
operator|new
name|EndpointSubscription
argument_list|<
name|T
argument_list|>
argument_list|(
name|endpoint
argument_list|,
name|subscriber
argument_list|,
name|converter
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

