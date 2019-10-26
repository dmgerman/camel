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
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeMultipart
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
operator|.
name|MailConstants
operator|.
name|MAIL_ALTERNATIVE_BODY
import|;
end_import

begin_class
DECL|class|MimeMultipartAlternativeTest
specifier|public
class|class
name|MimeMultipartAlternativeTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|alternativeBody
specifier|private
name|String
name|alternativeBody
init|=
literal|"hello world! (plain text)"
decl_stmt|;
DECL|field|htmlBody
specifier|private
name|String
name|htmlBody
init|=
literal|"<html><body><h1>Hello</h1>World<img src=\"cid:0001\"></body></html>"
decl_stmt|;
DECL|method|sendMultipartEmail (boolean useInlineattachments)
specifier|private
name|void
name|sendMultipartEmail
parameter_list|(
name|boolean
name|useInlineattachments
parameter_list|)
throws|throws
name|Exception
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
comment|// create an exchange with a normal body and attachment to be produced as email
name|MailEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"smtp://ryan@mymailserver.com?password=secret"
argument_list|,
name|MailEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setUseInlineAttachments
argument_list|(
name|useInlineattachments
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setAlternativeBodyHeader
argument_list|(
name|MailConstants
operator|.
name|MAIL_ALTERNATIVE_BODY
argument_list|)
expr_stmt|;
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
name|htmlBody
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|MAIL_ALTERNATIVE_BODY
argument_list|,
name|alternativeBody
argument_list|)
expr_stmt|;
name|in
operator|.
name|addAttachment
argument_list|(
literal|"cid:0001"
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
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
name|MailConstants
operator|.
name|MAIL_ALTERNATIVE_BODY
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|context
operator|.
name|createProducerTemplate
argument_list|()
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|verifyTheRecivedEmail (String expectString)
specifier|private
name|void
name|verifyTheRecivedEmail
parameter_list|(
name|String
name|expectString
parameter_list|)
throws|throws
name|Exception
block|{
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
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|(
operator|(
operator|(
name|MailMessage
operator|)
name|out
operator|.
name|getIn
argument_list|()
operator|)
operator|.
name|getMessage
argument_list|()
operator|.
name|getSize
argument_list|()
argument_list|)
decl_stmt|;
operator|(
operator|(
name|MailMessage
operator|)
name|out
operator|.
name|getIn
argument_list|()
operator|)
operator|.
name|getMessage
argument_list|()
operator|.
name|writeTo
argument_list|(
name|baos
argument_list|)
expr_stmt|;
name|String
name|dumpedMessage
init|=
name|baos
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"There should have the "
operator|+
name|expectString
argument_list|,
name|dumpedMessage
operator|.
name|indexOf
argument_list|(
name|expectString
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"multipart alternative: \n{}"
argument_list|,
name|dumpedMessage
argument_list|)
expr_stmt|;
comment|// plain text
name|assertEquals
argument_list|(
name|alternativeBody
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
literal|"Should not have null attachments"
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
name|assertEquals
argument_list|(
literal|"multipart body should have 2 parts"
argument_list|,
literal|2
argument_list|,
name|out
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|MimeMultipart
operator|.
name|class
argument_list|)
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMultipartEmailWithInlineAttachments ()
specifier|public
name|void
name|testMultipartEmailWithInlineAttachments
parameter_list|()
throws|throws
name|Exception
block|{
name|sendMultipartEmail
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|verifyTheRecivedEmail
argument_list|(
literal|"Content-Disposition: inline; filename=0001"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMultipartEmailWithRegularAttachments ()
specifier|public
name|void
name|testMultipartEmailWithRegularAttachments
parameter_list|()
throws|throws
name|Exception
block|{
name|sendMultipartEmail
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|verifyTheRecivedEmail
argument_list|(
literal|"Content-Disposition: attachment; filename=0001"
argument_list|)
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
literal|"pop3://ryan@mymailserver.com?password=secret&initialDelay=100&delay=100"
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

