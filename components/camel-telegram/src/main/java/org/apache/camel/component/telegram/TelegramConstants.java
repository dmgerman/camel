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

begin_comment
comment|/**  * Useful constants for the Telegram component.  */
end_comment

begin_class
DECL|class|TelegramConstants
specifier|public
specifier|final
class|class
name|TelegramConstants
block|{
DECL|field|TELEGRAM_CHAT_ID
specifier|public
specifier|static
specifier|final
name|String
name|TELEGRAM_CHAT_ID
init|=
literal|"CamelTelegramChatId"
decl_stmt|;
DECL|field|TELEGRAM_MEDIA_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|TELEGRAM_MEDIA_TYPE
init|=
literal|"CamelTelegramMediaType"
decl_stmt|;
DECL|field|TELEGRAM_MEDIA_TITLE_CAPTION
specifier|public
specifier|static
specifier|final
name|String
name|TELEGRAM_MEDIA_TITLE_CAPTION
init|=
literal|"CamelTelegramMediaTitleCaption"
decl_stmt|;
DECL|field|TELEGRAM_PARSE_MODE
specifier|public
specifier|static
specifier|final
name|String
name|TELEGRAM_PARSE_MODE
init|=
literal|"CamelTelegramParseMode"
decl_stmt|;
DECL|method|TelegramConstants ()
specifier|private
name|TelegramConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

