begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
package|;
end_package

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
name|util
operator|.
name|ExchangeHelper
import|;
end_import

begin_comment
comment|/**  * Implementation of the {@link SynchronizedExchange} interface that correctly handles all completion  * synchronisation courtesies for multiple consumers.  */
end_comment

begin_class
DECL|class|MultipleConsumerSynchronizedExchange
specifier|public
class|class
name|MultipleConsumerSynchronizedExchange
extends|extends
name|AbstractSynchronizedExchange
block|{
DECL|field|expectedConsumers
specifier|private
specifier|final
name|int
name|expectedConsumers
decl_stmt|;
DECL|field|processedConsumers
specifier|private
specifier|final
name|AtomicInteger
name|processedConsumers
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|field|resultHandled
specifier|private
specifier|final
name|AtomicBoolean
name|resultHandled
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|method|MultipleConsumerSynchronizedExchange (Exchange exchange, int expectedConsumers)
specifier|public
name|MultipleConsumerSynchronizedExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|int
name|expectedConsumers
parameter_list|)
block|{
name|super
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|this
operator|.
name|expectedConsumers
operator|=
name|expectedConsumers
expr_stmt|;
name|processedConsumers
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|consumed (Exchange result)
specifier|public
name|void
name|consumed
parameter_list|(
name|Exchange
name|result
parameter_list|)
block|{
if|if
condition|(
name|processedConsumers
operator|.
name|incrementAndGet
argument_list|()
operator|==
name|expectedConsumers
operator|||
name|result
operator|.
name|getException
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|resultHandled
operator|.
name|getAndSet
argument_list|(
literal|true
argument_list|)
condition|)
block|{
comment|// all consumers are done processing or an exception occurred
comment|//SEDA Does not configure an aggregator in the internally used MulticastProcessor
comment|//As a result, the behaviour of SEDA in case of multicast is to only copy the results on an error
if|if
condition|(
name|result
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ExchangeHelper
operator|.
name|copyResults
argument_list|(
name|getExchange
argument_list|()
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
name|performSynchronization
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|cancelAndGetOriginalExchange ()
specifier|public
name|Exchange
name|cancelAndGetOriginalExchange
parameter_list|()
block|{
name|resultHandled
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|cancelAndGetOriginalExchange
argument_list|()
return|;
block|}
block|}
end_class

end_unit

