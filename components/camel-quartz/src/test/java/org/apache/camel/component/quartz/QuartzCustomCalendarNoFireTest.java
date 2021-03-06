begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quartz
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quartz
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|BindToRegistry
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|Scheduler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|impl
operator|.
name|calendar
operator|.
name|HolidayCalendar
import|;
end_import

begin_comment
comment|/**  * This test a timer endpoint in a route with Custom calendar.  */
end_comment

begin_class
DECL|class|QuartzCustomCalendarNoFireTest
specifier|public
class|class
name|QuartzCustomCalendarNoFireTest
extends|extends
name|BaseQuartzTest
block|{
annotation|@
name|Test
DECL|method|testQuartzCustomCronRouteNoFire ()
specifier|public
name|void
name|testQuartzCustomCronRouteNoFire
parameter_list|()
throws|throws
name|Exception
block|{
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
name|expectedMinimumMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|QuartzComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"quartz"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|Scheduler
name|scheduler
init|=
name|component
operator|.
name|getScheduler
argument_list|()
decl_stmt|;
name|Calendar
name|c
init|=
name|scheduler
operator|.
name|getCalendar
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_CAMEL_CUSTOM_CALENDAR
argument_list|)
decl_stmt|;
name|Date
name|now
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|java
operator|.
name|util
operator|.
name|Calendar
name|tomorrow
init|=
name|java
operator|.
name|util
operator|.
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|tomorrow
operator|.
name|setTime
argument_list|(
name|now
argument_list|)
expr_stmt|;
name|tomorrow
operator|.
name|add
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
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|c
operator|.
name|isTimeIncluded
argument_list|(
name|tomorrow
operator|.
name|getTimeInMillis
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|c
operator|.
name|isTimeIncluded
argument_list|(
name|now
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|BindToRegistry
argument_list|(
literal|"calendar"
argument_list|)
DECL|method|loadCalendar ()
specifier|public
name|HolidayCalendar
name|loadCalendar
parameter_list|()
throws|throws
name|Exception
block|{
name|HolidayCalendar
name|cal
init|=
operator|new
name|HolidayCalendar
argument_list|()
decl_stmt|;
name|cal
operator|.
name|addExcludedDate
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|cal
return|;
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
literal|"quartz://MyTimer?customCalendar=#calendar&cron=05+00+00+*+*+?"
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

