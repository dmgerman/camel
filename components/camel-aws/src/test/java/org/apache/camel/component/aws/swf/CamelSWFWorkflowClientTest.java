begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.swf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|swf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|List
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
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|AmazonSimpleWorkflowClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|DynamicWorkflowClientExternal
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|DynamicWorkflowClientExternalImpl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|model
operator|.
name|DescribeWorkflowExecutionRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|model
operator|.
name|WorkflowExecution
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|model
operator|.
name|WorkflowExecutionDetail
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleworkflow
operator|.
name|model
operator|.
name|WorkflowExecutionInfo
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

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|MatcherAssert
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
DECL|class|CamelSWFWorkflowClientTest
specifier|public
class|class
name|CamelSWFWorkflowClientTest
block|{
DECL|field|configuration
specifier|private
name|SWFConfiguration
name|configuration
decl_stmt|;
DECL|field|swClient
specifier|private
name|AmazonSimpleWorkflowClient
name|swClient
decl_stmt|;
DECL|field|endpoint
specifier|private
name|SWFEndpoint
name|endpoint
decl_stmt|;
DECL|field|camelSWFWorkflowClient
specifier|private
name|CamelSWFWorkflowClient
name|camelSWFWorkflowClient
decl_stmt|;
DECL|field|clientExternal
specifier|private
name|DynamicWorkflowClientExternal
name|clientExternal
decl_stmt|;
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
name|configuration
operator|=
operator|new
name|SWFConfiguration
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|setDomainName
argument_list|(
literal|"testDomain"
argument_list|)
expr_stmt|;
name|swClient
operator|=
name|mock
argument_list|(
name|AmazonSimpleWorkflowClient
operator|.
name|class
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setAmazonSWClient
argument_list|(
name|swClient
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setStartWorkflowOptionsParameters
argument_list|(
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|Object
operator|>
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|SWFEndpoint
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|clientExternal
operator|=
name|mock
argument_list|(
name|DynamicWorkflowClientExternalImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|camelSWFWorkflowClient
operator|=
operator|new
name|CamelSWFWorkflowClient
argument_list|(
name|endpoint
argument_list|,
name|configuration
argument_list|)
block|{
annotation|@
name|Override
name|DynamicWorkflowClientExternal
name|getDynamicWorkflowClient
parameter_list|(
name|String
name|workflowId
parameter_list|,
name|String
name|runId
parameter_list|)
block|{
return|return
name|clientExternal
return|;
block|}
block|}
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDescribeWorkflowInstance ()
specifier|public
name|void
name|testDescribeWorkflowInstance
parameter_list|()
throws|throws
name|Exception
block|{
name|WorkflowExecutionInfo
name|executionInfo
init|=
operator|new
name|WorkflowExecutionInfo
argument_list|()
decl_stmt|;
name|executionInfo
operator|.
name|setCloseStatus
argument_list|(
literal|"COMPLETED"
argument_list|)
expr_stmt|;
name|Date
name|closeTimestamp
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|executionInfo
operator|.
name|setCloseTimestamp
argument_list|(
name|closeTimestamp
argument_list|)
expr_stmt|;
name|executionInfo
operator|.
name|setExecutionStatus
argument_list|(
literal|"CLOSED"
argument_list|)
expr_stmt|;
name|executionInfo
operator|.
name|setTagList
argument_list|(
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|Collections
operator|.
name|EMPTY_LIST
argument_list|)
expr_stmt|;
name|WorkflowExecutionDetail
name|workflowExecutionDetail
init|=
operator|new
name|WorkflowExecutionDetail
argument_list|()
decl_stmt|;
name|workflowExecutionDetail
operator|.
name|setExecutionInfo
argument_list|(
name|executionInfo
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|swClient
operator|.
name|describeWorkflowExecution
argument_list|(
name|any
argument_list|(
name|DescribeWorkflowExecutionRequest
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|workflowExecutionDetail
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|description
init|=
name|camelSWFWorkflowClient
operator|.
name|describeWorkflowInstance
argument_list|(
literal|"123"
argument_list|,
literal|"run1"
argument_list|)
decl_stmt|;
name|DescribeWorkflowExecutionRequest
name|describeRequest
init|=
operator|new
name|DescribeWorkflowExecutionRequest
argument_list|()
decl_stmt|;
name|describeRequest
operator|.
name|setDomain
argument_list|(
name|configuration
operator|.
name|getDomainName
argument_list|()
argument_list|)
expr_stmt|;
name|describeRequest
operator|.
name|setExecution
argument_list|(
operator|new
name|WorkflowExecution
argument_list|()
operator|.
name|withWorkflowId
argument_list|(
literal|"123"
argument_list|)
operator|.
name|withRunId
argument_list|(
literal|"run1"
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|swClient
argument_list|)
operator|.
name|describeWorkflowExecution
argument_list|(
name|describeRequest
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
name|String
operator|)
name|description
operator|.
name|get
argument_list|(
literal|"closeStatus"
argument_list|)
argument_list|,
name|is
argument_list|(
literal|"COMPLETED"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
name|Date
operator|)
name|description
operator|.
name|get
argument_list|(
literal|"closeTimestamp"
argument_list|)
argument_list|,
name|is
argument_list|(
name|closeTimestamp
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
name|String
operator|)
name|description
operator|.
name|get
argument_list|(
literal|"executionStatus"
argument_list|)
argument_list|,
name|is
argument_list|(
literal|"CLOSED"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
name|List
operator|)
name|description
operator|.
name|get
argument_list|(
literal|"tagList"
argument_list|)
argument_list|,
name|is
argument_list|(
name|Collections
operator|.
name|EMPTY_LIST
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
name|WorkflowExecutionDetail
operator|)
name|description
operator|.
name|get
argument_list|(
literal|"executionDetail"
argument_list|)
argument_list|,
name|is
argument_list|(
name|workflowExecutionDetail
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSignalWorkflowExecution ()
specifier|public
name|void
name|testSignalWorkflowExecution
parameter_list|()
throws|throws
name|Exception
block|{
name|camelSWFWorkflowClient
operator|.
name|signalWorkflowExecution
argument_list|(
literal|"123"
argument_list|,
literal|"run1"
argument_list|,
literal|"signalMethod"
argument_list|,
literal|"Hi"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|clientExternal
argument_list|)
operator|.
name|signalWorkflowExecution
argument_list|(
literal|"signalMethod"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"Hi"
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetWorkflowExecutionState ()
specifier|public
name|void
name|testGetWorkflowExecutionState
parameter_list|()
throws|throws
name|Throwable
block|{
name|Class
argument_list|<
name|String
argument_list|>
name|stateType
init|=
name|String
operator|.
name|class
decl_stmt|;
name|when
argument_list|(
name|clientExternal
operator|.
name|getWorkflowExecutionState
argument_list|(
name|stateType
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"some state"
argument_list|)
expr_stmt|;
name|String
name|state
init|=
operator|(
name|String
operator|)
name|camelSWFWorkflowClient
operator|.
name|getWorkflowExecutionState
argument_list|(
literal|"123"
argument_list|,
literal|"run1"
argument_list|,
name|stateType
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|clientExternal
argument_list|)
operator|.
name|getWorkflowExecutionState
argument_list|(
name|stateType
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|state
argument_list|,
name|is
argument_list|(
literal|"some state"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRequestCancelWorkflowExecution ()
specifier|public
name|void
name|testRequestCancelWorkflowExecution
parameter_list|()
throws|throws
name|Throwable
block|{
name|camelSWFWorkflowClient
operator|.
name|requestCancelWorkflowExecution
argument_list|(
literal|"123"
argument_list|,
literal|"run1"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|clientExternal
argument_list|)
operator|.
name|requestCancelWorkflowExecution
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTerminateWorkflowExecution ()
specifier|public
name|void
name|testTerminateWorkflowExecution
parameter_list|()
throws|throws
name|Throwable
block|{
name|camelSWFWorkflowClient
operator|.
name|terminateWorkflowExecution
argument_list|(
literal|"123"
argument_list|,
literal|"run1"
argument_list|,
literal|"reason"
argument_list|,
literal|"details"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|clientExternal
argument_list|)
operator|.
name|terminateWorkflowExecution
argument_list|(
literal|"reason"
argument_list|,
literal|"details"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStartWorkflowExecution ()
specifier|public
name|void
name|testStartWorkflowExecution
parameter_list|()
throws|throws
name|Throwable
block|{
name|WorkflowExecution
name|workflowExecution
init|=
operator|new
name|WorkflowExecution
argument_list|()
decl_stmt|;
name|workflowExecution
operator|.
name|setWorkflowId
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|workflowExecution
operator|.
name|setRunId
argument_list|(
literal|"run1"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|clientExternal
operator|.
name|getWorkflowExecution
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|workflowExecution
argument_list|)
expr_stmt|;
name|String
index|[]
name|ids
init|=
name|camelSWFWorkflowClient
operator|.
name|startWorkflowExecution
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|"eventName"
argument_list|,
literal|"version"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|clientExternal
argument_list|)
operator|.
name|startWorkflowExecution
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|null
block|}
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|"123"
argument_list|,
name|is
argument_list|(
name|ids
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|"run1"
argument_list|,
name|is
argument_list|(
name|ids
index|[
literal|1
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

