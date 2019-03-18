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
name|Map
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
name|Taxonomy
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
name|WordpressServiceTaxonomy
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
name|TaxonomySPI
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
name|Strings
operator|.
name|emptyToNull
import|;
end_import

begin_class
DECL|class|WordpressServiceTaxonomyAdapter
specifier|public
class|class
name|WordpressServiceTaxonomyAdapter
extends|extends
name|AbstractWordpressServiceAdapter
argument_list|<
name|TaxonomySPI
argument_list|>
implements|implements
name|WordpressServiceTaxonomy
block|{
DECL|method|WordpressServiceTaxonomyAdapter (String wordpressUrl, String apiVersion)
specifier|public
name|WordpressServiceTaxonomyAdapter
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
name|TaxonomySPI
argument_list|>
name|getSpiType
parameter_list|()
block|{
return|return
name|TaxonomySPI
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|list (Context context, String postType)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Taxonomy
argument_list|>
name|list
parameter_list|(
name|Context
name|context
parameter_list|,
name|String
name|postType
parameter_list|)
block|{
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
name|context
argument_list|,
name|postType
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|retrieve (Context context, String taxonomy)
specifier|public
name|Taxonomy
name|retrieve
parameter_list|(
name|Context
name|context
parameter_list|,
name|String
name|taxonomy
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|emptyToNull
argument_list|(
name|taxonomy
argument_list|)
argument_list|,
literal|"Please define a taxonomy"
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
name|context
argument_list|,
name|taxonomy
argument_list|)
return|;
block|}
block|}
end_class

end_unit

