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
comment|/**  * An<a href="http://activemq.apache.org/camel/endpoint.html">endpoint</a>  * implements the<a  * href="http://activemq.apache.org/camel/message-endpoint.html">Message  * Endpoint</a> pattern and represents an endpoint that can send and receive  * message exchanges  *  * @see Exchange  * @see Message  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|Endpoint
specifier|public
interface|interface
name|Endpoint
block|{
comment|/**      * Returns if the endpoint should be a CamelContext singleton. If the      * endpoint is a Singleton, then a single Endpoint instance will be shared      * by all routes with the same URI. Because the endpoint is shared, it      * should be treated as an immutable.      */
DECL|method|isSingleton ()
name|boolean
name|isSingleton
parameter_list|()
function_decl|;
comment|/**      * Returns the string representation of the endpoint URI      */
DECL|method|getEndpointUri ()
name|String
name|getEndpointUri
parameter_list|()
function_decl|;
comment|/**      * Create a new exchange for communicating with this endpoint      */
DECL|method|createExchange ()
name|Exchange
name|createExchange
parameter_list|()
function_decl|;
comment|/**      * Create a new exchange for communicating with this endpoint      * with the specified {@link ExchangePattern} such as whether its going      * to be an {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut} exchange      *      * @param pattern the message exchange pattern for the exchange      */
DECL|method|createExchange (ExchangePattern pattern)
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
function_decl|;
comment|/**      * Creates a new exchange for communicating with this exchange using the      * given exchange to pre-populate the values of the headers and messages      */
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
comment|/**      * Creates a new producer which is used send messages into the endpoint      *      * @return a newly created producer      */
DECL|method|createProducer ()
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Creates a new<a      * href="http://activemq.apache.org/camel/event-driven-consumer.html">Event      * Driven Consumer</a> which consumes messages from the endpoint using the      * given processor      *      * @return a newly created consumer      */
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
comment|/**      * Creates a new<a      * href="http://activemq.apache.org/camel/polling-consumer.html">Polling      * Consumer</a> so that the caller can poll message exchanges from the      * consumer using {@link PollingConsumer#receive()},      * {@link PollingConsumer#receiveNoWait()} or      * {@link PollingConsumer#receive(long)} whenever it is ready to do so      * rather than using the<a      * href="http://activemq.apache.org/camel/event-driven-consumer.html">Event      * Based Consumer</a> returned by {@link #createConsumer(Processor)}      *      * @return a newly created pull consumer      * @throws Exception if the pull consumer could not be created      */
DECL|method|createPollingConsumer ()
name|PollingConsumer
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|configureProperties (Map options)
name|void
name|configureProperties
parameter_list|(
name|Map
name|options
parameter_list|)
function_decl|;
DECL|method|setCamelContext (CamelContext context)
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
annotation|@
name|Deprecated
DECL|method|getContext ()
name|CamelContext
name|getContext
parameter_list|()
function_decl|;
annotation|@
name|Deprecated
DECL|method|setContext (CamelContext context)
name|void
name|setContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
comment|/**      * Should all properties be known or does the endpoint allow unknown options?      *<p/>      *<tt>Lenient = false</tt> means that the endpoint should validate that all      * given options is known and configured properly      *<tt>lenient = true</tt> means that the endpoint allows additional unknown options to      * be passed to it but does not throw a ResolveEndpointFailedException when creating      * the endpoint.      *<p/>      * This options is used by a few components for instance the HTTP based that can have      * dynamic URI options appended that is targeted for an external system.      *<p/>      * Most endpoints is configued to be<b>not</b> lenient.      */
DECL|method|isLenientProperties ()
name|boolean
name|isLenientProperties
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

