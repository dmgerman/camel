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
comment|/**  * Represents the base exchange interface providing access to the request, response and fault {@link Message} instances.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|Exchange
specifier|public
interface|interface
name|Exchange
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
comment|/**      * Returns the exchange headers      */
DECL|method|getHeaders ()
name|Headers
name|getHeaders
parameter_list|()
function_decl|;
comment|/**      * Returns the inbound request message      * @return the message      */
DECL|method|getIn ()
name|Message
name|getIn
parameter_list|()
function_decl|;
comment|/**      * Returns the aresponse message      * @return the response      */
DECL|method|getOut ()
name|Message
name|getOut
parameter_list|()
function_decl|;
comment|/**      * Returns the fault message      * @return the fault      */
DECL|method|getFault ()
name|Message
name|getFault
parameter_list|()
function_decl|;
comment|/**      * Returns the exception associated with this exchange      * @return the exception (or null if no faults)      */
DECL|method|getException ()
name|Throwable
name|getException
parameter_list|()
function_decl|;
comment|/**      * Sets the exception associated with this exchange      * @param e      */
DECL|method|setException (Throwable e)
name|void
name|setException
parameter_list|(
name|Throwable
name|e
parameter_list|)
function_decl|;
comment|/**      * Returns the container so that a processor can resolve endpoints from URIs      *      * @return the container which owns this exchange      */
DECL|method|getContext ()
name|CamelContext
argument_list|<
name|Exchange
argument_list|>
name|getContext
parameter_list|()
function_decl|;
comment|/**      * Creates a copy of the current message exchange so that it can be forwarded to another      * destination      */
DECL|method|copy ()
name|Exchange
name|copy
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

