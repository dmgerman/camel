begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The google-calendar component provides access to Google Calendar in a  * streaming mode.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|GoogleCalendarStreamEndpointBuilderFactory
specifier|public
interface|interface
name|GoogleCalendarStreamEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Google Calendar Stream component.      */
DECL|interface|GoogleCalendarStreamEndpointBuilder
specifier|public
interface|interface
name|GoogleCalendarStreamEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedGoogleCalendarStreamEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedGoogleCalendarStreamEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Specifies an index for the endpoint.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|index (String index)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|index
parameter_list|(
name|String
name|index
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"index"
argument_list|,
name|index
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * OAuth 2 access token. This typically expires after an hour so          * refreshToken is recommended for long term usage.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|accessToken ( String accessToken)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|accessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"accessToken"
argument_list|,
name|accessToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Google Calendar application name. Example would be          * camel-google-calendar/1.0.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|applicationName ( String applicationName)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|applicationName
parameter_list|(
name|String
name|applicationName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"applicationName"
argument_list|,
name|applicationName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The calendarId to be used.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|calendarId (String calendarId)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|calendarId
parameter_list|(
name|String
name|calendarId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"calendarId"
argument_list|,
name|calendarId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Client ID of the calendar application.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|clientId (String clientId)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|clientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientId"
argument_list|,
name|clientId
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Client secret of the calendar application.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|clientSecret ( String clientSecret)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|clientSecret
parameter_list|(
name|String
name|clientSecret
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"clientSecret"
argument_list|,
name|clientSecret
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Take into account the lastUpdate of the last event polled as start          * date for the next poll.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|considerLastUpdate ( boolean considerLastUpdate)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|considerLastUpdate
parameter_list|(
name|boolean
name|considerLastUpdate
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"considerLastUpdate"
argument_list|,
name|considerLastUpdate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Take into account the lastUpdate of the last event polled as start          * date for the next poll.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|considerLastUpdate ( String considerLastUpdate)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|considerLastUpdate
parameter_list|(
name|String
name|considerLastUpdate
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"considerLastUpdate"
argument_list|,
name|considerLastUpdate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Consume events in the selected calendar from now on.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|consumeFromNow ( boolean consumeFromNow)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|consumeFromNow
parameter_list|(
name|boolean
name|consumeFromNow
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"consumeFromNow"
argument_list|,
name|consumeFromNow
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Consume events in the selected calendar from now on.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|consumeFromNow ( String consumeFromNow)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|consumeFromNow
parameter_list|(
name|String
name|consumeFromNow
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"consumeFromNow"
argument_list|,
name|consumeFromNow
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Max results to be returned.          *           * The option is a:<code>int</code> type.          *           * Group: consumer          */
DECL|method|maxResults (int maxResults)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|maxResults
parameter_list|(
name|int
name|maxResults
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"maxResults"
argument_list|,
name|maxResults
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Max results to be returned.          *           * The option will be converted to a<code>int</code> type.          *           * Group: consumer          */
DECL|method|maxResults (String maxResults)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|maxResults
parameter_list|(
name|String
name|maxResults
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"maxResults"
argument_list|,
name|maxResults
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The query to execute on calendar.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|query (String query)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|query
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"query"
argument_list|,
name|query
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * OAuth 2 refresh token. Using this, the Google Calendar component can          * obtain a new accessToken whenever the current one expires - a          * necessity if the application is long-lived.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|refreshToken ( String refreshToken)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|refreshToken
parameter_list|(
name|String
name|refreshToken
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"refreshToken"
argument_list|,
name|refreshToken
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies the level of permissions you want a calendar application to          * have to a user account. See          * https://developers.google.com/calendar/auth for more info.          *           * The option is a:<code>java.util.List&lt;java.lang.String&gt;</code>          * type.          *           * Group: consumer          */
DECL|method|scopes (List<String> scopes)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|scopes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|scopes
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"scopes"
argument_list|,
name|scopes
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specifies the level of permissions you want a calendar application to          * have to a user account. See          * https://developers.google.com/calendar/auth for more info.          *           * The option will be converted to a          *<code>java.util.List&lt;java.lang.String&gt;</code> type.          *           * Group: consumer          */
DECL|method|scopes (String scopes)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|scopes
parameter_list|(
name|String
name|scopes
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"scopes"
argument_list|,
name|scopes
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Google Calendar Stream component.      */
DECL|interface|AdvancedGoogleCalendarStreamEndpointBuilder
specifier|public
interface|interface
name|AdvancedGoogleCalendarStreamEndpointBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|GoogleCalendarStreamEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedGoogleCalendarStreamEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedGoogleCalendarStreamEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedGoogleCalendarStreamEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedGoogleCalendarStreamEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * The google-calendar component provides access to Google Calendar in a      * streaming mode.      * Maven coordinates: org.apache.camel:camel-google-calendar      */
DECL|method|googleCalendarStream (String path)
specifier|default
name|GoogleCalendarStreamEndpointBuilder
name|googleCalendarStream
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|GoogleCalendarStreamEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|GoogleCalendarStreamEndpointBuilder
implements|,
name|AdvancedGoogleCalendarStreamEndpointBuilder
block|{
specifier|public
name|GoogleCalendarStreamEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"google-calendar-stream"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|GoogleCalendarStreamEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

