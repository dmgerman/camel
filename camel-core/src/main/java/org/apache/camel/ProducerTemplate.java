begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Map
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
name|ExecutorService
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
name|Future
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
name|TimeUnit
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
name|TimeoutException
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
name|Synchronization
import|;
end_import

begin_comment
comment|/**  * Template for working with Camel and sending {@link Message} instances in an  * {@link Exchange} to an {@link Endpoint}.  *<br/>  *<p/><b>Important:</b> Read the javadoc of each method carefully to ensure the behavior of the method is understood.  * Some methods is for<tt>InOnly</tt>, others for<tt>InOut</tt> MEP. And some methods throws  * {@link org.apache.camel.CamelExecutionException} while others stores any thrown exception on the returned  * {@link Exchange}.  *<br/>  *<p/>The {@link ProducerTemplate} is<b>thread safe</b>.  *<br/>  *<p/>All the methods which sends a message may throw {@link FailedToCreateProducerException} in  * case the {@link Producer} could not be created. Or a {@link NoSuchEndpointException} if the endpoint could  * not be resolved. There may be other related exceptions being thrown which occurs<i>before</i> the {@link Producer}  * has started sending the message.  *<br/>  *<p/>All the sendBody or requestBody methods will return the content according to this strategy:  *<ul>  *<li>throws {@link org.apache.camel.CamelExecutionException} if processing failed<i>during</i> routing  *       with the caused exception wrapped</li>  *<li>The<tt>fault.body</tt> if there is a fault message set and its not<tt>null</tt></li>  *<li>Either<tt>IN</tt> or<tt>OUT</tt> body according to the message exchange pattern. If the pattern is  *   Out capable then the<tt>OUT</tt> body is returned, otherwise<tt>IN</tt>.  *</ul>  *<br/>  *<p/>Before using the template it must be started.  * And when you are done using the template, make sure to {@link #stop()} the template.  *<br/>  *<p/><b>Important note on usage:</b> See this  *<a href="http://camel.apache.org/why-does-camel-use-too-many-threads-with-producertemplate.html">FAQ entry</a>  * before using.  *  * @version   */
end_comment

