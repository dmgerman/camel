begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
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
comment|/**  * A specialized {@link AggregationStrategy} which gets a callback when the aggregated {@link Exchange} fails to add  * in the {@link org.apache.camel.spi.OptimisticLockingAggregationRepository} because of  * an {@link org.apache.camel.spi.OptimisticLockingAggregationRepository.OptimisticLockingException}.  *<p/>  * Please note that when aggregating {@link Exchange}'s to be careful not to modify and return the {@code oldExchange}  * from the {@link AggregationStrategy#aggregate(org.apache.camel.Exchange, org.apache.camel.Exchange)} method.  * If you are using the default MemoryAggregationRepository this will mean you have modified the value of an object  * already referenced/stored by the MemoryAggregationRepository. This makes it impossible for optimistic locking  * to work correctly with the MemoryAggregationRepository.  *<p/>  * You should instead return either the new {@code newExchange} or a completely new instance of {@link Exchange}. This  * is due to the nature of how the underlying {@link java.util.concurrent.ConcurrentHashMap} performs CAS operations  * on the value identity.  *  * @see java.util.concurrent.ConcurrentHashMap  *  * @version  */
end_comment

begin_interface
DECL|interface|OptimisticLockingAwareAggregationStrategy
specifier|public
interface|interface
name|OptimisticLockingAwareAggregationStrategy
extends|extends
name|AggregationStrategy
block|{
comment|// TODO: In Camel 3.0 we should move this to org.apache.camel package
DECL|method|onOptimisticLockFailure (Exchange oldExchange, Exchange newExchange)
name|void
name|onOptimisticLockFailure
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

