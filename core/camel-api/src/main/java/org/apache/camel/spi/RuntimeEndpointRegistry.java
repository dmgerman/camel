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
name|List
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
comment|/**  * A registry which listen for runtime usage of {@link org.apache.camel.Endpoint} during routing in Camel.  */
end_comment

begin_interface
DECL|interface|RuntimeEndpointRegistry
specifier|public
interface|interface
name|RuntimeEndpointRegistry
extends|extends
name|StaticService
block|{
comment|/**      * Statistics gathered about the endpoint.      */
DECL|interface|Statistic
interface|interface
name|Statistic
block|{
comment|/**          * The endpoint uri          */
DECL|method|getUri ()
name|String
name|getUri
parameter_list|()
function_decl|;
comment|/**          * The route id (if the endpoint is associated with a route)          */
DECL|method|getRouteId ()
name|String
name|getRouteId
parameter_list|()
function_decl|;
comment|/**          * Whether the endpoint is used as input our output          *<p/>          * The returned value can either be<tt>in</tt> or<tt>out</tt>          */
DECL|method|getDirection ()
name|String
name|getDirection
parameter_list|()
function_decl|;
comment|/**          * Usage of the endpoint, such as how many messages it has received / sent to          *<p/>          * This information is only available if {@link org.apache.camel.ManagementStatisticsLevel} is configured as          * {@link org.apache.camel.ManagementStatisticsLevel#Extended}.          */
DECL|method|getHits ()
name|long
name|getHits
parameter_list|()
function_decl|;
block|}
comment|/**      * Whether gathering runtime usage is enabled or not.      */
DECL|method|isEnabled ()
name|boolean
name|isEnabled
parameter_list|()
function_decl|;
comment|/**      * Sets whether gathering runtime usage is enabled or not.      */
DECL|method|setEnabled (boolean enabled)
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
function_decl|;
comment|/**      * Maximum number of endpoints to keep in the cache per route.      *<p/>      * The default value is<tt>1000</tt>      */
DECL|method|getLimit ()
name|int
name|getLimit
parameter_list|()
function_decl|;
comment|/**      * Sets the maximum number of endpoints to keep in the cache per route.      */
DECL|method|setLimit (int limit)
name|void
name|setLimit
parameter_list|(
name|int
name|limit
parameter_list|)
function_decl|;
comment|/**      * Clears the registry      */
DECL|method|clear ()
name|void
name|clear
parameter_list|()
function_decl|;
comment|/**      * Reset the statistic counters      */
DECL|method|reset ()
name|void
name|reset
parameter_list|()
function_decl|;
comment|/**      * Number of endpoints currently in the cache.      */
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
comment|/**      * Gets all the endpoint uris captured during runtime routing that are in-use of the routes.      *      * @param includeInputs whether to include route inputs      */
DECL|method|getAllEndpoints (boolean includeInputs)
name|List
argument_list|<
name|String
argument_list|>
name|getAllEndpoints
parameter_list|(
name|boolean
name|includeInputs
parameter_list|)
function_decl|;
comment|/**      * Gets all the endpoint uris captured from the given route during runtime routing that are in-use of the routes.      *      * @param routeId       the route id      * @param includeInputs whether to include route inputs      */
DECL|method|getEndpointsPerRoute (String routeId, boolean includeInputs)
name|List
argument_list|<
name|String
argument_list|>
name|getEndpointsPerRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|boolean
name|includeInputs
parameter_list|)
function_decl|;
comment|/**      * Gets details about all the endpoint captured from the given route during runtime routing that are in-use of the routes.      */
DECL|method|getEndpointStatistics ()
name|List
argument_list|<
name|Statistic
argument_list|>
name|getEndpointStatistics
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

