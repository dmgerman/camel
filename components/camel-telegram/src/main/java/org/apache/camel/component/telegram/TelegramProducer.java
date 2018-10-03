begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|component
operator|.
name|telegram
operator|.
name|model
operator|.
name|OutgoingMessage
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
name|DefaultProducer
import|;
end_import

begin_comment
comment|/**  * A producer that sends messages to Telegram through the bot API.  */
end_comment

begin_class
DECL|class|TelegramProducer
specifier|public
class|class
name|TelegramProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|endpoint
specifier|private
name|TelegramEndpoint
name|endpoint
decl_stmt|;
DECL|method|TelegramProducer (TelegramEndpoint endpoint)
specifier|public
name|TelegramProducer
parameter_list|(
name|TelegramEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// fail fast
name|log
operator|.
name|debug
argument_list|(
literal|"Received exchange with empty body, skipping"
argument_list|)
expr_stmt|;
return|return;
block|}
name|TelegramConfiguration
name|config
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
comment|// Tries to get a message in its OutgoingMessage format
comment|// Automatic conversion applies here
name|OutgoingMessage
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|OutgoingMessage
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot convert the content to a Telegram OutgoingMessage"
argument_list|)
throw|;
block|}
if|if
condition|(
name|message
operator|.
name|getChatId
argument_list|()
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Chat id is null on outgoing message, trying resolution"
argument_list|)
expr_stmt|;
name|String
name|chatId
init|=
name|resolveChatId
argument_list|(
name|config
argument_list|,
name|message
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Resolved chat id is {}"
argument_list|,
name|chatId
argument_list|)
expr_stmt|;
name|message
operator|.
name|setChatId
argument_list|(
name|chatId
argument_list|)
expr_stmt|;
block|}
name|TelegramService
name|service
init|=
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Message being sent is: {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Headers of message being sent are: {}"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|sendMessage
argument_list|(
name|config
operator|.
name|getAuthorizationToken
argument_list|()
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|resolveChatId (TelegramConfiguration config, OutgoingMessage message, Exchange exchange)
specifier|private
name|String
name|resolveChatId
parameter_list|(
name|TelegramConfiguration
name|config
parameter_list|,
name|OutgoingMessage
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|chatId
decl_stmt|;
comment|// Try to get the chat id from the message body
name|chatId
operator|=
name|message
operator|.
name|getChatId
argument_list|()
expr_stmt|;
comment|// Get the chat id from headers
if|if
condition|(
name|chatId
operator|==
literal|null
condition|)
block|{
name|chatId
operator|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|TelegramConstants
operator|.
name|TELEGRAM_CHAT_ID
argument_list|)
expr_stmt|;
block|}
comment|// If not present in the headers, use the configured value for chat id
if|if
condition|(
name|chatId
operator|==
literal|null
condition|)
block|{
name|chatId
operator|=
name|config
operator|.
name|getChatId
argument_list|()
expr_stmt|;
block|}
comment|// Chat id is mandatory
if|if
condition|(
name|chatId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Chat id is not set in message headers or route configuration"
argument_list|)
throw|;
block|}
return|return
name|chatId
return|;
block|}
block|}
end_class

end_unit

