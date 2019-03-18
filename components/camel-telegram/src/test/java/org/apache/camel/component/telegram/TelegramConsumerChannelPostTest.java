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
name|ArgumentMatchers
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
comment|/**  *   * Test channel data updates are converted by camel application.  *  */
end_comment

begin_class
DECL|class|TelegramConsumerChannelPostTest
specifier|public
class|class
name|TelegramConsumerChannelPostTest
extends|extends
name|TelegramTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
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
literal|"messages/updates-channelMessage.json"
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
parameter_list|(
name|i
parameter_list|)
lambda|->
name|defaultRes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReceptionOfMessageWithAMessage ()
specifier|public
name|void
name|testReceptionOfMessageWithAMessage
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
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|mediaExchange
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
name|IncomingMessage
name|msg
init|=
name|mediaExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|IncomingMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"-1001245756934"
argument_list|,
name|mediaExchange
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
argument_list|)
expr_stmt|;
comment|//checking body
name|assertNotNull
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|msg
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
literal|67L
argument_list|)
argument_list|,
name|msg
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
literal|1546505413L
argument_list|)
argument_list|,
name|msg
operator|.
name|getDate
argument_list|()
argument_list|)
expr_stmt|;
comment|// checking chat
name|Chat
name|chat
init|=
name|msg
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
literal|"-1001245756934"
argument_list|,
name|chat
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cameltemp"
argument_list|,
name|chat
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"channel"
argument_list|,
name|chat
operator|.
name|getType
argument_list|()
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
throws|throws
name|Exception
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
literal|"telegram:bots/mock-token"
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

