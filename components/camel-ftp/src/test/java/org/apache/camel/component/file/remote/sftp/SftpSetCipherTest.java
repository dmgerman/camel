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
name|remote
operator|.
name|SftpEndpoint
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
DECL|class|SftpSetCipherTest
specifier|public
class|class
name|SftpSetCipherTest
extends|extends
name|SftpServerTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testSftpSetCipherName ()
specifier|public
name|void
name|testSftpSetCipherName
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
name|String
name|cipher
init|=
literal|"blowfish-cbc"
decl_stmt|;
name|String
name|uri
init|=
literal|"sftp://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/"
operator|+
name|FTP_ROOT_DIR
operator|+
literal|"?username=admin&password=admin&ciphers="
operator|+
name|cipher
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|uri
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
comment|// test setting the cipher doesn't interfere with message payload
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|"/hello.txt"
argument_list|)
decl_stmt|;
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
name|file
argument_list|)
argument_list|)
expr_stmt|;
comment|// did we actually set the correct cipher?
name|SftpEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|,
name|SftpEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|cipher
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getCiphers
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

