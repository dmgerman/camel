begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.rest.resources
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|rest
operator|.
name|resources
package|;
end_package

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
name|ImplicitProduces
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
name|spring
operator|.
name|Autowire
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
name|spi
operator|.
name|inject
operator|.
name|Inject
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
name|spi
operator|.
name|resource
operator|.
name|Singleton
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
name|model
operator|.
name|RouteType
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
name|RoutesType
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
name|rest
operator|.
name|model
operator|.
name|Camel
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
name|rest
operator|.
name|model
operator|.
name|EndpointLink
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
name|rest
operator|.
name|model
operator|.
name|Endpoints
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * The resource for the CamelContext  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
annotation|@
name|ImplicitProduces
argument_list|(
name|Constants
operator|.
name|HTML_MIME_TYPES
argument_list|)
annotation|@
name|Singleton
DECL|class|CamelContextResource
specifier|public
class|class
name|CamelContextResource
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|CamelContextResource (@nject CamelContext camelContext)
specifier|public
name|CamelContextResource
parameter_list|(
annotation|@
name|Inject
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|camelContext
operator|.
name|getName
argument_list|()
return|;
block|}
comment|// XML / JSON representations
comment|//-------------------------------------------------------------------------
annotation|@
name|GET
comment|// TODO we can replace this long expression with a static constant
comment|// when Jersey supports JAX-RS 1.1
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
DECL|method|getCamel ()
specifier|public
name|Camel
name|getCamel
parameter_list|()
block|{
return|return
operator|new
name|Camel
argument_list|(
name|camelContext
argument_list|)
return|;
block|}
comment|/**      * Returns a list of endpoints available in this context      *      * @return      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"endpoints"
argument_list|)
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
DECL|method|getEndpointsDTO ()
specifier|public
name|Endpoints
name|getEndpointsDTO
parameter_list|()
block|{
return|return
operator|new
name|Endpoints
argument_list|(
name|camelContext
argument_list|)
return|;
block|}
DECL|method|getEndpoints ()
specifier|public
name|List
argument_list|<
name|EndpointLink
argument_list|>
name|getEndpoints
parameter_list|()
block|{
return|return
name|getEndpointsDTO
argument_list|()
operator|.
name|getEndpoints
argument_list|()
return|;
block|}
comment|/**      * Looks up an individual endpoint      */
annotation|@
name|Path
argument_list|(
literal|"endpoint/{id}"
argument_list|)
DECL|method|getEndpoint (@athParamR) String id)
specifier|public
name|EndpointResource
name|getEndpoint
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
comment|// TODO lets assume the ID is the endpoint
name|Endpoint
name|endpoint
init|=
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|EndpointResource
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns the routes currently active within this context      *      * @return      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"routes"
argument_list|)
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
name|RoutesType
name|getRouteDefinitions
parameter_list|()
block|{
name|RoutesType
name|answer
init|=
operator|new
name|RoutesType
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
name|RouteType
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
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getRoutes ()
specifier|public
name|List
argument_list|<
name|RouteType
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

