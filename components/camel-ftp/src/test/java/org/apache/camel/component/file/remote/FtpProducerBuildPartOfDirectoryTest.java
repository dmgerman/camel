begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
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
operator|.
name|remote
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
name|component
operator|.
name|file
operator|.
name|FileComponent
import|;
end_import

begin_comment
comment|/**  * Unit test to verify that Camel can build remote directory on FTP server if missing (full or part of).  */
end_comment

begin_class
DECL|class|FtpProducerBuildPartOfDirectoryTest
specifier|public
class|class
name|FtpProducerBuildPartOfDirectoryTest
extends|extends
name|FtpServerTestSupport
block|{
DECL|field|port
specifier|private
name|String
name|port
init|=
literal|"20021"
decl_stmt|;
DECL|field|ftpUrl
specifier|private
name|String
name|ftpUrl
init|=
literal|"ftp://admin@localhost:"
operator|+
name|port
operator|+
literal|"/upload/user/claus?binary=false&password=admin"
decl_stmt|;
DECL|method|getPort ()
specifier|public
name|String
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|testProduceAndBuildPartOfRemotFolderTest ()
specifier|public
name|void
name|testProduceAndBuildPartOfRemotFolderTest
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"./res/home/"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"./res/home/upload/user/superman"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|ftpUrl
argument_list|,
literal|"Bye World"
argument_list|,
name|FileComponent
operator|.
name|HEADER_FILE_NAME
argument_list|,
literal|"claus.txt"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"./res/home/upload/user/claus/claus.txt"
argument_list|)
decl_stmt|;
name|file
operator|=
name|file
operator|.
name|getAbsoluteFile
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The uploaded file should exists"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|IOConverter
operator|.
name|toString
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createDirectory (String s)
specifier|private
specifier|static
name|void
name|createDirectory
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|file
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

