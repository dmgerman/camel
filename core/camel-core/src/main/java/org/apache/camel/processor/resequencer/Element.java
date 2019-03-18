begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * A container for objects to be resequenced. This container can be scheduled  * for timing out. Non-scheduled objects or already timed-out objects are ready  * for being released by the {@link ResequencerEngine}.  */
end_comment

begin_class
DECL|class|Element
class|class
name|Element
parameter_list|<
name|E
parameter_list|>
implements|implements
name|TimeoutHandler
block|{
comment|/**      * The contained object.      */
DECL|field|object
specifier|private
name|E
name|object
decl_stmt|;
comment|/**      * Not<code>null</code> if this element is currently beeing scheduled for      * timing out.      */
DECL|field|timeout
specifier|private
name|Timeout
name|timeout
decl_stmt|;
comment|/**      * Creates a new container instance.      *       * @param object contained object.      */
DECL|method|Element (E object)
name|Element
parameter_list|(
name|E
name|object
parameter_list|)
block|{
name|this
operator|.
name|object
operator|=
name|object
expr_stmt|;
block|}
comment|/**      * Returns the contained object.      *       * @return the contained object.      */
DECL|method|getObject ()
specifier|public
name|E
name|getObject
parameter_list|()
block|{
return|return
name|object
return|;
block|}
comment|/**      * Returns<code>true</code> if this element is currently scheduled for      * timing out.      *       * @return<code>true</code> if scheduled or<code>false</code> if not      *         scheduled or already timed-out.      */
DECL|method|scheduled ()
specifier|public
specifier|synchronized
name|boolean
name|scheduled
parameter_list|()
block|{
return|return
name|timeout
operator|!=
literal|null
return|;
block|}
comment|/**      * Schedules the given timeout task. Before this methods calls the      * {@link Timeout#schedule()} method it sets this element as timeout      * listener.      *       * @param t a timeout task.      */
DECL|method|schedule (Timeout t)
specifier|public
specifier|synchronized
name|void
name|schedule
parameter_list|(
name|Timeout
name|t
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|t
expr_stmt|;
name|this
operator|.
name|timeout
operator|.
name|setTimeoutHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|timeout
operator|.
name|schedule
argument_list|()
expr_stmt|;
block|}
comment|/**      * Cancels the scheduled timeout for this element. If this element is not      * scheduled or has already timed-out this method has no effect.      */
DECL|method|cancel ()
specifier|public
specifier|synchronized
name|void
name|cancel
parameter_list|()
block|{
if|if
condition|(
name|timeout
operator|!=
literal|null
condition|)
block|{
name|timeout
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
name|timeout
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Marks this element as timed-out.      *       * @param t timeout task that caused the notification.      */
DECL|method|timeout (Timeout t)
specifier|public
specifier|synchronized
name|void
name|timeout
parameter_list|(
name|Timeout
name|t
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

