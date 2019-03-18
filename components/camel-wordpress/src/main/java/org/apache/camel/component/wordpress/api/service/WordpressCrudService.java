begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress.api.service
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
name|SearchCriteria
import|;
end_import

begin_comment
comment|/**  * Common interface for services performing CRUD operations  *   * @param<T> model type  * @param<S> {@link SearchCriteria} used for this model  */
end_comment

begin_interface
DECL|interface|WordpressCrudService
specifier|public
interface|interface
name|WordpressCrudService
parameter_list|<
name|T
parameter_list|,
name|S
extends|extends
name|SearchCriteria
parameter_list|>
extends|extends
name|WordpressService
block|{
DECL|method|retrieve (Integer entityID, Context context)
name|T
name|retrieve
parameter_list|(
name|Integer
name|entityID
parameter_list|,
name|Context
name|context
parameter_list|)
function_decl|;
DECL|method|retrieve (Integer entityID)
name|T
name|retrieve
parameter_list|(
name|Integer
name|entityID
parameter_list|)
function_decl|;
DECL|method|create (T entity)
name|T
name|create
parameter_list|(
name|T
name|entity
parameter_list|)
function_decl|;
DECL|method|delete (Integer entityID)
name|T
name|delete
parameter_list|(
name|Integer
name|entityID
parameter_list|)
function_decl|;
DECL|method|forceDelete (Integer entityID)
name|DeletedModel
argument_list|<
name|T
argument_list|>
name|forceDelete
parameter_list|(
name|Integer
name|entityID
parameter_list|)
function_decl|;
DECL|method|list (S searchCriteria)
name|List
argument_list|<
name|T
argument_list|>
name|list
parameter_list|(
name|S
name|searchCriteria
parameter_list|)
function_decl|;
DECL|method|update (Integer entityID, T entity)
name|T
name|update
parameter_list|(
name|Integer
name|entityID
parameter_list|,
name|T
name|entity
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

