begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|amazonaws
operator|.
name|AmazonClientException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|AmazonServiceException
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
name|model
operator|.
name|Run
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
name|StartWorkflowExecutionRequest
import|;
end_import

begin_class
DECL|class|AmazonSWFClientMock
specifier|public
class|class
name|AmazonSWFClientMock
extends|extends
name|AmazonSimpleWorkflowClient
block|{
annotation|@
name|Override
DECL|method|startWorkflowExecution (StartWorkflowExecutionRequest startWorkflowExecutionRequest)
specifier|public
name|Run
name|startWorkflowExecution
parameter_list|(
name|StartWorkflowExecutionRequest
name|startWorkflowExecutionRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
return|return
operator|new
name|Run
argument_list|()
operator|.
name|withRunId
argument_list|(
literal|"run1"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

