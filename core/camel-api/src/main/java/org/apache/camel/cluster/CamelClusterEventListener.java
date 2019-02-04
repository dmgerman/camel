begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cluster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cluster
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

begin_comment
comment|/**  * Marker interface for cluster events  */
end_comment

begin_interface
DECL|interface|CamelClusterEventListener
specifier|public
interface|interface
name|CamelClusterEventListener
block|{
DECL|interface|Leadership
interface|interface
name|Leadership
extends|extends
name|CamelClusterEventListener
block|{
comment|/**          * Notify a change in the leadership for a particular cluster.          *          * @param view the cluster view          * @param leader the optional new leader          */
DECL|method|leadershipChanged (CamelClusterView view, Optional<CamelClusterMember> leader)
name|void
name|leadershipChanged
parameter_list|(
name|CamelClusterView
name|view
parameter_list|,
name|Optional
argument_list|<
name|CamelClusterMember
argument_list|>
name|leader
parameter_list|)
function_decl|;
block|}
DECL|interface|Membership
interface|interface
name|Membership
extends|extends
name|CamelClusterEventListener
block|{
comment|/**          * Notify a change (addition) in the cluster composition.          *          * @param view the cluster view          * @param member the member that has been added          */
DECL|method|memberAdded (CamelClusterView view, CamelClusterMember member)
name|void
name|memberAdded
parameter_list|(
name|CamelClusterView
name|view
parameter_list|,
name|CamelClusterMember
name|member
parameter_list|)
function_decl|;
comment|/**          * Notify a change (removal) in the cluster composition.          *          * @param view the cluster view          * @param member the member that has been removed          */
DECL|method|memberRemoved (CamelClusterView view, CamelClusterMember member)
name|void
name|memberRemoved
parameter_list|(
name|CamelClusterView
name|view
parameter_list|,
name|CamelClusterMember
name|member
parameter_list|)
function_decl|;
block|}
block|}
end_interface

end_unit

