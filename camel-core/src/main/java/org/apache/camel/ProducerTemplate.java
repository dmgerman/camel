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
comment|/**  * Template (named like Spring's TransactionTemplate& JmsTemplate  * et al) for working with Camel and sending {@link Message} instances in an  * {@link Exchange} to an {@link Endpoint}.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|ProducerTemplate
specifier|public
interface|interface
name|ProducerTemplate
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|Service
block|{
comment|/**      * Sends the exchange to the default endpoint      *      * @param exchange the exchange to send      */
DECL|method|send (E exchange)
name|E
name|send
parameter_list|(
name|E
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Sends an exchange to the default endpoint using a supplied      *      * @param processor the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      */
DECL|method|send (Processor processor)
name|E
name|send
parameter_list|(
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Sends the body to the default endpoint and returns the result content      *      * @param body the body to send      * @return the returned message body      */
DECL|method|sendBody (Object body)
name|Object
name|sendBody
parameter_list|(
name|Object
name|body
parameter_list|)
function_decl|;
comment|/**      * Sends the body to the default endpoint with a specified header and header      * value      *      * @param body        the payload send      * @param header      the header name      * @param headerValue the header value      * @return the result      */
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
comment|/**      * Sends the body to the default endpoint with the specified headers and      * header values      *      * @param body the payload send      * @return the result      */
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
comment|/**      * Sends the exchange to the given endpoint      *      * @param endpointUri the endpoint URI to send the exchange to      * @param exchange    the exchange to send      */
DECL|method|send (String endpointUri, E exchange)
name|E
name|send
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|E
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Sends an exchange to an endpoint using a supplied processor      *      * @param endpointUri the endpoint URI to send the exchange to      * @param processor   the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      */
DECL|method|send (String endpointUri, Processor processor)
name|E
name|send
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Sends an exchange to an endpoint using a supplied processor      *      * @param endpointUri the endpoint URI to send the exchange to      * @param pattern     the message {@link ExchangePattern} such as      *                    {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param processor   the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      */
DECL|method|send (String endpointUri, ExchangePattern pattern, Processor processor)
name|E
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
comment|/**      * Sends an exchange to an endpoint using a supplied processor      *      * @param endpointUri the endpoint URI to send the exchange to      * @param processor   the transformer used to populate the new exchange      * {@link Processor} to populate the exchange.      * @param callback    the callback will be called when the exchange is completed.      */
DECL|method|send (String endpointUri, Processor processor, AsyncCallback callback)
name|E
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
comment|/**      * Sends the exchange to the given endpoint      *      * @param endpoint the endpoint to send the exchange to      * @param exchange the exchange to send      */
DECL|method|send (Endpoint<E> endpoint, E exchange)
name|E
name|send
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|E
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Sends an exchange to an endpoint using a supplied processor      *      * @param endpoint  the endpoint to send the exchange to      * @param processor the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      */
DECL|method|send (Endpoint<E> endpoint, Processor processor)
name|E
name|send
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Sends an exchange to an endpoint using a supplied processor      *      * @param endpoint  the endpoint to send the exchange to      * @param pattern   the message {@link ExchangePattern} such as      *                  {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param processor the transformer used to populate the new exchange      * {@link Processor} to populate the exchange      */
DECL|method|send (Endpoint<E> endpoint, ExchangePattern pattern, Processor processor)
name|E
name|send
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Sends an exchange to an endpoint using a supplied processor      *      * @param endpoint  the endpoint to send the exchange to      * @param processor the transformer used to populate the new exchange      * {@link Processor} to populate the exchange.      * @param callback  the callback will be called when the exchange is completed.      */
DECL|method|send (Endpoint<E> endpoint, Processor processor, AsyncCallback callback)
name|E
name|send
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body      *      * @param endpoint   the endpoint to send the exchange to      * @param body       the payload      * @return the result      */
DECL|method|sendBody (Endpoint<E> endpoint, Object body)
name|Object
name|sendBody
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|)
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body      *      * @param endpointUri   the endpoint URI to send the exchange to      * @param body          the payload      * @return the result      */
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
comment|/**      * Send the body to an endpoint with the given {@link ExchangePattern}      * returning any result output body      *      * @param endpoint      the endpoint to send the exchange to      * @param body          the payload      * @param pattern       the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @return the result      */
DECL|method|sendBody (Endpoint<E> endpoint, ExchangePattern pattern, Object body)
name|Object
name|sendBody
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Object
name|body
parameter_list|)
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body      *      * @param endpointUri   the endpoint URI to send the exchange to      * @param pattern       the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body          the payload      * @return the result      */
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
comment|/**      * Sends the body to an endpoint with a specified header and header value      *      * @param endpointUri the endpoint URI to send to      * @param body the payload send      * @param header the header name      * @param headerValue the header value      * @return the result      */
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
comment|/**      * Sends the body to an endpoint with a specified header and header value      *      * @param endpoint the Endpoint to send to      * @param body the payload send      * @param header the header name      * @param headerValue the header value      * @return the result      */
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
comment|/**      * Sends the body to an endpoint with a specified header and header value      *      * @param endpoint the Endpoint to send to      * @param pattern the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body the payload send      * @param header the header name      * @param headerValue the header value      * @return the result      */
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
comment|/**      * Sends the body to an endpoint with a specified header and header value      *      * @param endpoint the Endpoint URI to send to      * @param pattern the message {@link ExchangePattern} such as      *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}      * @param body the payload send      * @param header the header name      * @param headerValue the header value      * @return the result      */
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
comment|/**      * Sends the body to an endpoint with the specified headers and header      * values      *      * @param endpointUri the endpoint URI to send to      * @param body the payload send      * @return the result      */
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
comment|/**      * Sends the body to an endpoint with the specified headers and header      * values      *      * @param endpoint the endpoint URI to send to      * @param body the payload send      * @return the result      */
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
comment|// Methods using an InOut ExchangePattern
comment|// -----------------------------------------------------------------------
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpoint  the Endpoint to send to      * @param processor the processor which will populate the exchange before sending      * @return the result      */
DECL|method|request (Endpoint<E> endpoint, Processor processor)
name|E
name|request
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpoint the Endpoint to send to      * @param body     the payload      * @return the result      */
DECL|method|requestBody (Endpoint<E> endpoint, Object body)
name|Object
name|requestBody
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|)
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpoint    the Endpoint to send to      * @param body        the payload      * @param header      the header name      * @param headerValue the header value      * @return the result      */
DECL|method|requestBodyAndHeader (Endpoint<E> endpoint, Object body, String header, Object headerValue)
name|Object
name|requestBodyAndHeader
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
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
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpointUri the endpoint URI to send to      * @param processor the processor which will populate the exchange before sending      * @return the result      */
DECL|method|request (String endpointUri, Processor processor)
name|E
name|request
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpointUri the endpoint URI to send to      * @param body        the payload      * @return the result      */
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
comment|/**      * Send the body to an endpoint returning any result output body.      * Uses an {@link ExchangePattern#InOut} message exchange pattern.      *      * @param endpointUri the endpoint URI to send to      * @param body        the payload      * @param header      the header name      * @param headerValue the header value      * @return the result      */
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
block|}
end_interface

end_unit

