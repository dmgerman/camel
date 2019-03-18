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
name|Produce
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
name|ProducerTemplate
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
name|servicenow
operator|.
name|model
operator|.
name|Scorecard
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
DECL|class|ServiceNowScorecardTest
specifier|public
class|class
name|ServiceNowScorecardTest
extends|extends
name|ServiceNowTestSupport
block|{
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:servicenow"
argument_list|)
DECL|field|template
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Test
DECL|method|testScorecard ()
specifier|public
name|void
name|testScorecard
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Scorecard
argument_list|>
name|scorecardList
init|=
name|template
operator|.
name|requestBodyAndHeaders
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
name|ServiceNowConstants
operator|.
name|RESOURCE_SCORECARDS
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
name|ServiceNowConstants
operator|.
name|ACTION_SUBJECT
argument_list|,
name|ServiceNowConstants
operator|.
name|ACTION_SUBJECT_PERFORMANCE_ANALYTICS
argument_list|)
operator|.
name|put
argument_list|(
name|ServiceNowConstants
operator|.
name|MODEL
argument_list|,
name|Scorecard
operator|.
name|class
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|scorecardList
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
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

