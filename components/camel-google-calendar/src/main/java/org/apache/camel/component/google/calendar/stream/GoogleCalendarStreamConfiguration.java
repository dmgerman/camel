begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.calendar.stream
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
name|calendar
operator|.
name|stream
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
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|calendar
operator|.
name|CalendarScopes
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
name|RuntimeCamelException
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriParams
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
name|spi
operator|.
name|UriPath
import|;
end_import

begin_comment
comment|/**  * Component configuration for GoogleCalendar stream component.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|GoogleCalendarStreamConfiguration
specifier|public
class|class
name|GoogleCalendarStreamConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|DEFAULT_SCOPES
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|DEFAULT_SCOPES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|CalendarScopes
operator|.
name|CALENDAR
argument_list|)
decl_stmt|;
annotation|@
name|UriPath
DECL|field|index
specifier|private
name|String
name|index
decl_stmt|;
annotation|@
name|UriParam
DECL|field|scopes
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|scopes
init|=
name|DEFAULT_SCOPES
decl_stmt|;
annotation|@
name|UriParam
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
annotation|@
name|UriParam
DECL|field|clientSecret
specifier|private
name|String
name|clientSecret
decl_stmt|;
annotation|@
name|UriParam
DECL|field|accessToken
specifier|private
name|String
name|accessToken
decl_stmt|;
annotation|@
name|UriParam
DECL|field|refreshToken
specifier|private
name|String
name|refreshToken
decl_stmt|;
annotation|@
name|UriParam
DECL|field|applicationName
specifier|private
name|String
name|applicationName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"10"
argument_list|)
DECL|field|maxResults
specifier|private
name|int
name|maxResults
init|=
literal|10
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"primary"
argument_list|)
DECL|field|calendarSummaryName
specifier|private
name|String
name|calendarSummaryName
init|=
literal|"primary"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|consumeFromNow
specifier|private
name|boolean
name|consumeFromNow
init|=
literal|true
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
comment|/**      * Client ID of the mail application      */
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
comment|/**      * Client secret of the mail application      */
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
comment|/**      * OAuth 2 access token. This typically expires after an hour so      * refreshToken is recommended for long term usage.      */
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
comment|/**      * OAuth 2 refresh token. Using this, the Google Calendar component can      * obtain a new accessToken whenever the current one expires - a necessity      * if the application is long-lived.      */
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
comment|/**      * Google Calendar application name. Example would be "camel-google-calendar/1.0"      */
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
argument_list|<
name|String
argument_list|>
name|getScopes
parameter_list|()
block|{
return|return
name|scopes
return|;
block|}
comment|/**      * Specifies the level of permissions you want a mail application to have to      * a user account. See https://developers.google.com/calendar/api/auth/scopes      * for more info.      */
DECL|method|setScopes (List<String> scopes)
specifier|public
name|void
name|setScopes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
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
comment|/**      * Specifies an index for the endpoint      */
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
comment|/**      * The query to execute on calendar      */
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
name|int
name|getMaxResults
parameter_list|()
block|{
return|return
name|maxResults
return|;
block|}
comment|/**      * Max results to be returned      */
DECL|method|setMaxResults (int maxResults)
specifier|public
name|void
name|setMaxResults
parameter_list|(
name|int
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
DECL|method|getCalendarSummaryName ()
specifier|public
name|String
name|getCalendarSummaryName
parameter_list|()
block|{
return|return
name|calendarSummaryName
return|;
block|}
comment|/**      * Calendar Summary Name to use      */
DECL|method|setCalendarSummaryName (String calendarSummaryName)
specifier|public
name|void
name|setCalendarSummaryName
parameter_list|(
name|String
name|calendarSummaryName
parameter_list|)
block|{
name|this
operator|.
name|calendarSummaryName
operator|=
name|calendarSummaryName
expr_stmt|;
block|}
DECL|method|isConsumeFromNow ()
specifier|public
name|boolean
name|isConsumeFromNow
parameter_list|()
block|{
return|return
name|consumeFromNow
return|;
block|}
comment|/**      * Consume events in the selected calendar from now on      */
DECL|method|setConsumeFromNow (boolean consumeFromNow)
specifier|public
name|void
name|setConsumeFromNow
parameter_list|(
name|boolean
name|consumeFromNow
parameter_list|)
block|{
name|this
operator|.
name|consumeFromNow
operator|=
name|consumeFromNow
expr_stmt|;
block|}
comment|// *************************************************
comment|//
comment|// *************************************************
DECL|method|copy ()
specifier|public
name|GoogleCalendarStreamConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|GoogleCalendarStreamConfiguration
operator|)
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

