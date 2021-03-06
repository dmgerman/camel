begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbpm.listeners
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbpm
operator|.
name|listeners
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|jbpm
operator|.
name|JBPMCamelConsumerAware
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
name|jbpm
operator|.
name|JBPMConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kie
operator|.
name|api
operator|.
name|task
operator|.
name|TaskEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kie
operator|.
name|api
operator|.
name|task
operator|.
name|TaskLifeCycleEventListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kie
operator|.
name|internal
operator|.
name|runtime
operator|.
name|Cacheable
import|;
end_import

begin_class
DECL|class|CamelTaskEventListener
specifier|public
class|class
name|CamelTaskEventListener
implements|implements
name|Cacheable
implements|,
name|TaskLifeCycleEventListener
implements|,
name|JBPMCamelConsumerAware
block|{
DECL|field|consumers
specifier|private
name|Set
argument_list|<
name|JBPMConsumer
argument_list|>
name|consumers
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|beforeTaskActivatedEvent (TaskEvent event)
specifier|public
name|void
name|beforeTaskActivatedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"beforeTaskActivatedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeTaskClaimedEvent (TaskEvent event)
specifier|public
name|void
name|beforeTaskClaimedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"beforeTaskClaimedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeTaskSkippedEvent (TaskEvent event)
specifier|public
name|void
name|beforeTaskSkippedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"beforeTaskSkippedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeTaskStartedEvent (TaskEvent event)
specifier|public
name|void
name|beforeTaskStartedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"beforeTaskStartedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeTaskStoppedEvent (TaskEvent event)
specifier|public
name|void
name|beforeTaskStoppedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"beforeTaskStoppedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeTaskCompletedEvent (TaskEvent event)
specifier|public
name|void
name|beforeTaskCompletedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"beforeTaskCompletedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeTaskFailedEvent (TaskEvent event)
specifier|public
name|void
name|beforeTaskFailedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"beforeTaskFailedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeTaskAddedEvent (TaskEvent event)
specifier|public
name|void
name|beforeTaskAddedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"beforeTaskAddedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeTaskExitedEvent (TaskEvent event)
specifier|public
name|void
name|beforeTaskExitedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"beforeTaskExitedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeTaskReleasedEvent (TaskEvent event)
specifier|public
name|void
name|beforeTaskReleasedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"beforeTaskReleasedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeTaskResumedEvent (TaskEvent event)
specifier|public
name|void
name|beforeTaskResumedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"beforeTaskResumedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeTaskSuspendedEvent (TaskEvent event)
specifier|public
name|void
name|beforeTaskSuspendedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"beforeTaskSuspendedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeTaskForwardedEvent (TaskEvent event)
specifier|public
name|void
name|beforeTaskForwardedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"beforeTaskForwardedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeTaskDelegatedEvent (TaskEvent event)
specifier|public
name|void
name|beforeTaskDelegatedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"beforeTaskDelegatedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeTaskNominatedEvent (TaskEvent event)
specifier|public
name|void
name|beforeTaskNominatedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"beforeTaskNominatedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterTaskActivatedEvent (TaskEvent event)
specifier|public
name|void
name|afterTaskActivatedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"afterTaskActivatedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterTaskClaimedEvent (TaskEvent event)
specifier|public
name|void
name|afterTaskClaimedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"afterTaskClaimedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterTaskSkippedEvent (TaskEvent event)
specifier|public
name|void
name|afterTaskSkippedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"afterTaskSkippedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterTaskStartedEvent (TaskEvent event)
specifier|public
name|void
name|afterTaskStartedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"afterTaskStartedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterTaskStoppedEvent (TaskEvent event)
specifier|public
name|void
name|afterTaskStoppedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"afterTaskStoppedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterTaskCompletedEvent (TaskEvent event)
specifier|public
name|void
name|afterTaskCompletedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"afterTaskCompletedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterTaskFailedEvent (TaskEvent event)
specifier|public
name|void
name|afterTaskFailedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"afterTaskFailedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterTaskAddedEvent (TaskEvent event)
specifier|public
name|void
name|afterTaskAddedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"afterTaskAddedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterTaskExitedEvent (TaskEvent event)
specifier|public
name|void
name|afterTaskExitedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"afterTaskExitedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterTaskReleasedEvent (TaskEvent event)
specifier|public
name|void
name|afterTaskReleasedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"afterTaskReleasedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterTaskResumedEvent (TaskEvent event)
specifier|public
name|void
name|afterTaskResumedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"afterTaskResumedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterTaskSuspendedEvent (TaskEvent event)
specifier|public
name|void
name|afterTaskSuspendedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"afterTaskSuspendedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterTaskForwardedEvent (TaskEvent event)
specifier|public
name|void
name|afterTaskForwardedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"afterTaskForwardedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterTaskDelegatedEvent (TaskEvent event)
specifier|public
name|void
name|afterTaskDelegatedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"afterTaskDelegatedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterTaskNominatedEvent (TaskEvent event)
specifier|public
name|void
name|afterTaskNominatedEvent
parameter_list|(
name|TaskEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|consumers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sendMessage
argument_list|(
literal|"afterTaskNominatedEvent"
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{              }
annotation|@
name|Override
DECL|method|addConsumer (JBPMConsumer consumer)
specifier|public
name|void
name|addConsumer
parameter_list|(
name|JBPMConsumer
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|consumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removeConsumer (JBPMConsumer consumer)
specifier|public
name|void
name|removeConsumer
parameter_list|(
name|JBPMConsumer
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|consumers
operator|.
name|remove
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|sendMessage (String eventType, Object event)
specifier|protected
name|void
name|sendMessage
parameter_list|(
name|String
name|eventType
parameter_list|,
name|Object
name|event
parameter_list|)
block|{
name|this
operator|.
name|consumers
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|c
lambda|->
name|c
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
argument_list|)
operator|.
name|forEach
argument_list|(
name|c
lambda|->
name|c
operator|.
name|sendMessage
argument_list|(
name|eventType
argument_list|,
name|event
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

