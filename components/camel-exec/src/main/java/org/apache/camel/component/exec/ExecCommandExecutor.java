begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.exec
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|exec
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Executes {@link ExecCommand} instances.  */
end_comment

begin_interface
DECL|interface|ExecCommandExecutor
specifier|public
interface|interface
name|ExecCommandExecutor
block|{
comment|/**      * Executes the<code>command</code> and returns a not-<code>null</code>      * {@link ExecResult} instance.      *       * @param execCommand The command object, that describes the executable application      * @return the result      * @throws ExecException if the execution failed      * @throws IOException if there is an invalid path in the working directory,      *             or if the absolute path of the command executable is invalid,      *             or if the executable does not exist      */
DECL|method|execute (ExecCommand execCommand)
name|ExecResult
name|execute
parameter_list|(
name|ExecCommand
name|execCommand
parameter_list|)
throws|throws
name|ExecException
throws|,
name|IOException
function_decl|;
block|}
end_interface

end_unit

