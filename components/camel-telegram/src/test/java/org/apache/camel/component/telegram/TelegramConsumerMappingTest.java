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
name|time
operator|.
name|Instant
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
name|mock
operator|.
name|MockEndpoint
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
name|Chat
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
name|IncomingMessage
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
name|MessageResult
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
name|model
operator|.
name|User
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
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_comment
comment|/**  * Tests the JSON mapping of the API updates.  */
end_comment

begin_class
DECL|class|TelegramConsumerMappingTest
specifier|public
class|class
name|TelegramConsumerMappingTest
extends|extends
name|TelegramTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:telegram"
argument_list|)
DECL|field|endpoint
specifier|private
name|MockEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|Before
DECL|method|mockAPIs ()
specifier|public
name|void
name|mockAPIs
parameter_list|()
block|{
name|TelegramService
name|api
init|=
name|mockTelegramService
argument_list|()
decl_stmt|;
name|UpdateResult
name|res1
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
name|UpdateResult
name|defaultRes
init|=
name|getJSONResource
argument_list|(
literal|"messages/updates-empty.json"
argument_list|,
name|UpdateResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|api
operator|.
name|getUpdates
argument_list|(
name|any
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|res1
argument_list|)
operator|.
name|thenAnswer
argument_list|(
name|i
lambda|->
name|defaultRes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMessageMapping ()
specifier|public
name|void
name|testMessageMapping
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|ex
init|=
name|endpoint
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Message
name|m
init|=
name|ex
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|m
argument_list|)
expr_stmt|;
comment|// checking headers
name|assertEquals
argument_list|(
literal|"-45658"
argument_list|,
name|m
operator|.
name|getHeader
argument_list|(
name|TelegramConstants
operator|.
name|TELEGRAM_CHAT_ID
argument_list|)
argument_list|)
expr_stmt|;
comment|// checking body
name|assertNotNull
argument_list|(
name|m
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|m
operator|.
name|getBody
argument_list|()
operator|instanceof
name|IncomingMessage
argument_list|)
expr_stmt|;
name|IncomingMessage
name|body
init|=
operator|(
name|IncomingMessage
operator|)
name|m
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a message"
argument_list|,
name|body
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|179L
argument_list|)
argument_list|,
name|body
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Instant
operator|.
name|ofEpochSecond
argument_list|(
literal|1463436626L
argument_list|)
argument_list|,
name|body
operator|.
name|getDate
argument_list|()
argument_list|)
expr_stmt|;
comment|// checking from
name|User
name|user
init|=
name|body
operator|.
name|getFrom
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"John"
argument_list|,
name|user
operator|.
name|getFirstName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Doe"
argument_list|,
name|user
operator|.
name|getLastName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|1585844777
argument_list|)
argument_list|,
name|user
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// checking chat
name|Chat
name|chat
init|=
name|body
operator|.
name|getChat
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|chat
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"-45658"
argument_list|,
name|chat
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A chat group"
argument_list|,
name|chat
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"group"
argument_list|,
name|chat
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMessageResultMapping ()
specifier|public
name|void
name|testMessageResultMapping
parameter_list|()
block|{
name|MessageResult
name|messageResult
init|=
name|getJSONResource
argument_list|(
literal|"messages/updates-sendLocation.json"
argument_list|,
name|MessageResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|messageResult
operator|.
name|isOk
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|messageResult
operator|.
name|isOk
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Long
operator|)
literal|33L
argument_list|,
name|messageResult
operator|.
name|getMessage
argument_list|()
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Instant
operator|.
name|ofEpochSecond
argument_list|(
literal|1548091564
argument_list|)
operator|.
name|getEpochSecond
argument_list|()
argument_list|,
name|messageResult
operator|.
name|getMessage
argument_list|()
operator|.
name|getDate
argument_list|()
operator|.
name|getEpochSecond
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Long
operator|)
literal|665977497L
argument_list|,
name|messageResult
operator|.
name|getMessage
argument_list|()
operator|.
name|getFrom
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|messageResult
operator|.
name|getMessage
argument_list|()
operator|.
name|getFrom
argument_list|()
operator|.
name|isBot
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"camelbot"
argument_list|,
name|messageResult
operator|.
name|getMessage
argument_list|()
operator|.
name|getFrom
argument_list|()
operator|.
name|getFirstName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"camel_component_bot"
argument_list|,
name|messageResult
operator|.
name|getMessage
argument_list|()
operator|.
name|getFrom
argument_list|()
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"-182520913"
argument_list|,
name|messageResult
operator|.
name|getMessage
argument_list|()
operator|.
name|getChat
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testgroup"
argument_list|,
name|messageResult
operator|.
name|getMessage
argument_list|()
operator|.
name|getChat
argument_list|()
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"group"
argument_list|,
name|messageResult
operator|.
name|getMessage
argument_list|()
operator|.
name|getChat
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|messageResult
operator|.
name|getMessage
argument_list|()
operator|.
name|getChat
argument_list|()
operator|.
name|isAllMembersAreAdministrators
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|59.9386292
argument_list|,
name|messageResult
operator|.
name|getMessage
argument_list|()
operator|.
name|getLocation
argument_list|()
operator|.
name|getLatitude
argument_list|()
argument_list|,
literal|1.0E
operator|-
literal|07
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|30.3141308
argument_list|,
name|messageResult
operator|.
name|getMessage
argument_list|()
operator|.
name|getLocation
argument_list|()
operator|.
name|getLongitude
argument_list|()
argument_list|,
literal|1.0E
operator|-
literal|07
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
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
name|to
argument_list|(
literal|"mock:telegram"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

