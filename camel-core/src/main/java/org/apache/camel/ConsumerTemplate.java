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

begin_comment
comment|/**  * Template (named like Spring's TransactionTemplate& JmsTemplate  * et al) for working with Camel and consuming {@link Message} instances in an  * {@link Exchange} from an {@link Endpoint}.  *<p/>  * This template is an implementation of the  *<a href="http://camel.apache.org/polling-consumer.html">Polling Consumer EIP</a>.  * This is<b>not</b> the<a href="http://camel.apache.org/event-driven-consumer.html">Event Driven Consumer EIP</a>.  *<p/>  *<b>All</b> methods throws {@link RuntimeCamelException} if consuming of  * the {@link Exchange} failed and an Exception occurred. The<tt>getCause</tt>  * method on {@link RuntimeCamelException} returns the wrapper original caused  * exception.  *<p/>  * All the receive<b>Body</b> methods will return the content according to this strategy  *<ul>  *<li>throws {@link RuntimeCamelException} as stated above</li>  *<li>The<tt>fault.body</tt> if there is a fault message set and its not<tt>null</tt></li>  *<li>The<tt>out.body</tt> if there is a out message set and its not<tt>null</tt></li>  *<li>The<tt>in.body</tt></li>  *</ul>  *<p/>  *<br/>  * Before using the template it must be started.  * And when you are done using the template, make sure to {@link #stop()} the template.  *<br/>  *<p/><b>Important note on usage:</b> See this  *<a href="http://camel.apache.org/why-does-camel-use-too-many-threads-with-producertemplate.html">FAQ entry</a>  * before using, it applies to this {@link ConsumerTemplate} as well.  *  * @version   */
end_comment

