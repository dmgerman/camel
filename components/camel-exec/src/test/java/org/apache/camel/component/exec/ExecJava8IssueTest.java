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
name|FileWriter
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
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|impl
operator|.
name|DefaultCamelContext
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
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_comment
comment|/**  * Test to duplicate issues with Camel's exec command in Java 8 on Unix  * This issue appears to be caused by a race condition, so this test does not always fail  */
end_comment

begin_class
DECL|class|ExecJava8IssueTest
specifier|public
class|class
name|ExecJava8IssueTest
extends|extends
name|Assert
block|{
DECL|field|tempDir
specifier|private
name|File
name|tempDir
decl_stmt|;
DECL|field|tempDirName
specifier|private
specifier|final
name|String
name|tempDirName
init|=
name|name
argument_list|()
decl_stmt|;
DECL|field|tempFileName
specifier|private
specifier|final
name|String
name|tempFileName
init|=
name|name
argument_list|()
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|tempDir
operator|=
operator|new
name|File
argument_list|(
literal|"target"
argument_list|,
name|tempDirName
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|tempDir
operator|.
name|mkdir
argument_list|()
operator|)
condition|)
block|{
name|fail
argument_list|(
literal|"Couldn't create temp dir for test"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|tempDir
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|OS
operator|.
name|isFamilyUnix
argument_list|()
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"The test 'CamelExecTest' does not support the following OS : "
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|tempFilePath
init|=
name|tempDir
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/"
operator|+
name|tempFileName
decl_stmt|;
specifier|final
name|File
name|script
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"script"
argument_list|,
literal|".sh"
argument_list|,
name|tempDir
argument_list|)
decl_stmt|;
name|writeScript
argument_list|(
name|script
argument_list|)
expr_stmt|;
specifier|final
name|String
name|exec
init|=
literal|"bash?args="
operator|+
name|script
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" "
operator|+
name|tempFilePath
operator|+
literal|"&outFile="
operator|+
name|tempFilePath
decl_stmt|;
name|DefaultCamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:source"
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:"
operator|+
name|tempDir
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"?fileName="
operator|+
name|tempFileName
argument_list|)
operator|.
name|to
argument_list|(
literal|"exec:"
operator|+
name|exec
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|String
name|output
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
name|assertEquals
argument_list|(
literal|"hello world\n"
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|ProducerTemplate
name|pt
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|String
name|payload
init|=
literal|"hello"
decl_stmt|;
name|pt
operator|.
name|sendBody
argument_list|(
literal|"direct:source"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a script which will append " world" to a file      */
DECL|method|writeScript (File script)
specifier|private
name|void
name|writeScript
parameter_list|(
name|File
name|script
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|FileWriter
name|fw
init|=
operator|new
name|FileWriter
argument_list|(
name|script
argument_list|)
init|;
name|PrintWriter
name|pw
operator|=
operator|new
name|PrintWriter
argument_list|(
name|fw
argument_list|)
init|;
init|)
block|{
name|String
name|s
init|=
literal|"echo \" world\">> $1"
decl_stmt|;
name|pw
operator|.
name|print
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns a random UUID      */
DECL|method|name ()
specifier|private
name|String
name|name
parameter_list|()
block|{
return|return
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

