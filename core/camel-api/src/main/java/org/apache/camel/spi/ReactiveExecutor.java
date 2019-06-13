begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
name|AsyncCallback
import|;
end_import

begin_comment
comment|/**  * SPI to plugin different reactive engines in the Camel routing engine.  */
end_comment

begin_interface
DECL|interface|ReactiveExecutor
specifier|public
interface|interface
name|ReactiveExecutor
block|{
comment|/**      * Schedules the task to be run      *      * @param runnable    the task      */
DECL|method|schedule (Runnable runnable)
specifier|default
name|void
name|schedule
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
name|schedule
argument_list|(
name|runnable
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Schedules the task to be run      *      * @param runnable    the task      * @param description a human readable description for logging purpose      */
DECL|method|schedule (Runnable runnable, String description)
name|void
name|schedule
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|String
name|description
parameter_list|)
function_decl|;
comment|/**      * Schedules the task to be prioritized and run asap      *      * @param runnable    the task      */
DECL|method|scheduleMain (Runnable runnable)
specifier|default
name|void
name|scheduleMain
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
name|scheduleMain
argument_list|(
name|runnable
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Schedules the task to be prioritized and run asap      *      * @param runnable    the task      * @param description a human readable description for logging purpose      */
DECL|method|scheduleMain (Runnable runnable, String description)
name|void
name|scheduleMain
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|String
name|description
parameter_list|)
function_decl|;
comment|/**      * Schedules the task to run synchronously      *      * @param runnable    the task      */
DECL|method|scheduleSync (Runnable runnable)
specifier|default
name|void
name|scheduleSync
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
name|scheduleSync
argument_list|(
name|runnable
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Schedules the task to run synchronously      *      * @param runnable    the task      * @param description a human readable description for logging purpose      */
DECL|method|scheduleSync (Runnable runnable, String description)
name|void
name|scheduleSync
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|String
name|description
parameter_list|)
function_decl|;
comment|/**      * Executes the next task      *      * @return true if a task was executed or false if no more pending tasks      */
DECL|method|executeFromQueue ()
name|boolean
name|executeFromQueue
parameter_list|()
function_decl|;
comment|/**      * Schedules the callback to be run      *      * @param callback    the callable      */
DECL|method|callback (AsyncCallback callback)
specifier|default
name|void
name|callback
parameter_list|(
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|schedule
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Callback["
operator|+
name|callback
operator|+
literal|"]"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_interface

end_unit

