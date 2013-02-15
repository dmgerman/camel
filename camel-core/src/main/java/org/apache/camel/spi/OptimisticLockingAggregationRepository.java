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
comment|/**  * A specialized {@link org.apache.camel.spi.AggregationRepository} which also supports  * optimistic locking.  *  * If the underlying implementation cannot perform optimistic locking, it should  * not implement this interface.  *  * @version  */
end_comment

begin_interface
DECL|interface|OptimisticLockingAggregationRepository
specifier|public
interface|interface
name|OptimisticLockingAggregationRepository
extends|extends
name|AggregationRepository
block|{
comment|/**      * {@link Exception} used by an {@code AggregationRepository} to indicate that an optimistic      * update error has occurred and that the operation should be retried by the caller.      *<p/>      */
DECL|class|OptimisticLockingException
specifier|public
specifier|static
class|class
name|OptimisticLockingException
extends|extends
name|RuntimeException
block|{ }
comment|/**      * Add the given {@link org.apache.camel.Exchange} under the correlation key.      *<p/>      * Will perform optimistic locking to replace expected existing exchange with      * the new supplied exchange.      *      * @param camelContext   the current CamelContext      * @param key            the correlation key      * @param oldExchange    the old exchange that is expected to exist when replacing with the new exchange      * @param newExchange    the new aggregated exchange, to replace old exchange      * @return the old exchange if any existed      * @throws OptimisticLockingException This should be thrown when the currently stored exchange differs from the supplied oldExchange.      */
DECL|method|add (CamelContext camelContext, String key, Exchange oldExchange, Exchange newExchange)
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
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
throws|throws
name|OptimisticLockingException
function_decl|;
comment|/**      *      * @param camelContext   the current CamelContext      * @param key            the correlation key      * @param exchange       the exchange to remove      * @throws OptimisticLockingException This should be thrown when the exchange has already been deleted, or otherwise modified.      */
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
throws|throws
name|OptimisticLockingException
function_decl|;
block|}
end_interface

end_unit

