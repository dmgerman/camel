begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zendesk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zendesk
package|;
end_package

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
name|Iterator
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
name|zendesk
operator|.
name|internal
operator|.
name|ZendeskApiMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_import
import|import
name|org
operator|.
name|zendesk
operator|.
name|client
operator|.
name|v2
operator|.
name|model
operator|.
name|Comment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|zendesk
operator|.
name|client
operator|.
name|v2
operator|.
name|model
operator|.
name|Ticket
import|;
end_import

begin_comment
comment|/**  * The integration tests for ticket related Zendesk API.  */
end_comment

begin_class
DECL|class|ZendeskTicketIntegrationTest
specifier|public
class|class
name|ZendeskTicketIntegrationTest
extends|extends
name|AbstractZendeskTestSupport
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
name|ZendeskTicketIntegrationTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testGetTickets ()
specifier|public
name|void
name|testGetTickets
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Iterable
argument_list|<
name|?
argument_list|>
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://GETTICKETS"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"getTickets result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|ticket
range|:
name|result
control|)
block|{
name|LOG
operator|.
name|info
argument_list|(
name|ticket
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
name|count
operator|+
literal|" ticket(s) in total."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateUpdateCommentDeleteTicket ()
specifier|public
name|void
name|testCreateUpdateCommentDeleteTicket
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create new ticket
name|String
name|ticketSubject
init|=
literal|"Test Ticket"
decl_stmt|;
name|String
name|ticketDescription
init|=
literal|"This is a test ticket from camel-zendesk."
decl_stmt|;
name|Ticket
name|input
init|=
operator|new
name|Ticket
argument_list|()
decl_stmt|;
name|input
operator|.
name|setSubject
argument_list|(
name|ticketSubject
argument_list|)
expr_stmt|;
name|input
operator|.
name|setDescription
argument_list|(
name|ticketDescription
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|input
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|input
operator|.
name|getCreatedAt
argument_list|()
argument_list|)
expr_stmt|;
name|Ticket
name|answer
init|=
name|requestBody
argument_list|(
literal|"direct://CREATETICKET"
argument_list|,
name|input
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|answer
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|answer
operator|.
name|getCreatedAt
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|answer
operator|.
name|getCreatedAt
argument_list|()
argument_list|,
name|answer
operator|.
name|getUpdatedAt
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ticketSubject
argument_list|,
name|answer
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ticketDescription
argument_list|,
name|answer
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
comment|// update ticket description
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|String
name|ticketSubjectUpdated
init|=
name|ticketSubject
operator|+
literal|" And updated."
decl_stmt|;
name|input
operator|=
operator|new
name|Ticket
argument_list|()
expr_stmt|;
name|input
operator|.
name|setId
argument_list|(
name|answer
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|input
operator|.
name|setSubject
argument_list|(
name|ticketSubjectUpdated
argument_list|)
expr_stmt|;
name|answer
operator|=
name|requestBody
argument_list|(
literal|"direct://UPDATETICKET"
argument_list|,
name|input
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotEquals
argument_list|(
name|answer
operator|.
name|getCreatedAt
argument_list|()
argument_list|,
name|answer
operator|.
name|getUpdatedAt
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ticketSubjectUpdated
argument_list|,
name|answer
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ticketDescription
argument_list|,
name|answer
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
comment|// get ticket and compare
name|Ticket
name|answer2
init|=
name|requestBody
argument_list|(
literal|"direct://GETTICKET"
argument_list|,
name|answer
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|answer
operator|.
name|getSubject
argument_list|()
argument_list|,
name|answer2
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|answer
operator|.
name|getDescription
argument_list|()
argument_list|,
name|answer2
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|answer
operator|.
name|getId
argument_list|()
argument_list|,
name|answer2
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|answer
operator|.
name|getCreatedAt
argument_list|()
argument_list|,
name|answer2
operator|.
name|getCreatedAt
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|answer
operator|.
name|getUpdatedAt
argument_list|()
argument_list|,
name|answer2
operator|.
name|getUpdatedAt
argument_list|()
argument_list|)
expr_stmt|;
comment|// add a comment to the ticket
name|String
name|commentBody
init|=
literal|"This is a comment from camel-zendesk."
decl_stmt|;
specifier|final
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
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"ticketId"
argument_list|,
name|ZendeskApiMethod
operator|.
name|CREATECOMMENT
operator|.
name|getArgNames
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|long
operator|.
name|class
argument_list|,
name|ZendeskApiMethod
operator|.
name|CREATECOMMENT
operator|.
name|getArgTypes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"CamelZendesk.ticketId"
argument_list|,
name|answer
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|Comment
name|comment
init|=
operator|new
name|Comment
argument_list|()
decl_stmt|;
name|comment
operator|.
name|setBody
argument_list|(
name|commentBody
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|comment
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|comment
operator|.
name|getCreatedAt
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"comment"
argument_list|,
name|ZendeskApiMethod
operator|.
name|CREATECOMMENT
operator|.
name|getArgNames
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Comment
operator|.
name|class
argument_list|,
name|ZendeskApiMethod
operator|.
name|CREATECOMMENT
operator|.
name|getArgTypes
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"CamelZendesk.comment"
argument_list|,
name|comment
argument_list|)
expr_stmt|;
name|requestBodyAndHeaders
argument_list|(
literal|"direct://CREATECOMMENT"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|Iterable
name|iterable
init|=
name|requestBody
argument_list|(
literal|"direct://GETTICKETCOMMENTS"
argument_list|,
name|answer
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
name|iterator
init|=
name|iterable
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Comment
name|comment1
init|=
operator|(
name|Comment
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ticketDescription
argument_list|,
name|comment1
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|comment1
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|comment1
operator|.
name|getCreatedAt
argument_list|()
argument_list|)
expr_stmt|;
name|Comment
name|comment2
init|=
operator|(
name|Comment
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|commentBody
argument_list|,
name|comment2
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|comment2
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|comment2
operator|.
name|getCreatedAt
argument_list|()
argument_list|)
expr_stmt|;
comment|// delete ticket
name|requestBody
argument_list|(
literal|"direct://DELETETICKET"
argument_list|,
name|answer
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|Ticket
name|mustBeDeleted
init|=
name|requestBody
argument_list|(
literal|"direct://GETTICKET"
argument_list|,
name|answer
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|mustBeDeleted
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInBodyParams ()
specifier|public
name|void
name|testInBodyParams
parameter_list|()
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"ticket"
argument_list|,
name|ZendeskApiMethod
operator|.
name|CREATETICKET
operator|.
name|getArgNames
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Ticket
operator|.
name|class
argument_list|,
name|ZendeskApiMethod
operator|.
name|CREATETICKET
operator|.
name|getArgTypes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"ticket"
argument_list|,
name|ZendeskApiMethod
operator|.
name|UPDATETICKET
operator|.
name|getArgNames
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Ticket
operator|.
name|class
argument_list|,
name|ZendeskApiMethod
operator|.
name|UPDATETICKET
operator|.
name|getArgTypes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"id"
argument_list|,
name|ZendeskApiMethod
operator|.
name|GETTICKET
operator|.
name|getArgNames
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|long
operator|.
name|class
argument_list|,
name|ZendeskApiMethod
operator|.
name|GETTICKET
operator|.
name|getArgTypes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"id"
argument_list|,
name|ZendeskApiMethod
operator|.
name|GETTICKETCOMMENTS
operator|.
name|getArgNames
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|long
operator|.
name|class
argument_list|,
name|ZendeskApiMethod
operator|.
name|GETTICKETCOMMENTS
operator|.
name|getArgTypes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
block|{
name|from
argument_list|(
literal|"direct://GETTICKETS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"zendesk://getTickets"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://CREATETICKET"
argument_list|)
operator|.
name|to
argument_list|(
literal|"zendesk://createTicket?inBody=ticket"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://UPDATETICKET"
argument_list|)
operator|.
name|to
argument_list|(
literal|"zendesk://updateTicket?inBody=ticket"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://GETTICKET"
argument_list|)
operator|.
name|to
argument_list|(
literal|"zendesk://getTicket?inBody=id"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://CREATECOMMENT"
argument_list|)
operator|.
name|to
argument_list|(
literal|"zendesk://createComment"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://GETTICKETCOMMENTS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"zendesk://getTicketComments?inBody=id"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct://DELETETICKET"
argument_list|)
operator|.
name|to
argument_list|(
literal|"zendesk://deleteTicket?inBody=id"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

