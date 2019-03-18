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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_comment
comment|/**  * Value object, that represents the result of an {@link ExecCommand} execution.  */
end_comment

begin_class
DECL|class|ExecResult
specifier|public
class|class
name|ExecResult
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|2238558080056724637L
decl_stmt|;
DECL|field|command
specifier|private
specifier|final
name|ExecCommand
name|command
decl_stmt|;
DECL|field|exitValue
specifier|private
specifier|final
name|int
name|exitValue
decl_stmt|;
DECL|field|stdout
specifier|private
specifier|final
name|InputStream
name|stdout
decl_stmt|;
DECL|field|stderr
specifier|private
specifier|final
name|InputStream
name|stderr
decl_stmt|;
comment|/**      * Creates a<code>ExecResult</code> instance.      *       * @param command A not-null reference of {@link ExecCommand}, that produced      *            the result.      * @param stdout InputStream with the stdout of the command executable. If      *            there was no stdout, the value must be<code>null</code>.      * @param stderr InputStream with the stderr of the command executable. If      *            there was no stderr, the value must be<code>null</code>.      * @param exitValue the exit value of the command executable.      */
DECL|method|ExecResult (ExecCommand command, InputStream stdout, InputStream stderr, int exitValue)
specifier|public
name|ExecResult
parameter_list|(
name|ExecCommand
name|command
parameter_list|,
name|InputStream
name|stdout
parameter_list|,
name|InputStream
name|stderr
parameter_list|,
name|int
name|exitValue
parameter_list|)
block|{
name|notNull
argument_list|(
name|command
argument_list|,
literal|"command"
argument_list|)
expr_stmt|;
name|this
operator|.
name|command
operator|=
name|command
expr_stmt|;
name|this
operator|.
name|stdout
operator|=
name|stdout
expr_stmt|;
name|this
operator|.
name|stderr
operator|=
name|stderr
expr_stmt|;
name|this
operator|.
name|exitValue
operator|=
name|exitValue
expr_stmt|;
block|}
comment|/**      * The executed command, that produced this result. The returned object is      * never<code>null</code>.      *       * @return The executed command, that produced this result.      */
DECL|method|getCommand ()
specifier|public
name|ExecCommand
name|getCommand
parameter_list|()
block|{
return|return
name|command
return|;
block|}
comment|/**      * The exit value of the command executable.      *       * @return The exit value of the command executable      */
DECL|method|getExitValue ()
specifier|public
name|int
name|getExitValue
parameter_list|()
block|{
return|return
name|exitValue
return|;
block|}
comment|/**      * Returns the content of the standart output (stdout) of the executed      * command or<code>null</code>, if no output was produced in the stdout.      *       * @return The standart output (stdout) of the command executable.      */
DECL|method|getStdout ()
specifier|public
name|InputStream
name|getStdout
parameter_list|()
block|{
return|return
name|stdout
return|;
block|}
comment|/**      * Returns the content of the standart error output (stderr) of the executed      * command or<code>null</code>, if no output was produced in the stderr.      *       * @return The standart error output (stderr) of the command executable.      */
DECL|method|getStderr ()
specifier|public
name|InputStream
name|getStderr
parameter_list|()
block|{
return|return
name|stderr
return|;
block|}
block|}
end_class

end_unit

