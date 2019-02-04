begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.cluster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|cluster
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
name|model
operator|.
name|RouteDefinition
import|;
end_import

begin_interface
annotation|@
name|FunctionalInterface
DECL|interface|ClusteredRouteFilter
specifier|public
interface|interface
name|ClusteredRouteFilter
block|{
comment|/**      * Test if the route should be clustered or not.      *      * @param camelContext  the camel context      * @param routeId the route id      * @param route the route definition      * @return true if the route should be included      */
DECL|method|test (CamelContext camelContext, String routeId, RouteDefinition route)
name|boolean
name|test
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|routeId
parameter_list|,
name|RouteDefinition
name|route
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

