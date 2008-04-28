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

begin_comment
comment|/**  * Unit test for processOnlyUnseenMessages option.  */
end_comment

begin_class
DECL|class|MailProcessOnlyUnseenMessagesTest
specifier|public
class|class
name|MailProcessOnlyUnseenMessagesTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testProcessOnlyUnseenMessages ()
specifier|public
name|void
name|testProcessOnlyUnseenMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareMailbox
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:a"
argument_list|,
literal|"Message 3"
argument_list|)
expr_stmt|;
comment|// let Camel have time to consume and process the mailbox
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
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
comment|// TODO: below fails for Mail component, maybe a converter is missing
comment|//mock.expectedBodiesReceived("Message 3");
name|String
name|body
init|=
name|mock
operator|.
name|assertExchangeReceived
argument_list|(
literal|0
argument_list|)
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
name|assertEquals
argument_list|(
literal|"Message 3"
argument_list|,
name|body
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
name|JavaMailConnection
name|connection
init|=
operator|new
name|JavaMailConnection
argument_list|()
decl_stmt|;
name|connection
operator|.
name|setProtocol
argument_list|(
literal|"pop3"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setHost
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setPort
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setUsername
argument_list|(
literal|"claus"
argument_list|)
expr_stmt|;
comment|// inserts two messages with the SEEN flag
name|Folder
name|folder
init|=
name|connection
operator|.
name|getFolder
argument_list|(
literal|"pop3"
argument_list|,
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
name|connection
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
name|connection
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
comment|// START SNIPPET: e1
comment|// consume only new unseen massages from the mailbox and poll the mailbox with 60 seconds interval
name|long
name|delay
init|=
literal|60
operator|*
literal|1000L
decl_stmt|;
name|from
argument_list|(
literal|"pop3://localhost?username=claus&password=secret&processOnlyUnseenMessages=true&consumer.delay="
operator|+
name|delay
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

