begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.spark
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|spark
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
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.example.spark.User} service which we rest enable from the {@link org.apache.camel.example.spark.UserRouteBuilder}.  */
end_comment

begin_class
DECL|class|UserService
specifier|public
class|class
name|UserService
block|{
comment|// use a tree map so they become sorted
DECL|field|users
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|User
argument_list|>
name|users
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|UserService ()
specifier|public
name|UserService
parameter_list|()
block|{
name|users
operator|.
name|put
argument_list|(
literal|"123"
argument_list|,
operator|new
name|User
argument_list|(
literal|123
argument_list|,
literal|"John Doe"
argument_list|)
argument_list|)
expr_stmt|;
name|users
operator|.
name|put
argument_list|(
literal|"456"
argument_list|,
operator|new
name|User
argument_list|(
literal|456
argument_list|,
literal|"Donald Duck"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets a user by the given id      *      * @param id  the id of the user      * @return the user, or<tt>null</tt> if no user exists      */
DECL|method|getUser (String id)
specifier|public
name|User
name|getUser
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|users
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
comment|/**      * List all users      *      * @return the list of all users      */
DECL|method|listUsers ()
specifier|public
name|Collection
argument_list|<
name|User
argument_list|>
name|listUsers
parameter_list|()
block|{
return|return
name|users
operator|.
name|values
argument_list|()
return|;
block|}
comment|/**      * Updates or creates the given user      *      * @param user the user      * @return the old user before it was updated, or<tt>null</tt> if creating a new user      */
DECL|method|updateUser (User user)
specifier|public
name|User
name|updateUser
parameter_list|(
name|User
name|user
parameter_list|)
block|{
return|return
name|users
operator|.
name|put
argument_list|(
literal|""
operator|+
name|user
operator|.
name|getId
argument_list|()
argument_list|,
name|user
argument_list|)
return|;
block|}
block|}
end_class

end_unit