begin_interface
DECL|interface|ProducerTemplate
specifier|public
interface|interface
name|ProducerTemplate
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
comment|/**      * Get the default endpoint to use if none is specified      *       * @return the default endpoint instance      */
DECL|method|getDefaultEndpoint ()
name|Endpoint
name|getDefaultEndpoint
parameter_list|()
function_decl|;
comment|/**      * Sets the default endpoint to use if none is specified      *       * @param defaultEndpoint the default endpoint instance      */
DECL|method|setDefaultEndpoint (Endpoint defaultEndpoint)
name|void
name|setDefaultEndpoint
parameter_list|(
name|Endpoint
name|defaultEndpoint
parameter_list|)
function_decl|;
comment|/**      * Sets the default endpoint uri to use if none is specified      *       *  @param endpointUri the default endpoint uri      */
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
comment|// Synchronous methods
comment|// -----------------------------------------------------------------------
comment|/**      * Sends the exchange to the default endpoint      *<br/><br/>      *<b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is<b>not</b> thrown from this method, but you can access it from the returned exchange using      * {@link org.apache.camel.Exchange#getException()}.      *      * @param exchange the exchange to send      * @return the returned exchange      */
DECL|method|send (Exchange exchange)
name|Exchange
name|send
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Sends an exchange to the default endpoint using a supplied processor      *<br/><br/>      *<b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is<b>not</b> thrown from this method, but you can access it from the returned exchange using      * {@link org.apache.camel.Exchange#getException()}.      *      * @param processor the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      * @return the returned exchange      */
DECL|method|send (Processor processor)
name|Exchange
name|send
parameter_list|(
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Sends the body to the default endpoint      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param body the payload to send      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBody (Object body)
name|void
name|sendBody
parameter_list|(
name|Object
name|body
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to the default endpoint with a specified header and header value      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param body the payload to send      * @param header the header name      * @param headerValue the header value      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBodyAndHeader (Object body, String header, Object headerValue)
name|void
name|sendBodyAndHeader
parameter_list|(
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to the default endpoint with a specified property and property value      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param body          the payload to send      * @param property      the property name      * @param propertyValue the property value      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBodyAndProperty (Object body, String property, Object propertyValue)
name|void
name|sendBodyAndProperty
parameter_list|(
name|Object
name|body
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to the default endpoint with the specified headers and header values      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param body the payload to send      * @param headers      the headers      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBodyAndHeaders (Object body, Map<String, Object> headers)
name|void
name|sendBodyAndHeaders
parameter_list|(
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|// Allow sending to arbitrary endpoints
comment|// -----------------------------------------------------------------------
comment|/**      * Sends the exchange to the given endpoint      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is<b>not</b> thrown from this method, but you can access it from the returned exchange using      * {@link org.apache.camel.Exchange#getException()}.      *      * @param endpointUri the endpoint URI to send the exchange to      * @param exchange    the exchange to send      * @return the returned exchange      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|send (String endpointUri, Exchange exchange)
name|Exchange
name|send
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Sends an exchange to an endpoint using a supplied processor      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is<b>not</b> thrown from this method, but you can access it from the returned exchange using      * {@link org.apache.camel.Exchange#getException()}.      *      * @param endpointUri the endpoint URI to send the exchange to      * @param processor   the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      * @return the returned exchange      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|send (String endpointUri, Processor processor)
name|Exchange
name|send
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Sends an exchange to an endpoint using a supplied processor      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is<b>not</b> thrown from this method, but you can access it from the returned exchange using      * {@link org.apache.camel.Exchange#getException()}.      *      * @param endpointUri the endpoint URI to send the exchange to      * @param pattern     the message {@link ExchangePattern} such as      *                    {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param processor   the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      * @return the returned exchange      */
DECL|method|send (String endpointUri, ExchangePattern pattern, Processor processor)
name|Exchange
name|send
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Sends the exchange to the given endpoint      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is<b>not</b> thrown from this method, but you can access it from the returned exchange using      * {@link org.apache.camel.Exchange#getException()}.      *      * @param endpoint the endpoint to send the exchange to      * @param exchange the exchange to send      * @return the returned exchange      */
DECL|method|send (Endpoint endpoint, Exchange exchange)
name|Exchange
name|send
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Sends an exchange to an endpoint using a supplied processor      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is<b>not</b> thrown from this method, but you can access it from the returned exchange using      * {@link org.apache.camel.Exchange#getException()}.      *      * @param endpoint  the endpoint to send the exchange to      * @param processor the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      * @return the returned exchange      */
DECL|method|send (Endpoint endpoint, Processor processor)
name|Exchange
name|send
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Sends an exchange to an endpoint using a supplied processor      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is<b>not</b> thrown from this method, but you can access it from the returned exchange using      * {@link org.apache.camel.Exchange#getException()}.      *      * @param endpoint  the endpoint to send the exchange to      * @param pattern   the message {@link ExchangePattern} such as      *                  {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param processor the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      * @return the returned exchange      */
DECL|method|send (Endpoint endpoint, ExchangePattern pattern, Processor processor)
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
function_decl|;
comment|/**      * Send the body to an endpoint      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpoint   the endpoint to send the exchange to      * @param body       the payload      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBody (Endpoint endpoint, Object body)
name|void
name|sendBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Send the body to an endpoint      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpointUri   the endpoint URI to send the exchange to      * @param body          the payload      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBody (String endpointUri, Object body)
name|void
name|sendBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Send the body to an endpoint with the given {@link ExchangePattern}      * returning any result output body      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpoint      the endpoint to send the exchange to      * @param body          the payload      * @param pattern       the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @return the result if {@link ExchangePattern} is OUT capable, otherwise<tt>null</tt>      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBody (Endpoint endpoint, ExchangePattern pattern, Object body)
name|Object
name|sendBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpointUri   the endpoint URI to send the exchange to      * @param pattern       the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body          the payload      * @return the result if {@link ExchangePattern} is OUT capable, otherwise<tt>null</tt>      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBody (String endpointUri, ExchangePattern pattern, Object body)
name|Object
name|sendBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to an endpoint with a specified header and header value      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpointUri the endpoint URI to send to      * @param body the payload to send      * @param header the header name      * @param headerValue the header value      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBodyAndHeader (String endpointUri, Object body, String header, Object headerValue)
name|void
name|sendBodyAndHeader
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to an endpoint with a specified header and header value      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpoint the Endpoint to send to      * @param body the payload to send      * @param header the header name      * @param headerValue the header value      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBodyAndHeader (Endpoint endpoint, Object body, String header, Object headerValue)
name|void
name|sendBodyAndHeader
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to an endpoint with a specified header and header value      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpoint the Endpoint to send to      * @param pattern the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body the payload to send      * @param header the header name      * @param headerValue the header value      * @return the result if {@link ExchangePattern} is OUT capable, otherwise<tt>null</tt>      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBodyAndHeader (Endpoint endpoint, ExchangePattern pattern, Object body, String header, Object headerValue)
name|Object
name|sendBodyAndHeader
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to an endpoint with a specified header and header value      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpoint the Endpoint URI to send to      * @param pattern the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body the payload to send      * @param header the header name      * @param headerValue the header value      * @return the result if {@link ExchangePattern} is OUT capable, otherwise<tt>null</tt>      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBodyAndHeader (String endpoint, ExchangePattern pattern, Object body, String header, Object headerValue)
name|Object
name|sendBodyAndHeader
parameter_list|(
name|String
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to an endpoint with a specified property and property value      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpointUri the endpoint URI to send to      * @param body the payload to send      * @param property the property name      * @param propertyValue the property value      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBodyAndProperty (String endpointUri, Object body, String property, Object propertyValue)
name|void
name|sendBodyAndProperty
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to an endpoint with a specified property and property value      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpoint the Endpoint to send to      * @param body the payload to send      * @param property the property name      * @param propertyValue the property value      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBodyAndProperty (Endpoint endpoint, Object body, String property, Object propertyValue)
name|void
name|sendBodyAndProperty
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to an endpoint with a specified property and property value      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpoint the Endpoint to send to      * @param pattern the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body the payload to send      * @param property the property name      * @param propertyValue the property value      * @return the result if {@link ExchangePattern} is OUT capable, otherwise<tt>null</tt>      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBodyAndProperty (Endpoint endpoint, ExchangePattern pattern, Object body, String property, Object propertyValue)
name|Object
name|sendBodyAndProperty
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to an endpoint with a specified property and property value      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpoint the Endpoint URI to send to      * @param pattern the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body the payload to send      * @param property the property name      * @param propertyValue the property value      * @return the result if {@link ExchangePattern} is OUT capable, otherwise<tt>null</tt>      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBodyAndProperty (String endpoint, ExchangePattern pattern, Object body, String property, Object propertyValue)
name|Object
name|sendBodyAndProperty
parameter_list|(
name|String
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to an endpoint with the specified headers and header values      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpointUri the endpoint URI to send to      * @param body the payload to send      * @param headers headers      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers)
name|void
name|sendBodyAndHeaders
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to an endpoint with the specified headers and header values      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpoint the endpoint URI to send to      * @param body the payload to send      * @param headers headers      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBodyAndHeaders (Endpoint endpoint, Object body, Map<String, Object> headers)
name|void
name|sendBodyAndHeaders
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to an endpoint with the specified headers and header values      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpointUri the endpoint URI to send to      * @param pattern the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body the payload to send      * @param headers headers      * @return the result if {@link ExchangePattern} is OUT capable, otherwise<tt>null</tt>      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBodyAndHeaders (String endpointUri, ExchangePattern pattern, Object body, Map<String, Object> headers)
name|Object
name|sendBodyAndHeaders
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to an endpoint with the specified headers and header values      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpoint the endpoint URI to send to      * @param pattern the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body the payload to send      * @param headers headers      * @return the result if {@link ExchangePattern} is OUT capable, otherwise<tt>null</tt>      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|sendBodyAndHeaders (Endpoint endpoint, ExchangePattern pattern, Object body, Map<String, Object> headers)
name|Object
name|sendBodyAndHeaders
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|// Methods using an InOut ExchangePattern
comment|// -----------------------------------------------------------------------
comment|/**      * Sends an exchange to an endpoint using a supplied processor      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is<b>not</b> thrown from this method, but you can access it from the returned exchange using      * {@link org.apache.camel.Exchange#getException()}.      *      * @param endpoint  the Endpoint to send to      * @param processor the processor which will populate the exchange before sending      * @return the result (see class javadoc)      */
DECL|method|request (Endpoint endpoint, Processor processor)
name|Exchange
name|request
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Sends an exchange to an endpoint using a supplied processor      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is<b>not</b> thrown from this method, but you can access it from the returned exchange using      * {@link org.apache.camel.Exchange#getException()}.      *      * @param endpointUri the endpoint URI to send to      * @param processor the processor which will populate the exchange before sending      * @return the result (see class javadoc)      */
DECL|method|request (String endpointUri, Processor processor)
name|Exchange
name|request
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Sends the body to the default endpoint and returns the result content      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param body the payload to send      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|requestBody (Object body)
name|Object
name|requestBody
parameter_list|(
name|Object
name|body
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to the default endpoint and returns the result content      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param body the payload to send      * @param type the expected response type      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|requestBody (Object body, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBody
parameter_list|(
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpoint the Endpoint to send to      * @param body     the payload      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|requestBody (Endpoint endpoint, Object body)
name|Object
name|requestBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpoint the Endpoint to send to      * @param body     the payload      * @param type     the expected response type      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|requestBody (Endpoint endpoint, Object body, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpointUri the endpoint URI to send to      * @param body        the payload      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|requestBody (String endpointUri, Object body)
name|Object
name|requestBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpointUri the endpoint URI to send to      * @param body        the payload      * @param type        the expected response type      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|requestBody (String endpointUri, Object body, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to the default endpoint and returns the result content      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param body        the payload      * @param header      the header name      * @param headerValue the header value      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|requestBodyAndHeader (Object body, String header, Object headerValue)
name|Object
name|requestBodyAndHeader
parameter_list|(
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpoint    the Endpoint to send to      * @param body        the payload      * @param header      the header name      * @param headerValue the header value      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|requestBodyAndHeader (Endpoint endpoint, Object body, String header, Object headerValue)
name|Object
name|requestBodyAndHeader
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpoint    the Endpoint to send to      * @param body        the payload      * @param header      the header name      * @param headerValue the header value      * @param type        the expected response type      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|requestBodyAndHeader (Endpoint endpoint, Object body, String header, Object headerValue, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBodyAndHeader
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpointUri the endpoint URI to send to      * @param body        the payload      * @param header      the header name      * @param headerValue the header value      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|requestBodyAndHeader (String endpointUri, Object body, String header, Object headerValue)
name|Object
name|requestBodyAndHeader
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpointUri the endpoint URI to send to      * @param body        the payload      * @param header      the header name      * @param headerValue the header value      * @param type        the expected response type      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|requestBodyAndHeader (String endpointUri, Object body, String header, Object headerValue, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBodyAndHeader
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to an endpoint with the specified headers and header values.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpointUri the endpoint URI to send to      * @param body the payload to send      * @param headers headers      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|requestBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers)
name|Object
name|requestBodyAndHeaders
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to an endpoint with the specified headers and header values.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpointUri the endpoint URI to send to      * @param body the payload to send      * @param headers headers      * @param type the expected response type      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|requestBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBodyAndHeaders
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to an endpoint with the specified headers and header values.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpoint the endpoint URI to send to      * @param body the payload to send      * @param headers headers      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|requestBodyAndHeaders (Endpoint endpoint, Object body, Map<String, Object> headers)
name|Object
name|requestBodyAndHeaders
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to the default endpoint and returns the result content      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param body the payload to send      * @param headers headers      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|requestBodyAndHeaders (Object body, Map<String, Object> headers)
name|Object
name|requestBodyAndHeaders
parameter_list|(
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Sends the body to an endpoint with the specified headers and header values.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *<br/><br/>      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param endpoint the endpoint URI to send to      * @param body the payload to send      * @param headers headers      * @param type the expected response type      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|requestBodyAndHeaders (Endpoint endpoint, Object body, Map<String, Object> headers, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBodyAndHeaders
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|// Asynchronous methods
comment|// -----------------------------------------------------------------------
comment|/**      * Sets a custom executor service to use for async messaging.      *      * @param executorService  the executor service.      */
DECL|method|setExecutorService (ExecutorService executorService)
name|void
name|setExecutorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous exchange to the given endpoint.      *      * @param endpointUri the endpoint URI to send the exchange to      * @param exchange    the exchange to send      * @return a handle to be used to get the response in the future      */
DECL|method|asyncSend (String endpointUri, Exchange exchange)
name|Future
argument_list|<
name|Exchange
argument_list|>
name|asyncSend
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous exchange to the given endpoint.      *      * @param endpointUri the endpoint URI to send the exchange to      * @param processor   the transformer used to populate the new exchange      * @return a handle to be used to get the response in the future      */
DECL|method|asyncSend (String endpointUri, Processor processor)
name|Future
argument_list|<
name|Exchange
argument_list|>
name|asyncSend
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOnly} message exchange pattern.      *      * @param endpointUri the endpoint URI to send the exchange to      * @param body        the body to send      * @return a handle to be used to get the response in the future      */
DECL|method|asyncSendBody (String endpointUri, Object body)
name|Future
argument_list|<
name|Object
argument_list|>
name|asyncSendBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpointUri the endpoint URI to send the exchange to      * @param body        the body to send      * @return a handle to be used to get the response in the future      */
DECL|method|asyncRequestBody (String endpointUri, Object body)
name|Future
argument_list|<
name|Object
argument_list|>
name|asyncRequestBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpointUri the endpoint URI to send the exchange to      * @param body        the body to send      * @param header      the header name      * @param headerValue the header value      * @return a handle to be used to get the response in the future      */
DECL|method|asyncRequestBodyAndHeader (String endpointUri, Object body, String header, Object headerValue)
name|Future
argument_list|<
name|Object
argument_list|>
name|asyncRequestBodyAndHeader
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpointUri the endpoint URI to send the exchange to      * @param body        the body to send      * @param headers     headers      * @return a handle to be used to get the response in the future      */
DECL|method|asyncRequestBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers)
name|Future
argument_list|<
name|Object
argument_list|>
name|asyncRequestBodyAndHeaders
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpointUri the endpoint URI to send the exchange to      * @param body        the body to send      * @param type        the expected response type      * @return a handle to be used to get the response in the future      */
DECL|method|asyncRequestBody (String endpointUri, Object body, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Future
argument_list|<
name|T
argument_list|>
name|asyncRequestBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpointUri the endpoint URI to send the exchange to      * @param body        the body to send      * @param header      the header name      * @param headerValue the header value      * @param type        the expected response type      * @return a handle to be used to get the response in the future      */
DECL|method|asyncRequestBodyAndHeader (String endpointUri, Object body, String header, Object headerValue, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Future
argument_list|<
name|T
argument_list|>
name|asyncRequestBodyAndHeader
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpointUri the endpoint URI to send the exchange to      * @param body        the body to send      * @param headers     headers      * @param type        the expected response type      * @return a handle to be used to get the response in the future      */
DECL|method|asyncRequestBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Future
argument_list|<
name|T
argument_list|>
name|asyncRequestBodyAndHeaders
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous exchange to the given endpoint.      *      * @param endpoint    the endpoint to send the exchange to      * @param exchange    the exchange to send      * @return a handle to be used to get the response in the future      */
DECL|method|asyncSend (Endpoint endpoint, Exchange exchange)
name|Future
argument_list|<
name|Exchange
argument_list|>
name|asyncSend
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous exchange to the given endpoint.      *      * @param endpoint    the endpoint to send the exchange to      * @param processor   the transformer used to populate the new exchange      * @return a handle to be used to get the response in the future      */
DECL|method|asyncSend (Endpoint endpoint, Processor processor)
name|Future
argument_list|<
name|Exchange
argument_list|>
name|asyncSend
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOnly} message exchange pattern.      *      * @param endpoint    the endpoint to send the exchange to      * @param body        the body to send      * @return a handle to be used to get the response in the future      */
DECL|method|asyncSendBody (Endpoint endpoint, Object body)
name|Future
argument_list|<
name|Object
argument_list|>
name|asyncSendBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpoint    the endpoint to send the exchange to      * @param body        the body to send      * @return a handle to be used to get the response in the future      */
DECL|method|asyncRequestBody (Endpoint endpoint, Object body)
name|Future
argument_list|<
name|Object
argument_list|>
name|asyncRequestBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpoint the endpoint to send the exchange to      * @param body        the body to send      * @param header      the header name      * @param headerValue the header value      * @return a handle to be used to get the response in the future      */
DECL|method|asyncRequestBodyAndHeader (Endpoint endpoint, Object body, String header, Object headerValue)
name|Future
argument_list|<
name|Object
argument_list|>
name|asyncRequestBodyAndHeader
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpoint    the endpoint to send the exchange to      * @param body        the body to send      * @param headers     headers      * @return a handle to be used to get the response in the future      */
DECL|method|asyncRequestBodyAndHeaders (Endpoint endpoint, Object body, Map<String, Object> headers)
name|Future
argument_list|<
name|Object
argument_list|>
name|asyncRequestBodyAndHeaders
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpoint    the endpoint to send the exchange to      * @param body        the body to send      * @param type        the expected response type      * @return a handle to be used to get the response in the future      */
DECL|method|asyncRequestBody (Endpoint endpoint, Object body, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Future
argument_list|<
name|T
argument_list|>
name|asyncRequestBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpoint    the endpoint to send the exchange to      * @param body        the body to send      * @param header      the header name      * @param headerValue the header value      * @param type        the expected response type      * @return a handle to be used to get the response in the future      */
DECL|method|asyncRequestBodyAndHeader (Endpoint endpoint, Object body, String header, Object headerValue, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Future
argument_list|<
name|T
argument_list|>
name|asyncRequestBodyAndHeader
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|String
name|header
parameter_list|,
name|Object
name|headerValue
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpoint    the endpoint to send the exchange to      * @param body        the body to send      * @param headers     headers      * @param type        the expected response type      * @return a handle to be used to get the response in the future      */
DECL|method|asyncRequestBodyAndHeaders (Endpoint endpoint, Object body, Map<String, Object> headers, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Future
argument_list|<
name|T
argument_list|>
name|asyncRequestBodyAndHeaders
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Gets the response body from the future handle, will wait until the response is ready.      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param future      the handle to get the response      * @param type        the expected response type      * @return the result (see class javadoc)      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|extractFutureBody (Future<Object> future, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|extractFutureBody
parameter_list|(
name|Future
argument_list|<
name|Object
argument_list|>
name|future
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|CamelExecutionException
function_decl|;
comment|/**      * Gets the response body from the future handle, will wait at most the given time for the response to be ready.      *<p/><b>Notice:</b> that if the processing of the exchange failed with an Exception      * it is thrown from this method as a {@link org.apache.camel.CamelExecutionException} with      * the caused exception wrapped.      *      * @param future      the handle to get the response      * @param timeout     the maximum time to wait      * @param unit        the time unit of the timeout argument      * @param type        the expected response type      * @return the result (see class javadoc)      * @throws java.util.concurrent.TimeoutException if the wait timed out      * @throws CamelExecutionException if the processing of the exchange failed      */
DECL|method|extractFutureBody (Future<Object> future, long timeout, TimeUnit unit, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|extractFutureBody
parameter_list|(
name|Future
argument_list|<
name|Object
argument_list|>
name|future
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|TimeoutException
throws|,
name|CamelExecutionException
function_decl|;
comment|// Asynchronous methods with callback
comment|// -----------------------------------------------------------------------
comment|/**      * Sends an asynchronous exchange to the given endpoint.      *      * @param endpointUri   the endpoint URI to send the exchange to      * @param exchange      the exchange to send      * @param onCompletion  callback invoked when exchange has been completed      * @return a handle to be used to get the response in the future      */
DECL|method|asyncCallback (String endpointUri, Exchange exchange, Synchronization onCompletion)
name|Future
argument_list|<
name|Exchange
argument_list|>
name|asyncCallback
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Synchronization
name|onCompletion
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous exchange to the given endpoint.      *      * @param endpoint      the endpoint to send the exchange to      * @param exchange      the exchange to send      * @param onCompletion  callback invoked when exchange has been completed      * @return a handle to be used to get the response in the future      */
DECL|method|asyncCallback (Endpoint endpoint, Exchange exchange, Synchronization onCompletion)
name|Future
argument_list|<
name|Exchange
argument_list|>
name|asyncCallback
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Synchronization
name|onCompletion
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous exchange to the given endpoint using a supplied processor.      *      * @param endpointUri   the endpoint URI to send the exchange to      * @param processor     the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      * @param onCompletion  callback invoked when exchange has been completed      * @return a handle to be used to get the response in the future      */
DECL|method|asyncCallback (String endpointUri, Processor processor, Synchronization onCompletion)
name|Future
argument_list|<
name|Exchange
argument_list|>
name|asyncCallback
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Synchronization
name|onCompletion
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous exchange to the given endpoint using a supplied processor.      *      * @param endpoint      the endpoint to send the exchange to      * @param processor     the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      * @param onCompletion  callback invoked when exchange has been completed      * @return a handle to be used to get the response in the future      */
DECL|method|asyncCallback (Endpoint endpoint, Processor processor, Synchronization onCompletion)
name|Future
argument_list|<
name|Exchange
argument_list|>
name|asyncCallback
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Synchronization
name|onCompletion
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOnly} message exchange pattern.      *      * @param endpointUri   the endpoint URI to send the exchange to      * @param body          the body to send      * @param onCompletion  callback invoked when exchange has been completed      * @return a handle to be used to get the response in the future      */
DECL|method|asyncCallbackSendBody (String endpointUri, Object body, Synchronization onCompletion)
name|Future
argument_list|<
name|Object
argument_list|>
name|asyncCallbackSendBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Synchronization
name|onCompletion
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOnly} message exchange pattern.      *      * @param endpoint      the endpoint to send the exchange to      * @param body          the body to send      * @param onCompletion  callback invoked when exchange has been completed      * @return a handle to be used to get the response in the future      */
DECL|method|asyncCallbackSendBody (Endpoint endpoint, Object body, Synchronization onCompletion)
name|Future
argument_list|<
name|Object
argument_list|>
name|asyncCallbackSendBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|Synchronization
name|onCompletion
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpointUri   the endpoint URI to send the exchange to      * @param body          the body to send      * @param onCompletion  callback invoked when exchange has been completed      * @return a handle to be used to get the response in the future      */
DECL|method|asyncCallbackRequestBody (String endpointUri, Object body, Synchronization onCompletion)
name|Future
argument_list|<
name|Object
argument_list|>
name|asyncCallbackRequestBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Synchronization
name|onCompletion
parameter_list|)
function_decl|;
comment|/**      * Sends an asynchronous body to the given endpoint.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpoint      the endpoint to send the exchange to      * @param body          the body to send      * @param onCompletion  callback invoked when exchange has been completed      * @return a handle to be used to get the response in the future      */
DECL|method|asyncCallbackRequestBody (Endpoint endpoint, Object body, Synchronization onCompletion)
name|Future
argument_list|<
name|Object
argument_list|>
name|asyncCallbackRequestBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|,
name|Synchronization
name|onCompletion
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

