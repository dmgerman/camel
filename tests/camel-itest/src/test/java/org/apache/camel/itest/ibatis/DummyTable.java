begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.ibatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|ibatis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_interface
DECL|interface|DummyTable
specifier|public
interface|interface
name|DummyTable
extends|extends
name|Iterable
argument_list|<
name|Integer
argument_list|>
block|{
DECL|method|create ()
name|void
name|create
parameter_list|()
function_decl|;
DECL|method|add (int value)
name|void
name|add
parameter_list|(
name|int
name|value
parameter_list|)
function_decl|;
DECL|method|clear ()
name|void
name|clear
parameter_list|()
function_decl|;
DECL|method|drop ()
name|void
name|drop
parameter_list|()
function_decl|;
DECL|method|values ()
name|Collection
argument_list|<
name|Integer
argument_list|>
name|values
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

