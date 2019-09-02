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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|mail
operator|.
name|Folder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|MessagingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Store
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
name|MimeMessage
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
name|Attachment
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
name|hamcrest
operator|.
name|core
operator|.
name|AnyOf
operator|.
name|anyOf
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|IsEqual
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|StringContains
operator|.
name|containsString
import|;
end_import

begin_class
DECL|class|NestedMimeMessageConsumeTest
specifier|public
class|class
name|NestedMimeMessageConsumeTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testNestedMultipart ()
specifier|public
name|void
name|testNestedMultipart
parameter_list|()
throws|throws
name|Exception
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|prepareMailbox
argument_list|(
literal|"james3"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|resultEndpoint
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|text
init|=
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
decl_stmt|;
name|assertThat
argument_list|(
name|text
argument_list|,
name|containsString
argument_list|(
literal|"Test with bold face, pictures and attachments"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/plain; charset=us-ascii"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Content-Type"
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|attachmentNames
init|=
name|exchange
operator|.
name|getIn
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
operator|.
name|getAttachmentNames
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"attachments got lost"
argument_list|,
name|attachmentNames
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|attachmentNames
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|s
range|:
name|attachmentNames
control|)
block|{
name|Attachment
name|att
init|=
name|exchange
operator|.
name|getIn
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
operator|.
name|getAttachmentObject
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|DataHandler
name|dh
init|=
name|att
operator|.
name|getDataHandler
argument_list|()
decl_stmt|;
name|Object
name|content
init|=
name|dh
operator|.
name|getContent
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Content should not be empty"
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|dh
operator|.
name|getName
argument_list|()
argument_list|,
name|anyOf
argument_list|(
name|equalTo
argument_list|(
literal|"image001.png"
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"test.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|prepareMailbox (String user)
specifier|private
name|void
name|prepareMailbox
parameter_list|(
name|String
name|user
parameter_list|)
throws|throws
name|MessagingException
block|{
comment|// connect to mailbox
name|JavaMailSender
name|sender
init|=
operator|new
name|DefaultJavaMailSender
argument_list|()
decl_stmt|;
name|Store
name|store
init|=
name|sender
operator|.
name|getSession
argument_list|()
operator|.
name|getStore
argument_list|(
literal|"pop3"
argument_list|)
decl_stmt|;
name|store
operator|.
name|connect
argument_list|(
literal|"localhost"
argument_list|,
literal|25
argument_list|,
name|user
argument_list|,
literal|"secret"
argument_list|)
expr_stmt|;
name|Folder
name|folder
init|=
name|store
operator|.
name|getFolder
argument_list|(
literal|"INBOX"
argument_list|)
decl_stmt|;
name|folder
operator|.
name|open
argument_list|(
name|Folder
operator|.
name|READ_WRITE
argument_list|)
expr_stmt|;
name|folder
operator|.
name|expunge
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/nested-multipart.elm"
argument_list|)
decl_stmt|;
name|Message
name|hurzMsg
init|=
operator|new
name|MimeMessage
argument_list|(
name|sender
operator|.
name|getSession
argument_list|()
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|Message
index|[]
name|messages
init|=
operator|new
name|Message
index|[]
block|{
name|hurzMsg
block|}
decl_stmt|;
comment|// insert one signed message
name|folder
operator|.
name|appendMessages
argument_list|(
name|messages
argument_list|)
expr_stmt|;
name|folder
operator|.
name|close
argument_list|(
literal|true
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
block|{
name|from
argument_list|(
literal|"pop3://james3@localhost?initialDelay=100&delay=100"
argument_list|)
operator|.
name|removeHeader
argument_list|(
literal|"to"
argument_list|)
operator|.
name|to
argument_list|(
literal|"smtp://james4@localhost"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"pop3://james4@localhost?initialDelay=200&delay=100"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
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