begin_interface
DECL|interface|ConsumerTemplate
specifier|public
interface|interface
name|ConsumerTemplate
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
comment|/**      * Gets the maximum cache size used.      *      * @return the maximum cache size      */
DECL|method|getMaximumCacheSize ()
name|int
name|getMaximumCacheSize
parameter_list|()
function_decl|;
comment|/**      * Sets a custom maximum cache size.      *      * @param maximumCacheSize the custom maximum cache size      */
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
comment|// Synchronous methods
comment|// -----------------------------------------------------------------------
comment|/**      * Receives from the endpoint, waiting until there is a response      *<p/>      *<b>Important:</b> See {@link #doneUoW(Exchange)}      *      * @param endpointUri the endpoint to receive from      * @return the returned exchange      */
DECL|method|receive (String endpointUri)
name|Exchange
name|receive
parameter_list|(
name|String
name|endpointUri
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, waiting until there is a response.      *<p/>      *<b>Important:</b> See {@link #doneUoW(Exchange)}      *      * @param endpoint the endpoint to receive from      * @return the returned exchange      * @see #doneUoW(Exchange)      */
DECL|method|receive (Endpoint endpoint)
name|Exchange
name|receive
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, waiting until there is a response      * or the timeout occurs      *<p/>      *<b>Important:</b> See {@link #doneUoW(Exchange)}      *      * @param endpointUri the endpoint to receive from      * @param timeout     timeout in millis to wait for a response      * @return the returned exchange, or<tt>null</tt> if no response      * @see #doneUoW(Exchange)      */
DECL|method|receive (String endpointUri, long timeout)
name|Exchange
name|receive
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|long
name|timeout
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, waiting until there is a response      * or the timeout occurs      *<p/>      *<b>Important:</b> See {@link #doneUoW(Exchange)}      *      * @param endpoint the endpoint to receive from      * @param timeout  timeout in millis to wait for a response      * @return the returned exchange, or<tt>null</tt> if no response      * @see #doneUoW(Exchange)      */
DECL|method|receive (Endpoint endpoint, long timeout)
name|Exchange
name|receive
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|long
name|timeout
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, not waiting for a response if non exists.      *<p/>      *<b>Important:</b> See {@link #doneUoW(Exchange)}      *      * @param endpointUri the endpoint to receive from      * @return the returned exchange, or<tt>null</tt> if no response      */
DECL|method|receiveNoWait (String endpointUri)
name|Exchange
name|receiveNoWait
parameter_list|(
name|String
name|endpointUri
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, not waiting for a response if non exists.      *<p/>      *<b>Important:</b> See {@link #doneUoW(Exchange)}      *      * @param endpoint the endpoint to receive from      * @return the returned exchange, or<tt>null</tt> if no response      */
DECL|method|receiveNoWait (Endpoint endpoint)
name|Exchange
name|receiveNoWait
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, waiting until there is a response      *      * @param endpointUri the endpoint to receive from      * @return the returned response body      */
DECL|method|receiveBody (String endpointUri)
name|Object
name|receiveBody
parameter_list|(
name|String
name|endpointUri
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, waiting until there is a response      *      * @param endpoint the endpoint to receive from      * @return the returned response body      */
DECL|method|receiveBody (Endpoint endpoint)
name|Object
name|receiveBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, waiting until there is a response      * or the timeout occurs      *      * @param endpointUri the endpoint to receive from      * @param timeout     timeout in millis to wait for a response      * @return the returned response body, or<tt>null</tt> if no response      */
DECL|method|receiveBody (String endpointUri, long timeout)
name|Object
name|receiveBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|long
name|timeout
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, waiting until there is a response      * or the timeout occurs      *      * @param endpoint the endpoint to receive from      * @param timeout  timeout in millis to wait for a response      * @return the returned response body, or<tt>null</tt> if no response      */
DECL|method|receiveBody (Endpoint endpoint, long timeout)
name|Object
name|receiveBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|long
name|timeout
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, not waiting for a response if non exists.      *      * @param endpointUri the endpoint to receive from      * @return the returned response body, or<tt>null</tt> if no response      */
DECL|method|receiveBodyNoWait (String endpointUri)
name|Object
name|receiveBodyNoWait
parameter_list|(
name|String
name|endpointUri
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, not waiting for a response if non exists.      *      * @param endpoint the endpoint to receive from      * @return the returned response body, or<tt>null</tt> if no response      */
DECL|method|receiveBodyNoWait (Endpoint endpoint)
name|Object
name|receiveBodyNoWait
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, waiting until there is a response      *      * @param endpointUri the endpoint to receive from      * @param type        the expected response type      * @return the returned response body      */
DECL|method|receiveBody (String endpointUri, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, waiting until there is a response      *      * @param endpoint the endpoint to receive from      * @param type     the expected response type      * @return the returned response body      */
DECL|method|receiveBody (Endpoint endpoint, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, waiting until there is a response      * or the timeout occurs      *      * @param endpointUri the endpoint to receive from      * @param timeout     timeout in millis to wait for a response      * @param type        the expected response type      * @return the returned response body, or<tt>null</tt> if no response      */
DECL|method|receiveBody (String endpointUri, long timeout, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|long
name|timeout
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, waiting until there is a response      * or the timeout occurs      *      * @param endpoint the endpoint to receive from      * @param timeout  timeout in millis to wait for a response      * @param type     the expected response type      * @return the returned response body, or<tt>null</tt> if no response      */
DECL|method|receiveBody (Endpoint endpoint, long timeout, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|long
name|timeout
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, not waiting for a response if non exists.      *      * @param endpointUri the endpoint to receive from      * @param type        the expected response type      * @return the returned response body, or<tt>null</tt> if no response      */
DECL|method|receiveBodyNoWait (String endpointUri, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBodyNoWait
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Receives from the endpoint, not waiting for a response if non exists.      *      * @param endpoint the endpoint to receive from      * @param type     the expected response type      * @return the returned response body, or<tt>null</tt> if no response      */
DECL|method|receiveBodyNoWait (Endpoint endpoint, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|receiveBodyNoWait
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * If you have used any of the<tt>receive</tt> methods which returns a {@link Exchange} type      * then you need to invoke this method when you are done using the returned {@link Exchange}.      *<p/>      * This is needed to ensure any {@link org.apache.camel.spi.Synchronization} works is being executed.      * For example if you consumed from a file endpoint, then the consumed file is only moved/delete when      * you done the {@link Exchange}.      *<p/>      * Note for all the other<tt>receive</tt> methods which does<b>not</b> return a {@link Exchange} type,      * the done has been executed automatic by Camel itself.      *      * @param exchange  the exchange      */
DECL|method|doneUoW (Exchange exchange)
name|void
name|doneUoW
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

