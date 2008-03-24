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
name|Date
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|CascadeType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|FetchType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|GeneratedValue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Id
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|ManyToOne
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Temporal
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|TemporalType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Transient
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
name|processor
operator|.
name|ProcessContext
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
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The default state for a specific activity within a process  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Entity
DECL|class|ActivityState
specifier|public
class|class
name|ActivityState
extends|extends
name|TemporalEntity
block|{
DECL|field|processInstance
specifier|private
name|ProcessInstance
name|processInstance
decl_stmt|;
DECL|field|receivedMessageCount
specifier|private
name|Integer
name|receivedMessageCount
init|=
literal|0
decl_stmt|;
DECL|field|activityDefinition
specifier|private
name|ActivityDefinition
name|activityDefinition
decl_stmt|;
DECL|field|timeExpected
specifier|private
name|Date
name|timeExpected
decl_stmt|;
annotation|@
name|Temporal
argument_list|(
name|TemporalType
operator|.
name|TIME
argument_list|)
DECL|field|timeOverdue
specifier|private
name|Date
name|timeOverdue
decl_stmt|;
DECL|field|escalationLevel
specifier|private
name|Integer
name|escalationLevel
init|=
literal|0
decl_stmt|;
comment|// This crap is required to work around a bug in hibernate
annotation|@
name|Override
annotation|@
name|Id
annotation|@
name|GeneratedValue
DECL|method|getId ()
specifier|public
name|Long
name|getId
parameter_list|()
block|{
return|return
name|super
operator|.
name|getId
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ActivityState["
operator|+
name|getId
argument_list|()
operator|+
literal|" on "
operator|+
name|getProcessInstance
argument_list|()
operator|+
literal|" "
operator|+
name|getActivityDefinition
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|processExchange (ActivityRules activityRules, ProcessContext context)
specifier|public
specifier|synchronized
name|void
name|processExchange
parameter_list|(
name|ActivityRules
name|activityRules
parameter_list|,
name|ProcessContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|messageCount
init|=
literal|0
decl_stmt|;
name|Integer
name|count
init|=
name|getReceivedMessageCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|count
operator|!=
literal|null
condition|)
block|{
name|messageCount
operator|=
name|count
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
name|setReceivedMessageCount
argument_list|(
operator|++
name|messageCount
argument_list|)
expr_stmt|;
if|if
condition|(
name|messageCount
operator|==
literal|1
condition|)
block|{
name|onFirstMessage
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
name|int
name|expectedMessages
init|=
name|activityRules
operator|.
name|getExpectedMessages
argument_list|()
decl_stmt|;
if|if
condition|(
name|messageCount
operator|==
name|expectedMessages
condition|)
block|{
name|onExpectedMessage
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|messageCount
operator|>
name|expectedMessages
condition|)
block|{
name|onExcessMessage
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns true if this state is for the given activity      */
DECL|method|isActivity (ActivityRules activityRules)
specifier|public
name|boolean
name|isActivity
parameter_list|(
name|ActivityRules
name|activityRules
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|getActivityDefinition
argument_list|()
argument_list|,
name|activityRules
operator|.
name|getActivityDefinition
argument_list|()
argument_list|)
return|;
block|}
comment|// Properties
comment|// -----------------------------------------------------------------------
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
DECL|method|getProcessInstance ()
specifier|public
name|ProcessInstance
name|getProcessInstance
parameter_list|()
block|{
return|return
name|processInstance
return|;
block|}
DECL|method|setProcessInstance (ProcessInstance processInstance)
specifier|public
name|void
name|setProcessInstance
parameter_list|(
name|ProcessInstance
name|processInstance
parameter_list|)
block|{
name|this
operator|.
name|processInstance
operator|=
name|processInstance
expr_stmt|;
name|processInstance
operator|.
name|getActivityStates
argument_list|()
operator|.
name|add
argument_list|(
name|this
argument_list|)
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
DECL|method|getActivityDefinition ()
specifier|public
name|ActivityDefinition
name|getActivityDefinition
parameter_list|()
block|{
return|return
name|activityDefinition
return|;
block|}
DECL|method|setActivityDefinition (ActivityDefinition activityDefinition)
specifier|public
name|void
name|setActivityDefinition
parameter_list|(
name|ActivityDefinition
name|activityDefinition
parameter_list|)
block|{
name|this
operator|.
name|activityDefinition
operator|=
name|activityDefinition
expr_stmt|;
block|}
DECL|method|getEscalationLevel ()
specifier|public
name|Integer
name|getEscalationLevel
parameter_list|()
block|{
return|return
name|escalationLevel
return|;
block|}
DECL|method|setEscalationLevel (Integer escalationLevel)
specifier|public
name|void
name|setEscalationLevel
parameter_list|(
name|Integer
name|escalationLevel
parameter_list|)
block|{
name|this
operator|.
name|escalationLevel
operator|=
name|escalationLevel
expr_stmt|;
block|}
DECL|method|getReceivedMessageCount ()
specifier|public
name|Integer
name|getReceivedMessageCount
parameter_list|()
block|{
return|return
name|receivedMessageCount
return|;
block|}
DECL|method|setReceivedMessageCount (Integer receivedMessageCount)
specifier|public
name|void
name|setReceivedMessageCount
parameter_list|(
name|Integer
name|receivedMessageCount
parameter_list|)
block|{
name|this
operator|.
name|receivedMessageCount
operator|=
name|receivedMessageCount
expr_stmt|;
block|}
annotation|@
name|Temporal
argument_list|(
name|TemporalType
operator|.
name|TIME
argument_list|)
DECL|method|getTimeExpected ()
specifier|public
name|Date
name|getTimeExpected
parameter_list|()
block|{
return|return
name|timeExpected
return|;
block|}
DECL|method|setTimeExpected (Date timeExpected)
specifier|public
name|void
name|setTimeExpected
parameter_list|(
name|Date
name|timeExpected
parameter_list|)
block|{
name|this
operator|.
name|timeExpected
operator|=
name|timeExpected
expr_stmt|;
block|}
annotation|@
name|Temporal
argument_list|(
name|TemporalType
operator|.
name|TIME
argument_list|)
DECL|method|getTimeOverdue ()
specifier|public
name|Date
name|getTimeOverdue
parameter_list|()
block|{
return|return
name|timeOverdue
return|;
block|}
DECL|method|setTimeOverdue (Date timeOverdue)
specifier|public
name|void
name|setTimeOverdue
parameter_list|(
name|Date
name|timeOverdue
parameter_list|)
block|{
name|this
operator|.
name|timeOverdue
operator|=
name|timeOverdue
expr_stmt|;
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
name|super
operator|.
name|setTimeCompleted
argument_list|(
name|timeCompleted
argument_list|)
expr_stmt|;
if|if
condition|(
name|timeCompleted
operator|!=
literal|null
condition|)
block|{
name|setEscalationLevel
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Transient
DECL|method|getCorrelationKey ()
specifier|public
name|String
name|getCorrelationKey
parameter_list|()
block|{
name|ProcessInstance
name|pi
init|=
name|getProcessInstance
argument_list|()
decl_stmt|;
if|if
condition|(
name|pi
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|pi
operator|.
name|getCorrelationKey
argument_list|()
return|;
block|}
comment|// Implementation methods
comment|// -----------------------------------------------------------------------
comment|/**      * Called when the first message is reached      */
DECL|method|onFirstMessage (ProcessContext context)
specifier|protected
name|void
name|onFirstMessage
parameter_list|(
name|ProcessContext
name|context
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isStarted
argument_list|()
condition|)
block|{
name|setTimeStarted
argument_list|(
name|currentTime
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|onStarted
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Called when the expected number of messages are is reached      */
DECL|method|onExpectedMessage (ProcessContext context)
specifier|protected
name|void
name|onExpectedMessage
parameter_list|(
name|ProcessContext
name|context
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isCompleted
argument_list|()
condition|)
block|{
name|setTimeCompleted
argument_list|(
name|currentTime
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|onCompleted
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Called when an excess message (after the expected number of messages) are      * received      */
DECL|method|onExcessMessage (ProcessContext context)
specifier|protected
name|void
name|onExcessMessage
parameter_list|(
name|ProcessContext
name|context
parameter_list|)
block|{
comment|// TODO
block|}
DECL|method|currentTime ()
specifier|protected
name|Date
name|currentTime
parameter_list|()
block|{
return|return
operator|new
name|Date
argument_list|()
return|;
block|}
block|}
end_class

end_unit

