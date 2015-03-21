begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
package|;
end_package

begin_comment
comment|/**  * A controller which allows controlling a {@link org.apache.camel.processor.aggregate.AggregateProcessor} from an  * external source, such as Java API or JMX. This can be used to force completion of aggregated groups, and more.  */
end_comment

begin_interface
DECL|interface|AggregateController
specifier|public
interface|interface
name|AggregateController
block|{
comment|/**      * Callback when the aggregate processor is started.      *      * @param processor the aggregate processor      */
DECL|method|onStart (AggregateProcessor processor)
name|void
name|onStart
parameter_list|(
name|AggregateProcessor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Callback when the aggregate processor is stopped.      *      * @param processor the aggregate processor      */
DECL|method|onStop (AggregateProcessor processor)
name|void
name|onStop
parameter_list|(
name|AggregateProcessor
name|processor
parameter_list|)
function_decl|;
comment|/**      * To force completing a specific group by its key.      *      * @param key the key      * @return<tt>1</tt> if the group was forced completed,<tt>0</tt> if the group does not exists      */
DECL|method|forceCompletionOfGroup (String key)
name|int
name|forceCompletionOfGroup
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * To force complete of all groups      *      * @return number of groups that was forced completed      */
DECL|method|forceCompletionOfAllGroups ()
name|int
name|forceCompletionOfAllGroups
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

