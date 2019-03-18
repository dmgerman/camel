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
name|List
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
name|FreeBusyRequest
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
name|FreeBusyRequestItem
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
name|CalendarFreebusyApiMethod
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
comment|/**  * The class source won't be generated again if the generator MOJO finds it under src/test/java.  */
end_comment

begin_class
DECL|class|CalendarFreebusyIntegrationTest
specifier|public
class|class
name|CalendarFreebusyIntegrationTest
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
name|CalendarFreebusyIntegrationTest
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
name|CalendarFreebusyApiMethod
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testQuery ()
specifier|public
name|void
name|testQuery
parameter_list|()
throws|throws
name|Exception
block|{
comment|// using com.google.api.services.calendar.model.FreeBusyRequest message
comment|// body for single parameter "content"
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
name|FreeBusyRequest
name|request
init|=
operator|new
name|FreeBusyRequest
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|FreeBusyRequestItem
argument_list|>
name|items
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|items
operator|.
name|add
argument_list|(
operator|new
name|FreeBusyRequestItem
argument_list|()
operator|.
name|setId
argument_list|(
name|getCalendar
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|request
operator|.
name|setItems
argument_list|(
name|items
argument_list|)
expr_stmt|;
name|request
operator|.
name|setTimeMin
argument_list|(
name|DateTime
operator|.
name|parseRfc3339
argument_list|(
literal|"2014-11-10T20:45:30-00:00"
argument_list|)
argument_list|)
expr_stmt|;
name|request
operator|.
name|setTimeMax
argument_list|(
name|DateTime
operator|.
name|parseRfc3339
argument_list|(
literal|"2014-11-10T21:45:30-00:00"
argument_list|)
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
name|FreeBusyResponse
name|result
init|=
name|requestBody
argument_list|(
literal|"direct://QUERY"
argument_list|,
name|request
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"query result"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"query: "
operator|+
name|result
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
comment|// test route for query
name|from
argument_list|(
literal|"direct://QUERY"
argument_list|)
operator|.
name|to
argument_list|(
literal|"google-calendar://"
operator|+
name|PATH_PREFIX
operator|+
literal|"/query?inBody=content"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

