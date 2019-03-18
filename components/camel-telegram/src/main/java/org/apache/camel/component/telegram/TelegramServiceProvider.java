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
name|component
operator|.
name|telegram
operator|.
name|service
operator|.
name|TelegramServiceRestBotAPIAdapter
import|;
end_import

begin_comment
comment|/**  * Provides access to an instance of the Telegram service. It allows changing the default implementation of the service for testing purposes.  * Currently the Telegram API does not allow a Bot sending messages to other BOTs (https://core.telegram.org/bots/faq#why-doesn-39t-my-bot-see-messages-from-other-bots),  * so the service needs to be mocked for end-to-end testing.  *  * The Rest client used as default implementation is thread safe, considering the current usage of the APIs. It is treated as a singleton.  */
end_comment

begin_class
DECL|class|TelegramServiceProvider
specifier|public
specifier|final
class|class
name|TelegramServiceProvider
block|{
DECL|field|INSTANCE
specifier|private
specifier|static
specifier|final
name|TelegramServiceProvider
name|INSTANCE
init|=
operator|new
name|TelegramServiceProvider
argument_list|()
decl_stmt|;
comment|/**      * The default service.      */
DECL|field|service
specifier|private
specifier|final
name|TelegramService
name|service
decl_stmt|;
comment|/**      * An alternative service used for testing purposes.      */
DECL|field|telegramService
specifier|private
name|TelegramService
name|telegramService
decl_stmt|;
DECL|method|TelegramServiceProvider ()
specifier|private
name|TelegramServiceProvider
parameter_list|()
block|{
comment|// Using the Rest Bot API by default
name|this
operator|.
name|service
operator|=
operator|new
name|TelegramServiceRestBotAPIAdapter
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns the singleton provider.      */
DECL|method|get ()
specifier|public
specifier|static
name|TelegramServiceProvider
name|get
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
comment|/**      * Provides the current service. It can be the default one or an alternative one.      * @return the active {@code TelegramService}      */
DECL|method|getService ()
specifier|public
name|TelegramService
name|getService
parameter_list|()
block|{
if|if
condition|(
name|telegramService
operator|!=
literal|null
condition|)
block|{
comment|// no need for synchronization, it's only for testing purposes
return|return
name|telegramService
return|;
block|}
return|return
name|service
return|;
block|}
comment|/**      * Get the current alternative service, if any.      *      * @return the current alternative service      */
DECL|method|getAlternativeService ()
specifier|public
name|TelegramService
name|getAlternativeService
parameter_list|()
block|{
return|return
name|telegramService
return|;
block|}
comment|/**      * Allows setting an alternative service.      *      * @param service the alternative service      */
DECL|method|setAlternativeService (TelegramService service)
specifier|public
name|void
name|setAlternativeService
parameter_list|(
name|TelegramService
name|service
parameter_list|)
block|{
name|this
operator|.
name|telegramService
operator|=
name|service
expr_stmt|;
block|}
comment|/**      * Restores the provider to its original state.      */
DECL|method|restoreDefaultService ()
specifier|public
name|void
name|restoreDefaultService
parameter_list|()
block|{
name|this
operator|.
name|telegramService
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

