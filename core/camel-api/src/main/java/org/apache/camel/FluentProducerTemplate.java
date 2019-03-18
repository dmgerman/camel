begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|Future
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
import|;
end_import

begin_comment
comment|/**  * Template for working with Camel and sending {@link Message} instances in an  * {@link Exchange} to an {@link Endpoint} using a<i>fluent</i> build style.  *<br/>  *<p/><b>Important:</b> Read the javadoc of each method carefully to ensure the behavior of the method is understood.  * Some methods is for<tt>InOnly</tt>, others for<tt>InOut</tt> MEP. And some methods throws  * {@link org.apache.camel.CamelExecutionException} while others stores any thrown exception on the returned  * {@link Exchange}.  *<br/>  *<p/>The {@link FluentProducerTemplate} is<b>thread safe</b>.  *<br/>  *<p/>All the methods which sends a message may throw {@link FailedToCreateProducerException} in  * case the {@link Producer} could not be created. Or a {@link NoSuchEndpointException} if the endpoint could  * not be resolved. There may be other related exceptions being thrown which occurs<i>before</i> the {@link Producer}  * has started sending the message.  *<br/>  *<p/>All the send or request methods will return the content according to this strategy:  *<ul>  *<li>throws {@link org.apache.camel.CamelExecutionException} if processing failed<i>during</i> routing  *       with the caused exception wrapped</li>  *<li>The<tt>fault.body</tt> if there is a fault message set and its not<tt>null</tt></li>  *<li>Either<tt>IN</tt> or<tt>OUT</tt> body according to the message exchange pattern. If the pattern is  *   Out capable then the<tt>OUT</tt> body is returned, otherwise<tt>IN</tt>.  *</ul>  *<br/>  *<p/>Before using the template it must be started.  * And when you are done using the template, make sure to {@link #stop()} the template.  *<br/>  *<p/><b>Important note on usage:</b> See this  *<a href="http://camel.apache.org/why-does-camel-use-too-many-threads-with-producertemplate.html">FAQ entry</a>  * before using.  *  * @see ProducerTemplate  * @see ConsumerTemplate  */
end_comment

