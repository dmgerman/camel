begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.bam.rules
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|bam
operator|.
name|rules
package|;
end_package

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
name|bam
operator|.
name|model
operator|.
name|ActivityState
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
name|bam
operator|.
name|model
operator|.
name|ProcessDefinition
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
name|bam
operator|.
name|model
operator|.
name|ProcessInstance
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
name|impl
operator|.
name|ServiceSupport
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
name|util
operator|.
name|ServiceHelper
import|;
end_import

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

begin_comment
comment|/**  * @version $Revision: $  */
end_comment

begin_class
DECL|class|ProcessRules
specifier|public
class|class
name|ProcessRules
extends|extends
name|ServiceSupport
block|{
DECL|field|processDefinition
specifier|private
name|ProcessDefinition
name|processDefinition
decl_stmt|;
DECL|field|activities
specifier|private
name|List
argument_list|<
name|ActivityRules
argument_list|>
name|activities
init|=
operator|new
name|ArrayList
argument_list|<
name|ActivityRules
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|processExpired (ActivityState activityState)
specifier|public
name|void
name|processExpired
parameter_list|(
name|ActivityState
name|activityState
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|ActivityRules
name|activityRules
range|:
name|activities
control|)
block|{
name|activityRules
operator|.
name|processExpired
argument_list|(
name|activityState
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processExchange (Exchange exchange, ProcessInstance process)
specifier|public
name|void
name|processExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|ProcessInstance
name|process
parameter_list|)
block|{
for|for
control|(
name|ActivityRules
name|activityRules
range|:
name|activities
control|)
block|{
name|activityRules
operator|.
name|processExchange
argument_list|(
name|exchange
argument_list|,
name|process
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getActivities ()
specifier|public
name|List
argument_list|<
name|ActivityRules
argument_list|>
name|getActivities
parameter_list|()
block|{
return|return
name|activities
return|;
block|}
DECL|method|getProcessDefinition ()
specifier|public
name|ProcessDefinition
name|getProcessDefinition
parameter_list|()
block|{
return|return
name|processDefinition
return|;
block|}
DECL|method|setProcessDefinition (ProcessDefinition processDefinition)
specifier|public
name|void
name|setProcessDefinition
parameter_list|(
name|ProcessDefinition
name|processDefinition
parameter_list|)
block|{
name|this
operator|.
name|processDefinition
operator|=
name|processDefinition
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|activities
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|activities
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

