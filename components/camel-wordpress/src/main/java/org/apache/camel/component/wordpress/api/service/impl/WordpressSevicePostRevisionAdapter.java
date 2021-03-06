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
name|PostRevision
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
name|WordpressServicePostRevision
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
name|PostRevisionsSPI
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

begin_class
DECL|class|WordpressSevicePostRevisionAdapter
specifier|public
class|class
name|WordpressSevicePostRevisionAdapter
extends|extends
name|AbstractWordpressServiceAdapter
argument_list|<
name|PostRevisionsSPI
argument_list|>
implements|implements
name|WordpressServicePostRevision
block|{
DECL|method|WordpressSevicePostRevisionAdapter (final String wordpressUrl, final String apiVersion)
specifier|public
name|WordpressSevicePostRevisionAdapter
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
name|PostRevisionsSPI
argument_list|>
name|getSpiType
parameter_list|()
block|{
return|return
name|PostRevisionsSPI
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|delete (Integer postId, Integer revisionId)
specifier|public
name|void
name|delete
parameter_list|(
name|Integer
name|postId
parameter_list|,
name|Integer
name|revisionId
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|postId
operator|>
literal|0
argument_list|,
literal|"Please define a post id"
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|revisionId
operator|>
literal|0
argument_list|,
literal|"Please define a revision id"
argument_list|)
expr_stmt|;
name|this
operator|.
name|getSpi
argument_list|()
operator|.
name|delete
argument_list|(
name|this
operator|.
name|getApiVersion
argument_list|()
argument_list|,
name|postId
argument_list|,
name|revisionId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|retrieve (Integer postId, Integer revisionId, Context context)
specifier|public
name|PostRevision
name|retrieve
parameter_list|(
name|Integer
name|postId
parameter_list|,
name|Integer
name|revisionId
parameter_list|,
name|Context
name|context
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|postId
operator|>
literal|0
argument_list|,
literal|"Please define a post id"
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|revisionId
operator|>
literal|0
argument_list|,
literal|"Please define a revision id"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|getSpi
argument_list|()
operator|.
name|retrieveRevision
argument_list|(
name|this
operator|.
name|getApiVersion
argument_list|()
argument_list|,
name|postId
argument_list|,
name|revisionId
argument_list|,
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|list (Integer postId, Context context)
specifier|public
name|List
argument_list|<
name|PostRevision
argument_list|>
name|list
parameter_list|(
name|Integer
name|postId
parameter_list|,
name|Context
name|context
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|postId
operator|>
literal|0
argument_list|,
literal|"Please define a post id"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
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
name|postId
argument_list|,
name|context
argument_list|)
return|;
block|}
block|}
end_class

end_unit

