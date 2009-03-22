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

begin_comment
comment|/**  * This converter is capable of converting from one message   * @version $Revision$  * @since 2.0  */
end_comment

begin_interface
DECL|interface|MessageConverter
specifier|public
interface|interface
name|MessageConverter
block|{
comment|/**      * Converts the IN message of a given Camel Exchange to the new type      *       * @param<T> the new type      * @param type the new class type      * @param exchange the exchange that contains the source message      * @return      */
DECL|method|convertInMessageTo (Class<T> type, Exchange exchange)
parameter_list|<
name|T
parameter_list|>
name|T
name|convertInMessageTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Converts the OUT message of a given Camel Exchange to the new type      *       * @param<T>      * @param type      * @param exchange      * @return      */
DECL|method|convertOutMessageTo (Class<T> type, Exchange exchange)
parameter_list|<
name|T
parameter_list|>
name|T
name|convertOutMessageTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

