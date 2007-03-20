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
comment|/**  * Represents the base interface of an exchange  *  * @version $Revision$  * @param<M> message or payload type  * @param<R> message or payload type for a response (for request/response exchange)  * @param<F> fault type  */
end_comment

begin_interface
DECL|interface|Exchange
specifier|public
interface|interface
name|Exchange
parameter_list|<
name|M
parameter_list|,
name|R
parameter_list|,
name|F
parameter_list|>
block|{
comment|/**      * Returns the exchange id      * @return the unique id of the exchange      */
DECL|method|getExchangeId ()
name|String
name|getExchangeId
parameter_list|()
function_decl|;
comment|/**      * Set the exchange id      * @param id      */
DECL|method|setExchangeId (String id)
name|void
name|setExchangeId
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Accesses a specific header      * @param name       * @return object header associated with the name      */
DECL|method|getHeader (String name)
name|Object
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Sets a header on the exchange      * @param name of the header       * @param value to associate with the name      */
DECL|method|setHeader (String name, Object value)
name|void
name|setHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**      * Returns all of the headers associated with the request      * @return all the headers in a Map      */
DECL|method|getHeaders ()
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHeaders
parameter_list|()
function_decl|;
comment|/**      * Returns the request message      * @return the message      */
DECL|method|getRequest ()
name|M
name|getRequest
parameter_list|()
function_decl|;
comment|/**      * Returns the response message      * @return the response      */
DECL|method|getResponse ()
name|R
name|getResponse
parameter_list|()
function_decl|;
comment|/**      * Returns the fault message      * @return the fault      */
DECL|method|getFault ()
name|F
name|getFault
parameter_list|()
function_decl|;
comment|/**      * Returns the exception associated with this exchange      * @return the exception (or null if no faults)      */
DECL|method|getException ()
name|Exception
name|getException
parameter_list|()
function_decl|;
comment|/**      * Sets the exception associated with this exchange      * @param e       */
DECL|method|setException (Exception e)
name|void
name|setException
parameter_list|(
name|Exception
name|e
parameter_list|)
function_decl|;
comment|/**      * Returns the container so that a processor can resolve endpoints from URIs      *      * @return the container which owns this exchange      */
DECL|method|getContainer ()
name|CamelContext
argument_list|<
name|Exchange
argument_list|>
name|getContainer
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

