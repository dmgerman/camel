begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * A {@link Service} which has all the lifecycle events and offers details about its current state.  */
end_comment

begin_interface
DECL|interface|StatefulService
specifier|public
interface|interface
name|StatefulService
extends|extends
name|SuspendableService
extends|,
name|ShutdownableService
block|{
comment|/**      * Returns the current status      *      * @return the current status      */
DECL|method|getStatus ()
name|ServiceStatus
name|getStatus
parameter_list|()
function_decl|;
comment|/**      * Whether the service is started      *      * @return true if this service has been started      */
DECL|method|isStarted ()
name|boolean
name|isStarted
parameter_list|()
function_decl|;
comment|/**      * Whether the service is starting      *      * @return true if this service is being started      */
DECL|method|isStarting ()
name|boolean
name|isStarting
parameter_list|()
function_decl|;
comment|/**      * Whether the service is stopping      *      * @return true if this service is in the process of stopping      */
DECL|method|isStopping ()
name|boolean
name|isStopping
parameter_list|()
function_decl|;
comment|/**      * Whether the service is stopped      *      * @return true if this service is stopped      */
DECL|method|isStopped ()
name|boolean
name|isStopped
parameter_list|()
function_decl|;
comment|/**      * Whether the service is suspending      *      * @return true if this service is in the process of suspending      */
DECL|method|isSuspending ()
name|boolean
name|isSuspending
parameter_list|()
function_decl|;
comment|/**      * Helper methods so the service knows if it should keep running.      * Returns<tt>false</tt> if the service is being stopped or is stopped.      *      * @return<tt>true</tt> if the service should continue to run.      */
DECL|method|isRunAllowed ()
name|boolean
name|isRunAllowed
parameter_list|()
function_decl|;
comment|/**      * Returns the version of this service      *      * @return the version      */
DECL|method|getVersion ()
name|String
name|getVersion
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

