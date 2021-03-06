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
name|SearchCriteria
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
name|WordpressCrudService
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
comment|/**  * Base service adapter implementation with CRUD commons operations.  *   * @param<A>  * @param<T>  */
end_comment

begin_class
DECL|class|AbstractWordpressCrudServiceAdapter
specifier|abstract
class|class
name|AbstractWordpressCrudServiceAdapter
parameter_list|<
name|A
parameter_list|,
name|T
parameter_list|,
name|S
extends|extends
name|SearchCriteria
parameter_list|>
extends|extends
name|AbstractWordpressServiceAdapter
argument_list|<
name|A
argument_list|>
implements|implements
name|WordpressCrudService
argument_list|<
name|T
argument_list|,
name|S
argument_list|>
block|{
DECL|method|AbstractWordpressCrudServiceAdapter (final String wordpressUrl, final String apiVersion)
name|AbstractWordpressCrudServiceAdapter
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
DECL|method|create (T object)
specifier|public
specifier|final
name|T
name|create
parameter_list|(
name|T
name|object
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|object
argument_list|,
literal|"Please define an object to create"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|doCreate
argument_list|(
name|object
argument_list|)
return|;
block|}
DECL|method|doCreate (T object)
specifier|protected
specifier|abstract
name|T
name|doCreate
parameter_list|(
name|T
name|object
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|delete (Integer id)
specifier|public
specifier|final
name|T
name|delete
parameter_list|(
name|Integer
name|id
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|id
operator|>
literal|0
argument_list|,
literal|"The id is mandatory"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|doDelete
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|forceDelete (Integer id)
specifier|public
specifier|final
name|DeletedModel
argument_list|<
name|T
argument_list|>
name|forceDelete
parameter_list|(
name|Integer
name|id
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|id
operator|>
literal|0
argument_list|,
literal|"The id is mandatory"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|doForceDelete
argument_list|(
name|id
argument_list|)
return|;
block|}
DECL|method|doDelete (Integer id)
specifier|protected
specifier|abstract
name|T
name|doDelete
parameter_list|(
name|Integer
name|id
parameter_list|)
function_decl|;
DECL|method|doForceDelete (Integer id)
specifier|protected
name|DeletedModel
argument_list|<
name|T
argument_list|>
name|doForceDelete
parameter_list|(
name|Integer
name|id
parameter_list|)
block|{
specifier|final
name|DeletedModel
argument_list|<
name|T
argument_list|>
name|deletedModel
init|=
operator|new
name|DeletedModel
argument_list|<>
argument_list|()
decl_stmt|;
name|deletedModel
operator|.
name|setPrevious
argument_list|(
name|this
operator|.
name|doDelete
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|deletedModel
operator|.
name|setDeleted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|deletedModel
return|;
block|}
annotation|@
name|Override
DECL|method|update (Integer id, T object)
specifier|public
specifier|final
name|T
name|update
parameter_list|(
name|Integer
name|id
parameter_list|,
name|T
name|object
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|object
argument_list|,
literal|"Please define an object to update"
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|id
operator|>
literal|0
argument_list|,
literal|"The id is mandatory"
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|doUpdate
argument_list|(
name|id
argument_list|,
name|object
argument_list|)
return|;
block|}
DECL|method|doUpdate (Integer id, T object)
specifier|protected
specifier|abstract
name|T
name|doUpdate
parameter_list|(
name|Integer
name|id
parameter_list|,
name|T
name|object
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|retrieve (Integer entityID)
specifier|public
name|T
name|retrieve
parameter_list|(
name|Integer
name|entityID
parameter_list|)
block|{
return|return
name|this
operator|.
name|retrieve
argument_list|(
name|entityID
argument_list|,
name|Context
operator|.
name|view
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|retrieve (Integer entityID, Context context)
specifier|public
specifier|final
name|T
name|retrieve
parameter_list|(
name|Integer
name|entityID
parameter_list|,
name|Context
name|context
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|entityID
operator|>
literal|0
argument_list|,
literal|"Please provide a non zero id"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|context
argument_list|,
literal|"Provide a context"
argument_list|)
expr_stmt|;
comment|// return this.getSpi().retrieve(getApiVersion(), entityID, context);
return|return
name|doRetrieve
argument_list|(
name|entityID
argument_list|,
name|context
argument_list|)
return|;
block|}
DECL|method|doRetrieve (Integer entityID, Context context)
specifier|protected
specifier|abstract
name|T
name|doRetrieve
parameter_list|(
name|Integer
name|entityID
parameter_list|,
name|Context
name|context
parameter_list|)
function_decl|;
block|}
end_class

end_unit

