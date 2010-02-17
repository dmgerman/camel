begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Access to a repository to store aggregated exchanges to support pluggable implementations.  *    * @version $Revision$  */
end_comment

begin_interface
DECL|interface|AggregationRepository
specifier|public
interface|interface
name|AggregationRepository
parameter_list|<
name|K
parameter_list|>
block|{
comment|/**      * Add the given {@link Exchange} under the correlation key.      *<p/>      * Will replace any existing exchange.      *      * @param key  the correlation key      * @param exchange the aggregated exchange      * @return the old exchange if any existed      */
DECL|method|add (K key, Exchange exchange)
name|Exchange
name|add
parameter_list|(
name|K
name|key
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Gets the given exchange with the correlation key      *      * @param key the correlation key      * @return the exchange, or<tt>null</tt> if no exchange was previously added      */
DECL|method|get (K key)
name|Exchange
name|get
parameter_list|(
name|K
name|key
parameter_list|)
function_decl|;
comment|/**      * Removes the exchange with the given correlation key      *      * @param key the correlation key      */
DECL|method|remove (K key)
name|void
name|remove
parameter_list|(
name|K
name|key
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

