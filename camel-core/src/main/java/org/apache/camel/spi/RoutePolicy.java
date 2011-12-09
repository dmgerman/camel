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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Route
import|;
end_import

begin_comment
comment|/**  * Policy for a {@link Route} which allows controlling the route at runtime.  *<p/>  * For example using the {@link org.apache.camel.impl.ThrottlingInflightRoutePolicy} to throttle the {@link Route}  * at runtime where it suspends and resume the {@link org.apache.camel.Route#getConsumer()}.  *  * @version   */
end_comment

begin_interface
DECL|interface|RoutePolicy
specifier|public
interface|interface
name|RoutePolicy
block|{
comment|/**      * Callback invoked when the {@link Route} is being initialized      *      * @param route     the route being initialized      */
DECL|method|onInit (Route route)
name|void
name|onInit
parameter_list|(
name|Route
name|route
parameter_list|)
function_decl|;
comment|/**      * Callback invoked when the {@link Route} is being removed from {@link org.apache.camel.CamelContext}      *      * @param route     the route being removed      */
DECL|method|onRemove (Route route)
name|void
name|onRemove
parameter_list|(
name|Route
name|route
parameter_list|)
function_decl|;
comment|/**      * Callback invoked when the {@link Route} is being started      *      * @param route     the route being started      */
DECL|method|onStart (Route route)
name|void
name|onStart
parameter_list|(
name|Route
name|route
parameter_list|)
function_decl|;
comment|/**      * Callback invoked when the {@link Route} is being stopped      *      * @param route     the route being stopped      */
DECL|method|onStop (Route route)
name|void
name|onStop
parameter_list|(
name|Route
name|route
parameter_list|)
function_decl|;
comment|/**      * Callback invoked when the {@link Route} is being suspended      *      * @param route     the route being suspended      */
DECL|method|onSuspend (Route route)
name|void
name|onSuspend
parameter_list|(
name|Route
name|route
parameter_list|)
function_decl|;
comment|/**      * Callback invoked when the {@link Route} is being resumed      *      * @param route     the route being resumed      */
DECL|method|onResume (Route route)
name|void
name|onResume
parameter_list|(
name|Route
name|route
parameter_list|)
function_decl|;
comment|/**      * Callback invoked when an {@link Exchange} is started being routed on the given {@link Route}      *      * @param route     the route where the exchange started from      * @param exchange  the created exchange      */
DECL|method|onExchangeBegin (Route route, Exchange exchange)
name|void
name|onExchangeBegin
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Callback invoked when an {@link Exchange} is done being routed, where it started from the given {@link Route}      *<p/>      * Notice this callback is invoked when the<b>Exchange</b> is done and the {@link Route} is the route where      * the {@link Exchange} was started. Most often its also the route where the exchange is done. However its      * possible to route an {@link Exchange} to other routes using endpoints such as      *<b>direct</b> or<b>seda</b>. Bottom line is that the {@link Route} parameter may not be the endpoint      * route and thus why we state its the starting route.      *      * @param route     the route where the exchange started from      * @param exchange  the created exchange      */
DECL|method|onExchangeDone (Route route, Exchange exchange)
name|void
name|onExchangeDone
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

