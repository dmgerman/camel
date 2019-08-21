begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

begin_comment
comment|/**  * Task or command being executed when using the advice with feature.  */
end_comment

begin_interface
DECL|interface|AdviceWithTask
specifier|public
interface|interface
name|AdviceWithTask
block|{
comment|/**      * The task to execute      *      * @throws Exception is thrown if error during executing the task, or      *             invalid input.      */
DECL|method|task ()
name|void
name|task
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

