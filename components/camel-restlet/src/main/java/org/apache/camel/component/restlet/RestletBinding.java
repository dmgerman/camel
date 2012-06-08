begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
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
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Response
import|;
end_import

begin_comment
comment|/**  * Interface for converting between Camel message and Restlet message.  *   * @version   */
end_comment

begin_interface
DECL|interface|RestletBinding
specifier|public
interface|interface
name|RestletBinding
block|{
comment|/**      * Populate Restlet request from Camel message      *        * @param exchange message to be copied from       * @param response to be populated      * @throws Exception is thrown if error processing      */
DECL|method|populateRestletResponseFromExchange (Exchange exchange, Response response)
name|void
name|populateRestletResponseFromExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Response
name|response
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Populate Camel message from Restlet request      *       *      * @param request message to be copied from      * @param response the response      * @param exchange to be populated  @throws Exception is thrown if error processing      * @throws Exception is thrown if error processing      */
DECL|method|populateExchangeFromRestletRequest (Request request, Response response, Exchange exchange)
name|void
name|populateExchangeFromRestletRequest
parameter_list|(
name|Request
name|request
parameter_list|,
name|Response
name|response
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Populate Restlet Request from Camel message      *       * @param request to be populated      * @param exchange message to be copied from      */
DECL|method|populateRestletRequestFromExchange (Request request, Exchange exchange)
name|void
name|populateRestletRequestFromExchange
parameter_list|(
name|Request
name|request
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Populate Camel message from Restlet response      *       * @param exchange to be populated      * @param response message to be copied from      * @throws Exception is thrown if error processing      */
DECL|method|populateExchangeFromRestletResponse (Exchange exchange, Response response)
name|void
name|populateExchangeFromRestletResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Response
name|response
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

