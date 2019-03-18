begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_class
DECL|class|FtpProducerAllowNullBodyTest
specifier|public
class|class
name|FtpProducerAllowNullBodyTest
extends|extends
name|FtpServerTestSupport
block|{
DECL|method|getFtpUrl ()
specifier|private
name|String
name|getFtpUrl
parameter_list|()
block|{
return|return
literal|"ftp://admin@localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/allownull?password=admin&fileName=allowNullBody.txt"
return|;
block|}
annotation|@
name|Test
DECL|method|testAllowNullBodyTrue ()
specifier|public
name|void
name|testAllowNullBodyTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&allowNullBody=true"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertFileExists
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/allownull/allowNullBody.txt"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAllowNullBodyFalse ()
specifier|public
name|void
name|testAllowNullBodyFalse
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|getFtpUrl
argument_list|()
operator|+
literal|"&allowNullBody=false"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a GenericFileOperationFailedException"
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
name|endsWith
argument_list|(
literal|"allowNullBody.txt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertFalse
argument_list|(
literal|"allowNullBody set to false with null body should not create a new file"
argument_list|,
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/allownull/allowNullBody.txt"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

