begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|List
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
name|Exchange
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
name|IncidentWithParms
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
DECL|class|ServiceNowTableTest
specifier|public
class|class
name|ServiceNowTableTest
extends|extends
name|ServiceNowTestSupport
block|{
annotation|@
name|Test
DECL|method|testRetrieveSome ()
specifier|public
name|void
name|testRetrieveSome
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
name|expectedMessageCount
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
name|SYSPARM_LIMIT
argument_list|,
literal|10
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
name|Exchange
name|exchange
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
decl_stmt|;
name|List
argument_list|<
name|Incident
argument_list|>
name|items
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|items
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|items
operator|.
name|size
argument_list|()
operator|<=
literal|10
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|RESPONSE_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|OFFSET_FIRST
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|OFFSET_NEXT
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|OFFSET_LAST
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRetrieveSomeWithParams ()
specifier|public
name|void
name|testRetrieveSomeWithParams
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
name|expectedMessageCount
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
name|SYSPARM_LIMIT
argument_list|,
literal|10
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_EXCLUDE_REFERENCE_LINK
argument_list|,
literal|false
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
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|MODEL
argument_list|,
name|IncidentWithParms
operator|.
name|class
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
name|Exchange
name|exchange
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
decl_stmt|;
name|List
argument_list|<
name|Incident
argument_list|>
name|items
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|items
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|items
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|items
operator|.
name|size
argument_list|()
operator|<=
literal|10
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|RESPONSE_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|OFFSET_FIRST
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|OFFSET_NEXT
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ServiceNowConstants
operator|.
name|OFFSET_LAST
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRetrieveSomeWithDefaults ()
specifier|public
name|void
name|testRetrieveSomeWithDefaults
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:servicenow-defaults"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
argument_list|()
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:servicenow-defaults"
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
name|SYSPARM_LIMIT
argument_list|,
literal|10
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
name|Exchange
name|exchange
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
decl_stmt|;
name|List
argument_list|<
name|Incident
argument_list|>
name|items
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|items
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|items
operator|.
name|size
argument_list|()
operator|<=
literal|10
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIncidentWorkflow ()
specifier|public
name|void
name|testIncidentWorkflow
parameter_list|()
throws|throws
name|Exception
block|{
name|Incident
name|incident
init|=
literal|null
decl_stmt|;
name|String
name|sysId
decl_stmt|;
name|String
name|number
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:servicenow"
argument_list|)
decl_stmt|;
comment|// ************************
comment|// Create incident
comment|// ************************
block|{
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
name|incident
operator|=
operator|new
name|Incident
argument_list|()
expr_stmt|;
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
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|incident
operator|=
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
argument_list|(
name|Incident
operator|.
name|class
argument_list|)
expr_stmt|;
name|sysId
operator|=
name|incident
operator|.
name|getId
argument_list|()
expr_stmt|;
name|number
operator|=
name|incident
operator|.
name|getNumber
argument_list|()
expr_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"****************************************************"
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|" Incident created"
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"  sysid  = {}"
argument_list|,
name|sysId
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"  number = {}"
argument_list|,
name|number
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"****************************************************"
argument_list|)
expr_stmt|;
block|}
comment|// ************************
comment|// Search for the incident
comment|// ************************
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Search the record {}"
argument_list|,
name|sysId
argument_list|)
expr_stmt|;
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
name|PARAM_TABLE_NAME
argument_list|,
literal|"incident"
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
name|number
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
name|List
argument_list|<
name|Incident
argument_list|>
name|incidents
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
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|incidents
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|number
argument_list|,
name|incidents
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sysId
argument_list|,
name|incidents
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// ************************
comment|// Modify the incident
comment|// ************************
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Update the record {}"
argument_list|,
name|sysId
argument_list|)
expr_stmt|;
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
name|incident
operator|=
operator|new
name|Incident
argument_list|()
expr_stmt|;
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
literal|"The incident"
argument_list|)
expr_stmt|;
name|incident
operator|.
name|setSeverity
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|incident
operator|.
name|setImpact
argument_list|(
literal|3
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
name|ACTION_MODIFY
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
name|put
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_SYS_ID
argument_list|,
name|sysId
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
name|incident
operator|=
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
argument_list|(
name|Incident
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|number
argument_list|,
name|incident
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|incident
operator|.
name|getSeverity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|incident
operator|.
name|getImpact
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The incident"
argument_list|,
name|incident
operator|.
name|getShortDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// ************************
comment|// Retrieve it via query
comment|// ************************
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Retrieve the record {}"
argument_list|,
name|sysId
argument_list|)
expr_stmt|;
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
name|PARAM_TABLE_NAME
argument_list|,
literal|"incident"
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
name|number
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
name|List
argument_list|<
name|Incident
argument_list|>
name|incidents
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
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|incidents
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|number
argument_list|,
name|incidents
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sysId
argument_list|,
name|incidents
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|incidents
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getSeverity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|incidents
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getImpact
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The incident"
argument_list|,
name|incidents
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getShortDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// ************************
comment|// Retrieve by sys id
comment|// ************************
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Search the record {}"
argument_list|,
name|sysId
argument_list|)
expr_stmt|;
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
name|PARAM_TABLE_NAME
argument_list|,
literal|"incident"
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_SYS_ID
argument_list|,
name|sysId
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
name|incident
operator|=
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
argument_list|(
name|Incident
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|incident
operator|.
name|getSeverity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|incident
operator|.
name|getImpact
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The incident"
argument_list|,
name|incident
operator|.
name|getShortDescription
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|number
argument_list|,
name|incident
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// ************************
comment|// Delete it
comment|// ************************
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Delete the record {}"
argument_list|,
name|sysId
argument_list|)
expr_stmt|;
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
name|ACTION_DELETE
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
name|put
argument_list|(
name|ServiceNowParams
operator|.
name|PARAM_SYS_ID
argument_list|,
name|sysId
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
block|}
comment|// ************************
comment|// Retrieve by id, should fail
comment|// ************************
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Find the record {}, should fail"
argument_list|,
name|sysId
argument_list|)
expr_stmt|;
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
name|PARAM_SYS_ID
argument_list|,
name|sysId
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
literal|"Record "
operator|+
name|number
operator|+
literal|" should have been deleted"
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
comment|// we are good
block|}
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
operator|+
literal|"?model.incident=org.apache.camel.component.servicenow.model.Incident"
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
name|from
argument_list|(
literal|"direct:servicenow-defaults"
argument_list|)
operator|.
name|to
argument_list|(
literal|"servicenow:{{env:SERVICENOW_INSTANCE}}"
operator|+
literal|"?model.incident=org.apache.camel.component.servicenow.model.Incident"
operator|+
literal|"&resource=table"
operator|+
literal|"&table=incident"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:org.apache.camel.component.servicenow?level=INFO&showAll=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:servicenow-defaults"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

