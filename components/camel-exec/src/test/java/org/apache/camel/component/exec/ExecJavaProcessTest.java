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
name|ArrayList
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|EndpointInject
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
name|camel
operator|.
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
name|converter
operator|.
name|IOConverter
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|junit
operator|.
name|Test
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
name|EXEC_COMMAND_WORKING_DIR
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
name|EXEC_EXIT_VALUE
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
name|ExecBinding
operator|.
name|EXEC_USE_STDERR_ON_EMPTY_STDOUT
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
name|buildJavaExecutablePath
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
name|EXIT_WITH_VALUE_0
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
name|EXIT_WITH_VALUE_1
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
name|PRINT_ARGS_STDOUT
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
name|PRINT_IN_STDERR
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
name|READ_INPUT_LINES_AND_PRINT_THEM
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
name|SLEEP_WITH_TIMEOUT
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
name|THREADS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
operator|.
name|LINE_SEPARATOR
import|;
end_import

begin_comment
comment|/**  * Tests the functionality of the {@link ExecComponent}, executing<br>  *<i>java org.apache.camel.component.exec.ExecutableJavaProgram</i><br>  * command.<b>Note, that the tests assume, that the JAVA_HOME system variable  * is set.</b> This is a more credible assumption, than assuming that java is in  * the path, because the Maven scripts build the path to java with the JAVA_HOME  * environment variable.  *   * @see {@link ExecutableJavaProgram}  */
end_comment

