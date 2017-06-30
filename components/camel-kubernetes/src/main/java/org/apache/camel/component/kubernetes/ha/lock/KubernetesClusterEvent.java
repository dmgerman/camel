begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.ha.lock
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kubernetes
operator|.
name|ha
operator|.
name|lock
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Super interface for events produced by the Kubernetes cluster.  */
end_comment

begin_interface
annotation|@
name|FunctionalInterface
DECL|interface|KubernetesClusterEvent
specifier|public
interface|interface
name|KubernetesClusterEvent
block|{
DECL|method|getData ()
name|Object
name|getData
parameter_list|()
function_decl|;
comment|/**      * Event signalling that the list of members of the Kubernetes cluster has changed.      */
DECL|interface|KubernetesClusterMemberListChangedEvent
interface|interface
name|KubernetesClusterMemberListChangedEvent
extends|extends
name|KubernetesClusterEvent
block|{
annotation|@
name|Override
DECL|method|getData ()
name|Set
argument_list|<
name|String
argument_list|>
name|getData
parameter_list|()
function_decl|;
block|}
comment|/**      * Event signalling the presence of a new leader.      */
DECL|interface|KubernetesClusterLeaderChangedEvent
interface|interface
name|KubernetesClusterLeaderChangedEvent
extends|extends
name|KubernetesClusterEvent
block|{
annotation|@
name|Override
DECL|method|getData ()
name|Optional
argument_list|<
name|String
argument_list|>
name|getData
parameter_list|()
function_decl|;
block|}
block|}
end_interface

end_unit

