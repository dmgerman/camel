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
comment|/**  * A specialized {@link AggregationStrategy} which has callback when the aggregated {@link Exchange} is completed.  *  * @version  */
end_comment

begin_interface
DECL|interface|CompletionAwareAggregationStrategy
specifier|public
interface|interface
name|CompletionAwareAggregationStrategy
extends|extends
name|AggregationStrategy
block|{
comment|// TODO: In Camel 3.0 we should move this to org.apache.camel package
comment|/**      * The aggregated {@link Exchange} has completed      *      *<b>Important:</b> This method must<b>not</b> throw any exceptions.      *      * @param exchange  the current aggregated exchange, or the original {@link org.apache.camel.Exchange} if no aggregation      *                  has been done before the completion occurred      */
DECL|method|onCompletion (Exchange exchange)
name|void
name|onCompletion
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

