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
import|import
name|org
operator|.
name|mockito
operator|.
name|ArgumentCaptor
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
name|eq
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
name|verify
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
name|uri
operator|=
literal|"direct:message"
argument_list|)
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Before
DECL|method|mockAPIs ()
specifier|public
name|void
name|mockAPIs
parameter_list|()
block|{
name|mockTelegramService
argument_list|()
expr_stmt|;
block|}
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
name|TelegramService
name|service
init|=
name|currentMockService
argument_list|()
decl_stmt|;
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
name|ArgumentCaptor
argument_list|<
name|OutgoingTextMessage
argument_list|>
name|captor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|OutgoingTextMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|service
argument_list|)
operator|.
name|sendMessage
argument_list|(
name|eq
argument_list|(
literal|"mock-token"
argument_list|)
argument_list|,
name|captor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|OutgoingTextMessage
argument_list|>
name|msgs
init|=
name|captor
operator|.
name|getAllValues
argument_list|()
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
literal|"direct:message"
argument_list|)
operator|.
name|to
argument_list|(
literal|"telegram:bots/mock-token?chatId=1234"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
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
block|}
end_class

end_unit

