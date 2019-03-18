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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Represents the binding of input and output types of a  * {@link ExecCommandExecutor} to an {@link Exchange}. The input of the executor  * is an {@link ExecCommand} and the output is an {@link ExecResult}.  */
end_comment

begin_interface
DECL|interface|ExecBinding
specifier|public
interface|interface
name|ExecBinding
block|{
comment|/**      * The header value overrides the executable of the command, configured in      * the exec endpoint URI. As executable is considered the remaining of the      * {@link ExecEndpoint} URI;<br>      *<br>      * e.g. in the URI<i><code>exec:C:/Program Files/jdk/java.exe</code></i>,      *<code>C:/Program Files/jdk/java.exe<code> is the executable.      */
DECL|field|EXEC_COMMAND_EXECUTABLE
name|String
name|EXEC_COMMAND_EXECUTABLE
init|=
literal|"CamelExecCommandExecutable"
decl_stmt|;
comment|/**      * The header value overrides the existing command arguments in the      * {@link ExecEndpoint} URI. The arguments may be a      *<code>List<String></code>. In this case no parsing of the arguments is      * necessary.      *       * @see {@link #EXEC_COMMAND_EXECUTABLE}      */
DECL|field|EXEC_COMMAND_ARGS
name|String
name|EXEC_COMMAND_ARGS
init|=
literal|"CamelExecCommandArgs"
decl_stmt|;
comment|/**      * Specifies the file name of a file, created by the executable, that should      * be considered as output of the executable, e.g. a log file.      *       * @see ExecResultConverter#toInputStream(ExecResult)      */
DECL|field|EXEC_COMMAND_OUT_FILE
name|String
name|EXEC_COMMAND_OUT_FILE
init|=
literal|"CamelExecCommandOutFile"
decl_stmt|;
comment|/**      * Sets the working directory of the {@link #EXEC_COMMAND_EXECUTABLE}. The      * header value overrides any existing command in the endpoint URI. If this      * is not configured, the working directory of the current process will be      * used.      */
DECL|field|EXEC_COMMAND_WORKING_DIR
name|String
name|EXEC_COMMAND_WORKING_DIR
init|=
literal|"CamelExecCommandWorkingDir"
decl_stmt|;
comment|/**      * Specifies the amount of time, in milliseconds, after which the process of      * the executable should be terminated. The default value is      * {@link Long#MAX_VALUE}.      */
DECL|field|EXEC_COMMAND_TIMEOUT
name|String
name|EXEC_COMMAND_TIMEOUT
init|=
literal|"CamelExecCommandTimeout"
decl_stmt|;
comment|/**      * The value of this header is a {@link InputStream} with the standard error      * stream of the executable.      */
DECL|field|EXEC_STDERR
name|String
name|EXEC_STDERR
init|=
literal|"CamelExecStderr"
decl_stmt|;
comment|/**      * The value of this header is the exit value that is returned, after the      * execution. By convention a non-zero status exit value indicates abnormal      * termination.<br>      *<b>Note that the exit value is OS dependent.</b>      */
DECL|field|EXEC_EXIT_VALUE
name|String
name|EXEC_EXIT_VALUE
init|=
literal|"CamelExecExitValue"
decl_stmt|;
comment|/**      * The value of this header is a boolean which indicates whether or not      * to fallback and use stderr when stdout is empty.      */
DECL|field|EXEC_USE_STDERR_ON_EMPTY_STDOUT
name|String
name|EXEC_USE_STDERR_ON_EMPTY_STDOUT
init|=
literal|"CamelExecUseStderrOnEmptyStdout"
decl_stmt|;
comment|/**      * Creates a {@link ExecCommand} from the headers in the      *<code>exchange</code> and the settings of the<code>endpoint</code>.      *       * @param exchange a Camel {@link Exchange}      * @param endpoint an {@link ExecEndpoint} instance      * @return an {@link ExecCommand} object      * @see ExecCommandExecutor      */
DECL|method|readInput (Exchange exchange, ExecEndpoint endpoint)
name|ExecCommand
name|readInput
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|ExecEndpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Populates the exchange form the {@link ExecResult}.      *       * @param exchange a Camel {@link Exchange}, in which to write the      *<code>result</code>      * @param result the result of a command execution      * @see ExecCommandExecutor      */
DECL|method|writeOutput (Exchange exchange, ExecResult result)
name|void
name|writeOutput
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|ExecResult
name|result
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

