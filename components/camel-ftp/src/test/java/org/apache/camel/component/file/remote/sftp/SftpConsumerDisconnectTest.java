begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Ignore
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
annotation|@
name|Ignore
DECL|class|SftpConsumerDisconnectTest
specifier|public
class|class
name|SftpConsumerDisconnectTest
extends|extends
name|SftpServerTestSupport
block|{
DECL|field|SAMPLE_FILE_NAME_1
specifier|private
specifier|static
specifier|final
name|String
name|SAMPLE_FILE_NAME_1
init|=
name|String
operator|.
name|format
argument_list|(
literal|"sample-1-%s.txt"
argument_list|,
name|SftpConsumerDisconnectTest
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|SAMPLE_FILE_NAME_2
specifier|private
specifier|static
specifier|final
name|String
name|SAMPLE_FILE_NAME_2
init|=
name|String
operator|.
name|format
argument_list|(
literal|"sample-2-%s.txt"
argument_list|,
name|SftpConsumerDisconnectTest
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|SAMPLE_FILE_CHARSET
specifier|private
specifier|static
specifier|final
name|String
name|SAMPLE_FILE_CHARSET
init|=
literal|"iso-8859-1"
decl_stmt|;
DECL|field|SAMPLE_FILE_PAYLOAD
specifier|private
specifier|static
specifier|final
name|String
name|SAMPLE_FILE_PAYLOAD
init|=
literal|"abc"
decl_stmt|;
annotation|@
name|Test
DECL|method|testConsumeDelete ()
specifier|public
name|void
name|testConsumeDelete
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
comment|// prepare sample file to be consumed by SFTP consumer
name|createSampleFile
argument_list|(
name|SAMPLE_FILE_NAME_1
argument_list|)
expr_stmt|;
comment|// Prepare expectations
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
name|expectedBodiesReceived
argument_list|(
name|SAMPLE_FILE_PAYLOAD
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
comment|// Check that expectations are satisfied
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|250
argument_list|)
expr_stmt|;
comment|// File is deleted
name|File
name|deletedFile
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/"
operator|+
name|SAMPLE_FILE_NAME_1
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"File should have been deleted: "
operator|+
name|deletedFile
argument_list|,
name|deletedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumeMove ()
specifier|public
name|void
name|testConsumeMove
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
comment|// moved file after its processed
name|String
name|movedFile
init|=
name|FTP_ROOT_DIR
operator|+
literal|"/.camel/"
operator|+
name|SAMPLE_FILE_NAME_2
decl_stmt|;
comment|// prepare sample file to be consumed by SFTP consumer
name|createSampleFile
argument_list|(
name|SAMPLE_FILE_NAME_2
argument_list|)
expr_stmt|;
comment|// Prepare expectations
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
name|expectedBodiesReceived
argument_list|(
name|SAMPLE_FILE_PAYLOAD
argument_list|)
expr_stmt|;
comment|// use mock to assert that the file will be moved there eventually
name|mock
operator|.
name|expectedFileExists
argument_list|(
name|movedFile
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
comment|// Check that expectations are satisfied
name|assertMockEndpointsSatisfied
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
literal|"sftp://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/"
operator|+
name|FTP_ROOT_DIR
operator|+
literal|"?username=admin&password=admin&delete=true"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
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
name|disconnectAllSessions
argument_list|()
expr_stmt|;
comment|// disconnect all Sessions from
comment|// the SFTP server
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"sftp://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/"
operator|+
name|FTP_ROOT_DIR
operator|+
literal|"?username=admin&password=admin&noop=false&move=.camel"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
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
name|disconnectAllSessions
argument_list|()
expr_stmt|;
comment|// disconnect all Sessions from the SFTP server
block|}
block|}
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
DECL|method|createSampleFile (String fileName)
specifier|private
name|void
name|createSampleFile
parameter_list|(
name|String
name|fileName
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/"
operator|+
name|fileName
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|write
argument_list|(
name|file
argument_list|,
name|SAMPLE_FILE_PAYLOAD
argument_list|,
name|SAMPLE_FILE_CHARSET
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

