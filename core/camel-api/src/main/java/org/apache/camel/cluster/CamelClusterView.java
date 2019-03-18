begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**      * Provides the leader member if elected.      *      * @return the leader member.      */
DECL|method|getLeader ()
name|Optional
argument_list|<
name|CamelClusterMember
argument_list|>
name|getLeader
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
DECL|method|addEventListener (CamelClusterEventListener listener)
name|void
name|addEventListener
parameter_list|(
name|CamelClusterEventListener
name|listener
parameter_list|)
function_decl|;
comment|/**      * Remove the event listener.      *      * @param listener the event listener.      */
DECL|method|removeEventListener (CamelClusterEventListener listener)
name|void
name|removeEventListener
parameter_list|(
name|CamelClusterEventListener
name|listener
parameter_list|)
function_decl|;
comment|/**      * Access the underlying concrete CamelClusterView implementation to      * provide access to further features.      *      * @param clazz the proprietary class or interface of the underlying concrete CamelClusterView.      * @return an instance of the underlying concrete CamelClusterView as the required type.      */
DECL|method|unwrap (Class<T> clazz)
specifier|default
parameter_list|<
name|T
extends|extends
name|CamelClusterView
parameter_list|>
name|T
name|unwrap
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|CamelClusterView
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|this
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to unwrap this CamelClusterView type ("
operator|+
name|getClass
argument_list|()
operator|+
literal|") to the required type ("
operator|+
name|clazz
operator|+
literal|")"
argument_list|)
throw|;
block|}
block|}
end_interface

end_unit

