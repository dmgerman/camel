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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|ExchangePattern
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
name|component
operator|.
name|exec
operator|.
name|impl
operator|.
name|ExecCommandExecutorMock
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
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
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
name|EXEC_COMMAND_WORKING_DIR
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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

begin_comment
comment|/**  * Test the functionality of {@link ExecProducer}  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"exec-mock-executor-context.xml"
block|}
argument_list|)
DECL|class|ExecProducerTest
specifier|public
class|class
name|ExecProducerTest
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
annotation|@
name|Autowired
DECL|field|execCommandExecutorMock
specifier|private
name|ExecCommandExecutorMock
name|execCommandExecutorMock
decl_stmt|;
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testWithContextConfiguration ()
specifier|public
name|void
name|testWithContextConfiguration
parameter_list|()
block|{
name|producerTemplate
operator|.
name|sendBody
argument_list|(
literal|"direct:input"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
comment|// the expected string is defined in the route configuration
name|assertEquals
argument_list|(
literal|"mockedByCommandExecutorMock.exe"
argument_list|,
name|execCommandExecutorMock
operator|.
name|lastCommandResult
operator|.
name|getCommand
argument_list|()
operator|.
name|getExecutable
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testOverrideExecutable ()
specifier|public
name|void
name|testOverrideExecutable
parameter_list|()
block|{
specifier|final
name|String
name|command
init|=
literal|"java"
decl_stmt|;
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
literal|"noinput"
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
name|command
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|command
argument_list|,
name|execCommandExecutorMock
operator|.
name|lastCommandResult
operator|.
name|getCommand
argument_list|()
operator|.
name|getExecutable
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that the args are set literally.      */
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testOverrideArgs ()
specifier|public
name|void
name|testOverrideArgs
parameter_list|()
block|{
specifier|final
name|String
index|[]
name|args
init|=
block|{
literal|"-version"
block|,
literal|"classpath:c:/program files/test/"
block|}
decl_stmt|;
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
literal|"noinput"
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
name|Arrays
operator|.
name|asList
argument_list|(
name|args
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|commandArgs
init|=
name|execCommandExecutorMock
operator|.
name|lastCommandResult
operator|.
name|getCommand
argument_list|()
operator|.
name|getArgs
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|,
name|commandArgs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|args
index|[
literal|1
index|]
argument_list|,
name|commandArgs
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testOverrideTimeout ()
specifier|public
name|void
name|testOverrideTimeout
parameter_list|()
block|{
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
literal|"noinput"
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
literal|"1000"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1000
argument_list|,
name|execCommandExecutorMock
operator|.
name|lastCommandResult
operator|.
name|getCommand
argument_list|()
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testInputLines ()
specifier|public
name|void
name|testInputLines
parameter_list|()
throws|throws
name|IOException
block|{
comment|// String must be convertible to InputStream
specifier|final
name|String
name|input
init|=
literal|"line1"
operator|+
name|LINE_SEPARATOR
operator|+
literal|"line2"
decl_stmt|;
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
name|input
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|input
argument_list|,
name|IOUtils
operator|.
name|toString
argument_list|(
name|execCommandExecutorMock
operator|.
name|lastCommandResult
operator|.
name|getCommand
argument_list|()
operator|.
name|getInput
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testInputLinesNotConvertibleToInputStream ()
specifier|public
name|void
name|testInputLinesNotConvertibleToInputStream
parameter_list|()
throws|throws
name|IOException
block|{
comment|// String must be convertible to InputStream
specifier|final
name|Integer
name|notConvertibleToInputStreamBody
init|=
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
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
name|setBody
argument_list|(
name|notConvertibleToInputStreamBody
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|ExecResult
name|result
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
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|result
operator|.
name|getCommand
argument_list|()
operator|.
name|getInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testNullInBody ()
specifier|public
name|void
name|testNullInBody
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Null body must also be supported
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
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|ExecResult
name|result
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
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|result
operator|.
name|getCommand
argument_list|()
operator|.
name|getInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testOverrideWorkingDir ()
specifier|public
name|void
name|testOverrideWorkingDir
parameter_list|()
block|{
specifier|final
name|String
name|workingDir
init|=
literal|"c:/program files/test"
decl_stmt|;
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
literal|""
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
name|workingDir
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|workingDir
argument_list|,
name|execCommandExecutorMock
operator|.
name|lastCommandResult
operator|.
name|getCommand
argument_list|()
operator|.
name|getWorkingDir
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testInInOnlyExchange ()
specifier|public
name|void
name|testInInOnlyExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
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
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"inonly"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// test the conversion
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
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testOutCapableExchange ()
specifier|public
name|void
name|testOutCapableExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
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
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"inout"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// test the conversion
name|ExecResult
name|result
init|=
name|exchange
operator|.
name|getOut
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
block|}
block|}
end_class

end_unit

