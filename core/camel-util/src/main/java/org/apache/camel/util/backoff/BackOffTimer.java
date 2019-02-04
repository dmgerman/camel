begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.backoff
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|backoff
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BiConsumer
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
name|function
operator|.
name|ThrowingFunction
import|;
end_import

begin_comment
comment|/**  * A simple timer utility that use a linked {@link BackOff} to determine when  * a task should be executed.  */
end_comment

begin_class
DECL|class|BackOffTimer
specifier|public
class|class
name|BackOffTimer
block|{
DECL|field|scheduler
specifier|private
specifier|final
name|ScheduledExecutorService
name|scheduler
decl_stmt|;
DECL|method|BackOffTimer (ScheduledExecutorService scheduler)
specifier|public
name|BackOffTimer
parameter_list|(
name|ScheduledExecutorService
name|scheduler
parameter_list|)
block|{
name|this
operator|.
name|scheduler
operator|=
name|scheduler
expr_stmt|;
block|}
comment|/**      * Schedule the given function/task to be executed some time in the future      * according to the given backOff.      */
DECL|method|schedule (BackOff backOff, ThrowingFunction<Task, Boolean, Exception> function)
specifier|public
name|Task
name|schedule
parameter_list|(
name|BackOff
name|backOff
parameter_list|,
name|ThrowingFunction
argument_list|<
name|Task
argument_list|,
name|Boolean
argument_list|,
name|Exception
argument_list|>
name|function
parameter_list|)
block|{
specifier|final
name|BackOffTimerTask
name|task
init|=
operator|new
name|BackOffTimerTask
argument_list|(
name|backOff
argument_list|,
name|scheduler
argument_list|,
name|function
argument_list|)
decl_stmt|;
name|long
name|delay
init|=
name|task
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|delay
operator|!=
name|BackOff
operator|.
name|NEVER
condition|)
block|{
name|scheduler
operator|.
name|schedule
argument_list|(
name|task
argument_list|,
name|delay
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|task
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
return|return
name|task
return|;
block|}
comment|// ****************************************
comment|// TimerTask
comment|// ****************************************
DECL|interface|Task
specifier|public
interface|interface
name|Task
block|{
DECL|enum|Status
enum|enum
name|Status
block|{
DECL|enumConstant|Active
name|Active
block|,
DECL|enumConstant|Inactive
name|Inactive
block|,
DECL|enumConstant|Exhausted
name|Exhausted
block|}
comment|/**          * The back-off associated with this task.          */
DECL|method|getBackOff ()
name|BackOff
name|getBackOff
parameter_list|()
function_decl|;
comment|/**          * Gets the task status.          */
DECL|method|getStatus ()
name|Status
name|getStatus
parameter_list|()
function_decl|;
comment|/**          * The number of attempts so far.          */
DECL|method|getCurrentAttempts ()
name|long
name|getCurrentAttempts
parameter_list|()
function_decl|;
comment|/**          * The current computed delay.          */
DECL|method|getCurrentDelay ()
name|long
name|getCurrentDelay
parameter_list|()
function_decl|;
comment|/**          * The current elapsed time.          */
DECL|method|getCurrentElapsedTime ()
name|long
name|getCurrentElapsedTime
parameter_list|()
function_decl|;
comment|/**          * The time the last attempt has been performed.          */
DECL|method|getLastAttemptTime ()
name|long
name|getLastAttemptTime
parameter_list|()
function_decl|;
comment|/**          * An indication about the time the next attempt will be made.          */
DECL|method|getNextAttemptTime ()
name|long
name|getNextAttemptTime
parameter_list|()
function_decl|;
comment|/**          * Reset the task.          */
DECL|method|reset ()
name|void
name|reset
parameter_list|()
function_decl|;
comment|/**          * Cancel the task.          */
DECL|method|cancel ()
name|void
name|cancel
parameter_list|()
function_decl|;
comment|/**          * Action to execute when the context is completed (cancelled or exhausted)          *          * @param whenCompleted the consumer.          */
DECL|method|whenComplete (BiConsumer<Task, Throwable> whenCompleted)
name|void
name|whenComplete
parameter_list|(
name|BiConsumer
argument_list|<
name|Task
argument_list|,
name|Throwable
argument_list|>
name|whenCompleted
parameter_list|)
function_decl|;
block|}
block|}
end_class

end_unit

