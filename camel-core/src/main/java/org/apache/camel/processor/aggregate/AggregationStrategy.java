begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A strategy for aggregating two exchanges together into a single exchange.  * Possible implementations include performing some kind of combining or delta processing,  * such as adding line items together into an invoice or just using the newest exchange and  * removing old exchanges such as for state tracking or market data prices; where old values are of little use.  *  * @version $Revision: 1.1 $  */
end_comment

begin_interface
DECL|interface|AggregationStrategy
specifier|public
interface|interface
name|AggregationStrategy
block|{
comment|/**      * Aggregates an old and new exchange together to create a single combined      * exchange      *      * @param oldExchange the oldest exchange      * @param newExchange the newest exchange      * @return a combined composite of the two exchanges      */
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange)
name|Exchange
name|aggregate
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

