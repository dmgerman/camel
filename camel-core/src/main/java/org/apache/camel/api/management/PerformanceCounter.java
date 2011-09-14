begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
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
comment|/**  * A counter that gathers performance metrics when {@link org.apache.camel.Exchange} are routed in Camel.  *  * @version   */
end_comment

begin_interface
DECL|interface|PerformanceCounter
specifier|public
interface|interface
name|PerformanceCounter
block|{
comment|/**      * Executed when an {@link org.apache.camel.Exchange} is complete.      *      * @param exchange the exchange      * @param time  the time it took in millis to complete it      */
DECL|method|completedExchange (Exchange exchange, long time)
name|void
name|completedExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|long
name|time
parameter_list|)
function_decl|;
comment|/**      * Executed when an {@link org.apache.camel.Exchange} failed.      *      * @param exchange the exchange      */
DECL|method|failedExchange (Exchange exchange)
name|void
name|failedExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Is statistics enabled.      *<p/>      * They can be enabled and disabled at runtime      *      * @return whether statistics is enabled or not      */
DECL|method|isStatisticsEnabled ()
name|boolean
name|isStatisticsEnabled
parameter_list|()
function_decl|;
comment|/**      * Sets whether statistics is enabled.      *<p/>      * They can be enabled and disabled at runtime      *      * @param statisticsEnabled whether statistics is enabled or not      */
DECL|method|setStatisticsEnabled (boolean statisticsEnabled)
name|void
name|setStatisticsEnabled
parameter_list|(
name|boolean
name|statisticsEnabled
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

