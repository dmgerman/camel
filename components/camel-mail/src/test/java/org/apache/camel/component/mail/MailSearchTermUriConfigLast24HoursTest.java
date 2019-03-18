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
name|InternetAddress
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

begin_class
DECL|class|MailSearchTermUriConfigLast24HoursTest
specifier|public
class|class
name|MailSearchTermUriConfigLast24HoursTest
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
name|Test
DECL|method|testSearchTerm ()
specifier|public
name|void
name|testSearchTerm
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
literal|6
argument_list|,
name|mailbox
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// should only get the 4 latest emails that was sent within the last 24 hours
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
literal|"Ordering ActiveMQ in Action"
argument_list|,
literal|"This is spam"
argument_list|,
literal|"We meet at 7pm the usual place"
argument_list|,
literal|"I am attaching you"
argument_list|)
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
name|long
name|twoDaysAgo
init|=
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
operator|-
literal|2
operator|*
literal|24
operator|*
literal|60
operator|*
literal|60
operator|*
literal|1000L
decl_stmt|;
name|long
name|twentyHoursAgo
init|=
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
operator|-
literal|1
operator|*
literal|20
operator|*
literal|60
operator|*
literal|60
operator|*
literal|1000L
decl_stmt|;
name|long
name|oneHourAgo
init|=
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
operator|-
literal|1
operator|*
literal|1
operator|*
literal|60
operator|*
literal|60
operator|*
literal|1000L
decl_stmt|;
comment|// inserts 5 new messages
name|Message
index|[]
name|messages
init|=
operator|new
name|Message
index|[
literal|6
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
literal|"Apache Camel rocks"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|0
index|]
operator|.
name|setText
argument_list|(
literal|"I like riding the Camel"
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
name|setFrom
argument_list|(
operator|new
name|InternetAddress
argument_list|(
literal|"someone@somewhere.com"
argument_list|)
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
name|twoDaysAgo
argument_list|)
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
name|setSubject
argument_list|(
literal|"Order"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|1
index|]
operator|.
name|setText
argument_list|(
literal|"Ordering Camel in Action"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|1
index|]
operator|.
name|setFrom
argument_list|(
operator|new
name|InternetAddress
argument_list|(
literal|"dude@somewhere.com"
argument_list|)
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
name|twoDaysAgo
argument_list|)
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
name|setSubject
argument_list|(
literal|"Order"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|2
index|]
operator|.
name|setText
argument_list|(
literal|"Ordering ActiveMQ in Action"
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
name|setFrom
argument_list|(
operator|new
name|InternetAddress
argument_list|(
literal|"dude@somewhere.com"
argument_list|)
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
name|twentyHoursAgo
argument_list|)
argument_list|)
expr_stmt|;
name|messages
index|[
literal|3
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
literal|3
index|]
operator|.
name|setSubject
argument_list|(
literal|"Buy pharmacy"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|3
index|]
operator|.
name|setText
argument_list|(
literal|"This is spam"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|3
index|]
operator|.
name|setHeader
argument_list|(
literal|"Message-ID"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|3
index|]
operator|.
name|setFrom
argument_list|(
operator|new
name|InternetAddress
argument_list|(
literal|"spam@me.com"
argument_list|)
argument_list|)
expr_stmt|;
name|messages
index|[
literal|3
index|]
operator|.
name|setSentDate
argument_list|(
operator|new
name|Date
argument_list|(
name|twentyHoursAgo
argument_list|)
argument_list|)
expr_stmt|;
name|messages
index|[
literal|4
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
literal|4
index|]
operator|.
name|setSubject
argument_list|(
literal|"Beers tonight?"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|4
index|]
operator|.
name|setText
argument_list|(
literal|"We meet at 7pm the usual place"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|4
index|]
operator|.
name|setHeader
argument_list|(
literal|"Message-ID"
argument_list|,
literal|"4"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|4
index|]
operator|.
name|setFrom
argument_list|(
operator|new
name|InternetAddress
argument_list|(
literal|"barney@simpsons.com"
argument_list|)
argument_list|)
expr_stmt|;
name|messages
index|[
literal|4
index|]
operator|.
name|setSentDate
argument_list|(
operator|new
name|Date
argument_list|(
name|oneHourAgo
argument_list|)
argument_list|)
expr_stmt|;
name|messages
index|[
literal|5
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
literal|5
index|]
operator|.
name|setSubject
argument_list|(
literal|"Spambot attack"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|5
index|]
operator|.
name|setText
argument_list|(
literal|"I am attaching you"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|5
index|]
operator|.
name|setHeader
argument_list|(
literal|"Message-ID"
argument_list|,
literal|"5"
argument_list|)
expr_stmt|;
name|messages
index|[
literal|5
index|]
operator|.
name|setFrom
argument_list|(
operator|new
name|InternetAddress
argument_list|(
literal|"spambot@me.com"
argument_list|)
argument_list|)
expr_stmt|;
name|messages
index|[
literal|5
index|]
operator|.
name|setSentDate
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|messages
index|[
literal|5
index|]
operator|.
name|setSentDate
argument_list|(
operator|new
name|Date
argument_list|(
name|oneHourAgo
argument_list|)
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
literal|"pop3://bill@localhost?password=secret&searchTerm.fromSentDate=now-24h&consumer.initialDelay=100&consumer.delay=100"
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