begin_class
DECL|class|ExecJavaProcessTest
specifier|public
class|class
name|ExecJavaProcessTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|EXECUTABLE_PROGRAM_ARG
specifier|private
specifier|static
specifier|final
name|String
name|EXECUTABLE_PROGRAM_ARG
init|=
name|ExecutableJavaProgram
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
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
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:output"
argument_list|)
DECL|field|output
specifier|private
name|MockEndpoint
name|output
decl_stmt|;
annotation|@
name|Test
DECL|method|testExecJavaProcessExitCode0 ()
specifier|public
name|void
name|testExecJavaProcessExitCode0
parameter_list|()
throws|throws
name|Exception
block|{
name|output
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|output
operator|.
name|expectedHeaderReceived
argument_list|(
name|EXEC_EXIT_VALUE
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|sendExchange
argument_list|(
name|EXIT_WITH_VALUE_0
argument_list|,
name|NO_TIMEOUT
argument_list|)
expr_stmt|;
name|output
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExecJavaProcessExitCode1 ()
specifier|public
name|void
name|testExecJavaProcessExitCode1
parameter_list|()
throws|throws
name|Exception
block|{
name|output
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|output
operator|.
name|expectedHeaderReceived
argument_list|(
name|EXEC_EXIT_VALUE
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|sendExchange
argument_list|(
name|EXIT_WITH_VALUE_1
argument_list|,
name|NO_TIMEOUT
argument_list|)
expr_stmt|;
name|output
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExecJavaProcessStdout ()
specifier|public
name|void
name|testExecJavaProcessStdout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commandArgument
init|=
name|PRINT_IN_STDOUT
decl_stmt|;
name|output
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|output
operator|.
name|expectedHeaderReceived
argument_list|(
name|EXEC_EXIT_VALUE
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|Exchange
name|e
init|=
name|sendExchange
argument_list|(
name|commandArgument
argument_list|,
name|NO_TIMEOUT
argument_list|)
decl_stmt|;
name|ExecResult
name|inBody
init|=
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ExecResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|output
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PRINT_IN_STDOUT
argument_list|,
name|IOUtils
operator|.
name|toString
argument_list|(
name|inBody
operator|.
name|getStdout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertResultToString ()
specifier|public
name|void
name|testConvertResultToString
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commandArgument
init|=
name|PRINT_IN_STDOUT
decl_stmt|;
name|output
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|e
init|=
name|sendExchange
argument_list|(
name|commandArgument
argument_list|,
name|NO_TIMEOUT
argument_list|)
decl_stmt|;
name|output
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|String
name|out
init|=
name|e
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
name|assertEquals
argument_list|(
name|PRINT_IN_STDOUT
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testByteArrayInputStreamIsResetInConverter ()
specifier|public
name|void
name|testByteArrayInputStreamIsResetInConverter
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commandArgument
init|=
name|PRINT_IN_STDOUT
decl_stmt|;
name|output
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|e
init|=
name|sendExchange
argument_list|(
name|commandArgument
argument_list|,
name|NO_TIMEOUT
argument_list|)
decl_stmt|;
name|String
name|out1
init|=
name|e
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
comment|// the second conversion should not need a reset, this is handled
comment|// in the type converter.
name|String
name|out2
init|=
name|e
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
name|output
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PRINT_IN_STDOUT
argument_list|,
name|out1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|out1
argument_list|,
name|out2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIfStdoutIsNullStderrIsReturnedInConverter ()
specifier|public
name|void
name|testIfStdoutIsNullStderrIsReturnedInConverter
parameter_list|()
throws|throws
name|Exception
block|{
comment|// this will be printed
name|String
name|commandArgument
init|=
name|PRINT_IN_STDERR
decl_stmt|;
name|output
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|e
init|=
name|sendExchange
argument_list|(
name|commandArgument
argument_list|,
name|NO_TIMEOUT
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ExecResult
name|body
init|=
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ExecResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|output
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"the test executable must not print anything in stdout"
argument_list|,
name|body
operator|.
name|getStdout
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"the test executable must print in stderr"
argument_list|,
name|body
operator|.
name|getStderr
argument_list|()
argument_list|)
expr_stmt|;
comment|// the converter must fall back to the stderr, because stdout is null
name|String
name|stderr
init|=
name|e
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
name|assertEquals
argument_list|(
name|PRINT_IN_STDERR
argument_list|,
name|stderr
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStdoutIsNull ()
specifier|public
name|void
name|testStdoutIsNull
parameter_list|()
throws|throws
name|Exception
block|{
comment|// this will be printed
name|String
name|commandArgument
init|=
name|PRINT_IN_STDERR
decl_stmt|;
name|output
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|e
init|=
name|sendExchange
argument_list|(
name|commandArgument
argument_list|,
name|NO_TIMEOUT
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ExecResult
name|body
init|=
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ExecResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|output
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"the test executable must not print anything in stdout"
argument_list|,
name|body
operator|.
name|getStdout
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"the test executable must print in stderr"
argument_list|,
name|body
operator|.
name|getStderr
argument_list|()
argument_list|)
expr_stmt|;
comment|// the converter must fall back to the stderr, because stdout is null
name|String
name|out
init|=
name|e
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
name|assertNull
argument_list|(
literal|"Should be null"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertResultToInputStream ()
specifier|public
name|void
name|testConvertResultToInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commandArgument
init|=
name|PRINT_IN_STDOUT
decl_stmt|;
name|output
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|e
init|=
name|sendExchange
argument_list|(
name|commandArgument
argument_list|,
name|NO_TIMEOUT
argument_list|)
decl_stmt|;
name|output
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|InputStream
name|out
init|=
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PRINT_IN_STDOUT
argument_list|,
name|IOUtils
operator|.
name|toString
argument_list|(
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertResultToByteArray ()
specifier|public
name|void
name|testConvertResultToByteArray
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commandArgument
init|=
name|PRINT_IN_STDOUT
decl_stmt|;
name|output
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|e
init|=
name|sendExchange
argument_list|(
name|commandArgument
argument_list|,
name|NO_TIMEOUT
argument_list|)
decl_stmt|;
name|output
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|byte
index|[]
name|out
init|=
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PRINT_IN_STDOUT
argument_list|,
operator|new
name|String
argument_list|(
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvalidWorkingDir ()
specifier|public
name|void
name|testInvalidWorkingDir
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commandArgument
init|=
name|PRINT_IN_STDOUT
decl_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|args
init|=
name|buildArgs
argument_list|(
name|commandArgument
argument_list|)
decl_stmt|;
specifier|final
name|String
name|javaAbsolutePath
init|=
name|buildJavaExecutablePath
argument_list|()
decl_stmt|;
name|Exchange
name|e
init|=
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
name|setHeader
argument_list|(
name|EXEC_COMMAND_EXECUTABLE
argument_list|,
name|javaAbsolutePath
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EXEC_COMMAND_WORKING_DIR
argument_list|,
literal|"\\cdd:///invalidWWorkginDir"
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
name|args
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ExecException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getException
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test print in stdout from threads.      */
annotation|@
name|Test
DECL|method|testExecJavaProcessThreads ()
specifier|public
name|void
name|testExecJavaProcessThreads
parameter_list|()
throws|throws
name|Exception
block|{
name|output
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|sendExchange
argument_list|(
name|THREADS
argument_list|,
name|NO_TIMEOUT
argument_list|)
decl_stmt|;
name|String
name|err
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EXEC_STDERR
argument_list|,
name|InputStream
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|ExecResult
name|result
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ExecResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
index|[]
name|outs
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
name|result
operator|.
name|getStdout
argument_list|()
argument_list|)
operator|.
name|split
argument_list|(
name|LINE_SEPARATOR
argument_list|)
decl_stmt|;
name|String
index|[]
name|errs
init|=
name|err
operator|.
name|split
argument_list|(
name|LINE_SEPARATOR
argument_list|)
decl_stmt|;
name|output
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|ExecutableJavaProgram
operator|.
name|LINES_TO_PRINT_FROM_EACH_THREAD
argument_list|,
name|outs
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ExecutableJavaProgram
operator|.
name|LINES_TO_PRINT_FROM_EACH_THREAD
argument_list|,
name|errs
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test print in stdout using string as args      */
annotation|@
name|Test
DECL|method|testExecJavaArgsAsString ()
specifier|public
name|void
name|testExecJavaArgsAsString
parameter_list|()
throws|throws
name|Exception
block|{
name|output
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|producerTemplate
operator|.
name|send
argument_list|(
literal|"direct:input"
argument_list|,
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
specifier|final
name|String
name|javaAbsolutePath
init|=
name|buildJavaExecutablePath
argument_list|()
decl_stmt|;
comment|// use string for args
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
name|String
name|args
init|=
literal|"-cp \""
operator|+
name|classpath
operator|+
literal|"\" "
operator|+
name|EXECUTABLE_PROGRAM_ARG
operator|+
literal|" "
operator|+
name|PRINT_IN_STDOUT
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"hello"
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
name|javaAbsolutePath
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
name|args
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EXEC_USE_STDERR_ON_EMPTY_STDOUT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|output
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|ExecResult
name|result
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ExecResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|IOConverter
operator|.
name|toString
argument_list|(
name|result
operator|.
name|getStdout
argument_list|()
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PRINT_IN_STDOUT
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test print in stdout using string as args with quotes      */
annotation|@
name|Test
DECL|method|testExecJavaArgsAsStringWithQuote ()
specifier|public
name|void
name|testExecJavaArgsAsStringWithQuote
parameter_list|()
throws|throws
name|Exception
block|{
name|output
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|producerTemplate
operator|.
name|send
argument_list|(
literal|"direct:input"
argument_list|,
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
specifier|final
name|String
name|javaAbsolutePath
init|=
name|buildJavaExecutablePath
argument_list|()
decl_stmt|;
comment|// use string for args
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
name|String
name|args
init|=
literal|"-cp \""
operator|+
name|classpath
operator|+
literal|"\" "
operator|+
name|EXECUTABLE_PROGRAM_ARG
operator|+
literal|" "
operator|+
name|PRINT_ARGS_STDOUT
operator|+
literal|" \"Hello World\""
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"hello"
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
name|javaAbsolutePath
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
name|args
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EXEC_USE_STDERR_ON_EMPTY_STDOUT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|output
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|ExecResult
name|result
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ExecResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|IOConverter
operator|.
name|toString
argument_list|(
name|result
operator|.
name|getStdout
argument_list|()
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|out
argument_list|,
name|out
operator|.
name|contains
argument_list|(
literal|"1Hello World"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test print in stdout using string as args with quotes      */
annotation|@
name|Test
DECL|method|testExecJavaArgsAsStringWithoutQuote ()
specifier|public
name|void
name|testExecJavaArgsAsStringWithoutQuote
parameter_list|()
throws|throws
name|Exception
block|{
name|output
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|producerTemplate
operator|.
name|send
argument_list|(
literal|"direct:input"
argument_list|,
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
specifier|final
name|String
name|javaAbsolutePath
init|=
name|buildJavaExecutablePath
argument_list|()
decl_stmt|;
comment|// use string for args
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
name|String
name|args
init|=
literal|"-cp \""
operator|+
name|classpath
operator|+
literal|"\" "
operator|+
name|EXECUTABLE_PROGRAM_ARG
operator|+
literal|" "
operator|+
name|PRINT_ARGS_STDOUT
operator|+
literal|" Hello World"
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"hello"
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
name|javaAbsolutePath
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
name|args
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EXEC_USE_STDERR_ON_EMPTY_STDOUT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|output
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|ExecResult
name|result
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ExecResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|String
name|out
init|=
name|IOConverter
operator|.
name|toString
argument_list|(
name|result
operator|.
name|getStdout
argument_list|()
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|out
argument_list|,
name|out
operator|.
name|contains
argument_list|(
literal|"1Hello"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
argument_list|,
name|out
operator|.
name|contains
argument_list|(
literal|"2World"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test if the process will be terminate in about a second      */
annotation|@
name|Test
DECL|method|testExecJavaProcessTimeout ()
specifier|public
name|void
name|testExecJavaProcessTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|killAfterMillis
init|=
literal|1000
decl_stmt|;
name|output
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// add some tolerance
name|output
operator|.
name|setMinimumResultWaitTime
argument_list|(
literal|800
argument_list|)
expr_stmt|;
comment|// max (the test program sleeps 60 000)
name|output
operator|.
name|setResultWaitTime
argument_list|(
literal|30000
argument_list|)
expr_stmt|;
name|sendExchange
argument_list|(
name|SLEEP_WITH_TIMEOUT
argument_list|,
name|killAfterMillis
argument_list|)
expr_stmt|;
name|output
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test reading of input lines from the executable's stdin      */
annotation|@
name|Test
DECL|method|testExecJavaProcessInputLines ()
specifier|public
name|void
name|testExecJavaProcessInputLines
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|lines
init|=
literal|10
decl_stmt|;
for|for
control|(
name|int
name|t
init|=
literal|1
init|;
name|t
operator|<
name|lines
condition|;
name|t
operator|++
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"Line"
operator|+
name|t
operator|+
name|LINE_SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|String
name|whiteSpaceSeparatedLines
init|=
name|builder
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|expected
init|=
name|builder
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Exchange
name|e
init|=
name|sendExchange
argument_list|(
name|READ_INPUT_LINES_AND_PRINT_THEM
argument_list|,
literal|20000
argument_list|,
name|whiteSpaceSeparatedLines
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ExecResult
name|inBody
init|=
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ExecResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|IOUtils
operator|.
name|toString
argument_list|(
name|inBody
operator|.
name|getStdout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|sendExchange (final Object commandArgument, final long timeout)
specifier|protected
name|Exchange
name|sendExchange
parameter_list|(
specifier|final
name|Object
name|commandArgument
parameter_list|,
specifier|final
name|long
name|timeout
parameter_list|)
block|{
return|return
name|sendExchange
argument_list|(
name|commandArgument
argument_list|,
name|timeout
argument_list|,
literal|"testBody"
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|sendExchange (final Object commandArgument, final long timeout, final String body, final boolean useStderrOnEmptyStdout)
specifier|protected
name|Exchange
name|sendExchange
parameter_list|(
specifier|final
name|Object
name|commandArgument
parameter_list|,
specifier|final
name|long
name|timeout
parameter_list|,
specifier|final
name|String
name|body
parameter_list|,
specifier|final
name|boolean
name|useStderrOnEmptyStdout
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|args
init|=
name|buildArgs
argument_list|(
name|commandArgument
argument_list|)
decl_stmt|;
specifier|final
name|String
name|javaAbsolutePath
init|=
name|buildJavaExecutablePath
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
name|body
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
name|javaAbsolutePath
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
name|timeout
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
name|args
argument_list|)
expr_stmt|;
if|if
condition|(
name|useStderrOnEmptyStdout
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|EXEC_USE_STDERR_ON_EMPTY_STDOUT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|buildArgs (Object commandArgument)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|buildArgs
parameter_list|(
name|Object
name|commandArgument
parameter_list|)
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
name|List
argument_list|<
name|String
argument_list|>
name|args
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|args
operator|.
name|add
argument_list|(
literal|"-cp"
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|classpath
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|EXECUTABLE_PROGRAM_ARG
argument_list|)
expr_stmt|;
name|args
operator|.
name|add
argument_list|(
name|commandArgument
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|args
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:input"
argument_list|)
operator|.
name|to
argument_list|(
literal|"exec:java"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:output"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

