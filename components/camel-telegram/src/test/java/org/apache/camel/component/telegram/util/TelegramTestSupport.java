begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram.util
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
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
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
name|TelegramService
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
name|TelegramServiceProvider
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
name|InlineKeyboardButton
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
name|OutgoingTextMessage
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
name|ReplyKeyboardMarkup
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_comment
comment|/**  * A support test class for Telegram tests.  */
end_comment

begin_class
DECL|class|TelegramTestSupport
specifier|public
class|class
name|TelegramTestSupport
extends|extends
name|CamelTestSupport
block|{
comment|/**      * Indicates whether the {@code TelegramService} has been mocked during last test.      */
DECL|field|telegramServiceMocked
specifier|private
name|boolean
name|telegramServiceMocked
decl_stmt|;
comment|/**      * Restores the status of {@code TelegramServiceProvider} if it has been mocked.      */
annotation|@
name|Override
DECL|method|doPostTearDown ()
specifier|public
name|void
name|doPostTearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|telegramServiceMocked
condition|)
block|{
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|restoreDefaultService
argument_list|()
expr_stmt|;
name|this
operator|.
name|telegramServiceMocked
operator|=
literal|false
expr_stmt|;
block|}
block|}
comment|/**      * Setup an alternative mock {@code TelegramService} in the {@code TelegramServiceProvider} and return it.      *      * @return the mock service      */
DECL|method|mockTelegramService ()
specifier|public
name|TelegramService
name|mockTelegramService
parameter_list|()
block|{
name|TelegramService
name|mockService
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|TelegramService
operator|.
name|class
argument_list|)
decl_stmt|;
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|setAlternativeService
argument_list|(
name|mockService
argument_list|)
expr_stmt|;
name|this
operator|.
name|telegramServiceMocked
operator|=
literal|true
expr_stmt|;
return|return
name|mockService
return|;
block|}
comment|/**      * Construct an inline keyboard sample to be used with an OutgoingTextMessage.      *       * @param message OutgoingTextMessage previously created      * @return OutgoingTextMessage set with an inline keyboard      */
DECL|method|withInlineKeyboardContainingTwoRows (OutgoingTextMessage message)
specifier|public
name|OutgoingTextMessage
name|withInlineKeyboardContainingTwoRows
parameter_list|(
name|OutgoingTextMessage
name|message
parameter_list|)
block|{
name|InlineKeyboardButton
name|buttonOptionOneI
init|=
name|InlineKeyboardButton
operator|.
name|builder
argument_list|()
operator|.
name|text
argument_list|(
literal|"Option One - I"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|InlineKeyboardButton
name|buttonOptionOneII
init|=
name|InlineKeyboardButton
operator|.
name|builder
argument_list|()
operator|.
name|text
argument_list|(
literal|"Option One - II"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|InlineKeyboardButton
name|buttonOptionTwoI
init|=
name|InlineKeyboardButton
operator|.
name|builder
argument_list|()
operator|.
name|text
argument_list|(
literal|"Option Two - I"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|ReplyKeyboardMarkup
name|replyMarkup
init|=
name|ReplyKeyboardMarkup
operator|.
name|builder
argument_list|()
operator|.
name|keyboard
argument_list|()
operator|.
name|addRow
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|buttonOptionOneI
argument_list|,
name|buttonOptionOneII
argument_list|)
argument_list|)
operator|.
name|addRow
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|buttonOptionTwoI
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
operator|.
name|oneTimeKeyboard
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|message
operator|.
name|setReplyKeyboardMarkup
argument_list|(
name|replyMarkup
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
comment|/**      * Retrieves the currently mocked {@code TelegramService}.      *      * @return the current mock of the telegram service      */
DECL|method|currentMockService ()
specifier|public
name|TelegramService
name|currentMockService
parameter_list|()
block|{
return|return
name|TelegramServiceProvider
operator|.
name|get
argument_list|()
operator|.
name|getAlternativeService
argument_list|()
return|;
block|}
comment|/**      * Retrieves a response from a JSON file on classpath.      *      * @param fileName the filename in the classpath      * @param clazz the target class      * @param<T> the type of the returned object      * @return the object representation of the JSON file      */
DECL|method|getJSONResource (String fileName, Class<T> clazz)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getJSONResource
parameter_list|(
name|String
name|fileName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
name|ObjectMapper
name|mapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|stream
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|fileName
argument_list|)
init|)
block|{
name|T
name|value
init|=
name|mapper
operator|.
name|readValue
argument_list|(
name|stream
argument_list|,
name|clazz
argument_list|)
decl_stmt|;
return|return
name|value
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to load file "
operator|+
name|fileName
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

