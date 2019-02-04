begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.resequencer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|resequencer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Timer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimerTask
import|;
end_import

begin_comment
comment|/**  * A timer task that notifies handlers about scheduled timeouts.  *   * @see Timer  * @see TimerTask  */
end_comment

begin_class
DECL|class|Timeout
specifier|public
class|class
name|Timeout
extends|extends
name|TimerTask
block|{
DECL|field|timeoutHandler
specifier|private
name|TimeoutHandler
name|timeoutHandler
decl_stmt|;
DECL|field|timer
specifier|private
name|Timer
name|timer
decl_stmt|;
DECL|field|timeout
specifier|private
name|long
name|timeout
decl_stmt|;
comment|/**      * Creates a new timeout task using the given {@link Timer} instance and      * timeout value. The task is not scheduled immediately. It will be      * scheduled by calling this task's {@link #schedule()} method.      *       * @param timer a timer      * @param timeout a timeout value.      */
DECL|method|Timeout (Timer timer, long timeout)
specifier|public
name|Timeout
parameter_list|(
name|Timer
name|timer
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
name|this
operator|.
name|timer
operator|=
name|timer
expr_stmt|;
block|}
comment|/**      * Returns the timeout handler that has been registered for notification.      *       * @return the timeout handler.      */
DECL|method|getTimeoutHandlers ()
specifier|public
name|TimeoutHandler
name|getTimeoutHandlers
parameter_list|()
block|{
return|return
name|timeoutHandler
return|;
block|}
comment|/**      * Sets a timeout handler for receiving timeout notifications.      *       * @param timeoutHandler      *            a timeout handler.      */
DECL|method|setTimeoutHandler (TimeoutHandler timeoutHandler)
specifier|public
name|void
name|setTimeoutHandler
parameter_list|(
name|TimeoutHandler
name|timeoutHandler
parameter_list|)
block|{
name|this
operator|.
name|timeoutHandler
operator|=
name|timeoutHandler
expr_stmt|;
block|}
comment|/**      * Schedules this timeout task.      */
DECL|method|schedule ()
specifier|public
name|void
name|schedule
parameter_list|()
block|{
name|timer
operator|.
name|schedule
argument_list|(
name|this
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
comment|/**      * Notifies the timeout handler about the scheduled timeout.      */
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|timeoutHandler
operator|.
name|timeout
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

