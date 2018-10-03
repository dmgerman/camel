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
name|Collection
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
name|Endpoint
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|StaticService
import|;
end_import

begin_comment
comment|/**  * A repository which tracks in flight {@link Exchange}s.  */
end_comment

begin_interface
DECL|interface|InflightRepository
specifier|public
interface|interface
name|InflightRepository
extends|extends
name|StaticService
block|{
comment|/**      * Information about the inflight exchange.      */
DECL|interface|InflightExchange
interface|interface
name|InflightExchange
block|{
comment|/**          * The exchange being inflight          */
DECL|method|getExchange ()
name|Exchange
name|getExchange
parameter_list|()
function_decl|;
comment|/**          * The duration in millis the exchange has been inflight          */
DECL|method|getDuration ()
name|long
name|getDuration
parameter_list|()
function_decl|;
comment|/**          * The elapsed time in millis processing the exchange at the current node          */
DECL|method|getElapsed ()
name|long
name|getElapsed
parameter_list|()
function_decl|;
comment|/**          * The id of the node from the route where the exchange currently is being processed          *<p/>          * Is<tt>null</tt> if message history is disabled.          */
DECL|method|getNodeId ()
name|String
name|getNodeId
parameter_list|()
function_decl|;
comment|/**          * The id of the route where the exchange originates (started)          */
DECL|method|getFromRouteId ()
name|String
name|getFromRouteId
parameter_list|()
function_decl|;
comment|/**          * The id of the route where the exchange currently is being processed          *<p/>          * Is<tt>null</tt> if message history is disabled.          */
DECL|method|getAtRouteId ()
name|String
name|getAtRouteId
parameter_list|()
function_decl|;
block|}
comment|/**      * Adds the exchange to the inflight registry to the total counter      *      * @param exchange  the exchange      */
DECL|method|add (Exchange exchange)
name|void
name|add
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Removes the exchange from the inflight registry to the total counter      *      * @param exchange  the exchange      */
DECL|method|remove (Exchange exchange)
name|void
name|remove
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Adds the exchange to the inflight registry associated to the given route      *      * @param exchange  the exchange      * @param routeId the id of the route      */
DECL|method|add (Exchange exchange, String routeId)
name|void
name|add
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|routeId
parameter_list|)
function_decl|;
comment|/**      * Removes the exchange from the inflight registry removing association to the given route      *      * @param exchange  the exchange      * @param routeId the id of the route      */
DECL|method|remove (Exchange exchange, String routeId)
name|void
name|remove
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|routeId
parameter_list|)
function_decl|;
comment|/**      * Current size of inflight exchanges.      *<p/>      * Will return 0 if there are no inflight exchanges.      *      * @return number of exchanges currently in flight.      */
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
comment|/**      * Adds the route from the in flight registry.      *<p/>      * Is used for initializing up resources      *      * @param routeId the id of the route      */
DECL|method|addRoute (String routeId)
name|void
name|addRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
function_decl|;
comment|/**      * Removes the route from the in flight registry.      *<p/>      * Is used for cleaning up resources to avoid leaking.      *      * @param routeId the id of the route      */
DECL|method|removeRoute (String routeId)
name|void
name|removeRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
function_decl|;
comment|/**     * Current size of inflight exchanges which are from the given route.      *<p/>      * Will return 0 if there are no inflight exchanges.      *      * @param routeId the id of the route      * @return number of exchanges currently in flight.      */
DECL|method|size (String routeId)
name|int
name|size
parameter_list|(
name|String
name|routeId
parameter_list|)
function_decl|;
comment|/**      * A<i>read-only</i> browser of the {@link InflightExchange}s that are currently inflight.      */
DECL|method|browse ()
name|Collection
argument_list|<
name|InflightExchange
argument_list|>
name|browse
parameter_list|()
function_decl|;
comment|/**      * A<i>read-only</i> browser of the {@link InflightExchange}s that are currently inflight that started from the given route.      *      * @param fromRouteId  the route id, or<tt>null</tt> for all routes.      */
DECL|method|browse (String fromRouteId)
name|Collection
argument_list|<
name|InflightExchange
argument_list|>
name|browse
parameter_list|(
name|String
name|fromRouteId
parameter_list|)
function_decl|;
comment|/**      * A<i>read-only</i> browser of the {@link InflightExchange}s that are currently inflight.      *      * @param limit maximum number of entries to return      * @param sortByLongestDuration to sort by the longest duration. Set to<tt>true</tt> to include the exchanges that has been inflight the longest time,      *                              set to<tt>false</tt> to sort by exchange id      */
DECL|method|browse (int limit, boolean sortByLongestDuration)
name|Collection
argument_list|<
name|InflightExchange
argument_list|>
name|browse
parameter_list|(
name|int
name|limit
parameter_list|,
name|boolean
name|sortByLongestDuration
parameter_list|)
function_decl|;
comment|/**      * A<i>read-only</i> browser of the {@link InflightExchange}s that are currently inflight that started from the given route.      *      * @param fromRouteId  the route id, or<tt>null</tt> for all routes.      * @param limit maximum number of entries to return      * @param sortByLongestDuration to sort by the longest duration. Set to<tt>true</tt> to include the exchanges that has been inflight the longest time,      *                              set to<tt>false</tt> to sort by exchange id      */
DECL|method|browse (String fromRouteId, int limit, boolean sortByLongestDuration)
name|Collection
argument_list|<
name|InflightExchange
argument_list|>
name|browse
parameter_list|(
name|String
name|fromRouteId
parameter_list|,
name|int
name|limit
parameter_list|,
name|boolean
name|sortByLongestDuration
parameter_list|)
function_decl|;
comment|/**      * Gets the oldest {@link InflightExchange} that are currently inflight that started from the given route.      *      * @param fromRouteId  the route id, or<tt>null</tt> for all routes.      * @return the oldest, or<tt>null</tt> if none inflight      */
DECL|method|oldest (String fromRouteId)
name|InflightExchange
name|oldest
parameter_list|(
name|String
name|fromRouteId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

