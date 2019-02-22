begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.mail.stream.springboot
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
name|stream
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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

begin_comment
comment|/**  * The google-mail component provides access to Google Mail.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.google-mail-stream"
argument_list|)
DECL|class|GoogleMailStreamComponentConfiguration
specifier|public
class|class
name|GoogleMailStreamComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the google-mail-stream component.      * This is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * The configuration      */
DECL|field|configuration
specifier|private
name|GoogleMailStreamConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * The client Factory. The option is a      * org.apache.camel.component.google.mail.GoogleMailClientFactory type.      */
DECL|field|clientFactory
specifier|private
name|String
name|clientFactory
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|GoogleMailStreamConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( GoogleMailStreamConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|GoogleMailStreamConfigurationNestedConfiguration
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
name|String
name|getClientFactory
parameter_list|()
block|{
return|return
name|clientFactory
return|;
block|}
DECL|method|setClientFactory (String clientFactory)
specifier|public
name|void
name|setClientFactory
parameter_list|(
name|String
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
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|class|GoogleMailStreamConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|GoogleMailStreamConfigurationNestedConfiguration
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
name|stream
operator|.
name|GoogleMailStreamConfiguration
operator|.
name|class
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
comment|/**          * Specifies an index for the endpoint          */
DECL|field|index
specifier|private
name|String
name|index
decl_stmt|;
comment|/**          * The query to execute on gmail box          */
DECL|field|query
specifier|private
name|String
name|query
init|=
literal|"is:unread"
decl_stmt|;
comment|/**          * Max results to be returned          */
DECL|field|maxResults
specifier|private
name|Long
name|maxResults
init|=
literal|10L
decl_stmt|;
comment|/**          * Comma separated list of labels to take into account          */
DECL|field|labels
specifier|private
name|String
name|labels
decl_stmt|;
comment|/**          * Mark the message as read once it has been consumed          */
DECL|field|markAsRead
specifier|private
name|Boolean
name|markAsRead
init|=
literal|false
decl_stmt|;
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
DECL|method|getIndex ()
specifier|public
name|String
name|getIndex
parameter_list|()
block|{
return|return
name|index
return|;
block|}
DECL|method|setIndex (String index)
specifier|public
name|void
name|setIndex
parameter_list|(
name|String
name|index
parameter_list|)
block|{
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
block|}
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
DECL|method|setQuery (String query)
specifier|public
name|void
name|setQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
DECL|method|getMaxResults ()
specifier|public
name|Long
name|getMaxResults
parameter_list|()
block|{
return|return
name|maxResults
return|;
block|}
DECL|method|setMaxResults (Long maxResults)
specifier|public
name|void
name|setMaxResults
parameter_list|(
name|Long
name|maxResults
parameter_list|)
block|{
name|this
operator|.
name|maxResults
operator|=
name|maxResults
expr_stmt|;
block|}
DECL|method|getLabels ()
specifier|public
name|String
name|getLabels
parameter_list|()
block|{
return|return
name|labels
return|;
block|}
DECL|method|setLabels (String labels)
specifier|public
name|void
name|setLabels
parameter_list|(
name|String
name|labels
parameter_list|)
block|{
name|this
operator|.
name|labels
operator|=
name|labels
expr_stmt|;
block|}
DECL|method|getMarkAsRead ()
specifier|public
name|Boolean
name|getMarkAsRead
parameter_list|()
block|{
return|return
name|markAsRead
return|;
block|}
DECL|method|setMarkAsRead (Boolean markAsRead)
specifier|public
name|void
name|setMarkAsRead
parameter_list|(
name|Boolean
name|markAsRead
parameter_list|)
block|{
name|this
operator|.
name|markAsRead
operator|=
name|markAsRead
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

