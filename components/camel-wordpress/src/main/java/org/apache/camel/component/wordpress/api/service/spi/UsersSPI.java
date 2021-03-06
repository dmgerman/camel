begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress.api.service.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|wordpress
operator|.
name|api
operator|.
name|service
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|DELETE
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
name|QueryParam
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
name|component
operator|.
name|wordpress
operator|.
name|api
operator|.
name|model
operator|.
name|Context
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
name|component
operator|.
name|wordpress
operator|.
name|api
operator|.
name|model
operator|.
name|DeletedModel
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
name|component
operator|.
name|wordpress
operator|.
name|api
operator|.
name|model
operator|.
name|Order
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
name|component
operator|.
name|wordpress
operator|.
name|api
operator|.
name|model
operator|.
name|User
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
name|component
operator|.
name|wordpress
operator|.
name|api
operator|.
name|model
operator|.
name|UserOrderBy
import|;
end_import

begin_comment
comment|/**  * Describes the Users Wordpress API  *   * @see<a href= "https://developer.wordpress.org/rest-api/reference/users/">Users API Reference</a>  * @since 0.0.1  */
end_comment

begin_interface
annotation|@
name|Path
argument_list|(
literal|"/wp"
argument_list|)
DECL|interface|UsersSPI
specifier|public
interface|interface
name|UsersSPI
block|{
comment|// @formatter:off
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/v{apiVersion}/users"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
DECL|method|list (@athParamR) String apiVersion, @QueryParam(R) Context context, @QueryParam(R) Integer page, @QueryParam(R) Integer perPage, @QueryParam(R) String search, @QueryParam(R) List<Integer> exclude, @QueryParam(R) List<Integer> include, @QueryParam(R) List<Integer> offset, @QueryParam(R) Order order, @QueryParam(R) UserOrderBy orderBy, @QueryParam(R) List<String> slug, @QueryParam(R) List<String> roles)
name|List
argument_list|<
name|User
argument_list|>
name|list
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"apiVersion"
argument_list|)
name|String
name|apiVersion
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"context"
argument_list|)
name|Context
name|context
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"page"
argument_list|)
name|Integer
name|page
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"per_page"
argument_list|)
name|Integer
name|perPage
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"search"
argument_list|)
name|String
name|search
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"exclude"
argument_list|)
name|List
argument_list|<
name|Integer
argument_list|>
name|exclude
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"include"
argument_list|)
name|List
argument_list|<
name|Integer
argument_list|>
name|include
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"offset"
argument_list|)
name|List
argument_list|<
name|Integer
argument_list|>
name|offset
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"order"
argument_list|)
name|Order
name|order
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"orderby"
argument_list|)
name|UserOrderBy
name|orderBy
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"slug"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|slug
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"roles"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|roles
parameter_list|)
function_decl|;
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/v{apiVersion}/users/{id}"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
DECL|method|retrieve (@athParamR) String apiVersion, @PathParam(R) Integer id, @QueryParam(R) Context context)
name|User
name|retrieve
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"apiVersion"
argument_list|)
name|String
name|apiVersion
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|Integer
name|id
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"context"
argument_list|)
name|Context
name|context
parameter_list|)
function_decl|;
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/v{apiVersion}/users"
argument_list|)
DECL|method|create (@athParamR) String apiVersion, User user)
name|User
name|create
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"apiVersion"
argument_list|)
name|String
name|apiVersion
parameter_list|,
name|User
name|user
parameter_list|)
function_decl|;
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/v{apiVersion}/users/{id}"
argument_list|)
DECL|method|update (@athParamR) String apiVersion, @PathParam(R) Integer id, User tag)
name|User
name|update
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"apiVersion"
argument_list|)
name|String
name|apiVersion
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|Integer
name|id
parameter_list|,
name|User
name|tag
parameter_list|)
function_decl|;
comment|/**      * @param apiVersion      * @param id Unique identifier for the user.      * @param force Required to be true, as users do not support trashing.      * @param reassignId Reassign the deleted user's posts and links to this user ID.      */
annotation|@
name|DELETE
annotation|@
name|Path
argument_list|(
literal|"/v{apiVersion}/users/{id}"
argument_list|)
DECL|method|delete (@athParamR) String apiVersion, @PathParam(R) Integer id, @QueryParam(R) boolean force, @QueryParam(R) Integer reassignId)
name|DeletedModel
argument_list|<
name|User
argument_list|>
name|delete
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"apiVersion"
argument_list|)
name|String
name|apiVersion
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|Integer
name|id
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"force"
argument_list|)
name|boolean
name|force
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"reassign"
argument_list|)
name|Integer
name|reassignId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

