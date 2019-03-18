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
name|services
operator|.
name|simpleworkflow
operator|.
name|flow
operator|.
name|DataConverter
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
name|DecisionContext
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
name|WorkflowTypeRegistrationOptions
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
name|generic
operator|.
name|WorkflowDefinition
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
name|generic
operator|.
name|WorkflowDefinitionFactory
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
name|worker
operator|.
name|CurrentDecisionContext
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
name|WorkflowType
import|;
end_import

begin_class
DECL|class|CamelWorkflowDefinitionFactory
specifier|public
class|class
name|CamelWorkflowDefinitionFactory
extends|extends
name|WorkflowDefinitionFactory
block|{
DECL|field|swfWorkflowConsumer
specifier|private
name|SWFWorkflowConsumer
name|swfWorkflowConsumer
decl_stmt|;
DECL|field|workflowType
specifier|private
name|WorkflowType
name|workflowType
decl_stmt|;
DECL|field|registrationOptions
specifier|private
name|WorkflowTypeRegistrationOptions
name|registrationOptions
decl_stmt|;
DECL|field|dataConverter
specifier|private
name|DataConverter
name|dataConverter
decl_stmt|;
DECL|method|CamelWorkflowDefinitionFactory (SWFWorkflowConsumer swfWorkflowConsumer, WorkflowType workflowType, WorkflowTypeRegistrationOptions registrationOptions, DataConverter dataConverter)
specifier|public
name|CamelWorkflowDefinitionFactory
parameter_list|(
name|SWFWorkflowConsumer
name|swfWorkflowConsumer
parameter_list|,
name|WorkflowType
name|workflowType
parameter_list|,
name|WorkflowTypeRegistrationOptions
name|registrationOptions
parameter_list|,
name|DataConverter
name|dataConverter
parameter_list|)
block|{
name|this
operator|.
name|swfWorkflowConsumer
operator|=
name|swfWorkflowConsumer
expr_stmt|;
name|this
operator|.
name|workflowType
operator|=
name|workflowType
expr_stmt|;
name|this
operator|.
name|registrationOptions
operator|=
name|registrationOptions
expr_stmt|;
name|this
operator|.
name|dataConverter
operator|=
name|dataConverter
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getWorkflowRegistrationOptions ()
specifier|public
name|WorkflowTypeRegistrationOptions
name|getWorkflowRegistrationOptions
parameter_list|()
block|{
return|return
name|registrationOptions
return|;
block|}
annotation|@
name|Override
DECL|method|getWorkflowDefinition (DecisionContext context)
specifier|public
name|WorkflowDefinition
name|getWorkflowDefinition
parameter_list|(
name|DecisionContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|CurrentDecisionContext
operator|.
name|set
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
operator|new
name|CamelWorkflowDefinition
argument_list|(
name|swfWorkflowConsumer
argument_list|,
name|context
argument_list|,
name|dataConverter
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|deleteWorkflowDefinition (WorkflowDefinition instance)
specifier|public
name|void
name|deleteWorkflowDefinition
parameter_list|(
name|WorkflowDefinition
name|instance
parameter_list|)
block|{
name|CurrentDecisionContext
operator|.
name|unset
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getWorkflowType ()
specifier|public
name|WorkflowType
name|getWorkflowType
parameter_list|()
block|{
return|return
name|workflowType
return|;
block|}
block|}
end_class

end_unit

