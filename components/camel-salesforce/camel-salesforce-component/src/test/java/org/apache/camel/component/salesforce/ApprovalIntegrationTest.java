begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|junittoolbox
operator|.
name|ParallelParameterized
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|approval
operator|.
name|ApprovalRequest
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|approval
operator|.
name|ApprovalRequest
operator|.
name|Action
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|approval
operator|.
name|ApprovalResult
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|approval
operator|.
name|Approvals
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|approval
operator|.
name|Approvals
operator|.
name|Info
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|ParallelParameterized
operator|.
name|class
argument_list|)
DECL|class|ApprovalIntegrationTest
specifier|public
class|class
name|ApprovalIntegrationTest
extends|extends
name|AbstractApprovalIntegrationTest
block|{
DECL|field|format
specifier|private
specifier|final
name|String
name|format
decl_stmt|;
DECL|method|ApprovalIntegrationTest (final String format)
specifier|public
name|ApprovalIntegrationTest
parameter_list|(
specifier|final
name|String
name|format
parameter_list|)
block|{
name|super
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSubmitAndFetchApprovals ()
specifier|public
name|void
name|shouldSubmitAndFetchApprovals
parameter_list|()
block|{
specifier|final
name|ApprovalResult
name|approvalResult
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"salesforce:approval?"
comment|//
operator|+
literal|"format=%s"
comment|//
operator|+
literal|"&approvalActionType=Submit"
comment|//
operator|+
literal|"&approvalContextId=%s"
comment|//
operator|+
literal|"&approvalNextApproverIds=%s"
comment|//
operator|+
literal|"&approvalComments=Integration test"
comment|//
operator|+
literal|"&approvalProcessDefinitionNameOrId=Test_Account_Process"
argument_list|,
name|format
argument_list|,
name|accountIds
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|userId
argument_list|)
argument_list|,
name|NOT_USED
argument_list|,
name|ApprovalResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Approval should have resulted in value"
argument_list|,
name|approvalResult
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"There should be one Account waiting approval"
argument_list|,
literal|1
argument_list|,
name|approvalResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Instance status of the item in approval result should be `Pending`"
argument_list|,
literal|"Pending"
argument_list|,
name|approvalResult
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getInstanceStatus
argument_list|()
argument_list|)
expr_stmt|;
comment|// as it stands on 18.11.2016. the GET method on /vXX.X/process/approvals/ with Accept other than
comment|// `application/json` results in HTTP status 500, so only JSON is supported
specifier|final
name|Approvals
name|approvals
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"salesforce:approvals"
argument_list|,
name|NOT_USED
argument_list|,
name|Approvals
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Approvals should be fetched"
argument_list|,
name|approvals
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Info
argument_list|>
name|accountApprovals
init|=
name|approvals
operator|.
name|approvalsFor
argument_list|(
literal|"Account"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"There should be one Account waiting approval"
argument_list|,
literal|1
argument_list|,
name|accountApprovals
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSubmitBulkApprovals ()
specifier|public
name|void
name|shouldSubmitBulkApprovals
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|ApprovalRequest
argument_list|>
name|approvalRequests
init|=
name|accountIds
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|id
lambda|->
block|{
specifier|final
name|ApprovalRequest
name|request
init|=
operator|new
name|ApprovalRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setContextId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|request
operator|.
name|setComments
argument_list|(
literal|"Approval for "
operator|+
name|id
argument_list|)
expr_stmt|;
name|request
operator|.
name|setActionType
argument_list|(
name|Action
operator|.
name|Submit
argument_list|)
expr_stmt|;
return|return
name|request
return|;
block|}
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|ApprovalResult
name|approvalResult
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"salesforce:approval?"
comment|//
operator|+
literal|"format=%s"
comment|//
operator|+
literal|"&approvalActionType=Submit"
comment|//
operator|+
literal|"&approvalNextApproverIds=%s"
comment|//
operator|+
literal|"&approvalProcessDefinitionNameOrId=Test_Account_Process"
argument_list|,
name|format
argument_list|,
name|userId
argument_list|)
argument_list|,
name|approvalRequests
argument_list|,
name|ApprovalResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should have same number of approval results as requests"
argument_list|,
name|approvalRequests
operator|.
name|size
argument_list|()
argument_list|,
name|approvalResult
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Parameters
argument_list|(
name|name
operator|=
literal|"format = {0}"
argument_list|)
DECL|method|formats ()
specifier|public
specifier|static
name|Iterable
argument_list|<
name|String
argument_list|>
name|formats
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
literal|"JSON"
argument_list|,
literal|"XML"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

