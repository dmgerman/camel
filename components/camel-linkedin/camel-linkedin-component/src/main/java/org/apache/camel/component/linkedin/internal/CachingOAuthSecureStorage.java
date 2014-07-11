begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.linkedin.internal
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
name|internal
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
name|linkedin
operator|.
name|api
operator|.
name|OAuthSecureStorage
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
name|linkedin
operator|.
name|api
operator|.
name|OAuthToken
import|;
end_import

begin_comment
comment|/**  * Caching implementation of {@link org.apache.camel.component.linkedin.api.OAuthSecureStorage}  */
end_comment

begin_class
DECL|class|CachingOAuthSecureStorage
specifier|public
class|class
name|CachingOAuthSecureStorage
implements|implements
name|OAuthSecureStorage
block|{
DECL|field|secureStorage
specifier|private
specifier|final
name|OAuthSecureStorage
name|secureStorage
decl_stmt|;
DECL|field|token
specifier|private
name|OAuthToken
name|token
decl_stmt|;
DECL|method|CachingOAuthSecureStorage (OAuthSecureStorage secureStorage)
specifier|public
name|CachingOAuthSecureStorage
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
annotation|@
name|Override
DECL|method|getOAuthToken ()
specifier|public
name|OAuthToken
name|getOAuthToken
parameter_list|()
block|{
comment|// delegate only if token doesn't exist or has expired
if|if
condition|(
name|secureStorage
operator|!=
literal|null
operator|&&
operator|(
name|token
operator|==
literal|null
operator|||
name|token
operator|.
name|getExpiryTime
argument_list|()
operator|<
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|)
condition|)
block|{
name|token
operator|=
name|secureStorage
operator|.
name|getOAuthToken
argument_list|()
expr_stmt|;
block|}
return|return
name|token
return|;
block|}
annotation|@
name|Override
DECL|method|saveOAuthToken (OAuthToken newToken)
specifier|public
name|void
name|saveOAuthToken
parameter_list|(
name|OAuthToken
name|newToken
parameter_list|)
block|{
name|token
operator|=
name|newToken
expr_stmt|;
if|if
condition|(
name|secureStorage
operator|!=
literal|null
condition|)
block|{
name|secureStorage
operator|.
name|saveOAuthToken
argument_list|(
name|newToken
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

