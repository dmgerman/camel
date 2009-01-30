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

begin_comment
comment|/**  * Template (named like Spring's TransactionTemplate& JmsTemplate  * et al) for working with Camel and sending {@link Message} instances in an  * {@link Exchange} to an {@link Endpoint}.  *<p/>  *<b>All</b> methods throws {@link RuntimeCamelException} if processing of  * the {@link Exchange} failed and an Exception occured. The<tt>getCause</tt>  * method on {@link RuntimeCamelException} returns the wrapper original caused  * exception.  *<p/>  * All the send<b>Body</b> methods will return the content according to this strategy  *<ul>  *<li>throws {@link RuntimeCamelException} as stated above</li>  *<li>The<tt>fault.body</tt> if there is a fault message set and its not<tt>null</tt></li>  *<li>Either<tt>IN</tt> or<tt>OUT</tt> body according to the message exchange pattern. If the pattern is  *   Out capable then the<tt>OUT</tt> body is returned, otherwise<tt>IN</tt>.  *</ul>  *<p/>  *<b>Important note on usage:</b> See this  *<a href="http://camel.apache.org/why-does-camel-use-too-many-threads-with-producertemplate.html">FAQ entry</a>  * before using.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|ProducerTemplate
specifier|public
interface|interface
name|ProducerTemplate
extends|extends
name|Service
block|{
comment|/**      * Sends the exchange to the default endpoint      *      * @param exchange the exchange to send      * @return the returned exchange      */
DECL|method|send (Exchange exchange)
name|Exchange
name|send
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Sends an exchange to the default endpoint using a supplied processor      *      * @param processor the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      * @return the returned exchange      */
DECL|method|send (Processor processor)
name|Exchange
name|send
parameter_list|(
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Sends the body to the default endpoint and returns the result content      *      * @param body the payload to send      * @return the result (see class javadoc)      */
DECL|method|sendBody (Object body)
name|Object
name|sendBody
parameter_list|(
name|Object
name|body
parameter_list|)
function_decl|;
comment|/**      * Sends the body to the default endpoint with a specified header and header      * value      *      * @param body the payload to send      * @param header the header name      * @param headerValue the header value      * @return the result (see class javadoc)      */
DECL|method|sendBodyAndHeader (Object body, String header, Object headerValue)
name|Object
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
function_decl|;
comment|/**      * Sends the body to the default endpoint with a specified property and property      * value      *      * @param body          the payload to send      * @param property      the property name      * @param propertyValue the property value      * @return the result (see class javadoc)      */
DECL|method|sendBodyAndProperty (Object body, String property, Object propertyValue)
name|Object
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
function_decl|;
comment|/**      * Sends the body to the default endpoint with the specified headers and      * header values      *      * @param body the payload to send      * @param headers      the headers      * @return the result (see class javadoc)      */
DECL|method|sendBodyAndHeaders (Object body, Map<String, Object> headers)
name|Object
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
function_decl|;
comment|// Allow sending to arbitrary endpoints
comment|// -----------------------------------------------------------------------
comment|/**      * Sends the exchange to the given endpoint      *      * @param endpointUri the endpoint URI to send the exchange to      * @param exchange    the exchange to send      * @return the returned exchange      */
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
comment|/**      * Sends an exchange to an endpoint using a supplied processor      *      * @param endpointUri the endpoint URI to send the exchange to      * @param processor   the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      * @return the returned exchange      */
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
comment|/**      * Sends an exchange to an endpoint using a supplied processor      *      * @param endpointUri the endpoint URI to send the exchange to      * @param pattern     the message {@link ExchangePattern} such as      *                    {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param processor   the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      * @return the returned exchange      */
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
comment|/**      * Sends an exchange to an endpoint using a supplied processor      *      * @param endpointUri the endpoint URI to send the exchange to      * @param processor   the transformer used to populate the new exchange      * {@link Processor} to populate the exchange.      * @param callback    the callback will be called when the exchange is completed.      * @return the returned exchange      */
DECL|method|send (String endpointUri, Processor processor, AsyncCallback callback)
name|Exchange
name|send
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Sends the exchange to the given endpoint      *      * @param endpoint the endpoint to send the exchange to      * @param exchange the exchange to send      * @return the returned exchange      */
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
comment|/**      * Sends an exchange to an endpoint using a supplied processor      *      * @param endpoint  the endpoint to send the exchange to      * @param processor the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      * @return the returned exchange      */
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
comment|/**      * Sends an exchange to an endpoint using a supplied processor      *      * @param endpoint  the endpoint to send the exchange to      * @param pattern   the message {@link ExchangePattern} such as      *                  {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param processor the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      * @return the returned exchange      */
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
comment|/**      * Sends an exchange to an endpoint using a supplied processor      *      * @param endpoint  the endpoint to send the exchange to      * @param processor the transformer used to populate the new exchange      * {@link Processor} to populate the exchange.      * @param callback  the callback will be called when the exchange is completed.      * @return the returned exchange      */
DECL|method|send (Endpoint endpoint, Processor processor, AsyncCallback callback)
name|Exchange
name|send
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body      *      * @param endpoint   the endpoint to send the exchange to      * @param body       the payload      * @return the result (see class javadoc)      */
DECL|method|sendBody (Endpoint endpoint, Object body)
name|Object
name|sendBody
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|)
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body      *      * @param endpointUri   the endpoint URI to send the exchange to      * @param body          the payload      * @return the result (see class javadoc)      */
DECL|method|sendBody (String endpointUri, Object body)
name|Object
name|sendBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|)
function_decl|;
comment|/**      * Send the body to an endpoint with the given {@link ExchangePattern}      * returning any result output body      *      * @param endpoint      the endpoint to send the exchange to      * @param body          the payload      * @param pattern       the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @return the result (see class javadoc)      */
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
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body      *      * @param endpointUri   the endpoint URI to send the exchange to      * @param pattern       the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body          the payload      * @return the result (see class javadoc)      */
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
function_decl|;
comment|/**      * Sends the body to an endpoint with a specified header and header value      *      * @param endpointUri the endpoint URI to send to      * @param body the payload to send      * @param header the header name      * @param headerValue the header value      * @return the result (see class javadoc)      */
DECL|method|sendBodyAndHeader (String endpointUri, Object body, String header, Object headerValue)
name|Object
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
function_decl|;
comment|/**      * Sends the body to an endpoint with a specified header and header value      *      * @param endpoint the Endpoint to send to      * @param body the payload to send      * @param header the header name      * @param headerValue the header value      * @return the result (see class javadoc)      */
DECL|method|sendBodyAndHeader (Endpoint endpoint, Object body, String header, Object headerValue)
name|Object
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
function_decl|;
comment|/**      * Sends the body to an endpoint with a specified header and header value      *      * @param endpoint the Endpoint to send to      * @param pattern the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body the payload to send      * @param header the header name      * @param headerValue the header value      * @return the result (see class javadoc)      */
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
function_decl|;
comment|/**      * Sends the body to an endpoint with a specified header and header value      *      * @param endpoint the Endpoint URI to send to      * @param pattern the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body the payload to send      * @param header the header name      * @param headerValue the header value      * @return the result (see class javadoc)      */
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
function_decl|;
comment|/**      * Sends the body to an endpoint with a specified property and property value      *      * @param endpointUri the endpoint URI to send to      * @param body the payload to send      * @param property the property name      * @param propertyValue the property value      * @return the result (see class javadoc)      */
DECL|method|sendBodyAndProperty (String endpointUri, Object body, String property, Object propertyValue)
name|Object
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
function_decl|;
comment|/**      * Sends the body to an endpoint with a specified property and property value      *      * @param endpoint the Endpoint to send to      * @param body the payload to send      * @param property the property name      * @param propertyValue the property value      * @return the result (see class javadoc)      */
DECL|method|sendBodyAndProperty (Endpoint endpoint, Object body, String property, Object propertyValue)
name|Object
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
function_decl|;
comment|/**      * Sends the body to an endpoint with a specified property and property value      *      * @param endpoint the Endpoint to send to      * @param pattern the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body the payload to send      * @param property the property name      * @param propertyValue the property value      * @return the result (see class javadoc)      */
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
function_decl|;
comment|/**      * Sends the body to an endpoint with a specified property and property value      *      * @param endpoint the Endpoint URI to send to      * @param pattern the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body the payload to send      * @param property the property name      * @param propertyValue the property value      * @return the result (see class javadoc)      */
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
function_decl|;
comment|/**      * Sends the body to an endpoint with the specified headers and header      * values      *      * @param endpointUri the endpoint URI to send to      * @param body the payload to send      * @param headers headers      * @return the result (see class javadoc)      */
DECL|method|sendBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers)
name|Object
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
function_decl|;
comment|/**      * Sends the body to an endpoint with the specified headers and header      * values      *      * @param endpoint the endpoint URI to send to      * @param body the payload to send      * @param headers headers      * @return the result (see class javadoc)      */
DECL|method|sendBodyAndHeaders (Endpoint endpoint, Object body, Map<String, Object> headers)
name|Object
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
function_decl|;
comment|/**      * Sends the body to an endpoint with the specified headers and header      * values      *      * @param endpointUri the endpoint URI to send to      * @param pattern the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body the payload to send      * @param headers headers      * @return the result (see class javadoc)      */
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
function_decl|;
comment|/**      * Sends the body to an endpoint with the specified headers and header      * values      *      * @param endpoint the endpoint URI to send to      * @param pattern the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body the payload to send      * @param headers headers      * @return the result (see class javadoc)      */
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
function_decl|;
comment|// Methods using an InOut ExchangePattern
comment|// -----------------------------------------------------------------------
comment|/**      * Sends an exchange to an endpoint using a supplied processor      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpoint  the Endpoint to send to      * @param processor the processor which will populate the exchange before sending      * @return the result (see class javadoc)      */
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
comment|/**      * Sends an exchange to an endpoint using a supplied processor      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpointUri the endpoint URI to send to      * @param processor the processor which will populate the exchange before sending      * @return the result (see class javadoc)      */
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
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpoint the Endpoint to send to      * @param body     the payload      * @return the result (see class javadoc)      */
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
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpointUri the endpoint URI to send to      * @param body        the payload      * @return the result (see class javadoc)      */
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
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpoint    the Endpoint to send to      * @param body        the payload      * @param header      the header name      * @param headerValue the header value      * @return the result (see class javadoc)      */
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
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpointUri the endpoint URI to send to      * @param body        the payload      * @param header      the header name      * @param headerValue the header value      * @return the result (see class javadoc)      */
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
function_decl|;
comment|/**      * Sends the body to an endpoint with the specified headers and header      * values.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpointUri the endpoint URI to send to      * @param body the payload to send      * @param headers headers      * @return the result (see class javadoc)      */
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
function_decl|;
comment|/**      * Sends the body to an endpoint with the specified headers and header      * values.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpoint the endpoint URI to send to      * @param body the payload to send      * @param headers headers      * @return the result (see class javadoc)      */
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
function_decl|;
block|}
end_interface

end_unit

