begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.calendar
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|calendar
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|TimeZone
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|util
operator|.
name|DateTime
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|calendar
operator|.
name|model
operator|.
name|Event
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|calendar
operator|.
name|model
operator|.
name|EventAttendee
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|calendar
operator|.
name|model
operator|.
name|EventDateTime
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
name|google
operator|.
name|calendar
operator|.
name|internal
operator|.
name|CalendarEventsApiMethod
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
name|google
operator|.
name|calendar
operator|.
name|internal
operator|.
name|GoogleCalendarApiCollection
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

begin_comment
comment|/**  * Test class for {@link com.google.api.services.calendar.Calendar$Events} APIs.  */
end_comment

begin_class
DECL|class|CalendarEventsIntegrationTest
specifier|public
class|class
name|CalendarEventsIntegrationTest
extends|extends
name|AbstractGoogleCalendarTestSupport
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
name|CalendarEventsIntegrationTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PATH_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|PATH_PREFIX
init|=
name|GoogleCalendarApiCollection
operator|.
name|getCollection
argument_list|()
operator|.
name|getApiName
argument_list|(
name|CalendarEventsApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testInsert ()
specifier|public
name|void
name|testInsert
parameter_list|()
throws|throws
name|Exception
block|{
name|Event
name|event
init|=
operator|new
name|Event
argument_list|()
decl_stmt|;
name|event
operator|.
name|setSummary
argument_list|(
literal|"Feed the Camel"
argument_list|)
expr_stmt|;
name|event
operator|.
name|setLocation
argument_list|(
literal|"Somewhere"
argument_list|)
expr_stmt|;
name|ArrayList
argument_list|<
name|EventAttendee
argument_list|>
name|attendees
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|attendees
operator|.
name|add
argument_list|(
operator|new
name|EventAttendee
argument_list|()
operator|.
name|setEmail
argument_list|(
literal|"camel-google-calendar.janstey@gmail.com"
argument_list|)
argument_list|)
expr_stmt|;
name|event
operator|.
name|setAttendees
argument_list|(
name|attendees
argument_list|)
expr_stmt|;
name|Date
name|startDate
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|Date
name|endDate
init|=
operator|new
name|Date
argument_list|(
name|startDate
operator|.
name|getTime
argument_list|()
operator|+
literal|3600000
argument_list|)
decl_stmt|;
name|DateTime
name|start
init|=
operator|new
name|DateTime
argument_list|(
name|startDate
argument_list|,
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"UTC"
argument_list|)
argument_list|)
decl_stmt|;
name|event
operator|.
name|setStart
argument_list|(
operator|new
name|EventDateTime
argument_list|()
operator|.
name|setDateTime
argument_list|(
name|start
argument_list|)
argument_list|)
expr_stmt|;
name|DateTime
name|end
init|=
operator|new
name|DateTime
argument_list|(
name|endDate
argument_list|,
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"UTC"
argument_list|)
argument_list|)
decl_stmt|;
name|event
operator|.
name|setEnd
argument_list|(
operator|new
name|EventDateTime
argument_list|()
operator|.
name|setDateTime
argument_list|(
name|end
argument_list|)
argument_list|)
expr_stmt|;
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
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleCalendar.calendarId"
argument_list|,
name|getCalendar
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is com.google.api.services.calendar.model.Event
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleCalendar.content"
argument_list|,
name|event
argument_list|)
expr_stmt|;
specifier|final
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|calendar
operator|.
name|model
operator|.
name|Event
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://INSERT"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Feed the Camel"
argument_list|,
name|result
operator|.
name|getSummary
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"insert: "
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testManipulatingAnEvent ()
specifier|public
name|void
name|testManipulatingAnEvent
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Add an event
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
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleCalendar.calendarId"
argument_list|,
name|getCalendar
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleCalendar.text"
argument_list|,
literal|"Feed the Camel"
argument_list|)
expr_stmt|;
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|calendar
operator|.
name|model
operator|.
name|Event
name|result
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://QUICKADD"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"quickAdd result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// Check if it is in the list of events for this calendar
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|calendar
operator|.
name|model
operator|.
name|Events
name|events
init|=
name|requestBody
argument_list|(
literal|"direct://LIST"
argument_list|,
name|getCalendar
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|Event
name|item
init|=
name|events
operator|.
name|getItems
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|eventId
init|=
name|item
operator|.
name|getId
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Feed the Camel"
argument_list|,
name|item
operator|.
name|getSummary
argument_list|()
argument_list|)
expr_stmt|;
comment|// Get the event metadata
name|headers
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleCalendar.calendarId"
argument_list|,
name|getCalendar
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleCalendar.eventId"
argument_list|,
name|eventId
argument_list|)
expr_stmt|;
name|result
operator|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://GET"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Feed the Camel"
argument_list|,
name|result
operator|.
name|getSummary
argument_list|()
argument_list|)
expr_stmt|;
comment|// Change the event
name|result
operator|.
name|setSummary
argument_list|(
literal|"Feed the Camel later"
argument_list|)
expr_stmt|;
comment|// parameter type is com.google.api.services.calendar.model.Event
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleCalendar.content"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|Event
name|newResult
init|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://UPDATE"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Feed the Camel later"
argument_list|,
name|newResult
operator|.
name|getSummary
argument_list|()
argument_list|)
expr_stmt|;
comment|// Delete the event
name|headers
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleCalendar.calendarId"
argument_list|,
name|getCalendar
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// parameter type is String
name|headers
operator|.
name|put
argument_list|(
literal|"CamelGoogleCalendar.eventId"
argument_list|,
name|eventId
argument_list|)
expr_stmt|;
name|result
operator|=
name|requestBodyAndHeaders
argument_list|(
literal|"direct://DELETE"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
comment|// Check if it is NOT in the list of events for this calendar
name|events
operator|=
name|requestBody
argument_list|(
literal|"direct://LIST"
argument_list|,
name|getCalendar
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|events
operator|.
name|getItems
argument_list|()
operator|.
name|size
argument_list|()
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// test route for calendarImport
name|from
argument_list|(
literal|"direct://CALENDARIMPORT"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-calendar://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/calendarImport"
argument_list|)
expr_stmt|;
comment|// test route for delete
name|from
argument_list|(
literal|"direct://DELETE"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-calendar://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/delete"
argument_list|)
expr_stmt|;
comment|// test route for get
name|from
argument_list|(
literal|"direct://GET"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-calendar://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/get"
argument_list|)
expr_stmt|;
comment|// test route for insert
name|from
argument_list|(
literal|"direct://INSERT"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-calendar://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/insert"
argument_list|)
expr_stmt|;
comment|// test route for instances
name|from
argument_list|(
literal|"direct://INSTANCES"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-calendar://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/instances"
argument_list|)
expr_stmt|;
comment|// test route for list
name|from
argument_list|(
literal|"direct://LIST"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-calendar://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/list?inBody=calendarId"
argument_list|)
expr_stmt|;
comment|// test route for move
name|from
argument_list|(
literal|"direct://MOVE"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-calendar://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/move"
argument_list|)
expr_stmt|;
comment|// test route for patch
name|from
argument_list|(
literal|"direct://PATCH"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-calendar://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/patch"
argument_list|)
expr_stmt|;
comment|// test route for quickAdd
name|from
argument_list|(
literal|"direct://QUICKADD"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-calendar://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/quickAdd"
argument_list|)
expr_stmt|;
comment|// test route for update
name|from
argument_list|(
literal|"direct://UPDATE"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-calendar://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/update"
argument_list|)
expr_stmt|;
comment|// test route for watch
name|from
argument_list|(
literal|"direct://WATCH"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-calendar://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/watch"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

