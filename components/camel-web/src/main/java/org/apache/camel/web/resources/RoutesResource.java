begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.resources
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|resources
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
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
name|model
operator|.
name|RouteDefinition
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
name|RoutesDefinition
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
name|view
operator|.
name|RouteDotGenerator
import|;
end_import

begin_comment
comment|/**  * The active routes in Camel which are used to implement one or more<a  * href="http://camel.apache.org/enterprise-integration-patterns.html"  *>Enterprise Integration Paterns</a>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RoutesResource
specifier|public
class|class
name|RoutesResource
extends|extends
name|CamelChildResourceSupport
block|{
DECL|method|RoutesResource (CamelContextResource contextResource)
specifier|public
name|RoutesResource
parameter_list|(
name|CamelContextResource
name|contextResource
parameter_list|)
block|{
name|super
argument_list|(
name|contextResource
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the routes currently active within this context      */
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|TEXT_XML
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|,
name|MediaType
operator|.
name|APPLICATION_JSON
block|}
argument_list|)
DECL|method|getRouteDefinitions ()
specifier|public
name|RoutesDefinition
name|getRouteDefinitions
parameter_list|()
block|{
name|RoutesDefinition
name|answer
init|=
operator|new
name|RoutesDefinition
argument_list|()
decl_stmt|;
name|CamelContext
name|camelContext
init|=
name|getCamelContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|camelContext
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|list
init|=
name|camelContext
operator|.
name|getRouteDefinitions
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setRoutes
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Returns the Graphviz DOT<a      * href="http://camel.apache.org/visualisation.html">Visualisation</a> of      * the current Camel routes      */
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
name|Constants
operator|.
name|DOT_MIMETYPE
argument_list|)
DECL|method|getDot ()
specifier|public
name|String
name|getDot
parameter_list|()
throws|throws
name|IOException
block|{
name|RouteDotGenerator
name|generator
init|=
operator|new
name|RouteDotGenerator
argument_list|(
literal|"/tmp/camel"
argument_list|)
decl_stmt|;
return|return
name|generator
operator|.
name|getRoutesText
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Looks up an individual route      */
annotation|@
name|Path
argument_list|(
literal|"{id}"
argument_list|)
DECL|method|getRoute (@athParamR) String id)
specifier|public
name|RouteResource
name|getRoute
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|)
block|{
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|list
init|=
name|getRoutes
argument_list|()
decl_stmt|;
for|for
control|(
name|RouteDefinition
name|routeType
range|:
name|list
control|)
block|{
if|if
condition|(
name|routeType
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
operator|new
name|RouteResource
argument_list|(
name|this
argument_list|,
name|routeType
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Looks up an individual route with specified language      */
annotation|@
name|Path
argument_list|(
literal|"{id}/{language}"
argument_list|)
DECL|method|getRoute (@athParamR) String id, @PathParam(R) String language)
specifier|public
name|RouteResource
name|getRoute
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"language"
argument_list|)
name|String
name|language
parameter_list|)
block|{
name|RouteResource
name|routeResource
init|=
name|getRoute
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|routeResource
operator|!=
literal|null
condition|)
block|{
name|routeResource
operator|.
name|setLanguage
argument_list|(
name|language
argument_list|)
expr_stmt|;
block|}
return|return
name|routeResource
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getRoutes ()
specifier|public
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|getRoutes
parameter_list|()
block|{
return|return
name|getRouteDefinitions
argument_list|()
operator|.
name|getRoutes
argument_list|()
return|;
block|}
block|}
end_class

end_unit

