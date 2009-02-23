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
name|com
operator|.
name|sun
operator|.
name|jersey
operator|.
name|api
operator|.
name|representation
operator|.
name|Form
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
name|web
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
name|web
operator|.
name|model
operator|.
name|Endpoints
import|;
end_import

begin_comment
comment|/**  * The active endpoints in Camel  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|EndpointsResource
specifier|public
class|class
name|EndpointsResource
extends|extends
name|CamelChildResourceSupport
block|{
DECL|field|error
specifier|private
name|String
name|error
init|=
literal|""
decl_stmt|;
DECL|field|newUri
specifier|private
name|String
name|newUri
init|=
literal|"mock:someName"
decl_stmt|;
DECL|method|EndpointsResource (CamelContextResource contextResource)
specifier|public
name|EndpointsResource
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
comment|/**      * Returns a list of endpoints available in this context      *      * @return      */
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
DECL|method|getDTO ()
specifier|public
name|Endpoints
name|getDTO
parameter_list|()
block|{
return|return
operator|new
name|Endpoints
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns the resource of an individual Camel endpoint      *      * @param id the endpoints unique URI      */
annotation|@
name|Path
argument_list|(
literal|"{id}"
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
literal|null
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
comment|// lets remove any whitespace
name|id
operator|=
name|id
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|id
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|endpoint
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
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
name|getContextResource
argument_list|()
argument_list|,
name|id
argument_list|,
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
comment|// Creating endpoints
comment|//-------------------------------------------------------------------------
comment|/**      * Creates a new Endpoint for the given URI that is posted.      *      * @param uri the plain text URI of the endpoint to be created      */
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
DECL|method|postUri (String uri)
specifier|public
name|Response
name|postUri
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|EndpointResource
name|endpoint
init|=
name|getEndpoint
argument_list|(
name|uri
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
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
return|return
name|Response
operator|.
name|noContent
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Creates a new Endpoint for the given URI that is posted using form encoding with the<code>uri</code>      * value in the form being used to specify the endpoints unique URI.      *      * @param formData is the form data POSTed typically from a HTML form with the<code>uri</code> field used to create      *                 the endpoint      */
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
DECL|method|postUriForm (@ontext UriInfo uriInfo, Form formData)
specifier|public
name|Response
name|postUriForm
parameter_list|(
annotation|@
name|Context
name|UriInfo
name|uriInfo
parameter_list|,
name|Form
name|formData
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Received form! "
operator|+
name|formData
argument_list|)
expr_stmt|;
name|newUri
operator|=
name|formData
operator|.
name|getFirst
argument_list|(
literal|"uri"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|EndpointResource
name|endpoint
init|=
name|getEndpoint
argument_list|(
name|newUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|String
name|href
init|=
name|endpoint
operator|.
name|getHref
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Created endpoint so redirecting to "
operator|+
name|href
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
name|href
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
literal|"Could not find a component to resolve that URI"
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Failed to create new endpoint!"
argument_list|)
expr_stmt|;
comment|// lets re-render the form
return|return
name|Response
operator|.
name|ok
argument_list|(
operator|new
name|Viewable
argument_list|(
literal|"index"
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
comment|// Properties
comment|//-------------------------------------------------------------------------
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
name|getDTO
argument_list|()
operator|.
name|getEndpoints
argument_list|()
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
DECL|method|getNewUri ()
specifier|public
name|String
name|getNewUri
parameter_list|()
block|{
return|return
name|newUri
return|;
block|}
block|}
end_class

end_unit

