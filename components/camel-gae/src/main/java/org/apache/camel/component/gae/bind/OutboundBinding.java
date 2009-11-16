begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.bind
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|bind
package|;
end_package

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
name|Producer
import|;
end_import

begin_comment
comment|/**  * Represents the binding of request and response types to an {@link Exchange}.  * The request and response types are defined via the type parameters  *<code>S</code> and<code>T</code>, respectively. The OutboundBinding is used  * by {@link Producer} implementations to translate between {@link Exchange}  * objects and protocol-specific or services-specific messages.  *   * @param S  *            request type.  * @param T  *            response type.  * @param E  *            endpoint type.  */
end_comment

begin_interface
DECL|interface|OutboundBinding
specifier|public
interface|interface
name|OutboundBinding
parameter_list|<
name|E
extends|extends
name|Endpoint
parameter_list|,
name|S
parameter_list|,
name|T
parameter_list|>
block|{
comment|/**      * Creates or populates a request object from {@link Exchange} and endpoint configuration data.      *       * @param endpoint endpoint providing binding-relevant information.       * @param exchange exchange to read data from.      * @param request request to be populated or created (if<code>null</code>) from exchange data.      * @return the populated response.      */
DECL|method|writeRequest (E endpoint, Exchange exchange, S request)
name|S
name|writeRequest
parameter_list|(
name|E
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|S
name|request
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Populates an {@link Exchange} from response data and endpoint configuration data.      *       * @param endpoint endpoint providing binding-relevant information.       * @param exchange exchange to be populated or created (if<code>null</code>) from response data.      * @param response response to read data from.      * @return the populated exchange.      */
DECL|method|readResponse (E endpoint, Exchange exchange, T response)
name|Exchange
name|readResponse
parameter_list|(
name|E
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|T
name|response
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

