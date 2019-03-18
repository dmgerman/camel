begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yammer.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yammer
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|annotate
operator|.
name|JsonIgnoreProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|annotate
operator|.
name|JsonProperty
import|;
end_import

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|Contact
specifier|public
class|class
name|Contact
block|{
annotation|@
name|JsonProperty
argument_list|(
literal|"email_addresses"
argument_list|)
DECL|field|emailAddresses
specifier|private
name|List
argument_list|<
name|EmailAddress
argument_list|>
name|emailAddresses
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"has_fake_email"
argument_list|)
DECL|field|hasFakeEmail
specifier|private
name|Boolean
name|hasFakeEmail
decl_stmt|;
DECL|field|im
specifier|private
name|Im
name|im
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"phone_numbers"
argument_list|)
DECL|field|phoneNumbers
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|phoneNumbers
decl_stmt|;
DECL|method|getEmailAddresses ()
specifier|public
name|List
argument_list|<
name|EmailAddress
argument_list|>
name|getEmailAddresses
parameter_list|()
block|{
return|return
name|emailAddresses
return|;
block|}
DECL|method|setEmailAddresses (List<EmailAddress> emailAddresses)
specifier|public
name|void
name|setEmailAddresses
parameter_list|(
name|List
argument_list|<
name|EmailAddress
argument_list|>
name|emailAddresses
parameter_list|)
block|{
name|this
operator|.
name|emailAddresses
operator|=
name|emailAddresses
expr_stmt|;
block|}
DECL|method|getHasFakeEmail ()
specifier|public
name|Boolean
name|getHasFakeEmail
parameter_list|()
block|{
return|return
name|hasFakeEmail
return|;
block|}
DECL|method|setHasFakeEmail (Boolean hasFakeEmail)
specifier|public
name|void
name|setHasFakeEmail
parameter_list|(
name|Boolean
name|hasFakeEmail
parameter_list|)
block|{
name|this
operator|.
name|hasFakeEmail
operator|=
name|hasFakeEmail
expr_stmt|;
block|}
DECL|method|getIm ()
specifier|public
name|Im
name|getIm
parameter_list|()
block|{
return|return
name|im
return|;
block|}
DECL|method|setIm (Im im)
specifier|public
name|void
name|setIm
parameter_list|(
name|Im
name|im
parameter_list|)
block|{
name|this
operator|.
name|im
operator|=
name|im
expr_stmt|;
block|}
DECL|method|getPhoneNumbers ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getPhoneNumbers
parameter_list|()
block|{
return|return
name|phoneNumbers
return|;
block|}
DECL|method|setPhoneNumbers (List<String> phoneNumbers)
specifier|public
name|void
name|setPhoneNumbers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|phoneNumbers
parameter_list|)
block|{
name|this
operator|.
name|phoneNumbers
operator|=
name|phoneNumbers
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Contact [emailAddresses="
operator|+
name|emailAddresses
operator|+
literal|", hasFakeEmail="
operator|+
name|hasFakeEmail
operator|+
literal|", im="
operator|+
name|im
operator|+
literal|", phoneNumbers="
operator|+
name|phoneNumbers
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

