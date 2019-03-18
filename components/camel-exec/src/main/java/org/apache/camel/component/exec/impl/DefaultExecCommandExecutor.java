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
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|ExecCommandExecutor
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
name|camel
operator|.
name|component
operator|.
name|exec
operator|.
name|ExecEndpoint
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
name|ExecException
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
name|ExecResult
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
name|ExecuteException
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
name|ExecuteWatchdog
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
name|PumpStreamHandler
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
comment|/**  * Executes the command utilizing the<a  * href="http://commons.apache.org/exec/">Apache Commons exec library</a>. Adds  * a shutdown hook for every executed process.  */
end_comment

begin_class
DECL|class|DefaultExecCommandExecutor
specifier|public
class|class
name|DefaultExecCommandExecutor
implements|implements
name|ExecCommandExecutor
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DefaultExecCommandExecutor
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|execute (ExecCommand command)
specifier|public
name|ExecResult
name|execute
parameter_list|(
name|ExecCommand
name|command
parameter_list|)
block|{
name|notNull
argument_list|(
name|command
argument_list|,
literal|"command"
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|err
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|DefaultExecutor
name|executor
init|=
name|prepareDefaultExecutor
argument_list|(
name|command
argument_list|)
decl_stmt|;
comment|// handle error and output of the process and write them to the given
comment|// out stream
name|PumpStreamHandler
name|handler
init|=
operator|new
name|PumpStreamHandler
argument_list|(
name|out
argument_list|,
name|err
argument_list|,
name|command
operator|.
name|getInput
argument_list|()
argument_list|)
decl_stmt|;
name|executor
operator|.
name|setStreamHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|CommandLine
name|cl
init|=
name|toCommandLine
argument_list|(
name|command
argument_list|)
decl_stmt|;
try|try
block|{
name|int
name|exitValue
init|=
name|executor
operator|.
name|execute
argument_list|(
name|cl
argument_list|)
decl_stmt|;
comment|// if the size is zero, we have no output, so construct the result
comment|// with null (required by ExecResult)
name|InputStream
name|stdout
init|=
name|out
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|?
literal|null
else|:
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|InputStream
name|stderr
init|=
name|err
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|?
literal|null
else|:
operator|new
name|ByteArrayInputStream
argument_list|(
name|err
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|ExecResult
name|result
init|=
operator|new
name|ExecResult
argument_list|(
name|command
argument_list|,
name|stdout
argument_list|,
name|stderr
argument_list|,
name|exitValue
argument_list|)
decl_stmt|;
return|return
name|result
return|;
block|}
catch|catch
parameter_list|(
name|ExecuteException
name|ee
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"ExecException while executing command: "
operator|+
name|command
operator|.
name|toString
argument_list|()
operator|+
literal|" - "
operator|+
name|ee
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|stdout
init|=
name|out
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|?
literal|null
else|:
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|InputStream
name|stderr
init|=
name|err
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|?
literal|null
else|:
operator|new
name|ByteArrayInputStream
argument_list|(
name|err
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ExecException
argument_list|(
literal|"Failed to execute command "
operator|+
name|command
argument_list|,
name|stdout
argument_list|,
name|stderr
argument_list|,
name|ee
operator|.
name|getExitValue
argument_list|()
argument_list|,
name|ee
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|InputStream
name|stdout
init|=
name|out
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|?
literal|null
else|:
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|InputStream
name|stderr
init|=
name|err
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|?
literal|null
else|:
operator|new
name|ByteArrayInputStream
argument_list|(
name|err
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|exitValue
init|=
literal|0
decl_stmt|;
comment|// use 0 as exit value as the executor didn't return the value
if|if
condition|(
name|executor
operator|instanceof
name|ExecDefaultExecutor
condition|)
block|{
comment|// get the exit value from the executor as it captures this to work around the common-exec bug
name|exitValue
operator|=
operator|(
operator|(
name|ExecDefaultExecutor
operator|)
name|executor
operator|)
operator|.
name|getExitValue
argument_list|()
expr_stmt|;
block|}
comment|// workaround to ignore if the stream was already closes due some race condition in commons-exec
name|String
name|msg
init|=
name|ioe
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|!=
literal|null
operator|&&
literal|"stream closed"
operator|.
name|equals
argument_list|(
name|msg
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Ignoring Stream closed IOException"
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
name|ExecResult
name|result
init|=
operator|new
name|ExecResult
argument_list|(
name|command
argument_list|,
name|stdout
argument_list|,
name|stderr
argument_list|,
name|exitValue
argument_list|)
decl_stmt|;
return|return
name|result
return|;
block|}
comment|// invalid working dir
name|LOG
operator|.
name|error
argument_list|(
literal|"IOException while executing command: "
operator|+
name|command
operator|.
name|toString
argument_list|()
operator|+
literal|" - "
operator|+
name|ioe
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ExecException
argument_list|(
literal|"Unable to execute command "
operator|+
name|command
argument_list|,
name|stdout
argument_list|,
name|stderr
argument_list|,
name|exitValue
argument_list|,
name|ioe
argument_list|)
throw|;
block|}
finally|finally
block|{
comment|// the inputStream must be closed after the execution
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|command
operator|.
name|getInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
name|ExecDefaultExecutor
argument_list|()
decl_stmt|;
name|executor
operator|.
name|setExitValues
argument_list|(
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|execCommand
operator|.
name|getWorkingDir
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|executor
operator|.
name|setWorkingDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|execCommand
operator|.
name|getWorkingDir
argument_list|()
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|execCommand
operator|.
name|getTimeout
argument_list|()
operator|!=
name|ExecEndpoint
operator|.
name|NO_TIMEOUT
condition|)
block|{
name|executor
operator|.
name|setWatchdog
argument_list|(
operator|new
name|ExecuteWatchdog
argument_list|(
name|execCommand
operator|.
name|getTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Transforms an {@link ExecCommand} to a {@link CommandLine}. No quoting fo      * the arguments is used.      *      * @param execCommand a not-null<code>ExecCommand</code> instance.      * @return a {@link CommandLine} object.      */
DECL|method|toCommandLine (ExecCommand execCommand)
specifier|protected
name|CommandLine
name|toCommandLine
parameter_list|(
name|ExecCommand
name|execCommand
parameter_list|)
block|{
name|notNull
argument_list|(
name|execCommand
argument_list|,
literal|"execCommand"
argument_list|)
expr_stmt|;
name|CommandLine
name|cl
init|=
operator|new
name|CommandLine
argument_list|(
name|execCommand
operator|.
name|getExecutable
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|args
init|=
name|execCommand
operator|.
name|getArgs
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|arg
range|:
name|args
control|)
block|{
comment|// do not handle quoting here, it is already quoted
name|cl
operator|.
name|addArgument
argument_list|(
name|arg
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|cl
return|;
block|}
block|}
end_class

end_unit

