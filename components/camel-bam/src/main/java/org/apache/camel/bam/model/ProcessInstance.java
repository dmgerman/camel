begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.bam.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|bam
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|HashSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|*
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
name|rules
operator|.
name|ActivityRules
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Represents a single business process  *  * @version $Revision: $  */
end_comment

begin_class
annotation|@
name|Entity
DECL|class|ProcessInstance
specifier|public
class|class
name|ProcessInstance
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ProcessInstance
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|processDefinition
specifier|private
name|ProcessDefinition
name|processDefinition
decl_stmt|;
DECL|field|activityStates
specifier|private
name|Collection
argument_list|<
name|ActivityState
argument_list|>
name|activityStates
init|=
operator|new
name|HashSet
argument_list|<
name|ActivityState
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|correlationKey
specifier|private
name|String
name|correlationKey
decl_stmt|;
DECL|field|timeStarted
specifier|private
name|Date
name|timeStarted
decl_stmt|;
DECL|field|timeCompleted
specifier|private
name|Date
name|timeCompleted
decl_stmt|;
DECL|method|ProcessInstance ()
specifier|public
name|ProcessInstance
parameter_list|()
block|{
name|setTimeStarted
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ProcessInstance["
operator|+
name|getCorrelationKey
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Id
DECL|method|getCorrelationKey ()
specifier|public
name|String
name|getCorrelationKey
parameter_list|()
block|{
return|return
name|correlationKey
return|;
block|}
DECL|method|setCorrelationKey (String correlationKey)
specifier|public
name|void
name|setCorrelationKey
parameter_list|(
name|String
name|correlationKey
parameter_list|)
block|{
name|this
operator|.
name|correlationKey
operator|=
name|correlationKey
expr_stmt|;
block|}
annotation|@
name|ManyToOne
argument_list|(
name|fetch
operator|=
name|FetchType
operator|.
name|LAZY
argument_list|,
name|cascade
operator|=
block|{
name|CascadeType
operator|.
name|PERSIST
block|,
name|CascadeType
operator|.
name|MERGE
block|}
argument_list|)
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
annotation|@
name|OneToMany
argument_list|(
name|mappedBy
operator|=
literal|"processInstance"
argument_list|,
name|fetch
operator|=
name|FetchType
operator|.
name|LAZY
argument_list|,
name|cascade
operator|=
block|{
name|CascadeType
operator|.
name|ALL
block|}
argument_list|)
DECL|method|getActivityStates ()
specifier|public
name|Collection
argument_list|<
name|ActivityState
argument_list|>
name|getActivityStates
parameter_list|()
block|{
return|return
name|activityStates
return|;
block|}
DECL|method|setActivityStates (Collection<ActivityState> activityStates)
specifier|public
name|void
name|setActivityStates
parameter_list|(
name|Collection
argument_list|<
name|ActivityState
argument_list|>
name|activityStates
parameter_list|)
block|{
name|this
operator|.
name|activityStates
operator|=
name|activityStates
expr_stmt|;
block|}
annotation|@
name|Transient
DECL|method|isStarted ()
specifier|public
name|boolean
name|isStarted
parameter_list|()
block|{
return|return
name|timeStarted
operator|!=
literal|null
return|;
block|}
annotation|@
name|Transient
DECL|method|isCompleted ()
specifier|public
name|boolean
name|isCompleted
parameter_list|()
block|{
return|return
name|timeCompleted
operator|!=
literal|null
return|;
block|}
annotation|@
name|Temporal
argument_list|(
name|TemporalType
operator|.
name|TIME
argument_list|)
DECL|method|getTimeStarted ()
specifier|public
name|Date
name|getTimeStarted
parameter_list|()
block|{
return|return
name|timeStarted
return|;
block|}
DECL|method|setTimeStarted (Date timeStarted)
specifier|public
name|void
name|setTimeStarted
parameter_list|(
name|Date
name|timeStarted
parameter_list|)
block|{
name|this
operator|.
name|timeStarted
operator|=
name|timeStarted
expr_stmt|;
block|}
annotation|@
name|Temporal
argument_list|(
name|TemporalType
operator|.
name|TIME
argument_list|)
DECL|method|getTimeCompleted ()
specifier|public
name|Date
name|getTimeCompleted
parameter_list|()
block|{
return|return
name|timeCompleted
return|;
block|}
DECL|method|setTimeCompleted (Date timeCompleted)
specifier|public
name|void
name|setTimeCompleted
parameter_list|(
name|Date
name|timeCompleted
parameter_list|)
block|{
name|this
operator|.
name|timeCompleted
operator|=
name|timeCompleted
expr_stmt|;
block|}
comment|// Helper methods
comment|//-------------------------------------------------------------------------
comment|/**      * Returns the activity state for the given activity      *      * @param activityRules the activity to find the state for      * @return the activity state or null if no state could be found for the      *         given activity      */
DECL|method|getActivityState (ActivityRules activityRules)
specifier|public
name|ActivityState
name|getActivityState
parameter_list|(
name|ActivityRules
name|activityRules
parameter_list|)
block|{
for|for
control|(
name|ActivityState
name|activityState
range|:
name|getActivityStates
argument_list|()
control|)
block|{
if|if
condition|(
name|activityState
operator|.
name|isActivity
argument_list|(
name|activityRules
argument_list|)
condition|)
block|{
return|return
name|activityState
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getOrCreateActivityState (ActivityRules activityRules)
specifier|public
name|ActivityState
name|getOrCreateActivityState
parameter_list|(
name|ActivityRules
name|activityRules
parameter_list|)
block|{
name|ActivityState
name|state
init|=
name|getActivityState
argument_list|(
name|activityRules
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|==
literal|null
condition|)
block|{
name|state
operator|=
name|createActivityState
argument_list|()
expr_stmt|;
name|state
operator|.
name|setProcessInstance
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|state
operator|.
name|setActivityDefinition
argument_list|(
name|activityRules
operator|.
name|getActivityDefinition
argument_list|()
argument_list|)
expr_stmt|;
comment|// we don't need to do: getTemplate().persist(state);
block|}
return|return
name|state
return|;
block|}
DECL|method|createActivityState ()
specifier|protected
name|ActivityState
name|createActivityState
parameter_list|()
block|{
return|return
operator|new
name|ActivityState
argument_list|()
return|;
block|}
block|}
end_class

end_unit

