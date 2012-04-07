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
comment|/**  * A specialized {@link org.apache.camel.processor.aggregate.AggregationStrategy} which can handle timeouts as well.  *  * @version   */
end_comment

begin_interface
DECL|interface|TimeoutAwareAggregationStrategy
specifier|public
interface|interface
name|TimeoutAwareAggregationStrategy
extends|extends
name|AggregationStrategy
block|{
comment|// TODO: In Camel 3.0 we should move this to org.apache.camel package
comment|/**      * A timeout occurred      *      * @param oldExchange  the current aggregated exchange, or the original {@link Exchange} if no aggregation      *                     has been done before the timeout occurred      * @param index        the index, may be<tt>-1</tt> if not possible to determine the index      * @param total        the total, may be<tt>-1</tt> if not possible to determine the total      * @param timeout      the timeout value in millis, may be<tt>-1</tt> if not possible to determine the timeout      */
DECL|method|timeout (Exchange oldExchange, int index, int total, long timeout)
name|void
name|timeout
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|total
parameter_list|,
name|long
name|timeout
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

