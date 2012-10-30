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
comment|/**  * Aggregate all exchanges into a single combined Exchange holding all the aggregated exchanges  * in a {@link java.util.List} as a exchange property with the key  * {@link org.apache.camel.Exchange#GROUPED_EXCHANGE}.  *  * @version   */
end_comment

begin_class
DECL|class|GroupedExchangeAggregationStrategy
specifier|public
class|class
name|GroupedExchangeAggregationStrategy
extends|extends
name|AbstractListAggregationStrategy
argument_list|<
name|Exchange
argument_list|>
block|{
annotation|@
name|Override
DECL|method|isStoreAsBodyOnCompletion ()
specifier|public
name|boolean
name|isStoreAsBodyOnCompletion
parameter_list|()
block|{
comment|// keep the list as a property to be compatible with old behavior
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|getValue (Exchange exchange)
specifier|public
name|Exchange
name|getValue
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

