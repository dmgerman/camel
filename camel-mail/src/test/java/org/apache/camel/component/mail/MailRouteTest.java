begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ContextTestSupport
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
name|builder
operator|.
name|RouteBuilder
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
name|util
operator|.
name|ObjectHelper
operator|.
name|asString
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

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|MailRouteTest
specifier|public
class|class
name|MailRouteTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|resultEndpoint
specifier|private
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
DECL|method|testSendAndReceiveMails ()
specifier|public
name|void
name|testSendAndReceiveMails
parameter_list|()
throws|throws
name|Exception
block|{
name|resultEndpoint
operator|=
operator|(
name|MockEndpoint
operator|)
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"hello world!"
argument_list|)
expr_stmt|;
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"reply-to"
argument_list|,
literal|"reply1@localhost"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"smtp://james@localhost"
argument_list|,
literal|"hello world!"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
comment|// lets test the first sent worked
name|assertMailboxReceivedMessages
argument_list|(
literal|"james@localhost"
argument_list|)
expr_stmt|;
comment|// lets sleep to check that the mail poll does not redeliver duplicate mails
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
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
literal|"reply1@localhost"
argument_list|,
name|replyTo
argument_list|)
expr_stmt|;
name|assertMailboxReceivedMessages
argument_list|(
literal|"copy@localhost"
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
name|logMessage
argument_list|(
name|message
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
literal|"smtp://james@localhost"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:a"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"smtp://result@localhost"
argument_list|,
literal|"smtp://copy@localhost"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"smtp://result@localhost"
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
DECL|method|logMessage (Message message)
specifier|protected
name|void
name|logMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
throws|,
name|MessagingException
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Received: "
operator|+
name|message
operator|.
name|getContent
argument_list|()
operator|+
literal|" from: "
operator|+
name|asString
argument_list|(
name|message
operator|.
name|getFrom
argument_list|()
argument_list|)
operator|+
literal|" to: "
operator|+
name|asString
argument_list|(
name|message
operator|.
name|getRecipients
argument_list|(
name|RecipientType
operator|.
name|TO
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

