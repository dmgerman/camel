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

begin_comment
comment|/**  * An<a href="http://activemq.apache.org/camel/endpoint.html">endpoint</a> implements the   *<a href="http://activemq.apache.org/camel/message-endpoint.html">Message Endpoint</a>  * pattern and represents an endpoint that can send and receive message exchanges  *  * @see Exchange, Message  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|Endpoint
specifier|public
interface|interface
name|Endpoint
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
block|{
comment|/**      * Returns the string representation of the endpoint URI      */
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
function_decl|;
comment|/**      * Create a new exchange for communicating with this endpoint      */
DECL|method|createExchange ()
name|E
name|createExchange
parameter_list|()
function_decl|;
comment|/**      * Creates a new exchange for communicating with this exchange using the given exchange to pre-populate the values      * of the headers and messages      */
DECL|method|createExchange (E exchange)
name|E
name|createExchange
parameter_list|(
name|E
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Returns the context which created the endpoint      *      * @return the context which created the endpoint      */
DECL|method|getContext ()
name|CamelContext
name|getContext
parameter_list|()
function_decl|;
comment|/**      * Creates a new producer which is used send messages into the endpoint      *      * @return a newly created producer      */
DECL|method|createProducer ()
name|Producer
argument_list|<
name|E
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Creates a new consumer which consumes messages from the endpoint using the given processor      *      * @return a newly created consumer      */
DECL|method|createConsumer (Processor<E> processor)
name|Consumer
argument_list|<
name|E
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
argument_list|<
name|E
argument_list|>
name|processor
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

