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
name|IncomingDocument
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
comment|/**  * Tests the reception of messages without text having media content.  */
end_comment

begin_class
DECL|class|TelegramConsumerMediaDocumentTest
specifier|public
class|class
name|TelegramConsumerMediaDocumentTest
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
name|res
init|=
name|getJSONResource
argument_list|(
literal|"messages/updates-media-document.json"
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
name|res
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
DECL|method|testReceptionOfMessageWithADocument ()
specifier|public
name|void
name|testReceptionOfMessageWithADocument
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
name|IncomingDocument
name|document
init|=
name|msg
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AgADBAADq6cxG0bQcwnUb4Cga-eXxnodQxkABLXiiSI1vzZK8XXXXXXX"
argument_list|,
name|document
operator|.
name|getFileId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|12530
argument_list|)
argument_list|,
name|document
operator|.
name|getFileSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"file.png"
argument_list|,
name|document
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"image/png"
argument_list|,
name|document
operator|.
name|getMimeType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|document
operator|.
name|getThumb
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|90
argument_list|)
argument_list|,
name|document
operator|.
name|getThumb
argument_list|()
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|80
argument_list|)
argument_list|,
name|document
operator|.
name|getThumb
argument_list|()
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|1253
argument_list|)
argument_list|,
name|document
operator|.
name|getThumb
argument_list|()
operator|.
name|getFileSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AgADBAADq6cxG0bQcwnUb4Cga-eXxnodQxkABLXiiSI1vzZK9XXXXXXX"
argument_list|,
name|document
operator|.
name|getThumb
argument_list|()
operator|.
name|getFileId
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

