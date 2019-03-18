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
comment|/** * LinkedIn OAuth Token */
end_comment

begin_class
DECL|class|OAuthToken
specifier|public
specifier|final
class|class
name|OAuthToken
block|{
DECL|field|refreshToken
specifier|private
specifier|final
name|String
name|refreshToken
decl_stmt|;
DECL|field|accessToken
specifier|private
specifier|final
name|String
name|accessToken
decl_stmt|;
DECL|field|expiryTime
specifier|private
name|long
name|expiryTime
decl_stmt|;
DECL|method|OAuthToken (String refreshToken, String accessToken, long expiryTime)
specifier|public
name|OAuthToken
parameter_list|(
name|String
name|refreshToken
parameter_list|,
name|String
name|accessToken
parameter_list|,
name|long
name|expiryTime
parameter_list|)
block|{
name|this
operator|.
name|refreshToken
operator|=
name|refreshToken
expr_stmt|;
name|this
operator|.
name|accessToken
operator|=
name|accessToken
expr_stmt|;
name|this
operator|.
name|expiryTime
operator|=
name|expiryTime
expr_stmt|;
block|}
DECL|method|getRefreshToken ()
specifier|public
name|String
name|getRefreshToken
parameter_list|()
block|{
return|return
name|refreshToken
return|;
block|}
DECL|method|getAccessToken ()
specifier|public
name|String
name|getAccessToken
parameter_list|()
block|{
return|return
name|accessToken
return|;
block|}
DECL|method|getExpiryTime ()
specifier|public
name|long
name|getExpiryTime
parameter_list|()
block|{
return|return
name|expiryTime
return|;
block|}
comment|// package method for testing only
DECL|method|setExpiryTime (long expiryTime)
name|void
name|setExpiryTime
parameter_list|(
name|long
name|expiryTime
parameter_list|)
block|{
name|this
operator|.
name|expiryTime
operator|=
name|expiryTime
expr_stmt|;
block|}
block|}
end_class

end_unit

