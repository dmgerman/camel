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
name|FileOutputStream
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
name|junit
operator|.
name|jupiter
operator|.
name|api
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
name|test
operator|.
name|junit5
operator|.
name|TestSupport
operator|.
name|createDirectory
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|FtpChangedZeroLengthReadLockTest
specifier|public
class|class
name|FtpChangedZeroLengthReadLockTest
extends|extends
name|FtpServerTestSupport
block|{
DECL|method|getFtpUrl ()
specifier|protected
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
literal|"/changed?password=admin&readLock=changed&readLockCheckInterval=1000&readLockMinLength=0&delete=true"
return|;
block|}
annotation|@
name|Test
DECL|method|testChangedReadLock ()
specifier|public
name|void
name|testChangedReadLock
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/changed/out/zerofile.dat"
argument_list|)
expr_stmt|;
name|writeZeroFile
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|writeZeroFile ()
specifier|private
name|void
name|writeZeroFile
parameter_list|()
throws|throws
name|Exception
block|{
name|createDirectory
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/changed"
argument_list|)
expr_stmt|;
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/changed/zerofile.dat"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|fos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|fos
operator|.
name|close
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
throws|throws
name|Exception
block|{
return|return
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
name|getFtpUrl
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:target/changed/out"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

