begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|AsyncProducerCallback
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
name|FailedToCreateProducerException
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
name|ProducerCallback
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
name|ServicePoolAware
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
name|processor
operator|.
name|UnitOfWorkProducer
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
name|spi
operator|.
name|ServicePool
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
name|ServiceSupport
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
name|AsyncProcessorConverterHelper
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
name|CamelContextHelper
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
name|EventHelper
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
name|LRUCache
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
name|LRUSoftCache
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
name|ServiceHelper
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
name|StopWatch
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
comment|/**  * Cache containing created {@link Producer}.  *  * @version   */
end_comment

begin_class
DECL|class|ProducerCache
specifier|public
class|class
name|ProducerCache
extends|extends
name|ServiceSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ProducerCache
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|pool
specifier|private
specifier|final
name|ServicePool
argument_list|<
name|Endpoint
argument_list|,
name|Producer
argument_list|>
name|pool
decl_stmt|;
DECL|field|producers
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Producer
argument_list|>
name|producers
decl_stmt|;
DECL|field|source
specifier|private
specifier|final
name|Object
name|source
decl_stmt|;
DECL|method|ProducerCache (Object source, CamelContext camelContext)
specifier|public
name|ProducerCache
parameter_list|(
name|Object
name|source
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
argument_list|(
name|source
argument_list|,
name|camelContext
argument_list|,
name|CamelContextHelper
operator|.
name|getMaximumCachePoolSize
argument_list|(
name|camelContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|ProducerCache (Object source, CamelContext camelContext, int cacheSize)
specifier|public
name|ProducerCache
parameter_list|(
name|Object
name|source
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|,
name|int
name|cacheSize
parameter_list|)
block|{
name|this
argument_list|(
name|source
argument_list|,
name|camelContext
argument_list|,
name|camelContext
operator|.
name|getProducerServicePool
argument_list|()
argument_list|,
name|createLRUCache
argument_list|(
name|cacheSize
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|ProducerCache (Object source, CamelContext camelContext, ServicePool<Endpoint, Producer> producerServicePool, Map<String, Producer> cache)
specifier|public
name|ProducerCache
parameter_list|(
name|Object
name|source
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|,
name|ServicePool
argument_list|<
name|Endpoint
argument_list|,
name|Producer
argument_list|>
name|producerServicePool
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Producer
argument_list|>
name|cache
parameter_list|)
block|{
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|pool
operator|=
name|producerServicePool
expr_stmt|;
name|this
operator|.
name|producers
operator|=
name|cache
expr_stmt|;
block|}
comment|/**      * Creates the {@link LRUCache} to be used.      *<p/>      * This implementation returns a {@link LRUSoftCache} instance.       * @param cacheSize the cache size      * @return the cache      */
DECL|method|createLRUCache (int cacheSize)
specifier|protected
specifier|static
name|LRUCache
argument_list|<
name|String
argument_list|,
name|Producer
argument_list|>
name|createLRUCache
parameter_list|(
name|int
name|cacheSize
parameter_list|)
block|{
comment|// We use a soft reference cache to allow the JVM to re-claim memory if it runs low on memory.
return|return
operator|new
name|LRUSoftCache
argument_list|<
name|String
argument_list|,
name|Producer
argument_list|>
argument_list|(
name|cacheSize
argument_list|)
return|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
comment|/**      * Gets the source which uses this cache      *      * @return the source      */
DECL|method|getSource ()
specifier|public
name|Object
name|getSource
parameter_list|()
block|{
return|return
name|source
return|;
block|}
comment|/**      * Acquires a pooled producer which you<b>must</b> release back again after usage using the      * {@link #releaseProducer(org.apache.camel.Endpoint, org.apache.camel.Producer)} method.      *      * @param endpoint the endpoint      * @return the producer      */
DECL|method|acquireProducer (Endpoint endpoint)
specifier|public
name|Producer
name|acquireProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|doGetProducer
argument_list|(
name|endpoint
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Releases an acquired producer back after usage.      *      * @param endpoint the endpoint      * @param producer the producer to release      * @throws Exception can be thrown if error stopping producer if that was needed.      */
DECL|method|releaseProducer (Endpoint endpoint, Producer producer)
specifier|public
name|void
name|releaseProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Producer
name|producer
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|producer
operator|instanceof
name|ServicePoolAware
condition|)
block|{
comment|// release back to the pool
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|producer
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
comment|// stop non singleton producers as we should not leak resources
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Starts the {@link Producer} to be used for sending to the given endpoint      *<p/>      * This can be used to early start the {@link Producer} to ensure it can be created,      * such as when Camel is started. This allows to fail fast in case the {@link Producer}      * could not be started.      *      * @param endpoint the endpoint to send the exchange to      * @throws Exception is thrown if failed to create or start the {@link Producer}      */
DECL|method|startProducer (Endpoint endpoint)
specifier|public
name|void
name|startProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|Producer
name|producer
init|=
name|acquireProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|releaseProducer
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sends the exchange to the given endpoint.      *<p>      * This method will<b>not</b> throw an exception. If processing of the given      * Exchange failed then the exception is stored on the provided Exchange      *      * @param endpoint the endpoint to send the exchange to      * @param exchange the exchange to send      */
DECL|method|send (Endpoint endpoint, Exchange exchange)
specifier|public
name|void
name|send
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|sendExchange
argument_list|(
name|endpoint
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sends an exchange to an endpoint using a supplied      * {@link Processor} to populate the exchange      *<p>      * This method will<b>not</b> throw an exception. If processing of the given      * Exchange failed then the exception is stored on the return Exchange      *      * @param endpoint the endpoint to send the exchange to      * @param processor the transformer used to populate the new exchange      * @throws org.apache.camel.CamelExecutionException is thrown if sending failed      * @return the exchange      */
DECL|method|send (Endpoint endpoint, Processor processor)
specifier|public
name|Exchange
name|send
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
return|return
name|sendExchange
argument_list|(
name|endpoint
argument_list|,
literal|null
argument_list|,
name|processor
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Sends an exchange to an endpoint using a supplied      * {@link Processor} to populate the exchange      *<p>      * This method will<b>not</b> throw an exception. If processing of the given      * Exchange failed then the exception is stored on the return Exchange      *      * @param endpoint the endpoint to send the exchange to      * @param pattern the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param processor the transformer used to populate the new exchange      * @return the exchange      */
DECL|method|send (Endpoint endpoint, ExchangePattern pattern, Processor processor)
specifier|public
name|Exchange
name|send
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
return|return
name|sendExchange
argument_list|(
name|endpoint
argument_list|,
name|pattern
argument_list|,
name|processor
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Sends an exchange to an endpoint using a supplied callback, using the synchronous processing.      *<p/>      * If an exception was thrown during processing, it would be set on the given Exchange      *      * @param endpoint  the endpoint to send the exchange to      * @param exchange  the exchange, can be<tt>null</tt> if so then create a new exchange from the producer      * @param pattern   the exchange pattern, can be<tt>null</tt>      * @param callback  the callback      * @return the response from the callback      * @see #doInAsyncProducer(org.apache.camel.Endpoint, org.apache.camel.Exchange, org.apache.camel.ExchangePattern, org.apache.camel.AsyncCallback, org.apache.camel.AsyncProducerCallback)      */
DECL|method|doInProducer (Endpoint endpoint, Exchange exchange, ExchangePattern pattern, ProducerCallback<T> callback)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|doInProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|ProducerCallback
argument_list|<
name|T
argument_list|>
name|callback
parameter_list|)
block|{
name|T
name|answer
init|=
literal|null
decl_stmt|;
comment|// get the producer and we do not mind if its pooled as we can handle returning it back to the pool
name|Producer
name|producer
init|=
name|doGetProducer
argument_list|(
name|endpoint
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|producer
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|isStopped
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring exchange sent after processor is stopped: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No producer, this processor has not been started: "
operator|+
name|this
argument_list|)
throw|;
block|}
block|}
name|StopWatch
name|watch
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
comment|// record timing for sending the exchange using the producer
name|watch
operator|=
operator|new
name|StopWatch
argument_list|()
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|EventHelper
operator|.
name|notifyExchangeSending
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
comment|// invoke the callback
name|answer
operator|=
name|callback
operator|.
name|doInProducer
argument_list|(
name|producer
argument_list|,
name|exchange
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|long
name|timeTaken
init|=
name|watch
operator|.
name|stop
argument_list|()
decl_stmt|;
comment|// emit event that the exchange was sent to the endpoint
name|EventHelper
operator|.
name|notifyExchangeSent
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|,
name|timeTaken
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|producer
operator|instanceof
name|ServicePoolAware
condition|)
block|{
comment|// release back to the pool
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|producer
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
comment|// stop non singleton producers as we should not leak resources
try|try
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore and continue
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error stopping producer: "
operator|+
name|producer
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Sends an exchange to an endpoint using a supplied callback supporting the asynchronous routing engine.      *<p/>      * If an exception was thrown during processing, it would be set on the given Exchange      *      * @param endpoint         the endpoint to send the exchange to      * @param exchange         the exchange, can be<tt>null</tt> if so then create a new exchange from the producer      * @param pattern          the exchange pattern, can be<tt>null</tt>      * @param callback         the asynchronous callback      * @param producerCallback the producer template callback to be executed      * @return (doneSync)<tt>true</tt> to continue execute synchronously,<tt>false</tt> to continue being executed asynchronously      */
DECL|method|doInAsyncProducer (final Endpoint endpoint, final Exchange exchange, final ExchangePattern pattern, final AsyncCallback callback, final AsyncProducerCallback producerCallback)
specifier|public
name|boolean
name|doInAsyncProducer
parameter_list|(
specifier|final
name|Endpoint
name|endpoint
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|ExchangePattern
name|pattern
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
specifier|final
name|AsyncProducerCallback
name|producerCallback
parameter_list|)
block|{
name|boolean
name|sync
init|=
literal|true
decl_stmt|;
comment|// get the producer and we do not mind if its pooled as we can handle returning it back to the pool
specifier|final
name|Producer
name|producer
init|=
name|doGetProducer
argument_list|(
name|endpoint
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|producer
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|isStopped
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring exchange sent after processor is stopped: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No producer, this processor has not been started: "
operator|+
name|this
argument_list|)
throw|;
block|}
block|}
comment|// record timing for sending the exchange using the producer
specifier|final
name|StopWatch
name|watch
init|=
name|exchange
operator|!=
literal|null
condition|?
operator|new
name|StopWatch
argument_list|()
else|:
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|EventHelper
operator|.
name|notifyExchangeSending
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
comment|// invoke the callback
name|AsyncProcessor
name|asyncProcessor
init|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|producer
argument_list|)
decl_stmt|;
name|sync
operator|=
name|producerCallback
operator|.
name|doInAsyncProducer
argument_list|(
name|producer
argument_list|,
name|asyncProcessor
argument_list|,
name|exchange
argument_list|,
name|pattern
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|watch
operator|!=
literal|null
condition|)
block|{
name|long
name|timeTaken
init|=
name|watch
operator|.
name|stop
argument_list|()
decl_stmt|;
comment|// emit event that the exchange was sent to the endpoint
name|EventHelper
operator|.
name|notifyExchangeSent
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|,
name|timeTaken
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|producer
operator|instanceof
name|ServicePoolAware
condition|)
block|{
comment|// release back to the pool
name|pool
operator|.
name|release
argument_list|(
name|endpoint
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|producer
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
comment|// stop non singleton producers as we should not leak resources
try|try
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore and continue
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error stopping producer: "
operator|+
name|producer
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ensure exceptions is caught and set on the exchange
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sync
return|;
block|}
DECL|method|sendExchange (final Endpoint endpoint, ExchangePattern pattern, final Processor processor, Exchange exchange)
specifier|protected
name|Exchange
name|sendExchange
parameter_list|(
specifier|final
name|Endpoint
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
specifier|final
name|Processor
name|processor
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|doInProducer
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|pattern
argument_list|,
operator|new
name|ProducerCallback
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|Exchange
name|doInProducer
parameter_list|(
name|Producer
name|producer
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|==
literal|null
condition|)
block|{
name|exchange
operator|=
name|pattern
operator|!=
literal|null
condition|?
name|producer
operator|.
name|createExchange
argument_list|(
name|pattern
argument_list|)
else|:
name|producer
operator|.
name|createExchange
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
comment|// lets populate using the processor callback
try|try
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// populate failed so return
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
block|}
comment|// now lets dispatch
name|LOG
operator|.
name|debug
argument_list|(
literal|">>>> {} {}"
argument_list|,
name|endpoint
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// set property which endpoint we send to
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
comment|// send the exchange using the processor
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
try|try
block|{
name|EventHelper
operator|.
name|notifyExchangeSending
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
comment|// ensure we run in an unit of work
name|Producer
name|target
init|=
operator|new
name|UnitOfWorkProducer
argument_list|(
name|producer
argument_list|)
decl_stmt|;
name|target
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ensure exceptions is caught and set on the exchange
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// emit event that the exchange was sent to the endpoint
name|long
name|timeTaken
init|=
name|watch
operator|.
name|stop
argument_list|()
decl_stmt|;
name|EventHelper
operator|.
name|notifyExchangeSent
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|,
name|timeTaken
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|doGetProducer (Endpoint endpoint, boolean pooled)
specifier|protected
specifier|synchronized
name|Producer
name|doGetProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|boolean
name|pooled
parameter_list|)
block|{
name|String
name|key
init|=
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
name|Producer
name|answer
init|=
name|producers
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|pooled
operator|&&
name|answer
operator|==
literal|null
condition|)
block|{
comment|// try acquire from connection pool
name|answer
operator|=
name|pool
operator|.
name|acquire
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// create a new producer
try|try
block|{
name|answer
operator|=
name|endpoint
operator|.
name|createProducer
argument_list|()
expr_stmt|;
comment|// must then start service so producer is ready to be used
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FailedToCreateProducerException
argument_list|(
name|endpoint
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// add producer to cache or pool if applicable
if|if
condition|(
name|pooled
operator|&&
name|answer
operator|instanceof
name|ServicePoolAware
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding to producer service pool with key: {} for producer: {}"
argument_list|,
name|endpoint
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|=
name|pool
operator|.
name|addAndAcquire
argument_list|(
name|endpoint
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|answer
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding to producer cache with key: {} for producer: {}"
argument_list|,
name|endpoint
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|producers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|producers
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|pool
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// when stopping we intend to shutdown
name|ServiceHelper
operator|.
name|stopAndShutdownService
argument_list|(
name|pool
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|producers
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|producers
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns the current size of the cache      *      * @return the current size      */
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
name|int
name|size
init|=
name|producers
operator|.
name|size
argument_list|()
decl_stmt|;
name|size
operator|+=
name|pool
operator|.
name|size
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"size = {}"
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|size
return|;
block|}
comment|/**      * Gets the maximum cache size (capacity).      *<p/>      * Will return<tt>-1</tt> if it cannot determine this if a custom cache was used.      *      * @return the capacity      */
DECL|method|getCapacity ()
specifier|public
name|int
name|getCapacity
parameter_list|()
block|{
name|int
name|capacity
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|producers
operator|instanceof
name|LRUCache
condition|)
block|{
name|LRUCache
argument_list|<
name|String
argument_list|,
name|Producer
argument_list|>
name|cache
init|=
operator|(
name|LRUCache
argument_list|<
name|String
argument_list|,
name|Producer
argument_list|>
operator|)
name|producers
decl_stmt|;
name|capacity
operator|=
name|cache
operator|.
name|getMaxCacheSize
argument_list|()
expr_stmt|;
block|}
return|return
name|capacity
return|;
block|}
comment|/**      * Gets the cache hits statistic      *<p/>      * Will return<tt>-1</tt> if it cannot determine this if a custom cache was used.      *      * @return the hits      */
DECL|method|getHits ()
specifier|public
name|long
name|getHits
parameter_list|()
block|{
name|long
name|hits
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|producers
operator|instanceof
name|LRUCache
condition|)
block|{
name|LRUCache
argument_list|<
name|String
argument_list|,
name|Producer
argument_list|>
name|cache
init|=
operator|(
name|LRUCache
argument_list|<
name|String
argument_list|,
name|Producer
argument_list|>
operator|)
name|producers
decl_stmt|;
name|hits
operator|=
name|cache
operator|.
name|getHits
argument_list|()
expr_stmt|;
block|}
return|return
name|hits
return|;
block|}
comment|/**      * Gets the cache misses statistic      *<p/>      * Will return<tt>-1</tt> if it cannot determine this if a custom cache was used.      *      * @return the misses      */
DECL|method|getMisses ()
specifier|public
name|long
name|getMisses
parameter_list|()
block|{
name|long
name|misses
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|producers
operator|instanceof
name|LRUCache
condition|)
block|{
name|LRUCache
argument_list|<
name|String
argument_list|,
name|Producer
argument_list|>
name|cache
init|=
operator|(
name|LRUCache
argument_list|<
name|String
argument_list|,
name|Producer
argument_list|>
operator|)
name|producers
decl_stmt|;
name|misses
operator|=
name|cache
operator|.
name|getMisses
argument_list|()
expr_stmt|;
block|}
return|return
name|misses
return|;
block|}
comment|/**      * Resets the cache statistics      */
DECL|method|resetCacheStatistics ()
specifier|public
name|void
name|resetCacheStatistics
parameter_list|()
block|{
if|if
condition|(
name|producers
operator|instanceof
name|LRUCache
condition|)
block|{
name|LRUCache
argument_list|<
name|String
argument_list|,
name|Producer
argument_list|>
name|cache
init|=
operator|(
name|LRUCache
argument_list|<
name|String
argument_list|,
name|Producer
argument_list|>
operator|)
name|producers
decl_stmt|;
name|cache
operator|.
name|resetStatistics
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Purges this cache      */
DECL|method|purge ()
specifier|public
specifier|synchronized
name|void
name|purge
parameter_list|()
block|{
name|producers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|pool
operator|.
name|purge
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ProducerCache for source: "
operator|+
name|source
operator|+
literal|", capacity: "
operator|+
name|getCapacity
argument_list|()
return|;
block|}
block|}
end_class

end_unit

