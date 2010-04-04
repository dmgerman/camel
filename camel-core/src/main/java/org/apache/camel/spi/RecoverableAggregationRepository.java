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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
comment|/**  * A specialized {@link org.apache.camel.spi.AggregationRepository} which also supports  * recovery. This usually requires a repository which is persisted.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|RecoverableAggregationRepository
specifier|public
interface|interface
name|RecoverableAggregationRepository
parameter_list|<
name|K
parameter_list|>
extends|extends
name|AggregationRepository
argument_list|<
name|K
argument_list|>
block|{
comment|/**      * Scans the repository for {@link Exchange}s to be recovered      *       * @param camelContext   the current CamelContext      * @return the exchange ids for to be recovered      */
DECL|method|scan (CamelContext camelContext)
name|Set
argument_list|<
name|String
argument_list|>
name|scan
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
function_decl|;
comment|/**      * Recovers the exchange with the given exchange id      *      * @param camelContext   the current CamelContext      * @param exchangeId     exchange id      * @return the recovered exchange or<tt>null</tt> if not found      */
DECL|method|recover (CamelContext camelContext, String exchangeId)
name|Exchange
name|recover
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|exchangeId
parameter_list|)
function_decl|;
comment|/**      * Sets the interval between scans      *      * @param interval  the interval      * @param timeUnit  the time unit      */
DECL|method|setCheckInterval (long interval, TimeUnit timeUnit)
name|void
name|setCheckInterval
parameter_list|(
name|long
name|interval
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
function_decl|;
comment|/**      * Sets the interval between scans      *      * @param interval  the interval in millis      */
DECL|method|setCheckInterval (long interval)
name|void
name|setCheckInterval
parameter_list|(
name|long
name|interval
parameter_list|)
function_decl|;
comment|/**      * Gets the interval between scans in millis.      *      * @return the interval in millis      */
DECL|method|getCheckIntervalInMillis ()
name|long
name|getCheckIntervalInMillis
parameter_list|()
function_decl|;
comment|/**      * Whether or not recovery is enabled or not      *      * @return<tt>true</tt> to use recovery,<tt>false</tt> otherwise.      */
DECL|method|isUseRecovery ()
name|boolean
name|isUseRecovery
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

