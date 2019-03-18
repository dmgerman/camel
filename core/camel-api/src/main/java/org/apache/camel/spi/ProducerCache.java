begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
name|CompletableFuture
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
name|AsyncCallback
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
name|AsyncProcessor
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
name|AsyncProducer
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ExchangePattern
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
name|Processor
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
name|Service
import|;
end_import

begin_interface
DECL|interface|ProducerCache
specifier|public
interface|interface
name|ProducerCache
extends|extends
name|Service
block|{
DECL|method|acquireProducer (Endpoint endpoint)
name|AsyncProducer
name|acquireProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
DECL|method|releaseProducer (Endpoint endpoint, AsyncProducer producer)
name|void
name|releaseProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|AsyncProducer
name|producer
parameter_list|)
function_decl|;
DECL|method|send (Endpoint endpoint, Exchange exchange, Processor resultProcessor)
name|Exchange
name|send
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|resultProcessor
parameter_list|)
function_decl|;
DECL|method|asyncSendExchange (Endpoint endpoint, ExchangePattern pattern, Processor processor, Processor resultProcessor, Exchange inExchange, CompletableFuture<Exchange> exchangeFuture)
name|CompletableFuture
argument_list|<
name|Exchange
argument_list|>
name|asyncSendExchange
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Processor
name|resultProcessor
parameter_list|,
name|Exchange
name|inExchange
parameter_list|,
name|CompletableFuture
argument_list|<
name|Exchange
argument_list|>
name|exchangeFuture
parameter_list|)
function_decl|;
DECL|method|getSource ()
name|Object
name|getSource
parameter_list|()
function_decl|;
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
DECL|method|getCapacity ()
name|int
name|getCapacity
parameter_list|()
function_decl|;
DECL|method|getHits ()
name|long
name|getHits
parameter_list|()
function_decl|;
DECL|method|getMisses ()
name|long
name|getMisses
parameter_list|()
function_decl|;
DECL|method|getEvicted ()
name|long
name|getEvicted
parameter_list|()
function_decl|;
DECL|method|resetCacheStatistics ()
name|void
name|resetCacheStatistics
parameter_list|()
function_decl|;
DECL|method|purge ()
name|void
name|purge
parameter_list|()
function_decl|;
DECL|method|cleanUp ()
name|void
name|cleanUp
parameter_list|()
function_decl|;
DECL|method|isEventNotifierEnabled ()
name|boolean
name|isEventNotifierEnabled
parameter_list|()
function_decl|;
DECL|method|setEventNotifierEnabled (boolean eventNotifierEnabled)
name|void
name|setEventNotifierEnabled
parameter_list|(
name|boolean
name|eventNotifierEnabled
parameter_list|)
function_decl|;
DECL|method|getEndpointUtilizationStatistics ()
name|EndpointUtilizationStatistics
name|getEndpointUtilizationStatistics
parameter_list|()
function_decl|;
DECL|method|doInAsyncProducer (Endpoint endpoint, Exchange exchange, AsyncCallback callback, AsyncProducerCallback asyncProducerCallback)
name|boolean
name|doInAsyncProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|AsyncProducerCallback
name|asyncProducerCallback
parameter_list|)
function_decl|;
comment|/**      * Callback for sending a exchange message to a endpoint using an {@link AsyncProcessor} capable producer.      *<p/>      * Using this callback as a template pattern ensures that Camel handles the resource handling and will      * start and stop the given producer, to avoid resource leaks.      *      */
DECL|interface|AsyncProducerCallback
interface|interface
name|AsyncProducerCallback
block|{
comment|/**          * Performs operation on the given producer to send the given exchange.          *          * @param asyncProducer   the async producer, is never<tt>null</tt>          * @param exchange        the exchange to process          * @param callback        the async callback          * @return (doneSync)<tt>true</tt> to continue execute synchronously,<tt>false</tt> to continue being executed asynchronously          */
DECL|method|doInAsyncProducer (AsyncProducer asyncProducer, Exchange exchange, AsyncCallback callback)
name|boolean
name|doInAsyncProducer
parameter_list|(
name|AsyncProducer
name|asyncProducer
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
function_decl|;
block|}
block|}
end_interface

end_unit

