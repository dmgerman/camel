begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Interface to bind between Camel and CXF exchange for RESTful resources.  *  * @version   */
end_comment

begin_interface
DECL|interface|CxfRsBinding
specifier|public
interface|interface
name|CxfRsBinding
block|{
comment|/**      * Populate the camel exchange from the CxfRsRequest, the exchange will be consumed      * by the processor which the CxfRsConsumer attached.      *      * @param camelExchange camel exchange object      * @param cxfExchange   cxf exchange object      * @param method        the method which is need for the camel component      * @param paramArray    the parameter list for the method invocation      */
DECL|method|populateExchangeFromCxfRsRequest (Exchange cxfExchange, org.apache.camel.Exchange camelExchange, Method method, Object[] paramArray)
name|void
name|populateExchangeFromCxfRsRequest
parameter_list|(
name|Exchange
name|cxfExchange
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|paramArray
parameter_list|)
function_decl|;
comment|/**      * Populate the CxfRsResponse object from the camel exchange      *      * @param camelExchange camel exchange object      * @param cxfExchange   cxf exchange object      * @return the response object      * @throws Exception can be thrown if error in the binding process      */
DECL|method|populateCxfRsResponseFromExchange (org.apache.camel.Exchange camelExchange, Exchange cxfExchange)
name|Object
name|populateCxfRsResponseFromExchange
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
parameter_list|,
name|Exchange
name|cxfExchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Bind the camel in message body to a request body that gets passed      * to CXF RS {@link org.apache.cxf.jaxrs.client.WebClient} APIs.      *      * @param camelMessage  the source message      * @param camelExchange the Camel exchange      * @return the request object to be passed to invoke a WebClient      * @throws Exception can be thrown if error in the binding process      */
DECL|method|bindCamelMessageBodyToRequestBody (org.apache.camel.Message camelMessage, org.apache.camel.Exchange camelExchange)
name|Object
name|bindCamelMessageBodyToRequestBody
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Bind the camel headers to request headers that gets passed to CXF RS      * {@link org.apache.cxf.jaxrs.client.WebClient} APIs.      *      * @param camelHeaders  the source headers      * @param camelExchange the Camel exchange      * @throws Exception can be thrown if error in the binding process      * @return the headers      */
DECL|method|bindCamelHeadersToRequestHeaders (Map<String, Object> camelHeaders, org.apache.camel.Exchange camelExchange)
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|bindCamelHeadersToRequestHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|camelHeaders
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Bind the HTTP response body to camel out body      *      * @param response the response      * @param camelExchange the exchange      * @return the object to be set in the Camel out message body      * @throws Exception can be thrown if error in the binding process      */
DECL|method|bindResponseToCamelBody (Object response, org.apache.camel.Exchange camelExchange)
name|Object
name|bindResponseToCamelBody
parameter_list|(
name|Object
name|response
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Bind the response headers to camel out headers.      *      * @param response the response      * @param camelExchange the exchange      * @return headers to be set in the Camel out message      * @throws Exception can be thrown if error in the binding process      */
DECL|method|bindResponseHeadersToCamelHeaders (Object response, org.apache.camel.Exchange camelExchange)
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|bindResponseHeadersToCamelHeaders
parameter_list|(
name|Object
name|response
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

