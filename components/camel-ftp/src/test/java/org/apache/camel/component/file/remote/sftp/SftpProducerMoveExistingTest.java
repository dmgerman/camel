begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote.sftp
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
operator|.
name|sftp
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
name|CamelExecutionException
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
name|component
operator|.
name|file
operator|.
name|GenericFileOperationFailedException
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
comment|/**  *  */
end_comment

begin_class
DECL|class|SftpProducerMoveExistingTest
specifier|public
class|class
name|SftpProducerMoveExistingTest
extends|extends
name|SftpServerTestSupport
block|{
DECL|method|getFtpUrl ()
specifier|private
name|String
name|getFtpUrl
parameter_list|()
block|{
return|return
literal|"sftp://admin@localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/"
operator|+
name|FTP_ROOT_DIR
operator|+
literal|"/move?password=admin&fileExist=Move"
return|;
block|}
annotation|@
name|Test
DECL|method|testExistingFileDoesNotExists ()
specifier|public
name|void
name|testExistingFileDoesNotExists
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&moveExisting=${file:parent}/renamed-${file:onlyname}"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/hello.txt"
argument_list|)
expr_stmt|;
name|assertFileNotExists
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/renamed-hello.txt"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExistingFileExists ()
specifier|public
name|void
name|testExistingFileExists
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&moveExisting=${file:parent}/renamed-${file:onlyname}"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&moveExisting=${file:parent}/renamed-${file:onlyname}"
argument_list|,
literal|"Bye World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/hello.txt"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/hello.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/renamed-hello.txt"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/renamed-hello.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExistingFileExistsMoveSubDir ()
specifier|public
name|void
name|testExistingFileExistsMoveSubDir
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&moveExisting=backup"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&moveExisting=backup"
argument_list|,
literal|"Bye World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/hello.txt"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/hello.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// would move into sub directory and keep existing name as is
name|assertFileExists
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/backup/hello.txt"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/backup/hello.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFailOnMoveExistingFileExistsEagerDeleteTrue ()
specifier|public
name|void
name|testFailOnMoveExistingFileExistsEagerDeleteTrue
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&moveExisting=${file:parent}/renamed-${file:onlyname}&eagerDeleteTargetFile=true"
argument_list|,
literal|"Old file"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"renamed-hello.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&moveExisting=${file:parent}/renamed-${file:onlyname}&eagerDeleteTargetFile=true"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
comment|// we should be okay as we will just delete any existing file
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&moveExisting=${file:parent}/renamed-${file:onlyname}&eagerDeleteTargetFile=true"
argument_list|,
literal|"Bye World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
comment|// we could write the new file so the old context should be there
name|assertFileExists
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/hello.txt"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/hello.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// and the renamed file should be overridden
name|assertFileExists
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/renamed-hello.txt"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/renamed-hello.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFailOnMoveExistingFileExistsEagerDeleteFalse ()
specifier|public
name|void
name|testFailOnMoveExistingFileExistsEagerDeleteFalse
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&moveExisting=${file:parent}/renamed-${file:onlyname}&eagerDeleteTargetFile=true"
argument_list|,
literal|"Old file"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"renamed-hello.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&moveExisting=${file:parent}/renamed-${file:onlyname}&eagerDeleteTargetFile=false"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&moveExisting=${file:parent}/renamed-${file:onlyname}&eagerDeleteTargetFile=false"
argument_list|,
literal|"Bye World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|GenericFileOperationFailedException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|GenericFileOperationFailedException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|cause
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Cannot move existing file"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// we could not write the new file so the previous context should be there
name|assertFileExists
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/hello.txt"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/hello.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// and the renamed file should be untouched
name|assertFileExists
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/renamed-hello.txt"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Old file"
argument_list|,
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/move/renamed-hello.txt"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

