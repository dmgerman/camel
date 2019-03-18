begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.slack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|slack
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
name|Consumer
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
name|Exchange
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
name|ExchangePattern
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
name|Message
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
name|Processor
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
name|Producer
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
name|component
operator|.
name|slack
operator|.
name|helper
operator|.
name|SlackMessage
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
name|Metadata
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
name|UriEndpoint
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
name|UriPath
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
name|support
operator|.
name|ScheduledPollEndpoint
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
name|util
operator|.
name|ObjectHelper
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
name|util
operator|.
name|json
operator|.
name|JsonObject
import|;
end_import

begin_comment
comment|/**  * The slack component allows you to send messages to Slack.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.16.0"
argument_list|,
name|scheme
operator|=
literal|"slack"
argument_list|,
name|title
operator|=
literal|"Slack"
argument_list|,
name|syntax
operator|=
literal|"slack:channel"
argument_list|,
name|label
operator|=
literal|"social"
argument_list|)
DECL|class|SlackEndpoint
specifier|public
class|class
name|SlackEndpoint
extends|extends
name|ScheduledPollEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|channel
specifier|private
name|String
name|channel
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|webhookUrl
specifier|private
name|String
name|webhookUrl
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|iconUrl
specifier|private
name|String
name|iconUrl
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|iconEmoji
specifier|private
name|String
name|iconEmoji
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|token
specifier|private
name|String
name|token
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"10"
argument_list|)
DECL|field|maxResults
specifier|private
name|String
name|maxResults
init|=
literal|"10"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"https://slack.com"
argument_list|)
DECL|field|serverUrl
specifier|private
name|String
name|serverUrl
init|=
literal|"https://slack.com"
decl_stmt|;
comment|/**      * Constructor for SlackEndpoint      *      * @param uri the full component url      * @param channelName the channel or username the message is directed at      * @param component the component that was created      */
DECL|method|SlackEndpoint (String uri, String channelName, SlackComponent component)
specifier|public
name|SlackEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|channelName
parameter_list|,
name|SlackComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|webhookUrl
operator|=
name|component
operator|.
name|getWebhookUrl
argument_list|()
expr_stmt|;
name|this
operator|.
name|channel
operator|=
name|channelName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|SlackProducer
name|producer
init|=
operator|new
name|SlackProducer
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
name|producer
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|token
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Missing required endpoint configuration: token must be defined for Slack consumer"
argument_list|)
throw|;
block|}
name|SlackConsumer
name|consumer
init|=
operator|new
name|SlackConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * The incoming webhook URL      */
DECL|method|setWebhookUrl (String webhookUrl)
specifier|public
name|void
name|setWebhookUrl
parameter_list|(
name|String
name|webhookUrl
parameter_list|)
block|{
name|this
operator|.
name|webhookUrl
operator|=
name|webhookUrl
expr_stmt|;
block|}
DECL|method|getWebhookUrl ()
specifier|public
name|String
name|getWebhookUrl
parameter_list|()
block|{
return|return
name|webhookUrl
return|;
block|}
DECL|method|getChannel ()
specifier|public
name|String
name|getChannel
parameter_list|()
block|{
return|return
name|channel
return|;
block|}
comment|/**      * The channel name (syntax #name) or slackuser (syntax @userName) to send a      * message directly to an user.      */
DECL|method|setChannel (String channel)
specifier|public
name|void
name|setChannel
parameter_list|(
name|String
name|channel
parameter_list|)
block|{
name|this
operator|.
name|channel
operator|=
name|channel
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
comment|/**      * This is the username that the bot will have when sending messages to a      * channel or user.      */
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getIconUrl ()
specifier|public
name|String
name|getIconUrl
parameter_list|()
block|{
return|return
name|iconUrl
return|;
block|}
comment|/**      * The avatar that the component will use when sending message to a channel      * or user.      */
DECL|method|setIconUrl (String iconUrl)
specifier|public
name|void
name|setIconUrl
parameter_list|(
name|String
name|iconUrl
parameter_list|)
block|{
name|this
operator|.
name|iconUrl
operator|=
name|iconUrl
expr_stmt|;
block|}
DECL|method|getIconEmoji ()
specifier|public
name|String
name|getIconEmoji
parameter_list|()
block|{
return|return
name|iconEmoji
return|;
block|}
comment|/**      * Use a Slack emoji as an avatar      */
DECL|method|setIconEmoji (String iconEmoji)
specifier|public
name|void
name|setIconEmoji
parameter_list|(
name|String
name|iconEmoji
parameter_list|)
block|{
name|this
operator|.
name|iconEmoji
operator|=
name|iconEmoji
expr_stmt|;
block|}
DECL|method|getToken ()
specifier|public
name|String
name|getToken
parameter_list|()
block|{
return|return
name|token
return|;
block|}
comment|/**      * The token to use      */
DECL|method|setToken (String token)
specifier|public
name|void
name|setToken
parameter_list|(
name|String
name|token
parameter_list|)
block|{
name|this
operator|.
name|token
operator|=
name|token
expr_stmt|;
block|}
DECL|method|getMaxResults ()
specifier|public
name|String
name|getMaxResults
parameter_list|()
block|{
return|return
name|maxResults
return|;
block|}
comment|/**      * The Max Result for the poll      */
DECL|method|setMaxResults (String maxResult)
specifier|public
name|void
name|setMaxResults
parameter_list|(
name|String
name|maxResult
parameter_list|)
block|{
name|this
operator|.
name|maxResults
operator|=
name|maxResult
expr_stmt|;
block|}
DECL|method|getServerUrl ()
specifier|public
name|String
name|getServerUrl
parameter_list|()
block|{
return|return
name|serverUrl
return|;
block|}
comment|/**      * The Server URL of the Slack instance      */
DECL|method|setServerUrl (String serverUrl)
specifier|public
name|void
name|setServerUrl
parameter_list|(
name|String
name|serverUrl
parameter_list|)
block|{
name|this
operator|.
name|serverUrl
operator|=
name|serverUrl
expr_stmt|;
block|}
DECL|method|createExchange (JsonObject object)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|JsonObject
name|object
parameter_list|)
block|{
return|return
name|createExchange
argument_list|(
name|getExchangePattern
argument_list|()
argument_list|,
name|object
argument_list|)
return|;
block|}
DECL|method|createExchange (ExchangePattern pattern, JsonObject object)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|,
name|JsonObject
name|object
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|super
operator|.
name|createExchange
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
name|SlackMessage
name|slackMessage
init|=
operator|new
name|SlackMessage
argument_list|()
decl_stmt|;
name|String
name|text
init|=
name|object
operator|.
name|getString
argument_list|(
literal|"text"
argument_list|)
decl_stmt|;
name|String
name|username
init|=
name|object
operator|.
name|getString
argument_list|(
literal|"username"
argument_list|)
decl_stmt|;
name|slackMessage
operator|.
name|setText
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|slackMessage
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|object
operator|.
name|get
argument_list|(
literal|"icons"
argument_list|)
argument_list|)
condition|)
block|{
name|JsonObject
name|icons
init|=
name|object
operator|.
name|getMap
argument_list|(
literal|"icons"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|icons
operator|.
name|get
argument_list|(
literal|"emoji"
argument_list|)
argument_list|)
condition|)
block|{
name|slackMessage
operator|.
name|setIconEmoji
argument_list|(
name|icons
operator|.
name|getString
argument_list|(
literal|"emoji"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|slackMessage
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

