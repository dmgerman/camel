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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
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
name|Produce
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
name|ProducerTemplate
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
name|OS
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
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
name|component
operator|.
name|exec
operator|.
name|ExecBinding
operator|.
name|EXEC_COMMAND_ARGS
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
name|component
operator|.
name|exec
operator|.
name|ExecBinding
operator|.
name|EXEC_COMMAND_EXECUTABLE
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
name|component
operator|.
name|exec
operator|.
name|ExecBinding
operator|.
name|EXEC_COMMAND_TIMEOUT
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
name|component
operator|.
name|exec
operator|.
name|ExecBinding
operator|.
name|EXEC_STDERR
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
name|component
operator|.
name|exec
operator|.
name|ExecEndpoint
operator|.
name|NO_TIMEOUT
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
name|component
operator|.
name|exec
operator|.
name|ExecTestUtils
operator|.
name|getClasspathResourceFileOrNull
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
name|component
operator|.
name|exec
operator|.
name|ExecutableJavaProgram
operator|.
name|PRINT_IN_STDOUT
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Test executing a OS script. Use only manually, see the TODO  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|ExecScriptTest
specifier|public
class|class
name|ExecScriptTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:input"
argument_list|)
DECL|field|producerTemplate
specifier|private
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
comment|/**      * TODO<b>the test is ignored for now to prevent accidental build      * failures.</b> Java 1.5 does not offer a method to check if a file is      * executable there is only a canRead method, which is not enough to      * guarantee that the script can be executed.<br>      *       * @throws Exception      */
annotation|@
name|Test
annotation|@
name|DirtiesContext
annotation|@
name|Ignore
DECL|method|testExecuteScript ()
specifier|public
name|void
name|testExecuteScript
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|scriptFile
init|=
name|getExecScriptFileOrNull
argument_list|(
literal|"exec-test-script"
argument_list|)
decl_stmt|;
if|if
condition|(
name|scriptFile
operator|!=
literal|null
condition|)
block|{
name|String
name|classpathArg
init|=
name|getClasspathArg
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|executeScript
argument_list|(
name|scriptFile
argument_list|,
name|NO_TIMEOUT
argument_list|,
name|classpathArg
argument_list|,
name|PRINT_IN_STDOUT
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|String
name|out
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|err
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EXEC_STDERR
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|contains
argument_list|(
name|PRINT_IN_STDOUT
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|err
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|String
name|os
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
decl_stmt|;
name|logger
operator|.
name|warn
argument_list|(
literal|"Executing batch scripts is not tested on "
operator|+
name|os
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|executeScript (final File scriptFile, long timeout, String... args)
specifier|private
name|Exchange
name|executeScript
parameter_list|(
specifier|final
name|File
name|scriptFile
parameter_list|,
name|long
name|timeout
parameter_list|,
name|String
modifier|...
name|args
parameter_list|)
block|{
name|StringBuilder
name|argsBuilder
init|=
operator|new
name|StringBuilder
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
name|argsBuilder
operator|.
name|append
argument_list|(
name|arg
operator|+
literal|" "
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|whiteSpaceSeparatedArgs
init|=
name|argsBuilder
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
return|return
name|producerTemplate
operator|.
name|send
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|PRINT_IN_STDOUT
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EXEC_COMMAND_TIMEOUT
argument_list|,
name|NO_TIMEOUT
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EXEC_COMMAND_EXECUTABLE
argument_list|,
name|scriptFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EXEC_COMMAND_ARGS
argument_list|,
name|whiteSpaceSeparatedArgs
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|getClasspathArg ()
specifier|private
name|String
name|getClasspathArg
parameter_list|()
block|{
name|String
name|classpath
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.class.path"
argument_list|)
decl_stmt|;
if|if
condition|(
name|OS
operator|.
name|isFamilyWindows
argument_list|()
condition|)
block|{
comment|// On windows the ";" character is replaced by a space by the
comment|// command interpreter. Thus the classpath is split with the
comment|// ;-token. Therefore the classpath should be quoted with double
comment|// quotes
name|classpath
operator|=
literal|"\"\""
operator|+
name|classpath
operator|+
literal|"\"\""
expr_stmt|;
block|}
else|else
block|{
comment|// quote only once
name|classpath
operator|=
literal|"\""
operator|+
name|classpath
operator|+
literal|"\""
expr_stmt|;
block|}
return|return
name|classpath
return|;
block|}
DECL|method|getExecScriptFileOrNull (String scriptNameBase)
specifier|private
name|File
name|getExecScriptFileOrNull
parameter_list|(
name|String
name|scriptNameBase
parameter_list|)
block|{
name|String
name|resource
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|OS
operator|.
name|isFamilyWindows
argument_list|()
condition|)
block|{
name|resource
operator|=
name|scriptNameBase
operator|+
literal|".bat"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|OS
operator|.
name|isFamilyUnix
argument_list|()
condition|)
block|{
name|resource
operator|=
name|scriptNameBase
operator|+
literal|".sh"
expr_stmt|;
block|}
name|File
name|resourceFile
init|=
name|getClasspathResourceFileOrNull
argument_list|(
name|resource
argument_list|)
decl_stmt|;
comment|// TODO use canExecute here (available since java 1.6)
if|if
condition|(
name|resourceFile
operator|!=
literal|null
operator|&&
operator|!
name|resourceFile
operator|.
name|canRead
argument_list|()
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"The resource  "
operator|+
name|resourceFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" is not readable!"
argument_list|)
expr_stmt|;
comment|// it is not readable, do not try to execute it
return|return
literal|null
return|;
block|}
return|return
name|resourceFile
return|;
block|}
block|}
end_class

end_unit

