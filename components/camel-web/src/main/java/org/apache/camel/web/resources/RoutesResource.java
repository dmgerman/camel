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
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Consumes
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
name|FormParam
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
name|POST
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
name|Context
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|UriInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|api
operator|.
name|view
operator|.
name|Viewable
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
name|ModelCamelContext
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
name|util
operator|.
name|ObjectHelper
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|model
operator|.
name|Route
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * The active routes in Camel which are used to implement one or more<a  * href="http://camel.apache.org/enterprise-integration-patterns.html"  *>Enterprise Integration Patterns</a>  */
end_comment

begin_class
DECL|class|RoutesResource
specifier|public
class|class
name|RoutesResource
extends|extends
name|CamelChildResourceSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RoutesResource
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|error
specifier|private
name|String
name|error
init|=
literal|""
decl_stmt|;
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
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
name|getRouteDefinitions
argument_list|()
operator|.
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
literal|"{id}/lang/{language}"
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
annotation|@
name|Path
argument_list|(
literal|"{id}/status"
argument_list|)
DECL|method|getRouteStatus (@athParamR) String id)
specifier|public
name|RouteStatusResource
name|getRouteStatus
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
name|RouteResource
name|routeResource
init|=
name|getRoute
argument_list|(
name|id
argument_list|)
decl_stmt|;
return|return
name|routeResource
operator|.
name|getRouteStatus
argument_list|()
return|;
block|}
comment|/**      * Creates a new route using form encoded data from a web form      *       * @param language is the edited language used on this route      * @param body the route definition content      */
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
DECL|method|postRouteForm (@ontext UriInfo uriInfo, @FormParam(R) String language, @FormParam(R) String body)
specifier|public
name|Response
name|postRouteForm
parameter_list|(
annotation|@
name|Context
name|UriInfo
name|uriInfo
parameter_list|,
annotation|@
name|FormParam
argument_list|(
literal|"language"
argument_list|)
name|String
name|language
parameter_list|,
annotation|@
name|FormParam
argument_list|(
literal|"route"
argument_list|)
name|String
name|body
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"New Route is: {}"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
name|body
argument_list|)
expr_stmt|;
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
name|error
operator|=
literal|"No Route submitted!"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|language
operator|.
name|equals
argument_list|(
name|RouteResource
operator|.
name|LANGUAGE_XML
argument_list|)
condition|)
block|{
return|return
name|parseXml
argument_list|(
name|body
argument_list|)
return|;
block|}
name|error
operator|=
literal|"Not supported language!"
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
operator|new
name|Viewable
argument_list|(
literal|"edit"
argument_list|,
name|this
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getRoutes ()
specifier|public
name|List
argument_list|<
name|Route
argument_list|>
name|getRoutes
parameter_list|()
block|{
name|List
argument_list|<
name|Route
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Route
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|RouteDefinition
name|def
range|:
name|getRouteDefinitions
argument_list|()
operator|.
name|getRoutes
argument_list|()
control|)
block|{
name|Route
name|route
init|=
operator|new
name|Route
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|def
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getError ()
specifier|public
name|String
name|getError
parameter_list|()
block|{
return|return
name|error
return|;
block|}
comment|/**      * process the route configuration defined in Xml      */
DECL|method|parseXml (String xml)
specifier|private
name|Response
name|parseXml
parameter_list|(
name|String
name|xml
parameter_list|)
block|{
try|try
block|{
name|JAXBContext
name|context
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|Constants
operator|.
name|JAXB_PACKAGES
argument_list|)
decl_stmt|;
name|Unmarshaller
name|unmarshaller
init|=
name|context
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
operator|new
name|StringReader
argument_list|(
name|xml
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|RouteDefinition
condition|)
block|{
name|RouteDefinition
name|routeDefinition
init|=
operator|(
name|RouteDefinition
operator|)
name|value
decl_stmt|;
comment|// add the route
operator|(
operator|(
name|ModelCamelContext
operator|)
name|getCamelContext
argument_list|()
operator|)
operator|.
name|addRouteDefinitions
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|routeDefinition
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|seeOther
argument_list|(
operator|new
name|URI
argument_list|(
literal|"/routes"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
else|else
block|{
name|error
operator|=
literal|"Posted XML is not a route but is of type "
operator|+
name|ObjectHelper
operator|.
name|className
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
name|error
operator|=
literal|"Failed to parse XML: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|error
operator|=
literal|"Failed to install route: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
expr_stmt|;
block|}
comment|// lets re-render the form
return|return
name|Response
operator|.
name|ok
argument_list|(
operator|new
name|Viewable
argument_list|(
literal|"create"
argument_list|,
name|this
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

