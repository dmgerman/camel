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
name|Endpoint
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
name|Producer
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

begin_class
DECL|class|FromFtpConsumerTemplateRollbackTest
specifier|public
class|class
name|FromFtpConsumerTemplateRollbackTest
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
literal|"/deletefile?password=admin&binary=false&delete=true"
return|;
block|}
DECL|method|getFtpUrlInvalid ()
specifier|protected
name|String
name|getFtpUrlInvalid
parameter_list|()
block|{
comment|// use invalid starting directory and do not allow creating it so we force the poll to fail
return|return
literal|"ftp://admin@localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/unknown?password=admin&binary=false&delete=true&autoCreate=false"
return|;
block|}
annotation|@
name|Override
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|prepareFtpServer
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumerTemplateRollback ()
specifier|public
name|void
name|testConsumerTemplateRollback
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|consumer
operator|.
name|receiveBody
argument_list|(
name|getFtpUrlInvalid
argument_list|()
argument_list|,
literal|2000
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should fail and rollback"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|GenericFileOperationFailedException
name|ge
init|=
name|assertIsInstanceOf
argument_list|(
name|GenericFileOperationFailedException
operator|.
name|class
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|550
argument_list|,
name|ge
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|prepareFtpServer ()
specifier|private
name|void
name|prepareFtpServer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prepares the FTP Server by creating a file on the server that we want to unit
comment|// test that we can pool and store as a local file
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World this file will be deleted"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// assert file is created
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/deletefile/hello.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The file should exists"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

