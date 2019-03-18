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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_comment
comment|/**  * Parameters for OAuth 2.0 flow used by {@link LinkedInOAuthRequestFilter}. */
end_comment

begin_class
DECL|class|OAuthParams
specifier|public
class|class
name|OAuthParams
block|{
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
DECL|field|userPassword
specifier|private
name|String
name|userPassword
decl_stmt|;
DECL|field|secureStorage
specifier|private
name|OAuthSecureStorage
name|secureStorage
decl_stmt|;
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
DECL|field|clientSecret
specifier|private
name|String
name|clientSecret
decl_stmt|;
DECL|field|scopes
specifier|private
name|OAuthScope
index|[]
name|scopes
decl_stmt|;
DECL|field|redirectUri
specifier|private
name|String
name|redirectUri
decl_stmt|;
DECL|method|OAuthParams ()
specifier|public
name|OAuthParams
parameter_list|()
block|{     }
DECL|method|OAuthParams (String userName, String userPassword, OAuthSecureStorage secureStorage, String clientId, String clientSecret, String redirectUri, OAuthScope... scopes)
specifier|public
name|OAuthParams
parameter_list|(
name|String
name|userName
parameter_list|,
name|String
name|userPassword
parameter_list|,
name|OAuthSecureStorage
name|secureStorage
parameter_list|,
name|String
name|clientId
parameter_list|,
name|String
name|clientSecret
parameter_list|,
name|String
name|redirectUri
parameter_list|,
name|OAuthScope
modifier|...
name|scopes
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
name|this
operator|.
name|userPassword
operator|=
name|userPassword
expr_stmt|;
name|this
operator|.
name|secureStorage
operator|=
name|secureStorage
expr_stmt|;
name|this
operator|.
name|clientId
operator|=
name|clientId
expr_stmt|;
name|this
operator|.
name|clientSecret
operator|=
name|clientSecret
expr_stmt|;
name|this
operator|.
name|scopes
operator|=
name|scopes
operator|!=
literal|null
condition|?
name|Arrays
operator|.
name|copyOf
argument_list|(
name|scopes
argument_list|,
name|scopes
operator|.
name|length
argument_list|)
else|:
literal|null
expr_stmt|;
name|this
operator|.
name|redirectUri
operator|=
name|redirectUri
expr_stmt|;
block|}
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
DECL|method|setUserName (String userName)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
block|}
DECL|method|getUserPassword ()
specifier|public
name|String
name|getUserPassword
parameter_list|()
block|{
return|return
name|userPassword
return|;
block|}
DECL|method|setUserPassword (String userPassword)
specifier|public
name|void
name|setUserPassword
parameter_list|(
name|String
name|userPassword
parameter_list|)
block|{
name|this
operator|.
name|userPassword
operator|=
name|userPassword
expr_stmt|;
block|}
DECL|method|getClientId ()
specifier|public
name|String
name|getClientId
parameter_list|()
block|{
return|return
name|clientId
return|;
block|}
DECL|method|setClientId (String clientId)
specifier|public
name|void
name|setClientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|this
operator|.
name|clientId
operator|=
name|clientId
expr_stmt|;
block|}
DECL|method|getScopes ()
specifier|public
name|OAuthScope
index|[]
name|getScopes
parameter_list|()
block|{
return|return
name|scopes
return|;
block|}
DECL|method|setScopes (OAuthScope[] scopes)
specifier|public
name|void
name|setScopes
parameter_list|(
name|OAuthScope
index|[]
name|scopes
parameter_list|)
block|{
name|this
operator|.
name|scopes
operator|=
name|scopes
expr_stmt|;
block|}
DECL|method|getRedirectUri ()
specifier|public
name|String
name|getRedirectUri
parameter_list|()
block|{
return|return
name|redirectUri
return|;
block|}
DECL|method|setRedirectUri (String redirectUri)
specifier|public
name|void
name|setRedirectUri
parameter_list|(
name|String
name|redirectUri
parameter_list|)
block|{
name|this
operator|.
name|redirectUri
operator|=
name|redirectUri
expr_stmt|;
block|}
DECL|method|getClientSecret ()
specifier|public
name|String
name|getClientSecret
parameter_list|()
block|{
return|return
name|clientSecret
return|;
block|}
DECL|method|setClientSecret (String clientSecret)
specifier|public
name|void
name|setClientSecret
parameter_list|(
name|String
name|clientSecret
parameter_list|)
block|{
name|this
operator|.
name|clientSecret
operator|=
name|clientSecret
expr_stmt|;
block|}
DECL|method|getSecureStorage ()
specifier|public
name|OAuthSecureStorage
name|getSecureStorage
parameter_list|()
block|{
return|return
name|secureStorage
return|;
block|}
DECL|method|setSecureStorage (OAuthSecureStorage secureStorage)
specifier|public
name|void
name|setSecureStorage
parameter_list|(
name|OAuthSecureStorage
name|secureStorage
parameter_list|)
block|{
name|this
operator|.
name|secureStorage
operator|=
name|secureStorage
expr_stmt|;
block|}
block|}
end_class

end_unit

