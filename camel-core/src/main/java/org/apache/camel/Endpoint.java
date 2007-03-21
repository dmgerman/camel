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
comment|/**  * Represents an endpoint that can send and receive message exchanges  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|Endpoint
specifier|public
interface|interface
name|Endpoint
parameter_list|<
name|E
parameter_list|>
extends|extends
name|Processor
argument_list|<
name|E
argument_list|>
block|{
comment|/**      * Returns the string representation of the endpoint URI      */
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
function_decl|;
comment|/**      * Sends an outbound exchange to the endpoint      */
DECL|method|onExchange (E exchange)
name|void
name|onExchange
parameter_list|(
name|E
name|exchange
parameter_list|)
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
comment|/**      * Called by the container to Activate the endpoint.  Once activated,      * the endpoint will start delivering inbound message exchanges      * that are received to the specified processor.      *      * The processor must be thread safe ( or stateless ) since some endpoints       * may choose to deliver exchanges concurrently to the processor.      *       * @throws IllegalStateException if the Endpoint has already been activated.      */
DECL|method|activate (Processor<E> processor)
name|void
name|activate
parameter_list|(
name|Processor
argument_list|<
name|E
argument_list|>
name|processor
parameter_list|)
throws|throws
name|IllegalStateException
function_decl|;
comment|/**      * Called by the container when the endpoint is deactivated      */
DECL|method|deactivate ()
name|void
name|deactivate
parameter_list|()
function_decl|;
comment|/**      * Returns the context which created the endpoint      *      * @return the context which created the endpoint      */
DECL|method|getContext ()
name|CamelContext
name|getContext
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

