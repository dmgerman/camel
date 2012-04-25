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
comment|/**  * Provides a channel on which clients can create and invoke message exchanges  * on an {@link Endpoint}  *   * @version   */
end_comment

begin_interface
DECL|interface|Producer
specifier|public
interface|interface
name|Producer
extends|extends
name|Processor
extends|,
name|Service
extends|,
name|IsSingleton
block|{
comment|/**      * Gets the endpoint this producer sends to.      *      * @return the endpoint      */
DECL|method|getEndpoint ()
name|Endpoint
name|getEndpoint
parameter_list|()
function_decl|;
comment|/**      * Creates a new exchange to send to this endpoint      *       * @return a newly created exchange      */
DECL|method|createExchange ()
name|Exchange
name|createExchange
parameter_list|()
function_decl|;
comment|/**      * Creates a new exchange of the given pattern to send to this endpoint      *      * @param pattern the exchange pattern      * @return a newly created exchange      */
DECL|method|createExchange (ExchangePattern pattern)
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
function_decl|;
comment|/**      * Creates a new exchange for communicating with this exchange using the      * given exchange to pre-populate the values of the headers and messages      *      * @param exchange the existing exchange      * @return the created exchange      * @deprecated will be removed in Camel 3.0      */
annotation|@
name|Deprecated
DECL|method|createExchange (Exchange exchange)
name|Exchange
name|createExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

