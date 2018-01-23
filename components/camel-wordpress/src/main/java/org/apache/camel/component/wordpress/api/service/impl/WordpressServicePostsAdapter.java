begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress.api.service.impl
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
name|impl
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
name|Post
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
name|PostSearchCriteria
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
name|service
operator|.
name|WordpressServicePosts
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
name|service
operator|.
name|spi
operator|.
name|PostsSPI
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactory
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

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_comment
comment|/**  * The {@link WordpressServicePosts} implementation. Aggregates the {@link PostsSPI} interface using {@link JAXRSClientFactory} to make the API calls.  *   * @since 0.0.1  */
end_comment

begin_class
DECL|class|WordpressServicePostsAdapter
specifier|public
class|class
name|WordpressServicePostsAdapter
extends|extends
name|AbstractWordpressCrudServiceAdapter
argument_list|<
name|PostsSPI
argument_list|,
name|Post
argument_list|,
name|PostSearchCriteria
argument_list|>
implements|implements
name|WordpressServicePosts
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|WordpressServicePostsAdapter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|WordpressServicePostsAdapter (final String wordpressUrl, final String apiVersion)
specifier|public
name|WordpressServicePostsAdapter
parameter_list|(
specifier|final
name|String
name|wordpressUrl
parameter_list|,
specifier|final
name|String
name|apiVersion
parameter_list|)
block|{
name|super
argument_list|(
name|wordpressUrl
argument_list|,
name|apiVersion
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getSpiType ()
specifier|protected
name|Class
argument_list|<
name|PostsSPI
argument_list|>
name|getSpiType
parameter_list|()
block|{
return|return
name|PostsSPI
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|list (PostSearchCriteria criteria)
specifier|public
name|List
argument_list|<
name|Post
argument_list|>
name|list
parameter_list|(
name|PostSearchCriteria
name|criteria
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Calling list posts: searchCriteria {}"
argument_list|,
name|criteria
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|criteria
argument_list|,
literal|"Please provide a search criteria"
argument_list|)
expr_stmt|;
return|return
name|getSpi
argument_list|()
operator|.
name|list
argument_list|(
name|this
operator|.
name|getApiVersion
argument_list|()
argument_list|,
name|criteria
operator|.
name|getContext
argument_list|()
argument_list|,
name|criteria
operator|.
name|getPage
argument_list|()
argument_list|,
name|criteria
operator|.
name|getPerPage
argument_list|()
argument_list|,
name|criteria
operator|.
name|getSearch
argument_list|()
argument_list|,
name|criteria
operator|.
name|getAfter
argument_list|()
argument_list|,
name|criteria
operator|.
name|getAuthor
argument_list|()
argument_list|,
name|criteria
operator|.
name|getAuthorExclude
argument_list|()
argument_list|,
name|criteria
operator|.
name|getBefore
argument_list|()
argument_list|,
name|criteria
operator|.
name|getExclude
argument_list|()
argument_list|,
name|criteria
operator|.
name|getInclude
argument_list|()
argument_list|,
name|criteria
operator|.
name|getOffset
argument_list|()
argument_list|,
name|criteria
operator|.
name|getOrder
argument_list|()
argument_list|,
name|criteria
operator|.
name|getOrderBy
argument_list|()
argument_list|,
name|criteria
operator|.
name|getSlug
argument_list|()
argument_list|,
name|criteria
operator|.
name|getStatus
argument_list|()
argument_list|,
name|criteria
operator|.
name|getCategories
argument_list|()
argument_list|,
name|criteria
operator|.
name|getCategoriesExclude
argument_list|()
argument_list|,
name|criteria
operator|.
name|getTags
argument_list|()
argument_list|,
name|criteria
operator|.
name|getTagsExclude
argument_list|()
argument_list|,
name|criteria
operator|.
name|getStick
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|retrieve (Integer postId, Context context, String password)
specifier|public
name|Post
name|retrieve
parameter_list|(
name|Integer
name|postId
parameter_list|,
name|Context
name|context
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Calling retrievePosts: postId {};  postContext: {}"
argument_list|,
name|postId
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|postId
operator|>
literal|0
argument_list|,
literal|"Please provide a non zero post id"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|context
argument_list|,
literal|"Provide a post context"
argument_list|)
expr_stmt|;
return|return
name|getSpi
argument_list|()
operator|.
name|retrieve
argument_list|(
name|this
operator|.
name|getApiVersion
argument_list|()
argument_list|,
name|postId
argument_list|,
name|context
argument_list|,
name|password
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doRetrieve (Integer postId, Context context)
specifier|protected
name|Post
name|doRetrieve
parameter_list|(
name|Integer
name|postId
parameter_list|,
name|Context
name|context
parameter_list|)
block|{
return|return
name|this
operator|.
name|retrieve
argument_list|(
name|postId
argument_list|,
name|context
argument_list|,
literal|""
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|retrieve (Integer postId)
specifier|public
name|Post
name|retrieve
parameter_list|(
name|Integer
name|postId
parameter_list|)
block|{
return|return
name|this
operator|.
name|retrieve
argument_list|(
name|postId
argument_list|,
name|Context
operator|.
name|view
argument_list|,
literal|""
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doCreate (Post object)
specifier|protected
name|Post
name|doCreate
parameter_list|(
name|Post
name|object
parameter_list|)
block|{
return|return
name|getSpi
argument_list|()
operator|.
name|create
argument_list|(
name|this
operator|.
name|getApiVersion
argument_list|()
argument_list|,
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doDelete (Integer id)
specifier|protected
name|Post
name|doDelete
parameter_list|(
name|Integer
name|id
parameter_list|)
block|{
return|return
name|getSpi
argument_list|()
operator|.
name|delete
argument_list|(
name|getApiVersion
argument_list|()
argument_list|,
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doForceDelete (Integer id)
specifier|protected
name|DeletedModel
argument_list|<
name|Post
argument_list|>
name|doForceDelete
parameter_list|(
name|Integer
name|id
parameter_list|)
block|{
return|return
name|getSpi
argument_list|()
operator|.
name|forceDelete
argument_list|(
name|getApiVersion
argument_list|()
argument_list|,
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doUpdate (Integer id, Post object)
specifier|protected
name|Post
name|doUpdate
parameter_list|(
name|Integer
name|id
parameter_list|,
name|Post
name|object
parameter_list|)
block|{
return|return
name|getSpi
argument_list|()
operator|.
name|update
argument_list|(
name|getApiVersion
argument_list|()
argument_list|,
name|id
argument_list|,
name|object
argument_list|)
return|;
block|}
block|}
end_class

end_unit

