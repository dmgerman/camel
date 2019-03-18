begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.exec.impl
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
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|exec
operator|.
name|ExecCommand
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
name|component
operator|.
name|exec
operator|.
name|ExecDefaultExecutor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|exec
operator|.
name|DefaultExecutor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|exec
operator|.
name|ShutdownHookProcessDestroyer
import|;
end_import

begin_comment
comment|/**  * Mock of {@link org.apache.camel.component.exec.ExecCommandExecutor} which provokes to throw an  * {@link org.apache.camel.component.exec.ExecException}  */
end_comment

begin_class
DECL|class|ProvokeExceptionExecCommandExecutor
specifier|public
class|class
name|ProvokeExceptionExecCommandExecutor
extends|extends
name|DefaultExecCommandExecutor
block|{
annotation|@
name|Override
DECL|method|prepareDefaultExecutor (ExecCommand execCommand)
specifier|protected
name|DefaultExecutor
name|prepareDefaultExecutor
parameter_list|(
name|ExecCommand
name|execCommand
parameter_list|)
block|{
name|DefaultExecutor
name|executor
init|=
operator|new
name|DefaultExecutorMock
argument_list|()
decl_stmt|;
name|executor
operator|.
name|setExitValues
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|executor
operator|.
name|setProcessDestroyer
argument_list|(
operator|new
name|ShutdownHookProcessDestroyer
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|executor
return|;
block|}
DECL|class|DefaultExecutorMock
specifier|public
class|class
name|DefaultExecutorMock
extends|extends
name|ExecDefaultExecutor
block|{
annotation|@
name|Override
DECL|method|isFailure (int exitValue)
specifier|public
name|boolean
name|isFailure
parameter_list|(
name|int
name|exitValue
parameter_list|)
block|{
comment|//provoke ExecuteException
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

