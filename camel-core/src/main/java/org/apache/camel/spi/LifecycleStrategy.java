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
name|Route
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
name|Service
import|;
end_import

begin_interface
DECL|interface|LifecycleStrategy
specifier|public
interface|interface
name|LifecycleStrategy
block|{
comment|/**      * Notification on adding a {@link CamelContext}.      */
DECL|method|onContextCreate (CamelContext context)
name|void
name|onContextCreate
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
comment|/**      * Notification on adding an {@link Endpoint}.      */
DECL|method|onEndpointAdd (Endpoint<? extends Exchange> endpoint)
name|void
name|onEndpointAdd
parameter_list|(
name|Endpoint
argument_list|<
name|?
extends|extends
name|Exchange
argument_list|>
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Notification on adding a {@link Service}.      */
DECL|method|onServiceAdd (CamelContext context, Service service)
name|void
name|onServiceAdd
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
parameter_list|)
function_decl|;
comment|/**      * Notification on adding {@link Route}(s).      */
DECL|method|onRoutesAdd (Collection<Route> routes)
name|void
name|onRoutesAdd
parameter_list|(
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
function_decl|;
comment|/**      * Notification on adding {@link RouteContext}(s).      *      * @param routeContext      */
DECL|method|onRouteContextCreate (RouteContext routeContext)
name|void
name|onRouteContextCreate
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

