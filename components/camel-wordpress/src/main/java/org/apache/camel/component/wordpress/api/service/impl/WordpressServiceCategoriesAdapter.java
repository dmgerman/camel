begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Category
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
name|CategorySearchCriteria
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
name|service
operator|.
name|WordpressServiceCategories
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
name|CategoriesSPI
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

begin_class
DECL|class|WordpressServiceCategoriesAdapter
specifier|public
class|class
name|WordpressServiceCategoriesAdapter
extends|extends
name|AbstractWordpressCrudServiceAdapter
argument_list|<
name|CategoriesSPI
argument_list|,
name|Category
argument_list|,
name|CategorySearchCriteria
argument_list|>
implements|implements
name|WordpressServiceCategories
block|{
DECL|method|WordpressServiceCategoriesAdapter (String wordpressUrl, String apiVersion)
specifier|public
name|WordpressServiceCategoriesAdapter
parameter_list|(
name|String
name|wordpressUrl
parameter_list|,
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
name|CategoriesSPI
argument_list|>
name|getSpiType
parameter_list|()
block|{
return|return
name|CategoriesSPI
operator|.
name|class
return|;
block|}
comment|// @formatter:off
annotation|@
name|Override
DECL|method|list (CategorySearchCriteria criteria)
specifier|public
name|List
argument_list|<
name|Category
argument_list|>
name|list
parameter_list|(
name|CategorySearchCriteria
name|criteria
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|criteria
argument_list|,
literal|"The search criteria must be defined"
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
name|isHideEmpty
argument_list|()
argument_list|,
name|criteria
operator|.
name|getParent
argument_list|()
argument_list|,
name|criteria
operator|.
name|getPostId
argument_list|()
argument_list|,
name|criteria
operator|.
name|getSlug
argument_list|()
argument_list|)
return|;
block|}
comment|// @formatter:on
annotation|@
name|Override
DECL|method|doCreate (Category object)
specifier|protected
name|Category
name|doCreate
parameter_list|(
name|Category
name|object
parameter_list|)
block|{
return|return
name|getSpi
argument_list|()
operator|.
name|create
argument_list|(
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
name|Category
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
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doUpdate (Integer id, Category object)
specifier|protected
name|Category
name|doUpdate
parameter_list|(
name|Integer
name|id
parameter_list|,
name|Category
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
annotation|@
name|Override
DECL|method|doRetrieve (Integer entityID, Context context)
specifier|protected
name|Category
name|doRetrieve
parameter_list|(
name|Integer
name|entityID
parameter_list|,
name|Context
name|context
parameter_list|)
block|{
return|return
name|getSpi
argument_list|()
operator|.
name|retrieve
argument_list|(
name|getApiVersion
argument_list|()
argument_list|,
name|entityID
argument_list|,
name|context
argument_list|)
return|;
block|}
block|}
end_class

end_unit

