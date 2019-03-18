begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.linkedin.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|linkedin
operator|.
name|api
package|;
end_package

begin_comment
comment|/**  * Secure token storage for {@link OAuthToken}  */
end_comment

begin_interface
DECL|interface|OAuthSecureStorage
specifier|public
interface|interface
name|OAuthSecureStorage
block|{
comment|/**      * Get token from secure storage.      * @return null if a secure token doesn't exist and {@link LinkedInOAuthRequestFilter} must create one.      */
DECL|method|getOAuthToken ()
name|OAuthToken
name|getOAuthToken
parameter_list|()
function_decl|;
comment|/**      * Save token to secure storage.      * Only called when {@link LinkedInOAuthRequestFilter} creates one.      * @param newToken      */
DECL|method|saveOAuthToken (OAuthToken newToken)
name|void
name|saveOAuthToken
parameter_list|(
name|OAuthToken
name|newToken
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

