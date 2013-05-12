begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ical
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ical
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|DateTime
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|PropertyList
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|TimeZone
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|TimeZoneRegistry
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|TimeZoneRegistryFactory
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|component
operator|.
name|VEvent
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|component
operator|.
name|VTimeZone
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|parameter
operator|.
name|Cn
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|parameter
operator|.
name|Role
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|property
operator|.
name|Attendee
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|property
operator|.
name|CalScale
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|property
operator|.
name|DtEnd
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|property
operator|.
name|DtStamp
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|property
operator|.
name|DtStart
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|property
operator|.
name|ProdId
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|property
operator|.
name|Summary
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|property
operator|.
name|Uid
import|;
end_import

begin_import
import|import
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|property
operator|.
name|Version
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
name|converter
operator|.
name|IOConverter
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
name|After
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

begin_comment
comment|/**  * Small unit test which verifies ical data format.  */
end_comment

begin_class
DECL|class|ICalDataFormatTest
specifier|public
class|class
name|ICalDataFormatTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|defaultTimeZone
specifier|private
name|java
operator|.
name|util
operator|.
name|TimeZone
name|defaultTimeZone
decl_stmt|;
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
name|defaultTimeZone
operator|=
name|java
operator|.
name|util
operator|.
name|TimeZone
operator|.
name|getDefault
argument_list|()
expr_stmt|;
name|java
operator|.
name|util
operator|.
name|TimeZone
operator|.
name|setDefault
argument_list|(
name|java
operator|.
name|util
operator|.
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"America/New_York"
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|java
operator|.
name|util
operator|.
name|TimeZone
operator|.
name|setDefault
argument_list|(
name|defaultTimeZone
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshal ()
specifier|public
name|void
name|testUnmarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|stream
init|=
name|IOConverter
operator|.
name|toInputStream
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/data.ics"
argument_list|)
argument_list|)
decl_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|createTestCalendar
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|stream
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshal ()
specifier|public
name|void
name|testMarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|Calendar
name|testCalendar
init|=
name|createTestCalendar
argument_list|()
decl_stmt|;
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|testCalendar
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|testCalendar
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
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
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:unmarshal"
argument_list|)
operator|.
name|unmarshal
argument_list|(
literal|"ical"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:marshal"
argument_list|)
operator|.
name|marshal
argument_list|(
literal|"ical"
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
comment|/**      * Creates test calendar instance.      *       * @return ICal calendar object.      */
DECL|method|createTestCalendar ()
specifier|protected
name|Calendar
name|createTestCalendar
parameter_list|()
throws|throws
name|ParseException
block|{
comment|// Create a TimeZone
name|TimeZoneRegistry
name|registry
init|=
name|TimeZoneRegistryFactory
operator|.
name|getInstance
argument_list|()
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|TimeZone
name|timezone
init|=
name|registry
operator|.
name|getTimeZone
argument_list|(
literal|"America/New_York"
argument_list|)
decl_stmt|;
name|VTimeZone
name|tz
init|=
name|timezone
operator|.
name|getVTimeZone
argument_list|()
decl_stmt|;
comment|// Start Date is on: April 1, 2013, 9:00 am
name|java
operator|.
name|util
operator|.
name|Calendar
name|startDate
init|=
operator|new
name|GregorianCalendar
argument_list|()
decl_stmt|;
name|startDate
operator|.
name|setTimeZone
argument_list|(
name|timezone
argument_list|)
expr_stmt|;
name|startDate
operator|.
name|set
argument_list|(
name|java
operator|.
name|util
operator|.
name|Calendar
operator|.
name|MONTH
argument_list|,
name|java
operator|.
name|util
operator|.
name|Calendar
operator|.
name|APRIL
argument_list|)
expr_stmt|;
name|startDate
operator|.
name|set
argument_list|(
name|java
operator|.
name|util
operator|.
name|Calendar
operator|.
name|DAY_OF_MONTH
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|startDate
operator|.
name|set
argument_list|(
name|java
operator|.
name|util
operator|.
name|Calendar
operator|.
name|YEAR
argument_list|,
literal|2013
argument_list|)
expr_stmt|;
name|startDate
operator|.
name|set
argument_list|(
name|java
operator|.
name|util
operator|.
name|Calendar
operator|.
name|HOUR_OF_DAY
argument_list|,
literal|17
argument_list|)
expr_stmt|;
name|startDate
operator|.
name|set
argument_list|(
name|java
operator|.
name|util
operator|.
name|Calendar
operator|.
name|MINUTE
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|startDate
operator|.
name|set
argument_list|(
name|java
operator|.
name|util
operator|.
name|Calendar
operator|.
name|SECOND
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// End Date is on: April 1, 2013, 13:00
name|java
operator|.
name|util
operator|.
name|Calendar
name|endDate
init|=
operator|new
name|GregorianCalendar
argument_list|()
decl_stmt|;
name|endDate
operator|.
name|setTimeZone
argument_list|(
name|timezone
argument_list|)
expr_stmt|;
name|endDate
operator|.
name|set
argument_list|(
name|java
operator|.
name|util
operator|.
name|Calendar
operator|.
name|MONTH
argument_list|,
name|java
operator|.
name|util
operator|.
name|Calendar
operator|.
name|APRIL
argument_list|)
expr_stmt|;
name|endDate
operator|.
name|set
argument_list|(
name|java
operator|.
name|util
operator|.
name|Calendar
operator|.
name|DAY_OF_MONTH
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|endDate
operator|.
name|set
argument_list|(
name|java
operator|.
name|util
operator|.
name|Calendar
operator|.
name|YEAR
argument_list|,
literal|2013
argument_list|)
expr_stmt|;
name|endDate
operator|.
name|set
argument_list|(
name|java
operator|.
name|util
operator|.
name|Calendar
operator|.
name|HOUR_OF_DAY
argument_list|,
literal|21
argument_list|)
expr_stmt|;
name|endDate
operator|.
name|set
argument_list|(
name|java
operator|.
name|util
operator|.
name|Calendar
operator|.
name|MINUTE
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|endDate
operator|.
name|set
argument_list|(
name|java
operator|.
name|util
operator|.
name|Calendar
operator|.
name|SECOND
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// Create the event
name|PropertyList
name|propertyList
init|=
operator|new
name|PropertyList
argument_list|()
decl_stmt|;
name|propertyList
operator|.
name|add
argument_list|(
operator|new
name|DtStamp
argument_list|(
literal|"20130324T180000Z"
argument_list|)
argument_list|)
expr_stmt|;
name|propertyList
operator|.
name|add
argument_list|(
operator|new
name|DtStart
argument_list|(
operator|new
name|DateTime
argument_list|(
name|startDate
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|propertyList
operator|.
name|add
argument_list|(
operator|new
name|DtEnd
argument_list|(
operator|new
name|DateTime
argument_list|(
name|endDate
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|propertyList
operator|.
name|add
argument_list|(
operator|new
name|Summary
argument_list|(
literal|"Progress Meeting"
argument_list|)
argument_list|)
expr_stmt|;
name|VEvent
name|meeting
init|=
operator|new
name|VEvent
argument_list|(
name|propertyList
argument_list|)
decl_stmt|;
comment|// add timezone info..
name|meeting
operator|.
name|getProperties
argument_list|()
operator|.
name|add
argument_list|(
name|tz
operator|.
name|getTimeZoneId
argument_list|()
argument_list|)
expr_stmt|;
comment|// generate unique identifier..
name|meeting
operator|.
name|getProperties
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|Uid
argument_list|(
literal|"00000000"
argument_list|)
argument_list|)
expr_stmt|;
comment|// add attendees..
name|Attendee
name|dev1
init|=
operator|new
name|Attendee
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"mailto:dev1@mycompany.com"
argument_list|)
argument_list|)
decl_stmt|;
name|dev1
operator|.
name|getParameters
argument_list|()
operator|.
name|add
argument_list|(
name|Role
operator|.
name|REQ_PARTICIPANT
argument_list|)
expr_stmt|;
name|dev1
operator|.
name|getParameters
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|Cn
argument_list|(
literal|"Developer 1"
argument_list|)
argument_list|)
expr_stmt|;
name|meeting
operator|.
name|getProperties
argument_list|()
operator|.
name|add
argument_list|(
name|dev1
argument_list|)
expr_stmt|;
name|Attendee
name|dev2
init|=
operator|new
name|Attendee
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"mailto:dev2@mycompany.com"
argument_list|)
argument_list|)
decl_stmt|;
name|dev2
operator|.
name|getParameters
argument_list|()
operator|.
name|add
argument_list|(
name|Role
operator|.
name|OPT_PARTICIPANT
argument_list|)
expr_stmt|;
name|dev2
operator|.
name|getParameters
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|Cn
argument_list|(
literal|"Developer 2"
argument_list|)
argument_list|)
expr_stmt|;
name|meeting
operator|.
name|getProperties
argument_list|()
operator|.
name|add
argument_list|(
name|dev2
argument_list|)
expr_stmt|;
comment|// Create a calendar
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|Calendar
name|icsCalendar
init|=
operator|new
name|net
operator|.
name|fortuna
operator|.
name|ical4j
operator|.
name|model
operator|.
name|Calendar
argument_list|()
decl_stmt|;
name|icsCalendar
operator|.
name|getProperties
argument_list|()
operator|.
name|add
argument_list|(
name|Version
operator|.
name|VERSION_2_0
argument_list|)
expr_stmt|;
name|icsCalendar
operator|.
name|getProperties
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|ProdId
argument_list|(
literal|"-//Events Calendar//iCal4j 1.0//EN"
argument_list|)
argument_list|)
expr_stmt|;
name|icsCalendar
operator|.
name|getProperties
argument_list|()
operator|.
name|add
argument_list|(
name|CalScale
operator|.
name|GREGORIAN
argument_list|)
expr_stmt|;
comment|// Add the event and print
name|icsCalendar
operator|.
name|getComponents
argument_list|()
operator|.
name|add
argument_list|(
name|meeting
argument_list|)
expr_stmt|;
return|return
name|icsCalendar
return|;
block|}
block|}
end_class

end_unit

