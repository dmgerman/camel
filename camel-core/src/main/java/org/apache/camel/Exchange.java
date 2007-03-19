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
comment|/**  * Represents the base interface of an exchange  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|Exchange
specifier|public
interface|interface
name|Exchange
parameter_list|<
name|M
parameter_list|>
block|{
comment|/**      * Accesses a specific header      */
DECL|method|getHeader (String name)
parameter_list|<
name|T
parameter_list|>
name|T
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Sets a header on the exchange      */
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
comment|/**      * Returns all of the headers associated with the request      */
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
comment|/**      * Returns the request message      */
DECL|method|getRequest ()
name|M
name|getRequest
parameter_list|()
function_decl|;
comment|/**      * Returns the response message      */
DECL|method|getResponse ()
name|M
name|getResponse
parameter_list|()
function_decl|;
comment|/**      * Returns the fault message      */
DECL|method|getFault ()
name|M
name|getFault
parameter_list|()
function_decl|;
comment|/**      * Returns the exception associated with this exchange      */
DECL|method|getException ()
name|Exception
name|getException
parameter_list|()
function_decl|;
comment|/**      * Sets the exception associated with this exchange      */
DECL|method|setException (Exception e)
name|void
name|setException
parameter_list|(
name|Exception
name|e
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

