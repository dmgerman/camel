begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
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
name|ContextTestSupport
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|MoveFilesToDirectoryTest
specifier|public
class|class
name|MoveFilesToDirectoryTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|testDirectory
specifier|protected
name|String
name|testDirectory
init|=
literal|"target/test/MoveFilesToDirectoryTest"
decl_stmt|;
DECL|field|inputDirectory
specifier|protected
name|String
name|inputDirectory
init|=
name|testDirectory
operator|+
literal|"/input"
decl_stmt|;
DECL|field|outputDirectory
specifier|protected
name|String
name|outputDirectory
init|=
name|testDirectory
operator|+
literal|"/output"
decl_stmt|;
DECL|field|fileName
specifier|protected
name|String
name|fileName
init|=
literal|"foo.txt"
decl_stmt|;
DECL|field|expectedBody
specifier|protected
name|Object
name|expectedBody
init|=
literal|"Hello there!"
decl_stmt|;
DECL|field|noop
specifier|protected
name|boolean
name|noop
decl_stmt|;
DECL|method|testFileRoute ()
specifier|public
name|void
name|testFileRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:"
operator|+
name|inputDirectory
argument_list|,
name|expectedBody
argument_list|,
name|FileComponent
operator|.
name|HEADER_FILE_NAME
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|result
operator|.
name|setResultWaitTime
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
comment|// now lets wait a bit and move that file
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// lets delete the output directory
name|deleteDirectory
argument_list|(
name|outputDirectory
argument_list|)
expr_stmt|;
comment|// now lets wait a bit for it to be polled
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|inputDirectory
operator|+
literal|"/"
operator|+
name|fileName
argument_list|)
decl_stmt|;
name|File
name|outDir
init|=
operator|new
name|File
argument_list|(
name|outputDirectory
argument_list|)
decl_stmt|;
name|outDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|File
name|newFile
init|=
operator|new
name|File
argument_list|(
name|outDir
argument_list|,
name|fileName
argument_list|)
decl_stmt|;
name|assertFileExists
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|assertFileNotExists
argument_list|(
name|newFile
argument_list|)
expr_stmt|;
name|boolean
name|answer
init|=
name|file
operator|.
name|renameTo
argument_list|(
name|newFile
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Move of file: "
operator|+
name|file
operator|+
literal|" to "
operator|+
name|newFile
operator|+
literal|" should have worked!"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
name|assertFileNotExists
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
name|newFile
argument_list|)
expr_stmt|;
comment|// now lets wait for multiple polls to check we only process it once
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|assertFileNotExists (File file)
specifier|protected
name|void
name|assertFileNotExists
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|assertFalse
argument_list|(
literal|"File should not exist: "
operator|+
name|file
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertFileExists (File file)
specifier|protected
name|void
name|assertFileExists
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"File should exist: "
operator|+
name|file
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
name|testDirectory
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
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
name|getOutputEndpointUri
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|getOutputEndpointUri ()
specifier|protected
name|String
name|getOutputEndpointUri
parameter_list|()
block|{
return|return
literal|"file:"
operator|+
name|outputDirectory
return|;
block|}
block|}
end_class

end_unit

