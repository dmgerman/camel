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
name|IOException
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
name|net
operator|.
name|ftp
operator|.
name|FTPClient
import|;
end_import

begin_comment
comment|/**  * Utility methods for FTP.  */
end_comment

begin_class
DECL|class|FtpUtils
specifier|public
class|class
name|FtpUtils
block|{
DECL|method|FtpUtils ()
specifier|private
name|FtpUtils
parameter_list|()
block|{     }
DECL|method|connect (FTPClient client, RemoteFileConfiguration config)
specifier|public
specifier|static
name|void
name|connect
parameter_list|(
name|FTPClient
name|client
parameter_list|,
name|RemoteFileConfiguration
name|config
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|host
init|=
name|config
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|int
name|port
init|=
name|config
operator|.
name|getPort
argument_list|()
decl_stmt|;
name|String
name|username
init|=
name|config
operator|.
name|getUsername
argument_list|()
decl_stmt|;
name|client
operator|.
name|connect
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
expr_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|login
argument_list|(
name|username
argument_list|,
name|config
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|client
operator|.
name|login
argument_list|(
literal|"anonymous"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|client
operator|.
name|setFileType
argument_list|(
name|config
operator|.
name|isBinary
argument_list|()
condition|?
name|FTPClient
operator|.
name|BINARY_FILE_TYPE
else|:
name|FTPClient
operator|.
name|ASCII_FILE_TYPE
argument_list|)
expr_stmt|;
block|}
DECL|method|disconnect (FTPClient client)
specifier|public
specifier|static
name|void
name|disconnect
parameter_list|(
name|FTPClient
name|client
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|client
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|client
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createNewFtpClient ()
specifier|public
specifier|static
name|FTPClient
name|createNewFtpClient
parameter_list|()
block|{
return|return
operator|new
name|FTPClient
argument_list|()
return|;
block|}
block|}
end_class

end_unit

