begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
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
comment|/**  * Bean holding the configuration of the telegram component.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|TelegramConfiguration
specifier|public
class|class
name|TelegramConfiguration
block|{
DECL|field|ENDPOINT_TYPE_BOTS
specifier|public
specifier|static
specifier|final
name|String
name|ENDPOINT_TYPE_BOTS
init|=
literal|"bots"
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The endpoint type. Currently, only the 'bots' type is supported."
argument_list|,
name|enums
operator|=
name|ENDPOINT_TYPE_BOTS
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"The authorization token for using the bot (ask the BotFather)"
argument_list|,
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|authorizationToken
specifier|private
name|String
name|authorizationToken
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"HTTP proxy host which could be used when sending out the message."
argument_list|,
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"HTTP proxy port which could be used when sending out the message."
argument_list|,
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyPort
specifier|private
name|Integer
name|proxyPort
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"The identifier of the chat that will receive the produced messages. Chat ids can be first obtained from incoming messages "
operator|+
literal|"(eg. when a telegram user starts a conversation with a bot, its client sends automatically a '/start' message containing the chat id). "
operator|+
literal|"It is an optional parameter, as the chat id can be set dynamically for each outgoing message (using body or headers)."
argument_list|,
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|chatId
specifier|private
name|String
name|chatId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Timeout in seconds for long polling. Put 0 for short polling or a bigger number for long polling. Long polling produces shorter response time."
argument_list|,
name|optionalPrefix
operator|=
literal|"consumer."
argument_list|,
name|defaultValue
operator|=
literal|"30"
argument_list|,
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|timeout
specifier|private
name|Integer
name|timeout
init|=
literal|30
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Limit on the number of updates that can be received in a single polling request."
argument_list|,
name|optionalPrefix
operator|=
literal|"consumer."
argument_list|,
name|defaultValue
operator|=
literal|"100"
argument_list|,
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|limit
specifier|private
name|Integer
name|limit
init|=
literal|100
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Can be used to set an alternative base URI, e.g. when you want to test the component against a mock Telegram API"
argument_list|)
DECL|field|baseUri
specifier|private
name|String
name|baseUri
decl_stmt|;
DECL|method|TelegramConfiguration ()
specifier|public
name|TelegramConfiguration
parameter_list|()
block|{     }
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (String type)
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getAuthorizationToken ()
specifier|public
name|String
name|getAuthorizationToken
parameter_list|()
block|{
return|return
name|authorizationToken
return|;
block|}
DECL|method|setAuthorizationToken (String authorizationToken)
specifier|public
name|void
name|setAuthorizationToken
parameter_list|(
name|String
name|authorizationToken
parameter_list|)
block|{
name|this
operator|.
name|authorizationToken
operator|=
name|authorizationToken
expr_stmt|;
block|}
DECL|method|getProxyHost ()
specifier|public
name|String
name|getProxyHost
parameter_list|()
block|{
return|return
name|proxyHost
return|;
block|}
DECL|method|setProxyHost (String proxyHost)
specifier|public
name|void
name|setProxyHost
parameter_list|(
name|String
name|proxyHost
parameter_list|)
block|{
name|this
operator|.
name|proxyHost
operator|=
name|proxyHost
expr_stmt|;
block|}
DECL|method|getProxyPort ()
specifier|public
name|Integer
name|getProxyPort
parameter_list|()
block|{
return|return
name|proxyPort
return|;
block|}
DECL|method|setProxyPort (Integer proxyPort)
specifier|public
name|void
name|setProxyPort
parameter_list|(
name|Integer
name|proxyPort
parameter_list|)
block|{
name|this
operator|.
name|proxyPort
operator|=
name|proxyPort
expr_stmt|;
block|}
DECL|method|getChatId ()
specifier|public
name|String
name|getChatId
parameter_list|()
block|{
return|return
name|chatId
return|;
block|}
DECL|method|setChatId (String chatId)
specifier|public
name|void
name|setChatId
parameter_list|(
name|String
name|chatId
parameter_list|)
block|{
name|this
operator|.
name|chatId
operator|=
name|chatId
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|Integer
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (Integer timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Integer
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getLimit ()
specifier|public
name|Integer
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
DECL|method|setLimit (Integer limit)
specifier|public
name|void
name|setLimit
parameter_list|(
name|Integer
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
block|}
DECL|method|getBaseUri ()
specifier|public
name|String
name|getBaseUri
parameter_list|()
block|{
return|return
name|baseUri
return|;
block|}
comment|/**      * Set an alternative base URI, e.g. when you want to test the component against a mock Telegram API.      */
DECL|method|setBaseUri (String telegramBaseUri)
specifier|public
name|void
name|setBaseUri
parameter_list|(
name|String
name|telegramBaseUri
parameter_list|)
block|{
name|this
operator|.
name|baseUri
operator|=
name|telegramBaseUri
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
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"TelegramConfiguration{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"type='"
argument_list|)
operator|.
name|append
argument_list|(
name|type
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", authorizationToken='"
argument_list|)
operator|.
name|append
argument_list|(
name|authorizationToken
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", chatId='"
argument_list|)
operator|.
name|append
argument_list|(
name|chatId
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", timeout="
argument_list|)
operator|.
name|append
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", limit="
argument_list|)
operator|.
name|append
argument_list|(
name|limit
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

