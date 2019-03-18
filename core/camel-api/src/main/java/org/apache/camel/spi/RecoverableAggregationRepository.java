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
comment|/**  * A specialized {@link org.apache.camel.spi.AggregationRepository} which also supports  * recovery. This usually requires a repository which is persisted.  */
end_comment

begin_interface
DECL|interface|RecoverableAggregationRepository
specifier|public
interface|interface
name|RecoverableAggregationRepository
extends|extends
name|AggregationRepository
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
comment|/**      * Sets the interval between recovery scans      *      * @param interval  the interval      * @param timeUnit  the time unit      */
DECL|method|setRecoveryInterval (long interval, TimeUnit timeUnit)
name|void
name|setRecoveryInterval
parameter_list|(
name|long
name|interval
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
function_decl|;
comment|/**      * Sets the interval between recovery scans      *      * @param interval  the interval in millis      */
DECL|method|setRecoveryInterval (long interval)
name|void
name|setRecoveryInterval
parameter_list|(
name|long
name|interval
parameter_list|)
function_decl|;
comment|/**      * Gets the interval between recovery scans in millis.      *      * @return the interval in millis      */
DECL|method|getRecoveryIntervalInMillis ()
name|long
name|getRecoveryIntervalInMillis
parameter_list|()
function_decl|;
comment|/**      * Sets whether or not recovery is enabled      *      * @param useRecovery whether or not recovery is enabled      */
DECL|method|setUseRecovery (boolean useRecovery)
name|void
name|setUseRecovery
parameter_list|(
name|boolean
name|useRecovery
parameter_list|)
function_decl|;
comment|/**      * Whether or not recovery is enabled or not      *      * @return<tt>true</tt> to use recovery,<tt>false</tt> otherwise.      */
DECL|method|isUseRecovery ()
name|boolean
name|isUseRecovery
parameter_list|()
function_decl|;
comment|/**      * Sets an optional dead letter channel which exhausted recovered {@link Exchange}      * should be send to.      *<p/>      * By default this option is disabled      *      * @param deadLetterUri  the uri of the dead letter channel      */
DECL|method|setDeadLetterUri (String deadLetterUri)
name|void
name|setDeadLetterUri
parameter_list|(
name|String
name|deadLetterUri
parameter_list|)
function_decl|;
comment|/**      * Gets the dead letter channel      *      * @return  the uri of the dead letter channel      */
DECL|method|getDeadLetterUri ()
name|String
name|getDeadLetterUri
parameter_list|()
function_decl|;
comment|/**      * Sets an optional limit of the number of redelivery attempt of recovered {@link Exchange}      * should be attempted, before its exhausted.      *<p/>      * When this limit is hit, then the {@link Exchange} is moved to the dead letter channel.      *<p/>      * By default this option is disabled      *      * @param maximumRedeliveries the maximum redeliveries      */
DECL|method|setMaximumRedeliveries (int maximumRedeliveries)
name|void
name|setMaximumRedeliveries
parameter_list|(
name|int
name|maximumRedeliveries
parameter_list|)
function_decl|;
comment|/**      * Gets the maximum redelivery attempts to do before a recovered {@link Exchange} is doomed      * as exhausted and moved to the dead letter channel.      *      * @return the maximum redeliveries      */
DECL|method|getMaximumRedeliveries ()
name|int
name|getMaximumRedeliveries
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

