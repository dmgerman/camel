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
name|java
operator|.
name|util
operator|.
name|Date
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
name|com
operator|.
name|sun
operator|.
name|mail
operator|.
name|imap
operator|.
name|SortTerm
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
comment|/**  * This is a test that checks integration of the sort term in Camel. The actual sorting logic is tested in the  * SortUtilTest.  */
end_comment

begin_class
DECL|class|MailSortTermTest
specifier|public
class|class
name|MailSortTermTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
annotation|@
name|Before
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
literal|"sortAscendingDate"
argument_list|,
operator|new
name|SortTerm
index|[]
block|{
name|SortTerm
operator|.
name|DATE
block|}
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"sortDescendingDate"
argument_list|,
operator|new
name|SortTerm
index|[]
block|{
name|SortTerm
operator|.
name|REVERSE
block|,
name|SortTerm
operator|.
name|DATE
block|}
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"searchTerm"
argument_list|,
operator|new
name|SearchTermBuilder
argument_list|()
operator|.
name|subject
argument_list|(
literal|"Camel"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testSortTerm ()
specifier|public
name|void
name|testSortTerm
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
literal|3
argument_list|,
name|mailbox
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// This one has search term *not* set
name|MockEndpoint
name|mockAsc
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:resultAscending"
argument_list|)
decl_stmt|;
name|mockAsc
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Earlier date"
argument_list|,
literal|"Later date"
argument_list|)
expr_stmt|;
name|context
operator|.
name|startAllRoutes
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
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
comment|// inserts 3 messages, one with earlier, one with later sent date and one with invalid subject (not returned in search)
name|Message
index|[]
name|messages
init|=
operator|new
name|Message
index|[
literal|3
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
name|setText
argument_list|(
literal|"Earlier date"
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
name|setSentDate
argument_list|(
operator|new
name|Date
argument_list|(
literal|10000
argument_list|)
argument_list|)
expr_stmt|;
name|messages
index|[
literal|0
index|]
operator|.
name|setSubject
argument_list|(
literal|"Camel"
argument_list|)
expr_stmt|;
name|messages
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
name|messages
index|[
literal|1
index|]
operator|.
name|setText
argument_list|(
literal|"Later date"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|1
index|]
operator|.
name|setHeader
argument_list|(
literal|"Message-ID"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|1
index|]
operator|.
name|setSentDate
argument_list|(
operator|new
name|Date
argument_list|(
literal|20000
argument_list|)
argument_list|)
expr_stmt|;
name|messages
index|[
literal|1
index|]
operator|.
name|setSubject
argument_list|(
literal|"Camel"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|2
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
literal|2
index|]
operator|.
name|setText
argument_list|(
literal|"Even later date"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|2
index|]
operator|.
name|setHeader
argument_list|(
literal|"Message-ID"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|2
index|]
operator|.
name|setSentDate
argument_list|(
operator|new
name|Date
argument_list|(
literal|30000
argument_list|)
argument_list|)
expr_stmt|;
name|messages
index|[
literal|2
index|]
operator|.
name|setSubject
argument_list|(
literal|"Invalid"
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
name|context
operator|.
name|setAutoStartup
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"pop3://bill@localhost?password=secret&searchTerm=#searchTerm&sortTerm=#sortAscendingDate&consumer.initialDelay=100&consumer.delay=100"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:resultAscending"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

