begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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

begin_comment
comment|/**  * Access to a repository to store aggregated exchanges to support pluggable implementations.  *    */
end_comment

begin_interface
DECL|interface|AggregationRepository
specifier|public
interface|interface
name|AggregationRepository
block|{
comment|/**      * Add the given {@link Exchange} under the correlation key.      *<p/>      * Will replace any existing exchange.      *<p/>      *<b>Important:</b> This method is<b>not</b> invoked if only one exchange was completed, and therefore      * the exchange does not need to be added to a repository, as its completed immediately.      *      * @param camelContext   the current CamelContext      * @param key            the correlation key      * @param exchange       the aggregated exchange      * @return the old exchange if any existed      */
DECL|method|add (CamelContext camelContext, String key, Exchange exchange)
name|Exchange
name|add
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Gets the given exchange with the correlation key      *<p/>      * This method is always invoked for any incoming exchange in the aggregator.      *      * @param camelContext   the current CamelContext      * @param key            the correlation key      * @return the exchange, or<tt>null</tt> if no exchange was previously added      */
DECL|method|get (CamelContext camelContext, String key)
name|Exchange
name|get
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Removes the exchange with the given correlation key, which should happen      * when an {@link Exchange} is completed      *<p/>      *<b>Important:</b> This method is<b>not</b> invoked if only one exchange was completed, and therefore      * the exchange does not need to be added to a repository, as its completed immediately.      *      * @param camelContext   the current CamelContext      * @param key            the correlation key      * @param exchange       the exchange to remove      */
DECL|method|remove (CamelContext camelContext, String key, Exchange exchange)
name|void
name|remove
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|key
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Confirms the completion of the {@link Exchange}.      *<p/>      * This method is always invoked.      *      * @param camelContext  the current CamelContext      * @param exchangeId    exchange id to confirm      */
DECL|method|confirm (CamelContext camelContext, String exchangeId)
name|void
name|confirm
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|exchangeId
parameter_list|)
function_decl|;
comment|/**      * Gets the keys currently in the repository.      *      * @return the keys      */
DECL|method|getKeys ()
name|Set
argument_list|<
name|String
argument_list|>
name|getKeys
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

