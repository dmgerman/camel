begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.auth
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|auth
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gdata
operator|.
name|client
operator|.
name|authn
operator|.
name|oauth
operator|.
name|OAuthParameters
import|;
end_import

begin_comment
comment|/**  * Interface to Google's OAuth services.   */
end_comment

begin_interface
DECL|interface|GAuthService
specifier|public
interface|interface
name|GAuthService
block|{
comment|/**      * Gets an unauthorized request token from Google.      *       * @param oauthParameters value object for providing input data and storing result data      *                        (unauthorized request token).      */
DECL|method|getUnauthorizedRequestToken (OAuthParameters oauthParameters)
name|void
name|getUnauthorizedRequestToken
parameter_list|(
name|OAuthParameters
name|oauthParameters
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Gets an access token from Google.      *       * @param oauthParameters value object for providing input data (authorized request token)      *                        and storing result data (access token).      */
DECL|method|getAccessToken (OAuthParameters oauthParameters)
name|void
name|getAccessToken
parameter_list|(
name|OAuthParameters
name|oauthParameters
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

