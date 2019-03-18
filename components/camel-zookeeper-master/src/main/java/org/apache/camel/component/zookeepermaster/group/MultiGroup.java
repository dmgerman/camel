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

begin_interface
DECL|interface|MultiGroup
specifier|public
interface|interface
name|MultiGroup
parameter_list|<
name|T
extends|extends
name|NodeState
parameter_list|>
extends|extends
name|Group
argument_list|<
name|T
argument_list|>
block|{
comment|/**      * Check if we are the master.      */
DECL|method|isMaster (String id)
name|boolean
name|isMaster
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

