begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.ha
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ha
package|;
end_package

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
name|Optional
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
name|CamelContextAware
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
name|Service
import|;
end_import

begin_comment
comment|/**  * Represents the View of the cluster at some given period of time.  */
end_comment

begin_interface
DECL|interface|CamelClusterView
specifier|public
interface|interface
name|CamelClusterView
extends|extends
name|Service
extends|,
name|CamelContextAware
block|{
comment|/**      * @return the cluster.      */
DECL|method|getClusterService ()
name|CamelClusterService
name|getClusterService
parameter_list|()
function_decl|;
comment|/**      * @return the namespace for this view.      */
DECL|method|getNamespace ()
name|String
name|getNamespace
parameter_list|()
function_decl|;
comment|/**      * Provides the master member if elected.      *      * @return the master member.      */
DECL|method|getMaster ()
name|Optional
argument_list|<
name|CamelClusterMember
argument_list|>
name|getMaster
parameter_list|()
function_decl|;
comment|/**      * Provides the local member.      *      * @return the local member.      */
DECL|method|getLocalMember ()
name|CamelClusterMember
name|getLocalMember
parameter_list|()
function_decl|;
comment|/**      * Provides the list of members of the cluster.      *      * @return the list of members.      */
DECL|method|getMembers ()
name|List
argument_list|<
name|CamelClusterMember
argument_list|>
name|getMembers
parameter_list|()
function_decl|;
comment|/**      * Add an event listener.      *      * @param listener the event listener.      */
DECL|method|addEventListener (CameClusterEventListener listener)
name|void
name|addEventListener
parameter_list|(
name|CameClusterEventListener
name|listener
parameter_list|)
function_decl|;
comment|/**      * Remove the event listener.      *      * @param listener the event listener.      */
DECL|method|removeEventListener (CameClusterEventListener listener)
name|void
name|removeEventListener
parameter_list|(
name|CameClusterEventListener
name|listener
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

