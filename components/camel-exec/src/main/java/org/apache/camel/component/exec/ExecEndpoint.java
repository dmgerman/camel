begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Processor
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
name|Producer
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
name|impl
operator|.
name|DefaultExecBinding
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
name|impl
operator|.
name|DefaultEndpoint
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The endpoint utilizes an {@link ExecCommandExecutor} to execute a system  * command when it receives message exchanges.  *  * @see ExecBinding  * @see ExecCommandExecutor  * @see ExecCommand  * @see ExecResult  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"exec"
argument_list|,
name|title
operator|=
literal|"Exec"
argument_list|,
name|syntax
operator|=
literal|"exec:executable"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"system"
argument_list|)
DECL|class|ExecEndpoint
specifier|public
class|class
name|ExecEndpoint
extends|extends
name|DefaultEndpoint
block|{
comment|/**      * Indicates that no {@link #timeout} is used.      */
DECL|field|NO_TIMEOUT
specifier|public
specifier|static
specifier|final
name|long
name|NO_TIMEOUT
init|=
name|Long
operator|.
name|MAX_VALUE
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|executable
specifier|private
name|String
name|executable
decl_stmt|;
annotation|@
name|UriParam
DECL|field|args
specifier|private
name|String
name|args
decl_stmt|;
annotation|@
name|UriParam
DECL|field|workingDir
specifier|private
name|String
name|workingDir
decl_stmt|;
annotation|@
name|UriParam
DECL|field|timeout
specifier|private
name|long
name|timeout
decl_stmt|;
annotation|@
name|UriParam
DECL|field|outFile
specifier|private
name|String
name|outFile
decl_stmt|;
annotation|@
name|UriParam
DECL|field|commandExecutor
specifier|private
name|ExecCommandExecutor
name|commandExecutor
decl_stmt|;
annotation|@
name|UriParam
DECL|field|binding
specifier|private
name|ExecBinding
name|binding
decl_stmt|;
annotation|@
name|UriParam
DECL|field|useStderrOnEmptyStdout
specifier|private
name|boolean
name|useStderrOnEmptyStdout
decl_stmt|;
DECL|method|ExecEndpoint (String uri, ExecComponent component)
specifier|public
name|ExecEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|ExecComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|NO_TIMEOUT
expr_stmt|;
name|this
operator|.
name|binding
operator|=
operator|new
name|DefaultExecBinding
argument_list|()
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|ExecProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Consumer not supported for ExecEndpoint!"
argument_list|)
throw|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getExecutable ()
specifier|public
name|String
name|getExecutable
parameter_list|()
block|{
return|return
name|executable
return|;
block|}
comment|/**      * Sets the executable to be executed. The executable must not be empty or      *<code>null</code>.      */
DECL|method|setExecutable (String executable)
specifier|public
name|void
name|setExecutable
parameter_list|(
name|String
name|executable
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|executable
argument_list|,
literal|"executable"
argument_list|)
expr_stmt|;
name|this
operator|.
name|executable
operator|=
name|executable
expr_stmt|;
block|}
DECL|method|getArgs ()
specifier|public
name|String
name|getArgs
parameter_list|()
block|{
return|return
name|args
return|;
block|}
comment|/**      * The arguments may be one or many whitespace-separated tokens.      */
DECL|method|setArgs (String args)
specifier|public
name|void
name|setArgs
parameter_list|(
name|String
name|args
parameter_list|)
block|{
name|this
operator|.
name|args
operator|=
name|args
expr_stmt|;
block|}
DECL|method|getWorkingDir ()
specifier|public
name|String
name|getWorkingDir
parameter_list|()
block|{
return|return
name|workingDir
return|;
block|}
comment|/**      * The directory in which the command should be executed. If null, the working directory of the current process will be used.      */
DECL|method|setWorkingDir (String dir)
specifier|public
name|void
name|setWorkingDir
parameter_list|(
name|String
name|dir
parameter_list|)
block|{
name|this
operator|.
name|workingDir
operator|=
name|dir
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
comment|/**      * The timeout, in milliseconds, after which the executable should be terminated. If execution has not completed within the timeout, the component will send a termination request.      */
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
if|if
condition|(
name|timeout
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The timeout must be a positive long!"
argument_list|)
throw|;
block|}
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getOutFile ()
specifier|public
name|String
name|getOutFile
parameter_list|()
block|{
return|return
name|outFile
return|;
block|}
comment|/**      * The name of a file, created by the executable, that should be considered as its output.      * If no outFile is set, the standard output (stdout) of the executable will be used instead.      */
DECL|method|setOutFile (String outFile)
specifier|public
name|void
name|setOutFile
parameter_list|(
name|String
name|outFile
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|outFile
argument_list|,
literal|"outFile"
argument_list|)
expr_stmt|;
name|this
operator|.
name|outFile
operator|=
name|outFile
expr_stmt|;
block|}
DECL|method|getCommandExecutor ()
specifier|public
name|ExecCommandExecutor
name|getCommandExecutor
parameter_list|()
block|{
return|return
name|commandExecutor
return|;
block|}
comment|/**      * A reference to a org.apache.commons.exec.ExecCommandExecutor in the Registry that customizes the command execution.      * The default command executor utilizes the commons-exec library, which adds a shutdown hook for every executed command.      */
DECL|method|setCommandExecutor (ExecCommandExecutor commandExecutor)
specifier|public
name|void
name|setCommandExecutor
parameter_list|(
name|ExecCommandExecutor
name|commandExecutor
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|commandExecutor
argument_list|,
literal|"commandExecutor"
argument_list|)
expr_stmt|;
name|this
operator|.
name|commandExecutor
operator|=
name|commandExecutor
expr_stmt|;
block|}
DECL|method|getBinding ()
specifier|public
name|ExecBinding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
comment|/**      * A reference to a org.apache.commons.exec.ExecBinding in the Registry.      */
DECL|method|setBinding (ExecBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|ExecBinding
name|binding
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|binding
argument_list|,
literal|"binding"
argument_list|)
expr_stmt|;
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|isUseStderrOnEmptyStdout ()
specifier|public
name|boolean
name|isUseStderrOnEmptyStdout
parameter_list|()
block|{
return|return
name|useStderrOnEmptyStdout
return|;
block|}
comment|/**      * A boolean indicating that when stdout is empty, this component will populate the Camel Message Body with stderr. This behavior is disabled (false) by default.      */
DECL|method|setUseStderrOnEmptyStdout (boolean useStderrOnEmptyStdout)
specifier|public
name|void
name|setUseStderrOnEmptyStdout
parameter_list|(
name|boolean
name|useStderrOnEmptyStdout
parameter_list|)
block|{
name|this
operator|.
name|useStderrOnEmptyStdout
operator|=
name|useStderrOnEmptyStdout
expr_stmt|;
block|}
block|}
end_class

end_unit