begin_interface
DECL|interface|FluentProducerTemplate
specifier|public
interface|interface
name|FluentProducerTemplate
extends|extends
name|Service
block|{
comment|/**      * Get the {@link CamelContext}      *      * @return camelContext the Camel context      */
DECL|method|getCamelContext ()
name|CamelContext
name|getCamelContext
parameter_list|()
function_decl|;
comment|// Configuration methods
comment|// -----------------------------------------------------------------------
comment|/**      * Gets the maximum cache size used in the backing cache pools.      *      * @return the maximum cache size      */
DECL|method|getMaximumCacheSize ()
name|int
name|getMaximumCacheSize
parameter_list|()
function_decl|;
comment|/**      * Sets a custom maximum cache size to use in the backing cache pools.      *      * @param maximumCacheSize the custom maximum cache size      */
DECL|method|setMaximumCacheSize (int maximumCacheSize)
name|void
name|setMaximumCacheSize
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
function_decl|;
comment|/**      * Gets an approximated size of the current cached resources in the backing cache pools.      *      * @return the size of current cached resources      */
DECL|method|getCurrentCacheSize ()
name|int
name|getCurrentCacheSize
parameter_list|()
function_decl|;
comment|/**      * Get the default endpoint to use if none is specified      *      * @return the default endpoint instance      */
DECL|method|getDefaultEndpoint ()
name|Endpoint
name|getDefaultEndpoint
parameter_list|()
function_decl|;
comment|/**      * Sets the default endpoint to use if none is specified      *      * @param defaultEndpoint the default endpoint instance      */
DECL|method|setDefaultEndpoint (Endpoint defaultEndpoint)
name|void
name|setDefaultEndpoint
parameter_list|(
name|Endpoint
name|defaultEndpoint
parameter_list|)
function_decl|;
comment|/**      * Sets the default endpoint uri to use if none is specified      *      *  @param endpointUri the default endpoint uri      */
DECL|method|setDefaultEndpointUri (String endpointUri)
name|void
name|setDefaultEndpointUri
parameter_list|(
name|String
name|endpointUri
parameter_list|)
function_decl|;
comment|/**      * Sets whether the {@link org.apache.camel.spi.EventNotifier} should be      * used by this {@link ProducerTemplate} to send events about the {@link Exchange}      * being sent.      *<p/>      * By default this is enabled.      *      * @param enabled<tt>true</tt> to enable,<tt>false</tt> to disable.      */
DECL|method|setEventNotifierEnabled (boolean enabled)
name|void
name|setEventNotifierEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
function_decl|;
comment|/**      * Whether the {@link org.apache.camel.spi.EventNotifier} should be      * used by this {@link ProducerTemplate} to send events about the {@link Exchange}      * being sent.      *      * @return<tt>true</tt> if enabled,<tt>false</tt> otherwise      */
DECL|method|isEventNotifierEnabled ()
name|boolean
name|isEventNotifierEnabled
parameter_list|()
function_decl|;
comment|/**      * Cleanup the cache (purging stale entries)      */
DECL|method|cleanUp ()
name|void
name|cleanUp
parameter_list|()
function_decl|;
comment|// Fluent methods
comment|// -----------------------------------------------------------------------
comment|/**      * Remove the body and headers.      */
DECL|method|clearAll ()
name|FluentProducerTemplate
name|clearAll
parameter_list|()
function_decl|;
comment|/**      * Set the header      *      * @param key the key of the header      * @param value the value of the header      */
DECL|method|withHeader (String key, Object value)
name|FluentProducerTemplate
name|withHeader
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**      * Remove the headers.      */
DECL|method|clearHeaders ()
name|FluentProducerTemplate
name|clearHeaders
parameter_list|()
function_decl|;
comment|/**      * Set the message body      *      * @param body the body      */
DECL|method|withBody (Object body)
name|FluentProducerTemplate
name|withBody
parameter_list|(
name|Object
name|body
parameter_list|)
function_decl|;
comment|/**      * Set the message body after converting it to the given type      *      * @param body the body      * @param type the type which the body should be converted to      */
DECL|method|withBodyAs (Object body, Class<?> type)
name|FluentProducerTemplate
name|withBodyAs
parameter_list|(
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Remove the body.      */
DECL|method|clearBody ()
name|FluentProducerTemplate
name|clearBody
parameter_list|()
function_decl|;
comment|/**      * To customize the producer template for advanced usage like to set the      * executor service to use.      *      *<pre>      * {@code      * FluentProducerTemplate.on(context)      *     .withTemplateCustomizer(      *         template -> {      *             template.setExecutorService(myExecutor);      *             template.setMaximumCacheSize(10);      *         }      *      )      *     .withBody("the body")      *     .to("direct:start")      *     .request()}      *</pre>      *      * Note that it is invoked only once.      *      * @param templateCustomizer the customizer      */
DECL|method|withTemplateCustomizer (java.util.function.Consumer<ProducerTemplate> templateCustomizer)
name|FluentProducerTemplate
name|withTemplateCustomizer
parameter_list|(
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
argument_list|<
name|ProducerTemplate
argument_list|>
name|templateCustomizer
parameter_list|)
function_decl|;
comment|/**      * Set the exchange to use for send.      *      * When using withExchange then you must use the send method (request is not supported).      *      * @param exchange the exchange      */
DECL|method|withExchange (Exchange exchange)
name|FluentProducerTemplate
name|withExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Set the exchangeSupplier which will be invoke to get the exchange to be      * used for send.      *      * When using withExchange then you must use the send method (request is not supported).      *      * @param exchangeSupplier the supplier      */
DECL|method|withExchange (Supplier<Exchange> exchangeSupplier)
name|FluentProducerTemplate
name|withExchange
parameter_list|(
name|Supplier
argument_list|<
name|Exchange
argument_list|>
name|exchangeSupplier
parameter_list|)
function_decl|;
comment|/**      * Set the processor to use for send/request.      *      *<pre>      * {@code      * FluentProducerTemplate.on(context)      *     .withProcessor(      *         exchange -> {      *             exchange.getIn().setHeader("Key1", "Val1");      *             exchange.getIn().setHeader("Key2", "Val2");      *             exchange.getIn().setBody("the body");      *         }      *      )      *     .to("direct:start")      *     .request()}      *</pre>      *      * @param processor       */
DECL|method|withProcessor (Processor processor)
name|FluentProducerTemplate
name|withProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Set the processorSupplier which will be invoke to get the processor to be      * used for send/request.      *      * @param processorSupplier the supplier      */
DECL|method|withProcessor (Supplier<Processor> processorSupplier)
name|FluentProducerTemplate
name|withProcessor
parameter_list|(
name|Supplier
argument_list|<
name|Processor
argument_list|>
name|processorSupplier
parameter_list|)
function_decl|;
comment|/**      * Endpoint to send to      *      * @param endpointUri the endpoint URI to send to      */
DECL|method|to (String endpointUri)
name|FluentProducerTemplate
name|to
parameter_list|(
name|String
name|endpointUri
parameter_list|)
function_decl|;
comment|/**      * Endpoint to send to      *      * @param endpoint the endpoint to send to      */
DECL|method|to (Endpoint endpoint)
name|FluentProducerTemplate
name|to
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Send to an endpoint (InOut) returning any result output body.      *      * @return the result      * @throws CamelExecutionException is thrown if error occurred      */
DECL|method|request ()
name|Object
name|request
parameter_list|()
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Send to an endpoint (InOut).      *      * @param type the expected response type      * @return the result      * @throws CamelExecutionException is thrown if error occurred      */
DECL|method|request (Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|request
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends asynchronously to the given endpoint (InOut).      *      * @return a handle to be used to get the response in the future      */
DECL|method|asyncRequest ()
name|Future
argument_list|<
name|Object
argument_list|>
name|asyncRequest
parameter_list|()
function_decl|;
comment|/**      * Sends asynchronously to the given endpoint (InOut).      *      * @param type the expected response type      * @return a handle to be used to get the response in the future      */
DECL|method|asyncRequest (Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Future
argument_list|<
name|T
argument_list|>
name|asyncRequest
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Send to an endpoint (InOnly)      *      * @throws CamelExecutionException is thrown if error occurred      */
DECL|method|send ()
name|Exchange
name|send
parameter_list|()
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends asynchronously to the given endpoint (InOnly).      *      * @return a handle to be used to get the response in the future      */
DECL|method|asyncSend ()
name|Future
argument_list|<
name|Exchange
argument_list|>
name|asyncSend
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

