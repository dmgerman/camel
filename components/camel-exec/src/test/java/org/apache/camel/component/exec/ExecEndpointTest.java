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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|Component
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
name|UnsafeUriCharactersEncoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|ExecEndpoint
operator|.
name|NO_TIMEOUT
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertSame
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
comment|/**  * Test the configuration of {@link ExecEndpoint}  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|ExecEndpointTest
specifier|public
class|class
name|ExecEndpointTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
annotation|@
name|Autowired
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Autowired
DECL|field|customBinding
specifier|private
name|ExecBinding
name|customBinding
decl_stmt|;
annotation|@
name|Autowired
DECL|field|customExecutor
specifier|private
name|ExecCommandExecutor
name|customExecutor
decl_stmt|;
DECL|field|component
specifier|private
name|Component
name|component
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|component
operator|=
name|camelContext
operator|.
name|getComponent
argument_list|(
literal|"exec"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testValidComponentDescriptor ()
specifier|public
name|void
name|testValidComponentDescriptor
parameter_list|()
block|{
name|assertNotNull
argument_list|(
literal|"The Camel Exec component can not be resolved"
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testCreateEndpointDefaultConf ()
specifier|public
name|void
name|testCreateEndpointDefaultConf
parameter_list|()
throws|throws
name|Exception
block|{
name|ExecEndpoint
name|e
init|=
name|createExecEndpoint
argument_list|(
literal|"exec:test"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The Camel Exec component must create instances of "
operator|+
name|ExecEndpoint
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|e
operator|instanceof
name|ExecEndpoint
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|e
operator|.
name|getArgs
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|e
operator|.
name|getWorkingDir
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|e
operator|.
name|getOutFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|NO_TIMEOUT
argument_list|,
name|e
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|e
operator|.
name|getExecutable
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|e
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testCreateEndpointDefaultNoTimeout ()
specifier|public
name|void
name|testCreateEndpointDefaultNoTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|ExecEndpoint
name|e
init|=
name|createExecEndpoint
argument_list|(
literal|"exec:test"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ExecEndpoint
operator|.
name|NO_TIMEOUT
argument_list|,
name|e
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
DECL|method|testCreateEndpointCustomBinding ()
specifier|public
name|void
name|testCreateEndpointCustomBinding
parameter_list|()
throws|throws
name|Exception
block|{
name|ExecEndpoint
name|e
init|=
name|createExecEndpoint
argument_list|(
literal|"exec:test?binding=#customBinding"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"Expected is the custom customBinding reference from the application context"
argument_list|,
name|customBinding
argument_list|,
name|e
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testCreateEndpointCustomCommandExecutor ()
specifier|public
name|void
name|testCreateEndpointCustomCommandExecutor
parameter_list|()
throws|throws
name|Exception
block|{
name|ExecEndpoint
name|e
init|=
name|createExecEndpoint
argument_list|(
literal|"exec:test?commandExecutor=#customExecutor"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
literal|"Expected is the custom customExecutor reference from the application context"
argument_list|,
name|customExecutor
argument_list|,
name|e
operator|.
name|getCommandExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testCreateEndpointWithArgs ()
specifier|public
name|void
name|testCreateEndpointWithArgs
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|args
init|=
literal|"arg1 arg2 arg3"
decl_stmt|;
comment|// Need to properly encode the URI
name|ExecEndpoint
name|e
init|=
name|createExecEndpoint
argument_list|(
literal|"exec:test?args="
operator|+
name|args
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|"+"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|args
argument_list|,
name|e
operator|.
name|getArgs
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testCreateEndpointWithArgs2 ()
specifier|public
name|void
name|testCreateEndpointWithArgs2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|args
init|=
literal|"arg1 \"arg2 \" arg3"
decl_stmt|;
name|ExecEndpoint
name|e
init|=
name|createExecEndpoint
argument_list|(
literal|"exec:test?args="
operator|+
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|args
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|args
argument_list|,
name|e
operator|.
name|getArgs
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testCreateEndpointWithArgs3 ()
specifier|public
name|void
name|testCreateEndpointWithArgs3
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|args
init|=
literal|"RAW(arg1+arg2 arg3)"
decl_stmt|;
comment|// Just avoid URI encoding by using the RAW()
name|ExecEndpoint
name|e
init|=
name|createExecEndpoint
argument_list|(
literal|"exec:test?args="
operator|+
name|args
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"arg1+arg2 arg3"
argument_list|,
name|e
operator|.
name|getArgs
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testCreateEndpointWithTimeout ()
specifier|public
name|void
name|testCreateEndpointWithTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|timeout
init|=
literal|1999999L
decl_stmt|;
name|ExecEndpoint
name|e
init|=
name|createExecEndpoint
argument_list|(
literal|"exec:test?timeout="
operator|+
name|timeout
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|timeout
argument_list|,
name|e
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
DECL|method|testCreateEndpointWithOutFile ()
specifier|public
name|void
name|testCreateEndpointWithOutFile
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|outFile
init|=
literal|"output.txt"
decl_stmt|;
name|ExecEndpoint
name|e
init|=
name|createExecEndpoint
argument_list|(
literal|"exec:test?outFile="
operator|+
name|outFile
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|outFile
argument_list|,
name|e
operator|.
name|getOutFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testCreateEndpointWithWorkingDir ()
specifier|public
name|void
name|testCreateEndpointWithWorkingDir
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|workingDir
init|=
literal|"/workingdir"
decl_stmt|;
name|ExecEndpoint
name|e
init|=
name|createExecEndpoint
argument_list|(
literal|"exec:test?workingDir="
operator|+
name|workingDir
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|workingDir
argument_list|,
name|e
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
DECL|method|testCreateEndpointEscapedWorkingDir ()
specifier|public
name|void
name|testCreateEndpointEscapedWorkingDir
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|cmd
init|=
literal|"temp.exe"
decl_stmt|;
name|String
name|dir
init|=
literal|"\"c:/program files/wokr/temp\""
decl_stmt|;
name|String
name|uri
init|=
literal|"exec:"
operator|+
name|cmd
operator|+
literal|"?workingDir="
operator|+
name|dir
decl_stmt|;
name|ExecEndpoint
name|endpoint
init|=
name|createExecEndpoint
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|uri
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|cmd
argument_list|,
name|endpoint
operator|.
name|getExecutable
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getArgs
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dir
argument_list|,
name|endpoint
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
DECL|method|testCreateEndpointEscapedCommand ()
specifier|public
name|void
name|testCreateEndpointEscapedCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|executable
init|=
literal|"C:/Program Files/test/text.exe"
decl_stmt|;
name|String
name|uri
init|=
literal|"exec:"
operator|+
name|executable
decl_stmt|;
name|ExecEndpoint
name|endpoint
init|=
name|createExecEndpoint
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|uri
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getArgs
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getWorkingDir
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|executable
argument_list|,
name|endpoint
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
DECL|method|testCreateEndpointComposite ()
specifier|public
name|void
name|testCreateEndpointComposite
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|workingDir
init|=
literal|"/workingdir"
decl_stmt|;
name|String
name|argsEscaped
init|=
literal|"arg1 arg2 \"arg 3\""
decl_stmt|;
name|long
name|timeout
init|=
literal|10000L
decl_stmt|;
name|String
name|uri
init|=
literal|"exec:executable.exe?workingDir="
operator|+
name|workingDir
operator|+
literal|"&timeout="
operator|+
name|timeout
operator|+
literal|"&args="
operator|+
name|argsEscaped
decl_stmt|;
name|ExecEndpoint
name|e
init|=
name|createExecEndpoint
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|uri
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|workingDir
argument_list|,
name|e
operator|.
name|getWorkingDir
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|argsEscaped
argument_list|,
name|e
operator|.
name|getArgs
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|timeout
argument_list|,
name|e
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
DECL|method|testCreateEndpointComposite2 ()
specifier|public
name|void
name|testCreateEndpointComposite2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|workingDir
init|=
literal|"/workingdir"
decl_stmt|;
name|String
name|outFile
init|=
literal|"target/outfile.xml"
decl_stmt|;
name|long
name|timeout
init|=
literal|10000
decl_stmt|;
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"exec:executable.exe"
argument_list|)
operator|.
name|append
argument_list|(
literal|"?workingDir="
operator|+
name|workingDir
argument_list|)
operator|.
name|append
argument_list|(
literal|"&timeout="
operator|+
name|timeout
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"&outFile="
operator|+
name|outFile
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"&commandExecutor=#customExecutor&binding=#customBinding"
argument_list|)
expr_stmt|;
name|ExecEndpoint
name|e
init|=
name|createExecEndpoint
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|encode
argument_list|(
name|builder
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|workingDir
argument_list|,
name|e
operator|.
name|getWorkingDir
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|timeout
argument_list|,
name|e
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|outFile
argument_list|,
name|e
operator|.
name|getOutFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|customBinding
argument_list|,
name|e
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|customExecutor
argument_list|,
name|e
operator|.
name|getCommandExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createExecEndpoint (String uri)
specifier|private
name|ExecEndpoint
name|createExecEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Using Exec endpoint URI "
operator|+
name|uri
argument_list|)
expr_stmt|;
return|return
operator|(
name|ExecEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|)
return|;
block|}
block|}
end_class

end_unit

