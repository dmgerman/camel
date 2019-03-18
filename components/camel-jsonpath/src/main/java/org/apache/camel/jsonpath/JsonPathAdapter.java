begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jsonpath
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jsonpath
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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

begin_interface
DECL|interface|JsonPathAdapter
specifier|public
interface|interface
name|JsonPathAdapter
block|{
comment|/**      * Initializes the adapter      *      * @param camelContext the CamelContext      */
DECL|method|init (CamelContext camelContext)
name|void
name|init
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
function_decl|;
comment|/**      * Attempt to read/convert the message body into a {@link Map} type      *      * @param body the message body      * @param exchange the Camel exchange      * @return converted as {@link Map} or<tt>null</tt> if not possible      */
DECL|method|readValue (Object body, Exchange exchange)
name|Map
name|readValue
parameter_list|(
name|Object
name|body
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Attempts to write the value as a JSOn {@link String} value.      *      * @param value  the value      * @param exchange the Camel exchange      * @return written as {@link String} JSon or<tt>null</tt> if not possible      */
DECL|method|writeAsString (Object value, Exchange exchange)
name|String
name|writeAsString
parameter_list|(
name|Object
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

