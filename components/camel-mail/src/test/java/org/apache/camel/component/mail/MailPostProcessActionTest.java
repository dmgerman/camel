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
name|impl
operator|.
name|JndiRegistry
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Tests if post process action is called if it is set  */
end_comment

begin_class
DECL|class|MailPostProcessActionTest
specifier|public
class|class
name|MailPostProcessActionTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MailPostProcessActionTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|action
specifier|private
name|TestPostProcessAction
name|action
decl_stmt|;
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
name|action
operator|=
operator|new
name|TestPostProcessAction
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"postProcessAction"
argument_list|,
name|action
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testActionCalled ()
specifier|public
name|void
name|testActionCalled
parameter_list|()
throws|throws
name|Exception
block|{
name|Mailbox
name|mailbox
init|=
name|Mailbox
operator|.
name|get
argument_list|(
literal|"bill@localhost"
argument_list|)
decl_stmt|;
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
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"TestText"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|waitForActionCalled
argument_list|()
expr_stmt|;
block|}
DECL|method|waitForActionCalled ()
specifier|private
name|void
name|waitForActionCalled
parameter_list|()
throws|throws
name|InterruptedException
block|{
comment|// Wait for a maximum of 500 ms for the action to be called
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|50
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|action
operator|.
name|hasBeenCalled
argument_list|()
condition|)
block|{
break|break;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sleeping for 10 millis to wait for action call"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|10
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|action
operator|.
name|hasBeenCalled
argument_list|()
argument_list|)
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
literal|"bill"
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
comment|// inserts 1 new message
name|Message
index|[]
name|messages
init|=
operator|new
name|Message
index|[
literal|1
index|]
decl_stmt|;
name|messages
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
name|messages
index|[
literal|0
index|]
operator|.
name|setSubject
argument_list|(
literal|"TestSubject"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|0
index|]
operator|.
name|setHeader
argument_list|(
literal|"Message-ID"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|0
index|]
operator|.
name|setText
argument_list|(
literal|"TestText"
argument_list|)
expr_stmt|;
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
literal|"pop3://bill@localhost?password=secret&postProcessAction=#postProcessAction"
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
DECL|class|TestPostProcessAction
specifier|private
class|class
name|TestPostProcessAction
implements|implements
name|MailBoxPostProcessAction
block|{
DECL|field|called
specifier|private
name|boolean
name|called
decl_stmt|;
annotation|@
name|Override
DECL|method|process (Folder folder)
specifier|public
name|void
name|process
parameter_list|(
name|Folder
name|folder
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Assert that we are looking at the correct folder with our message
specifier|final
name|Message
index|[]
name|messages
init|=
name|folder
operator|.
name|getMessages
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|messages
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TestSubject"
argument_list|,
name|messages
index|[
literal|0
index|]
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
comment|// And mark ourselves as "called"
name|called
operator|=
literal|true
expr_stmt|;
block|}
comment|/**          * @return true if the action has been called          */
DECL|method|hasBeenCalled ()
specifier|public
name|boolean
name|hasBeenCalled
parameter_list|()
block|{
return|return
name|called
return|;
block|}
block|}
block|}
end_class

end_unit

