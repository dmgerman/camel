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

begin_comment
comment|/**  * Callback interface used to get notifications of changes to a cluster group.  */
end_comment

begin_interface
DECL|interface|GroupListener
specifier|public
interface|interface
name|GroupListener
parameter_list|<
name|T
extends|extends
name|NodeState
parameter_list|>
block|{
DECL|enum|GroupEvent
enum|enum
name|GroupEvent
block|{
DECL|enumConstant|CONNECTED
name|CONNECTED
block|,
DECL|enumConstant|CHANGED
name|CHANGED
block|,
DECL|enumConstant|DISCONNECTED
name|DISCONNECTED
block|}
DECL|method|groupEvent (Group<T> group, GroupEvent event)
name|void
name|groupEvent
parameter_list|(
name|Group
argument_list|<
name|T
argument_list|>
name|group
parameter_list|,
name|GroupEvent
name|event
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

