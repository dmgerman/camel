begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeepermaster.group
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeepermaster
operator|.
name|group
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_interface
DECL|interface|Group
specifier|public
interface|interface
name|Group
parameter_list|<
name|T
extends|extends
name|NodeState
parameter_list|>
extends|extends
name|Closeable
block|{
comment|/**      * Are we connected with the cluster?      */
DECL|method|isConnected ()
name|boolean
name|isConnected
parameter_list|()
function_decl|;
comment|/**      * Start this member      */
DECL|method|start ()
name|void
name|start
parameter_list|()
function_decl|;
comment|/**      * A member should be closed to release acquired resources used      * to monitor the group membership.      *      * When the member is closed, any memberships registered via this      * Group will be removed from the group.      */
annotation|@
name|Override
DECL|method|close ()
name|void
name|close
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Registers a listener which will be called      * when the cluster membership changes or      * the group is connected or disconnected.      */
DECL|method|add (GroupListener<T> listener)
name|void
name|add
parameter_list|(
name|GroupListener
argument_list|<
name|T
argument_list|>
name|listener
parameter_list|)
function_decl|;
comment|/**      * Removes a previously added listener.      */
DECL|method|remove (GroupListener<T> listener)
name|void
name|remove
parameter_list|(
name|GroupListener
argument_list|<
name|T
argument_list|>
name|listener
parameter_list|)
function_decl|;
comment|/**      * Update the state of this group member.      * If the state is null, the member will leave the group.      *      * This method can be called even if the group is not started,      * in which case the state will be stored and updated      * when the group becomes started.      *      * @param state the new state of this group member      */
DECL|method|update (T state)
name|void
name|update
parameter_list|(
name|T
name|state
parameter_list|)
function_decl|;
comment|/**      * Get the list of members connected to this group.      */
DECL|method|members ()
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|members
parameter_list|()
function_decl|;
comment|/**      * Check if we are the master.      */
DECL|method|isMaster ()
name|boolean
name|isMaster
parameter_list|()
function_decl|;
comment|/**      * Retrieve the master node.      */
DECL|method|master ()
name|T
name|master
parameter_list|()
function_decl|;
comment|/**      * Retrieve the list of slaves.      */
DECL|method|slaves ()
name|List
argument_list|<
name|T
argument_list|>
name|slaves
parameter_list|()
function_decl|;
comment|/**      * Gets the last state.      *<p/>      * This can be used by clients to access that last state, such as when the clients is being added      * as a {@link #add(GroupListener) listener} but wants to retrieve the last state to be up to date when the      * client is added.      *      * @return the state, or<tt>null</tt> if no last state yet.      */
DECL|method|getLastState ()
name|T
name|getLastState
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

