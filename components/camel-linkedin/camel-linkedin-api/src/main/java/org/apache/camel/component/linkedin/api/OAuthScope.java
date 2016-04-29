begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * OAuth scope for use in {@link LinkedInOAuthRequestFilter}  */
end_comment

begin_enum
DECL|enum|OAuthScope
specifier|public
enum|enum
name|OAuthScope
block|{
DECL|enumConstant|R_BASICPROFILE
name|R_BASICPROFILE
argument_list|(
literal|"r_basicprofile"
argument_list|)
block|,
DECL|enumConstant|R_FULLPROFILE
name|R_FULLPROFILE
argument_list|(
literal|"r_fullprofile"
argument_list|)
block|,
DECL|enumConstant|R_EMAILADDRESS
name|R_EMAILADDRESS
argument_list|(
literal|"r_emailaddress"
argument_list|)
block|,
DECL|enumConstant|R_NETWORK
name|R_NETWORK
argument_list|(
literal|"r_network"
argument_list|)
block|,
DECL|enumConstant|R_CONTACTINFO
name|R_CONTACTINFO
argument_list|(
literal|"r_contactinfo"
argument_list|)
block|,
DECL|enumConstant|Deprecated
annotation|@
name|Deprecated
comment|// use W_SHARE instead
DECL|enumConstant|RW_NUS
name|RW_NUS
argument_list|(
literal|"rw_nus"
argument_list|)
block|,
DECL|enumConstant|RW_COMPANY_ADMIN
name|RW_COMPANY_ADMIN
argument_list|(
literal|"rw_company_admin"
argument_list|)
block|,
DECL|enumConstant|RW_GROUPS
name|RW_GROUPS
argument_list|(
literal|"rw_groups"
argument_list|)
block|,
DECL|enumConstant|W_MESSAGES
name|W_MESSAGES
argument_list|(
literal|"w_messages"
argument_list|)
block|,
DECL|enumConstant|W_SHARE
name|W_SHARE
argument_list|(
literal|"w_share"
argument_list|)
block|;
DECL|field|value
specifier|private
specifier|final
name|String
name|value
decl_stmt|;
DECL|method|OAuthScope (String value)
name|OAuthScope
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
DECL|method|getValue ()
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|fromValue (String value)
specifier|public
specifier|static
name|OAuthScope
name|fromValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
for|for
control|(
name|OAuthScope
name|scope
range|:
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|scope
operator|.
name|value
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|scope
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|value
argument_list|)
throw|;
block|}
DECL|method|fromValues (String... values)
specifier|public
specifier|static
name|OAuthScope
index|[]
name|fromValues
parameter_list|(
name|String
modifier|...
name|values
parameter_list|)
block|{
if|if
condition|(
name|values
operator|==
literal|null
operator|||
name|values
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
operator|new
name|OAuthScope
index|[
literal|0
index|]
return|;
block|}
specifier|final
name|OAuthScope
index|[]
name|result
init|=
operator|new
name|OAuthScope
index|[
name|values
operator|.
name|length
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|values
control|)
block|{
name|result
index|[
name|i
operator|++
index|]
operator|=
name|fromValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_enum

end_unit

