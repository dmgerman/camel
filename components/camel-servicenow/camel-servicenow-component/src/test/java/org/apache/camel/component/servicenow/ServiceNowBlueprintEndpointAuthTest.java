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
name|test
operator|.
name|blueprint
operator|.
name|CamelBlueprintTestSupport
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
DECL|class|ServiceNowBlueprintEndpointAuthTest
specifier|public
class|class
name|ServiceNowBlueprintEndpointAuthTest
extends|extends
name|CamelBlueprintTestSupport
block|{
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"OSGI-INF/blueprint/blueprint-endpoint-auth.xml"
return|;
block|}
annotation|@
name|Test
DECL|method|testAuth ()
specifier|public
name|void
name|testAuth
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
name|ServiceNowTestSupport
operator|.
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
block|}
end_class

end_unit

