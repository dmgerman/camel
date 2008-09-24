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
comment|/**  * Unit test for fetch size.  */
end_comment

begin_class
DECL|class|MailFetchSizeTest
specifier|public
class|class
name|MailFetchSizeTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testFetchSize ()
specifier|public
name|void
name|testFetchSize
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareMailbox
argument_list|()
expr_stmt|;
name|Mailbox
name|mailbox
init|=
name|Mailbox
operator|.
name|get
argument_list|(
literal|"jones@localhost"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|mailbox
operator|.
name|size
argument_list|()
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
literal|2
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Message 0"
argument_list|,
literal|"Message 1"
argument_list|)
expr_stmt|;
comment|// should be done within 2 seconds as no delay when started
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|2000L
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|mailbox
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// reset mock to assert the next batch of 2 messages polled
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Message 2"
argument_list|,
literal|"Message 3"
argument_list|)
expr_stmt|;
comment|// should be done within 5 (delay) + 1 seconds (polling)
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|7000L
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mailbox
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// reset mock to assert the last message polled
name|mock
operator|.
name|reset
argument_list|()
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
literal|"jones"
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
comment|// inserts 5 new messages
name|Message
index|[]
name|messages
init|=
operator|new
name|Message
index|[
literal|5
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|messages
index|[
name|i
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
name|messages
index|[
name|i
index|]
operator|.
name|setText
argument_list|(
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
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
literal|"pop3://jones@localhost?password=secret&fetchSize=2&consumer.delay=5000"
operator|+
literal|"&deleteProcessedMessages=true"
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

