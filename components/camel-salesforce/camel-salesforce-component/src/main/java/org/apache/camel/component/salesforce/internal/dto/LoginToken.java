begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.dto
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|internal
operator|.
name|dto
package|;
end_package

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonIgnoreProperties
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonProperty
import|;
end_import

begin_comment
comment|/**  * DTO for Salesforce login  */
end_comment

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|LoginToken
specifier|public
class|class
name|LoginToken
block|{
DECL|field|accessToken
specifier|private
name|String
name|accessToken
decl_stmt|;
DECL|field|instanceUrl
specifier|private
name|String
name|instanceUrl
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|signature
specifier|private
name|String
name|signature
decl_stmt|;
DECL|field|issuedAt
specifier|private
name|String
name|issuedAt
decl_stmt|;
DECL|field|tokenType
specifier|private
name|String
name|tokenType
decl_stmt|;
DECL|field|isReadOnly
specifier|private
name|String
name|isReadOnly
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"access_token"
argument_list|)
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
annotation|@
name|JsonProperty
argument_list|(
literal|"access_token"
argument_list|)
DECL|method|setAccessToken (String accessToken)
specifier|public
name|void
name|setAccessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
block|{
name|this
operator|.
name|accessToken
operator|=
name|accessToken
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"instance_url"
argument_list|)
DECL|method|getInstanceUrl ()
specifier|public
name|String
name|getInstanceUrl
parameter_list|()
block|{
return|return
name|instanceUrl
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"instance_url"
argument_list|)
DECL|method|setInstanceUrl (String instanceUrl)
specifier|public
name|void
name|setInstanceUrl
parameter_list|(
name|String
name|instanceUrl
parameter_list|)
block|{
name|this
operator|.
name|instanceUrl
operator|=
name|instanceUrl
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getSignature ()
specifier|public
name|String
name|getSignature
parameter_list|()
block|{
return|return
name|signature
return|;
block|}
DECL|method|setSignature (String signature)
specifier|public
name|void
name|setSignature
parameter_list|(
name|String
name|signature
parameter_list|)
block|{
name|this
operator|.
name|signature
operator|=
name|signature
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"issued_at"
argument_list|)
DECL|method|getIssuedAt ()
specifier|public
name|String
name|getIssuedAt
parameter_list|()
block|{
return|return
name|issuedAt
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"issued_at"
argument_list|)
DECL|method|setIssuedAt (String issuedAt)
specifier|public
name|void
name|setIssuedAt
parameter_list|(
name|String
name|issuedAt
parameter_list|)
block|{
name|this
operator|.
name|issuedAt
operator|=
name|issuedAt
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"token_type"
argument_list|)
DECL|method|getTokenType ()
specifier|public
name|String
name|getTokenType
parameter_list|()
block|{
return|return
name|tokenType
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"token_type"
argument_list|)
DECL|method|setTokenType (String tokenType)
specifier|public
name|void
name|setTokenType
parameter_list|(
name|String
name|tokenType
parameter_list|)
block|{
name|this
operator|.
name|tokenType
operator|=
name|tokenType
expr_stmt|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"is_readonly"
argument_list|)
DECL|method|getIsReadOnly ()
specifier|public
name|String
name|getIsReadOnly
parameter_list|()
block|{
return|return
name|isReadOnly
return|;
block|}
annotation|@
name|JsonProperty
argument_list|(
literal|"is_readonly"
argument_list|)
DECL|method|setIsReadOnly (String isReadOnly)
specifier|public
name|void
name|setIsReadOnly
parameter_list|(
name|String
name|isReadOnly
parameter_list|)
block|{
name|this
operator|.
name|isReadOnly
operator|=
name|isReadOnly
expr_stmt|;
block|}
block|}
end_class

end_unit

