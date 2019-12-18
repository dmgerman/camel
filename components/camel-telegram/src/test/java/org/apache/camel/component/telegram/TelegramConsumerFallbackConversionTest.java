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
name|ProducerTemplate
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
name|assertEquals
import|;
end_import

begin_comment
comment|/**  * Checks if conversions of generic objects are happening correctly.  */
end_comment

begin_class
DECL|class|TelegramConsumerFallbackConversionTest
specifier|public
class|class
name|TelegramConsumerFallbackConversionTest
extends|extends
name|TelegramTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
literal|"direct:message"
argument_list|)
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Test
DECL|method|testEverythingOk ()
specifier|public
name|void
name|testEverythingOk
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
operator|new
name|BrandNewType
argument_list|(
literal|"wrapped message"
argument_list|)
argument_list|)
expr_stmt|;
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
operator|==
literal|1
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
literal|1
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|msgs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getText
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"wrapped message"
argument_list|,
name|text
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
literal|"direct:message"
argument_list|)
operator|.
name|to
argument_list|(
literal|"telegram:bots?authorizationToken=mock-token&chatId=1234"
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

begin_class
DECL|class|BrandNewType
specifier|private
specifier|static
class|class
name|BrandNewType
block|{
DECL|field|message
name|String
name|message
decl_stmt|;
DECL|method|BrandNewType (String message)
name|BrandNewType
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
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
comment|// to use default conversion from Object to String
return|return
name|message
return|;
block|}
block|}
end_class

unit|}
end_unit

