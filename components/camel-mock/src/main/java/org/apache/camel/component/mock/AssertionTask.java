begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mock
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mock
package|;
end_package

begin_comment
comment|/**  * Assertion task that supports fail fast mode by running the assertion asap on the n'th received message.  */
end_comment

begin_interface
DECL|interface|AssertionTask
specifier|public
interface|interface
name|AssertionTask
extends|extends
name|Runnable
block|{
comment|/**      * Asserts on the n'th received message      *      * @param index the n'th received message      */
DECL|method|assertOnIndex (int index)
name|void
name|assertOnIndex
parameter_list|(
name|int
name|index
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

