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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|mail
operator|.
name|Address
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
name|Message
operator|.
name|RecipientType
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
name|Processor
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

begin_class
DECL|class|MailRouteTest
specifier|public
class|class
name|MailRouteTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testSendAndReceiveMails ()
specifier|public
name|void
name|testSendAndReceiveMails
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
name|expectedBodiesReceived
argument_list|(
literal|"hello world!"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"reply-to"
argument_list|,
literal|"route-test-reply@localhost"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"smtp://route-test-james@localhost"
argument_list|,
literal|"hello world!"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
comment|// lets test the first sent worked
name|assertMailboxReceivedMessages
argument_list|(
literal|"route-test-james@localhost"
argument_list|)
expr_stmt|;
comment|// lets test the receive worked
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// Validate that the headers were preserved.
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
name|replyTo
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"reply-to"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"route-test-reply@localhost"
argument_list|,
name|replyTo
argument_list|)
expr_stmt|;
name|assertMailboxReceivedMessages
argument_list|(
literal|"route-test-copy@localhost"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMailSubjectWithUnicode ()
specifier|public
name|void
name|testMailSubjectWithUnicode
parameter_list|()
throws|throws
name|Exception
block|{
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
specifier|final
name|String
name|body
init|=
literal|"Hello Camel Riders!"
decl_stmt|;
specifier|final
name|String
name|subject
init|=
literal|"My Camel \u2122"
decl_stmt|;
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
comment|// now we don't use the UTF-8 encoding
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"subject"
argument_list|,
literal|"=?US-ASCII?Q?My_Camel_=3F?="
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:a"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"subject"
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"US-ASCII"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should not have attachements"
argument_list|,
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|(
name|AttachmentMessage
operator|.
name|class
argument_list|)
operator|.
name|hasAttachments
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertMailboxReceivedMessages (String name)
specifier|protected
name|void
name|assertMailboxReceivedMessages
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
throws|,
name|MessagingException
block|{
name|Mailbox
name|mailbox
init|=
name|Mailbox
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|name
operator|+
literal|" should have received 1 mail"
argument_list|,
literal|1
argument_list|,
name|mailbox
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|mailbox
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|name
operator|+
literal|" should have received at least one mail!"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hello world!"
argument_list|,
name|message
operator|.
name|getContent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"camel@localhost"
argument_list|,
name|message
operator|.
name|getFrom
argument_list|()
index|[
literal|0
index|]
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Address
name|adr
range|:
name|message
operator|.
name|getRecipients
argument_list|(
name|RecipientType
operator|.
name|TO
argument_list|)
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|adr
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Should have found the recpient to in the mail: "
operator|+
name|name
argument_list|,
name|found
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
literal|"pop3://route-test-james@localhost?initialDelay=100&delay=100"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:a"
argument_list|)
expr_stmt|;
comment|// must use fixed to option to send the mail to the given
comment|// reciever, as we have polled
comment|// a mail from a mailbox where it already has the 'old' To as
comment|// header value
comment|// here we send the mail to 2 recievers. notice we can use a
comment|// plain string with semi colon
comment|// to seperate the mail addresses
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"to"
argument_list|,
name|constant
argument_list|(
literal|"route-test-result@localhost; route-test-copy@localhost"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"smtp://localhost"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"pop3://route-test-result@localhost?initialDelay=100&delay=100"
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

