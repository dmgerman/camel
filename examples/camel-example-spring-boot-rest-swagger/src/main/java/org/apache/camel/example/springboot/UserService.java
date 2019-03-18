begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * Service interface for managing users.  */
end_comment

begin_interface
DECL|interface|UserService
specifier|public
interface|interface
name|UserService
block|{
comment|/**      * Find a user by the given ID      *      * @param id      *            the ID of the user      * @return the user, or<code>null</code> if user not found.      */
DECL|method|findUser (Integer id)
name|User
name|findUser
parameter_list|(
name|Integer
name|id
parameter_list|)
function_decl|;
comment|/**      * Find all users      *      * @return a collection of all users      */
DECL|method|findUsers ()
name|Collection
argument_list|<
name|User
argument_list|>
name|findUsers
parameter_list|()
function_decl|;
comment|/**      * Update the given user      *      * @param user      *            the user      */
DECL|method|updateUser (User user)
name|void
name|updateUser
parameter_list|(
name|User
name|user
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

