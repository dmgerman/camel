begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|FileDataSource
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
name|Producer
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
name|attachment
operator|.
name|AttachmentMessage
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jvnet
operator|.
name|mock_javamail
operator|.
name|Mailbox
import|;
end_import

begin_comment
comment|/**  * Unit test for Camel attachments and Mail attachments.  */
end_comment

begin_class
DECL|class|MailAttachmentDuplicateNamesTest
specifier|public
class|class
name|MailAttachmentDuplicateNamesTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testSendAndRecieveMailWithAttachmentsWithDuplicateNames ()
specifier|public
name|void
name|testSendAndRecieveMailWithAttachmentsWithDuplicateNames
parameter_list|()
throws|throws
name|Exception
block|{
comment|// clear mailbox
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
comment|// START SNIPPET: e1
comment|// create an exchange with a normal body and attachment to be produced as email
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"smtp://james@mymailserver.com?password=secret"
argument_list|)
decl_stmt|;
comment|// create the exchange with the mail message that is multipart with a file and a Hello World text/plain message.
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|AttachmentMessage
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|in
operator|.
name|addAttachment
argument_list|(
literal|"logo.jpeg"
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|FileDataSource
argument_list|(
literal|"src/test/data/logo.jpeg"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|in
operator|.
name|addAttachment
argument_list|(
literal|"logo.jpeg"
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|FileDataSource
argument_list|(
literal|"src/test/data/logo.jpeg"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// create a producer that can produce the exchange (= send the mail)
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
comment|// start the producer
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// and let it go (processes the exchange by sending the email)
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|out
init|=
name|mock
operator|.
name|assertExchangeReceived
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// plain text
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|out
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
argument_list|)
expr_stmt|;
comment|// attachment
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|attachments
init|=
name|out
operator|.
name|getIn
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have attachments"
argument_list|,
name|attachments
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|attachments
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataHandler
name|handler
init|=
name|out
operator|.
name|getIn
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
operator|.
name|getAttachment
argument_list|(
literal|"logo.jpeg"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The logo should be there"
argument_list|,
name|handler
argument_list|)
expr_stmt|;
comment|// content type should match
name|boolean
name|match1
init|=
literal|"image/jpeg; name=logo.jpeg"
operator|.
name|equals
argument_list|(
name|handler
operator|.
name|getContentType
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|match2
init|=
literal|"application/octet-stream; name=logo.jpeg"
operator|.
name|equals
argument_list|(
name|handler
operator|.
name|getContentType
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match 1 or 2"
argument_list|,
name|match1
operator|||
name|match2
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"pop3://james@mymailserver.com?password=secret&initialDelay=100&delay=100"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

