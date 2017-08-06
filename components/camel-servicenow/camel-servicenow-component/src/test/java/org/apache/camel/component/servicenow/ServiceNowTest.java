begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalDate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalDateTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|temporal
operator|.
name|ChronoUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|JsonNode
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
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
name|component
operator|.
name|servicenow
operator|.
name|model
operator|.
name|Incident
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

begin_class
DECL|class|ServiceNowTest
specifier|public
class|class
name|ServiceNowTest
extends|extends
name|ServiceNowTestSupport
block|{
annotation|@
name|Test
DECL|method|testExceptions ()
specifier|public
name|void
name|testExceptions
parameter_list|()
throws|throws
name|Exception
block|{
comment|// 404
try|try
block|{
name|template
argument_list|()
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:servicenow"
argument_list|,
literal|null
argument_list|,
name|kvBuilder
argument_list|()
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|RESOURCE
argument_list|,
literal|"table"
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|ACTION
argument_list|,
name|ServiceNowConstants
operator|.
name|ACTION_RETRIEVE
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_QUERY
argument_list|,
literal|"number="
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_TABLE_NAME
argument_list|,
literal|"incident"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ServiceNowException
argument_list|)
expr_stmt|;
name|ServiceNowException
name|sne
init|=
operator|(
name|ServiceNowException
operator|)
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"failure"
argument_list|,
name|sne
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sne
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"No Record found"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sne
operator|.
name|getDetail
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Records matching query not found"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// 400
try|try
block|{
name|template
argument_list|()
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:servicenow"
argument_list|,
literal|null
argument_list|,
name|kvBuilder
argument_list|()
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|RESOURCE
argument_list|,
literal|"table"
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|ACTION
argument_list|,
name|ServiceNowConstants
operator|.
name|ACTION_RETRIEVE
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_QUERY
argument_list|,
literal|"number="
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_TABLE_NAME
argument_list|,
literal|"notExistingTable"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ServiceNowException
argument_list|)
expr_stmt|;
name|ServiceNowException
name|sne
init|=
operator|(
name|ServiceNowException
operator|)
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"failure"
argument_list|,
name|sne
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sne
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Invalid table notExistingTable"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|sne
operator|.
name|getDetail
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testBodyMismatch ()
specifier|public
name|void
name|testBodyMismatch
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
argument_list|()
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:servicenow"
argument_list|,
literal|"NotAnIncidentObject"
argument_list|,
name|kvBuilder
argument_list|()
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|RESOURCE
argument_list|,
literal|"table"
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|ACTION
argument_list|,
name|ServiceNowConstants
operator|.
name|ACTION_CREATE
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_TABLE_NAME
argument_list|,
literal|"incident"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should fail as body is not compatible with model defined in route for table incident"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalArgumentException
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testRequestResponseModels ()
specifier|public
name|void
name|testRequestResponseModels
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:servicenow"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Incident
name|incident
init|=
operator|new
name|Incident
argument_list|()
decl_stmt|;
name|incident
operator|.
name|setDescription
argument_list|(
literal|"my incident"
argument_list|)
expr_stmt|;
name|incident
operator|.
name|setShortDescription
argument_list|(
literal|"An incident"
argument_list|)
expr_stmt|;
name|incident
operator|.
name|setSeverity
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|incident
operator|.
name|setImpact
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
argument_list|()
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:servicenow"
argument_list|,
name|incident
argument_list|,
name|kvBuilder
argument_list|()
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|RESOURCE
argument_list|,
name|ServiceNowConstants
operator|.
name|RESOURCE_TABLE
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|ACTION
argument_list|,
name|ServiceNowConstants
operator|.
name|ACTION_CREATE
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|REQUEST_MODEL
argument_list|,
name|Incident
operator|.
name|class
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|RESPONSE_MODEL
argument_list|,
name|JsonNode
operator|.
name|class
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_TABLE_NAME
argument_list|,
literal|"incident"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Object
name|body
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|body
operator|instanceof
name|JsonNode
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testVersionedApiRequest ()
specifier|public
name|void
name|testVersionedApiRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:servicenow"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Incident
name|incident
init|=
operator|new
name|Incident
argument_list|()
decl_stmt|;
name|incident
operator|.
name|setDescription
argument_list|(
literal|"my incident"
argument_list|)
expr_stmt|;
name|incident
operator|.
name|setShortDescription
argument_list|(
literal|"An incident"
argument_list|)
expr_stmt|;
name|incident
operator|.
name|setSeverity
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|incident
operator|.
name|setImpact
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
argument_list|()
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:servicenow"
argument_list|,
name|incident
argument_list|,
name|kvBuilder
argument_list|()
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|RESOURCE
argument_list|,
name|ServiceNowConstants
operator|.
name|RESOURCE_TABLE
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|API_VERSION
argument_list|,
literal|"v1"
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|ACTION
argument_list|,
name|ServiceNowConstants
operator|.
name|ACTION_CREATE
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|REQUEST_MODEL
argument_list|,
name|Incident
operator|.
name|class
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|RESPONSE_MODEL
argument_list|,
name|JsonNode
operator|.
name|class
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_TABLE_NAME
argument_list|,
literal|"incident"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Object
name|body
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|body
operator|instanceof
name|JsonNode
argument_list|)
expr_stmt|;
block|}
comment|// *********************************
comment|// Date/Time
comment|// *********************************
annotation|@
name|Test
DECL|method|testDateTimeWithDefaults ()
specifier|public
name|void
name|testDateTimeWithDefaults
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|ServiceNowConfiguration
name|configuration
init|=
operator|new
name|ServiceNowConfiguration
argument_list|()
decl_stmt|;
name|ObjectMapper
name|mapper
init|=
name|configuration
operator|.
name|getOrCreateMapper
argument_list|()
decl_stmt|;
name|DateTimeBean
name|bean
init|=
operator|new
name|DateTimeBean
argument_list|()
decl_stmt|;
name|String
name|serialized
init|=
name|mapper
operator|.
name|writeValueAsString
argument_list|(
name|bean
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
name|serialized
argument_list|)
expr_stmt|;
name|DateTimeBean
name|deserialized
init|=
name|mapper
operator|.
name|readValue
argument_list|(
name|serialized
argument_list|,
name|DateTimeBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|bean
operator|.
name|dateTime
argument_list|,
name|deserialized
operator|.
name|dateTime
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|bean
operator|.
name|date
argument_list|,
name|deserialized
operator|.
name|date
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|bean
operator|.
name|time
argument_list|,
name|deserialized
operator|.
name|time
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDateTimeWithCustomFormats ()
specifier|public
name|void
name|testDateTimeWithCustomFormats
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|ServiceNowConfiguration
name|configuration
init|=
operator|new
name|ServiceNowConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setDateFormat
argument_list|(
literal|"yyyyMMdd"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setTimeFormat
argument_list|(
literal|"HHmmss"
argument_list|)
expr_stmt|;
name|ObjectMapper
name|mapper
init|=
name|configuration
operator|.
name|getOrCreateMapper
argument_list|()
decl_stmt|;
name|DateTimeBean
name|bean
init|=
operator|new
name|DateTimeBean
argument_list|()
decl_stmt|;
name|String
name|serialized
init|=
name|mapper
operator|.
name|writeValueAsString
argument_list|(
name|bean
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
name|serialized
argument_list|)
expr_stmt|;
name|DateTimeBean
name|deserialized
init|=
name|mapper
operator|.
name|readValue
argument_list|(
name|serialized
argument_list|,
name|DateTimeBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|bean
operator|.
name|dateTime
argument_list|,
name|deserialized
operator|.
name|dateTime
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|bean
operator|.
name|date
argument_list|,
name|deserialized
operator|.
name|date
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|bean
operator|.
name|time
argument_list|,
name|deserialized
operator|.
name|time
argument_list|)
expr_stmt|;
block|}
DECL|class|DateTimeBean
specifier|public
specifier|static
class|class
name|DateTimeBean
block|{
DECL|field|dateTime
name|LocalDateTime
name|dateTime
decl_stmt|;
DECL|field|date
name|LocalDate
name|date
decl_stmt|;
DECL|field|time
name|LocalTime
name|time
decl_stmt|;
DECL|method|DateTimeBean ()
specifier|public
name|DateTimeBean
parameter_list|()
block|{
name|dateTime
operator|=
name|LocalDateTime
operator|.
name|now
argument_list|()
operator|.
name|truncatedTo
argument_list|(
name|ChronoUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|date
operator|=
name|dateTime
operator|.
name|toLocalDate
argument_list|()
expr_stmt|;
name|time
operator|=
name|dateTime
operator|.
name|toLocalTime
argument_list|()
expr_stmt|;
block|}
DECL|method|getDateTime ()
specifier|public
name|LocalDateTime
name|getDateTime
parameter_list|()
block|{
return|return
name|dateTime
return|;
block|}
DECL|method|setDateTime (LocalDateTime dateTime)
specifier|public
name|void
name|setDateTime
parameter_list|(
name|LocalDateTime
name|dateTime
parameter_list|)
block|{
name|this
operator|.
name|dateTime
operator|=
name|dateTime
expr_stmt|;
block|}
DECL|method|getDate ()
specifier|public
name|LocalDate
name|getDate
parameter_list|()
block|{
return|return
name|date
return|;
block|}
DECL|method|setDate (LocalDate date)
specifier|public
name|void
name|setDate
parameter_list|(
name|LocalDate
name|date
parameter_list|)
block|{
name|this
operator|.
name|date
operator|=
name|date
expr_stmt|;
block|}
DECL|method|getTime ()
specifier|public
name|LocalTime
name|getTime
parameter_list|()
block|{
return|return
name|time
return|;
block|}
DECL|method|setTime (LocalTime time)
specifier|public
name|void
name|setTime
parameter_list|(
name|LocalTime
name|time
parameter_list|)
block|{
name|this
operator|.
name|time
operator|=
name|time
expr_stmt|;
block|}
block|}
comment|// *************************************************************************
comment|//
comment|// *************************************************************************
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
literal|"direct:servicenow"
argument_list|)
operator|.
name|to
argument_list|(
literal|"servicenow:{{env:SERVICENOW_INSTANCE}}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:org.apache.camel.component.servicenow?level=INFO&showAll=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:servicenow"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

