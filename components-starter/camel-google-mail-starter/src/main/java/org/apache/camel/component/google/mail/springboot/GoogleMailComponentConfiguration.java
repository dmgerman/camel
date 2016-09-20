begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.mail.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|mail
operator|.
name|springboot
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|mail
operator|.
name|GoogleMailClientFactory
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
name|google
operator|.
name|mail
operator|.
name|internal
operator|.
name|GoogleMailApiName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * The google-mail component provides access to Google Mail.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.google-mail"
argument_list|)
DECL|class|GoogleMailComponentConfiguration
specifier|public
class|class
name|GoogleMailComponentConfiguration
block|{
comment|/**      * To use the shared configuration      */
DECL|field|configuration
specifier|private
name|GoogleMailConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * To use the GoogleCalendarClientFactory as factory for creating the      * client. Will by default use BatchGoogleMailClientFactory      */
annotation|@
name|NestedConfigurationProperty
DECL|field|clientFactory
specifier|private
name|GoogleMailClientFactory
name|clientFactory
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|GoogleMailConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( GoogleMailConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|GoogleMailConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getClientFactory ()
specifier|public
name|GoogleMailClientFactory
name|getClientFactory
parameter_list|()
block|{
return|return
name|clientFactory
return|;
block|}
DECL|method|setClientFactory (GoogleMailClientFactory clientFactory)
specifier|public
name|void
name|setClientFactory
parameter_list|(
name|GoogleMailClientFactory
name|clientFactory
parameter_list|)
block|{
name|this
operator|.
name|clientFactory
operator|=
name|clientFactory
expr_stmt|;
block|}
DECL|class|GoogleMailConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|GoogleMailConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|mail
operator|.
name|GoogleMailConfiguration
operator|.
name|class
decl_stmt|;
comment|/**          * What kind of operation to perform          */
DECL|field|apiName
specifier|private
name|GoogleMailApiName
name|apiName
decl_stmt|;
comment|/**          * What sub operation to use for the selected operation          */
DECL|field|methodName
specifier|private
name|String
name|methodName
decl_stmt|;
comment|/**          * Client ID of the mail application          */
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
comment|/**          * Client secret of the mail application          */
DECL|field|clientSecret
specifier|private
name|String
name|clientSecret
decl_stmt|;
comment|/**          * OAuth 2 access token. This typically expires after an hour so          * refreshToken is recommended for long term usage.          */
DECL|field|accessToken
specifier|private
name|String
name|accessToken
decl_stmt|;
comment|/**          * OAuth 2 refresh token. Using this, the Google Calendar component can          * obtain a new accessToken whenever the current one expires - a          * necessity if the application is long-lived.          */
DECL|field|refreshToken
specifier|private
name|String
name|refreshToken
decl_stmt|;
comment|/**          * Google mail application name. Example would be          * "camel-google-mail/1.0"          */
DECL|field|applicationName
specifier|private
name|String
name|applicationName
decl_stmt|;
comment|/**          * Specifies the level of permissions you want a mail application to          * have to a user account. See          * https://developers.google.com/gmail/api/auth/scopes for more info.          */
DECL|field|scopes
specifier|private
name|List
name|scopes
decl_stmt|;
DECL|method|getApiName ()
specifier|public
name|GoogleMailApiName
name|getApiName
parameter_list|()
block|{
return|return
name|apiName
return|;
block|}
DECL|method|setApiName (GoogleMailApiName apiName)
specifier|public
name|void
name|setApiName
parameter_list|(
name|GoogleMailApiName
name|apiName
parameter_list|)
block|{
name|this
operator|.
name|apiName
operator|=
name|apiName
expr_stmt|;
block|}
DECL|method|getMethodName ()
specifier|public
name|String
name|getMethodName
parameter_list|()
block|{
return|return
name|methodName
return|;
block|}
DECL|method|setMethodName (String methodName)
specifier|public
name|void
name|setMethodName
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|this
operator|.
name|methodName
operator|=
name|methodName
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
DECL|method|setRefreshToken (String refreshToken)
specifier|public
name|void
name|setRefreshToken
parameter_list|(
name|String
name|refreshToken
parameter_list|)
block|{
name|this
operator|.
name|refreshToken
operator|=
name|refreshToken
expr_stmt|;
block|}
DECL|method|getApplicationName ()
specifier|public
name|String
name|getApplicationName
parameter_list|()
block|{
return|return
name|applicationName
return|;
block|}
DECL|method|setApplicationName (String applicationName)
specifier|public
name|void
name|setApplicationName
parameter_list|(
name|String
name|applicationName
parameter_list|)
block|{
name|this
operator|.
name|applicationName
operator|=
name|applicationName
expr_stmt|;
block|}
DECL|method|getScopes ()
specifier|public
name|List
name|getScopes
parameter_list|()
block|{
return|return
name|scopes
return|;
block|}
DECL|method|setScopes (List scopes)
specifier|public
name|void
name|setScopes
parameter_list|(
name|List
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
block|}
block|}
end_class

end_unit

