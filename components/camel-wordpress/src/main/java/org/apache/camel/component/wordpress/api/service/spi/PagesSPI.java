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
name|Date
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
name|Page
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
name|PageOrderBy
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
name|PublishableStatus
import|;
end_import

begin_comment
comment|/**  * Describes the Wordpress Pages API.  *   * @see<a href= "https://developer.wordpress.org/rest-api/reference/pages/">Pages API Reference</a>  * @since 0.0.1  */
end_comment

begin_interface
annotation|@
name|Path
argument_list|(
literal|"/wp"
argument_list|)
DECL|interface|PagesSPI
specifier|public
interface|interface
name|PagesSPI
block|{
comment|// @formatter:off
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/v{apiVersion}/pages"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
DECL|method|list (@athParamR) String apiVersion, @QueryParam(R) Context context, @QueryParam(R) Integer page, @QueryParam(R) Integer perPage, @QueryParam(R) String search, @QueryParam(R) Date after, @QueryParam(R) List<Integer> author, @QueryParam(R) List<Integer> authorExclude, @QueryParam(R) Date before, @QueryParam(R) List<Integer> exclude, @QueryParam(R) List<Integer> include, @QueryParam(R) Integer menuOrder, @QueryParam(R) List<Integer> offset, @QueryParam(R) Order order, @QueryParam(R) PageOrderBy orderBy, @QueryParam(R) Integer parent, @QueryParam(R) Integer parentExclude, @QueryParam(R) List<String> slug, @QueryParam(R) PublishableStatus status, @QueryParam(R) String filter)
name|List
argument_list|<
name|Page
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
literal|"after"
argument_list|)
name|Date
name|after
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"author"
argument_list|)
name|List
argument_list|<
name|Integer
argument_list|>
name|author
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"author_exclude"
argument_list|)
name|List
argument_list|<
name|Integer
argument_list|>
name|authorExclude
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"before"
argument_list|)
name|Date
name|before
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
literal|"menu_order"
argument_list|)
name|Integer
name|menuOrder
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
name|PageOrderBy
name|orderBy
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"parent"
argument_list|)
name|Integer
name|parent
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"parent_exclude"
argument_list|)
name|Integer
name|parentExclude
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
literal|"status"
argument_list|)
name|PublishableStatus
name|status
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"filter"
argument_list|)
name|String
name|filter
parameter_list|)
function_decl|;
comment|// @formatter:off
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/v{apiVersion}/pages/{pageId}"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
DECL|method|retrieve (@athParamR) String apiVersion, @PathParam(R) int pageId, @QueryParam(R) Context context, @QueryParam(R) String password)
name|Page
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
literal|"pageId"
argument_list|)
name|int
name|pageId
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
literal|"password"
argument_list|)
name|String
name|password
parameter_list|)
function_decl|;
comment|// @formatter:on
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/v{apiVersion}/pages"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
DECL|method|create (@athParamR) String apiVersion, Page page)
name|Page
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
name|Page
name|page
parameter_list|)
function_decl|;
comment|// @formatter:off
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/v{apiVersion}/pages/{pageId}"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
DECL|method|update (@athParamR) String apiVersion, @PathParam(R) int pageId, Page page)
name|Page
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
literal|"pageId"
argument_list|)
name|int
name|pageId
parameter_list|,
name|Page
name|page
parameter_list|)
function_decl|;
annotation|@
name|DELETE
annotation|@
name|Path
argument_list|(
literal|"/v{apiVersion}/pages/{pageId}"
argument_list|)
DECL|method|delete (@athParamR) String apiVersion, @PathParam(R) int pageId, @QueryParam(R) boolean force)
name|Page
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
literal|"pageId"
argument_list|)
name|int
name|pageId
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"force"
argument_list|)
name|boolean
name|force
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

