begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress.api.auth
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
name|auth
package|;
end_package

begin_comment
comment|/**  * Wordpress Authentication Mecanism needed to perform privileged actions like create, update or delete a post.  *   * @see<a href= "https://developer.wordpress.org/rest-api/using-the-rest-api/authentication/">Wordpress API Authentication</a>  * @since 0.1  */
end_comment

begin_interface
DECL|interface|WordpressAuthentication
specifier|public
interface|interface
name|WordpressAuthentication
block|{
DECL|method|setPassword (String pwd)
name|void
name|setPassword
parameter_list|(
name|String
name|pwd
parameter_list|)
function_decl|;
DECL|method|setUsername (String user)
name|void
name|setUsername
parameter_list|(
name|String
name|user
parameter_list|)
function_decl|;
DECL|method|getUsername ()
name|String
name|getUsername
parameter_list|()
function_decl|;
DECL|method|configureAuthentication (Object client)
name|void
name|configureAuthentication
parameter_list|(
name|Object
name|client
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

