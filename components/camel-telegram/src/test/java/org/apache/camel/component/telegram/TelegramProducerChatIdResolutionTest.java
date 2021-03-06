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
name|Endpoint
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
name|EndpointInject
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
name|TelegramMockRoutes
operator|.
name|MockProcessor
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
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
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
name|assertNull
import|;
end_import

begin_comment
comment|/**  * Tests a producer route with a fixed destination.  */
end_comment

begin_class
DECL|class|TelegramProducerChatIdResolutionTest
specifier|public
class|class
name|TelegramProducerChatIdResolutionTest
extends|extends
name|TelegramTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
literal|"direct:telegram"
argument_list|)
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
annotation|@
name|Test
DECL|method|testRouteWithFixedChatId ()
specifier|public
name|void
name|testRouteWithFixedChatId
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|MockProcessor
argument_list|<
name|OutgoingTextMessage
argument_list|>
name|mockProcessor
init|=
name|getMockRoutes
argument_list|()
operator|.
name|getMock
argument_list|(
literal|"sendMessage"
argument_list|)
decl_stmt|;
name|mockProcessor
operator|.
name|clearRecordedMessages
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|endpoint
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
specifier|final
name|OutgoingTextMessage
name|message
init|=
name|mockProcessor
operator|.
name|awaitRecordedMessages
argument_list|(
literal|1
argument_list|,
literal|5000
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"my-id"
argument_list|,
name|message
operator|.
name|getChatId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|message
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|message
operator|.
name|getParseMode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRouteWithOverridenChatId ()
specifier|public
name|void
name|testRouteWithOverridenChatId
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|MockProcessor
argument_list|<
name|OutgoingTextMessage
argument_list|>
name|mockProcessor
init|=
name|getMockRoutes
argument_list|()
operator|.
name|getMock
argument_list|(
literal|"sendMessage"
argument_list|)
decl_stmt|;
name|mockProcessor
operator|.
name|clearRecordedMessages
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello 2"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|TelegramConstants
operator|.
name|TELEGRAM_CHAT_ID
argument_list|,
literal|"my-second-id"
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
specifier|final
name|OutgoingTextMessage
name|message
init|=
name|mockProcessor
operator|.
name|awaitRecordedMessages
argument_list|(
literal|1
argument_list|,
literal|5000
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"my-second-id"
argument_list|,
name|message
operator|.
name|getChatId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello 2"
argument_list|,
name|message
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|message
operator|.
name|getParseMode
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"direct:telegram"
argument_list|)
operator|.
name|to
argument_list|(
literal|"telegram:bots?authorizationToken=mock-token&chatId=my-id"
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
return|return
operator|new
name|TelegramMockRoutes
argument_list|(
name|port
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
argument_list|)
return|;
block|}
end_function

unit|}
end_unit

