begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|login
operator|.
name|LoginException
import|;
end_import

begin_comment
comment|/**  * A {@link SecurityAuthenticator} allows to plugin custom authenticators,  * such as JAAS based or custom implementations.  */
end_comment

begin_interface
DECL|interface|SecurityAuthenticator
specifier|public
interface|interface
name|SecurityAuthenticator
block|{
comment|/**      * Sets the name of the realm to use.      */
DECL|method|setName (String name)
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Gets the name of the realm.      */
DECL|method|getName ()
name|String
name|getName
parameter_list|()
function_decl|;
DECL|method|setRoleClassNames (String names)
name|void
name|setRoleClassNames
parameter_list|(
name|String
name|names
parameter_list|)
function_decl|;
comment|/**      * Attempts to login the {@link java.security.Principal} on this realm.      *<p/>      * The login is a success if no Exception is thrown, and a {@link Subject} is returned.      *      * @param principal       the principal      * @return the subject for the logged in principal, must<b>not</b> be<tt>null</tt>      * @throws LoginException is thrown if error logging in the {@link java.security.Principal}      */
DECL|method|login (HttpPrincipal principal)
name|Subject
name|login
parameter_list|(
name|HttpPrincipal
name|principal
parameter_list|)
throws|throws
name|LoginException
function_decl|;
comment|/**      * Attempt to logout the subject.      *      * @param subject  subject to logout      * @throws LoginException is thrown if error logging out subject      */
DECL|method|logout (Subject subject)
name|void
name|logout
parameter_list|(
name|Subject
name|subject
parameter_list|)
throws|throws
name|LoginException
function_decl|;
comment|/**      * Gets the user roles from the given {@link Subject}      *      * @param subject the subject      * @return<tt>null</tt> if no roles, otherwise a String with roles separated by comma.      */
DECL|method|getUserRoles (Subject subject)
name|String
name|getUserRoles
parameter_list|(
name|Subject
name|subject
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

