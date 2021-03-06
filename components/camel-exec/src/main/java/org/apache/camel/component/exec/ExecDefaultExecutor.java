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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|CommandLine
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

begin_class
DECL|class|ExecDefaultExecutor
specifier|public
class|class
name|ExecDefaultExecutor
extends|extends
name|DefaultExecutor
block|{
DECL|field|process
specifier|private
specifier|transient
name|Process
name|process
decl_stmt|;
DECL|method|ExecDefaultExecutor ()
specifier|public
name|ExecDefaultExecutor
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|launch (CommandLine command, Map<String, String> env, File dir)
specifier|protected
name|Process
name|launch
parameter_list|(
name|CommandLine
name|command
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|env
parameter_list|,
name|File
name|dir
parameter_list|)
throws|throws
name|IOException
block|{
name|process
operator|=
name|super
operator|.
name|launch
argument_list|(
name|command
argument_list|,
name|env
argument_list|,
name|dir
argument_list|)
expr_stmt|;
return|return
name|process
return|;
block|}
DECL|method|getExitValue ()
specifier|public
name|int
name|getExitValue
parameter_list|()
block|{
if|if
condition|(
name|process
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|process
operator|.
name|exitValue
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IllegalThreadStateException
name|e
parameter_list|)
block|{
comment|// ignore the process is alive
block|}
block|}
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

