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
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
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
specifier|final
class|class
name|FtpUtils
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|FtpUtils
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|FtpUtils ()
specifier|private
name|FtpUtils
parameter_list|()
block|{     }
DECL|method|connect (FTPClient client, RemoteFileConfiguration config)
specifier|public
specifier|static
name|boolean
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
if|if
condition|(
name|config
operator|.
name|getFtpClientConfig
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Configuring FTPClient with config: "
operator|+
name|config
operator|.
name|getFtpClientConfig
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|configure
argument_list|(
name|config
operator|.
name|getFtpClientConfig
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Connecting to "
operator|+
name|config
argument_list|)
expr_stmt|;
name|client
operator|.
name|connect
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
expr_stmt|;
comment|// must enter passive mode directly after connect
if|if
condition|(
name|config
operator|.
name|isPassiveMode
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using passive mode connections"
argument_list|)
expr_stmt|;
name|client
operator|.
name|enterLocalPassiveMode
argument_list|()
expr_stmt|;
block|}
name|boolean
name|login
decl_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Attempting to login user: "
operator|+
name|username
argument_list|)
expr_stmt|;
name|login
operator|=
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"Attempting to login anonymous"
argument_list|)
expr_stmt|;
name|login
operator|=
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"User "
operator|+
operator|(
name|username
operator|!=
literal|null
condition|?
name|username
else|:
literal|"anonymous"
operator|)
operator|+
literal|" logged in: "
operator|+
name|login
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|login
condition|)
block|{
return|return
literal|false
return|;
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
return|return
literal|true
return|;
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
DECL|method|buildDirectory (FTPClient ftpClient, String dirName)
specifier|public
specifier|static
name|boolean
name|buildDirectory
parameter_list|(
name|FTPClient
name|ftpClient
parameter_list|,
name|String
name|dirName
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|originalDirectory
init|=
name|ftpClient
operator|.
name|printWorkingDirectory
argument_list|()
decl_stmt|;
name|boolean
name|success
init|=
literal|false
decl_stmt|;
try|try
block|{
comment|// maybe the full directory already exsits
name|success
operator|=
name|ftpClient
operator|.
name|changeWorkingDirectory
argument_list|(
name|dirName
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|success
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Trying to build remote directory: "
operator|+
name|dirName
argument_list|)
expr_stmt|;
block|}
name|success
operator|=
name|ftpClient
operator|.
name|makeDirectory
argument_list|(
name|dirName
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|success
condition|)
block|{
comment|// we are here if the server side doesn't create intermediate folders
comment|// so create the folder one by one
name|buildDirectoryChunks
argument_list|(
name|ftpClient
argument_list|,
name|dirName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
comment|// change back to original directory
name|ftpClient
operator|.
name|changeWorkingDirectory
argument_list|(
name|originalDirectory
argument_list|)
expr_stmt|;
block|}
return|return
name|success
return|;
block|}
DECL|method|buildDirectoryChunks (FTPClient ftpClient, String dirName)
specifier|public
specifier|static
name|boolean
name|buildDirectoryChunks
parameter_list|(
name|FTPClient
name|ftpClient
parameter_list|,
name|String
name|dirName
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|dirName
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|String
index|[]
name|dirs
init|=
name|dirName
operator|.
name|split
argument_list|(
literal|"\\/"
argument_list|)
decl_stmt|;
name|boolean
name|success
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|dir
range|:
name|dirs
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|dir
argument_list|)
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
expr_stmt|;
name|String
name|directory
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Trying to build remote directory: "
operator|+
name|directory
argument_list|)
expr_stmt|;
block|}
name|success
operator|=
name|ftpClient
operator|.
name|makeDirectory
argument_list|(
name|directory
argument_list|)
expr_stmt|;
block|}
return|return
name|success
return|;
block|}
block|}
end_class

end_unit

