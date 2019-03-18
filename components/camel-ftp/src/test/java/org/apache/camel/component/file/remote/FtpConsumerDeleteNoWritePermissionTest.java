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
name|PollingConsumer
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

begin_comment
comment|/**  * User does not have write permissions so can't deleted consumed file.  */
end_comment

begin_class
DECL|class|FtpConsumerDeleteNoWritePermissionTest
specifier|public
class|class
name|FtpConsumerDeleteNoWritePermissionTest
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
literal|"ftp://dummy@localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/deletenoperm?password=foo"
operator|+
literal|"&delete=true&consumer.delay=5000"
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
DECL|method|testConsumerDeleteNoWritePermission ()
specifier|public
name|void
name|testConsumerDeleteNoWritePermission
parameter_list|()
throws|throws
name|Exception
block|{
name|PollingConsumer
name|consumer
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|)
operator|.
name|createPollingConsumer
argument_list|()
decl_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
name|Exchange
name|out
init|=
name|consumer
operator|.
name|receive
argument_list|(
literal|3000
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should get the file"
argument_list|,
name|out
argument_list|)
expr_stmt|;
try|try
block|{
comment|// give consumer time to try to delete the file
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GenericFileOperationFailedException
name|fofe
parameter_list|)
block|{
comment|// expected, ignore
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
comment|// prepares the FTP Server by creating files on the server that we want to unit
comment|// test that we can pool and store as a local file
name|String
name|ftpUrl
init|=
literal|"ftp://admin@localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/deletenoperm/?password=admin"
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|ftpUrl
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
block|}
block|}
end_class

end_unit

