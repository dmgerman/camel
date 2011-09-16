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
comment|/**  * An<a href="http://camel.apache.org/endpoint.html">endpoint</a>  * implements the<a  * href="http://camel.apache.org/message-endpoint.html">Message  * Endpoint</a> pattern and represents an endpoint that can send and receive  * message exchanges  *  * @see Exchange  * @see Message  * @version   */
end_comment

begin_interface
DECL|interface|Endpoint
specifier|public
interface|interface
name|Endpoint
extends|extends
name|IsSingleton
extends|,
name|Service
block|{
comment|/**      * Returns the string representation of the endpoint URI      *      * @return the endpoint URI      */
DECL|method|getEndpointUri ()
name|String
name|getEndpointUri
parameter_list|()
function_decl|;
comment|/**      * Returns the object representation of the endpoint configuration      *      * @return the endpoint URI      */
DECL|method|getEndpointConfiguration ()
name|EndpointConfiguration
name|getEndpointConfiguration
parameter_list|()
function_decl|;
comment|/**      * Returns a string key of this endpoint.      *<p/>      * This key is used by {@link org.apache.camel.spi.LifecycleStrategy} when registering endpoint.      * This allows to register different instances of endpoints with the same key.      *<p/>      * For JMX mbeans this allows us to use the same JMX Mbean for all endpoints that are logical      * the same but have different parameters. For instance the http endpoint.      *      * @return the endpoint key      */
DECL|method|getEndpointKey ()
name|String
name|getEndpointKey
parameter_list|()
function_decl|;
comment|/**      * Create a new exchange for communicating with this endpoint      *      * @return a new exchange      */
DECL|method|createExchange ()
name|Exchange
name|createExchange
parameter_list|()
function_decl|;
comment|/**      * Create a new exchange for communicating with this endpoint      * with the specified {@link ExchangePattern} such as whether its going      * to be an {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut} exchange      *      * @param pattern the message exchange pattern for the exchange      * @return a new exchange      */
DECL|method|createExchange (ExchangePattern pattern)
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
function_decl|;
comment|/**      * Creates a new exchange for communicating with this exchange using the      * given exchange to pre-populate the values of the headers and messages      *      * @param exchange given exchange to use for pre-populate      * @return a new exchange      */
DECL|method|createExchange (Exchange exchange)
name|Exchange
name|createExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Returns the context which created the endpoint      *      * @return the context which created the endpoint      */
DECL|method|getCamelContext ()
name|CamelContext
name|getCamelContext
parameter_list|()
function_decl|;
comment|/**      * Creates a new producer which is used send messages into the endpoint      *      * @return a newly created producer      * @throws Exception can be thrown      */
DECL|method|createProducer ()
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Creates a new<a      * href="http://camel.apache.org/event-driven-consumer.html">Event      * Driven Consumer</a> which consumes messages from the endpoint using the      * given processor      *      * @param processor  the given processor      * @return a newly created consumer      * @throws Exception can be thrown      */
DECL|method|createConsumer (Processor processor)
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Creates a new<a      * href="http://camel.apache.org/polling-consumer.html">Polling      * Consumer</a> so that the caller can poll message exchanges from the      * consumer using {@link PollingConsumer#receive()},      * {@link PollingConsumer#receiveNoWait()} or      * {@link PollingConsumer#receive(long)} whenever it is ready to do so      * rather than using the<a      * href="http://camel.apache.org/event-driven-consumer.html">Event      * Based Consumer</a> returned by {@link #createConsumer(Processor)}      *      * @return a newly created pull consumer      * @throws Exception if the pull consumer could not be created      */
DECL|method|createPollingConsumer ()
name|PollingConsumer
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Configure properties on this endpoint.      *       * @param options  the options (properties)      */
DECL|method|configureProperties (Map<String, Object> options)
name|void
name|configureProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
function_decl|;
comment|/**      * Sets the camel context.      *      * @param context the camel context      */
DECL|method|setCamelContext (CamelContext context)
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
comment|/**      * Should all properties be known or does the endpoint allow unknown options?      *<p/>      *<tt>lenient = false</tt> means that the endpoint should validate that all      * given options is known and configured properly.      *<tt>lenient = true</tt> means that the endpoint allows additional unknown options to      * be passed to it but does not throw a ResolveEndpointFailedException when creating      * the endpoint.      *<p/>      * This options is used by a few components for instance the HTTP based that can have      * dynamic URI options appended that is targeted for an external system.      *<p/>      * Most endpoints is configured to be<b>not</b> lenient.      *      * @return whether properties is lenient or not      */
DECL|method|isLenientProperties ()
name|boolean
name|isLenientProperties
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

