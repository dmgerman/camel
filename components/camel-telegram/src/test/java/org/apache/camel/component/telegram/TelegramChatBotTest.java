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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|RoutesBuilder
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
name|RouteBuilder
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
name|UpdateResult
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
name|util
operator|.
name|TelegramMockRoutes
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
name|util
operator|.
name|TelegramTestSupport
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
name|util
operator|.
name|TelegramTestUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|awaitility
operator|.
name|Awaitility
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit5
operator|.
name|TestSupport
operator|.
name|assertCollectionSize
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Tests a chain made of a consumer and a producer to create a direct chat-bot.  */
end_comment

begin_class
DECL|class|TelegramChatBotTest
specifier|public
class|class
name|TelegramChatBotTest
extends|extends
name|TelegramTestSupport
block|{
annotation|@
name|Test
DECL|method|testChatBotResult ()
specifier|public
name|void
name|testChatBotResult
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|OutgoingTextMessage
argument_list|>
name|msgs
init|=
name|Awaitility
operator|.
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
parameter_list|()
lambda|->
name|getMockRoutes
argument_list|()
operator|.
name|getMock
argument_list|(
literal|"sendMessage"
argument_list|)
operator|.
name|getRecordedMessages
argument_list|()
argument_list|,
name|rawMessages
lambda|->
name|rawMessages
operator|.
name|size
argument_list|()
operator|>=
literal|2
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|message
lambda|->
operator|(
name|OutgoingTextMessage
operator|)
name|message
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|assertCollectionSize
argument_list|(
name|msgs
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|msgs
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|m
lambda|->
literal|"echo from the bot: Hello World!"
operator|.
name|equals
argument_list|(
name|m
operator|.
name|getText
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|msgs
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|m
lambda|->
literal|"echo from the bot: taken"
operator|.
name|equals
argument_list|(
name|m
operator|.
name|getText
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|msgs
operator|.
name|stream
argument_list|()
operator|.
name|noneMatch
argument_list|(
name|m
lambda|->
name|m
operator|.
name|getParseMode
argument_list|()
operator|!=
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This method simulates the first step of the chat-bot logic.      *      * @param exchange the current exchange originating from the telegram bot      */
DECL|method|chatBotProcess1 (Exchange exchange)
specifier|public
name|void
name|chatBotProcess1
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|equals
argument_list|(
literal|"intercept"
argument_list|)
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"taken"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This method simulates the second step of the chat-bot logic.      *      * @param message the message coming from the telegram bot      * @return the reply, if any      */
DECL|method|chatBotProcess2 (String message)
specifier|public
name|String
name|chatBotProcess2
parameter_list|(
name|String
name|message
parameter_list|)
block|{
return|return
literal|"echo from the bot: "
operator|+
name|message
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilders ()
specifier|protected
name|RoutesBuilder
index|[]
name|createRouteBuilders
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RoutesBuilder
index|[]
block|{
name|getMockRoutes
argument_list|()
block|,
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"telegram:bots?authorizationToken=mock-token"
argument_list|)
operator|.
name|bean
argument_list|(
name|TelegramChatBotTest
operator|.
name|this
argument_list|,
literal|"chatBotProcess1"
argument_list|)
operator|.
name|bean
argument_list|(
name|TelegramChatBotTest
operator|.
name|this
argument_list|,
literal|"chatBotProcess2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"telegram:bots?authorizationToken=mock-token"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_function
unit|}      @
name|Override
DECL|method|createMockRoutes ()
specifier|protected
name|TelegramMockRoutes
name|createMockRoutes
parameter_list|()
block|{
name|UpdateResult
name|request
init|=
name|getJSONResource
argument_list|(
literal|"messages/updates-single.json"
argument_list|,
name|UpdateResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|request
operator|.
name|getUpdates
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMessage
argument_list|()
operator|.
name|setText
argument_list|(
literal|"Hello World!"
argument_list|)
expr_stmt|;
name|request
operator|.
name|getUpdates
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMessage
argument_list|()
operator|.
name|getChat
argument_list|()
operator|.
name|setId
argument_list|(
literal|"my-chat-id"
argument_list|)
expr_stmt|;
name|UpdateResult
name|request2
init|=
name|getJSONResource
argument_list|(
literal|"messages/updates-single.json"
argument_list|,
name|UpdateResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|request2
operator|.
name|getUpdates
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMessage
argument_list|()
operator|.
name|setText
argument_list|(
literal|"intercept"
argument_list|)
expr_stmt|;
name|request2
operator|.
name|getUpdates
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMessage
argument_list|()
operator|.
name|getChat
argument_list|()
operator|.
name|setId
argument_list|(
literal|"my-chat-id"
argument_list|)
expr_stmt|;
return|return
operator|new
name|TelegramMockRoutes
argument_list|(
name|port
argument_list|)
operator|.
name|addEndpoint
argument_list|(
literal|"getUpdates"
argument_list|,
literal|"GET"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|TelegramTestUtil
operator|.
name|serialize
argument_list|(
name|request
argument_list|)
argument_list|,
name|TelegramTestUtil
operator|.
name|serialize
argument_list|(
name|request2
argument_list|)
argument_list|,
name|TelegramTestUtil
operator|.
name|stringResource
argument_list|(
literal|"messages/updates-empty.json"
argument_list|)
argument_list|)
operator|.
name|addEndpoint
argument_list|(
literal|"sendMessage"
argument_list|,
literal|"POST"
argument_list|,
name|OutgoingTextMessage
operator|.
name|class
argument_list|,
name|TelegramTestUtil
operator|.
name|stringResource
argument_list|(
literal|"messages/send-message.json"
argument_list|)
argument_list|,
name|TelegramTestUtil
operator|.
name|stringResource
argument_list|(
literal|"messages/send-message.json"
argument_list|)
argument_list|,
name|TelegramTestUtil
operator|.
name|stringResource
argument_list|(
literal|"messages/send-message.json"
argument_list|)
argument_list|)
return|;
block|}
end_function

unit|}
end_unit

