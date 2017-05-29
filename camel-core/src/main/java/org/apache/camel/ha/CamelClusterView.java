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
name|function
operator|.
name|BiConsumer
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
name|Predicate
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
block|{
DECL|enum|Event
enum|enum
name|Event
block|{
DECL|enumConstant|KEEP_ALIVE
name|KEEP_ALIVE
block|,
DECL|enumConstant|LEADERSHIP_CHANGED
name|LEADERSHIP_CHANGED
block|;     }
comment|/**      * @return the cluster.      */
DECL|method|getCluster ()
name|CamelCluster
name|getCluster
parameter_list|()
function_decl|;
comment|/**      * @return the namespace for this view.      */
DECL|method|getNamespace ()
name|String
name|getNamespace
parameter_list|()
function_decl|;
comment|/**      * Provides the master member.      *      * @return the master member.      */
DECL|method|getMaster ()
name|CamelClusterMember
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
comment|/**      * Add an event consumer.      *      * @param consumer the event consumer.      */
DECL|method|addEventListener (BiConsumer<Event, Object> consumer)
name|void
name|addEventListener
parameter_list|(
name|BiConsumer
argument_list|<
name|Event
argument_list|,
name|Object
argument_list|>
name|consumer
parameter_list|)
function_decl|;
comment|/**      * Add an event consumer for events matching the given predicate.      *      * @param predicate the predicate to filter events.      * @param consumer the event consumer.      */
DECL|method|addEventListener (Predicate<Event> predicate, BiConsumer<Event, Object> consumer)
name|void
name|addEventListener
parameter_list|(
name|Predicate
argument_list|<
name|Event
argument_list|>
name|predicate
parameter_list|,
name|BiConsumer
argument_list|<
name|Event
argument_list|,
name|Object
argument_list|>
name|consumer
parameter_list|)
function_decl|;
comment|/**      * Remove the event consumer.      *      * @param event the event consumer.      */
DECL|method|removeEventListener (BiConsumer<Event, Object> event)
name|void
name|removeEventListener
parameter_list|(
name|BiConsumer
argument_list|<
name|Event
argument_list|,
name|Object
argument_list|>
name|event
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

