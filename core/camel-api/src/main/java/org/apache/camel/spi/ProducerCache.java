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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|RejectedExecutionException
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
name|Producer
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

begin_comment
comment|/**  * Cache containing created {@link Producer}.  */
end_comment

begin_interface
DECL|interface|ProducerCache
specifier|public
interface|interface
name|ProducerCache
extends|extends
name|Service
block|{
comment|/**      * Acquires a pooled producer which you<b>must</b> release back again after usage using the      * {@link #releaseProducer(org.apache.camel.Endpoint, org.apache.camel.AsyncProducer)} method.      *<p/>      * If the producer is currently starting then the cache will wait at most 30 seconds for the producer      * to finish starting and be ready for use.      *      * @param endpoint the endpoint      * @return the producer      */
DECL|method|acquireProducer (Endpoint endpoint)
name|AsyncProducer
name|acquireProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Releases an acquired producer back after usage.      *      * @param endpoint the endpoint      * @param producer the producer to release      */
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
comment|/**      * Sends the exchange to the given endpoint.      *<p>      * This method will<b>not</b> throw an exception. If processing of the given      * Exchange failed then the exception is stored on the provided Exchange      *      * @param endpoint the endpoint to send the exchange to      * @param exchange the exchange to send      * @throws RejectedExecutionException is thrown if CamelContext is stopped      */
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
comment|/**      * Asynchronously sends an exchange to an endpoint using a supplied      * {@link Processor} to populate the exchange      *<p>      * This method will<b>neither</b> throw an exception<b>nor</b> complete future exceptionally.      * If processing of the given Exchange failed then the exception is stored on the return Exchange      *      * @param endpoint        the endpoint to send the exchange to      * @param pattern         the message {@link ExchangePattern} such as      *                        {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param processor       the transformer used to populate the new exchange      * @param resultProcessor a processor to process the exchange when the send is complete.      * @param exchange        an exchange to use in processing. Exchange will be created if parameter is null.      * @param future          the preexisting future to complete when processing is done or null if to create new one      * @return future that completes with exchange when processing is done. Either passed into future parameter      *              or new one if parameter was null      */
DECL|method|asyncSendExchange (Endpoint endpoint, ExchangePattern pattern, Processor processor, Processor resultProcessor, Exchange exchange, CompletableFuture<Exchange> future)
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
name|exchange
parameter_list|,
name|CompletableFuture
argument_list|<
name|Exchange
argument_list|>
name|future
parameter_list|)
function_decl|;
comment|/**      * Gets the source which uses this cache      *      * @return the source      */
DECL|method|getSource ()
name|Object
name|getSource
parameter_list|()
function_decl|;
comment|/**      * Returns the current size of the cache      *      * @return the current size      */
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
comment|/**      * Gets the maximum cache size (capacity).      *      * @return the capacity      */
DECL|method|getCapacity ()
name|int
name|getCapacity
parameter_list|()
function_decl|;
comment|/**      * Gets the cache hits statistic      *<p/>      * Will return<tt>-1</tt> if it cannot determine this if a custom cache was used.      *      * @return the hits      */
DECL|method|getHits ()
name|long
name|getHits
parameter_list|()
function_decl|;
comment|/**      * Gets the cache misses statistic      *<p/>      * Will return<tt>-1</tt> if it cannot determine this if a custom cache was used.      *      * @return the misses      */
DECL|method|getMisses ()
name|long
name|getMisses
parameter_list|()
function_decl|;
comment|/**      * Gets the cache evicted statistic      *<p/>      * Will return<tt>-1</tt> if it cannot determine this if a custom cache was used.      *      * @return the evicted      */
DECL|method|getEvicted ()
name|long
name|getEvicted
parameter_list|()
function_decl|;
comment|/**      * Resets the cache statistics      */
DECL|method|resetCacheStatistics ()
name|void
name|resetCacheStatistics
parameter_list|()
function_decl|;
comment|/**      * Purges this cache      */
DECL|method|purge ()
name|void
name|purge
parameter_list|()
function_decl|;
comment|/**      * Cleanup the cache (purging stale entries)      */
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
comment|/**      * Whether {@link org.apache.camel.spi.EventNotifier} is enabled      */
DECL|method|setEventNotifierEnabled (boolean eventNotifierEnabled)
name|void
name|setEventNotifierEnabled
parameter_list|(
name|boolean
name|eventNotifierEnabled
parameter_list|)
function_decl|;
comment|/**      * Gets the endpoint statistics      */
DECL|method|getEndpointUtilizationStatistics ()
name|EndpointUtilizationStatistics
name|getEndpointUtilizationStatistics
parameter_list|()
function_decl|;
comment|/**      * Sends an exchange to an endpoint using a supplied callback supporting the asynchronous routing engine.      *<p/>      * If an exception was thrown during processing, it would be set on the given Exchange      *      * @param endpoint         the endpoint to send the exchange to      * @param exchange         the exchange, can be<tt>null</tt> if so then create a new exchange from the producer      * @param callback         the asynchronous callback      * @param producerCallback the producer template callback to be executed      * @return (doneSync)<tt>true</tt> to continue execute synchronously,<tt>false</tt> to continue being executed asynchronously      */
DECL|method|doInAsyncProducer (Endpoint endpoint, Exchange exchange, AsyncCallback callback, AsyncProducerCallback producerCallback)
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
name|producerCallback
parameter_list|)
function_decl|;
comment|/**      * Callback for sending a exchange message to a endpoint using an {@link AsyncProcessor} capable producer.      *<p/>      * Using this callback as a template pattern ensures that Camel handles the resource handling and will      * start and stop the given producer, to avoid resource leaks.      */
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

