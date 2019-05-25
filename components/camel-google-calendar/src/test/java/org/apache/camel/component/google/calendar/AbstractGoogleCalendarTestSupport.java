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
name|io
operator|.
name|IOException
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
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
name|Calendar
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
name|CamelContext
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
name|CamelExecutionException
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
name|support
operator|.
name|PropertyBindingSupport
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

begin_comment
comment|/**  * Abstract base class for GoogleCalendar Integration tests generated by Camel  * API component maven plugin.  */
end_comment

begin_class
DECL|class|AbstractGoogleCalendarTestSupport
specifier|public
class|class
name|AbstractGoogleCalendarTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEST_OPTIONS_PROPERTIES
specifier|private
specifier|static
specifier|final
name|String
name|TEST_OPTIONS_PROPERTIES
init|=
literal|"/test-options.properties"
decl_stmt|;
DECL|field|calendar
specifier|private
name|Calendar
name|calendar
decl_stmt|;
annotation|@
name|Before
DECL|method|createTestCalendar ()
specifier|public
name|void
name|createTestCalendar
parameter_list|()
block|{
name|Calendar
name|calendar
init|=
operator|new
name|Calendar
argument_list|()
decl_stmt|;
name|Random
name|rand
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|calendar
operator|.
name|setSummary
argument_list|(
literal|"camel-calendar-"
operator|+
name|rand
operator|.
name|nextInt
argument_list|()
argument_list|)
expr_stmt|;
name|calendar
operator|.
name|setTimeZone
argument_list|(
literal|"America/St_Johns"
argument_list|)
expr_stmt|;
name|this
operator|.
name|calendar
operator|=
name|requestBody
argument_list|(
literal|"google-calendar://calendars/insert?inBody=content"
argument_list|,
name|calendar
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|deleteTestCalendar ()
specifier|public
name|void
name|deleteTestCalendar
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|calendar
operator|!=
literal|null
condition|)
block|{
name|requestBody
argument_list|(
literal|"google-calendar://calendars/delete?inBody=calendarId"
argument_list|,
name|getCalendar
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|setCalendar
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{         }
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
comment|// read GoogleCalendar component configuration from
comment|// TEST_OPTIONS_PROPERTIES
specifier|final
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
name|properties
operator|.
name|load
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|TEST_OPTIONS_PROPERTIES
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s could not be loaded: %s"
argument_list|,
name|TEST_OPTIONS_PROPERTIES
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|options
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|GoogleCalendarConfiguration
name|configuration
init|=
operator|new
name|GoogleCalendarConfiguration
argument_list|()
decl_stmt|;
name|PropertyBindingSupport
operator|.
name|bindProperties
argument_list|(
name|context
argument_list|,
name|configuration
argument_list|,
name|options
argument_list|)
expr_stmt|;
comment|// add GoogleCalendarComponent to Camel context
specifier|final
name|GoogleCalendarComponent
name|component
init|=
operator|new
name|GoogleCalendarComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"google-calendar"
argument_list|,
name|component
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
comment|// only create the context once for this class
return|return
literal|true
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|requestBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBodyAndHeaders
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|CamelExecutionException
block|{
return|return
operator|(
name|T
operator|)
name|template
argument_list|()
operator|.
name|requestBodyAndHeaders
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|,
name|headers
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|requestBody (String endpoint, Object body)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|requestBody
parameter_list|(
name|String
name|endpoint
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|CamelExecutionException
block|{
return|return
operator|(
name|T
operator|)
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
name|endpoint
argument_list|,
name|body
argument_list|)
return|;
block|}
DECL|method|getCalendar ()
specifier|public
name|Calendar
name|getCalendar
parameter_list|()
block|{
if|if
condition|(
name|calendar
operator|==
literal|null
condition|)
block|{
name|createTestCalendar
argument_list|()
expr_stmt|;
block|}
return|return
name|calendar
return|;
block|}
DECL|method|setCalendar (Calendar calendar)
specifier|public
name|void
name|setCalendar
parameter_list|(
name|Calendar
name|calendar
parameter_list|)
block|{
name|this
operator|.
name|calendar
operator|=
name|calendar
expr_stmt|;
block|}
block|}
end_class

end_unit

