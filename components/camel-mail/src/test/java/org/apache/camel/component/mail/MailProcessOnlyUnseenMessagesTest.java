begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|mail
operator|.
name|Flags
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
import|import
name|org
operator|.
name|springframework
operator|.
name|mail
operator|.
name|javamail
operator|.
name|JavaMailSenderImpl
import|;
end_import

begin_comment
comment|/**  * Unit test for unseen option.  */
end_comment

begin_class
DECL|class|MailProcessOnlyUnseenMessagesTest
specifier|public
class|class
name|MailProcessOnlyUnseenMessagesTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareMailbox
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProcessOnlyUnseenMessages ()
specifier|public
name|void
name|testProcessOnlyUnseenMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Message 3"
argument_list|)
expr_stmt|;
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
name|expectedBodiesReceived
argument_list|(
literal|"Message 3"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// reset mock so we can make new assertions
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// send a new message, now we should only receive this new massages as all the others has been SEEN
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Message 4"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Message 4"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|prepareMailbox ()
specifier|private
name|void
name|prepareMailbox
parameter_list|()
throws|throws
name|Exception
block|{
comment|// connect to mailbox
name|Mailbox
operator|.
name|clearAll
argument_list|()
expr_stmt|;
name|JavaMailSenderImpl
name|sender
init|=
operator|new
name|JavaMailSenderImpl
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
literal|"imap"
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
literal|"claus"
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
comment|// inserts two messages with the SEEN flag
name|Message
index|[]
name|msg
init|=
operator|new
name|Message
index|[
literal|2
index|]
decl_stmt|;
name|msg
index|[
literal|0
index|]
operator|=
operator|new
name|MimeMessage
argument_list|(
name|sender
operator|.
name|getSession
argument_list|()
argument_list|)
expr_stmt|;
name|msg
index|[
literal|0
index|]
operator|.
name|setText
argument_list|(
literal|"Message 1"
argument_list|)
expr_stmt|;
name|msg
index|[
literal|0
index|]
operator|.
name|setFlag
argument_list|(
name|Flags
operator|.
name|Flag
operator|.
name|SEEN
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|msg
index|[
literal|1
index|]
operator|=
operator|new
name|MimeMessage
argument_list|(
name|sender
operator|.
name|getSession
argument_list|()
argument_list|)
expr_stmt|;
name|msg
index|[
literal|1
index|]
operator|.
name|setText
argument_list|(
literal|"Message 2"
argument_list|)
expr_stmt|;
name|msg
index|[
literal|1
index|]
operator|.
name|setFlag
argument_list|(
name|Flags
operator|.
name|Flag
operator|.
name|SEEN
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|folder
operator|.
name|appendMessages
argument_list|(
name|msg
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
literal|"direct:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"smtp://claus@localhost"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"imap://localhost?username=claus&password=secret&unseen=true&consumer.delay=1000"
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

